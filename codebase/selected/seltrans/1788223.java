package   codebook  .  java  ; 

import   org  .  gjt  .  sp  .  jedit  .  View  ; 
import   org  .  gjt  .  sp  .  jedit  .  GUIUtilities  ; 
import   org  .  gjt  .  sp  .  util  .  Log  ; 
import   java  .  net  .  URL  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  ObjectOutputStream  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  util  .  Scanner  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  regex  .  Pattern  ; 
import   java  .  util  .  regex  .  Matcher  ; 






public   class   ApiParser  { 

public   static   final   String  [  ]  MODIFIERS  =  new   String  [  ]  {  "final"  ,  "synchronized"  }  ; 

public   static   final   String  [  ]  VISIBILITY  =  new   String  [  ]  {  "public"  ,  "protected"  ,  "private"  }  ; 

private   static   HashMap  <  String  ,  ArrayList  <  String  >  >  packageLists  ; 






public   static   void   parseAPI  (  final   View   view  ,  String   _path  ,  final   boolean   remote  )  { 
final   String   sep  =  (  remote  )  ?  "/"  :  File  .  separator  ; 
final   String   path  =  (  _path  .  endsWith  (  sep  )  )  ?  _path  :  _path  +  sep  ; 
view  .  getStatus  (  )  .  setMessage  (  "Preparing to parse..."  )  ; 
new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
LinkedList  <  String  >  clsList  =  new   LinkedList  <  String  >  (  )  ; 
try  { 
String   classlist  =  readPage  (  path  +  "allclasses-frame.html"  ,  remote  )  ; 
if  (  classlist  ==  null  )  { 
view  .  getStatus  (  )  .  setMessage  (  ""  )  ; 
GUIUtilities  .  error  (  view  ,  "codebook.msg.invalid-api-target"  ,  null  )  ; 
return  ; 
} 
Pattern   p  =  Pattern  .  compile  (  "<A HREF=\".*?>.*?</A>"  )  ; 
Matcher   m  =  p  .  matcher  (  classlist  )  ; 
while  (  m  .  find  (  )  )  { 
String   page  =  classlist  .  substring  (  m  .  start  (  )  +  9  ,  classlist  .  indexOf  (  "\""  ,  m  .  start  (  )  +  9  )  )  ; 
clsList  .  add  (  path  +  page  )  ; 
} 
int   total  =  clsList  .  size  (  )  ; 
int   i  =  1  ; 
packageLists  =  new   HashMap  <  String  ,  ArrayList  <  String  >  >  (  )  ; 
for  (  final   String   cls  :  clsList  )  { 
view  .  getStatus  (  )  .  setMessage  (  "("  +  i  +  " / "  +  total  +  ") Parsing "  +  cls  +  "..."  )  ; 
parse  (  cls  ,  remote  )  ; 
i  ++  ; 
} 
}  catch  (  Exception   e  )  { 
GUIUtilities  .  error  (  view  ,  "codebook.msg.error-parsing-api"  ,  null  )  ; 
packageLists  =  null  ; 
e  .  printStackTrace  (  )  ; 
} 
try  { 
File   pkgsFolder  =  new   File  (  JavaRunner  .  dir  +  "api-pkg"  )  ; 
if  (  !  pkgsFolder  .  exists  (  )  )  pkgsFolder  .  mkdir  (  )  ; 
for  (  String   key  :  packageLists  .  keySet  (  )  )  { 
File   dat  =  new   File  (  JavaRunner  .  dir  +  "api-pkg"  +  File  .  separator  +  key  )  ; 
ObjectOutputStream   out  =  new   ObjectOutputStream  (  new   FileOutputStream  (  dat  )  )  ; 
out  .  writeObject  (  packageLists  .  get  (  key  )  )  ; 
out  .  close  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
}  finally  { 
packageLists  =  null  ; 
} 
view  .  getStatus  (  )  .  setMessageAndClear  (  "Api parsing complete"  )  ; 
} 
}  )  .  start  (  )  ; 
} 

public   static   void   parseAPI  (  View   view  ,  String   path  )  { 
parseAPI  (  view  ,  path  ,  path  .  startsWith  (  "http://"  )  )  ; 
} 






public   static   void   parse  (  String   path  ,  boolean   remote  )  { 
JavaClass   jcl  =  new   JavaClass  (  )  ; 
String   text  =  null  ; 
try  { 
text  =  readPage  (  path  ,  remote  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  0  )  ; 
} 
String   classData  =  pullSection  (  text  ,  "<!-- ======== START OF CLASS DATA ======== -->"  ,  false  )  ; 
if  (  classData  .  length  (  )  >  0  )  { 
int   br  =  classData  .  indexOf  (  "<BR>"  )  ; 
String   pkg  =  clearTags  (  classData  .  substring  (  0  ,  br  )  )  .  trim  (  )  ; 
int   pre  =  classData  .  indexOf  (  "</H2>"  ,  br  )  ; 
String   title  =  clearTags  (  classData  .  substring  (  br  ,  pre  )  )  .  trim  (  )  ; 
String   name  =  title  .  substring  (  title  .  indexOf  (  " "  )  +  1  )  ; 
if  (  name  .  indexOf  (  "<"  )  !=  -  1  )  name  =  name  .  substring  (  0  ,  name  .  indexOf  (  "<"  )  )  ; 
jcl  .  setPackage  (  pkg  )  ; 
jcl  .  setName  (  name  )  ; 
if  (  packageLists  .  get  (  pkg  )  ==  null  )  { 
packageLists  .  put  (  pkg  ,  new   ArrayList  <  String  >  (  )  )  ; 
} 
packageLists  .  get  (  pkg  )  .  add  (  name  )  ; 
} 
String   fieldData  =  pullSection  (  text  ,  "<!-- =========== FIELD SUMMARY =========== -->"  ,  true  )  ; 
if  (  fieldData  .  length  (  )  >  0  )  { 
Pattern   p  =  Pattern  .  compile  (  "<TR .*?>.*?</TR>"  )  ; 
Matcher   m  =  p  .  matcher  (  fieldData  )  ; 
while  (  m  .  find  (  )  )  { 
String   field  =  fieldData  .  substring  (  m  .  start  (  )  ,  m  .  end  (  )  )  ; 
if  (  field  .  indexOf  (  "Deprecated."  )  !=  -  1  ||  field  .  indexOf  (  "Field Summary"  )  !=  -  1  )  continue  ; 
int   br  =  field  .  indexOf  (  "<BR>"  )  ; 
String   field_value  =  clearTags  (  field  .  substring  (  0  ,  br  )  )  .  trim  (  )  ; 
String   field_desc  =  clearTags  (  field  .  substring  (  br  ,  field  .  length  (  )  )  )  .  trim  (  )  ; 
boolean   is_static  =  false  ; 
if  (  field_value  .  startsWith  (  "static "  )  )  { 
is_static  =  true  ; 
field_value  =  field_value  .  substring  (  7  )  ; 
} 
String   vis  =  "public"  ; 
for  (  int   i  =  0  ;  i  <  VISIBILITY  .  length  ;  i  ++  )  { 
if  (  field_value  .  startsWith  (  VISIBILITY  [  i  ]  +  " "  )  )  { 
vis  =  VISIBILITY  [  i  ]  ; 
field_value  =  field_value  .  substring  (  VISIBILITY  [  i  ]  .  length  (  )  +  1  )  ; 
} 
} 
int   space  =  field_value  .  indexOf  (  " "  )  ; 
String   field_type  =  field_value  .  substring  (  0  ,  space  )  ; 
field_value  =  field_value  .  substring  (  space  +  1  )  ; 
jcl  .  addField  (  field_value  ,  field_type  ,  field_desc  ,  vis  ,  is_static  )  ; 
} 
} 
String   constructorData  =  pullSection  (  text  ,  "<!-- ======== CONSTRUCTOR SUMMARY ======== -->"  ,  true  )  ; 
if  (  constructorData  .  length  (  )  >  0  )  { 
Pattern   p  =  Pattern  .  compile  (  "<TR .*?>.*?</TR>"  )  ; 
Matcher   m  =  p  .  matcher  (  constructorData  )  ; 
while  (  m  .  find  (  )  )  { 
String   con  =  constructorData  .  substring  (  m  .  start  (  )  ,  m  .  end  (  )  )  ; 
if  (  con  .  indexOf  (  "Deprecated."  )  !=  -  1  ||  con  .  indexOf  (  "Constructor Summary"  )  !=  -  1  )  continue  ; 
int   br  =  con  .  indexOf  (  "<BR>"  )  ; 
String   con_value  =  clearTags  (  con  .  substring  (  0  ,  br  )  )  .  trim  (  )  ; 
String   con_desc  =  clearTags  (  con  .  substring  (  br  ,  con  .  length  (  )  )  )  .  trim  (  )  ; 
jcl  .  addConstructor  (  con_value  ,  con_desc  )  ; 
} 
} 
String   methodData  =  pullSection  (  text  ,  "<!-- ========== METHOD SUMMARY =========== -->"  ,  true  )  ; 
if  (  methodData  .  length  (  )  >  0  )  { 
Pattern   p  =  Pattern  .  compile  (  "<TR .*?>.*?</TR>"  )  ; 
Matcher   m  =  p  .  matcher  (  methodData  )  ; 
while  (  m  .  find  (  )  )  { 
String   method  =  methodData  .  substring  (  m  .  start  (  )  ,  m  .  end  (  )  )  ; 
if  (  method  .  indexOf  (  "Deprecated."  )  !=  -  1  ||  method  .  indexOf  (  "Method Summary"  )  !=  -  1  )  continue  ; 
int   br  =  method  .  indexOf  (  "<BR>"  ,  method  .  indexOf  (  "</FONT>"  )  )  ; 
if  (  br  ==  -  1  )  { 
int   end  =  methodData  .  indexOf  (  "</TR>"  ,  m  .  end  (  )  +  1  )  ; 
method  =  methodData  .  substring  (  m  .  start  (  )  ,  end  )  ; 
br  =  method  .  indexOf  (  "<BR>"  ,  method  .  indexOf  (  "</FONT>"  )  )  ; 
} 
String   method_value  =  clearTags  (  method  .  substring  (  0  ,  br  )  )  .  trim  (  )  ; 
String   method_desc  =  clearTags  (  method  .  substring  (  br  ,  method  .  length  (  )  )  )  .  trim  (  )  ; 
boolean   is_static  =  false  ; 
if  (  method_value  .  startsWith  (  "static "  )  )  { 
method_value  =  method_value  .  substring  (  7  )  ; 
is_static  =  true  ; 
} 
String   vis  =  "public"  ; 
for  (  int   i  =  0  ;  i  <  VISIBILITY  .  length  ;  i  ++  )  { 
if  (  method_value  .  startsWith  (  VISIBILITY  [  i  ]  +  " "  )  )  { 
vis  =  VISIBILITY  [  i  ]  ; 
method_value  =  method_value  .  substring  (  VISIBILITY  [  i  ]  .  length  (  )  +  1  )  ; 
} 
} 
int   space  =  method_value  .  lastIndexOf  (  " "  ,  method_value  .  indexOf  (  "("  )  )  ; 
String   method_type  =  method_value  .  substring  (  0  ,  space  )  ; 
method_value  =  method_value  .  substring  (  space  +  1  )  ; 
jcl  .  addMethod  (  method_value  ,  method_type  ,  method_desc  ,  vis  ,  is_static  )  ; 
} 
} 
jcl  .  save  (  )  ; 
} 

public   static   void   parse  (  String   path  )  { 
parse  (  path  ,  path  .  startsWith  (  "http://"  )  )  ; 
} 









private   static   String   pullSection  (  String   text  ,  String   header  ,  boolean   isTable  )  { 
int   i  =  text  .  indexOf  (  header  )  ; 
if  (  i  ==  -  1  )  return  ""  ; 
i  +=  header  .  length  (  )  ; 
int   j  =  (  isTable  )  ?  text  .  indexOf  (  "<!--"  ,  text  .  indexOf  (  "<TABLE "  ,  i  )  )  :  text  .  indexOf  (  "<!--"  ,  i  )  ; 
return   text  .  substring  (  i  ,  j  )  ; 
} 






private   static   String   clearTags  (  String   text  )  { 
text  =  text  .  replace  (  "</CODE>"  ,  " "  )  ; 
text  =  text  .  replace  (  "&nbsp;"  ,  " "  )  ; 
text  =  text  .  replaceAll  (  "<.*?>"  ,  ""  )  ; 
text  =  text  .  replace  (  "&gt;"  ,  ">"  )  ; 
text  =  text  .  replace  (  "&lt;"  ,  "<"  )  ; 
text  =  text  .  replaceAll  (  "\\s{2,}"  ,  " "  )  ; 
text  =  text  .  replace  (  "abstract "  ,  ""  )  ; 
text  =  text  .  replace  (  "synchronized "  ,  ""  )  ; 
text  =  text  .  replace  (  "native "  ,  ""  )  ; 
text  =  text  .  replace  (  "final "  ,  ""  )  ; 
text  =  text  .  replace  (  "strictfp "  ,  ""  )  ; 
return   text  ; 
} 






private   static   String   readPage  (  String   path  ,  boolean   remote  )  { 
StringBuffer   text  =  new   StringBuffer  (  ""  )  ; 
InputStream   in  =  null  ; 
try  { 
if  (  remote  )  in  =  new   URL  (  path  )  .  openConnection  (  )  .  getInputStream  (  )  ;  else   in  =  new   FileInputStream  (  new   File  (  path  )  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
Scanner   read  =  new   Scanner  (  new   InputStreamReader  (  in  )  )  ; 
while  (  read  .  hasNext  (  )  )  { 
text  .  append  (  read  .  next  (  )  +  " "  )  ; 
} 
final   String   tagRegex  =  "<.*?>"  ; 
Pattern   tagPattern  =  Pattern  .  compile  (  tagRegex  )  ; 
Matcher   tagMatcher  =  tagPattern  .  matcher  (  text  )  ; 
while  (  tagMatcher  .  find  (  )  )  { 
StringBuffer   str  =  new   StringBuffer  (  text  .  substring  (  tagMatcher  .  start  (  )  ,  tagMatcher  .  end  (  )  )  )  ; 
final   String   quoteRegex  =  "\".*?\"|'.*?'"  ; 
Pattern   quotePattern  =  Pattern  .  compile  (  quoteRegex  )  ; 
Matcher   quoteMatcher  =  quotePattern  .  matcher  (  str  )  ; 
int   i  =  0  ; 
while  (  quoteMatcher  .  find  (  )  )  { 
String   toCaps  =  str  .  substring  (  i  ,  quoteMatcher  .  start  (  )  )  ; 
str  .  replace  (  i  ,  quoteMatcher  .  start  (  )  ,  toCaps  .  toUpperCase  (  )  )  ; 
i  =  quoteMatcher  .  end  (  )  ; 
} 
text  .  replace  (  tagMatcher  .  start  (  )  ,  tagMatcher  .  end  (  )  ,  str  .  toString  (  )  )  ; 
} 
return   text  .  toString  (  )  ; 
} 






public   static   class   JavaClass   implements   Serializable  { 

private   String   pkg  ; 

private   String   name  ; 

private   ArrayList  <  String  [  ]  >  fields  ; 

private   ArrayList  <  String  [  ]  >  constructors  ; 

private   ArrayList  <  String  [  ]  >  methods  ; 

JavaClass  (  )  { 
fields  =  new   ArrayList  <  String  [  ]  >  (  )  ; 
constructors  =  new   ArrayList  <  String  [  ]  >  (  )  ; 
methods  =  new   ArrayList  <  String  [  ]  >  (  )  ; 
} 

public   void   setPackage  (  String   pkg  )  { 
this  .  pkg  =  pkg  ; 
} 

public   void   setName  (  String   name  )  { 
this  .  name  =  name  ; 
} 

public   void   addField  (  String   val  ,  String   type  ,  String   desc  ,  String   vis  ,  boolean   is_static  )  { 
String  [  ]  field  =  new   String  [  ]  {  val  ,  type  ,  desc  ,  vis  ,  is_static  ?  "static"  :  "instance"  }  ; 
fields  .  add  (  field  )  ; 
} 

public   void   addConstructor  (  String   val  ,  String   desc  )  { 
String  [  ]  con  =  new   String  [  ]  {  val  ,  desc  }  ; 
constructors  .  add  (  con  )  ; 
} 

public   void   addMethod  (  String   val  ,  String   type  ,  String   desc  ,  String   vis  ,  boolean   is_static  )  { 
String  [  ]  method  =  new   String  [  ]  {  val  ,  type  ,  desc  ,  vis  ,  is_static  ?  "static"  :  "instance"  }  ; 
methods  .  add  (  method  )  ; 
} 

public   String   getPackage  (  )  { 
return   this  .  pkg  ; 
} 

public   String   getName  (  )  { 
return   this  .  name  ; 
} 

public   ArrayList  <  String  [  ]  >  getFields  (  )  { 
return   this  .  fields  ; 
} 

public   ArrayList  <  String  [  ]  >  getConstructors  (  )  { 
return   this  .  constructors  ; 
} 

public   ArrayList  <  String  [  ]  >  getMethods  (  )  { 
return   this  .  methods  ; 
} 

public   void   save  (  )  { 
String   _name  =  new   String  (  name  )  ; 
int   lt  =  -  1  ; 
if  (  (  lt  =  _name  .  indexOf  (  "<"  )  )  !=  -  1  )  _name  =  _name  .  substring  (  0  ,  lt  )  ; 
String   s  =  File  .  separator  ; 
String   classDir  =  JavaRunner  .  dir  +  "api-cls"  +  s  +  _name  +  s  ; 
Log  .  log  (  Log  .  DEBUG  ,  ApiParser  .  class  ,  "classdir = "  +  classDir  )  ; 
try  { 
File   classDirFile  =  new   File  (  classDir  )  ; 
if  (  !  classDirFile  .  exists  (  )  )  classDirFile  .  mkdirs  (  )  ; 
File   dat  =  new   File  (  classDir  +  pkg  )  ; 
ObjectOutputStream   out  =  new   ObjectOutputStream  (  new   FileOutputStream  (  dat  )  )  ; 
out  .  writeObject  (  this  )  ; 
out  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
System  .  exit  (  0  )  ; 
} 
} 
} 
} 

