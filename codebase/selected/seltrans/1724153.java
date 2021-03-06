package   org  .  ofbiz  .  content  .  data  ; 

import   java  .  io  .  *  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLConnection  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  TreeMap  ; 
import   javax  .  servlet  .  http  .  HttpServletRequest  ; 
import   javax  .  servlet  .  http  .  HttpSession  ; 
import   javax  .  xml  .  parsers  .  ParserConfigurationException  ; 
import   org  .  apache  .  commons  .  fileupload  .  DiskFileUpload  ; 
import   org  .  apache  .  commons  .  fileupload  .  FileItem  ; 
import   org  .  apache  .  commons  .  fileupload  .  FileUploadException  ; 
import   org  .  ofbiz  .  base  .  util  .  Debug  ; 
import   org  .  ofbiz  .  base  .  util  .  GeneralException  ; 
import   org  .  ofbiz  .  base  .  util  .  UtilHttp  ; 
import   org  .  ofbiz  .  base  .  util  .  UtilMisc  ; 
import   org  .  ofbiz  .  base  .  util  .  UtilProperties  ; 
import   org  .  ofbiz  .  base  .  util  .  UtilValidate  ; 
import   org  .  ofbiz  .  base  .  util  .  collections  .  MapStack  ; 
import   org  .  ofbiz  .  base  .  util  .  template  .  FreeMarkerWorker  ; 
import   org  .  ofbiz  .  base  .  location  .  FlexibleLocation  ; 
import   org  .  ofbiz  .  content  .  content  .  UploadContentAndImage  ; 
import   org  .  ofbiz  .  content  .  email  .  NotificationServices  ; 
import   org  .  ofbiz  .  entity  .  GenericDelegator  ; 
import   org  .  ofbiz  .  entity  .  GenericEntityException  ; 
import   org  .  ofbiz  .  entity  .  GenericValue  ; 
import   org  .  ofbiz  .  entity  .  util  .  ByteWrapper  ; 
import   org  .  ofbiz  .  service  .  GenericServiceException  ; 
import   org  .  ofbiz  .  service  .  LocalDispatcher  ; 
import   org  .  ofbiz  .  widget  .  html  .  HtmlScreenRenderer  ; 
import   org  .  ofbiz  .  widget  .  screen  .  ModelScreen  ; 
import   org  .  ofbiz  .  widget  .  screen  .  ScreenFactory  ; 
import   org  .  ofbiz  .  widget  .  screen  .  ScreenRenderer  ; 
import   org  .  ofbiz  .  widget  .  screen  .  ScreenStringRenderer  ; 
import   org  .  xml  .  sax  .  SAXException  ; 
import   freemarker  .  template  .  Template  ; 
import   freemarker  .  template  .  TemplateException  ; 
import   javolution  .  util  .  FastMap  ; 




public   class   DataResourceWorker  { 

public   static   final   String   module  =  DataResourceWorker  .  class  .  getName  (  )  ; 

public   static   final   String   err_resource  =  "ContentErrorUiLabel"  ; 







public   static   String   getDataCategoryMap  (  GenericDelegator   delegator  ,  int   depth  ,  Map   categoryNode  ,  List   categoryTypeIds  ,  boolean   getAll  )  throws   GenericEntityException  { 
String   errorMsg  =  null  ; 
String   parentCategoryId  =  (  String  )  categoryNode  .  get  (  "id"  )  ; 
String   currentDataCategoryId  =  null  ; 
int   sz  =  categoryTypeIds  .  size  (  )  ; 
if  (  depth  >=  0  &&  (  sz  -  depth  )  >  0  )  { 
currentDataCategoryId  =  (  String  )  categoryTypeIds  .  get  (  sz  -  depth  -  1  )  ; 
} 
String   matchValue  =  null  ; 
if  (  parentCategoryId  !=  null  )  { 
matchValue  =  parentCategoryId  ; 
}  else  { 
matchValue  =  null  ; 
} 
List   categoryValues  =  delegator  .  findByAndCache  (  "DataCategory"  ,  UtilMisc  .  toMap  (  "parentCategoryId"  ,  matchValue  )  )  ; 
categoryNode  .  put  (  "count"  ,  new   Integer  (  categoryValues  .  size  (  )  )  )  ; 
List   subCategoryIds  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  <  categoryValues  .  size  (  )  ;  i  ++  )  { 
GenericValue   category  =  (  GenericValue  )  categoryValues  .  get  (  i  )  ; 
String   id  =  (  String  )  category  .  get  (  "dataCategoryId"  )  ; 
String   categoryName  =  (  String  )  category  .  get  (  "categoryName"  )  ; 
Map   newNode  =  new   HashMap  (  )  ; 
newNode  .  put  (  "id"  ,  id  )  ; 
newNode  .  put  (  "name"  ,  categoryName  )  ; 
errorMsg  =  getDataCategoryMap  (  delegator  ,  depth  +  1  ,  newNode  ,  categoryTypeIds  ,  getAll  )  ; 
if  (  errorMsg  !=  null  )  break  ; 
subCategoryIds  .  add  (  newNode  )  ; 
} 
if  (  parentCategoryId  ==  null  ||  parentCategoryId  .  equals  (  "ROOT"  )  ||  (  currentDataCategoryId  !=  null  &&  currentDataCategoryId  .  equals  (  parentCategoryId  )  )  ||  getAll  )  { 
categoryNode  .  put  (  "kids"  ,  subCategoryIds  )  ; 
} 
return   errorMsg  ; 
} 




public   static   void   getDataCategoryAncestry  (  GenericDelegator   delegator  ,  String   dataCategoryId  ,  List   categoryTypeIds  )  throws   GenericEntityException  { 
categoryTypeIds  .  add  (  dataCategoryId  )  ; 
GenericValue   dataCategoryValue  =  delegator  .  findByPrimaryKey  (  "DataCategory"  ,  UtilMisc  .  toMap  (  "dataCategoryId"  ,  dataCategoryId  )  )  ; 
if  (  dataCategoryValue  ==  null  )  return  ; 
String   parentCategoryId  =  (  String  )  dataCategoryValue  .  get  (  "parentCategoryId"  )  ; 
if  (  parentCategoryId  !=  null  )  { 
getDataCategoryAncestry  (  delegator  ,  parentCategoryId  ,  categoryTypeIds  )  ; 
} 
} 





public   static   void   buildList  (  HashMap   nd  ,  List   lst  ,  int   depth  )  { 
String   id  =  (  String  )  nd  .  get  (  "id"  )  ; 
String   nm  =  (  String  )  nd  .  get  (  "name"  )  ; 
String   spc  =  ""  ; 
for  (  int   i  =  0  ;  i  <  depth  ;  i  ++  )  spc  +=  "&nbsp;&nbsp;"  ; 
HashMap   map  =  new   HashMap  (  )  ; 
map  .  put  (  "dataCategoryId"  ,  id  )  ; 
map  .  put  (  "categoryName"  ,  spc  +  nm  )  ; 
if  (  id  !=  null  &&  !  id  .  equals  (  "ROOT"  )  &&  !  id  .  equals  (  ""  )  )  { 
lst  .  add  (  map  )  ; 
} 
List   kids  =  (  List  )  nd  .  get  (  "kids"  )  ; 
int   sz  =  kids  .  size  (  )  ; 
for  (  int   i  =  0  ;  i  <  sz  ;  i  ++  )  { 
HashMap   kidNode  =  (  HashMap  )  kids  .  get  (  i  )  ; 
buildList  (  kidNode  ,  lst  ,  depth  +  1  )  ; 
} 
} 

public   static   String   uploadAndStoreImage  (  HttpServletRequest   request  ,  String   idField  ,  String   uploadField  )  { 
DiskFileUpload   fu  =  new   DiskFileUpload  (  )  ; 
List   lst  =  null  ; 
Locale   locale  =  UtilHttp  .  getLocale  (  request  )  ; 
try  { 
lst  =  fu  .  parseRequest  (  request  )  ; 
}  catch  (  FileUploadException   e  )  { 
request  .  setAttribute  (  "_ERROR_MESSAGE_"  ,  e  .  toString  (  )  )  ; 
return  "error"  ; 
} 
if  (  lst  .  size  (  )  ==  0  )  { 
String   errMsg  =  UtilProperties  .  getMessage  (  DataResourceWorker  .  err_resource  ,  "dataResourceWorker.no_files_uploaded"  ,  locale  )  ; 
request  .  setAttribute  (  "_ERROR_MESSAGE_"  ,  errMsg  )  ; 
Debug  .  logWarning  (  "[DataEvents.uploadImage] No files uploaded"  ,  module  )  ; 
return  "error"  ; 
} 
FileItem   fi  =  null  ; 
FileItem   imageFi  =  null  ; 
String   imageFileName  =  null  ; 
Map   passedParams  =  new   HashMap  (  )  ; 
HttpSession   session  =  request  .  getSession  (  )  ; 
GenericValue   userLogin  =  (  GenericValue  )  session  .  getAttribute  (  "userLogin"  )  ; 
passedParams  .  put  (  "userLogin"  ,  userLogin  )  ; 
byte  [  ]  imageBytes  =  null  ; 
for  (  int   i  =  0  ;  i  <  lst  .  size  (  )  ;  i  ++  )  { 
fi  =  (  FileItem  )  lst  .  get  (  i  )  ; 
String   fieldName  =  fi  .  getFieldName  (  )  ; 
if  (  fi  .  isFormField  (  )  )  { 
String   fieldStr  =  fi  .  getString  (  )  ; 
passedParams  .  put  (  fieldName  ,  fieldStr  )  ; 
}  else   if  (  fieldName  .  startsWith  (  "imageData"  )  )  { 
imageFi  =  fi  ; 
imageBytes  =  imageFi  .  get  (  )  ; 
passedParams  .  put  (  fieldName  ,  imageBytes  )  ; 
imageFileName  =  imageFi  .  getName  (  )  ; 
passedParams  .  put  (  "drObjectInfo"  ,  imageFileName  )  ; 
if  (  Debug  .  infoOn  (  )  )  Debug  .  logInfo  (  "[UploadContentAndImage]imageData: "  +  imageBytes  .  length  ,  module  )  ; 
} 
} 
if  (  imageBytes  !=  null  &&  imageBytes  .  length  >  0  )  { 
String   mimeType  =  getMimeTypeFromImageFileName  (  imageFileName  )  ; 
if  (  UtilValidate  .  isNotEmpty  (  mimeType  )  )  { 
passedParams  .  put  (  "drMimeTypeId"  ,  mimeType  )  ; 
try  { 
String   returnMsg  =  UploadContentAndImage  .  processContentUpload  (  passedParams  ,  ""  ,  request  )  ; 
if  (  returnMsg  .  equals  (  "error"  )  )  { 
return  "error"  ; 
} 
}  catch  (  GenericServiceException   e  )  { 
request  .  setAttribute  (  "_ERROR_MESSAGE_"  ,  e  .  getMessage  (  )  )  ; 
return  "error"  ; 
} 
}  else  { 
request  .  setAttribute  (  "_ERROR_MESSAGE_"  ,  "mimeType is empty."  )  ; 
return  "error"  ; 
} 
} 
return  "success"  ; 
} 

public   static   String   getMimeTypeFromImageFileName  (  String   imageFileName  )  { 
String   mimeType  =  null  ; 
if  (  UtilValidate  .  isEmpty  (  imageFileName  )  )  return   mimeType  ; 
int   pos  =  imageFileName  .  lastIndexOf  (  "."  )  ; 
if  (  pos  <  0  )  return   mimeType  ; 
String   suffix  =  imageFileName  .  substring  (  pos  +  1  )  ; 
String   suffixLC  =  suffix  .  toLowerCase  (  )  ; 
if  (  suffixLC  .  equals  (  "jpg"  )  )  mimeType  =  "image/jpeg"  ;  else   mimeType  =  "image/"  +  suffixLC  ; 
return   mimeType  ; 
} 




public   static   String   callDataResourcePermissionCheck  (  GenericDelegator   delegator  ,  LocalDispatcher   dispatcher  ,  Map   context  )  { 
Map   permResults  =  callDataResourcePermissionCheckResult  (  delegator  ,  dispatcher  ,  context  )  ; 
String   permissionStatus  =  (  String  )  permResults  .  get  (  "permissionStatus"  )  ; 
return   permissionStatus  ; 
} 




public   static   Map   callDataResourcePermissionCheckResult  (  GenericDelegator   delegator  ,  LocalDispatcher   dispatcher  ,  Map   context  )  { 
Map   permResults  =  new   HashMap  (  )  ; 
String   skipPermissionCheck  =  (  String  )  context  .  get  (  "skipPermissionCheck"  )  ; 
if  (  Debug  .  infoOn  (  )  )  Debug  .  logInfo  (  "in callDataResourcePermissionCheckResult, skipPermissionCheck:"  +  skipPermissionCheck  ,  ""  )  ; 
if  (  skipPermissionCheck  ==  null  ||  skipPermissionCheck  .  length  (  )  ==  0  ||  (  !  skipPermissionCheck  .  equalsIgnoreCase  (  "true"  )  &&  !  skipPermissionCheck  .  equalsIgnoreCase  (  "granted"  )  )  )  { 
GenericValue   userLogin  =  (  GenericValue  )  context  .  get  (  "userLogin"  )  ; 
Map   serviceInMap  =  new   HashMap  (  )  ; 
serviceInMap  .  put  (  "userLogin"  ,  userLogin  )  ; 
serviceInMap  .  put  (  "targetOperationList"  ,  context  .  get  (  "targetOperationList"  )  )  ; 
serviceInMap  .  put  (  "contentPurposeList"  ,  context  .  get  (  "contentPurposeList"  )  )  ; 
serviceInMap  .  put  (  "entityOperation"  ,  context  .  get  (  "entityOperation"  )  )  ; 
String   ownerContentId  =  (  String  )  context  .  get  (  "ownerContentId"  )  ; 
if  (  ownerContentId  !=  null  &&  ownerContentId  .  length  (  )  >  0  )  { 
try  { 
GenericValue   content  =  delegator  .  findByPrimaryKeyCache  (  "Content"  ,  UtilMisc  .  toMap  (  "contentId"  ,  ownerContentId  )  )  ; 
if  (  content  !=  null  )  serviceInMap  .  put  (  "currentContent"  ,  content  )  ; 
}  catch  (  GenericEntityException   e  )  { 
Debug  .  logError  (  e  ,  "e.getMessage()"  ,  "ContentServices"  )  ; 
} 
} 
try  { 
permResults  =  dispatcher  .  runSync  (  "checkContentPermission"  ,  serviceInMap  )  ; 
}  catch  (  GenericServiceException   e  )  { 
Debug  .  logError  (  e  ,  "Problem checking permissions"  ,  "ContentServices"  )  ; 
} 
}  else  { 
permResults  .  put  (  "permissionStatus"  ,  "granted"  )  ; 
} 
return   permResults  ; 
} 




public   static   byte  [  ]  acquireImage  (  GenericDelegator   delegator  ,  String   dataResourceId  )  throws   GenericEntityException  { 
byte  [  ]  b  =  null  ; 
GenericValue   dataResource  =  delegator  .  findByPrimaryKeyCache  (  "DataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
if  (  dataResource  ==  null  )  return   b  ; 
b  =  acquireImage  (  delegator  ,  dataResource  )  ; 
return   b  ; 
} 

public   static   byte  [  ]  acquireImage  (  GenericDelegator   delegator  ,  GenericValue   dataResource  )  throws   GenericEntityException  { 
byte  [  ]  b  =  null  ; 
String   dataResourceTypeId  =  dataResource  .  getString  (  "dataResourceTypeId"  )  ; 
String   dataResourceId  =  dataResource  .  getString  (  "dataResourceId"  )  ; 
GenericValue   imageDataResource  =  delegator  .  findByPrimaryKey  (  "ImageDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
if  (  imageDataResource  !=  null  )  { 
b  =  imageDataResource  .  getBytes  (  "imageData"  )  ; 
} 
return   b  ; 
} 




public   static   String   getImageType  (  GenericDelegator   delegator  ,  String   dataResourceId  )  throws   GenericEntityException  { 
GenericValue   dataResource  =  delegator  .  findByPrimaryKey  (  "DataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
String   imageType  =  getImageType  (  delegator  ,  dataResource  )  ; 
return   imageType  ; 
} 

public   static   String   getMimeType  (  GenericValue   dataResource  )  { 
String   mimeTypeId  =  null  ; 
if  (  dataResource  !=  null  )  { 
mimeTypeId  =  (  String  )  dataResource  .  get  (  "mimeTypeId"  )  ; 
if  (  UtilValidate  .  isEmpty  (  mimeTypeId  )  )  { 
String   fileName  =  (  String  )  dataResource  .  get  (  "objectInfo"  )  ; 
if  (  fileName  !=  null  &&  fileName  .  indexOf  (  '.'  )  >  -  1  )  { 
String   fileExtension  =  fileName  .  substring  (  fileName  .  lastIndexOf  (  '.'  )  +  1  )  ; 
if  (  UtilValidate  .  isNotEmpty  (  fileExtension  )  )  { 
GenericValue   ext  =  null  ; 
try  { 
ext  =  dataResource  .  getDelegator  (  )  .  findByPrimaryKey  (  "FileExtension"  ,  UtilMisc  .  toMap  (  "fileExtensionId"  ,  fileExtension  )  )  ; 
}  catch  (  GenericEntityException   e  )  { 
Debug  .  logError  (  e  ,  module  )  ; 
} 
if  (  ext  !=  null  )  { 
mimeTypeId  =  ext  .  getString  (  "mimeTypeId"  )  ; 
} 
} 
} 
if  (  UtilValidate  .  isEmpty  (  mimeTypeId  )  )  { 
mimeTypeId  =  "application/octet-stream"  ; 
} 
} 
} 
return   mimeTypeId  ; 
} 


public   static   String   getImageType  (  GenericDelegator   delegator  ,  GenericValue   dataResource  )  { 
String   imageType  =  null  ; 
if  (  dataResource  !=  null  )  { 
imageType  =  (  String  )  dataResource  .  get  (  "mimeTypeId"  )  ; 
if  (  UtilValidate  .  isEmpty  (  imageType  )  )  { 
String   imageFileNameExt  =  null  ; 
String   imageFileName  =  (  String  )  dataResource  .  get  (  "objectInfo"  )  ; 
if  (  UtilValidate  .  isNotEmpty  (  imageFileName  )  )  { 
int   pos  =  imageFileName  .  lastIndexOf  (  "."  )  ; 
if  (  pos  >=  0  )  imageFileNameExt  =  imageFileName  .  substring  (  pos  +  1  )  ; 
} 
imageType  =  "image/"  +  imageFileNameExt  ; 
} 
} 
return   imageType  ; 
} 

public   static   String   buildRequestPrefix  (  GenericDelegator   delegator  ,  Locale   locale  ,  String   webSiteId  ,  String   https  )  { 
Map   prefixValues  =  FastMap  .  newInstance  (  )  ; 
String   prefix  ; 
NotificationServices  .  setBaseUrl  (  delegator  ,  webSiteId  ,  prefixValues  )  ; 
if  (  https  !=  null  &&  https  .  equalsIgnoreCase  (  "true"  )  )  { 
prefix  =  (  String  )  prefixValues  .  get  (  "baseSecureUrl"  )  ; 
}  else  { 
prefix  =  (  String  )  prefixValues  .  get  (  "baseUrl"  )  ; 
} 
if  (  UtilValidate  .  isEmpty  (  prefix  )  )  { 
if  (  https  !=  null  &&  https  .  equalsIgnoreCase  (  "true"  )  )  { 
prefix  =  UtilProperties  .  getMessage  (  "content"  ,  "baseSecureUrl"  ,  locale  )  ; 
}  else  { 
prefix  =  UtilProperties  .  getMessage  (  "content"  ,  "baseUrl"  ,  locale  )  ; 
} 
} 
return   prefix  ; 
} 

public   static   File   getContentFile  (  String   dataResourceTypeId  ,  String   objectInfo  ,  String   contextRoot  )  throws   GeneralException  ,  FileNotFoundException  { 
File   file  =  null  ; 
if  (  dataResourceTypeId  .  equals  (  "LOCAL_FILE"  )  ||  dataResourceTypeId  .  equals  (  "LOCAL_FILE_BIN"  )  )  { 
file  =  new   File  (  objectInfo  )  ; 
if  (  !  file  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "No file found: "  +  (  objectInfo  )  )  ; 
} 
if  (  !  file  .  isAbsolute  (  )  )  { 
throw   new   GeneralException  (  "File ("  +  objectInfo  +  ") is not absolute"  )  ; 
} 
}  else   if  (  dataResourceTypeId  .  equals  (  "OFBIZ_FILE"  )  ||  dataResourceTypeId  .  equals  (  "OFBIZ_FILE_BIN"  )  )  { 
String   prefix  =  System  .  getProperty  (  "ofbiz.home"  )  ; 
String   sep  =  ""  ; 
if  (  objectInfo  .  indexOf  (  "/"  )  !=  0  &&  prefix  .  lastIndexOf  (  "/"  )  !=  (  prefix  .  length  (  )  -  1  )  )  { 
sep  =  "/"  ; 
} 
file  =  new   File  (  prefix  +  sep  +  objectInfo  )  ; 
if  (  !  file  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "No file found: "  +  (  prefix  +  sep  +  objectInfo  )  )  ; 
} 
}  else   if  (  dataResourceTypeId  .  equals  (  "CONTEXT_FILE"  )  ||  dataResourceTypeId  .  equals  (  "CONTEXT_FILE_BIN"  )  )  { 
if  (  UtilValidate  .  isEmpty  (  contextRoot  )  )  { 
throw   new   GeneralException  (  "Cannot find CONTEXT_FILE with an empty context root!"  )  ; 
} 
String   sep  =  ""  ; 
if  (  objectInfo  .  indexOf  (  "/"  )  !=  0  &&  contextRoot  .  lastIndexOf  (  "/"  )  !=  (  contextRoot  .  length  (  )  -  1  )  )  { 
sep  =  "/"  ; 
} 
file  =  new   File  (  contextRoot  +  sep  +  objectInfo  )  ; 
if  (  !  file  .  exists  (  )  )  { 
throw   new   FileNotFoundException  (  "No file found: "  +  (  contextRoot  +  sep  +  objectInfo  )  )  ; 
} 
} 
return   file  ; 
} 

public   static   String   getDataResourceMimeType  (  GenericDelegator   delegator  ,  String   dataResourceId  ,  GenericValue   view  )  throws   GenericEntityException  { 
String   mimeType  =  null  ; 
if  (  view  !=  null  )  mimeType  =  view  .  getString  (  "drMimeTypeId"  )  ; 
if  (  UtilValidate  .  isEmpty  (  mimeType  )  &&  UtilValidate  .  isNotEmpty  (  dataResourceId  )  )  { 
GenericValue   dataResource  =  delegator  .  findByPrimaryKeyCache  (  "DataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
mimeType  =  dataResource  .  getString  (  "mimeTypeId"  )  ; 
} 
return   mimeType  ; 
} 

public   static   String   getDataResourceContentUploadPath  (  )  { 
String   initialPath  =  UtilProperties  .  getPropertyValue  (  "content.properties"  ,  "content.upload.path.prefix"  )  ; 
double   maxFiles  =  UtilProperties  .  getPropertyNumber  (  "content.properties"  ,  "content.upload.max.files"  )  ; 
if  (  maxFiles  <  1  )  { 
maxFiles  =  250  ; 
} 
String   ofbizHome  =  System  .  getProperty  (  "ofbiz.home"  )  ; 
if  (  !  initialPath  .  startsWith  (  "/"  )  )  { 
initialPath  =  "/"  +  initialPath  ; 
} 
Comparator   desc  =  new   Comparator  (  )  { 

public   int   compare  (  Object   o1  ,  Object   o2  )  { 
if  (  (  (  Long  )  o1  )  .  longValue  (  )  >  (  (  Long  )  o2  )  .  longValue  (  )  )  { 
return  -  1  ; 
}  else   if  (  (  (  Long  )  o1  )  .  longValue  (  )  <  (  (  Long  )  o2  )  .  longValue  (  )  )  { 
return   1  ; 
} 
return   0  ; 
} 
}  ; 
String   parentDir  =  ofbizHome  +  initialPath  ; 
File   parent  =  new   File  (  parentDir  )  ; 
TreeMap   dirMap  =  new   TreeMap  (  desc  )  ; 
if  (  parent  .  exists  (  )  )  { 
File  [  ]  subs  =  parent  .  listFiles  (  )  ; 
for  (  int   i  =  0  ;  i  <  subs  .  length  ;  i  ++  )  { 
if  (  subs  [  i  ]  .  isDirectory  (  )  )  { 
dirMap  .  put  (  new   Long  (  subs  [  0  ]  .  lastModified  (  )  )  ,  subs  [  i  ]  )  ; 
} 
} 
}  else  { 
boolean   created  =  parent  .  mkdir  (  )  ; 
if  (  !  created  )  { 
Debug  .  logWarning  (  "Unable to create top level upload directory ["  +  parentDir  +  "]."  ,  module  )  ; 
} 
} 
File   latestDir  =  null  ; 
if  (  dirMap  !=  null  &&  dirMap  .  size  (  )  >  0  )  { 
latestDir  =  (  File  )  dirMap  .  values  (  )  .  iterator  (  )  .  next  (  )  ; 
if  (  latestDir  !=  null  )  { 
File  [  ]  dirList  =  latestDir  .  listFiles  (  )  ; 
if  (  dirList  .  length  >=  maxFiles  )  { 
latestDir  =  makeNewDirectory  (  parent  )  ; 
} 
} 
}  else  { 
latestDir  =  makeNewDirectory  (  parent  )  ; 
} 
Debug  .  log  (  "Directory Name : "  +  latestDir  .  getName  (  )  ,  module  )  ; 
return   latestDir  .  getAbsolutePath  (  )  .  replace  (  '\\'  ,  '/'  )  ; 
} 

private   static   File   makeNewDirectory  (  File   parent  )  { 
File   latestDir  =  null  ; 
boolean   newDir  =  false  ; 
while  (  !  newDir  )  { 
latestDir  =  new   File  (  parent  ,  ""  +  System  .  currentTimeMillis  (  )  )  ; 
if  (  !  latestDir  .  exists  (  )  )  { 
latestDir  .  mkdir  (  )  ; 
newDir  =  true  ; 
} 
} 
return   latestDir  ; 
} 

public   static   String   renderDataResourceAsText  (  GenericDelegator   delegator  ,  String   dataResourceId  ,  Map   templateContext  ,  Locale   locale  ,  String   targetMimeTypeId  ,  boolean   cache  )  throws   GeneralException  ,  IOException  { 
Writer   writer  =  new   StringWriter  (  )  ; 
renderDataResourceAsText  (  delegator  ,  dataResourceId  ,  writer  ,  templateContext  ,  locale  ,  targetMimeTypeId  ,  cache  )  ; 
return   writer  .  toString  (  )  ; 
} 

public   static   void   renderDataResourceAsText  (  GenericDelegator   delegator  ,  String   dataResourceId  ,  Writer   out  ,  Map   templateContext  ,  Locale   locale  ,  String   targetMimeTypeId  ,  boolean   cache  )  throws   GeneralException  ,  IOException  { 
if  (  dataResourceId  ==  null  )  { 
throw   new   GeneralException  (  "Cannot lookup data resource with for a null dataResourceId"  )  ; 
} 
if  (  templateContext  ==  null  )  { 
templateContext  =  FastMap  .  newInstance  (  )  ; 
} 
if  (  UtilValidate  .  isEmpty  (  targetMimeTypeId  )  )  { 
targetMimeTypeId  =  "text/html"  ; 
} 
if  (  locale  ==  null  )  { 
locale  =  Locale  .  getDefault  (  )  ; 
} 
if  (  cache  )  { 
String   disableCache  =  UtilProperties  .  getPropertyValue  (  "content"  ,  "disable.ftl.template.cache"  )  ; 
if  (  disableCache  ==  null  ||  !  disableCache  .  equalsIgnoreCase  (  "true"  )  )  { 
Template   cachedTemplate  =  FreeMarkerWorker  .  getTemplateCached  (  dataResourceId  )  ; 
if  (  cachedTemplate  !=  null  )  { 
try  { 
String   subContentId  =  (  String  )  templateContext  .  get  (  "subContentId"  )  ; 
if  (  UtilValidate  .  isNotEmpty  (  subContentId  )  )  { 
templateContext  .  put  (  "contentId"  ,  subContentId  )  ; 
templateContext  .  put  (  "subContentId"  ,  null  )  ; 
templateContext  .  put  (  "globalNodeTrail"  ,  null  )  ; 
} 
FreeMarkerWorker  .  renderTemplateCached  (  cachedTemplate  ,  templateContext  ,  out  )  ; 
}  catch  (  TemplateException   e  )  { 
Debug  .  logError  (  "Error rendering FTL template. "  +  e  .  getMessage  (  )  ,  module  )  ; 
throw   new   GeneralException  (  "Error rendering FTL template"  ,  e  )  ; 
} 
return  ; 
} 
} 
} 
if  (  !  targetMimeTypeId  .  startsWith  (  "text/"  )  )  { 
throw   new   GeneralException  (  "The desired mime-type is not a text type, cannot render as text: "  +  targetMimeTypeId  )  ; 
} 
GenericValue   dataResource  =  null  ; 
if  (  cache  )  { 
dataResource  =  delegator  .  findByPrimaryKeyCache  (  "DataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
}  else  { 
dataResource  =  delegator  .  findByPrimaryKey  (  "DataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
} 
if  (  dataResource  ==  null  )  { 
throw   new   GeneralException  (  "No data resource object found for dataResourceId: ["  +  dataResourceId  +  "]"  )  ; 
} 
String   dataTemplateTypeId  =  dataResource  .  getString  (  "dataTemplateTypeId"  )  ; 
if  (  UtilValidate  .  isEmpty  (  dataTemplateTypeId  )  ||  "NONE"  .  equals  (  dataTemplateTypeId  )  )  { 
DataResourceWorker  .  writeDataResourceText  (  dataResource  ,  targetMimeTypeId  ,  locale  ,  templateContext  ,  delegator  ,  out  ,  true  )  ; 
}  else  { 
templateContext  .  put  (  "mimeTypeId"  ,  targetMimeTypeId  )  ; 
if  (  "FTL"  .  equals  (  dataTemplateTypeId  )  )  { 
try  { 
String   templateText  =  getDataResourceText  (  dataResource  ,  targetMimeTypeId  ,  locale  ,  templateContext  ,  delegator  ,  cache  )  ; 
FreeMarkerWorker  .  renderTemplate  (  "DataResource:"  +  dataResourceId  ,  templateText  ,  templateContext  ,  out  )  ; 
}  catch  (  TemplateException   e  )  { 
throw   new   GeneralException  (  "Error rendering FTL template"  ,  e  )  ; 
} 
}  else   if  (  "SCREEN_COMBINED"  .  equals  (  dataTemplateTypeId  )  )  { 
try  { 
MapStack   context  =  MapStack  .  create  (  templateContext  )  ; 
context  .  put  (  "locale"  ,  locale  )  ; 
Map   prc  =  FastMap  .  newInstance  (  )  ; 
String   textData  =  (  String  )  context  .  get  (  "textData"  )  ; 
String   mapKey  =  (  String  )  context  .  get  (  "mapKey"  )  ; 
if  (  mapKey  !=  null  )  { 
prc  .  put  (  mapKey  ,  textData  )  ; 
} 
prc  .  put  (  "body"  ,  textData  )  ; 
context  .  put  (  "preRenderedContent"  ,  prc  )  ; 
ScreenRenderer   screens  =  (  ScreenRenderer  )  context  .  get  (  "screens"  )  ; 
if  (  screens  ==  null  )  { 
screens  =  new   ScreenRenderer  (  out  ,  context  ,  new   HtmlScreenRenderer  (  )  )  ; 
screens  .  getContext  (  )  .  put  (  "screens"  ,  screens  )  ; 
} 
ScreenStringRenderer   renderer  =  screens  .  getScreenStringRenderer  (  )  ; 
String   combinedName  =  (  String  )  dataResource  .  get  (  "objectInfo"  )  ; 
ModelScreen   modelScreen  =  ScreenFactory  .  getScreenFromLocation  (  combinedName  )  ; 
modelScreen  .  renderScreenString  (  out  ,  context  ,  renderer  )  ; 
}  catch  (  SAXException   e  )  { 
throw   new   GeneralException  (  "Error rendering Screen template"  ,  e  )  ; 
}  catch  (  ParserConfigurationException   e  )  { 
throw   new   GeneralException  (  "Error rendering Screen template"  ,  e  )  ; 
} 
}  else  { 
throw   new   GeneralException  (  "The dataTemplateTypeId ["  +  dataTemplateTypeId  +  "] is not yet supported"  )  ; 
} 
} 
} 


public   static   String   renderDataResourceAsText  (  GenericDelegator   delegator  ,  String   dataResourceId  ,  Map   templateContext  ,  GenericValue   view  ,  Locale   locale  ,  String   mimeTypeId  )  throws   GeneralException  ,  IOException  { 
return   renderDataResourceAsText  (  delegator  ,  dataResourceId  ,  templateContext  ,  locale  ,  mimeTypeId  ,  false  )  ; 
} 


public   static   void   renderDataResourceAsText  (  GenericDelegator   delegator  ,  String   dataResourceId  ,  Writer   out  ,  Map   templateContext  ,  GenericValue   view  ,  Locale   locale  ,  String   targetMimeTypeId  )  throws   GeneralException  ,  IOException  { 
renderDataResourceAsText  (  delegator  ,  dataResourceId  ,  out  ,  templateContext  ,  locale  ,  targetMimeTypeId  ,  false  )  ; 
} 


public   static   String   renderDataResourceAsTextCache  (  GenericDelegator   delegator  ,  String   dataResourceId  ,  Map   templateContext  ,  GenericValue   view  ,  Locale   locale  ,  String   mimeTypeId  )  throws   GeneralException  ,  IOException  { 
return   renderDataResourceAsText  (  delegator  ,  dataResourceId  ,  templateContext  ,  locale  ,  mimeTypeId  ,  true  )  ; 
} 


public   static   void   renderDataResourceAsTextCache  (  GenericDelegator   delegator  ,  String   dataResourceId  ,  Writer   out  ,  Map   templateContext  ,  GenericValue   view  ,  Locale   locale  ,  String   targetMimeTypeId  )  throws   GeneralException  ,  IOException  { 
renderDataResourceAsText  (  delegator  ,  dataResourceId  ,  out  ,  templateContext  ,  locale  ,  targetMimeTypeId  ,  true  )  ; 
} 

public   static   String   getDataResourceText  (  GenericValue   dataResource  ,  String   mimeTypeId  ,  Locale   locale  ,  Map   context  ,  GenericDelegator   delegator  ,  boolean   cache  )  throws   IOException  ,  GeneralException  { 
Writer   out  =  new   StringWriter  (  )  ; 
writeDataResourceText  (  dataResource  ,  mimeTypeId  ,  locale  ,  context  ,  delegator  ,  out  ,  cache  )  ; 
return   out  .  toString  (  )  ; 
} 

public   static   void   writeDataResourceText  (  GenericValue   dataResource  ,  String   mimeTypeId  ,  Locale   locale  ,  Map   templateContext  ,  GenericDelegator   delegator  ,  Writer   out  ,  boolean   cache  )  throws   IOException  ,  GeneralException  { 
Map   context  =  (  Map  )  templateContext  .  get  (  "context"  )  ; 
if  (  context  ==  null  )  { 
context  =  FastMap  .  newInstance  (  )  ; 
} 
String   webSiteId  =  (  String  )  templateContext  .  get  (  "webSiteId"  )  ; 
if  (  UtilValidate  .  isEmpty  (  webSiteId  )  )  { 
if  (  context  !=  null  )  webSiteId  =  (  String  )  context  .  get  (  "webSiteId"  )  ; 
} 
String   https  =  (  String  )  templateContext  .  get  (  "https"  )  ; 
if  (  UtilValidate  .  isEmpty  (  https  )  )  { 
if  (  context  !=  null  )  https  =  (  String  )  context  .  get  (  "https"  )  ; 
} 
String   dataResourceId  =  dataResource  .  getString  (  "dataResourceId"  )  ; 
String   dataResourceTypeId  =  dataResource  .  getString  (  "dataResourceTypeId"  )  ; 
if  (  UtilValidate  .  isEmpty  (  dataResourceTypeId  )  )  { 
dataResourceTypeId  =  "SHORT_TEXT"  ; 
} 
if  (  "SHORT_TEXT"  .  equals  (  dataResourceTypeId  )  ||  "LINK"  .  equals  (  dataResourceTypeId  )  )  { 
String   text  =  dataResource  .  getString  (  "objectInfo"  )  ; 
writeText  (  dataResource  ,  text  ,  templateContext  ,  mimeTypeId  ,  locale  ,  out  )  ; 
}  else   if  (  "ELECTRONIC_TEXT"  .  equals  (  dataResourceTypeId  )  )  { 
GenericValue   electronicText  ; 
if  (  cache  )  { 
electronicText  =  delegator  .  findByPrimaryKeyCache  (  "ElectronicText"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
}  else  { 
electronicText  =  delegator  .  findByPrimaryKey  (  "ElectronicText"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
} 
String   text  =  electronicText  .  getString  (  "textData"  )  ; 
writeText  (  dataResource  ,  text  ,  templateContext  ,  mimeTypeId  ,  locale  ,  out  )  ; 
}  else   if  (  dataResourceTypeId  .  endsWith  (  "_OBJECT"  )  )  { 
String   text  =  (  String  )  dataResource  .  get  (  "dataResourceId"  )  ; 
writeText  (  dataResource  ,  text  ,  templateContext  ,  mimeTypeId  ,  locale  ,  out  )  ; 
}  else   if  (  dataResourceTypeId  .  equals  (  "URL_RESOURCE"  )  )  { 
String   text  =  null  ; 
URL   url  =  FlexibleLocation  .  resolveLocation  (  dataResource  .  getString  (  "objectInfo"  )  )  ; 
if  (  url  .  getHost  (  )  !=  null  )  { 
InputStream   in  =  url  .  openStream  (  )  ; 
int   c  ; 
StringWriter   sw  =  new   StringWriter  (  )  ; 
while  (  (  c  =  in  .  read  (  )  )  !=  -  1  )  { 
sw  .  write  (  c  )  ; 
} 
sw  .  close  (  )  ; 
text  =  sw  .  toString  (  )  ; 
}  else  { 
String   prefix  =  DataResourceWorker  .  buildRequestPrefix  (  delegator  ,  locale  ,  webSiteId  ,  https  )  ; 
String   sep  =  ""  ; 
if  (  url  .  toString  (  )  .  indexOf  (  "/"  )  !=  0  &&  prefix  .  lastIndexOf  (  "/"  )  !=  (  prefix  .  length  (  )  -  1  )  )  { 
sep  =  "/"  ; 
} 
String   fixedUrlStr  =  prefix  +  sep  +  url  .  toString  (  )  ; 
URL   fixedUrl  =  new   URL  (  fixedUrlStr  )  ; 
text  =  (  String  )  fixedUrl  .  getContent  (  )  ; 
} 
out  .  write  (  text  )  ; 
}  else   if  (  dataResourceTypeId  .  endsWith  (  "_FILE_BIN"  )  )  { 
writeText  (  dataResource  ,  dataResourceId  ,  templateContext  ,  mimeTypeId  ,  locale  ,  out  )  ; 
}  else   if  (  dataResourceTypeId  .  endsWith  (  "_FILE"  )  )  { 
String   dataResourceMimeTypeId  =  dataResource  .  getString  (  "mimeTypeId"  )  ; 
String   objectInfo  =  dataResource  .  getString  (  "objectInfo"  )  ; 
String   rootDir  =  (  String  )  context  .  get  (  "rootDir"  )  ; 
if  (  dataResourceMimeTypeId  ==  null  ||  dataResourceMimeTypeId  .  startsWith  (  "text"  )  )  { 
renderFile  (  dataResourceTypeId  ,  objectInfo  ,  rootDir  ,  out  )  ; 
}  else  { 
writeText  (  dataResource  ,  dataResourceId  ,  templateContext  ,  mimeTypeId  ,  locale  ,  out  )  ; 
} 
}  else  { 
throw   new   GeneralException  (  "The dataResourceTypeId ["  +  dataResourceTypeId  +  "] is not supported in renderDataResourceAsText"  )  ; 
} 
} 


public   static   String   getDataResourceTextCache  (  GenericValue   dataResource  ,  String   mimeTypeId  ,  Locale   locale  ,  Map   context  ,  GenericDelegator   delegator  )  throws   IOException  ,  GeneralException  { 
return   getDataResourceText  (  dataResource  ,  mimeTypeId  ,  locale  ,  context  ,  delegator  ,  true  )  ; 
} 


public   static   void   writeDataResourceTextCache  (  GenericValue   dataResource  ,  String   mimeTypeId  ,  Locale   locale  ,  Map   context  ,  GenericDelegator   delegator  ,  Writer   outWriter  )  throws   IOException  ,  GeneralException  { 
writeDataResourceText  (  dataResource  ,  mimeTypeId  ,  locale  ,  context  ,  delegator  ,  outWriter  ,  true  )  ; 
} 


public   static   String   getDataResourceText  (  GenericValue   dataResource  ,  String   mimeTypeId  ,  Locale   locale  ,  Map   context  ,  GenericDelegator   delegator  )  throws   IOException  ,  GeneralException  { 
return   getDataResourceText  (  dataResource  ,  mimeTypeId  ,  locale  ,  context  ,  delegator  ,  false  )  ; 
} 


public   static   void   writeDataResourceText  (  GenericValue   dataResource  ,  String   mimeTypeId  ,  Locale   locale  ,  Map   context  ,  GenericDelegator   delegator  ,  Writer   out  )  throws   IOException  ,  GeneralException  { 
writeDataResourceText  (  dataResource  ,  mimeTypeId  ,  locale  ,  context  ,  delegator  ,  out  ,  false  )  ; 
} 

public   static   void   writeText  (  GenericValue   dataResource  ,  String   textData  ,  Map   context  ,  String   targetMimeTypeId  ,  Locale   locale  ,  Writer   out  )  throws   GeneralException  ,  IOException  { 
String   dataResourceMimeTypeId  =  dataResource  .  getString  (  "mimeTypeId"  )  ; 
GenericDelegator   delegator  =  dataResource  .  getDelegator  (  )  ; 
if  (  UtilValidate  .  isEmpty  (  dataResourceMimeTypeId  )  )  { 
dataResourceMimeTypeId  =  "text/html"  ; 
} 
if  (  UtilValidate  .  isEmpty  (  targetMimeTypeId  )  )  { 
targetMimeTypeId  =  "text/html"  ; 
} 
if  (  !  targetMimeTypeId  .  startsWith  (  "text"  )  )  { 
throw   new   GeneralException  (  "Method writeText() only supports rendering text content : "  +  targetMimeTypeId  +  " is not supported"  )  ; 
} 
if  (  "text/html"  .  equals  (  targetMimeTypeId  )  )  { 
GenericValue   mimeTypeTemplate  =  delegator  .  findByPrimaryKeyCache  (  "MimeTypeHtmlTemplate"  ,  UtilMisc  .  toMap  (  "mimeTypeId"  ,  dataResourceMimeTypeId  )  )  ; 
if  (  mimeTypeTemplate  !=  null  &&  mimeTypeTemplate  .  get  (  "templateLocation"  )  !=  null  )  { 
Map   mimeContext  =  FastMap  .  newInstance  (  )  ; 
mimeContext  .  putAll  (  context  )  ; 
mimeContext  .  put  (  "dataResource"  ,  dataResource  )  ; 
mimeContext  .  put  (  "textData"  ,  textData  )  ; 
String   mimeString  =  DataResourceWorker  .  renderMimeTypeTemplate  (  mimeTypeTemplate  ,  context  )  ; 
out  .  write  (  mimeString  )  ; 
}  else  { 
out  .  write  (  textData  )  ; 
} 
}  else   if  (  "text/plain"  .  equals  (  targetMimeTypeId  )  )  { 
out  .  write  (  textData  )  ; 
} 
} 

public   static   String   renderMimeTypeTemplate  (  GenericValue   mimeTypeTemplate  ,  Map   context  )  throws   GeneralException  ,  IOException  { 
String   location  =  mimeTypeTemplate  .  getString  (  "templateLocation"  )  ; 
StringWriter   writer  =  new   StringWriter  (  )  ; 
try  { 
FreeMarkerWorker  .  renderTemplateAtLocation  (  location  ,  context  ,  writer  )  ; 
}  catch  (  TemplateException   e  )  { 
throw   new   GeneralException  (  e  .  getMessage  (  )  ,  e  )  ; 
} 
return   writer  .  toString  (  )  ; 
} 

public   static   void   renderFile  (  String   dataResourceTypeId  ,  String   objectInfo  ,  String   rootDir  ,  Writer   out  )  throws   GeneralException  ,  IOException  { 
if  (  dataResourceTypeId  .  equals  (  "LOCAL_FILE"  )  )  { 
File   file  =  new   File  (  objectInfo  )  ; 
if  (  !  file  .  isAbsolute  (  )  )  { 
throw   new   GeneralException  (  "File ("  +  objectInfo  +  ") is not absolute"  )  ; 
} 
int   c  ; 
FileReader   in  =  new   FileReader  (  file  )  ; 
while  (  (  c  =  in  .  read  (  )  )  !=  -  1  )  { 
out  .  write  (  c  )  ; 
} 
}  else   if  (  dataResourceTypeId  .  equals  (  "OFBIZ_FILE"  )  )  { 
String   prefix  =  System  .  getProperty  (  "ofbiz.home"  )  ; 
String   sep  =  ""  ; 
if  (  objectInfo  .  indexOf  (  "/"  )  !=  0  &&  prefix  .  lastIndexOf  (  "/"  )  !=  (  prefix  .  length  (  )  -  1  )  )  { 
sep  =  "/"  ; 
} 
File   file  =  new   File  (  prefix  +  sep  +  objectInfo  )  ; 
int   c  ; 
FileReader   in  =  new   FileReader  (  file  )  ; 
while  (  (  c  =  in  .  read  (  )  )  !=  -  1  )  out  .  write  (  c  )  ; 
}  else   if  (  dataResourceTypeId  .  equals  (  "CONTEXT_FILE"  )  )  { 
String   prefix  =  rootDir  ; 
String   sep  =  ""  ; 
if  (  objectInfo  .  indexOf  (  "/"  )  !=  0  &&  prefix  .  lastIndexOf  (  "/"  )  !=  (  prefix  .  length  (  )  -  1  )  )  { 
sep  =  "/"  ; 
} 
File   file  =  new   File  (  prefix  +  sep  +  objectInfo  )  ; 
int   c  ; 
FileReader   in  =  null  ; 
try  { 
in  =  new   FileReader  (  file  )  ; 
String   enc  =  in  .  getEncoding  (  )  ; 
if  (  Debug  .  infoOn  (  )  )  Debug  .  logInfo  (  "in serveImage, encoding:"  +  enc  ,  module  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
Debug  .  logError  (  e  ,  " in renderDataResourceAsHtml(CONTEXT_FILE), in FNFexception:"  ,  module  )  ; 
throw   new   GeneralException  (  "Could not find context file to render"  ,  e  )  ; 
}  catch  (  Exception   e  )  { 
Debug  .  logError  (  " in renderDataResourceAsHtml(CONTEXT_FILE), got exception:"  +  e  .  getMessage  (  )  ,  module  )  ; 
} 
while  (  (  c  =  in  .  read  (  )  )  !=  -  1  )  { 
out  .  write  (  c  )  ; 
} 
} 
} 













public   static   Map   getDataResourceStream  (  GenericValue   dataResource  ,  String   https  ,  String   webSiteId  ,  Locale   locale  ,  String   contextRoot  ,  boolean   cache  )  throws   IOException  ,  GeneralException  { 
if  (  dataResource  ==  null  )  { 
throw   new   GeneralException  (  "Cannot stream null data resource!"  )  ; 
} 
String   dataResourceTypeId  =  dataResource  .  getString  (  "dataResourceTypeId"  )  ; 
String   dataResourceId  =  dataResource  .  getString  (  "dataResourceId"  )  ; 
GenericDelegator   delegator  =  dataResource  .  getDelegator  (  )  ; 
if  (  dataResourceTypeId  .  endsWith  (  "_TEXT"  )  ||  "LINK"  .  equals  (  dataResourceTypeId  )  )  { 
String   text  =  ""  ; 
if  (  "SHORT_TEXT"  .  equals  (  dataResourceTypeId  )  ||  "LINK"  .  equals  (  dataResourceTypeId  )  )  { 
text  =  dataResource  .  getString  (  "objectInfo"  )  ; 
}  else   if  (  "ELECTRONIC_TEXT"  .  equals  (  dataResourceTypeId  )  )  { 
GenericValue   electronicText  ; 
if  (  cache  )  { 
electronicText  =  delegator  .  findByPrimaryKeyCache  (  "ElectronicText"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
}  else  { 
electronicText  =  delegator  .  findByPrimaryKey  (  "ElectronicText"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
} 
if  (  electronicText  !=  null  )  { 
text  =  electronicText  .  getString  (  "textData"  )  ; 
} 
}  else  { 
throw   new   GeneralException  (  "Unsupported TEXT type; cannot stream"  )  ; 
} 
byte  [  ]  bytes  =  text  .  getBytes  (  )  ; 
return   UtilMisc  .  toMap  (  "stream"  ,  new   ByteArrayInputStream  (  bytes  )  ,  "length"  ,  new   Integer  (  bytes  .  length  )  )  ; 
}  else   if  (  dataResourceTypeId  .  endsWith  (  "_OBJECT"  )  )  { 
byte  [  ]  bytes  =  new   byte  [  0  ]  ; 
GenericValue   valObj  ; 
if  (  "IMAGE_OBJECT"  .  equals  (  dataResourceTypeId  )  )  { 
if  (  cache  )  { 
valObj  =  delegator  .  findByPrimaryKeyCache  (  "ImageDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
}  else  { 
valObj  =  delegator  .  findByPrimaryKey  (  "ImageDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
} 
if  (  valObj  !=  null  )  { 
bytes  =  valObj  .  getBytes  (  "imageData"  )  ; 
} 
}  else   if  (  "VIDEO_OBJECT"  .  equals  (  dataResourceTypeId  )  )  { 
if  (  cache  )  { 
valObj  =  delegator  .  findByPrimaryKeyCache  (  "VideoDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
}  else  { 
valObj  =  delegator  .  findByPrimaryKey  (  "VideoDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
} 
if  (  valObj  !=  null  )  { 
bytes  =  valObj  .  getBytes  (  "videoData"  )  ; 
} 
}  else   if  (  "AUDIO_OBJECT"  .  equals  (  dataResourceTypeId  )  )  { 
if  (  cache  )  { 
valObj  =  delegator  .  findByPrimaryKeyCache  (  "AudioDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
}  else  { 
valObj  =  delegator  .  findByPrimaryKey  (  "AudioDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
} 
if  (  valObj  !=  null  )  { 
bytes  =  valObj  .  getBytes  (  "audioData"  )  ; 
} 
}  else   if  (  "OTHER_OBJECT"  .  equals  (  dataResourceTypeId  )  )  { 
if  (  cache  )  { 
valObj  =  delegator  .  findByPrimaryKeyCache  (  "OtherDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
}  else  { 
valObj  =  delegator  .  findByPrimaryKey  (  "OtherDataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
} 
if  (  valObj  !=  null  )  { 
bytes  =  valObj  .  getBytes  (  "dataResourceContent"  )  ; 
} 
}  else  { 
throw   new   GeneralException  (  "Unsupported OBJECT type ["  +  dataResourceTypeId  +  "]; cannot stream"  )  ; 
} 
return   UtilMisc  .  toMap  (  "stream"  ,  new   ByteArrayInputStream  (  bytes  )  ,  "length"  ,  new   Long  (  bytes  .  length  )  )  ; 
}  else   if  (  dataResourceTypeId  .  endsWith  (  "_FILE"  )  ||  dataResourceTypeId  .  endsWith  (  "_FILE_BIN"  )  )  { 
String   objectInfo  =  dataResource  .  getString  (  "objectInfo"  )  ; 
if  (  UtilValidate  .  isNotEmpty  (  objectInfo  )  )  { 
File   file  =  DataResourceWorker  .  getContentFile  (  dataResourceTypeId  ,  objectInfo  ,  contextRoot  )  ; 
return   UtilMisc  .  toMap  (  "stream"  ,  new   FileInputStream  (  file  )  ,  "length"  ,  new   Long  (  file  .  length  (  )  )  )  ; 
}  else  { 
throw   new   GeneralException  (  "No objectInfo found for FILE type ["  +  dataResourceTypeId  +  "]; cannot stream"  )  ; 
} 
}  else   if  (  "URL_RESOURCE"  .  equals  (  dataResourceTypeId  )  )  { 
String   objectInfo  =  dataResource  .  getString  (  "objectInfo"  )  ; 
if  (  UtilValidate  .  isNotEmpty  (  objectInfo  )  )  { 
URL   url  =  new   URL  (  objectInfo  )  ; 
if  (  url  .  getHost  (  )  ==  null  )  { 
String   newUrl  =  DataResourceWorker  .  buildRequestPrefix  (  delegator  ,  locale  ,  webSiteId  ,  https  )  ; 
if  (  !  newUrl  .  endsWith  (  "/"  )  )  { 
newUrl  =  newUrl  +  "/"  ; 
} 
newUrl  =  newUrl  +  url  .  toString  (  )  ; 
url  =  new   URL  (  newUrl  )  ; 
} 
URLConnection   con  =  url  .  openConnection  (  )  ; 
return   UtilMisc  .  toMap  (  "stream"  ,  con  .  getInputStream  (  )  ,  "length"  ,  new   Long  (  con  .  getContentLength  (  )  )  )  ; 
}  else  { 
throw   new   GeneralException  (  "No objectInfo found for URL_RESOURCE type; cannot stream"  )  ; 
} 
} 
throw   new   GeneralException  (  "The dataResourceTypeId ["  +  dataResourceTypeId  +  "] is not supported in getDataResourceStream"  )  ; 
} 

public   static   void   streamDataResource  (  OutputStream   os  ,  GenericDelegator   delegator  ,  String   dataResourceId  ,  String   https  ,  String   webSiteId  ,  Locale   locale  ,  String   rootDir  )  throws   IOException  ,  GeneralException  { 
try  { 
GenericValue   dataResource  =  delegator  .  findByPrimaryKeyCache  (  "DataResource"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
if  (  dataResource  ==  null  )  { 
throw   new   GeneralException  (  "Error in streamDataResource: DataResource with ID ["  +  dataResourceId  +  "] was not found."  )  ; 
} 
String   dataResourceTypeId  =  dataResource  .  getString  (  "dataResourceTypeId"  )  ; 
if  (  UtilValidate  .  isEmpty  (  dataResourceTypeId  )  )  { 
dataResourceTypeId  =  "SHORT_TEXT"  ; 
} 
String   mimeTypeId  =  dataResource  .  getString  (  "mimeTypeId"  )  ; 
if  (  UtilValidate  .  isEmpty  (  mimeTypeId  )  )  { 
mimeTypeId  =  "text/html"  ; 
} 
if  (  dataResourceTypeId  .  equals  (  "SHORT_TEXT"  )  )  { 
String   text  =  dataResource  .  getString  (  "objectInfo"  )  ; 
os  .  write  (  text  .  getBytes  (  )  )  ; 
}  else   if  (  dataResourceTypeId  .  equals  (  "ELECTRONIC_TEXT"  )  )  { 
GenericValue   electronicText  =  delegator  .  findByPrimaryKeyCache  (  "ElectronicText"  ,  UtilMisc  .  toMap  (  "dataResourceId"  ,  dataResourceId  )  )  ; 
if  (  electronicText  !=  null  )  { 
String   text  =  electronicText  .  getString  (  "textData"  )  ; 
if  (  text  !=  null  )  os  .  write  (  text  .  getBytes  (  )  )  ; 
} 
}  else   if  (  dataResourceTypeId  .  equals  (  "IMAGE_OBJECT"  )  )  { 
byte  [  ]  imageBytes  =  acquireImage  (  delegator  ,  dataResource  )  ; 
if  (  imageBytes  !=  null  )  os  .  write  (  imageBytes  )  ; 
}  else   if  (  dataResourceTypeId  .  equals  (  "LINK"  )  )  { 
String   text  =  dataResource  .  getString  (  "objectInfo"  )  ; 
os  .  write  (  text  .  getBytes  (  )  )  ; 
}  else   if  (  dataResourceTypeId  .  equals  (  "URL_RESOURCE"  )  )  { 
URL   url  =  new   URL  (  dataResource  .  getString  (  "objectInfo"  )  )  ; 
if  (  url  .  getHost  (  )  ==  null  )  { 
String   prefix  =  buildRequestPrefix  (  delegator  ,  locale  ,  webSiteId  ,  https  )  ; 
String   sep  =  ""  ; 
if  (  url  .  toString  (  )  .  indexOf  (  "/"  )  !=  0  &&  prefix  .  lastIndexOf  (  "/"  )  !=  (  prefix  .  length  (  )  -  1  )  )  { 
sep  =  "/"  ; 
} 
String   s2  =  prefix  +  sep  +  url  .  toString  (  )  ; 
url  =  new   URL  (  s2  )  ; 
} 
InputStream   in  =  url  .  openStream  (  )  ; 
int   c  ; 
while  (  (  c  =  in  .  read  (  )  )  !=  -  1  )  { 
os  .  write  (  c  )  ; 
} 
}  else   if  (  dataResourceTypeId  .  indexOf  (  "_FILE"  )  >=  0  )  { 
String   objectInfo  =  dataResource  .  getString  (  "objectInfo"  )  ; 
File   inputFile  =  getContentFile  (  dataResourceTypeId  ,  objectInfo  ,  rootDir  )  ; 
FileInputStream   fis  =  new   FileInputStream  (  inputFile  )  ; 
int   c  ; 
while  (  (  c  =  fis  .  read  (  )  )  !=  -  1  )  { 
os  .  write  (  c  )  ; 
} 
}  else  { 
throw   new   GeneralException  (  "The dataResourceTypeId ["  +  dataResourceTypeId  +  "] is not supported in streamDataResource"  )  ; 
} 
}  catch  (  GenericEntityException   e  )  { 
throw   new   GeneralException  (  "Error in streamDataResource"  ,  e  )  ; 
} 
} 

public   static   ByteWrapper   getContentAsByteWrapper  (  GenericDelegator   delegator  ,  String   dataResourceId  ,  String   https  ,  String   webSiteId  ,  Locale   locale  ,  String   rootDir  )  throws   IOException  ,  GeneralException  { 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
streamDataResource  (  baos  ,  delegator  ,  dataResourceId  ,  https  ,  webSiteId  ,  locale  ,  rootDir  )  ; 
ByteWrapper   byteWrapper  =  new   ByteWrapper  (  baos  .  toByteArray  (  )  )  ; 
return   byteWrapper  ; 
} 
} 

