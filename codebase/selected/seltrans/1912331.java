package   org  .  omg  .  CosEventComm  ; 

public   abstract   class   PullConsumerPOA   extends   org  .  omg  .  PortableServer  .  Servant   implements   org  .  omg  .  CORBA  .  portable  .  InvokeHandler  ,  PullConsumerOperations  { 

static   final   String  [  ]  _ob_ids_  =  {  "IDL:omg.org/CosEventComm/PullConsumer:1.0"  }  ; 

public   PullConsumer   _this  (  )  { 
return   PullConsumerHelper  .  narrow  (  super  .  _this_object  (  )  )  ; 
} 

public   PullConsumer   _this  (  org  .  omg  .  CORBA  .  ORB   orb  )  { 
return   PullConsumerHelper  .  narrow  (  super  .  _this_object  (  orb  )  )  ; 
} 

public   String  [  ]  _all_interfaces  (  org  .  omg  .  PortableServer  .  POA   poa  ,  byte  [  ]  objectId  )  { 
return   _ob_ids_  ; 
} 

public   org  .  omg  .  CORBA  .  portable  .  OutputStream   _invoke  (  String   opName  ,  org  .  omg  .  CORBA  .  portable  .  InputStream   in  ,  org  .  omg  .  CORBA  .  portable  .  ResponseHandler   handler  )  { 
final   String  [  ]  _ob_names  =  {  "disconnect_pull_consumer"  }  ; 
int   _ob_left  =  0  ; 
int   _ob_right  =  _ob_names  .  length  ; 
int   _ob_index  =  -  1  ; 
while  (  _ob_left  <  _ob_right  )  { 
int   _ob_m  =  (  _ob_left  +  _ob_right  )  /  2  ; 
int   _ob_res  =  _ob_names  [  _ob_m  ]  .  compareTo  (  opName  )  ; 
if  (  _ob_res  ==  0  )  { 
_ob_index  =  _ob_m  ; 
break  ; 
}  else   if  (  _ob_res  >  0  )  _ob_right  =  _ob_m  ;  else   _ob_left  =  _ob_m  +  1  ; 
} 
switch  (  _ob_index  )  { 
case   0  : 
return   _OB_op_disconnect_pull_consumer  (  in  ,  handler  )  ; 
} 
throw   new   org  .  omg  .  CORBA  .  BAD_OPERATION  (  )  ; 
} 

private   org  .  omg  .  CORBA  .  portable  .  OutputStream   _OB_op_disconnect_pull_consumer  (  org  .  omg  .  CORBA  .  portable  .  InputStream   in  ,  org  .  omg  .  CORBA  .  portable  .  ResponseHandler   handler  )  { 
org  .  omg  .  CORBA  .  portable  .  OutputStream   out  =  null  ; 
disconnect_pull_consumer  (  )  ; 
out  =  handler  .  createReply  (  )  ; 
return   out  ; 
} 
} 

