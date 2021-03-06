package   jmri  .  jmrit  .  symbolicprog  .  tabbedframe  ; 

import   jmri  .  Programmer  ; 
import   jmri  .  util  .  davidflanagan  .  *  ; 
import   jmri  .  ShutDownTask  ; 
import   jmri  .  implementation  .  swing  .  SwingShutDownTask  ; 
import   jmri  .  jmrit  .  XmlFile  ; 
import   jmri  .  jmrit  .  decoderdefn  .  DecoderFile  ; 
import   jmri  .  jmrit  .  decoderdefn  .  DecoderIndexFile  ; 
import   jmri  .  jmrit  .  roster  .  *  ; 
import   jmri  .  jmrit  .  symbolicprog  .  *  ; 
import   jmri  .  util  .  JmriJFrame  ; 
import   java  .  awt  .  Dimension  ; 
import   java  .  awt  .  Font  ; 
import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  awt  .  event  .  ActionListener  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  util  .  Enumeration  ; 
import   javax  .  swing  .  *  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   org  .  jdom  .  Attribute  ; 
import   org  .  jdom  .  Element  ; 
import   jmri  .  util  .  BusyGlassPane  ; 
import   java  .  awt  .  event  .  ItemListener  ; 
import   java  .  awt  .  Cursor  ; 
import   java  .  awt  .  Rectangle  ; 
import   java  .  awt  .  event  .  ItemEvent  ; 








public   abstract   class   PaneProgFrame   extends   JmriJFrame   implements   java  .  beans  .  PropertyChangeListener  ,  PaneContainer  { 

static   final   java  .  util  .  ResourceBundle   rbt  =  jmri  .  jmrit  .  symbolicprog  .  SymbolicProgBundle  .  bundle  (  )  ; 

JLabel   progStatus  =  new   JLabel  (  rbt  .  getString  (  "StateIdle"  )  )  ; 

CvTableModel   cvModel  =  null  ; 

IndexedCvTableModel   iCvModel  =  null  ; 

VariableTableModel   variableModel  ; 

ResetTableModel   resetModel  =  null  ; 

JMenu   resetMenu  =  null  ; 

Programmer   mProgrammer  ; 

JPanel   modePane  =  null  ; 

boolean   _opsMode  ; 

RosterEntry   _rosterEntry  =  null  ; 

RosterEntryPane   _rPane  =  null  ; 

FunctionLabelPane   _flPane  =  null  ; 

RosterMediaPane   _rMPane  =  null  ; 

List  <  JPanel  >  paneList  =  new   ArrayList  <  JPanel  >  (  )  ; 

int   paneListIndex  ; 

BusyGlassPane   glassPane  ; 

List  <  JComponent  >  activeComponents  =  new   ArrayList  <  JComponent  >  (  )  ; 

String   filename  =  null  ; 

JTabbedPane   tabPane  =  new   JTabbedPane  (  )  ; 

JToggleButton   readChangesButton  =  new   JToggleButton  (  rbt  .  getString  (  "ButtonReadChangesAllSheets"  )  )  ; 

JToggleButton   writeChangesButton  =  new   JToggleButton  (  rbt  .  getString  (  "ButtonWriteChangesAllSheets"  )  )  ; 

JToggleButton   readAllButton  =  new   JToggleButton  (  rbt  .  getString  (  "ButtonReadAllSheets"  )  )  ; 

JToggleButton   writeAllButton  =  new   JToggleButton  (  rbt  .  getString  (  "ButtonWriteAllSheets"  )  )  ; 

ItemListener   l1  ; 

ItemListener   l2  ; 

ItemListener   l3  ; 

ItemListener   l4  ; 

ShutDownTask   decoderDirtyTask  ; 

ShutDownTask   fileDirtyTask  ; 





protected   abstract   JPanel   getModePane  (  )  ; 

protected   void   installComponents  (  )  { 
if  (  jmri  .  InstanceManager  .  shutDownManagerInstance  (  )  !=  null  )  { 
if  (  decoderDirtyTask  ==  null  )  decoderDirtyTask  =  new   SwingShutDownTask  (  "DecoderPro Decoder Window Check"  ,  rbt  .  getString  (  "PromptQuitWindowNotWrittenDecoder"  )  ,  (  String  )  null  ,  this  )  { 

public   boolean   checkPromptNeeded  (  )  { 
return  !  checkDirtyDecoder  (  )  ; 
} 
}  ; 
jmri  .  InstanceManager  .  shutDownManagerInstance  (  )  .  register  (  decoderDirtyTask  )  ; 
if  (  fileDirtyTask  ==  null  )  fileDirtyTask  =  new   SwingShutDownTask  (  "DecoderPro Decoder Window Check"  ,  rbt  .  getString  (  "PromptQuitWindowNotWrittenConfig"  )  ,  rbt  .  getString  (  "PromptSaveQuit"  )  ,  this  )  { 

public   boolean   checkPromptNeeded  (  )  { 
return  !  checkDirtyFile  (  )  ; 
} 

public   boolean   doPrompt  (  )  { 
boolean   result  =  storeFile  (  )  ; 
return   result  ; 
} 
}  ; 
jmri  .  InstanceManager  .  shutDownManagerInstance  (  )  .  register  (  fileDirtyTask  )  ; 
} 
JMenuBar   menuBar  =  new   JMenuBar  (  )  ; 
setJMenuBar  (  menuBar  )  ; 
JMenu   fileMenu  =  new   JMenu  (  rbt  .  getString  (  "MenuFile"  )  )  ; 
menuBar  .  add  (  fileMenu  )  ; 
if  (  !  _opsMode  )  { 
resetMenu  =  new   JMenu  (  rbt  .  getString  (  "MenuReset"  )  )  ; 
menuBar  .  add  (  resetMenu  )  ; 
resetMenu  .  add  (  new   FactoryResetAction  (  rbt  .  getString  (  "MenuFactoryReset"  )  ,  resetModel  ,  this  )  )  ; 
resetMenu  .  setEnabled  (  false  )  ; 
} 
fileMenu  .  add  (  new   AbstractAction  (  rbt  .  getString  (  "MenuSave"  )  )  { 

public   void   actionPerformed  (  ActionEvent   e  )  { 
storeFile  (  )  ; 
} 
}  )  ; 
JMenu   printSubMenu  =  new   JMenu  (  rbt  .  getString  (  "MenuPrint"  )  )  ; 
printSubMenu  .  add  (  new   PrintAction  (  rbt  .  getString  (  "MenuPrintAll"  )  ,  this  ,  false  )  )  ; 
printSubMenu  .  add  (  new   PrintCvAction  (  rbt  .  getString  (  "MenuPrintCVs"  )  ,  cvModel  ,  this  ,  false  )  )  ; 
fileMenu  .  add  (  printSubMenu  )  ; 
JMenu   printPreviewSubMenu  =  new   JMenu  (  rbt  .  getString  (  "MenuPrintPreview"  )  )  ; 
printPreviewSubMenu  .  add  (  new   PrintAction  (  rbt  .  getString  (  "MenuPrintPreviewAll"  )  ,  this  ,  true  )  )  ; 
printPreviewSubMenu  .  add  (  new   PrintCvAction  (  rbt  .  getString  (  "MenuPrintPreviewCVs"  )  ,  cvModel  ,  this  ,  true  )  )  ; 
fileMenu  .  add  (  printPreviewSubMenu  )  ; 
JMenu   importSubMenu  =  new   JMenu  (  rbt  .  getString  (  "MenuImport"  )  )  ; 
fileMenu  .  add  (  importSubMenu  )  ; 
importSubMenu  .  add  (  new   Pr1ImportAction  (  rbt  .  getString  (  "MenuImportPr1"  )  ,  cvModel  ,  this  )  )  ; 
JMenu   exportSubMenu  =  new   JMenu  (  rbt  .  getString  (  "MenuExport"  )  )  ; 
fileMenu  .  add  (  exportSubMenu  )  ; 
exportSubMenu  .  add  (  new   CsvExportAction  (  rbt  .  getString  (  "MenuExportCSV"  )  ,  cvModel  ,  this  )  )  ; 
exportSubMenu  .  add  (  new   Pr1ExportAction  (  rbt  .  getString  (  "MenuExportPr1DOS"  )  ,  cvModel  ,  this  )  )  ; 
exportSubMenu  .  add  (  new   Pr1WinExportAction  (  rbt  .  getString  (  "MenuExportPr1WIN"  )  ,  cvModel  ,  this  )  )  ; 
JPanel   pane  =  new   JPanel  (  )  ; 
pane  .  setLayout  (  new   BoxLayout  (  pane  ,  BoxLayout  .  Y_AXIS  )  )  ; 
enableReadButtons  (  )  ; 
readChangesButton  .  addItemListener  (  l1  =  new   ItemListener  (  )  { 

public   void   itemStateChanged  (  ItemEvent   e  )  { 
if  (  e  .  getStateChange  (  )  ==  ItemEvent  .  SELECTED  )  { 
prepGlassPane  (  readChangesButton  )  ; 
readChangesButton  .  setText  (  rbt  .  getString  (  "ButtonStopReadChangesAll"  )  )  ; 
readChanges  (  )  ; 
}  else  { 
if  (  _programmingPane  !=  null  )  { 
_programmingPane  .  stopProgramming  (  )  ; 
} 
paneListIndex  =  paneList  .  size  (  )  ; 
readChangesButton  .  setText  (  rbt  .  getString  (  "ButtonReadChangesAllSheets"  )  )  ; 
} 
} 
}  )  ; 
readAllButton  .  addItemListener  (  l3  =  new   ItemListener  (  )  { 

public   void   itemStateChanged  (  ItemEvent   e  )  { 
if  (  e  .  getStateChange  (  )  ==  ItemEvent  .  SELECTED  )  { 
prepGlassPane  (  readAllButton  )  ; 
readAllButton  .  setText  (  rbt  .  getString  (  "ButtonStopReadAll"  )  )  ; 
readAll  (  )  ; 
}  else  { 
if  (  _programmingPane  !=  null  )  { 
_programmingPane  .  stopProgramming  (  )  ; 
} 
paneListIndex  =  paneList  .  size  (  )  ; 
readAllButton  .  setText  (  rbt  .  getString  (  "ButtonReadAllSheets"  )  )  ; 
} 
} 
}  )  ; 
writeChangesButton  .  setToolTipText  (  rbt  .  getString  (  "TipWriteHighlightedValues"  )  )  ; 
writeChangesButton  .  addItemListener  (  l2  =  new   ItemListener  (  )  { 

public   void   itemStateChanged  (  ItemEvent   e  )  { 
if  (  e  .  getStateChange  (  )  ==  ItemEvent  .  SELECTED  )  { 
prepGlassPane  (  writeChangesButton  )  ; 
writeChangesButton  .  setText  (  rbt  .  getString  (  "ButtonStopWriteChangesAll"  )  )  ; 
writeChanges  (  )  ; 
}  else  { 
if  (  _programmingPane  !=  null  )  { 
_programmingPane  .  stopProgramming  (  )  ; 
} 
paneListIndex  =  paneList  .  size  (  )  ; 
writeChangesButton  .  setText  (  rbt  .  getString  (  "ButtonWriteChangesAllSheets"  )  )  ; 
} 
} 
}  )  ; 
writeAllButton  .  setToolTipText  (  rbt  .  getString  (  "TipWriteAllValues"  )  )  ; 
writeAllButton  .  addItemListener  (  l4  =  new   ItemListener  (  )  { 

public   void   itemStateChanged  (  ItemEvent   e  )  { 
if  (  e  .  getStateChange  (  )  ==  ItemEvent  .  SELECTED  )  { 
prepGlassPane  (  writeAllButton  )  ; 
writeAllButton  .  setText  (  rbt  .  getString  (  "ButtonStopWriteAll"  )  )  ; 
writeAll  (  )  ; 
}  else  { 
if  (  _programmingPane  !=  null  )  { 
_programmingPane  .  stopProgramming  (  )  ; 
} 
paneListIndex  =  paneList  .  size  (  )  ; 
writeAllButton  .  setText  (  rbt  .  getString  (  "ButtonWriteAllSheets"  )  )  ; 
} 
} 
}  )  ; 
pane  .  add  (  tabPane  )  ; 
modePane  =  getModePane  (  )  ; 
if  (  modePane  !=  null  )  { 
JPanel   bottom  =  new   JPanel  (  )  ; 
bottom  .  setLayout  (  new   BoxLayout  (  bottom  ,  BoxLayout  .  X_AXIS  )  )  ; 
bottom  .  add  (  readChangesButton  )  ; 
bottom  .  add  (  writeChangesButton  )  ; 
bottom  .  add  (  readAllButton  )  ; 
bottom  .  add  (  writeAllButton  )  ; 
pane  .  add  (  bottom  )  ; 
pane  .  add  (  new   JSeparator  (  javax  .  swing  .  SwingConstants  .  HORIZONTAL  )  )  ; 
pane  .  add  (  modePane  )  ; 
pane  .  add  (  new   JSeparator  (  javax  .  swing  .  SwingConstants  .  HORIZONTAL  )  )  ; 
progStatus  .  setAlignmentX  (  JLabel  .  CENTER_ALIGNMENT  )  ; 
pane  .  add  (  progStatus  )  ; 
} 
getContentPane  (  )  .  add  (  pane  )  ; 
addHelp  (  )  ; 
} 

public   List  <  JPanel  >  getPaneList  (  )  { 
return   paneList  ; 
} 

void   addHelp  (  )  { 
addHelpMenu  (  "package.jmri.jmrit.symbolicprog.tabbedframe.PaneProgFrame"  ,  true  )  ; 
} 

public   Dimension   getPreferredSize  (  )  { 
Dimension   screen  =  getMaximumSize  (  )  ; 
int   width  =  Math  .  min  (  super  .  getPreferredSize  (  )  .  width  ,  screen  .  width  )  ; 
int   height  =  Math  .  min  (  super  .  getPreferredSize  (  )  .  height  ,  screen  .  height  )  ; 
return   new   Dimension  (  width  ,  height  )  ; 
} 

public   Dimension   getMaximumSize  (  )  { 
Dimension   screen  =  getToolkit  (  )  .  getScreenSize  (  )  ; 
return   new   Dimension  (  screen  .  width  ,  screen  .  height  -  35  )  ; 
} 






void   enableReadButtons  (  )  { 
readChangesButton  .  setToolTipText  (  rbt  .  getString  (  "TipReadChanges"  )  )  ; 
readAllButton  .  setToolTipText  (  rbt  .  getString  (  "TipReadAll"  )  )  ; 
if  (  cvModel  !=  null  &&  cvModel  .  getProgrammer  (  )  !=  null  &&  !  cvModel  .  getProgrammer  (  )  .  getCanRead  (  )  )  { 
readChangesButton  .  setEnabled  (  false  )  ; 
readAllButton  .  setEnabled  (  false  )  ; 
readChangesButton  .  setToolTipText  (  rbt  .  getString  (  "TipNoRead"  )  )  ; 
readAllButton  .  setToolTipText  (  rbt  .  getString  (  "TipNoRead"  )  )  ; 
}  else  { 
readChangesButton  .  setEnabled  (  true  )  ; 
readAllButton  .  setEnabled  (  true  )  ; 
} 
} 


















@  SuppressWarnings  (  "unchecked"  ) 
public   PaneProgFrame  (  DecoderFile   pDecoderFile  ,  RosterEntry   pRosterEntry  ,  String   pFrameTitle  ,  String   pProgrammerFile  ,  Programmer   pProg  ,  boolean   opsMode  )  { 
super  (  pFrameTitle  )  ; 
_opsMode  =  opsMode  ; 
mProgrammer  =  pProg  ; 
cvModel  =  new   CvTableModel  (  progStatus  ,  mProgrammer  )  ; 
iCvModel  =  new   IndexedCvTableModel  (  progStatus  ,  mProgrammer  )  ; 
variableModel  =  new   VariableTableModel  (  progStatus  ,  new   String  [  ]  {  "Name"  ,  "Value"  }  ,  cvModel  ,  iCvModel  )  ; 
resetModel  =  new   ResetTableModel  (  progStatus  ,  mProgrammer  )  ; 
_rosterEntry  =  pRosterEntry  ; 
if  (  _rosterEntry  ==  null  )  log  .  error  (  "null RosterEntry pointer"  )  ; 
filename  =  pProgrammerFile  ; 
installComponents  (  )  ; 
if  (  _rosterEntry  .  getFileName  (  )  !=  null  )  { 
_rosterEntry  .  readFile  (  )  ; 
} 
if  (  pDecoderFile  !=  null  )  loadDecoderFile  (  pDecoderFile  ,  _rosterEntry  )  ;  else   loadDecoderFromLoco  (  pRosterEntry  )  ; 
saveDefaults  (  )  ; 
if  (  _rosterEntry  .  getFileName  (  )  !=  null  )  _rosterEntry  .  loadCvModel  (  cvModel  ,  iCvModel  )  ; 
variableModel  .  setFileDirty  (  false  )  ; 
if  (  !  _opsMode  )  { 
if  (  resetModel  .  getRowCount  (  )  >  0  )  { 
resetMenu  .  setEnabled  (  true  )  ; 
} 
} 
loadProgrammerFile  (  pRosterEntry  )  ; 
Attribute   a  ; 
if  (  (  a  =  programmerRoot  .  getChild  (  "programmer"  )  .  getAttribute  (  "decoderFilePanes"  )  )  !=  null  &&  a  .  getValue  (  )  .  equals  (  "yes"  )  )  { 
if  (  decoderRoot  !=  null  )  { 
List  <  Element  >  paneList  =  decoderRoot  .  getChildren  (  "pane"  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "will process "  +  paneList  .  size  (  )  +  " pane definitions from decoder file"  )  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
String   pname  =  paneList  .  get  (  i  )  .  getAttribute  (  "name"  )  .  getValue  (  )  ; 
newPane  (  pname  ,  paneList  .  get  (  i  )  ,  modelElem  ,  true  )  ; 
} 
} 
} 
pack  (  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "PaneProgFrame \""  +  pFrameTitle  +  "\" constructed for file "  +  _rosterEntry  .  getFileName  (  )  +  ", unconstrained size is "  +  super  .  getPreferredSize  (  )  +  ", constrained to "  +  getPreferredSize  (  )  )  ; 
} 




Element   modelElem  =  null  ; 

Element   decoderRoot  =  null  ; 

protected   void   loadDecoderFromLoco  (  RosterEntry   r  )  { 
String   decoderModel  =  r  .  getDecoderModel  (  )  ; 
String   decoderFamily  =  r  .  getDecoderFamily  (  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "selected loco uses decoder "  +  decoderFamily  +  " "  +  decoderModel  )  ; 
List  <  DecoderFile  >  l  =  DecoderIndexFile  .  instance  (  )  .  matchingDecoderList  (  null  ,  decoderFamily  ,  null  ,  null  ,  null  ,  decoderModel  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "found "  +  l  .  size  (  )  +  " matches"  )  ; 
if  (  l  .  size  (  )  ==  0  )  { 
log  .  debug  (  "Loco uses "  +  decoderFamily  +  " "  +  decoderModel  +  " decoder, but no such decoder defined"  )  ; 
l  =  DecoderIndexFile  .  instance  (  )  .  matchingDecoderList  (  null  ,  null  ,  null  ,  null  ,  null  ,  decoderModel  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "found "  +  l  .  size  (  )  +  " matches without family key"  )  ; 
} 
if  (  l  .  size  (  )  >  0  )  { 
DecoderFile   d  =  l  .  get  (  0  )  ; 
loadDecoderFile  (  d  ,  r  )  ; 
}  else  { 
if  (  decoderModel  .  equals  (  ""  )  )  log  .  debug  (  "blank decoderModel requested, so nothing loaded"  )  ;  else   log  .  warn  (  "no matching \""  +  decoderModel  +  "\" decoder found for loco, no decoder info loaded"  )  ; 
} 
} 

protected   void   loadDecoderFile  (  DecoderFile   df  ,  RosterEntry   re  )  { 
if  (  df  ==  null  )  { 
log  .  warn  (  "loadDecoder file invoked with null object"  )  ; 
return  ; 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "loadDecoderFile from "  +  DecoderFile  .  fileLocation  +  " "  +  df  .  getFilename  (  )  )  ; 
try  { 
decoderRoot  =  df  .  rootFromName  (  DecoderFile  .  fileLocation  +  df  .  getFilename  (  )  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "Exception while loading decoder XML file: "  +  df  .  getFilename  (  )  ,  e  )  ; 
} 
df  .  getProductID  (  )  ; 
df  .  loadVariableModel  (  decoderRoot  .  getChild  (  "decoder"  )  ,  variableModel  )  ; 
if  (  variableModel  .  piCv  (  )  >=  0  )  { 
resetModel  .  setPiCv  (  variableModel  .  piCv  (  )  )  ; 
} 
if  (  variableModel  .  siCv  (  )  >=  0  )  { 
resetModel  .  setSiCv  (  variableModel  .  siCv  (  )  )  ; 
} 
df  .  loadResetModel  (  decoderRoot  .  getChild  (  "decoder"  )  ,  resetModel  )  ; 
re  .  loadFunctions  (  decoderRoot  .  getChild  (  "decoder"  )  .  getChild  (  "family"  )  .  getChild  (  "functionlabels"  )  )  ; 
if  (  decoderRoot  .  getAttribute  (  "showEmptyPanes"  )  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "Found in decoder "  +  decoderRoot  .  getAttribute  (  "showEmptyPanes"  )  .  getValue  (  )  )  ; 
if  (  decoderRoot  .  getAttribute  (  "showEmptyPanes"  )  .  getValue  (  )  .  equals  (  "yes"  )  )  setShowEmptyPanes  (  true  )  ;  else   if  (  decoderRoot  .  getAttribute  (  "showEmptyPanes"  )  .  getValue  (  )  .  equals  (  "no"  )  )  setShowEmptyPanes  (  false  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "result "  +  getShowEmptyPanes  (  )  )  ; 
} 
modelElem  =  df  .  getModelElement  (  )  ; 
} 

protected   void   loadProgrammerFile  (  RosterEntry   r  )  { 
XmlFile   pf  =  new   XmlFile  (  )  { 
}  ; 
try  { 
programmerRoot  =  pf  .  rootFromName  (  filename  )  ; 
if  (  programmerRoot  .  getChild  (  "programmer"  )  .  getAttribute  (  "showEmptyPanes"  )  !=  null  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "Found in programmer "  +  programmerRoot  .  getChild  (  "programmer"  )  .  getAttribute  (  "showEmptyPanes"  )  .  getValue  (  )  )  ; 
if  (  programmerRoot  .  getChild  (  "programmer"  )  .  getAttribute  (  "showEmptyPanes"  )  .  getValue  (  )  .  equals  (  "yes"  )  )  setShowEmptyPanes  (  true  )  ;  else   if  (  programmerRoot  .  getChild  (  "programmer"  )  .  getAttribute  (  "showEmptyPanes"  )  .  getValue  (  )  .  equals  (  "no"  )  )  setShowEmptyPanes  (  false  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "result "  +  getShowEmptyPanes  (  )  )  ; 
} 
readConfig  (  programmerRoot  ,  r  )  ; 
}  catch  (  Exception   e  )  { 
log  .  error  (  "exception reading programmer file: "  +  filename  ,  e  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 

Element   programmerRoot  =  null  ; 




protected   boolean   checkDirtyDecoder  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "Checking decoder dirty status. CV: "  +  cvModel  .  decoderDirty  (  )  +  " variables:"  +  variableModel  .  decoderDirty  (  )  )  ; 
return  (  getModePane  (  )  !=  null  &&  (  cvModel  .  decoderDirty  (  )  ||  variableModel  .  decoderDirty  (  )  )  )  ; 
} 




protected   boolean   checkDirtyFile  (  )  { 
return  (  variableModel  .  fileDirty  (  )  ||  _rPane  .  guiChanged  (  _rosterEntry  )  ||  _flPane  .  guiChanged  (  _rosterEntry  )  ||  _rMPane  .  guiChanged  (  _rosterEntry  )  )  ; 
} 

protected   void   handleDirtyFile  (  )  { 
} 






public   void   windowClosing  (  java  .  awt  .  event  .  WindowEvent   e  )  { 
setDefaultCloseOperation  (  WindowConstants  .  DO_NOTHING_ON_CLOSE  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "Checking decoder dirty status. CV: "  +  cvModel  .  decoderDirty  (  )  +  " variables:"  +  variableModel  .  decoderDirty  (  )  )  ; 
if  (  checkDirtyDecoder  (  )  )  { 
if  (  JOptionPane  .  showConfirmDialog  (  null  ,  rbt  .  getString  (  "PromptCloseWindowNotWrittenDecoder"  )  ,  rbt  .  getString  (  "PromptChooseOne"  )  ,  JOptionPane  .  OK_CANCEL_OPTION  )  ==  JOptionPane  .  CANCEL_OPTION  )  return  ; 
} 
if  (  checkDirtyFile  (  )  )  { 
int   option  =  JOptionPane  .  showOptionDialog  (  null  ,  rbt  .  getString  (  "PromptCloseWindowNotWrittenConfig"  )  ,  rbt  .  getString  (  "PromptChooseOne"  )  ,  JOptionPane  .  YES_NO_CANCEL_OPTION  ,  JOptionPane  .  WARNING_MESSAGE  ,  null  ,  new   String  [  ]  {  rbt  .  getString  (  "PromptSaveAndClose"  )  ,  rbt  .  getString  (  "PromptClose"  )  ,  rbt  .  getString  (  "PromptCancel"  )  }  ,  rbt  .  getString  (  "PromptSaveAndClose"  )  )  ; 
if  (  option  ==  0  )  { 
if  (  !  storeFile  (  )  )  return  ; 
}  else   if  (  option  ==  2  )  { 
return  ; 
} 
} 
List  <  RosterEntry  >  l  =  Roster  .  instance  (  )  .  matchingList  (  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  rbt  .  getString  (  "LabelNewDecoder"  )  )  ; 
if  (  l  .  size  (  )  >  0  &&  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "Removing "  +  l  .  size  (  )  +  " <new loco> entries"  )  ; 
while  (  l  .  size  (  )  >  0  )  { 
Roster  .  instance  (  )  .  removeEntry  (  l  .  get  (  0  )  )  ; 
l  =  Roster  .  instance  (  )  .  matchingList  (  null  ,  null  ,  null  ,  null  ,  null  ,  null  ,  rbt  .  getString  (  "LabelNewDecoder"  )  )  ; 
} 
setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  DISPOSE_ON_CLOSE  )  ; 
if  (  jmri  .  InstanceManager  .  shutDownManagerInstance  (  )  !=  null  )  jmri  .  InstanceManager  .  shutDownManagerInstance  (  )  .  deregister  (  decoderDirtyTask  )  ; 
decoderDirtyTask  =  null  ; 
if  (  jmri  .  InstanceManager  .  shutDownManagerInstance  (  )  !=  null  )  jmri  .  InstanceManager  .  shutDownManagerInstance  (  )  .  deregister  (  fileDirtyTask  )  ; 
fileDirtyTask  =  null  ; 
super  .  windowClosing  (  e  )  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
void   readConfig  (  Element   root  ,  RosterEntry   r  )  { 
Element   base  ; 
if  (  (  base  =  root  .  getChild  (  "programmer"  )  )  ==  null  )  { 
log  .  error  (  "xml file top element is not programmer"  )  ; 
return  ; 
} 
tabPane  .  addTab  (  rbt  .  getString  (  "ROSTER ENTRY"  )  ,  makeInfoPane  (  r  )  )  ; 
if  (  root  .  getChild  (  "programmer"  )  .  getAttribute  (  "showFnLanelPane"  )  .  getValue  (  )  .  equals  (  "yes"  )  )  { 
tabPane  .  addTab  (  rbt  .  getString  (  "FUNCTION LABELS"  )  ,  makeFunctionLabelPane  (  r  )  )  ; 
}  else  { 
makeFunctionLabelPane  (  r  )  ; 
} 
if  (  root  .  getChild  (  "programmer"  )  .  getAttribute  (  "showRosterMediaPane"  )  .  getValue  (  )  .  equals  (  "yes"  )  )  { 
tabPane  .  addTab  (  rbt  .  getString  (  "ROSTER MEDIA"  )  ,  makeMediaPane  (  r  )  )  ; 
}  else  { 
makeMediaPane  (  r  )  ; 
} 
List  <  Element  >  paneList  =  base  .  getChildren  (  "pane"  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "will process "  +  paneList  .  size  (  )  +  " pane definitions"  )  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
String   name  =  paneList  .  get  (  i  )  .  getAttribute  (  "name"  )  .  getValue  (  )  ; 
newPane  (  name  ,  paneList  .  get  (  i  )  ,  modelElem  ,  false  )  ; 
} 
} 





protected   void   resetToDefaults  (  )  { 
int   n  =  defaultCvValues  .  length  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
CvValue   cv  =  cvModel  .  getCvByNumber  (  defaultCvNumbers  [  i  ]  )  ; 
if  (  cv  ==  null  )  log  .  warn  (  "Trying to set default in CV "  +  defaultCvNumbers  [  i  ]  +  " but didn't find the CV object"  )  ;  else   cv  .  setValue  (  defaultCvValues  [  i  ]  )  ; 
} 
n  =  defaultIndexedCvValues  .  length  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
CvValue   cv  =  iCvModel  .  getCvByRow  (  i  )  ; 
if  (  cv  ==  null  )  log  .  warn  (  "Trying to set default in indexed CV from row "  +  i  +  " but didn't find the CV object"  )  ;  else   cv  .  setValue  (  defaultIndexedCvValues  [  i  ]  )  ; 
} 
} 

int   defaultCvValues  [  ]  =  null  ; 

int   defaultCvNumbers  [  ]  =  null  ; 

int   defaultIndexedCvValues  [  ]  =  null  ; 





protected   void   saveDefaults  (  )  { 
int   n  =  cvModel  .  getRowCount  (  )  ; 
defaultCvValues  =  new   int  [  n  ]  ; 
defaultCvNumbers  =  new   int  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
CvValue   cv  =  cvModel  .  getCvByRow  (  i  )  ; 
defaultCvValues  [  i  ]  =  cv  .  getValue  (  )  ; 
defaultCvNumbers  [  i  ]  =  cv  .  number  (  )  ; 
} 
n  =  iCvModel  .  getRowCount  (  )  ; 
defaultIndexedCvValues  =  new   int  [  n  ]  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
CvValue   cv  =  iCvModel  .  getCvByRow  (  i  )  ; 
defaultIndexedCvValues  [  i  ]  =  cv  .  getValue  (  )  ; 
} 
} 

protected   JPanel   makeInfoPane  (  RosterEntry   r  )  { 
JPanel   outer  =  new   JPanel  (  )  ; 
outer  .  setLayout  (  new   BoxLayout  (  outer  ,  BoxLayout  .  Y_AXIS  )  )  ; 
JPanel   body  =  new   JPanel  (  )  ; 
body  .  setLayout  (  new   BoxLayout  (  body  ,  BoxLayout  .  Y_AXIS  )  )  ; 
JScrollPane   scrollPane  =  new   JScrollPane  (  body  )  ; 
_rPane  =  new   RosterEntryPane  (  r  )  ; 
_rPane  .  setMaximumSize  (  _rPane  .  getPreferredSize  (  )  )  ; 
body  .  add  (  _rPane  )  ; 
JButton   store  =  new   JButton  (  rbt  .  getString  (  "ButtonSave"  )  )  ; 
store  .  setAlignmentX  (  JLabel  .  CENTER_ALIGNMENT  )  ; 
store  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   e  )  { 
storeFile  (  )  ; 
} 
}  )  ; 
JButton   reset  =  new   JButton  (  rbt  .  getString  (  "ButtonResetDefaults"  )  )  ; 
reset  .  setAlignmentX  (  JLabel  .  CENTER_ALIGNMENT  )  ; 
store  .  setPreferredSize  (  reset  .  getPreferredSize  (  )  )  ; 
reset  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   e  )  { 
resetToDefaults  (  )  ; 
} 
}  )  ; 
store  .  setPreferredSize  (  reset  .  getPreferredSize  (  )  )  ; 
JPanel   buttons  =  new   JPanel  (  )  ; 
buttons  .  setLayout  (  new   BoxLayout  (  buttons  ,  BoxLayout  .  X_AXIS  )  )  ; 
buttons  .  add  (  store  )  ; 
buttons  .  add  (  reset  )  ; 
body  .  add  (  buttons  )  ; 
outer  .  add  (  scrollPane  )  ; 
java  .  beans  .  PropertyChangeListener   dccNews  =  new   java  .  beans  .  PropertyChangeListener  (  )  { 

public   void   propertyChange  (  java  .  beans  .  PropertyChangeEvent   e  )  { 
updateDccAddress  (  )  ; 
} 
}  ; 
primaryAddr  =  variableModel  .  findVar  (  "Short Address"  )  ; 
if  (  primaryAddr  ==  null  )  log  .  debug  (  "DCC Address monitor didnt find a Short Address variable"  )  ;  else   primaryAddr  .  addPropertyChangeListener  (  dccNews  )  ; 
extendAddr  =  variableModel  .  findVar  (  "Long Address"  )  ; 
if  (  extendAddr  ==  null  )  log  .  debug  (  "DCC Address monitor didnt find an Long Address variable"  )  ;  else   extendAddr  .  addPropertyChangeListener  (  dccNews  )  ; 
addMode  =  variableModel  .  findVar  (  "Address Format"  )  ; 
if  (  addMode  ==  null  )  log  .  debug  (  "DCC Address monitor didnt find an Address Format variable"  )  ;  else   addMode  .  addPropertyChangeListener  (  dccNews  )  ; 
return   outer  ; 
} 

protected   JPanel   makeFunctionLabelPane  (  RosterEntry   r  )  { 
JPanel   outer  =  new   JPanel  (  )  ; 
outer  .  setLayout  (  new   BoxLayout  (  outer  ,  BoxLayout  .  Y_AXIS  )  )  ; 
JPanel   body  =  new   JPanel  (  )  ; 
body  .  setLayout  (  new   BoxLayout  (  body  ,  BoxLayout  .  Y_AXIS  )  )  ; 
JScrollPane   scrollPane  =  new   JScrollPane  (  body  )  ; 
JLabel   title  =  new   JLabel  (  rbt  .  getString  (  "UseThisTabCustomize"  )  )  ; 
title  .  setAlignmentX  (  JLabel  .  CENTER_ALIGNMENT  )  ; 
body  .  add  (  title  )  ; 
body  .  add  (  new   JLabel  (  " "  )  )  ; 
_flPane  =  new   FunctionLabelPane  (  r  )  ; 
body  .  add  (  _flPane  )  ; 
JButton   store  =  new   JButton  (  rbt  .  getString  (  "ButtonSave"  )  )  ; 
store  .  setAlignmentX  (  JLabel  .  CENTER_ALIGNMENT  )  ; 
store  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   e  )  { 
storeFile  (  )  ; 
} 
}  )  ; 
JPanel   buttons  =  new   JPanel  (  )  ; 
buttons  .  setLayout  (  new   BoxLayout  (  buttons  ,  BoxLayout  .  X_AXIS  )  )  ; 
buttons  .  add  (  store  )  ; 
body  .  add  (  buttons  )  ; 
outer  .  add  (  scrollPane  )  ; 
return   outer  ; 
} 

protected   JPanel   makeMediaPane  (  RosterEntry   r  )  { 
JPanel   outer  =  new   JPanel  (  )  ; 
outer  .  setLayout  (  new   BoxLayout  (  outer  ,  BoxLayout  .  Y_AXIS  )  )  ; 
JPanel   body  =  new   JPanel  (  )  ; 
body  .  setLayout  (  new   BoxLayout  (  body  ,  BoxLayout  .  Y_AXIS  )  )  ; 
JScrollPane   scrollPane  =  new   JScrollPane  (  body  )  ; 
JLabel   title  =  new   JLabel  (  rbt  .  getString  (  "UseThisTabMedia"  )  )  ; 
title  .  setAlignmentX  (  JLabel  .  CENTER_ALIGNMENT  )  ; 
body  .  add  (  title  )  ; 
body  .  add  (  new   JLabel  (  " "  )  )  ; 
_rMPane  =  new   RosterMediaPane  (  r  )  ; 
_rMPane  .  setMaximumSize  (  _rMPane  .  getPreferredSize  (  )  )  ; 
body  .  add  (  _rMPane  )  ; 
JButton   store  =  new   JButton  (  rbt  .  getString  (  "ButtonSave"  )  )  ; 
store  .  setAlignmentX  (  JLabel  .  CENTER_ALIGNMENT  )  ; 
store  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   e  )  { 
storeFile  (  )  ; 
} 
}  )  ; 
JPanel   buttons  =  new   JPanel  (  )  ; 
buttons  .  setLayout  (  new   BoxLayout  (  buttons  ,  BoxLayout  .  X_AXIS  )  )  ; 
buttons  .  add  (  store  )  ; 
body  .  add  (  buttons  )  ; 
outer  .  add  (  scrollPane  )  ; 
return   outer  ; 
} 

VariableValue   primaryAddr  =  null  ; 

VariableValue   extendAddr  =  null  ; 

VariableValue   addMode  =  null  ; 

void   updateDccAddress  (  )  { 
boolean   longMode  =  false  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "updateDccAddress: short "  +  (  primaryAddr  ==  null  ?  "<null>"  :  primaryAddr  .  getValueString  (  )  )  +  " long "  +  (  extendAddr  ==  null  ?  "<null>"  :  extendAddr  .  getValueString  (  )  )  +  " mode "  +  (  addMode  ==  null  ?  "<null>"  :  addMode  .  getValueString  (  )  )  )  ; 
String   newAddr  =  null  ; 
if  (  addMode  ==  null  ||  extendAddr  ==  null  ||  !  addMode  .  getValueString  (  )  .  equals  (  "1"  )  )  { 
longMode  =  false  ; 
if  (  primaryAddr  !=  null  &&  !  primaryAddr  .  getValueString  (  )  .  equals  (  ""  )  )  newAddr  =  primaryAddr  .  getValueString  (  )  ; 
}  else  { 
if  (  extendAddr  !=  null  &&  !  extendAddr  .  getValueString  (  )  .  equals  (  ""  )  )  longMode  =  true  ; 
newAddr  =  extendAddr  .  getValueString  (  )  ; 
} 
if  (  newAddr  !=  null  )  { 
_rPane  .  setDccAddress  (  newAddr  )  ; 
_rPane  .  setDccAddressLong  (  longMode  )  ; 
} 
} 

public   void   newPane  (  String   name  ,  Element   pane  ,  Element   modelElem  ,  boolean   enableEmpty  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "newPane with enableEmpty "  +  enableEmpty  +  " getShowEmptyPanes() "  +  getShowEmptyPanes  (  )  )  ; 
PaneProgPane   p  =  new   PaneProgPane  (  this  ,  name  ,  pane  ,  cvModel  ,  iCvModel  ,  variableModel  ,  modelElem  )  ; 
p  .  setOpaque  (  true  )  ; 
if  (  enableEmpty  ||  (  p  .  cvList  .  size  (  )  !=  0  )  ||  (  p  .  varList  .  size  (  )  !=  0  ||  (  p  .  indexedCvList  .  size  (  )  !=  0  )  )  )  { 
tabPane  .  addTab  (  name  ,  p  )  ; 
}  else   if  (  getShowEmptyPanes  (  )  )  { 
tabPane  .  addTab  (  name  ,  p  )  ; 
int   index  =  tabPane  .  indexOfTab  (  name  )  ; 
tabPane  .  setEnabledAt  (  index  ,  false  )  ; 
tabPane  .  setToolTipTextAt  (  index  ,  rbt  .  getString  (  "TipTabDisabledNoCategory"  )  )  ; 
}  else  { 
} 
paneList  .  add  (  p  )  ; 
} 

public   BusyGlassPane   getBusyGlassPane  (  )  { 
return   glassPane  ; 
} 




public   void   prepGlassPane  (  AbstractButton   activeButton  )  { 
List  <  Rectangle  >  rectangles  =  new   ArrayList  <  Rectangle  >  (  )  ; 
if  (  glassPane  !=  null  )  { 
glassPane  .  dispose  (  )  ; 
} 
activeComponents  .  clear  (  )  ; 
activeComponents  .  add  (  activeButton  )  ; 
if  (  activeButton  ==  readChangesButton  ||  activeButton  ==  readAllButton  ||  activeButton  ==  writeChangesButton  ||  activeButton  ==  writeAllButton  )  { 
if  (  activeButton  ==  readChangesButton  )  { 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
activeComponents  .  add  (  (  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  readChangesButton  )  ; 
} 
}  else   if  (  activeButton  ==  readAllButton  )  { 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
activeComponents  .  add  (  (  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  readAllButton  )  ; 
} 
}  else   if  (  activeButton  ==  writeChangesButton  )  { 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
activeComponents  .  add  (  (  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  writeChangesButton  )  ; 
} 
}  else   if  (  activeButton  ==  writeAllButton  )  { 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
activeComponents  .  add  (  (  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  writeAllButton  )  ; 
} 
} 
for  (  int   i  =  0  ;  i  <  tabPane  .  getTabCount  (  )  ;  i  ++  )  { 
rectangles  .  add  (  tabPane  .  getUI  (  )  .  getTabBounds  (  tabPane  ,  i  )  )  ; 
} 
} 
glassPane  =  new   BusyGlassPane  (  activeComponents  ,  rectangles  ,  this  .  getContentPane  (  )  ,  this  )  ; 
this  .  setGlassPane  (  glassPane  )  ; 
} 

public   void   paneFinished  (  )  { 
if  (  !  isBusy  (  )  )  { 
if  (  glassPane  !=  null  )  { 
glassPane  .  setVisible  (  false  )  ; 
glassPane  .  dispose  (  )  ; 
glassPane  =  null  ; 
} 
setCursor  (  Cursor  .  getDefaultCursor  (  )  )  ; 
enableButtons  (  true  )  ; 
} 
} 










public   void   enableButtons  (  boolean   stat  )  { 
if  (  stat  )  { 
enableReadButtons  (  )  ; 
}  else  { 
readChangesButton  .  setEnabled  (  false  )  ; 
readAllButton  .  setEnabled  (  false  )  ; 
} 
writeChangesButton  .  setEnabled  (  stat  )  ; 
writeAllButton  .  setEnabled  (  stat  )  ; 
if  (  modePane  !=  null  )  { 
modePane  .  setEnabled  (  stat  )  ; 
} 
} 

boolean   justChanges  ; 

public   boolean   isBusy  (  )  { 
return   _busy  ; 
} 

private   boolean   _busy  =  false  ; 

private   void   setBusy  (  boolean   stat  )  { 
_busy  =  stat  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
if  (  stat  )  { 
(  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  enableButtons  (  false  )  ; 
}  else  { 
(  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  enableButtons  (  true  )  ; 
} 
} 
if  (  !  stat  )  { 
paneFinished  (  )  ; 
} 
} 










public   boolean   readChanges  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "readChanges starts"  )  ; 
justChanges  =  true  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
(  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  setToRead  (  justChanges  ,  true  )  ; 
} 
setBusy  (  true  )  ; 
enableButtons  (  false  )  ; 
readChangesButton  .  setEnabled  (  true  )  ; 
glassPane  .  setVisible  (  true  )  ; 
paneListIndex  =  0  ; 
return   doRead  (  )  ; 
} 










public   boolean   readAll  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "readAll starts"  )  ; 
justChanges  =  false  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
(  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  setToRead  (  justChanges  ,  true  )  ; 
} 
setBusy  (  true  )  ; 
enableButtons  (  false  )  ; 
readAllButton  .  setEnabled  (  true  )  ; 
glassPane  .  setVisible  (  true  )  ; 
paneListIndex  =  0  ; 
return   doRead  (  )  ; 
} 

boolean   doRead  (  )  { 
_read  =  true  ; 
while  (  paneListIndex  <  paneList  .  size  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "doRead on "  +  paneListIndex  )  ; 
_programmingPane  =  (  PaneProgPane  )  paneList  .  get  (  paneListIndex  )  ; 
_programmingPane  .  addPropertyChangeListener  (  this  )  ; 
boolean   running  ; 
if  (  justChanges  )  running  =  _programmingPane  .  readPaneChanges  (  )  ;  else   running  =  _programmingPane  .  readPaneAll  (  )  ; 
paneListIndex  ++  ; 
if  (  running  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "doRead expecting callback from readPane "  +  paneListIndex  )  ; 
return   true  ; 
}  else  { 
_programmingPane  .  removePropertyChangeListener  (  this  )  ; 
} 
} 
_programmingPane  =  null  ; 
enableButtons  (  true  )  ; 
setBusy  (  false  )  ; 
readChangesButton  .  setSelected  (  false  )  ; 
readAllButton  .  setSelected  (  false  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "doRead found nothing to do"  )  ; 
return   false  ; 
} 










public   boolean   writeAll  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "writeAll starts"  )  ; 
justChanges  =  false  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
(  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  setToWrite  (  justChanges  ,  true  )  ; 
} 
setBusy  (  true  )  ; 
enableButtons  (  false  )  ; 
writeAllButton  .  setEnabled  (  true  )  ; 
glassPane  .  setVisible  (  true  )  ; 
paneListIndex  =  0  ; 
return   doWrite  (  )  ; 
} 










public   boolean   writeChanges  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "writeChanges starts"  )  ; 
justChanges  =  true  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
(  (  PaneProgPane  )  paneList  .  get  (  i  )  )  .  setToWrite  (  justChanges  ,  true  )  ; 
} 
setBusy  (  true  )  ; 
enableButtons  (  false  )  ; 
writeChangesButton  .  setEnabled  (  true  )  ; 
glassPane  .  setVisible  (  true  )  ; 
paneListIndex  =  0  ; 
return   doWrite  (  )  ; 
} 

boolean   doWrite  (  )  { 
_read  =  false  ; 
while  (  paneListIndex  <  paneList  .  size  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "doWrite starts on "  +  paneListIndex  )  ; 
_programmingPane  =  (  PaneProgPane  )  paneList  .  get  (  paneListIndex  )  ; 
_programmingPane  .  addPropertyChangeListener  (  this  )  ; 
boolean   running  ; 
if  (  justChanges  )  running  =  _programmingPane  .  writePaneChanges  (  )  ;  else   running  =  _programmingPane  .  writePaneAll  (  )  ; 
paneListIndex  ++  ; 
if  (  running  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "doWrite expecting callback from writePane "  +  paneListIndex  )  ; 
return   true  ; 
}  else   _programmingPane  .  removePropertyChangeListener  (  this  )  ; 
} 
_programmingPane  =  null  ; 
enableButtons  (  true  )  ; 
setBusy  (  false  )  ; 
writeChangesButton  .  setSelected  (  false  )  ; 
writeAllButton  .  setSelected  (  false  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "doWrite found nothing to do"  )  ; 
return   false  ; 
} 

public   void   doPrintPanes  (  boolean   preview  )  { 
HardcopyWriter   w  =  null  ; 
try  { 
w  =  new   HardcopyWriter  (  this  ,  getRosterEntry  (  )  .  getId  (  )  ,  10  ,  .8  ,  .5  ,  .5  ,  .5  ,  preview  )  ; 
}  catch  (  HardcopyWriter  .  PrintCanceledException   ex  )  { 
log  .  debug  (  "Print cancelled"  )  ; 
return  ; 
} 
printInfoSection  (  w  )  ; 
if  (  _flPane  .  includeInPrint  (  )  )  _flPane  .  printPane  (  w  )  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "start printing page "  +  i  )  ; 
PaneProgPane   pane  =  (  PaneProgPane  )  paneList  .  get  (  i  )  ; 
if  (  pane  .  includeInPrint  (  )  )  pane  .  printPane  (  w  )  ; 
} 
w  .  write  (  w  .  getCurrentLineNumber  (  )  ,  0  ,  w  .  getCurrentLineNumber  (  )  ,  w  .  getCharactersPerLine  (  )  +  1  )  ; 
w  .  close  (  )  ; 
} 

public   void   printPanes  (  final   boolean   preview  )  { 
final   JFrame   frame  =  new   JFrame  (  "Select Items to Print"  )  ; 
JPanel   p1  =  new   JPanel  (  )  ; 
p1  .  setLayout  (  new   BoxLayout  (  p1  ,  BoxLayout  .  PAGE_AXIS  )  )  ; 
JLabel   l1  =  new   JLabel  (  "Select the items that you"  )  ; 
p1  .  add  (  l1  )  ; 
l1  =  new   JLabel  (  "wish to appear in the print out"  )  ; 
p1  .  add  (  l1  )  ; 
JPanel   select  =  new   JPanel  (  )  ; 
final   Hashtable  <  JCheckBox  ,  PaneProgPane  >  printList  =  new   Hashtable  <  JCheckBox  ,  PaneProgPane  >  (  )  ; 
select  .  setLayout  (  new   BoxLayout  (  select  ,  BoxLayout  .  PAGE_AXIS  )  )  ; 
final   JCheckBox   funct  =  new   JCheckBox  (  "Function List"  )  ; 
funct  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
_flPane  .  includeInPrint  (  funct  .  isSelected  (  )  )  ; 
} 
}  )  ; 
_flPane  .  includeInPrint  (  false  )  ; 
select  .  add  (  funct  )  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
final   PaneProgPane   pane  =  (  PaneProgPane  )  paneList  .  get  (  i  )  ; 
pane  .  includeInPrint  (  false  )  ; 
final   JCheckBox   item  =  new   JCheckBox  (  paneList  .  get  (  i  )  .  getName  (  )  )  ; 
printList  .  put  (  item  ,  pane  )  ; 
item  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
pane  .  includeInPrint  (  item  .  isSelected  (  )  )  ; 
} 
}  )  ; 
select  .  add  (  item  )  ; 
} 
final   JCheckBox   selectAll  =  new   JCheckBox  (  "Select All"  )  ; 
selectAll  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
_flPane  .  includeInPrint  (  selectAll  .  isSelected  (  )  )  ; 
funct  .  setSelected  (  selectAll  .  isSelected  (  )  )  ; 
Enumeration  <  JCheckBox  >  en  =  printList  .  keys  (  )  ; 
while  (  en  .  hasMoreElements  (  )  )  { 
JCheckBox   check  =  en  .  nextElement  (  )  ; 
printList  .  get  (  check  )  .  includeInPrint  (  selectAll  .  isSelected  (  )  )  ; 
check  .  setSelected  (  selectAll  .  isSelected  (  )  )  ; 
} 
} 
}  )  ; 
select  .  add  (  selectAll  )  ; 
JButton   cancel  =  new   JButton  (  "Cancel"  )  ; 
JButton   ok  =  new   JButton  (  "Okay"  )  ; 
cancel  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
frame  .  dispose  (  )  ; 
} 
}  )  ; 
ok  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
doPrintPanes  (  preview  )  ; 
frame  .  dispose  (  )  ; 
} 
}  )  ; 
JPanel   buttons  =  new   JPanel  (  )  ; 
buttons  .  add  (  cancel  )  ; 
buttons  .  add  (  ok  )  ; 
p1  .  add  (  select  )  ; 
p1  .  add  (  buttons  )  ; 
frame  .  add  (  p1  )  ; 
frame  .  pack  (  )  ; 
frame  .  setVisible  (  true  )  ; 
} 

public   void   printInfoSection  (  HardcopyWriter   w  )  { 
ImageIcon   icon  =  new   ImageIcon  (  ClassLoader  .  getSystemResource  (  "resources/decoderpro.gif"  )  )  ; 
w  .  write  (  icon  .  getImage  (  )  ,  new   JLabel  (  icon  )  )  ; 
w  .  setFontStyle  (  Font  .  BOLD  )  ; 
int   height  =  icon  .  getImage  (  )  .  getHeight  (  null  )  ; 
int   blanks  =  (  height  -  w  .  getLineAscent  (  )  )  /  w  .  getLineHeight  (  )  ; 
try  { 
for  (  int   i  =  0  ;  i  <  blanks  ;  i  ++  )  { 
String   s  =  "\n"  ; 
w  .  write  (  s  ,  0  ,  s  .  length  (  )  )  ; 
} 
}  catch  (  IOException   e  )  { 
log  .  warn  (  "error during printing: "  +  e  )  ; 
} 
_rosterEntry  .  printEntry  (  w  )  ; 
w  .  setFontStyle  (  Font  .  PLAIN  )  ; 
} 

boolean   _read  =  true  ; 

PaneProgPane   _programmingPane  =  null  ; 






public   void   propertyChange  (  java  .  beans  .  PropertyChangeEvent   e  )  { 
if  (  _programmingPane  ==  null  )  { 
log  .  warn  (  "unexpected propertyChange: "  +  e  )  ; 
return  ; 
}  else   if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "property changed: "  +  e  .  getPropertyName  (  )  +  " new value: "  +  e  .  getNewValue  (  )  )  ; 
log  .  debug  (  "check valid: "  +  (  e  .  getSource  (  )  ==  _programmingPane  )  +  " "  +  (  !  e  .  getPropertyName  (  )  .  equals  (  "Busy"  )  )  +  " "  +  (  (  (  Boolean  )  e  .  getNewValue  (  )  )  .  equals  (  Boolean  .  FALSE  )  )  )  ; 
if  (  e  .  getSource  (  )  ==  _programmingPane  &&  e  .  getPropertyName  (  )  .  equals  (  "Busy"  )  &&  (  (  Boolean  )  e  .  getNewValue  (  )  )  .  equals  (  Boolean  .  FALSE  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "end of a programming pane operation, remove"  )  ; 
_programmingPane  .  removePropertyChangeListener  (  this  )  ; 
_programmingPane  =  null  ; 
if  (  _read  &&  readChangesButton  .  isSelected  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "restart readChanges"  )  ; 
doRead  (  )  ; 
}  else   if  (  _read  &&  readAllButton  .  isSelected  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "restart readAll"  )  ; 
doRead  (  )  ; 
}  else   if  (  writeChangesButton  .  isSelected  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "restart writeChanges"  )  ; 
doWrite  (  )  ; 
}  else   if  (  writeAllButton  .  isSelected  (  )  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "restart writeAll"  )  ; 
doWrite  (  )  ; 
}  else  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "read/write end because button is lifted"  )  ; 
setBusy  (  false  )  ; 
} 
} 
} 





public   boolean   storeFile  (  )  { 
log  .  debug  (  "storeFile starts"  )  ; 
if  (  _rPane  .  checkDuplicate  (  )  )  { 
JOptionPane  .  showMessageDialog  (  this  ,  rbt  .  getString  (  "ErrorDuplicateID"  )  )  ; 
return   false  ; 
} 
updateDccAddress  (  )  ; 
_rPane  .  update  (  _rosterEntry  )  ; 
_flPane  .  update  (  _rosterEntry  )  ; 
_rMPane  .  update  (  _rosterEntry  )  ; 
if  (  _rosterEntry  .  getId  (  )  .  equals  (  ""  )  ||  _rosterEntry  .  getId  (  )  .  equals  (  rbt  .  getString  (  "LabelNewDecoder"  )  )  )  { 
log  .  debug  (  "storeFile without a filename; issued dialog"  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  rbt  .  getString  (  "PromptFillInID"  )  )  ; 
return   false  ; 
} 
_rosterEntry  .  ensureFilenameExists  (  )  ; 
String   filename  =  _rosterEntry  .  getFileName  (  )  ; 
_rosterEntry  .  writeFile  (  cvModel  ,  iCvModel  ,  variableModel  )  ; 
variableModel  .  setFileDirty  (  false  )  ; 
XmlFile  .  ensurePrefsPresent  (  XmlFile  .  prefsDir  (  )  )  ; 
Roster  .  writeRosterFile  (  )  ; 
_rPane  .  updateGUI  (  _rosterEntry  )  ; 
progStatus  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateSaveOK"  )  ,  new   Object  [  ]  {  filename  }  )  )  ; 
return   true  ; 
} 






public   void   dispose  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "dispose local"  )  ; 
readChangesButton  .  removeItemListener  (  l1  )  ; 
writeChangesButton  .  removeItemListener  (  l2  )  ; 
readAllButton  .  removeItemListener  (  l3  )  ; 
writeAllButton  .  removeItemListener  (  l4  )  ; 
if  (  _programmingPane  !=  null  )  _programmingPane  .  removePropertyChangeListener  (  this  )  ; 
for  (  int   i  =  0  ;  i  <  paneList  .  size  (  )  ;  i  ++  )  { 
PaneProgPane   p  =  (  PaneProgPane  )  paneList  .  get  (  i  )  ; 
p  .  dispose  (  )  ; 
} 
paneList  .  clear  (  )  ; 
_rPane  .  dispose  (  )  ; 
_flPane  .  dispose  (  )  ; 
_rMPane  .  dispose  (  )  ; 
variableModel  .  dispose  (  )  ; 
cvModel  .  dispose  (  )  ; 
iCvModel  .  dispose  (  )  ; 
progStatus  =  null  ; 
cvModel  =  null  ; 
iCvModel  =  null  ; 
variableModel  =  null  ; 
_rosterEntry  =  null  ; 
_rPane  =  null  ; 
_flPane  =  null  ; 
_rMPane  =  null  ; 
paneList  .  clear  (  )  ; 
paneList  =  null  ; 
_programmingPane  =  null  ; 
tabPane  =  null  ; 
readChangesButton  =  null  ; 
writeChangesButton  =  null  ; 
readAllButton  =  null  ; 
writeAllButton  =  null  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "dispose superclass"  )  ; 
removeAll  (  )  ; 
super  .  dispose  (  )  ; 
} 




public   static   void   setShowEmptyPanes  (  boolean   yes  )  { 
showEmptyPanes  =  yes  ; 
} 

public   static   boolean   getShowEmptyPanes  (  )  { 
return   showEmptyPanes  ; 
} 

static   boolean   showEmptyPanes  =  true  ; 

public   RosterEntry   getRosterEntry  (  )  { 
return   _rosterEntry  ; 
} 

static   org  .  apache  .  log4j  .  Logger   log  =  org  .  apache  .  log4j  .  Logger  .  getLogger  (  PaneProgFrame  .  class  .  getName  (  )  )  ; 
} 

