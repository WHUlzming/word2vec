package   org  .  monet  .  reportservice  .  library  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   org  .  apache  .  log4j  .  Logger  ; 

public   class   Zipper  { 

private   static   final   Integer   BUFFER_SIZE  =  8192  ; 

private   static   Logger   log  =  Logger  .  getRootLogger  (  )  ; 

public   static   Boolean   decompress  (  String   sFilename  ,  String   sDestination  )  { 
log  .  info  (  "PentahoReport() file: "  +  sFilename  +  " destination:"  +  sDestination  )  ; 
BufferedOutputStream   oDestination  ; 
FileInputStream   oOrigin  ; 
ZipInputStream   oInput  ; 
try  { 
oDestination  =  null  ; 
oOrigin  =  new   FileInputStream  (  new   File  (  sFilename  )  )  ; 
oInput  =  new   ZipInputStream  (  new   BufferedInputStream  (  oOrigin  )  )  ; 
int   iCount  ; 
byte   aData  [  ]  =  new   byte  [  BUFFER_SIZE  ]  ; 
ZipEntry   oEntry  ; 
while  (  (  oEntry  =  oInput  .  getNextEntry  (  )  )  !=  null  )  { 
if  (  oEntry  .  isDirectory  (  )  )  new   File  (  sDestination  +  File  .  separator  +  oEntry  .  getName  (  )  )  .  mkdirs  (  )  ;  else  { 
String   sDestFN  =  sDestination  +  "/"  +  oEntry  .  getName  (  )  ; 
String   dir  =  sDestFN  .  substring  (  0  ,  sDestFN  .  lastIndexOf  (  '/'  )  )  ; 
if  (  !  (  new   File  (  dir  )  )  .  exists  (  )  )  { 
new   File  (  dir  )  .  mkdirs  (  )  ; 
} 
FileOutputStream   oOutput  =  new   FileOutputStream  (  sDestFN  )  ; 
oDestination  =  new   BufferedOutputStream  (  oOutput  ,  BUFFER_SIZE  )  ; 
while  (  (  iCount  =  oInput  .  read  (  aData  ,  0  ,  BUFFER_SIZE  )  )  !=  -  1  )  { 
oDestination  .  write  (  aData  ,  0  ,  iCount  )  ; 
} 
oDestination  .  flush  (  )  ; 
oDestination  .  close  (  )  ; 
} 
} 
oInput  .  close  (  )  ; 
}  catch  (  Exception   oException  )  { 
log  .  error  (  oException  .  getMessage  (  )  )  ; 
oException  .  printStackTrace  (  )  ; 
return   false  ; 
} 
return   true  ; 
} 

public   static   Boolean   compress  (  String   sSourceDir  ,  ArrayList  <  String  >  aFiles  ,  String   sDestinationFilename  )  { 
log  .  info  (  "PentahoReport() sourceDir: "  +  sSourceDir  +  " destination:"  +  sDestinationFilename  )  ; 
BufferedInputStream   oOrigin  =  null  ; 
FileOutputStream   oDestination  ; 
ZipOutputStream   oOutput  ; 
Iterator  <  String  >  oIterator  ; 
byte  [  ]  aData  ; 
try  { 
oDestination  =  new   FileOutputStream  (  sDestinationFilename  )  ; 
oOutput  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  oDestination  )  )  ; 
aData  =  new   byte  [  BUFFER_SIZE  ]  ; 
oIterator  =  aFiles  .  iterator  (  )  ; 
while  (  oIterator  .  hasNext  (  )  )  { 
String   sFilename  =  (  String  )  oIterator  .  next  (  )  ; 
FileInputStream   fisInput  =  new   FileInputStream  (  sSourceDir  +  File  .  separator  +  sFilename  )  ; 
oOrigin  =  new   BufferedInputStream  (  fisInput  ,  BUFFER_SIZE  )  ; 
ZipEntry   oEntry  =  new   ZipEntry  (  sFilename  .  replace  (  '\\'  ,  '/'  )  )  ; 
oOutput  .  putNextEntry  (  oEntry  )  ; 
int   iCount  ; 
while  (  (  iCount  =  oOrigin  .  read  (  aData  ,  0  ,  BUFFER_SIZE  )  )  !=  -  1  )  { 
oOutput  .  write  (  aData  ,  0  ,  iCount  )  ; 
} 
oOrigin  .  close  (  )  ; 
} 
oOutput  .  close  (  )  ; 
}  catch  (  Exception   oException  )  { 
log  .  error  (  oException  .  getMessage  (  )  )  ; 
oException  .  printStackTrace  (  )  ; 
return   false  ; 
} 
return   true  ; 
} 
} 

