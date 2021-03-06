import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  nio  .  ByteOrder  ; 
import   java  .  text  .  DecimalFormat  ; 
import   java  .  text  .  MessageFormat  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  ResourceBundle  ; 
import   javax  .  xml  .  transform  .  Templates  ; 
import   javax  .  xml  .  transform  .  Transformer  ; 
import   javax  .  xml  .  transform  .  TransformerConfigurationException  ; 
import   javax  .  xml  .  transform  .  TransformerFactory  ; 
import   javax  .  xml  .  transform  .  sax  .  SAXTransformerFactory  ; 
import   javax  .  xml  .  transform  .  sax  .  TransformerHandler  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamResult  ; 
import   javax  .  xml  .  transform  .  stream  .  StreamSource  ; 
import   gnu  .  getopt  .  Getopt  ; 
import   gnu  .  getopt  .  LongOpt  ; 
import   org  .  dcm4che  .  data  .  Dataset  ; 
import   org  .  dcm4che  .  data  .  DcmEncodeParam  ; 
import   org  .  dcm4che  .  data  .  DcmObjectFactory  ; 
import   org  .  dcm4che  .  data  .  DcmParseException  ; 
import   org  .  dcm4che  .  dict  .  DictionaryFactory  ; 
import   org  .  dcm4che  .  dict  .  TagDictionary  ; 
import   org  .  dcm4che  .  dict  .  Tags  ; 
import   org  .  dcm4che  .  dict  .  VRMap  ; 
import   org  .  dcm4che  .  media  .  DirBuilder  ; 
import   org  .  dcm4che  .  media  .  DirBuilderFactory  ; 
import   org  .  dcm4che  .  media  .  DirBuilderPref  ; 
import   org  .  dcm4che  .  media  .  DirReader  ; 
import   org  .  dcm4che  .  media  .  DirRecord  ; 
import   org  .  dcm4che  .  media  .  DirWriter  ; 
import   org  .  dcm4che  .  util  .  UIDGenerator  ; 






public   class   DcmDir  { 

private   static   final   String   FILE_SET_INFO  =  "=== File Set Info ==="  ; 

private   static   final   String  [  ]  QRLEVEL  =  {  "PATIENT"  ,  "STUDY"  ,  "SERIES"  ,  "IMAGE"  }  ; 

private   static   ResourceBundle   messages  =  ResourceBundle  .  getBundle  (  "DcmDir"  ,  Locale  .  getDefault  (  )  )  ; 

private   static   final   DcmObjectFactory   dof  =  DcmObjectFactory  .  getInstance  (  )  ; 

private   static   final   DirBuilderFactory   fact  =  DirBuilderFactory  .  getInstance  (  )  ; 

private   final   TagDictionary   dict  =  DictionaryFactory  .  getInstance  (  )  .  getDefaultTagDictionary  (  )  ; 

private   File   dirFile  =  null  ; 

private   File   readMeFile  =  null  ; 

private   String   readMeCharset  =  null  ; 

private   boolean   skipGroupLen  =  true  ; 

private   boolean   undefSeqLen  =  true  ; 

private   boolean   undefItemLen  =  true  ; 

private   String   id  =  ""  ; 

private   String   uid  =  null  ; 

private   Integer   maxlen  =  new   Integer  (  79  )  ; 

private   Integer   vallen  =  new   Integer  (  64  )  ; 

private   boolean   onlyInUse  =  false  ; 

private   boolean   ignoreCase  =  false  ; 

private   final   Properties   cfg  ; 

private   final   Dataset   keys  =  dof  .  newDataset  (  )  ; 

private   final   int   qrLevel  ; 

private   static   HashSet   patientIDs  =  new   HashSet  (  )  ; 

private   static   HashSet   studyUIDs  =  new   HashSet  (  )  ; 

private   static   HashSet   seriesUIDs  =  new   HashSet  (  )  ; 

private   static   HashSet   sopInstUIDs  =  new   HashSet  (  )  ; 

private   LinkedList   fileIDs  =  new   LinkedList  (  )  ; 

private   static   final   LongOpt  [  ]  LONG_OPTS  =  new   LongOpt  [  ]  {  new   LongOpt  (  "onlyInUse"  ,  LongOpt  .  NO_ARGUMENT  ,  null  ,  3  )  ,  new   LongOpt  (  "ignoreCase"  ,  LongOpt  .  NO_ARGUMENT  ,  null  ,  3  )  ,  new   LongOpt  (  "maxlen"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  2  )  ,  new   LongOpt  (  "vallen"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  2  )  ,  new   LongOpt  (  "fs-uid"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  2  )  ,  new   LongOpt  (  "fs-id"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  2  )  ,  new   LongOpt  (  "readme"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  2  )  ,  new   LongOpt  (  "readme-charset"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  2  )  ,  new   LongOpt  (  "grouplen"  ,  LongOpt  .  NO_ARGUMENT  ,  null  ,  3  )  ,  new   LongOpt  (  "seqlen"  ,  LongOpt  .  NO_ARGUMENT  ,  null  ,  3  )  ,  new   LongOpt  (  "itemlen"  ,  LongOpt  .  NO_ARGUMENT  ,  null  ,  3  )  ,  new   LongOpt  (  "pat"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  'p'  )  ,  new   LongOpt  (  "study"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  's'  )  ,  new   LongOpt  (  "series"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  'e'  )  ,  new   LongOpt  (  "sop"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  'o'  )  ,  new   LongOpt  (  "key"  ,  LongOpt  .  REQUIRED_ARGUMENT  ,  null  ,  'y'  )  ,  new   LongOpt  (  "help"  ,  LongOpt  .  NO_ARGUMENT  ,  null  ,  'h'  )  ,  new   LongOpt  (  "version"  ,  LongOpt  .  NO_ARGUMENT  ,  null  ,  'v'  )  }  ; 







public   static   void   main  (  String   args  [  ]  )  throws   Exception  { 
Getopt   g  =  new   Getopt  (  "dcmdir"  ,  args  ,  "c:t:q:a:x:X:z:P:"  ,  LONG_OPTS  )  ; 
Properties   cfg  =  loadConfig  (  )  ; 
int   cmd  =  0  ; 
File   dirfile  =  null  ; 
int   c  ; 
while  (  (  c  =  g  .  getopt  (  )  )  !=  -  1  )  { 
switch  (  c  )  { 
case   2  : 
cfg  .  put  (  LONG_OPTS  [  g  .  getLongind  (  )  ]  .  getName  (  )  ,  g  .  getOptarg  (  )  )  ; 
break  ; 
case   3  : 
cfg  .  put  (  LONG_OPTS  [  g  .  getLongind  (  )  ]  .  getName  (  )  ,  "<yes>"  )  ; 
break  ; 
case  'c'  : 
case  't'  : 
case  'q'  : 
case  'a'  : 
case  'x'  : 
case  'X'  : 
case  'P'  : 
case  'z'  : 
cmd  =  c  ; 
dirfile  =  new   File  (  g  .  getOptarg  (  )  )  ; 
break  ; 
case  'p'  : 
patientIDs  .  add  (  g  .  getOptarg  (  )  )  ; 
break  ; 
case  's'  : 
studyUIDs  .  add  (  g  .  getOptarg  (  )  )  ; 
break  ; 
case  'e'  : 
seriesUIDs  .  add  (  g  .  getOptarg  (  )  )  ; 
break  ; 
case  'o'  : 
sopInstUIDs  .  add  (  g  .  getOptarg  (  )  )  ; 
break  ; 
case  'y'  : 
putKey  (  cfg  ,  g  .  getOptarg  (  )  )  ; 
break  ; 
case  'v'  : 
exit  (  messages  .  getString  (  "version"  )  ,  false  )  ; 
case  'h'  : 
exit  (  messages  .  getString  (  "usage"  )  ,  false  )  ; 
case  '?'  : 
exit  (  null  ,  true  )  ; 
break  ; 
} 
} 
if  (  cmd  ==  0  )  { 
exit  (  messages  .  getString  (  "missing"  )  ,  true  )  ; 
} 
try  { 
DcmDir   dcmdir  =  new   DcmDir  (  dirfile  ,  cfg  )  ; 
switch  (  cmd  )  { 
case   0  : 
exit  (  messages  .  getString  (  "missing"  )  ,  true  )  ; 
break  ; 
case  'c'  : 
dcmdir  .  create  (  args  ,  g  .  getOptind  (  )  )  ; 
break  ; 
case  't'  : 
dcmdir  .  list  (  )  ; 
break  ; 
case  'q'  : 
dcmdir  .  query  (  )  ; 
break  ; 
case  'a'  : 
dcmdir  .  append  (  args  ,  g  .  getOptind  (  )  )  ; 
break  ; 
case  'x'  : 
case  'X'  : 
dcmdir  .  remove  (  args  ,  g  .  getOptind  (  )  ,  cmd  ==  'X'  )  ; 
break  ; 
case  'z'  : 
dcmdir  .  compact  (  )  ; 
break  ; 
case  'P'  : 
dcmdir  .  purge  (  )  ; 
break  ; 
default  : 
throw   new   RuntimeException  (  )  ; 
} 
}  catch  (  IllegalArgumentException   e  )  { 
e  .  printStackTrace  (  )  ; 
exit  (  e  .  getMessage  (  )  ,  true  )  ; 
} 
} 

private   static   void   putKey  (  Properties   cfg  ,  String   s  )  { 
int   pos  =  s  .  indexOf  (  ':'  )  ; 
if  (  pos  ==  -  1  )  { 
cfg  .  put  (  "key."  +  s  ,  ""  )  ; 
}  else  { 
cfg  .  put  (  "key."  +  s  .  substring  (  0  ,  pos  )  ,  s  .  substring  (  pos  +  1  )  )  ; 
} 
} 

DcmDir  (  File   dirfile  ,  Properties   cfg  )  { 
this  .  dirFile  =  dirfile  ; 
this  .  cfg  =  cfg  ; 
String   rm  =  replace  (  cfg  .  getProperty  (  "readme"  )  ,  "<none>"  ,  null  )  ; 
if  (  rm  !=  null  )  { 
this  .  readMeFile  =  new   File  (  rm  )  ; 
this  .  readMeCharset  =  replace  (  cfg  .  getProperty  (  "readme-charset"  )  ,  "<none>"  ,  null  )  ; 
} 
this  .  id  =  replace  (  cfg  .  getProperty  (  "fs-id"  ,  ""  )  ,  "<none>"  ,  ""  )  ; 
this  .  uid  =  replace  (  cfg  .  getProperty  (  "fs-uid"  ,  ""  )  ,  "<auto>"  ,  ""  )  ; 
this  .  maxlen  =  new   Integer  (  cfg  .  getProperty  (  "maxlen"  ,  "79"  )  )  ; 
this  .  vallen  =  new   Integer  (  cfg  .  getProperty  (  "maxlen"  ,  "64"  )  )  ; 
this  .  skipGroupLen  =  !  "<yes>"  .  equals  (  cfg  .  getProperty  (  "grouplen"  )  )  ; 
this  .  undefSeqLen  =  !  "<yes>"  .  equals  (  cfg  .  getProperty  (  "seqlen"  )  )  ; 
this  .  undefItemLen  =  !  "<yes>"  .  equals  (  cfg  .  getProperty  (  "itemlen"  )  )  ; 
this  .  onlyInUse  =  "<yes>"  .  equals  (  cfg  .  getProperty  (  "onlyInUse"  )  )  ; 
this  .  ignoreCase  =  "<yes>"  .  equals  (  cfg  .  getProperty  (  "ignoreCase"  )  )  ; 
for  (  Enumeration   it  =  cfg  .  keys  (  )  ;  it  .  hasMoreElements  (  )  ;  )  { 
String   key  =  (  String  )  it  .  nextElement  (  )  ; 
if  (  key  .  startsWith  (  "key."  )  )  { 
try  { 
keys  .  putXX  (  Tags  .  forName  (  key  .  substring  (  4  )  )  ,  cfg  .  getProperty  (  key  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   IllegalArgumentException  (  "Illegal key - "  +  key  +  "="  +  cfg  .  getProperty  (  key  )  )  ; 
} 
} 
} 
String   qrl  =  keys  .  getString  (  Tags  .  QueryRetrieveLevel  ,  QRLEVEL  [  1  ]  )  ; 
this  .  qrLevel  =  Arrays  .  asList  (  QRLEVEL  )  .  indexOf  (  qrl  )  ; 
if  (  qrLevel  ==  -  1  )  { 
throw   new   IllegalArgumentException  (  "Illegal Query Retrieve Level - "  +  qrl  )  ; 
} 
keys  .  remove  (  Tags  .  QueryRetrieveLevel  )  ; 
} 

private   TransformerHandler   getTransformerHandler  (  SAXTransformerFactory   tf  ,  Templates   tpl  ,  String   dsprompt  )  throws   TransformerConfigurationException  ,  IOException  { 
TransformerHandler   th  =  tf  .  newTransformerHandler  (  tpl  )  ; 
th  .  setResult  (  new   StreamResult  (  System  .  out  )  )  ; 
Transformer   t  =  th  .  getTransformer  (  )  ; 
t  .  setParameter  (  "maxlen"  ,  maxlen  )  ; 
t  .  setParameter  (  "vallen"  ,  vallen  )  ; 
t  .  setParameter  (  "vallen"  ,  vallen  )  ; 
t  .  setParameter  (  "dsprompt"  ,  dsprompt  )  ; 
return   th  ; 
} 







public   void   list  (  )  throws   IOException  ,  TransformerConfigurationException  { 
SAXTransformerFactory   tf  =  (  SAXTransformerFactory  )  TransformerFactory  .  newInstance  (  )  ; 
Templates   xslt  =  loadDcmDirXSL  (  tf  )  ; 
DirReader   reader  =  fact  .  newDirReader  (  dirFile  )  ; 
reader  .  getFileSetInfo  (  )  .  writeFile2  (  getTransformerHandler  (  tf  ,  xslt  ,  FILE_SET_INFO  )  ,  dict  ,  null  ,  128  ,  null  )  ; 
try  { 
list  (  ""  ,  reader  .  getFirstRecord  (  onlyInUse  )  ,  tf  ,  xslt  )  ; 
}  finally  { 
reader  .  close  (  )  ; 
} 
} 

private   static   Templates   loadDcmDirXSL  (  SAXTransformerFactory   tf  )  throws   IOException  ,  TransformerConfigurationException  { 
ClassLoader   cl  =  Thread  .  currentThread  (  )  .  getContextClassLoader  (  )  ; 
InputStream   in  =  cl  .  getResourceAsStream  (  "Dcm2Xml2.xsl"  )  ; 
return   tf  .  newTemplates  (  new   StreamSource  (  in  )  )  ; 
} 

private   static   final   DecimalFormat   POS_FORMAT  =  new   DecimalFormat  (  "0000 DIRECTORY RECORD - "  )  ; 

private   void   list  (  String   prefix  ,  DirRecord   first  ,  SAXTransformerFactory   tf  ,  Templates   xslt  )  throws   IOException  ,  TransformerConfigurationException  { 
int   count  =  1  ; 
for  (  DirRecord   rec  =  first  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  onlyInUse  )  )  { 
Dataset   ds  =  rec  .  getDataset  (  )  ; 
String   prompt  =  POS_FORMAT  .  format  (  ds  .  getItemOffset  (  )  )  +  prefix  +  count  +  " ["  +  rec  .  getType  (  )  +  "]"  ; 
ds  .  writeDataset2  (  getTransformerHandler  (  tf  ,  xslt  ,  prompt  )  ,  dict  ,  null  ,  128  ,  null  )  ; 
list  (  prefix  +  count  +  '.'  ,  rec  .  getFirstChild  (  onlyInUse  )  ,  tf  ,  xslt  )  ; 
++  count  ; 
} 
} 







public   void   query  (  )  throws   IOException  ,  TransformerConfigurationException  { 
SAXTransformerFactory   tf  =  (  SAXTransformerFactory  )  TransformerFactory  .  newInstance  (  )  ; 
Templates   xslt  =  loadDcmDirXSL  (  tf  )  ; 
DirReader   reader  =  fact  .  newDirReader  (  dirFile  )  ; 
try  { 
query  (  ""  ,  1  ,  0  ,  reader  .  getFirstRecordBy  (  null  ,  keys  ,  ignoreCase  )  ,  tf  ,  xslt  )  ; 
}  finally  { 
reader  .  close  (  )  ; 
} 
} 

private   int   query  (  String   prefix  ,  int   no  ,  int   level  ,  DirRecord   dr  ,  SAXTransformerFactory   tf  ,  Templates   xslt  )  throws   IOException  ,  TransformerConfigurationException  { 
int   count  =  1  ; 
for  (  ;  dr  !=  null  ;  dr  =  dr  .  getNextSiblingBy  (  null  ,  keys  ,  ignoreCase  )  )  { 
if  (  level  >=  qrLevel  )  { 
Dataset   ds  =  dr  .  getDataset  (  )  ; 
String   prompt  =  POS_FORMAT  .  format  (  ds  .  getItemOffset  (  )  )  +  prefix  +  count  +  " ["  +  dr  .  getType  (  )  +  "] #"  +  no  ; 
ds  .  writeDataset  (  getTransformerHandler  (  tf  ,  xslt  ,  prompt  )  ,  dict  )  ; 
++  no  ; 
}  else  { 
no  =  query  (  prefix  +  count  +  '.'  ,  no  ,  level  +  1  ,  dr  .  getFirstChildBy  (  null  ,  keys  ,  ignoreCase  )  ,  tf  ,  xslt  )  ; 
} 
++  count  ; 
} 
return   no  ; 
} 

private   DcmEncodeParam   encodeParam  (  )  { 
return   new   DcmEncodeParam  (  ByteOrder  .  LITTLE_ENDIAN  ,  true  ,  false  ,  false  ,  skipGroupLen  ,  undefSeqLen  ,  undefItemLen  )  ; 
} 








public   void   create  (  String  [  ]  args  ,  int   off  )  throws   IOException  { 
if  (  uid  ==  null  ||  uid  .  length  (  )  ==  0  )  { 
uid  =  UIDGenerator  .  getInstance  (  )  .  createUID  (  )  ; 
} 
File   rootDir  =  dirFile  .  getParentFile  (  )  ; 
if  (  rootDir  !=  null  &&  !  rootDir  .  exists  (  )  )  { 
rootDir  .  mkdirs  (  )  ; 
} 
DirWriter   writer  =  fact  .  newDirWriter  (  dirFile  ,  uid  ,  id  ,  readMeFile  ,  readMeCharset  ,  encodeParam  (  )  )  ; 
try  { 
build  (  writer  ,  args  ,  off  )  ; 
}  finally  { 
writer  .  close  (  )  ; 
} 
} 








public   void   append  (  String  [  ]  args  ,  int   off  )  throws   IOException  { 
DirWriter   writer  =  fact  .  newDirWriter  (  dirFile  ,  encodeParam  (  )  )  ; 
try  { 
build  (  writer  ,  args  ,  off  )  ; 
}  finally  { 
writer  .  close  (  )  ; 
} 
} 

private   void   addDirBuilderPrefElem  (  HashMap   map  ,  String   key  )  { 
if  (  !  key  .  startsWith  (  "dir."  )  )  { 
return  ; 
} 
int   pos2  =  key  .  lastIndexOf  (  '.'  )  ; 
String   type  =  key  .  substring  (  4  ,  pos2  )  .  replace  (  '_'  ,  ' '  )  ; 
Dataset   ds  =  (  Dataset  )  map  .  get  (  type  )  ; 
if  (  ds  ==  null  )  { 
map  .  put  (  type  ,  ds  =  dof  .  newDataset  (  )  )  ; 
} 
int   tag  =  Tags  .  forName  (  key  .  substring  (  pos2  +  1  )  )  ; 
ds  .  putXX  (  tag  ,  VRMap  .  DEFAULT  .  lookup  (  tag  )  )  ; 
} 

private   DirBuilderPref   getDirBuilderPref  (  )  { 
HashMap   map  =  new   HashMap  (  )  ; 
for  (  Enumeration   en  =  cfg  .  keys  (  )  ;  en  .  hasMoreElements  (  )  ;  )  { 
addDirBuilderPrefElem  (  map  ,  (  String  )  en  .  nextElement  (  )  )  ; 
} 
DirBuilderPref   pref  =  fact  .  newDirBuilderPref  (  )  ; 
for  (  Iterator   it  =  map  .  entrySet  (  )  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
Map  .  Entry   entry  =  (  Map  .  Entry  )  it  .  next  (  )  ; 
pref  .  setFilterForRecordType  (  (  String  )  entry  .  getKey  (  )  ,  (  Dataset  )  entry  .  getValue  (  )  )  ; 
} 
return   pref  ; 
} 

private   void   build  (  DirWriter   w  ,  String  [  ]  args  ,  int   off  )  throws   IOException  { 
DirBuilderPref   pref  =  getDirBuilderPref  (  )  ; 
long   t1  =  System  .  currentTimeMillis  (  )  ; 
int  [  ]  counter  =  new   int  [  2  ]  ; 
DirBuilder   builder  =  fact  .  newDirBuilder  (  w  ,  pref  )  ; 
for  (  int   i  =  off  ;  i  <  args  .  length  ;  ++  i  )  { 
append  (  builder  ,  new   File  (  args  [  i  ]  )  ,  counter  )  ; 
} 
long   t2  =  System  .  currentTimeMillis  (  )  ; 
System  .  out  .  println  (  MessageFormat  .  format  (  messages  .  getString  (  "insertDone"  )  ,  new   Object  [  ]  {  String  .  valueOf  (  counter  [  1  ]  )  ,  String  .  valueOf  (  counter  [  0  ]  )  ,  String  .  valueOf  (  (  t2  -  t1  )  /  1000f  )  }  )  )  ; 
} 









public   void   append  (  DirBuilder   builder  ,  File   file  ,  int  [  ]  counter  )  throws   IOException  { 
if  (  file  .  isDirectory  (  )  )  { 
File  [  ]  files  =  file  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  ++  i  )  { 
append  (  builder  ,  files  [  i  ]  ,  counter  )  ; 
} 
}  else  { 
try  { 
counter  [  1  ]  +=  builder  .  addFileRef  (  file  )  ; 
++  counter  [  0  ]  ; 
System  .  out  .  print  (  '.'  )  ; 
}  catch  (  DcmParseException   e  )  { 
System  .  out  .  println  (  MessageFormat  .  format  (  messages  .  getString  (  "insertFailed"  )  ,  new   Object  [  ]  {  file  }  )  )  ; 
e  .  printStackTrace  (  System  .  out  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
System  .  out  .  println  (  MessageFormat  .  format  (  messages  .  getString  (  "insertFailed"  )  ,  new   Object  [  ]  {  file  }  )  )  ; 
e  .  printStackTrace  (  System  .  out  )  ; 
} 
} 
} 






public   void   compact  (  )  throws   IOException  { 
DirWriter   writer  =  fact  .  newDirWriter  (  dirFile  ,  encodeParam  (  )  )  ; 
long   t1  =  System  .  currentTimeMillis  (  )  ; 
long   len1  =  dirFile  .  length  (  )  ; 
try  { 
writer  =  writer  .  compact  (  )  ; 
}  finally  { 
writer  .  close  (  )  ; 
} 
long   t2  =  System  .  currentTimeMillis  (  )  ; 
long   len2  =  dirFile  .  length  (  )  ; 
System  .  out  .  println  (  MessageFormat  .  format  (  messages  .  getString  (  "compactDone"  )  ,  new   Object  [  ]  {  dirFile  ,  String  .  valueOf  (  len1  )  ,  String  .  valueOf  (  len2  )  ,  String  .  valueOf  (  (  t2  -  t1  )  /  1000f  )  }  )  )  ; 
} 






public   void   purge  (  )  throws   IOException  { 
DirWriter   writer  =  fact  .  newDirWriter  (  dirFile  ,  encodeParam  (  )  )  ; 
long   t1  =  System  .  currentTimeMillis  (  )  ; 
long   len1  =  dirFile  .  length  (  )  ; 
int   count  =  0  ; 
try  { 
count  =  doPurge  (  writer  )  ; 
}  finally  { 
writer  .  close  (  )  ; 
} 
long   t2  =  System  .  currentTimeMillis  (  )  ; 
long   len2  =  dirFile  .  length  (  )  ; 
System  .  out  .  println  (  MessageFormat  .  format  (  messages  .  getString  (  "purgeDone"  )  ,  new   Object  [  ]  {  String  .  valueOf  (  count  )  ,  String  .  valueOf  (  (  t2  -  t1  )  /  1000f  )  }  )  )  ; 
} 

private   void   addFileIDs  (  DirWriter   w  ,  File   file  )  throws   IOException  { 
if  (  file  .  isDirectory  (  )  )  { 
File  [  ]  files  =  file  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  files  .  length  ;  ++  i  )  { 
addFileIDs  (  w  ,  files  [  i  ]  )  ; 
} 
}  else  { 
fileIDs  .  add  (  w  .  toFileIDs  (  file  )  )  ; 
} 
} 









public   void   remove  (  String  [  ]  args  ,  int   off  ,  boolean   delFiles  )  throws   IOException  { 
long   t1  =  System  .  currentTimeMillis  (  )  ; 
int  [  ]  counter  =  new   int  [  2  ]  ; 
DirWriter   w  =  fact  .  newDirWriter  (  dirFile  ,  encodeParam  (  )  )  ; 
try  { 
for  (  int   i  =  off  ;  i  <  args  .  length  ;  ++  i  )  { 
addFileIDs  (  w  ,  new   File  (  args  [  i  ]  )  )  ; 
} 
doRemove  (  w  ,  counter  ,  delFiles  )  ; 
}  finally  { 
w  .  close  (  )  ; 
} 
long   t2  =  System  .  currentTimeMillis  (  )  ; 
System  .  out  .  println  (  MessageFormat  .  format  (  messages  .  getString  (  "removeDone"  )  ,  new   Object  [  ]  {  String  .  valueOf  (  counter  [  1  ]  )  ,  String  .  valueOf  (  counter  [  0  ]  )  ,  String  .  valueOf  (  (  t2  -  t1  )  /  1000f  )  }  )  )  ; 
} 

private   void   doRemove  (  DirWriter   w  ,  int  [  ]  counter  ,  boolean   delFiles  )  throws   IOException  { 
for  (  DirRecord   rec  =  w  .  getFirstRecord  (  true  )  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  true  )  )  { 
if  (  patientIDs  .  contains  (  rec  .  getDataset  (  )  .  getString  (  Tags  .  PatientID  )  )  )  { 
if  (  delFiles  )  { 
deleteRefFiles  (  w  ,  rec  ,  counter  )  ; 
} 
counter  [  1  ]  +=  w  .  remove  (  rec  )  ; 
}  else   if  (  doRemoveStudy  (  w  ,  rec  ,  counter  ,  delFiles  )  )  { 
counter  [  1  ]  +=  w  .  remove  (  rec  )  ; 
} 
} 
} 

private   boolean   doRemoveStudy  (  DirWriter   w  ,  DirRecord   parent  ,  int  [  ]  counter  ,  boolean   delFiles  )  throws   IOException  { 
boolean   matchAll  =  true  ; 
LinkedList   toRemove  =  new   LinkedList  (  )  ; 
for  (  DirRecord   rec  =  parent  .  getFirstChild  (  true  )  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  true  )  )  { 
if  (  studyUIDs  .  contains  (  rec  .  getDataset  (  )  .  getString  (  Tags  .  StudyInstanceUID  )  )  )  { 
if  (  delFiles  )  { 
deleteRefFiles  (  w  ,  rec  ,  counter  )  ; 
} 
toRemove  .  add  (  rec  )  ; 
}  else   if  (  doRemoveSeries  (  w  ,  rec  ,  counter  ,  delFiles  )  )  { 
toRemove  .  add  (  rec  )  ; 
}  else  { 
matchAll  =  false  ; 
} 
} 
if  (  matchAll  )  { 
return   true  ; 
} 
for  (  Iterator   it  =  toRemove  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
counter  [  1  ]  +=  w  .  remove  (  (  DirRecord  )  it  .  next  (  )  )  ; 
} 
return   false  ; 
} 

private   boolean   doRemoveSeries  (  DirWriter   w  ,  DirRecord   parent  ,  int  [  ]  counter  ,  boolean   delFiles  )  throws   IOException  { 
boolean   matchAll  =  true  ; 
LinkedList   toRemove  =  new   LinkedList  (  )  ; 
for  (  DirRecord   rec  =  parent  .  getFirstChild  (  true  )  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  true  )  )  { 
if  (  seriesUIDs  .  contains  (  rec  .  getDataset  (  )  .  getString  (  Tags  .  SeriesInstanceUID  )  )  )  { 
if  (  delFiles  )  { 
deleteRefFiles  (  w  ,  rec  ,  counter  )  ; 
} 
toRemove  .  add  (  rec  )  ; 
}  else   if  (  doRemoveInstances  (  w  ,  rec  ,  counter  ,  delFiles  )  )  { 
toRemove  .  add  (  rec  )  ; 
}  else  { 
matchAll  =  false  ; 
} 
} 
if  (  matchAll  )  { 
return   true  ; 
} 
for  (  Iterator   it  =  toRemove  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
counter  [  1  ]  +=  w  .  remove  (  (  DirRecord  )  it  .  next  (  )  )  ; 
} 
return   false  ; 
} 

private   boolean   doRemoveInstances  (  DirWriter   w  ,  DirRecord   parent  ,  int  [  ]  counter  ,  boolean   delFiles  )  throws   IOException  { 
boolean   matchAll  =  true  ; 
LinkedList   toRemove  =  new   LinkedList  (  )  ; 
for  (  DirRecord   rec  =  parent  .  getFirstChild  (  true  )  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  true  )  )  { 
if  (  sopInstUIDs  .  contains  (  rec  .  getRefSOPInstanceUID  (  )  )  ||  matchFileIDs  (  rec  .  getRefFileIDs  (  )  )  )  { 
if  (  delFiles  )  { 
deleteRefFiles  (  w  ,  rec  ,  counter  )  ; 
} 
toRemove  .  add  (  rec  )  ; 
}  else  { 
matchAll  =  false  ; 
} 
} 
if  (  matchAll  )  { 
return   true  ; 
} 
for  (  Iterator   it  =  toRemove  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
counter  [  1  ]  +=  w  .  remove  (  (  DirRecord  )  it  .  next  (  )  )  ; 
} 
return   false  ; 
} 

private   boolean   matchFileIDs  (  String  [  ]  ids  )  { 
if  (  ids  ==  null  ||  fileIDs  .  isEmpty  (  )  )  { 
return   false  ; 
} 
for  (  Iterator   iter  =  fileIDs  .  iterator  (  )  ;  iter  .  hasNext  (  )  ;  )  { 
if  (  Arrays  .  equals  (  (  String  [  ]  )  iter  .  next  (  )  ,  ids  )  )  { 
return   true  ; 
} 
} 
return   false  ; 
} 

private   void   deleteRefFiles  (  DirWriter   w  ,  DirRecord   rec  ,  int  [  ]  counter  )  throws   IOException  { 
String  [  ]  fileIDs  =  rec  .  getRefFileIDs  (  )  ; 
if  (  fileIDs  !=  null  )  { 
File   f  =  w  .  getRefFile  (  fileIDs  )  ; 
if  (  !  f  .  delete  (  )  )  { 
System  .  out  .  println  (  MessageFormat  .  format  (  messages  .  getString  (  "deleteFailed"  )  ,  new   Object  [  ]  {  f  }  )  )  ; 
}  else  { 
++  counter  [  0  ]  ; 
} 
} 
for  (  DirRecord   child  =  rec  .  getFirstChild  (  true  )  ;  child  !=  null  ;  child  =  child  .  getNextSibling  (  true  )  )  { 
deleteRefFiles  (  w  ,  child  ,  counter  )  ; 
} 
} 

private   int   doPurge  (  DirWriter   w  )  throws   IOException  { 
int  [  ]  counter  =  {  0  }  ; 
for  (  DirRecord   rec  =  w  .  getFirstRecord  (  true  )  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  true  )  )  { 
if  (  doPurgeStudy  (  w  ,  rec  ,  counter  )  )  { 
counter  [  0  ]  +=  w  .  remove  (  rec  )  ; 
} 
} 
return   counter  [  0  ]  ; 
} 

private   boolean   doPurgeStudy  (  DirWriter   w  ,  DirRecord   parent  ,  int  [  ]  counter  )  throws   IOException  { 
boolean   matchAll  =  true  ; 
LinkedList   toRemove  =  new   LinkedList  (  )  ; 
for  (  DirRecord   rec  =  parent  .  getFirstChild  (  true  )  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  true  )  )  { 
if  (  doPurgeSeries  (  w  ,  rec  ,  counter  )  )  { 
toRemove  .  add  (  rec  )  ; 
}  else  { 
matchAll  =  false  ; 
} 
} 
if  (  matchAll  )  { 
return   true  ; 
} 
for  (  Iterator   it  =  toRemove  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
counter  [  0  ]  +=  w  .  remove  (  (  DirRecord  )  it  .  next  (  )  )  ; 
} 
return   false  ; 
} 

private   boolean   doPurgeSeries  (  DirWriter   w  ,  DirRecord   parent  ,  int  [  ]  counter  )  throws   IOException  { 
boolean   matchAll  =  true  ; 
LinkedList   toRemove  =  new   LinkedList  (  )  ; 
for  (  DirRecord   rec  =  parent  .  getFirstChild  (  true  )  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  true  )  )  { 
if  (  doPurgeInstances  (  w  ,  rec  ,  counter  )  )  { 
toRemove  .  add  (  rec  )  ; 
}  else  { 
matchAll  =  false  ; 
} 
} 
if  (  matchAll  )  { 
return   true  ; 
} 
for  (  Iterator   it  =  toRemove  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
counter  [  0  ]  +=  w  .  remove  (  (  DirRecord  )  it  .  next  (  )  )  ; 
} 
return   false  ; 
} 

private   boolean   doPurgeInstances  (  DirWriter   w  ,  DirRecord   parent  ,  int  [  ]  counter  )  throws   IOException  { 
boolean   matchAll  =  true  ; 
LinkedList   toRemove  =  new   LinkedList  (  )  ; 
for  (  DirRecord   rec  =  parent  .  getFirstChild  (  true  )  ;  rec  !=  null  ;  rec  =  rec  .  getNextSibling  (  true  )  )  { 
File   file  =  w  .  getRefFile  (  rec  .  getRefFileIDs  (  )  )  ; 
if  (  !  file  .  exists  (  )  )  { 
toRemove  .  add  (  rec  )  ; 
}  else  { 
matchAll  =  false  ; 
} 
} 
if  (  matchAll  )  { 
return   true  ; 
} 
for  (  Iterator   it  =  toRemove  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
counter  [  0  ]  +=  w  .  remove  (  (  DirRecord  )  it  .  next  (  )  )  ; 
} 
return   false  ; 
} 

private   static   Properties   loadConfig  (  )  { 
InputStream   in  =  DcmDir  .  class  .  getResourceAsStream  (  "dcmdir.cfg"  )  ; 
try  { 
Properties   retval  =  new   Properties  (  )  ; 
retval  .  load  (  in  )  ; 
return   retval  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Could not read dcmdir.cfg"  ,  e  )  ; 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   ignore  )  { 
} 
} 
} 
} 

private   static   void   exit  (  String   prompt  ,  boolean   error  )  { 
if  (  prompt  !=  null  )  { 
System  .  err  .  println  (  prompt  )  ; 
} 
if  (  error  )  { 
System  .  err  .  println  (  messages  .  getString  (  "try"  )  )  ; 
} 
System  .  exit  (  1  )  ; 
} 

private   static   String   replace  (  String   val  ,  String   from  ,  String   to  )  { 
return   from  .  equals  (  val  )  ?  to  :  val  ; 
} 
} 

