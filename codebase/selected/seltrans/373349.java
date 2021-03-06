package   jmri  .  jmrit  .  symbolicprog  ; 

import   jmri  .  Programmer  ; 
import   jmri  .  ProgListener  ; 
import   java  .  awt  .  Color  ; 
import   javax  .  swing  .  JLabel  ; 
import   javax  .  swing  .  JTextField  ; 
















public   class   CvValue   extends   AbstractValue   implements   ProgListener  { 

static   final   java  .  util  .  ResourceBundle   rbt  =  jmri  .  jmrit  .  symbolicprog  .  SymbolicProgBundle  .  bundle  (  )  ; 

public   CvValue  (  int   num  ,  Programmer   pProgrammer  )  { 
_num  =  num  ; 
mProgrammer  =  pProgrammer  ; 
_tableEntry  =  new   JTextField  (  "0"  ,  3  )  ; 
_defaultColor  =  _tableEntry  .  getBackground  (  )  ; 
_tableEntry  .  setBackground  (  COLOR_UNKNOWN  )  ; 
} 

public   CvValue  (  int   num  ,  String   cvName  ,  int   piCv  ,  int   piVal  ,  int   siCv  ,  int   siVal  ,  int   iCv  ,  Programmer   pProgrammer  )  { 
_num  =  num  ; 
_cvName  =  cvName  ; 
if  (  cvName  ==  null  )  log  .  error  (  "cvName == null in ctor num: "  +  num  )  ; 
_piCv  =  piCv  ; 
_piVal  =  piVal  ; 
_siCv  =  siCv  ; 
_siVal  =  siVal  ; 
_iCv  =  iCv  ; 
mProgrammer  =  pProgrammer  ; 
_tableEntry  =  new   JTextField  (  "0"  ,  3  )  ; 
_defaultColor  =  _tableEntry  .  getBackground  (  )  ; 
_tableEntry  .  setBackground  (  COLOR_UNKNOWN  )  ; 
} 

public   String   toString  (  )  { 
return  "CvValue _num="  +  _num  +  " _cvName="  +  _cvName  +  " _piCv="  +  _piCv  +  " _siCv="  +  _siCv  +  " _iCv="  +  _iCv  ; 
} 

public   int   number  (  )  { 
return   _num  ; 
} 

private   int   _num  ; 

public   String   cvName  (  )  { 
return   _cvName  ; 
} 

private   String   _cvName  =  ""  ; 

public   int   piCv  (  )  { 
return   _piCv  ; 
} 

private   int   _piCv  ; 

public   int   piVal  (  )  { 
return   _piVal  ; 
} 

private   int   _piVal  ; 

public   int   siCv  (  )  { 
return   _siCv  ; 
} 

private   int   _siCv  ; 

public   int   siVal  (  )  { 
return   _siVal  ; 
} 

private   int   _siVal  ; 

public   int   iCv  (  )  { 
return   _iCv  ; 
} 

private   int   _iCv  ; 

private   JLabel   _status  =  null  ; 

private   Programmer   mProgrammer  ; 

public   int   getValue  (  )  { 
return   _value  ; 
} 

Color   getColor  (  )  { 
return   _tableEntry  .  getBackground  (  )  ; 
} 

protected   void   notifyValueChange  (  int   value  )  { 
prop  .  firePropertyChange  (  "Value"  ,  null  ,  Integer  .  valueOf  (  value  )  )  ; 
} 





public   void   setValue  (  int   value  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "CV "  +  number  (  )  +  " value changed from "  +  _value  +  " to "  +  value  )  ; 
setState  (  EDITED  )  ; 
if  (  _value  !=  value  )  { 
_value  =  value  ; 
_tableEntry  .  setText  (  ""  +  value  )  ; 
notifyValueChange  (  value  )  ; 
} 
} 

private   int   _value  =  0  ; 





public   int   getDecoderValue  (  )  { 
return   _decoderValue  ; 
} 

private   int   _decoderValue  =  0  ; 

public   int   getState  (  )  { 
return   _state  ; 
} 




public   void   setState  (  int   state  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "cv "  +  number  (  )  +  " set state from "  +  _state  +  " to "  +  state  )  ; 
int   oldstate  =  _state  ; 
_state  =  state  ; 
switch  (  state  )  { 
case   UNKNOWN  : 
setColor  (  COLOR_UNKNOWN  )  ; 
break  ; 
case   EDITED  : 
setColor  (  COLOR_EDITED  )  ; 
break  ; 
case   READ  : 
setColor  (  COLOR_READ  )  ; 
break  ; 
case   STORED  : 
setColor  (  COLOR_STORED  )  ; 
break  ; 
case   FROMFILE  : 
setColor  (  COLOR_FROMFILE  )  ; 
break  ; 
case   SAME  : 
setColor  (  COLOR_SAME  )  ; 
break  ; 
case   DIFF  : 
setColor  (  COLOR_DIFF  )  ; 
break  ; 
default  : 
log  .  error  (  "Inconsistent state: "  +  _state  )  ; 
} 
if  (  oldstate  !=  state  )  prop  .  firePropertyChange  (  "State"  ,  Integer  .  valueOf  (  oldstate  )  ,  Integer  .  valueOf  (  state  )  )  ; 
} 

private   int   _state  =  0  ; 

public   boolean   isBusy  (  )  { 
return   _busy  ; 
} 





private   void   setBusy  (  boolean   busy  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "setBusy from "  +  _busy  +  " to "  +  busy  +  " state "  +  _state  )  ; 
boolean   oldBusy  =  _busy  ; 
_busy  =  busy  ; 
notifyBusyChange  (  oldBusy  ,  busy  )  ; 
} 




private   void   notifyBusyChange  (  boolean   oldBusy  ,  boolean   newBusy  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "notifyBusy from "  +  oldBusy  +  " to "  +  newBusy  +  " current state "  +  _state  )  ; 
if  (  oldBusy  !=  newBusy  )  prop  .  firePropertyChange  (  "Busy"  ,  oldBusy  ?  Boolean  .  TRUE  :  Boolean  .  FALSE  ,  newBusy  ?  Boolean  .  TRUE  :  Boolean  .  FALSE  )  ; 
} 

private   boolean   _busy  =  false  ; 

Color   _defaultColor  ; 

void   setColor  (  Color   c  )  { 
if  (  c  !=  null  )  _tableEntry  .  setBackground  (  c  )  ;  else   _tableEntry  .  setBackground  (  _defaultColor  )  ; 
} 

JTextField   _tableEntry  =  null  ; 

JTextField   getTableEntry  (  )  { 
return   _tableEntry  ; 
} 






public   void   setReadOnly  (  boolean   is  )  { 
_readOnly  =  is  ; 
} 

private   boolean   _readOnly  =  false  ; 






public   boolean   getReadOnly  (  )  { 
return   _readOnly  ; 
} 






public   void   setInfoOnly  (  boolean   is  )  { 
_infoOnly  =  is  ; 
} 

private   boolean   _infoOnly  =  false  ; 






public   boolean   getInfoOnly  (  )  { 
return   _infoOnly  ; 
} 






public   void   setWriteOnly  (  boolean   is  )  { 
_writeOnly  =  is  ; 
} 

private   boolean   _writeOnly  =  false  ; 






public   boolean   getWriteOnly  (  )  { 
return   _writeOnly  ; 
} 

public   void   setToRead  (  boolean   state  )  { 
if  (  getInfoOnly  (  )  ||  getWriteOnly  (  )  )  state  =  false  ; 
_toRead  =  state  ; 
} 

public   boolean   isToRead  (  )  { 
return   _toRead  ; 
} 

private   boolean   _toRead  =  false  ; 

public   void   setToWrite  (  boolean   state  )  { 
if  (  getInfoOnly  (  )  ||  getReadOnly  (  )  )  state  =  false  ; 
_toWrite  =  state  ; 
} 

public   boolean   isToWrite  (  )  { 
return   _toWrite  ; 
} 

private   boolean   _toWrite  =  false  ; 

private   boolean   _reading  =  false  ; 

private   boolean   _confirm  =  false  ; 

public   void   read  (  JLabel   status  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "read call with Cv number "  +  _num  )  ; 
setToRead  (  false  )  ; 
_status  =  status  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateReadingCV"  )  ,  new   Object  [  ]  {  ""  +  _num  }  )  )  ; 
if  (  mProgrammer  !=  null  )  { 
setBusy  (  true  )  ; 
_reading  =  true  ; 
_confirm  =  false  ; 
try  { 
mProgrammer  .  readCV  (  _num  ,  this  )  ; 
}  catch  (  Exception   e  )  { 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateExceptionDuringRead"  )  ,  new   Object  [  ]  {  e  .  toString  (  )  }  )  )  ; 
log  .  warn  (  "Exception during CV read: "  +  e  )  ; 
setBusy  (  false  )  ; 
} 
}  else  { 
if  (  status  !=  null  )  status  .  setText  (  rbt  .  getString  (  "StateNoProgrammer"  )  )  ; 
log  .  error  (  "No programmer available!"  )  ; 
} 
} 

public   void   readIcV  (  JLabel   status  )  { 
setToRead  (  false  )  ; 
_status  =  status  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateReadingIndexedCV"  )  ,  new   Object  [  ]  {  ""  +  _iCv  ,  ""  +  _piVal  +  (  _siVal  >=  0  ?  "."  +  _siVal  :  ""  )  }  )  )  ; 
if  (  mProgrammer  !=  null  )  { 
setBusy  (  true  )  ; 
_reading  =  true  ; 
_confirm  =  false  ; 
try  { 
setState  (  UNKNOWN  )  ; 
mProgrammer  .  readCV  (  _iCv  ,  this  )  ; 
}  catch  (  Exception   e  )  { 
setState  (  UNKNOWN  )  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateExceptionDuringIndexedRead"  )  ,  new   Object  [  ]  {  e  .  toString  (  )  }  )  )  ; 
log  .  warn  (  "Exception during IndexedCV read: "  +  e  )  ; 
setBusy  (  false  )  ; 
} 
}  else  { 
if  (  status  !=  null  )  status  .  setText  (  rbt  .  getString  (  "StateNoProgrammer"  )  )  ; 
log  .  error  (  "No programmer available!"  )  ; 
} 
} 

public   void   confirmIcV  (  JLabel   status  )  { 
setToRead  (  false  )  ; 
_status  =  status  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateConfirmIndexedCV"  )  ,  new   Object  [  ]  {  ""  +  _iCv  ,  ""  +  _piVal  +  (  _siVal  >=  0  ?  "."  +  _siVal  :  ""  )  }  )  )  ; 
if  (  mProgrammer  !=  null  )  { 
setBusy  (  true  )  ; 
_reading  =  false  ; 
_confirm  =  true  ; 
try  { 
setState  (  UNKNOWN  )  ; 
mProgrammer  .  readCV  (  _iCv  ,  this  )  ; 
}  catch  (  Exception   e  )  { 
setState  (  UNKNOWN  )  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateExceptionDuringIndexedRead"  )  ,  new   Object  [  ]  {  e  .  toString  (  )  }  )  )  ; 
log  .  warn  (  "Exception during IndexedCV read: "  +  e  )  ; 
setBusy  (  false  )  ; 
} 
}  else  { 
if  (  status  !=  null  )  status  .  setText  (  rbt  .  getString  (  "StateNoProgrammer"  )  )  ; 
log  .  error  (  "No programmer available!"  )  ; 
} 
} 

public   void   confirm  (  JLabel   status  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "confirm call with Cv number "  +  _num  )  ; 
_status  =  status  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateConfirmingCV"  )  ,  new   Object  [  ]  {  ""  +  _num  }  )  )  ; 
if  (  mProgrammer  !=  null  )  { 
setBusy  (  true  )  ; 
_reading  =  false  ; 
_confirm  =  true  ; 
try  { 
mProgrammer  .  confirmCV  (  _num  ,  _value  ,  this  )  ; 
}  catch  (  Exception   e  )  { 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateExceptionDuringConfirm"  )  ,  new   Object  [  ]  {  e  .  toString  (  )  }  )  )  ; 
log  .  warn  (  "Exception during CV read: "  +  e  )  ; 
setBusy  (  false  )  ; 
} 
}  else  { 
if  (  status  !=  null  )  status  .  setText  (  rbt  .  getString  (  "StateNoProgrammer"  )  )  ; 
log  .  error  (  "No programmer available!"  )  ; 
} 
} 

public   void   write  (  JLabel   status  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "write call with Cv number "  +  _num  )  ; 
setToWrite  (  false  )  ; 
_status  =  status  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateWritingCV"  )  ,  new   Object  [  ]  {  ""  +  _num  }  )  )  ; 
if  (  mProgrammer  !=  null  )  { 
setBusy  (  true  )  ; 
_reading  =  false  ; 
_confirm  =  false  ; 
try  { 
setState  (  UNKNOWN  )  ; 
mProgrammer  .  writeCV  (  _num  ,  _value  ,  this  )  ; 
}  catch  (  Exception   e  )  { 
setState  (  UNKNOWN  )  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateExceptionDuringWrite"  )  ,  new   Object  [  ]  {  e  .  toString  (  )  }  )  )  ; 
log  .  warn  (  "Exception during CV write: "  +  e  )  ; 
setBusy  (  false  )  ; 
} 
}  else  { 
if  (  status  !=  null  )  status  .  setText  (  rbt  .  getString  (  "StateNoProgrammer"  )  )  ; 
log  .  error  (  "No programmer available!"  )  ; 
} 
} 

public   void   writePI  (  JLabel   status  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "write call with PI number "  +  _piVal  )  ; 
_status  =  status  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateWritingPICV"  )  ,  new   Object  [  ]  {  ""  +  _num  }  )  )  ; 
if  (  mProgrammer  !=  null  )  { 
setBusy  (  true  )  ; 
_reading  =  false  ; 
_confirm  =  false  ; 
try  { 
setState  (  UNKNOWN  )  ; 
mProgrammer  .  writeCV  (  _piCv  ,  _piVal  ,  this  )  ; 
}  catch  (  Exception   e  )  { 
setState  (  UNKNOWN  )  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateExceptionDuringWrite"  )  ,  new   Object  [  ]  {  e  .  toString  (  )  }  )  )  ; 
log  .  warn  (  "Exception during CV write: "  +  e  )  ; 
setBusy  (  false  )  ; 
} 
}  else  { 
if  (  status  !=  null  )  status  .  setText  (  rbt  .  getString  (  "StateNoProgrammer"  )  )  ; 
log  .  error  (  "No programmer available!"  )  ; 
} 
} 

public   void   writeSI  (  JLabel   status  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "write call with SI number "  +  _siVal  )  ; 
_status  =  status  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateWritingSICV"  )  ,  new   Object  [  ]  {  ""  +  _num  }  )  )  ; 
if  (  mProgrammer  !=  null  )  { 
setBusy  (  true  )  ; 
_reading  =  false  ; 
_confirm  =  false  ; 
try  { 
setState  (  UNKNOWN  )  ; 
if  (  _siVal  >=  0  )  { 
mProgrammer  .  writeCV  (  _siCv  ,  _siVal  ,  this  )  ; 
}  else  { 
mProgrammer  .  writeCV  (  _siCv  ,  0  ,  this  )  ; 
} 
}  catch  (  Exception   e  )  { 
setState  (  UNKNOWN  )  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateExceptionDuringWrite"  )  ,  new   Object  [  ]  {  e  .  toString  (  )  }  )  )  ; 
log  .  warn  (  "Exception during CV write: "  +  e  )  ; 
setBusy  (  false  )  ; 
} 
}  else  { 
if  (  status  !=  null  )  status  .  setText  (  rbt  .  getString  (  "StateNoProgrammer"  )  )  ; 
log  .  error  (  "No programmer available!"  )  ; 
} 
} 

public   void   writeIcV  (  JLabel   status  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "write call with IndexedCv number "  +  _iCv  )  ; 
setToWrite  (  false  )  ; 
_status  =  status  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateWritingIndexedCV"  )  ,  new   Object  [  ]  {  ""  +  _iCv  ,  ""  +  _piVal  +  (  _siVal  >=  0  ?  "."  +  _siVal  :  ""  )  }  )  )  ; 
if  (  mProgrammer  !=  null  )  { 
setBusy  (  true  )  ; 
_reading  =  false  ; 
_confirm  =  false  ; 
try  { 
setState  (  UNKNOWN  )  ; 
mProgrammer  .  writeCV  (  _iCv  ,  _value  ,  this  )  ; 
}  catch  (  Exception   e  )  { 
setState  (  UNKNOWN  )  ; 
if  (  status  !=  null  )  status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateExceptionDuringIndexedWrite"  )  ,  new   Object  [  ]  {  e  .  toString  (  )  }  )  )  ; 
log  .  warn  (  "Exception during CV write: "  +  e  )  ; 
setBusy  (  false  )  ; 
} 
}  else  { 
if  (  status  !=  null  )  status  .  setText  (  rbt  .  getString  (  "StateNoProgrammer"  )  )  ; 
log  .  error  (  "No programmer available!"  )  ; 
} 
} 

public   void   programmingOpReply  (  int   value  ,  int   retval  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "CV progOpReply for CV "  +  _num  +  " with retval "  +  retval  +  " during "  +  (  _reading  ?  "read sequence"  :  (  _confirm  ?  "confirm sequence"  :  "write sequence"  )  )  )  ; 
if  (  !  _busy  )  log  .  error  (  "opReply when not busy!"  )  ; 
boolean   oldBusy  =  _busy  ; 
if  (  retval  ==  OK  )  { 
if  (  _status  !=  null  )  _status  .  setText  (  rbt  .  getString  (  "StateOK"  )  )  ; 
if  (  _reading  )  { 
_value  =  value  ; 
_tableEntry  .  setText  (  Integer  .  toString  (  value  )  )  ; 
notifyValueChange  (  value  )  ; 
setState  (  READ  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "CV setting not busy on end read"  )  ; 
_busy  =  false  ; 
notifyBusyChange  (  oldBusy  ,  _busy  )  ; 
}  else   if  (  _confirm  )  { 
_decoderValue  =  value  ; 
if  (  value  ==  _value  )  setState  (  SAME  )  ;  else   setState  (  DIFF  )  ; 
_busy  =  false  ; 
notifyBusyChange  (  oldBusy  ,  _busy  )  ; 
}  else  { 
setState  (  STORED  )  ; 
_busy  =  false  ; 
notifyBusyChange  (  oldBusy  ,  _busy  )  ; 
} 
}  else  { 
if  (  _status  !=  null  )  _status  .  setText  (  java  .  text  .  MessageFormat  .  format  (  rbt  .  getString  (  "StateProgrammerError"  )  ,  new   Object  [  ]  {  mProgrammer  .  decodeErrorCode  (  retval  )  }  )  )  ; 
javax  .  swing  .  Timer   timer  =  new   javax  .  swing  .  Timer  (  1000  ,  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   e  )  { 
errorTimeout  (  )  ; 
} 
}  )  ; 
timer  .  setInitialDelay  (  1000  )  ; 
timer  .  setRepeats  (  false  )  ; 
timer  .  start  (  )  ; 
} 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "CV progOpReply end of handling CV "  +  _num  )  ; 
} 

void   errorTimeout  (  )  { 
setState  (  UNKNOWN  )  ; 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "CV setting not busy on error reply"  )  ; 
_busy  =  false  ; 
notifyBusyChange  (  true  ,  _busy  )  ; 
} 

public   void   dispose  (  )  { 
if  (  log  .  isDebugEnabled  (  )  )  log  .  debug  (  "dispose"  )  ; 
} 

static   org  .  apache  .  log4j  .  Logger   log  =  org  .  apache  .  log4j  .  Logger  .  getLogger  (  CvValue  .  class  .  getName  (  )  )  ; 
} 

