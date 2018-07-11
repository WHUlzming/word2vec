package   org  .  apache  .  xalan  .  extensions  ; 

import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  lang  .  reflect  .  Method  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Vector  ; 
import   javax  .  xml  .  transform  .  TransformerException  ; 
import   org  .  apache  .  xml  .  res  .  XMLErrorResources  ; 
import   org  .  apache  .  xml  .  res  .  XMLMessages  ; 
import   org  .  apache  .  xalan  .  res  .  XSLMessages  ; 
import   org  .  apache  .  xalan  .  res  .  XSLTErrorResources  ; 
import   org  .  apache  .  xalan  .  templates  .  ElemTemplateElement  ; 
import   org  .  apache  .  xalan  .  templates  .  Stylesheet  ; 
import   org  .  apache  .  xalan  .  transformer  .  TransformerImpl  ; 
import   org  .  apache  .  xml  .  dtm  .  DTMIterator  ; 
import   org  .  apache  .  xml  .  dtm  .  ref  .  DTMNodeList  ; 
import   org  .  apache  .  xml  .  utils  .  StringVector  ; 
import   org  .  apache  .  xml  .  utils  .  SystemIDResolver  ; 
import   org  .  apache  .  xpath  .  XPathProcessorException  ; 
import   org  .  apache  .  xpath  .  functions  .  FuncExtFunction  ; 
import   org  .  apache  .  xpath  .  objects  .  XObject  ; 








public   class   ExtensionHandlerGeneral   extends   ExtensionHandler  { 


private   String   m_scriptSrc  ; 


private   String   m_scriptSrcURL  ; 


private   Hashtable   m_functions  =  new   Hashtable  (  )  ; 


private   Hashtable   m_elements  =  new   Hashtable  (  )  ; 


private   Object   m_engine  ; 


private   Method   m_engineCall  =  null  ; 


private   static   String   BSF_MANAGER  ; 


private   static   final   String   DEFAULT_BSF_MANAGER  =  "org.apache.bsf.BSFManager"  ; 


private   static   final   String   propName  =  "org.apache.xalan.extensions.bsf.BSFManager"  ; 


private   static   final   Integer   ZEROINT  =  new   Integer  (  0  )  ; 

static  { 
BSF_MANAGER  =  ObjectFactory  .  lookUpFactoryClassName  (  propName  ,  null  ,  null  )  ; 
if  (  BSF_MANAGER  ==  null  )  { 
BSF_MANAGER  =  DEFAULT_BSF_MANAGER  ; 
} 
} 















public   ExtensionHandlerGeneral  (  String   namespaceUri  ,  StringVector   elemNames  ,  StringVector   funcNames  ,  String   scriptLang  ,  String   scriptSrcURL  ,  String   scriptSrc  ,  String   systemId  )  throws   TransformerException  { 
super  (  namespaceUri  ,  scriptLang  )  ; 
if  (  elemNames  !=  null  )  { 
Object   junk  =  new   Object  (  )  ; 
int   n  =  elemNames  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
String   tok  =  elemNames  .  elementAt  (  i  )  ; 
m_elements  .  put  (  tok  ,  junk  )  ; 
} 
} 
if  (  funcNames  !=  null  )  { 
Object   junk  =  new   Object  (  )  ; 
int   n  =  funcNames  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
String   tok  =  funcNames  .  elementAt  (  i  )  ; 
m_functions  .  put  (  tok  ,  junk  )  ; 
} 
} 
m_scriptSrcURL  =  scriptSrcURL  ; 
m_scriptSrc  =  scriptSrc  ; 
if  (  m_scriptSrcURL  !=  null  )  { 
URL   url  =  null  ; 
try  { 
url  =  new   URL  (  m_scriptSrcURL  )  ; 
}  catch  (  java  .  net  .  MalformedURLException   mue  )  { 
int   indexOfColon  =  m_scriptSrcURL  .  indexOf  (  ':'  )  ; 
int   indexOfSlash  =  m_scriptSrcURL  .  indexOf  (  '/'  )  ; 
if  (  (  indexOfColon  !=  -  1  )  &&  (  indexOfSlash  !=  -  1  )  &&  (  indexOfColon  <  indexOfSlash  )  )  { 
url  =  null  ; 
throw   new   TransformerException  (  XSLMessages  .  createMessage  (  XSLTErrorResources  .  ER_COULD_NOT_FIND_EXTERN_SCRIPT  ,  new   Object  [  ]  {  m_scriptSrcURL  }  )  ,  mue  )  ; 
}  else  { 
try  { 
url  =  new   URL  (  new   URL  (  SystemIDResolver  .  getAbsoluteURI  (  systemId  )  )  ,  m_scriptSrcURL  )  ; 
}  catch  (  java  .  net  .  MalformedURLException   mue2  )  { 
throw   new   TransformerException  (  XSLMessages  .  createMessage  (  XSLTErrorResources  .  ER_COULD_NOT_FIND_EXTERN_SCRIPT  ,  new   Object  [  ]  {  m_scriptSrcURL  }  )  ,  mue2  )  ; 
} 
} 
} 
if  (  url  !=  null  )  { 
try  { 
URLConnection   uc  =  url  .  openConnection  (  )  ; 
InputStream   is  =  uc  .  getInputStream  (  )  ; 
byte  [  ]  bArray  =  new   byte  [  uc  .  getContentLength  (  )  ]  ; 
is  .  read  (  bArray  )  ; 
m_scriptSrc  =  new   String  (  bArray  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   TransformerException  (  XSLMessages  .  createMessage  (  XSLTErrorResources  .  ER_COULD_NOT_FIND_EXTERN_SCRIPT  ,  new   Object  [  ]  {  m_scriptSrcURL  }  )  ,  ioe  )  ; 
} 
} 
} 
Object   manager  =  null  ; 
try  { 
manager  =  ObjectFactory  .  newInstance  (  BSF_MANAGER  ,  ObjectFactory  .  findClassLoader  (  )  ,  true  )  ; 
}  catch  (  ObjectFactory  .  ConfigurationError   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
if  (  manager  ==  null  )  { 
throw   new   TransformerException  (  XSLMessages  .  createMessage  (  XSLTErrorResources  .  ER_CANNOT_INIT_BSFMGR  ,  null  )  )  ; 
} 
try  { 
Method   loadScriptingEngine  =  manager  .  getClass  (  )  .  getMethod  (  "loadScriptingEngine"  ,  new   Class  [  ]  {  String  .  class  }  )  ; 
m_engine  =  loadScriptingEngine  .  invoke  (  manager  ,  new   Object  [  ]  {  scriptLang  }  )  ; 
Method   engineExec  =  m_engine  .  getClass  (  )  .  getMethod  (  "exec"  ,  new   Class  [  ]  {  String  .  class  ,  Integer  .  TYPE  ,  Integer  .  TYPE  ,  Object  .  class  }  )  ; 
engineExec  .  invoke  (  m_engine  ,  new   Object  [  ]  {  "XalanScript"  ,  ZEROINT  ,  ZEROINT  ,  m_scriptSrc  }  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
throw   new   TransformerException  (  XSLMessages  .  createMessage  (  XSLTErrorResources  .  ER_CANNOT_CMPL_EXTENSN  ,  null  )  ,  e  )  ; 
} 
} 






public   boolean   isFunctionAvailable  (  String   function  )  { 
return  (  m_functions  .  get  (  function  )  !=  null  )  ; 
} 






public   boolean   isElementAvailable  (  String   element  )  { 
return  (  m_elements  .  get  (  element  )  !=  null  )  ; 
} 













public   Object   callFunction  (  String   funcName  ,  Vector   args  ,  Object   methodKey  ,  ExpressionContext   exprContext  )  throws   TransformerException  { 
Object  [  ]  argArray  ; 
try  { 
argArray  =  new   Object  [  args  .  size  (  )  ]  ; 
for  (  int   i  =  0  ;  i  <  argArray  .  length  ;  i  ++  )  { 
Object   o  =  args  .  get  (  i  )  ; 
argArray  [  i  ]  =  (  o   instanceof   XObject  )  ?  (  (  XObject  )  o  )  .  object  (  )  :  o  ; 
o  =  argArray  [  i  ]  ; 
if  (  null  !=  o  &&  o   instanceof   DTMIterator  )  { 
argArray  [  i  ]  =  new   DTMNodeList  (  (  DTMIterator  )  o  )  ; 
} 
} 
if  (  m_engineCall  ==  null  )  { 
m_engineCall  =  m_engine  .  getClass  (  )  .  getMethod  (  "call"  ,  new   Class  [  ]  {  Object  .  class  ,  String  .  class  ,  Object  [  ]  .  class  }  )  ; 
} 
return   m_engineCall  .  invoke  (  m_engine  ,  new   Object  [  ]  {  null  ,  funcName  ,  argArray  }  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
String   msg  =  e  .  getMessage  (  )  ; 
if  (  null  !=  msg  )  { 
if  (  msg  .  startsWith  (  "Stopping after fatal error:"  )  )  { 
msg  =  msg  .  substring  (  "Stopping after fatal error:"  .  length  (  )  )  ; 
} 
throw   new   TransformerException  (  e  )  ; 
}  else  { 
throw   new   TransformerException  (  XSLMessages  .  createMessage  (  XSLTErrorResources  .  ER_CANNOT_CREATE_EXTENSN  ,  new   Object  [  ]  {  funcName  ,  e  }  )  )  ; 
} 
} 
} 










public   Object   callFunction  (  FuncExtFunction   extFunction  ,  Vector   args  ,  ExpressionContext   exprContext  )  throws   TransformerException  { 
return   callFunction  (  extFunction  .  getFunctionName  (  )  ,  args  ,  extFunction  .  getMethodKey  (  )  ,  exprContext  )  ; 
} 


















public   void   processElement  (  String   localPart  ,  ElemTemplateElement   element  ,  TransformerImpl   transformer  ,  Stylesheet   stylesheetTree  ,  Object   methodKey  )  throws   TransformerException  ,  IOException  { 
Object   result  =  null  ; 
XSLProcessorContext   xpc  =  new   XSLProcessorContext  (  transformer  ,  stylesheetTree  )  ; 
try  { 
Vector   argv  =  new   Vector  (  2  )  ; 
argv  .  add  (  xpc  )  ; 
argv  .  add  (  element  )  ; 
result  =  callFunction  (  localPart  ,  argv  ,  methodKey  ,  transformer  .  getXPathContext  (  )  .  getExpressionContext  (  )  )  ; 
}  catch  (  XPathProcessorException   e  )  { 
throw   new   TransformerException  (  e  .  getMessage  (  )  ,  e  )  ; 
} 
if  (  result  !=  null  )  { 
xpc  .  outputToResultTree  (  stylesheetTree  ,  result  )  ; 
} 
} 
} 

