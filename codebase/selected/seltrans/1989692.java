package   net  .  sourceforge  .  cruisecontrol  .  publishers  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  Serializable  ; 
import   java  .  io  .  UnsupportedEncodingException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  TreeSet  ; 
import   javax  .  mail  .  Message  ; 
import   javax  .  mail  .  MessagingException  ; 
import   javax  .  mail  .  SendFailedException  ; 
import   javax  .  mail  .  Session  ; 
import   javax  .  mail  .  Transport  ; 
import   javax  .  mail  .  internet  .  AddressException  ; 
import   javax  .  mail  .  internet  .  InternetAddress  ; 
import   javax  .  mail  .  internet  .  MimeMessage  ; 
import   net  .  sourceforge  .  cruisecontrol  .  CruiseControlException  ; 
import   net  .  sourceforge  .  cruisecontrol  .  Modification  ; 
import   net  .  sourceforge  .  cruisecontrol  .  Publisher  ; 
import   net  .  sourceforge  .  cruisecontrol  .  publishers  .  email  .  EmailMapper  ; 
import   net  .  sourceforge  .  cruisecontrol  .  publishers  .  email  .  EmailMapperHelper  ; 
import   net  .  sourceforge  .  cruisecontrol  .  publishers  .  email  .  EmailMapping  ; 
import   net  .  sourceforge  .  cruisecontrol  .  util  .  ValidationHelper  ; 
import   net  .  sourceforge  .  cruisecontrol  .  util  .  XMLLogHelper  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  apache  .  oro  .  io  .  GlobFilenameFilter  ; 
import   org  .  apache  .  oro  .  text  .  MalformedCachePatternException  ; 
import   org  .  apache  .  commons  .  validator  .  EmailValidator  ; 
import   org  .  jdom  .  Element  ; 









public   abstract   class   EmailPublisher   implements   Publisher  { 

private   static   final   Logger   LOG  =  Logger  .  getLogger  (  EmailPublisher  .  class  )  ; 

private   String   mailHost  ; 

private   String   userName  ; 

private   String   password  ; 

private   String   mailPort  ; 

private   boolean   useSSL  ; 

private   String   buildResultsURL  ; 

private   Always  [  ]  alwaysAddresses  =  new   Always  [  0  ]  ; 

private   Failure  [  ]  failureAddresses  =  new   Failure  [  0  ]  ; 

private   Success  [  ]  successAddresses  =  new   Success  [  0  ]  ; 

private   Alert  [  ]  alertAddresses  =  new   Alert  [  0  ]  ; 

private   Ignore  [  ]  ignoreUsers  =  new   Ignore  [  0  ]  ; 

private   EmailMapper  [  ]  emailMapper  =  new   EmailMapper  [  0  ]  ; 

private   EmailMapperHelper   mapperHelper  =  new   EmailMapperHelper  (  )  ; 

private   String   returnAddress  ; 

private   String   returnName  ; 

private   String   defaultSuffix  =  ""  ; 

private   String   reportSuccess  =  "always"  ; 

private   boolean   spamWhileBroken  =  true  ; 

private   boolean   skipUsers  =  false  ; 

private   String   subjectPrefix  ; 

private   boolean   failAsImportant  =  true  ; 








protected   abstract   String   createMessage  (  XMLLogHelper   logHelper  )  ; 

public   void   validate  (  )  throws   CruiseControlException  { 
ValidationHelper  .  assertIsSet  (  getMailHost  (  )  ,  "mailhost"  ,  this  .  getClass  (  )  )  ; 
ValidationHelper  .  assertIsSet  (  getReturnAddress  (  )  ,  "returnaddress"  ,  this  .  getClass  (  )  )  ; 
ValidationHelper  .  assertFalse  (  getUsername  (  )  !=  null  &&  getPassword  (  )  ==  null  ,  "'password' is required if 'username' is set for email."  )  ; 
ValidationHelper  .  assertFalse  (  getPassword  (  )  !=  null  &&  getUsername  (  )  ==  null  ,  "'username' is required if 'password' is set for email."  )  ; 
validateAddresses  (  alwaysAddresses  )  ; 
validateAddresses  (  alertAddresses  )  ; 
validateAddresses  (  failureAddresses  )  ; 
validateAddresses  (  successAddresses  )  ; 
for  (  int   i  =  0  ;  i  <  ignoreUsers  .  length  ;  i  ++  )  { 
ignoreUsers  [  i  ]  .  validate  (  )  ; 
} 
} 

private   void   validateAddresses  (  Address  [  ]  addresses  )  throws   CruiseControlException  { 
for  (  int   i  =  0  ;  i  <  addresses  .  length  ;  i  ++  )  { 
addresses  [  i  ]  .  validate  (  )  ; 
} 
} 








protected   String   createSubject  (  XMLLogHelper   logHelper  )  throws   CruiseControlException  { 
StringBuffer   subjectLine  =  new   StringBuffer  (  )  ; 
if  (  subjectPrefix  !=  null  )  { 
subjectLine  .  append  (  subjectPrefix  )  .  append  (  " "  )  ; 
} 
subjectLine  .  append  (  logHelper  .  getProjectName  (  )  )  ; 
if  (  logHelper  .  isBuildSuccessful  (  )  )  { 
String   label  =  logHelper  .  getLabel  (  )  ; 
if  (  label  .  trim  (  )  .  length  (  )  >  0  )  { 
subjectLine  .  append  (  " "  )  .  append  (  logHelper  .  getLabel  (  )  )  ; 
} 
if  (  logHelper  .  isBuildFix  (  )  )  { 
subjectLine  .  append  (  " Build Fixed"  )  ; 
}  else  { 
subjectLine  .  append  (  " Build Successful"  )  ; 
} 
}  else  { 
subjectLine  .  append  (  " Build Failed"  )  ; 
} 
return   subjectLine  .  toString  (  )  ; 
} 








protected   boolean   shouldSend  (  XMLLogHelper   logHelper  )  throws   CruiseControlException  { 
if  (  logHelper  .  isBuildSuccessful  (  )  )  { 
if  (  reportSuccess  .  equalsIgnoreCase  (  "always"  )  )  { 
return   true  ; 
} 
if  (  reportSuccess  .  equalsIgnoreCase  (  "fixes"  )  )  { 
if  (  logHelper  .  wasPreviousBuildSuccessful  (  )  )  { 
LOG  .  debug  (  "reportSuccess is set to 'fixes', not sending emails for repeated successful builds."  )  ; 
return   false  ; 
}  else  { 
return   true  ; 
} 
} 
if  (  reportSuccess  .  equalsIgnoreCase  (  "never"  )  )  { 
LOG  .  debug  (  "reportSuccess is set to 'never', not sending emails for successful builds."  )  ; 
return   false  ; 
} 
}  else  { 
if  (  !  logHelper  .  wasPreviousBuildSuccessful  (  )  &&  logHelper  .  isBuildNecessary  (  )  &&  !  spamWhileBroken  )  { 
LOG  .  debug  (  "spamWhileBroken is set to false, not sending email"  )  ; 
return   false  ; 
} 
} 
return   true  ; 
} 













protected   String   createUserList  (  XMLLogHelper   logHelper  )  throws   CruiseControlException  { 
Set   emails  =  createUserSet  (  logHelper  )  ; 
return   createEmailString  (  emails  )  ; 
} 















protected   Set   createUserSet  (  XMLLogHelper   logHelper  )  throws   CruiseControlException  { 
Set   users  =  skipUsers  ?  new   HashSet  (  )  :  logHelper  .  getBuildParticipants  (  )  ; 
for  (  int   i  =  0  ;  i  <  ignoreUsers  .  length  ;  i  ++  )  { 
users  .  remove  (  ignoreUsers  [  i  ]  .  getUser  (  )  )  ; 
} 
for  (  int   i  =  0  ;  i  <  alwaysAddresses  .  length  ;  i  ++  )  { 
users  .  add  (  alwaysAddresses  [  i  ]  .  getAddress  (  )  )  ; 
} 
if  (  !  logHelper  .  isBuildSuccessful  (  )  )  { 
for  (  int   i  =  0  ;  i  <  failureAddresses  .  length  ;  i  ++  )  { 
users  .  add  (  failureAddresses  [  i  ]  .  getAddress  (  )  )  ; 
} 
} 
if  (  logHelper  .  isBuildFix  (  )  )  { 
for  (  int   i  =  0  ;  i  <  failureAddresses  .  length  ;  i  ++  )  { 
if  (  failureAddresses  [  i  ]  .  shouldReportWhenFixed  (  )  )  { 
users  .  add  (  failureAddresses  [  i  ]  .  getAddress  (  )  )  ; 
} 
} 
} 
if  (  logHelper  .  isBuildSuccessful  (  )  )  { 
for  (  int   i  =  0  ;  i  <  successAddresses  .  length  ;  i  ++  )  { 
users  .  add  (  successAddresses  [  i  ]  .  getAddress  (  )  )  ; 
} 
} 
Set   emails  =  new   TreeSet  (  )  ; 
mapperHelper  .  mapUsers  (  this  ,  users  ,  emails  )  ; 
for  (  Iterator   iterator  =  emails  .  iterator  (  )  ;  iterator  .  hasNext  (  )  ;  )  { 
String   email  =  (  String  )  iterator  .  next  (  )  ; 
if  (  !  isValid  (  email  )  )  { 
LOG  .  warn  (  "Invalid email: "  +  email  )  ; 
} 
} 
return   emails  ; 
} 

boolean   isValid  (  String   address  )  { 
return   EmailValidator  .  getInstance  (  )  .  isValid  (  address  )  ; 
} 






public   void   publish  (  Element   cruisecontrolLog  )  throws   CruiseControlException  { 
XMLLogHelper   helper  =  new   XMLLogHelper  (  cruisecontrolLog  )  ; 
boolean   important  =  failAsImportant  &&  !  helper  .  isBuildSuccessful  (  )  ; 
Set   userSet  =  new   HashSet  (  )  ; 
Set   alertSet  =  createAlertUserSet  (  helper  )  ; 
String   subject  =  createSubject  (  helper  )  ; 
if  (  !  alertSet  .  isEmpty  (  )  )  { 
String   alertSubject  =  "[MOD ALERT] "  +  subject  ; 
sendMail  (  createEmailString  (  alertSet  )  ,  alertSubject  ,  createMessage  (  helper  )  ,  important  )  ; 
} 
if  (  shouldSend  (  helper  )  )  { 
userSet  .  addAll  (  createUserSet  (  helper  )  )  ; 
userSet  .  removeAll  (  alertSet  )  ; 
if  (  !  userSet  .  isEmpty  (  )  )  { 
sendMail  (  createEmailString  (  userSet  )  ,  subject  ,  createMessage  (  helper  )  ,  important  )  ; 
}  else  { 
if  (  alertSet  .  isEmpty  (  )  )  { 
LOG  .  info  (  "No recipients, so not sending email"  )  ; 
} 
} 
} 
} 






protected   Properties   getMailProperties  (  )  { 
Properties   props  =  System  .  getProperties  (  )  ; 
props  .  put  (  "mail.smtp.host"  ,  mailHost  )  ; 
props  .  put  (  "mail.smtp.sendpartial"  ,  "true"  )  ; 
if  (  mailPort  !=  null  )  { 
props  .  put  (  "mail.smtp.port"  ,  mailPort  )  ; 
} 
LOG  .  debug  (  "mailHost is "  +  mailHost  +  ", mailPort is "  +  mailPort  ==  null  ?  "default"  :  mailPort  )  ; 
if  (  userName  !=  null  &&  password  !=  null  )  { 
props  .  put  (  "mail.smtp.auth"  ,  "true"  )  ; 
if  (  useSSL  )  { 
if  (  mailPort  !=  null  )  { 
props  .  put  (  "mail.smtp.socketFactory.port"  ,  mailPort  )  ; 
} 
props  .  put  (  "mail.smtp.socketFactory.class"  ,  "javax.net.ssl.SSLSocketFactory"  )  ; 
props  .  put  (  "mail.smtp.socketFactory.fallback"  ,  "false"  )  ; 
} 
} 
return   props  ; 
} 












protected   boolean   sendMail  (  String   toList  ,  String   subject  ,  String   message  ,  boolean   important  )  throws   CruiseControlException  { 
boolean   emailSent  =  false  ; 
if  (  toList  !=  null  &&  toList  .  trim  (  )  .  length  (  )  !=  0  )  { 
LOG  .  debug  (  "Sending email to: "  +  toList  )  ; 
Session   session  =  Session  .  getDefaultInstance  (  getMailProperties  (  )  ,  null  )  ; 
session  .  setDebug  (  LOG  .  isDebugEnabled  (  )  )  ; 
try  { 
Message   msg  =  new   MimeMessage  (  session  )  ; 
msg  .  setFrom  (  getFromAddress  (  )  )  ; 
msg  .  setRecipients  (  Message  .  RecipientType  .  TO  ,  InternetAddress  .  parse  (  toList  ,  false  )  )  ; 
msg  .  setSubject  (  subject  )  ; 
msg  .  setSentDate  (  new   Date  (  )  )  ; 
String   importance  =  (  important  )  ?  "High"  :  "Normal"  ; 
msg  .  addHeader  (  "Importance"  ,  importance  )  ; 
addContentToMessage  (  message  ,  msg  )  ; 
if  (  userName  !=  null  &&  password  !=  null  )  { 
msg  .  saveChanges  (  )  ; 
Transport   transport  =  session  .  getTransport  (  "smtp"  )  ; 
transport  .  connect  (  mailHost  ,  userName  ,  password  )  ; 
transport  .  sendMessage  (  msg  ,  msg  .  getAllRecipients  (  )  )  ; 
transport  .  close  (  )  ; 
}  else  { 
Transport  .  send  (  msg  )  ; 
} 
emailSent  =  true  ; 
}  catch  (  SendFailedException   e  )  { 
LOG  .  warn  (  e  .  getMessage  (  )  ,  e  )  ; 
}  catch  (  MessagingException   e  )  { 
throw   new   CruiseControlException  (  e  .  getClass  (  )  .  getName  (  )  +  ": "  +  e  .  getMessage  (  )  ,  e  )  ; 
} 
} 
return   emailSent  ; 
} 










protected   void   addContentToMessage  (  String   content  ,  Message   msg  )  throws   MessagingException  { 
msg  .  setText  (  content  )  ; 
} 

protected   InternetAddress   getFromAddress  (  )  throws   AddressException  { 
InternetAddress   fromAddress  =  new   InternetAddress  (  returnAddress  )  ; 
if  (  returnName  !=  null  )  { 
try  { 
fromAddress  =  new   InternetAddress  (  returnAddress  ,  returnName  )  ; 
}  catch  (  UnsupportedEncodingException   e  )  { 
LOG  .  error  (  "error setting returnName ["  +  returnName  +  "]: "  +  e  .  getMessage  (  )  )  ; 
} 
} 
return   fromAddress  ; 
} 

public   void   setMailHost  (  String   hostname  )  { 
mailHost  =  hostname  ; 
} 

public   String   getMailHost  (  )  { 
return   mailHost  ; 
} 

public   void   setUsername  (  String   name  )  { 
userName  =  name  ; 
} 

public   String   getUsername  (  )  { 
return   userName  ; 
} 

public   void   setPassword  (  String   passwd  )  { 
password  =  passwd  ; 
} 

public   String   getPassword  (  )  { 
return   password  ; 
} 

public   void   setMailPort  (  String   port  )  { 
mailPort  =  port  ; 
} 

public   String   getMailPort  (  )  { 
return   mailPort  ; 
} 

public   void   setUseSSL  (  boolean   useSSL  )  { 
this  .  useSSL  =  useSSL  ; 
} 

public   void   setSubjectPrefix  (  String   prefix  )  { 
subjectPrefix  =  prefix  ; 
} 

public   String   getSubjectPrefix  (  )  { 
return   subjectPrefix  ; 
} 

public   String   getBuildResultsURL  (  )  { 
return   buildResultsURL  ; 
} 

public   void   setBuildResultsURL  (  String   url  )  { 
buildResultsURL  =  url  ; 
} 

public   EmailMapper  [  ]  getEmailMapper  (  )  { 
return   emailMapper  ; 
} 

public   String   getReturnAddress  (  )  { 
return   returnAddress  ; 
} 

public   void   setReturnAddress  (  String   emailAddress  )  { 
returnAddress  =  emailAddress  ; 
} 

public   String   getReturnName  (  )  { 
return   returnName  ; 
} 

public   void   setReturnName  (  String   emailReturnName  )  { 
returnName  =  emailReturnName  ; 
} 

public   String   getDefaultSuffix  (  )  { 
return   defaultSuffix  ; 
} 

public   void   setDefaultSuffix  (  String   defaultEmailSuffix  )  { 
defaultSuffix  =  defaultEmailSuffix  ; 
} 

public   void   setReportSuccess  (  String   report  )  { 
reportSuccess  =  report  ; 
} 

public   void   setSkipUsers  (  boolean   skip  )  { 
skipUsers  =  skip  ; 
} 

public   void   setSpamWhileBroken  (  boolean   spam  )  { 
spamWhileBroken  =  spam  ; 
} 

public   void   setFailAsImportant  (  boolean   important  )  { 
failAsImportant  =  important  ; 
} 

public   Ignore   createIgnore  (  )  { 
List   ignoreList  =  new   ArrayList  (  )  ; 
ignoreList  .  addAll  (  Arrays  .  asList  (  ignoreUsers  )  )  ; 
Ignore   ignore  =  new   Ignore  (  )  ; 
ignoreList  .  add  (  ignore  )  ; 
ignoreUsers  =  (  Ignore  [  ]  )  ignoreList  .  toArray  (  new   Ignore  [  0  ]  )  ; 
return   ignore  ; 
} 

public   Always   createAlways  (  )  { 
List   alwaysList  =  new   ArrayList  (  )  ; 
alwaysList  .  addAll  (  Arrays  .  asList  (  alwaysAddresses  )  )  ; 
Always   always  =  new   Always  (  )  ; 
alwaysList  .  add  (  always  )  ; 
alwaysAddresses  =  (  Always  [  ]  )  alwaysList  .  toArray  (  new   Always  [  0  ]  )  ; 
return   always  ; 
} 

public   Failure   createFailure  (  )  { 
List   failureList  =  new   ArrayList  (  )  ; 
failureList  .  addAll  (  Arrays  .  asList  (  failureAddresses  )  )  ; 
Failure   failure  =  new   Failure  (  )  ; 
failureList  .  add  (  failure  )  ; 
failureAddresses  =  (  Failure  [  ]  )  failureList  .  toArray  (  new   Failure  [  0  ]  )  ; 
return   failure  ; 
} 

public   Success   createSuccess  (  )  { 
List   successList  =  new   ArrayList  (  )  ; 
successList  .  addAll  (  Arrays  .  asList  (  successAddresses  )  )  ; 
Success   success  =  new   Success  (  )  ; 
successList  .  add  (  success  )  ; 
successAddresses  =  (  Success  [  ]  )  successList  .  toArray  (  new   Success  [  0  ]  )  ; 
return   success  ; 
} 

public   Alert   createAlert  (  )  { 
List   alertsList  =  new   ArrayList  (  )  ; 
alertsList  .  addAll  (  Arrays  .  asList  (  alertAddresses  )  )  ; 
Alert   alert  =  new   Alert  (  )  ; 
alertsList  .  add  (  alert  )  ; 
alertAddresses  =  (  Alert  [  ]  )  alertsList  .  toArray  (  new   Alert  [  0  ]  )  ; 
return   alert  ; 
} 

public   void   add  (  EmailMapping   mapping  )  { 
EmailMapperHelper  .  addCacheEntry  (  this  ,  mapping  .  getAlias  (  )  ,  mapping  .  getAddress  (  )  )  ; 
} 

public   void   add  (  EmailMapper   mapper  )  { 
List   mapperList  =  new   ArrayList  (  )  ; 
mapperList  .  addAll  (  Arrays  .  asList  (  emailMapper  )  )  ; 
mapper  .  setPublisher  (  this  )  ; 
mapperList  .  add  (  mapper  )  ; 
emailMapper  =  (  EmailMapper  [  ]  )  mapperList  .  toArray  (  new   EmailMapper  [  0  ]  )  ; 
} 

public   static   class   Ignore   implements   Serializable  { 

private   static   final   long   serialVersionUID  =  4763763343885476189L  ; 

private   String   user  ; 

public   String   getUser  (  )  { 
return   user  ; 
} 

public   void   validate  (  )  throws   CruiseControlException  { 
ValidationHelper  .  assertIsSet  (  user  ,  "user"  ,  getClass  (  )  )  ; 
} 

public   void   setUser  (  String   theUser  )  { 
user  =  theUser  ; 
} 
} 

public   static   class   Address   implements   Serializable  { 

private   static   final   long   serialVersionUID  =  -  7143350548506511400L  ; 

private   String   address  ; 

public   String   getAddress  (  )  { 
return   address  ; 
} 

public   void   setAddress  (  String   theAddress  )  { 
address  =  theAddress  ; 
} 

public   void   validate  (  )  throws   CruiseControlException  { 
ValidationHelper  .  assertIsSet  (  address  ,  "address"  ,  getClass  (  )  )  ; 
ValidationHelper  .  assertFalse  (  address  .  equals  (  ""  )  ,  "empty string is not a valid value of address for "  +  getClass  (  )  .  getName  (  )  )  ; 
} 
} 

public   static   class   Always   extends   Address  { 

private   static   final   long   serialVersionUID  =  -  4906620821397147420L  ; 
} 

public   static   class   Failure   extends   Address  { 

private   static   final   long   serialVersionUID  =  -  6027290532428946586L  ; 




private   boolean   reportWhenFixed  =  false  ; 

public   boolean   shouldReportWhenFixed  (  )  { 
return   reportWhenFixed  ; 
} 

public   void   setReportWhenFixed  (  boolean   reportWhenFixed  )  { 
this  .  reportWhenFixed  =  reportWhenFixed  ; 
} 
} 

public   static   class   Success   extends   Address  { 

private   static   final   long   serialVersionUID  =  8001068483405344504L  ; 
} 

public   static   class   Alert   extends   Address  { 

private   static   final   long   serialVersionUID  =  3644326033589789766L  ; 

public   void   validate  (  )  throws   CruiseControlException  { 
super  .  validate  (  )  ; 
ValidationHelper  .  assertIsSet  (  fileRegExpr  ,  "fileregexpr"  ,  getClass  (  )  )  ; 
try  { 
fileFilter  =  new   GlobFilenameFilter  (  fileRegExpr  )  ; 
}  catch  (  MalformedCachePatternException   mcpe  )  { 
ValidationHelper  .  fail  (  "invalid regexp '"  +  fileRegExpr  +  "'"  ,  mcpe  )  ; 
} 
} 




private   String   fileRegExpr  =  null  ; 




private   GlobFilenameFilter   fileFilter  =  null  ; 







public   void   setFileRegExpr  (  String   f  )  { 
this  .  fileRegExpr  =  f  ; 
} 
} 
















protected   String   createAlertUserList  (  XMLLogHelper   logHelper  )  throws   CruiseControlException  { 
return   createEmailString  (  createAlertUserSet  (  logHelper  )  )  ; 
} 









protected   Set   createAlertUserSet  (  XMLLogHelper   logHelper  )  throws   CruiseControlException  { 
if  (  alertAddresses  .  length  ==  0  )  { 
return   Collections  .  EMPTY_SET  ; 
} 
Set   users  =  new   HashSet  (  )  ; 
Set   modificationSet  =  logHelper  .  getModifications  (  )  ; 
for  (  Iterator   modificationIter  =  modificationSet  .  iterator  (  )  ;  modificationIter  .  hasNext  (  )  ;  )  { 
Modification   mod  =  (  Modification  )  modificationIter  .  next  (  )  ; 
String   modifiedFile  =  mod  .  getFullPath  (  )  ; 
LOG  .  debug  (  "Modified file: "  +  modifiedFile  )  ; 
for  (  int   i  =  0  ;  i  <  alertAddresses  .  length  ;  i  ++  )  { 
String   emailAddress  =  alertAddresses  [  i  ]  .  getAddress  (  )  ; 
if  (  emailAddress  !=  null  &&  !  ""  .  equals  (  emailAddress  .  trim  (  )  )  &&  !  users  .  contains  (  emailAddress  )  &&  matchRegExpr  (  modifiedFile  ,  alertAddresses  [  i  ]  .  fileFilter  )  )  { 
users  .  add  (  emailAddress  )  ; 
LOG  .  info  (  "Alert '"  +  emailAddress  +  "' because their fileRegExpr '"  +  alertAddresses  [  i  ]  .  fileRegExpr  +  "' matched "  +  modifiedFile  )  ; 
} 
} 
} 
Set   emails  =  new   TreeSet  (  )  ; 
mapperHelper  .  mapUsers  (  this  ,  users  ,  emails  )  ; 
return   emails  ; 
} 








protected   String   createEmailString  (  Set   emails  )  { 
StringBuffer   commaDelimitedString  =  new   StringBuffer  (  )  ; 
Iterator   emailIterator  =  appendDefaultSuffix  (  emails  )  .  iterator  (  )  ; 
while  (  emailIterator  .  hasNext  (  )  )  { 
String   mappedUser  =  (  String  )  emailIterator  .  next  (  )  ; 
commaDelimitedString  .  append  (  mappedUser  )  ; 
if  (  emailIterator  .  hasNext  (  )  )  { 
commaDelimitedString  .  append  (  ","  )  ; 
} 
} 
LOG  .  debug  (  "List of emails: "  +  commaDelimitedString  )  ; 
return   commaDelimitedString  .  toString  (  )  ; 
} 

private   Set   appendDefaultSuffix  (  Set   emails  )  { 
Set   result  =  new   TreeSet  (  )  ; 
Iterator   emailIterator  =  emails  .  iterator  (  )  ; 
while  (  emailIterator  .  hasNext  (  )  )  { 
String   mappedUser  =  (  String  )  emailIterator  .  next  (  )  ; 
if  (  mappedUser  .  indexOf  (  "@"  )  <  0  )  { 
mappedUser  +=  defaultSuffix  ; 
} 
result  .  add  (  mappedUser  )  ; 
} 
return   result  ; 
} 










protected   boolean   matchRegExpr  (  String   input  ,  GlobFilenameFilter   pattern  )  { 
File   file  =  new   File  (  input  )  ; 
String   path  =  file  .  toString  (  )  ; 
if  (  File  .  separatorChar  ==  '\\'  )  { 
path  =  path  .  replace  (  '\\'  ,  '/'  )  ; 
} 
return   pattern  .  accept  (  file  ,  path  )  ; 
} 
} 

