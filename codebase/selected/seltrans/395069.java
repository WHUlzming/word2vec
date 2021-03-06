package   org  .  jgroups  .  blocks  ; 

import   java  .  io  .  Serializable  ; 
import   java  .  util  .  Collection  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Map  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  StringTokenizer  ; 
import   java  .  util  .  TreeMap  ; 
import   java  .  util  .  Vector  ; 
import   javax  .  management  .  MBeanServer  ; 
import   org  .  jgroups  .  Address  ; 
import   org  .  jgroups  .  Channel  ; 
import   org  .  jgroups  .  ChannelClosedException  ; 
import   org  .  jgroups  .  ChannelNotConnectedException  ; 
import   org  .  jgroups  .  JChannel  ; 
import   org  .  jgroups  .  Message  ; 
import   org  .  jgroups  .  ReceiverAdapter  ; 
import   org  .  jgroups  .  View  ; 
import   org  .  jgroups  .  annotations  .  Unsupported  ; 
import   org  .  jgroups  .  jmx  .  JmxConfigurator  ; 
import   org  .  jgroups  .  logging  .  Log  ; 
import   org  .  jgroups  .  logging  .  LogFactory  ; 
import   org  .  jgroups  .  util  .  Util  ; 







@  Unsupported 
public   class   ReplicatedTree   extends   ReceiverAdapter  { 

public   static   final   String   SEPARATOR  =  "/"  ; 

static   final   int   INDENT  =  4  ; 

Node   root  =  new   Node  (  SEPARATOR  ,  SEPARATOR  ,  null  ,  null  )  ; 

final   Vector  <  ReplicatedTreeListener  >  listeners  =  new   Vector  <  ReplicatedTreeListener  >  (  )  ; 

JChannel   channel  =  null  ; 

String   groupname  =  "ReplicatedTree-Group"  ; 

final   Vector  <  Address  >  members  =  new   Vector  <  Address  >  (  )  ; 

long   state_fetch_timeout  =  10000  ; 

boolean   jmx  =  false  ; 

protected   final   Log   log  =  LogFactory  .  getLog  (  this  .  getClass  (  )  )  ; 




boolean   remote_calls  =  true  ; 

String   props  =  "udp.xml"  ; 



private   boolean   send_message  =  false  ; 

public   interface   ReplicatedTreeListener  { 

void   nodeAdded  (  String   fqn  )  ; 

void   nodeRemoved  (  String   fqn  )  ; 

void   nodeModified  (  String   fqn  )  ; 

void   viewChange  (  View   new_view  )  ; 
} 





public   ReplicatedTree  (  String   groupname  ,  String   props  ,  long   state_fetch_timeout  )  throws   Exception  { 
if  (  groupname  !=  null  )  this  .  groupname  =  groupname  ; 
if  (  props  !=  null  )  this  .  props  =  props  ; 
this  .  state_fetch_timeout  =  state_fetch_timeout  ; 
channel  =  new   JChannel  (  this  .  props  )  ; 
channel  .  setReceiver  (  this  )  ; 
channel  .  connect  (  this  .  groupname  )  ; 
start  (  )  ; 
} 

public   ReplicatedTree  (  String   groupname  ,  String   props  ,  long   state_fetch_timeout  ,  boolean   jmx  )  throws   Exception  { 
if  (  groupname  !=  null  )  this  .  groupname  =  groupname  ; 
if  (  props  !=  null  )  this  .  props  =  props  ; 
this  .  jmx  =  jmx  ; 
this  .  state_fetch_timeout  =  state_fetch_timeout  ; 
channel  =  new   JChannel  (  this  .  props  )  ; 
channel  .  setReceiver  (  this  )  ; 
channel  .  connect  (  this  .  groupname  )  ; 
if  (  jmx  )  { 
MBeanServer   server  =  Util  .  getMBeanServer  (  )  ; 
if  (  server  ==  null  )  throw   new   Exception  (  "No MBeanServers found; need to run with an MBeanServer present, or inside JDK 5"  )  ; 
JmxConfigurator  .  registerChannel  (  channel  ,  server  ,  "jgroups"  ,  channel  .  getClusterName  (  )  ,  true  )  ; 
} 
start  (  )  ; 
} 

public   ReplicatedTree  (  )  { 
} 




public   ReplicatedTree  (  JChannel   channel  )  throws   Exception  { 
this  .  channel  =  channel  ; 
channel  .  setReceiver  (  this  )  ; 
start  (  )  ; 
} 

public   void   setRemoteCalls  (  boolean   flag  )  { 
remote_calls  =  flag  ; 
} 

public   void   setRootNode  (  Node   n  )  { 
root  =  n  ; 
} 

public   Address   getLocalAddress  (  )  { 
return   channel  !=  null  ?  channel  .  getAddress  (  )  :  null  ; 
} 

public   Vector  <  Address  >  getMembers  (  )  { 
return   members  ; 
} 




public   void   fetchState  (  long   timeout  )  throws   ChannelClosedException  ,  ChannelNotConnectedException  { 
boolean   rc  =  channel  .  getState  (  null  ,  timeout  )  ; 
if  (  log  .  isInfoEnabled  (  )  )  { 
if  (  rc  )  log  .  info  (  "state was retrieved successfully"  )  ;  else   log  .  info  (  "state could not be retrieved (first member)"  )  ; 
} 
} 

public   void   addReplicatedTreeListener  (  ReplicatedTreeListener   listener  )  { 
if  (  !  listeners  .  contains  (  listener  )  )  listeners  .  addElement  (  listener  )  ; 
} 

public   void   removeReplicatedTreeListener  (  ReplicatedTreeListener   listener  )  { 
listeners  .  removeElement  (  listener  )  ; 
} 

public   final   void   start  (  )  throws   Exception  { 
boolean   rc  =  channel  .  getState  (  null  ,  state_fetch_timeout  )  ; 
if  (  log  .  isInfoEnabled  (  )  )  { 
if  (  rc  )  log  .  info  (  "state was retrieved successfully"  )  ;  else   log  .  info  (  "state could not be retrieved (first member)"  )  ; 
} 
} 

public   void   stop  (  )  { 
Util  .  close  (  channel  )  ; 
} 









public   void   put  (  String   fqn  ,  HashMap   data  )  { 
if  (  !  remote_calls  )  { 
_put  (  fqn  ,  data  )  ; 
return  ; 
} 
if  (  send_message  ==  true  )  { 
if  (  channel  ==  null  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "channel is null, cannot broadcast PUT request"  )  ; 
return  ; 
} 
try  { 
channel  .  send  (  new   Message  (  null  ,  null  ,  new   Request  (  Request  .  PUT  ,  fqn  ,  data  )  )  )  ; 
}  catch  (  Exception   ex  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "failure bcasting PUT request: "  +  ex  )  ; 
} 
}  else  { 
_put  (  fqn  ,  data  )  ; 
} 
} 









public   void   put  (  String   fqn  ,  String   key  ,  Object   value  )  { 
if  (  !  remote_calls  )  { 
_put  (  fqn  ,  key  ,  value  )  ; 
return  ; 
} 
if  (  send_message  ==  true  )  { 
if  (  channel  ==  null  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "channel is null, cannot broadcast PUT request"  )  ; 
return  ; 
} 
try  { 
channel  .  send  (  new   Message  (  null  ,  null  ,  new   Request  (  Request  .  PUT  ,  fqn  ,  key  ,  value  )  )  )  ; 
}  catch  (  Exception   ex  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "failure bcasting PUT request: "  +  ex  )  ; 
} 
}  else  { 
_put  (  fqn  ,  key  ,  value  )  ; 
} 
} 





public   void   remove  (  String   fqn  )  { 
if  (  !  remote_calls  )  { 
_remove  (  fqn  )  ; 
return  ; 
} 
if  (  send_message  ==  true  )  { 
if  (  channel  ==  null  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "channel is null, cannot broadcast REMOVE request"  )  ; 
return  ; 
} 
try  { 
channel  .  send  (  new   Message  (  null  ,  null  ,  new   Request  (  Request  .  REMOVE  ,  fqn  )  )  )  ; 
}  catch  (  Exception   ex  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "failure bcasting REMOVE request: "  +  ex  )  ; 
} 
}  else  { 
_remove  (  fqn  )  ; 
} 
} 






public   void   remove  (  String   fqn  ,  String   key  )  { 
if  (  !  remote_calls  )  { 
_remove  (  fqn  ,  key  )  ; 
return  ; 
} 
if  (  send_message  ==  true  )  { 
if  (  channel  ==  null  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "channel is null, cannot broadcast REMOVE request"  )  ; 
return  ; 
} 
try  { 
channel  .  send  (  new   Message  (  null  ,  null  ,  new   Request  (  Request  .  REMOVE  ,  fqn  ,  key  )  )  )  ; 
}  catch  (  Exception   ex  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "failure bcasting REMOVE request: "  +  ex  )  ; 
} 
}  else  { 
_remove  (  fqn  ,  key  )  ; 
} 
} 






public   boolean   exists  (  String   fqn  )  { 
if  (  fqn  ==  null  )  return   false  ; 
return   findNode  (  fqn  )  !=  null  ; 
} 







public   Set   getKeys  (  String   fqn  )  { 
Node   n  =  findNode  (  fqn  )  ; 
Map   data  ; 
if  (  n  ==  null  )  return   null  ; 
data  =  n  .  getData  (  )  ; 
if  (  data  ==  null  )  return   null  ; 
return   data  .  keySet  (  )  ; 
} 







public   Object   get  (  String   fqn  ,  String   key  )  { 
Node   n  =  findNode  (  fqn  )  ; 
if  (  n  ==  null  )  return   null  ; 
return   n  .  getData  (  key  )  ; 
} 








Map  <  String  ,  Object  >  get  (  String   fqn  )  { 
Node   n  =  findNode  (  fqn  )  ; 
if  (  n  ==  null  )  return   null  ; 
return   n  .  getData  (  )  ; 
} 





public   String   print  (  String   fqn  )  { 
Node   n  =  findNode  (  fqn  )  ; 
if  (  n  ==  null  )  return   null  ; 
return   n  .  toString  (  )  ; 
} 






public   Set   getChildrenNames  (  String   fqn  )  { 
Node   n  =  findNode  (  fqn  )  ; 
Map   m  ; 
if  (  n  ==  null  )  return   null  ; 
m  =  n  .  getChildren  (  )  ; 
if  (  m  !=  null  )  return   m  .  keySet  (  )  ;  else   return   null  ; 
} 

public   String   toString  (  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
int   indent  =  0  ; 
Map   children  ; 
children  =  root  .  getChildren  (  )  ; 
if  (  children  !=  null  &&  children  .  size  (  )  >  0  )  { 
Collection   nodes  =  children  .  values  (  )  ; 
for  (  Iterator   it  =  nodes  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
(  (  Node  )  it  .  next  (  )  )  .  print  (  sb  ,  indent  )  ; 
sb  .  append  (  '\n'  )  ; 
} 
}  else   sb  .  append  (  SEPARATOR  )  ; 
return   sb  .  toString  (  )  ; 
} 





public   String   getGroupName  (  )  { 
return   groupname  ; 
} 





public   Channel   getChannel  (  )  { 
return   channel  ; 
} 





public   int   getGroupMembersNumber  (  )  { 
return   members  .  size  (  )  ; 
} 

public   void   _put  (  String   fqn  ,  HashMap   data  )  { 
Node   n  ; 
StringHolder   child_name  =  new   StringHolder  (  )  ; 
boolean   child_exists  =  false  ; 
if  (  fqn  ==  null  )  return  ; 
n  =  findParentNode  (  fqn  ,  child_name  ,  true  )  ; 
if  (  child_name  .  getValue  (  )  !=  null  )  { 
child_exists  =  n  .  childExists  (  child_name  .  getValue  (  )  )  ; 
n  .  createChild  (  child_name  .  getValue  (  )  ,  fqn  ,  n  ,  data  )  ; 
}  else  { 
child_exists  =  true  ; 
n  .  setData  (  data  )  ; 
} 
if  (  child_exists  )  notifyNodeModified  (  fqn  )  ;  else   notifyNodeAdded  (  fqn  )  ; 
} 

public   void   _put  (  String   fqn  ,  String   key  ,  Object   value  )  { 
Node   n  ; 
StringHolder   child_name  =  new   StringHolder  (  )  ; 
boolean   child_exists  =  false  ; 
if  (  fqn  ==  null  ||  key  ==  null  ||  value  ==  null  )  return  ; 
n  =  findParentNode  (  fqn  ,  child_name  ,  true  )  ; 
if  (  child_name  .  getValue  (  )  !=  null  )  { 
child_exists  =  n  .  childExists  (  child_name  .  getValue  (  )  )  ; 
n  .  createChild  (  child_name  .  getValue  (  )  ,  fqn  ,  n  ,  key  ,  value  )  ; 
}  else  { 
child_exists  =  true  ; 
n  .  setData  (  key  ,  value  )  ; 
} 
if  (  child_exists  )  notifyNodeModified  (  fqn  )  ;  else   notifyNodeAdded  (  fqn  )  ; 
} 

public   void   _remove  (  String   fqn  )  { 
Node   n  ; 
StringHolder   child_name  =  new   StringHolder  (  )  ; 
if  (  fqn  ==  null  )  return  ; 
if  (  fqn  .  equals  (  SEPARATOR  )  )  { 
root  .  removeAll  (  )  ; 
notifyNodeRemoved  (  fqn  )  ; 
return  ; 
} 
n  =  findParentNode  (  fqn  ,  child_name  ,  false  )  ; 
if  (  n  ==  null  )  return  ; 
n  .  removeChild  (  child_name  .  getValue  (  )  ,  fqn  )  ; 
notifyNodeRemoved  (  fqn  )  ; 
} 

public   void   _remove  (  String   fqn  ,  String   key  )  { 
Node   n  ; 
if  (  fqn  ==  null  ||  key  ==  null  )  return  ; 
n  =  findNode  (  fqn  )  ; 
if  (  n  !=  null  )  n  .  removeData  (  key  )  ; 
} 

public   void   _removeData  (  String   fqn  )  { 
Node   n  ; 
if  (  fqn  ==  null  )  return  ; 
n  =  findNode  (  fqn  )  ; 
if  (  n  !=  null  )  n  .  removeData  (  )  ; 
} 


public   void   receive  (  Message   msg  )  { 
Request   req  =  null  ; 
if  (  msg  ==  null  ||  msg  .  getLength  (  )  ==  0  )  return  ; 
try  { 
req  =  (  Request  )  msg  .  getObject  (  )  ; 
String   fqn  =  req  .  fqn  ; 
switch  (  req  .  type  )  { 
case   Request  .  PUT  : 
if  (  req  .  key  !=  null  &&  req  .  value  !=  null  )  _put  (  fqn  ,  req  .  key  ,  req  .  value  )  ;  else   _put  (  fqn  ,  req  .  data  )  ; 
break  ; 
case   Request  .  REMOVE  : 
if  (  req  .  key  !=  null  )  _remove  (  fqn  ,  req  .  key  )  ;  else   _remove  (  fqn  )  ; 
break  ; 
default  : 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "type "  +  req  .  type  +  " unknown"  )  ; 
break  ; 
} 
}  catch  (  Exception   ex  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "failed unmarshalling request: "  +  ex  )  ; 
} 
} 


public   byte  [  ]  getState  (  )  { 
try  { 
return   Util  .  objectToByteBuffer  (  root  .  clone  (  )  )  ; 
}  catch  (  Throwable   ex  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "exception returning cache: "  +  ex  )  ; 
return   null  ; 
} 
} 


public   void   setState  (  byte  [  ]  new_state  )  { 
Node   new_root  =  null  ; 
Object   obj  ; 
if  (  new_state  ==  null  )  { 
if  (  log  .  isInfoEnabled  (  )  )  log  .  info  (  "new cache is null"  )  ; 
return  ; 
} 
try  { 
obj  =  Util  .  objectFromByteBuffer  (  new_state  )  ; 
new_root  =  (  Node  )  (  (  Node  )  obj  )  .  clone  (  )  ; 
root  =  new_root  ; 
notifyAllNodesCreated  (  root  )  ; 
}  catch  (  Throwable   ex  )  { 
if  (  log  .  isErrorEnabled  (  )  )  log  .  error  (  "could not set cache: "  +  ex  )  ; 
} 
} 

public   void   viewAccepted  (  View   new_view  )  { 
Vector  <  Address  >  new_mbrs  =  new_view  .  getMembers  (  )  ; 
if  (  new_mbrs  !=  null  )  { 
notifyViewChange  (  new_view  )  ; 
members  .  removeAllElements  (  )  ; 
for  (  int   i  =  0  ;  i  <  new_mbrs  .  size  (  )  ;  i  ++  )  members  .  addElement  (  new_mbrs  .  elementAt  (  i  )  )  ; 
} 
send_message  =  members  .  size  (  )  >  1  ; 
} 










Node   findParentNode  (  String   fqn  ,  StringHolder   child_name  ,  boolean   create_if_not_exists  )  { 
Node   curr  =  root  ,  node  ; 
StringTokenizer   tok  ; 
String   name  ; 
StringBuilder   sb  =  null  ; 
if  (  fqn  ==  null  ||  fqn  .  equals  (  SEPARATOR  )  ||  ""  .  equals  (  fqn  )  )  return   curr  ; 
sb  =  new   StringBuilder  (  )  ; 
tok  =  new   StringTokenizer  (  fqn  ,  SEPARATOR  )  ; 
while  (  tok  .  countTokens  (  )  >  1  )  { 
name  =  tok  .  nextToken  (  )  ; 
sb  .  append  (  SEPARATOR  )  .  append  (  name  )  ; 
node  =  curr  .  getChild  (  name  )  ; 
if  (  node  ==  null  &&  create_if_not_exists  )  node  =  curr  .  createChild  (  name  ,  sb  .  toString  (  )  ,  null  ,  null  )  ; 
if  (  node  ==  null  )  return   null  ;  else   curr  =  node  ; 
} 
if  (  tok  .  countTokens  (  )  >  0  &&  child_name  !=  null  )  child_name  .  setValue  (  tok  .  nextToken  (  )  )  ; 
return   curr  ; 
} 







Node   findNode  (  String   fqn  )  { 
StringHolder   sh  =  new   StringHolder  (  )  ; 
Node   n  =  findParentNode  (  fqn  ,  sh  ,  false  )  ; 
String   child_name  =  sh  .  getValue  (  )  ; 
if  (  fqn  ==  null  ||  fqn  .  equals  (  SEPARATOR  )  ||  ""  .  equals  (  fqn  )  )  return   root  ; 
if  (  n  ==  null  ||  child_name  ==  null  )  return   null  ;  else   return   n  .  getChild  (  child_name  )  ; 
} 

void   notifyNodeAdded  (  String   fqn  )  { 
for  (  int   i  =  0  ;  i  <  listeners  .  size  (  )  ;  i  ++  )  listeners  .  elementAt  (  i  )  .  nodeAdded  (  fqn  )  ; 
} 

void   notifyNodeRemoved  (  String   fqn  )  { 
for  (  int   i  =  0  ;  i  <  listeners  .  size  (  )  ;  i  ++  )  listeners  .  elementAt  (  i  )  .  nodeRemoved  (  fqn  )  ; 
} 

void   notifyNodeModified  (  String   fqn  )  { 
for  (  int   i  =  0  ;  i  <  listeners  .  size  (  )  ;  i  ++  )  listeners  .  elementAt  (  i  )  .  nodeModified  (  fqn  )  ; 
} 

void   notifyViewChange  (  View   v  )  { 
for  (  int   i  =  0  ;  i  <  listeners  .  size  (  )  ;  i  ++  )  listeners  .  elementAt  (  i  )  .  viewChange  (  v  )  ; 
} 



void   notifyAllNodesCreated  (  Node   curr  )  { 
Node   n  ; 
Map   children  ; 
if  (  curr  ==  null  )  return  ; 
notifyNodeAdded  (  curr  .  fqn  )  ; 
if  (  (  children  =  curr  .  getChildren  (  )  )  !=  null  )  { 
for  (  Iterator   it  =  children  .  values  (  )  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
n  =  (  Node  )  it  .  next  (  )  ; 
notifyAllNodesCreated  (  n  )  ; 
} 
} 
} 

public   static   class   Node   implements   Serializable  { 

String   name  =  null  ; 

String   fqn  =  null  ; 

Node   parent  =  null  ; 

TreeMap  <  String  ,  Node  >  children  =  null  ; 

Map  <  String  ,  Object  >  data  =  null  ; 

private   static   final   long   serialVersionUID  =  -  3077676554440038890L  ; 

private   Node  (  String   child_name  ,  String   fqn  ,  Node   parent  ,  Map  <  String  ,  Object  >  data  )  { 
name  =  child_name  ; 
this  .  fqn  =  fqn  ; 
this  .  parent  =  parent  ; 
if  (  data  !=  null  )  this  .  data  =  (  HashMap  <  String  ,  Object  >  )  (  (  HashMap  )  data  )  .  clone  (  )  ; 
} 

private   Node  (  String   child_name  ,  String   fqn  ,  Node   parent  ,  String   key  ,  Object   value  )  { 
name  =  child_name  ; 
this  .  fqn  =  fqn  ; 
this  .  parent  =  parent  ; 
if  (  data  ==  null  )  data  =  new   HashMap  <  String  ,  Object  >  (  )  ; 
data  .  put  (  key  ,  value  )  ; 
} 

void   setData  (  Map   data  )  { 
if  (  data  ==  null  )  return  ; 
if  (  this  .  data  ==  null  )  this  .  data  =  new   HashMap  <  String  ,  Object  >  (  )  ; 
this  .  data  .  putAll  (  data  )  ; 
} 

void   setData  (  String   key  ,  Object   value  )  { 
if  (  this  .  data  ==  null  )  this  .  data  =  new   HashMap  <  String  ,  Object  >  (  )  ; 
this  .  data  .  put  (  key  ,  value  )  ; 
} 

Map  <  String  ,  Object  >  getData  (  )  { 
return   data  ; 
} 

Object   getData  (  String   key  )  { 
return   data  !=  null  ?  data  .  get  (  key  )  :  null  ; 
} 

boolean   childExists  (  String   child_name  )  { 
return   child_name  !=  null  &&  children  !=  null  &&  children  .  containsKey  (  child_name  )  ; 
} 

Node   createChild  (  String   child_name  ,  String   fqn  ,  Node   parent  ,  HashMap  <  String  ,  Object  >  data  )  { 
Node   child  =  null  ; 
if  (  child_name  ==  null  )  return   null  ; 
if  (  children  ==  null  )  children  =  new   TreeMap  <  String  ,  Node  >  (  )  ; 
child  =  children  .  get  (  child_name  )  ; 
if  (  child  !=  null  )  child  .  setData  (  data  )  ;  else  { 
child  =  new   Node  (  child_name  ,  fqn  ,  parent  ,  data  )  ; 
children  .  put  (  child_name  ,  child  )  ; 
} 
return   child  ; 
} 

Node   createChild  (  String   child_name  ,  String   fqn  ,  Node   parent  ,  String   key  ,  Object   value  )  { 
Node   child  =  null  ; 
if  (  child_name  ==  null  )  return   null  ; 
if  (  children  ==  null  )  children  =  new   TreeMap  <  String  ,  Node  >  (  )  ; 
child  =  (  Node  )  children  .  get  (  child_name  )  ; 
if  (  child  !=  null  )  child  .  setData  (  key  ,  value  )  ;  else  { 
child  =  new   Node  (  child_name  ,  fqn  ,  parent  ,  key  ,  value  )  ; 
children  .  put  (  child_name  ,  child  )  ; 
} 
return   child  ; 
} 

Node   getChild  (  String   child_name  )  { 
return   child_name  ==  null  ?  null  :  children  ==  null  ?  null  :  (  Node  )  children  .  get  (  child_name  )  ; 
} 

Map  <  String  ,  Node  >  getChildren  (  )  { 
return   children  ; 
} 

void   removeData  (  String   key  )  { 
if  (  data  !=  null  )  data  .  remove  (  key  )  ; 
} 

void   removeData  (  )  { 
if  (  data  !=  null  )  data  .  clear  (  )  ; 
} 

void   removeChild  (  String   child_name  ,  String   fqn  )  { 
if  (  child_name  !=  null  &&  children  !=  null  &&  children  .  containsKey  (  child_name  )  )  { 
children  .  remove  (  child_name  )  ; 
} 
} 

void   removeAll  (  )  { 
if  (  children  !=  null  )  children  .  clear  (  )  ; 
} 

void   print  (  StringBuilder   sb  ,  int   indent  )  { 
printIndent  (  sb  ,  indent  )  ; 
sb  .  append  (  SEPARATOR  )  .  append  (  name  )  ; 
if  (  children  !=  null  &&  !  children  .  isEmpty  (  )  )  { 
Collection   values  =  children  .  values  (  )  ; 
for  (  Iterator   it  =  values  .  iterator  (  )  ;  it  .  hasNext  (  )  ;  )  { 
sb  .  append  (  '\n'  )  ; 
(  (  Node  )  it  .  next  (  )  )  .  print  (  sb  ,  indent  +  INDENT  )  ; 
} 
} 
} 

static   void   printIndent  (  StringBuilder   sb  ,  int   indent  )  { 
if  (  sb  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  indent  ;  i  ++  )  sb  .  append  (  ' '  )  ; 
} 
} 

public   String   toString  (  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
if  (  name  !=  null  )  sb  .  append  (  "\nname="  +  name  )  ; 
if  (  fqn  !=  null  )  sb  .  append  (  "\nfqn="  +  fqn  )  ; 
if  (  data  !=  null  )  sb  .  append  (  "\ndata="  +  data  )  ; 
return   sb  .  toString  (  )  ; 
} 

public   Object   clone  (  )  throws   CloneNotSupportedException  { 
Node   n  =  new   Node  (  name  ,  fqn  ,  parent  !=  null  ?  (  Node  )  parent  .  clone  (  )  :  null  ,  data  )  ; 
if  (  children  !=  null  )  n  .  children  =  (  TreeMap  )  children  .  clone  (  )  ; 
return   n  ; 
} 
} 

private   static   class   StringHolder  { 

String   s  =  null  ; 

private   StringHolder  (  )  { 
} 

void   setValue  (  String   s  )  { 
this  .  s  =  s  ; 
} 

String   getValue  (  )  { 
return   s  ; 
} 
} 




private   static   class   Request   implements   Serializable  { 

static   final   int   PUT  =  1  ; 

static   final   int   REMOVE  =  2  ; 

int   type  =  0  ; 

String   fqn  =  null  ; 

String   key  =  null  ; 

Object   value  =  null  ; 

HashMap   data  =  null  ; 

private   static   final   long   serialVersionUID  =  7772753222127676782L  ; 

private   Request  (  int   type  ,  String   fqn  )  { 
this  .  type  =  type  ; 
this  .  fqn  =  fqn  ; 
} 

private   Request  (  int   type  ,  String   fqn  ,  HashMap   data  )  { 
this  (  type  ,  fqn  )  ; 
this  .  data  =  data  ; 
} 

private   Request  (  int   type  ,  String   fqn  ,  String   key  )  { 
this  (  type  ,  fqn  )  ; 
this  .  key  =  key  ; 
} 

private   Request  (  int   type  ,  String   fqn  ,  String   key  ,  Object   value  )  { 
this  (  type  ,  fqn  )  ; 
this  .  key  =  key  ; 
this  .  value  =  value  ; 
} 

public   String   toString  (  )  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
sb  .  append  (  type2String  (  type  )  )  .  append  (  " ("  )  ; 
if  (  fqn  !=  null  )  sb  .  append  (  " fqn="  +  fqn  )  ; 
switch  (  type  )  { 
case   PUT  : 
if  (  data  !=  null  )  sb  .  append  (  ", data="  +  data  )  ; 
if  (  key  !=  null  )  sb  .  append  (  ", key="  +  key  )  ; 
if  (  value  !=  null  )  sb  .  append  (  ", value="  +  value  )  ; 
break  ; 
case   REMOVE  : 
if  (  key  !=  null  )  sb  .  append  (  ", key="  +  key  )  ; 
break  ; 
default  : 
break  ; 
} 
sb  .  append  (  ')'  )  ; 
return   sb  .  toString  (  )  ; 
} 

static   String   type2String  (  int   t  )  { 
switch  (  t  )  { 
case   PUT  : 
return  "PUT"  ; 
case   REMOVE  : 
return  "REMOVE"  ; 
default  : 
return  "UNKNOWN"  ; 
} 
} 
} 

public   static   void   main  (  String  [  ]  args  )  { 
ReplicatedTree   tree  =  null  ; 
HashMap   m  =  new   HashMap  (  )  ; 
String   props  ; 
props  =  "UDP(mcast_addr=224.0.0.36;mcast_port=55566;ip_ttl=32;"  +  "mcast_send_buf_size=150000;mcast_recv_buf_size=80000):"  +  "PING(timeout=2000;num_initial_members=3):"  +  "MERGE2(min_interval=5000;max_interval=10000):"  +  "FD_SOCK:"  +  "VERIFY_SUSPECT(timeout=1500):"  +  "pbcast.STABLE(desired_avg_gossip=20000):"  +  "pbcast.NAKACK(gc_lag=50;retransmit_timeout=600,1200,2400,4800):"  +  "UNICAST(timeout=5000):"  +  "FRAG(frag_size=16000;down_thread=false;up_thread=false):"  +  "pbcast.GMS(join_timeout=5000;"  +  "print_local_addr=true):"  +  "pbcast.STATE_TRANSFER"  ; 
try  { 
tree  =  new   ReplicatedTree  (  null  ,  props  ,  10000  )  ; 
tree  .  addReplicatedTreeListener  (  new   MyListener  (  )  )  ; 
tree  .  put  (  "/a/b/c"  ,  null  )  ; 
tree  .  put  (  "/a/b/c1"  ,  null  )  ; 
tree  .  put  (  "/a/b/c2"  ,  null  )  ; 
tree  .  put  (  "/a/b1/chat"  ,  null  )  ; 
tree  .  put  (  "/a/b1/chat2"  ,  null  )  ; 
tree  .  put  (  "/a/b1/chat5"  ,  null  )  ; 
System  .  out  .  println  (  tree  )  ; 
m  .  put  (  "name"  ,  "Bela Ban"  )  ; 
m  .  put  (  "age"  ,  new   Integer  (  36  )  )  ; 
m  .  put  (  "cube"  ,  "240-17"  )  ; 
tree  .  put  (  "/a/b/c"  ,  m  )  ; 
System  .  out  .  println  (  "info for for \"/a/b/c\" is "  +  tree  .  print  (  "/a/b/c"  )  )  ; 
tree  .  put  (  "/a/b/c"  ,  "age"  ,  new   Integer  (  37  )  )  ; 
System  .  out  .  println  (  "info for for \"/a/b/c\" is "  +  tree  .  print  (  "/a/b/c"  )  )  ; 
tree  .  remove  (  "/a/b"  )  ; 
System  .  out  .  println  (  tree  )  ; 
}  catch  (  Exception   ex  )  { 
System  .  err  .  println  (  ex  )  ; 
} 
} 

static   class   MyListener   implements   ReplicatedTreeListener  { 

public   void   nodeAdded  (  String   fqn  )  { 
System  .  out  .  println  (  "** node added: "  +  fqn  )  ; 
} 

public   void   nodeRemoved  (  String   fqn  )  { 
System  .  out  .  println  (  "** node removed: "  +  fqn  )  ; 
} 

public   void   nodeModified  (  String   fqn  )  { 
System  .  out  .  println  (  "** node modified: "  +  fqn  )  ; 
} 

public   void   viewChange  (  View   new_view  )  { 
System  .  out  .  println  (  "** view change: "  +  new_view  )  ; 
} 
} 
} 

