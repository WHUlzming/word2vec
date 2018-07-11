package   ces  .  platform  .  infoplat  .  ui  .  workflow  .  collect  ; 

import   java  .  util  .  Hashtable  ; 
import   ces  .  coral  .  log  .  *  ; 
import   ces  .  platform  .  infoplat  .  core  .  *  ; 
import   ces  .  platform  .  infoplat  .  core  .  base  .  *  ; 
import   ces  .  platform  .  infoplat  .  ui  .  common  .  *  ; 









public   class   ColletTreeAuthority   extends   TreeNodeAuthority  { 

private   Logger   log  =  new   Logger  (  getClass  (  )  )  ; 

private   static   TreeNodeAuthority   authority  ; 

private   ColletTreeAuthority  (  )  { 
} 

public   boolean   hasPermission  (  Hashtable   extrAuths  )  throws   Exception  { 
if  (  operates  ==  null  )  { 
return   false  ; 
} 
boolean   result  =  true  ; 
try  { 
Pepodom   pepodom  =  new   Pepodom  (  )  ; 
for  (  int   i  =  0  ;  i  <  operates  .  length  ;  i  ++  )  { 
pepodom  .  setLoginProvider  (  userId  )  ; 
int   resId  =  0  ; 
if  (  treeNode  .  getType  (  )  .  equalsIgnoreCase  (  "site"  )  )  { 
resId  =  Const  .  SITE_TYPE_RES  +  (  (  Site  )  treeNode  )  .  getSiteID  (  )  ; 
}  else   if  (  treeNode  .  getType  (  )  .  equalsIgnoreCase  (  "type"  )  )  { 
DocType   docType  =  (  DocType  )  treeNode  ; 
if  (  docType  .  getProcessId  (  )  !=  processId  )  { 
result  =  false  ; 
break  ; 
} 
resId  =  Const  .  DOC_TYPE_RES  +  (  (  DocType  )  treeNode  )  .  getDocTypeID  (  )  ; 
}  else  { 
resId  =  Const  .  CHANNEL_TYPE_RES  +  (  (  Channel  )  treeNode  )  .  getChannelID  (  )  ; 
} 
pepodom  .  setResID  (  Integer  .  toString  (  resId  )  )  ; 
pepodom  .  setOperateID  (  operates  [  i  ]  )  ; 
if  (  !  pepodom  .  isDisplay  (  extrAuths  )  )  { 
result  =  false  ; 
break  ; 
} 
} 
}  catch  (  Exception   ex  )  { 
log  .  error  (  "��ѯ����Ȩ��ʧ��!"  ,  ex  )  ; 
throw   ex  ; 
} 
return   result  ; 
} 





public   static   TreeNodeAuthority   getInstance  (  )  { 
if  (  authority  ==  null  )  { 
authority  =  new   ColletTreeAuthority  (  )  ; 
} 
return   authority  ; 
} 
} 

