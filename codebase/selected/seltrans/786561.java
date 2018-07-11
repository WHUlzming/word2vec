package   com  .  sparkit  .  extracta  .  builder  ; 

import   java  .  awt  .  *  ; 
import   java  .  awt  .  event  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  util  .  *  ; 
import   javax  .  swing  .  *  ; 
import   org  .  w3c  .  dom  .  *  ; 
import   com  .  sparkit  .  extracta  .  *  ; 
import   com  .  sparkit  .  extracta  .  builder  .  model  .  *  ; 
import   com  .  sparkit  .  extracta  .  builder  .  tools  .  *  ; 
import   com  .  sparkit  .  extracta  .  builder  .  ui  .  *  ; 
import   com  .  sparkit  .  extracta  .  edl  .  *  ; 










public   class   VisualBuilder   extends   JFrame  { 


private   static   VisualBuilder   m_instance  =  null  ; 


private   InputPanel   m_inputPanel  ; 

private   ResultPanel   m_resultPanel  ; 


private   JMenuBar   m_mainJMenuBar  ; 


private   BuilderAction   m_aboutAction  ; 

private   BuilderAction   m_collapseTreeAction  ; 

private   BuilderAction   m_deleteAction  ; 

private   BuilderAction   m_editAction  ; 

private   BuilderAction   m_exitAction  ; 

private   BuilderAction   m_expandTreeAction  ; 

private   BuilderAction   m_exportNodeAction  ; 

private   BuilderAction   m_findNodeAction  ; 

private   BuilderAction   m_findNextNodeAction  ; 

private   BuilderAction   m_importUrlAction  ; 

private   BuilderAction   m_newAction  ; 

private   BuilderAction   m_openAction  ; 

private   BuilderAction   m_refreshAction  ; 

private   BuilderAction   m_refreshInputAction  ; 

private   BuilderAction   m_refreshOutputAction  ; 

private   BuilderAction   m_removeAllExportedElementsAction  ; 

private   BuilderAction   m_saveAction  ; 

private   BuilderAction   m_saveAsAction  ; 

private   BuilderAction   m_settingsAction  ; 

private   BuilderAction   m_showInBrowserAction  ; 

private   BuilderAction   m_showResultInBrowserAction  ; 

private   BuilderAction   m_updateInputTreeModelAction  ; 


private   Map   m_nameCounters  ; 


private   boolean   m_bModified  ; 


private   JTextField   m_urlTF  ; 

private   String   m_sUrlString  ; 


private   File   m_outputFile  ; 


private   String   m_fileChooserPath  =  System  .  getProperty  (  "user.dir"  )  ; 


private   final   int   m_nMaxHistoryFileLength  =  35  ; 




public   VisualBuilder  (  )  throws   Exception  { 
super  (  "VisualBuilder"  )  ; 
BuilderSettings  .  init  (  )  ; 
buildUI  (  getContentPane  (  )  )  ; 
initNameCounters  (  null  )  ; 
pack  (  )  ; 
Dimension   screenSize  =  java  .  awt  .  Toolkit  .  getDefaultToolkit  (  )  .  getScreenSize  (  )  ; 
Dimension   frameSize  =  getSize  (  )  ; 
if  (  frameSize  .  height  >  screenSize  .  height  )  { 
frameSize  .  height  =  screenSize  .  height  ; 
} 
if  (  frameSize  .  width  >  screenSize  .  width  )  { 
frameSize  .  width  =  screenSize  .  width  ; 
} 
setLocation  (  (  screenSize  .  width  -  frameSize  .  width  )  /  2  ,  (  screenSize  .  height  -  frameSize  .  height  )  /  2  )  ; 
setDefaultCloseOperation  (  JFrame  .  DO_NOTHING_ON_CLOSE  )  ; 
addWindowListener  (  new   WindowAdapter  (  )  { 

public   void   windowClosing  (  WindowEvent   e  )  { 
try  { 
getExitAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
}  catch  (  Throwable   t  )  { 
UITools  .  handleThrowable  (  t  )  ; 
System  .  exit  (  1  )  ; 
} 
} 
}  )  ; 
setModified  (  false  )  ; 
} 







private   void   buildUI  (  Container   container  )  throws   Exception  { 
container  .  setLayout  (  new   BorderLayout  (  )  )  ; 
m_inputPanel  =  new   InputPanel  (  this  )  ; 
m_resultPanel  =  new   ResultPanel  (  this  )  ; 
JSplitPane   splitPane  =  new   JSplitPane  (  JSplitPane  .  HORIZONTAL_SPLIT  ,  m_inputPanel  ,  m_resultPanel  )  ; 
m_resultPanel  .  refresh  (  )  ; 
splitPane  .  setDividerLocation  (  400  )  ; 
splitPane  .  setOneTouchExpandable  (  true  )  ; 
splitPane  .  setResizeWeight  (  0.5  )  ; 
container  .  add  (  splitPane  ,  BorderLayout  .  CENTER  )  ; 
container  .  add  (  createToolBar  (  )  ,  BorderLayout  .  NORTH  )  ; 
m_mainJMenuBar  =  createMainMenu  (  )  ; 
setJMenuBar  (  m_mainJMenuBar  )  ; 
} 






private   JToolBar   createToolBar  (  )  { 
JToolBar   toolBar  =  new   JToolBar  (  )  ; 
toolBar  .  setFloatable  (  false  )  ; 
toolBar  .  add  (  new   NoFocusButton  (  getNewAction  (  )  ,  "New blank xml document"  )  )  ; 
toolBar  .  add  (  new   NoFocusButton  (  getOpenAction  (  )  ,  "Open"  )  )  ; 
toolBar  .  add  (  new   NoFocusButton  (  getSaveAction  (  )  ,  "Save"  )  )  ; 
toolBar  .  addSeparator  (  )  ; 
toolBar  .  add  (  new   NoFocusButton  (  getExportNodeAction  (  )  ,  "Select element"  )  )  ; 
toolBar  .  add  (  new   NoFocusButton  (  getShowResultInBrowserAction  (  )  ,  "Show the result tree in a browser"  )  )  ; 
toolBar  .  addSeparator  (  )  ; 
toolBar  .  add  (  new   JLabel  (  "URL: "  )  )  ; 
m_urlTF  =  new   JTextField  (  )  ; 
m_urlTF  .  getInputMap  (  )  .  put  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_ENTER  ,  0  )  ,  "IMPORT_URL"  )  ; 
m_urlTF  .  getActionMap  (  )  .  put  (  "IMPORT_URL"  ,  getImportUrlAction  (  )  )  ; 
toolBar  .  add  (  m_urlTF  )  ; 
toolBar  .  add  (  new   NoFocusButton  (  getImportUrlAction  (  )  ,  "Import URL"  )  )  ; 
return   toolBar  ; 
} 






private   JMenuBar   createMainMenu  (  )  { 
JMenuBar   menuBar  ; 
JMenu   menu  ,  submenu  ; 
JMenuItem   menuItem  ; 
menuBar  =  new   JMenuBar  (  )  ; 
menu  =  new   JMenu  (  "File"  )  ; 
menu  .  setMnemonic  (  KeyEvent  .  VK_F  )  ; 
menuItem  =  new   JMenuItem  (  getNewAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_N  )  ; 
menuItem  .  setAccelerator  (  KeyStroke  .  getKeyStroke  (  KeyEvent  .  VK_F  ,  KeyEvent  .  CTRL_MASK  )  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  =  new   JMenuItem  (  getOpenAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_O  )  ; 
menu  .  add  (  menuItem  )  ; 
menu  .  addSeparator  (  )  ; 
menuItem  =  new   JMenuItem  (  getSaveAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_S  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  =  new   JMenuItem  (  getSaveAsAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_A  )  ; 
menu  .  add  (  menuItem  )  ; 
Iterator   historyFiles  =  BuilderSettings  .  getHistoryFiles  (  )  .  iterator  (  )  ; 
if  (  historyFiles  .  hasNext  (  )  )  { 
menu  .  addSeparator  (  )  ; 
} 
while  (  historyFiles  .  hasNext  (  )  )  { 
File   file  =  (  File  )  historyFiles  .  next  (  )  ; 
menuItem  =  new   JMenuItem  (  createNamedOpenAction  (  file  )  )  ; 
menu  .  add  (  menuItem  )  ; 
} 
menu  .  addSeparator  (  )  ; 
menuItem  =  new   JMenuItem  (  getExitAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_E  )  ; 
menu  .  add  (  menuItem  )  ; 
menuBar  .  add  (  menu  )  ; 
menu  =  new   JMenu  (  "Source"  )  ; 
menu  .  setMnemonic  (  KeyEvent  .  VK_S  )  ; 
menuItem  =  new   JMenuItem  (  getShowInBrowserAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_S  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  =  new   JMenuItem  (  getExportNodeAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_E  )  ; 
menu  .  add  (  menuItem  )  ; 
menu  .  addSeparator  (  )  ; 
menuItem  =  new   JMenuItem  (  getFindNodeAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_F  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  =  new   JMenuItem  (  getFindNextNodeAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_N  )  ; 
menu  .  add  (  menuItem  )  ; 
menu  .  addSeparator  (  )  ; 
menuItem  =  new   JMenuItem  (  getExpandTreeAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_X  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  =  new   JMenuItem  (  getCollapseTreeAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_C  )  ; 
menu  .  add  (  menuItem  )  ; 
menuBar  .  add  (  menu  )  ; 
menu  =  new   JMenu  (  "Result"  )  ; 
menu  .  setMnemonic  (  KeyEvent  .  VK_R  )  ; 
menuItem  =  new   JMenuItem  (  getShowResultInBrowserAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_S  )  ; 
menu  .  add  (  menuItem  )  ; 
menu  .  addSeparator  (  )  ; 
menuItem  =  new   JMenuItem  (  getFindNodeAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_F  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  =  new   JMenuItem  (  getFindNextNodeAction  (  )  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_N  )  ; 
menu  .  addSeparator  (  )  ; 
menuItem  =  new   JMenuItem  (  getExpandTreeAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_X  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  =  new   JMenuItem  (  getCollapseTreeAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_C  )  ; 
menu  .  add  (  menuItem  )  ; 
menuBar  .  add  (  menu  )  ; 
menu  =  new   JMenu  (  "Selection"  )  ; 
menu  .  setMnemonic  (  KeyEvent  .  VK_L  )  ; 
menuItem  =  new   JMenuItem  (  getEditAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_E  )  ; 
menu  .  add  (  menuItem  )  ; 
menu  .  addSeparator  (  )  ; 
menuItem  =  new   JMenuItem  (  getDeleteAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_R  )  ; 
menu  .  add  (  menuItem  )  ; 
menuItem  =  new   JMenuItem  (  getRemoveAllExportedElementsAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_A  )  ; 
menu  .  add  (  menuItem  )  ; 
menuBar  .  add  (  menu  )  ; 
menu  =  new   JMenu  (  "Tools"  )  ; 
menu  .  setMnemonic  (  KeyEvent  .  VK_T  )  ; 
menuItem  =  new   JMenuItem  (  getSettingsAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_S  )  ; 
menu  .  add  (  menuItem  )  ; 
menuBar  .  add  (  menu  )  ; 
menu  =  new   JMenu  (  "Help"  )  ; 
menu  .  setMnemonic  (  KeyEvent  .  VK_H  )  ; 
menuItem  =  new   JMenuItem  (  getAboutAction  (  )  )  ; 
menuItem  .  setMnemonic  (  KeyEvent  .  VK_A  )  ; 
menu  .  add  (  menuItem  )  ; 
menuBar  .  add  (  menu  )  ; 
return   menuBar  ; 
} 






public   static   void   main  (  String  [  ]  args  )  { 
try  { 
String   sJavaVersion  =  System  .  getProperties  (  )  .  getProperty  (  "java.version"  )  ; 
if  (  sJavaVersion  .  compareTo  (  "1.3"  )  <  0  )  { 
System  .  out  .  println  (  "Wrong Java 2 Runtime. Version 1.3 or higher is required."  )  ; 
System  .  out  .  println  (  "Cannot launch Extracta Visual Builder."  )  ; 
System  .  exit  (  1  )  ; 
} 
VisualBuilder  .  getInstance  (  )  ; 
}  catch  (  Throwable   t  )  { 
System  .  out  .  println  (  "Error initializing program!\n"  )  ; 
if  (  t   instanceof   ExtractaRuntimeException  )  { 
System  .  out  .  println  (  t  .  getMessage  (  )  )  ; 
}  else  { 
t  .  printStackTrace  (  )  ; 
} 
System  .  exit  (  1  )  ; 
} 
try  { 
VisualBuilder  .  getInstance  (  )  .  show  (  )  ; 
}  catch  (  Throwable   t  )  { 
UITools  .  handleThrowable  (  t  )  ; 
} 
} 






public   static   VisualBuilder   getInstance  (  )  throws   Exception  { 
if  (  m_instance  ==  null  )  { 
m_instance  =  new   VisualBuilder  (  )  ; 
} 
return   m_instance  ; 
} 







public   BuilderAction   getAboutAction  (  )  { 
if  (  m_aboutAction  ==  null  )  { 
m_aboutAction  =  new   BuilderAction  (  "About..."  ,  UIFactory  .  ABOUT_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  { 
openAbout  (  )  ; 
} 
}  )  ; 
} 
return   m_aboutAction  ; 
} 







public   BuilderAction   getCollapseTreeAction  (  )  { 
if  (  m_collapseTreeAction  ==  null  )  { 
m_collapseTreeAction  =  new   BuilderAction  (  "Collapse tree"  ,  UIFactory  .  UNDO_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  { 
JTree   tree  =  getSelectedTree  (  )  ; 
UITools  .  collapseTree  (  tree  ,  (  FilteredTreeNode  )  tree  .  getLastSelectedPathComponent  (  )  )  ; 
} 
}  )  ; 
} 
return   m_collapseTreeAction  ; 
} 







public   BuilderAction   getDeleteAction  (  )  { 
if  (  m_deleteAction  ==  null  )  { 
m_deleteAction  =  new   BuilderAction  (  "Remove selected"  ,  UIFactory  .  DELETE_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
m_resultPanel  .  deleteSelectedElement  (  )  ; 
} 
}  )  ; 
} 
return   m_deleteAction  ; 
} 







public   BuilderAction   getEditAction  (  )  { 
if  (  m_editAction  ==  null  )  { 
m_editAction  =  new   BuilderAction  (  "Edit selected node..."  ,  UIFactory  .  EDIT_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
m_resultPanel  .  editSelectedElement  (  )  ; 
getRefreshAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
} 
}  )  ; 
} 
return   m_editAction  ; 
} 







public   BuilderAction   getExitAction  (  )  { 
if  (  m_exitAction  ==  null  )  { 
m_exitAction  =  new   BuilderAction  (  "Exit"  ,  null  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
if  (  isModifiedChecked  (  )  )  { 
System  .  exit  (  0  )  ; 
} 
} 
}  )  ; 
} 
return   m_exitAction  ; 
} 







public   BuilderAction   getExpandTreeAction  (  )  { 
if  (  m_expandTreeAction  ==  null  )  { 
m_expandTreeAction  =  new   BuilderAction  (  "Expand Tree"  ,  UIFactory  .  OPEN_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  { 
JTree   tree  =  getSelectedTree  (  )  ; 
UITools  .  expandTree  (  tree  ,  (  FilteredTreeNode  )  tree  .  getLastSelectedPathComponent  (  )  )  ; 
} 
}  )  ; 
} 
return   m_expandTreeAction  ; 
} 







public   BuilderAction   getFindNodeAction  (  )  { 
if  (  m_findNodeAction  ==  null  )  { 
m_findNodeAction  =  new   BuilderAction  (  "Find..."  ,  UIFactory  .  FIND_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
UITools  .  visualSearchNode  (  getSelectedTree  (  )  )  ; 
} 
}  )  ; 
} 
return   m_findNodeAction  ; 
} 







public   BuilderAction   getImportUrlAction  (  )  { 
if  (  m_importUrlAction  ==  null  )  { 
m_importUrlAction  =  new   BuilderAction  (  ""  ,  UIFactory  .  REFRESH_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
importUrl  (  )  ; 
getRefreshAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
} 
}  )  ; 
} 
return   m_importUrlAction  ; 
} 







public   BuilderAction   getNewAction  (  )  { 
if  (  m_newAction  ==  null  )  { 
m_newAction  =  new   BuilderAction  (  "New"  ,  UIFactory  .  NEW_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
if  (  isModifiedChecked  (  )  )  { 
m_resultPanel  .  removeAllExportedElements  (  false  )  ; 
Document   document  =  DomHelper  .  createEmptyDocument  (  )  ; 
m_inputPanel  .  setDocument  (  document  )  ; 
m_resultPanel  .  setContext  (  new   ResultContext  (  new   Edl  (  )  ,  document  ,  null  )  )  ; 
m_outputFile  =  null  ; 
m_sUrlString  =  null  ; 
m_urlTF  .  setText  (  m_sUrlString  )  ; 
setModified  (  false  )  ; 
getRefreshAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
} 
} 
}  )  ; 
} 
return   m_newAction  ; 
} 









private   BuilderAction   createNamedOpenAction  (  File   file  )  { 
String   fileName  =  file  .  getAbsolutePath  (  )  ; 
final   File   fileToOpen  =  file  ; 
if  (  fileName  .  length  (  )  >  m_nMaxHistoryFileLength  )  { 
String   separator  =  System  .  getProperty  (  "file.separator"  )  ; 
StringTokenizer   tokenizer  =  new   StringTokenizer  (  fileName  ,  separator  )  ; 
if  (  tokenizer  .  countTokens  (  )  >  1  )  { 
String   firstFolder  =  tokenizer  .  nextToken  (  )  +  separator  +  tokenizer  .  nextToken  (  )  +  separator  ; 
Stack   stack  =  new   Stack  (  )  ; 
while  (  tokenizer  .  hasMoreTokens  (  )  )  { 
stack  .  push  (  tokenizer  .  nextToken  (  )  )  ; 
} 
String   lastFolders  =  new   String  (  )  ; 
boolean   bFileNameAdded  =  false  ; 
while  (  !  stack  .  empty  (  )  &&  (  (  lastFolders  .  length  (  )  +  (  (  String  )  stack  .  peek  (  )  )  .  length  (  )  )  <  m_nMaxHistoryFileLength  )  )  { 
bFileNameAdded  =  true  ; 
lastFolders  =  separator  +  (  String  )  stack  .  pop  (  )  +  lastFolders  ; 
} 
if  (  bFileNameAdded  )  { 
fileName  =  firstFolder  +  " ... "  +  lastFolders  ; 
}  else  { 
fileName  =  (  (  String  )  stack  .  pop  (  )  )  .  substring  (  0  ,  m_nMaxHistoryFileLength  -  3  )  +  "..."  ; 
} 
}  else  { 
fileName  =  fileName  .  substring  (  0  ,  m_nMaxHistoryFileLength  -  3  )  +  "..."  ; 
} 
} 
BuilderAction   action  =  new   BuilderAction  (  fileName  ,  UIFactory  .  OPEN_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
openFile  (  fileToOpen  )  ; 
} 
}  )  ; 
return   action  ; 
} 







public   BuilderAction   getOpenAction  (  )  { 
if  (  m_openAction  ==  null  )  { 
m_openAction  =  new   BuilderAction  (  "Open..."  ,  UIFactory  .  OPEN_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
openFile  (  null  )  ; 
} 
}  )  ; 
} 
return   m_openAction  ; 
} 







public   BuilderAction   getFindNextNodeAction  (  )  { 
if  (  m_findNextNodeAction  ==  null  )  { 
m_findNextNodeAction  =  new   BuilderAction  (  "Find Next"  ,  UIFactory  .  FIND_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
UITools  .  visualSearchNextNode  (  getSelectedTree  (  )  )  ; 
} 
}  )  ; 
} 
return   m_findNextNodeAction  ; 
} 







public   BuilderAction   getExportNodeAction  (  )  { 
if  (  m_exportNodeAction  ==  null  )  { 
m_exportNodeAction  =  new   BuilderAction  (  "Select Element"  ,  UIFactory  .  FORWARD_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
exportNode  (  )  ; 
getRefreshAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
} 
}  )  ; 
} 
return   m_exportNodeAction  ; 
} 







public   BuilderAction   getRefreshAction  (  )  { 
if  (  m_refreshAction  ==  null  )  { 
m_refreshAction  =  new   BuilderAction  (  "Refresh"  ,  UIFactory  .  REFRESH_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
refreshTitle  (  )  ; 
getRefreshInputAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
getRefreshOutputAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
} 
}  )  ; 
} 
return   m_refreshAction  ; 
} 







public   BuilderAction   getRefreshInputAction  (  )  { 
if  (  m_refreshInputAction  ==  null  )  { 
m_refreshInputAction  =  new   BuilderAction  (  "Refresh input"  ,  UIFactory  .  REFRESH_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
m_inputPanel  .  refresh  (  )  ; 
} 
}  )  ; 
} 
return   m_refreshInputAction  ; 
} 







public   BuilderAction   getRefreshOutputAction  (  )  { 
if  (  m_refreshOutputAction  ==  null  )  { 
m_refreshOutputAction  =  new   BuilderAction  (  "Refresh output"  ,  UIFactory  .  REFRESH_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
m_resultPanel  .  refresh  (  )  ; 
} 
}  )  ; 
} 
return   m_refreshOutputAction  ; 
} 







public   BuilderAction   getRemoveAllExportedElementsAction  (  )  { 
if  (  m_removeAllExportedElementsAction  ==  null  )  { 
m_removeAllExportedElementsAction  =  new   BuilderAction  (  "Remove all"  ,  UIFactory  .  DELETE_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
m_resultPanel  .  removeAllExportedElements  (  )  ; 
getRefreshAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
} 
}  )  ; 
} 
return   m_removeAllExportedElementsAction  ; 
} 







public   BuilderAction   getSaveAction  (  )  { 
if  (  m_saveAction  ==  null  )  { 
m_saveAction  =  new   BuilderAction  (  "Save"  ,  UIFactory  .  SAVE_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
if  (  save  (  false  )  )  { 
refreshTitle  (  )  ; 
} 
} 
}  )  ; 
} 
return   m_saveAction  ; 
} 







public   BuilderAction   getSaveAsAction  (  )  { 
if  (  m_saveAsAction  ==  null  )  { 
m_saveAsAction  =  new   BuilderAction  (  "Save as..."  ,  UIFactory  .  SAVE_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
if  (  save  (  true  )  )  { 
refreshTitle  (  )  ; 
} 
} 
}  )  ; 
} 
return   m_saveAsAction  ; 
} 







public   BuilderAction   getSettingsAction  (  )  { 
if  (  m_settingsAction  ==  null  )  { 
m_settingsAction  =  new   BuilderAction  (  "Settings..."  ,  UIFactory  .  PREFERENCES_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
if  (  openSettings  (  )  ==  SettingsDialog  .  OK  )  { 
getUpdateInputTreeModelAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
getRefreshAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
BuilderSettings  .  m_tagFilter  =  new   TagFilter  (  BuilderSettings  .  m_hiddenTags  )  ; 
} 
} 
}  )  ; 
} 
return   m_settingsAction  ; 
} 







public   BuilderAction   getShowInBrowserAction  (  )  { 
if  (  m_showInBrowserAction  ==  null  )  { 
m_showInBrowserAction  =  new   BuilderAction  (  "Show in browser"  ,  UIFactory  .  ZOOM_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
if  (  m_sUrlString  !=  null  )  { 
BrowserControl  .  displayURL  (  m_sUrlString  )  ; 
}  else  { 
JOptionPane  .  showMessageDialog  (  VisualBuilder  .  this  ,  "No source file loaded!"  )  ; 
} 
} 
}  )  ; 
} 
return   m_showInBrowserAction  ; 
} 







public   BuilderAction   getShowResultInBrowserAction  (  )  { 
if  (  m_showResultInBrowserAction  ==  null  )  { 
m_showResultInBrowserAction  =  new   BuilderAction  (  "Show in browser"  ,  UIFactory  .  ZOOM_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
m_resultPanel  .  showInBrowser  (  )  ; 
} 
}  )  ; 
} 
return   m_showResultInBrowserAction  ; 
} 







public   BuilderAction   getUpdateInputTreeModelAction  (  )  { 
if  (  m_updateInputTreeModelAction  ==  null  )  { 
m_updateInputTreeModelAction  =  new   BuilderAction  (  "Update input tree model"  ,  UIFactory  .  REFRESH_ICON  ,  new   ICommand  (  )  { 

public   void   execute  (  )  throws   Exception  { 
m_inputPanel  .  updateModel  (  )  ; 
} 
}  )  ; 
} 
return   m_updateInputTreeModelAction  ; 
} 




private   void   openAbout  (  )  { 
AboutBox  .  handleDialog  (  this  )  ; 
} 






private   void   exportNode  (  )  throws   Exception  { 
FilteredTreeNode   filteredNode  =  (  FilteredTreeNode  )  m_inputPanel  .  getTree  (  )  .  getLastSelectedPathComponent  (  )  ; 
if  (  filteredNode  !=  null  )  { 
ItemDescriptor   itemDescriptor  =  new   ItemDescriptor  (  new   XPathExpression  (  filteredNode  .  getOriginalNode  (  )  )  )  ; 
itemDescriptor  .  setName  (  getName  (  filteredNode  .  getOriginalNode  (  )  )  )  ; 
if  (  !  BuilderSettings  .  m_bDefaultSelection  )  { 
itemDescriptor  =  ExportedElementDialog  .  handleDialog  (  itemDescriptor  )  ; 
} 
if  (  itemDescriptor  !=  null  )  { 
m_resultPanel  .  addElement  (  itemDescriptor  )  ; 
} 
setModified  (  true  )  ; 
}  else  { 
JOptionPane  .  showMessageDialog  (  this  ,  "No element selected!"  )  ; 
} 
} 






public   void   setModified  (  boolean   bModified  )  { 
m_bModified  =  bModified  ; 
} 




private   void   importUrl  (  )  throws   ExtractaException  { 
UITools  .  changeCursor  (  UITools  .  WAIT_CURSOR  ,  this  )  ; 
try  { 
m_sUrlString  =  m_urlTF  .  getText  (  )  ; 
URL   url  =  new   URL  (  m_sUrlString  )  ; 
Document   document  =  DomHelper  .  parseHtml  (  url  .  openStream  (  )  )  ; 
m_inputPanel  .  setDocument  (  document  )  ; 
Edl   edl  =  new   Edl  (  )  ; 
edl  .  addUrlDescriptor  (  new   UrlDescriptor  (  m_sUrlString  )  )  ; 
m_resultPanel  .  setContext  (  new   ResultContext  (  edl  ,  document  ,  url  )  )  ; 
setModified  (  true  )  ; 
}  catch  (  IOException   ex  )  { 
throw   new   ExtractaException  (  "Can not open URL!"  ,  ex  )  ; 
}  finally  { 
UITools  .  changeCursor  (  UITools  .  DEFAULT_CURSOR  ,  this  )  ; 
} 
} 




private   void   openFile  (  File   file  )  throws   Exception  { 
try  { 
if  (  file  !=  null  )  { 
m_fileChooserPath  =  file  .  getParent  (  )  ; 
asyncLoadFile  (  file  )  ; 
repaint  (  )  ; 
}  else   if  (  isModifiedChecked  (  )  )  { 
JFileChooser   fc  =  new   JFileChooser  (  m_fileChooserPath  )  ; 
fc  .  setFileFilter  (  UIFactory  .  getEdlFileFilter  (  )  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  this  )  ; 
if  (  returnVal  ==  JFileChooser  .  APPROVE_OPTION  )  { 
file  =  fc  .  getSelectedFile  (  )  ; 
m_fileChooserPath  =  fc  .  getSelectedFile  (  )  .  getParent  (  )  ; 
asyncLoadFile  (  file  )  ; 
repaint  (  )  ; 
} 
} 
}  catch  (  IOException   ex  )  { 
throw   new   ExtractaException  (  "Unknown URL."  ,  ex  )  ; 
} 
} 






private   void   loadFile  (  File   file  )  throws   Exception  { 
Edl   edl  =  new   Edl  (  "file:///"  +  file  .  getAbsolutePath  (  )  )  ; 
URL   url  =  ExtractaHelper  .  retrieveUrl  (  edl  .  getUrlRetrievalDescriptor  (  )  )  ; 
String   sUrlString  =  url  .  toExternalForm  (  )  ; 
if  (  sUrlString  .  startsWith  (  "file:/"  )  &&  (  sUrlString  .  charAt  (  6  )  !=  '/'  )  )  { 
sUrlString  =  sUrlString  .  substring  (  0  ,  6  )  +  "//"  +  sUrlString  .  substring  (  6  )  ; 
} 
Document   document  =  DomHelper  .  parseHtml  (  url  .  openStream  (  )  )  ; 
m_inputPanel  .  setDocument  (  document  )  ; 
m_resultPanel  .  setContext  (  new   ResultContext  (  edl  ,  document  ,  url  )  )  ; 
initNameCounters  (  edl  .  getItemDescriptors  (  )  )  ; 
m_outputFile  =  file  ; 
m_sUrlString  =  sUrlString  ; 
m_urlTF  .  setText  (  m_sUrlString  )  ; 
updateHistroy  (  m_outputFile  )  ; 
setModified  (  false  )  ; 
} 




private   void   updateHistroy  (  File   file  )  throws   IOException  { 
BuilderSettings  .  addHistoryFile  (  file  )  ; 
BuilderSettings  .  saveSettings  (  )  ; 
m_mainJMenuBar  =  createMainMenu  (  )  ; 
setJMenuBar  (  m_mainJMenuBar  )  ; 
m_mainJMenuBar  .  setVisible  (  false  )  ; 
m_mainJMenuBar  .  setVisible  (  true  )  ; 
} 




private   int   openSettings  (  )  throws   IOException  { 
return   SettingsDialog  .  handleDialog  (  this  )  ; 
} 










private   boolean   save  (  boolean   bSaveAs  )  throws   IOException  { 
if  (  (  m_outputFile  ==  null  )  ||  bSaveAs  )  { 
File   selectedFile  ; 
JFileChooser   fc  =  new   JFileChooser  (  m_fileChooserPath  )  ; 
fc  .  setFileFilter  (  UIFactory  .  getEdlFileFilter  (  )  )  ; 
int   returnVal  =  fc  .  showSaveDialog  (  this  )  ; 
if  (  returnVal  ==  JFileChooser  .  APPROVE_OPTION  )  { 
selectedFile  =  fc  .  getSelectedFile  (  )  ; 
String   sSelectedFilePath  =  selectedFile  .  getAbsolutePath  (  )  ; 
int   nDotIndex  =  sSelectedFilePath  .  indexOf  (  "."  )  ; 
String   sExtension  =  (  nDotIndex  >  0  )  ?  ""  :  ".edl"  ; 
m_outputFile  =  new   File  (  sSelectedFilePath  +  sExtension  )  ; 
m_fileChooserPath  =  fc  .  getSelectedFile  (  )  .  getParent  (  )  ; 
}  else  { 
return   false  ; 
} 
} 
FileOutputStream   fos  =  new   FileOutputStream  (  m_outputFile  )  ; 
UITools  .  changeCursor  (  UITools  .  WAIT_CURSOR  ,  this  )  ; 
try  { 
DomHelper  .  printXml  (  m_resultPanel  .  getContext  (  )  .  getEdl  (  )  .  toXmlDocument  (  )  ,  fos  )  ; 
updateHistroy  (  m_outputFile  )  ; 
setModified  (  false  )  ; 
}  finally  { 
UITools  .  changeCursor  (  UITools  .  DEFAULT_CURSOR  ,  this  )  ; 
} 
return   true  ; 
} 





private   void   refreshTitle  (  )  { 
StringBuffer   sTitle  =  new   StringBuffer  (  "VisualBuilder"  )  ; 
if  (  m_outputFile  !=  null  )  { 
sTitle  .  append  (  " "  )  .  append  (  m_outputFile  .  getPath  (  )  )  ; 
} 
if  (  m_bModified  )  { 
sTitle  .  append  (  " *"  )  ; 
} 
setTitle  (  sTitle  .  toString  (  )  )  ; 
} 





private   JTree   getSelectedTree  (  )  { 
JTree   inputTree  =  m_inputPanel  .  getTree  (  )  ; 
return   inputTree  .  hasFocus  (  )  ?  inputTree  :  m_resultPanel  .  getTree  (  )  ; 
} 






private   boolean   isModifiedChecked  (  )  throws   IOException  { 
if  (  m_bModified  )  { 
int   nResult  =  JOptionPane  .  showConfirmDialog  (  this  ,  "Data not saved! Save now?"  ,  "Confirmation"  ,  JOptionPane  .  YES_NO_CANCEL_OPTION  )  ; 
if  (  nResult  ==  JOptionPane  .  CANCEL_OPTION  )  { 
return   false  ; 
} 
if  (  nResult  ==  JOptionPane  .  YES_OPTION  )  { 
return   save  (  false  )  ; 
} 
} 
return   true  ; 
} 




private   void   initNameCounters  (  java  .  util  .  List   itemDescriptors  )  { 
m_nameCounters  =  new   HashMap  (  )  ; 
if  (  itemDescriptors  !=  null  )  { 
Iterator   iter  =  itemDescriptors  .  iterator  (  )  ; 
while  (  iter  .  hasNext  (  )  )  { 
ItemDescriptor   item  =  (  ItemDescriptor  )  iter  .  next  (  )  ; 
incNameCounters  (  truncateLastNumber  (  item  .  getName  (  )  )  )  ; 
} 
} 
} 

private   String   truncateLastNumber  (  String   s  )  { 
int   i  =  s  .  length  (  )  -  1  ; 
while  (  (  i  >  0  )  &&  Character  .  isDigit  (  s  .  charAt  (  i  )  )  )  { 
i  --  ; 
} 
return   s  .  substring  (  0  ,  i  +  1  )  .  trim  (  )  ; 
} 

private   String   getName  (  Node   node  )  { 
String   sNodeName  =  node  .  getNodeName  (  )  ; 
sNodeName  =  sNodeName  .  substring  (  0  ,  1  )  .  toUpperCase  (  )  +  sNodeName  .  substring  (  1  ,  sNodeName  .  length  (  )  )  ; 
incNameCounters  (  sNodeName  )  ; 
return   sNodeName  +  (  (  Integer  )  m_nameCounters  .  get  (  sNodeName  )  )  .  intValue  (  )  ; 
} 

private   void   incNameCounters  (  String   s  )  { 
if  (  m_nameCounters  .  containsKey  (  s  )  )  { 
Integer   oldCount  =  (  Integer  )  m_nameCounters  .  get  (  s  )  ; 
m_nameCounters  .  put  (  s  ,  new   Integer  (  oldCount  .  intValue  (  )  +  1  )  )  ; 
}  else  { 
m_nameCounters  .  put  (  s  ,  new   Integer  (  1  )  )  ; 
} 
} 










private   void   asyncLoadFile  (  final   File   file  )  throws   Exception  { 
final   JFrame   mainFrame  =  this  ; 
Thread   loader  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
UITools  .  changeCursor  (  UITools  .  WAIT_CURSOR  ,  mainFrame  )  ; 
loadFile  (  file  )  ; 
getRefreshAction  (  )  .  getCommand  (  )  .  execute  (  )  ; 
UITools  .  changeCursor  (  UITools  .  DEFAULT_CURSOR  ,  mainFrame  )  ; 
}  catch  (  Throwable   t  )  { 
UITools  .  handleThrowable  (  t  )  ; 
}  finally  { 
UITools  .  changeCursor  (  UITools  .  DEFAULT_CURSOR  ,  mainFrame  )  ; 
} 
} 
}  )  ; 
loader  .  start  (  )  ; 
} 
} 

