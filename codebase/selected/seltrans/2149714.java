package   gui  .  form  ; 

import   java  .  awt  .  Toolkit  ; 
import   java  .  awt  .  event  .  ItemEvent  ; 
import   java  .  util  .  concurrent  .  ExecutionException  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   ui  .  Manager  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   javax  .  swing  .  ImageIcon  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JFrame  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  SwingUtilities  ; 
import   javax  .  swing  .  border  .  Border  ; 
import   javax  .  swing  .  border  .  TitledBorder  ; 
import   reportingModule  .  PeerSearchUI  ; 
import   reportingModule  .  ReportingModule  ; 
import   ui  .  FileOperator  ; 
import   ui  .  peerSearchReportData  ; 





enum   TabNames  { 

NewProject  ,  AddDocument  ,  SelectDocument  ,  KnowledgeBase  ,  Settings  ,  StartCheck 
} 

public   class   WizardForm   extends   javax  .  swing  .  JFrame  { 

String   destFolderPath  =  null  ; 

String   sourceFolderName  ; 

String   fName  ; 

String   indexFolderPath  ; 

Manager   manager  =  new   Manager  (  )  ; 

ReportData   repdata  ; 

peerSearchReportData   peerRepData  ; 

ArrayList  <  String  >  fileArrayList  =  new   ArrayList  <  String  >  (  )  ; 

String  [  ]  [  ]  temp  =  null  ; 

ReportingModule   rp  ; 


WizardForm  (  )  { 
initComponents  (  )  ; 
this  .  setIconImage  (  Main  .  getPlagiabustImage  (  )  )  ; 
ViewButton  .  setVisible  (  false  )  ; 
ProjectLocationLabel2  .  setVisible  (  false  )  ; 
singleDetectionButton  .  setSelected  (  true  )  ; 
disableTabSet  (  )  ; 
OutputStream   out  =  new   OutputStream  (  )  { 

@  Override 
public   void   write  (  int   b  )  throws   IOException  { 
updateTextArea  (  String  .  valueOf  (  (  char  )  b  )  )  ; 
} 

@  Override 
public   void   write  (  byte  [  ]  b  ,  int   off  ,  int   len  )  throws   IOException  { 
updateTextArea  (  new   String  (  b  ,  off  ,  len  )  )  ; 
} 

@  Override 
public   void   write  (  byte  [  ]  b  )  throws   IOException  { 
write  (  b  ,  0  ,  b  .  length  )  ; 
} 
}  ; 
System  .  setOut  (  new   PrintStream  (  out  ,  true  )  )  ; 
ProjectLocationTextField  .  setText  (  Main  .  getDesktop  (  )  .  getAbsolutePath  (  )  )  ; 
ProjectFolderTextField  .  setText  (  new   File  (  Main  .  getDesktop  (  )  .  getAbsolutePath  (  )  +  File  .  separatorChar  +  ProjectNameTextField  .  getText  (  )  )  .  getAbsolutePath  (  )  )  ; 
DeleteProjectFilesCheckBox  .  setSelected  (  true  )  ; 
InternetSearchCompletionLabel  .  setVisible  (  false  )  ; 
PlagiabustSearchCompletionLabel  .  setVisible  (  false  )  ; 
DownloadFromInternetProgressBar  .  setVisible  (  false  )  ; 
DownloadFromPlagiabustProgressBar  .  setVisible  (  false  )  ; 
} 

private   void   updateTextArea  (  final   String   text  )  { 
SwingUtilities  .  invokeLater  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
jTextArea1  .  append  (  text  )  ; 
} 
}  )  ; 
} 

private   void   disableTabSet  (  )  { 
for  (  int   i  =  1  ;  i  <=  TabNames  .  StartCheck  .  ordinal  (  )  ;  i  ++  )  { 
WizardTabbedPane  .  setEnabledAt  (  i  ,  false  )  ; 
} 
} 

private   boolean   checkConnectivity  (  boolean   isLocalHost  )  { 
boolean   connected  =  false  ; 
try  { 
URL   url  =  new   URL  (  "http://www.google.com"  )  ; 
if  (  isLocalHost  )  { 
url  =  new   URL  (  PlagiabustServerManager  .  getSolrURL  (  )  )  ; 
} 
HttpURLConnection   urlConnect  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
if  (  urlConnect  .  getResponseCode  (  )  ==  HttpURLConnection  .  HTTP_OK  )  { 
connected  =  true  ; 
} 
}  catch  (  Exception   ex  )  { 
} 
return   connected  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
jLabel20  =  new   javax  .  swing  .  JLabel  (  )  ; 
buttonGroup1  =  new   javax  .  swing  .  ButtonGroup  (  )  ; 
WizardMainPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
DescriptionPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
DescriptionScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
DescriptionTextArea  =  new   javax  .  swing  .  JTextArea  (  )  ; 
WizardImageLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
WizardSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
WizardTabbedPane  =  new   javax  .  swing  .  JTabbedPane  (  )  ; 
ProjectLocationPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
ProjectNameLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
ProjectLocationLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
ProjectFolderLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
NameLocationTopSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
NameLocationLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
ProjectNameTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
ProjectLocationTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
ProjectFolderTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
LocationBrowseButton  =  new   javax  .  swing  .  JButton  (  )  ; 
NameLocationBottomSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
NameLocationBannerLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
DeleteProjectFilesCheckBox  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
Step1ImageLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
singleDetectionButton  =  new   javax  .  swing  .  JRadioButton  (  )  ; 
peerDetectionButton  =  new   javax  .  swing  .  JRadioButton  (  )  ; 
ProjectFolderLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentSeparator1  =  new   javax  .  swing  .  JSeparator  (  )  ; 
AddDocumentPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
DocumentManagerButton  =  new   javax  .  swing  .  JButton  (  )  ; 
AddDocumentBannerLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
Step2ImageLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
ProjectLocationLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
selectedDocumentLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentBannerLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
SelectDocumentsPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
AddDocumentBannerLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
ProjectLocationLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
peerSourceTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
LocationBrowseButton1  =  new   javax  .  swing  .  JButton  (  )  ; 
AddDocumentSeparator2  =  new   javax  .  swing  .  JSeparator  (  )  ; 
KnowledgeBasePanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
KBManagerButton  =  new   javax  .  swing  .  JButton  (  )  ; 
KBManagerBannerLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
KBManagerButton1  =  new   javax  .  swing  .  JButton  (  )  ; 
KBManagerLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
KBManagerSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
KeywordLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
KeyWordSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
Step3ImageLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentBannerLabel3  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentBannerLabel4  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentBannerLabel5  =  new   javax  .  swing  .  JLabel  (  )  ; 
SettingsPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
SettingsBannerLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
Step4ImageLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
SettingsLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
SettingsSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
NumOfPeerDocLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
NumOfExternalLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
NumOfExternalSourcesComboBox  =  new   javax  .  swing  .  JComboBox  (  )  ; 
NumOfPeerSourceComboBox  =  new   javax  .  swing  .  JComboBox  (  )  ; 
ModesOfCheckLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
ModesOfCheckSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
PharaphraseCheckBox  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
jPanel1  =  new   javax  .  swing  .  JPanel  (  )  ; 
PlagiabustWebserverCheckBox  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
InternetSearchCheckBox  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
AddDocumentBannerLabel9  =  new   javax  .  swing  .  JLabel  (  )  ; 
AddDocumentBannerLabel8  =  new   javax  .  swing  .  JLabel  (  )  ; 
StartCheckPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
CheckControlButton  =  new   javax  .  swing  .  JButton  (  )  ; 
CheckBannerLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
Step5ImageLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
TaskCompletionLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
TaskCompletionSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
InternetSearchCompletionLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
PreprocessDocumentCompletionLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
IndexFilesCompletionLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
CrossCheckCompletionLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
DownloadFromInternetProgressBar  =  new   javax  .  swing  .  JProgressBar  (  )  ; 
PreprocessDocumentProgressBar  =  new   javax  .  swing  .  JProgressBar  (  )  ; 
CreateIndexProgressBar  =  new   javax  .  swing  .  JProgressBar  (  )  ; 
CrossCheckProgressBar  =  new   javax  .  swing  .  JProgressBar  (  )  ; 
CommandSeparator  =  new   javax  .  swing  .  JSeparator  (  )  ; 
CommandsLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
ViewButton  =  new   javax  .  swing  .  JButton  (  )  ; 
jScrollPane1  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTextArea1  =  new   javax  .  swing  .  JTextArea  (  )  ; 
PlagiabustSearchCompletionLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
DownloadFromPlagiabustProgressBar  =  new   javax  .  swing  .  JProgressBar  (  )  ; 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
preprocessLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
crosscheckLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
WizardNextButton  =  new   javax  .  swing  .  JButton  (  )  ; 
WizardPreviousButton  =  new   javax  .  swing  .  JButton  (  )  ; 
jLabel20  .  setText  (  "jLabel20"  )  ; 
setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  DISPOSE_ON_CLOSE  )  ; 
setTitle  (  "New Project"  )  ; 
setCursor  (  new   java  .  awt  .  Cursor  (  java  .  awt  .  Cursor  .  DEFAULT_CURSOR  )  )  ; 
setResizable  (  false  )  ; 
WizardMainPanel  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "New Plagiarism Check Wizard :"  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_JUSTIFICATION  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  ,  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  )  ; 
WizardMainPanel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  780  ,  380  )  )  ; 
DescriptionPanel  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "Description"  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_JUSTIFICATION  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  ,  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  )  ; 
DescriptionPanel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  300  ,  50  )  )  ; 
DescriptionScrollPane  .  setHorizontalScrollBarPolicy  (  javax  .  swing  .  ScrollPaneConstants  .  HORIZONTAL_SCROLLBAR_NEVER  )  ; 
DescriptionTextArea  .  setBackground  (  new   java  .  awt  .  Color  (  240  ,  240  ,  240  )  )  ; 
DescriptionTextArea  .  setColumns  (  20  )  ; 
DescriptionTextArea  .  setEditable  (  false  )  ; 
DescriptionTextArea  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
DescriptionTextArea  .  setLineWrap  (  true  )  ; 
DescriptionTextArea  .  setRows  (  5  )  ; 
DescriptionTextArea  .  setText  (  "This area contains helpful information for each step of plagiarism detection process."  )  ; 
DescriptionTextArea  .  setWrapStyleWord  (  true  )  ; 
DescriptionTextArea  .  setBorder  (  null  )  ; 
DescriptionScrollPane  .  setViewportView  (  DescriptionTextArea  )  ; 
WizardImageLabel  .  setHorizontalAlignment  (  javax  .  swing  .  SwingConstants  .  CENTER  )  ; 
WizardImageLabel  .  setIcon  (  new   javax  .  swing  .  ImageIcon  (  getClass  (  )  .  getResource  (  "/Images/New folder.png"  )  )  )  ; 
WizardImageLabel  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
WizardImageLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  300  ,  250  )  )  ; 
javax  .  swing  .  GroupLayout   DescriptionPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  DescriptionPanel  )  ; 
DescriptionPanel  .  setLayout  (  DescriptionPanelLayout  )  ; 
DescriptionPanelLayout  .  setHorizontalGroup  (  DescriptionPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  DescriptionPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  DescriptionPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  DescriptionScrollPane  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  268  ,  Short  .  MAX_VALUE  )  .  addComponent  (  WizardImageLabel  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  268  ,  Short  .  MAX_VALUE  )  )  .  addContainerGap  (  )  )  )  ; 
DescriptionPanelLayout  .  setVerticalGroup  (  DescriptionPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  DescriptionPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  WizardImageLabel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  202  ,  Short  .  MAX_VALUE  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  DescriptionScrollPane  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  104  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  )  ; 
WizardSeparator  .  setOrientation  (  javax  .  swing  .  SwingConstants  .  VERTICAL  )  ; 
WizardTabbedPane  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
WizardTabbedPane  .  addChangeListener  (  new   javax  .  swing  .  event  .  ChangeListener  (  )  { 

public   void   stateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
WizardTabbedPaneStateChanged  (  evt  )  ; 
} 
}  )  ; 
ProjectLocationPanel  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
ProjectLocationPanel  .  setLayout  (  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteLayout  (  )  )  ; 
ProjectNameLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ProjectNameLabel  .  setText  (  "Project Name:"  )  ; 
ProjectLocationPanel  .  add  (  ProjectNameLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  89  ,  -  1  ,  -  1  )  )  ; 
ProjectLocationLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ProjectLocationLabel  .  setText  (  "Project Location:"  )  ; 
ProjectLocationPanel  .  add  (  ProjectLocationLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  133  ,  -  1  ,  -  1  )  )  ; 
ProjectFolderLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ProjectFolderLabel  .  setText  (  "Project Folder:"  )  ; 
ProjectLocationPanel  .  add  (  ProjectFolderLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  186  ,  -  1  ,  -  1  )  )  ; 
ProjectLocationPanel  .  add  (  NameLocationTopSeparator  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  344  ,  19  ,  -  1  ,  -  1  )  )  ; 
NameLocationLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
NameLocationLabel  .  setText  (  "Name and Location"  )  ; 
NameLocationLabel  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
ProjectLocationPanel  .  add  (  NameLocationLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  49  ,  -  1  ,  -  1  )  )  ; 
ProjectNameTextField  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ProjectNameTextField  .  setText  (  "PlagiarismCheckProject"  )  ; 
ProjectNameTextField  .  addKeyListener  (  new   java  .  awt  .  event  .  KeyAdapter  (  )  { 

public   void   keyReleased  (  java  .  awt  .  event  .  KeyEvent   evt  )  { 
ProjectNameTextFieldKeyReleased  (  evt  )  ; 
} 
}  )  ; 
ProjectLocationPanel  .  add  (  ProjectNameTextField  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  145  ,  86  ,  398  ,  -  1  )  )  ; 
ProjectLocationTextField  .  setEditable  (  false  )  ; 
ProjectLocationTextField  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ProjectLocationPanel  .  add  (  ProjectLocationTextField  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  145  ,  130  ,  398  ,  -  1  )  )  ; 
ProjectFolderTextField  .  setEditable  (  false  )  ; 
ProjectFolderTextField  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ProjectLocationPanel  .  add  (  ProjectFolderTextField  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  145  ,  183  ,  398  ,  -  1  )  )  ; 
LocationBrowseButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
LocationBrowseButton  .  setText  (  "Browse"  )  ; 
LocationBrowseButton  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  30  ,  30  )  )  ; 
LocationBrowseButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
LocationBrowseButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
ProjectLocationPanel  .  add  (  LocationBrowseButton  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  557  ,  125  ,  77  ,  -  1  )  )  ; 
ProjectLocationPanel  .  add  (  NameLocationBottomSeparator  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  0  ,  0  ,  -  1  ,  0  )  )  ; 
NameLocationBannerLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
NameLocationBannerLabel  .  setText  (  "Step 1 - Select Name and Location"  )  ; 
NameLocationBannerLabel  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
NameLocationBannerLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  212  ,  32  )  )  ; 
ProjectLocationPanel  .  add  (  NameLocationBannerLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  11  ,  240  ,  -  1  )  )  ; 
DeleteProjectFilesCheckBox  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
DeleteProjectFilesCheckBox  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
DeleteProjectFilesCheckBox  .  setText  (  "Delete Project Related Files After Completion"  )  ; 
ProjectLocationPanel  .  add  (  DeleteProjectFilesCheckBox  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  266  ,  -  1  ,  -  1  )  )  ; 
Step1ImageLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  32  ,  32  )  )  ; 
ProjectLocationPanel  .  add  (  Step1ImageLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  702  ,  78  ,  -  1  ,  -  1  )  )  ; 
singleDetectionButton  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
buttonGroup1  .  add  (  singleDetectionButton  )  ; 
singleDetectionButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
singleDetectionButton  .  setText  (  "Single"  )  ; 
singleDetectionButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
singleDetectionButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
ProjectLocationPanel  .  add  (  singleDetectionButton  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  145  ,  225  ,  -  1  ,  -  1  )  )  ; 
peerDetectionButton  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
buttonGroup1  .  add  (  peerDetectionButton  )  ; 
peerDetectionButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
peerDetectionButton  .  setText  (  "Peer"  )  ; 
ProjectLocationPanel  .  add  (  peerDetectionButton  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  220  ,  225  ,  -  1  ,  -  1  )  )  ; 
ProjectFolderLabel1  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ProjectFolderLabel1  .  setText  (  "Detection Choice"  )  ; 
ProjectFolderLabel1  .  setRequestFocusEnabled  (  false  )  ; 
ProjectFolderLabel1  .  setVerifyInputWhenFocusTarget  (  false  )  ; 
ProjectLocationPanel  .  add  (  ProjectFolderLabel1  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  233  ,  -  1  ,  -  1  )  )  ; 
ProjectLocationPanel  .  add  (  AddDocumentSeparator1  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  70  ,  724  ,  -  1  )  )  ; 
WizardTabbedPane  .  addTab  (  "New Project"  ,  ProjectLocationPanel  )  ; 
AddDocumentPanel  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
DocumentManagerButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
DocumentManagerButton  .  setText  (  "Select Document"  )  ; 
DocumentManagerButton  .  setHorizontalTextPosition  (  javax  .  swing  .  SwingConstants  .  CENTER  )  ; 
DocumentManagerButton  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  180  ,  40  )  )  ; 
DocumentManagerButton  .  setVerifyInputWhenFocusTarget  (  false  )  ; 
DocumentManagerButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
DocumentManagerButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
AddDocumentBannerLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
AddDocumentBannerLabel  .  setText  (  "Step 2 - Select a single document to check for plagiarism."  )  ; 
AddDocumentBannerLabel  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
AddDocumentBannerLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  327  ,  32  )  )  ; 
AddDocumentLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
AddDocumentLabel  .  setText  (  "Add Document"  )  ; 
AddDocumentLabel  .  setVerticalTextPosition  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
Step2ImageLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  32  ,  32  )  )  ; 
ProjectLocationLabel2  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
ProjectLocationLabel2  .  setText  (  "Selected Document:"  )  ; 
selectedDocumentLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
AddDocumentBannerLabel2  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
AddDocumentBannerLabel2  .  setText  (  "Select a single document for the plagiarism test. You can select MS Word (.docx, .doc), .pdf, .rtf, .txt files."  )  ; 
AddDocumentBannerLabel2  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
AddDocumentBannerLabel2  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  327  ,  32  )  )  ; 
javax  .  swing  .  GroupLayout   AddDocumentPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  AddDocumentPanel  )  ; 
AddDocumentPanel  .  setLayout  (  AddDocumentPanelLayout  )  ; 
AddDocumentPanelLayout  .  setHorizontalGroup  (  AddDocumentPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  AddDocumentPanelLayout  .  createSequentialGroup  (  )  .  addGroup  (  AddDocumentPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  AddDocumentPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  AddDocumentPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  AddDocumentSeparator  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  720  ,  Short  .  MAX_VALUE  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  AddDocumentPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  AddDocumentBannerLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  382  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  306  ,  Short  .  MAX_VALUE  )  .  addComponent  (  Step2ImageLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addComponent  (  AddDocumentLabel  )  )  )  .  addGroup  (  AddDocumentPanelLayout  .  createSequentialGroup  (  )  .  addGap  (  38  ,  38  ,  38  )  .  addComponent  (  ProjectLocationLabel2  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  selectedDocumentLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  471  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  AddDocumentPanelLayout  .  createSequentialGroup  (  )  .  addGap  (  88  ,  88  ,  88  )  .  addComponent  (  DocumentManagerButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  AddDocumentPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  AddDocumentBannerLabel2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  666  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  .  addContainerGap  (  )  )  )  ; 
AddDocumentPanelLayout  .  setVerticalGroup  (  AddDocumentPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  AddDocumentPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  AddDocumentPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  Step2ImageLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  AddDocumentBannerLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  32  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  AddDocumentLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  AddDocumentSeparator  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  12  ,  12  ,  12  )  .  addComponent  (  AddDocumentBannerLabel2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  32  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  DocumentManagerButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  32  ,  32  ,  32  )  .  addGroup  (  AddDocumentPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  ProjectLocationLabel2  )  .  addComponent  (  selectedDocumentLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  15  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addContainerGap  (  315  ,  Short  .  MAX_VALUE  )  )  )  ; 
WizardTabbedPane  .  addTab  (  "Suspicious Document"  ,  AddDocumentPanel  )  ; 
SelectDocumentsPanel  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
SelectDocumentsPanel  .  setLayout  (  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteLayout  (  )  )  ; 
AddDocumentBannerLabel1  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
AddDocumentBannerLabel1  .  setText  (  "Step 2 - Add Documents for new Plagiarism Detection Project"  )  ; 
AddDocumentBannerLabel1  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
AddDocumentBannerLabel1  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  327  ,  32  )  )  ; 
SelectDocumentsPanel  .  add  (  AddDocumentBannerLabel1  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  13  ,  430  ,  -  1  )  )  ; 
AddDocumentLabel1  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
AddDocumentLabel1  .  setText  (  "Select Sources"  )  ; 
AddDocumentLabel1  .  setVerticalTextPosition  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
SelectDocumentsPanel  .  add  (  AddDocumentLabel1  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  51  ,  -  1  ,  -  1  )  )  ; 
ProjectLocationLabel1  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ProjectLocationLabel1  .  setText  (  "Source Location"  )  ; 
SelectDocumentsPanel  .  add  (  ProjectLocationLabel1  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  100  ,  -  1  ,  -  1  )  )  ; 
peerSourceTextField  .  setEditable  (  false  )  ; 
peerSourceTextField  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
peerSourceTextField  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
peerSourceTextFieldActionPerformed  (  evt  )  ; 
} 
}  )  ; 
SelectDocumentsPanel  .  add  (  peerSourceTextField  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  133  ,  97  ,  338  ,  -  1  )  )  ; 
LocationBrowseButton1  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
LocationBrowseButton1  .  setText  (  "Browse"  )  ; 
LocationBrowseButton1  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  30  ,  30  )  )  ; 
LocationBrowseButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
LocationBrowseButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
SelectDocumentsPanel  .  add  (  LocationBrowseButton1  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  481  ,  92  ,  77  ,  -  1  )  )  ; 
SelectDocumentsPanel  .  add  (  AddDocumentSeparator2  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  72  ,  724  ,  -  1  )  )  ; 
WizardTabbedPane  .  addTab  (  "Source Documents"  ,  SelectDocumentsPanel  )  ; 
KnowledgeBasePanel  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
KBManagerButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
KBManagerButton  .  setText  (  "Plagiabust Server Manager"  )  ; 
KBManagerButton  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  180  ,  40  )  )  ; 
KBManagerButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
KBManagerButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
KBManagerBannerLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
KBManagerBannerLabel  .  setText  (  "Step 3 - Creating a Knowledge Base For the Subject"  )  ; 
KBManagerBannerLabel  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
KBManagerBannerLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  254  ,  32  )  )  ; 
KBManagerButton1  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
KBManagerButton1  .  setText  (  "Keyword Manager"  )  ; 
KBManagerButton1  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  180  ,  40  )  )  ; 
KBManagerButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
KBManagerButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
KBManagerLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
KBManagerLabel  .  setText  (  "Knowledge Base"  )  ; 
KeywordLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
KeywordLabel  .  setText  (  "List of Keywords"  )  ; 
Step3ImageLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  32  ,  32  )  )  ; 
AddDocumentBannerLabel3  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
AddDocumentBannerLabel3  .  setText  (  "Using Plagiabust Server Manager you can add all sort of documents to create a knwoldege base for particular subject of plagiarism"  )  ; 
AddDocumentBannerLabel3  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
AddDocumentBannerLabel3  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  327  ,  32  )  )  ; 
AddDocumentBannerLabel4  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
AddDocumentBannerLabel4  .  setText  (  "test."  )  ; 
AddDocumentBannerLabel4  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
AddDocumentBannerLabel4  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  327  ,  32  )  )  ; 
AddDocumentBannerLabel5  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
AddDocumentBannerLabel5  .  setText  (  "Using Keyword Manager create a list of Keywords that are particular to the subject of plagiarism test."  )  ; 
AddDocumentBannerLabel5  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
AddDocumentBannerLabel5  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  327  ,  32  )  )  ; 
javax  .  swing  .  GroupLayout   KnowledgeBasePanelLayout  =  new   javax  .  swing  .  GroupLayout  (  KnowledgeBasePanel  )  ; 
KnowledgeBasePanel  .  setLayout  (  KnowledgeBasePanelLayout  )  ; 
KnowledgeBasePanelLayout  .  setHorizontalGroup  (  KnowledgeBasePanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  KBManagerSeparator  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  720  ,  Short  .  MAX_VALUE  )  .  addComponent  (  KeyWordSeparator  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  720  ,  Short  .  MAX_VALUE  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  KBManagerBannerLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  330  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  358  ,  Short  .  MAX_VALUE  )  .  addComponent  (  Step3ImageLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  KBManagerLabel  )  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addGap  (  182  ,  182  ,  182  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  KBManagerButton1  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  KBManagerButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  )  .  addContainerGap  (  )  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  AddDocumentBannerLabel3  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  710  ,  Short  .  MAX_VALUE  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  AddDocumentBannerLabel4  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  359  ,  Short  .  MAX_VALUE  )  .  addGap  (  351  ,  351  ,  351  )  )  )  .  addGap  (  20  ,  20  ,  20  )  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  KeywordLabel  )  .  addContainerGap  (  627  ,  Short  .  MAX_VALUE  )  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  AddDocumentBannerLabel5  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  710  ,  Short  .  MAX_VALUE  )  .  addGap  (  20  ,  20  ,  20  )  )  )  ; 
KnowledgeBasePanelLayout  .  setVerticalGroup  (  KnowledgeBasePanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  KnowledgeBasePanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  KBManagerBannerLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  32  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  Step3ImageLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  KBManagerLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  KBManagerSeparator  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  AddDocumentBannerLabel3  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  25  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  AddDocumentBannerLabel4  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  25  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  19  ,  Short  .  MAX_VALUE  )  .  addComponent  (  KBManagerButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  28  ,  28  ,  28  )  .  addComponent  (  KeywordLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  AddDocumentBannerLabel5  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  25  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  15  ,  15  ,  15  )  .  addComponent  (  KBManagerButton1  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  84  ,  84  ,  84  )  .  addComponent  (  KeyWordSeparator  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  104  ,  104  ,  104  )  )  )  ; 
WizardTabbedPane  .  addTab  (  "Knowledge Base "  ,  KnowledgeBasePanel  )  ; 
SettingsPanel  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
SettingsBannerLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
SettingsBannerLabel  .  setText  (  "Step 4 - Apply Custom Settings for the Project"  )  ; 
SettingsBannerLabel  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
SettingsBannerLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  237  ,  32  )  )  ; 
Step4ImageLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  32  ,  32  )  )  ; 
SettingsLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
SettingsLabel  .  setText  (  "Per Document Settings"  )  ; 
NumOfPeerDocLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
NumOfPeerDocLabel  .  setText  (  "Maximum Number of Peer Documents : "  )  ; 
NumOfExternalLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
NumOfExternalLabel  .  setText  (  "Maximum Number of External Sources : "  )  ; 
NumOfExternalSourcesComboBox  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
NumOfExternalSourcesComboBox  .  setModel  (  new   javax  .  swing  .  DefaultComboBoxModel  (  new   String  [  ]  {  "1"  ,  "2"  ,  "3"  ,  "4"  ,  "5"  ,  "6"  ,  "7"  ,  "8"  ,  "9"  ,  "10"  ,  "15"  ,  "20"  }  )  )  ; 
NumOfExternalSourcesComboBox  .  setSelectedIndex  (  9  )  ; 
NumOfPeerSourceComboBox  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
NumOfPeerSourceComboBox  .  setModel  (  new   javax  .  swing  .  DefaultComboBoxModel  (  new   String  [  ]  {  "1"  ,  "2"  ,  "3"  ,  "4"  ,  "5"  ,  "6"  ,  "7"  ,  "8"  ,  "9"  ,  "10"  ,  "15"  ,  "20"  }  )  )  ; 
NumOfPeerSourceComboBox  .  setSelectedIndex  (  9  )  ; 
ModesOfCheckLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
ModesOfCheckLabel  .  setText  (  "Modes of Check"  )  ; 
PharaphraseCheckBox  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
PharaphraseCheckBox  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
PharaphraseCheckBox  .  setText  (  "Paraphrased Plagiarism Detection"  )  ; 
jPanel1  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
jPanel1  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createTitledBorder  (  null  ,  "Externel Source Detection"  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_JUSTIFICATION  ,  javax  .  swing  .  border  .  TitledBorder  .  DEFAULT_POSITION  ,  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  )  ; 
PlagiabustWebserverCheckBox  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
PlagiabustWebserverCheckBox  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
PlagiabustWebserverCheckBox  .  setText  (  "Plagiabust Webserver Search"  )  ; 
PlagiabustWebserverCheckBox  .  addItemListener  (  new   java  .  awt  .  event  .  ItemListener  (  )  { 

public   void   itemStateChanged  (  java  .  awt  .  event  .  ItemEvent   evt  )  { 
PlagiabustWebserverCheckBoxItemStateChanged  (  evt  )  ; 
} 
}  )  ; 
InternetSearchCheckBox  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
InternetSearchCheckBox  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
InternetSearchCheckBox  .  setText  (  "Internet Search"  )  ; 
InternetSearchCheckBox  .  addItemListener  (  new   java  .  awt  .  event  .  ItemListener  (  )  { 

public   void   itemStateChanged  (  java  .  awt  .  event  .  ItemEvent   evt  )  { 
InternetSearchCheckBoxItemStateChanged  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   jPanel1Layout  =  new   javax  .  swing  .  GroupLayout  (  jPanel1  )  ; 
jPanel1  .  setLayout  (  jPanel1Layout  )  ; 
jPanel1Layout  .  setHorizontalGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanel1Layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  PlagiabustWebserverCheckBox  )  .  addComponent  (  InternetSearchCheckBox  )  )  .  addContainerGap  (  519  ,  Short  .  MAX_VALUE  )  )  )  ; 
jPanel1Layout  .  setVerticalGroup  (  jPanel1Layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  jPanel1Layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  PlagiabustWebserverCheckBox  )  .  addGap  (  18  ,  18  ,  18  )  .  addComponent  (  InternetSearchCheckBox  )  .  addContainerGap  (  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  )  )  ; 
AddDocumentBannerLabel9  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
AddDocumentBannerLabel9  .  setText  (  "Each document can be plagiarised by external sources and other peer documents. Set approximated value for number of each"  )  ; 
AddDocumentBannerLabel9  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
AddDocumentBannerLabel9  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  327  ,  32  )  )  ; 
AddDocumentBannerLabel8  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
AddDocumentBannerLabel8  .  setText  (  "kind of sources."  )  ; 
AddDocumentBannerLabel8  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
AddDocumentBannerLabel8  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  327  ,  32  )  )  ; 
javax  .  swing  .  GroupLayout   SettingsPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  SettingsPanel  )  ; 
SettingsPanel  .  setLayout  (  SettingsPanelLayout  )  ; 
SettingsPanelLayout  .  setHorizontalGroup  (  SettingsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  SettingsPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  SettingsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  jPanel1  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addComponent  (  PharaphraseCheckBox  )  .  addComponent  (  ModesOfCheckSeparator  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  720  ,  Short  .  MAX_VALUE  )  .  addComponent  (  SettingsSeparator  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  720  ,  Short  .  MAX_VALUE  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  SettingsPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  SettingsBannerLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  307  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  381  ,  Short  .  MAX_VALUE  )  .  addComponent  (  Step4ImageLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addComponent  (  SettingsLabel  )  .  addGroup  (  SettingsPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  NumOfPeerDocLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  336  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  NumOfPeerSourceComboBox  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGroup  (  SettingsPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  NumOfExternalLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  336  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  NumOfExternalSourcesComboBox  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addComponent  (  ModesOfCheckLabel  )  .  addComponent  (  AddDocumentBannerLabel8  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  720  ,  Short  .  MAX_VALUE  )  .  addComponent  (  AddDocumentBannerLabel9  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  720  ,  Short  .  MAX_VALUE  )  )  .  addContainerGap  (  )  )  )  ; 
SettingsPanelLayout  .  setVerticalGroup  (  SettingsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  SettingsPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  SettingsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  SettingsBannerLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  32  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  Step4ImageLabel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  SettingsLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  SettingsSeparator  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  10  ,  10  ,  10  )  .  addComponent  (  AddDocumentBannerLabel9  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  18  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  AddDocumentBannerLabel8  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  15  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  SettingsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  NumOfPeerSourceComboBox  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  NumOfPeerDocLabel  )  )  .  addGap  (  18  ,  18  ,  18  )  .  addGroup  (  SettingsPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  NumOfExternalLabel  )  .  addComponent  (  NumOfExternalSourcesComboBox  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  .  addGap  (  28  ,  28  ,  28  )  .  addComponent  (  ModesOfCheckLabel  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  ModesOfCheckSeparator  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  PharaphraseCheckBox  )  .  addGap  (  25  ,  25  ,  25  )  .  addComponent  (  jPanel1  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  130  ,  Short  .  MAX_VALUE  )  )  )  ; 
WizardTabbedPane  .  addTab  (  "Settings    "  ,  SettingsPanel  )  ; 
StartCheckPanel  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
StartCheckPanel  .  setLayout  (  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteLayout  (  )  )  ; 
CheckControlButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
CheckControlButton  .  setText  (  "Start Check"  )  ; 
CheckControlButton  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  180  ,  40  )  )  ; 
CheckControlButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
CheckControlButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
StartCheckPanel  .  add  (  CheckControlButton  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  364  ,  247  ,  141  ,  -  1  )  )  ; 
CheckBannerLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
CheckBannerLabel  .  setText  (  "Step 5 - Start Plagiarism Detection Process"  )  ; 
CheckBannerLabel  .  setVerticalAlignment  (  javax  .  swing  .  SwingConstants  .  TOP  )  ; 
CheckBannerLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  252  ,  32  )  )  ; 
StartCheckPanel  .  add  (  CheckBannerLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  11  ,  295  ,  -  1  )  )  ; 
Step5ImageLabel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  32  ,  32  )  )  ; 
StartCheckPanel  .  add  (  Step5ImageLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  1764  ,  34  ,  -  1  ,  -  1  )  )  ; 
TaskCompletionLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
TaskCompletionLabel  .  setText  (  "Task Completion"  )  ; 
StartCheckPanel  .  add  (  TaskCompletionLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  49  ,  -  1  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  TaskCompletionSeparator  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  0  ,  0  ,  -  1  ,  -  1  )  )  ; 
InternetSearchCompletionLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
InternetSearchCompletionLabel  .  setText  (  "Downloading From Internet "  )  ; 
StartCheckPanel  .  add  (  InternetSearchCompletionLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  106  ,  -  1  ,  -  1  )  )  ; 
PreprocessDocumentCompletionLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
PreprocessDocumentCompletionLabel  .  setText  (  "Preprocess Documents"  )  ; 
StartCheckPanel  .  add  (  PreprocessDocumentCompletionLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  148  ,  -  1  ,  -  1  )  )  ; 
IndexFilesCompletionLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
IndexFilesCompletionLabel  .  setText  (  "Creating File Indexes"  )  ; 
StartCheckPanel  .  add  (  IndexFilesCompletionLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  127  ,  -  1  ,  -  1  )  )  ; 
CrossCheckCompletionLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
CrossCheckCompletionLabel  .  setText  (  "Document Cross Check"  )  ; 
StartCheckPanel  .  add  (  CrossCheckCompletionLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  169  ,  -  1  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  DownloadFromInternetProgressBar  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  187  ,  106  ,  460  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  PreprocessDocumentProgressBar  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  187  ,  149  ,  460  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  CreateIndexProgressBar  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  187  ,  128  ,  460  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  CrossCheckProgressBar  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  187  ,  170  ,  460  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  CommandSeparator  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  255  ,  119  ,  -  1  ,  -  1  )  )  ; 
CommandsLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  1  ,  12  )  )  ; 
CommandsLabel  .  setText  (  "Commands"  )  ; 
StartCheckPanel  .  add  (  CommandsLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  190  ,  -  1  ,  -  1  )  )  ; 
ViewButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
ViewButton  .  setText  (  "View Report"  )  ; 
ViewButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
ViewButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
StartCheckPanel  .  add  (  ViewButton  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  530  ,  246  ,  117  ,  43  )  )  ; 
jTextArea1  .  setColumns  (  20  )  ; 
jTextArea1  .  setEditable  (  false  )  ; 
jTextArea1  .  setLineWrap  (  true  )  ; 
jTextArea1  .  setRows  (  5  )  ; 
jScrollPane1  .  setViewportView  (  jTextArea1  )  ; 
StartCheckPanel  .  add  (  jScrollPane1  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  312  ,  707  ,  177  )  )  ; 
PlagiabustSearchCompletionLabel  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
PlagiabustSearchCompletionLabel  .  setText  (  "Downloading From Plagiabust"  )  ; 
StartCheckPanel  .  add  (  PlagiabustSearchCompletionLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  85  ,  -  1  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  DownloadFromPlagiabustProgressBar  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  187  ,  85  ,  460  ,  -  1  )  )  ; 
jLabel1  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
jLabel1  .  setText  (  "Use command button to initaite,"  )  ; 
StartCheckPanel  .  add  (  jLabel1  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  211  ,  390  ,  -  1  )  )  ; 
jLabel2  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
jLabel2  .  setText  (  "terminate and view check results"  )  ; 
StartCheckPanel  .  add  (  jLabel2  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  10  ,  230  ,  -  1  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  preprocessLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  660  ,  150  ,  70  ,  -  1  )  )  ; 
StartCheckPanel  .  add  (  crosscheckLabel  ,  new   org  .  netbeans  .  lib  .  awtextra  .  AbsoluteConstraints  (  660  ,  170  ,  70  ,  -  1  )  )  ; 
WizardTabbedPane  .  addTab  (  "Start Check   "  ,  StartCheckPanel  )  ; 
WizardNextButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
WizardNextButton  .  setIcon  (  new   javax  .  swing  .  ImageIcon  (  getClass  (  )  .  getResource  (  "/Images/next-icon.png"  )  )  )  ; 
WizardNextButton  .  setText  (  "Next Step"  )  ; 
WizardNextButton  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  180  ,  40  )  )  ; 
WizardNextButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
WizardNextButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
WizardPreviousButton  .  setBackground  (  new   java  .  awt  .  Color  (  255  ,  255  ,  255  )  )  ; 
WizardPreviousButton  .  setFont  (  new   java  .  awt  .  Font  (  "Tahoma"  ,  0  ,  12  )  )  ; 
WizardPreviousButton  .  setIcon  (  new   javax  .  swing  .  ImageIcon  (  getClass  (  )  .  getResource  (  "/Images/previous-icon.png"  )  )  )  ; 
WizardPreviousButton  .  setText  (  "Previous Step"  )  ; 
WizardPreviousButton  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  180  ,  40  )  )  ; 
WizardPreviousButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
WizardPreviousButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
javax  .  swing  .  GroupLayout   WizardMainPanelLayout  =  new   javax  .  swing  .  GroupLayout  (  WizardMainPanel  )  ; 
WizardMainPanel  .  setLayout  (  WizardMainPanelLayout  )  ; 
WizardMainPanelLayout  .  setHorizontalGroup  (  WizardMainPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  WizardMainPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  DescriptionPanel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  8  ,  8  ,  8  )  .  addComponent  (  WizardSeparator  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  WizardMainPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  WizardMainPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  WizardPreviousButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  ,  34  ,  Short  .  MAX_VALUE  )  .  addComponent  (  WizardNextButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addGap  (  361  ,  361  ,  361  )  )  .  addGroup  (  WizardMainPanelLayout  .  createSequentialGroup  (  )  .  addComponent  (  WizardTabbedPane  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  745  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  )  )  )  )  )  ; 
WizardMainPanelLayout  .  setVerticalGroup  (  WizardMainPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  WizardSeparator  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  635  ,  Short  .  MAX_VALUE  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  WizardMainPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  WizardTabbedPane  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  558  ,  Short  .  MAX_VALUE  )  .  addGap  (  26  ,  26  ,  26  )  .  addGroup  (  WizardMainPanelLayout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  WizardPreviousButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  WizardNextButton  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  .  addGroup  (  WizardMainPanelLayout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  DescriptionPanel  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  363  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addContainerGap  (  261  ,  Short  .  MAX_VALUE  )  )  )  ; 
javax  .  swing  .  GroupLayout   layout  =  new   javax  .  swing  .  GroupLayout  (  getContentPane  (  )  )  ; 
getContentPane  (  )  .  setLayout  (  layout  )  ; 
layout  .  setHorizontalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  WizardMainPanel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  1093  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  ; 
layout  .  setVerticalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addComponent  (  WizardMainPanel  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  663  ,  Short  .  MAX_VALUE  )  .  addContainerGap  (  )  )  )  ; 
WizardMainPanel  .  getAccessibleContext  (  )  .  setAccessibleName  (  "Plagiarism Check :"  )  ; 
pack  (  )  ; 
} 

private   void   DocumentManagerButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
DocumentManagerForm  .  getInstance  (  )  .  setVisible  (  true  )  ; 
} 

private   void   KBManagerButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
PlagiabustServerManager   plagiabustServerManager  =  new   PlagiabustServerManager  (  )  ; 
plagiabustServerManager  .  setVisible  (  true  )  ; 
} 

private   void   KBManagerButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
KeywordManager  .  getInstance  (  )  .  setVisible  (  true  )  ; 
} 

private   void   WizardTabbedPaneStateChanged  (  javax  .  swing  .  event  .  ChangeEvent   evt  )  { 
int   currentPanelIndex  =  WizardTabbedPane  .  getSelectedIndex  (  )  ; 
setDescriptionInformation  (  currentPanelIndex  )  ; 
if  (  currentPanelIndex  ==  TabNames  .  StartCheck  .  ordinal  (  )  )  { 
WizardNextButton  .  setVisible  (  false  )  ; 
WizardPreviousButton  .  setVisible  (  true  )  ; 
}  else   if  (  currentPanelIndex  ==  TabNames  .  NewProject  .  ordinal  (  )  )  { 
WizardPreviousButton  .  setVisible  (  false  )  ; 
WizardNextButton  .  setVisible  (  true  )  ; 
}  else  { 
WizardNextButton  .  setVisible  (  true  )  ; 
WizardPreviousButton  .  setVisible  (  true  )  ; 
} 
} 

private   void   WizardPreviousButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
int   currentTabIndex  =  WizardTabbedPane  .  getSelectedIndex  (  )  ; 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  false  )  ; 
WizardTabbedPane  .  setSelectedIndex  (  --  currentTabIndex  )  ; 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  true  )  ; 
if  (  peerDetectionButton  .  isSelected  (  )  &&  currentTabIndex  ==  TabNames  .  AddDocument  .  ordinal  (  )  )  { 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  false  )  ; 
WizardTabbedPane  .  setSelectedIndex  (  --  currentTabIndex  )  ; 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  true  )  ; 
} 
} 

private   void   WizardNextButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
int   currentTabIndex  =  WizardTabbedPane  .  getSelectedIndex  (  )  ; 
if  (  isTabDataValid  (  currentTabIndex  )  )  { 
if  (  singleDetectionButton  .  isSelected  (  )  &&  WizardTabbedPane  .  getSelectedIndex  (  )  ==  TabNames  .  NewProject  .  ordinal  (  )  )  { 
WizardTabbedPane  .  setEnabledAt  (  TabNames  .  AddDocument  .  ordinal  (  )  ,  true  )  ; 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  false  )  ; 
WizardTabbedPane  .  setSelectedIndex  (  ++  currentTabIndex  )  ; 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  true  )  ; 
}  else   if  (  peerDetectionButton  .  isSelected  (  )  &&  WizardTabbedPane  .  getSelectedIndex  (  )  ==  TabNames  .  NewProject  .  ordinal  (  )  )  { 
WizardTabbedPane  .  setEnabledAt  (  TabNames  .  AddDocument  .  ordinal  (  )  ,  false  )  ; 
WizardTabbedPane  .  setSelectedIndex  (  TabNames  .  SelectDocument  .  ordinal  (  )  )  ; 
WizardTabbedPane  .  setEnabledAt  (  TabNames  .  NewProject  .  ordinal  (  )  ,  false  )  ; 
currentTabIndex  =  TabNames  .  SelectDocument  .  ordinal  (  )  ; 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  true  )  ; 
}  else  { 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  false  )  ; 
WizardTabbedPane  .  setSelectedIndex  (  ++  currentTabIndex  )  ; 
WizardTabbedPane  .  setEnabledAt  (  currentTabIndex  ,  true  )  ; 
} 
} 
} 

public   boolean   isTabDataValid  (  int   selectedTabIndex  )  { 
boolean   isValidData  =  true  ; 
switch  (  selectedTabIndex  )  { 
case   0  : 
if  (  ProjectLocationTextField  .  getText  (  )  .  equals  (  ""  )  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please select a valid project location to continue."  ,  "Notification"  ,  JOptionPane  .  INFORMATION_MESSAGE  ,  Main  .  getPlagiabustIcon  (  )  )  ; 
isValidData  =  false  ; 
} 
if  (  ProjectNameTextField  .  getText  (  )  .  equals  (  ""  )  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please enter a valid project name to continue."  ,  "Notification"  ,  JOptionPane  .  INFORMATION_MESSAGE  ,  Main  .  getPlagiabustIcon  (  )  )  ; 
isValidData  =  false  ; 
} 
if  (  !  isValidFileName  (  ProjectNameTextField  .  getText  (  )  )  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Project name cannot contain any of the following characters.\n / \\ ? : * : \" < > | "  ,  "Notification"  ,  JOptionPane  .  INFORMATION_MESSAGE  ,  Main  .  getPlagiabustIcon  (  )  )  ; 
isValidData  =  false  ; 
} 
break  ; 
case   1  : 
if  (  singleDetectionButton  .  isSelected  (  )  &&  selectedDocumentLabel  .  getText  (  )  .  equals  (  ""  )  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please select a file to continue."  ,  "Notification"  ,  JOptionPane  .  INFORMATION_MESSAGE  ,  Main  .  getPlagiabustIcon  (  )  )  ; 
isValidData  =  false  ; 
} 
break  ; 
case   2  : 
if  (  peerSourceTextField  .  getText  (  )  .  equals  (  ""  )  )  { 
JOptionPane  .  showMessageDialog  (  null  ,  "Please select a source location of documents to continue."  ,  "Notification"  ,  JOptionPane  .  INFORMATION_MESSAGE  ,  Main  .  getPlagiabustIcon  (  )  )  ; 
isValidData  =  false  ; 
} 
break  ; 
default  : 
break  ; 
} 
return   isValidData  ; 
} 

public   boolean   isValidFileName  (  String   fileName  )  { 
String  [  ]  characters  =  {  "/"  ,  "\\"  ,  ":"  ,  "*"  ,  "?"  ,  "\""  ,  "<"  ,  ">"  ,  "|"  }  ; 
for  (  int   i  =  0  ;  i  <  characters  .  length  ;  i  ++  )  { 
if  (  fileName  .  contains  (  characters  [  i  ]  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 

public   void   setDescriptionInformation  (  int   selectedTabIndex  )  { 
switch  (  selectedTabIndex  )  { 
case   0  : 
ImageIcon   selectIcon  =  new   ImageIcon  (  "src"  +  File  .  separator  +  "Images"  +  File  .  separator  +  "New folder.png"  )  ; 
WizardImageLabel  .  setIcon  (  selectIcon  )  ; 
DescriptionTextArea  .  setText  (  "Step 1 - Select Name and Location \nSelect a suitable project name and then a location. \nAll the documents and internet downloaded documents are saved here."  )  ; 
break  ; 
case   1  : 
String   fname  =  "src"  +  File  .  separator  +  "Images"  +  File  .  separator  +  "New document.png"  ; 
ImageIcon   documentIcon  =  new   ImageIcon  (  fname  )  ; 
WizardImageLabel  .  setIcon  (  documentIcon  )  ; 
DescriptionTextArea  .  setText  (  "Step 2 - Select a single document to check for plagiarism. This document is cross checked for plagiarism with the set of documents chosen in the next step."  )  ; 
break  ; 
case   2  : 
ImageIcon   documentsIcon  =  new   ImageIcon  (  "src"  +  File  .  separator  +  "Images"  +  File  .  separator  +  "Add documents.png"  )  ; 
WizardImageLabel  .  setIcon  (  documentsIcon  )  ; 
if  (  singleDetectionButton  .  isSelected  (  )  )  { 
DescriptionTextArea  .  setText  (  "Step 3 - Add Documents for new Plagiarism Detection Project. These documents are used to compare with the document ealier selected."  )  ; 
AddDocumentBannerLabel1  .  setText  (  "Step 3 - Add Documents for new Plagiarism Detection Project"  )  ; 
}  else  { 
DescriptionTextArea  .  setText  (  "Step 2 - Add Documents for new Plagiarism Detection Project"  )  ; 
AddDocumentBannerLabel1  .  setText  (  "Step 2 - Add Documents for new Plagiarism Detection Project"  )  ; 
} 
break  ; 
case   3  : 
ImageIcon   knowledgeBaseIcon  =  new   ImageIcon  (  "src"  +  File  .  separator  +  "Images"  +  File  .  separator  +  "knowlegde_base_icon.png"  )  ; 
WizardImageLabel  .  setIcon  (  knowledgeBaseIcon  )  ; 
if  (  singleDetectionButton  .  isSelected  (  )  )  { 
DescriptionTextArea  .  setText  (  "Step 4 - Creating a Knowledge Base For the Subject"  )  ; 
KBManagerBannerLabel  .  setText  (  "Step 4 - Creating a Knowledge Base For the Subject"  )  ; 
}  else  { 
DescriptionTextArea  .  setText  (  "Step 3 - Creating a Knowledge Base For the Subject"  )  ; 
KBManagerBannerLabel  .  setText  (  "Step 3 - Creating a Knowledge Base For the Subject"  )  ; 
} 
break  ; 
case   4  : 
ImageIcon   settingsIcon  =  new   ImageIcon  (  "src"  +  File  .  separator  +  "Images"  +  File  .  separator  +  "Wizard settings.png"  )  ; 
WizardImageLabel  .  setIcon  (  settingsIcon  )  ; 
if  (  singleDetectionButton  .  isSelected  (  )  )  { 
DescriptionTextArea  .  setText  (  "Step 5 - Apply Custom Settings for the Project"  )  ; 
SettingsBannerLabel  .  setText  (  "Step 5 - Apply Custom Settings for the Project"  )  ; 
}  else  { 
DescriptionTextArea  .  setText  (  "Step 4 - Apply Custom Settings for the Project"  )  ; 
SettingsBannerLabel  .  setText  (  "Step 4 - Apply Custom Settings for the Project"  )  ; 
} 
break  ; 
case   5  : 
ImageIcon   startCheckIcon  =  new   ImageIcon  (  "src"  +  File  .  separator  +  "Images"  +  File  .  separator  +  "Start check.png"  )  ; 
WizardImageLabel  .  setIcon  (  startCheckIcon  )  ; 
if  (  singleDetectionButton  .  isSelected  (  )  )  { 
DescriptionTextArea  .  setText  (  "Step 6 - Start Plagiarism Detection Process"  )  ; 
CheckBannerLabel  .  setText  (  "Step 6 - Start Plagiarism Detection Process"  )  ; 
}  else  { 
PreprocessDocumentCompletionLabel  .  setText  (  "Index Files Cross Check"  )  ; 
CrossCheckCompletionLabel  .  setText  (  "Download Files Cross Check"  )  ; 
DescriptionTextArea  .  setText  (  "Step 5 - Start Plagiarism Detection Process"  )  ; 
CheckBannerLabel  .  setText  (  "Step 5 - Start Plagiarism Detection Process"  )  ; 
} 
break  ; 
default  : 
break  ; 
} 
} 

public   void   setup  (  )  { 
sourceFolderName  =  peerSourceTextField  .  getText  (  )  ; 
fName  =  fileNameConverter  (  DocumentManagerForm  .  fileName  )  ; 
String   fullPathName  =  DocumentManagerForm  .  selectedFileName  ; 
File   projectFolder  =  new   File  (  ProjectFolderTextField  .  getText  (  )  )  ; 
projectFolder  .  mkdir  (  )  ; 
FileOperator   fileOPerator  =  new   FileOperator  (  sourceFolderName  ,  projectFolder  .  getAbsolutePath  (  )  )  ; 
fileOPerator  .  anyToTextConverter  (  fullPathName  )  ; 
fileOPerator  .  TextFileIndexer  (  )  ; 
destFolderPath  =  fileOPerator  .  getDestinatonFolderPath  (  )  ; 
indexFolderPath  =  fileOPerator  .  getIndexFolderPath  (  )  ; 
} 

public   String   fileNameConverter  (  String   fName  )  { 
File   fil  =  new   File  (  fName  )  ; 
int   index  =  fil  .  getName  (  )  .  lastIndexOf  (  '.'  )  ; 
String   fileName  =  ""  ; 
if  (  index  >  0  &&  index  <=  fil  .  getName  (  )  .  length  (  )  -  2  )  { 
fileName  =  fil  .  getName  (  )  .  substring  (  0  ,  index  )  +  ".txt"  ; 
} 
return   fileName  ; 
} 

public   void   peerSetup  (  )  { 
sourceFolderName  =  peerSourceTextField  .  getText  (  )  ; 
File   projectFolder  =  new   File  (  ProjectFolderTextField  .  getText  (  )  )  ; 
projectFolder  .  mkdir  (  )  ; 
FileOperator   fileOPerator  =  new   FileOperator  (  sourceFolderName  ,  projectFolder  .  getAbsolutePath  (  )  )  ; 
fileOPerator  .  anyToTextConverter  (  )  ; 
fileOPerator  .  TextFileIndexer  (  )  ; 
destFolderPath  =  fileOPerator  .  getDestinatonFolderPath  (  )  ; 
indexFolderPath  =  fileOPerator  .  getIndexFolderPath  (  )  ; 
} 

private   void   CheckControlButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
CheckControlButton  .  setEnabled  (  false  )  ; 
if  (  singleDetectionButton  .  isSelected  (  )  )  { 
this  .  setup  (  )  ; 
File   destFolder  =  new   File  (  destFolderPath  )  ; 
String   selectedDocumentPath  =  destFolderPath  +  File  .  separatorChar  +  fName  ; 
final   Worker   sworker  =  new   Worker  (  destFolderPath  ,  fName  ,  indexFolderPath  ,  selectedDocumentPath  ,  manager  ,  DownloadFromInternetProgressBar  ,  DownloadFromPlagiabustProgressBar  ,  CreateIndexProgressBar  ,  PreprocessDocumentProgressBar  ,  CrossCheckProgressBar  ,  Integer  .  parseInt  (  (  String  )  NumOfExternalSourcesComboBox  .  getSelectedItem  (  )  )  ,  Integer  .  parseInt  (  (  String  )  NumOfPeerSourceComboBox  .  getSelectedItem  (  )  )  ,  PlagiabustWebserverCheckBox  .  isSelected  (  )  ,  InternetSearchCheckBox  .  isSelected  (  )  ,  PharaphraseCheckBox  .  isSelected  (  )  )  { 

@  Override 
protected   void   done  (  )  { 
try  { 
repdata  =  get  (  )  ; 
}  catch  (  InterruptedException   ex  )  { 
Logger  .  getLogger  (  WizardForm  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ExecutionException   ex  )  { 
Logger  .  getLogger  (  WizardForm  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
setTempArray  (  repdata  )  ; 
CheckControlButton  .  setVisible  (  false  )  ; 
ViewButton  .  setVisible  (  true  )  ; 
} 
}  ; 
WizardPreviousButton  .  setEnabled  (  false  )  ; 
sworker  .  execute  (  )  ; 
} 
if  (  peerDetectionButton  .  isSelected  (  )  )  { 
this  .  peerSetup  (  )  ; 
File  [  ]  files  =  manager  .  getFilesIntheFolder  (  destFolderPath  )  ; 
for  (  int   arr  =  0  ;  arr  <  files  .  length  ;  arr  ++  )  { 
fileArrayList  .  add  (  files  [  arr  ]  .  getAbsolutePath  (  )  )  ; 
} 
final   PeerSearchWorker   psworker  =  new   PeerSearchWorker  (  destFolderPath  ,  files  ,  indexFolderPath  ,  fileArrayList  ,  manager  ,  DownloadFromInternetProgressBar  ,  DownloadFromPlagiabustProgressBar  ,  CreateIndexProgressBar  ,  PreprocessDocumentProgressBar  ,  CrossCheckProgressBar  ,  Integer  .  parseInt  (  (  String  )  NumOfExternalSourcesComboBox  .  getSelectedItem  (  )  )  ,  Integer  .  parseInt  (  (  String  )  NumOfPeerSourceComboBox  .  getSelectedItem  (  )  )  ,  PlagiabustWebserverCheckBox  .  isSelected  (  )  ,  InternetSearchCheckBox  .  isSelected  (  )  ,  PharaphraseCheckBox  .  isSelected  (  )  )  { 

@  Override 
protected   void   done  (  )  { 
try  { 
peerRepData  =  get  (  )  ; 
}  catch  (  InterruptedException   ex  )  { 
Logger  .  getLogger  (  WizardForm  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ExecutionException   ex  )  { 
Logger  .  getLogger  (  WizardForm  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
CheckControlButton  .  setVisible  (  false  )  ; 
ViewButton  .  setVisible  (  true  )  ; 
printTemp  (  peerRepData  )  ; 
} 
}  ; 
WizardPreviousButton  .  setEnabled  (  false  )  ; 
psworker  .  execute  (  )  ; 
} 
} 

public   void   printTemp  (  peerSearchReportData   temp  )  { 
Iterator   it  =  temp  .  getInternetFilesReportData  (  )  .  entrySet  (  )  .  iterator  (  )  ; 
while  (  it  .  hasNext  (  )  )  { 
Map  .  Entry   pair  =  (  Map  .  Entry  )  it  .  next  (  )  ; 
String   fileName  =  (  String  )  pair  .  getKey  (  )  ; 
HashMap  <  String  ,  String  >  map  =  (  HashMap  <  String  ,  String  >  )  pair  .  getValue  (  )  ; 
System  .  err  .  println  (  fileName  )  ; 
System  .  err  .  println  (  )  ; 
System  .  err  .  println  (  )  ; 
System  .  err  .  println  (  map  .  entrySet  (  )  )  ; 
System  .  err  .  println  (  )  ; 
System  .  err  .  println  (  )  ; 
} 
} 

private   void   ViewButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
File   projectFolder  =  new   File  (  ProjectFolderTextField  .  getText  (  )  )  ; 
ViewButton  .  setEnabled  (  false  )  ; 
if  (  singleDetectionButton  .  isSelected  (  )  )  { 
System  .  err  .  print  (  "start"  )  ; 
String   selectedDocumentPath  =  destFolderPath  +  File  .  separator  +  fileNameConverter  (  DocumentManagerForm  .  fileName  )  ; 
final   ReportWorker   repworker  =  new   ReportWorker  (  selectedDocumentPath  ,  repdata  ,  projectFolder  ,  DeleteProjectFilesCheckBox  .  isSelected  (  )  )  { 

@  Override 
protected   void   done  (  )  { 
try  { 
rp  =  get  (  )  ; 
}  catch  (  InterruptedException   ex  )  { 
Logger  .  getLogger  (  WizardForm  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ExecutionException   ex  )  { 
Logger  .  getLogger  (  WizardForm  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
close  (  )  ; 
rp  .  setVisible  (  true  )  ; 
} 
}  ; 
repworker  .  execute  (  )  ; 
} 
if  (  peerDetectionButton  .  isSelected  (  )  )  { 
PeerSearchUI   peerUI  =  new   PeerSearchUI  (  )  ; 
peerUI  .  setFolder  (  sourceFolderName  )  ; 
peerUI  .  setData  (  peerRepData  .  getPeerFilesReportData  (  )  ,  peerRepData  .  getInternetFilesReportData  (  )  ,  DeleteProjectFilesCheckBox  .  isSelected  (  )  ,  projectFolder  ,  peerRepData  .  getPeerUrlList  (  )  )  ; 
peerUI  .  processResults  (  )  ; 
peerUI  .  setResultDetails  (  )  ; 
peerUI  .  setVisible  (  true  )  ; 
this  .  setVisible  (  false  )  ; 
} 
} 

private   void   LocationBrowseButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
final   JFileChooser   fc  =  new   JFileChooser  (  Main  .  getDesktop  (  )  )  ; 
fc  .  setApproveButtonText  (  "Select"  )  ; 
fc  .  setFileSelectionMode  (  JFileChooser  .  DIRECTORIES_ONLY  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  this  )  ; 
if  (  returnVal  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   selectedFolder  =  fc  .  getSelectedFile  (  )  ; 
ProjectLocationTextField  .  setText  (  selectedFolder  .  getAbsolutePath  (  )  )  ; 
ProjectFolderTextField  .  setText  (  selectedFolder  .  getAbsolutePath  (  )  +  File  .  separatorChar  +  ProjectNameTextField  .  getText  (  )  )  ; 
fc  .  setVisible  (  false  )  ; 
} 
} 

private   void   singleDetectionButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
} 

private   void   LocationBrowseButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
final   JFileChooser   fc  =  new   JFileChooser  (  Main  .  getDesktop  (  )  )  ; 
fc  .  setApproveButtonText  (  "Select"  )  ; 
fc  .  setFileSelectionMode  (  JFileChooser  .  DIRECTORIES_ONLY  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  this  )  ; 
if  (  returnVal  ==  JFileChooser  .  APPROVE_OPTION  )  { 
File   selectedFolder  =  fc  .  getSelectedFile  (  )  ; 
peerSourceTextField  .  setText  (  selectedFolder  .  getAbsolutePath  (  )  )  ; 
fc  .  setVisible  (  false  )  ; 
} 
} 

private   void   ProjectNameTextFieldKeyReleased  (  java  .  awt  .  event  .  KeyEvent   evt  )  { 
if  (  !  ProjectLocationTextField  .  getText  (  )  .  equals  (  ""  )  )  { 
ProjectFolderTextField  .  setText  (  ProjectLocationTextField  .  getText  (  )  +  File  .  separatorChar  +  ProjectNameTextField  .  getText  (  )  )  ; 
} 
} 

private   void   peerSourceTextFieldActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
} 

private   void   PlagiabustWebserverCheckBoxItemStateChanged  (  java  .  awt  .  event  .  ItemEvent   evt  )  { 
if  (  evt  .  getStateChange  (  )  ==  ItemEvent  .  SELECTED  )  { 
if  (  this  .  checkConnectivity  (  true  )  )  { 
DownloadFromPlagiabustProgressBar  .  setVisible  (  true  )  ; 
PlagiabustSearchCompletionLabel  .  setVisible  (  true  )  ; 
}  else  { 
PlagiabustWebserverCheckBox  .  setSelected  (  false  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  "Plagiabust Web Server is unavailable"  ,  "Connection Failed"  ,  JOptionPane  .  ERROR_MESSAGE  ,  Main  .  getPlagiabustIcon  (  )  )  ; 
} 
}  else  { 
DownloadFromPlagiabustProgressBar  .  setVisible  (  false  )  ; 
PlagiabustSearchCompletionLabel  .  setVisible  (  false  )  ; 
} 
} 

private   void   InternetSearchCheckBoxItemStateChanged  (  java  .  awt  .  event  .  ItemEvent   evt  )  { 
if  (  evt  .  getStateChange  (  )  ==  ItemEvent  .  SELECTED  )  { 
if  (  this  .  checkConnectivity  (  false  )  )  { 
DownloadFromInternetProgressBar  .  setVisible  (  true  )  ; 
InternetSearchCompletionLabel  .  setVisible  (  true  )  ; 
}  else  { 
InternetSearchCheckBox  .  setSelected  (  false  )  ; 
JOptionPane  .  showMessageDialog  (  this  ,  "Internet connection is unavailable"  ,  "Connection Failed"  ,  JOptionPane  .  ERROR_MESSAGE  ,  Main  .  getPlagiabustIcon  (  )  )  ; 
} 
}  else  { 
DownloadFromInternetProgressBar  .  setVisible  (  false  )  ; 
InternetSearchCompletionLabel  .  setVisible  (  false  )  ; 
} 
} 

public   void   setTempArray  (  ReportData   temp  )  { 
this  .  repdata  =  temp  ; 
} 

public   void   printTemp  (  String  [  ]  [  ]  temp  )  { 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
for  (  int   j  =  0  ;  j  <  temp  [  i  ]  .  length  ;  j  ++  )  { 
if  (  temp  [  i  ]  [  j  ]  !=  null  )  { 
System  .  out  .  println  (  temp  [  i  ]  [  j  ]  )  ; 
} 
} 
} 
} 

public   void   close  (  )  { 
this  .  setVisible  (  false  )  ; 
} 

private   javax  .  swing  .  JLabel   AddDocumentBannerLabel  ; 

private   javax  .  swing  .  JLabel   AddDocumentBannerLabel1  ; 

private   javax  .  swing  .  JLabel   AddDocumentBannerLabel2  ; 

private   javax  .  swing  .  JLabel   AddDocumentBannerLabel3  ; 

private   javax  .  swing  .  JLabel   AddDocumentBannerLabel4  ; 

private   javax  .  swing  .  JLabel   AddDocumentBannerLabel5  ; 

private   javax  .  swing  .  JLabel   AddDocumentBannerLabel8  ; 

private   javax  .  swing  .  JLabel   AddDocumentBannerLabel9  ; 

private   javax  .  swing  .  JLabel   AddDocumentLabel  ; 

private   javax  .  swing  .  JLabel   AddDocumentLabel1  ; 

private   javax  .  swing  .  JPanel   AddDocumentPanel  ; 

private   javax  .  swing  .  JSeparator   AddDocumentSeparator  ; 

private   javax  .  swing  .  JSeparator   AddDocumentSeparator1  ; 

private   javax  .  swing  .  JSeparator   AddDocumentSeparator2  ; 

private   javax  .  swing  .  JLabel   CheckBannerLabel  ; 

private   javax  .  swing  .  JButton   CheckControlButton  ; 

private   javax  .  swing  .  JSeparator   CommandSeparator  ; 

private   javax  .  swing  .  JLabel   CommandsLabel  ; 

private   javax  .  swing  .  JProgressBar   CreateIndexProgressBar  ; 

private   javax  .  swing  .  JLabel   CrossCheckCompletionLabel  ; 

private   javax  .  swing  .  JProgressBar   CrossCheckProgressBar  ; 

private   javax  .  swing  .  JCheckBox   DeleteProjectFilesCheckBox  ; 

private   javax  .  swing  .  JPanel   DescriptionPanel  ; 

private   javax  .  swing  .  JScrollPane   DescriptionScrollPane  ; 

private   javax  .  swing  .  JTextArea   DescriptionTextArea  ; 

private   javax  .  swing  .  JButton   DocumentManagerButton  ; 

private   javax  .  swing  .  JProgressBar   DownloadFromInternetProgressBar  ; 

private   javax  .  swing  .  JProgressBar   DownloadFromPlagiabustProgressBar  ; 

private   javax  .  swing  .  JLabel   IndexFilesCompletionLabel  ; 

private   javax  .  swing  .  JCheckBox   InternetSearchCheckBox  ; 

private   javax  .  swing  .  JLabel   InternetSearchCompletionLabel  ; 

private   javax  .  swing  .  JLabel   KBManagerBannerLabel  ; 

private   javax  .  swing  .  JButton   KBManagerButton  ; 

private   javax  .  swing  .  JButton   KBManagerButton1  ; 

private   javax  .  swing  .  JLabel   KBManagerLabel  ; 

private   javax  .  swing  .  JSeparator   KBManagerSeparator  ; 

private   javax  .  swing  .  JSeparator   KeyWordSeparator  ; 

private   javax  .  swing  .  JLabel   KeywordLabel  ; 

private   javax  .  swing  .  JPanel   KnowledgeBasePanel  ; 

private   javax  .  swing  .  JButton   LocationBrowseButton  ; 

private   javax  .  swing  .  JButton   LocationBrowseButton1  ; 

private   javax  .  swing  .  JLabel   ModesOfCheckLabel  ; 

private   javax  .  swing  .  JSeparator   ModesOfCheckSeparator  ; 

private   javax  .  swing  .  JLabel   NameLocationBannerLabel  ; 

private   javax  .  swing  .  JSeparator   NameLocationBottomSeparator  ; 

private   javax  .  swing  .  JLabel   NameLocationLabel  ; 

private   javax  .  swing  .  JSeparator   NameLocationTopSeparator  ; 

private   javax  .  swing  .  JLabel   NumOfExternalLabel  ; 

private   javax  .  swing  .  JComboBox   NumOfExternalSourcesComboBox  ; 

private   javax  .  swing  .  JLabel   NumOfPeerDocLabel  ; 

private   javax  .  swing  .  JComboBox   NumOfPeerSourceComboBox  ; 

private   javax  .  swing  .  JCheckBox   PharaphraseCheckBox  ; 

private   javax  .  swing  .  JLabel   PlagiabustSearchCompletionLabel  ; 

private   javax  .  swing  .  JCheckBox   PlagiabustWebserverCheckBox  ; 

private   javax  .  swing  .  JLabel   PreprocessDocumentCompletionLabel  ; 

private   javax  .  swing  .  JProgressBar   PreprocessDocumentProgressBar  ; 

private   javax  .  swing  .  JLabel   ProjectFolderLabel  ; 

private   javax  .  swing  .  JLabel   ProjectFolderLabel1  ; 

private   javax  .  swing  .  JTextField   ProjectFolderTextField  ; 

private   javax  .  swing  .  JLabel   ProjectLocationLabel  ; 

private   javax  .  swing  .  JLabel   ProjectLocationLabel1  ; 

static   javax  .  swing  .  JLabel   ProjectLocationLabel2  ; 

private   javax  .  swing  .  JPanel   ProjectLocationPanel  ; 

private   javax  .  swing  .  JTextField   ProjectLocationTextField  ; 

private   javax  .  swing  .  JLabel   ProjectNameLabel  ; 

private   javax  .  swing  .  JTextField   ProjectNameTextField  ; 

private   javax  .  swing  .  JPanel   SelectDocumentsPanel  ; 

private   javax  .  swing  .  JLabel   SettingsBannerLabel  ; 

private   javax  .  swing  .  JLabel   SettingsLabel  ; 

private   javax  .  swing  .  JPanel   SettingsPanel  ; 

private   javax  .  swing  .  JSeparator   SettingsSeparator  ; 

private   javax  .  swing  .  JPanel   StartCheckPanel  ; 

private   javax  .  swing  .  JLabel   Step1ImageLabel  ; 

private   javax  .  swing  .  JLabel   Step2ImageLabel  ; 

private   javax  .  swing  .  JLabel   Step3ImageLabel  ; 

private   javax  .  swing  .  JLabel   Step4ImageLabel  ; 

private   javax  .  swing  .  JLabel   Step5ImageLabel  ; 

private   javax  .  swing  .  JLabel   TaskCompletionLabel  ; 

private   javax  .  swing  .  JSeparator   TaskCompletionSeparator  ; 

private   javax  .  swing  .  JButton   ViewButton  ; 

private   javax  .  swing  .  JLabel   WizardImageLabel  ; 

private   javax  .  swing  .  JPanel   WizardMainPanel  ; 

private   javax  .  swing  .  JButton   WizardNextButton  ; 

private   javax  .  swing  .  JButton   WizardPreviousButton  ; 

private   javax  .  swing  .  JSeparator   WizardSeparator  ; 

private   javax  .  swing  .  JTabbedPane   WizardTabbedPane  ; 

private   javax  .  swing  .  ButtonGroup   buttonGroup1  ; 

public   static   javax  .  swing  .  JLabel   crosscheckLabel  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JLabel   jLabel20  ; 

private   javax  .  swing  .  JPanel   jPanel1  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane1  ; 

private   javax  .  swing  .  JTextArea   jTextArea1  ; 

private   javax  .  swing  .  JRadioButton   peerDetectionButton  ; 

private   javax  .  swing  .  JTextField   peerSourceTextField  ; 

public   static   javax  .  swing  .  JLabel   preprocessLabel  ; 

static   javax  .  swing  .  JLabel   selectedDocumentLabel  ; 

private   javax  .  swing  .  JRadioButton   singleDetectionButton  ; 
} 
