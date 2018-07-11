package   newgen  .  presentation  .  administration  ; 

import   java  .  util  .  *  ; 
import   org  .  jdom  .  *  ; 
import   org  .  jdom  .  output  .  *  ; 
import   org  .  jdom  .  input  .  *  ; 





public   class   NewSeriesStatementAFInternalFrame   extends   javax  .  swing  .  JInternalFrame  { 


public   NewSeriesStatementAFInternalFrame  (  )  { 
initComponents  (  )  ; 
javax  .  help  .  HelpBroker   helpbroker  =  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getHelpbroker  (  )  ; 
javax  .  help  .  HelpSet   helpset  =  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getHelpset  (  )  ; 
helpbroker  .  enableHelp  (  this  ,  "Authorityfileshelp"  ,  helpset  )  ; 
java  .  awt  .  event  .  ActionListener   bhelpal  =  new   javax  .  help  .  CSH  .  DisplayHelpFromSource  (  helpbroker  )  ; 
this  .  bhelp  .  addActionListener  (  bhelpal  )  ; 
this  .  seriesstatementpanel  =  new   newgen  .  presentation  .  administration  .  SeriesStatementAF  (  )  ; 
this  .  addPanel  .  add  (  this  .  seriesstatementpanel  )  ; 
newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  applyOrientation  (  this  )  ; 
} 

public   static   NewSeriesStatementAFInternalFrame   getInstance  (  )  { 
if  (  thisScreen  ==  null  )  { 
thisScreen  =  new   NewSeriesStatementAFInternalFrame  (  )  ; 
thisScreen  .  setSize  (  620  ,  450  )  ; 
thisScreen  .  show  (  )  ; 
}  else  { 
thisScreen  .  setSize  (  620  ,  450  )  ; 
thisScreen  .  show  (  )  ; 
} 
return   thisScreen  ; 
} 






private   void   initComponents  (  )  { 
jPanel1  =  new   javax  .  swing  .  JPanel  (  )  ; 
bok  =  new   javax  .  swing  .  JButton  (  )  ; 
bhelp  =  new   javax  .  swing  .  JButton  (  )  ; 
bHelpCsh  =  new   javax  .  swing  .  JButton  (  )  ; 
bcancel  =  new   javax  .  swing  .  JButton  (  )  ; 
bexit  =  new   javax  .  swing  .  JButton  (  )  ; 
addPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
setClosable  (  true  )  ; 
setIconifiable  (  true  )  ; 
setMaximizable  (  true  )  ; 
setResizable  (  true  )  ; 
setTitle  (  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getMyResource  (  )  .  getString  (  "NewSeriesStatement"  )  )  ; 
jPanel1  .  setBorder  (  new   javax  .  swing  .  border  .  EtchedBorder  (  )  )  ; 
bok  .  setMnemonic  (  'o'  )  ; 
bok  .  setText  (  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getMyResource  (  )  .  getString  (  "Ok"  )  )  ; 
bok  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
bokActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel1  .  add  (  bok  )  ; 
bhelp  .  setIcon  (  new   javax  .  swing  .  ImageIcon  (  getClass  (  )  .  getResource  (  "/newgen/images/help.gif"  )  )  )  ; 
bhelp  .  setMnemonic  (  'h'  )  ; 
jPanel1  .  add  (  bhelp  )  ; 
bHelpCsh  .  setIcon  (  new   javax  .  swing  .  ImageIcon  (  getClass  (  )  .  getResource  (  "/newgen/images/helpcsh.gif"  )  )  )  ; 
jPanel1  .  add  (  bHelpCsh  )  ; 
bcancel  .  setMnemonic  (  'c'  )  ; 
bcancel  .  setText  (  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getMyResource  (  )  .  getString  (  "Cancel"  )  )  ; 
jPanel1  .  add  (  bcancel  )  ; 
bexit  .  setMnemonic  (  'e'  )  ; 
bexit  .  setText  (  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getMyResource  (  )  .  getString  (  "Close"  )  )  ; 
bexit  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
bexitActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel1  .  add  (  bexit  )  ; 
getContentPane  (  )  .  add  (  jPanel1  ,  java  .  awt  .  BorderLayout  .  SOUTH  )  ; 
addPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
getContentPane  (  )  .  add  (  addPanel  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
pack  (  )  ; 
} 

private   void   bexitActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
this  .  dispose  (  )  ; 
} 

private   void   bokActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  this  .  seriesstatementpanel  .  getEnteredvalues  (  )  .  get  (  0  )  .  toString  (  )  .  trim  (  )  .  equals  (  ""  )  )  { 
this  .  showWarningMessage  (  "Enter Series Title"  )  ; 
}  else  { 
String  [  ]  patlib  =  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getPatronLibraryIds  (  )  ; 
String   xmlreq  =  newgen  .  presentation  .  administration  .  AdministrationXMLGenerator  .  getInstance  (  )  .  saveSeriesName  (  "2"  ,  seriesstatementpanel  .  getEnteredvalues  (  )  ,  patlib  )  ; 
try  { 
java  .  net  .  URL   url  =  new   java  .  net  .  URL  (  ResourceBundle  .  getBundle  (  "Administration"  )  .  getString  (  "ServerURL"  )  +  ResourceBundle  .  getBundle  (  "Administration"  )  .  getString  (  "ServletSubPath"  )  +  "SeriesNameServlet"  )  ; 
java  .  net  .  URLConnection   urlconn  =  (  java  .  net  .  URLConnection  )  url  .  openConnection  (  )  ; 
urlconn  .  setDoOutput  (  true  )  ; 
java  .  io  .  OutputStream   dos  =  urlconn  .  getOutputStream  (  )  ; 
dos  .  write  (  xmlreq  .  getBytes  (  )  )  ; 
java  .  io  .  InputStream   ios  =  urlconn  .  getInputStream  (  )  ; 
SAXBuilder   saxb  =  new   SAXBuilder  (  )  ; 
Document   retdoc  =  saxb  .  build  (  ios  )  ; 
Element   rootelement  =  retdoc  .  getRootElement  (  )  ; 
if  (  rootelement  .  getChild  (  "Error"  )  ==  null  )  { 
this  .  showInformationMessage  (  ResourceBundle  .  getBundle  (  "Administration"  )  .  getString  (  "DataSavedInDatabase"  )  )  ; 
}  else  { 
this  .  showErrorMessage  (  ResourceBundle  .  getBundle  (  "Administration"  )  .  getString  (  "ErrorPleaseContactTheVendor"  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  )  ; 
} 
} 
} 

void   showErrorMessage  (  String   message  )  { 
newgen  .  presentation  .  NewGenMain   app  =  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  ; 
app  .  showErrorMessage  (  message  )  ; 
} 

void   showInformationMessage  (  String   message  )  { 
newgen  .  presentation  .  NewGenMain   app  =  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  ; 
app  .  showInformationMessage  (  message  )  ; 
} 

void   showWarningMessage  (  String   message  )  { 
newgen  .  presentation  .  NewGenMain   app  =  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  ; 
app  .  showWarningMessage  (  message  )  ; 
} 

void   showQuestionMessage  (  String   message  )  { 
newgen  .  presentation  .  NewGenMain   app  =  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  ; 
app  .  showQuestionMessage  (  message  )  ; 
} 

private   javax  .  swing  .  JPanel   addPanel  ; 

private   javax  .  swing  .  JButton   bHelpCsh  ; 

private   javax  .  swing  .  JButton   bcancel  ; 

private   javax  .  swing  .  JButton   bexit  ; 

private   javax  .  swing  .  JButton   bhelp  ; 

private   javax  .  swing  .  JButton   bok  ; 

private   javax  .  swing  .  JPanel   jPanel1  ; 

private   static   NewSeriesStatementAFInternalFrame   thisScreen  ; 

private   newgen  .  presentation  .  administration  .  SeriesStatementAF   seriesstatementpanel  ; 
} 

