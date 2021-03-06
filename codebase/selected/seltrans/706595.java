package   org  .  aacc  .  agenttoolbar  ; 

import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  awt  .  event  .  ActionListener  ; 
import   java  .  security  .  MessageDigest  ; 





public   class   LoginDialog   extends   javax  .  swing  .  JDialog   implements   ActionListener  { 


public   LoginDialog  (  ToolbarModel   model  ,  boolean   isModal  )  { 
super  (  new   javax  .  swing  .  JFrame  (  )  ,  isModal  )  ; 
initComponents  (  )  ; 
this  .  model  =  model  ; 
btnOK  .  setActionCommand  (  "OK"  )  ; 
btnOK  .  addActionListener  (  this  )  ; 
btnCancel  .  setActionCommand  (  "Cancel"  )  ; 
btnCancel  .  addActionListener  (  this  )  ; 
setDefaultCloseOperation  (  LoginDialog  .  DISPOSE_ON_CLOSE  )  ; 
setVisible  (  true  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
lblUser  =  new   javax  .  swing  .  JLabel  (  )  ; 
lblPassword  =  new   javax  .  swing  .  JLabel  (  )  ; 
lblExtension  =  new   javax  .  swing  .  JLabel  (  )  ; 
btnOK  =  new   javax  .  swing  .  JButton  (  )  ; 
btnCancel  =  new   javax  .  swing  .  JButton  (  )  ; 
txtUser  =  new   javax  .  swing  .  JTextField  (  )  ; 
txtExtension  =  new   javax  .  swing  .  JTextField  (  )  ; 
txtPassword  =  new   javax  .  swing  .  JPasswordField  (  )  ; 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel3  =  new   javax  .  swing  .  JLabel  (  )  ; 
setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  DISPOSE_ON_CLOSE  )  ; 
org  .  jdesktop  .  application  .  ResourceMap   resourceMap  =  org  .  jdesktop  .  application  .  Application  .  getInstance  (  org  .  aacc  .  agenttoolbar  .  MainApplication  .  class  )  .  getContext  (  )  .  getResourceMap  (  LoginDialog  .  class  )  ; 
setTitle  (  resourceMap  .  getString  (  "Form.title"  )  )  ; 
setAlwaysOnTop  (  true  )  ; 
setLocationByPlatform  (  true  )  ; 
setName  (  "Form"  )  ; 
setResizable  (  false  )  ; 
lblUser  .  setName  (  "lblUser"  )  ; 
lblPassword  .  setName  (  "lblPassword"  )  ; 
lblExtension  .  setName  (  "lblExtension"  )  ; 
btnOK  .  setText  (  resourceMap  .  getString  (  "btnOK.text"  )  )  ; 
btnOK  .  setName  (  "btnOK"  )  ; 
btnCancel  .  setText  (  resourceMap  .  getString  (  "btnCancel.text"  )  )  ; 
btnCancel  .  setName  (  "btnCancel"  )  ; 
txtUser  .  setName  (  "txtUser"  )  ; 
txtExtension  .  setName  (  "txtExtension"  )  ; 
txtPassword  .  setName  (  "txtPassword"  )  ; 
jLabel1  .  setText  (  resourceMap  .  getString  (  "jLabel1.text"  )  )  ; 
jLabel1  .  setName  (  "jLabel1"  )  ; 
jLabel2  .  setText  (  resourceMap  .  getString  (  "jLabel2.text"  )  )  ; 
jLabel2  .  setName  (  "jLabel2"  )  ; 
jLabel3  .  setText  (  resourceMap  .  getString  (  "jLabel3.text"  )  )  ; 
jLabel3  .  setName  (  "jLabel3"  )  ; 
javax  .  swing  .  GroupLayout   layout  =  new   javax  .  swing  .  GroupLayout  (  getContentPane  (  )  )  ; 
getContentPane  (  )  .  setLayout  (  layout  )  ; 
layout  .  setHorizontalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  jLabel3  )  .  addComponent  (  jLabel1  )  .  addComponent  (  jLabel2  )  )  .  addGap  (  69  ,  69  ,  69  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  txtExtension  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  177  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  txtPassword  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  177  ,  Short  .  MAX_VALUE  )  .  addComponent  (  txtUser  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  177  ,  Short  .  MAX_VALUE  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  lblUser  )  .  addComponent  (  lblExtension  )  .  addComponent  (  lblPassword  )  )  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  btnOK  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  70  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  btnCancel  )  )  )  .  addContainerGap  (  )  )  )  ; 
layout  .  linkSize  (  javax  .  swing  .  SwingConstants  .  HORIZONTAL  ,  new   java  .  awt  .  Component  [  ]  {  btnCancel  ,  btnOK  }  )  ; 
layout  .  setVerticalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  Short  .  MAX_VALUE  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  layout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel1  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  UNRELATED  )  .  addComponent  (  jLabel2  )  .  addGap  (  13  ,  13  ,  13  )  .  addComponent  (  jLabel3  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  layout  .  createSequentialGroup  (  )  .  addComponent  (  lblUser  )  .  addGap  (  57  ,  57  ,  57  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  layout  .  createSequentialGroup  (  )  .  addComponent  (  txtUser  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  20  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  txtPassword  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  txtExtension  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  20  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  )  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addComponent  (  lblPassword  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGap  (  18  ,  18  ,  18  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  btnOK  )  .  addComponent  (  btnCancel  )  )  )  )  .  addGap  (  27  ,  27  ,  27  )  .  addComponent  (  lblExtension  )  )  )  ; 
pack  (  )  ; 
} 








private   static   String   convertToHex  (  byte  [  ]  data  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
int   halfbyte  =  (  data  [  i  ]  >  >  >  4  )  &  0x0F  ; 
int   two_halfs  =  0  ; 
do  { 
if  (  (  0  <=  halfbyte  )  &&  (  halfbyte  <=  9  )  )  buf  .  append  (  (  char  )  (  '0'  +  halfbyte  )  )  ;  else   buf  .  append  (  (  char  )  (  'a'  +  (  halfbyte  -  10  )  )  )  ; 
halfbyte  =  data  [  i  ]  &  0x0F  ; 
}  while  (  two_halfs  ++  <  1  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 




private   String   getMD5  (  String   password  )  { 
try  { 
MessageDigest   md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
md  .  update  (  password  .  getBytes  (  )  )  ; 
byte  [  ]  data  =  md  .  digest  (  )  ; 
return   convertToHex  (  data  )  ; 
}  catch  (  Exception   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
return   null  ; 
} 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
String   command  =  evt  .  getActionCommand  (  )  ; 
if  (  command  .  equals  (  "OK"  )  )  { 
String   user  =  txtUser  .  getText  (  )  ; 
String   password  =  getMD5  (  txtPassword  .  getText  (  )  )  ; 
String   extension  =  txtExtension  .  getText  (  )  ; 
model  .  requestLogin  (  user  ,  password  ,  extension  )  ; 
dispose  (  )  ; 
}  else   if  (  command  .  equals  (  "Cancel"  )  )  { 
dispose  (  )  ; 
} 
} 

private   javax  .  swing  .  JButton   btnCancel  ; 

private   javax  .  swing  .  JButton   btnOK  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JLabel   jLabel3  ; 

private   javax  .  swing  .  JLabel   lblExtension  ; 

private   javax  .  swing  .  JLabel   lblPassword  ; 

private   javax  .  swing  .  JLabel   lblUser  ; 

private   javax  .  swing  .  JTextField   txtExtension  ; 

private   javax  .  swing  .  JPasswordField   txtPassword  ; 

private   javax  .  swing  .  JTextField   txtUser  ; 

private   ToolbarModel   model  ; 
} 

