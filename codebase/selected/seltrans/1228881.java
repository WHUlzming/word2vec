package   org  .  jscsi  .  initiator  .  devices  ; 

import   java  .  util  .  Vector  ; 
import   java  .  util  .  concurrent  .  CyclicBarrier  ; 
import   java  .  util  .  concurrent  .  ExecutorService  ; 
import   java  .  util  .  concurrent  .  Executors  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 









public   class   Raid1Device   implements   Device  { 

private   final   Device  [  ]  devices  ; 

private   int   blockSize  =  -  1  ; 

private   long   blockCount  =  -  1  ; 


private   static   final   Log   LOGGER  =  LogFactory  .  getLog  (  Raid1Device  .  class  )  ; 





private   int   nextTarget  ; 


private   final   ExecutorService   executor  ; 


private   CyclicBarrier   barrier  ; 










public   Raid1Device  (  final   Device  [  ]  initDevices  )  throws   Exception  { 
devices  =  initDevices  ; 
nextTarget  =  0  ; 
executor  =  Executors  .  newFixedThreadPool  (  devices  .  length  )  ; 
} 


public   void   close  (  )  throws   Exception  { 
if  (  blockCount  ==  -  1  )  { 
throw   new   NullPointerException  (  )  ; 
} 
executor  .  shutdown  (  )  ; 
for  (  Device   device  :  devices  )  { 
device  .  close  (  )  ; 
} 
blockSize  =  -  1  ; 
blockCount  =  -  1  ; 
LOGGER  .  info  (  "Closed "  +  getName  (  )  +  "."  )  ; 
} 


public   int   getBlockSize  (  )  { 
if  (  blockSize  ==  -  1  )  { 
throw   new   IllegalStateException  (  "You first have to open the Device!"  )  ; 
} 
return   blockSize  ; 
} 


public   String   getName  (  )  { 
String   name  =  "Raid1Device("  ; 
for  (  Device   device  :  devices  )  { 
name  +=  device  .  getName  (  )  +  "+"  ; 
} 
return   name  .  substring  (  0  ,  name  .  length  (  )  -  1  )  +  ")"  ; 
} 


public   long   getBlockCount  (  )  { 
if  (  blockCount  ==  -  1  )  { 
throw   new   IllegalStateException  (  "You first have to open the Device!"  )  ; 
} 
return   blockCount  ; 
} 


public   void   open  (  )  throws   Exception  { 
if  (  blockCount  !=  -  1  )  { 
throw   new   IllegalStateException  (  "Raid1Device is already opened!"  )  ; 
} 
for  (  Device   device  :  devices  )  { 
device  .  open  (  )  ; 
} 
blockSize  =  0  ; 
for  (  Device   device  :  devices  )  { 
if  (  blockSize  ==  0  )  { 
blockSize  =  (  int  )  device  .  getBlockSize  (  )  ; 
}  else   if  (  blockSize  !=  (  int  )  device  .  getBlockSize  (  )  )  { 
throw   new   IllegalArgumentException  (  "All devices must have the same block size!"  )  ; 
} 
} 
blockCount  =  Long  .  MAX_VALUE  ; 
for  (  Device   device  :  devices  )  { 
blockCount  =  Math  .  min  (  blockCount  ,  device  .  getBlockCount  (  )  )  ; 
} 
} 


public   void   read  (  final   long   address  ,  final   byte  [  ]  data  )  throws   Exception  { 
if  (  blockCount  ==  -  1  )  { 
throw   new   IllegalStateException  (  "You first have to open the Device!"  )  ; 
} 
int   blocks  =  data  .  length  /  blockSize  ; 
if  (  address  <  0  ||  address  +  blocks  >  blockCount  )  { 
long   adr  =  address  <  0  ?  address  :  address  +  blocks  -  1  ; 
throw   new   IllegalArgumentException  (  "Address "  +  adr  +  " out of range."  )  ; 
} 
if  (  data  .  length  %  blockSize  !=  0  )  { 
throw   new   IllegalArgumentException  (  "Number of bytes is not a multiple of the blocksize!"  )  ; 
} 
int   parts  =  (  blocks  >=  devices  .  length  )  ?  devices  .  length  :  (  int  )  blocks  ; 
barrier  =  new   CyclicBarrier  (  parts  +  1  )  ; 
int   targetBlockCount  ; 
Vector  <  byte  [  ]  >  targetData  =  new   Vector  <  byte  [  ]  >  (  )  ; 
int   targetBlockAddress  =  (  int  )  address  ; 
for  (  int   i  =  0  ;  i  <  parts  ;  i  ++  )  { 
targetBlockCount  =  blocks  /  devices  .  length  ; 
if  (  i  <  (  blocks  %  devices  .  length  )  )  { 
targetBlockCount  ++  ; 
} 
targetData  .  add  (  new   byte  [  targetBlockCount  *  blockSize  ]  )  ; 
if  (  targetBlockCount  !=  0  )  { 
executor  .  execute  (  new   ReadThread  (  devices  [  nextTarget  ]  ,  targetBlockAddress  ,  targetData  .  get  (  i  )  )  )  ; 
} 
targetBlockAddress  +=  targetBlockCount  ; 
nextTarget  =  (  nextTarget  <  devices  .  length  -  1  )  ?  nextTarget  +  1  :  0  ; 
} 
barrier  .  await  (  )  ; 
int   pos  =  0  ; 
for  (  int   i  =  0  ;  i  <  targetData  .  size  (  )  ;  i  ++  )  { 
System  .  arraycopy  (  targetData  .  get  (  i  )  ,  0  ,  data  ,  pos  ,  targetData  .  get  (  i  )  .  length  )  ; 
pos  +=  targetData  .  get  (  i  )  .  length  ; 
} 
} 


public   void   write  (  final   long   address  ,  final   byte  [  ]  data  )  throws   Exception  { 
if  (  blockCount  ==  -  1  )  { 
throw   new   IllegalStateException  (  "You first have to open the Device!"  )  ; 
} 
long   blocks  =  data  .  length  /  blockSize  ; 
if  (  address  <  0  ||  address  +  blocks  >  blockCount  )  { 
long   adr  =  address  <  0  ?  address  :  address  +  blocks  -  1  ; 
throw   new   IllegalArgumentException  (  "Address "  +  adr  +  " out of range."  )  ; 
} 
if  (  data  .  length  %  blockSize  !=  0  )  { 
throw   new   IllegalArgumentException  (  "Number of bytes is not a multiple of the blocksize!"  )  ; 
} 
barrier  =  new   CyclicBarrier  (  devices  .  length  +  1  )  ; 
for  (  int   i  =  0  ;  i  <  devices  .  length  ;  i  ++  )  { 
executor  .  execute  (  new   WriteThread  (  devices  [  i  ]  ,  (  int  )  address  ,  data  )  )  ; 
} 
barrier  .  await  (  )  ; 
} 







private   final   class   ReadThread   implements   Runnable  { 

private   final   Device   device  ; 

private   final   int   address  ; 

private   final   byte  [  ]  data  ; 

private   ReadThread  (  final   Device   readDevice  ,  final   int   readBlockAddress  ,  final   byte  [  ]  readData  )  { 
device  =  readDevice  ; 
address  =  readBlockAddress  ; 
data  =  readData  ; 
} 

public   void   run  (  )  { 
try  { 
device  .  read  (  address  ,  data  )  ; 
barrier  .  await  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 







private   final   class   WriteThread   implements   Runnable  { 

private   final   Device   device  ; 

private   final   int   address  ; 

private   final   byte  [  ]  data  ; 

private   WriteThread  (  final   Device   writeDevice  ,  final   int   writeBlockAddress  ,  final   byte  [  ]  writeData  )  { 
device  =  writeDevice  ; 
address  =  writeBlockAddress  ; 
data  =  writeData  ; 
} 

public   void   run  (  )  { 
try  { 
device  .  write  (  address  ,  data  )  ; 
barrier  .  await  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
} 

