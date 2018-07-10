package   ch  .  hsr  .  orm  .  codegen  .  ui  .  wizards  .  components  ; 

import   java  .  text  .  Collator  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   org  .  eclipse  .  core  .  resources  .  IContainer  ; 
import   org  .  eclipse  .  core  .  resources  .  IResource  ; 
import   org  .  eclipse  .  core  .  resources  .  IResourceProxy  ; 
import   org  .  eclipse  .  core  .  resources  .  IResourceProxyVisitor  ; 
import   org  .  eclipse  .  core  .  runtime  .  CoreException  ; 
import   org  .  eclipse  .  jface  .  dialogs  .  IDialogConstants  ; 
import   org  .  eclipse  .  jface  .  dialogs  .  IDialogSettings  ; 
import   org  .  eclipse  .  swt  .  SWT  ; 
import   org  .  eclipse  .  swt  .  custom  .  BusyIndicator  ; 
import   org  .  eclipse  .  swt  .  events  .  KeyAdapter  ; 
import   org  .  eclipse  .  swt  .  events  .  KeyEvent  ; 
import   org  .  eclipse  .  swt  .  events  .  ModifyEvent  ; 
import   org  .  eclipse  .  swt  .  events  .  ModifyListener  ; 
import   org  .  eclipse  .  swt  .  events  .  SelectionAdapter  ; 
import   org  .  eclipse  .  swt  .  events  .  SelectionEvent  ; 
import   org  .  eclipse  .  swt  .  graphics  .  Image  ; 
import   org  .  eclipse  .  swt  .  layout  .  GridData  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Button  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Composite  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Control  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Display  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Label  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Shell  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Table  ; 
import   org  .  eclipse  .  swt  .  widgets  .  TableItem  ; 
import   org  .  eclipse  .  swt  .  widgets  .  Text  ; 
import   org  .  eclipse  .  ui  .  dialogs  .  SelectionDialog  ; 
import   org  .  eclipse  .  ui  .  internal  .  ide  .  IDEWorkbenchMessages  ; 
import   org  .  eclipse  .  ui  .  internal  .  ide  .  IDEWorkbenchPlugin  ; 
import   org  .  eclipse  .  ui  .  internal  .  ide  .  StringMatcher  ; 
import   org  .  eclipse  .  ui  .  model  .  WorkbenchLabelProvider  ; 









public   class   ModelSelectionDialog   extends   SelectionDialog  { 

private   static   final   String   DIALOG_SETTINGS_SECTION  =  "ResourceListSelectionDialogSettings"  ; 

Text   pattern  ; 

Table   resourceNames  ; 

Table   folderNames  ; 

String   patternString  ; 

IContainer   container  ; 

int   typeMask  ; 

private   static   Collator   collator  =  Collator  .  getInstance  (  )  ; 

boolean   gatherResourcesDynamically  =  true  ; 

StringMatcher   stringMatcher  ; 

UpdateFilterThread   updateFilterThread  ; 

UpdateGatherThread   updateGatherThread  ; 

ResourceDescriptor  [  ]  descriptors  ; 

int   descriptorsSize  ; 

WorkbenchLabelProvider   labelProvider  =  new   WorkbenchLabelProvider  (  )  ; 

boolean   okEnabled  =  false  ; 

private   boolean   showDerived  =  false  ; 

private   Button   showDerivedButton  ; 

private   boolean   allowUserToToggleDerived  ; 

private   String   filter  ; 

static   class   ResourceDescriptor   implements   Comparable  { 

String   label  ; 

ArrayList   resources  =  new   ArrayList  (  )  ; 

boolean   resourcesSorted  =  true  ; 

public   int   compareTo  (  Object   o  )  { 
return   collator  .  compare  (  label  ,  (  (  ResourceDescriptor  )  o  )  .  label  )  ; 
} 
} 

class   UpdateFilterThread   extends   Thread  { 

boolean   stop  =  false  ; 

int   firstMatch  =  0  ; 

int   lastMatch  =  descriptorsSize  -  1  ; 

public   void   run  (  )  { 
Display   display  =  resourceNames  .  getDisplay  (  )  ; 
final   int   itemIndex  [  ]  =  {  0  }  ; 
final   int   itemCount  [  ]  =  {  0  }  ; 
final   boolean  [  ]  disposed  =  {  false  }  ; 
display  .  syncExec  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  resourceNames  .  isDisposed  (  )  )  { 
disposed  [  0  ]  =  true  ; 
return  ; 
} 
itemCount  [  0  ]  =  resourceNames  .  getItemCount  (  )  ; 
} 
}  )  ; 
if  (  disposed  [  0  ]  )  { 
return  ; 
} 
int   last  ; 
if  (  (  patternString  .  indexOf  (  '?'  )  ==  -  1  )  &&  (  patternString  .  endsWith  (  "*"  )  )  &&  (  patternString  .  indexOf  (  '*'  )  ==  patternString  .  length  (  )  -  1  )  )  { 
firstMatch  =  getFirstMatch  (  )  ; 
if  (  firstMatch  ==  -  1  )  { 
firstMatch  =  0  ; 
lastMatch  =  -  1  ; 
}  else  { 
lastMatch  =  getLastMatch  (  )  ; 
} 
last  =  lastMatch  ; 
for  (  int   i  =  firstMatch  ;  i  <=  lastMatch  ;  i  ++  )  { 
if  (  i  %  50  ==  0  )  { 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  stop  ||  resourceNames  .  isDisposed  (  )  )  { 
disposed  [  0  ]  =  true  ; 
return  ; 
} 
final   int   index  =  i  ; 
display  .  syncExec  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  stop  ||  resourceNames  .  isDisposed  (  )  )  { 
return  ; 
} 
updateItem  (  index  ,  itemIndex  [  0  ]  ,  itemCount  [  0  ]  )  ; 
itemIndex  [  0  ]  ++  ; 
} 
}  )  ; 
} 
}  else  { 
last  =  lastMatch  ; 
boolean   setFirstMatch  =  true  ; 
for  (  int   i  =  firstMatch  ;  i  <=  lastMatch  ;  i  ++  )  { 
if  (  i  %  50  ==  0  )  { 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  stop  ||  resourceNames  .  isDisposed  (  )  )  { 
disposed  [  0  ]  =  true  ; 
return  ; 
} 
final   int   index  =  i  ; 
if  (  match  (  descriptors  [  index  ]  .  label  )  )  { 
if  (  setFirstMatch  )  { 
setFirstMatch  =  false  ; 
firstMatch  =  index  ; 
} 
last  =  index  ; 
display  .  syncExec  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  stop  ||  resourceNames  .  isDisposed  (  )  )  { 
return  ; 
} 
updateItem  (  index  ,  itemIndex  [  0  ]  ,  itemCount  [  0  ]  )  ; 
itemIndex  [  0  ]  ++  ; 
} 
}  )  ; 
} 
} 
} 
if  (  disposed  [  0  ]  )  { 
return  ; 
} 
lastMatch  =  last  ; 
display  .  syncExec  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  resourceNames  .  isDisposed  (  )  )  { 
return  ; 
} 
itemCount  [  0  ]  =  resourceNames  .  getItemCount  (  )  ; 
if  (  itemIndex  [  0  ]  <  itemCount  [  0  ]  )  { 
resourceNames  .  setRedraw  (  false  )  ; 
resourceNames  .  remove  (  itemIndex  [  0  ]  ,  itemCount  [  0  ]  -  1  )  ; 
resourceNames  .  setRedraw  (  true  )  ; 
} 
if  (  resourceNames  .  getItemCount  (  )  ==  0  )  { 
folderNames  .  removeAll  (  )  ; 
updateOKState  (  false  )  ; 
} 
} 
}  )  ; 
} 
} 

class   UpdateGatherThread   extends   Thread  { 

boolean   stop  =  false  ; 

int   lastMatch  =  -  1  ; 

int   firstMatch  =  0  ; 

boolean   refilter  =  false  ; 

public   void   run  (  )  { 
Display   display  =  resourceNames  .  getDisplay  (  )  ; 
final   int   itemIndex  [  ]  =  {  0  }  ; 
final   int   itemCount  [  ]  =  {  0  }  ; 
final   boolean  [  ]  disposed  =  {  false  }  ; 
display  .  syncExec  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  resourceNames  .  isDisposed  (  )  )  { 
disposed  [  0  ]  =  true  ; 
return  ; 
} 
itemCount  [  0  ]  =  resourceNames  .  getItemCount  (  )  ; 
} 
}  )  ; 
if  (  disposed  [  0  ]  )  { 
return  ; 
} 
if  (  !  refilter  )  { 
for  (  int   i  =  0  ;  i  <=  lastMatch  ;  i  ++  )  { 
if  (  i  %  50  ==  0  )  { 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  stop  ||  resourceNames  .  isDisposed  (  )  )  { 
disposed  [  0  ]  =  true  ; 
return  ; 
} 
final   int   index  =  i  ; 
display  .  syncExec  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  stop  ||  resourceNames  .  isDisposed  (  )  )  { 
return  ; 
} 
updateItem  (  index  ,  itemIndex  [  0  ]  ,  itemCount  [  0  ]  )  ; 
itemIndex  [  0  ]  ++  ; 
} 
}  )  ; 
} 
}  else  { 
for  (  int   i  =  firstMatch  ;  i  <=  lastMatch  ;  i  ++  )  { 
if  (  i  %  50  ==  0  )  { 
try  { 
Thread  .  sleep  (  10  )  ; 
}  catch  (  InterruptedException   e  )  { 
} 
} 
if  (  stop  ||  resourceNames  .  isDisposed  (  )  )  { 
disposed  [  0  ]  =  true  ; 
return  ; 
} 
final   int   index  =  i  ; 
if  (  match  (  descriptors  [  index  ]  .  label  )  )  { 
display  .  syncExec  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  stop  ||  resourceNames  .  isDisposed  (  )  )  { 
return  ; 
} 
updateItem  (  index  ,  itemIndex  [  0  ]  ,  itemCount  [  0  ]  )  ; 
itemIndex  [  0  ]  ++  ; 
} 
}  )  ; 
} 
} 
} 
if  (  disposed  [  0  ]  )  { 
return  ; 
} 
display  .  syncExec  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  resourceNames  .  isDisposed  (  )  )  { 
return  ; 
} 
itemCount  [  0  ]  =  resourceNames  .  getItemCount  (  )  ; 
if  (  itemIndex  [  0  ]  <  itemCount  [  0  ]  )  { 
resourceNames  .  setRedraw  (  false  )  ; 
resourceNames  .  remove  (  itemIndex  [  0  ]  ,  itemCount  [  0  ]  -  1  )  ; 
resourceNames  .  setRedraw  (  true  )  ; 
} 
if  (  resourceNames  .  getItemCount  (  )  ==  0  )  { 
folderNames  .  removeAll  (  )  ; 
updateOKState  (  false  )  ; 
} 
} 
}  )  ; 
} 
} 









public   ModelSelectionDialog  (  Shell   parentShell  ,  IResource  [  ]  resources  ,  String   filter  )  { 
super  (  parentShell  )  ; 
this  .  filter  =  filter  ; 
setShellStyle  (  getShellStyle  (  )  |  SWT  .  RESIZE  )  ; 
gatherResourcesDynamically  =  false  ; 
initDescriptors  (  resources  )  ; 
} 
















public   ModelSelectionDialog  (  Shell   parentShell  ,  IContainer   container  ,  int   typeMask  ,  String   filter  )  { 
super  (  parentShell  )  ; 
this  .  filter  =  filter  ; 
this  .  container  =  container  ; 
this  .  typeMask  =  typeMask  ; 
setShellStyle  (  getShellStyle  (  )  |  SWT  .  RESIZE  )  ; 
} 




protected   String   adjustPattern  (  )  { 
String   text  =  pattern  .  getText  (  )  .  trim  (  )  ; 
if  (  text  .  endsWith  (  "<"  )  )  { 
return   text  .  substring  (  0  ,  text  .  length  (  )  -  1  )  ; 
} 
if  (  !  text  .  equals  (  ""  )  &&  !  text  .  endsWith  (  "*"  )  )  { 
return   text  +  "*"  ; 
} 
return   text  ; 
} 




protected   void   cancelPressed  (  )  { 
setResult  (  null  )  ; 
super  .  cancelPressed  (  )  ; 
} 




public   boolean   close  (  )  { 
boolean   result  =  super  .  close  (  )  ; 
labelProvider  .  dispose  (  )  ; 
return   result  ; 
} 




public   void   create  (  )  { 
super  .  create  (  )  ; 
pattern  .  setText  (  this  .  filter  )  ; 
pattern  .  setFocus  (  )  ; 
getButton  (  IDialogConstants  .  OK_ID  )  .  setEnabled  (  okEnabled  )  ; 
} 








protected   Control   createDialogArea  (  Composite   parent  )  { 
Composite   dialogArea  =  (  Composite  )  super  .  createDialogArea  (  parent  )  ; 
Label   l  =  new   Label  (  dialogArea  ,  SWT  .  NONE  )  ; 
l  .  setText  (  IDEWorkbenchMessages  .  ResourceSelectionDialog_label  )  ; 
GridData   data  =  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  ; 
l  .  setLayoutData  (  data  )  ; 
pattern  =  new   Text  (  dialogArea  ,  SWT  .  SINGLE  |  SWT  .  BORDER  )  ; 
pattern  .  setLayoutData  (  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  )  ; 
l  =  new   Label  (  dialogArea  ,  SWT  .  NONE  )  ; 
l  .  setText  (  IDEWorkbenchMessages  .  ResourceSelectionDialog_matching  )  ; 
data  =  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  ; 
l  .  setLayoutData  (  data  )  ; 
resourceNames  =  new   Table  (  dialogArea  ,  SWT  .  SINGLE  |  SWT  .  BORDER  |  SWT  .  V_SCROLL  )  ; 
data  =  new   GridData  (  GridData  .  FILL_BOTH  )  ; 
data  .  heightHint  =  12  *  resourceNames  .  getItemHeight  (  )  ; 
resourceNames  .  setLayoutData  (  data  )  ; 
l  =  new   Label  (  dialogArea  ,  SWT  .  NONE  )  ; 
l  .  setText  (  IDEWorkbenchMessages  .  ResourceSelectionDialog_folders  )  ; 
data  =  new   GridData  (  GridData  .  FILL_HORIZONTAL  )  ; 
l  .  setLayoutData  (  data  )  ; 
folderNames  =  new   Table  (  dialogArea  ,  SWT  .  SINGLE  |  SWT  .  BORDER  |  SWT  .  V_SCROLL  |  SWT  .  H_SCROLL  )  ; 
data  =  new   GridData  (  GridData  .  FILL_BOTH  )  ; 
data  .  widthHint  =  300  ; 
data  .  heightHint  =  4  *  folderNames  .  getItemHeight  (  )  ; 
folderNames  .  setLayoutData  (  data  )  ; 
if  (  gatherResourcesDynamically  )  { 
updateGatherThread  =  new   UpdateGatherThread  (  )  ; 
}  else  { 
updateFilterThread  =  new   UpdateFilterThread  (  )  ; 
} 
pattern  .  addKeyListener  (  new   KeyAdapter  (  )  { 

public   void   keyReleased  (  KeyEvent   e  )  { 
if  (  e  .  keyCode  ==  SWT  .  ARROW_DOWN  )  { 
resourceNames  .  setFocus  (  )  ; 
} 
} 
}  )  ; 
pattern  .  addModifyListener  (  new   ModifyListener  (  )  { 

public   void   modifyText  (  ModifyEvent   e  )  { 
refresh  (  false  )  ; 
} 
}  )  ; 
resourceNames  .  addSelectionListener  (  new   SelectionAdapter  (  )  { 

public   void   widgetSelected  (  SelectionEvent   e  )  { 
updateFolders  (  (  ResourceDescriptor  )  e  .  item  .  getData  (  )  )  ; 
} 

public   void   widgetDefaultSelected  (  SelectionEvent   e  )  { 
okPressed  (  )  ; 
} 
}  )  ; 
folderNames  .  addSelectionListener  (  new   SelectionAdapter  (  )  { 

public   void   widgetDefaultSelected  (  SelectionEvent   e  )  { 
okPressed  (  )  ; 
} 
}  )  ; 
if  (  getAllowUserToToggleDerived  (  )  )  { 
showDerivedButton  =  new   Button  (  dialogArea  ,  SWT  .  CHECK  )  ; 
showDerivedButton  .  setText  (  IDEWorkbenchMessages  .  ResourceSelectionDialog_showDerived  )  ; 
showDerivedButton  .  addSelectionListener  (  new   SelectionAdapter  (  )  { 

public   void   widgetSelected  (  SelectionEvent   e  )  { 
setShowDerived  (  showDerivedButton  .  getSelection  (  )  )  ; 
refresh  (  true  )  ; 
} 
}  )  ; 
showDerivedButton  .  setSelection  (  getShowDerived  (  )  )  ; 
} 
applyDialogFont  (  dialogArea  )  ; 
return   dialogArea  ; 
} 









public   boolean   getAllowUserToToggleDerived  (  )  { 
return   allowUserToToggleDerived  ; 
} 










public   void   setAllowUserToToggleDerived  (  boolean   allow  )  { 
allowUserToToggleDerived  =  allow  ; 
} 



private   void   filterResources  (  boolean   force  )  { 
String   oldPattern  =  force  ?  null  :  patternString  ; 
patternString  =  adjustPattern  (  )  ; 
if  (  !  force  &&  patternString  .  equals  (  oldPattern  )  )  { 
return  ; 
} 
updateFilterThread  .  stop  =  true  ; 
stringMatcher  =  new   StringMatcher  (  patternString  ,  true  ,  false  )  ; 
UpdateFilterThread   oldThread  =  updateFilterThread  ; 
updateFilterThread  =  new   UpdateFilterThread  (  )  ; 
if  (  patternString  .  equals  (  ""  )  )  { 
updateFilterThread  .  firstMatch  =  0  ; 
updateFilterThread  .  lastMatch  =  -  1  ; 
updateFilterThread  .  start  (  )  ; 
return  ; 
} 
if  (  oldPattern  !=  null  &&  (  oldPattern  .  length  (  )  !=  0  )  &&  oldPattern  .  endsWith  (  "*"  )  &&  patternString  .  endsWith  (  "*"  )  )  { 
int   matchLength  =  oldPattern  .  length  (  )  -  1  ; 
if  (  patternString  .  regionMatches  (  0  ,  oldPattern  ,  0  ,  matchLength  )  )  { 
updateFilterThread  .  firstMatch  =  oldThread  .  firstMatch  ; 
updateFilterThread  .  lastMatch  =  oldThread  .  lastMatch  ; 
updateFilterThread  .  start  (  )  ; 
return  ; 
} 
} 
updateFilterThread  .  firstMatch  =  0  ; 
updateFilterThread  .  lastMatch  =  descriptorsSize  -  1  ; 
updateFilterThread  .  start  (  )  ; 
} 






private   int   getFirstMatch  (  )  { 
int   high  =  descriptorsSize  ; 
int   low  =  -  1  ; 
boolean   match  =  false  ; 
ResourceDescriptor   desc  =  new   ResourceDescriptor  (  )  ; 
desc  .  label  =  patternString  .  substring  (  0  ,  patternString  .  length  (  )  -  1  )  ; 
while  (  high  -  low  >  1  )  { 
int   index  =  (  high  +  low  )  /  2  ; 
String   label  =  descriptors  [  index  ]  .  label  ; 
if  (  match  (  label  )  )  { 
high  =  index  ; 
match  =  true  ; 
}  else  { 
int   compare  =  descriptors  [  index  ]  .  compareTo  (  desc  )  ; 
if  (  compare  ==  -  1  )  { 
low  =  index  ; 
}  else  { 
high  =  index  ; 
} 
} 
} 
if  (  match  )  { 
return   high  ; 
} 
return  -  1  ; 
} 



private   void   gatherResources  (  boolean   force  )  { 
String   oldPattern  =  force  ?  null  :  patternString  ; 
patternString  =  adjustPattern  (  )  ; 
if  (  !  force  &&  patternString  .  equals  (  oldPattern  )  )  { 
return  ; 
} 
updateGatherThread  .  stop  =  true  ; 
updateGatherThread  =  new   UpdateGatherThread  (  )  ; 
if  (  patternString  .  equals  (  ""  )  )  { 
updateGatherThread  .  start  (  )  ; 
return  ; 
} 
stringMatcher  =  new   StringMatcher  (  patternString  ,  true  ,  false  )  ; 
if  (  oldPattern  !=  null  &&  (  oldPattern  .  length  (  )  !=  0  )  &&  oldPattern  .  endsWith  (  "*"  )  &&  patternString  .  endsWith  (  "*"  )  )  { 
int   matchLength  =  oldPattern  .  length  (  )  -  1  ; 
if  (  patternString  .  regionMatches  (  0  ,  oldPattern  ,  0  ,  matchLength  )  )  { 
updateGatherThread  .  refilter  =  true  ; 
updateGatherThread  .  firstMatch  =  0  ; 
updateGatherThread  .  lastMatch  =  descriptorsSize  -  1  ; 
updateGatherThread  .  start  (  )  ; 
return  ; 
} 
} 
final   ArrayList   resources  =  new   ArrayList  (  )  ; 
BusyIndicator  .  showWhile  (  getShell  (  )  .  getDisplay  (  )  ,  new   Runnable  (  )  { 

public   void   run  (  )  { 
getMatchingResources  (  resources  )  ; 
IResource   resourcesArray  [  ]  =  new   IResource  [  resources  .  size  (  )  ]  ; 
resources  .  toArray  (  resourcesArray  )  ; 
initDescriptors  (  resourcesArray  )  ; 
} 
}  )  ; 
updateGatherThread  .  firstMatch  =  0  ; 
updateGatherThread  .  lastMatch  =  descriptorsSize  -  1  ; 
updateGatherThread  .  start  (  )  ; 
} 








private   Image   getImage  (  ResourceDescriptor   desc  )  { 
IResource   r  =  (  IResource  )  desc  .  resources  .  get  (  0  )  ; 
return   labelProvider  .  getImage  (  r  )  ; 
} 






private   int   getLastMatch  (  )  { 
int   high  =  descriptorsSize  ; 
int   low  =  -  1  ; 
boolean   match  =  false  ; 
ResourceDescriptor   desc  =  new   ResourceDescriptor  (  )  ; 
desc  .  label  =  patternString  .  substring  (  0  ,  patternString  .  length  (  )  -  1  )  ; 
while  (  high  -  low  >  1  )  { 
int   index  =  (  high  +  low  )  /  2  ; 
String   label  =  descriptors  [  index  ]  .  label  ; 
if  (  match  (  label  )  )  { 
low  =  index  ; 
match  =  true  ; 
}  else  { 
int   compare  =  descriptors  [  index  ]  .  compareTo  (  desc  )  ; 
if  (  compare  ==  -  1  )  { 
low  =  index  ; 
}  else  { 
high  =  index  ; 
} 
} 
} 
if  (  match  )  { 
return   low  ; 
} 
return  -  1  ; 
} 









private   void   getMatchingResources  (  final   ArrayList   resources  )  { 
try  { 
container  .  accept  (  new   IResourceProxyVisitor  (  )  { 

public   boolean   visit  (  IResourceProxy   proxy  )  { 
if  (  !  getShowDerived  (  )  &&  proxy  .  isDerived  (  )  )  { 
return   false  ; 
} 
int   type  =  proxy  .  getType  (  )  ; 
if  (  (  typeMask  &  type  )  !=  0  )  { 
if  (  match  (  proxy  .  getName  (  )  )  )  { 
IResource   res  =  proxy  .  requestResource  (  )  ; 
if  (  select  (  res  )  )  { 
resources  .  add  (  res  )  ; 
return   true  ; 
} 
return   false  ; 
} 
} 
if  (  type  ==  IResource  .  FILE  )  { 
return   false  ; 
} 
return   true  ; 
} 
}  ,  IResource  .  NONE  )  ; 
}  catch  (  CoreException   e  )  { 
} 
} 

private   Image   getParentImage  (  IResource   resource  )  { 
IResource   parent  =  resource  .  getParent  (  )  ; 
return   labelProvider  .  getImage  (  parent  )  ; 
} 

private   String   getParentLabel  (  IResource   resource  )  { 
IResource   parent  =  resource  .  getParent  (  )  ; 
String   text  ; 
if  (  parent  .  getType  (  )  ==  IResource  .  ROOT  )  { 
text  =  labelProvider  .  getText  (  parent  )  ; 
}  else  { 
text  =  parent  .  getFullPath  (  )  .  makeRelative  (  )  .  toString  (  )  ; 
} 
if  (  text  ==  null  )  { 
return  ""  ; 
} 
return   text  ; 
} 









protected   boolean   getShowDerived  (  )  { 
return   showDerived  ; 
} 









protected   void   setShowDerived  (  boolean   show  )  { 
showDerived  =  show  ; 
} 








private   void   initDescriptors  (  final   IResource   resources  [  ]  )  { 
BusyIndicator  .  showWhile  (  null  ,  new   Runnable  (  )  { 

public   void   run  (  )  { 
descriptors  =  new   ResourceDescriptor  [  resources  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  resources  .  length  ;  i  ++  )  { 
IResource   r  =  resources  [  i  ]  ; 
ResourceDescriptor   d  =  new   ResourceDescriptor  (  )  ; 
d  .  label  =  r  .  getName  (  )  ; 
d  .  resources  .  add  (  r  )  ; 
descriptors  [  i  ]  =  d  ; 
} 
Arrays  .  sort  (  descriptors  )  ; 
descriptorsSize  =  descriptors  .  length  ; 
int   index  =  0  ; 
if  (  descriptorsSize  <  2  )  { 
return  ; 
} 
ResourceDescriptor   current  =  descriptors  [  index  ]  ; 
IResource   currentResource  =  (  IResource  )  current  .  resources  .  get  (  0  )  ; 
for  (  int   i  =  1  ;  i  <  descriptorsSize  ;  i  ++  )  { 
ResourceDescriptor   next  =  descriptors  [  i  ]  ; 
IResource   nextResource  =  (  IResource  )  next  .  resources  .  get  (  0  )  ; 
if  (  nextResource  .  getType  (  )  ==  currentResource  .  getType  (  )  &&  next  .  label  .  equals  (  current  .  label  )  )  { 
current  .  resources  .  add  (  nextResource  )  ; 
current  .  resourcesSorted  =  false  ; 
}  else  { 
if  (  current  .  resources  .  size  (  )  >  1  )  { 
current  .  resourcesSorted  =  false  ; 
} 
descriptors  [  index  +  1  ]  =  descriptors  [  i  ]  ; 
index  ++  ; 
current  =  descriptors  [  index  ]  ; 
currentResource  =  (  IResource  )  current  .  resources  .  get  (  0  )  ; 
} 
} 
descriptorsSize  =  index  +  1  ; 
} 
}  )  ; 
} 








private   boolean   match  (  String   label  )  { 
if  (  (  patternString  ==  null  )  ||  (  patternString  .  equals  (  ""  )  )  ||  (  patternString  .  equals  (  "*"  )  )  )  { 
return   true  ; 
} 
return   stringMatcher  .  match  (  label  )  ; 
} 





protected   void   okPressed  (  )  { 
TableItem   items  [  ]  =  folderNames  .  getSelection  (  )  ; 
if  (  items  .  length  ==  1  )  { 
ArrayList   result  =  new   ArrayList  (  )  ; 
result  .  add  (  items  [  0  ]  .  getData  (  )  )  ; 
setResult  (  result  )  ; 
} 
super  .  okPressed  (  )  ; 
} 








protected   boolean   select  (  IResource   resource  )  { 
return   true  ; 
} 












protected   void   refresh  (  boolean   force  )  { 
if  (  gatherResourcesDynamically  )  { 
gatherResources  (  force  )  ; 
}  else  { 
filterResources  (  force  )  ; 
} 
} 







private   void   updateFolders  (  final   ResourceDescriptor   desc  )  { 
BusyIndicator  .  showWhile  (  getShell  (  )  .  getDisplay  (  )  ,  new   Runnable  (  )  { 

public   void   run  (  )  { 
if  (  !  desc  .  resourcesSorted  )  { 
Collections  .  sort  (  desc  .  resources  ,  new   Comparator  (  )  { 

public   int   compare  (  Object   o1  ,  Object   o2  )  { 
String   s1  =  getParentLabel  (  (  IResource  )  o1  )  ; 
String   s2  =  getParentLabel  (  (  IResource  )  o2  )  ; 
return   collator  .  compare  (  s1  ,  s2  )  ; 
} 
}  )  ; 
desc  .  resourcesSorted  =  true  ; 
} 
folderNames  .  removeAll  (  )  ; 
for  (  int   i  =  0  ;  i  <  desc  .  resources  .  size  (  )  ;  i  ++  )  { 
TableItem   newItem  =  new   TableItem  (  folderNames  ,  SWT  .  NONE  )  ; 
IResource   r  =  (  IResource  )  desc  .  resources  .  get  (  i  )  ; 
newItem  .  setText  (  getParentLabel  (  r  )  )  ; 
newItem  .  setImage  (  getParentImage  (  r  )  )  ; 
newItem  .  setData  (  r  )  ; 
} 
folderNames  .  setSelection  (  0  )  ; 
} 
}  )  ; 
} 












private   void   updateItem  (  int   index  ,  int   itemPos  ,  int   itemCount  )  { 
ResourceDescriptor   desc  =  descriptors  [  index  ]  ; 
TableItem   item  ; 
if  (  itemPos  <  itemCount  )  { 
item  =  resourceNames  .  getItem  (  itemPos  )  ; 
if  (  item  .  getData  (  )  !=  desc  )  { 
item  .  setText  (  desc  .  label  )  ; 
item  .  setData  (  desc  )  ; 
item  .  setImage  (  getImage  (  desc  )  )  ; 
if  (  itemPos  ==  0  )  { 
resourceNames  .  setSelection  (  0  )  ; 
updateFolders  (  desc  )  ; 
} 
} 
}  else  { 
item  =  new   TableItem  (  resourceNames  ,  SWT  .  NONE  )  ; 
item  .  setText  (  desc  .  label  )  ; 
item  .  setData  (  desc  )  ; 
item  .  setImage  (  getImage  (  desc  )  )  ; 
if  (  itemPos  ==  0  )  { 
resourceNames  .  setSelection  (  0  )  ; 
updateFolders  (  desc  )  ; 
} 
} 
updateOKState  (  true  )  ; 
} 








protected   void   updateOKState  (  boolean   state  )  { 
Button   okButton  =  getButton  (  IDialogConstants  .  OK_ID  )  ; 
if  (  okButton  !=  null  &&  !  okButton  .  isDisposed  (  )  &&  state  !=  okEnabled  )  { 
okButton  .  setEnabled  (  state  )  ; 
okEnabled  =  state  ; 
} 
} 

protected   IDialogSettings   getDialogBoundsSettings  (  )  { 
IDialogSettings   settings  =  IDEWorkbenchPlugin  .  getDefault  (  )  .  getDialogSettings  (  )  ; 
IDialogSettings   section  =  settings  .  getSection  (  DIALOG_SETTINGS_SECTION  )  ; 
if  (  section  ==  null  )  { 
section  =  settings  .  addNewSection  (  DIALOG_SETTINGS_SECTION  )  ; 
} 
return   section  ; 
} 
} 

