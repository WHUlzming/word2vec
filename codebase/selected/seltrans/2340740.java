package   com  .  volantis  .  mcs  .  accessors  .  xml  ; 

import   com  .  volantis  .  devrep  .  device  .  api  .  xml  .  DeviceSchemas  ; 
import   com  .  volantis  .  devrep  .  localization  .  LocalizationFactory  ; 
import   com  .  volantis  .  devrep  .  repository  .  accessors  .  DeletionFilter  ; 
import   com  .  volantis  .  devrep  .  repository  .  accessors  .  EclipseDeviceRepository  ; 
import   com  .  volantis  .  devrep  .  repository  .  accessors  .  MDPRArchiveAccessor  ; 
import   com  .  volantis  .  devrep  .  repository  .  api  .  devices  .  DeviceRepositorySchemaConstants  ; 
import   com  .  volantis  .  devrep  .  repository  .  impl  .  testtools  .  device  .  TestDeviceRepositoryCreator  ; 
import   com  .  volantis  .  mcs  .  repository  .  RepositoryException  ; 
import   com  .  volantis  .  synergetics  .  cornerstone  .  utilities  .  xml  .  jaxp  .  TestTransformerMetaFactory  ; 
import   com  .  volantis  .  synergetics  .  cornerstone  .  utilities  .  xml  .  jaxp  .  TransformerMetaFactory  ; 
import   com  .  volantis  .  synergetics  .  io  .  IOUtils  ; 
import   com  .  volantis  .  synergetics  .  log  .  LogDispatcher  ; 
import   com  .  volantis  .  synergetics  .  testtools  .  JDOMUtils  ; 
import   com  .  volantis  .  synergetics  .  testtools  .  TestCaseAbstract  ; 
import   com  .  volantis  .  synergetics  .  testtools  .  io  .  ResourceTemporaryFileCreator  ; 
import   com  .  volantis  .  synergetics  .  testtools  .  io  .  TemporaryFileExecutor  ; 
import   com  .  volantis  .  synergetics  .  testtools  .  io  .  TemporaryFileManager  ; 
import   com  .  volantis  .  xml  .  schema  .  W3CSchemata  ; 
import   junitx  .  util  .  PrivateAccessor  ; 
import   org  .  apache  .  log4j  .  BasicConfigurator  ; 
import   org  .  apache  .  log4j  .  Category  ; 
import   org  .  apache  .  log4j  .  Level  ; 
import   org  .  custommonkey  .  xmlunit  .  XMLUnit  ; 
import   org  .  jdom  .  Document  ; 
import   org  .  jdom  .  Element  ; 
import   org  .  jdom  .  Namespace  ; 
import   org  .  jdom  .  filter  .  Filter  ; 
import   org  .  jdom  .  input  .  DefaultJDOMFactory  ; 
import   org  .  jdom  .  input  .  JDOMFactory  ; 
import   org  .  jdom  .  input  .  SAXBuilder  ; 
import   org  .  jdom  .  output  .  XMLOutputter  ; 
import   org  .  xml  .  sax  .  XMLFilter  ; 
import   java  .  io  .  BufferedInputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  StringReader  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Enumeration  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Properties  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipFile  ; 

public   class   EclipseDeviceRepositoryTestCase   extends   TestCaseAbstract  { 




private   static   final   LogDispatcher   logger  =  LocalizationFactory  .  createLogger  (  EclipseDeviceRepositoryTestCase  .  class  )  ; 




private   static   JDOMFactory   jdomFactory  =  new   DefaultJDOMFactory  (  )  ; 

public   static   final   String   DEVICE_NAME_ATTRIBUTE  =  "name"  ; 




private   static   final   DeletionFilter   STANDARD_ELEMENT_FILTER  =  new   DeletionFilter  (  new   DeletionFilter  .  NodeIdentifier  [  ]  {  new   DeletionFilter  .  NodeIdentifier  (  DeviceRepositorySchemaConstants  .  STANDARD_ELEMENT_NAME  ,  DeviceSchemas  .  DEVICE_CURRENT  .  getNamespaceURL  (  )  )  ,  new   DeletionFilter  .  NodeIdentifier  (  DeviceRepositorySchemaConstants  .  STANDARD_ELEMENT_NAME  ,  DeviceSchemas  .  IDENTIFICATION_CURRENT  .  getNamespaceURL  (  )  )  ,  new   DeletionFilter  .  NodeIdentifier  (  DeviceRepositorySchemaConstants  .  STANDARD_ELEMENT_NAME  ,  DeviceSchemas  .  TAC_IDENTIFICATION_CURRENT  .  getNamespaceURL  (  )  )  }  )  ; 




private   static   final   TransformerMetaFactory   transformerMetaFactory  =  new   TestTransformerMetaFactory  (  )  ; 







private   static   InputStream   getHierarchy  (  String   resource  )  throws   Exception  { 
InputStream   stream  =  EclipseDeviceRepositoryTestCase  .  class  .  getResourceAsStream  (  resource  )  ; 
if  (  stream  ==  null  )  { 
throw   new   Exception  (  resource  +  " not found"  )  ; 
} 
return   stream  ; 
} 







private   static   InputStream   getHierarchy  (  )  throws   Exception  { 
return   getHierarchy  (  "hierarchy-architecture.xml"  )  ; 
} 








private   static   EclipseDeviceRepository   updateHierarchy  (  InputStream   inputHierarchy  ,  String   filename  )  throws   Exception  { 
return   updateHierarchy  (  inputHierarchy  ,  filename  ,  null  )  ; 
} 








private   static   EclipseDeviceRepository   updateHierarchy  (  InputStream   inputHierarchy  ,  String   filename  ,  XMLFilter   filter  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  filename  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  filter  )  ; 
Document   document  =  accessor  .  createNewDocument  (  inputHierarchy  )  ; 
PrivateAccessor  .  setField  (  accessor  ,  "xmlHierarchyDocument"  ,  document  )  ; 
return   accessor  ; 
} 

private   static   EclipseDeviceRepository   updateHierarchy  (  final   InputStream   inputHierarchy  )  throws   Exception  { 
final   EclipseDeviceRepository  [  ]  accessor  =  new   EclipseDeviceRepository  [  1  ]  ; 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   temporaryFile  )  throws   Exception  { 
accessor  [  0  ]  =  updateHierarchy  (  inputHierarchy  ,  temporaryFile  .  getPath  (  )  )  ; 
} 
}  )  ; 
return   accessor  [  0  ]  ; 
} 











void   writeRepository  (  EclipseDeviceRepository   accessor  )  throws   Exception  { 
accessor  .  writeHierarchy  (  )  ; 
accessor  .  writeIdentifiers  (  )  ; 
accessor  .  writeDefinitions  (  )  ; 
accessor  .  writeProperties  (  )  ; 
accessor  .  writeTACs  (  )  ; 
accessor  .  saveRepositoryArchive  (  )  ; 
} 





protected   void   setUp  (  )  throws   Exception  { 
BasicConfigurator  .  configure  (  )  ; 
Category  .  getRoot  (  )  .  setLevel  (  Level  .  OFF  )  ; 
XMLUnit  .  setControlParser  (  "com.volantis.xml.xerces.jaxp.DocumentBuilderFactoryImpl"  )  ; 
XMLUnit  .  setTestParser  (  "com.volantis.xml.xerces.jaxp.DocumentBuilderFactoryImpl"  )  ; 
XMLUnit  .  setSAXParserFactory  (  "com.volantis.xml.xerces.jaxp.SAXParserFactoryImpl"  )  ; 
XMLUnit  .  setTransformerFactory  (  "com.volantis.xml.xalan.processor.TransformerFactoryImpl"  )  ; 
} 





protected   void   tearDown  (  )  throws   Exception  { 
Category  .  shutdown  (  )  ; 
} 




public   void   testRepositoryRead  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   temporaryFile  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  temporaryFile  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
assertEquals  (  "Root device name should match"  ,  "Master"  ,  accessor  .  getRootDeviceName  (  )  )  ; 
assertEquals  (  "Root device number of children should match"  ,  6  ,  accessor  .  getChildDeviceNames  (  "Master"  )  .  size  (  )  )  ; 
} 
}  )  ; 
} 




public   void   testRepositoryReadAndWrite  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
verifyHierarchy  (  accessor  )  ; 
} 
}  )  ; 
} 




public   void   testWriting  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
} 
}  )  ; 
} 




public   void   testUpdateHierarchy  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
Document   hierarchyDoc  =  (  Document  )  PrivateAccessor  .  getField  (  accessor  ,  "xmlHierarchyDocument"  )  ; 
Element   root  =  hierarchyDoc  .  getRootElement  (  )  ; 
Element   master  =  root  .  getChild  (  "device"  ,  root  .  getNamespace  (  )  )  ; 
Element   newDevice  =  new   Element  (  "device"  ,  root  .  getNamespace  (  )  )  ; 
newDevice  .  setAttribute  (  "name"  ,  "FakeDevice"  )  ; 
master  .  addContent  (  newDevice  )  ; 
accessor  .  writeHierarchy  (  )  ; 
accessor  .  saveRepositoryArchive  (  )  ; 
ZipArchive   archive  =  (  ZipArchive  )  PrivateAccessor  .  getField  (  accessor  ,  "repositoryArchive"  )  ; 
InputStream   input  =  archive  .  getInputFrom  (  "hierarchy.xml"  )  ; 
if  (  input  !=  null  )  { 
hierarchyDoc  =  accessor  .  createNewDocument  (  new   BufferedInputStream  (  input  )  )  ; 
} 
root  =  hierarchyDoc  .  getRootElement  (  )  ; 
master  =  root  .  getChild  (  "device"  ,  root  .  getNamespace  (  )  )  ; 
List   children  =  master  .  getChildren  (  )  ; 
Element   element  =  (  Element  )  children  .  get  (  children  .  size  (  )  -  1  )  ; 
assertEquals  (  "Expected new FakeDevice device."  ,  "FakeDevice"  ,  element  .  getAttributeValue  (  "name"  )  )  ; 
} 
}  )  ; 
} 




public   void   testWriteDefinitions  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
Element   root  =  accessor  .  getDevicePolicyDefinitions  (  )  .  getRootElement  (  )  ; 
List   children  =  root  .  getChildren  (  )  ; 
Element   customCat  =  null  ; 
for  (  int   i  =  0  ;  i  <  children  .  size  (  )  &&  customCat  ==  null  ;  i  ++  )  { 
Element   child  =  (  Element  )  children  .  get  (  i  )  ; 
if  (  child  .  getName  (  )  .  equals  (  "category"  )  )  { 
String   nameAtt  =  child  .  getAttributeValue  (  "name"  )  ; 
if  (  nameAtt  !=  null  &&  nameAtt  .  equals  (  "custom"  )  )  { 
customCat  =  child  ; 
} 
} 
} 
if  (  customCat  ==  null  )  { 
customCat  =  new   Element  (  "category"  ,  root  .  getNamespace  (  )  )  ; 
customCat  .  setAttribute  (  "name"  ,  "custom"  )  ; 
root  .  addContent  (  customCat  )  ; 
} 
Element   policy  =  new   Element  (  "policy"  ,  root  .  getNamespace  (  )  )  ; 
policy  .  setAttribute  (  "name"  ,  "wibble"  )  ; 
Element   type  =  new   Element  (  "type"  ,  root  .  getNamespace  (  )  )  ; 
policy  .  addContent  (  type  )  ; 
Element   bool  =  new   Element  (  "boolean"  ,  root  .  getNamespace  (  )  )  ; 
type  .  addContent  (  bool  )  ; 
customCat  .  addContent  (  policy  )  ; 
accessor  .  writeDefinitions  (  )  ; 
accessor  .  saveRepositoryArchive  (  )  ; 
boolean   savedNewDefinition  =  false  ; 
root  =  accessor  .  getDevicePolicyDefinitions  (  )  .  getRootElement  (  )  ; 
children  =  root  .  getChildren  (  )  ; 
for  (  int   i  =  0  ;  i  <  children  .  size  (  )  ;  i  ++  )  { 
Element   element  =  (  Element  )  children  .  get  (  i  )  ; 
if  (  element  .  getName  (  )  .  equals  (  "category"  )  )  { 
String   name  =  element  .  getAttributeValue  (  "name"  )  ; 
if  (  "custom"  .  equals  (  name  )  )  { 
List   custChildren  =  element  .  getChildren  (  )  ; 
for  (  int   j  =  0  ;  j  <  custChildren  .  size  (  )  ;  j  ++  )  { 
Element   ele  =  (  Element  )  custChildren  .  get  (  j  )  ; 
if  (  ele  .  getName  (  )  .  equals  (  "policy"  )  )  { 
String   polName  =  ele  .  getAttributeValue  (  "name"  )  ; 
if  (  "wibble"  .  equals  (  polName  )  )  { 
savedNewDefinition  =  true  ; 
} 
} 
} 
} 
} 
} 
assertTrue  (  "New policy definition was not saved and retrieved."  ,  savedNewDefinition  )  ; 
} 
}  )  ; 
} 




public   void   testReadDefinitions  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
Element   root  =  accessor  .  getDevicePolicyDefinitions  (  )  .  getRootElement  (  )  ; 
assertNotNull  (  "New policy definition was not saved and retrieved."  ,  root  )  ; 
} 
}  )  ; 
} 





public   void   testUpdateDevice  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
Map   devices  =  new   HashMap  (  )  ; 
Element   device  =  accessor  .  retrieveDeviceElement  (  "PC"  )  ; 
Element   policy  =  retrievePolicy  (  device  ,  "rendermode"  )  ; 
policy  .  setAttribute  (  "value"  ,  "wibble"  )  ; 
devices  .  put  (  "PC"  ,  device  )  ; 
accessor  .  writeDeviceElements  (  devices  )  ; 
accessor  .  saveRepositoryArchive  (  )  ; 
device  =  accessor  .  retrieveDeviceElement  (  "PC"  )  ; 
policy  =  retrievePolicy  (  device  ,  "rendermode"  )  ; 
assertEquals  (  "Should have seen a different value for the policy."  ,  "wibble"  ,  policy  .  getAttributeValue  (  "value"  )  )  ; 
} 
}  )  ; 
} 






public   void   testWriteDeviceElementsNoCustomProperties  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
ZipFile   beforeZip  =  new   ZipFile  (  repository  )  ; 
try  { 
assertNull  (  "standard device should not exist"  ,  beforeZip  .  getEntry  (  "standard/devices/NewDevice.xml"  )  )  ; 
assertNull  (  "custom device should not exist"  ,  beforeZip  .  getEntry  (  "custom/devices/NewDevice.xml"  )  )  ; 
}  finally  { 
beforeZip  .  close  (  )  ; 
} 
String   policies  =  "<policy name=\"width\" value=\"999\"/>"  +  "<policy name=\"ssl\" value=\"true\"/>"  ; 
String   newDevice  =  createDeviceElementString  (  policies  )  ; 
Map   deviceMap  =  new   HashMap  (  )  ; 
deviceMap  .  put  (  "NewDevice"  ,  JDOMUtils  .  createElement  (  newDevice  )  )  ; 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
accessor  .  writeDeviceElements  (  deviceMap  )  ; 
accessor  .  saveRepositoryArchive  (  )  ; 
ZipFile   afterZip  =  new   ZipFile  (  repository  )  ; 
try  { 
assertNotNull  (  "standard device should exist"  ,  afterZip  .  getEntry  (  "standard/devices/NewDevice.xml"  )  )  ; 
assertNull  (  "custom device should NOT exist"  ,  afterZip  .  getEntry  (  "custom/devices/NewDevice.xml"  )  )  ; 
Element   device  =  parseFile  (  afterZip  ,  "standard/devices/NewDevice.xml"  )  ; 
assertXMLEquals  (  "device element not as expected"  ,  newDevice  ,  JDOMUtils  .  convertToString  (  device  )  )  ; 
}  finally  { 
afterZip  .  close  (  )  ; 
} 
} 
}  )  ; 
} 






public   void   testWriteDeviceElementsOnlyCustomProperties  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
ZipFile   beforeZip  =  new   ZipFile  (  repository  )  ; 
try  { 
assertNull  (  "standard device should not exist"  ,  beforeZip  .  getEntry  (  "standard/devices/NewDevice.xml"  )  )  ; 
assertNull  (  "custom device should not exist"  ,  beforeZip  .  getEntry  (  "custom/devices/NewDevice.xml"  )  )  ; 
}  finally  { 
beforeZip  .  close  (  )  ; 
} 
String   policies  =  "<policy name=\"custom.width\" value=\"999\"/>"  +  "<policy name=\"custom.ssl\" value=\"true\"/>"  ; 
String   newDevice  =  createDeviceElementString  (  policies  )  ; 
Map   deviceMap  =  new   HashMap  (  )  ; 
deviceMap  .  put  (  "NewDevice"  ,  JDOMUtils  .  createElement  (  newDevice  )  )  ; 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
accessor  .  writeDeviceElements  (  deviceMap  )  ; 
accessor  .  saveRepositoryArchive  (  )  ; 
ZipFile   afterZip  =  new   ZipFile  (  repository  )  ; 
try  { 
assertNotNull  (  "standard device should exist"  ,  afterZip  .  getEntry  (  "standard/devices/NewDevice.xml"  )  )  ; 
assertNotNull  (  "custom device should exist"  ,  afterZip  .  getEntry  (  "custom/devices/NewDevice.xml"  )  )  ; 
Element   standardDevice  =  parseFile  (  afterZip  ,  "standard/devices/NewDevice.xml"  )  ; 
Element   expectedStandard  =  JDOMUtils  .  createDocument  (  newDevice  )  .  getRootElement  (  )  ; 
Namespace   ns  =  expectedStandard  .  getNamespace  (  )  ; 
expectedStandard  .  getChild  (  "policies"  ,  ns  )  .  removeChildren  (  "policy"  ,  ns  )  ; 
assertXMLEquals  (  "NewDevice device element not as expected"  ,  expectedStandard  ,  standardDevice  )  ; 
Element   customDevice  =  parseFile  (  afterZip  ,  "custom/devices/NewDevice.xml"  )  ; 
assertXMLEquals  (  "NewDevice device element not as expected"  ,  newDevice  ,  customDevice  )  ; 
}  finally  { 
afterZip  .  close  (  )  ; 
} 
} 
}  )  ; 
} 






public   void   testWriteDeviceElementsStandardAndCustomProperties  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
ZipFile   beforeZip  =  new   ZipFile  (  repository  )  ; 
try  { 
assertNull  (  "standard NewDevice device should not exist"  ,  beforeZip  .  getEntry  (  "standard/devices/NewDevice.xml"  )  )  ; 
assertNull  (  "custom NewDevice device should not exist"  ,  beforeZip  .  getEntry  (  "custom/devices/NewDevice.xml"  )  )  ; 
}  finally  { 
beforeZip  .  close  (  )  ; 
} 
String   policies  =  "<policy name=\"width\" value=\"999\"/>"  +  "<policy name=\"custom.ssl\" value=\"true\"/>"  ; 
Map   deviceMap  =  new   HashMap  (  )  ; 
String   newDevice  =  createDeviceElementString  (  policies  )  ; 
deviceMap  .  put  (  "NewDevice"  ,  JDOMUtils  .  createElement  (  newDevice  )  )  ; 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
accessor  .  writeDeviceElements  (  deviceMap  )  ; 
accessor  .  saveRepositoryArchive  (  )  ; 
ZipFile   afterZip  =  new   ZipFile  (  repository  )  ; 
try  { 
assertNotNull  (  "standard NewDevice device should exist"  ,  afterZip  .  getEntry  (  "standard/devices/NewDevice.xml"  )  )  ; 
assertNotNull  (  "custom NewDevice device should exist"  ,  afterZip  .  getEntry  (  "custom/devices/NewDevice.xml"  )  )  ; 
Element   standardNewDevice  =  parseFile  (  afterZip  ,  "standard/devices/NewDevice.xml"  )  ; 
String   standardPolices  =  "<policy name=\"width\" value=\"999\"/>"  ; 
String   expectedStandard  =  createDeviceElementString  (  standardPolices  )  ; 
assertXMLEquals  (  "standard device element not as expected"  ,  expectedStandard  ,  standardNewDevice  )  ; 
Element   customNewDevice  =  parseFile  (  afterZip  ,  "custom/devices/NewDevice.xml"  )  ; 
String   customPolicies  =  "<policy name=\"custom.ssl\" value=\"true\"/>"  ; 
String   expectedCustom  =  createDeviceElementString  (  customPolicies  )  ; 
assertXMLEquals  (  "standard device element not as expected"  ,  expectedCustom  ,  customNewDevice  )  ; 
}  finally  { 
afterZip  .  close  (  )  ; 
} 
} 
}  )  ; 
} 







private   String   createDeviceElementString  (  String   policies  )  { 
String   newDevice  =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"  +  "<device xmlns=\"http://www.volantis.com/xmlns/device-repository/device\" "  +  "xmlns:xsi=\""  +  W3CSchemata  .  XSI_NAMESPACE  +  "\" "  +  "xsi:schemaLocation=\"http://www.volantis.com/xmlns/device-repository/device "  +  "http://www.volantis.com/schema/device-repository/v3.0/device.xsd\">"  +  "<policies>"  +  policies  +  "</policies>"  +  "</device>"  ; 
return   newDevice  ; 
} 





public   void   testAddCustomPolicy  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
Element   device  =  accessor  .  retrieveDeviceElement  (  "PC"  )  ; 
Element   policies  =  device  .  getChild  (  "policies"  ,  device  .  getNamespace  (  )  )  ; 
Element   newPolicy  =  new   Element  (  "policy"  ,  device  .  getNamespace  (  )  )  ; 
newPolicy  .  setAttribute  (  "name"  ,  "custom.mypolicy"  )  ; 
newPolicy  .  setAttribute  (  "value"  ,  "xyz"  )  ; 
policies  .  addContent  (  newPolicy  )  ; 
Map   devices  =  new   HashMap  (  )  ; 
devices  .  put  (  "PC"  ,  device  )  ; 
ArrayList   list  =  new   ArrayList  (  1  )  ; 
list  .  add  (  device  )  ; 
accessor  .  writeDeviceElements  (  devices  )  ; 
accessor  .  saveRepositoryArchive  (  )  ; 
ZipArchive   archive  =  (  ZipArchive  )  PrivateAccessor  .  getField  (  accessor  ,  "repositoryArchive"  )  ; 
device  =  retrieveCustomDeviceElement  (  accessor  ,  archive  ,  "PC"  )  ; 
Element   policy  =  retrievePolicy  (  device  ,  "custom.mypolicy"  )  ; 
assertEquals  (  "Should have seen a different value for the policy."  ,  "xyz"  ,  policy  .  getAttributeValue  (  "value"  )  )  ; 
device  =  retrieveStandardDeviceElement  (  accessor  ,  archive  ,  "PC"  )  ; 
policy  =  retrievePolicy  (  device  ,  "custom.mypolicy"  )  ; 
assertNull  (  "policy should not have been found."  ,  policy  )  ; 
device  =  accessor  .  retrieveDeviceElement  (  "PC"  )  ; 
policy  =  retrievePolicy  (  device  ,  "custom.mypolicy"  )  ; 
assertEquals  (  "Should have seen a different value for the policy."  ,  "xyz"  ,  policy  .  getAttributeValue  (  "value"  )  )  ; 
} 
}  )  ; 
} 




public   void   testMergeDefinitionDocuments  (  )  throws   Throwable  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
Document   custom  =  new   Document  (  )  ; 
Element   root  =  new   Element  (  "definitions"  )  ; 
custom  .  setRootElement  (  root  )  ; 
Element   typesElement  =  new   Element  (  "types"  )  ; 
Element   type  =  new   Element  (  "type"  )  ; 
typesElement  .  addContent  (  type  )  ; 
root  .  addContent  (  typesElement  )  ; 
Document   master  =  (  Document  )  custom  .  clone  (  )  ; 
Element   masterRoot  =  master  .  getRootElement  (  )  ; 
Element   browserCat  =  new   Element  (  "category"  )  ; 
browserCat  .  setAttribute  (  "name"  ,  "browser"  )  ; 
masterRoot  .  addContent  (  browserCat  )  ; 
Element   ergoCat  =  new   Element  (  "category"  )  ; 
ergoCat  .  setAttribute  (  "name"  ,  "ergonomics"  )  ; 
masterRoot  .  addContent  (  ergoCat  )  ; 
Element   customCat  =  new   Element  (  "category"  )  ; 
customCat  .  setAttribute  (  "name"  ,  "custom"  )  ; 
root  .  addContent  (  customCat  )  ; 
assertTrue  (  "master definition should have 3 child elements"  ,  masterRoot  .  getChildren  (  )  .  size  (  )  ==  3  )  ; 
Class  [  ]  types  =  {  Document  .  class  ,  Document  .  class  }  ; 
Object  [  ]  params  =  {  master  ,  custom  }  ; 
try  { 
PrivateAccessor  .  invoke  (  accessor  ,  "mergeDefinitionDocuments"  ,  types  ,  params  )  ; 
}  catch  (  Throwable   throwable  )  { 
throw   new   Exception  (  throwable  )  ; 
} 
assertTrue  (  "master definition should now have 4 child elements"  ,  masterRoot  .  getChildren  (  )  .  size  (  )  ==  4  )  ; 
List   children  =  masterRoot  .  getChildren  (  )  ; 
Element   child1  =  (  Element  )  children  .  get  (  0  )  ; 
assertEquals  (  "Unexpected child element."  ,  "types"  ,  child1  .  getName  (  )  )  ; 
Element   child2  =  (  Element  )  children  .  get  (  1  )  ; 
String   name  =  child2  .  getAttributeValue  (  "name"  )  ; 
assertEquals  (  "Unexpected child element."  ,  "browser"  ,  name  )  ; 
Element   child3  =  (  Element  )  children  .  get  (  2  )  ; 
name  =  child3  .  getAttributeValue  (  "name"  )  ; 
assertEquals  (  "Unexpected child element."  ,  "ergonomics"  ,  name  )  ; 
Element   child4  =  (  Element  )  children  .  get  (  3  )  ; 
name  =  child4  .  getAttributeValue  (  "name"  )  ; 
assertEquals  (  "Unexpected child element."  ,  "custom"  ,  name  )  ; 
} 
}  )  ; 
} 




public   void   testMergeDeviceDocuments  (  )  throws   Throwable  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
Document   doc1  =  new   Document  (  )  ; 
Element   device1  =  new   Element  (  "device"  )  ; 
doc1  .  setRootElement  (  device1  )  ; 
Element   policies1  =  new   Element  (  "policies"  )  ; 
device1  .  addContent  (  policies1  )  ; 
Element   policy1a  =  new   Element  (  "policy"  )  ; 
policy1a  .  setAttribute  (  "name"  ,  "abc"  )  ; 
policy1a  .  setAttribute  (  "value"  ,  "123"  )  ; 
Element   policy1b  =  new   Element  (  "policy"  )  ; 
policy1b  .  setAttribute  (  "name"  ,  "def"  )  ; 
policy1b  .  setAttribute  (  "value"  ,  "456"  )  ; 
policies1  .  addContent  (  policy1a  )  ; 
policies1  .  addContent  (  policy1b  )  ; 
Document   doc2  =  new   Document  (  )  ; 
Element   device2  =  new   Element  (  "device"  )  ; 
doc2  .  setRootElement  (  device2  )  ; 
Element   policies2  =  new   Element  (  "policies"  )  ; 
device2  .  addContent  (  policies2  )  ; 
Element   policy2a  =  new   Element  (  "policy"  )  ; 
policy2a  .  setAttribute  (  "name"  ,  "ghi"  )  ; 
policy2a  .  setAttribute  (  "value"  ,  "789"  )  ; 
policies2  .  addContent  (  policy2a  )  ; 
assertTrue  (  "doc1 policies element should have 2 child policies"  ,  policies1  .  getChildren  (  )  .  size  (  )  ==  2  )  ; 
Class  [  ]  types  =  {  Document  .  class  ,  Document  .  class  }  ; 
Object  [  ]  params  =  {  doc1  ,  doc2  }  ; 
try  { 
PrivateAccessor  .  invoke  (  accessor  ,  "mergeDeviceDocuments"  ,  types  ,  params  )  ; 
}  catch  (  Throwable   throwable  )  { 
throw   new   Exception  (  throwable  )  ; 
} 
assertTrue  (  "doc1 policies element should have 3 child policies"  ,  policies1  .  getChildren  (  )  .  size  (  )  ==  3  )  ; 
} 
}  )  ; 
} 











protected   Element   retrievePolicy  (  Element   deviceElement  ,  String   policyName  )  throws   Exception  { 
Element   policies  =  deviceElement  .  getChild  (  "policies"  ,  deviceElement  .  getNamespace  (  )  )  ; 
Element   policy  =  null  ; 
Iterator   iterator  =  policies  .  getChildren  (  )  .  iterator  (  )  ; 
while  (  iterator  .  hasNext  (  )  &&  policy  ==  null  )  { 
Element   policyElement  =  (  Element  )  iterator  .  next  (  )  ; 
String   policyElementName  =  policyElement  .  getAttributeValue  (  DEVICE_NAME_ATTRIBUTE  )  ; 
if  (  policyElementName  .  equals  (  policyName  )  )  { 
policy  =  policyElement  ; 
} 
} 
return   policy  ; 
} 










protected   Element   retrieveCustomDeviceElement  (  EclipseDeviceRepository   accessor  ,  ZipArchive   archive  ,  String   deviceName  )  throws   RepositoryException  { 
Document   customDocument  =  null  ; 
InputStream   customInput  =  archive  .  getInputFrom  (  "custom/devices/"  +  deviceName  +  ".xml"  )  ; 
if  (  customInput  !=  null  )  { 
customDocument  =  accessor  .  createNewDocument  (  customInput  )  ; 
} 
return   customDocument  .  getRootElement  (  )  ; 
} 










protected   Element   retrieveStandardDeviceElement  (  EclipseDeviceRepository   accessor  ,  ZipArchive   archive  ,  String   deviceName  )  throws   RepositoryException  { 
Document   customDocument  =  null  ; 
InputStream   customInput  =  archive  .  getInputFrom  (  "standard/devices/"  +  deviceName  +  ".xml"  )  ; 
if  (  customInput  !=  null  )  { 
customDocument  =  accessor  .  createNewDocument  (  customInput  )  ; 
} 
return   customDocument  .  getRootElement  (  )  ; 
} 




public   void   testWritingReadonlyRepository  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  updateHierarchy  (  getHierarchy  (  )  ,  repository  .  getPath  (  )  )  ; 
repository  .  setReadOnly  (  )  ; 
writeRepository  (  accessor  )  ; 
assertTrue  (  "Repository should exist"  ,  repository  .  exists  (  )  )  ; 
File   backupRepository  =  new   File  (  repository  .  getPath  (  )  +  ".bak"  )  ; 
assertFalse  (  "Backup repository should NOT exist"  ,  backupRepository  .  exists  (  )  )  ; 
} 
}  )  ; 
} 







public   void   noTestWriteCustomProperties  (  )  throws   Throwable  { 
Category  .  getRoot  (  )  .  setLevel  (  Level  .  DEBUG  )  ; 
final   String   customPolicyProps  =  "custom/policies/resources/policies.properties"  ; 
final   String   policyPrefix  =  "policy."  +  EclipseDeviceRepository  .  getCustomPolicyNamePrefix  (  )  ; 
final   String   customNameKey  =  policyPrefix  +  "newCustom.name"  ; 
final   String   customNameValue  =  "newCustomPolicy"  ; 
final   String   customDescKey  =  policyPrefix  +  "newCustom.description"  ; 
final   String   customDescValue  =  "Some helpful description"  ; 
final   Properties   properties  =  new   Properties  (  )  ; 
properties  .  setProperty  (  customNameKey  ,  customNameValue  )  ; 
properties  .  setProperty  (  customDescKey  ,  customDescValue  )  ; 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
MDPRArchiveAccessor   archiveAccessor  =  new   MDPRArchiveAccessor  (  repository  .  getPath  (  )  ,  transformerMetaFactory  )  ; 
final   EclipseDeviceRepository   writeRepository  =  new   EclipseDeviceRepository  (  archiveAccessor  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
boolean   existsBefore  =  archiveAccessor  .  getArchive  (  )  .  exists  (  customPolicyProps  )  ; 
assertFalse  (  "Custom policies.properties should not exist"  ,  existsBefore  )  ; 
PrivateAccessor  .  setField  (  writeRepository  ,  "properties"  ,  properties  )  ; 
writeRepository  .  writeProperties  (  )  ; 
if  (  logger  .  isDebugEnabled  (  )  )  { 
dumpFile  (  repository  ,  new   File  (  "/tmp/r1.zip"  )  )  ; 
} 
writeRepository  .  saveRepositoryArchive  (  )  ; 
if  (  logger  .  isDebugEnabled  (  )  )  { 
dumpFile  (  repository  ,  new   File  (  "/tmp/r2.zip"  )  )  ; 
} 
final   EclipseDeviceRepository   readRepository  =  new   EclipseDeviceRepository  (  archiveAccessor  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
boolean   existsAfter  =  archiveAccessor  .  getArchive  (  )  .  exists  (  customPolicyProps  )  ; 
assertTrue  (  "Custom policies.properties should exist"  ,  existsAfter  )  ; 
String   actualPath  =  (  String  )  PrivateAccessor  .  getField  (  readRepository  ,  "customPropertiesPath"  )  ; 
assertEquals  (  "Custom properties file should be the default"  ,  customPolicyProps  ,  actualPath  )  ; 
final   Properties   propertiesRead  =  readRepository  .  getProperties  (  )  ; 
String   name  =  (  String  )  propertiesRead  .  get  (  customNameKey  )  ; 
assertEquals  (  "Custom name should be the same"  ,  customNameValue  ,  name  )  ; 
String   desc  =  (  String  )  propertiesRead  .  get  (  customDescKey  )  ; 
assertEquals  (  "Custom description should be the same"  ,  customDescValue  ,  desc  )  ; 
} 
}  )  ; 
} 











private   void   dumpFile  (  File   repository  ,  File   copy  )  { 
try  { 
if  (  copy  .  exists  (  )  &&  !  copy  .  delete  (  )  )  { 
throw   new   RuntimeException  (  "can't delete copy: "  +  copy  )  ; 
} 
printFile  (  "Real Archive File"  ,  repository  )  ; 
new   ZipArchive  (  repository  .  getPath  (  )  )  ; 
IOUtils  .  copyFiles  (  repository  ,  copy  )  ; 
printFile  (  "Copy Archive File"  ,  copy  )  ; 
new   ZipArchive  (  copy  .  getPath  (  )  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 









private   void   printFile  (  String   description  ,  File   file  )  { 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  description  +  ":"  +  file  +  ", "  +  "Exists:"  +  file  .  exists  (  )  +  ", "  +  "Length:"  +  file  .  length  (  )  +  ", "  +  "Last Modified: "  +  new   Date  (  file  .  lastModified  (  )  )  )  ; 
} 
} 




public   void   testGetFallbackDeviceName  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
assertEquals  (  "Fallback should match"  ,  null  ,  accessor  .  getFallbackDeviceName  (  null  )  )  ; 
assertEquals  (  "Fallback should match"  ,  "Master"  ,  accessor  .  getFallbackDeviceName  (  "Voice"  )  )  ; 
assertEquals  (  "Fallback should match"  ,  null  ,  accessor  .  getFallbackDeviceName  (  "xxx"  )  )  ; 
assertEquals  (  "Fallback should match"  ,  "WAP-Handset"  ,  accessor  .  getFallbackDeviceName  (  "Nokia-WAP"  )  )  ; 
} 
}  )  ; 
} 




public   void   testDeviceExistsInHierarchy  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
assertTrue  (  accessor  .  deviceExists  (  "Master"  )  )  ; 
assertFalse  (  accessor  .  deviceExists  (  "xxx"  )  )  ; 
assertFalse  (  accessor  .  deviceExists  (  null  )  )  ; 
} 
}  )  ; 
} 




public   void   testRetrieveDeviceIdentificationElement  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
Element   identification  =  accessor  .  retrieveDeviceIdentificationElement  (  "PC"  )  ; 
assertEquals  (  "The retrieved identication element is not for the"  +  "PC device."  ,  "PC"  ,  identification  .  getAttributeValue  (  "name"  )  )  ; 
} 
}  )  ; 
} 




public   void   testRetrieveDeviceTACIdentificationElement  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
Element   identification  =  accessor  .  retrieveTACDeviceElement  (  "Nokia-6210"  )  ; 
assertEquals  (  "The retrieved identication element is not for the"  +  "Nokia-6210 device."  ,  "Nokia-6210"  ,  identification  .  getAttributeValue  (  "name"  )  )  ; 
} 
}  )  ; 
} 




public   void   testIsStandardDevice  (  )  { 
String   device  =  "_device"  ; 
assertFalse  (  "Device \""  +  device  +  "\" should not be a standard device"  ,  EclipseDeviceRepository  .  isStandardDevice  (  device  )  )  ; 
device  =  "device"  ; 
assertTrue  (  "Device \""  +  device  +  "\" should be a standard device"  ,  EclipseDeviceRepository  .  isStandardDevice  (  device  )  )  ; 
} 





public   void   testNoDevice  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
try  { 
updateHierarchy  (  getHierarchy  (  "hierarchy-empty.xml"  )  )  ; 
fail  (  "empty hierarchy is not valid"  )  ; 
}  catch  (  RepositoryException   e  )  { 
} 
} 
}  )  ; 
} 




public   void   testGetRootDeviceName  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
assertEquals  (  "Root device name should match"  ,  "Master"  ,  accessor  .  getRootDeviceName  (  )  )  ; 
accessor  =  updateHierarchy  (  getHierarchy  (  "hierarchy-root.xml"  )  )  ; 
assertEquals  (  "Root device name should match"  ,  "root"  ,  accessor  .  getRootDeviceName  (  )  )  ; 
} 
}  )  ; 
} 




public   void   testGetChildDeviceNames  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
List   children  ; 
children  =  accessor  .  getChildDeviceNames  (  null  )  ; 
assertEquals  (  "Child device names should match"  ,  null  ,  children  )  ; 
children  =  accessor  .  getChildDeviceNames  (  "xxx"  )  ; 
assertEquals  (  "Child device names should match"  ,  null  ,  children  )  ; 
verifyHierarchy  (  accessor  )  ; 
children  =  accessor  .  getChildDeviceNames  (  "NoChildrenDevice"  )  ; 
assertNull  (  "No children should exist"  ,  children  )  ; 
} 
}  )  ; 
} 






private   void   verifyHierarchy  (  EclipseDeviceRepository   accessor  )  { 
List   children  ; 
children  =  accessor  .  getChildDeviceNames  (  "Master"  )  ; 
assertNotNull  (  "Children should exist"  ,  children  )  ; 
assertEquals  (  "Expected number of children"  ,  6  ,  children  .  size  (  )  )  ; 
String   expectedChildren  [  ]  =  {  "PC"  ,  "Mobile"  ,  "Voice"  ,  "TV"  ,  "Kiosk"  ,  "Internet-Appliance"  }  ; 
for  (  int   i  =  0  ;  i  <  expectedChildren  .  length  ;  i  ++  )  { 
assertTrue  (  "Child should be in collection"  ,  children  .  contains  (  expectedChildren  [  i  ]  )  )  ; 
} 
} 









public   void   notestRenameDevice  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
try  { 
accessor  .  renameDevice  (  null  ,  null  )  ; 
fail  (  "Expected an IllegalArgumentException"  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
try  { 
accessor  .  renameDevice  (  "PC"  ,  null  )  ; 
fail  (  "Expected an IllegalArgumentException"  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
try  { 
accessor  .  renameDevice  (  "xxx"  ,  "WP"  )  ; 
fail  (  "RepositoryException expected"  )  ; 
}  catch  (  RepositoryException   e  )  { 
} 
accessor  .  renameDevice  (  "PC"  ,  "PeeCee"  )  ; 
assertFalse  (  "PC should not be found"  ,  accessor  .  deviceExists  (  "PC"  )  )  ; 
assertTrue  (  "PeeCee should be found"  ,  accessor  .  deviceExists  (  "PeeCee"  )  )  ; 
accessor  .  renameDevice  (  "WAP-Handset"  ,  "WP"  )  ; 
assertFalse  (  "WAP-Handset should not be found"  ,  accessor  .  deviceExists  (  "WAP-Handset"  )  )  ; 
assertTrue  (  "WP should be found"  ,  accessor  .  deviceExists  (  "WP"  )  )  ; 
writeRepository  (  accessor  )  ; 
ZipFile   zipFile  =  new   ZipFile  (  repository  )  ; 
try  { 
Element   element  =  parseFile  (  zipFile  ,  "hierarchy.xml"  )  ; 
Element   master  =  assertContainsDevice  (  element  ,  "Master"  )  ; 
element  =  assertContainsDevice  (  master  ,  "Mobile"  )  ; 
element  =  assertContainsDevice  (  element  ,  "Handset"  )  ; 
assertContainsDevice  (  element  ,  "WP"  )  ; 
assertContainsDevice  (  master  ,  "PeeCee"  )  ; 
assertNull  (  zipFile  .  getEntry  (  "standard/devices/PC.xml"  )  )  ; 
assertNotNull  (  zipFile  .  getEntry  (  "standard/devices/PeeCee.xml"  )  )  ; 
assertNull  (  zipFile  .  getEntry  (  "standard/devices/WAP-Handset.xml"  )  )  ; 
assertNotNull  (  zipFile  .  getEntry  (  "standard/devices/WP.xml"  )  )  ; 
element  =  parseFile  (  zipFile  ,  "standard/devices/PeeCee.xml"  )  ; 
assertEquals  (  ""  ,  "PeeCee"  ,  element  .  getAttributeValue  (  "name"  )  )  ; 
element  =  parseFile  (  zipFile  ,  "standard/devices/WP.xml"  )  ; 
assertEquals  (  ""  ,  "WP"  ,  element  .  getAttributeValue  (  "name"  )  )  ; 
element  =  parseFile  (  zipFile  ,  "identification.xml"  )  ; 
assertContainsNoDevice  (  element  ,  "PC"  )  ; 
assertContainsDevice  (  element  ,  "PeeCee"  )  ; 
assertContainsNoDevice  (  element  ,  "WAP-Handset"  )  ; 
assertContainsDevice  (  element  ,  "WP"  )  ; 
element  =  parseFile  (  zipFile  ,  "tac-identification.xml"  )  ; 
assertContainsNoDevice  (  element  ,  "PC"  )  ; 
assertContainsDevice  (  element  ,  "PeeCee"  )  ; 
assertContainsNoDevice  (  element  ,  "WAP-Handset"  )  ; 
assertContainsDevice  (  element  ,  "WP"  )  ; 
}  finally  { 
zipFile  .  close  (  )  ; 
} 
} 
}  )  ; 
} 

private   Element   parseFile  (  ZipFile   zipFile  ,  String   name  )  throws   Exception  { 
SAXBuilder   builder  =  new   SAXBuilder  (  )  ; 
ZipEntry   entry  =  zipFile  .  getEntry  (  name  )  ; 
Document   doc  =  builder  .  build  (  zipFile  .  getInputStream  (  entry  )  )  ; 
Element   element  =  doc  .  getRootElement  (  )  ; 
return   element  ; 
} 









public   void   notestRemoveDevice  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
try  { 
accessor  .  removeDevice  (  null  )  ; 
fail  (  "Expected an IllegalArgumentException"  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
try  { 
accessor  .  removeDevice  (  "xxx"  )  ; 
fail  (  "Expected an RepositoryException"  )  ; 
}  catch  (  RepositoryException   e  )  { 
} 
try  { 
accessor  .  removeDevice  (  "PC"  )  ; 
}  catch  (  RepositoryException   e  )  { 
fail  (  "Did not expect a RepositoryException"  )  ; 
e  .  printStackTrace  (  )  ; 
} 
writeRepository  (  accessor  )  ; 
ZipFile   zipFile  =  new   ZipFile  (  repository  )  ; 
try  { 
Element   element  =  parseFile  (  zipFile  ,  "hierarchy.xml"  )  ; 
element  =  assertContainsDevice  (  element  ,  "Master"  )  ; 
assertContainsNoDevice  (  element  ,  "PC"  )  ; 
assertNull  (  zipFile  .  getEntry  (  "standard/devices/PC.xml"  )  )  ; 
assertNull  (  zipFile  .  getEntry  (  "custom/devices/PC.xml"  )  )  ; 
element  =  parseFile  (  zipFile  ,  "identification.xml"  )  ; 
element  =  assertContainsDevice  (  element  ,  "Master"  )  ; 
assertContainsNoDevice  (  element  ,  "PC"  )  ; 
element  =  parseFile  (  zipFile  ,  "tac-identification.xml"  )  ; 
element  =  assertContainsDevice  (  element  ,  "Master"  )  ; 
assertContainsNoDevice  (  element  ,  "PC"  )  ; 
}  finally  { 
zipFile  .  close  (  )  ; 
} 
} 
}  )  ; 
} 

private   void   assertDeviceFileEquals  (  ZipFile   zipFile  ,  String   deviceDirectory  ,  String   deviceName  ,  Map   customPolicyMap  )  throws   Exception  { 
Element   element  =  parseFile  (  zipFile  ,  "hierarchy.xml"  )  ; 
element  =  assertContainsDevice  (  element  ,  "Master"  )  ; 
assertContainsNoDevice  (  element  ,  "PC"  )  ; 
assertNull  (  ""  ,  zipFile  .  getEntry  (  "standard/devices/PC.xml"  )  )  ; 
assertNull  (  ""  ,  zipFile  .  getEntry  (  "custom/devices/PC.xml"  )  )  ; 
} 

Element   assertContainsDevice  (  Element   element  ,  String   name  )  { 
List   elements  =  element  .  getContent  (  new   DeviceFilter  (  name  )  )  ; 
assertEquals  (  ""  ,  elements  .  size  (  )  ,  1  )  ; 
element  =  (  Element  )  elements  .  get  (  0  )  ; 
return   element  ; 
} 

void   assertContainsNoDevice  (  Element   element  ,  String   name  )  { 
List   elements  =  element  .  getContent  (  new   DeviceFilter  (  name  )  )  ; 
assertEquals  (  ""  ,  elements  .  size  (  )  ,  0  )  ; 
} 





class   DeviceFilter   implements   Filter  { 

String   name  ; 

public   DeviceFilter  (  String   name  )  { 
this  .  name  =  name  ; 
} 

public   boolean   matches  (  Object   o  )  { 
boolean   success  =  false  ; 
if  (  o   instanceof   Element  )  { 
Element   element  =  (  Element  )  o  ; 
if  (  name  .  equals  (  element  .  getAttributeValue  (  "name"  )  )  )  { 
success  =  true  ; 
} 
} 
return   success  ; 
} 
} 




public   void   testExists  (  )  throws   Exception  { 
try  { 
new   EclipseDeviceRepository  (  null  ,  transformerMetaFactory  ,  jdomFactory  ,  null  )  ; 
fail  (  "Illegal Argument Exception should've been thrown"  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
try  { 
new   EclipseDeviceRepository  (  null  ,  jdomFactory  )  ; 
fail  (  "Illegal Argument Exception should've been thrown"  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
} 




public   void   testReadingEmptyRepository  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   ResourceTemporaryFileCreator  (  this  .  getClass  (  )  ,  "repository_empty.zip"  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
try  { 
new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  null  )  ; 
fail  (  "Expected Repository Exception (ZipExcepiton)"  )  ; 
}  catch  (  RepositoryException   e  )  { 
} 
} 
}  )  ; 
} 




public   void   testGetProperties  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
List   propsList  =  new   ArrayList  (  )  ; 
Properties   props  =  accessor  .  getProperties  (  )  ; 
Enumeration   propsEnum  =  props  .  propertyNames  (  )  ; 
while  (  propsEnum  .  hasMoreElements  (  )  )  { 
propsList  .  add  (  propsEnum  .  nextElement  (  )  )  ; 
} 
assertTrue  (  propsList  .  contains  (  "policy.J2MEconf.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.disptech.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.network.latency.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.gifinpage.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.mp3inpage.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.UAProf.TablesCapable.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.entrytype.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.portability.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.msvid.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.dvidcamera.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.wtls.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.postype.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.gpng2rule.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.protocol.wml.emulate.smallTag.name"  )  )  ; 
assertTrue  (  propsList  .  contains  (  "policy.wtlskeystrng.description"  )  )  ; 
} 
}  )  ; 
} 






public   void   testPopulatePropertiesWithLanguageCountryVariantSet  (  )  throws   Throwable  { 
doTestPopulateProperties  (  new   Locale  (  "en"  ,  "US"  ,  "VARIANT1_VARIANT2"  )  ,  "standard/policies/resources/"  +  "test_en_US_VARIANT1_VARIANT2.properties"  )  ; 
} 






public   void   testPopulatePropertiesWithLanguageCountryInvalidVariantSet  (  )  throws   Throwable  { 
doTestPopulateProperties  (  new   Locale  (  "en"  ,  "US"  ,  "variant99"  )  ,  "standard/policies/resources/test_en_US.properties"  )  ; 
} 






public   void   testPopulatePropertiesWithLanguageCountrySet  (  )  throws   Throwable  { 
doTestPopulateProperties  (  new   Locale  (  "en"  ,  "US"  )  ,  "standard/policies/resources/test_en_US.properties"  )  ; 
} 






public   void   testPopulatePropertiesWithLanguageInvalidCountryVariantSet  (  )  throws   Throwable  { 
doTestPopulateProperties  (  new   Locale  (  "en"  ,  "DE"  ,  "variant99"  )  ,  "standard/policies/resources/test_en.properties"  )  ; 
} 






public   void   testPopulatePropertiesWithLanguageSet  (  )  throws   Throwable  { 
doTestPopulateProperties  (  new   Locale  (  "en"  ,  ""  )  ,  "standard/policies/resources/test_en.properties"  )  ; 
} 






public   void   testPopulatePropertiesWithInvalidLanguageSet  (  )  throws   Throwable  { 
doTestPopulateProperties  (  new   Locale  (  "de"  ,  ""  )  ,  "standard/policies/resources/test.properties"  )  ; 
} 





public   void   testPopulatePropertiesWhenFileDoesNotExist  (  )  throws   Throwable  { 
assertNull  (  "Properties file was found"  ,  executePopulateProperties  (  new   Properties  (  )  ,  "bogus/path/test"  )  )  ; 
} 







private   void   doTestPopulateProperties  (  Locale   locale  ,  String   expectedFilename  )  throws   Throwable  { 
String   prefix  =  "standard/policies/resources/test"  ; 
Properties   properties  =  new   Properties  (  )  ; 
Locale   currentLocale  =  Locale  .  getDefault  (  )  ; 
try  { 
Locale  .  setDefault  (  locale  )  ; 
String   path  =  executePopulateProperties  (  properties  ,  prefix  )  ; 
assertEquals  (  "Unexpected properties file loaded "  ,  expectedFilename  ,  path  )  ; 
}  finally  { 
Locale  .  setDefault  (  currentLocale  )  ; 
} 
} 









private   String   executePopulateProperties  (  final   Properties   properties  ,  final   String   prefix  )  throws   Throwable  { 
final   String  [  ]  result  =  new   String  [  1  ]  ; 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   file  )  throws   Exception  { 
MDPRArchiveAccessor   archiveAccessor  =  new   MDPRArchiveAccessor  (  file  .  getPath  (  )  ,  transformerMetaFactory  )  ; 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  archiveAccessor  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
try  { 
result  [  0  ]  =  (  String  )  PrivateAccessor  .  invoke  (  accessor  ,  "populateProperties"  ,  new   Class  [  ]  {  ZipArchive  .  class  ,  String  .  class  ,  Properties  .  class  }  ,  new   Object  [  ]  {  archiveAccessor  .  getArchive  (  )  ,  prefix  ,  properties  }  )  ; 
}  catch  (  Throwable   throwable  )  { 
throw   new   Exception  (  throwable  )  ; 
} 
} 
}  )  ; 
return   result  [  0  ]  ; 
} 






public   void   testMoveDeviceNullDeviceArg  (  )  throws   Exception  { 
try  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
accessor  .  moveDevice  (  null  ,  "parent"  )  ; 
fail  (  "IllegalArgumentException was not thrown for null device"  )  ; 
} 
}  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
} 






public   void   testMoveDeviceNullParentDeviceArg  (  )  throws   Exception  { 
try  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
accessor  .  moveDevice  (  "device"  ,  null  )  ; 
fail  (  "IllegalArgumentException was not thrown for null parent"  )  ; 
} 
}  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
} 







public   void   testMoveDeviceNonExistentDeviceArg  (  )  throws   Exception  { 
try  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
accessor  .  moveDevice  (  "doesNotExistDevice"  ,  "parent"  )  ; 
fail  (  "RepositoryException was not thrown for non existent device"  )  ; 
} 
}  )  ; 
}  catch  (  RepositoryException   e  )  { 
} 
} 







public   void   testMoveDeviceNonExistentParentArg  (  )  throws   Exception  { 
try  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
accessor  .  moveDevice  (  "Master"  ,  "doesNotExistDevice"  )  ; 
fail  (  "RepositoryException was not thrown for non existent parent"  )  ; 
} 
}  )  ; 
}  catch  (  RepositoryException   e  )  { 
} 
} 







public   void   testMoveDeviceToInvalidChild  (  )  throws   Throwable  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
Element   pc  =  null  ; 
try  { 
pc  =  (  Element  )  PrivateAccessor  .  invoke  (  accessor  ,  "getHierarchyDeviceElement"  ,  new   Class  [  ]  {  String  .  class  }  ,  new   String  [  ]  {  "PC"  }  )  ; 
}  catch  (  Throwable   throwable  )  { 
throw   new   Exception  (  throwable  )  ; 
} 
assertNotNull  (  "PC device does not exist"  )  ; 
Element   pcParent  =  pc  .  getParent  (  )  ; 
try  { 
accessor  .  moveDevice  (  "Master"  ,  "PC"  )  ; 
fail  (  "RepositoryException was not thrown when moving a device "  +  "whose parent is a child of the device being moved"  )  ; 
}  catch  (  IllegalArgumentException   e  )  { 
} 
assertSame  (  "PC device has an unexcepted parent"  ,  pcParent  ,  pc  .  getParent  (  )  )  ; 
} 
}  )  ; 
} 






public   void   testMoveDevice  (  )  throws   Throwable  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
Element   pc  =  null  ; 
try  { 
pc  =  (  Element  )  PrivateAccessor  .  invoke  (  accessor  ,  "getHierarchyDeviceElement"  ,  new   Class  [  ]  {  String  .  class  }  ,  new   String  [  ]  {  "PC"  }  )  ; 
}  catch  (  Throwable   throwable  )  { 
throw   new   Exception  (  throwable  )  ; 
} 
assertNotEquals  (  pc  .  getParent  (  )  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  ,  "TV"  )  ; 
accessor  .  moveDevice  (  "PC"  ,  "TV"  )  ; 
assertEquals  (  "Device was not moved"  ,  "TV"  ,  pc  .  getParent  (  )  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  )  ; 
} 
}  )  ; 
} 





public   void   testGetDeviceHierarchyDocument  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   originalRepository  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  false  ,  true  ,  null  )  ; 
Document   originalDocument  =  (  Document  )  PrivateAccessor  .  getField  (  originalRepository  ,  "xmlHierarchyDocument"  )  ; 
Document   document  =  EclipseDeviceRepository  .  getDeviceHierarchyDocument  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  )  ; 
assertSame  (  "Accessor should not have been modified"  ,  originalDocument  ,  PrivateAccessor  .  getField  (  originalRepository  ,  "xmlHierarchyDocument"  )  )  ; 
assertNotNull  (  "Document should exist"  ,  document  )  ; 
assertEquals  (  "Root device name should match"  ,  "hierarchy"  ,  document  .  getRootElement  (  )  .  getName  (  )  )  ; 
List   list  =  document  .  getRootElement  (  )  .  getChildren  (  )  ; 
Element   element  =  (  Element  )  list  .  get  (  0  )  ; 
list  =  element  .  getChildren  (  )  ; 
assertEquals  (  "Device categories should match expected number"  ,  6  ,  list  .  size  (  )  )  ; 
} 
}  )  ; 
} 

public   void   testRemovalOfStandardElement  (  )  throws   Exception  { 
final   String   part1  =  "<device xmlns=\"http://www.volantis.com/xmlns/device-repository/device\" "  +  "xmlns:xsi=\""  +  W3CSchemata  .  XSI_NAMESPACE  +  "\" "  +  "xsi:schemaLocation=\"http://www.volantis.com/xmlns/device-repository/device "  +  "http://www.volantis.com/schema/device-repository/v3.0/device.xsd\">"  +  "<policies>"  +  "<device:policy xmlns:device=\"http://www.volantis.com/xmlns/device-repository/device\" name=\"protocol.content.type\" />"  +  "<device:policy xmlns:device=\"http://www.volantis.com/xmlns/device-repository/device\" name=\"adcpm32inpage\" value=\"false\""  ; 
final   String   standardElement  =  "><device:standard>"  +  "<device:policy name=\"adcpm32inpage\" value=\"false\" />"  +  "</device:standard>"  +  "</device:policy></policies></device>"  ; 
SAXBuilder   parser  =  new   SAXBuilder  (  )  ; 
Document   doc  =  parser  .  build  (  new   StringReader  (  part1  +  standardElement  )  )  ; 
XMLOutputter   outputter  =  new   XMLOutputter  (  )  ; 
outputter  .  setOmitDeclaration  (  true  )  ; 
parser  .  setXMLFilter  (  STANDARD_ELEMENT_FILTER  )  ; 
Document   cleaned  =  parser  .  build  (  new   StringReader  (  outputter  .  outputString  (  doc  )  )  )  ; 
String   expected  =  part1  +  " /></policies></device>"  +  PrivateAccessor  .  getField  (  XMLOutputter  .  class  ,  "STANDARD_LINE_SEPARATOR"  )  ; 
String   result  =  outputter  .  outputString  (  cleaned  )  ; 
assertEquals  (  "Result should match:'"  ,  expected  ,  result  )  ; 
} 








public   void   testGetTACIdentificationDocument  (  )  throws   Exception  { 
TemporaryFileManager   manager  =  new   TemporaryFileManager  (  new   TestDeviceRepositoryCreator  (  )  )  ; 
manager  .  executeWith  (  new   TemporaryFileExecutor  (  )  { 

public   void   execute  (  File   repository  )  throws   Exception  { 
EclipseDeviceRepository   accessor  =  new   EclipseDeviceRepository  (  repository  .  getPath  (  )  ,  transformerMetaFactory  ,  jdomFactory  ,  true  ,  true  ,  null  )  ; 
Document   document  =  accessor  .  getDeviceTACIdentificationDocument  (  )  ; 
assertNotNull  (  "TAC identification document should be non-null"  ,  document  )  ; 
Element   rootEl  =  document  .  getRootElement  (  )  ; 
assertEquals  (  "Root element must have expected number of children"  ,  5  ,  rootEl  .  getChildren  (  )  .  size  (  )  )  ; 
Element   childEl  =  (  Element  )  rootEl  .  getChildren  (  )  .  get  (  1  )  ; 
String   devName  =  childEl  .  getAttributeValue  (  DeviceRepositorySchemaConstants  .  DEVICE_NAME_ATTRIBUTE  )  ; 
assertEquals  (  "Second child should be Nokia 6210"  ,  "Nokia-6210"  ,  devName  )  ; 
assertEquals  (  "Second child should have two children"  ,  2  ,  childEl  .  getChildren  (  )  .  size  (  )  )  ; 
} 
}  )  ; 
} 
} 

