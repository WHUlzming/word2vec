package   org  .  t2framework  .  confeito  .  apache  .  commons  .  fileupload  .  disk  ; 

import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  ObjectInputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  util  .  Map  ; 
import   org  .  t2framework  .  confeito  .  apache  .  commons  .  fileupload  .  FileItem  ; 
import   org  .  t2framework  .  confeito  .  apache  .  commons  .  fileupload  .  FileItemHeaders  ; 
import   org  .  t2framework  .  confeito  .  apache  .  commons  .  fileupload  .  FileItemHeadersSupport  ; 
import   org  .  t2framework  .  confeito  .  apache  .  commons  .  fileupload  .  FileUploadException  ; 
import   org  .  t2framework  .  confeito  .  apache  .  commons  .  fileupload  .  ParameterParser  ; 
import   org  .  t2framework  .  confeito  .  apache  .  commons  .  io  .  IOUtils  ; 
import   org  .  t2framework  .  confeito  .  apache  .  commons  .  io  .  output  .  DeferredFileOutputStream  ; 






































public   class   DiskFileItem   implements   FileItem  ,  FileItemHeadersSupport  { 




private   static   final   long   serialVersionUID  =  2237570099615271025L  ; 






public   static   final   String   DEFAULT_CHARSET  =  "ISO-8859-1"  ; 




private   static   final   String   UID  =  new   java  .  rmi  .  server  .  UID  (  )  .  toString  (  )  .  replace  (  ':'  ,  '_'  )  .  replace  (  '-'  ,  '_'  )  ; 




private   static   int   counter  =  0  ; 




private   String   fieldName  ; 





private   String   contentType  ; 




private   boolean   isFormField  ; 




private   String   fileName  ; 





private   long   size  =  -  1  ; 




private   int   sizeThreshold  ; 




private   File   repository  ; 




private   byte  [  ]  cachedContent  ; 




private   transient   DeferredFileOutputStream   dfos  ; 




private   transient   File   tempFile  ; 




private   File   dfosFile  ; 




private   FileItemHeaders   headers  ; 






















public   DiskFileItem  (  String   fieldName  ,  String   contentType  ,  boolean   isFormField  ,  String   fileName  ,  int   sizeThreshold  ,  File   repository  )  { 
this  .  fieldName  =  fieldName  ; 
this  .  contentType  =  contentType  ; 
this  .  isFormField  =  isFormField  ; 
this  .  fileName  =  fileName  ; 
this  .  sizeThreshold  =  sizeThreshold  ; 
this  .  repository  =  repository  ; 
} 











public   InputStream   getInputStream  (  )  throws   IOException  { 
if  (  !  isInMemory  (  )  )  { 
return   new   FileInputStream  (  dfos  .  getFile  (  )  )  ; 
} 
if  (  cachedContent  ==  null  )  { 
cachedContent  =  dfos  .  getData  (  )  ; 
} 
return   new   ByteArrayInputStream  (  cachedContent  )  ; 
} 








public   String   getContentType  (  )  { 
return   contentType  ; 
} 








public   String   getCharSet  (  )  { 
ParameterParser   parser  =  new   ParameterParser  (  )  ; 
parser  .  setLowerCaseNames  (  true  )  ; 
Map  <  String  ,  String  >  params  =  parser  .  parse  (  getContentType  (  )  ,  ';'  )  ; 
return   params  .  get  (  "charset"  )  ; 
} 






public   String   getName  (  )  { 
return   fileName  ; 
} 








public   boolean   isInMemory  (  )  { 
if  (  cachedContent  !=  null  )  { 
return   true  ; 
} 
return   dfos  .  isInMemory  (  )  ; 
} 






public   long   getSize  (  )  { 
if  (  size  >=  0  )  { 
return   size  ; 
}  else   if  (  cachedContent  !=  null  )  { 
return   cachedContent  .  length  ; 
}  else   if  (  dfos  .  isInMemory  (  )  )  { 
return   dfos  .  getData  (  )  .  length  ; 
}  else  { 
return   dfos  .  getFile  (  )  .  length  (  )  ; 
} 
} 








public   byte  [  ]  get  (  )  { 
if  (  isInMemory  (  )  )  { 
if  (  cachedContent  ==  null  )  { 
cachedContent  =  dfos  .  getData  (  )  ; 
} 
return   cachedContent  ; 
} 
byte  [  ]  fileData  =  new   byte  [  (  int  )  getSize  (  )  ]  ; 
FileInputStream   fis  =  null  ; 
try  { 
fis  =  new   FileInputStream  (  dfos  .  getFile  (  )  )  ; 
fis  .  read  (  fileData  )  ; 
}  catch  (  IOException   e  )  { 
fileData  =  null  ; 
}  finally  { 
if  (  fis  !=  null  )  { 
try  { 
fis  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
return   fileData  ; 
} 














public   String   getString  (  final   String   charset  )  throws   UnsupportedEncodingException  { 
return   new   String  (  get  (  )  ,  charset  )  ; 
} 










public   String   getString  (  )  { 
byte  [  ]  rawdata  =  get  (  )  ; 
String   charset  =  getCharSet  (  )  ; 
if  (  charset  ==  null  )  { 
charset  =  DEFAULT_CHARSET  ; 
} 
try  { 
return   new   String  (  rawdata  ,  charset  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
return   new   String  (  rawdata  )  ; 
} 
} 























public   void   write  (  File   file  )  throws   Exception  { 
if  (  isInMemory  (  )  )  { 
FileOutputStream   fout  =  null  ; 
try  { 
fout  =  new   FileOutputStream  (  file  )  ; 
fout  .  write  (  get  (  )  )  ; 
}  finally  { 
if  (  fout  !=  null  )  { 
fout  .  close  (  )  ; 
} 
} 
}  else  { 
File   outputFile  =  getStoreLocation  (  )  ; 
if  (  outputFile  !=  null  )  { 
size  =  outputFile  .  length  (  )  ; 
if  (  !  outputFile  .  renameTo  (  file  )  )  { 
BufferedInputStream   in  =  null  ; 
BufferedOutputStream   out  =  null  ; 
try  { 
in  =  new   BufferedInputStream  (  new   FileInputStream  (  outputFile  )  )  ; 
out  =  new   BufferedOutputStream  (  new   FileOutputStream  (  file  )  )  ; 
IOUtils  .  copy  (  in  ,  out  )  ; 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
if  (  out  !=  null  )  { 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
} 
} 
} 
}  else  { 
throw   new   FileUploadException  (  "Cannot write uploaded file to disk!"  )  ; 
} 
} 
} 








public   void   delete  (  )  { 
cachedContent  =  null  ; 
File   outputFile  =  getStoreLocation  (  )  ; 
if  (  outputFile  !=  null  &&  outputFile  .  exists  (  )  )  { 
outputFile  .  delete  (  )  ; 
} 
} 










public   String   getFieldName  (  )  { 
return   fieldName  ; 
} 










public   void   setFieldName  (  String   fieldName  )  { 
this  .  fieldName  =  fieldName  ; 
} 











public   boolean   isFormField  (  )  { 
return   isFormField  ; 
} 












public   void   setFormField  (  boolean   state  )  { 
isFormField  =  state  ; 
} 











public   OutputStream   getOutputStream  (  )  throws   IOException  { 
if  (  dfos  ==  null  )  { 
File   outputFile  =  getTempFile  (  )  ; 
dfos  =  new   DeferredFileOutputStream  (  sizeThreshold  ,  outputFile  )  ; 
} 
return   dfos  ; 
} 













public   File   getStoreLocation  (  )  { 
return   dfos  ==  null  ?  null  :  dfos  .  getFile  (  )  ; 
} 




protected   void   finalize  (  )  { 
File   outputFile  =  dfos  .  getFile  (  )  ; 
if  (  outputFile  !=  null  &&  outputFile  .  exists  (  )  )  { 
outputFile  .  delete  (  )  ; 
} 
} 









protected   File   getTempFile  (  )  { 
if  (  tempFile  ==  null  )  { 
File   tempDir  =  repository  ; 
if  (  tempDir  ==  null  )  { 
tempDir  =  new   File  (  System  .  getProperty  (  "java.io.tmpdir"  )  )  ; 
} 
String   tempFileName  =  "upload_"  +  UID  +  "_"  +  getUniqueId  (  )  +  ".tmp"  ; 
tempFile  =  new   File  (  tempDir  ,  tempFileName  )  ; 
} 
return   tempFile  ; 
} 







private   static   String   getUniqueId  (  )  { 
final   int   limit  =  100000000  ; 
int   current  ; 
synchronized  (  DiskFileItem  .  class  )  { 
current  =  counter  ++  ; 
} 
String   id  =  Integer  .  toString  (  current  )  ; 
if  (  current  <  limit  )  { 
id  =  (  "00000000"  +  id  )  .  substring  (  id  .  length  (  )  )  ; 
} 
return   id  ; 
} 






public   String   toString  (  )  { 
return  "name="  +  this  .  getName  (  )  +  ", StoreLocation="  +  String  .  valueOf  (  this  .  getStoreLocation  (  )  )  +  ", size="  +  this  .  getSize  (  )  +  "bytes, "  +  "isFormField="  +  isFormField  (  )  +  ", FieldName="  +  this  .  getFieldName  (  )  ; 
} 










private   void   writeObject  (  ObjectOutputStream   out  )  throws   IOException  { 
if  (  dfos  .  isInMemory  (  )  )  { 
cachedContent  =  get  (  )  ; 
}  else  { 
cachedContent  =  null  ; 
dfosFile  =  dfos  .  getFile  (  )  ; 
} 
out  .  defaultWriteObject  (  )  ; 
} 












private   void   readObject  (  ObjectInputStream   in  )  throws   IOException  ,  ClassNotFoundException  { 
in  .  defaultReadObject  (  )  ; 
OutputStream   output  =  getOutputStream  (  )  ; 
if  (  cachedContent  !=  null  )  { 
output  .  write  (  cachedContent  )  ; 
}  else  { 
FileInputStream   input  =  new   FileInputStream  (  dfosFile  )  ; 
IOUtils  .  copy  (  input  ,  output  )  ; 
dfosFile  .  delete  (  )  ; 
dfosFile  =  null  ; 
} 
output  .  close  (  )  ; 
cachedContent  =  null  ; 
} 






public   FileItemHeaders   getHeaders  (  )  { 
return   headers  ; 
} 







public   void   setHeaders  (  FileItemHeaders   pHeaders  )  { 
headers  =  pHeaders  ; 
} 
} 

