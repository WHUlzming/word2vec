package   org  .  ibex  .  nestedvm  ; 

import   org  .  ibex  .  nestedvm  .  util  .  *  ; 
import   java  .  io  .  *  ; 

public   abstract   class   Runtime   implements   UsermodeConstants  ,  Registers  ,  Cloneable  { 

public   static   final   String   VERSION  =  "1.0"  ; 


static   final   boolean   STDERR_DIAG  =  true  ; 


protected   final   int   pageShift  ; 


private   final   int   stackBottom  ; 


protected   int  [  ]  [  ]  readPages  ; 



protected   int  [  ]  [  ]  writePages  ; 


private   int   heapEnd  ; 


private   static   final   int   STACK_GUARD_PAGES  =  4  ; 


protected   abstract   int   heapStart  (  )  ; 


protected   abstract   int   entryPoint  (  )  ; 


protected   int   userInfoBase  (  )  { 
return   0  ; 
} 

protected   int   userInfoSize  (  )  { 
return   0  ; 
} 


protected   abstract   int   gp  (  )  ; 


private   long   startTime  ; 


public   static   final   int   RUNNING  =  0  ; 


public   static   final   int   STOPPED  =  1  ; 


public   static   final   int   PAUSED  =  2  ; 


public   static   final   int   CALLJAVA  =  3  ; 


public   static   final   int   EXITED  =  4  ; 


public   static   final   int   EXECED  =  5  ; 


protected   int   state  =  STOPPED  ; 


public   final   int   getState  (  )  { 
return   state  ; 
} 



private   int   exitStatus  ; 

public   ExecutionException   exitException  ; 


FD  [  ]  fds  ; 

boolean   closeOnExec  [  ]  ; 


SecurityManager   sm  ; 

public   void   setSecurityManager  (  SecurityManager   sm  )  { 
this  .  sm  =  sm  ; 
} 


private   CallJavaCB   callJavaCB  ; 

public   void   setCallJavaCB  (  CallJavaCB   callJavaCB  )  { 
this  .  callJavaCB  =  callJavaCB  ; 
} 


private   byte  [  ]  _byteBuf  ; 



static   final   int   MAX_CHUNK  =  16  *  1024  *  1024  -  1024  ; 




protected   abstract   void   _execute  (  )  throws   ExecutionException  ; 



protected   int   lookupSymbol  (  String   symbol  )  { 
return  -  1  ; 
} 


protected   abstract   void   getCPUState  (  CPUState   state  )  ; 


protected   abstract   void   setCPUState  (  CPUState   state  )  ; 


static   final   boolean   win32Hacks  ; 

static  { 
String   os  =  Platform  .  getProperty  (  "os.name"  )  ; 
String   prop  =  Platform  .  getProperty  (  "nestedvm.win32hacks"  )  ; 
if  (  prop  !=  null  )  { 
win32Hacks  =  Boolean  .  valueOf  (  prop  )  .  booleanValue  (  )  ; 
}  else  { 
win32Hacks  =  os  !=  null  &&  os  .  toLowerCase  (  )  .  indexOf  (  "windows"  )  !=  -  1  ; 
} 
} 

protected   Object   clone  (  )  throws   CloneNotSupportedException  { 
Runtime   r  =  (  Runtime  )  super  .  clone  (  )  ; 
r  .  _byteBuf  =  null  ; 
r  .  startTime  =  0  ; 
r  .  fds  =  new   FD  [  OPEN_MAX  ]  ; 
for  (  int   i  =  0  ;  i  <  OPEN_MAX  ;  i  ++  )  if  (  fds  [  i  ]  !=  null  )  r  .  fds  [  i  ]  =  fds  [  i  ]  .  dup  (  )  ; 
int   totalPages  =  writePages  .  length  ; 
r  .  readPages  =  new   int  [  totalPages  ]  [  ]  ; 
r  .  writePages  =  new   int  [  totalPages  ]  [  ]  ; 
for  (  int   i  =  0  ;  i  <  totalPages  ;  i  ++  )  { 
if  (  readPages  [  i  ]  ==  null  )  continue  ; 
if  (  writePages  [  i  ]  ==  null  )  r  .  readPages  [  i  ]  =  readPages  [  i  ]  ;  else   r  .  readPages  [  i  ]  =  r  .  writePages  [  i  ]  =  (  int  [  ]  )  writePages  [  i  ]  .  clone  (  )  ; 
} 
return   r  ; 
} 

protected   Runtime  (  int   pageSize  ,  int   totalPages  )  { 
this  (  pageSize  ,  totalPages  ,  false  )  ; 
} 

protected   Runtime  (  int   pageSize  ,  int   totalPages  ,  boolean   exec  )  { 
if  (  pageSize  <=  0  )  throw   new   IllegalArgumentException  (  "pageSize <= 0"  )  ; 
if  (  totalPages  <=  0  )  throw   new   IllegalArgumentException  (  "totalPages <= 0"  )  ; 
if  (  (  pageSize  &  (  pageSize  -  1  )  )  !=  0  )  throw   new   IllegalArgumentException  (  "pageSize not a power of two"  )  ; 
int   _pageShift  =  0  ; 
while  (  pageSize  >  >  >  _pageShift  !=  1  )  _pageShift  ++  ; 
pageShift  =  _pageShift  ; 
int   heapStart  =  heapStart  (  )  ; 
int   totalMemory  =  totalPages  *  pageSize  ; 
int   stackSize  =  max  (  totalMemory  /  512  ,  ARG_MAX  +  65536  )  ; 
int   stackPages  =  0  ; 
if  (  totalPages  >  1  )  { 
stackSize  =  max  (  stackSize  ,  pageSize  )  ; 
stackSize  =  (  stackSize  +  pageSize  -  1  )  &  ~  (  pageSize  -  1  )  ; 
stackPages  =  stackSize  >  >  >  pageShift  ; 
heapStart  =  (  heapStart  +  pageSize  -  1  )  &  ~  (  pageSize  -  1  )  ; 
if  (  stackPages  +  STACK_GUARD_PAGES  +  (  heapStart  >  >  >  pageShift  )  >=  totalPages  )  throw   new   IllegalArgumentException  (  "total pages too small"  )  ; 
}  else  { 
if  (  pageSize  <  heapStart  +  stackSize  )  throw   new   IllegalArgumentException  (  "total memory too small"  )  ; 
heapStart  =  (  heapStart  +  4095  )  &  ~  4096  ; 
} 
stackBottom  =  totalMemory  -  stackSize  ; 
heapEnd  =  heapStart  ; 
readPages  =  new   int  [  totalPages  ]  [  ]  ; 
writePages  =  new   int  [  totalPages  ]  [  ]  ; 
if  (  totalPages  ==  1  )  { 
readPages  [  0  ]  =  writePages  [  0  ]  =  new   int  [  pageSize  >  >  2  ]  ; 
}  else  { 
for  (  int   i  =  (  stackBottom  >  >  >  pageShift  )  ;  i  <  writePages  .  length  ;  i  ++  )  { 
readPages  [  i  ]  =  writePages  [  i  ]  =  new   int  [  pageSize  >  >  2  ]  ; 
} 
} 
if  (  !  exec  )  { 
fds  =  new   FD  [  OPEN_MAX  ]  ; 
closeOnExec  =  new   boolean  [  OPEN_MAX  ]  ; 
InputStream   stdin  =  win32Hacks  ?  new   Win32ConsoleIS  (  System  .  in  )  :  System  .  in  ; 
addFD  (  new   TerminalFD  (  stdin  )  )  ; 
addFD  (  new   TerminalFD  (  System  .  out  )  )  ; 
addFD  (  new   TerminalFD  (  System  .  err  )  )  ; 
} 
} 



protected   final   void   initPages  (  int  [  ]  src  ,  int   addr  ,  boolean   ro  )  { 
int   pageWords  =  (  1  <<  pageShift  )  >  >  >  2  ; 
int   pageMask  =  (  1  <<  pageShift  )  -  1  ; 
for  (  int   i  =  0  ;  i  <  src  .  length  ;  )  { 
int   page  =  addr  >  >  >  pageShift  ; 
int   start  =  (  addr  &  pageMask  )  >  >  2  ; 
int   elements  =  min  (  pageWords  -  start  ,  src  .  length  -  i  )  ; 
if  (  readPages  [  page  ]  ==  null  )  { 
initPage  (  page  ,  ro  )  ; 
}  else   if  (  !  ro  )  { 
if  (  writePages  [  page  ]  ==  null  )  writePages  [  page  ]  =  readPages  [  page  ]  ; 
} 
System  .  arraycopy  (  src  ,  i  ,  readPages  [  page  ]  ,  start  ,  elements  )  ; 
i  +=  elements  ; 
addr  +=  elements  *  4  ; 
} 
} 


protected   final   void   clearPages  (  int   addr  ,  int   words  )  { 
int   pageWords  =  (  1  <<  pageShift  )  >  >  >  2  ; 
int   pageMask  =  (  1  <<  pageShift  )  -  1  ; 
for  (  int   i  =  0  ;  i  <  words  ;  )  { 
int   page  =  addr  >  >  >  pageShift  ; 
int   start  =  (  addr  &  pageMask  )  >  >  2  ; 
int   elements  =  min  (  pageWords  -  start  ,  words  -  i  )  ; 
if  (  readPages  [  page  ]  ==  null  )  { 
readPages  [  page  ]  =  writePages  [  page  ]  =  new   int  [  pageWords  ]  ; 
}  else  { 
if  (  writePages  [  page  ]  ==  null  )  writePages  [  page  ]  =  readPages  [  page  ]  ; 
for  (  int   j  =  start  ;  j  <  start  +  elements  ;  j  ++  )  writePages  [  page  ]  [  j  ]  =  0  ; 
} 
i  +=  elements  ; 
addr  +=  elements  *  4  ; 
} 
} 



public   final   void   copyin  (  int   addr  ,  byte  [  ]  buf  ,  int   count  )  throws   ReadFaultException  { 
int   pageWords  =  (  1  <<  pageShift  )  >  >  >  2  ; 
int   pageMask  =  pageWords  -  1  ; 
int   x  =  0  ; 
if  (  count  ==  0  )  return  ; 
if  (  (  addr  &  3  )  !=  0  )  { 
int   word  =  memRead  (  addr  &  ~  3  )  ; 
switch  (  addr  &  3  )  { 
case   1  : 
buf  [  x  ++  ]  =  (  byte  )  (  (  word  >  >  >  16  )  &  0xff  )  ; 
if  (  --  count  ==  0  )  break  ; 
case   2  : 
buf  [  x  ++  ]  =  (  byte  )  (  (  word  >  >  >  8  )  &  0xff  )  ; 
if  (  --  count  ==  0  )  break  ; 
case   3  : 
buf  [  x  ++  ]  =  (  byte  )  (  (  word  >  >  >  0  )  &  0xff  )  ; 
if  (  --  count  ==  0  )  break  ; 
} 
addr  =  (  addr  &  ~  3  )  +  4  ; 
} 
if  (  (  count  &  ~  3  )  !=  0  )  { 
int   c  =  count  >  >  >  2  ; 
int   a  =  addr  >  >  >  2  ; 
while  (  c  !=  0  )  { 
int  [  ]  page  =  readPages  [  a  >  >  >  (  pageShift  -  2  )  ]  ; 
if  (  page  ==  null  )  throw   new   ReadFaultException  (  a  <<  2  )  ; 
int   index  =  a  &  pageMask  ; 
int   n  =  min  (  c  ,  pageWords  -  index  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  x  +=  4  )  { 
int   word  =  page  [  index  +  i  ]  ; 
buf  [  x  +  0  ]  =  (  byte  )  (  (  word  >  >  >  24  )  &  0xff  )  ; 
buf  [  x  +  1  ]  =  (  byte  )  (  (  word  >  >  >  16  )  &  0xff  )  ; 
buf  [  x  +  2  ]  =  (  byte  )  (  (  word  >  >  >  8  )  &  0xff  )  ; 
buf  [  x  +  3  ]  =  (  byte  )  (  (  word  >  >  >  0  )  &  0xff  )  ; 
} 
a  +=  n  ; 
c  -=  n  ; 
} 
addr  =  a  <<  2  ; 
count  &=  3  ; 
} 
if  (  count  !=  0  )  { 
int   word  =  memRead  (  addr  )  ; 
switch  (  count  )  { 
case   3  : 
buf  [  x  +  2  ]  =  (  byte  )  (  (  word  >  >  >  8  )  &  0xff  )  ; 
case   2  : 
buf  [  x  +  1  ]  =  (  byte  )  (  (  word  >  >  >  16  )  &  0xff  )  ; 
case   1  : 
buf  [  x  +  0  ]  =  (  byte  )  (  (  word  >  >  >  24  )  &  0xff  )  ; 
} 
} 
} 



public   final   void   copyout  (  byte  [  ]  buf  ,  int   addr  ,  int   count  )  throws   FaultException  { 
int   pageWords  =  (  1  <<  pageShift  )  >  >  >  2  ; 
int   pageWordMask  =  pageWords  -  1  ; 
int   x  =  0  ; 
if  (  count  ==  0  )  return  ; 
if  (  (  addr  &  3  )  !=  0  )  { 
int   word  =  memRead  (  addr  &  ~  3  )  ; 
switch  (  addr  &  3  )  { 
case   1  : 
word  =  (  word  &  0xff00ffff  )  |  (  (  buf  [  x  ++  ]  &  0xff  )  <<  16  )  ; 
if  (  --  count  ==  0  )  break  ; 
case   2  : 
word  =  (  word  &  0xffff00ff  )  |  (  (  buf  [  x  ++  ]  &  0xff  )  <<  8  )  ; 
if  (  --  count  ==  0  )  break  ; 
case   3  : 
word  =  (  word  &  0xffffff00  )  |  (  (  buf  [  x  ++  ]  &  0xff  )  <<  0  )  ; 
if  (  --  count  ==  0  )  break  ; 
} 
memWrite  (  addr  &  ~  3  ,  word  )  ; 
addr  +=  x  ; 
} 
if  (  (  count  &  ~  3  )  !=  0  )  { 
int   c  =  count  >  >  >  2  ; 
int   a  =  addr  >  >  >  2  ; 
while  (  c  !=  0  )  { 
int  [  ]  page  =  writePages  [  a  >  >  >  (  pageShift  -  2  )  ]  ; 
if  (  page  ==  null  )  throw   new   WriteFaultException  (  a  <<  2  )  ; 
int   index  =  a  &  pageWordMask  ; 
int   n  =  min  (  c  ,  pageWords  -  index  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  ,  x  +=  4  )  page  [  index  +  i  ]  =  (  (  buf  [  x  +  0  ]  &  0xff  )  <<  24  )  |  (  (  buf  [  x  +  1  ]  &  0xff  )  <<  16  )  |  (  (  buf  [  x  +  2  ]  &  0xff  )  <<  8  )  |  (  (  buf  [  x  +  3  ]  &  0xff  )  <<  0  )  ; 
a  +=  n  ; 
c  -=  n  ; 
} 
addr  =  a  <<  2  ; 
count  &=  3  ; 
} 
if  (  count  !=  0  )  { 
int   word  =  memRead  (  addr  )  ; 
switch  (  count  )  { 
case   1  : 
word  =  (  word  &  0x00ffffff  )  |  (  (  buf  [  x  +  0  ]  &  0xff  )  <<  24  )  ; 
break  ; 
case   2  : 
word  =  (  word  &  0x0000ffff  )  |  (  (  buf  [  x  +  0  ]  &  0xff  )  <<  24  )  |  (  (  buf  [  x  +  1  ]  &  0xff  )  <<  16  )  ; 
break  ; 
case   3  : 
word  =  (  word  &  0x000000ff  )  |  (  (  buf  [  x  +  0  ]  &  0xff  )  <<  24  )  |  (  (  buf  [  x  +  1  ]  &  0xff  )  <<  16  )  |  (  (  buf  [  x  +  2  ]  &  0xff  )  <<  8  )  ; 
break  ; 
} 
memWrite  (  addr  ,  word  )  ; 
} 
} 

public   final   void   memcpy  (  int   dst  ,  int   src  ,  int   count  )  throws   FaultException  { 
int   pageWords  =  (  1  <<  pageShift  )  >  >  >  2  ; 
int   pageWordMask  =  pageWords  -  1  ; 
if  (  (  dst  &  3  )  ==  0  &&  (  src  &  3  )  ==  0  )  { 
if  (  (  count  &  ~  3  )  !=  0  )  { 
int   c  =  count  >  >  2  ; 
int   s  =  src  >  >  >  2  ; 
int   d  =  dst  >  >  >  2  ; 
while  (  c  !=  0  )  { 
int  [  ]  srcPage  =  readPages  [  s  >  >  >  (  pageShift  -  2  )  ]  ; 
if  (  srcPage  ==  null  )  throw   new   ReadFaultException  (  s  <<  2  )  ; 
int  [  ]  dstPage  =  writePages  [  d  >  >  >  (  pageShift  -  2  )  ]  ; 
if  (  dstPage  ==  null  )  throw   new   WriteFaultException  (  d  <<  2  )  ; 
int   srcIndex  =  s  &  pageWordMask  ; 
int   dstIndex  =  d  &  pageWordMask  ; 
int   n  =  min  (  c  ,  pageWords  -  max  (  srcIndex  ,  dstIndex  )  )  ; 
System  .  arraycopy  (  srcPage  ,  srcIndex  ,  dstPage  ,  dstIndex  ,  n  )  ; 
s  +=  n  ; 
d  +=  n  ; 
c  -=  n  ; 
} 
src  =  s  <<  2  ; 
dst  =  d  <<  2  ; 
count  &=  3  ; 
} 
if  (  count  !=  0  )  { 
int   word1  =  memRead  (  src  )  ; 
int   word2  =  memRead  (  dst  )  ; 
switch  (  count  )  { 
case   1  : 
memWrite  (  dst  ,  (  word1  &  0xff000000  )  |  (  word2  &  0x00ffffff  )  )  ; 
break  ; 
case   2  : 
memWrite  (  dst  ,  (  word1  &  0xffff0000  )  |  (  word2  &  0x0000ffff  )  )  ; 
break  ; 
case   3  : 
memWrite  (  dst  ,  (  word1  &  0xffffff00  )  |  (  word2  &  0x000000ff  )  )  ; 
break  ; 
} 
} 
}  else  { 
while  (  count  >  0  )  { 
int   n  =  min  (  count  ,  MAX_CHUNK  )  ; 
byte  [  ]  buf  =  byteBuf  (  n  )  ; 
copyin  (  src  ,  buf  ,  n  )  ; 
copyout  (  buf  ,  dst  ,  n  )  ; 
count  -=  n  ; 
src  +=  n  ; 
dst  +=  n  ; 
} 
} 
} 

public   final   void   memset  (  int   addr  ,  int   ch  ,  int   count  )  throws   FaultException  { 
int   pageWords  =  (  1  <<  pageShift  )  >  >  >  2  ; 
int   pageWordMask  =  pageWords  -  1  ; 
int   fourBytes  =  (  (  ch  &  0xff  )  <<  24  )  |  (  (  ch  &  0xff  )  <<  16  )  |  (  (  ch  &  0xff  )  <<  8  )  |  (  (  ch  &  0xff  )  <<  0  )  ; 
if  (  (  addr  &  3  )  !=  0  )  { 
int   word  =  memRead  (  addr  &  ~  3  )  ; 
switch  (  addr  &  3  )  { 
case   1  : 
word  =  (  word  &  0xff00ffff  )  |  (  (  ch  &  0xff  )  <<  16  )  ; 
if  (  --  count  ==  0  )  break  ; 
case   2  : 
word  =  (  word  &  0xffff00ff  )  |  (  (  ch  &  0xff  )  <<  8  )  ; 
if  (  --  count  ==  0  )  break  ; 
case   3  : 
word  =  (  word  &  0xffffff00  )  |  (  (  ch  &  0xff  )  <<  0  )  ; 
if  (  --  count  ==  0  )  break  ; 
} 
memWrite  (  addr  &  ~  3  ,  word  )  ; 
addr  =  (  addr  &  ~  3  )  +  4  ; 
} 
if  (  (  count  &  ~  3  )  !=  0  )  { 
int   c  =  count  >  >  2  ; 
int   a  =  addr  >  >  >  2  ; 
while  (  c  !=  0  )  { 
int  [  ]  page  =  readPages  [  a  >  >  >  (  pageShift  -  2  )  ]  ; 
if  (  page  ==  null  )  throw   new   WriteFaultException  (  a  <<  2  )  ; 
int   index  =  a  &  pageWordMask  ; 
int   n  =  min  (  c  ,  pageWords  -  index  )  ; 
for  (  int   i  =  index  ;  i  <  index  +  n  ;  i  ++  )  page  [  i  ]  =  fourBytes  ; 
a  +=  n  ; 
c  -=  n  ; 
} 
addr  =  a  <<  2  ; 
count  &=  3  ; 
} 
if  (  count  !=  0  )  { 
int   word  =  memRead  (  addr  )  ; 
switch  (  count  )  { 
case   1  : 
word  =  (  word  &  0x00ffffff  )  |  (  fourBytes  &  0xff000000  )  ; 
break  ; 
case   2  : 
word  =  (  word  &  0x0000ffff  )  |  (  fourBytes  &  0xffff0000  )  ; 
break  ; 
case   3  : 
word  =  (  word  &  0x000000ff  )  |  (  fourBytes  &  0xffffff00  )  ; 
break  ; 
} 
memWrite  (  addr  ,  word  )  ; 
} 
} 


public   final   int   memRead  (  int   addr  )  throws   ReadFaultException  { 
if  (  (  addr  &  3  )  !=  0  )  throw   new   ReadFaultException  (  addr  )  ; 
return   unsafeMemRead  (  addr  )  ; 
} 

protected   final   int   unsafeMemRead  (  int   addr  )  throws   ReadFaultException  { 
int   page  =  addr  >  >  >  pageShift  ; 
int   entry  =  (  addr  &  (  1  <<  pageShift  )  -  1  )  >  >  2  ; 
try  { 
return   readPages  [  page  ]  [  entry  ]  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
if  (  page  <  0  ||  page  >=  readPages  .  length  )  throw   new   ReadFaultException  (  addr  )  ; 
throw   e  ; 
}  catch  (  NullPointerException   e  )  { 
throw   new   ReadFaultException  (  addr  )  ; 
} 
} 


public   final   void   memWrite  (  int   addr  ,  int   value  )  throws   WriteFaultException  { 
if  (  (  addr  &  3  )  !=  0  )  throw   new   WriteFaultException  (  addr  )  ; 
unsafeMemWrite  (  addr  ,  value  )  ; 
} 

protected   final   void   unsafeMemWrite  (  int   addr  ,  int   value  )  throws   WriteFaultException  { 
int   page  =  addr  >  >  >  pageShift  ; 
int   entry  =  (  addr  &  (  1  <<  pageShift  )  -  1  )  >  >  2  ; 
try  { 
writePages  [  page  ]  [  entry  ]  =  value  ; 
}  catch  (  ArrayIndexOutOfBoundsException   e  )  { 
if  (  page  <  0  ||  page  >=  writePages  .  length  )  throw   new   WriteFaultException  (  addr  )  ; 
throw   e  ; 
}  catch  (  NullPointerException   e  )  { 
throw   new   WriteFaultException  (  addr  )  ; 
} 
} 


private   final   int  [  ]  initPage  (  int   page  )  { 
return   initPage  (  page  ,  false  )  ; 
} 


private   final   int  [  ]  initPage  (  int   page  ,  boolean   ro  )  { 
int  [  ]  buf  =  new   int  [  (  1  <<  pageShift  )  >  >  >  2  ]  ; 
writePages  [  page  ]  =  ro  ?  null  :  buf  ; 
readPages  [  page  ]  =  buf  ; 
return   buf  ; 
} 



public   final   int   exitStatus  (  )  { 
if  (  state  !=  EXITED  )  throw   new   IllegalStateException  (  "exitStatus() called in an inappropriate state"  )  ; 
return   exitStatus  ; 
} 

private   int   addStringArray  (  String  [  ]  strings  ,  int   topAddr  )  throws   FaultException  { 
int   count  =  strings  .  length  ; 
int   total  =  0  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  total  +=  strings  [  i  ]  .  length  (  )  +  1  ; 
total  +=  (  count  +  1  )  *  4  ; 
int   start  =  (  topAddr  -  total  )  &  ~  3  ; 
int   addr  =  start  +  (  count  +  1  )  *  4  ; 
int  [  ]  table  =  new   int  [  count  +  1  ]  ; 
try  { 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  { 
byte  [  ]  a  =  getBytes  (  strings  [  i  ]  )  ; 
table  [  i  ]  =  addr  ; 
copyout  (  a  ,  addr  ,  a  .  length  )  ; 
memset  (  addr  +  a  .  length  ,  0  ,  1  )  ; 
addr  +=  a  .  length  +  1  ; 
} 
addr  =  start  ; 
for  (  int   i  =  0  ;  i  <  count  +  1  ;  i  ++  )  { 
memWrite  (  addr  ,  table  [  i  ]  )  ; 
addr  +=  4  ; 
} 
}  catch  (  FaultException   e  )  { 
throw   new   RuntimeException  (  e  .  toString  (  )  )  ; 
} 
return   start  ; 
} 

String  [  ]  createEnv  (  String  [  ]  extra  )  { 
if  (  extra  ==  null  )  extra  =  new   String  [  0  ]  ; 
return   extra  ; 
} 






public   void   setUserInfo  (  int   index  ,  int   word  )  { 
if  (  index  <  0  ||  index  >=  userInfoSize  (  )  /  4  )  throw   new   IndexOutOfBoundsException  (  "setUserInfo called with index >= "  +  (  userInfoSize  (  )  /  4  )  )  ; 
try  { 
memWrite  (  userInfoBase  (  )  +  index  *  4  ,  word  )  ; 
}  catch  (  FaultException   e  )  { 
throw   new   RuntimeException  (  e  .  toString  (  )  )  ; 
} 
} 



public   int   getUserInfo  (  int   index  )  { 
if  (  index  <  0  ||  index  >=  userInfoSize  (  )  /  4  )  throw   new   IndexOutOfBoundsException  (  "setUserInfo called with index >= "  +  (  userInfoSize  (  )  /  4  )  )  ; 
try  { 
return   memRead  (  userInfoBase  (  )  +  index  *  4  )  ; 
}  catch  (  FaultException   e  )  { 
throw   new   RuntimeException  (  e  .  toString  (  )  )  ; 
} 
} 


private   void   __execute  (  )  { 
try  { 
_execute  (  )  ; 
}  catch  (  FaultException   e  )  { 
if  (  STDERR_DIAG  )  e  .  printStackTrace  (  )  ; 
exit  (  128  +  11  ,  true  )  ; 
exitException  =  e  ; 
}  catch  (  ExecutionException   e  )  { 
if  (  STDERR_DIAG  )  e  .  printStackTrace  (  )  ; 
exit  (  128  +  4  ,  true  )  ; 
exitException  =  e  ; 
} 
} 


public   final   boolean   execute  (  )  { 
if  (  state  !=  PAUSED  )  throw   new   IllegalStateException  (  "execute() called in inappropriate state"  )  ; 
if  (  startTime  ==  0  )  startTime  =  System  .  currentTimeMillis  (  )  ; 
state  =  RUNNING  ; 
__execute  (  )  ; 
if  (  state  !=  PAUSED  &&  state  !=  EXITED  &&  state  !=  EXECED  )  throw   new   IllegalStateException  (  "execute() ended up in an inappropriate state ("  +  state  +  ")"  )  ; 
return   state  !=  PAUSED  ; 
} 

static   String  [  ]  concatArgv  (  String   argv0  ,  String  [  ]  rest  )  { 
String  [  ]  argv  =  new   String  [  rest  .  length  +  1  ]  ; 
System  .  arraycopy  (  rest  ,  0  ,  argv  ,  1  ,  rest  .  length  )  ; 
argv  [  0  ]  =  argv0  ; 
return   argv  ; 
} 

public   final   int   run  (  )  { 
return   run  (  null  )  ; 
} 

public   final   int   run  (  String   argv0  ,  String  [  ]  rest  )  { 
return   run  (  concatArgv  (  argv0  ,  rest  )  )  ; 
} 

public   final   int   run  (  String  [  ]  args  )  { 
return   run  (  args  ,  null  )  ; 
} 



public   final   int   run  (  String  [  ]  args  ,  String  [  ]  env  )  { 
start  (  args  ,  env  )  ; 
for  (  ;  ;  )  { 
if  (  execute  (  )  )  break  ; 
if  (  STDERR_DIAG  )  System  .  err  .  println  (  "WARNING: Pause requested while executing run()"  )  ; 
} 
if  (  state  ==  EXECED  &&  STDERR_DIAG  )  System  .  err  .  println  (  "WARNING: Process exec()ed while being run under run()"  )  ; 
return   state  ==  EXITED  ?  exitStatus  (  )  :  0  ; 
} 

public   final   void   start  (  )  { 
start  (  null  )  ; 
} 

public   final   void   start  (  String  [  ]  args  )  { 
start  (  args  ,  null  )  ; 
} 


public   final   void   start  (  String  [  ]  args  ,  String  [  ]  environ  )  { 
int   top  ,  sp  ,  argsAddr  ,  envAddr  ; 
if  (  state  !=  STOPPED  )  throw   new   IllegalStateException  (  "start() called in inappropriate state"  )  ; 
if  (  args  ==  null  )  args  =  new   String  [  ]  {  getClass  (  )  .  getName  (  )  }  ; 
sp  =  top  =  writePages  .  length  *  (  1  <<  pageShift  )  ; 
try  { 
sp  =  argsAddr  =  addStringArray  (  args  ,  sp  )  ; 
sp  =  envAddr  =  addStringArray  (  createEnv  (  environ  )  ,  sp  )  ; 
}  catch  (  FaultException   e  )  { 
throw   new   IllegalArgumentException  (  "args/environ too big"  )  ; 
} 
sp  &=  ~  15  ; 
if  (  top  -  sp  >  ARG_MAX  )  throw   new   IllegalArgumentException  (  "args/environ too big"  )  ; 
if  (  heapEnd  ==  0  )  { 
heapEnd  =  heapStart  (  )  ; 
if  (  heapEnd  ==  0  )  throw   new   Error  (  "heapEnd == 0"  )  ; 
int   pageSize  =  writePages  .  length  ==  1  ?  4096  :  (  1  <<  pageShift  )  ; 
heapEnd  =  (  heapEnd  +  pageSize  -  1  )  &  ~  (  pageSize  -  1  )  ; 
} 
CPUState   cpuState  =  new   CPUState  (  )  ; 
cpuState  .  r  [  A0  ]  =  argsAddr  ; 
cpuState  .  r  [  A1  ]  =  envAddr  ; 
cpuState  .  r  [  SP  ]  =  sp  ; 
cpuState  .  r  [  RA  ]  =  0xdeadbeef  ; 
cpuState  .  r  [  GP  ]  =  gp  (  )  ; 
cpuState  .  pc  =  entryPoint  (  )  ; 
setCPUState  (  cpuState  )  ; 
state  =  PAUSED  ; 
_started  (  )  ; 
} 


void   _started  (  )  { 
} 

public   final   int   call  (  String   sym  ,  Object  [  ]  args  )  throws   CallException  ,  FaultException  { 
if  (  state  !=  PAUSED  &&  state  !=  CALLJAVA  )  throw   new   IllegalStateException  (  "call() called in inappropriate state"  )  ; 
if  (  args  .  length  >  7  )  throw   new   IllegalArgumentException  (  "args.length > 7"  )  ; 
CPUState   state  =  new   CPUState  (  )  ; 
getCPUState  (  state  )  ; 
int   sp  =  state  .  r  [  SP  ]  ; 
int  [  ]  ia  =  new   int  [  args  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
Object   o  =  args  [  i  ]  ; 
byte  [  ]  buf  =  null  ; 
if  (  o   instanceof   String  )  { 
buf  =  getBytes  (  (  String  )  o  )  ; 
}  else   if  (  o   instanceof   byte  [  ]  )  { 
buf  =  (  byte  [  ]  )  o  ; 
}  else   if  (  o   instanceof   Number  )  { 
ia  [  i  ]  =  (  (  Number  )  o  )  .  intValue  (  )  ; 
} 
if  (  buf  !=  null  )  { 
sp  -=  buf  .  length  ; 
copyout  (  buf  ,  sp  ,  buf  .  length  )  ; 
ia  [  i  ]  =  sp  ; 
} 
} 
int   oldSP  =  state  .  r  [  SP  ]  ; 
if  (  oldSP  ==  sp  )  return   call  (  sym  ,  ia  )  ; 
state  .  r  [  SP  ]  =  sp  ; 
setCPUState  (  state  )  ; 
int   ret  =  call  (  sym  ,  ia  )  ; 
state  .  r  [  SP  ]  =  oldSP  ; 
setCPUState  (  state  )  ; 
return   ret  ; 
} 

public   final   int   call  (  String   sym  )  throws   CallException  { 
return   call  (  sym  ,  new   int  [  ]  {  }  )  ; 
} 

public   final   int   call  (  String   sym  ,  int   a0  )  throws   CallException  { 
return   call  (  sym  ,  new   int  [  ]  {  a0  }  )  ; 
} 

public   final   int   call  (  String   sym  ,  int   a0  ,  int   a1  )  throws   CallException  { 
return   call  (  sym  ,  new   int  [  ]  {  a0  ,  a1  }  )  ; 
} 


public   final   int   call  (  String   sym  ,  int  [  ]  args  )  throws   CallException  { 
int   func  =  lookupSymbol  (  sym  )  ; 
if  (  func  ==  -  1  )  throw   new   CallException  (  sym  +  " not found"  )  ; 
int   helper  =  lookupSymbol  (  "_call_helper"  )  ; 
if  (  helper  ==  -  1  )  throw   new   CallException  (  "_call_helper not found"  )  ; 
return   call  (  helper  ,  func  ,  args  )  ; 
} 

public   final   int   call  (  int   addr  ,  int   a0  ,  int  [  ]  rest  )  throws   CallException  { 
if  (  rest  .  length  >  7  )  throw   new   IllegalArgumentException  (  "rest.length > 7"  )  ; 
if  (  state  !=  PAUSED  &&  state  !=  CALLJAVA  )  throw   new   IllegalStateException  (  "call() called in inappropriate state"  )  ; 
int   oldState  =  state  ; 
CPUState   saved  =  new   CPUState  (  )  ; 
getCPUState  (  saved  )  ; 
CPUState   cpustate  =  saved  .  dup  (  )  ; 
cpustate  .  r  [  SP  ]  =  cpustate  .  r  [  SP  ]  &  ~  15  ; 
cpustate  .  r  [  RA  ]  =  0xdeadbeef  ; 
cpustate  .  r  [  A0  ]  =  a0  ; 
switch  (  rest  .  length  )  { 
case   7  : 
cpustate  .  r  [  S3  ]  =  rest  [  6  ]  ; 
case   6  : 
cpustate  .  r  [  S2  ]  =  rest  [  5  ]  ; 
case   5  : 
cpustate  .  r  [  S1  ]  =  rest  [  4  ]  ; 
case   4  : 
cpustate  .  r  [  S0  ]  =  rest  [  3  ]  ; 
case   3  : 
cpustate  .  r  [  A3  ]  =  rest  [  2  ]  ; 
case   2  : 
cpustate  .  r  [  A2  ]  =  rest  [  1  ]  ; 
case   1  : 
cpustate  .  r  [  A1  ]  =  rest  [  0  ]  ; 
} 
cpustate  .  pc  =  addr  ; 
state  =  RUNNING  ; 
setCPUState  (  cpustate  )  ; 
__execute  (  )  ; 
getCPUState  (  cpustate  )  ; 
setCPUState  (  saved  )  ; 
if  (  state  !=  PAUSED  )  throw   new   CallException  (  "Process exit()ed while servicing a call() request"  )  ; 
state  =  oldState  ; 
return   cpustate  .  r  [  V1  ]  ; 
} 




public   final   int   addFD  (  FD   fd  )  { 
if  (  state  ==  EXITED  ||  state  ==  EXECED  )  throw   new   IllegalStateException  (  "addFD called in inappropriate state"  )  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  OPEN_MAX  ;  i  ++  )  if  (  fds  [  i  ]  ==  null  )  break  ; 
if  (  i  ==  OPEN_MAX  )  return  -  1  ; 
fds  [  i  ]  =  fd  ; 
closeOnExec  [  i  ]  =  false  ; 
return   i  ; 
} 


public   final   boolean   closeFD  (  int   fdn  )  { 
if  (  state  ==  EXITED  ||  state  ==  EXECED  )  throw   new   IllegalStateException  (  "closeFD called in inappropriate state"  )  ; 
if  (  fdn  <  0  ||  fdn  >=  OPEN_MAX  )  return   false  ; 
if  (  fds  [  fdn  ]  ==  null  )  return   false  ; 
fds  [  fdn  ]  .  close  (  )  ; 
fds  [  fdn  ]  =  null  ; 
return   true  ; 
} 


public   final   int   dupFD  (  int   fdn  )  { 
int   i  ; 
if  (  fdn  <  0  ||  fdn  >=  OPEN_MAX  )  return  -  1  ; 
if  (  fds  [  fdn  ]  ==  null  )  return  -  1  ; 
for  (  i  =  0  ;  i  <  OPEN_MAX  ;  i  ++  )  if  (  fds  [  i  ]  ==  null  )  break  ; 
if  (  i  ==  OPEN_MAX  )  return  -  1  ; 
fds  [  i  ]  =  fds  [  fdn  ]  .  dup  (  )  ; 
return   i  ; 
} 

public   static   final   int   RD_ONLY  =  0  ; 

public   static   final   int   WR_ONLY  =  1  ; 

public   static   final   int   RDWR  =  2  ; 

public   static   final   int   O_CREAT  =  0x0200  ; 

public   static   final   int   O_EXCL  =  0x0800  ; 

public   static   final   int   O_APPEND  =  0x0008  ; 

public   static   final   int   O_TRUNC  =  0x0400  ; 

public   static   final   int   O_NONBLOCK  =  0x4000  ; 

public   static   final   int   O_NOCTTY  =  0x8000  ; 

FD   hostFSOpen  (  final   File   f  ,  int   flags  ,  int   mode  ,  final   Object   data  )  throws   ErrnoException  { 
if  (  (  flags  &  ~  (  3  |  O_CREAT  |  O_EXCL  |  O_APPEND  |  O_TRUNC  )  )  !=  0  )  { 
if  (  STDERR_DIAG  )  System  .  err  .  println  (  "WARNING: Unsupported flags passed to open(\""  +  f  +  "\"): "  +  toHex  (  flags  &  ~  (  3  |  O_CREAT  |  O_EXCL  |  O_APPEND  |  O_TRUNC  )  )  )  ; 
throw   new   ErrnoException  (  ENOTSUP  )  ; 
} 
boolean   write  =  (  flags  &  3  )  !=  RD_ONLY  ; 
if  (  sm  !=  null  &&  !  (  write  ?  sm  .  allowWrite  (  f  )  :  sm  .  allowRead  (  f  )  )  )  throw   new   ErrnoException  (  EACCES  )  ; 
if  (  (  flags  &  (  O_EXCL  |  O_CREAT  )  )  ==  (  O_EXCL  |  O_CREAT  )  )  { 
try  { 
if  (  Platform  .  atomicCreateFile  (  f  )  )  throw   new   ErrnoException  (  EEXIST  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   ErrnoException  (  EIO  )  ; 
} 
}  else   if  (  !  f  .  exists  (  )  )  { 
if  (  (  flags  &  O_CREAT  )  ==  0  )  return   null  ; 
}  else   if  (  f  .  isDirectory  (  )  )  { 
return   hostFSDirFD  (  f  ,  data  )  ; 
} 
final   Seekable  .  File   sf  ; 
try  { 
sf  =  new   Seekable  .  File  (  f  ,  write  ,  (  flags  &  O_TRUNC  )  !=  0  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
if  (  e  .  getMessage  (  )  !=  null  &&  e  .  getMessage  (  )  .  indexOf  (  "Permission denied"  )  >=  0  )  throw   new   ErrnoException  (  EACCES  )  ; 
return   null  ; 
}  catch  (  IOException   e  )  { 
throw   new   ErrnoException  (  EIO  )  ; 
} 
return   new   SeekableFD  (  sf  ,  flags  )  { 

protected   FStat   _fstat  (  )  { 
return   hostFStat  (  f  ,  data  )  ; 
} 
}  ; 
} 

FStat   hostFStat  (  File   f  ,  Object   data  )  { 
return   new   HostFStat  (  f  )  ; 
} 

FD   hostFSDirFD  (  File   f  ,  Object   data  )  { 
return   null  ; 
} 

FD   _open  (  String   path  ,  int   flags  ,  int   mode  )  throws   ErrnoException  { 
return   hostFSOpen  (  new   File  (  path  )  ,  flags  ,  mode  ,  null  )  ; 
} 


private   int   sys_open  (  int   addr  ,  int   flags  ,  int   mode  )  throws   ErrnoException  ,  FaultException  { 
String   name  =  cstring  (  addr  )  ; 
if  (  name  .  length  (  )  ==  1024  &&  getClass  (  )  .  getName  (  )  .  equals  (  "tests.TeX"  )  )  name  =  name  .  trim  (  )  ; 
flags  &=  ~  O_NOCTTY  ; 
FD   fd  =  _open  (  name  ,  flags  ,  mode  )  ; 
if  (  fd  ==  null  )  return  -  ENOENT  ; 
int   fdn  =  addFD  (  fd  )  ; 
if  (  fdn  ==  -  1  )  { 
fd  .  close  (  )  ; 
return  -  ENFILE  ; 
} 
return   fdn  ; 
} 


private   int   sys_write  (  int   fdn  ,  int   addr  ,  int   count  )  throws   FaultException  ,  ErrnoException  { 
count  =  Math  .  min  (  count  ,  MAX_CHUNK  )  ; 
if  (  fdn  <  0  ||  fdn  >=  OPEN_MAX  )  return  -  EBADFD  ; 
if  (  fds  [  fdn  ]  ==  null  )  return  -  EBADFD  ; 
byte  [  ]  buf  =  byteBuf  (  count  )  ; 
copyin  (  addr  ,  buf  ,  count  )  ; 
try  { 
return   fds  [  fdn  ]  .  write  (  buf  ,  0  ,  count  )  ; 
}  catch  (  ErrnoException   e  )  { 
if  (  e  .  errno  ==  EPIPE  )  sys_exit  (  128  +  13  )  ; 
throw   e  ; 
} 
} 


private   int   sys_read  (  int   fdn  ,  int   addr  ,  int   count  )  throws   FaultException  ,  ErrnoException  { 
count  =  Math  .  min  (  count  ,  MAX_CHUNK  )  ; 
if  (  fdn  <  0  ||  fdn  >=  OPEN_MAX  )  return  -  EBADFD  ; 
if  (  fds  [  fdn  ]  ==  null  )  return  -  EBADFD  ; 
byte  [  ]  buf  =  byteBuf  (  count  )  ; 
int   n  =  fds  [  fdn  ]  .  read  (  buf  ,  0  ,  count  )  ; 
copyout  (  buf  ,  addr  ,  n  )  ; 
return   n  ; 
} 


private   int   sys_close  (  int   fdn  )  { 
return   closeFD  (  fdn  )  ?  0  :  -  EBADFD  ; 
} 


private   int   sys_lseek  (  int   fdn  ,  int   offset  ,  int   whence  )  throws   ErrnoException  { 
if  (  fdn  <  0  ||  fdn  >=  OPEN_MAX  )  return  -  EBADFD  ; 
if  (  fds  [  fdn  ]  ==  null  )  return  -  EBADFD  ; 
if  (  whence  !=  SEEK_SET  &&  whence  !=  SEEK_CUR  &&  whence  !=  SEEK_END  )  return  -  EINVAL  ; 
int   n  =  fds  [  fdn  ]  .  seek  (  offset  ,  whence  )  ; 
return   n  <  0  ?  -  ESPIPE  :  n  ; 
} 


int   stat  (  FStat   fs  ,  int   addr  )  throws   FaultException  { 
memWrite  (  addr  +  0  ,  (  fs  .  dev  (  )  <<  16  )  |  (  fs  .  inode  (  )  &  0xffff  )  )  ; 
memWrite  (  addr  +  4  ,  (  (  fs  .  type  (  )  &  0xf000  )  )  |  (  fs  .  mode  (  )  &  0xfff  )  )  ; 
memWrite  (  addr  +  8  ,  fs  .  nlink  (  )  <<  16  |  fs  .  uid  (  )  &  0xffff  )  ; 
memWrite  (  addr  +  12  ,  fs  .  gid  (  )  <<  16  |  0  )  ; 
memWrite  (  addr  +  16  ,  fs  .  size  (  )  )  ; 
memWrite  (  addr  +  20  ,  fs  .  atime  (  )  )  ; 
memWrite  (  addr  +  28  ,  fs  .  mtime  (  )  )  ; 
memWrite  (  addr  +  36  ,  fs  .  ctime  (  )  )  ; 
memWrite  (  addr  +  44  ,  fs  .  blksize  (  )  )  ; 
memWrite  (  addr  +  48  ,  fs  .  blocks  (  )  )  ; 
return   0  ; 
} 


private   int   sys_fstat  (  int   fdn  ,  int   addr  )  throws   FaultException  { 
if  (  fdn  <  0  ||  fdn  >=  OPEN_MAX  )  return  -  EBADFD  ; 
if  (  fds  [  fdn  ]  ==  null  )  return  -  EBADFD  ; 
return   stat  (  fds  [  fdn  ]  .  fstat  (  )  ,  addr  )  ; 
} 

private   int   sys_gettimeofday  (  int   timevalAddr  ,  int   timezoneAddr  )  throws   FaultException  { 
long   now  =  System  .  currentTimeMillis  (  )  ; 
int   tv_sec  =  (  int  )  (  now  /  1000  )  ; 
int   tv_usec  =  (  int  )  (  (  now  %  1000  )  *  1000  )  ; 
memWrite  (  timevalAddr  +  0  ,  tv_sec  )  ; 
memWrite  (  timevalAddr  +  4  ,  tv_usec  )  ; 
return   0  ; 
} 

private   int   sys_sleep  (  int   sec  )  { 
if  (  sec  <  0  )  sec  =  Integer  .  MAX_VALUE  ; 
try  { 
Thread  .  sleep  (  (  long  )  sec  *  1000  )  ; 
return   0  ; 
}  catch  (  InterruptedException   e  )  { 
return  -  1  ; 
} 
} 

private   int   sys_times  (  int   tms  )  { 
long   now  =  System  .  currentTimeMillis  (  )  ; 
int   userTime  =  (  int  )  (  (  now  -  startTime  )  /  16  )  ; 
int   sysTime  =  (  int  )  (  (  now  -  startTime  )  /  16  )  ; 
try  { 
if  (  tms  !=  0  )  { 
memWrite  (  tms  +  0  ,  userTime  )  ; 
memWrite  (  tms  +  4  ,  sysTime  )  ; 
memWrite  (  tms  +  8  ,  userTime  )  ; 
memWrite  (  tms  +  12  ,  sysTime  )  ; 
} 
}  catch  (  FaultException   e  )  { 
return  -  EFAULT  ; 
} 
return  (  int  )  now  ; 
} 

private   int   sys_sysconf  (  int   n  )  { 
switch  (  n  )  { 
case   _SC_CLK_TCK  : 
return   1000  ; 
case   _SC_PAGESIZE  : 
return   writePages  .  length  ==  1  ?  4096  :  (  1  <<  pageShift  )  ; 
case   _SC_PHYS_PAGES  : 
return   writePages  .  length  ==  1  ?  (  1  <<  pageShift  )  /  4096  :  writePages  .  length  ; 
default  : 
if  (  STDERR_DIAG  )  System  .  err  .  println  (  "WARNING: Attempted to use unknown sysconf key: "  +  n  )  ; 
return  -  EINVAL  ; 
} 
} 



public   final   int   sbrk  (  int   incr  )  { 
if  (  incr  <  0  )  return  -  ENOMEM  ; 
if  (  incr  ==  0  )  return   heapEnd  ; 
incr  =  (  incr  +  3  )  &  ~  3  ; 
int   oldEnd  =  heapEnd  ; 
int   newEnd  =  oldEnd  +  incr  ; 
if  (  newEnd  >=  stackBottom  )  return  -  ENOMEM  ; 
if  (  writePages  .  length  >  1  )  { 
int   pageMask  =  (  1  <<  pageShift  )  -  1  ; 
int   pageWords  =  (  1  <<  pageShift  )  >  >  >  2  ; 
int   start  =  (  oldEnd  +  pageMask  )  >  >  >  pageShift  ; 
int   end  =  (  newEnd  +  pageMask  )  >  >  >  pageShift  ; 
try  { 
for  (  int   i  =  start  ;  i  <  end  ;  i  ++  )  readPages  [  i  ]  =  writePages  [  i  ]  =  new   int  [  pageWords  ]  ; 
}  catch  (  OutOfMemoryError   e  )  { 
if  (  STDERR_DIAG  )  System  .  err  .  println  (  "WARNING: Caught OOM Exception in sbrk: "  +  e  )  ; 
return  -  ENOMEM  ; 
} 
} 
heapEnd  =  newEnd  ; 
return   oldEnd  ; 
} 


private   int   sys_getpid  (  )  { 
return   getPid  (  )  ; 
} 

int   getPid  (  )  { 
return   1  ; 
} 

public   static   interface   CallJavaCB  { 

public   int   call  (  int   a  ,  int   b  ,  int   c  ,  int   d  )  ; 
} 

private   int   sys_calljava  (  int   a  ,  int   b  ,  int   c  ,  int   d  )  { 
if  (  state  !=  RUNNING  )  throw   new   IllegalStateException  (  "wound up calling sys_calljava while not in RUNNING"  )  ; 
if  (  callJavaCB  !=  null  )  { 
state  =  CALLJAVA  ; 
int   ret  ; 
try  { 
ret  =  callJavaCB  .  call  (  a  ,  b  ,  c  ,  d  )  ; 
}  catch  (  RuntimeException   e  )  { 
System  .  err  .  println  (  "Error while executing callJavaCB"  )  ; 
e  .  printStackTrace  (  )  ; 
ret  =  0  ; 
} 
state  =  RUNNING  ; 
return   ret  ; 
}  else  { 
if  (  STDERR_DIAG  )  System  .  err  .  println  (  "WARNING: calljava syscall invoked without a calljava callback set"  )  ; 
return   0  ; 
} 
} 

private   int   sys_pause  (  )  { 
state  =  PAUSED  ; 
return   0  ; 
} 

private   int   sys_getpagesize  (  )  { 
return   writePages  .  length  ==  1  ?  4096  :  (  1  <<  pageShift  )  ; 
} 


void   _exited  (  )  { 
} 

void   exit  (  int   status  ,  boolean   fromSignal  )  { 
if  (  fromSignal  &&  fds  [  2  ]  !=  null  )  { 
try  { 
byte  [  ]  msg  =  getBytes  (  "Process exited on signal "  +  (  status  -  128  )  +  "\n"  )  ; 
fds  [  2  ]  .  write  (  msg  ,  0  ,  msg  .  length  )  ; 
}  catch  (  ErrnoException   e  )  { 
} 
} 
exitStatus  =  status  ; 
for  (  int   i  =  0  ;  i  <  fds  .  length  ;  i  ++  )  if  (  fds  [  i  ]  !=  null  )  closeFD  (  i  )  ; 
state  =  EXITED  ; 
_exited  (  )  ; 
} 

private   int   sys_exit  (  int   status  )  { 
exit  (  status  ,  false  )  ; 
return   0  ; 
} 

private   int   sys_fcntl  (  int   fdn  ,  int   cmd  ,  int   arg  )  { 
int   i  ; 
if  (  fdn  <  0  ||  fdn  >=  OPEN_MAX  )  return  -  EBADFD  ; 
if  (  fds  [  fdn  ]  ==  null  )  return  -  EBADFD  ; 
FD   fd  =  fds  [  fdn  ]  ; 
switch  (  cmd  )  { 
case   F_DUPFD  : 
if  (  arg  <  0  ||  arg  >=  OPEN_MAX  )  return  -  EINVAL  ; 
for  (  i  =  arg  ;  i  <  OPEN_MAX  ;  i  ++  )  if  (  fds  [  i  ]  ==  null  )  break  ; 
if  (  i  ==  OPEN_MAX  )  return  -  EMFILE  ; 
fds  [  i  ]  =  fd  .  dup  (  )  ; 
return   i  ; 
case   F_GETFL  : 
return   fd  .  flags  (  )  ; 
case   F_SETFD  : 
closeOnExec  [  fdn  ]  =  arg  !=  0  ; 
return   0  ; 
case   F_GETFD  : 
return   closeOnExec  [  fdn  ]  ?  1  :  0  ; 
default  : 
if  (  STDERR_DIAG  )  System  .  err  .  println  (  "WARNING: Unknown fcntl command: "  +  cmd  )  ; 
return  -  ENOSYS  ; 
} 
} 






protected   final   int   syscall  (  int   syscall  ,  int   a  ,  int   b  ,  int   c  ,  int   d  ,  int   e  ,  int   f  )  { 
try  { 
int   n  =  _syscall  (  syscall  ,  a  ,  b  ,  c  ,  d  ,  e  ,  f  )  ; 
return   n  ; 
}  catch  (  ErrnoException   ex  )  { 
return  -  ex  .  errno  ; 
}  catch  (  FaultException   ex  )  { 
return  -  EFAULT  ; 
}  catch  (  RuntimeException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
throw   new   Error  (  "Internal Error in _syscall()"  )  ; 
} 
} 

int   _syscall  (  int   syscall  ,  int   a  ,  int   b  ,  int   c  ,  int   d  ,  int   e  ,  int   f  )  throws   ErrnoException  ,  FaultException  { 
switch  (  syscall  )  { 
case   SYS_null  : 
return   0  ; 
case   SYS_exit  : 
return   sys_exit  (  a  )  ; 
case   SYS_pause  : 
return   sys_pause  (  )  ; 
case   SYS_write  : 
return   sys_write  (  a  ,  b  ,  c  )  ; 
case   SYS_fstat  : 
return   sys_fstat  (  a  ,  b  )  ; 
case   SYS_sbrk  : 
return   sbrk  (  a  )  ; 
case   SYS_open  : 
return   sys_open  (  a  ,  b  ,  c  )  ; 
case   SYS_close  : 
return   sys_close  (  a  )  ; 
case   SYS_read  : 
return   sys_read  (  a  ,  b  ,  c  )  ; 
case   SYS_lseek  : 
return   sys_lseek  (  a  ,  b  ,  c  )  ; 
case   SYS_getpid  : 
return   sys_getpid  (  )  ; 
case   SYS_calljava  : 
return   sys_calljava  (  a  ,  b  ,  c  ,  d  )  ; 
case   SYS_gettimeofday  : 
return   sys_gettimeofday  (  a  ,  b  )  ; 
case   SYS_sleep  : 
return   sys_sleep  (  a  )  ; 
case   SYS_times  : 
return   sys_times  (  a  )  ; 
case   SYS_getpagesize  : 
return   sys_getpagesize  (  )  ; 
case   SYS_fcntl  : 
return   sys_fcntl  (  a  ,  b  ,  c  )  ; 
case   SYS_sysconf  : 
return   sys_sysconf  (  a  )  ; 
case   SYS_memcpy  : 
memcpy  (  a  ,  b  ,  c  )  ; 
return   a  ; 
case   SYS_memset  : 
memset  (  a  ,  b  ,  c  )  ; 
return   a  ; 
case   SYS_kill  : 
case   SYS_fork  : 
case   SYS_pipe  : 
case   SYS_dup2  : 
case   SYS_waitpid  : 
case   SYS_stat  : 
case   SYS_mkdir  : 
case   SYS_getcwd  : 
case   SYS_chdir  : 
if  (  STDERR_DIAG  )  System  .  err  .  println  (  "Attempted to use a UnixRuntime syscall in Runtime ("  +  syscall  +  ")"  )  ; 
return  -  ENOSYS  ; 
default  : 
if  (  STDERR_DIAG  )  System  .  err  .  println  (  "Attempted to use unknown syscall: "  +  syscall  )  ; 
return  -  ENOSYS  ; 
} 
} 

public   int   xmalloc  (  int   size  )  { 
int   p  =  malloc  (  size  )  ; 
if  (  p  ==  0  )  throw   new   RuntimeException  (  "malloc() failed"  )  ; 
return   p  ; 
} 

public   int   xrealloc  (  int   addr  ,  int   newsize  )  { 
int   p  =  realloc  (  addr  ,  newsize  )  ; 
if  (  p  ==  0  )  throw   new   RuntimeException  (  "realloc() failed"  )  ; 
return   p  ; 
} 

public   int   realloc  (  int   addr  ,  int   newsize  )  { 
try  { 
return   call  (  "realloc"  ,  addr  ,  newsize  )  ; 
}  catch  (  CallException   e  )  { 
return   0  ; 
} 
} 

public   int   malloc  (  int   size  )  { 
try  { 
return   call  (  "malloc"  ,  size  )  ; 
}  catch  (  CallException   e  )  { 
return   0  ; 
} 
} 

public   void   free  (  int   p  )  { 
try  { 
if  (  p  !=  0  )  call  (  "free"  ,  p  )  ; 
}  catch  (  CallException   e  )  { 
} 
} 


public   int   strdup  (  String   s  )  { 
byte  [  ]  a  ; 
if  (  s  ==  null  )  s  =  "(null)"  ; 
byte  [  ]  a2  =  getBytes  (  s  )  ; 
a  =  new   byte  [  a2  .  length  +  1  ]  ; 
System  .  arraycopy  (  a2  ,  0  ,  a  ,  0  ,  a2  .  length  )  ; 
int   addr  =  malloc  (  a  .  length  )  ; 
if  (  addr  ==  0  )  return   0  ; 
try  { 
copyout  (  a  ,  addr  ,  a  .  length  )  ; 
}  catch  (  FaultException   e  )  { 
free  (  addr  )  ; 
return   0  ; 
} 
return   addr  ; 
} 


public   final   String   cstring  (  int   addr  )  throws   ReadFaultException  { 
StringBuffer   sb  =  new   StringBuffer  (  )  ; 
for  (  ;  ;  )  { 
int   word  =  memRead  (  addr  &  ~  3  )  ; 
switch  (  addr  &  3  )  { 
case   0  : 
if  (  (  (  word  >  >  >  24  )  &  0xff  )  ==  0  )  return   sb  .  toString  (  )  ; 
sb  .  append  (  (  char  )  (  (  word  >  >  >  24  )  &  0xff  )  )  ; 
addr  ++  ; 
case   1  : 
if  (  (  (  word  >  >  >  16  )  &  0xff  )  ==  0  )  return   sb  .  toString  (  )  ; 
sb  .  append  (  (  char  )  (  (  word  >  >  >  16  )  &  0xff  )  )  ; 
addr  ++  ; 
case   2  : 
if  (  (  (  word  >  >  >  8  )  &  0xff  )  ==  0  )  return   sb  .  toString  (  )  ; 
sb  .  append  (  (  char  )  (  (  word  >  >  >  8  )  &  0xff  )  )  ; 
addr  ++  ; 
case   3  : 
if  (  (  (  word  >  >  >  0  )  &  0xff  )  ==  0  )  return   sb  .  toString  (  )  ; 
sb  .  append  (  (  char  )  (  (  word  >  >  >  0  )  &  0xff  )  )  ; 
addr  ++  ; 
} 
} 
} 


public   abstract   static   class   FD  { 

private   int   refCount  =  1  ; 


public   int   read  (  byte  [  ]  a  ,  int   off  ,  int   length  )  throws   ErrnoException  { 
throw   new   ErrnoException  (  EBADFD  )  ; 
} 


public   int   write  (  byte  [  ]  a  ,  int   off  ,  int   length  )  throws   ErrnoException  { 
throw   new   ErrnoException  (  EBADFD  )  ; 
} 


public   int   seek  (  int   n  ,  int   whence  )  throws   ErrnoException  { 
return  -  1  ; 
} 

public   int   getdents  (  byte  [  ]  a  ,  int   off  ,  int   length  )  throws   ErrnoException  { 
throw   new   ErrnoException  (  EBADFD  )  ; 
} 



Seekable   seekable  (  )  { 
return   null  ; 
} 

private   FStat   cachedFStat  =  null  ; 

public   final   FStat   fstat  (  )  { 
if  (  cachedFStat  ==  null  )  cachedFStat  =  _fstat  (  )  ; 
return   cachedFStat  ; 
} 

protected   abstract   FStat   _fstat  (  )  ; 

public   abstract   int   flags  (  )  ; 


public   final   void   close  (  )  { 
if  (  --  refCount  ==  0  )  _close  (  )  ; 
} 

protected   void   _close  (  )  { 
} 

FD   dup  (  )  { 
refCount  ++  ; 
return   this  ; 
} 
} 


public   abstract   static   class   SeekableFD   extends   FD  { 

private   final   int   flags  ; 

private   final   Seekable   data  ; 

SeekableFD  (  Seekable   data  ,  int   flags  )  { 
this  .  data  =  data  ; 
this  .  flags  =  flags  ; 
} 

protected   abstract   FStat   _fstat  (  )  ; 

public   int   flags  (  )  { 
return   flags  ; 
} 

Seekable   seekable  (  )  { 
return   data  ; 
} 

public   int   seek  (  int   n  ,  int   whence  )  throws   ErrnoException  { 
try  { 
switch  (  whence  )  { 
case   SEEK_SET  : 
break  ; 
case   SEEK_CUR  : 
n  +=  data  .  pos  (  )  ; 
break  ; 
case   SEEK_END  : 
n  +=  data  .  length  (  )  ; 
break  ; 
default  : 
return  -  1  ; 
} 
data  .  seek  (  n  )  ; 
return   n  ; 
}  catch  (  IOException   e  )  { 
throw   new   ErrnoException  (  ESPIPE  )  ; 
} 
} 

public   int   write  (  byte  [  ]  a  ,  int   off  ,  int   length  )  throws   ErrnoException  { 
if  (  (  flags  &  3  )  ==  RD_ONLY  )  throw   new   ErrnoException  (  EBADFD  )  ; 
if  (  (  flags  &  O_APPEND  )  !=  0  )  seek  (  0  ,  SEEK_END  )  ; 
try  { 
return   data  .  write  (  a  ,  off  ,  length  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   ErrnoException  (  EIO  )  ; 
} 
} 

public   int   read  (  byte  [  ]  a  ,  int   off  ,  int   length  )  throws   ErrnoException  { 
if  (  (  flags  &  3  )  ==  WR_ONLY  )  throw   new   ErrnoException  (  EBADFD  )  ; 
try  { 
int   n  =  data  .  read  (  a  ,  off  ,  length  )  ; 
return   n  <  0  ?  0  :  n  ; 
}  catch  (  IOException   e  )  { 
throw   new   ErrnoException  (  EIO  )  ; 
} 
} 

protected   void   _close  (  )  { 
try  { 
data  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 

public   static   class   InputOutputStreamFD   extends   FD  { 

private   final   InputStream   is  ; 

private   final   OutputStream   os  ; 

public   InputOutputStreamFD  (  InputStream   is  )  { 
this  (  is  ,  null  )  ; 
} 

public   InputOutputStreamFD  (  OutputStream   os  )  { 
this  (  null  ,  os  )  ; 
} 

public   InputOutputStreamFD  (  InputStream   is  ,  OutputStream   os  )  { 
this  .  is  =  is  ; 
this  .  os  =  os  ; 
if  (  is  ==  null  &&  os  ==  null  )  throw   new   IllegalArgumentException  (  "at least one stream must be supplied"  )  ; 
} 

public   int   flags  (  )  { 
if  (  is  !=  null  &&  os  !=  null  )  return   O_RDWR  ; 
if  (  is  !=  null  )  return   O_RDONLY  ; 
if  (  os  !=  null  )  return   O_WRONLY  ; 
throw   new   Error  (  "should never happen"  )  ; 
} 

public   void   _close  (  )  { 
if  (  is  !=  null  )  try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
if  (  os  !=  null  )  try  { 
os  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 

public   int   read  (  byte  [  ]  a  ,  int   off  ,  int   length  )  throws   ErrnoException  { 
if  (  is  ==  null  )  return   super  .  read  (  a  ,  off  ,  length  )  ; 
try  { 
int   n  =  is  .  read  (  a  ,  off  ,  length  )  ; 
return   n  <  0  ?  0  :  n  ; 
}  catch  (  IOException   e  )  { 
throw   new   ErrnoException  (  EIO  )  ; 
} 
} 

public   int   write  (  byte  [  ]  a  ,  int   off  ,  int   length  )  throws   ErrnoException  { 
if  (  os  ==  null  )  return   super  .  write  (  a  ,  off  ,  length  )  ; 
try  { 
os  .  write  (  a  ,  off  ,  length  )  ; 
return   length  ; 
}  catch  (  IOException   e  )  { 
throw   new   ErrnoException  (  EIO  )  ; 
} 
} 

public   FStat   _fstat  (  )  { 
return   new   SocketFStat  (  )  ; 
} 
} 

static   class   TerminalFD   extends   InputOutputStreamFD  { 

public   TerminalFD  (  InputStream   is  )  { 
this  (  is  ,  null  )  ; 
} 

public   TerminalFD  (  OutputStream   os  )  { 
this  (  null  ,  os  )  ; 
} 

public   TerminalFD  (  InputStream   is  ,  OutputStream   os  )  { 
super  (  is  ,  os  )  ; 
} 

public   void   _close  (  )  { 
} 

public   FStat   _fstat  (  )  { 
return   new   SocketFStat  (  )  { 

public   int   type  (  )  { 
return   S_IFCHR  ; 
} 

public   int   mode  (  )  { 
return   0600  ; 
} 
}  ; 
} 
} 

static   class   Win32ConsoleIS   extends   InputStream  { 

private   int   pushedBack  =  -  1  ; 

private   final   InputStream   parent  ; 

public   Win32ConsoleIS  (  InputStream   parent  )  { 
this  .  parent  =  parent  ; 
} 

public   int   read  (  )  throws   IOException  { 
if  (  pushedBack  !=  -  1  )  { 
int   c  =  pushedBack  ; 
pushedBack  =  -  1  ; 
return   c  ; 
} 
int   c  =  parent  .  read  (  )  ; 
if  (  c  ==  '\r'  &&  (  c  =  parent  .  read  (  )  )  !=  '\n'  )  { 
pushedBack  =  c  ; 
return  '\r'  ; 
} 
return   c  ; 
} 

public   int   read  (  byte  [  ]  buf  ,  int   pos  ,  int   len  )  throws   IOException  { 
boolean   pb  =  false  ; 
if  (  pushedBack  !=  -  1  &&  len  >  0  )  { 
buf  [  0  ]  =  (  byte  )  pushedBack  ; 
pushedBack  =  -  1  ; 
pos  ++  ; 
len  --  ; 
pb  =  true  ; 
} 
int   n  =  parent  .  read  (  buf  ,  pos  ,  len  )  ; 
if  (  n  ==  -  1  )  return  -  1  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
if  (  buf  [  pos  +  i  ]  ==  '\r'  )  { 
if  (  i  ==  n  -  1  )  { 
int   c  =  parent  .  read  (  )  ; 
if  (  c  ==  '\n'  )  buf  [  pos  +  i  ]  =  '\n'  ;  else   pushedBack  =  c  ; 
}  else   if  (  buf  [  pos  +  i  +  1  ]  ==  '\n'  )  { 
System  .  arraycopy  (  buf  ,  pos  +  i  +  1  ,  buf  ,  pos  +  i  ,  len  -  i  -  1  )  ; 
n  --  ; 
} 
} 
} 
return   n  +  (  pb  ?  1  :  0  )  ; 
} 
} 

public   abstract   static   class   FStat  { 

public   static   final   int   S_IFIFO  =  0010000  ; 

public   static   final   int   S_IFCHR  =  0020000  ; 

public   static   final   int   S_IFDIR  =  0040000  ; 

public   static   final   int   S_IFREG  =  0100000  ; 

public   static   final   int   S_IFSOCK  =  0140000  ; 

public   int   mode  (  )  { 
return   0  ; 
} 

public   int   nlink  (  )  { 
return   0  ; 
} 

public   int   uid  (  )  { 
return   0  ; 
} 

public   int   gid  (  )  { 
return   0  ; 
} 

public   int   size  (  )  { 
return   0  ; 
} 

public   int   atime  (  )  { 
return   0  ; 
} 

public   int   mtime  (  )  { 
return   0  ; 
} 

public   int   ctime  (  )  { 
return   0  ; 
} 

public   int   blksize  (  )  { 
return   512  ; 
} 

public   int   blocks  (  )  { 
return  (  size  (  )  +  blksize  (  )  -  1  )  /  blksize  (  )  ; 
} 

public   abstract   int   dev  (  )  ; 

public   abstract   int   type  (  )  ; 

public   abstract   int   inode  (  )  ; 
} 

public   static   class   SocketFStat   extends   FStat  { 

public   int   dev  (  )  { 
return  -  1  ; 
} 

public   int   type  (  )  { 
return   S_IFSOCK  ; 
} 

public   int   inode  (  )  { 
return   hashCode  (  )  &  0x7fff  ; 
} 
} 

static   class   HostFStat   extends   FStat  { 

private   final   File   f  ; 

private   final   boolean   executable  ; 

public   HostFStat  (  File   f  )  { 
this  (  f  ,  false  )  ; 
} 

public   HostFStat  (  File   f  ,  boolean   executable  )  { 
this  .  f  =  f  ; 
this  .  executable  =  executable  ; 
} 

public   int   dev  (  )  { 
return   1  ; 
} 

public   int   inode  (  )  { 
return   f  .  getAbsolutePath  (  )  .  hashCode  (  )  &  0x7fff  ; 
} 

public   int   type  (  )  { 
return   f  .  isDirectory  (  )  ?  S_IFDIR  :  S_IFREG  ; 
} 

public   int   nlink  (  )  { 
return   1  ; 
} 

public   int   mode  (  )  { 
int   mode  =  0  ; 
boolean   canread  =  f  .  canRead  (  )  ; 
if  (  canread  &&  (  executable  ||  f  .  isDirectory  (  )  )  )  mode  |=  0111  ; 
if  (  canread  )  mode  |=  0444  ; 
if  (  f  .  canWrite  (  )  )  mode  |=  0222  ; 
return   mode  ; 
} 

public   int   size  (  )  { 
return  (  int  )  f  .  length  (  )  ; 
} 

public   int   mtime  (  )  { 
return  (  int  )  (  f  .  lastModified  (  )  /  1000  )  ; 
} 
} 

public   static   class   ReadFaultException   extends   FaultException  { 

public   ReadFaultException  (  int   addr  )  { 
super  (  addr  )  ; 
} 
} 

public   static   class   WriteFaultException   extends   FaultException  { 

public   WriteFaultException  (  int   addr  )  { 
super  (  addr  )  ; 
} 
} 

public   static   class   FaultException   extends   ExecutionException  { 

public   final   int   addr  ; 

public   final   RuntimeException   cause  ; 

public   FaultException  (  int   addr  )  { 
super  (  "fault at: "  +  toHex  (  addr  )  )  ; 
this  .  addr  =  addr  ; 
cause  =  null  ; 
} 

public   FaultException  (  RuntimeException   e  )  { 
super  (  e  .  toString  (  )  )  ; 
addr  =  -  1  ; 
cause  =  e  ; 
} 
} 

public   static   class   ExecutionException   extends   Exception  { 

private   String   message  =  "(null)"  ; 

private   String   location  =  "(unknown)"  ; 

public   ExecutionException  (  )  { 
} 

public   ExecutionException  (  String   s  )  { 
if  (  s  !=  null  )  message  =  s  ; 
} 

void   setLocation  (  String   s  )  { 
location  =  s  ==  null  ?  "(unknown)"  :  s  ; 
} 

public   final   String   getMessage  (  )  { 
return   message  +  " at "  +  location  ; 
} 
} 

public   static   class   CallException   extends   Exception  { 

public   CallException  (  String   s  )  { 
super  (  s  )  ; 
} 
} 

protected   static   class   ErrnoException   extends   Exception  { 

public   int   errno  ; 

public   ErrnoException  (  int   errno  )  { 
super  (  "Errno: "  +  errno  )  ; 
this  .  errno  =  errno  ; 
} 
} 

protected   static   class   CPUState  { 

public   CPUState  (  )  { 
} 

public   int  [  ]  r  =  new   int  [  32  ]  ; 

public   int  [  ]  f  =  new   int  [  32  ]  ; 

public   int   hi  ,  lo  ; 

public   int   fcsr  ; 

public   int   pc  ; 

public   CPUState   dup  (  )  { 
CPUState   c  =  new   CPUState  (  )  ; 
c  .  hi  =  hi  ; 
c  .  lo  =  lo  ; 
c  .  fcsr  =  fcsr  ; 
c  .  pc  =  pc  ; 
for  (  int   i  =  0  ;  i  <  32  ;  i  ++  )  { 
c  .  r  [  i  ]  =  r  [  i  ]  ; 
c  .  f  [  i  ]  =  f  [  i  ]  ; 
} 
return   c  ; 
} 
} 

public   static   class   SecurityManager  { 

public   boolean   allowRead  (  File   f  )  { 
return   true  ; 
} 

public   boolean   allowWrite  (  File   f  )  { 
return   true  ; 
} 

public   boolean   allowStat  (  File   f  )  { 
return   true  ; 
} 

public   boolean   allowUnlink  (  File   f  )  { 
return   true  ; 
} 
} 

protected   final   void   nullPointerCheck  (  int   addr  )  throws   ExecutionException  { 
if  (  addr  <  65536  )  throw   new   ExecutionException  (  "Attempted to dereference a null pointer "  +  toHex  (  addr  )  )  ; 
} 

byte  [  ]  byteBuf  (  int   size  )  { 
if  (  _byteBuf  ==  null  )  _byteBuf  =  new   byte  [  size  ]  ;  else   if  (  _byteBuf  .  length  <  size  )  _byteBuf  =  new   byte  [  min  (  max  (  _byteBuf  .  length  *  2  ,  size  )  ,  MAX_CHUNK  )  ]  ; 
return   _byteBuf  ; 
} 


protected   static   final   int  [  ]  decodeData  (  String   s  ,  int   words  )  { 
if  (  s  .  length  (  )  %  8  !=  0  )  throw   new   IllegalArgumentException  (  "string length must be a multiple of 8"  )  ; 
if  (  (  s  .  length  (  )  /  8  )  *  7  <  words  *  4  )  throw   new   IllegalArgumentException  (  "string isn't big enough"  )  ; 
int  [  ]  buf  =  new   int  [  words  ]  ; 
int   prev  =  0  ,  left  =  0  ; 
for  (  int   i  =  0  ,  n  =  0  ;  n  <  words  ;  i  +=  8  )  { 
long   l  =  0  ; 
for  (  int   j  =  0  ;  j  <  8  ;  j  ++  )  { 
l  <<=  7  ; 
l  |=  s  .  charAt  (  i  +  j  )  &  0x7f  ; 
} 
if  (  left  >  0  )  buf  [  n  ++  ]  =  prev  |  (  int  )  (  l  >  >  >  (  56  -  left  )  )  ; 
if  (  n  <  words  )  buf  [  n  ++  ]  =  (  int  )  (  l  >  >  >  (  24  -  left  )  )  ; 
left  =  (  left  +  8  )  &  0x1f  ; 
prev  =  (  int  )  (  l  <<  left  )  ; 
} 
return   buf  ; 
} 

static   byte  [  ]  getBytes  (  String   s  )  { 
try  { 
return   s  .  getBytes  (  "ISO-8859-1"  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   null  ; 
} 
} 

static   byte  [  ]  getNullTerminatedBytes  (  String   s  )  { 
byte  [  ]  buf1  =  getBytes  (  s  )  ; 
byte  [  ]  buf2  =  new   byte  [  buf1  .  length  +  1  ]  ; 
System  .  arraycopy  (  buf1  ,  0  ,  buf2  ,  0  ,  buf1  .  length  )  ; 
return   buf2  ; 
} 

static   final   String   toHex  (  int   n  )  { 
return  "0x"  +  Long  .  toString  (  n  &  0xffffffffL  ,  16  )  ; 
} 

static   final   int   min  (  int   a  ,  int   b  )  { 
return   a  <  b  ?  a  :  b  ; 
} 

static   final   int   max  (  int   a  ,  int   b  )  { 
return   a  >  b  ?  a  :  b  ; 
} 
} 

