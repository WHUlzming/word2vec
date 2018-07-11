package   com  .  sun  .  corba  .  se  .  spi  .  activation  ; 







public   abstract   class   _ServerManagerImplBase   extends   org  .  omg  .  CORBA  .  portable  .  ObjectImpl   implements   com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerManager  ,  org  .  omg  .  CORBA  .  portable  .  InvokeHandler  { 

public   _ServerManagerImplBase  (  )  { 
} 

private   static   java  .  util  .  Hashtable   _methods  =  new   java  .  util  .  Hashtable  (  )  ; 

static  { 
_methods  .  put  (  "active"  ,  new   java  .  lang  .  Integer  (  0  )  )  ; 
_methods  .  put  (  "registerEndpoints"  ,  new   java  .  lang  .  Integer  (  1  )  )  ; 
_methods  .  put  (  "getActiveServers"  ,  new   java  .  lang  .  Integer  (  2  )  )  ; 
_methods  .  put  (  "activate"  ,  new   java  .  lang  .  Integer  (  3  )  )  ; 
_methods  .  put  (  "shutdown"  ,  new   java  .  lang  .  Integer  (  4  )  )  ; 
_methods  .  put  (  "install"  ,  new   java  .  lang  .  Integer  (  5  )  )  ; 
_methods  .  put  (  "getORBNames"  ,  new   java  .  lang  .  Integer  (  6  )  )  ; 
_methods  .  put  (  "uninstall"  ,  new   java  .  lang  .  Integer  (  7  )  )  ; 
_methods  .  put  (  "locateServer"  ,  new   java  .  lang  .  Integer  (  8  )  )  ; 
_methods  .  put  (  "locateServerForORB"  ,  new   java  .  lang  .  Integer  (  9  )  )  ; 
_methods  .  put  (  "getEndpoint"  ,  new   java  .  lang  .  Integer  (  10  )  )  ; 
_methods  .  put  (  "getServerPortForType"  ,  new   java  .  lang  .  Integer  (  11  )  )  ; 
} 

public   org  .  omg  .  CORBA  .  portable  .  OutputStream   _invoke  (  String   $method  ,  org  .  omg  .  CORBA  .  portable  .  InputStream   in  ,  org  .  omg  .  CORBA  .  portable  .  ResponseHandler   $rh  )  { 
org  .  omg  .  CORBA  .  portable  .  OutputStream   out  =  null  ; 
java  .  lang  .  Integer   __method  =  (  java  .  lang  .  Integer  )  _methods  .  get  (  $method  )  ; 
if  (  __method  ==  null  )  throw   new   org  .  omg  .  CORBA  .  BAD_OPERATION  (  0  ,  org  .  omg  .  CORBA  .  CompletionStatus  .  COMPLETED_MAYBE  )  ; 
switch  (  __method  .  intValue  (  )  )  { 
case   0  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  Server   serverObj  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHelper  .  read  (  in  )  ; 
this  .  active  (  serverId  ,  serverObj  )  ; 
out  =  $rh  .  createReply  (  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   1  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
String   orbId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ORBidHelper  .  read  (  in  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  EndPointInfo   endPointInfo  [  ]  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  EndpointInfoListHelper  .  read  (  in  )  ; 
this  .  registerEndpoints  (  serverId  ,  orbId  ,  endPointInfo  )  ; 
out  =  $rh  .  createReply  (  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  NoSuchEndPoint   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  NoSuchEndPointHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ORBAlreadyRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ORBAlreadyRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   2  : 
{ 
int   $result  [  ]  =  null  ; 
$result  =  this  .  getActiveServers  (  )  ; 
out  =  $rh  .  createReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdsHelper  .  write  (  out  ,  $result  )  ; 
break  ; 
} 
case   3  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
this  .  activate  (  serverId  )  ; 
out  =  $rh  .  createReply  (  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerAlreadyActive   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerAlreadyActiveHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDown   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDownHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   4  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
this  .  shutdown  (  serverId  )  ; 
out  =  $rh  .  createReply  (  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotActive   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotActiveHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   5  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
this  .  install  (  serverId  )  ; 
out  =  $rh  .  createReply  (  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDown   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDownHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerAlreadyInstalled   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerAlreadyInstalledHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   6  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
String   $result  [  ]  =  null  ; 
$result  =  this  .  getORBNames  (  serverId  )  ; 
out  =  $rh  .  createReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ORBidListHelper  .  write  (  out  ,  $result  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   7  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
this  .  uninstall  (  serverId  )  ; 
out  =  $rh  .  createReply  (  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDown   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDownHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerAlreadyUninstalled   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerAlreadyUninstalledHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   8  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
String   endPoint  =  in  .  read_string  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  LocatorPackage  .  ServerLocation   $result  =  null  ; 
$result  =  this  .  locateServer  (  serverId  ,  endPoint  )  ; 
out  =  $rh  .  createReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  LocatorPackage  .  ServerLocationHelper  .  write  (  out  ,  $result  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  NoSuchEndPoint   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  NoSuchEndPointHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDown   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDownHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   9  : 
{ 
try  { 
int   serverId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerIdHelper  .  read  (  in  )  ; 
String   orbId  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ORBidHelper  .  read  (  in  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  LocatorPackage  .  ServerLocationPerORB   $result  =  null  ; 
$result  =  this  .  locateServerForORB  (  serverId  ,  orbId  )  ; 
out  =  $rh  .  createReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  LocatorPackage  .  ServerLocationPerORBHelper  .  write  (  out  ,  $result  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  InvalidORBid   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  InvalidORBidHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegistered   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerNotRegisteredHelper  .  write  (  out  ,  $ex  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDown   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  ServerHeldDownHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   10  : 
{ 
try  { 
String   endPointType  =  in  .  read_string  (  )  ; 
int   $result  =  (  int  )  0  ; 
$result  =  this  .  getEndpoint  (  endPointType  )  ; 
out  =  $rh  .  createReply  (  )  ; 
out  .  write_long  (  $result  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  NoSuchEndPoint   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  NoSuchEndPointHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
case   11  : 
{ 
try  { 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  LocatorPackage  .  ServerLocationPerORB   location  =  com  .  sun  .  corba  .  se  .  spi  .  activation  .  LocatorPackage  .  ServerLocationPerORBHelper  .  read  (  in  )  ; 
String   endPointType  =  in  .  read_string  (  )  ; 
int   $result  =  (  int  )  0  ; 
$result  =  this  .  getServerPortForType  (  location  ,  endPointType  )  ; 
out  =  $rh  .  createReply  (  )  ; 
out  .  write_long  (  $result  )  ; 
}  catch  (  com  .  sun  .  corba  .  se  .  spi  .  activation  .  NoSuchEndPoint   $ex  )  { 
out  =  $rh  .  createExceptionReply  (  )  ; 
com  .  sun  .  corba  .  se  .  spi  .  activation  .  NoSuchEndPointHelper  .  write  (  out  ,  $ex  )  ; 
} 
break  ; 
} 
default  : 
throw   new   org  .  omg  .  CORBA  .  BAD_OPERATION  (  0  ,  org  .  omg  .  CORBA  .  CompletionStatus  .  COMPLETED_MAYBE  )  ; 
} 
return   out  ; 
} 

private   static   String  [  ]  __ids  =  {  "IDL:activation/ServerManager:1.0"  ,  "IDL:activation/Activator:1.0"  ,  "IDL:activation/Locator:1.0"  }  ; 

public   String  [  ]  _ids  (  )  { 
return  (  String  [  ]  )  __ids  .  clone  (  )  ; 
} 
} 

