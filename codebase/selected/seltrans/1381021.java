package   pl  .  wcislo  .  sbql4j  .  tools  .  doclets  .  internal  .  toolkit  .  util  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  OutputStreamWriter  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  io  .  Writer  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedHashMap  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  TreeMap  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  AnnotationDesc  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  AnnotationTypeDoc  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  ClassDoc  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  ExecutableMemberDoc  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  MethodDoc  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  PackageDoc  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  Parameter  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  ParameterizedType  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  ProgramElementDoc  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  SourcePosition  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  Type  ; 
import   pl  .  wcislo  .  sbql4j  .  javadoc  .  TypeVariable  ; 
import   pl  .  wcislo  .  sbql4j  .  tools  .  doclets  .  internal  .  toolkit  .  Configuration  ; 











public   class   Util  { 





public   static   final   String  [  ]  [  ]  HTML_ESCAPE_CHARS  =  {  {  "&"  ,  "&amp;"  }  ,  {  "<"  ,  "&lt;"  }  ,  {  ">"  ,  "&gt;"  }  }  ; 










public   static   ProgramElementDoc  [  ]  excludeDeprecatedMembers  (  ProgramElementDoc  [  ]  members  )  { 
return   toProgramElementDocArray  (  excludeDeprecatedMembersAsList  (  members  )  )  ; 
} 










public   static   List   excludeDeprecatedMembersAsList  (  ProgramElementDoc  [  ]  members  )  { 
List   list  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  members  .  length  ;  i  ++  )  { 
if  (  members  [  i  ]  .  tags  (  "deprecated"  )  .  length  ==  0  )  { 
list  .  add  (  members  [  i  ]  )  ; 
} 
} 
Collections  .  sort  (  list  )  ; 
return   list  ; 
} 




public   static   ProgramElementDoc  [  ]  toProgramElementDocArray  (  List   list  )  { 
ProgramElementDoc  [  ]  pgmarr  =  new   ProgramElementDoc  [  list  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  list  .  size  (  )  ;  i  ++  )  { 
pgmarr  [  i  ]  =  (  ProgramElementDoc  )  (  list  .  get  (  i  )  )  ; 
} 
return   pgmarr  ; 
} 







public   static   boolean   nonPublicMemberFound  (  ProgramElementDoc  [  ]  members  )  { 
for  (  int   i  =  0  ;  i  <  members  .  length  ;  i  ++  )  { 
if  (  !  members  [  i  ]  .  isPublic  (  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 








public   static   MethodDoc   findMethod  (  ClassDoc   cd  ,  MethodDoc   method  )  { 
MethodDoc  [  ]  methods  =  cd  .  methods  (  )  ; 
for  (  int   i  =  0  ;  i  <  methods  .  length  ;  i  ++  )  { 
if  (  executableMembersEqual  (  method  ,  methods  [  i  ]  )  )  { 
return   methods  [  i  ]  ; 
} 
} 
return   null  ; 
} 






public   static   boolean   executableMembersEqual  (  ExecutableMemberDoc   member1  ,  ExecutableMemberDoc   member2  )  { 
if  (  !  (  member1   instanceof   MethodDoc  &&  member2   instanceof   MethodDoc  )  )  return   false  ; 
MethodDoc   method1  =  (  MethodDoc  )  member1  ; 
MethodDoc   method2  =  (  MethodDoc  )  member2  ; 
if  (  method1  .  isStatic  (  )  &&  method2  .  isStatic  (  )  )  { 
Parameter  [  ]  targetParams  =  method1  .  parameters  (  )  ; 
Parameter  [  ]  currentParams  ; 
if  (  method1  .  name  (  )  .  equals  (  method2  .  name  (  )  )  &&  (  currentParams  =  method2  .  parameters  (  )  )  .  length  ==  targetParams  .  length  )  { 
int   j  ; 
for  (  j  =  0  ;  j  <  targetParams  .  length  ;  j  ++  )  { 
if  (  !  (  targetParams  [  j  ]  .  typeName  (  )  .  equals  (  currentParams  [  j  ]  .  typeName  (  )  )  ||  currentParams  [  j  ]  .  type  (  )  instanceof   TypeVariable  ||  targetParams  [  j  ]  .  type  (  )  instanceof   TypeVariable  )  )  { 
break  ; 
} 
} 
if  (  j  ==  targetParams  .  length  )  { 
return   true  ; 
} 
} 
return   false  ; 
}  else  { 
return   method1  .  overrides  (  method2  )  ||  method2  .  overrides  (  method1  )  ||  member1  ==  member2  ; 
} 
} 





public   static   boolean   isCoreClass  (  ClassDoc   cd  )  { 
return   cd  .  containingClass  (  )  ==  null  ||  cd  .  isStatic  (  )  ; 
} 

public   static   boolean   matches  (  ProgramElementDoc   doc1  ,  ProgramElementDoc   doc2  )  { 
if  (  doc1   instanceof   ExecutableMemberDoc  &&  doc2   instanceof   ExecutableMemberDoc  )  { 
ExecutableMemberDoc   ed1  =  (  ExecutableMemberDoc  )  doc1  ; 
ExecutableMemberDoc   ed2  =  (  ExecutableMemberDoc  )  doc2  ; 
return   executableMembersEqual  (  ed1  ,  ed2  )  ; 
}  else  { 
return   doc1  .  name  (  )  .  equals  (  doc2  .  name  (  )  )  ; 
} 
} 







public   static   void   copyFile  (  File   destfile  ,  File   srcfile  )  throws   IOException  { 
byte  [  ]  bytearr  =  new   byte  [  512  ]  ; 
int   len  =  0  ; 
FileInputStream   input  =  new   FileInputStream  (  srcfile  )  ; 
File   destDir  =  destfile  .  getParentFile  (  )  ; 
destDir  .  mkdirs  (  )  ; 
FileOutputStream   output  =  new   FileOutputStream  (  destfile  )  ; 
try  { 
while  (  (  len  =  input  .  read  (  bytearr  )  )  !=  -  1  )  { 
output  .  write  (  bytearr  ,  0  ,  len  )  ; 
} 
}  catch  (  FileNotFoundException   exc  )  { 
}  catch  (  SecurityException   exc  )  { 
}  finally  { 
input  .  close  (  )  ; 
output  .  close  (  )  ; 
} 
} 














public   static   void   copyDocFiles  (  Configuration   configuration  ,  String   path  ,  String   dir  ,  boolean   overwrite  )  { 
if  (  checkCopyDocFilesErrors  (  configuration  ,  path  ,  dir  )  )  { 
return  ; 
} 
String   destname  =  configuration  .  docFileDestDirName  ; 
File   srcdir  =  new   File  (  path  +  dir  )  ; 
if  (  destname  .  length  (  )  >  0  &&  !  destname  .  endsWith  (  DirectoryManager  .  URL_FILE_SEPERATOR  )  )  { 
destname  +=  DirectoryManager  .  URL_FILE_SEPERATOR  ; 
} 
String   dest  =  destname  +  dir  ; 
try  { 
File   destdir  =  new   File  (  dest  )  ; 
DirectoryManager  .  createDirectory  (  configuration  ,  dest  )  ; 
String  [  ]  files  =  srcdir  .  list  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  i  ++  )  { 
File   srcfile  =  new   File  (  srcdir  ,  files  [  i  ]  )  ; 
File   destfile  =  new   File  (  destdir  ,  files  [  i  ]  )  ; 
if  (  srcfile  .  isFile  (  )  )  { 
if  (  destfile  .  exists  (  )  &&  !  overwrite  )  { 
configuration  .  message  .  warning  (  (  SourcePosition  )  null  ,  "doclet.Copy_Overwrite_warning"  ,  srcfile  .  toString  (  )  ,  destdir  .  toString  (  )  )  ; 
}  else  { 
configuration  .  message  .  notice  (  "doclet.Copying_File_0_To_Dir_1"  ,  srcfile  .  toString  (  )  ,  destdir  .  toString  (  )  )  ; 
Util  .  copyFile  (  destfile  ,  srcfile  )  ; 
} 
}  else   if  (  srcfile  .  isDirectory  (  )  )  { 
if  (  configuration  .  copydocfilesubdirs  &&  !  configuration  .  shouldExcludeDocFileDir  (  srcfile  .  getName  (  )  )  )  { 
copyDocFiles  (  configuration  ,  path  ,  dir  +  DirectoryManager  .  URL_FILE_SEPERATOR  +  srcfile  .  getName  (  )  ,  overwrite  )  ; 
} 
} 
} 
}  catch  (  SecurityException   exc  )  { 
throw   new   DocletAbortException  (  )  ; 
}  catch  (  IOException   exc  )  { 
throw   new   DocletAbortException  (  )  ; 
} 
} 








private   static   boolean   checkCopyDocFilesErrors  (  Configuration   configuration  ,  String   path  ,  String   dirName  )  { 
if  (  (  configuration  .  sourcepath  ==  null  ||  configuration  .  sourcepath  .  length  (  )  ==  0  )  &&  (  configuration  .  destDirName  ==  null  ||  configuration  .  destDirName  .  length  (  )  ==  0  )  )  { 
return   true  ; 
} 
File   sourcePath  ,  destPath  =  new   File  (  configuration  .  destDirName  )  ; 
StringTokenizer   pathTokens  =  new   StringTokenizer  (  configuration  .  sourcepath  ==  null  ?  ""  :  configuration  .  sourcepath  ,  File  .  pathSeparator  )  ; 
while  (  pathTokens  .  hasMoreTokens  (  )  )  { 
sourcePath  =  new   File  (  pathTokens  .  nextToken  (  )  )  ; 
if  (  destPath  .  equals  (  sourcePath  )  )  { 
return   true  ; 
} 
} 
File   srcdir  =  new   File  (  path  +  dirName  )  ; 
if  (  !  srcdir  .  exists  (  )  )  { 
return   true  ; 
} 
return   false  ; 
} 













public   static   void   copyResourceFile  (  Configuration   configuration  ,  String   resourcefile  ,  boolean   overwrite  )  { 
String   destdir  =  configuration  .  destDirName  ; 
String   destresourcesdir  =  destdir  +  "resources"  ; 
DirectoryManager  .  createDirectory  (  configuration  ,  destresourcesdir  )  ; 
File   destfile  =  new   File  (  destresourcesdir  ,  resourcefile  )  ; 
if  (  destfile  .  exists  (  )  &&  (  !  overwrite  )  )  return  ; 
try  { 
InputStream   in  =  Configuration  .  class  .  getResourceAsStream  (  "resources/"  +  resourcefile  )  ; 
if  (  in  ==  null  )  return  ; 
OutputStream   out  =  new   FileOutputStream  (  destfile  )  ; 
byte  [  ]  buf  =  new   byte  [  2048  ]  ; 
int   n  ; 
while  (  (  n  =  in  .  read  (  buf  )  )  >  0  )  out  .  write  (  buf  ,  0  ,  n  )  ; 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
}  catch  (  Throwable   t  )  { 
} 
} 







public   static   String   getPackageSourcePath  (  Configuration   configuration  ,  PackageDoc   pkgDoc  )  { 
try  { 
String   pkgPath  =  DirectoryManager  .  getDirectoryPath  (  pkgDoc  )  ; 
String   completePath  =  new   SourcePath  (  configuration  .  sourcepath  )  .  getDirectory  (  pkgPath  )  +  DirectoryManager  .  URL_FILE_SEPERATOR  ; 
completePath  =  Util  .  replaceText  (  completePath  ,  File  .  separator  ,  DirectoryManager  .  URL_FILE_SEPERATOR  )  ; 
pkgPath  =  Util  .  replaceText  (  pkgPath  ,  File  .  separator  ,  DirectoryManager  .  URL_FILE_SEPERATOR  )  ; 
return   completePath  .  substring  (  0  ,  completePath  .  indexOf  (  pkgPath  )  )  ; 
}  catch  (  Exception   e  )  { 
return  ""  ; 
} 
} 





private   static   class   TypeComparator   implements   Comparator  { 

public   int   compare  (  Object   type1  ,  Object   type2  )  { 
return  (  (  Type  )  type1  )  .  qualifiedTypeName  (  )  .  toLowerCase  (  )  .  compareTo  (  (  (  Type  )  type2  )  .  qualifiedTypeName  (  )  .  toLowerCase  (  )  )  ; 
} 
} 













public   static   List   getAllInterfaces  (  Type   type  ,  Configuration   configuration  ,  boolean   sort  )  { 
Map   results  =  sort  ?  new   TreeMap  (  )  :  new   LinkedHashMap  (  )  ; 
Type  [  ]  interfaceTypes  =  null  ; 
Type   superType  =  null  ; 
if  (  type   instanceof   ParameterizedType  )  { 
interfaceTypes  =  (  (  ParameterizedType  )  type  )  .  interfaceTypes  (  )  ; 
superType  =  (  (  ParameterizedType  )  type  )  .  superclassType  (  )  ; 
}  else   if  (  type   instanceof   ClassDoc  )  { 
interfaceTypes  =  (  (  ClassDoc  )  type  )  .  interfaceTypes  (  )  ; 
superType  =  (  (  ClassDoc  )  type  )  .  superclassType  (  )  ; 
}  else  { 
interfaceTypes  =  type  .  asClassDoc  (  )  .  interfaceTypes  (  )  ; 
superType  =  type  .  asClassDoc  (  )  .  superclassType  (  )  ; 
} 
for  (  int   i  =  0  ;  i  <  interfaceTypes  .  length  ;  i  ++  )  { 
Type   interfaceType  =  interfaceTypes  [  i  ]  ; 
ClassDoc   interfaceClassDoc  =  interfaceType  .  asClassDoc  (  )  ; 
if  (  !  (  interfaceClassDoc  .  isPublic  (  )  ||  (  configuration  ==  null  ||  isLinkable  (  interfaceClassDoc  ,  configuration  )  )  )  )  { 
continue  ; 
} 
results  .  put  (  interfaceClassDoc  ,  interfaceType  )  ; 
List   superInterfaces  =  getAllInterfaces  (  interfaceType  ,  configuration  ,  sort  )  ; 
for  (  Iterator   iter  =  superInterfaces  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Type   t  =  (  Type  )  iter  .  next  (  )  ; 
results  .  put  (  t  .  asClassDoc  (  )  ,  t  )  ; 
} 
} 
if  (  superType  ==  null  )  return   new   ArrayList  (  results  .  values  (  )  )  ; 
addAllInterfaceTypes  (  results  ,  superType  ,  superType   instanceof   ClassDoc  ?  (  (  ClassDoc  )  superType  )  .  interfaceTypes  (  )  :  (  (  ParameterizedType  )  superType  )  .  interfaceTypes  (  )  ,  false  ,  configuration  )  ; 
List   resultsList  =  new   ArrayList  (  results  .  values  (  )  )  ; 
if  (  sort  )  { 
Collections  .  sort  (  resultsList  ,  new   TypeComparator  (  )  )  ; 
} 
return   resultsList  ; 
} 

public   static   List   getAllInterfaces  (  Type   type  ,  Configuration   configuration  )  { 
return   getAllInterfaces  (  type  ,  configuration  ,  true  )  ; 
} 

private   static   void   findAllInterfaceTypes  (  Map   results  ,  ClassDoc   c  ,  boolean   raw  ,  Configuration   configuration  )  { 
Type   superType  =  c  .  superclassType  (  )  ; 
if  (  superType  ==  null  )  return  ; 
addAllInterfaceTypes  (  results  ,  superType  ,  superType   instanceof   ClassDoc  ?  (  (  ClassDoc  )  superType  )  .  interfaceTypes  (  )  :  (  (  ParameterizedType  )  superType  )  .  interfaceTypes  (  )  ,  raw  ,  configuration  )  ; 
} 

private   static   void   findAllInterfaceTypes  (  Map   results  ,  ParameterizedType   p  ,  Configuration   configuration  )  { 
Type   superType  =  p  .  superclassType  (  )  ; 
if  (  superType  ==  null  )  return  ; 
addAllInterfaceTypes  (  results  ,  superType  ,  superType   instanceof   ClassDoc  ?  (  (  ClassDoc  )  superType  )  .  interfaceTypes  (  )  :  (  (  ParameterizedType  )  superType  )  .  interfaceTypes  (  )  ,  false  ,  configuration  )  ; 
} 

private   static   void   addAllInterfaceTypes  (  Map   results  ,  Type   type  ,  Type  [  ]  interfaceTypes  ,  boolean   raw  ,  Configuration   configuration  )  { 
for  (  int   i  =  0  ;  i  <  interfaceTypes  .  length  ;  i  ++  )  { 
Type   interfaceType  =  interfaceTypes  [  i  ]  ; 
ClassDoc   interfaceClassDoc  =  interfaceType  .  asClassDoc  (  )  ; 
if  (  !  (  interfaceClassDoc  .  isPublic  (  )  ||  (  configuration  !=  null  &&  isLinkable  (  interfaceClassDoc  ,  configuration  )  )  )  )  { 
continue  ; 
} 
if  (  raw  )  interfaceType  =  interfaceType  .  asClassDoc  (  )  ; 
results  .  put  (  interfaceClassDoc  ,  interfaceType  )  ; 
List   superInterfaces  =  getAllInterfaces  (  interfaceType  ,  configuration  )  ; 
for  (  Iterator   iter  =  superInterfaces  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
Type   superInterface  =  (  Type  )  iter  .  next  (  )  ; 
results  .  put  (  superInterface  .  asClassDoc  (  )  ,  superInterface  )  ; 
} 
} 
if  (  type   instanceof   ParameterizedType  )  findAllInterfaceTypes  (  results  ,  (  ParameterizedType  )  type  ,  configuration  )  ;  else   if  (  (  (  ClassDoc  )  type  )  .  typeParameters  (  )  .  length  ==  0  )  findAllInterfaceTypes  (  results  ,  (  ClassDoc  )  type  ,  raw  ,  configuration  )  ;  else   findAllInterfaceTypes  (  results  ,  (  ClassDoc  )  type  ,  true  ,  configuration  )  ; 
} 

public   static   List   asList  (  ProgramElementDoc  [  ]  members  )  { 
List   list  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  members  .  length  ;  i  ++  )  { 
list  .  add  (  members  [  i  ]  )  ; 
} 
return   list  ; 
} 




public   static   String   quote  (  String   filepath  )  { 
return  (  "\""  +  filepath  +  "\""  )  ; 
} 






public   static   String   getPackageName  (  PackageDoc   packageDoc  )  { 
return   packageDoc  ==  null  ||  packageDoc  .  name  (  )  .  length  (  )  ==  0  ?  DocletConstants  .  DEFAULT_PACKAGE_NAME  :  packageDoc  .  name  (  )  ; 
} 






public   static   String   getPackageFileHeadName  (  PackageDoc   packageDoc  )  { 
return   packageDoc  ==  null  ||  packageDoc  .  name  (  )  .  length  (  )  ==  0  ?  DocletConstants  .  DEFAULT_PACKAGE_FILE_NAME  :  packageDoc  .  name  (  )  ; 
} 







public   static   String   replaceText  (  String   originalStr  ,  String   oldStr  ,  String   newStr  )  { 
if  (  oldStr  ==  null  ||  newStr  ==  null  ||  oldStr  .  equals  (  newStr  )  )  { 
return   originalStr  ; 
} 
StringBuffer   result  =  new   StringBuffer  (  originalStr  )  ; 
int   startIndex  =  0  ; 
while  (  (  startIndex  =  result  .  indexOf  (  oldStr  ,  startIndex  )  )  !=  -  1  )  { 
result  =  result  .  replace  (  startIndex  ,  startIndex  +  oldStr  .  length  (  )  ,  newStr  )  ; 
startIndex  +=  newStr  .  length  (  )  ; 
} 
return   result  .  toString  (  )  ; 
} 











public   static   String   escapeHtmlChars  (  String   s  )  { 
String   result  =  s  ; 
for  (  int   i  =  0  ;  i  <  HTML_ESCAPE_CHARS  .  length  ;  i  ++  )  { 
result  =  Util  .  replaceText  (  result  ,  HTML_ESCAPE_CHARS  [  i  ]  [  0  ]  ,  HTML_ESCAPE_CHARS  [  i  ]  [  1  ]  )  ; 
} 
return   result  ; 
} 
















public   static   Writer   genWriter  (  Configuration   configuration  ,  String   path  ,  String   filename  ,  String   docencoding  )  throws   IOException  ,  UnsupportedEncodingException  { 
FileOutputStream   fos  ; 
if  (  path  !=  null  )  { 
DirectoryManager  .  createDirectory  (  configuration  ,  path  )  ; 
fos  =  new   FileOutputStream  (  (  (  path  .  length  (  )  >  0  )  ?  path  +  File  .  separator  :  ""  )  +  filename  )  ; 
}  else  { 
fos  =  new   FileOutputStream  (  filename  )  ; 
} 
if  (  docencoding  ==  null  )  { 
OutputStreamWriter   oswriter  =  new   OutputStreamWriter  (  fos  )  ; 
docencoding  =  oswriter  .  getEncoding  (  )  ; 
return   oswriter  ; 
}  else  { 
return   new   OutputStreamWriter  (  fos  ,  docencoding  )  ; 
} 
} 









public   static   boolean   isDocumentedAnnotation  (  AnnotationTypeDoc   annotationDoc  )  { 
AnnotationDesc  [  ]  annotationDescList  =  annotationDoc  .  annotations  (  )  ; 
for  (  int   i  =  0  ;  i  <  annotationDescList  .  length  ;  i  ++  )  { 
if  (  annotationDescList  [  i  ]  .  annotationType  (  )  .  qualifiedName  (  )  .  equals  (  java  .  lang  .  annotation  .  Documented  .  class  .  getName  (  )  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 














public   static   String  [  ]  tokenize  (  String   s  ,  char   separator  ,  int   maxTokens  )  { 
List   tokens  =  new   ArrayList  (  )  ; 
StringBuilder   token  =  new   StringBuilder  (  )  ; 
boolean   prevIsEscapeChar  =  false  ; 
for  (  int   i  =  0  ;  i  <  s  .  length  (  )  ;  i  +=  Character  .  charCount  (  i  )  )  { 
int   currentChar  =  s  .  codePointAt  (  i  )  ; 
if  (  prevIsEscapeChar  )  { 
token  .  appendCodePoint  (  currentChar  )  ; 
prevIsEscapeChar  =  false  ; 
}  else   if  (  currentChar  ==  separator  &&  tokens  .  size  (  )  <  maxTokens  -  1  )  { 
tokens  .  add  (  token  .  toString  (  )  )  ; 
token  =  new   StringBuilder  (  )  ; 
}  else   if  (  currentChar  ==  '\\'  )  { 
prevIsEscapeChar  =  true  ; 
}  else  { 
token  .  appendCodePoint  (  currentChar  )  ; 
} 
} 
if  (  token  .  length  (  )  >  0  )  { 
tokens  .  add  (  token  .  toString  (  )  )  ; 
} 
return  (  String  [  ]  )  tokens  .  toArray  (  new   String  [  ]  {  }  )  ; 
} 














public   static   boolean   isLinkable  (  ClassDoc   classDoc  ,  Configuration   configuration  )  { 
return  (  (  classDoc  .  isIncluded  (  )  &&  configuration  .  isGeneratedDoc  (  classDoc  )  )  )  ||  (  configuration  .  extern  .  isExternal  (  classDoc  )  &&  (  classDoc  .  isPublic  (  )  ||  classDoc  .  isProtected  (  )  )  )  ; 
} 









public   static   Type   getFirstVisibleSuperClass  (  ClassDoc   classDoc  ,  Configuration   configuration  )  { 
if  (  classDoc  ==  null  )  { 
return   null  ; 
} 
Type   sup  =  classDoc  .  superclassType  (  )  ; 
ClassDoc   supClassDoc  =  classDoc  .  superclass  (  )  ; 
while  (  sup  !=  null  &&  (  !  (  supClassDoc  .  isPublic  (  )  ||  isLinkable  (  supClassDoc  ,  configuration  )  )  )  )  { 
if  (  supClassDoc  .  superclass  (  )  .  qualifiedName  (  )  .  equals  (  supClassDoc  .  qualifiedName  (  )  )  )  break  ; 
sup  =  supClassDoc  .  superclassType  (  )  ; 
supClassDoc  =  supClassDoc  .  superclass  (  )  ; 
} 
if  (  classDoc  .  equals  (  supClassDoc  )  )  { 
return   null  ; 
} 
return   sup  ; 
} 









public   static   ClassDoc   getFirstVisibleSuperClassCD  (  ClassDoc   classDoc  ,  Configuration   configuration  )  { 
if  (  classDoc  ==  null  )  { 
return   null  ; 
} 
ClassDoc   supClassDoc  =  classDoc  .  superclass  (  )  ; 
while  (  supClassDoc  !=  null  &&  (  !  (  supClassDoc  .  isPublic  (  )  ||  isLinkable  (  supClassDoc  ,  configuration  )  )  )  )  { 
supClassDoc  =  supClassDoc  .  superclass  (  )  ; 
} 
if  (  classDoc  .  equals  (  supClassDoc  )  )  { 
return   null  ; 
} 
return   supClassDoc  ; 
} 









public   static   String   getTypeName  (  Configuration   config  ,  ClassDoc   cd  ,  boolean   lowerCaseOnly  )  { 
String   typeName  =  ""  ; 
if  (  cd  .  isOrdinaryClass  (  )  )  { 
typeName  =  "doclet.Class"  ; 
}  else   if  (  cd  .  isInterface  (  )  )  { 
typeName  =  "doclet.Interface"  ; 
}  else   if  (  cd  .  isException  (  )  )  { 
typeName  =  "doclet.Exception"  ; 
}  else   if  (  cd  .  isError  (  )  )  { 
typeName  =  "doclet.Error"  ; 
}  else   if  (  cd  .  isAnnotationType  (  )  )  { 
typeName  =  "doclet.AnnotationType"  ; 
}  else   if  (  cd  .  isEnum  (  )  )  { 
typeName  =  "doclet.Enum"  ; 
} 
return   config  .  getText  (  lowerCaseOnly  ?  typeName  .  toLowerCase  (  )  :  typeName  )  ; 
} 







public   static   void   replaceTabs  (  int   tabLength  ,  StringBuffer   s  )  { 
int   index  ,  col  ; 
StringBuffer   whitespace  ; 
while  (  (  index  =  s  .  indexOf  (  "\t"  )  )  !=  -  1  )  { 
whitespace  =  new   StringBuffer  (  )  ; 
col  =  index  ; 
do  { 
whitespace  .  append  (  " "  )  ; 
col  ++  ; 
}  while  (  (  col  %  tabLength  )  !=  0  )  ; 
s  .  replace  (  index  ,  index  +  1  ,  whitespace  .  toString  (  )  )  ; 
} 
} 





public   static   void   setEnumDocumentation  (  Configuration   configuration  ,  ClassDoc   classDoc  )  { 
MethodDoc  [  ]  methods  =  classDoc  .  methods  (  )  ; 
for  (  int   j  =  0  ;  j  <  methods  .  length  ;  j  ++  )  { 
MethodDoc   currentMethod  =  methods  [  j  ]  ; 
if  (  currentMethod  .  name  (  )  .  equals  (  "values"  )  &&  currentMethod  .  parameters  (  )  .  length  ==  0  )  { 
currentMethod  .  setRawCommentText  (  configuration  .  getText  (  "doclet.enum_values_doc"  ,  classDoc  .  name  (  )  )  )  ; 
}  else   if  (  currentMethod  .  name  (  )  .  equals  (  "valueOf"  )  &&  currentMethod  .  parameters  (  )  .  length  ==  1  )  { 
Type   paramType  =  currentMethod  .  parameters  (  )  [  0  ]  .  type  (  )  ; 
if  (  paramType  !=  null  &&  paramType  .  qualifiedTypeName  (  )  .  equals  (  String  .  class  .  getName  (  )  )  )  { 
currentMethod  .  setRawCommentText  (  configuration  .  getText  (  "doclet.enum_valueof_doc"  )  )  ; 
} 
} 
} 
} 







public   static   boolean   isDeprecated  (  ProgramElementDoc   doc  )  { 
if  (  doc  .  tags  (  "deprecated"  )  .  length  >  0  )  { 
return   true  ; 
} 
AnnotationDesc  [  ]  annotationDescList  =  doc  .  annotations  (  )  ; 
for  (  int   i  =  0  ;  i  <  annotationDescList  .  length  ;  i  ++  )  { 
if  (  annotationDescList  [  i  ]  .  annotationType  (  )  .  qualifiedName  (  )  .  equals  (  java  .  lang  .  Deprecated  .  class  .  getName  (  )  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 
} 

