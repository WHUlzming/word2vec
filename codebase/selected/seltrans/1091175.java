package   org  .  bouncycastle  .  x509  ; 

import   org  .  bouncycastle  .  asn1  .  ASN1Encodable  ; 
import   org  .  bouncycastle  .  asn1  .  ASN1Sequence  ; 
import   org  .  bouncycastle  .  asn1  .  DERInteger  ; 
import   org  .  bouncycastle  .  asn1  .  DERSequence  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  AlgorithmIdentifier  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  GeneralName  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  GeneralNames  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  Holder  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  IssuerSerial  ; 
import   org  .  bouncycastle  .  asn1  .  x509  .  ObjectDigestInfo  ; 
import   org  .  bouncycastle  .  jce  .  PrincipalUtil  ; 
import   org  .  bouncycastle  .  jce  .  X509Principal  ; 
import   org  .  bouncycastle  .  util  .  Arrays  ; 
import   org  .  bouncycastle  .  util  .  Selector  ; 
import   javax  .  security  .  auth  .  x500  .  X500Principal  ; 
import   java  .  io  .  IOException  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  Principal  ; 
import   java  .  security  .  cert  .  CertSelector  ; 
import   java  .  security  .  cert  .  Certificate  ; 
import   java  .  security  .  cert  .  CertificateEncodingException  ; 
import   java  .  security  .  cert  .  CertificateParsingException  ; 
import   java  .  security  .  cert  .  X509Certificate  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 


















public   class   AttributeCertificateHolder   implements   CertSelector  ,  Selector  { 

final   Holder   holder  ; 

AttributeCertificateHolder  (  ASN1Sequence   seq  )  { 
holder  =  Holder  .  getInstance  (  seq  )  ; 
} 

public   AttributeCertificateHolder  (  X509Principal   issuerName  ,  BigInteger   serialNumber  )  { 
holder  =  new   org  .  bouncycastle  .  asn1  .  x509  .  Holder  (  new   IssuerSerial  (  new   GeneralNames  (  new   DERSequence  (  new   GeneralName  (  issuerName  )  )  )  ,  new   DERInteger  (  serialNumber  )  )  )  ; 
} 

public   AttributeCertificateHolder  (  X500Principal   issuerName  ,  BigInteger   serialNumber  )  { 
this  (  X509Util  .  convertPrincipal  (  issuerName  )  ,  serialNumber  )  ; 
} 

public   AttributeCertificateHolder  (  X509Certificate   cert  )  throws   CertificateParsingException  { 
X509Principal   name  ; 
try  { 
name  =  PrincipalUtil  .  getIssuerX509Principal  (  cert  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   CertificateParsingException  (  e  .  getMessage  (  )  )  ; 
} 
holder  =  new   Holder  (  new   IssuerSerial  (  generateGeneralNames  (  name  )  ,  new   DERInteger  (  cert  .  getSerialNumber  (  )  )  )  )  ; 
} 

public   AttributeCertificateHolder  (  X509Principal   principal  )  { 
holder  =  new   Holder  (  generateGeneralNames  (  principal  )  )  ; 
} 

public   AttributeCertificateHolder  (  X500Principal   principal  )  { 
this  (  X509Util  .  convertPrincipal  (  principal  )  )  ; 
} 
























public   AttributeCertificateHolder  (  int   digestedObjectType  ,  String   digestAlgorithm  ,  String   otherObjectTypeID  ,  byte  [  ]  objectDigest  )  { 
holder  =  new   Holder  (  new   ObjectDigestInfo  (  digestedObjectType  ,  otherObjectTypeID  ,  new   AlgorithmIdentifier  (  digestAlgorithm  )  ,  Arrays  .  clone  (  objectDigest  )  )  )  ; 
} 















public   int   getDigestedObjectType  (  )  { 
if  (  holder  .  getObjectDigestInfo  (  )  !=  null  )  { 
return   holder  .  getObjectDigestInfo  (  )  .  getDigestedObjectType  (  )  .  getValue  (  )  .  intValue  (  )  ; 
} 
return  -  1  ; 
} 







public   String   getDigestAlgorithm  (  )  { 
if  (  holder  .  getObjectDigestInfo  (  )  !=  null  )  { 
holder  .  getObjectDigestInfo  (  )  .  getDigestAlgorithm  (  )  .  getObjectId  (  )  .  getId  (  )  ; 
} 
return   null  ; 
} 






public   byte  [  ]  getObjectDigest  (  )  { 
if  (  holder  .  getObjectDigestInfo  (  )  !=  null  )  { 
holder  .  getObjectDigestInfo  (  )  .  getObjectDigest  (  )  .  getBytes  (  )  ; 
} 
return   null  ; 
} 







public   String   getOtherObjectTypeID  (  )  { 
if  (  holder  .  getObjectDigestInfo  (  )  !=  null  )  { 
holder  .  getObjectDigestInfo  (  )  .  getOtherObjectTypeID  (  )  .  getId  (  )  ; 
} 
return   null  ; 
} 

private   GeneralNames   generateGeneralNames  (  X509Principal   principal  )  { 
return   new   GeneralNames  (  new   DERSequence  (  new   GeneralName  (  principal  )  )  )  ; 
} 

private   boolean   matchesDN  (  X509Principal   subject  ,  GeneralNames   targets  )  { 
GeneralName  [  ]  names  =  targets  .  getNames  (  )  ; 
for  (  int   i  =  0  ;  i  !=  names  .  length  ;  i  ++  )  { 
GeneralName   gn  =  names  [  i  ]  ; 
if  (  gn  .  getTagNo  (  )  ==  GeneralName  .  directoryName  )  { 
try  { 
if  (  new   X509Principal  (  (  (  ASN1Encodable  )  gn  .  getName  (  )  )  .  getEncoded  (  )  )  .  equals  (  subject  )  )  { 
return   true  ; 
} 
}  catch  (  IOException   e  )  { 
} 
} 
} 
return   false  ; 
} 

private   Object  [  ]  getNames  (  GeneralName  [  ]  names  )  { 
List   l  =  new   ArrayList  (  names  .  length  )  ; 
for  (  int   i  =  0  ;  i  !=  names  .  length  ;  i  ++  )  { 
if  (  names  [  i  ]  .  getTagNo  (  )  ==  GeneralName  .  directoryName  )  { 
try  { 
l  .  add  (  new   X500Principal  (  (  (  ASN1Encodable  )  names  [  i  ]  .  getName  (  )  )  .  getEncoded  (  )  )  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   RuntimeException  (  "badly formed Name object"  )  ; 
} 
} 
} 
return   l  .  toArray  (  new   Object  [  l  .  size  (  )  ]  )  ; 
} 

private   Principal  [  ]  getPrincipals  (  GeneralNames   names  )  { 
Object  [  ]  p  =  this  .  getNames  (  names  .  getNames  (  )  )  ; 
List   l  =  new   ArrayList  (  )  ; 
for  (  int   i  =  0  ;  i  !=  p  .  length  ;  i  ++  )  { 
if  (  p  [  i  ]  instanceof   Principal  )  { 
l  .  add  (  p  [  i  ]  )  ; 
} 
} 
return  (  Principal  [  ]  )  l  .  toArray  (  new   Principal  [  l  .  size  (  )  ]  )  ; 
} 








public   Principal  [  ]  getEntityNames  (  )  { 
if  (  holder  .  getEntityName  (  )  !=  null  )  { 
return   getPrincipals  (  holder  .  getEntityName  (  )  )  ; 
} 
return   null  ; 
} 






public   Principal  [  ]  getIssuer  (  )  { 
if  (  holder  .  getBaseCertificateID  (  )  !=  null  )  { 
return   getPrincipals  (  holder  .  getBaseCertificateID  (  )  .  getIssuer  (  )  )  ; 
} 
return   null  ; 
} 








public   BigInteger   getSerialNumber  (  )  { 
if  (  holder  .  getBaseCertificateID  (  )  !=  null  )  { 
return   holder  .  getBaseCertificateID  (  )  .  getSerial  (  )  .  getValue  (  )  ; 
} 
return   null  ; 
} 

public   Object   clone  (  )  { 
return   new   AttributeCertificateHolder  (  (  ASN1Sequence  )  holder  .  toASN1Object  (  )  )  ; 
} 

public   boolean   match  (  Certificate   cert  )  { 
if  (  !  (  cert   instanceof   X509Certificate  )  )  { 
return   false  ; 
} 
X509Certificate   x509Cert  =  (  X509Certificate  )  cert  ; 
try  { 
if  (  holder  .  getBaseCertificateID  (  )  !=  null  )  { 
return   holder  .  getBaseCertificateID  (  )  .  getSerial  (  )  .  getValue  (  )  .  equals  (  x509Cert  .  getSerialNumber  (  )  )  &&  matchesDN  (  PrincipalUtil  .  getIssuerX509Principal  (  x509Cert  )  ,  holder  .  getBaseCertificateID  (  )  .  getIssuer  (  )  )  ; 
} 
if  (  holder  .  getEntityName  (  )  !=  null  )  { 
if  (  matchesDN  (  PrincipalUtil  .  getSubjectX509Principal  (  x509Cert  )  ,  holder  .  getEntityName  (  )  )  )  { 
return   true  ; 
} 
} 
if  (  holder  .  getObjectDigestInfo  (  )  !=  null  )  { 
MessageDigest   md  =  null  ; 
try  { 
md  =  MessageDigest  .  getInstance  (  getDigestAlgorithm  (  )  ,  "BC"  )  ; 
}  catch  (  Exception   e  )  { 
return   false  ; 
} 
switch  (  getDigestedObjectType  (  )  )  { 
case   ObjectDigestInfo  .  publicKey  : 
md  .  update  (  cert  .  getPublicKey  (  )  .  getEncoded  (  )  )  ; 
break  ; 
case   ObjectDigestInfo  .  publicKeyCert  : 
md  .  update  (  cert  .  getEncoded  (  )  )  ; 
break  ; 
} 
if  (  !  Arrays  .  areEqual  (  md  .  digest  (  )  ,  getObjectDigest  (  )  )  )  { 
return   false  ; 
} 
} 
}  catch  (  CertificateEncodingException   e  )  { 
return   false  ; 
} 
return   false  ; 
} 

public   boolean   equals  (  Object   obj  )  { 
if  (  obj  ==  this  )  { 
return   true  ; 
} 
if  (  !  (  obj   instanceof   AttributeCertificateHolder  )  )  { 
return   false  ; 
} 
AttributeCertificateHolder   other  =  (  AttributeCertificateHolder  )  obj  ; 
return   this  .  holder  .  equals  (  other  .  holder  )  ; 
} 

public   int   hashCode  (  )  { 
return   this  .  holder  .  hashCode  (  )  ; 
} 

public   boolean   match  (  Object   obj  )  { 
if  (  !  (  obj   instanceof   X509Certificate  )  )  { 
return   false  ; 
} 
return   match  (  (  Certificate  )  obj  )  ; 
} 
} 

