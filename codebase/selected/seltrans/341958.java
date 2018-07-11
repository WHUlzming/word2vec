package   newgen  .  presentation  .  administration  ; 

import   java  .  util  .  *  ; 
import   java  .  util  .  *  ; 
import   org  .  jdom  .  *  ; 
import   org  .  jdom  .  output  .  *  ; 
import   org  .  jdom  .  input  .  *  ; 





public   class   NewChronologicalSubDivisionAF   extends   javax  .  swing  .  JInternalFrame  { 


private   NewChronologicalSubDivisionAF  (  )  { 
initComponents  (  )  ; 
newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  applyOrientation  (  this  )  ; 
} 

public   static   NewChronologicalSubDivisionAF   getInstance  (  )  { 
if  (  thisScreen  ==  null  )  { 
thisScreen  =  new   NewChronologicalSubDivisionAF  (  )  ; 
thisScreen  .  setSize  (  536  ,  140  )  ; 
thisScreen  .  show  (  )  ; 
}  else  { 
thisScreen  .  setSize  (  536  ,  140  )  ; 
thisScreen  .  show  (  )  ; 
} 
return   thisScreen  ; 
} 






private   void   initComponents  (  )  { 
jPanel1  =  new   javax  .  swing  .  JPanel  (  )  ; 
jPanel2  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
tfChronologicalSubDivision  =  new   newgen  .  presentation  .  UnicodeTextField  (  )  ; 
jPanel3  =  new   javax  .  swing  .  JPanel  (  )  ; 
bok  =  new   javax  .  swing  .  JButton  (  )  ; 
bhelp  =  new   javax  .  swing  .  JButton  (  )  ; 
bcancel  =  new   javax  .  swing  .  JButton  (  )  ; 
bexit  =  new   javax  .  swing  .  JButton  (  )  ; 
setClosable  (  true  )  ; 
setIconifiable  (  true  )  ; 
setMaximizable  (  true  )  ; 
setResizable  (  true  )  ; 
setTitle  (  java  .  util  .  ResourceBundle  .  getBundle  (  "Administration"  )  .  getString  (  "NewChronologicalSubDivision"  )  )  ; 
jPanel1  .  setLayout  (  new   javax  .  swing  .  BoxLayout  (  jPanel1  ,  javax  .  swing  .  BoxLayout  .  Y_AXIS  )  )  ; 
jPanel2  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jPanel2  .  setBorder  (  new   javax  .  swing  .  border  .  EtchedBorder  (  )  )  ; 
jLabel1  .  setText  (  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getMyResource  (  )  .  getString  (  "ChronologicalSubDivision"  )  )  ; 
jPanel2  .  add  (  jLabel1  ,  new   java  .  awt  .  GridBagConstraints  (  )  )  ; 
tfChronologicalSubDivision  .  setColumns  (  35  )  ; 
jPanel2  .  add  (  tfChronologicalSubDivision  ,  new   java  .  awt  .  GridBagConstraints  (  )  )  ; 
jPanel1  .  add  (  jPanel2  )  ; 
bok  .  setMnemonic  (  'o'  )  ; 
bok  .  setText  (  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getMyResource  (  )  .  getString  (  "Ok"  )  )  ; 
bok  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
bokActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jPanel3  .  add  (  bok  )  ; 
bhelp  .  setIcon  (  new   javax  .  swing  .  ImageIcon  (  getClass  (  )  .  getResource  (  "/newgen/images/help.gif"  )  )  )  ; 
bhelp  .  setMnemonic  (  'h'  )  ; 
jPanel3  .  add  (  bhelp  )  ; 
bcancel  .  setMnemonic  (  'c'  )  ; 
bcancel  .  setText  (  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getMyResource  (  )  .  getString  (  "Cancel"  )  )  ; 
jPanel3  .  add  (  bcancel  )  ; 
bexit  .  setMnemonic  (  'e'  )  ; 
bexit  .  setText  (  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getMyResource  (  )  .  getString  (  "Close"  )  )  ; 
jPanel3  .  add  (  bexit  )  ; 
jPanel1  .  add  (  jPanel3  )  ; 
getContentPane  (  )  .  add  (  jPanel1  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
pack  (  )  ; 
} 

private   void   bokActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  this  .  tfChronologicalSubDivision  .  getText  (  )  .  trim  (  )  .  equals  (  ""  )  )  { 
this  .  showWarningMessage  (  "Enter chronological sub division"  )  ; 
}  else  { 
String  [  ]  patlib  =  newgen  .  presentation  .  NewGenMain  .  getAppletInstance  (  )  .  getPatronLibraryIds  (  )  ; 
String   xmlreq  =  newgen  .  presentation  .  administration  .  AdministrationXMLGenerator  .  getInstance  (  )  .  saveChronologicalSubDivision  (  "3"  ,  this  .  tfChronologicalSubDivision  .  getText  (  )  ,  patlib  )  ; 
System  .  out  .  println  (  xmlreq  )  ; 
try  { 
java  .  net  .  URL   url  =  new   java  .  net  .  URL  (  ResourceBundle  .  getBundle  (  "Administration"  )  .  getString  (  "ServerURL"  )  +  ResourceBundle  .  getBundle  (  "Administration"  )  .  getString  (  "ServletSubPath"  )  +  "SubDivisionServlet"  )  ; 
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

private   javax  .  swing  .  JButton   bcancel  ; 

private   javax  .  swing  .  JButton   bexit  ; 

private   javax  .  swing  .  JButton   bhelp  ; 

private   javax  .  swing  .  JButton   bok  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JPanel   jPanel1  ; 

private   javax  .  swing  .  JPanel   jPanel2  ; 

private   javax  .  swing  .  JPanel   jPanel3  ; 

private   newgen  .  presentation  .  UnicodeTextField   tfChronologicalSubDivision  ; 

private   static   NewChronologicalSubDivisionAF   thisScreen  ; 
} 

