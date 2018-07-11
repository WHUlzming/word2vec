package   com  .  spring  .  rssReader  .  web  ; 

import   com  .  spring  .  rssReader  .  *  ; 
import   com  .  spring  .  rssReader  .  jdbc  .  IChannelController  ; 
import   com  .  spring  .  rssReader  .  util  .  HtmlContentParser  ; 
import   com  .  spring  .  workflow  .  WorkflowConstants  ; 
import   com  .  spring  .  workflow  .  exception  .  ResponseUsedException  ; 
import   org  .  springframework  .  context  .  ApplicationContextException  ; 
import   org  .  springframework  .  context  .  ApplicationListener  ; 
import   org  .  springframework  .  context  .  ApplicationEvent  ; 
import   org  .  springframework  .  web  .  bind  .  RequestUtils  ; 
import   org  .  springframework  .  web  .  servlet  .  ModelAndView  ; 
import   org  .  springframework  .  web  .  servlet  .  mvc  .  multiaction  .  MultiActionController  ; 
import   javax  .  servlet  .  ServletException  ; 
import   javax  .  servlet  .  http  .  HttpServletRequest  ; 
import   javax  .  servlet  .  http  .  HttpServletResponse  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  *  ; 




public   class   ItemWebController   extends   MultiActionController   implements   ApplicationListener  { 








private   static   final   String   WORKFLOW_ACTION_ZAP  =  "zapItemTag"  ; 

private   IChannelController   channelController  =  null  ; 

protected   ICategoryDao   categoryDao  ; 

private   List   formattedCategory  ; 

public   ItemWebController  (  )  throws   ApplicationContextException  { 
} 

public   IChannelController   getChannelController  (  )  { 
return   channelController  ; 
} 

public   void   setChannelController  (  IChannelController   channelController  )  { 
this  .  channelController  =  channelController  ; 
} 

public   ICategoryDao   getCategoryDao  (  )  { 
return   categoryDao  ; 
} 

public   void   setCategoryDao  (  ICategoryDao   categoryDao  )  { 
this  .  categoryDao  =  categoryDao  ; 
} 

private   List   getCategory  (  )  { 
if  (  this  .  formattedCategory  ==  null  )  { 
Map   formats  =  (  new   FormattedCategory  (  getCategoryDao  (  )  .  load  (  )  )  )  .  format  (  )  ; 
formattedCategory  =  new   ArrayList  (  formats  .  values  (  )  )  ; 
} 
return   this  .  formattedCategory  ; 
} 

public   ModelAndView   getItems  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  { 
Channel   channel  =  getActiveChannel  (  request  )  ; 
ModelAndView   view  =  new   ModelAndView  (  "read"  )  ; 
if  (  showAll  (  request  )  )  { 
view  .  addObject  (  "items"  ,  channelController  .  getAllItems  (  channel  .  getId  (  )  )  )  ; 
view  .  addObject  (  "toggleShowAll"  ,  "Show new items"  )  ; 
}  else  { 
view  .  addObject  (  "items"  ,  channelController  .  getNewItems  (  channel  .  getId  (  )  )  )  ; 
view  .  addObject  (  "toggleShowAll"  ,  "Show all items"  )  ; 
} 
if  (  request  .  getParameter  (  "showWithoutTable"  )  !=  null  )  { 
view  .  addObject  (  "showWithoutTable"  ,  "1"  )  ; 
} 
view  .  addObject  (  "categories"  ,  this  .  getCategory  (  )  )  ; 
return   view  ; 
} 

private   boolean   showAll  (  HttpServletRequest   request  )  { 
boolean   showAll  =  true  ; 
if  (  request  .  getSession  (  )  .  getAttribute  (  "showAll"  )  !=  null  )  { 
showAll  =  request  .  getSession  (  )  .  getAttribute  (  "showAll"  )  .  equals  (  "1"  )  ; 
} 
return   showAll  ; 
} 

public   ModelAndView   toggleAllItems  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  { 
request  .  getSession  (  )  .  setAttribute  (  "showAll"  ,  (  showAll  (  request  )  ?  "0"  :  "1"  )  )  ; 
return   new   ModelAndView  (  ""  )  ; 
} 

public   ModelAndView   deleteAllItems  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  { 
Channel   channel  =  getActiveChannel  (  request  )  ; 
List   itemsToDelete  =  new   ArrayList  (  )  ; 
if  (  showAll  (  request  )  )  { 
itemsToDelete  =  channelController  .  getAllItems  (  channel  .  getId  (  )  )  ; 
}  else  { 
itemsToDelete  =  channelController  .  getNewItems  (  channel  .  getId  (  )  )  ; 
} 
channelController  .  deleteItems  (  itemsToDelete  )  ; 
channelController  .  markAsRead  (  channel  .  getId  (  )  )  ; 
return   new   ModelAndView  (  ""  )  ; 
} 

private   Channel   getActiveChannel  (  HttpServletRequest   request  )  { 
Channel   channel  =  (  Channel  )  request  .  getSession  (  )  .  getAttribute  (  ChannelWebController  .  ACTIVE_CHANNEL  )  ; 
if  (  channel  !=  null  )  { 
channel  =  channelController  .  getChannel  (  channel  .  getId  (  )  )  ; 
} 
return   channel  ; 
} 

public   ModelAndView   deleteItem  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  { 
Long   itemId  =  new   Long  (  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  )  ; 
channelController  .  deleteItem  (  itemId  )  ; 
return   new   ModelAndView  (  ""  ,  "message"  ,  "item.deleted"  )  ; 
} 

public   ModelAndView   deleteManyItems  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  { 
Enumeration   enumeration  =  request  .  getParameterNames  (  )  ; 
List   itemsToDelete  =  new   ArrayList  (  )  ; 
while  (  enumeration  .  hasMoreElements  (  )  )  { 
String   key  =  (  String  )  enumeration  .  nextElement  (  )  ; 
if  (  key  .  startsWith  (  "delete"  )  )  { 
itemsToDelete  .  add  (  channelController  .  getItem  (  Long  .  valueOf  (  key  .  substring  (  6  )  )  )  )  ; 
} 
} 
channelController  .  deleteItems  (  itemsToDelete  )  ; 
return   new   ModelAndView  (  ""  ,  "message"  ,  "items.deleted"  )  ; 
} 

public   ModelAndView   getItemAsHTML  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  channelController  .  getItem  (  new   Long  (  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  )  )  ; 
Channel   channel  =  channelController  .  getChannel  (  item  .  getChannelID  (  )  )  ; 
Item   itemToFetch  =  new   Item  (  )  ; 
itemToFetch  .  setPostedDate  (  item  .  getPostedDate  (  )  )  ; 
itemToFetch  .  setChannel  (  channel  )  ; 
itemToFetch  .  setUrl  (  item  .  getUrl  (  )  )  ; 
itemToFetch  .  getItemAsHTML  (  )  ; 
itemToFetch  .  setTitle  (  item  .  getTitle  (  )  )  ; 
itemToFetch  .  setFetched  (  true  )  ; 
channelController  .  update  (  channel  ,  itemToFetch  )  ; 
return   new   ModelAndView  (  ""  )  ; 
} 










public   ModelAndView   fetchUrl  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  new   Item  (  )  ; 
item  .  setUrl  (  request  .  getParameter  (  "fetchUrlString"  )  )  ; 
item  .  setChannel  (  getActiveChannel  (  request  )  )  ; 
item  .  getItemAsHTML  (  )  ; 
item  .  setFetched  (  true  )  ; 
channelController  .  update  (  getActiveChannel  (  request  )  ,  item  )  ; 
return   new   ModelAndView  (  ""  )  ; 
} 












public   ModelAndView   boostItem  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  channelController  .  getItem  (  new   Long  (  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  )  )  ; 
item  .  setPreferance  (  1  )  ; 
channelController  .  update  (  getActiveChannel  (  request  )  ,  item  )  ; 
response  .  setStatus  (  204  )  ; 
throw   new   ResponseUsedException  (  )  ; 
} 

public   ModelAndView   markItemAsRead  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  channelController  .  getItem  (  new   Long  (  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  )  )  ; 
channelController  .  markItemRead  (  item  ,  true  )  ; 
return   new   ModelAndView  (  ""  )  ; 
} 

public   ModelAndView   markItemAsNotRead  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  channelController  .  getItem  (  new   Long  (  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  )  )  ; 
channelController  .  markItemRead  (  item  ,  false  )  ; 
return   new   ModelAndView  (  ""  )  ; 
} 











public   ModelAndView   setActiveItem  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  channelController  .  getItem  (  new   Long  (  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  )  )  ; 
request  .  getSession  (  )  .  setAttribute  (  "activeItemID"  ,  item  .  getId  (  )  )  ; 
request  .  getSession  (  )  .  removeAttribute  (  "deletedItems"  )  ; 
return   new   ModelAndView  (  ""  )  ; 
} 






private   Item   getActiveItem  (  HttpServletRequest   request  )  { 
Long   itemId  =  (  Long  )  request  .  getSession  (  )  .  getAttribute  (  "activeItemID"  )  ; 
if  (  itemId  !=  null  )  { 
return   channelController  .  getItem  (  itemId  )  ; 
} 
return   null  ; 
} 

public   ModelAndView   editItem  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  channelController  .  getItem  (  new   Long  (  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  )  )  ; 
return   new   ModelAndView  (  ""  ,  "item"  ,  item  )  ; 
} 

public   ModelAndView   saveEditedItem  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  channelController  .  getItem  (  new   Long  (  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  )  )  ; 
item  .  setDescription  (  request  .  getParameter  (  "description"  )  )  ; 
channelController  .  update  (  item  .  getChannel  (  )  ,  item  )  ; 
return   new   ModelAndView  (  ""  ,  "message"  ,  "message.updated"  )  ; 
} 

public   ModelAndView   setCategoryAtItem  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
long   itemId  =  RequestUtils  .  getRequiredLongParameter  (  request  ,  WorkflowConstants  .  ID_NAME  )  ; 
Item   item  =  channelController  .  getItem  (  new   Long  (  itemId  )  )  ; 
List   choosenCategories  =  new   ArrayList  (  )  ; 
String  [  ]  categories  =  request  .  getParameterValues  (  "categories_"  +  itemId  )  ; 
for  (  int   i  =  0  ;  i  <  categories  .  length  ;  i  ++  )  { 
if  (  categories  [  i  ]  .  equals  (  "-1"  )  )  { 
continue  ; 
} 
choosenCategories  .  add  (  getCategoryDao  (  )  .  load  (  new   Long  (  categories  [  i  ]  )  )  )  ; 
} 
item  .  setCategories  (  choosenCategories  )  ; 
channelController  .  update  (  getActiveChannel  (  request  )  ,  item  )  ; 
response  .  setStatus  (  204  )  ; 
throw   new   ResponseUsedException  (  )  ; 
} 

public   ModelAndView   setCategoryAtChannel  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Channel   channel  =  getActiveChannel  (  request  )  ; 
List   choosenCategories  =  new   ArrayList  (  )  ; 
String  [  ]  categories  =  request  .  getParameterValues  (  "categorieChannel"  )  ; 
for  (  int   i  =  0  ;  i  <  categories  .  length  ;  i  ++  )  { 
if  (  categories  [  i  ]  .  equals  (  "-1"  )  )  { 
continue  ; 
} 
choosenCategories  .  add  (  getCategoryDao  (  )  .  load  (  new   Long  (  categories  [  i  ]  )  )  )  ; 
} 
channel  .  setCategories  (  choosenCategories  )  ; 
channelController  .  update  (  channel  )  ; 
response  .  setStatus  (  204  )  ; 
throw   new   ResponseUsedException  (  )  ; 
} 











public   ModelAndView   showItemContent  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  getActiveItem  (  request  )  ; 
Stack   deletedItems  =  getDeletedTags  (  request  )  ; 
HtmlContentParser   contentParser  =  new   HtmlContentParser  (  )  ; 
contentParser  .  setAddZapMethod  (  true  )  ; 
String   content  =  contentParser  .  getContent  (  item  .  getDescription  (  )  ,  deletedItems  )  ; 
ModelAndView   modelAndView  =  new   ModelAndView  (  ""  )  ; 
modelAndView  .  addObject  (  "content"  ,  content  )  ; 
modelAndView  .  addObject  (  "item"  ,  item  )  ; 
return   modelAndView  ; 
} 

public   ModelAndView   zapItemTag  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Stack   deletedItems  =  getDeletedTags  (  request  )  ; 
if  (  request  .  getParameter  (  WorkflowConstants  .  ID_NAME  )  !=  null  )  { 
deletedItems  .  push  (  request  .  getParameter  (  WorkflowConstants  .  ID_NAME  )  )  ; 
request  .  getSession  (  )  .  setAttribute  (  "deletedItems"  ,  deletedItems  )  ; 
} 
return   showItemContent  (  request  ,  response  )  ; 
} 

public   ModelAndView   undoZapItemTag  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Stack   deletedItems  =  getDeletedTags  (  request  )  ; 
if  (  request  .  getParameter  (  WorkflowConstants  .  ID_NAME  )  !=  null  )  { 
if  (  !  deletedItems  .  isEmpty  (  )  )  { 
deletedItems  .  pop  (  )  ; 
} 
request  .  getSession  (  )  .  setAttribute  (  "deletedItems"  ,  deletedItems  )  ; 
} 
return   showItemContent  (  request  ,  response  )  ; 
} 

public   ModelAndView   saveZappedItem  (  HttpServletRequest   request  ,  HttpServletResponse   response  )  throws   ServletException  ,  IOException  { 
Item   item  =  getActiveItem  (  request  )  ; 
Stack   deletedItems  =  getDeletedTags  (  request  )  ; 
HtmlContentParser   contentParser  =  new   HtmlContentParser  (  )  ; 
contentParser  .  setAddZapMethod  (  false  )  ; 
String   content  =  contentParser  .  getContent  (  item  .  getDescription  (  )  ,  deletedItems  )  ; 
item  .  setDescription  (  content  )  ; 
channelController  .  update  (  channelController  .  getChannel  (  item  .  getChannelID  (  )  )  ,  item  )  ; 
ModelAndView   modelAndView  =  new   ModelAndView  (  ""  )  ; 
modelAndView  .  addObject  (  "message"  ,  "item.saved"  )  ; 
return   modelAndView  ; 
} 

private   Stack   getDeletedTags  (  HttpServletRequest   request  )  { 
Stack   deletedItems  =  (  Stack  )  request  .  getSession  (  )  .  getAttribute  (  "deletedItems"  )  ; 
if  (  deletedItems  ==  null  )  { 
deletedItems  =  new   Stack  (  )  ; 
} 
return   deletedItems  ; 
} 

public   void   onApplicationEvent  (  ApplicationEvent   event  )  { 
if  (  event   instanceof   CategoryDaoChangedEvent  )  { 
this  .  formattedCategory  =  null  ; 
} 
} 
} 

