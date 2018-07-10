package   com  .  google  .  android  .  net  ; 

import   android  .  content  .  ContentResolver  ; 
import   android  .  content  .  ContentValues  ; 
import   android  .  content  .  Context  ; 
import   android  .  net  .  http  .  AndroidHttpClient  ; 
import   android  .  os  .  Build  ; 
import   android  .  os  .  NetStat  ; 
import   android  .  os  .  SystemClock  ; 
import   android  .  provider  .  Checkin  ; 
import   android  .  util  .  Config  ; 
import   android  .  util  .  Log  ; 
import   org  .  apache  .  http  .  HttpEntity  ; 
import   org  .  apache  .  http  .  HttpEntityEnclosingRequest  ; 
import   org  .  apache  .  http  .  HttpHost  ; 
import   org  .  apache  .  http  .  HttpRequest  ; 
import   org  .  apache  .  http  .  HttpResponse  ; 
import   org  .  apache  .  http  .  ProtocolException  ; 
import   org  .  apache  .  http  .  client  .  ClientProtocolException  ; 
import   org  .  apache  .  http  .  client  .  HttpClient  ; 
import   org  .  apache  .  http  .  client  .  ResponseHandler  ; 
import   org  .  apache  .  http  .  client  .  methods  .  HttpUriRequest  ; 
import   org  .  apache  .  http  .  conn  .  ClientConnectionManager  ; 
import   org  .  apache  .  http  .  conn  .  scheme  .  LayeredSocketFactory  ; 
import   org  .  apache  .  http  .  conn  .  scheme  .  Scheme  ; 
import   org  .  apache  .  http  .  conn  .  scheme  .  SchemeRegistry  ; 
import   org  .  apache  .  http  .  conn  .  scheme  .  SocketFactory  ; 
import   org  .  apache  .  http  .  impl  .  client  .  EntityEnclosingRequestWrapper  ; 
import   org  .  apache  .  http  .  impl  .  client  .  RequestWrapper  ; 
import   org  .  apache  .  http  .  params  .  HttpParams  ; 
import   org  .  apache  .  http  .  protocol  .  HttpContext  ; 
import   org  .  apache  .  harmony  .  xnet  .  provider  .  jsse  .  SSLClientSessionCache  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  InetAddress  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  URI  ; 
import   java  .  net  .  URISyntaxException  ; 





public   class   GoogleHttpClient   implements   HttpClient  { 

private   static   final   String   TAG  =  "GoogleHttpClient"  ; 

private   static   final   boolean   LOCAL_LOGV  =  Config  .  LOGV  ||  false  ; 


public   static   class   BlockedRequestException   extends   IOException  { 

private   final   UrlRules  .  Rule   mRule  ; 

BlockedRequestException  (  UrlRules  .  Rule   rule  )  { 
super  (  "Blocked by rule: "  +  rule  .  mName  )  ; 
mRule  =  rule  ; 
} 
} 

private   final   AndroidHttpClient   mClient  ; 

private   final   ContentResolver   mResolver  ; 

private   final   String   mAppName  ,  mUserAgent  ; 

private   final   ThreadLocal  <  Boolean  >  mConnectionAllocated  =  new   ThreadLocal  <  Boolean  >  (  )  ; 





public   GoogleHttpClient  (  ContentResolver   resolver  ,  String   userAgent  )  { 
mClient  =  AndroidHttpClient  .  newInstance  (  userAgent  )  ; 
mResolver  =  resolver  ; 
mUserAgent  =  mAppName  =  userAgent  ; 
} 





public   GoogleHttpClient  (  ContentResolver   resolver  ,  String   appAndVersion  ,  boolean   gzipCapable  )  { 
this  (  resolver  ,  null  ,  appAndVersion  ,  gzipCapable  )  ; 
} 





















public   GoogleHttpClient  (  Context   context  ,  String   appAndVersion  ,  boolean   gzipCapable  )  { 
this  (  context  .  getContentResolver  (  )  ,  SSLClientSessionCacheFactory  .  getCache  (  context  )  ,  appAndVersion  ,  gzipCapable  )  ; 
} 

private   GoogleHttpClient  (  ContentResolver   resolver  ,  SSLClientSessionCache   cache  ,  String   appAndVersion  ,  boolean   gzipCapable  )  { 
String   userAgent  =  appAndVersion  +  " ("  +  Build  .  DEVICE  +  " "  +  Build  .  ID  +  ")"  ; 
if  (  gzipCapable  )  { 
userAgent  =  userAgent  +  "; gzip"  ; 
} 
mClient  =  AndroidHttpClient  .  newInstance  (  userAgent  ,  cache  )  ; 
mResolver  =  resolver  ; 
mAppName  =  appAndVersion  ; 
mUserAgent  =  userAgent  ; 
SchemeRegistry   registry  =  getConnectionManager  (  )  .  getSchemeRegistry  (  )  ; 
for  (  String   name  :  registry  .  getSchemeNames  (  )  )  { 
Scheme   scheme  =  registry  .  unregister  (  name  )  ; 
SocketFactory   sf  =  scheme  .  getSocketFactory  (  )  ; 
if  (  sf   instanceof   LayeredSocketFactory  )  { 
sf  =  new   WrappedLayeredSocketFactory  (  (  LayeredSocketFactory  )  sf  )  ; 
}  else  { 
sf  =  new   WrappedSocketFactory  (  sf  )  ; 
} 
registry  .  register  (  new   Scheme  (  name  ,  sf  ,  scheme  .  getDefaultPort  (  )  )  )  ; 
} 
} 








private   class   WrappedSocketFactory   implements   SocketFactory  { 

private   SocketFactory   mDelegate  ; 

private   WrappedSocketFactory  (  SocketFactory   delegate  )  { 
mDelegate  =  delegate  ; 
} 

public   final   Socket   createSocket  (  )  throws   IOException  { 
return   mDelegate  .  createSocket  (  )  ; 
} 

public   final   boolean   isSecure  (  Socket   s  )  { 
return   mDelegate  .  isSecure  (  s  )  ; 
} 

public   final   Socket   connectSocket  (  Socket   s  ,  String   h  ,  int   p  ,  InetAddress   la  ,  int   lp  ,  HttpParams   params  )  throws   IOException  { 
mConnectionAllocated  .  set  (  Boolean  .  TRUE  )  ; 
return   mDelegate  .  connectSocket  (  s  ,  h  ,  p  ,  la  ,  lp  ,  params  )  ; 
} 
} 


private   class   WrappedLayeredSocketFactory   extends   WrappedSocketFactory   implements   LayeredSocketFactory  { 

private   LayeredSocketFactory   mDelegate  ; 

private   WrappedLayeredSocketFactory  (  LayeredSocketFactory   sf  )  { 
super  (  sf  )  ; 
mDelegate  =  sf  ; 
} 

public   final   Socket   createSocket  (  Socket   s  ,  String   host  ,  int   port  ,  boolean   autoClose  )  throws   IOException  { 
return   mDelegate  .  createSocket  (  s  ,  host  ,  port  ,  autoClose  )  ; 
} 
} 





public   void   close  (  )  { 
mClient  .  close  (  )  ; 
} 


public   HttpResponse   executeWithoutRewriting  (  HttpUriRequest   request  ,  HttpContext   context  )  throws   IOException  { 
int   code  =  -  1  ; 
long   start  =  SystemClock  .  elapsedRealtime  (  )  ; 
try  { 
HttpResponse   response  ; 
mConnectionAllocated  .  set  (  null  )  ; 
if  (  NetworkStatsEntity  .  shouldLogNetworkStats  (  )  )  { 
int   uid  =  android  .  os  .  Process  .  myUid  (  )  ; 
long   startTx  =  NetStat  .  getUidTxBytes  (  uid  )  ; 
long   startRx  =  NetStat  .  getUidRxBytes  (  uid  )  ; 
response  =  mClient  .  execute  (  request  ,  context  )  ; 
HttpEntity   origEntity  =  response  ==  null  ?  null  :  response  .  getEntity  (  )  ; 
if  (  origEntity  !=  null  )  { 
long   now  =  SystemClock  .  elapsedRealtime  (  )  ; 
long   elapsed  =  now  -  start  ; 
NetworkStatsEntity   entity  =  new   NetworkStatsEntity  (  origEntity  ,  mAppName  ,  uid  ,  startTx  ,  startRx  ,  elapsed  ,  now  )  ; 
response  .  setEntity  (  entity  )  ; 
} 
}  else  { 
response  =  mClient  .  execute  (  request  ,  context  )  ; 
} 
code  =  response  .  getStatusLine  (  )  .  getStatusCode  (  )  ; 
return   response  ; 
}  finally  { 
try  { 
long   elapsed  =  SystemClock  .  elapsedRealtime  (  )  -  start  ; 
ContentValues   values  =  new   ContentValues  (  )  ; 
values  .  put  (  Checkin  .  Stats  .  COUNT  ,  1  )  ; 
values  .  put  (  Checkin  .  Stats  .  SUM  ,  elapsed  /  1000.0  )  ; 
values  .  put  (  Checkin  .  Stats  .  TAG  ,  Checkin  .  Stats  .  Tag  .  HTTP_REQUEST  +  ":"  +  mAppName  )  ; 
mResolver  .  insert  (  Checkin  .  Stats  .  CONTENT_URI  ,  values  )  ; 
if  (  mConnectionAllocated  .  get  (  )  ==  null  &&  code  >=  0  )  { 
values  .  put  (  Checkin  .  Stats  .  TAG  ,  Checkin  .  Stats  .  Tag  .  HTTP_REUSED  +  ":"  +  mAppName  )  ; 
mResolver  .  insert  (  Checkin  .  Stats  .  CONTENT_URI  ,  values  )  ; 
} 
String   status  =  code  <  0  ?  "IOException"  :  Integer  .  toString  (  code  )  ; 
values  .  put  (  Checkin  .  Stats  .  TAG  ,  Checkin  .  Stats  .  Tag  .  HTTP_STATUS  +  ":"  +  mAppName  +  ":"  +  status  )  ; 
mResolver  .  insert  (  Checkin  .  Stats  .  CONTENT_URI  ,  values  )  ; 
}  catch  (  Exception   e  )  { 
Log  .  e  (  TAG  ,  "Error recording stats"  ,  e  )  ; 
} 
} 
} 

public   String   rewriteURI  (  String   original  )  { 
UrlRules   rules  =  UrlRules  .  getRules  (  mResolver  )  ; 
UrlRules  .  Rule   rule  =  rules  .  matchRule  (  original  )  ; 
return   rule  .  apply  (  original  )  ; 
} 

public   HttpResponse   execute  (  HttpUriRequest   request  ,  HttpContext   context  )  throws   IOException  { 
URI   uri  =  request  .  getURI  (  )  ; 
String   original  =  uri  .  toString  (  )  ; 
UrlRules   rules  =  UrlRules  .  getRules  (  mResolver  )  ; 
UrlRules  .  Rule   rule  =  rules  .  matchRule  (  original  )  ; 
String   rewritten  =  rule  .  apply  (  original  )  ; 
if  (  rewritten  ==  null  )  { 
Log  .  w  (  TAG  ,  "Blocked by "  +  rule  .  mName  +  ": "  +  original  )  ; 
throw   new   BlockedRequestException  (  rule  )  ; 
}  else   if  (  rewritten  ==  original  )  { 
return   executeWithoutRewriting  (  request  ,  context  )  ; 
} 
try  { 
uri  =  new   URI  (  rewritten  )  ; 
}  catch  (  URISyntaxException   e  )  { 
throw   new   RuntimeException  (  "Bad URL from rule: "  +  rule  .  mName  ,  e  )  ; 
} 
RequestWrapper   wrapper  =  wrapRequest  (  request  )  ; 
wrapper  .  setURI  (  uri  )  ; 
request  =  wrapper  ; 
if  (  LOCAL_LOGV  )  Log  .  v  (  TAG  ,  "Rule "  +  rule  .  mName  +  ": "  +  original  +  " -> "  +  rewritten  )  ; 
return   executeWithoutRewriting  (  request  ,  context  )  ; 
} 




private   static   RequestWrapper   wrapRequest  (  HttpUriRequest   request  )  throws   IOException  { 
try  { 
RequestWrapper   wrapped  ; 
if  (  request   instanceof   HttpEntityEnclosingRequest  )  { 
wrapped  =  new   EntityEnclosingRequestWrapper  (  (  HttpEntityEnclosingRequest  )  request  )  ; 
}  else  { 
wrapped  =  new   RequestWrapper  (  request  )  ; 
} 
wrapped  .  resetHeaders  (  )  ; 
return   wrapped  ; 
}  catch  (  ProtocolException   e  )  { 
throw   new   ClientProtocolException  (  e  )  ; 
} 
} 











public   static   String   getGzipCapableUserAgent  (  String   originalUserAgent  )  { 
return   originalUserAgent  +  "; gzip"  ; 
} 

public   HttpParams   getParams  (  )  { 
return   mClient  .  getParams  (  )  ; 
} 

public   ClientConnectionManager   getConnectionManager  (  )  { 
return   mClient  .  getConnectionManager  (  )  ; 
} 

public   HttpResponse   execute  (  HttpUriRequest   request  )  throws   IOException  { 
return   execute  (  request  ,  (  HttpContext  )  null  )  ; 
} 

public   HttpResponse   execute  (  HttpHost   target  ,  HttpRequest   request  )  throws   IOException  { 
return   mClient  .  execute  (  target  ,  request  )  ; 
} 

public   HttpResponse   execute  (  HttpHost   target  ,  HttpRequest   request  ,  HttpContext   context  )  throws   IOException  { 
return   mClient  .  execute  (  target  ,  request  ,  context  )  ; 
} 

public  <  T  >  T   execute  (  HttpUriRequest   request  ,  ResponseHandler  <  ?  extends   T  >  responseHandler  )  throws   IOException  ,  ClientProtocolException  { 
return   mClient  .  execute  (  request  ,  responseHandler  )  ; 
} 

public  <  T  >  T   execute  (  HttpUriRequest   request  ,  ResponseHandler  <  ?  extends   T  >  responseHandler  ,  HttpContext   context  )  throws   IOException  ,  ClientProtocolException  { 
return   mClient  .  execute  (  request  ,  responseHandler  ,  context  )  ; 
} 

public  <  T  >  T   execute  (  HttpHost   target  ,  HttpRequest   request  ,  ResponseHandler  <  ?  extends   T  >  responseHandler  )  throws   IOException  ,  ClientProtocolException  { 
return   mClient  .  execute  (  target  ,  request  ,  responseHandler  )  ; 
} 

public  <  T  >  T   execute  (  HttpHost   target  ,  HttpRequest   request  ,  ResponseHandler  <  ?  extends   T  >  responseHandler  ,  HttpContext   context  )  throws   IOException  ,  ClientProtocolException  { 
return   mClient  .  execute  (  target  ,  request  ,  responseHandler  ,  context  )  ; 
} 







public   void   enableCurlLogging  (  String   name  ,  int   level  )  { 
mClient  .  enableCurlLogging  (  name  ,  level  )  ; 
} 




public   void   disableCurlLogging  (  )  { 
mClient  .  disableCurlLogging  (  )  ; 
} 
} 

