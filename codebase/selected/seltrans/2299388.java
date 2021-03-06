package   org  .  knopflerfish  .  bundle  .  trayicon  ; 

import   org  .  osgi  .  framework  .  *  ; 
import   com  .  jeans  .  trayicon  .  *  ; 
import   java  .  awt  .  Component  ; 
import   java  .  awt  .  Frame  ; 
import   java  .  awt  .  Image  ; 
import   java  .  awt  .  Point  ; 
import   java  .  awt  .  Toolkit  ; 
import   java  .  awt  .  event  .  *  ; 
import   java  .  awt  .  image  .  ImageObserver  ; 
import   java  .  io  .  *  ; 
import   java  .  net  .  URL  ; 
import   javax  .  swing  .  JPopupMenu  ; 
import   javax  .  swing  .  MenuElement  ; 
import   javax  .  swing  .  JMenuItem  ; 
import   javax  .  swing  .  JMenu  ; 
import   javax  .  swing  .  JCheckBoxMenuItem  ; 
import   javax  .  swing  .  event  .  *  ; 
import   org  .  knopflerfish  .  service  .  trayicon  .  *  ; 

public   class   TrayIconWrapper  { 

WindowsTrayIcon   windowsTrayIcon  ; 

TrayIconManager   manager  ; 

TrayIcon   trayIcon  ; 

public   TrayIconWrapper  (  TrayIcon   trayIcon  )  { 
this  .  manager  =  Activator  .  manager  ; 
this  .  trayIcon  =  trayIcon  ; 
if  (  trayIcon  ==  null  )  { 
throw   new   IllegalArgumentException  (  "TrayIcon argument cannot be null"  )  ; 
} 
} 

SwingTrayPopup   swingPopup  =  null  ; 

JPopupMenu   jPopup  =  null  ; 

Frame   frame  ; 

public   void   open  (  )  { 
if  (  windowsTrayIcon  !=  null  )  { 
Activator  .  log  .  info  (  "id="  +  trayIcon  .  getId  (  )  +  " already open"  )  ; 
return  ; 
} 
if  (  WindowsTrayIcon  .  isRunning  (  trayIcon  .  getId  (  )  )  )  { 
Activator  .  log  .  warn  (  "id="  +  trayIcon  .  getId  (  )  +  " already running"  )  ; 
} 
try  { 
Activator  .  log  .  info  (  "open trayicon id="  +  trayIcon  .  getId  (  )  +  ", name="  +  trayIcon  .  getName  (  )  )  ; 
frame  =  new   Frame  (  ""  )  { 

public   Point   getLocationOnScreen  (  )  { 
return   new   Point  (  0  ,  0  )  ; 
} 
}  ; 
WindowsTrayIcon  .  initTrayIcon  (  trayIcon  .  getId  (  )  )  ; 
Image   img  =  loadImage  (  trayIcon  .  getImageURL  (  )  )  ; 
windowsTrayIcon  =  new   WindowsTrayIcon  (  img  ,  img  .  getWidth  (  null  )  ,  img  .  getHeight  (  null  )  )  ; 
manager  .  logErr  (  )  ; 
windowsTrayIcon  .  setToolTipText  (  trayIcon  .  getName  (  )  )  ; 
manager  .  logErr  (  )  ; 
windowsTrayIcon  .  addMouseListener  (  trayIcon  )  ; 
manager  .  logErr  (  )  ; 
windowsTrayIcon  .  addActionListener  (  trayIcon  )  ; 
manager  .  logErr  (  )  ; 
jPopup  =  trayIcon  .  getTrayJPopupMenu  (  )  ; 
if  (  jPopup  !=  null  )  { 
try  { 
String   javaVersion  =  System  .  getProperty  (  "java.version"  )  ; 
if  (  javaVersion  .  startsWith  (  "1.3"  )  )  { 
Activator  .  log  .  info  (  "workaround for java 1.3 menues"  )  ; 
if  (  true  )  { 
TrayIconPopup   popup  =  makeTrayIconPopup  (  trayIcon  .  getTrayJPopupMenu  (  )  ,  null  )  ; 
windowsTrayIcon  .  setPopup  (  popup  )  ; 
}  else  { 
windowsTrayIcon  .  addMouseListener  (  new   MouseAdapter  (  )  { 

public   void   mousePressed  (  MouseEvent   ev  )  { 
Component   comp  =  windowsTrayIcon  .  getDummyComponent  (  )  ; 
jPopup  .  show  (  comp  ,  ev  .  getX  (  )  ,  ev  .  getY  (  )  -  jPopup  .  getSize  (  )  .  height  )  ; 
} 
}  )  ; 
} 
}  else  { 
swingPopup  =  new   SwingTrayPopup  (  )  ; 
MenuElement  [  ]  subMenus  =  jPopup  .  getSubElements  (  )  ; 
for  (  int   i  =  0  ;  i  <  subMenus  .  length  ;  i  ++  )  { 
if  (  subMenus  [  i  ]  instanceof   JMenuItem  )  { 
swingPopup  .  add  (  (  JMenuItem  )  subMenus  [  i  ]  )  ; 
} 
} 
swingPopup  .  setTrayIcon  (  windowsTrayIcon  )  ; 
} 
}  catch  (  Exception   e  )  { 
Activator  .  log  .  error  (  "Failed to set menu"  )  ; 
} 
} 
windowsTrayIcon  .  setVisible  (  true  )  ; 
manager  .  logErr  (  )  ; 
String   startupMsg  =  trayIcon  .  getStartupMessage  (  )  ; 
if  (  startupMsg  !=  null  )  { 
if  (  WindowsTrayIcon  .  supportsBalloonMessages  (  )  )  { 
windowsTrayIcon  .  showBalloon  (  startupMsg  ,  trayIcon  .  getName  (  )  ,  5  *  1000  ,  WindowsTrayIcon  .  BALLOON_INFO  )  ; 
} 
} 
sendEvent  (  new   TrayEvent  (  TrayEvent  .  OPENED  )  )  ; 
}  catch  (  Exception   e  )  { 
Activator  .  log  .  error  (  "Failed to set up TrayIcon"  ,  e  )  ; 
} 
} 

void   sendEvent  (  TrayEvent   ev  )  { 
try  { 
trayIcon  .  trayEvent  (  ev  )  ; 
}  catch  (  Exception   e  )  { 
Activator  .  log  .  error  (  "event send failed"  ,  e  )  ; 
} 
} 

void   close  (  )  { 
Activator  .  log  .  info  (  "TrayIconWrapper.close() trayicon id="  +  trayIcon  .  getId  (  )  )  ; 
if  (  trayIcon  !=  null  )  { 
sendEvent  (  new   TrayEvent  (  TrayEvent  .  CLOSED  )  )  ; 
} 
if  (  windowsTrayIcon  !=  null  )  { 
Activator  .  log  .  info  (  " call freeIcon"  )  ; 
windowsTrayIcon  .  freeIcon  (  )  ; 
Activator  .  log  .  info  (  " called freeIcon"  )  ; 
} 
Activator  .  log  .  info  (  "TrayIconWrapper.closed trayicon id="  +  trayIcon  .  getId  (  )  )  ; 
trayIcon  =  null  ; 
windowsTrayIcon  =  null  ; 
} 

Object   imageLock  =  new   Object  (  )  ; 

boolean   bGotImage  =  false  ; 

Image   loadImage  (  URL   url  )  throws   IOException  { 
synchronized  (  imageLock  )  { 
Toolkit   tk  =  Toolkit  .  getDefaultToolkit  (  )  ; 
Image   img  =  tk  .  createImage  (  loadURL  (  url  )  )  ; 
img  .  getHeight  (  new   ImageObserver  (  )  { 

public   boolean   imageUpdate  (  Image   img  ,  int   infoflags  ,  int   x  ,  int   y  ,  int   width  ,  int   height  )  { 
if  (  0  !=  (  infoflags  &  ImageObserver  .  HEIGHT  )  )  { 
synchronized  (  imageLock  )  { 
bGotImage  =  true  ; 
imageLock  .  notifyAll  (  )  ; 
} 
return   true  ; 
} 
return   false  ; 
} 
}  )  ; 
if  (  !  bGotImage  )  { 
try  { 
imageLock  .  wait  (  1000  *  10  )  ; 
}  catch  (  InterruptedException   e  )  { 
throw   new   IOException  (  "Image load timed out: "  +  e  )  ; 
} 
} 
return   img  ; 
} 
} 

TrayIconPopup   makeTrayIconPopup  (  MenuElement   el  ,  String   name  )  { 
if  (  el  ==  null  )  { 
return   null  ; 
} 
TrayIconPopup   popup  =  name  !=  null  ?  new   TrayIconPopup  (  name  )  :  new   TrayIconPopup  (  )  ; 
MenuElement  [  ]  subMenus  =  el  .  getSubElements  (  )  ; 
for  (  int   i  =  0  ;  i  <  subMenus  .  length  ;  i  ++  )  { 
if  (  subMenus  [  i  ]  instanceof   JCheckBoxMenuItem  )  { 
popup  .  addMenuItem  (  new   TrayIconPopupCheckItemW  (  (  JCheckBoxMenuItem  )  subMenus  [  i  ]  )  )  ; 
}  else   if  (  subMenus  [  i  ]  instanceof   JMenu  )  { 
JMenu   menu  =  (  JMenu  )  subMenus  [  i  ]  ; 
MenuElement  [  ]  subMenus2  =  menu  .  getSubElements  (  )  ; 
if  (  subMenus2  .  length  ==  1  )  { 
popup  .  addMenuItem  (  makeTrayIconPopup  (  subMenus2  [  0  ]  ,  menu  .  getText  (  )  )  )  ; 
} 
}  else   if  (  subMenus  [  i  ]  instanceof   JPopupMenu  )  { 
JPopupMenu   menu  =  (  JPopupMenu  )  subMenus  [  i  ]  ; 
popup  .  addMenuItem  (  makeTrayIconPopup  (  menu  ,  name  )  )  ; 
}  else   if  (  subMenus  [  i  ]  instanceof   JMenuItem  )  { 
popup  .  addMenuItem  (  new   TrayIconPopupSimpleItemW  (  (  JMenuItem  )  subMenus  [  i  ]  )  )  ; 
}  else  { 
} 
} 
return   popup  ; 
} 




public   static   byte  [  ]  loadURL  (  URL   url  )  throws   IOException  { 
byte  [  ]  buf  =  new   byte  [  1024  ]  ; 
ByteArrayOutputStream   bout  =  new   ByteArrayOutputStream  (  )  ; 
BufferedInputStream   in  =  new   BufferedInputStream  (  url  .  openStream  (  )  )  ; 
int   n  ; 
while  (  (  n  =  in  .  read  (  buf  )  )  >  0  )  { 
bout  .  write  (  buf  ,  0  ,  n  )  ; 
} 
try  { 
in  .  close  (  )  ; 
}  catch  (  Exception   ignored  )  { 
} 
return   bout  .  toByteArray  (  )  ; 
} 

public   String   toString  (  )  { 
return  "TrayIconWrapper["  +  "trayIcon="  +  trayIcon  +  "windowsTrayIcon="  +  windowsTrayIcon  +  "]"  ; 
} 
} 

class   TrayIconPopupSimpleItemW   extends   TrayIconPopupSimpleItem  { 

JMenuItem   jItem  ; 

TrayIconPopupSimpleItemW  (  JMenuItem   jItem  )  { 
super  (  jItem  .  getText  (  )  )  ; 
this  .  jItem  =  jItem  ; 
try  { 
ActionListener  [  ]  la  =  (  ActionListener  [  ]  )  jItem  .  getListeners  (  ActionListener  .  class  )  ; 
for  (  int   i  =  0  ;  la  !=  null  &&  i  <  la  .  length  ;  i  ++  )  { 
addActionListener  (  la  [  i  ]  )  ; 
} 
}  catch  (  Exception   e  )  { 
Activator  .  log  .  error  (  "Failed to get actionlisteners"  ,  e  )  ; 
} 
} 

public   String   getName  (  )  { 
return   jItem  .  getText  (  )  ; 
} 
} 

class   TrayIconPopupCheckItemW   extends   TrayIconPopupCheckItem   implements   ChangeListener  { 

JCheckBoxMenuItem   jItem  ; 

TrayIconPopupCheckItemW  (  JCheckBoxMenuItem   jItem  )  { 
super  (  jItem  .  getText  (  )  )  ; 
this  .  jItem  =  jItem  ; 
try  { 
ActionListener  [  ]  la  =  (  ActionListener  [  ]  )  jItem  .  getListeners  (  ActionListener  .  class  )  ; 
for  (  int   i  =  0  ;  la  !=  null  &&  i  <  la  .  length  ;  i  ++  )  { 
addActionListener  (  la  [  i  ]  )  ; 
} 
}  catch  (  Exception   e  )  { 
Activator  .  log  .  error  (  "Failed to get actionlisteners"  ,  e  )  ; 
} 
jItem  .  addChangeListener  (  this  )  ; 
} 

public   void   stateChanged  (  ChangeEvent   e  )  { 
super  .  setCheck  (  getCheck  (  )  )  ; 
} 

public   String   getName  (  )  { 
return   jItem  .  getText  (  )  ; 
} 

public   boolean   getCheck  (  )  { 
return   jItem  .  getState  (  )  ; 
} 

public   void   setCheck  (  boolean   b  )  { 
super  .  setCheck  (  b  )  ; 
jItem  .  setState  (  b  )  ; 
} 
} 

