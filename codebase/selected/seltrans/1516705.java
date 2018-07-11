package   com  .  clouds  .  aic  .  tools  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  DataOutputStream  ; 
import   java  .  io  .  FileInputStream  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  List  ; 
import   org  .  apache  .  http  .  HttpResponse  ; 
import   org  .  apache  .  http  .  NameValuePair  ; 
import   org  .  apache  .  http  .  client  .  HttpClient  ; 
import   org  .  apache  .  http  .  client  .  entity  .  UrlEncodedFormEntity  ; 
import   org  .  apache  .  http  .  client  .  methods  .  HttpPost  ; 
import   org  .  apache  .  http  .  impl  .  client  .  DefaultHttpClient  ; 
import   org  .  apache  .  http  .  message  .  BasicNameValuePair  ; 
import   org  .  apache  .  http  .  util  .  EntityUtils  ; 
import   android  .  graphics  .  Bitmap  ; 
import   android  .  graphics  .  BitmapFactory  ; 
import   android  .  util  .  Log  ; 

public   class   CloudFileManagementService  { 

public   static   HashMap  <  String  ,  Bitmap  >  thumb_cache  =  new   HashMap  <  String  ,  Bitmap  >  (  )  ; 

public   static   String   sayHelloToEveryone  (  )  { 
String   result  =  "Hi, everyboday."  ; 
return   result  ; 
} 








public   static   String   get_feeds_for_current_user  (  String   sessionid  ,  String   maxNumberOfFeedString  ,  String   owner_alias  )  { 
String   resultJsonString  =  "some problem existed inside the create_new_tag() function if you see this string"  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "get_feeds_for_current_user"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Profile"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "get_feeds"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "only_public_feeds"  ,  "false"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "count"  ,  maxNumberOfFeedString  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "order"  ,  "DESC"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "order_field"  ,  "modified"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "owner_alias"  ,  owner_alias  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "before_feed_key"  ,  "current_id"  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
resultJsonString  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  resultJsonString  )  ; 
return   resultJsonString  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   resultJsonString  ; 
} 

















public   static   List  <  JsonObject  >  get_contents_by_tag  (  String   sessionid  ,  String   tag  )  { 
ArrayList   files  =  new   ArrayList  (  )  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "get_contents_by_tag"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "offset"  ,  "0"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "tag"  ,  tag  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Storage"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "count"  ,  "30"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "get_contents_by_tag"  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
String   jsonstring  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  jsonstring  )  ; 
JsonObject   result  =  JsonObject  .  parse  (  jsonstring  )  ; 
Float   status  =  (  Float  )  result  .  getValue  (  "result_code"  )  ; 
Log  .  d  (  "responseStatusCode"  ,  status  .  toString  (  )  )  ; 
if  (  !  status  .  equals  (  (  float  )  0.0  )  )  { 
throw   new   RuntimeException  (  "Error! get files info failed"  )  ; 
} 
JsonObject   data  =  (  JsonObject  )  result  .  getValue  (  "data"  )  ; 
files  =  (  ArrayList  )  data  .  getValue  (  "files"  )  ; 
if  (  files  .  size  (  )  ==  0  )  { 
files  =  new   ArrayList  (  )  ; 
return   files  ; 
}  else  { 
return   files  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   files  ; 
} 







public   static   List  <  JsonObject  >  get_all_share_contents  (  String   sessionid  )  { 
ArrayList   files  =  new   ArrayList  (  )  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "get_share_contents"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "offset"  ,  "0"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Shared"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "count"  ,  "30"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "get_others_initial_contents"  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
String   jsonstring  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  jsonstring  )  ; 
JsonObject   result  =  JsonObject  .  parse  (  jsonstring  )  ; 
Float   status  =  (  Float  )  result  .  getValue  (  "result_code"  )  ; 
Log  .  d  (  "DEBUG"  ,  status  .  toString  (  )  )  ; 
if  (  !  status  .  equals  (  (  float  )  0.0  )  )  { 
throw   new   RuntimeException  (  "Error! get files info failed"  )  ; 
} 
JsonObject   data  =  (  JsonObject  )  result  .  getValue  (  "data"  )  ; 
files  =  (  ArrayList  )  data  .  getValue  (  "files"  )  ; 
return   files  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   files  ; 
} 







public   static   List  <  JsonObject  >  get_personal_share_contents  (  String   sessionid  )  { 
ArrayList   files  =  new   ArrayList  (  )  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "get_share_contents"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Shared"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "get_personal_shared_data"  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
String   jsonstring  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  jsonstring  )  ; 
JsonObject   result  =  JsonObject  .  parse  (  jsonstring  )  ; 
Float   status  =  (  Float  )  result  .  getValue  (  "result_code"  )  ; 
Log  .  d  (  "DEBUG"  ,  status  .  toString  (  )  )  ; 
if  (  !  status  .  equals  (  (  float  )  0.0  )  )  { 
throw   new   RuntimeException  (  "Error! get files info failed"  )  ; 
} 
JsonObject   data  =  (  JsonObject  )  result  .  getValue  (  "data"  )  ; 
files  =  (  ArrayList  )  data  .  getValue  (  "files"  )  ; 
return   files  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   files  ; 
} 








public   static   Bitmap   download_picture_thumb  (  String   sessionid  ,  String   key  )  { 
if  (  thumb_cache  .  containsKey  (  key  )  )  { 
return   thumb_cache  .  get  (  key  )  ; 
} 
OutputStream   os  =  null  ; 
String   urlString  =  "https://mt0-s2.cloud.cm/rpc/raw?c=Pictures&m=download_picture_thumb&thumb_size=medium&key="  +  key  ; 
Bitmap   bitmap  =  null  ; 
ArrayList   files  =  new   ArrayList  (  )  ; 
try  { 
URL   url  =  new   URL  (  urlString  )  ; 
Log  .  d  (  "current running function name:"  ,  "download_picture_thumb"  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
conn  .  setRequestProperty  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
conn  .  setRequestMethod  (  "POST"  )  ; 
conn  .  setDoInput  (  true  )  ; 
InputStream   is  =  conn  .  getInputStream  (  )  ; 
Log  .  d  (  "size of the picture in download_picture_thumb"  ,  ""  +  is  .  available  (  )  )  ; 
bitmap  =  BitmapFactory  .  decodeStream  (  is  )  ; 
thumb_cache  .  put  (  key  ,  bitmap  )  ; 
return   bitmap  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   bitmap  ; 
} 








public   static   Bitmap   download_picture_original  (  String   sessionid  ,  String   key  )  { 
OutputStream   os  =  null  ; 
String   urlString  =  "https://mt0-s2.cloud.cm/rpc/raw?c=Pictures&m=download_picture&key="  +  key  ; 
Bitmap   bitmap  =  null  ; 
ArrayList   files  =  new   ArrayList  (  )  ; 
try  { 
URL   url  =  new   URL  (  urlString  )  ; 
Log  .  d  (  "current running function name:"  ,  "download_picture_original"  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
conn  .  setRequestProperty  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
conn  .  setRequestMethod  (  "POST"  )  ; 
conn  .  setDoInput  (  true  )  ; 
InputStream   is  =  conn  .  getInputStream  (  )  ; 
bitmap  =  BitmapFactory  .  decodeStream  (  is  )  ; 
return   bitmap  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   bitmap  ; 
} 








public   static   String   create_new_tag  (  String   sessionid  ,  String   parentTagName  ,  String   tagName  )  { 
String   resultJsonString  =  "some problem existed inside the create_new_tag() function if you see this string"  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "create_new_tag"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Storage"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "create_new_tag"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "parent_tag"  ,  parentTagName  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "tag"  ,  tagName  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
resultJsonString  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  resultJsonString  )  ; 
return   resultJsonString  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   resultJsonString  ; 
} 









public   static   String   remove_tag  (  String   sessionid  ,  String   absolutePathForTheSpesificTag  )  { 
String   resultJsonString  =  "some problem existed inside the create_new_tag() function if you see this string"  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "remove_tag"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Storage"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "remove_tag"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "absolute_tags"  ,  absolutePathForTheSpesificTag  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
resultJsonString  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  resultJsonString  )  ; 
return   resultJsonString  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   resultJsonString  ; 
} 







public   static   String   remove_file  (  String   sessionid  ,  String   key  )  { 
String   resultJsonString  =  "some problem existed inside the create_new_tag() function if you see this string"  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "remove_file"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Storage"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "remove_file"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "keys"  ,  key  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
resultJsonString  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  resultJsonString  )  ; 
return   resultJsonString  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   resultJsonString  ; 
} 











public   static   String   move_tags  (  String   sessionid  ,  String   absolutePathForTheMovedTags  ,  String   absolutePathForTheDestinationTag  )  { 
String   resultJsonString  =  "some problem existed inside the create_new_tag() function if you see this string"  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "move_tags"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Storage"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "move_tag"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "absolute_new_parent_tag"  ,  absolutePathForTheDestinationTag  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "absolute_tags"  ,  absolutePathForTheMovedTags  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
resultJsonString  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
return   resultJsonString  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   resultJsonString  ; 
} 










public   static   String   move_files  (  String   sessionid  ,  String   keys  ,  String   absolutePathForTheDestinationTag  )  { 
String   resultJsonString  =  "some problem existed inside the create_new_tag() function if you see this string"  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "move_files"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Storage"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "move_file"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "absolute_new_parent_tag"  ,  absolutePathForTheDestinationTag  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "keys"  ,  keys  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
resultJsonString  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  resultJsonString  )  ; 
return   resultJsonString  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   resultJsonString  ; 
} 







public   static   String   rename_file  (  String   sessionid  ,  String   key  ,  String   newFileName  )  { 
String   jsonstring  =  ""  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "rename_file"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Storage"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "rename_file"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "new_name"  ,  newFileName  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "key"  ,  key  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
jsonstring  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  jsonstring  )  ; 
return   jsonstring  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   jsonstring  ; 
} 







public   static   String   rename_tag  (  String   sessionid  ,  String   originalTag  ,  String   newTagName  )  { 
String   jsonstring  =  ""  ; 
try  { 
Log  .  d  (  "current running function name:"  ,  "rename_tag"  )  ; 
HttpClient   httpclient  =  new   DefaultHttpClient  (  )  ; 
HttpPost   httppost  =  new   HttpPost  (  "https://mt0-app.cloud.cm/rpc/json"  )  ; 
List  <  NameValuePair  >  nameValuePairs  =  new   ArrayList  <  NameValuePair  >  (  2  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "c"  ,  "Storage"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "m"  ,  "rename_tag"  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "new_tag_name"  ,  newTagName  )  )  ; 
nameValuePairs  .  add  (  new   BasicNameValuePair  (  "absolute_tag"  ,  originalTag  )  )  ; 
httppost  .  setEntity  (  new   UrlEncodedFormEntity  (  nameValuePairs  )  )  ; 
httppost  .  setHeader  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
HttpResponse   response  =  httpclient  .  execute  (  httppost  )  ; 
jsonstring  =  EntityUtils  .  toString  (  response  .  getEntity  (  )  )  ; 
Log  .  d  (  "jsonStringReturned:"  ,  jsonstring  )  ; 
return   jsonstring  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   jsonstring  ; 
} 






public   static   InputStream   download_file  (  String   sessionid  ,  String   key  )  { 
String   urlString  =  "https://s2.cloud.cm/rpc/raw?c=Storage&m=download_file&key="  +  key  ; 
try  { 
URL   url  =  new   URL  (  urlString  )  ; 
Log  .  d  (  "current running function name:"  ,  "download_file"  )  ; 
HttpURLConnection   conn  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
conn  .  setRequestProperty  (  "Cookie"  ,  "PHPSESSID="  +  sessionid  )  ; 
conn  .  setRequestMethod  (  "POST"  )  ; 
conn  .  setDoInput  (  true  )  ; 
InputStream   is  =  conn  .  getInputStream  (  )  ; 
Log  .  d  (  "size of the picture file"  ,  ""  +  is  .  available  (  )  )  ; 
return   is  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   null  ; 
} 










public   static   String   upload_file  (  String   sessionid  ,  String   localFilePath  ,  String   remoteTagPath  )  { 
String   jsonstring  =  "If you see this message, there is some problem inside the function:upload_file()"  ; 
String   srcPath  =  localFilePath  ; 
String   uploadUrl  =  "https://s2.cloud.cm/rpc/json/?session_id="  +  sessionid  +  "&c=Storage&m=upload_file&tag="  +  remoteTagPath  ; 
String   end  =  "\r\n"  ; 
String   twoHyphens  =  "--"  ; 
String   boundary  =  "******"  ; 
try  { 
URL   url  =  new   URL  (  uploadUrl  )  ; 
HttpURLConnection   httpURLConnection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
httpURLConnection  .  setDoInput  (  true  )  ; 
httpURLConnection  .  setDoOutput  (  true  )  ; 
httpURLConnection  .  setUseCaches  (  false  )  ; 
httpURLConnection  .  setRequestMethod  (  "POST"  )  ; 
httpURLConnection  .  setRequestProperty  (  "Connection"  ,  "Keep-Alive"  )  ; 
httpURLConnection  .  setRequestProperty  (  "Charset"  ,  "UTF-8"  )  ; 
httpURLConnection  .  setRequestProperty  (  "Content-Type"  ,  "multipart/form-data;boundary="  +  boundary  )  ; 
DataOutputStream   dos  =  new   DataOutputStream  (  httpURLConnection  .  getOutputStream  (  )  )  ; 
dos  .  writeBytes  (  twoHyphens  +  boundary  +  end  )  ; 
dos  .  writeBytes  (  "Content-Disposition: form-data; name=\"file\"; filename=\""  +  srcPath  .  substring  (  srcPath  .  lastIndexOf  (  "/"  )  +  1  )  +  "\""  +  end  )  ; 
dos  .  writeBytes  (  end  )  ; 
FileInputStream   fis  =  new   FileInputStream  (  srcPath  )  ; 
byte  [  ]  buffer  =  new   byte  [  8192  ]  ; 
int   count  =  0  ; 
while  (  (  count  =  fis  .  read  (  buffer  )  )  !=  -  1  )  { 
dos  .  write  (  buffer  ,  0  ,  count  )  ; 
} 
fis  .  close  (  )  ; 
dos  .  writeBytes  (  end  )  ; 
dos  .  writeBytes  (  twoHyphens  +  boundary  +  twoHyphens  +  end  )  ; 
dos  .  flush  (  )  ; 
InputStream   is  =  httpURLConnection  .  getInputStream  (  )  ; 
InputStreamReader   isr  =  new   InputStreamReader  (  is  ,  "utf-8"  )  ; 
BufferedReader   br  =  new   BufferedReader  (  isr  )  ; 
jsonstring  =  br  .  readLine  (  )  ; 
dos  .  close  (  )  ; 
is  .  close  (  )  ; 
return   jsonstring  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   jsonstring  ; 
} 
} 

