package   alto  .  sec  .  x509  ; 

import   alto  .  io  .  Code  ; 
import   alto  .  io  .  Check  ; 
import   alto  .  sec  .  util  .  DerEncoder  ; 
import   alto  .  sec  .  util  .  DerInputStream  ; 
import   alto  .  sec  .  util  .  DerOutputStream  ; 
import   alto  .  sec  .  util  .  DerValue  ; 
import   alto  .  sec  .  util  .  ObjectIdentifier  ; 
import   alto  .  io  .  u  .  Hex  ; 
import   alto  .  io  .  u  .  B64  ; 
import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  security  .  PublicKey  ; 
import   java  .  security  .  Principal  ; 
import   java  .  security  .  PrivateKey  ; 
import   java  .  security  .  InvalidKeyException  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  NoSuchProviderException  ; 
import   java  .  security  .  Signature  ; 
import   java  .  security  .  SignatureException  ; 
import   java  .  security  .  cert  .  Certificate  ; 
import   java  .  security  .  cert  .  CertificateException  ; 
import   java  .  security  .  cert  .  CertificateEncodingException  ; 
import   java  .  security  .  cert  .  CertificateExpiredException  ; 
import   java  .  security  .  cert  .  CertificateNotYetValidException  ; 
import   java  .  security  .  cert  .  CertificateParsingException  ; 
import   java  .  util  .  *  ; 
































public   class   X509Certificate   extends   Certificate   implements   DerEncoder  { 

private   static   final   long   serialVersionUID  =  -  3457612960190864406L  ; 

public   static   final   String   CERT_DEC_BEGIN  =  "-----BEGIN CERTIFICATE-----"  ; 

public   static   final   String   CERT_DEC_END  =  "-----END CERTIFICATE-----"  ; 

public   static   final   byte  [  ]  CERT_ENC_BEGIN  =  (  CERT_DEC_BEGIN  +  "\r\n"  )  .  getBytes  (  )  ; 

public   static   final   int   CERT_ENC_BEGIN_LEN  =  CERT_ENC_BEGIN  .  length  ; 

public   static   final   byte  [  ]  CERT_ENC_END  =  (  "\r\n"  +  CERT_DEC_END  +  "\r\n"  )  .  getBytes  (  )  ; 

public   static   final   int   CERT_ENC_END_LEN  =  CERT_ENC_END  .  length  ; 

private   static   final   String   DOT  =  "."  ; 




public   static   final   String   NAME  =  "x509"  ; 

public   static   final   String   INFO  =  X509CertInfo  .  NAME  ; 

public   static   final   String   ALG_ID  =  "algorithm"  ; 

public   static   final   String   SIGNATURE  =  "signature"  ; 

public   static   final   String   SIGNED_CERT  =  "signed_cert"  ; 

public   static   final   String   SUBJECT_DN  =  NAME  +  DOT  +  INFO  +  DOT  +  X509CertInfo  .  SUBJECT  +  DOT  +  CertificateSubjectName  .  DN_NAME  ; 

public   static   final   String   ISSUER_DN  =  NAME  +  DOT  +  INFO  +  DOT  +  X509CertInfo  .  ISSUER  +  DOT  +  CertificateIssuerName  .  DN_NAME  ; 

public   static   final   String   SERIAL_ID  =  NAME  +  DOT  +  INFO  +  DOT  +  X509CertInfo  .  SERIAL_NUMBER  +  DOT  +  CertificateSerialNumber  .  NUMBER  ; 

public   static   final   String   PUBLIC_KEY  =  NAME  +  DOT  +  INFO  +  DOT  +  X509CertInfo  .  KEY  +  DOT  +  CertificateX509Key  .  KEY  ; 

public   static   final   String   VERSION  =  NAME  +  DOT  +  INFO  +  DOT  +  X509CertInfo  .  VERSION  +  DOT  +  CertificateVersion  .  VERSION  ; 

public   static   final   String   SIG_ALG  =  NAME  +  DOT  +  ALG_ID  ; 

public   static   final   String   SIG  =  NAME  +  DOT  +  SIGNATURE  ; 

private   static   final   java  .  io  .  InputStream   Markable  (  java  .  io  .  InputStream   in  )  { 
if  (  in   instanceof   java  .  io  .  ByteArrayInputStream  )  return   in  ;  else   if  (  in   instanceof   java  .  io  .  BufferedInputStream  )  return   in  ;  else   return  (  new   java  .  io  .  BufferedInputStream  (  in  )  )  ; 
} 

private   boolean   readOnly  =  false  ; 

private   byte  [  ]  signedCert  =  null  ; 

protected   X509CertInfo   info  =  null  ; 

protected   AlgorithmId   algId  =  null  ; 

protected   byte  [  ]  signature  =  null  ; 

private   static   final   String   KEY_USAGE_OID  =  "2.5.29.15"  ; 

private   static   final   String   EXTENDED_KEY_USAGE_OID  =  "2.5.29.37"  ; 

private   static   final   String   BASIC_CONSTRAINT_OID  =  "2.5.29.19"  ; 

private   static   final   String   SUBJECT_ALT_NAME_OID  =  "2.5.29.17"  ; 

private   static   final   String   ISSUER_ALT_NAME_OID  =  "2.5.29.18"  ; 

private   static   final   String   AUTH_INFO_ACCESS_OID  =  "1.3.6.1.5.5.7.1.1"  ; 

private   static   final   int   NUM_STANDARD_KEY_USAGE  =  9  ; 

private   Collection  <  List  <  ?  >  >  subjectAlternativeNames  ; 

private   Collection  <  List  <  ?  >  >  issuerAlternativeNames  ; 

private   List  <  String  >  extKeyUsage  ; 

private   Set  <  AccessDescription  >  authInfoAccess  ; 






private   PublicKey   verifiedPublicKey  ; 






private   String   verifiedProvider  ; 






private   boolean   verificationResult  ; 



public   X509Certificate  (  )  { 
super  (  "X.509"  )  ; 
} 













public   X509Certificate  (  byte  [  ]  certData  )  throws   CertificateException  { 
this  (  )  ; 
try  { 
parse  (  new   DerValue  (  certData  )  )  ; 
}  catch  (  IOException   e  )  { 
signedCert  =  null  ; 
CertificateException   ce  =  new   CertificateException  (  "Unable to initialize, "  +  e  )  ; 
ce  .  initCause  (  e  )  ; 
throw   ce  ; 
} 
} 











public   X509Certificate  (  InputStream   in  )  throws   CertificateException  ,  IOException  { 
this  (  )  ; 
this  .  decode  (  in  )  ; 
} 








public   X509Certificate  (  X509CertInfo   certInfo  )  { 
this  (  )  ; 
this  .  info  =  certInfo  ; 
} 









public   X509Certificate  (  DerValue   derVal  )  throws   CertificateException  { 
this  (  )  ; 
try  { 
this  .  parse  (  derVal  )  ; 
}  catch  (  IOException   e  )  { 
this  .  signedCert  =  null  ; 
throw   new   CertificateException  (  "Unable to initialize"  ,  e  )  ; 
} 
} 









private   DerValue   readRFC1421Cert  (  InputStream   in  )  throws   IOException  { 
DerValue   der  =  null  ; 
String   line  =  null  ; 
BufferedReader   certBufferedReader  =  new   BufferedReader  (  new   InputStreamReader  (  in  ,  "ASCII"  )  )  ; 
try  { 
line  =  certBufferedReader  .  readLine  (  )  ; 
}  catch  (  IOException   ioe1  )  { 
throw   new   IOException  (  "Unable to read InputStream: "  +  ioe1  .  getMessage  (  )  )  ; 
} 
if  (  line  .  equals  (  CERT_DEC_BEGIN  )  )  { 
ByteArrayOutputStream   decstream  =  new   ByteArrayOutputStream  (  )  ; 
try  { 
while  (  (  line  =  certBufferedReader  .  readLine  (  )  )  !=  null  )  { 
if  (  line  .  equals  (  CERT_DEC_END  )  )  { 
der  =  new   DerValue  (  decstream  .  toByteArray  (  )  )  ; 
break  ; 
}  else  { 
decstream  .  write  (  B64  .  decode  (  line  )  )  ; 
} 
} 
}  catch  (  IOException   ioe2  )  { 
throw   new   IOException  (  "Unable to read InputStream: "  +  ioe2  .  getMessage  (  )  )  ; 
} 
}  else  { 
throw   new   IOException  (  "InputStream is not RFC1421 encoded DER bytes"  )  ; 
} 
return   der  ; 
} 







public   void   encode  (  OutputStream   out  )  throws   java  .  security  .  KeyException  ,  java  .  io  .  IOException  { 
byte  [  ]  signedCert  =  this  .  signedCert  ; 
if  (  null  ==  signedCert  )  throw   new   java  .  security  .  KeyException  (  "Null certificate to encode"  )  ;  else  { 
out  .  write  (  signedCert  .  clone  (  )  ,  0  ,  signedCert  .  length  )  ; 
} 
} 

public   void   decode  (  InputStream   in  )  throws   CertificateException  ,  IOException  { 
if  (  this  .  readOnly  )  throw   new   CertificateException  (  "Unable to overwrite certificate"  )  ;  else  { 
in  =  Markable  (  in  )  ; 
try  { 
in  .  mark  (  Integer  .  MAX_VALUE  )  ; 
DerValue   der  =  readRFC1421Cert  (  in  )  ; 
this  .  parse  (  der  )  ; 
}  catch  (  IOException   ioe  )  { 
in  .  reset  (  )  ; 
DerValue   der  =  new   DerValue  (  in  )  ; 
this  .  parse  (  der  )  ; 
} 
} 
} 









public   void   derEncode  (  OutputStream   out  )  throws   java  .  io  .  IOException  { 
byte  [  ]  signedCert  =  this  .  signedCert  ; 
if  (  null  ==  signedCert  )  throw   new   IOException  (  "Null certificate to encode"  )  ;  else   out  .  write  (  signedCert  .  clone  (  )  )  ; 
} 








public   void   b64Encode  (  OutputStream   out  )  throws   java  .  io  .  IOException  { 
byte  [  ]  signedCert  =  this  .  signedCert  ; 
if  (  null  ==  signedCert  )  throw   new   IOException  (  "Null certificate to encode"  )  ;  else  { 
out  .  write  (  CERT_ENC_BEGIN  ,  0  ,  CERT_ENC_BEGIN_LEN  )  ; 
B64  .  Encoder   base64  =  new   B64  .  Encoder  (  out  )  ; 
base64  .  write  (  signedCert  ,  0  ,  signedCert  .  length  )  ; 
base64  .  flush  (  )  ; 
out  .  write  (  CERT_ENC_END  ,  0  ,  CERT_ENC_END_LEN  )  ; 
} 
} 









public   byte  [  ]  getEncoded  (  )  throws   CertificateEncodingException  { 
return   getEncodedInternal  (  )  .  clone  (  )  ; 
} 






public   byte  [  ]  getEncodedInternal  (  )  throws   CertificateEncodingException  { 
if  (  signedCert  ==  null  )  { 
throw   new   CertificateEncodingException  (  "Null certificate"  )  ; 
} 
return   signedCert  ; 
} 
















public   void   verify  (  PublicKey   key  )  throws   CertificateException  ,  NoSuchAlgorithmException  ,  InvalidKeyException  ,  NoSuchProviderException  ,  SignatureException  { 
verify  (  key  ,  ""  )  ; 
} 

















public   synchronized   void   verify  (  PublicKey   key  ,  String   sigProvider  )  throws   CertificateException  ,  NoSuchAlgorithmException  ,  InvalidKeyException  ,  NoSuchProviderException  ,  SignatureException  { 
if  (  sigProvider  ==  null  )  { 
sigProvider  =  ""  ; 
} 
if  (  (  verifiedPublicKey  !=  null  )  &&  verifiedPublicKey  .  equals  (  key  )  )  { 
if  (  sigProvider  .  equals  (  verifiedProvider  )  )  { 
if  (  verificationResult  )  { 
return  ; 
}  else  { 
throw   new   SignatureException  (  "Signature does not match."  )  ; 
} 
} 
} 
if  (  signedCert  ==  null  )  { 
throw   new   CertificateEncodingException  (  "Uninitialized certificate"  )  ; 
} 
Signature   sigVerf  =  null  ; 
if  (  sigProvider  .  length  (  )  ==  0  )  { 
sigVerf  =  Signature  .  getInstance  (  algId  .  getName  (  )  )  ; 
}  else  { 
sigVerf  =  Signature  .  getInstance  (  algId  .  getName  (  )  ,  sigProvider  )  ; 
} 
sigVerf  .  initVerify  (  key  )  ; 
byte  [  ]  rawCert  =  info  .  getEncodedInfo  (  )  ; 
sigVerf  .  update  (  rawCert  ,  0  ,  rawCert  .  length  )  ; 
verificationResult  =  sigVerf  .  verify  (  signature  )  ; 
verifiedPublicKey  =  key  ; 
verifiedProvider  =  sigProvider  ; 
if  (  verificationResult  ==  false  )  { 
throw   new   SignatureException  (  "Signature does not match."  )  ; 
} 
} 

















public   void   sign  (  PrivateKey   key  ,  String   algorithm  )  throws   CertificateException  ,  NoSuchAlgorithmException  ,  InvalidKeyException  ,  NoSuchProviderException  ,  SignatureException  { 
sign  (  key  ,  algorithm  ,  null  )  ; 
} 


















public   void   sign  (  PrivateKey   key  ,  String   algorithm  ,  String   provider  )  throws   CertificateException  ,  NoSuchAlgorithmException  ,  InvalidKeyException  ,  NoSuchProviderException  ,  SignatureException  { 
try  { 
if  (  readOnly  )  throw   new   CertificateEncodingException  (  "cannot over-write existing certificate"  )  ; 
Signature   sigEngine  =  null  ; 
if  (  (  provider  ==  null  )  ||  (  provider  .  length  (  )  ==  0  )  )  sigEngine  =  Signature  .  getInstance  (  algorithm  )  ;  else   sigEngine  =  Signature  .  getInstance  (  algorithm  ,  provider  )  ; 
sigEngine  .  initSign  (  key  )  ; 
algId  =  AlgorithmId  .  get  (  sigEngine  .  getAlgorithm  (  )  )  ; 
DerOutputStream   out  =  new   DerOutputStream  (  )  ; 
DerOutputStream   tmp  =  new   DerOutputStream  (  )  ; 
info  .  encode  (  tmp  )  ; 
byte  [  ]  rawCert  =  tmp  .  toByteArray  (  )  ; 
algId  .  encode  (  tmp  )  ; 
sigEngine  .  update  (  rawCert  ,  0  ,  rawCert  .  length  )  ; 
signature  =  sigEngine  .  sign  (  )  ; 
tmp  .  putBitString  (  signature  )  ; 
out  .  write  (  DerValue  .  tag_Sequence  ,  tmp  )  ; 
signedCert  =  out  .  toByteArray  (  )  ; 
readOnly  =  true  ; 
}  catch  (  IOException   e  )  { 
throw   new   CertificateEncodingException  (  e  .  toString  (  )  )  ; 
} 
} 

public   void   sign  (  PrivateKey   keyPrivate  ,  X500Signer   signer  )  throws   IOException  ,  CertificateException  ,  NoSuchAlgorithmException  ,  InvalidKeyException  ,  NoSuchProviderException  ,  SignatureException  { 
if  (  readOnly  )  throw   new   CertificateEncodingException  (  "cannot over-write existing certificate"  )  ;  else  { 
Signature   sigEngine  =  signer  .  getSignature  (  )  ; 
sigEngine  .  initSign  (  keyPrivate  )  ; 
AlgorithmId   algId  =  signer  .  getAlgorithmId  (  )  ; 
DerOutputStream   out  =  new   DerOutputStream  (  )  ; 
DerOutputStream   tmp  =  new   DerOutputStream  (  )  ; 
info  .  encode  (  tmp  )  ; 
byte  [  ]  rawCert  =  tmp  .  toByteArray  (  )  ; 
algId  .  encode  (  tmp  )  ; 
sigEngine  .  update  (  rawCert  ,  0  ,  rawCert  .  length  )  ; 
signature  =  sigEngine  .  sign  (  )  ; 
tmp  .  putBitString  (  signature  )  ; 
out  .  write  (  DerValue  .  tag_Sequence  ,  tmp  )  ; 
signedCert  =  out  .  toByteArray  (  )  ; 
readOnly  =  true  ; 
} 
} 









public   void   checkValidity  (  )  throws   CertificateExpiredException  ,  CertificateNotYetValidException  { 
Date   date  =  new   Date  (  )  ; 
checkValidity  (  date  )  ; 
} 














public   void   checkValidity  (  Date   date  )  throws   CertificateExpiredException  ,  CertificateNotYetValidException  { 
CertificateValidity   interval  =  null  ; 
try  { 
interval  =  (  CertificateValidity  )  info  .  get  (  CertificateValidity  .  NAME  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   CertificateNotYetValidException  (  "Incorrect validity period"  )  ; 
} 
if  (  interval  ==  null  )  throw   new   CertificateNotYetValidException  (  "Null validity period"  )  ; 
interval  .  valid  (  date  )  ; 
} 











public   Object   get  (  String   name  )  throws   CertificateParsingException  { 
X509AttributeName   attr  =  new   X509AttributeName  (  name  )  ; 
String   id  =  attr  .  getPrefix  (  )  ; 
if  (  !  (  id  .  equalsIgnoreCase  (  NAME  )  )  )  { 
throw   new   CertificateParsingException  (  "Invalid root of "  +  "attribute name, expected ["  +  NAME  +  "], received "  +  "["  +  id  +  "]"  )  ; 
} 
attr  =  new   X509AttributeName  (  attr  .  getSuffix  (  )  )  ; 
id  =  attr  .  getPrefix  (  )  ; 
if  (  id  .  equalsIgnoreCase  (  INFO  )  )  { 
if  (  info  ==  null  )  { 
return   null  ; 
} 
if  (  attr  .  getSuffix  (  )  !=  null  )  { 
try  { 
return   info  .  get  (  attr  .  getSuffix  (  )  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   CertificateParsingException  (  e  .  toString  (  )  )  ; 
}  catch  (  CertificateException   e  )  { 
throw   new   CertificateParsingException  (  e  .  toString  (  )  )  ; 
} 
}  else  { 
return   info  ; 
} 
}  else   if  (  id  .  equalsIgnoreCase  (  ALG_ID  )  )  { 
return  (  algId  )  ; 
}  else   if  (  id  .  equalsIgnoreCase  (  SIGNATURE  )  )  { 
if  (  signature  !=  null  )  return   signature  .  clone  (  )  ;  else   return   null  ; 
}  else   if  (  id  .  equalsIgnoreCase  (  SIGNED_CERT  )  )  { 
if  (  signedCert  !=  null  )  return   signedCert  .  clone  (  )  ;  else   return   null  ; 
}  else  { 
throw   new   CertificateParsingException  (  "Attribute name not "  +  "recognized or get() not allowed for the same: "  +  id  )  ; 
} 
} 









public   void   set  (  String   name  ,  Object   obj  )  throws   CertificateException  ,  IOException  { 
if  (  readOnly  )  throw   new   CertificateException  (  "cannot over-write existing"  +  " certificate"  )  ; 
X509AttributeName   attr  =  new   X509AttributeName  (  name  )  ; 
String   id  =  attr  .  getPrefix  (  )  ; 
if  (  !  (  id  .  equalsIgnoreCase  (  NAME  )  )  )  { 
throw   new   CertificateException  (  "Invalid root of attribute name,"  +  " expected ["  +  NAME  +  "], received "  +  id  )  ; 
} 
attr  =  new   X509AttributeName  (  attr  .  getSuffix  (  )  )  ; 
id  =  attr  .  getPrefix  (  )  ; 
if  (  id  .  equalsIgnoreCase  (  INFO  )  )  { 
if  (  attr  .  getSuffix  (  )  ==  null  )  { 
if  (  !  (  obj   instanceof   X509CertInfo  )  )  { 
throw   new   CertificateException  (  "Attribute value should"  +  " be of type X509CertInfo."  )  ; 
} 
info  =  (  X509CertInfo  )  obj  ; 
signedCert  =  null  ; 
}  else  { 
info  .  set  (  attr  .  getSuffix  (  )  ,  obj  )  ; 
signedCert  =  null  ; 
} 
}  else  { 
throw   new   CertificateException  (  "Attribute name not recognized or "  +  "set() not allowed for the same: "  +  id  )  ; 
} 
} 








public   void   delete  (  String   name  )  throws   CertificateException  ,  IOException  { 
if  (  readOnly  )  throw   new   CertificateException  (  "cannot over-write existing"  +  " certificate"  )  ; 
X509AttributeName   attr  =  new   X509AttributeName  (  name  )  ; 
String   id  =  attr  .  getPrefix  (  )  ; 
if  (  !  (  id  .  equalsIgnoreCase  (  NAME  )  )  )  { 
throw   new   CertificateException  (  "Invalid root of attribute name,"  +  " expected ["  +  NAME  +  "], received "  +  id  )  ; 
} 
attr  =  new   X509AttributeName  (  attr  .  getSuffix  (  )  )  ; 
id  =  attr  .  getPrefix  (  )  ; 
if  (  id  .  equalsIgnoreCase  (  INFO  )  )  { 
if  (  attr  .  getSuffix  (  )  !=  null  )  { 
info  =  null  ; 
}  else  { 
info  .  delete  (  attr  .  getSuffix  (  )  )  ; 
} 
}  else   if  (  id  .  equalsIgnoreCase  (  ALG_ID  )  )  { 
algId  =  null  ; 
}  else   if  (  id  .  equalsIgnoreCase  (  SIGNATURE  )  )  { 
signature  =  null  ; 
}  else   if  (  id  .  equalsIgnoreCase  (  SIGNED_CERT  )  )  { 
signedCert  =  null  ; 
}  else  { 
throw   new   CertificateException  (  "Attribute name not recognized or "  +  "delete() not allowed for the same: "  +  id  )  ; 
} 
} 





public   Enumeration  <  String  >  getElements  (  )  { 
AttributeNameEnumeration   elements  =  new   AttributeNameEnumeration  (  )  ; 
elements  .  addElement  (  NAME  +  DOT  +  INFO  )  ; 
elements  .  addElement  (  NAME  +  DOT  +  ALG_ID  )  ; 
elements  .  addElement  (  NAME  +  DOT  +  SIGNATURE  )  ; 
elements  .  addElement  (  NAME  +  DOT  +  SIGNED_CERT  )  ; 
return   elements  .  elements  (  )  ; 
} 




public   String   getName  (  )  { 
return  (  NAME  )  ; 
} 







public   String   toString  (  )  { 
if  (  info  ==  null  ||  algId  ==  null  ||  signature  ==  null  )  return  ""  ; 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
sb  .  append  (  "[\n"  )  ; 
sb  .  append  (  info  .  toString  (  )  +  "\n"  )  ; 
sb  .  append  (  "  Algorithm: ["  +  algId  .  toString  (  )  +  "]\n"  )  ; 
sb  .  append  (  "  Signature:\n"  +  Hex  .  encode  (  signature  )  )  ; 
sb  .  append  (  "\n]"  )  ; 
return   sb  .  toString  (  )  ; 
} 






public   PublicKey   getPublicKey  (  )  { 
if  (  info  ==  null  )  return   null  ; 
try  { 
PublicKey   key  =  (  PublicKey  )  info  .  get  (  CertificateX509Key  .  NAME  +  DOT  +  CertificateX509Key  .  KEY  )  ; 
return   key  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






public   int   getVersion  (  )  { 
if  (  info  ==  null  )  return  -  1  ; 
try  { 
int   vers  =  (  (  Integer  )  info  .  get  (  CertificateVersion  .  NAME  +  DOT  +  CertificateVersion  .  VERSION  )  )  .  intValue  (  )  ; 
return   vers  +  1  ; 
}  catch  (  Exception   e  )  { 
return  -  1  ; 
} 
} 






public   BigInteger   getSerialNumber  (  )  { 
SerialNumber   ser  =  getSerialNumberObject  (  )  ; 
return   ser  !=  null  ?  ser  .  getNumber  (  )  :  null  ; 
} 







public   SerialNumber   getSerialNumberObject  (  )  { 
if  (  info  ==  null  )  return   null  ; 
try  { 
SerialNumber   ser  =  (  SerialNumber  )  info  .  get  (  CertificateSerialNumber  .  NAME  +  DOT  +  CertificateSerialNumber  .  NUMBER  )  ; 
return   ser  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






public   Principal   getSubjectDN  (  )  { 
if  (  info  ==  null  )  return   null  ; 
try  { 
Principal   subject  =  (  Principal  )  info  .  get  (  CertificateSubjectName  .  NAME  +  DOT  +  CertificateSubjectName  .  DN_NAME  )  ; 
return   subject  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






public   X500Name   getSubjectX500Principal  (  )  { 
if  (  info  ==  null  )  { 
return   null  ; 
} 
try  { 
return  (  X500Name  )  info  .  get  (  CertificateSubjectName  .  NAME  +  DOT  +  CertificateSubjectName  .  DN_PRINCIPAL  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






public   Principal   getIssuerDN  (  )  { 
if  (  info  ==  null  )  return   null  ;  else  { 
try  { 
return  (  Principal  )  info  .  get  (  CertificateIssuerName  .  NAME  +  DOT  +  CertificateIssuerName  .  DN_NAME  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 
} 






public   X500Name   getIssuerX500Principal  (  )  { 
if  (  info  ==  null  )  return   null  ;  else  { 
try  { 
return  (  X500Name  )  info  .  get  (  CertificateIssuerName  .  NAME  +  DOT  +  CertificateIssuerName  .  DN_PRINCIPAL  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 
} 






public   Date   getNotBefore  (  )  { 
if  (  info  ==  null  )  return   null  ; 
try  { 
Date   d  =  (  Date  )  info  .  get  (  CertificateValidity  .  NAME  +  DOT  +  CertificateValidity  .  NOT_BEFORE  )  ; 
return   d  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






public   Date   getNotAfter  (  )  { 
if  (  info  ==  null  )  return   null  ; 
try  { 
Date   d  =  (  Date  )  info  .  get  (  CertificateValidity  .  NAME  +  DOT  +  CertificateValidity  .  NOT_AFTER  )  ; 
return   d  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 









public   byte  [  ]  getTBSCertificate  (  )  throws   CertificateEncodingException  { 
if  (  info  !=  null  )  { 
return   info  .  getEncodedInfo  (  )  ; 
}  else   throw   new   CertificateEncodingException  (  "Uninitialized certificate"  )  ; 
} 






public   byte  [  ]  getSignature  (  )  { 
if  (  signature  ==  null  )  return   null  ; 
byte  [  ]  dup  =  new   byte  [  signature  .  length  ]  ; 
System  .  arraycopy  (  signature  ,  0  ,  dup  ,  0  ,  dup  .  length  )  ; 
return   dup  ; 
} 








public   String   getSigAlgName  (  )  { 
if  (  algId  ==  null  )  return   null  ; 
return  (  algId  .  getName  (  )  )  ; 
} 







public   String   getSigAlgOID  (  )  { 
if  (  algId  ==  null  )  return   null  ; 
ObjectIdentifier   oid  =  algId  .  getOID  (  )  ; 
return  (  oid  .  toString  (  )  )  ; 
} 








public   byte  [  ]  getSigAlgParams  (  )  { 
if  (  algId  ==  null  )  return   null  ; 
try  { 
return   algId  .  getEncodedParams  (  )  ; 
}  catch  (  IOException   e  )  { 
return   null  ; 
} 
} 






public   boolean  [  ]  getIssuerUniqueID  (  )  { 
if  (  info  ==  null  )  return   null  ; 
try  { 
UniqueIdentity   id  =  (  UniqueIdentity  )  info  .  get  (  CertificateIssuerUniqueIdentity  .  NAME  +  DOT  +  CertificateIssuerUniqueIdentity  .  ID  )  ; 
if  (  id  ==  null  )  return   null  ;  else   return  (  id  .  getId  (  )  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






public   boolean  [  ]  getSubjectUniqueID  (  )  { 
if  (  info  ==  null  )  return   null  ; 
try  { 
UniqueIdentity   id  =  (  UniqueIdentity  )  info  .  get  (  CertificateSubjectUniqueIdentity  .  NAME  +  DOT  +  CertificateSubjectUniqueIdentity  .  ID  )  ; 
if  (  id  ==  null  )  return   null  ;  else   return  (  id  .  getId  (  )  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






public   AuthorityKeyIdentifierExtension   getAuthorityKeyIdentifierExtension  (  )  { 
return  (  AuthorityKeyIdentifierExtension  )  getExtension  (  PKIXExtensions  .  AuthorityKey_Id  )  ; 
} 






public   BasicConstraintsExtension   getBasicConstraintsExtension  (  )  { 
return  (  BasicConstraintsExtension  )  getExtension  (  PKIXExtensions  .  BasicConstraints_Id  )  ; 
} 






public   CertificatePoliciesExtension   getCertificatePoliciesExtension  (  )  { 
return  (  CertificatePoliciesExtension  )  getExtension  (  PKIXExtensions  .  CertificatePolicies_Id  )  ; 
} 






public   ExtendedKeyUsageExtension   getExtendedKeyUsageExtension  (  )  { 
return  (  ExtendedKeyUsageExtension  )  getExtension  (  PKIXExtensions  .  ExtendedKeyUsage_Id  )  ; 
} 






public   IssuerAlternativeNameExtension   getIssuerAlternativeNameExtension  (  )  { 
return  (  IssuerAlternativeNameExtension  )  getExtension  (  PKIXExtensions  .  IssuerAlternativeName_Id  )  ; 
} 





public   NameConstraintsExtension   getNameConstraintsExtension  (  )  { 
return  (  NameConstraintsExtension  )  getExtension  (  PKIXExtensions  .  NameConstraints_Id  )  ; 
} 






public   PolicyConstraintsExtension   getPolicyConstraintsExtension  (  )  { 
return  (  PolicyConstraintsExtension  )  getExtension  (  PKIXExtensions  .  PolicyConstraints_Id  )  ; 
} 






public   PolicyMappingsExtension   getPolicyMappingsExtension  (  )  { 
return  (  PolicyMappingsExtension  )  getExtension  (  PKIXExtensions  .  PolicyMappings_Id  )  ; 
} 





public   PrivateKeyUsageExtension   getPrivateKeyUsageExtension  (  )  { 
return  (  PrivateKeyUsageExtension  )  getExtension  (  PKIXExtensions  .  PrivateKeyUsage_Id  )  ; 
} 






public   SubjectAlternativeNameExtension   getSubjectAlternativeNameExtension  (  )  { 
return  (  SubjectAlternativeNameExtension  )  getExtension  (  PKIXExtensions  .  SubjectAlternativeName_Id  )  ; 
} 






public   SubjectKeyIdentifierExtension   getSubjectKeyIdentifierExtension  (  )  { 
return  (  SubjectKeyIdentifierExtension  )  getExtension  (  PKIXExtensions  .  SubjectKey_Id  )  ; 
} 






public   CRLDistributionPointsExtension   getCRLDistributionPointsExtension  (  )  { 
return  (  CRLDistributionPointsExtension  )  getExtension  (  PKIXExtensions  .  CRLDistributionPoints_Id  )  ; 
} 





public   boolean   hasUnsupportedCriticalExtension  (  )  { 
if  (  info  ==  null  )  return   false  ; 
try  { 
CertificateExtensions   exts  =  (  CertificateExtensions  )  info  .  get  (  CertificateExtensions  .  NAME  )  ; 
if  (  exts  ==  null  )  return   false  ; 
return   exts  .  hasUnsupportedCriticalExtension  (  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
} 









public   Set  <  String  >  getCriticalExtensionOIDs  (  )  { 
if  (  info  ==  null  )  { 
return   null  ; 
} 
try  { 
CertificateExtensions   exts  =  (  CertificateExtensions  )  info  .  get  (  CertificateExtensions  .  NAME  )  ; 
if  (  exts  ==  null  )  { 
return   null  ; 
} 
Set  <  String  >  extSet  =  new   HashSet  <  String  >  (  )  ; 
for  (  Extension   ex  :  exts  .  getAllExtensions  (  )  )  { 
if  (  ex  .  isCritical  (  )  )  { 
extSet  .  add  (  ex  .  getExtensionId  (  )  .  toString  (  )  )  ; 
} 
} 
return   extSet  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 









public   Set  <  String  >  getNonCriticalExtensionOIDs  (  )  { 
if  (  info  ==  null  )  { 
return   null  ; 
} 
try  { 
CertificateExtensions   exts  =  (  CertificateExtensions  )  info  .  get  (  CertificateExtensions  .  NAME  )  ; 
if  (  exts  ==  null  )  { 
return   null  ; 
} 
Set  <  String  >  extSet  =  new   HashSet  <  String  >  (  )  ; 
for  (  Extension   ex  :  exts  .  getAllExtensions  (  )  )  { 
if  (  !  ex  .  isCritical  (  )  )  { 
extSet  .  add  (  ex  .  getExtensionId  (  )  .  toString  (  )  )  ; 
} 
} 
extSet  .  addAll  (  exts  .  getUnparseableExtensions  (  )  .  keySet  (  )  )  ; 
return   extSet  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 








public   Extension   getExtension  (  ObjectIdentifier   oid  )  { 
if  (  info  ==  null  )  { 
return   null  ; 
} 
try  { 
CertificateExtensions   extensions  ; 
try  { 
extensions  =  (  CertificateExtensions  )  info  .  get  (  CertificateExtensions  .  NAME  )  ; 
}  catch  (  CertificateException   ce  )  { 
return   null  ; 
} 
if  (  extensions  ==  null  )  { 
return   null  ; 
}  else  { 
for  (  Extension   ex  :  extensions  .  getAllExtensions  (  )  )  { 
if  (  ex  .  getExtensionId  (  )  .  equals  (  oid  )  )  { 
return   ex  ; 
} 
} 
return   null  ; 
} 
}  catch  (  IOException   ioe  )  { 
return   null  ; 
} 
} 

public   Extension   getUnparseableExtension  (  ObjectIdentifier   oid  )  { 
if  (  info  ==  null  )  { 
return   null  ; 
} 
try  { 
CertificateExtensions   extensions  ; 
try  { 
extensions  =  (  CertificateExtensions  )  info  .  get  (  CertificateExtensions  .  NAME  )  ; 
}  catch  (  CertificateException   ce  )  { 
return   null  ; 
} 
if  (  extensions  ==  null  )  { 
return   null  ; 
}  else  { 
return   extensions  .  getUnparseableExtensions  (  )  .  get  (  oid  .  toString  (  )  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
return   null  ; 
} 
} 







public   byte  [  ]  getExtensionValue  (  String   oid  )  { 
try  { 
ObjectIdentifier   findOID  =  new   ObjectIdentifier  (  oid  )  ; 
String   extAlias  =  OIDMap  .  getName  (  findOID  )  ; 
Extension   certExt  =  null  ; 
CertificateExtensions   exts  =  (  CertificateExtensions  )  info  .  get  (  CertificateExtensions  .  NAME  )  ; 
if  (  extAlias  ==  null  )  { 
if  (  exts  ==  null  )  { 
return   null  ; 
} 
for  (  Extension   ex  :  exts  .  getAllExtensions  (  )  )  { 
ObjectIdentifier   inCertOID  =  ex  .  getExtensionId  (  )  ; 
if  (  inCertOID  .  equals  (  findOID  )  )  { 
certExt  =  ex  ; 
break  ; 
} 
} 
}  else  { 
try  { 
certExt  =  (  Extension  )  this  .  get  (  extAlias  )  ; 
}  catch  (  CertificateException   e  )  { 
} 
} 
if  (  certExt  ==  null  )  { 
if  (  exts  !=  null  )  { 
certExt  =  exts  .  getUnparseableExtensions  (  )  .  get  (  oid  )  ; 
} 
if  (  certExt  ==  null  )  { 
return   null  ; 
} 
} 
byte  [  ]  extData  =  certExt  .  getExtensionValue  (  )  ; 
if  (  extData  ==  null  )  { 
return   null  ; 
} 
DerOutputStream   out  =  new   DerOutputStream  (  )  ; 
out  .  putOctetString  (  extData  )  ; 
return   out  .  toByteArray  (  )  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 






public   boolean  [  ]  getKeyUsage  (  )  { 
try  { 
String   extAlias  =  OIDMap  .  getName  (  PKIXExtensions  .  KeyUsage_Id  )  ; 
if  (  extAlias  ==  null  )  return   null  ; 
KeyUsageExtension   certExt  =  (  KeyUsageExtension  )  this  .  get  (  extAlias  )  ; 
if  (  certExt  ==  null  )  return   null  ; 
boolean  [  ]  ret  =  certExt  .  getBits  (  )  ; 
if  (  ret  .  length  <  NUM_STANDARD_KEY_USAGE  )  { 
boolean  [  ]  usageBits  =  new   boolean  [  NUM_STANDARD_KEY_USAGE  ]  ; 
System  .  arraycopy  (  ret  ,  0  ,  usageBits  ,  0  ,  ret  .  length  )  ; 
ret  =  usageBits  ; 
} 
return   ret  ; 
}  catch  (  Exception   e  )  { 
return   null  ; 
} 
} 







public   synchronized   List  <  String  >  getExtendedKeyUsage  (  )  throws   CertificateParsingException  { 
if  (  readOnly  &&  extKeyUsage  !=  null  )  { 
return   extKeyUsage  ; 
}  else  { 
ExtendedKeyUsageExtension   ext  =  getExtendedKeyUsageExtension  (  )  ; 
if  (  ext  ==  null  )  { 
return   null  ; 
} 
extKeyUsage  =  Collections  .  unmodifiableList  (  ext  .  getExtendedKeyUsage  (  )  )  ; 
return   extKeyUsage  ; 
} 
} 







public   static   List  <  String  >  getExtendedKeyUsage  (  Certificate   cert  )  throws   CertificateException  { 
try  { 
byte  [  ]  ext  =  toImpl  (  cert  )  .  getExtensionValue  (  EXTENDED_KEY_USAGE_OID  )  ; 
if  (  ext  ==  null  )  return   null  ;  else  { 
DerValue   val  =  new   DerValue  (  ext  )  ; 
byte  [  ]  data  =  val  .  getOctetString  (  )  ; 
ExtendedKeyUsageExtension   ekuExt  =  new   ExtendedKeyUsageExtension  (  Boolean  .  FALSE  ,  data  )  ; 
return   Collections  .  unmodifiableList  (  ekuExt  .  getExtendedKeyUsage  (  )  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
throw   new   CertificateParsingException  (  ioe  )  ; 
} 
} 






public   int   getBasicConstraints  (  )  { 
try  { 
String   extAlias  =  OIDMap  .  getName  (  PKIXExtensions  .  BasicConstraints_Id  )  ; 
if  (  extAlias  ==  null  )  return  -  1  ;  else  { 
BasicConstraintsExtension   certExt  =  (  BasicConstraintsExtension  )  this  .  get  (  extAlias  )  ; 
if  (  certExt  ==  null  )  return  -  1  ;  else  { 
if  (  (  (  Boolean  )  certExt  .  get  (  BasicConstraintsExtension  .  IS_CA  )  )  .  booleanValue  (  )  )  return  (  (  Integer  )  certExt  .  get  (  BasicConstraintsExtension  .  PATH_LEN  )  )  .  intValue  (  )  ;  else   return  -  1  ; 
} 
} 
}  catch  (  Exception   e  )  { 
return  -  1  ; 
} 
} 










private   static   Collection  <  List  <  ?  >  >  makeAltNames  (  GeneralNames   names  )  { 
if  (  names  .  isEmpty  (  )  )  { 
return   Collections  .  <  List  <  ?  >  >  emptySet  (  )  ; 
} 
Set  <  List  <  ?  >  >  newNames  =  new   HashSet  <  List  <  ?  >  >  (  )  ; 
for  (  GeneralName   gname  :  names  .  names  (  )  )  { 
GeneralNameInterface   name  =  gname  .  getName  (  )  ; 
List  <  Object  >  nameEntry  =  new   ArrayList  <  Object  >  (  2  )  ; 
nameEntry  .  add  (  Integer  .  valueOf  (  name  .  getType  (  )  )  )  ; 
switch  (  name  .  getType  (  )  )  { 
case   GeneralNameInterface  .  NAME_RFC822  : 
nameEntry  .  add  (  (  (  RFC822Name  )  name  )  .  getName  (  )  )  ; 
break  ; 
case   GeneralNameInterface  .  NAME_DNS  : 
nameEntry  .  add  (  (  (  DNSName  )  name  )  .  getName  (  )  )  ; 
break  ; 
case   GeneralNameInterface  .  NAME_DIRECTORY  : 
nameEntry  .  add  (  (  (  X500Name  )  name  )  .  getRFC2253Name  (  )  )  ; 
break  ; 
case   GeneralNameInterface  .  NAME_URI  : 
nameEntry  .  add  (  (  (  URIName  )  name  )  .  getName  (  )  )  ; 
break  ; 
case   GeneralNameInterface  .  NAME_IP  : 
try  { 
nameEntry  .  add  (  (  (  IPAddressName  )  name  )  .  getName  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   RuntimeException  (  "IPAddress cannot be parsed"  ,  ioe  )  ; 
} 
break  ; 
case   GeneralNameInterface  .  NAME_OID  : 
nameEntry  .  add  (  (  (  OIDName  )  name  )  .  getOID  (  )  .  toString  (  )  )  ; 
break  ; 
default  : 
DerOutputStream   derOut  =  new   DerOutputStream  (  )  ; 
try  { 
name  .  encode  (  derOut  )  ; 
}  catch  (  IOException   ioe  )  { 
throw   new   RuntimeException  (  "name cannot be encoded"  ,  ioe  )  ; 
} 
nameEntry  .  add  (  derOut  .  toByteArray  (  )  )  ; 
break  ; 
} 
newNames  .  add  (  Collections  .  unmodifiableList  (  nameEntry  )  )  ; 
} 
return   Collections  .  unmodifiableCollection  (  newNames  )  ; 
} 

private   static   Collection  <  List  <  ?  >  >  cloneAltNames  (  Collection  <  List  <  ?  >  >  altNames  )  { 
boolean   mustClone  =  false  ; 
for  (  List  <  ?  >  nameEntry  :  altNames  )  { 
if  (  nameEntry  .  get  (  1  )  instanceof   byte  [  ]  )  { 
mustClone  =  true  ; 
} 
} 
if  (  mustClone  )  { 
Set  <  List  <  ?  >  >  namesCopy  =  new   HashSet  <  List  <  ?  >  >  (  )  ; 
for  (  List  <  ?  >  nameEntry  :  altNames  )  { 
Object   nameObject  =  nameEntry  .  get  (  1  )  ; 
if  (  nameObject   instanceof   byte  [  ]  )  { 
List  <  Object  >  nameEntryCopy  =  new   ArrayList  <  Object  >  (  nameEntry  )  ; 
nameEntryCopy  .  set  (  1  ,  (  (  byte  [  ]  )  nameObject  )  .  clone  (  )  )  ; 
namesCopy  .  add  (  Collections  .  unmodifiableList  (  nameEntryCopy  )  )  ; 
}  else  { 
namesCopy  .  add  (  nameEntry  )  ; 
} 
} 
return   Collections  .  unmodifiableCollection  (  namesCopy  )  ; 
}  else  { 
return   altNames  ; 
} 
} 







public   synchronized   Collection  <  List  <  ?  >  >  getSubjectAlternativeNames  (  )  throws   CertificateParsingException  { 
if  (  readOnly  &&  subjectAlternativeNames  !=  null  )  { 
return   cloneAltNames  (  subjectAlternativeNames  )  ; 
} 
SubjectAlternativeNameExtension   subjectAltNameExt  =  getSubjectAlternativeNameExtension  (  )  ; 
if  (  subjectAltNameExt  ==  null  )  { 
return   null  ; 
} 
GeneralNames   names  ; 
try  { 
names  =  (  GeneralNames  )  subjectAltNameExt  .  get  (  SubjectAlternativeNameExtension  .  SUBJECT_NAME  )  ; 
}  catch  (  IOException   ioe  )  { 
return   Collections  .  <  List  <  ?  >  >  emptySet  (  )  ; 
} 
subjectAlternativeNames  =  makeAltNames  (  names  )  ; 
return   subjectAlternativeNames  ; 
} 







public   static   Collection  <  List  <  ?  >  >  getSubjectAlternativeNames  (  Certificate   cert  )  throws   CertificateException  { 
try  { 
byte  [  ]  ext  =  toImpl  (  cert  )  .  getExtensionValue  (  SUBJECT_ALT_NAME_OID  )  ; 
if  (  ext  ==  null  )  { 
return   null  ; 
}  else  { 
DerValue   val  =  new   DerValue  (  ext  )  ; 
byte  [  ]  data  =  val  .  getOctetString  (  )  ; 
SubjectAlternativeNameExtension   subjectAltNameExt  =  new   SubjectAlternativeNameExtension  (  Boolean  .  FALSE  ,  data  )  ; 
GeneralNames   names  ; 
try  { 
names  =  (  GeneralNames  )  subjectAltNameExt  .  get  (  SubjectAlternativeNameExtension  .  SUBJECT_NAME  )  ; 
}  catch  (  IOException   ioe  )  { 
return   Collections  .  <  List  <  ?  >  >  emptySet  (  )  ; 
} 
return   makeAltNames  (  names  )  ; 
} 
}  catch  (  IOException   ioe  )  { 
throw   new   CertificateParsingException  (  ioe  )  ; 
} 
} 







@  Code  (  Check  .  Builds  ) 
public   synchronized   Collection  <  List  <  ?  >  >  getIssuerAlternativeNames  (  )  throws   CertificateException  { 
if  (  readOnly  &&  this  .  issuerAlternativeNames  !=  null  )  { 
return   cloneAltNames  (  issuerAlternativeNames  )  ; 
}  else  { 
IssuerAlternativeNameExtension   issuerAltNameExt  =  this  .  getIssuerAlternativeNameExtension  (  )  ; 
if  (  issuerAltNameExt  ==  null  )  return   null  ;  else  { 
GeneralNames   names  ; 
try  { 
names  =  (  GeneralNames  )  issuerAltNameExt  .  get  (  IssuerAlternativeNameExtension  .  ISSUER_NAME  )  ; 
this  .  issuerAlternativeNames  =  makeAltNames  (  names  )  ; 
return   this  .  issuerAlternativeNames  ; 
}  catch  (  IOException   ioe  )  { 
return   Collections  .  <  List  <  ?  >  >  emptySet  (  )  ; 
} 
} 
} 
} 







public   static   Collection  <  List  <  ?  >  >  getIssuerAlternativeNames  (  Certificate   cert  )  throws   CertificateException  { 
try  { 
byte  [  ]  ext  =  toImpl  (  cert  )  .  getExtensionValue  (  ISSUER_ALT_NAME_OID  )  ; 
if  (  ext  ==  null  )  { 
return   null  ; 
} 
DerValue   val  =  new   DerValue  (  ext  )  ; 
byte  [  ]  data  =  val  .  getOctetString  (  )  ; 
IssuerAlternativeNameExtension   issuerAltNameExt  =  new   IssuerAlternativeNameExtension  (  Boolean  .  FALSE  ,  data  )  ; 
GeneralNames   names  ; 
try  { 
names  =  (  GeneralNames  )  issuerAltNameExt  .  get  (  IssuerAlternativeNameExtension  .  ISSUER_NAME  )  ; 
}  catch  (  IOException   ioe  )  { 
return   Collections  .  <  List  <  ?  >  >  emptySet  (  )  ; 
} 
return   makeAltNames  (  names  )  ; 
}  catch  (  IOException   ioe  )  { 
CertificateParsingException   cpe  =  new   CertificateParsingException  (  )  ; 
cpe  .  initCause  (  ioe  )  ; 
throw   cpe  ; 
} 
} 

public   AuthorityInfoAccessExtension   getAuthorityInfoAccessExtension  (  )  { 
return  (  AuthorityInfoAccessExtension  )  getExtension  (  PKIXExtensions  .  AuthInfoAccess_Id  )  ; 
} 

private   void   parse  (  DerValue   val  )  throws   CertificateException  ,  IOException  { 
if  (  readOnly  )  throw   new   CertificateParsingException  (  "cannot over-write existing certificate"  )  ; 
if  (  val  .  data  ==  null  ||  val  .  tag  !=  DerValue  .  tag_Sequence  )  throw   new   CertificateParsingException  (  "invalid DER-encoded certificate data"  )  ; 
signedCert  =  val  .  toByteArray  (  )  ; 
DerValue  [  ]  seq  =  new   DerValue  [  3  ]  ; 
seq  [  0  ]  =  val  .  data  .  getDerValue  (  )  ; 
seq  [  1  ]  =  val  .  data  .  getDerValue  (  )  ; 
seq  [  2  ]  =  val  .  data  .  getDerValue  (  )  ; 
if  (  val  .  data  .  available  (  )  !=  0  )  { 
throw   new   CertificateParsingException  (  "signed overrun, bytes = "  +  val  .  data  .  available  (  )  )  ; 
} 
if  (  seq  [  0  ]  .  tag  !=  DerValue  .  tag_Sequence  )  { 
throw   new   CertificateParsingException  (  "signed fields invalid"  )  ; 
} 
algId  =  AlgorithmId  .  parse  (  seq  [  1  ]  )  ; 
signature  =  seq  [  2  ]  .  getBitString  (  )  ; 
if  (  seq  [  1  ]  .  data  .  available  (  )  !=  0  )  { 
throw   new   CertificateParsingException  (  "algid field overrun"  )  ; 
} 
if  (  seq  [  2  ]  .  data  .  available  (  )  !=  0  )  throw   new   CertificateParsingException  (  "signed fields overrun"  )  ; 
info  =  new   X509CertInfo  (  seq  [  0  ]  )  ; 
AlgorithmId   infoSigAlg  =  (  AlgorithmId  )  info  .  get  (  CertificateAlgorithmId  .  NAME  +  DOT  +  CertificateAlgorithmId  .  ALGORITHM  )  ; 
if  (  !  algId  .  equals  (  infoSigAlg  )  )  throw   new   CertificateException  (  "Signature algorithm mismatch"  )  ; 
readOnly  =  true  ; 
} 






private   static   X500Name   getX500Principal  (  Certificate   cert  ,  boolean   getIssuer  )  throws   Exception  { 
byte  [  ]  encoded  =  cert  .  getEncoded  (  )  ; 
DerInputStream   derIn  =  new   DerInputStream  (  encoded  )  ; 
DerValue   tbsCert  =  derIn  .  getSequence  (  3  )  [  0  ]  ; 
DerInputStream   tbsIn  =  tbsCert  .  data  ; 
DerValue   tmp  ; 
tmp  =  tbsIn  .  getDerValue  (  )  ; 
if  (  tmp  .  isContextSpecific  (  (  byte  )  0  )  )  { 
tmp  =  tbsIn  .  getDerValue  (  )  ; 
} 
tmp  =  tbsIn  .  getDerValue  (  )  ; 
tmp  =  tbsIn  .  getDerValue  (  )  ; 
if  (  getIssuer  ==  false  )  { 
tmp  =  tbsIn  .  getDerValue  (  )  ; 
tmp  =  tbsIn  .  getDerValue  (  )  ; 
} 
byte  [  ]  principalBytes  =  tmp  .  toByteArray  (  )  ; 
return   new   X500Name  (  principalBytes  )  ; 
} 





public   static   X500Name   getSubjectX500Principal  (  Certificate   cert  )  { 
try  { 
return   getX500Principal  (  cert  ,  false  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Could not parse subject"  ,  e  )  ; 
} 
} 





public   static   X500Name   getIssuerX500Principal  (  Certificate   cert  )  { 
try  { 
return   getX500Principal  (  cert  ,  true  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   RuntimeException  (  "Could not parse issuer"  ,  e  )  ; 
} 
} 







public   static   byte  [  ]  getEncodedInternal  (  Certificate   cert  )  throws   CertificateEncodingException  { 
if  (  cert   instanceof   X509Certificate  )  { 
return  (  (  X509Certificate  )  cert  )  .  getEncodedInternal  (  )  ; 
}  else  { 
return   cert  .  getEncoded  (  )  ; 
} 
} 






public   static   X509Certificate   toImpl  (  Certificate   cert  )  throws   CertificateException  { 
if  (  cert   instanceof   X509Certificate  )  { 
return  (  X509Certificate  )  cert  ; 
}  else  { 
return   new   X509Certificate  (  cert  .  getEncoded  (  )  )  ; 
} 
} 





public   static   boolean   isSelfIssued  (  Certificate   cert  )  throws   CertificateException  { 
X500Name   subject  =  toImpl  (  cert  )  .  getSubjectX500Principal  (  )  ; 
X500Name   issuer  =  toImpl  (  cert  )  .  getIssuerX500Principal  (  )  ; 
return   subject  .  equals  (  issuer  )  ; 
} 







public   static   boolean   isSelfSigned  (  Certificate   cert  ,  String   sigProvider  )  throws   CertificateException  { 
if  (  isSelfIssued  (  cert  )  )  { 
try  { 
if  (  sigProvider  ==  null  )  { 
cert  .  verify  (  cert  .  getPublicKey  (  )  )  ; 
}  else  { 
cert  .  verify  (  cert  .  getPublicKey  (  )  ,  sigProvider  )  ; 
} 
return   true  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return   false  ; 
} 
} 

