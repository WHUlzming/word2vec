package   com  .  safi  .  asterisk  .  actionstep  .  impl  ; 

import   java  .  util  .  logging  .  Level  ; 
import   org  .  apache  .  commons  .  lang  .  StringUtils  ; 
import   org  .  asteriskjava  .  fastagi  .  AgiChannel  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  Notification  ; 
import   org  .  eclipse  .  emf  .  common  .  notify  .  NotificationChain  ; 
import   org  .  eclipse  .  emf  .  ecore  .  EClass  ; 
import   org  .  eclipse  .  emf  .  ecore  .  InternalEObject  ; 
import   org  .  eclipse  .  emf  .  ecore  .  impl  .  ENotificationImpl  ; 
import   com  .  safi  .  asterisk  .  Call  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  ActionstepPackage  ; 
import   com  .  safi  .  asterisk  .  actionstep  .  Voicemail  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepException  ; 
import   com  .  safi  .  core  .  actionstep  .  ActionStepFactory  ; 
import   com  .  safi  .  core  .  actionstep  .  DynamicValue  ; 
import   com  .  safi  .  core  .  actionstep  .  Output  ; 
import   com  .  safi  .  core  .  actionstep  .  OutputType  ; 
import   com  .  safi  .  core  .  actionstep  .  impl  .  ActionStepImpl  ; 
import   com  .  safi  .  core  .  call  .  CallConsumer1  ; 
import   com  .  safi  .  core  .  call  .  CallPackage  ; 
import   com  .  safi  .  core  .  call  .  SafiCall  ; 
import   com  .  safi  .  core  .  saflet  .  SafletContext  ; 
import   com  .  safi  .  db  .  VariableType  ; 
import   com  .  safi  .  db  .  util  .  VariableTranslator  ; 



















public   class   VoicemailImpl   extends   AsteriskActionStepImpl   implements   Voicemail  { 









protected   SafiCall   call1  ; 









protected   DynamicValue   mailbox  ; 









protected   static   final   boolean   SKIP_INSTRUCTIONS_EDEFAULT  =  false  ; 









protected   boolean   skipInstructions  =  SKIP_INSTRUCTIONS_EDEFAULT  ; 









protected   static   final   boolean   PLAY_UNAVAILABLE_MESSAGE_EDEFAULT  =  false  ; 









protected   boolean   playUnavailableMessage  =  PLAY_UNAVAILABLE_MESSAGE_EDEFAULT  ; 









protected   static   final   boolean   PLAY_BUSY_MESSAGE_EDEFAULT  =  false  ; 









protected   boolean   playBusyMessage  =  PLAY_BUSY_MESSAGE_EDEFAULT  ; 









protected   static   final   int   RECORDING_GAIN_EDEFAULT  =  -  1  ; 









protected   int   recordingGain  =  RECORDING_GAIN_EDEFAULT  ; 






protected   VoicemailImpl  (  )  { 
super  (  )  ; 
} 

@  Override 
public   void   beginProcessing  (  SafletContext   context  )  throws   ActionStepException  { 
super  .  beginProcessing  (  context  )  ; 
Exception   exception  =  null  ; 
int   idx  =  0  ; 
if  (  call1  ==  null  )  { 
handleException  (  context  ,  new   ActionStepException  (  "No current call found"  )  )  ; 
return  ; 
}  else   if  (  !  (  call1   instanceof   Call  )  )  { 
handleException  (  context  ,  new   ActionStepException  (  "Call isn't isn't an Asterisk call: "  +  call1  .  getClass  (  )  .  getName  (  )  )  )  ; 
return  ; 
} 
if  (  (  (  Call  )  call1  )  .  getChannel  (  )  ==  null  )  { 
handleException  (  context  ,  new   ActionStepException  (  "No channel found in current context"  )  )  ; 
return  ; 
} 
AgiChannel   channel  =  (  (  Call  )  call1  )  .  getChannel  (  )  ; 
try  { 
String   mb  =  (  String  )  VariableTranslator  .  translateValue  (  VariableType  .  TEXT  ,  resolveDynamicValue  (  mailbox  ,  context  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "Getting Voicemail for mailbox: "  +  mb  )  ; 
if  (  StringUtils  .  isBlank  (  mb  )  )  { 
exception  =  new   ActionStepException  (  "mailbox is required for Voicemail"  )  ; 
}  else  { 
StringBuffer   appCmd  =  new   StringBuffer  (  )  ; 
appCmd  .  append  (  mb  )  ; 
if  (  skipInstructions  )  appCmd  .  append  (  '|'  )  .  append  (  's'  )  ; 
if  (  playUnavailableMessage  )  { 
appCmd  .  append  (  '|'  )  ; 
appCmd  .  append  (  'u'  )  ; 
}  else   if  (  playBusyMessage  )  { 
appCmd  .  append  (  '|'  )  ; 
appCmd  .  append  (  'b'  )  ; 
} 
if  (  recordingGain  !=  0  )  { 
appCmd  .  append  (  '|'  )  ; 
appCmd  .  append  (  "g("  )  .  append  (  recordingGain  )  .  append  (  ")"  )  ; 
} 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "sending VoiceMail "  +  appCmd  )  ; 
int   result  =  channel  .  exec  (  "VoiceMail"  ,  appCmd  .  toString  (  )  )  ; 
if  (  debugLog  .  isLoggable  (  Level  .  FINEST  )  )  debug  (  "VoiceMail returned "  +  translateAppReturnValue  (  result  )  +  " of int "  +  result  )  ; 
if  (  result  ==  -  2  )  { 
exception  =  new   ActionStepException  (  "Application VoiceMail not found"  )  ; 
}  else   if  (  result  ==  -  1  )  { 
exception  =  new   ActionStepException  (  "Channel was hung up"  )  ; 
} 
String   status  =  channel  .  getVariable  (  "VMSTATUS"  )  ; 
if  (  StringUtils  .  equalsIgnoreCase  (  "SUCCESS"  ,  status  )  )  idx  =  1  ;  else   if  (  StringUtils  .  equalsIgnoreCase  (  "USEREXIT"  ,  status  )  )  idx  =  2  ;  else   exception  =  new   ActionStepException  (  "Voicemail() returned failure: "  +  status  )  ; 
} 
}  catch  (  Exception   e  )  { 
exception  =  e  ; 
} 
if  (  exception  !=  null  )  { 
handleException  (  context  ,  exception  )  ; 
return  ; 
} 
handleSuccess  (  context  ,  idx  )  ; 
} 

@  Override 
public   void   createDefaultOutputs  (  )  { 
super  .  createDefaultOutputs  (  )  ; 
Output   o  =  ActionStepFactory  .  eINSTANCE  .  createOutput  (  )  ; 
o  .  setOutputType  (  OutputType  .  CHOICE  )  ; 
o  .  setName  (  "userExit"  )  ; 
setErrorOutput  (  o  )  ; 
getOutputs  (  )  .  add  (  o  )  ; 
} 






@  Override 
protected   EClass   eStaticClass  (  )  { 
return   ActionstepPackage  .  Literals  .  VOICEMAIL  ; 
} 






public   SafiCall   getCall1  (  )  { 
if  (  call1  !=  null  &&  call1  .  eIsProxy  (  )  )  { 
InternalEObject   oldCall1  =  (  InternalEObject  )  call1  ; 
call1  =  (  SafiCall  )  eResolveProxy  (  oldCall1  )  ; 
if  (  call1  !=  oldCall1  )  { 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  RESOLVE  ,  ActionstepPackage  .  VOICEMAIL__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 
} 
return   call1  ; 
} 






public   SafiCall   basicGetCall1  (  )  { 
return   call1  ; 
} 






public   void   setCall1  (  SafiCall   newCall1  )  { 
SafiCall   oldCall1  =  call1  ; 
call1  =  newCall1  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL__CALL1  ,  oldCall1  ,  call1  )  )  ; 
} 






public   DynamicValue   getMailbox  (  )  { 
return   mailbox  ; 
} 






public   NotificationChain   basicSetMailbox  (  DynamicValue   newMailbox  ,  NotificationChain   msgs  )  { 
DynamicValue   oldMailbox  =  mailbox  ; 
mailbox  =  newMailbox  ; 
if  (  eNotificationRequired  (  )  )  { 
ENotificationImpl   notification  =  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL__MAILBOX  ,  oldMailbox  ,  newMailbox  )  ; 
if  (  msgs  ==  null  )  msgs  =  notification  ;  else   msgs  .  add  (  notification  )  ; 
} 
return   msgs  ; 
} 






public   void   setMailbox  (  DynamicValue   newMailbox  )  { 
if  (  newMailbox  !=  mailbox  )  { 
NotificationChain   msgs  =  null  ; 
if  (  mailbox  !=  null  )  msgs  =  (  (  InternalEObject  )  mailbox  )  .  eInverseRemove  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  VOICEMAIL__MAILBOX  ,  null  ,  msgs  )  ; 
if  (  newMailbox  !=  null  )  msgs  =  (  (  InternalEObject  )  newMailbox  )  .  eInverseAdd  (  this  ,  EOPPOSITE_FEATURE_BASE  -  ActionstepPackage  .  VOICEMAIL__MAILBOX  ,  null  ,  msgs  )  ; 
msgs  =  basicSetMailbox  (  newMailbox  ,  msgs  )  ; 
if  (  msgs  !=  null  )  msgs  .  dispatch  (  )  ; 
}  else   if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL__MAILBOX  ,  newMailbox  ,  newMailbox  )  )  ; 
} 






public   boolean   isSkipInstructions  (  )  { 
return   skipInstructions  ; 
} 






public   void   setSkipInstructions  (  boolean   newSkipInstructions  )  { 
boolean   oldSkipInstructions  =  skipInstructions  ; 
skipInstructions  =  newSkipInstructions  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL__SKIP_INSTRUCTIONS  ,  oldSkipInstructions  ,  skipInstructions  )  )  ; 
} 






public   boolean   isPlayUnavailableMessage  (  )  { 
return   playUnavailableMessage  ; 
} 






public   void   setPlayUnavailableMessage  (  boolean   newPlayUnavailableMessage  )  { 
boolean   oldPlayUnavailableMessage  =  playUnavailableMessage  ; 
playUnavailableMessage  =  newPlayUnavailableMessage  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL__PLAY_UNAVAILABLE_MESSAGE  ,  oldPlayUnavailableMessage  ,  playUnavailableMessage  )  )  ; 
} 






public   boolean   isPlayBusyMessage  (  )  { 
return   playBusyMessage  ; 
} 






public   void   setPlayBusyMessage  (  boolean   newPlayBusyMessage  )  { 
boolean   oldPlayBusyMessage  =  playBusyMessage  ; 
playBusyMessage  =  newPlayBusyMessage  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL__PLAY_BUSY_MESSAGE  ,  oldPlayBusyMessage  ,  playBusyMessage  )  )  ; 
} 






public   int   getRecordingGain  (  )  { 
return   recordingGain  ; 
} 






public   void   setRecordingGain  (  int   newRecordingGain  )  { 
int   oldRecordingGain  =  recordingGain  ; 
recordingGain  =  newRecordingGain  ; 
if  (  eNotificationRequired  (  )  )  eNotify  (  new   ENotificationImpl  (  this  ,  Notification  .  SET  ,  ActionstepPackage  .  VOICEMAIL__RECORDING_GAIN  ,  oldRecordingGain  ,  recordingGain  )  )  ; 
} 






@  Override 
public   NotificationChain   eInverseRemove  (  InternalEObject   otherEnd  ,  int   featureID  ,  NotificationChain   msgs  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL__MAILBOX  : 
return   basicSetMailbox  (  null  ,  msgs  )  ; 
} 
return   super  .  eInverseRemove  (  otherEnd  ,  featureID  ,  msgs  )  ; 
} 






@  Override 
public   Object   eGet  (  int   featureID  ,  boolean   resolve  ,  boolean   coreType  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL__CALL1  : 
if  (  resolve  )  return   getCall1  (  )  ; 
return   basicGetCall1  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL__MAILBOX  : 
return   getMailbox  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL__SKIP_INSTRUCTIONS  : 
return   isSkipInstructions  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL__PLAY_UNAVAILABLE_MESSAGE  : 
return   isPlayUnavailableMessage  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL__PLAY_BUSY_MESSAGE  : 
return   isPlayBusyMessage  (  )  ; 
case   ActionstepPackage  .  VOICEMAIL__RECORDING_GAIN  : 
return   getRecordingGain  (  )  ; 
} 
return   super  .  eGet  (  featureID  ,  resolve  ,  coreType  )  ; 
} 






@  SuppressWarnings  (  "unchecked"  ) 
@  Override 
public   void   eSet  (  int   featureID  ,  Object   newValue  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL__CALL1  : 
setCall1  (  (  SafiCall  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__MAILBOX  : 
setMailbox  (  (  DynamicValue  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__SKIP_INSTRUCTIONS  : 
setSkipInstructions  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__PLAY_UNAVAILABLE_MESSAGE  : 
setPlayUnavailableMessage  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__PLAY_BUSY_MESSAGE  : 
setPlayBusyMessage  (  (  Boolean  )  newValue  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__RECORDING_GAIN  : 
setRecordingGain  (  (  Integer  )  newValue  )  ; 
return  ; 
} 
super  .  eSet  (  featureID  ,  newValue  )  ; 
} 






@  Override 
public   void   eUnset  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL__CALL1  : 
setCall1  (  (  SafiCall  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__MAILBOX  : 
setMailbox  (  (  DynamicValue  )  null  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__SKIP_INSTRUCTIONS  : 
setSkipInstructions  (  SKIP_INSTRUCTIONS_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__PLAY_UNAVAILABLE_MESSAGE  : 
setPlayUnavailableMessage  (  PLAY_UNAVAILABLE_MESSAGE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__PLAY_BUSY_MESSAGE  : 
setPlayBusyMessage  (  PLAY_BUSY_MESSAGE_EDEFAULT  )  ; 
return  ; 
case   ActionstepPackage  .  VOICEMAIL__RECORDING_GAIN  : 
setRecordingGain  (  RECORDING_GAIN_EDEFAULT  )  ; 
return  ; 
} 
super  .  eUnset  (  featureID  )  ; 
} 






@  Override 
public   boolean   eIsSet  (  int   featureID  )  { 
switch  (  featureID  )  { 
case   ActionstepPackage  .  VOICEMAIL__CALL1  : 
return   call1  !=  null  ; 
case   ActionstepPackage  .  VOICEMAIL__MAILBOX  : 
return   mailbox  !=  null  ; 
case   ActionstepPackage  .  VOICEMAIL__SKIP_INSTRUCTIONS  : 
return   skipInstructions  !=  SKIP_INSTRUCTIONS_EDEFAULT  ; 
case   ActionstepPackage  .  VOICEMAIL__PLAY_UNAVAILABLE_MESSAGE  : 
return   playUnavailableMessage  !=  PLAY_UNAVAILABLE_MESSAGE_EDEFAULT  ; 
case   ActionstepPackage  .  VOICEMAIL__PLAY_BUSY_MESSAGE  : 
return   playBusyMessage  !=  PLAY_BUSY_MESSAGE_EDEFAULT  ; 
case   ActionstepPackage  .  VOICEMAIL__RECORDING_GAIN  : 
return   recordingGain  !=  RECORDING_GAIN_EDEFAULT  ; 
} 
return   super  .  eIsSet  (  featureID  )  ; 
} 






@  Override 
public   int   eBaseStructuralFeatureID  (  int   derivedFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  derivedFeatureID  )  { 
case   ActionstepPackage  .  VOICEMAIL__CALL1  : 
return   CallPackage  .  CALL_CONSUMER1__CALL1  ; 
default  : 
return  -  1  ; 
} 
} 
return   super  .  eBaseStructuralFeatureID  (  derivedFeatureID  ,  baseClass  )  ; 
} 






@  Override 
public   int   eDerivedStructuralFeatureID  (  int   baseFeatureID  ,  Class  <  ?  >  baseClass  )  { 
if  (  baseClass  ==  CallConsumer1  .  class  )  { 
switch  (  baseFeatureID  )  { 
case   CallPackage  .  CALL_CONSUMER1__CALL1  : 
return   ActionstepPackage  .  VOICEMAIL__CALL1  ; 
default  : 
return  -  1  ; 
} 
} 
return   super  .  eDerivedStructuralFeatureID  (  baseFeatureID  ,  baseClass  )  ; 
} 






@  Override 
public   String   toString  (  )  { 
if  (  eIsProxy  (  )  )  return   super  .  toString  (  )  ; 
StringBuffer   result  =  new   StringBuffer  (  super  .  toString  (  )  )  ; 
result  .  append  (  " (skipInstructions: "  )  ; 
result  .  append  (  skipInstructions  )  ; 
result  .  append  (  ", playUnavailableMessage: "  )  ; 
result  .  append  (  playUnavailableMessage  )  ; 
result  .  append  (  ", playBusyMessage: "  )  ; 
result  .  append  (  playBusyMessage  )  ; 
result  .  append  (  ", recordingGain: "  )  ; 
result  .  append  (  recordingGain  )  ; 
result  .  append  (  ')'  )  ; 
return   result  .  toString  (  )  ; 
} 
} 

