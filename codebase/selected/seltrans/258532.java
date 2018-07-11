package   org  .  plantstreamer  .  export  .  opclist  ; 

import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  beans  .  PropertyChangeEvent  ; 
import   java  .  beans  .  PropertyChangeListener  ; 
import   java  .  io  .  File  ; 
import   java  .  text  .  MessageFormat  ; 
import   java  .  util  .  ResourceBundle  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   org  .  plantstreamer  .  treetable  .  OPCTreeTableModel  ; 
import   swingextras  .  action  .  ActionX  ; 
import   swingextras  .  GuiUtils  ; 
import   swingextras  .  icons  .  IconManager  ; 






@  SuppressWarnings  (  "serial"  ) 
public   class   OPCListExportDialog   extends   javax  .  swing  .  JDialog  { 

private   static   final   ResourceBundle   bundle  =  java  .  util  .  ResourceBundle  .  getBundle  (  "org/plantstreamer/i18n/common"  )  ; 

private   CSVExportWorker   worker  ; 

private   OPCTreeTableModel   model  ; 






public   OPCListExportDialog  (  java  .  awt  .  Window   parent  ,  OPCTreeTableModel   model  )  { 
super  (  parent  ,  ModalityType  .  MODELESS  )  ; 
this  .  model  =  model  ; 
initComponents  (  )  ; 
setTitle  (  bundle  .  getString  (  "OPC_list_export"  )  )  ; 
setIconImages  (  parent  .  getIconImages  (  )  )  ; 
GuiUtils  .  centerWindowOnParent  (  this  )  ; 
setAlwaysOnTop  (  true  )  ; 
jTextFieldFile  .  setText  (  new   File  (  System  .  getProperty  (  "user.home"  )  ,  "opclist.csv"  )  .  getAbsolutePath  (  )  )  ; 
simpleStatusBar  .  addLogger  (  Logger  .  getLogger  (  CSVExportWorker  .  class  .  getName  (  )  )  )  ; 
@  SuppressWarnings  (  "serial"  )  ActionX   actionCancel  =  new   ActionX  (  bundle  .  getString  (  "Cancel"  )  ,  IconManager  .  getIcon  (  "16x16/actions/button_cancel.png"  )  )  { 

@  Override 
public   void   actionPerformed  (  ActionEvent   e  )  { 
cancel  (  )  ; 
} 
}  ; 
jButtonCancel  .  setAction  (  actionCancel  )  ; 
GuiUtils  .  installEscapeActionDialog  (  this  ,  jButtonCancel  .  getAction  (  )  )  ; 
getRootPane  (  )  .  setDefaultButton  (  jButtonExport  )  ; 
} 




public   void   cancel  (  )  { 
if  (  worker  !=  null  &&  !  worker  .  isDone  (  )  )  { 
int   answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  bundle  .  getString  (  "Do_you_really_want_to_stop_the_export?"  )  ,  bundle  .  getString  (  "Stop_export"  )  ,  JOptionPane  .  YES_NO_OPTION  ,  JOptionPane  .  QUESTION_MESSAGE  )  ; 
if  (  answer  ==  JOptionPane  .  YES_OPTION  )  { 
worker  .  cancel  (  false  )  ; 
} 
return  ; 
}  else  { 
dispose  (  )  ; 
} 
} 

private   void   export  (  )  { 
if  (  worker  !=  null  &&  !  worker  .  isDone  (  )  )  { 
return  ; 
} 
File   file  =  new   File  (  jTextFieldFile  .  getText  (  )  )  ; 
if  (  file  .  exists  (  )  )  { 
int   answer  =  JOptionPane  .  showConfirmDialog  (  this  ,  MessageFormat  .  format  (  bundle  .  getString  (  "File_{0}_already_exists!\nDo_you_really_want_to_overwrite_it?"  )  ,  file  .  getPath  (  )  )  ,  bundle  .  getString  (  "File_exists"  )  ,  JOptionPane  .  YES_NO_OPTION  )  ; 
if  (  answer  !=  JOptionPane  .  YES_OPTION  )  { 
return  ; 
} 
} 
jButtonExport  .  setEnabled  (  false  )  ; 
worker  =  new   CSVExportWorker  (  file  ,  model  ,  jCheckBoxProperties  .  isSelected  (  )  )  ; 
worker  .  getPropertyChangeSupport  (  )  .  addPropertyChangeListener  (  "state"  ,  new   PropertyChangeListener  (  )  { 

@  Override 
public   void   propertyChange  (  PropertyChangeEvent   evt  )  { 
if  (  worker  .  isDone  (  )  )  { 
jButtonExport  .  setEnabled  (  true  )  ; 
} 
} 
}  )  ; 
worker  .  execute  (  )  ; 
simpleStatusBar  .  addWorker  (  worker  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
java  .  awt  .  GridBagConstraints   gridBagConstraints  ; 
jPanelButton  =  new   javax  .  swing  .  JPanel  (  )  ; 
jButtonExport  =  new   javax  .  swing  .  JButton  (  )  ; 
jButtonCancel  =  new   javax  .  swing  .  JButton  (  )  ; 
simpleStatusBar  =  new   swingextras  .  gui  .  SimpleStatusBar  (  )  ; 
header  =  new   swingextras  .  Header  (  )  ; 
jPanel2  =  new   javax  .  swing  .  JPanel  (  )  ; 
jPanelGeneral  =  new   javax  .  swing  .  JPanel  (  )  ; 
jButtonFile  =  new   javax  .  swing  .  JButton  (  )  ; 
jTextFieldFile  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
jPanel1  =  new   javax  .  swing  .  JPanel  (  )  ; 
jCheckBoxProperties  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  DISPOSE_ON_CLOSE  )  ; 
getContentPane  (  )  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jButtonExport  .  setIcon  (  IconManager  .  getIcon  (  "16x16/actions/fileexport.png"  )  )  ; 
jButtonExport  .  setText  (  bundle  .  getString  (  "Export"  )  )  ; 
jButtonExport  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButtonExportActionPerformed  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   jPanelButtonLayout  =  new   javax  .  swing  .  GroupLayout  (  jPanelButton  )  ; 
jPanelButton  .  setLayout  (  jPanelButtonLayout  )  ; 
jPanelButtonLayout  .  setHorizontalGroup  (  jPanelButtonLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  jPanelButtonLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  319  ,  Short  .  MAX_VALUE  )  .  addComponent  (  jButtonExport  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jButtonCancel  )  .  addGap  (  6  ,  6  ,  6  )  )  )  ; 
jPanelButtonLayout  .  setVerticalGroup  (  jPanelButtonLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanelButtonLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jButtonCancel  )  .  addComponent  (  jButtonExport  )  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  2  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  10  ,  10  ,  10  ,  10  )  ; 
getContentPane  (  )  .  add  (  jPanelButton  ,  gridBagConstraints  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  3  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
getContentPane  (  )  .  add  (  simpleStatusBar  ,  gridBagConstraints  )  ; 
header  .  setTitle  (  bundle  .  getString  (  "OPC_list_export"  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTH  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
getContentPane  (  )  .  add  (  header  ,  gridBagConstraints  )  ; 
jPanel2  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jPanelGeneral  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jButtonFile  .  setIcon  (  IconManager  .  getIcon  (  "16x16/actions/fileopen.png"  )  )  ; 
jButtonFile  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButtonFileActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  2  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanelGeneral  .  add  (  jButtonFile  ,  gridBagConstraints  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanelGeneral  .  add  (  jTextFieldFile  ,  gridBagConstraints  )  ; 
jLabel2  .  setText  (  bundle  .  getString  (  "File_name:"  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  EAST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanelGeneral  .  add  (  jLabel2  ,  gridBagConstraints  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanel2  .  add  (  jPanelGeneral  ,  gridBagConstraints  )  ; 
jPanel1  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jCheckBoxProperties  .  setText  (  bundle  .  getString  (  "Export_item_properties"  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
jPanel1  .  add  (  jCheckBoxProperties  ,  gridBagConstraints  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  2  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  0  ,  5  ,  0  ,  5  )  ; 
jPanel2  .  add  (  jPanel1  ,  gridBagConstraints  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  weighty  =  1.0  ; 
getContentPane  (  )  .  add  (  jPanel2  ,  gridBagConstraints  )  ; 
pack  (  )  ; 
} 

private   void   jButtonFileActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
JFileChooser   fileChooser  =  new   JFileChooser  (  )  ; 
fileChooser  .  setFileSelectionMode  (  JFileChooser  .  FILES_ONLY  )  ; 
fileChooser  .  setMultiSelectionEnabled  (  false  )  ; 
int   answer  =  fileChooser  .  showSaveDialog  (  this  )  ; 
if  (  answer  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   file  =  fileChooser  .  getSelectedFile  (  )  ; 
if  (  file  !=  null  )  { 
jTextFieldFile  .  setText  (  file  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 

private   void   jButtonExportActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
export  (  )  ; 
} 

private   swingextras  .  Header   header  ; 

protected   javax  .  swing  .  JButton   jButtonCancel  ; 

protected   javax  .  swing  .  JButton   jButtonExport  ; 

private   javax  .  swing  .  JButton   jButtonFile  ; 

private   javax  .  swing  .  JCheckBox   jCheckBoxProperties  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JPanel   jPanel1  ; 

private   javax  .  swing  .  JPanel   jPanel2  ; 

private   javax  .  swing  .  JPanel   jPanelButton  ; 

private   javax  .  swing  .  JPanel   jPanelGeneral  ; 

private   javax  .  swing  .  JTextField   jTextFieldFile  ; 

private   swingextras  .  gui  .  SimpleStatusBar   simpleStatusBar  ; 
} 

