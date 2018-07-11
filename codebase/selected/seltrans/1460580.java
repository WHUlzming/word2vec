package   org  .  hibernate  .  bytecode  .  buildtime  ; 

import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   java  .  util  .  zip  .  CRC32  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  DataInputStream  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   org  .  hibernate  .  bytecode  .  util  .  ByteCodeHelper  ; 
import   org  .  hibernate  .  bytecode  .  util  .  ClassDescriptor  ; 
import   org  .  hibernate  .  bytecode  .  util  .  FieldFilter  ; 
import   org  .  hibernate  .  bytecode  .  ClassTransformer  ; 






public   abstract   class   AbstractInstrumenter   implements   Instrumenter  { 

private   static   final   int   ZIP_MAGIC  =  0x504B0304  ; 

private   static   final   int   CLASS_MAGIC  =  0xCAFEBABE  ; 

protected   final   Logger   logger  ; 

protected   final   Options   options  ; 







public   AbstractInstrumenter  (  Logger   logger  ,  Options   options  )  { 
this  .  logger  =  logger  ; 
this  .  options  =  options  ; 
} 










protected   abstract   ClassDescriptor   getClassDescriptor  (  byte  [  ]  byecode  )  throws   Exception  ; 










protected   abstract   ClassTransformer   getClassTransformer  (  ClassDescriptor   descriptor  ,  Set   classNames  )  ; 







public   void   execute  (  Set   files  )  { 
Set   classNames  =  new   HashSet  (  )  ; 
if  (  options  .  performExtendedInstrumentation  (  )  )  { 
logger  .  debug  (  "collecting class names for extended instrumentation determination"  )  ; 
try  { 
Iterator   itr  =  files  .  iterator  (  )  ; 
while  (  itr  .  hasNext  (  )  )  { 
final   File   file  =  (  File  )  itr  .  next  (  )  ; 
collectClassNames  (  file  ,  classNames  )  ; 
} 
}  catch  (  ExecutionException   ee  )  { 
throw   ee  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExecutionException  (  e  )  ; 
} 
} 
logger  .  info  (  "starting instrumentation"  )  ; 
try  { 
Iterator   itr  =  files  .  iterator  (  )  ; 
while  (  itr  .  hasNext  (  )  )  { 
final   File   file  =  (  File  )  itr  .  next  (  )  ; 
processFile  (  file  ,  classNames  )  ; 
} 
}  catch  (  ExecutionException   ee  )  { 
throw   ee  ; 
}  catch  (  Exception   e  )  { 
throw   new   ExecutionException  (  e  )  ; 
} 
} 












private   void   collectClassNames  (  File   file  ,  final   Set   classNames  )  throws   Exception  { 
if  (  isClassFile  (  file  )  )  { 
byte  [  ]  bytes  =  ByteCodeHelper  .  readByteCode  (  file  )  ; 
ClassDescriptor   descriptor  =  getClassDescriptor  (  bytes  )  ; 
classNames  .  add  (  descriptor  .  getName  (  )  )  ; 
}  else   if  (  isJarFile  (  file  )  )  { 
ZipEntryHandler   collector  =  new   ZipEntryHandler  (  )  { 

public   void   handleEntry  (  ZipEntry   entry  ,  byte  [  ]  byteCode  )  throws   Exception  { 
if  (  !  entry  .  isDirectory  (  )  )  { 
DataInputStream   din  =  new   DataInputStream  (  new   ByteArrayInputStream  (  byteCode  )  )  ; 
if  (  din  .  readInt  (  )  ==  CLASS_MAGIC  )  { 
classNames  .  add  (  getClassDescriptor  (  byteCode  )  .  getName  (  )  )  ; 
} 
} 
} 
}  ; 
ZipFileProcessor   processor  =  new   ZipFileProcessor  (  collector  )  ; 
processor  .  process  (  file  )  ; 
} 
} 










protected   final   boolean   isClassFile  (  File   file  )  throws   IOException  { 
return   checkMagic  (  file  ,  CLASS_MAGIC  )  ; 
} 










protected   final   boolean   isJarFile  (  File   file  )  throws   IOException  { 
return   checkMagic  (  file  ,  ZIP_MAGIC  )  ; 
} 

protected   final   boolean   checkMagic  (  File   file  ,  long   magic  )  throws   IOException  { 
DataInputStream   in  =  new   DataInputStream  (  new   FileInputStream  (  file  )  )  ; 
try  { 
int   m  =  in  .  readInt  (  )  ; 
return   magic  ==  m  ; 
}  finally  { 
in  .  close  (  )  ; 
} 
} 












protected   void   processFile  (  File   file  ,  Set   classNames  )  throws   Exception  { 
if  (  isClassFile  (  file  )  )  { 
logger  .  debug  (  "processing class file : "  +  file  .  getAbsolutePath  (  )  )  ; 
processClassFile  (  file  ,  classNames  )  ; 
}  else   if  (  isJarFile  (  file  )  )  { 
logger  .  debug  (  "processing jar file : "  +  file  .  getAbsolutePath  (  )  )  ; 
processJarFile  (  file  ,  classNames  )  ; 
}  else  { 
logger  .  debug  (  "ignoring file : "  +  file  .  getAbsolutePath  (  )  )  ; 
} 
} 










protected   void   processClassFile  (  File   file  ,  Set   classNames  )  throws   Exception  { 
byte  [  ]  bytes  =  ByteCodeHelper  .  readByteCode  (  file  )  ; 
ClassDescriptor   descriptor  =  getClassDescriptor  (  bytes  )  ; 
ClassTransformer   transformer  =  getClassTransformer  (  descriptor  ,  classNames  )  ; 
if  (  transformer  ==  null  )  { 
logger  .  debug  (  "no trasformer for class file : "  +  file  .  getAbsolutePath  (  )  )  ; 
return  ; 
} 
logger  .  info  (  "processing class : "  +  descriptor  .  getName  (  )  +  ";  file = "  +  file  .  getAbsolutePath  (  )  )  ; 
byte  [  ]  transformedBytes  =  transformer  .  transform  (  getClass  (  )  .  getClassLoader  (  )  ,  descriptor  .  getName  (  )  ,  null  ,  null  ,  descriptor  .  getBytes  (  )  )  ; 
OutputStream   out  =  new   FileOutputStream  (  file  )  ; 
try  { 
out  .  write  (  transformedBytes  )  ; 
out  .  flush  (  )  ; 
}  finally  { 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   ignore  )  { 
} 
} 
} 










protected   void   processJarFile  (  final   File   file  ,  final   Set   classNames  )  throws   Exception  { 
File   tempFile  =  File  .  createTempFile  (  file  .  getName  (  )  ,  null  ,  new   File  (  file  .  getAbsoluteFile  (  )  .  getParent  (  )  )  )  ; 
try  { 
FileOutputStream   fout  =  new   FileOutputStream  (  tempFile  ,  false  )  ; 
try  { 
final   ZipOutputStream   out  =  new   ZipOutputStream  (  fout  )  ; 
ZipEntryHandler   transformer  =  new   ZipEntryHandler  (  )  { 

public   void   handleEntry  (  ZipEntry   entry  ,  byte  [  ]  byteCode  )  throws   Exception  { 
logger  .  debug  (  "starting zip entry : "  +  entry  .  toString  (  )  )  ; 
if  (  !  entry  .  isDirectory  (  )  )  { 
DataInputStream   din  =  new   DataInputStream  (  new   ByteArrayInputStream  (  byteCode  )  )  ; 
if  (  din  .  readInt  (  )  ==  CLASS_MAGIC  )  { 
ClassDescriptor   descriptor  =  getClassDescriptor  (  byteCode  )  ; 
ClassTransformer   transformer  =  getClassTransformer  (  descriptor  ,  classNames  )  ; 
if  (  transformer  ==  null  )  { 
logger  .  debug  (  "no transformer for zip entry :  "  +  entry  .  toString  (  )  )  ; 
}  else  { 
logger  .  info  (  "processing class : "  +  descriptor  .  getName  (  )  +  ";  entry = "  +  file  .  getAbsolutePath  (  )  )  ; 
byteCode  =  transformer  .  transform  (  getClass  (  )  .  getClassLoader  (  )  ,  descriptor  .  getName  (  )  ,  null  ,  null  ,  descriptor  .  getBytes  (  )  )  ; 
} 
}  else  { 
logger  .  debug  (  "ignoring zip entry : "  +  entry  .  toString  (  )  )  ; 
} 
} 
ZipEntry   outEntry  =  new   ZipEntry  (  entry  .  getName  (  )  )  ; 
outEntry  .  setMethod  (  entry  .  getMethod  (  )  )  ; 
outEntry  .  setComment  (  entry  .  getComment  (  )  )  ; 
outEntry  .  setSize  (  byteCode  .  length  )  ; 
if  (  outEntry  .  getMethod  (  )  ==  ZipEntry  .  STORED  )  { 
CRC32   crc  =  new   CRC32  (  )  ; 
crc  .  update  (  byteCode  )  ; 
outEntry  .  setCrc  (  crc  .  getValue  (  )  )  ; 
outEntry  .  setCompressedSize  (  byteCode  .  length  )  ; 
} 
out  .  putNextEntry  (  outEntry  )  ; 
out  .  write  (  byteCode  )  ; 
out  .  closeEntry  (  )  ; 
} 
}  ; 
ZipFileProcessor   processor  =  new   ZipFileProcessor  (  transformer  )  ; 
processor  .  process  (  file  )  ; 
out  .  close  (  )  ; 
}  finally  { 
fout  .  close  (  )  ; 
} 
if  (  file  .  delete  (  )  )  { 
File   newFile  =  new   File  (  tempFile  .  getAbsolutePath  (  )  )  ; 
if  (  !  newFile  .  renameTo  (  file  )  )  { 
throw   new   IOException  (  "can not rename "  +  tempFile  +  " to "  +  file  )  ; 
} 
}  else  { 
throw   new   IOException  (  "can not delete "  +  file  )  ; 
} 
}  finally  { 
if  (  !  tempFile  .  delete  (  )  )  { 
logger  .  info  (  "Unable to cleanup temporary jar file : "  +  tempFile  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 




protected   class   CustomFieldFilter   implements   FieldFilter  { 

private   final   ClassDescriptor   descriptor  ; 

private   final   Set   classNames  ; 

public   CustomFieldFilter  (  ClassDescriptor   descriptor  ,  Set   classNames  )  { 
this  .  descriptor  =  descriptor  ; 
this  .  classNames  =  classNames  ; 
} 

public   boolean   shouldInstrumentField  (  String   className  ,  String   fieldName  )  { 
if  (  descriptor  .  getName  (  )  .  equals  (  className  )  )  { 
logger  .  trace  (  "accepting transformation of field ["  +  className  +  "."  +  fieldName  +  "]"  )  ; 
return   true  ; 
}  else  { 
logger  .  trace  (  "rejecting transformation of field ["  +  className  +  "."  +  fieldName  +  "]"  )  ; 
return   false  ; 
} 
} 

public   boolean   shouldTransformFieldAccess  (  String   transformingClassName  ,  String   fieldOwnerClassName  ,  String   fieldName  )  { 
if  (  descriptor  .  getName  (  )  .  equals  (  fieldOwnerClassName  )  )  { 
logger  .  trace  (  "accepting transformation of field access ["  +  fieldOwnerClassName  +  "."  +  fieldName  +  "]"  )  ; 
return   true  ; 
}  else   if  (  options  .  performExtendedInstrumentation  (  )  &&  classNames  .  contains  (  fieldOwnerClassName  )  )  { 
logger  .  trace  (  "accepting extended transformation of field access ["  +  fieldOwnerClassName  +  "."  +  fieldName  +  "]"  )  ; 
return   true  ; 
}  else  { 
logger  .  trace  (  "rejecting transformation of field access ["  +  fieldOwnerClassName  +  "."  +  fieldName  +  "]; caller = "  +  transformingClassName  )  ; 
return   false  ; 
} 
} 
} 




private   static   interface   ZipEntryHandler  { 









public   void   handleEntry  (  ZipEntry   entry  ,  byte  [  ]  byteCode  )  throws   Exception  ; 
} 




private   static   class   ZipFileProcessor  { 

private   final   ZipEntryHandler   entryHandler  ; 

public   ZipFileProcessor  (  ZipEntryHandler   entryHandler  )  { 
this  .  entryHandler  =  entryHandler  ; 
} 

public   void   process  (  File   file  )  throws   Exception  { 
ZipInputStream   zip  =  new   ZipInputStream  (  new   FileInputStream  (  file  )  )  ; 
try  { 
ZipEntry   entry  ; 
while  (  (  entry  =  zip  .  getNextEntry  (  )  )  !=  null  )  { 
byte   bytes  [  ]  =  ByteCodeHelper  .  readByteCode  (  zip  )  ; 
entryHandler  .  handleEntry  (  entry  ,  bytes  )  ; 
zip  .  closeEntry  (  )  ; 
} 
}  finally  { 
zip  .  close  (  )  ; 
} 
} 
} 
} 

