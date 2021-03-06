package   ch  .  unifr  .  nio  .  framework  .  examples  ; 

import   ch  .  unifr  .  nio  .  framework  .  ChannelHandler  ; 
import   ch  .  unifr  .  nio  .  framework  .  Dispatcher  ; 
import   ch  .  unifr  .  nio  .  framework  .  HandlerAdapter  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  AbstractForwarder  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  ByteBufferArraySequenceForwarder  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  ByteBufferToArrayTransformer  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  ByteBufferToStringTransformer  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  ChannelReader  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  ChannelWriter  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  DummyTrafficOutputForwarder  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  StringToByteBufferTransformer  ; 
import   ch  .  unifr  .  nio  .  framework  .  transform  .  TrafficShaperCoordinator  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  InetSocketAddress  ; 
import   java  .  nio  .  channels  .  SocketChannel  ; 
import   java  .  util  .  ResourceBundle  ; 
import   java  .  util  .  concurrent  .  Executors  ; 
import   java  .  util  .  logging  .  ConsoleHandler  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   java  .  util  .  prefs  .  Preferences  ; 
import   javax  .  swing  .  JFrame  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  SwingUtilities  ; 
import   javax  .  swing  .  text  .  BadLocationException  ; 
import   javax  .  swing  .  text  .  Document  ; 





public   class   DummyTrafficGUIClient   extends   JFrame   implements   ChannelHandler  { 

private   static   final   Logger   LOGGER  =  Logger  .  getLogger  (  DummyTrafficGUIClient  .  class  .  getName  (  )  )  ; 

private   static   final   String   MESSAGE_SIZE_PREF_KEY  =  "MESSAGE_SIZE"  ; 

private   static   final   String   DELAY_PREF_KEY  =  "DELAY"  ; 

private   final   ChannelReader   reader  ; 

private   final   ChannelWriter   writer  ; 

private   final   TrafficShaperCoordinator   trafficShaperCoordinator  ; 

private   final   StringToByteBufferTransformer   stringToByteBufferTransformer  ; 

private   final   ResourceBundle   STRINGS  ; 

private   final   Preferences   preferences  ; 






public   DummyTrafficGUIClient  (  String   host  ,  int   port  )  throws   IOException  { 
STRINGS  =  ResourceBundle  .  getBundle  (  "ch/unifr/nio/framework/examples/Bundle"  )  ; 
initComponents  (  )  ; 
preferences  =  Preferences  .  userNodeForPackage  (  DummyTrafficGUIClient  .  class  )  ; 
messageSizeTextField  .  setText  (  preferences  .  get  (  MESSAGE_SIZE_PREF_KEY  ,  null  )  )  ; 
delayTextField  .  setText  (  preferences  .  get  (  DELAY_PREF_KEY  ,  null  )  )  ; 
Logger   nioLogger  =  Logger  .  getLogger  (  "ch.unifr.nio.framework"  )  ; 
nioLogger  .  setLevel  (  Level  .  FINEST  )  ; 
ConsoleHandler   consoleHandler  =  new   ConsoleHandler  (  )  ; 
consoleHandler  .  setLevel  (  Level  .  FINEST  )  ; 
nioLogger  .  addHandler  (  consoleHandler  )  ; 
reader  =  new   ChannelReader  (  false  ,  1024  ,  10240  )  ; 
ByteBufferToStringTransformer   byteBufferToStringTransformer  =  new   ByteBufferToStringTransformer  (  )  ; 
reader  .  setNextForwarder  (  byteBufferToStringTransformer  )  ; 
byteBufferToStringTransformer  .  setNextForwarder  (  new   DummyTrafficClientTransformer  (  echoTextArea  .  getDocument  (  )  )  )  ; 
writer  =  new   ChannelWriter  (  false  )  ; 
ByteBufferArraySequenceForwarder   byteBufferArraySequenceForwarder  =  new   ByteBufferArraySequenceForwarder  (  )  ; 
byteBufferArraySequenceForwarder  .  setNextForwarder  (  writer  )  ; 
DummyTrafficOutputForwarder   dummyTrafficOutputTransformer  =  new   DummyTrafficOutputForwarder  (  2  )  ; 
trafficShaperCoordinator  =  new   TrafficShaperCoordinator  (  Executors  .  newSingleThreadScheduledExecutor  (  )  ,  1  ,  1000  ,  false  )  ; 
trafficShaperCoordinator  .  addTrafficShaper  (  this  ,  dummyTrafficOutputTransformer  )  ; 
dummyTrafficOutputTransformer  .  setNextForwarder  (  byteBufferArraySequenceForwarder  )  ; 
ByteBufferToArrayTransformer   byteBufferToArrayTransformer  =  new   ByteBufferToArrayTransformer  (  )  ; 
byteBufferToArrayTransformer  .  setNextForwarder  (  dummyTrafficOutputTransformer  )  ; 
stringToByteBufferTransformer  =  new   StringToByteBufferTransformer  (  )  ; 
stringToByteBufferTransformer  .  setNextForwarder  (  byteBufferToArrayTransformer  )  ; 
InetSocketAddress   socketAddress  =  new   InetSocketAddress  (  host  ,  port  )  ; 
SocketChannel   channel  =  SocketChannel  .  open  (  socketAddress  )  ; 
channel  .  configureBlocking  (  false  )  ; 
Dispatcher   dispatcher  =  new   Dispatcher  (  )  ; 
dispatcher  .  start  (  )  ; 
dispatcher  .  registerChannel  (  channel  ,  this  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
private   void   initComponents  (  )  { 
jLabel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
jScrollPane1  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
inputTextArea  =  new   javax  .  swing  .  JTextArea  (  )  ; 
sendButton  =  new   javax  .  swing  .  JButton  (  )  ; 
jLabel2  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel3  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel4  =  new   javax  .  swing  .  JLabel  (  )  ; 
messageSizeTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
delayTextField  =  new   javax  .  swing  .  JTextField  (  )  ; 
jLabel5  =  new   javax  .  swing  .  JLabel  (  )  ; 
jLabel6  =  new   javax  .  swing  .  JLabel  (  )  ; 
jSeparator1  =  new   javax  .  swing  .  JSeparator  (  )  ; 
dummyTrafficToggleButton  =  new   javax  .  swing  .  JToggleButton  (  )  ; 
jSeparator2  =  new   javax  .  swing  .  JSeparator  (  )  ; 
jLabel7  =  new   javax  .  swing  .  JLabel  (  )  ; 
jScrollPane2  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
echoTextArea  =  new   javax  .  swing  .  JTextArea  (  )  ; 
setDefaultCloseOperation  (  javax  .  swing  .  WindowConstants  .  EXIT_ON_CLOSE  )  ; 
java  .  util  .  ResourceBundle   bundle  =  java  .  util  .  ResourceBundle  .  getBundle  (  "ch/unifr/nio/framework/examples/Bundle"  )  ; 
setTitle  (  bundle  .  getString  (  "DummyTrafficGUIClient.title"  )  )  ; 
addWindowListener  (  new   java  .  awt  .  event  .  WindowAdapter  (  )  { 

public   void   windowClosing  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
formWindowClosing  (  evt  )  ; 
} 
}  )  ; 
jLabel1  .  setFont  (  jLabel1  .  getFont  (  )  .  deriveFont  (  jLabel1  .  getFont  (  )  .  getStyle  (  )  |  java  .  awt  .  Font  .  BOLD  )  )  ; 
jLabel1  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.jLabel1.text"  )  )  ; 
inputTextArea  .  setColumns  (  20  )  ; 
inputTextArea  .  setRows  (  5  )  ; 
jScrollPane1  .  setViewportView  (  inputTextArea  )  ; 
sendButton  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.sendButton.text"  )  )  ; 
sendButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
sendButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
jLabel2  .  setFont  (  jLabel2  .  getFont  (  )  .  deriveFont  (  jLabel2  .  getFont  (  )  .  getStyle  (  )  |  java  .  awt  .  Font  .  BOLD  )  )  ; 
jLabel2  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.jLabel2.text"  )  )  ; 
jLabel3  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.jLabel3.text"  )  )  ; 
jLabel4  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.jLabel4.text"  )  )  ; 
messageSizeTextField  .  setColumns  (  7  )  ; 
messageSizeTextField  .  setHorizontalAlignment  (  javax  .  swing  .  JTextField  .  RIGHT  )  ; 
messageSizeTextField  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.messageSizeTextField.text"  )  )  ; 
delayTextField  .  setColumns  (  7  )  ; 
delayTextField  .  setHorizontalAlignment  (  javax  .  swing  .  JTextField  .  RIGHT  )  ; 
delayTextField  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.delayTextField.text"  )  )  ; 
jLabel5  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.jLabel5.text"  )  )  ; 
jLabel6  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.jLabel6.text"  )  )  ; 
dummyTrafficToggleButton  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.dummyTrafficToggleButton.text"  )  )  ; 
dummyTrafficToggleButton  .  addItemListener  (  new   java  .  awt  .  event  .  ItemListener  (  )  { 

public   void   itemStateChanged  (  java  .  awt  .  event  .  ItemEvent   evt  )  { 
dummyTrafficToggleButtonItemStateChanged  (  evt  )  ; 
} 
}  )  ; 
jLabel7  .  setFont  (  jLabel7  .  getFont  (  )  .  deriveFont  (  jLabel7  .  getFont  (  )  .  getStyle  (  )  |  java  .  awt  .  Font  .  BOLD  )  )  ; 
jLabel7  .  setText  (  bundle  .  getString  (  "DummyTrafficGUIClient.jLabel7.text"  )  )  ; 
echoTextArea  .  setColumns  (  20  )  ; 
echoTextArea  .  setEditable  (  false  )  ; 
echoTextArea  .  setRows  (  5  )  ; 
jScrollPane2  .  setViewportView  (  echoTextArea  )  ; 
javax  .  swing  .  GroupLayout   layout  =  new   javax  .  swing  .  GroupLayout  (  getContentPane  (  )  )  ; 
getContentPane  (  )  .  setLayout  (  layout  )  ; 
layout  .  setHorizontalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addContainerGap  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel1  )  .  addGap  (  374  ,  374  ,  374  )  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  sendButton  )  .  addGap  (  374  ,  374  ,  374  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  layout  .  createSequentialGroup  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  jSeparator1  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  450  ,  Short  .  MAX_VALUE  )  .  addComponent  (  jScrollPane1  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  450  ,  Short  .  MAX_VALUE  )  )  .  addContainerGap  (  )  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel4  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  delayTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel6  )  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel3  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  messageSizeTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel5  )  )  .  addComponent  (  jLabel2  )  .  addComponent  (  dummyTrafficToggleButton  )  )  .  addContainerGap  (  )  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addComponent  (  jLabel7  )  .  addContainerGap  (  435  ,  Short  .  MAX_VALUE  )  )  .  addGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  ,  layout  .  createSequentialGroup  (  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  TRAILING  )  .  addComponent  (  jScrollPane2  ,  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  450  ,  Short  .  MAX_VALUE  )  .  addComponent  (  jSeparator2  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  450  ,  Short  .  MAX_VALUE  )  )  .  addContainerGap  (  )  )  )  )  )  ; 
layout  .  linkSize  (  javax  .  swing  .  SwingConstants  .  HORIZONTAL  ,  new   java  .  awt  .  Component  [  ]  {  jLabel3  ,  jLabel4  }  )  ; 
layout  .  setVerticalGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  LEADING  )  .  addGroup  (  layout  .  createSequentialGroup  (  )  .  addGap  (  17  ,  17  ,  17  )  .  addComponent  (  jLabel1  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jScrollPane1  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  84  ,  Short  .  MAX_VALUE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  sendButton  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jSeparator1  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  10  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel2  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel3  )  .  addComponent  (  messageSizeTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel5  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addGroup  (  layout  .  createParallelGroup  (  javax  .  swing  .  GroupLayout  .  Alignment  .  BASELINE  )  .  addComponent  (  jLabel4  )  .  addComponent  (  delayTextField  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  javax  .  swing  .  GroupLayout  .  DEFAULT_SIZE  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addComponent  (  jLabel6  )  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  dummyTrafficToggleButton  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jSeparator2  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  ,  9  ,  javax  .  swing  .  GroupLayout  .  PREFERRED_SIZE  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jLabel7  )  .  addPreferredGap  (  javax  .  swing  .  LayoutStyle  .  ComponentPlacement  .  RELATED  )  .  addComponent  (  jScrollPane2  )  .  addGap  (  21  ,  21  ,  21  )  )  )  ; 
pack  (  )  ; 
} 

private   void   sendButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
String   text  =  inputTextArea  .  getText  (  )  ; 
try  { 
LOGGER  .  log  (  Level  .  FINEST  ,  "Sending \"{0}\""  ,  text  )  ; 
stringToByteBufferTransformer  .  forward  (  text  )  ; 
inputTextArea  .  setText  (  null  )  ; 
}  catch  (  IOException   ex  )  { 
LOGGER  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
} 

private   void   dummyTrafficToggleButtonItemStateChanged  (  java  .  awt  .  event  .  ItemEvent   evt  )  { 
if  (  dummyTrafficToggleButton  .  isSelected  (  )  )  { 
String   messageSizeString  =  messageSizeTextField  .  getText  (  )  ; 
int   messageSize  ; 
try  { 
messageSize  =  Integer  .  parseInt  (  messageSizeString  )  ; 
}  catch  (  Exception   e  )  { 
LOGGER  .  log  (  Level  .  SEVERE  ,  null  ,  e  )  ; 
showError  (  e  .  toString  (  )  )  ; 
return  ; 
} 
String   delayString  =  delayTextField  .  getText  (  )  ; 
int   delay  ; 
try  { 
delay  =  Integer  .  parseInt  (  delayString  )  ; 
}  catch  (  Exception   e  )  { 
LOGGER  .  log  (  Level  .  SEVERE  ,  null  ,  e  )  ; 
showError  (  e  .  toString  (  )  )  ; 
return  ; 
} 
trafficShaperCoordinator  .  setPackageSize  (  messageSize  )  ; 
trafficShaperCoordinator  .  setDelay  (  delay  )  ; 
trafficShaperCoordinator  .  start  (  )  ; 
}  else  { 
trafficShaperCoordinator  .  stop  (  )  ; 
} 
} 

private   void   formWindowClosing  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
preferences  .  put  (  MESSAGE_SIZE_PREF_KEY  ,  messageSizeTextField  .  getText  (  )  )  ; 
preferences  .  put  (  DELAY_PREF_KEY  ,  delayTextField  .  getText  (  )  )  ; 
} 




public   static   void   main  (  final   String   args  [  ]  )  { 
if  (  args  .  length  !=  2  )  { 
System  .  out  .  println  (  "Usage: DummyTrafficGUIClient <server host> <server port>"  )  ; 
System  .  exit  (  1  )  ; 
} 
java  .  awt  .  EventQueue  .  invokeLater  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
try  { 
DummyTrafficGUIClient   client  =  new   DummyTrafficGUIClient  (  args  [  0  ]  ,  Integer  .  parseInt  (  args  [  1  ]  )  )  ; 
client  .  setVisible  (  true  )  ; 
}  catch  (  IOException   ex  )  { 
LOGGER  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
System  .  exit  (  -  1  )  ; 
} 
} 
}  )  ; 
} 

private   javax  .  swing  .  JTextField   delayTextField  ; 

private   javax  .  swing  .  JToggleButton   dummyTrafficToggleButton  ; 

private   javax  .  swing  .  JTextArea   echoTextArea  ; 

private   javax  .  swing  .  JTextArea   inputTextArea  ; 

private   javax  .  swing  .  JLabel   jLabel1  ; 

private   javax  .  swing  .  JLabel   jLabel2  ; 

private   javax  .  swing  .  JLabel   jLabel3  ; 

private   javax  .  swing  .  JLabel   jLabel4  ; 

private   javax  .  swing  .  JLabel   jLabel5  ; 

private   javax  .  swing  .  JLabel   jLabel6  ; 

private   javax  .  swing  .  JLabel   jLabel7  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane1  ; 

private   javax  .  swing  .  JScrollPane   jScrollPane2  ; 

private   javax  .  swing  .  JSeparator   jSeparator1  ; 

private   javax  .  swing  .  JSeparator   jSeparator2  ; 

private   javax  .  swing  .  JTextField   messageSizeTextField  ; 

private   javax  .  swing  .  JButton   sendButton  ; 

@  Override 
public   void   channelRegistered  (  HandlerAdapter   handlerAdapter  )  { 
} 

@  Override 
public   ChannelReader   getChannelReader  (  )  { 
return   reader  ; 
} 

@  Override 
public   ChannelWriter   getChannelWriter  (  )  { 
return   writer  ; 
} 

@  Override 
public   void   inputClosed  (  )  { 
System  .  out  .  println  (  "inputClosed()"  )  ; 
} 

@  Override 
public   void   channelException  (  Exception   exception  )  { 
LOGGER  .  log  (  Level  .  SEVERE  ,  null  ,  exception  )  ; 
} 

private   void   showError  (  String   errorMessage  )  { 
JOptionPane  .  showMessageDialog  (  this  ,  errorMessage  ,  STRINGS  .  getString  (  "Error"  )  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 

private   class   DummyTrafficClientTransformer   extends   AbstractForwarder  <  String  ,  Void  >  { 

private   final   Document   echoDocument  ; 

public   DummyTrafficClientTransformer  (  Document   echoDocument  )  { 
this  .  echoDocument  =  echoDocument  ; 
} 

@  Override 
public   void   forward  (  final   String   input  )  throws   IOException  { 
SwingUtilities  .  invokeLater  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
int   length  =  echoDocument  .  getLength  (  )  ; 
try  { 
echoDocument  .  insertString  (  length  ,  input  +  '\n'  ,  null  )  ; 
}  catch  (  BadLocationException   ex  )  { 
LOGGER  .  log  (  Level  .  SEVERE  ,  null  ,  ex  )  ; 
} 
} 
}  )  ; 
} 
} 
} 

