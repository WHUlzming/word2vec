package   org  .  signserver  .  web  ; 

import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLEncoder  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Map  .  Entry  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  signserver  .  testutils  .  ModulesTestCase  ; 








public   abstract   class   WebTestCase   extends   ModulesTestCase  { 


private   static   final   Logger   LOG  =  Logger  .  getLogger  (  WebTestCase  .  class  )  ; 

private   static   final   String   CRLF  =  "\r\n"  ; 

protected   abstract   String   getServletURL  (  )  ; 


protected   void   assertStatusReturned  (  Map  <  String  ,  String  >  fields  ,  int   expected  )  { 
assertStatusReturned  (  fields  ,  expected  ,  false  )  ; 
} 





protected   void   assertStatusReturned  (  Map  <  String  ,  String  >  fields  ,  int   expected  ,  boolean   skipMultipartTest  )  { 
try  { 
HttpURLConnection   con  =  WebTestCase  .  sendGet  (  getServletURL  (  )  ,  fields  )  ; 
int   response  =  con  .  getResponseCode  (  )  ; 
String   message  =  con  .  getResponseMessage  (  )  ; 
LOG  .  info  (  "Returned "  +  response  +  " "  +  message  )  ; 
assertEquals  (  "status response: "  +  message  ,  expected  ,  response  )  ; 
con  .  disconnect  (  )  ; 
}  catch  (  IOException   ex  )  { 
LOG  .  error  (  "IOException"  ,  ex  )  ; 
fail  (  ex  .  getMessage  (  )  )  ; 
} 
try  { 
HttpURLConnection   con  =  WebTestCase  .  sendPostFormUrlencoded  (  getServletURL  (  )  ,  fields  )  ; 
int   response  =  con  .  getResponseCode  (  )  ; 
String   message  =  con  .  getResponseMessage  (  )  ; 
LOG  .  info  (  "Returned "  +  response  +  " "  +  message  )  ; 
assertEquals  (  "status response: "  +  message  ,  expected  ,  response  )  ; 
con  .  disconnect  (  )  ; 
}  catch  (  IOException   ex  )  { 
LOG  .  error  (  "IOException"  ,  ex  )  ; 
fail  (  ex  .  getMessage  (  )  )  ; 
} 
if  (  !  skipMultipartTest  )  { 
try  { 
HttpURLConnection   con  =  WebTestCase  .  sendPostMultipartFormData  (  getServletURL  (  )  ,  fields  )  ; 
int   response  =  con  .  getResponseCode  (  )  ; 
String   message  =  con  .  getResponseMessage  (  )  ; 
LOG  .  info  (  "Returned "  +  response  +  " "  +  message  )  ; 
assertEquals  (  "status response: "  +  message  ,  expected  ,  response  )  ; 
con  .  disconnect  (  )  ; 
}  catch  (  IOException   ex  )  { 
LOG  .  error  (  "IOException"  ,  ex  )  ; 
fail  (  ex  .  getMessage  (  )  )  ; 
} 
} 
} 





protected   void   assertStatusReturned  (  Map  <  String  ,  String  >  fields  ,  String   method  ,  int   expected  )  { 
try  { 
HttpURLConnection   con  =  WebTestCase  .  send  (  getServletURL  (  )  ,  fields  ,  method  )  ; 
int   response  =  con  .  getResponseCode  (  )  ; 
String   message  =  con  .  getResponseMessage  (  )  ; 
LOG  .  info  (  "Returned "  +  response  +  " "  +  message  )  ; 
assertEquals  (  "status response: "  +  message  ,  expected  ,  response  )  ; 
con  .  disconnect  (  )  ; 
}  catch  (  IOException   ex  )  { 
LOG  .  error  (  "IOException"  ,  ex  )  ; 
fail  (  ex  .  getMessage  (  )  )  ; 
} 
} 






protected   void   assertStatusReturnedNotEqual  (  Map  <  String  ,  String  >  fields  ,  String   method  ,  int   notExpected  )  { 
try  { 
HttpURLConnection   con  =  WebTestCase  .  send  (  getServletURL  (  )  ,  fields  ,  method  )  ; 
int   response  =  con  .  getResponseCode  (  )  ; 
String   message  =  con  .  getResponseMessage  (  )  ; 
LOG  .  info  (  "Returned "  +  response  +  " "  +  message  )  ; 
assertFalse  (  "status response: "  +  message  ,  notExpected  ==  response  )  ; 
con  .  disconnect  (  )  ; 
}  catch  (  IOException   ex  )  { 
LOG  .  error  (  "IOException"  ,  ex  )  ; 
fail  (  ex  .  getMessage  (  )  )  ; 
} 
} 

protected   static   HttpURLConnection   openConnection  (  String   baseURL  ,  String   queryString  )  throws   MalformedURLException  ,  IOException  { 
final   StringBuilder   buff  =  new   StringBuilder  (  )  ; 
buff  .  append  (  baseURL  )  ; 
if  (  queryString  !=  null  )  { 
buff  .  append  (  "?"  )  ; 
buff  .  append  (  queryString  )  ; 
} 
final   URL   url  =  new   URL  (  buff  .  toString  (  )  )  ; 
return  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
} 

protected   static   HttpURLConnection   getConnectionWithMethod  (  String   baseURL  ,  final   Map  <  String  ,  String  >  fields  ,  String   method  )  throws   IOException  { 
final   StringBuilder   buff  =  new   StringBuilder  (  )  ; 
for  (  Entry  <  String  ,  String  >  entry  :  fields  .  entrySet  (  )  )  { 
buff  .  append  (  entry  .  getKey  (  )  )  .  append  (  "="  )  .  append  (  URLEncoder  .  encode  (  entry  .  getValue  (  )  ,  "UTF-8"  )  )  .  append  (  "&"  )  ; 
} 
final   String   body  =  buff  .  toString  (  )  ; 
HttpURLConnection   con  =  openConnection  (  baseURL  ,  body  )  ; 
con  .  setRequestMethod  (  method  )  ; 
return   con  ; 
} 

protected   static   HttpURLConnection   sendGet  (  String   baseURL  ,  final   Map  <  String  ,  String  >  fields  )  throws   IOException  { 
return   getConnectionWithMethod  (  baseURL  ,  fields  ,  "GET"  )  ; 
} 

protected   static   HttpURLConnection   send  (  String   baseURL  ,  final   Map  <  String  ,  String  >  fields  ,  String   method  )  throws   IOException  { 
return   getConnectionWithMethod  (  baseURL  ,  fields  ,  method  )  ; 
} 

protected   static   HttpURLConnection   sendPostFormUrlencoded  (  final   String   baseURL  ,  final   Map  <  String  ,  String  >  fields  )  throws   MalformedURLException  ,  IOException  { 
final   StringBuilder   buff  =  new   StringBuilder  (  )  ; 
for  (  Entry  <  String  ,  String  >  entry  :  fields  .  entrySet  (  )  )  { 
buff  .  append  (  entry  .  getKey  (  )  )  .  append  (  "="  )  .  append  (  URLEncoder  .  encode  (  entry  .  getValue  (  )  ,  "UTF-8"  )  )  .  append  (  "&"  )  ; 
} 
final   String   body  =  buff  .  toString  (  )  ; 
HttpURLConnection   con  =  openConnection  (  baseURL  ,  null  )  ; 
con  .  setRequestMethod  (  "POST"  )  ; 
con  .  setAllowUserInteraction  (  false  )  ; 
con  .  setDoOutput  (  true  )  ; 
con  .  setRequestProperty  (  "Content-Type"  ,  "application/x-www-form-urlencoded"  )  ; 
PrintWriter   out  =  new   PrintWriter  (  con  .  getOutputStream  (  )  )  ; 
out  .  print  (  body  )  ; 
out  .  close  (  )  ; 
return   con  ; 
} 

protected   static   byte  [  ]  sendPostFormUrlencodedReadBody  (  final   String   baseURL  ,  final   Map  <  String  ,  String  >  fields  )  throws   MalformedURLException  ,  IOException  { 
ByteArrayOutputStream   bout  =  new   ByteArrayOutputStream  (  )  ; 
HttpURLConnection   conn  =  null  ; 
InputStream   in  =  null  ; 
try  { 
conn  =  sendPostFormUrlencoded  (  baseURL  ,  fields  )  ; 
LOG  .  info  (  "Response ("  +  conn  .  getResponseCode  (  )  +  "): "  +  conn  .  getResponseMessage  (  )  )  ; 
conn  .  getResponseCode  (  )  ; 
in  =  conn  .  getInputStream  (  )  ; 
int   b  ; 
while  (  (  b  =  in  .  read  (  )  )  !=  -  1  )  { 
bout  .  write  (  b  )  ; 
} 
return   bout  .  toByteArray  (  )  ; 
}  finally  { 
if  (  in  !=  null  )  { 
try  { 
in  .  close  (  )  ; 
}  catch  (  IOException   ignored  )  { 
} 
} 
conn  .  disconnect  (  )  ; 
} 
} 

protected   static   HttpURLConnection   sendPostMultipartFormData  (  final   String   baseURL  ,  final   Map  <  String  ,  String  >  fields  )  throws   MalformedURLException  ,  IOException  { 
final   String   boundary  =  "---------------------------1004178514282965110854332084"  ; 
HttpURLConnection   con  =  openConnection  (  baseURL  ,  null  )  ; 
con  .  setRequestMethod  (  "POST"  )  ; 
con  .  setAllowUserInteraction  (  false  )  ; 
con  .  setDoOutput  (  true  )  ; 
con  .  setRequestProperty  (  "Content-Type"  ,  "multipart/form-data; boundary="  +  boundary  )  ; 
PrintWriter   out  =  new   PrintWriter  (  con  .  getOutputStream  (  )  )  ; 
for  (  Entry  <  String  ,  String  >  field  :  fields  .  entrySet  (  )  )  { 
out  .  print  (  "--"  )  ; 
out  .  print  (  boundary  )  ; 
out  .  print  (  CRLF  )  ; 
out  .  print  (  "Content-Disposition: form-data; name=\""  )  ; 
out  .  print  (  field  .  getKey  (  )  )  ; 
out  .  print  (  "\""  )  ; 
if  (  field  .  getKey  (  )  .  equals  (  "data"  )  )  { 
out  .  print  (  "; filename=\"data\""  )  ; 
out  .  print  (  CRLF  )  ; 
out  .  print  (  "Content-Type: application/octet-stream"  )  ; 
} 
out  .  print  (  CRLF  )  ; 
out  .  print  (  CRLF  )  ; 
out  .  print  (  field  .  getValue  (  )  )  ; 
out  .  print  (  CRLF  )  ; 
} 
out  .  print  (  "--"  )  ; 
out  .  print  (  boundary  )  ; 
out  .  print  (  "--"  )  ; 
out  .  print  (  CRLF  )  ; 
out  .  close  (  )  ; 
return   con  ; 
} 
} 

