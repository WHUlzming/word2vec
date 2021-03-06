package   org  .  signserver  .  module  .  mrtdsodsigner  ; 

import   java  .  io  .  ByteArrayInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  security  .  MessageDigest  ; 
import   java  .  security  .  NoSuchAlgorithmException  ; 
import   java  .  security  .  cert  .  Certificate  ; 
import   java  .  security  .  cert  .  CertificateFactory  ; 
import   java  .  security  .  cert  .  X509Certificate  ; 
import   java  .  util  .  *  ; 
import   org  .  signserver  .  cli  .  CommandLineInterface  ; 
import   org  .  signserver  .  common  .  *  ; 
import   org  .  signserver  .  module  .  mrtdsodsigner  .  jmrtd  .  SODFile  ; 
import   org  .  signserver  .  server  .  cryptotokens  .  HardCodedCryptoToken  ; 
import   org  .  signserver  .  testutils  .  ModulesTestCase  ; 
import   org  .  signserver  .  testutils  .  TestingSecurityManager  ; 






public   class   MRTDSODSignerTest   extends   ModulesTestCase  { 


private   static   final   int   WORKER1  =  7897  ; 


private   static   final   int   WORKER2  =  7898  ; 


private   static   final   int   WORKER3  =  7899  ; 


private   static   final   int   WORKER4  =  7900  ; 


private   static   final   int   WORKER1B  =  7901  ; 

private   static   final   int   WORKER1C  =  7902  ; 

private   static   final   int   WORKER1D  =  7903  ; 


private   static   final   int   WORKER5  =  7904  ; 

@  Override 
protected   void   setUp  (  )  throws   Exception  { 
super  .  setUp  (  )  ; 
SignServerUtil  .  installBCProvider  (  )  ; 
} 

@  Override 
protected   void   tearDown  (  )  throws   Exception  { 
super  .  tearDown  (  )  ; 
TestingSecurityManager  .  remove  (  )  ; 
} 

public   void   test00SetupDatabase  (  )  throws   Exception  { 
assertEquals  (  CommandLineInterface  .  RETURN_SUCCESS  ,  getAdminCLI  (  )  .  execute  (  "setproperties"  ,  getSignServerHome  (  )  .  getAbsolutePath  (  )  +  "/modules/SignServer-Module-MRTDSODSigner/src/conf/junittest-part-config.properties"  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1  ,  "KEYSTOREPATH"  ,  getSignServerHome  (  )  .  getAbsolutePath  (  )  +  File  .  separator  +  "res"  +  File  .  separator  +  "test"  +  File  .  separator  +  "demods1.p12"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1  ,  "KEYSTOREPASSWORD"  ,  "foo123"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1B  ,  "KEYSTOREPATH"  ,  getSignServerHome  (  )  .  getAbsolutePath  (  )  +  File  .  separator  +  "res"  +  File  .  separator  +  "test"  +  File  .  separator  +  "dss10/dss10_signer1.p12"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1B  ,  "KEYSTOREPASSWORD"  ,  "foo123"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER2  ,  "KEYSTOREPATH"  ,  getSignServerHome  (  )  .  getAbsolutePath  (  )  +  File  .  separator  +  "res"  +  File  .  separator  +  "test"  +  File  .  separator  +  "demods1.p12"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER2  ,  "KEYSTOREPASSWORD"  ,  "foo123"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER3  ,  "KEYSTOREPATH"  ,  getSignServerHome  (  )  .  getAbsolutePath  (  )  +  File  .  separator  +  "res"  +  File  .  separator  +  "test"  +  File  .  separator  +  "demods1.p12"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER3  ,  "KEYSTOREPASSWORD"  ,  "foo123"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER4  ,  "KEYSTOREPATH"  ,  getSignServerHome  (  )  .  getAbsolutePath  (  )  +  File  .  separator  +  "res"  +  File  .  separator  +  "test"  +  File  .  separator  +  "demods1.p12"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER4  ,  "KEYSTOREPASSWORD"  ,  "foo123"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER5  ,  "KEYSTOREPATH"  ,  getSignServerHome  (  )  .  getAbsolutePath  (  )  +  File  .  separator  +  "res"  +  File  .  separator  +  "test"  +  File  .  separator  +  "demodsecc1.p12"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER5  ,  "KEYSTOREPASSWORD"  ,  "foo123"  )  ; 
workerSession  .  reloadConfiguration  (  WORKER1  )  ; 
workerSession  .  reloadConfiguration  (  WORKER2  )  ; 
workerSession  .  reloadConfiguration  (  WORKER3  )  ; 
workerSession  .  reloadConfiguration  (  WORKER4  )  ; 
workerSession  .  reloadConfiguration  (  WORKER5  )  ; 
workerSession  .  reloadConfiguration  (  WORKER1B  )  ; 
workerSession  .  reloadConfiguration  (  WORKER1C  )  ; 
workerSession  .  reloadConfiguration  (  WORKER1D  )  ; 
} 






public   void   test02SignData  (  )  throws   Exception  { 
Map  <  Integer  ,  byte  [  ]  >  dataGroups1  =  new   LinkedHashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
dataGroups1  .  put  (  1  ,  digestHelper  (  "Dummy Value 1"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
dataGroups1  .  put  (  2  ,  digestHelper  (  "Dummy Value 2"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
signHelper  (  WORKER1  ,  12  ,  dataGroups1  ,  false  ,  "SHA256"  ,  "SHA256withRSA"  )  ; 
Map  <  Integer  ,  byte  [  ]  >  dataGroups2  =  new   LinkedHashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
dataGroups2  .  put  (  3  ,  digestHelper  (  "Dummy Value 3"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
dataGroups2  .  put  (  7  ,  digestHelper  (  "Dummy Value 4"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
dataGroups2  .  put  (  8  ,  digestHelper  (  "Dummy Value 5"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
dataGroups2  .  put  (  13  ,  digestHelper  (  "Dummy Value 6"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
signHelper  (  WORKER1  ,  13  ,  dataGroups2  ,  false  ,  "SHA256"  ,  "SHA256withRSA"  )  ; 
Map  <  Integer  ,  byte  [  ]  >  dataGroups3  =  new   LinkedHashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
dataGroups3  .  put  (  1  ,  digestHelper  (  "Dummy Value 7"  .  getBytes  (  )  ,  "SHA512"  )  )  ; 
dataGroups3  .  put  (  2  ,  digestHelper  (  "Dummy Value 8"  .  getBytes  (  )  ,  "SHA512"  )  )  ; 
signHelper  (  WORKER2  ,  14  ,  dataGroups3  ,  false  ,  "SHA512"  ,  "SHA512withRSA"  )  ; 
signHelper  (  WORKER5  ,  15  ,  dataGroups2  ,  false  ,  "SHA256"  ,  "SHA256withECDSA"  )  ; 
} 







public   void   test03SignUnhashedData  (  )  throws   Exception  { 
Map  <  Integer  ,  byte  [  ]  >  dataGroups1  =  new   LinkedHashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
dataGroups1  .  put  (  1  ,  "Dummy Value 1"  .  getBytes  (  )  )  ; 
dataGroups1  .  put  (  2  ,  "Dummy Value 2"  .  getBytes  (  )  )  ; 
signHelper  (  WORKER3  ,  15  ,  dataGroups1  ,  true  ,  "SHA256"  ,  "SHA256withRSA"  )  ; 
Map  <  Integer  ,  byte  [  ]  >  dataGroups2  =  new   LinkedHashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
dataGroups2  .  put  (  3  ,  "Dummy Value 3"  .  getBytes  (  )  )  ; 
dataGroups2  .  put  (  7  ,  "Dummy Value 4"  .  getBytes  (  )  )  ; 
dataGroups2  .  put  (  8  ,  "Dummy Value 5"  .  getBytes  (  )  )  ; 
dataGroups2  .  put  (  13  ,  "Dummy Value 6"  .  getBytes  (  )  )  ; 
signHelper  (  WORKER3  ,  16  ,  dataGroups2  ,  true  ,  "SHA256"  ,  "SHA256withRSA"  )  ; 
Map  <  Integer  ,  byte  [  ]  >  dataGroups3  =  new   LinkedHashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
dataGroups3  .  put  (  1  ,  "Dummy Value 7"  .  getBytes  (  )  )  ; 
dataGroups3  .  put  (  2  ,  "Dummy Value 8"  .  getBytes  (  )  )  ; 
signHelper  (  WORKER4  ,  17  ,  dataGroups3  ,  true  ,  "SHA512"  ,  "SHA512withRSA"  )  ; 
} 

public   void   test04MinRemainingCertVValidity  (  )  throws   Exception  { 
Map  <  Integer  ,  byte  [  ]  >  dataGroups1  =  new   LinkedHashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
dataGroups1  .  put  (  1  ,  digestHelper  (  "Dummy Value 1"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
dataGroups1  .  put  (  2  ,  digestHelper  (  "Dummy Value 2"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
signHelper  (  WORKER1  ,  12  ,  dataGroups1  ,  false  ,  "SHA256"  ,  "SHA256withRSA"  )  ; 
CertificateFactory   cf  =  CertificateFactory  .  getInstance  (  "X.509"  ,  "BC"  )  ; 
X509Certificate   cert  =  (  X509Certificate  )  cf  .  generateCertificate  (  new   ByteArrayInputStream  (  HardCodedCryptoToken  .  certbytes1  )  )  ; 
workerSession  .  uploadSignerCertificate  (  WORKER1  ,  cert  .  getEncoded  (  )  ,  GlobalConfiguration  .  SCOPE_GLOBAL  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1  ,  SignServerConstants  .  MINREMAININGCERTVALIDITY  ,  "6300"  )  ; 
workerSession  .  reloadConfiguration  (  WORKER1  )  ; 
boolean   thrown  =  false  ; 
try  { 
signHelper  (  WORKER1  ,  12  ,  dataGroups1  ,  false  ,  "SHA256"  ,  "SHA256withRSA"  )  ; 
}  catch  (  CryptoTokenOfflineException   e  )  { 
thrown  =  true  ; 
} 
assertTrue  (  thrown  )  ; 
} 

public   void   test04bMinRemainingCertVValidityWithSoftKeystore  (  )  throws   Exception  { 
Map  <  Integer  ,  byte  [  ]  >  dataGroups1  =  new   LinkedHashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
dataGroups1  .  put  (  1  ,  digestHelper  (  "Dummy Value 1"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
dataGroups1  .  put  (  2  ,  digestHelper  (  "Dummy Value 2"  .  getBytes  (  )  ,  "SHA256"  )  )  ; 
signHelper  (  WORKER1B  ,  12  ,  dataGroups1  ,  false  ,  "SHA256"  ,  "SHA256withRSA"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1B  ,  SignServerConstants  .  MINREMAININGCERTVALIDITY  ,  "6300"  )  ; 
workerSession  .  reloadConfiguration  (  WORKER1B  )  ; 
boolean   thrown  =  false  ; 
try  { 
signHelper  (  WORKER1B  ,  12  ,  dataGroups1  ,  false  ,  "SHA256"  ,  "SHA256withRSA"  )  ; 
}  catch  (  CryptoTokenOfflineException   e  )  { 
thrown  =  true  ; 
} 
assertTrue  (  thrown  )  ; 
} 





public   void   test04cRemainingValidity  (  )  throws   Exception  { 
Calendar   cal  =  Calendar  .  getInstance  (  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "CHECKCERTVALIDITY"  ,  "True"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "CHECKCERTPRIVATEKEYVALIDITY"  ,  "False"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "MINREMAININGCERTVALIDITY"  ,  "0"  )  ; 
Date   d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1C  )  ; 
assertNotNull  (  "test#1 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2030  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1C  )  ; 
assertNotNull  (  "test#2 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2025  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "CHECKCERTVALIDITY"  ,  "False"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "CHECKCERTPRIVATEKEYVALIDITY"  ,  "True"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1C  )  ; 
assertNotNull  (  "test#3 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2020  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1C  )  ; 
assertNotNull  (  "test#4 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2015  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "CHECKCERTVALIDITY"  ,  "True"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1C  )  ; 
assertNotNull  (  "test#5 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2020  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1C  )  ; 
assertNotNull  (  "test#6 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2015  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "CHECKCERTPRIVATEKEYVALIDITY"  ,  "False"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "MINREMAININGCERTVALIDITY"  ,  "3650"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1C  )  ; 
assertNotNull  (  "test#7 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2020  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1C  )  ; 
assertNotNull  (  "test#8 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2025  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1C  ,  "MINREMAININGCERTVALIDITY"  ,  "1460"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1C  )  ; 
assertNotNull  (  "test#9 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2026  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1C  )  ; 
assertNotNull  (  "test#10 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2025  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "CHECKCERTVALIDITY"  ,  "True"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "CHECKCERTPRIVATEKEYVALIDITY"  ,  "False"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "MINREMAININGCERTVALIDITY"  ,  "0"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1D  )  ; 
assertNotNull  (  "test#21 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2020  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
assertNotNull  (  "test#22 not null"  ,  d  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1D  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2015  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "CHECKCERTVALIDITY"  ,  "False"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "CHECKCERTPRIVATEKEYVALIDITY"  ,  "True"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1D  )  ; 
assertNotNull  (  "test#23 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2030  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1D  )  ; 
assertNotNull  (  "test#24 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2025  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "CHECKCERTVALIDITY"  ,  "True"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1D  )  ; 
assertNotNull  (  "test#25 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2020  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1D  )  ; 
assertNotNull  (  "test#26 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2025  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "CHECKCERTPRIVATEKEYVALIDITY"  ,  "False"  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "MINREMAININGCERTVALIDITY"  ,  "3650"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1D  )  ; 
assertNotNull  (  "test#27 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2010  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1D  )  ; 
assertNotNull  (  "test#28 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2015  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
workerSession  .  setWorkerProperty  (  WORKER1D  ,  "MINREMAININGCERTVALIDITY"  ,  "1460"  )  ; 
d  =  workerSession  .  getSigningValidityNotAfter  (  WORKER1D  )  ; 
assertNotNull  (  "test#29 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2016  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
d  =  workerSession  .  getSigningValidityNotBefore  (  WORKER1D  )  ; 
assertNotNull  (  "test#30 not null"  ,  d  )  ; 
cal  .  setTime  (  d  )  ; 
assertEquals  (  2015  ,  cal  .  get  (  Calendar  .  YEAR  )  )  ; 
} 

private   void   signHelper  (  int   workerId  ,  int   requestId  ,  Map  <  Integer  ,  byte  [  ]  >  dataGroups  ,  boolean   signerDoesHashing  ,  String   digestAlg  ,  String   sigAlg  )  throws   Exception  { 
Map  <  Integer  ,  byte  [  ]  >  expectedHashes  ; 
if  (  signerDoesHashing  )  { 
MessageDigest   d  =  MessageDigest  .  getInstance  (  digestAlg  ,  "BC"  )  ; 
expectedHashes  =  new   HashMap  <  Integer  ,  byte  [  ]  >  (  )  ; 
for  (  Map  .  Entry  <  Integer  ,  byte  [  ]  >  entry  :  dataGroups  .  entrySet  (  )  )  { 
expectedHashes  .  put  (  entry  .  getKey  (  )  ,  d  .  digest  (  entry  .  getValue  (  )  )  )  ; 
d  .  reset  (  )  ; 
} 
}  else  { 
expectedHashes  =  dataGroups  ; 
} 
SODSignResponse   res  =  (  SODSignResponse  )  workerSession  .  process  (  workerId  ,  new   SODSignRequest  (  requestId  ,  dataGroups  )  ,  new   RequestContext  (  )  )  ; 
assertNotNull  (  res  )  ; 
assertEquals  (  requestId  ,  res  .  getRequestID  (  )  )  ; 
Certificate   signercert  =  res  .  getSignerCertificate  (  )  ; 
assertNotNull  (  signercert  )  ; 
byte  [  ]  sodBytes  =  res  .  getProcessedData  (  )  ; 
SODFile   sod  =  new   SODFile  (  new   ByteArrayInputStream  (  sodBytes  )  )  ; 
boolean   verify  =  sod  .  checkDocSignature  (  signercert  )  ; 
assertTrue  (  "Signature verification"  ,  verify  )  ; 
Map  <  Integer  ,  byte  [  ]  >  actualDataGroupHashes  =  sod  .  getDataGroupHashes  (  )  ; 
assertEquals  (  expectedHashes  .  size  (  )  ,  actualDataGroupHashes  .  size  (  )  )  ; 
for  (  Map  .  Entry  <  Integer  ,  byte  [  ]  >  entry  :  actualDataGroupHashes  .  entrySet  (  )  )  { 
assertTrue  (  "DG"  +  entry  .  getKey  (  )  ,  Arrays  .  equals  (  expectedHashes  .  get  (  entry  .  getKey  (  )  )  ,  entry  .  getValue  (  )  )  )  ; 
} 
assertEquals  (  digestAlg  ,  sod  .  getDigestAlgorithm  (  )  )  ; 
assertEquals  (  sigAlg  ,  sod  .  getDigestEncryptionAlgorithm  (  )  )  ; 
} 

private   byte  [  ]  digestHelper  (  byte  [  ]  data  ,  String   digestAlgorithm  )  throws   NoSuchAlgorithmException  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  digestAlgorithm  )  ; 
return   md  .  digest  (  data  )  ; 
} 




public   void   test05GetStatus  (  )  throws   Exception  { 
SignerStatus   stat  =  (  SignerStatus  )  workerSession  .  getStatus  (  7897  )  ; 
assertTrue  (  stat  .  getTokenStatus  (  )  ==  SignerStatus  .  STATUS_ACTIVE  )  ; 
} 

public   void   test99TearDownDatabase  (  )  throws   Exception  { 
removeWorker  (  WORKER1  )  ; 
removeWorker  (  WORKER2  )  ; 
removeWorker  (  WORKER3  )  ; 
removeWorker  (  WORKER4  )  ; 
removeWorker  (  WORKER5  )  ; 
removeWorker  (  WORKER1B  )  ; 
removeWorker  (  WORKER1C  )  ; 
removeWorker  (  WORKER1D  )  ; 
} 
} 

