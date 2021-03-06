package   edu  .  univalle  .  lingweb  .  model  ; 

import   java  .  text  .  MessageFormat  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Set  ; 
import   javax  .  persistence  .  PersistenceException  ; 
import   javax  .  persistence  .  Query  ; 
import   org  .  apache  .  log4j  .  Logger  ; 
import   org  .  apache  .  log4j  .  xml  .  DOMConfigurator  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  EntityManagerHelper  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  ToForum  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  ToForumDAO  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  ToForumPost  ; 
import   edu  .  univalle  .  lingweb  .  persistence  .  ToForumPostDAO  ; 
import   edu  .  univalle  .  lingweb  .  rest  .  RestServiceResult  ; 







public   class   DataManagerForumPost   extends   DataManager  { 






private   Logger   log  =  Logger  .  getLogger  (  DataManagerForumPost  .  class  )  ; 




public   DataManagerForumPost  (  )  { 
super  (  )  ; 
DOMConfigurator  .  configure  (  DataManagerForumPost  .  class  .  getResource  (  "/log4j.xml"  )  )  ; 
} 












public   RestServiceResult   create  (  RestServiceResult   serviceResult  ,  ToForumPost   toForumPost  )  { 
ToForumPostDAO   toForumPostDAO  =  new   ToForumPostDAO  (  )  ; 
try  { 
toForumPost  .  setForumPostId  (  getSequence  (  "sq_to_forum_post"  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
toForumPostDAO  .  save  (  toForumPost  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  toForumPost  )  ; 
log  .  info  (  "El Anuncio al foro"  +  toForumPost  .  getToForum  (  )  .  getTitle  (  )  +  " fue creado con �xito..."  )  ; 
Object  [  ]  arrayParam  =  {  toForumPost  .  getToForum  (  )  .  getTitle  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.create.success"  )  ,  arrayParam  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al guardarel foro: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.create.error"  )  ,  e  .  getMessage  (  )  )  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   update  (  RestServiceResult   serviceResult  ,  ToForumPost   toForumPost  )  { 
ToForumPostDAO   toForumPostDAO  =  new   ToForumPostDAO  (  )  ; 
try  { 
log  .  info  (  "Actualizando el anuncio foro n�mero: "  +  toForumPost  .  getForumPostId  (  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
toForumPostDAO  .  update  (  toForumPost  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  toForumPost  )  ; 
Object  [  ]  args  =  {  toForumPost  .  getForumPostId  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.update.success"  )  ,  args  )  )  ; 
log  .  info  (  "Se actualizo el foro con �xito: "  +  toForumPost  .  getForumPostId  (  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al guardar el foro: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.update.error"  )  ,  e  .  getMessage  (  )  )  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   delete  (  RestServiceResult   serviceResult  ,  ToForumPost   toForumPost  )  { 
try  { 
log  .  info  (  "Eliminando el anuncio foro: "  +  toForumPost  .  getForumPostId  (  )  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
Query   query  =  EntityManagerHelper  .  createNativeQuery  (  Statements  .  DELETE_TO_FORUM_POST  )  ; 
query  .  setParameter  (  1  ,  toForumPost  .  getForumPostId  (  )  )  ; 
query  .  executeUpdate  (  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
EntityManagerHelper  .  refresh  (  toForumPost  )  ; 
Object  [  ]  arrayParam  =  {  toForumPost  .  getForumPostId  (  )  }  ; 
log  .  info  (  "Anuncio de Foro  eliminado con �xito: "  +  toForumPost  .  getForumPostId  (  )  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.delete.success"  )  ,  arrayParam  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al eliminar el foro: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
Object  [  ]  arrayParam  =  {  toForumPost  .  getForumPostId  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.delete.error"  )  +  e  .  getMessage  (  )  ,  arrayParam  )  )  ; 
} 
return   serviceResult  ; 
} 















public   RestServiceResult   deleteMasive  (  RestServiceResult   serviceResult  ,  String   sArrayForumPostId  )  { 
try  { 
log  .  info  (  "Eliminando ANUNCIO FOROS: "  +  sArrayForumPostId  )  ; 
String   sSql  =  Statements  .  DELETE_MASIVE_NEWS  ; 
sSql  =  sSql  .  replaceFirst  (  "v1"  ,  sArrayForumPostId  )  ; 
EntityManagerHelper  .  beginTransaction  (  )  ; 
Query   query  =  EntityManagerHelper  .  createNativeQuery  (  sSql  )  ; 
int   nDeleted  =  query  .  executeUpdate  (  )  ; 
EntityManagerHelper  .  commit  (  )  ; 
Object  [  ]  arrayParam  =  {  nDeleted  }  ; 
log  .  info  (  " N�mero de ANUNCIO FOROS eliminados => "  +  nDeleted  )  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.delete.success"  )  ,  arrayParam  )  )  ; 
}  catch  (  PersistenceException   e  )  { 
EntityManagerHelper  .  rollback  (  )  ; 
log  .  error  (  "Error al eliminar el anuncio foro: "  +  e  .  getMessage  (  )  )  ; 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "forumPost.delete.error"  )  +  e  .  getMessage  (  )  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   search  (  RestServiceResult   serviceResult  ,  String   sForumPostId  )  { 
ToForumPost   toForumPost  =  new   ToForumPostDAO  (  )  .  findById  (  new   Long  (  sForumPostId  )  )  ; 
List  <  ToForumPost  >  list  =  new   ArrayList  <  ToForumPost  >  (  )  ; 
if  (  toForumPost  ==  null  )  { 
}  else  { 
list  .  add  (  toForumPost  )  ; 
} 
if  (  list  .  size  (  )  ==  0  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "forumPost.search.notFound"  )  )  ; 
}  else  { 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.search.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
} 
return   serviceResult  ; 
} 












public   RestServiceResult   search  (  RestServiceResult   serviceResult  ,  Long   nForumPostId  )  { 
ToForumPost   toForumPost  =  new   ToForumPostDAO  (  )  .  findById  (  nForumPostId  )  ; 
if  (  toForumPost  ==  null  )  { 
serviceResult  .  setError  (  true  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "forumPost.search.notFound"  )  )  ; 
}  else  { 
List  <  ToForumPost  >  list  =  new   ArrayList  <  ToForumPost  >  (  )  ; 
EntityManagerHelper  .  refresh  (  toForumPost  )  ; 
list  .  add  (  toForumPost  )  ; 
Object  [  ]  arrayParam  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.search.success"  )  ,  arrayParam  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
} 
return   serviceResult  ; 
} 










public   RestServiceResult   list  (  RestServiceResult   result  )  { 
return   list  (  result  ,  0  ,  0  )  ; 
} 










public   RestServiceResult   list  (  RestServiceResult   serviceResult  ,  int   nRowStart  ,  int   nMaxResults  )  { 
ToForumPostDAO   toForumPostDAO  =  new   ToForumPostDAO  (  )  ; 
List  <  ToForumPost  >  list  =  toForumPostDAO  .  findAll  (  nRowStart  ,  nMaxResults  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
serviceResult  .  setNumResult  (  0  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "forumPost.list.notFound"  )  )  ; 
}  else  { 
Object  [  ]  array  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.list.success"  )  ,  array  )  )  ; 
if  (  (  nRowStart  >  0  )  ||  (  nMaxResults  >  0  )  )  serviceResult  .  setNumResult  (  toForumPostDAO  .  findAll  (  )  .  size  (  )  )  ;  else   serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
serviceResult  .  setObjResult  (  list  )  ; 
return   serviceResult  ; 
} 










public   RestServiceResult   listForumPostForForum  (  RestServiceResult   result  ,  Long   nForumId  )  { 
return   listForumPostForForum  (  result  ,  0  ,  0  ,  nForumId  )  ; 
} 










public   RestServiceResult   listForumPostForForum  (  RestServiceResult   serviceResult  ,  int   nRowStart  ,  int   nMaxResults  ,  Long   nForumId  )  { 
List  <  ToForumPost  >  list  ; 
ToForum   toForum  =  new   ToForumDAO  (  )  .  findById  (  nForumId  )  ; 
EntityManagerHelper  .  refresh  (  toForum  )  ; 
Set  <  ToForumPost  >  setForumPost  =  toForum  .  getToForumPosts  (  )  ; 
list  =  new   ArrayList  <  ToForumPost  >  (  )  ; 
list  .  addAll  (  setForumPost  )  ; 
if  (  list  .  size  (  )  ==  0  )  { 
serviceResult  .  setNumResult  (  0  )  ; 
serviceResult  .  setMessage  (  bundle  .  getString  (  "forumPost.list.notFound"  )  )  ; 
}  else  { 
Object  [  ]  array  =  {  list  .  size  (  )  }  ; 
serviceResult  .  setMessage  (  MessageFormat  .  format  (  bundle  .  getString  (  "forumPost.list.success"  )  ,  array  )  )  ; 
serviceResult  .  setNumResult  (  list  .  size  (  )  )  ; 
} 
Collections  .  sort  (  list  ,  new   OrdeByForumPostId  (  )  )  ; 
serviceResult  .  setObjResult  (  list  )  ; 
return   serviceResult  ; 
} 
} 






class   OrdeByForumPostId   implements   Comparator  <  ToForumPost  >  { 

public   int   compare  (  ToForumPost   forumPost1  ,  ToForumPost   forumPost2  )  { 
return   forumPost1  .  getForumPostId  (  )  .  intValue  (  )  -  forumPost2  .  getForumPostId  (  )  .  intValue  (  )  ; 
} 
} 

