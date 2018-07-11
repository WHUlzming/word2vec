package   org  .  unicef  .  doc  .  ibis  .  nut  ; 

import   java  .  math  .  *  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  swing  .  *  ; 
import   javax  .  xml  .  datatype  .  DatatypeConfigurationException  ; 
import   org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  *  ; 
import   org  .  unicef  .  doc  .  ibis  .  nut  .  exceptions  .  *  ; 
import   org  .  unicef  .  doc  .  ibis  .  nut  .  controllers  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   org  .  jdesktop  .  beansbinding  .  Binding  ; 





public   class   AudioPanel   extends   javax  .  swing  .  JPanel  { 

private   org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  ObjectFactory   objectFactory  ; 

private   Config   fConfig  ; 

private   HashMap  <  String  ,  org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  File  >  fMediaMap  =  new   HashMap  <  String  ,  org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  File  >  (  )  ; 


public   AudioPanel  (  )  { 
fConfig  =  Config  .  getInstance  (  )  ; 
if  (  fConfig  !=  null  )  { 
objectFactory  =  fConfig  .  getObjectFactory  (  )  ; 
} 
initComponents  (  )  ; 
bindingGroup  .  addBindingListener  (  new   StateChangeListener  (  )  )  ; 
fConfig  .  getDocumentManager  (  )  .  addPanel  (  this  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
java  .  awt  .  GridBagConstraints   gridBagConstraints  ; 
bindingGroup  =  new   org  .  jdesktop  .  beansbinding  .  BindingGroup  (  )  ; 
audio  =  initAudio  (  )  ; 
simpleTimecode1  =  initTimecode  (  )  ; 
fImageNames  =  new   java  .  util  .  Vector  <  String  >  (  )  ; 
fMedia  =  new   java  .  util  .  Vector  <  String  >  (  )  ; 
jPanel4  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel9  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField1  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel15  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField2  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel16  =  new   javax  .  swing  .  JLabel  (  )  ; 
jTextField3  =  new   javax  .  swing  .  JTextField  (  )  ; 
jTextField4  =  new   javax  .  swing  .  JTextField  (  )  ; 
jTextField8  =  new   javax  .  swing  .  JTextField  (  )  ; 
jTextField10  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel26  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel25  =  new   javax  .  swing  .  JLabel  (  )  ; 
jlScript  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel23  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel22  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel21  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel20  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel19  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel18  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel17  =  new   javax  .  swing  .  JLabel  (  )  ; 
jScrollPane5  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTextArea1  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jScrollPane6  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jTextArea2  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jComboBox1  =  new   javax  .  swing  .  JComboBox  (  )  ; 
jcbForPodcasting  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
jPanel18  =  new   javax  .  swing  .  JPanel  (  )  ; 
jLabel28  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel29  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel30  =  new   javax  .  swing  .  JLabel  (  )  ; 
jsHours  =  new   javax  .  swing  .  JSpinner  (  )  ; 
jsSeconds  =  new   javax  .  swing  .  JSpinner  (  )  ; 
jsMinutes  =  new   javax  .  swing  .  JSpinner  (  )  ; 
jsReleaseDate  =  new   javax  .  swing  .  JSpinner  (  )  ; 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
jsRecordDate  =  new   javax  .  swing  .  JSpinner  (  )  ; 
jScrollPane1  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jlMedia  =  new   javax  .  swing  .  JList  (  )  ; 
jPanel11  =  new   javax  .  swing  .  JPanel  (  )  ; 
jButton1  =  new   javax  .  swing  .  JButton  (  )  ; 
jButton10  =  new   javax  .  swing  .  JButton  (  )  ; 
jbViewFile  =  new   javax  .  swing  .  JButton  (  )  ; 
jbMediaSaveAs  =  new   javax  .  swing  .  JButton  (  )  ; 
ibMediaAddURL  =  new   javax  .  swing  .  JButton  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel3  =  new   javax  .  swing  .  JLabel  (  )  ; 
jcbForPRX  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
jLabel4  =  new   javax  .  swing  .  JLabel  (  )  ; 
jcbForTheNewsmarket  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
jLabel5  =  new   javax  .  swing  .  JLabel  (  )  ; 
jcbForUNICEF  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
jLabel6  =  new   javax  .  swing  .  JLabel  (  )  ; 
jcbForOneWorld  =  new   javax  .  swing  .  JCheckBox  (  )  ; 
jScrollPane2  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jtaScript  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jlRightboxText  =  new   javax  .  swing  .  JLabel  (  )  ; 
jspRightboxText  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
jtaRightboxText  =  new   javax  .  swing  .  JTextArea  (  )  ; 
jlRightboxTextInstructions  =  new   javax  .  swing  .  JLabel  (  )  ; 
if  (  audio  !=  null  )  { 
try  { 
AudioController   ac  =  new   AudioController  (  audio  )  ; 
ac  .  setRunningTime  (  simpleTimecode1  )  ; 
}  catch  (  Exception   ignore  )  { 
} 
} 
setAutoscrolls  (  true  )  ; 
setName  (  "Form"  )  ; 
setLayout  (  new   java  .  awt  .  FlowLayout  (  java  .  awt  .  FlowLayout  .  LEFT  )  )  ; 
jPanel4  .  setName  (  "jPanel4"  )  ; 
jPanel4  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
org  .  jdesktop  .  application  .  ResourceMap   resourceMap  =  org  .  jdesktop  .  application  .  Application  .  getInstance  (  org  .  unicef  .  doc  .  ibis  .  nut  .  Main  .  class  )  .  getContext  (  )  .  getResourceMap  (  AudioPanel  .  class  )  ; 
jLabel9  .  setText  (  resourceMap  .  getString  (  "jLabel9.text"  )  )  ; 
jLabel9  .  setName  (  "jLabel9"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel9  ,  gridBagConstraints  )  ; 
jTextField1  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  800  ,  25  )  )  ; 
jTextField1  .  setName  (  "jTextField1"  )  ; 
jTextField1  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  800  ,  25  )  )  ; 
org  .  jdesktop  .  beansbinding  .  Binding   binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${jobNumber}"  )  ,  jTextField1  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jTextField1  ,  gridBagConstraints  )  ; 
jLabel15  .  setText  (  resourceMap  .  getString  (  "jLabel15.text"  )  )  ; 
jLabel15  .  setName  (  "jLabel15"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel15  ,  gridBagConstraints  )  ; 
jTextField2  .  setName  (  "jTextField2"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${producer}"  )  ,  jTextField2  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jTextField2  ,  gridBagConstraints  )  ; 
jLabel16  .  setText  (  resourceMap  .  getString  (  "jLabel16.text"  )  )  ; 
jLabel16  .  setName  (  "jLabel16"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  2  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel16  ,  gridBagConstraints  )  ; 
jTextField3  .  setName  (  "jTextField3"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${editor}"  )  ,  jTextField3  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  2  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jTextField3  ,  gridBagConstraints  )  ; 
jTextField4  .  setName  (  "jTextField4"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${title}"  )  ,  jTextField4  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  3  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jTextField4  ,  gridBagConstraints  )  ; 
jTextField8  .  setName  (  "jTextField8"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${audioPackage}"  )  ,  jTextField8  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  10  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jTextField8  ,  gridBagConstraints  )  ; 
jTextField10  .  setName  (  "jTextField10"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${narrator}"  )  ,  jTextField10  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  12  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jTextField10  ,  gridBagConstraints  )  ; 
jLabel26  .  setText  (  resourceMap  .  getString  (  "jLabel26.text"  )  )  ; 
jLabel26  .  setName  (  "jLabel26"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  15  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel26  ,  gridBagConstraints  )  ; 
jLabel25  .  setText  (  resourceMap  .  getString  (  "jLabel25.text"  )  )  ; 
jLabel25  .  setName  (  "jLabel25"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  14  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel25  ,  gridBagConstraints  )  ; 
jlScript  .  setText  (  resourceMap  .  getString  (  "jlScript.text"  )  )  ; 
jlScript  .  setName  (  "jlScript"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  13  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jlScript  ,  gridBagConstraints  )  ; 
jLabel23  .  setText  (  resourceMap  .  getString  (  "jLabel23.text"  )  )  ; 
jLabel23  .  setName  (  "jLabel23"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  12  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel23  ,  gridBagConstraints  )  ; 
jLabel22  .  setText  (  resourceMap  .  getString  (  "jLabel22.text"  )  )  ; 
jLabel22  .  setName  (  "jLabel22"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  11  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel22  ,  gridBagConstraints  )  ; 
jLabel21  .  setText  (  resourceMap  .  getString  (  "jLabel21.text"  )  )  ; 
jLabel21  .  setName  (  "jLabel21"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  10  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel21  ,  gridBagConstraints  )  ; 
jLabel20  .  setText  (  resourceMap  .  getString  (  "jLabel20.text"  )  )  ; 
jLabel20  .  setName  (  "jLabel20"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  9  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel20  ,  gridBagConstraints  )  ; 
jLabel19  .  setText  (  resourceMap  .  getString  (  "jLabel19.text"  )  )  ; 
jLabel19  .  setName  (  "jLabel19"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  8  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel19  ,  gridBagConstraints  )  ; 
jLabel18  .  setText  (  resourceMap  .  getString  (  "jLabel18.text"  )  )  ; 
jLabel18  .  setName  (  "jLabel18"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  4  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel18  ,  gridBagConstraints  )  ; 
jLabel17  .  setText  (  resourceMap  .  getString  (  "jLabel17.text"  )  )  ; 
jLabel17  .  setName  (  "jLabel17"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  3  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel17  ,  gridBagConstraints  )  ; 
jScrollPane5  .  setHorizontalScrollBarPolicy  (  javax  .  swing  .  ScrollPaneConstants  .  HORIZONTAL_SCROLLBAR_NEVER  )  ; 
jScrollPane5  .  setVerticalScrollBarPolicy  (  javax  .  swing  .  ScrollPaneConstants  .  VERTICAL_SCROLLBAR_ALWAYS  )  ; 
jScrollPane5  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  23  ,  200  )  )  ; 
jScrollPane5  .  setName  (  "jScrollPane5"  )  ; 
jScrollPane5  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  183  ,  200  )  )  ; 
jTextArea1  .  setColumns  (  20  )  ; 
jTextArea1  .  setLineWrap  (  true  )  ; 
jTextArea1  .  setRows  (  5  )  ; 
jTextArea1  .  setWrapStyleWord  (  true  )  ; 
jTextArea1  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  4  ,  200  )  )  ; 
jTextArea1  .  setName  (  "jTextArea1"  )  ; 
jTextArea1  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  164  ,  150  )  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${blurb}"  )  ,  jTextArea1  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
jScrollPane5  .  setViewportView  (  jTextArea1  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  4  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jScrollPane5  ,  gridBagConstraints  )  ; 
jScrollPane6  .  setVerticalScrollBarPolicy  (  javax  .  swing  .  ScrollPaneConstants  .  VERTICAL_SCROLLBAR_ALWAYS  )  ; 
jScrollPane6  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  23  ,  200  )  )  ; 
jScrollPane6  .  setName  (  "jScrollPane6"  )  ; 
jScrollPane6  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  183  ,  200  )  )  ; 
jTextArea2  .  setColumns  (  20  )  ; 
jTextArea2  .  setLineWrap  (  true  )  ; 
jTextArea2  .  setRows  (  5  )  ; 
jTextArea2  .  setWrapStyleWord  (  true  )  ; 
jTextArea2  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  4  ,  200  )  )  ; 
jTextArea2  .  setName  (  "jTextArea2"  )  ; 
jTextArea2  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  164  ,  150  )  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${contact}"  )  ,  jTextArea2  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
jScrollPane6  .  setViewportView  (  jTextArea2  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  9  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jScrollPane6  ,  gridBagConstraints  )  ; 
jComboBox1  .  setModel  (  new   javax  .  swing  .  DefaultComboBoxModel  (  new   String  [  ]  {  "Radio Programme"  ,  "Radio Report"  }  )  )  ; 
jComboBox1  .  setName  (  "jComboBox1"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${format.format}"  )  ,  jComboBox1  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selectedItem"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  14  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jComboBox1  ,  gridBagConstraints  )  ; 
jcbForPodcasting  .  setName  (  "jcbForPodcasting"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forPodcasting}"  )  ,  jcbForPodcasting  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  15  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jcbForPodcasting  ,  gridBagConstraints  )  ; 
jPanel18  .  setName  (  "jPanel18"  )  ; 
jPanel18  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jLabel28  .  setText  (  resourceMap  .  getString  (  "jLabel28.text"  )  )  ; 
jLabel28  .  setName  (  "jLabel28"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanel18  .  add  (  jLabel28  ,  gridBagConstraints  )  ; 
jLabel29  .  setText  (  resourceMap  .  getString  (  "jLabel29.text"  )  )  ; 
jLabel29  .  setName  (  "jLabel29"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  2  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanel18  .  add  (  jLabel29  ,  gridBagConstraints  )  ; 
jLabel30  .  setText  (  resourceMap  .  getString  (  "jLabel30.text"  )  )  ; 
jLabel30  .  setName  (  "jLabel30"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  4  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanel18  .  add  (  jLabel30  ,  gridBagConstraints  )  ; 
jsHours  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  0  ,  0  ,  59  ,  1  )  )  ; 
jsHours  .  setName  (  "jsHours"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${runningTime.hours}"  )  ,  jsHours  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
binding  .  setSourceNullValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setSourceUnreadableValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setConverter  (  new   IntegerToBigIntegerConverter  (  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanel18  .  add  (  jsHours  ,  gridBagConstraints  )  ; 
jsSeconds  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  0  ,  0  ,  59  ,  1  )  )  ; 
jsSeconds  .  setName  (  "jsSeconds"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${runningTime.seconds}"  )  ,  jsSeconds  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
binding  .  setSourceNullValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setSourceUnreadableValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setConverter  (  new   IntegerToBigIntegerConverter  (  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  5  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanel18  .  add  (  jsSeconds  ,  gridBagConstraints  )  ; 
jsMinutes  .  setModel  (  new   javax  .  swing  .  SpinnerNumberModel  (  0  ,  0  ,  59  ,  1  )  )  ; 
jsMinutes  .  setName  (  "jsMinutes"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${runningTime.minutes}"  )  ,  jsMinutes  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
binding  .  setSourceNullValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setSourceUnreadableValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setConverter  (  new   IntegerToBigIntegerConverter  (  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  3  ; 
gridBagConstraints  .  gridy  =  0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  5  ,  5  ,  5  ,  5  )  ; 
jPanel18  .  add  (  jsMinutes  ,  gridBagConstraints  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  11  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  VERTICAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jPanel18  ,  gridBagConstraints  )  ; 
jsReleaseDate  .  setModel  (  new   javax  .  swing  .  SpinnerDateModel  (  )  )  ; 
jsReleaseDate  .  setEditor  (  new   javax  .  swing  .  JSpinner  .  DateEditor  (  jsReleaseDate  ,  "d/M/yy"  )  )  ; 
jsReleaseDate  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  75  ,  25  )  )  ; 
jsReleaseDate  .  setName  (  "jsReleaseDate"  )  ; 
jsReleaseDate  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  75  ,  25  )  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${releaseDateItem}"  )  ,  jsReleaseDate  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  8  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  VERTICAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jsReleaseDate  ,  gridBagConstraints  )  ; 
jLabel1  .  setText  (  resourceMap  .  getString  (  "jLabel1.text"  )  )  ; 
jLabel1  .  setName  (  "jLabel1"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  7  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel1  ,  gridBagConstraints  )  ; 
jsRecordDate  .  setModel  (  new   javax  .  swing  .  SpinnerDateModel  (  )  )  ; 
jsRecordDate  .  setEditor  (  new   javax  .  swing  .  JSpinner  .  DateEditor  (  jsRecordDate  ,  "d/M/yy"  )  )  ; 
jsRecordDate  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  75  ,  25  )  )  ; 
jsRecordDate  .  setName  (  "jsRecordDate"  )  ; 
jsRecordDate  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  75  ,  25  )  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${recordDateItem}"  )  ,  jsRecordDate  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  7  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  VERTICAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jsRecordDate  ,  gridBagConstraints  )  ; 
jScrollPane1  .  setName  (  "jScrollPane1"  )  ; 
jlMedia  .  setName  (  "jlMedia"  )  ; 
org  .  jdesktop  .  swingbinding  .  JListBinding   jListBinding  =  org  .  jdesktop  .  swingbinding  .  SwingBindings  .  createJListBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  fMedia  ,  jlMedia  )  ; 
bindingGroup  .  addBinding  (  jListBinding  )  ; 
jScrollPane1  .  setViewportView  (  jlMedia  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  22  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jScrollPane1  ,  gridBagConstraints  )  ; 
jPanel11  .  setName  (  "jPanel11"  )  ; 
jPanel11  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
jButton1  .  setText  (  resourceMap  .  getString  (  "jButton1.text"  )  )  ; 
jButton1  .  setName  (  "jButton1"  )  ; 
jButton1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  2  ,  2  ,  2  )  ; 
jPanel11  .  add  (  jButton1  ,  gridBagConstraints  )  ; 
jButton10  .  setText  (  resourceMap  .  getString  (  "jButton10.text"  )  )  ; 
jButton10  .  setName  (  "jButton10"  )  ; 
jButton10  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jButton10ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  2  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  2  ,  2  ,  2  )  ; 
jPanel11  .  add  (  jButton10  ,  gridBagConstraints  )  ; 
jbViewFile  .  setText  (  resourceMap  .  getString  (  "jbViewFile.text"  )  )  ; 
jbViewFile  .  setName  (  "jbViewFile"  )  ; 
jbViewFile  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jbViewFileActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  3  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  2  ,  2  ,  2  )  ; 
jPanel11  .  add  (  jbViewFile  ,  gridBagConstraints  )  ; 
jbMediaSaveAs  .  setText  (  resourceMap  .  getString  (  "jbMediaSaveAs.text"  )  )  ; 
jbMediaSaveAs  .  setName  (  "jbMediaSaveAs"  )  ; 
jbMediaSaveAs  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
jbMediaSaveAsActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  4  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  2  ,  2  ,  2  )  ; 
jPanel11  .  add  (  jbMediaSaveAs  ,  gridBagConstraints  )  ; 
ibMediaAddURL  .  setText  (  resourceMap  .  getString  (  "ibMediaAddURL.text"  )  )  ; 
ibMediaAddURL  .  setName  (  "ibMediaAddURL"  )  ; 
ibMediaAddURL  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
ibMediaAddURLActionPerformed  (  evt  )  ; 
} 
}  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  1  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  2  ,  2  ,  2  )  ; 
jPanel11  .  add  (  ibMediaAddURL  ,  gridBagConstraints  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  2  ; 
gridBagConstraints  .  gridy  =  22  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jPanel11  ,  gridBagConstraints  )  ; 
jLabel2  .  setText  (  resourceMap  .  getString  (  "jLabel2.text"  )  )  ; 
jLabel2  .  setName  (  "jLabel2"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  22  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel2  ,  gridBagConstraints  )  ; 
jLabel3  .  setText  (  resourceMap  .  getString  (  "jLabel3.text"  )  )  ; 
jLabel3  .  setName  (  "jLabel3"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  19  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel3  ,  gridBagConstraints  )  ; 
jcbForPRX  .  setText  (  resourceMap  .  getString  (  "jcbForPRX.text"  )  )  ; 
jcbForPRX  .  setName  (  "jcbForPRX"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forPRX}"  )  ,  jcbForPRX  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  19  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jcbForPRX  ,  gridBagConstraints  )  ; 
jLabel4  .  setText  (  resourceMap  .  getString  (  "jLabel4.text"  )  )  ; 
jLabel4  .  setName  (  "jLabel4"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  16  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel4  ,  gridBagConstraints  )  ; 
jcbForTheNewsmarket  .  setText  (  resourceMap  .  getString  (  "jcbForTheNewsmarket.text"  )  )  ; 
jcbForTheNewsmarket  .  setName  (  "jcbForTheNewsmarket"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forTheNewsmarket}"  )  ,  jcbForTheNewsmarket  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  16  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jcbForTheNewsmarket  ,  gridBagConstraints  )  ; 
jLabel5  .  setText  (  resourceMap  .  getString  (  "jLabel5.text"  )  )  ; 
jLabel5  .  setName  (  "jLabel5"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  17  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel5  ,  gridBagConstraints  )  ; 
jcbForUNICEF  .  setText  (  resourceMap  .  getString  (  "jcbForUNICEF.text"  )  )  ; 
jcbForUNICEF  .  setName  (  "jcbForUNICEF"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forUNICEF}"  )  ,  jcbForUNICEF  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  17  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jcbForUNICEF  ,  gridBagConstraints  )  ; 
jLabel6  .  setText  (  resourceMap  .  getString  (  "jLabel6.text"  )  )  ; 
jLabel6  .  setName  (  "jLabel6"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  18  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jLabel6  ,  gridBagConstraints  )  ; 
jcbForOneWorld  .  setText  (  resourceMap  .  getString  (  "jcbForOneWorld.text"  )  )  ; 
jcbForOneWorld  .  setName  (  "jcbForOneWorld"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forOneWorld}"  )  ,  jcbForOneWorld  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  18  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jcbForOneWorld  ,  gridBagConstraints  )  ; 
jScrollPane2  .  setHorizontalScrollBarPolicy  (  javax  .  swing  .  ScrollPaneConstants  .  HORIZONTAL_SCROLLBAR_NEVER  )  ; 
jScrollPane2  .  setVerticalScrollBarPolicy  (  javax  .  swing  .  ScrollPaneConstants  .  VERTICAL_SCROLLBAR_ALWAYS  )  ; 
jScrollPane2  .  setName  (  "jScrollPane2"  )  ; 
jScrollPane2  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  183  ,  200  )  )  ; 
jtaScript  .  setColumns  (  20  )  ; 
jtaScript  .  setLineWrap  (  true  )  ; 
jtaScript  .  setRows  (  5  )  ; 
jtaScript  .  setWrapStyleWord  (  true  )  ; 
jtaScript  .  setName  (  "jtaScript"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${script}"  )  ,  jtaScript  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
jScrollPane2  .  setViewportView  (  jtaScript  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  13  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jScrollPane2  ,  gridBagConstraints  )  ; 
jlRightboxText  .  setText  (  resourceMap  .  getString  (  "jlRightboxText.text"  )  )  ; 
jlRightboxText  .  setName  (  "jlRightboxText"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  0  ; 
gridBagConstraints  .  gridy  =  6  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  0  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jlRightboxText  ,  gridBagConstraints  )  ; 
jspRightboxText  .  setHorizontalScrollBarPolicy  (  javax  .  swing  .  ScrollPaneConstants  .  HORIZONTAL_SCROLLBAR_NEVER  )  ; 
jspRightboxText  .  setVerticalScrollBarPolicy  (  javax  .  swing  .  ScrollPaneConstants  .  VERTICAL_SCROLLBAR_ALWAYS  )  ; 
jspRightboxText  .  setName  (  "jspRightboxText"  )  ; 
jtaRightboxText  .  setColumns  (  20  )  ; 
jtaRightboxText  .  setLineWrap  (  true  )  ; 
jtaRightboxText  .  setRows  (  5  )  ; 
jtaRightboxText  .  setWrapStyleWord  (  true  )  ; 
jtaRightboxText  .  setName  (  "jtaRightboxText"  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${rightboxText}"  )  ,  jtaRightboxText  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
jspRightboxText  .  setViewportView  (  jtaRightboxText  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  6  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  NORTHWEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jspRightboxText  ,  gridBagConstraints  )  ; 
jlRightboxTextInstructions  .  setFont  (  resourceMap  .  getFont  (  "jlRightboxTextInstructions.font"  )  )  ; 
jlRightboxTextInstructions  .  setText  (  resourceMap  .  getString  (  "jlRightboxTextInstructions.text"  )  )  ; 
jlRightboxTextInstructions  .  setName  (  "jlRightboxTextInstructions"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridx  =  1  ; 
gridBagConstraints  .  gridy  =  5  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  2  ,  5  ,  2  ,  5  )  ; 
jPanel4  .  add  (  jlRightboxTextInstructions  ,  gridBagConstraints  )  ; 
add  (  jPanel4  )  ; 
bindingGroup  .  bind  (  )  ; 
} 

private   void   jButton1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
javax  .  swing  .  JFileChooser   jc  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
java  .  io  .  File   f  =  null  ; 
AudioController   audc  =  null  ; 
javax  .  swing  .  filechooser  .  FileFilter   filter  =  null  ; 
if  (  fConfig  ==  null  )  { 
fConfig  =  Config  .  getInstance  (  )  ; 
} 
jc  .  setCurrentDirectory  (  new   java  .  io  .  File  (  fConfig  .  getWorkingDirectory  (  )  )  )  ; 
filter  =  new   javax  .  swing  .  filechooser  .  FileNameExtensionFilter  (  "RealMedia File"  ,  "ram"  )  ; 
jc  .  addChoosableFileFilter  (  filter  )  ; 
filter  =  new   javax  .  swing  .  filechooser  .  FileNameExtensionFilter  (  "MPEG Audio"  ,  "mp3"  )  ; 
jc  .  addChoosableFileFilter  (  filter  )  ; 
filter  =  new   javax  .  swing  .  filechooser  .  FileNameExtensionFilter  (  "All supported audio formats (.ram, .mp3)"  ,  "ram"  ,  "mp3"  )  ; 
jc  .  addChoosableFileFilter  (  filter  )  ; 
jc  .  showOpenDialog  (  getTopLevelAncestor  (  )  )  ; 
try  { 
audc  =  new   AudioController  (  audio  )  ; 
}  catch  (  DatatypeConfigurationException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  CreationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ModificationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  NUTException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
f  =  jc  .  getSelectedFile  (  )  ; 
if  (  f  ==  null  )  { 
return  ; 
} 
fConfig  .  setWorkingDirectory  (  f  .  getPath  (  )  )  ; 
try  { 
audc  .  addFile  (  f  )  ; 
}  catch  (  CreationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ModificationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  NUTException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  DatatypeConfigurationException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
for  (  org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  File   buf  :  audio  .  getMedia  (  )  )  { 
fMediaMap  .  put  (  buf  .  getName  (  )  ,  buf  )  ; 
} 
jlMedia  .  setListData  (  fMediaMap  .  keySet  (  )  .  toArray  (  new   String  [  0  ]  )  )  ; 
updateSharedFields  (  )  ; 
jc  =  null  ; 
f  =  null  ; 
audc  =  null  ; 
filter  =  null  ; 
} 

private   void   jButton10ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
AudioController   audc  =  null  ; 
String   s  =  null  ; 
try  { 
audc  =  new   AudioController  (  audio  )  ; 
s  =  (  String  )  jlMedia  .  getSelectedValue  (  )  ; 
audc  .  removeFile  (  fMediaMap  .  get  (  s  )  )  ; 
jlMedia  .  setListData  (  fMediaMap  .  keySet  (  )  .  toArray  (  )  )  ; 
updateSharedFields  (  )  ; 
}  catch  (  CreationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ModificationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  NUTException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  DatatypeConfigurationException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
audc  =  null  ; 
s  =  null  ; 
} 

private   void   jbViewFileActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
String   key  =  null  ; 
org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  File   ifile  =  null  ; 
java  .  io  .  File   ofile  =  null  ; 
java  .  io  .  FileOutputStream   fout  =  null  ; 
java  .  awt  .  Desktop   theDesktop  =  null  ; 
if  (  jlMedia  .  getSelectedIndex  (  )  <  0  )  { 
return  ; 
} 
if  (  !  java  .  awt  .  Desktop  .  isDesktopSupported  (  )  )  { 
return  ; 
} 
try  { 
key  =  (  String  )  jlMedia  .  getSelectedValue  (  )  ; 
theDesktop  =  java  .  awt  .  Desktop  .  getDesktop  (  )  ; 
ifile  =  fMediaMap  .  get  (  key  )  ; 
if  (  ifile  !=  null  &&  ifile  .  isSetURI  (  )  &&  !  ifile  .  isSetContent  (  )  )  { 
theDesktop  .  browse  (  new   java  .  net  .  URI  (  ifile  .  getURI  (  )  )  )  ; 
return  ; 
} 
ofile  =  new   java  .  io  .  File  (  System  .  getProperty  (  "java.io.tmpdir"  )  +  System  .  getProperty  (  "file.separator"  )  +  java  .  util  .  UUID  .  randomUUID  (  )  .  toString  (  )  +  key  )  ; 
ofile  .  deleteOnExit  (  )  ; 
fout  =  new   java  .  io  .  FileOutputStream  (  ofile  )  ; 
fout  .  write  (  ifile  .  getContent  (  )  )  ; 
fout  .  flush  (  )  ; 
fout  .  close  (  )  ; 
theDesktop  .  browse  (  ofile  .  toURI  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ioe  )  ; 
}  catch  (  RuntimeException   rte  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  rte  )  ; 
}  catch  (  Exception   e  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  e  )  ; 
} 
key  =  null  ; 
ifile  =  null  ; 
ofile  =  null  ; 
fout  =  null  ; 
theDesktop  =  null  ; 
} 

private   void   jbMediaSaveAsActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
String   key  =  null  ; 
org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  File   ifile  =  null  ; 
java  .  io  .  File   ofile  =  null  ; 
java  .  io  .  FileOutputStream   fout  =  null  ; 
javax  .  swing  .  JFileChooser   jc  =  null  ; 
if  (  jlMedia  .  getSelectedIndex  (  )  <  0  )  { 
return  ; 
} 
try  { 
key  =  (  String  )  jlMedia  .  getSelectedValue  (  )  ; 
ifile  =  fMediaMap  .  get  (  key  )  ; 
if  (  ifile  !=  null  &&  ifile  .  isSetURI  (  )  &&  !  ifile  .  isSetContent  (  )  )  { 
return  ; 
} 
jc  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
jc  .  setCurrentDirectory  (  new   java  .  io  .  File  (  fConfig  .  getWorkingDirectory  (  )  )  )  ; 
ofile  =  new   java  .  io  .  File  (  jc  .  getCurrentDirectory  (  )  .  getAbsolutePath  (  )  +  System  .  getProperty  (  "file.separator"  )  +  key  )  ; 
jc  .  setSelectedFile  (  ofile  )  ; 
jc  .  showSaveDialog  (  this  )  ; 
ofile  =  jc  .  getSelectedFile  (  )  ; 
if  (  ofile  !=  null  )  { 
fout  =  new   java  .  io  .  FileOutputStream  (  ofile  )  ; 
fout  .  write  (  ifile  .  getContent  (  )  )  ; 
fout  .  flush  (  )  ; 
fout  .  close  (  )  ; 
if  (  ofile  .  isDirectory  (  )  )  { 
fConfig  .  setWorkingDirectory  (  ofile  .  getAbsolutePath  (  )  )  ; 
}  else  { 
if  (  ofile  .  getParentFile  (  )  !=  null  )  { 
fConfig  .  setWorkingDirectory  (  ofile  .  getParentFile  (  )  .  getAbsolutePath  (  )  )  ; 
} 
} 
} 
}  catch  (  IOException   ioe  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ioe  )  ; 
}  catch  (  RuntimeException   rte  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  rte  )  ; 
}  catch  (  Exception   e  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  e  )  ; 
} 
key  =  null  ; 
ifile  =  null  ; 
ofile  =  null  ; 
fout  =  null  ; 
jc  =  null  ; 
} 

private   void   ibMediaAddURLActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
AudioController   audc  =  null  ; 
java  .  net  .  URI   uri  =  null  ; 
String   s  =  (  String  )  JOptionPane  .  showInputDialog  (  jlMedia  ,  ""  ,  "Enter URL"  ,  JOptionPane  .  PLAIN_MESSAGE  ,  null  ,  null  ,  null  )  ; 
try  { 
uri  =  new   java  .  net  .  URI  (  s  )  ; 
}  catch  (  java  .  net  .  URISyntaxException   urie  )  { 
Logger  .  getLogger  (  VideoPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  urie  )  ; 
} 
try  { 
audc  =  new   AudioController  (  audio  )  ; 
audc  .  addFileURL  (  uri  )  ; 
}  catch  (  CreationTimeException   ex  )  { 
Logger  .  getLogger  (  VideoPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ModificationTimeException   ex  )  { 
Logger  .  getLogger  (  VideoPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  NUTException   ex  )  { 
Logger  .  getLogger  (  VideoPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  VideoPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  DatatypeConfigurationException   ex  )  { 
Logger  .  getLogger  (  VideoPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
for  (  org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  File   buf  :  audio  .  getMedia  (  )  )  { 
fMediaMap  .  put  (  buf  .  getName  (  )  ,  buf  )  ; 
} 
jlMedia  .  setListData  (  fMediaMap  .  keySet  (  )  .  toArray  (  new   String  [  0  ]  )  )  ; 
updateSharedFields  (  )  ; 
audc  =  null  ; 
uri  =  null  ; 
s  =  null  ; 
} 

private   org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  Audio   audio  ; 

private   java  .  util  .  Vector  <  String  >  fImageNames  ; 

private   java  .  util  .  Vector  <  String  >  fMedia  ; 

private   javax  .  swing  .  JButton   ibMediaAddURL  ; 

private   javax  .  swing  .  JButton   jButton1  ; 

private   javax  .  swing  .  JButton   jButton10  ; 

private   javax  .  swing  .  JComboBox   jComboBox1  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JLabel   jLabel15  ; 

private   javax  .  swing  .  JLabel   jLabel16  ; 

private   javax  .  swing  .  JLabel   jLabel17  ; 

private   javax  .  swing  .  JLabel   jLabel18  ; 

private   javax  .  swing  .  JLabel   jLabel19  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JLabel   jLabel20  ; 

private   javax  .  swing  .  JLabel   jLabel21  ; 

private   javax  .  swing  .  JLabel   jLabel22  ; 

private   javax  .  swing  .  JLabel   jLabel23  ; 

private   javax  .  swing  .  JLabel   jLabel25  ; 

private   javax  .  swing  .  JLabel   jLabel26  ; 

private   javax  .  swing  .  JLabel   jLabel28  ; 

private   javax  .  swing  .  JLabel   jLabel29  ; 

private   javax  .  swing  .  JLabel   jLabel3  ; 

private   javax  .  swing  .  JLabel   jLabel30  ; 

private   javax  .  swing  .  JLabel   jLabel4  ; 

private   javax  .  swing  .  JLabel   jLabel5  ; 

private   javax  .  swing  .  JLabel   jLabel6  ; 

private   javax  .  swing  .  JLabel   jLabel9  ; 

private   javax  .  swing  .  JPanel   jPanel11  ; 

private   javax  .  swing  .  JPanel   jPanel18  ; 

private   javax  .  swing  .  JPanel   jPanel4  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane1  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane2  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane5  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane6  ; 

private   javax  .  swing  .  JTextArea   jTextArea1  ; 

private   javax  .  swing  .  JTextArea   jTextArea2  ; 

private   javax  .  swing  .  JTextField   jTextField1  ; 

private   javax  .  swing  .  JTextField   jTextField10  ; 

private   javax  .  swing  .  JTextField   jTextField2  ; 

private   javax  .  swing  .  JTextField   jTextField3  ; 

private   javax  .  swing  .  JTextField   jTextField4  ; 

private   javax  .  swing  .  JTextField   jTextField8  ; 

private   javax  .  swing  .  JButton   jbMediaSaveAs  ; 

private   javax  .  swing  .  JButton   jbViewFile  ; 

private   javax  .  swing  .  JCheckBox   jcbForOneWorld  ; 

private   javax  .  swing  .  JCheckBox   jcbForPRX  ; 

private   javax  .  swing  .  JCheckBox   jcbForPodcasting  ; 

private   javax  .  swing  .  JCheckBox   jcbForTheNewsmarket  ; 

private   javax  .  swing  .  JCheckBox   jcbForUNICEF  ; 

private   javax  .  swing  .  JList   jlMedia  ; 

private   javax  .  swing  .  JLabel   jlRightboxText  ; 

private   javax  .  swing  .  JLabel   jlRightboxTextInstructions  ; 

private   javax  .  swing  .  JLabel   jlScript  ; 

private   javax  .  swing  .  JSpinner   jsHours  ; 

private   javax  .  swing  .  JSpinner   jsMinutes  ; 

private   javax  .  swing  .  JSpinner   jsRecordDate  ; 

private   javax  .  swing  .  JSpinner   jsReleaseDate  ; 

private   javax  .  swing  .  JSpinner   jsSeconds  ; 

private   javax  .  swing  .  JScrollPane   jspRightboxText  ; 

private   javax  .  swing  .  JTextArea   jtaRightboxText  ; 

private   javax  .  swing  .  JTextArea   jtaScript  ; 

private   org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  SimpleTimecode   simpleTimecode1  ; 

private   org  .  jdesktop  .  beansbinding  .  BindingGroup   bindingGroup  ; 

private   Audio   initAudio  (  )  { 
Audio   v  =  null  ; 
try  { 
if  (  objectFactory  ==  null  )  { 
objectFactory  =  new   org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  ObjectFactory  (  )  ; 
} 
if  (  objectFactory  !=  null  )  { 
v  =  objectFactory  .  createAudio  (  )  ; 
} 
new   AudioController  (  v  )  ; 
}  catch  (  Throwable   ignore  )  { 
} 
return  (  v  )  ; 
} 

private   SimpleTimecode   initTimecode  (  )  { 
SimpleTimecode   tc  =  null  ; 
try  { 
if  (  objectFactory  ==  null  )  { 
objectFactory  =  new   org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  ObjectFactory  (  )  ; 
} 
if  (  objectFactory  !=  null  )  { 
tc  =  objectFactory  .  createSimpleTimecode  (  )  ; 
} 
new   SimpleTimecodeController  (  tc  )  ; 
}  catch  (  Throwable   ignore  )  { 
} 
return  (  tc  )  ; 
} 

public   static   void   main  (  String   args  [  ]  )  { 
JFrame   jf  =  new   JFrame  (  )  ; 
AudioPanel   p  =  new   AudioPanel  (  )  ; 
p  .  setVisible  (  true  )  ; 
jf  .  setDefaultCloseOperation  (  JFrame  .  EXIT_ON_CLOSE  )  ; 
jf  .  getContentPane  (  )  .  add  (  p  )  ; 
jf  .  pack  (  )  ; 
jf  .  setVisible  (  true  )  ; 
} 




public   org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  Audio   getAudio  (  )  { 
persistBindings  (  )  ; 
try  { 
new   AudioController  (  audio  )  ; 
}  catch  (  CreationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ModificationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  NUTException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  DatatypeConfigurationException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
return   audio  ; 
} 




public   void   setAudio  (  org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  Audio   pAudio  )  { 
audio  =  pAudio  ; 
try  { 
new   AudioController  (  pAudio  )  ; 
}  catch  (  CreationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  ModificationTimeException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  NUTException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  IOException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
}  catch  (  DatatypeConfigurationException   ex  )  { 
Logger  .  getLogger  (  AudioPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
if  (  fMediaMap  !=  null  )  { 
fMediaMap  .  clear  (  )  ; 
}  else  { 
fMediaMap  =  new   HashMap  <  String  ,  org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  File  >  (  )  ; 
} 
for  (  org  .  unicef  .  doc  .  ibis  .  nut  .  persistence  .  File   f  :  audio  .  getMedia  (  )  )  { 
fMediaMap  .  put  (  f  .  getName  (  )  ,  f  )  ; 
} 
if  (  fMedia  !=  null  )  { 
fMedia  .  clear  (  )  ; 
}  else  { 
fMedia  =  new   Vector  <  String  >  (  )  ; 
} 
fMedia  .  addAll  (  fMediaMap  .  keySet  (  )  )  ; 
rebind  (  )  ; 
updateSharedFields  (  )  ; 
javax  .  swing  .  SwingUtilities  .  updateComponentTreeUI  (  this  )  ; 
} 

private   void   rebind  (  )  { 
org  .  jdesktop  .  beansbinding  .  Binding   binding  =  null  ; 
if  (  bindingGroup  ==  null  )  { 
bindingGroup  =  new   org  .  jdesktop  .  beansbinding  .  BindingGroup  (  )  ; 
bindingGroup  .  addBindingListener  (  new   StateChangeListener  (  )  )  ; 
}  else  { 
bindingGroup  .  unbind  (  )  ; 
} 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${rightboxText}"  )  ,  jtaRightboxText  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${jobNumber}"  )  ,  jTextField1  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${producer}"  )  ,  jTextField2  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${editor}"  )  ,  jTextField3  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${title}"  )  ,  jTextField4  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${audioPackage}"  )  ,  jTextField8  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${narrator}"  )  ,  jTextField10  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${blurb}"  )  ,  jTextArea1  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${contact}"  )  ,  jTextArea2  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${format.format}"  )  ,  jComboBox1  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selectedItem"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forPodcasting}"  )  ,  jcbForPodcasting  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${runningTime.hours}"  )  ,  jsHours  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
binding  .  setSourceNullValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setSourceUnreadableValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setConverter  (  new   IntegerToBigIntegerConverter  (  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${runningTime.seconds}"  )  ,  jsSeconds  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
binding  .  setSourceNullValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setSourceUnreadableValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setConverter  (  new   IntegerToBigIntegerConverter  (  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${runningTime.minutes}"  )  ,  jsMinutes  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
binding  .  setSourceNullValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setSourceUnreadableValue  (  BigInteger  .  ZERO  )  ; 
binding  .  setConverter  (  new   IntegerToBigIntegerConverter  (  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${releaseDateItem}"  )  ,  jsReleaseDate  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${recordDateItem}"  )  ,  jsRecordDate  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "value"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
org  .  jdesktop  .  swingbinding  .  JListBinding   jListBinding  =  org  .  jdesktop  .  swingbinding  .  SwingBindings  .  createJListBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  fMedia  ,  jlMedia  )  ; 
bindingGroup  .  addBinding  (  jListBinding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forPRX}"  )  ,  jcbForPRX  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forUNICEF}"  )  ,  jcbForUNICEF  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forTheNewsmarket}"  )  ,  jcbForTheNewsmarket  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${forOneWorld}"  )  ,  jcbForOneWorld  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "selected"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
binding  =  org  .  jdesktop  .  beansbinding  .  Bindings  .  createAutoBinding  (  org  .  jdesktop  .  beansbinding  .  AutoBinding  .  UpdateStrategy  .  READ_WRITE  ,  audio  ,  org  .  jdesktop  .  beansbinding  .  ELProperty  .  create  (  "${script}"  )  ,  jtaScript  ,  org  .  jdesktop  .  beansbinding  .  BeanProperty  .  create  (  "text"  )  )  ; 
bindingGroup  .  addBinding  (  binding  )  ; 
bindingGroup  .  bind  (  )  ; 
binding  =  null  ; 
} 

public   void   persistBindings  (  )  { 
for  (  Binding   b  :  bindingGroup  .  getBindings  (  )  )  { 
Binding  .  SyncFailure   failure  =  null  ; 
if  (  !  b  .  isManaged  (  )  )  { 
failure  =  b  .  saveAndNotify  (  )  ; 
if  (  failure  !=  null  )  { 
Logger  .  getLogger  (  VideoPanel  .  class  .  getName  (  )  )  .  log  (  Level  .  SEVERE  ,  null  ,  failure  .  toString  (  )  )  ; 
} 
} 
} 
} 

private   void   updateSharedFields  (  )  { 
if  (  fConfig  .  getDocumentManager  (  )  !=  null  )  { 
Iterator  <  JPanel  >  pnlenum  =  fConfig  .  getDocumentManager  (  )  .  getPanels  (  )  ; 
while  (  pnlenum  .  hasNext  (  )  )  { 
JPanel   pnl  =  pnlenum  .  next  (  )  ; 
if  (  pnl   instanceof   HomepageElementsPanel  )  { 
(  (  HomepageElementsPanel  )  pnl  )  .  setAudio  (  fMediaMap  .  keySet  (  )  .  iterator  (  )  )  ; 
javax  .  swing  .  SwingUtilities  .  updateComponentTreeUI  (  pnl  )  ; 
} 
} 
} 
} 

@  Override 
public   boolean   equals  (  Object   o  )  { 
if  (  this  ==  o  )  { 
return  (  true  )  ; 
}  else  { 
return  (  false  )  ; 
} 
} 

@  Override 
public   int   hashCode  (  )  { 
int   hash  =  7  ; 
hash  =  79  *  hash  +  (  this  .  audio  !=  null  ?  this  .  audio  .  hashCode  (  )  :  0  )  ; 
return   hash  ; 
} 
} 

