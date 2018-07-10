import   java  .  io  .  *  ; 
import   java  .  io  .  File  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLClassLoader  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  zip  .  *  ; 






public   class   RJavaClassLoader   extends   URLClassLoader  { 




String   rJavaPath  ; 




String   rJavaLibPath  ; 




HashMap   libMap  ; 




Vector   classPath  ; 




public   static   RJavaClassLoader   primaryLoader  =  null  ; 




public   static   boolean   verbose  =  false  ; 





public   boolean   useSystem  =  true  ; 




class   UnixFile   extends   File  { 




long   lastModStamp  ; 





public   UnixFile  (  String   fn  )  { 
super  (  u2w  (  fn  )  )  ; 
lastModStamp  =  0  ; 
} 




public   boolean   hasChanged  (  )  { 
long   curMod  =  lastModified  (  )  ; 
return  (  curMod  !=  lastModStamp  )  ; 
} 




public   void   update  (  )  { 
lastModStamp  =  lastModified  (  )  ; 
} 
} 




class   UnixJarFile   extends   UnixFile  { 




private   ZipFile   zfile  ; 




private   String   urlPrefix  ; 

public   UnixJarFile  (  String   filename  )  { 
super  (  filename  )  ; 
} 

public   void   update  (  )  { 
try  { 
if  (  zfile  !=  null  )  { 
zfile  .  close  (  )  ; 
} 
zfile  =  new   ZipFile  (  this  )  ; 
}  catch  (  Exception   tryCloseX  )  { 
} 
super  .  update  (  )  ; 
} 







public   InputStream   getResourceAsStream  (  String   name  )  { 
if  (  zfile  ==  null  ||  hasChanged  (  )  )  { 
update  (  )  ; 
} 
try  { 
if  (  zfile  ==  null  )  return   null  ; 
ZipEntry   e  =  zfile  .  getEntry  (  name  )  ; 
if  (  e  !=  null  )  return   zfile  .  getInputStream  (  e  )  ; 
}  catch  (  Exception   e  )  { 
if  (  verbose  )  System  .  err  .  println  (  "RJavaClassLoader$UnixJarFile: exception: "  +  e  .  getMessage  (  )  )  ; 
} 
return   null  ; 
} 

public   URL   getResource  (  String   name  )  { 
if  (  zfile  ==  null  ||  zfile  .  getEntry  (  name  )  ==  null  )  { 
return   null  ; 
} 
URL   u  =  null  ; 
if  (  urlPrefix  ==  null  )  { 
try  { 
urlPrefix  =  "jar:"  +  toURL  (  )  .  toString  (  )  +  "!"  ; 
}  catch  (  java  .  net  .  MalformedURLException   ex  )  { 
}  catch  (  java  .  io  .  IOException   ex  )  { 
} 
} 
try  { 
u  =  new   URL  (  urlPrefix  +  name  )  ; 
}  catch  (  java  .  net  .  MalformedURLException   ex  )  { 
} 
return   u  ; 
} 
} 

class   UnixDirectory   extends   UnixFile  { 

public   UnixDirectory  (  String   dirname  )  { 
super  (  dirname  )  ; 
} 
} 




public   static   RJavaClassLoader   getPrimaryLoader  (  )  { 
return   primaryLoader  ; 
} 








public   RJavaClassLoader  (  String   path  ,  String   libpath  )  { 
super  (  new   URL  [  ]  {  }  )  ; 
String   rjd  =  System  .  getProperty  (  "rJava.debug"  )  ; 
if  (  rjd  !=  null  &&  rjd  .  length  (  )  >  0  &&  !  rjd  .  equals  (  "0"  )  )  verbose  =  true  ; 
if  (  verbose  )  System  .  out  .  println  (  "RJavaClassLoader(\""  +  path  +  "\",\""  +  libpath  +  "\")"  )  ; 
if  (  primaryLoader  ==  null  )  { 
primaryLoader  =  this  ; 
if  (  verbose  )  System  .  out  .  println  (  " - primary loader"  )  ; 
}  else  { 
if  (  verbose  )  System  .  out  .  println  (  " - NOT primrary (this="  +  this  +  ", primary="  +  primaryLoader  +  ")"  )  ; 
} 
libMap  =  new   HashMap  (  )  ; 
classPath  =  new   Vector  (  )  ; 
classPath  .  add  (  new   UnixDirectory  (  path  +  "/java"  )  )  ; 
rJavaPath  =  path  ; 
rJavaLibPath  =  libpath  ; 
UnixFile   so  =  new   UnixFile  (  rJavaLibPath  +  "/rJava.so"  )  ; 
if  (  !  so  .  exists  (  )  )  so  =  new   UnixFile  (  rJavaLibPath  +  "/rJava.dll"  )  ; 
if  (  so  .  exists  (  )  )  libMap  .  put  (  "rJava"  ,  so  )  ; 
UnixFile   jri  =  new   UnixFile  (  path  +  "/jri/libjri.so"  )  ; 
String   rarch  =  System  .  getProperty  (  "r.arch"  )  ; 
if  (  rarch  !=  null  &&  rarch  .  length  (  )  >  0  )  { 
UnixFile   af  =  new   UnixFile  (  path  +  "/jri"  +  rarch  +  "/libjri.so"  )  ; 
if  (  af  .  exists  (  )  )  jri  =  af  ;  else  { 
af  =  new   UnixFile  (  path  +  "/jri"  +  rarch  +  "/jri.dll"  )  ; 
if  (  af  .  exists  (  )  )  jri  =  af  ; 
} 
} 
if  (  !  jri  .  exists  (  )  )  jri  =  new   UnixFile  (  path  +  "/jri/libjri.jnilib"  )  ; 
if  (  !  jri  .  exists  (  )  )  jri  =  new   UnixFile  (  path  +  "/jri/jri.dll"  )  ; 
if  (  jri  .  exists  (  )  )  { 
libMap  .  put  (  "jri"  ,  jri  )  ; 
if  (  verbose  )  System  .  out  .  println  (  " - registered JRI: "  +  jri  )  ; 
} 
if  (  primaryLoader  ==  this  )  Thread  .  currentThread  (  )  .  setContextClassLoader  (  this  )  ; 
if  (  verbose  )  { 
System  .  out  .  println  (  "RJavaClassLoader initialized.\n\nRegistered libraries:"  )  ; 
for  (  Iterator   entries  =  libMap  .  keySet  (  )  .  iterator  (  )  ;  entries  .  hasNext  (  )  ;  )  { 
Object   key  =  entries  .  next  (  )  ; 
System  .  out  .  println  (  "  "  +  key  +  ": '"  +  libMap  .  get  (  key  )  +  "'"  )  ; 
} 
System  .  out  .  println  (  "\nRegistered class paths:"  )  ; 
for  (  Enumeration   e  =  classPath  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  System  .  out  .  println  (  "  '"  +  e  .  nextElement  (  )  +  "'"  )  ; 
System  .  out  .  println  (  "\n-- end of class loader report --"  )  ; 
} 
} 




String   classNameToFile  (  String   cls  )  { 
return   cls  .  replace  (  '.'  ,  '/'  )  ; 
} 

protected   Class   findClass  (  String   name  )  throws   ClassNotFoundException  { 
Class   cl  =  null  ; 
if  (  verbose  )  System  .  out  .  println  (  ""  +  this  +  ".findClass("  +  name  +  ")"  )  ; 
if  (  "RJavaClassLoader"  .  equals  (  name  )  )  return   getClass  (  )  ; 
if  (  useSystem  )  { 
try  { 
cl  =  super  .  findClass  (  name  )  ; 
if  (  cl  !=  null  )  { 
if  (  verbose  )  System  .  out  .  println  (  "RJavaClassLoader: found class "  +  name  +  " using URL loader"  )  ; 
return   cl  ; 
} 
}  catch  (  Exception   fnf  )  { 
if  (  verbose  )  System  .  out  .  println  (  " - URL loader did not find it: "  +  fnf  )  ; 
} 
} 
if  (  verbose  )  System  .  out  .  println  (  "RJavaClassLoader.findClass(\""  +  name  +  "\")"  )  ; 
InputStream   ins  =  null  ; 
Exception   defineException  =  null  ; 
Enumeration   e  =  classPath  .  elements  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
UnixFile   cp  =  (  UnixFile  )  e  .  nextElement  (  )  ; 
if  (  verbose  )  System  .  out  .  println  (  " - trying class path \""  +  cp  +  "\""  )  ; 
try  { 
ins  =  null  ; 
if  (  cp   instanceof   UnixJarFile  )  { 
ins  =  (  (  UnixJarFile  )  cp  )  .  getResourceAsStream  (  classNameToFile  (  name  )  +  ".class"  )  ; 
if  (  verbose  )  System  .  out  .  println  (  "   JAR file, can get '"  +  classNameToFile  (  name  )  +  "'? "  +  (  (  ins  ==  null  )  ?  "YES"  :  "NO"  )  )  ; 
}  else   if  (  cp   instanceof   UnixDirectory  )  { 
UnixFile   class_f  =  new   UnixFile  (  cp  .  getPath  (  )  +  "/"  +  classNameToFile  (  name  )  +  ".class"  )  ; 
if  (  class_f  .  isFile  (  )  )  { 
ins  =  new   FileInputStream  (  class_f  )  ; 
} 
if  (  verbose  )  System  .  out  .  println  (  "   Directory, can get '"  +  class_f  +  "'? "  +  (  (  ins  ==  null  )  ?  "YES"  :  "NO"  )  )  ; 
} 
if  (  ins  !=  null  )  { 
int   al  =  128  *  1024  ; 
byte   fc  [  ]  =  new   byte  [  al  ]  ; 
int   n  =  ins  .  read  (  fc  )  ; 
int   rp  =  n  ; 
if  (  verbose  )  System  .  out  .  println  (  "  loading class file, initial n = "  +  n  )  ; 
while  (  n  >  0  )  { 
if  (  rp  ==  al  )  { 
int   nexa  =  al  *  2  ; 
if  (  nexa  <  512  *  1024  )  nexa  =  512  *  1024  ; 
byte   la  [  ]  =  new   byte  [  nexa  ]  ; 
System  .  arraycopy  (  fc  ,  0  ,  la  ,  0  ,  al  )  ; 
fc  =  la  ; 
al  =  nexa  ; 
} 
n  =  ins  .  read  (  fc  ,  rp  ,  fc  .  length  -  rp  )  ; 
if  (  verbose  )  System  .  out  .  println  (  "  next n = "  +  n  +  " (rp="  +  rp  +  ", al="  +  al  +  ")"  )  ; 
if  (  n  >  0  )  rp  +=  n  ; 
} 
ins  .  close  (  )  ; 
n  =  rp  ; 
if  (  verbose  )  System  .  out  .  println  (  "RJavaClassLoader: loaded class "  +  name  +  ", "  +  n  +  " bytes"  )  ; 
try  { 
cl  =  defineClass  (  name  ,  fc  ,  0  ,  n  )  ; 
}  catch  (  Exception   dce  )  { 
defineException  =  dce  ; 
break  ; 
} 
if  (  verbose  )  System  .  out  .  println  (  "  defineClass('"  +  name  +  "') returned "  +  cl  )  ; 
return   cl  ; 
} 
}  catch  (  Exception   ex  )  { 
} 
} 
if  (  defineException  !=  null  )  throw  (  new   ClassNotFoundException  (  "Class not found - candidate class binary found but could not be loaded"  ,  defineException  )  )  ; 
if  (  verbose  )  System  .  out  .  println  (  "    >> ClassNotFoundException "  )  ; 
if  (  cl  ==  null  )  { 
throw  (  new   ClassNotFoundException  (  )  )  ; 
} 
return   cl  ; 
} 

public   URL   findResource  (  String   name  )  { 
if  (  verbose  )  System  .  out  .  println  (  "RJavaClassLoader: findResource('"  +  name  +  "')"  )  ; 
if  (  useSystem  )  { 
try  { 
URL   u  =  super  .  findResource  (  name  )  ; 
if  (  u  !=  null  )  { 
if  (  verbose  )  System  .  out  .  println  (  "RJavaClassLoader: found resource in "  +  u  +  " using URL loader."  )  ; 
return   u  ; 
} 
}  catch  (  Exception   fre  )  { 
} 
} 
if  (  verbose  )  System  .  out  .  println  (  " - resource not found with URL loader, trying alternative"  )  ; 
Enumeration   e  =  classPath  .  elements  (  )  ; 
while  (  e  .  hasMoreElements  (  )  )  { 
UnixFile   cp  =  (  UnixFile  )  e  .  nextElement  (  )  ; 
try  { 
if  (  cp   instanceof   UnixJarFile  )  { 
URL   u  =  (  (  UnixJarFile  )  cp  )  .  getResource  (  name  )  ; 
if  (  u  !=  null  )  { 
if  (  verbose  )  System  .  out  .  println  (  " - found in a JAR file, URL "  +  u  )  ; 
return   u  ; 
} 
}  else   if  (  cp   instanceof   UnixDirectory  )  { 
UnixFile   res_f  =  new   UnixFile  (  cp  .  getPath  (  )  +  "/"  +  name  )  ; 
if  (  res_f  .  isFile  (  )  )  { 
if  (  verbose  )  System  .  out  .  println  (  " - find as a file: "  +  res_f  )  ; 
return   res_f  .  toURL  (  )  ; 
} 
} 
}  catch  (  Exception   iox  )  { 
} 
} 
return   null  ; 
} 


public   void   addRLibrary  (  String   name  ,  String   path  )  { 
libMap  .  put  (  name  ,  new   UnixFile  (  path  )  )  ; 
} 




public   void   addClassPath  (  String   cp  )  { 
UnixFile   f  =  new   UnixFile  (  cp  )  ; 
if  (  useSystem  )  { 
try  { 
addURL  (  f  .  toURL  (  )  )  ; 
}  catch  (  Exception   ufe  )  { 
} 
} 
UnixFile   g  =  null  ; 
if  (  f  .  isFile  (  )  &&  f  .  getName  (  )  .  endsWith  (  ".jar"  )  )  { 
g  =  new   UnixJarFile  (  cp  )  ; 
}  else   if  (  f  .  isDirectory  (  )  )  { 
g  =  new   UnixDirectory  (  cp  )  ; 
} 
if  (  g  !=  null  &&  !  classPath  .  contains  (  g  )  )  { 
classPath  .  add  (  g  )  ; 
System  .  setProperty  (  "java.class.path"  ,  System  .  getProperty  (  "java.class.path"  )  +  File  .  pathSeparator  +  g  .  getPath  (  )  )  ; 
} 
} 




public   void   addClassPath  (  String  [  ]  cp  )  { 
int   i  =  0  ; 
while  (  i  <  cp  .  length  )  addClassPath  (  cp  [  i  ++  ]  )  ; 
} 




public   String  [  ]  getClassPath  (  )  { 
int   j  =  classPath  .  size  (  )  ; 
String  [  ]  s  =  new   String  [  j  ]  ; 
int   i  =  0  ; 
while  (  i  <  j  )  { 
s  [  i  ]  =  (  (  UnixFile  )  classPath  .  elementAt  (  i  )  )  .  getPath  (  )  ; 
i  ++  ; 
} 
return   s  ; 
} 

protected   String   findLibrary  (  String   name  )  { 
if  (  verbose  )  System  .  out  .  println  (  "RJavaClassLoader.findLibrary(\""  +  name  +  "\")"  )  ; 
UnixFile   u  =  (  UnixFile  )  libMap  .  get  (  name  )  ; 
String   s  =  null  ; 
if  (  u  !=  null  &&  u  .  exists  (  )  )  s  =  u  .  getPath  (  )  ; 
if  (  verbose  )  System  .  out  .  println  (  " - mapping to "  +  (  (  s  ==  null  )  ?  "<none>"  :  s  )  )  ; 
return   s  ; 
} 








public   void   bootClass  (  String   cName  ,  String   mName  ,  String  [  ]  args  )  throws   java  .  lang  .  IllegalAccessException  ,  java  .  lang  .  reflect  .  InvocationTargetException  ,  java  .  lang  .  NoSuchMethodException  ,  java  .  lang  .  ClassNotFoundException  { 
Class   c  =  findClass  (  cName  )  ; 
resolveClass  (  c  )  ; 
java  .  lang  .  reflect  .  Method   m  =  c  .  getMethod  (  mName  ,  new   Class  [  ]  {  String  [  ]  .  class  }  )  ; 
m  .  invoke  (  null  ,  new   Object  [  ]  {  args  }  )  ; 
} 







public   static   void   setDebug  (  int   level  )  { 
verbose  =  (  level  >  0  )  ; 
} 






public   static   String   u2w  (  String   fn  )  { 
return  (  File  .  separatorChar  !=  '/'  )  ?  fn  .  replace  (  '/'  ,  File  .  separatorChar  )  :  fn  ; 
} 


















public   static   void   main  (  String  [  ]  args  )  { 
String   rJavaPath  =  System  .  getProperty  (  "rjava.path"  )  ; 
if  (  rJavaPath  ==  null  )  { 
System  .  err  .  println  (  "ERROR: rjava.path is not set"  )  ; 
System  .  exit  (  2  )  ; 
} 
String   rJavaLib  =  System  .  getProperty  (  "rjava.lib"  )  ; 
if  (  rJavaLib  ==  null  )  { 
rJavaLib  =  rJavaPath  +  File  .  separator  +  "libs"  ; 
} 
RJavaClassLoader   cl  =  new   RJavaClassLoader  (  u2w  (  rJavaPath  )  ,  u2w  (  rJavaLib  )  )  ; 
String   mainClass  =  System  .  getProperty  (  "main.class"  )  ; 
if  (  mainClass  ==  null  ||  mainClass  .  length  (  )  <  1  )  { 
System  .  err  .  println  (  "WARNING: main.class not specified, assuming 'Main'"  )  ; 
mainClass  =  "Main"  ; 
} 
String   classPath  =  System  .  getProperty  (  "rjava.class.path"  )  ; 
if  (  classPath  !=  null  )  { 
StringTokenizer   st  =  new   StringTokenizer  (  classPath  ,  File  .  pathSeparator  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
String   dirname  =  u2w  (  st  .  nextToken  (  )  )  ; 
cl  .  addClassPath  (  dirname  )  ; 
} 
} 
try  { 
cl  .  bootClass  (  mainClass  ,  "main"  ,  args  )  ; 
}  catch  (  Exception   ex  )  { 
System  .  err  .  println  (  "ERROR: while running main method: "  +  ex  )  ; 
ex  .  printStackTrace  (  )  ; 
} 
} 

class   RJavaObjectInputStream   extends   ObjectInputStream  { 

public   RJavaObjectInputStream  (  InputStream   in  )  throws   IOException  { 
super  (  in  )  ; 
} 

protected   Class   resolveClass  (  ObjectStreamClass   desc  )  throws   ClassNotFoundException  { 
return   Class  .  forName  (  desc  .  getName  (  )  ,  false  ,  RJavaClassLoader  .  getPrimaryLoader  (  )  )  ; 
} 
} 








public   static   byte  [  ]  toByte  (  Object   object  )  throws   Exception  { 
ByteArrayOutputStream   os  =  new   ByteArrayOutputStream  (  )  ; 
ObjectOutputStream   oos  =  new   ObjectOutputStream  (  (  OutputStream  )  os  )  ; 
oos  .  writeObject  (  object  )  ; 
oos  .  close  (  )  ; 
return   os  .  toByteArray  (  )  ; 
} 








public   Object   toObject  (  byte  [  ]  byteArray  )  throws   Exception  { 
InputStream   is  =  new   ByteArrayInputStream  (  byteArray  )  ; 
RJavaObjectInputStream   ois  =  new   RJavaObjectInputStream  (  is  )  ; 
Object   o  =  (  Object  )  ois  .  readObject  (  )  ; 
ois  .  close  (  )  ; 
return   o  ; 
} 




public   static   Object   toObjectPL  (  byte  [  ]  byteArray  )  throws   Exception  { 
return   RJavaClassLoader  .  getPrimaryLoader  (  )  .  toObject  (  byteArray  )  ; 
} 
} 

