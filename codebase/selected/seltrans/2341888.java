package   com  .  ivis  .  xprocess  .  util  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  jar  .  JarOutputStream  ; 
import   java  .  util  .  jar  .  Manifest  ; 
import   java  .  util  .  jar  .  Attributes  .  Name  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipInputStream  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   com  .  ivis  .  xprocess  .  framework  .  impl  .  DatasourceDescriptor  ; 
import   com  .  ivis  .  xprocess  .  framework  .  importexport  .  ImportExportException  ; 
import   com  .  ivis  .  xprocess  .  framework  .  importexport  .  ImportExportFault  ; 
import   com  .  ivis  .  xprocess  .  framework  .  importexport  .  XPXImport  ; 





public   class   ZipUtils  { 

private   static   final   Logger   logger  =  Logger  .  getLogger  (  ZipUtils  .  class  .  getName  (  )  )  ; 

public   static   final   String   DUMMY_ROOT_PORTFOLIO  =  "ROOT_PORTFOLIO"  ; 

public   static   final   String   MANIFEST_NAME  =  "xManifest.mf"  ; 














public   static   void   addToZip  (  String   path  ,  String   srcFile  ,  ZipOutputStream   zip  )  throws   ImportExportException  { 
File   file  =  new   File  (  srcFile  )  ; 
if  (  !  file  .  exists  (  )  ||  file  .  isDirectory  (  )  )  { 
return  ; 
} 
String   zipPath  ; 
if  (  !  path  .  endsWith  (  File  .  separator  )  )  { 
zipPath  =  path  +  File  .  separator  +  file  .  getName  (  )  ; 
}  else  { 
zipPath  =  path  +  file  .  getName  (  )  ; 
} 
byte  [  ]  buf  =  new   byte  [  2048  ]  ; 
int   len  ; 
FileInputStream   in  =  null  ; 
try  { 
in  =  new   FileInputStream  (  file  )  ; 
ZipEntry   objEntry  =  new   ZipEntry  (  zipPath  )  ; 
zip  .  putNextEntry  (  objEntry  )  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
zip  .  write  (  buf  ,  0  ,  len  )  ; 
} 
}  catch  (  Exception   e  )  { 
ImportExportException   ieo  =  new   ImportExportException  (  ImportExportFault  .  UNZIP_STREAM_FAILURE  ,  e  )  ; 
logger  .  throwing  (  ZipUtils  .  class  .  getName  (  )  ,  "addToZip"  ,  ieo  )  ; 
throw   ieo  ; 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   noop  )  { 
} 
} 
} 
} 

private   static   Manifest   getManifest  (  String   desc  )  { 
Manifest   manifest  =  new   Manifest  (  )  ; 
manifest  .  getMainAttributes  (  )  .  put  (  Name  .  MANIFEST_VERSION  ,  "1.0"  )  ; 
desc  =  desc  .  replaceAll  (  "\n"  ,  "\r "  )  ; 
manifest  .  getMainAttributes  (  )  .  put  (  new   Name  (  XPXImport  .  DESCRIPTION_NAME  )  ,  desc  )  ; 
manifest  .  getMainAttributes  (  )  .  put  (  new   Name  (  XPXImport  .  MINIMUM_CLIENT_VERSION_NAME  )  ,  Version  .  getMinimumCompatibleClientVersion  (  )  .  toString  (  )  )  ; 
return   manifest  ; 
} 










public   static   String   getManifestFromZip  (  ZipInputStream   in  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  2048  ]  ; 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
sb  .  append  (  new   String  (  buf  ,  0  ,  len  )  )  ; 
} 
return   sb  .  toString  (  )  ; 
} 








public   static   void   unZip  (  ZipInputStream   in  ,  String   zipPath  )  throws   ImportExportException  { 
zipPath  =  FileUtils  .  convertFileSeparators  (  zipPath  )  ; 
String   dir  =  zipPath  .  substring  (  0  ,  zipPath  .  lastIndexOf  (  File  .  separator  )  )  ; 
new   File  (  dir  )  .  mkdirs  (  )  ; 
File   file  =  new   File  (  zipPath  )  ; 
if  (  file  .  isDirectory  (  )  )  { 
return  ; 
} 
OutputStream   out  =  null  ; 
try  { 
out  =  new   FileOutputStream  (  file  )  ; 
byte  [  ]  buf  =  new   byte  [  2048  ]  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buf  )  )  >  0  )  { 
out  .  write  (  buf  ,  0  ,  len  )  ; 
} 
}  catch  (  FileNotFoundException   fileNotFoundException  )  { 
ImportExportException   ieo  =  new   ImportExportException  (  ImportExportFault  .  ZIP_FILE_NOT_FOUND  ,  "[FileNotFoundException] Could not create - "  +  file  .  getAbsolutePath  (  )  )  ; 
logger  .  severe  (  "Could not create - "  +  file  .  getAbsolutePath  (  )  )  ; 
throw   ieo  ; 
}  catch  (  Exception   e  )  { 
ImportExportException   ieo  =  new   ImportExportException  (  ImportExportFault  .  UNZIP_STREAM_FAILURE  ,  e  )  ; 
logger  .  throwing  (  ZipUtils  .  class  .  getName  (  )  ,  "unZip"  ,  ieo  )  ; 
throw   ieo  ; 
}  finally  { 
if  (  out  !=  null  )  { 
try  { 
out  .  close  (  )  ; 
}  catch  (  IOException   noop  )  { 
} 
} 
} 
} 









public   static   ZipOutputStream   createZip  (  File   zipFile  ,  String   manifestText  )  throws   ImportExportException  { 
if  (  zipFile  .  isDirectory  (  )  )  { 
ImportExportException   ieo  =  new   ImportExportException  (  ImportExportFault  .  MUST_BE_FILE  ,  "File passed to createZip can not be a directory. "  +  zipFile  .  getPath  (  )  )  ; 
logger  .  throwing  (  ZipUtils  .  class  .  getName  (  )  ,  "createZip"  ,  ieo  )  ; 
throw   ieo  ; 
} 
JarOutputStream   zos  =  null  ; 
try  { 
zos  =  new   JarOutputStream  (  new   FileOutputStream  (  zipFile  )  ,  getManifest  (  manifestText  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
ImportExportException   ieo  =  new   ImportExportException  (  ImportExportFault  .  ZIP_FILE_NOT_FOUND  ,  "Zip file not found "  +  zipFile  .  getPath  (  )  )  ; 
logger  .  throwing  (  ZipUtils  .  class  .  getName  (  )  ,  "createZip"  ,  ieo  )  ; 
throw   ieo  ; 
}  catch  (  IOException   e  )  { 
ImportExportException   ieo  =  new   ImportExportException  (  ImportExportFault  .  FAILED_TO_ADD_MANIFEST  ,  "Failed to add manifest"  ,  e  )  ; 
logger  .  throwing  (  ZipUtils  .  class  .  getName  (  )  ,  "createZip"  ,  ieo  )  ; 
throw   ieo  ; 
} 
return   zos  ; 
} 









public   static   String   getZipRelativePath  (  String   idxPath  )  { 
int   firstSlashPos  =  idxPath  .  indexOf  (  File  .  separator  )  ; 
int   secondSlashPos  =  idxPath  .  indexOf  (  File  .  separator  ,  firstSlashPos  +  1  )  ; 
String   fragment  =  idxPath  .  substring  (  0  ,  firstSlashPos  +  1  )  ; 
String   zipPath  =  ""  ; 
if  (  secondSlashPos  !=  -  1  )  { 
String   pathRelativeToPO  =  idxPath  .  substring  (  firstSlashPos  +  1  ,  secondSlashPos  )  ; 
zipPath  =  fragment  +  pathRelativeToPO  ; 
return   zipPath  ; 
} 
return   fragment  ; 
} 





public   static   String   getArtifactRelativePath  (  String   artifactPath  )  { 
int   artifactPos  =  artifactPath  .  indexOf  (  DatasourceDescriptor  .  ARTIFACT_DIRECTORY_DEFAULT  )  ; 
if  (  artifactPos  <  0  )  { 
return   null  ; 
} 
String   path  =  artifactPath  .  substring  (  artifactPos  )  ; 
return   getZipRelativePath  (  path  )  ; 
} 
} 

