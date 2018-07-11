package   de  .  nava  .  informa  .  impl  .  hibernate  ; 

import   java  .  util  .  Collection  ; 
import   java  .  util  .  Iterator  ; 
import   org  .  hibernate  .  HibernateException  ; 
import   org  .  apache  .  commons  .  logging  .  Log  ; 
import   org  .  apache  .  commons  .  logging  .  LogFactory  ; 
import   de  .  nava  .  informa  .  utils  .  InformaTestCase  ; 
import   de  .  nava  .  informa  .  utils  .  PersistChanGrpMgr  ; 
import   de  .  nava  .  informa  .  utils  .  PersistChanGrpMgrTask  ; 
import   de  .  nava  .  informa  .  utils  .  RssUrlTestData  ; 






public   class   TestPersistChannelGrp   extends   InformaTestCase  { 

private   SessionHandler   handler  ; 

private   static   Log   logger  =  LogFactory  .  getLog  (  TestPersistChannelGrp  .  class  )  ; 






public   TestPersistChannelGrp  (  String   name  )  { 
super  (  "TestPersistChannelGrp"  ,  name  )  ; 
} 






protected   void   setUp  (  )  throws   HibernateException  { 
handler  =  SessionHandler  .  getInstance  (  )  ; 
handler  .  getSession  (  )  ; 
} 







public   void   testEmptyGroups  (  )  throws   Exception  { 
PersistChanGrpMgr   agroup  ,  bgroup  ,  cgroup  ; 
agroup  =  makeEmptyGroup  (  "Group Foo"  )  ; 
bgroup  =  makeEmptyGroup  (  "Group Bar"  )  ; 
cgroup  =  makeEmptyGroup  (  "Group Too"  )  ; 
assertValidGroup  (  agroup  ,  "Group Foo"  ,  0  )  ; 
assertValidGroup  (  bgroup  ,  "Group Bar"  ,  0  )  ; 
assertValidGroup  (  cgroup  ,  "Group Too"  ,  0  )  ; 
} 






public   void   testGroupWithChannels  (  )  throws   Exception  { 
PersistChanGrpMgr   agroup  ; 
agroup  =  makeEmptyGroup  (  "Group Secret With Channels"  )  ; 
assertValidGroup  (  agroup  ,  "Group Secret With Channels"  ,  0  )  ; 
addChannel  (  agroup  ,  "Joho"  ,  "http://www.hyperorg.com/blogger/index.rdf"  )  ; 
addChannel  (  agroup  ,  "Raliable"  ,  "http://www.raibledesigns.com/rss/rd"  )  ; 
addChannel  (  agroup  ,  "Active Window"  ,  "http://www.activewin.com/awin/headlines.rss"  )  ; 
addChannel  (  agroup  ,  "Pitos Blog"  ,  "http://radio.weblogs.com/0125664/rss.xml"  )  ; 
runPersistChanGrpTask  (  agroup  ,  3  )  ; 
assertValidGroup  (  agroup  ,  "Group Secret With Channels"  ,  4  )  ; 
} 







public   void   testAddingExistingChannel  (  )  throws   Exception  { 
final   PersistChanGrpMgr   agroup  ; 
agroup  =  makeEmptyGroup  (  "A Group"  )  ; 
assertValidGroup  (  agroup  ,  "A Group"  ,  0  )  ; 
addGenChannels  (  agroup  ,  5  ,  3  )  ; 
final   PersistChanGrpMgr   bgroup  ; 
bgroup  =  makeEmptyGroup  (  "B Group"  )  ; 
assertValidGroup  (  bgroup  ,  "B Group"  ,  0  )  ; 
addGenChannels  (  bgroup  ,  5  ,  3  )  ; 
addChannel  (  agroup  ,  "Terrestial"  ,  "http://www.transterrestrial.com/index.xml"  )  ; 
assertValidGroup  (  agroup  ,  "A Group"  ,  6  )  ; 
addChannel  (  bgroup  ,  "Terrestial"  ,  "http://www.transterrestrial.com/index.xml"  )  ; 
assertValidGroup  (  bgroup  ,  "B Group"  ,  6  )  ; 
} 






public   void   testChannelMembership  (  )  throws   Exception  { 
final   PersistChanGrpMgr   agroup  ; 
agroup  =  makeEmptyGroup  (  "Membership Group"  )  ; 
addGenChannels  (  agroup  ,  3  ,  3  )  ; 
final   Iterator   channels  =  agroup  .  getChannelGroup  (  )  .  getChannels  (  )  .  iterator  (  )  ; 
while  (  channels  .  hasNext  (  )  )  { 
final   Channel   nextchan  =  (  Channel  )  channels  .  next  (  )  ; 
assertTrue  (  "Expected member channel not found"  ,  agroup  .  hasChannel  (  nextchan  )  )  ; 
} 
} 






public   void   testGroupDeactivate  (  )  throws   Exception  { 
final   PersistChanGrpMgr   group1  ,  group2  ,  group3  ; 
group1  =  makeEmptyGroup  (  "Activation Group 1"  )  ; 
group2  =  makeEmptyGroup  (  "Activation Group 2"  )  ; 
group3  =  makeEmptyGroup  (  "Activation Group 3"  )  ; 
addGenChannels  (  group1  ,  5  ,  3  )  ; 
addGenChannels  (  group2  ,  5  ,  3  )  ; 
addGenChannels  (  group3  ,  5  ,  3  )  ; 
assertFalse  (  "expected group 1 to be inactive"  ,  group1  .  isActivated  (  )  )  ; 
assertFalse  (  "expected group 2 to be inactive"  ,  group2  .  isActivated  (  )  )  ; 
assertFalse  (  "expected group 3 to be inactive"  ,  group3  .  isActivated  (  )  )  ; 
group1  .  activate  (  )  ; 
assertTrue  (  "expected group 1 to be active"  ,  group1  .  isActivated  (  )  )  ; 
assertFalse  (  "expected group 2 to be inactive"  ,  group2  .  isActivated  (  )  )  ; 
assertFalse  (  "expected group 3 to be inactive"  ,  group3  .  isActivated  (  )  )  ; 
Thread  .  sleep  (  500  )  ; 
group2  .  activate  (  )  ; 
assertTrue  (  "expected group 1 to be active"  ,  group1  .  isActivated  (  )  )  ; 
assertTrue  (  "expected group 2 to be active"  ,  group2  .  isActivated  (  )  )  ; 
assertFalse  (  "expected group 3 to be inactive"  ,  group3  .  isActivated  (  )  )  ; 
Thread  .  sleep  (  500  )  ; 
group3  .  activate  (  )  ; 
assertTrue  (  "expected group 1 to be active"  ,  group1  .  isActivated  (  )  )  ; 
assertTrue  (  "expected group 2 to be active"  ,  group2  .  isActivated  (  )  )  ; 
assertTrue  (  "expected group 3 to be active"  ,  group3  .  isActivated  (  )  )  ; 
Thread  .  sleep  (  500  )  ; 
group1  .  deActivate  (  true  )  ; 
assertFalse  (  "expected group 1 to be inactive"  ,  group1  .  isActivated  (  )  )  ; 
assertTrue  (  "expected group 2 to be active"  ,  group2  .  isActivated  (  )  )  ; 
assertTrue  (  "expected group 3 to be active"  ,  group3  .  isActivated  (  )  )  ; 
Thread  .  sleep  (  500  )  ; 
group2  .  deActivate  (  true  )  ; 
assertFalse  (  "expected group 1 to be inactive"  ,  group1  .  isActivated  (  )  )  ; 
assertFalse  (  "expected group 2 to be inactive"  ,  group2  .  isActivated  (  )  )  ; 
assertTrue  (  "expected group 3 to be active"  ,  group3  .  isActivated  (  )  )  ; 
Thread  .  sleep  (  500  )  ; 
group3  .  deActivate  (  true  )  ; 
assertFalse  (  "expected group 1 to be inactive"  ,  group1  .  isActivated  (  )  )  ; 
assertFalse  (  "expected group 2 to be inactive"  ,  group2  .  isActivated  (  )  )  ; 
assertFalse  (  "expected group 3 to be inactive"  ,  group3  .  isActivated  (  )  )  ; 
} 






public   void   testMoveChannelBetweenGroups  (  )  throws   Exception  { 
PersistChanGrpMgr   sourceGrp  ,  destGrp  ; 
sourceGrp  =  makeEmptyGroup  (  "Source Group"  )  ; 
destGrp  =  makeEmptyGroup  (  "Destination Group"  )  ; 
int   count  =  5  ; 
int   sourceHas  =  count  ; 
int   destHas  =  0  ; 
addGenChannels  (  sourceGrp  ,  count  ,  3  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  { 
assertValidGroup  (  sourceGrp  ,  "Source Group"  ,  sourceHas  -  i  )  ; 
assertValidGroup  (  destGrp  ,  "Destination Group"  ,  destHas  +  i  )  ; 
Channel   channelToMove  ; 
Collection   chans  =  sourceGrp  .  getChannelGroup  (  )  .  getChannels  (  )  ; 
Iterator   iter  =  chans  .  iterator  (  )  ; 
assertTrue  (  chans  .  size  (  )  >  0  )  ; 
channelToMove  =  (  Channel  )  iter  .  next  (  )  ; 
sourceGrp  .  moveChannelTo  (  channelToMove  ,  destGrp  )  ; 
} 
} 






public   void   testDeleteItemsFromChannels  (  )  throws   Exception  { 
PersistChanGrpMgr   delGrp  ; 
delGrp  =  makeEmptyGroup  (  "deleteTest Group"  )  ; 
int   count  =  10  ; 
addGenChannels  (  delGrp  ,  count  ,  5  )  ; 
Object  [  ]  channels  =  delGrp  .  getChannelGroup  (  )  .  getChannels  (  )  .  toArray  (  )  ; 
assertEquals  (  "Wrong number of channels in group"  ,  channels  .  length  ,  count  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  { 
Channel   aChan  =  (  Channel  )  channels  [  i  ]  ; 
Object  [  ]  items  =  aChan  .  getItems  (  )  .  toArray  (  )  ; 
int   howManyItems  =  items  .  length  ; 
for  (  int   j  =  0  ;  j  <  howManyItems  ;  j  ++  )  { 
Item   anItem  =  (  Item  )  items  [  j  ]  ; 
assertEquals  (  "Wrong number of items after delete"  ,  howManyItems  -  j  ,  aChan  .  getItems  (  )  .  size  (  )  )  ; 
delGrp  .  deleteItemFromChannel  (  aChan  ,  anItem  )  ; 
} 
} 
} 







public   void   testRemoveChannelsFromGroup  (  )  throws   Exception  { 
PersistChanGrpMgr   theGrp  ; 
int   count  =  5  ; 
theGrp  =  makeEmptyGroup  (  "The Group"  )  ; 
addGenChannels  (  theGrp  ,  count  ,  5  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  { 
ChannelBuilder   bld  =  theGrp  .  getBuilder  (  )  ; 
bld  .  beginTransaction  (  )  ; 
bld  .  update  (  theGrp  .  getChannelGroup  (  )  )  ; 
assertValidGroup  (  theGrp  ,  "The Group"  ,  count  -  i  )  ; 
Channel   channelToDelete  ; 
Collection   chans  =  theGrp  .  getChannelGroup  (  )  .  getChannels  (  )  ; 
Iterator   iter  =  chans  .  iterator  (  )  ; 
assertTrue  (  chans  .  size  (  )  >  0  )  ; 
channelToDelete  =  (  Channel  )  iter  .  next  (  )  ; 
bld  .  endTransaction  (  )  ; 
theGrp  .  deleteChannel  (  channelToDelete  )  ; 
} 
} 






public   void   testStartStopGroup  (  )  throws   Exception  { 
PersistChanGrpMgr   groupStopStart  ; 
groupStopStart  =  makeEmptyGroup  (  "stopStartGroup"  )  ; 
for  (  int   i  =  0  ;  i  <  2  ;  i  ++  )  { 
groupStopStart  .  activate  (  )  ; 
assertTrue  (  "Channel Group is supposed to be activated"  ,  groupStopStart  .  isActivated  (  )  )  ; 
groupStopStart  .  deActivate  (  true  )  ; 
assertFalse  (  "ChannelGroup is not supposed to be activated"  ,  groupStopStart  .  isActivated  (  )  )  ; 
Thread  .  sleep  (  500  )  ; 
} 
} 






public   void   testItemCount  (  )  throws   Exception  { 
PersistChanGrpMgr   countedGrp  ; 
countedGrp  =  makeEmptyGroup  (  "Item Count Group"  )  ; 
int   count  =  10  ; 
addGenChannels  (  countedGrp  ,  count  ,  5  )  ; 
Object  [  ]  channels  =  countedGrp  .  getChannelGroup  (  )  .  getChannels  (  )  .  toArray  (  )  ; 
assertEquals  (  "Wrong number of channels in group"  ,  channels  .  length  ,  count  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  { 
Channel   aChan  =  (  Channel  )  channels  [  i  ]  ; 
Object  [  ]  items  =  aChan  .  getItems  (  )  .  toArray  (  )  ; 
int   howManyItems  =  items  .  length  ; 
assertEquals  (  "Wrong Count returned"  ,  howManyItems  ,  countedGrp  .  getItemCount  (  aChan  )  )  ; 
} 
} 









protected   void   assertValidGroup  (  PersistChanGrpMgr   gp  ,  String   name  ,  int   size  )  throws   Exception  { 
ChannelGroup   gpc  =  gp  .  getChannelGroup  (  )  ; 
assertEquals  (  gpc  .  getTitle  (  )  ,  name  )  ; 
assertEquals  (  size  ,  gpc  .  getChannels  (  )  .  size  (  )  )  ; 
} 









private   void   addGenChannels  (  PersistChanGrpMgr   aGroup  ,  int   count  ,  int   taskRuns  )  { 
int   i  ; 
for  (  i  =  0  ;  i  <  count  ;  i  ++  )  { 
Channel   achannel  ; 
achannel  =  aGroup  .  addChannel  (  RssUrlTestData  .  generate  (  )  )  ; 
aGroup  .  notifyChannelsAndItems  (  achannel  )  ; 
} 
runPersistChanGrpTask  (  aGroup  ,  taskRuns  )  ; 
} 







public   PersistChanGrpMgr   makeEmptyGroup  (  String   name  )  { 
PersistChanGrpMgr   res  ; 
logger  .  info  (  "Creating group: "  +  name  )  ; 
res  =  new   PersistChanGrpMgr  (  handler  ,  false  )  ; 
res  .  createGroup  (  name  )  ; 
logger  .  info  (  "Result: "  +  res  )  ; 
assertEquals  (  "Newly created group has non-zero Channels"  ,  0  ,  res  .  getChannelGroup  (  )  .  getChannels  (  )  .  size  (  )  )  ; 
return   res  ; 
} 








private   void   addChannel  (  PersistChanGrpMgr   agroup  ,  String   label  ,  String   url  )  { 
Channel   achannel  ; 
logger  .  info  (  "Adding channel: "  +  label  +  " to "  +  agroup  +  " "  +  url  )  ; 
achannel  =  agroup  .  addChannel  (  url  )  ; 
agroup  .  notifyChannelsAndItems  (  achannel  )  ; 
runPersistChanGrpTask  (  agroup  ,  3  )  ; 
} 








protected   void   runPersistChanGrpTask  (  PersistChanGrpMgr   mgr  ,  int   count  )  { 
PersistChanGrpMgrTask   task  =  new   PersistChanGrpMgrTask  (  mgr  ,  0  )  ; 
for  (  int   i  =  0  ;  i  <  count  ;  i  ++  )  { 
task  .  performUpdates  (  )  ; 
} 
} 
} 

