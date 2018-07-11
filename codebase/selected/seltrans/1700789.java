package   net  .  sourceforge  .  dynamicgmf  .  diagram  .  part  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   org  .  eclipse  .  core  .  resources  .  IFile  ; 
import   org  .  eclipse  .  core  .  resources  .  IResource  ; 
import   org  .  eclipse  .  core  .  resources  .  ResourcesPlugin  ; 
import   org  .  eclipse  .  core  .  runtime  .  CoreException  ; 
import   org  .  eclipse  .  core  .  runtime  .  IPath  ; 
import   org  .  eclipse  .  core  .  runtime  .  NullProgressMonitor  ; 
import   org  .  eclipse  .  core  .  runtime  .  Path  ; 
import   org  .  eclipse  .  emf  .  common  .  util  .  URI  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EObject  ; 
import   org  .  eclipse  .  emf  .  ecore  .  resource  .  Resource  ; 
import   org  .  eclipse  .  emf  .  ecore  .  xmi  .  XMLResource  ; 
import   org  .  eclipse  .  gef  .  EditPart  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  editparts  .  DiagramEditPart  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  editparts  .  IGraphicalEditPart  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  editparts  .  IPrimaryEditPart  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  parts  .  IDiagramGraphicalViewer  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  diagram  .  ui  .  parts  .  IDiagramWorkbenchPart  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  emf  .  core  .  util  .  EMFCoreUtil  ; 
import   org  .  eclipse  .  gmf  .  runtime  .  notation  .  View  ; 
import   org  .  eclipse  .  jface  .  dialogs  .  IDialogSettings  ; 
import   org  .  eclipse  .  jface  .  wizard  .  Wizard  ; 
import   org  .  eclipse  .  jface  .  wizard  .  WizardDialog  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Shell  ; 
import   org  .  eclipse  .  ui  .  IWorkbenchPage  ; 
import   org  .  eclipse  .  ui  .  PartInitException  ; 
import   org  .  eclipse  .  ui  .  PlatformUI  ; 
import   org  .  eclipse  .  ui  .  part  .  FileEditorInput  ; 




public   class   EcoreModelDiagramEditorUtil  { 




public   static   Map   getSaveOptions  (  )  { 
Map   saveOptions  =  new   HashMap  (  )  ; 
saveOptions  .  put  (  XMLResource  .  OPTION_ENCODING  ,  "UTF-8"  )  ; 
saveOptions  .  put  (  Resource  .  OPTION_SAVE_ONLY_IF_CHANGED  ,  Resource  .  OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER  )  ; 
return   saveOptions  ; 
} 




public   static   boolean   openDiagram  (  Resource   diagram  )  throws   PartInitException  { 
String   path  =  diagram  .  getURI  (  )  .  toPlatformString  (  true  )  ; 
IResource   workspaceResource  =  ResourcesPlugin  .  getWorkspace  (  )  .  getRoot  (  )  .  findMember  (  new   Path  (  path  )  )  ; 
if  (  workspaceResource   instanceof   IFile  )  { 
IWorkbenchPage   page  =  PlatformUI  .  getWorkbench  (  )  .  getActiveWorkbenchWindow  (  )  .  getActivePage  (  )  ; 
URI   uri  =  diagram  .  getURI  (  )  ; 
return   null  !=  page  .  openEditor  (  new   FileEditorInput  (  (  IFile  )  workspaceResource  )  ,  "xml_diagram"  .  equals  (  uri  .  fileExtension  (  )  )  ?  "net.sourceforge.dynamicgmf.diagram.part.XMLReflectiveDiagramEditorID"  :  "net.sourceforge.dynamicgmf.diagram.part.ReflectiveDiagramEditorID"  )  ; 
} 
return   false  ; 
} 




public   static   void   setCharset  (  IFile   file  )  { 
if  (  file  ==  null  )  { 
return  ; 
} 
try  { 
file  .  setCharset  (  "UTF-8"  ,  new   NullProgressMonitor  (  )  )  ; 
}  catch  (  CoreException   e  )  { 
EcoreModelDiagramEditorPlugin  .  getInstance  (  )  .  logError  (  "Unable to set charset for file "  +  file  .  getFullPath  (  )  ,  e  )  ; 
} 
} 




public   static   String   getUniqueFileName  (  IPath   containerFullPath  ,  String   fileName  ,  String   extension  )  { 
if  (  containerFullPath  ==  null  )  { 
containerFullPath  =  new   Path  (  ""  )  ; 
} 
if  (  fileName  ==  null  ||  fileName  .  trim  (  )  .  length  (  )  ==  0  )  { 
fileName  =  "default"  ; 
} 
IPath   filePath  =  containerFullPath  .  append  (  fileName  )  ; 
if  (  extension  !=  null  &&  !  extension  .  equals  (  filePath  .  getFileExtension  (  )  )  )  { 
filePath  =  filePath  .  addFileExtension  (  extension  )  ; 
} 
extension  =  filePath  .  getFileExtension  (  )  ; 
fileName  =  filePath  .  removeFileExtension  (  )  .  lastSegment  (  )  ; 
int   i  =  1  ; 
while  (  ResourcesPlugin  .  getWorkspace  (  )  .  getRoot  (  )  .  exists  (  filePath  )  )  { 
i  ++  ; 
filePath  =  containerFullPath  .  append  (  fileName  +  i  )  ; 
if  (  extension  !=  null  )  { 
filePath  =  filePath  .  addFileExtension  (  extension  )  ; 
} 
} 
return   filePath  .  lastSegment  (  )  ; 
} 






public   static   void   runWizard  (  Shell   shell  ,  Wizard   wizard  ,  String   settingsKey  )  { 
IDialogSettings   pluginDialogSettings  =  EcoreModelDiagramEditorPlugin  .  getInstance  (  )  .  getDialogSettings  (  )  ; 
IDialogSettings   wizardDialogSettings  =  pluginDialogSettings  .  getSection  (  settingsKey  )  ; 
if  (  wizardDialogSettings  ==  null  )  { 
wizardDialogSettings  =  pluginDialogSettings  .  addNewSection  (  settingsKey  )  ; 
} 
wizard  .  setDialogSettings  (  wizardDialogSettings  )  ; 
WizardDialog   dialog  =  new   WizardDialog  (  shell  ,  wizard  )  ; 
dialog  .  create  (  )  ; 
dialog  .  getShell  (  )  .  setSize  (  Math  .  max  (  500  ,  dialog  .  getShell  (  )  .  getSize  (  )  .  x  )  ,  500  )  ; 
dialog  .  open  (  )  ; 
} 




public   static   void   selectElementsInDiagram  (  IDiagramWorkbenchPart   diagramPart  ,  List   editParts  )  { 
diagramPart  .  getDiagramGraphicalViewer  (  )  .  deselectAll  (  )  ; 
EditPart   firstPrimary  =  null  ; 
for  (  Iterator   it  =  editParts  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
EditPart   nextPart  =  (  EditPart  )  it  .  next  (  )  ; 
diagramPart  .  getDiagramGraphicalViewer  (  )  .  appendSelection  (  nextPart  )  ; 
if  (  firstPrimary  ==  null  &&  nextPart   instanceof   IPrimaryEditPart  )  { 
firstPrimary  =  nextPart  ; 
} 
} 
if  (  !  editParts  .  isEmpty  (  )  )  { 
diagramPart  .  getDiagramGraphicalViewer  (  )  .  reveal  (  firstPrimary  !=  null  ?  firstPrimary  :  (  EditPart  )  editParts  .  get  (  0  )  )  ; 
} 
} 




private   static   int   findElementsInDiagramByID  (  DiagramEditPart   diagramPart  ,  EObject   element  ,  List   editPartCollector  )  { 
IDiagramGraphicalViewer   viewer  =  (  IDiagramGraphicalViewer  )  diagramPart  .  getViewer  (  )  ; 
final   int   intialNumOfEditParts  =  editPartCollector  .  size  (  )  ; 
if  (  element   instanceof   View  )  { 
EditPart   editPart  =  (  EditPart  )  viewer  .  getEditPartRegistry  (  )  .  get  (  element  )  ; 
if  (  editPart  !=  null  )  { 
editPartCollector  .  add  (  editPart  )  ; 
return   1  ; 
} 
} 
String   elementID  =  EMFCoreUtil  .  getProxyID  (  element  )  ; 
List   associatedParts  =  viewer  .  findEditPartsForElement  (  elementID  ,  IGraphicalEditPart  .  class  )  ; 
for  (  Iterator   editPartIt  =  associatedParts  .  iterator  (  )  ;  editPartIt  .  hasNext  (  )  ;  )  { 
EditPart   nextPart  =  (  EditPart  )  editPartIt  .  next  (  )  ; 
EditPart   parentPart  =  nextPart  .  getParent  (  )  ; 
while  (  parentPart  !=  null  &&  !  associatedParts  .  contains  (  parentPart  )  )  { 
parentPart  =  parentPart  .  getParent  (  )  ; 
} 
if  (  parentPart  ==  null  )  { 
editPartCollector  .  add  (  nextPart  )  ; 
} 
} 
if  (  intialNumOfEditParts  ==  editPartCollector  .  size  (  )  )  { 
if  (  !  associatedParts  .  isEmpty  (  )  )  { 
editPartCollector  .  add  (  associatedParts  .  iterator  (  )  .  next  (  )  )  ; 
}  else  { 
if  (  element  .  eContainer  (  )  !=  null  )  { 
return   findElementsInDiagramByID  (  diagramPart  ,  element  .  eContainer  (  )  ,  editPartCollector  )  ; 
} 
} 
} 
return   editPartCollector  .  size  (  )  -  intialNumOfEditParts  ; 
} 




public   static   View   findView  (  DiagramEditPart   diagramEditPart  ,  EObject   targetElement  ,  LazyElement2ViewMap   lazyElement2ViewMap  )  { 
boolean   hasStructuralURI  =  false  ; 
if  (  targetElement  .  eResource  (  )  instanceof   XMLResource  )  { 
hasStructuralURI  =  (  (  XMLResource  )  targetElement  .  eResource  (  )  )  .  getID  (  targetElement  )  ==  null  ; 
} 
View   view  =  null  ; 
if  (  hasStructuralURI  &&  !  lazyElement2ViewMap  .  getElement2ViewMap  (  )  .  isEmpty  (  )  )  { 
view  =  (  View  )  lazyElement2ViewMap  .  getElement2ViewMap  (  )  .  get  (  targetElement  )  ; 
}  else   if  (  findElementsInDiagramByID  (  diagramEditPart  ,  targetElement  ,  lazyElement2ViewMap  .  editPartTmpHolder  )  >  0  )  { 
EditPart   editPart  =  (  EditPart  )  lazyElement2ViewMap  .  editPartTmpHolder  .  get  (  0  )  ; 
lazyElement2ViewMap  .  editPartTmpHolder  .  clear  (  )  ; 
view  =  editPart  .  getModel  (  )  instanceof   View  ?  (  View  )  editPart  .  getModel  (  )  :  null  ; 
} 
return  (  view  ==  null  )  ?  diagramEditPart  .  getDiagramView  (  )  :  view  ; 
} 




public   static   class   LazyElement2ViewMap  { 




private   Map   element2ViewMap  ; 




private   View   scope  ; 




private   Set   elementSet  ; 




public   final   List   editPartTmpHolder  =  new   ArrayList  (  )  ; 




public   LazyElement2ViewMap  (  View   scope  ,  Set   elements  )  { 
this  .  scope  =  scope  ; 
this  .  elementSet  =  elements  ; 
} 




public   final   Map   getElement2ViewMap  (  )  { 
if  (  element2ViewMap  ==  null  )  { 
element2ViewMap  =  new   HashMap  (  )  ; 
for  (  Iterator   it  =  elementSet  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
EObject   element  =  (  EObject  )  it  .  next  (  )  ; 
if  (  element   instanceof   View  )  { 
View   view  =  (  View  )  element  ; 
if  (  view  .  getDiagram  (  )  ==  scope  .  getDiagram  (  )  )  { 
element2ViewMap  .  put  (  element  ,  element  )  ; 
} 
} 
} 
buildElement2ViewMap  (  scope  ,  element2ViewMap  ,  elementSet  )  ; 
} 
return   element2ViewMap  ; 
} 




static   Map   buildElement2ViewMap  (  View   parentView  ,  Map   element2ViewMap  ,  Set   elements  )  { 
if  (  elements  .  size  (  )  ==  element2ViewMap  .  size  (  )  )  return   element2ViewMap  ; 
if  (  parentView  .  isSetElement  (  )  &&  !  element2ViewMap  .  containsKey  (  parentView  .  getElement  (  )  )  &&  elements  .  contains  (  parentView  .  getElement  (  )  )  )  { 
element2ViewMap  .  put  (  parentView  .  getElement  (  )  ,  parentView  )  ; 
if  (  elements  .  size  (  )  ==  element2ViewMap  .  size  (  )  )  return   element2ViewMap  ; 
} 
for  (  Iterator   it  =  parentView  .  getChildren  (  )  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
buildElement2ViewMap  (  (  View  )  it  .  next  (  )  ,  element2ViewMap  ,  elements  )  ; 
if  (  elements  .  size  (  )  ==  element2ViewMap  .  size  (  )  )  return   element2ViewMap  ; 
} 
for  (  Iterator   it  =  parentView  .  getSourceEdges  (  )  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
buildElement2ViewMap  (  (  View  )  it  .  next  (  )  ,  element2ViewMap  ,  elements  )  ; 
if  (  elements  .  size  (  )  ==  element2ViewMap  .  size  (  )  )  return   element2ViewMap  ; 
} 
for  (  Iterator   it  =  parentView  .  getSourceEdges  (  )  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
buildElement2ViewMap  (  (  View  )  it  .  next  (  )  ,  element2ViewMap  ,  elements  )  ; 
if  (  elements  .  size  (  )  ==  element2ViewMap  .  size  (  )  )  return   element2ViewMap  ; 
} 
return   element2ViewMap  ; 
} 
} 
} 

