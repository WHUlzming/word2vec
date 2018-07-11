import   com  .  sun  .  pdfview  .  OutlineNode  ; 
import   com  .  sun  .  pdfview  .  PDFDestination  ; 
import   com  .  sun  .  pdfview  .  PDFFile  ; 
import   com  .  sun  .  pdfview  .  PDFObject  ; 
import   com  .  sun  .  pdfview  .  PDFPage  ; 
import   com  .  sun  .  pdfview  .  action  .  GoToAction  ; 
import   com  .  sun  .  pdfview  .  action  .  PDFAction  ; 
import   com  .  sun  .  pdfview  .  print  .  PDFPrintPage  ; 
import   java  .  awt  .  BorderLayout  ; 
import   java  .  awt  .  Color  ; 
import   java  .  awt  .  Dimension  ; 
import   java  .  awt  .  Toolkit  ; 
import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  awt  .  event  .  ActionListener  ; 
import   java  .  awt  .  event  .  KeyEvent  ; 
import   java  .  awt  .  event  .  KeyListener  ; 
import   java  .  awt  .  event  .  WindowAdapter  ; 
import   java  .  awt  .  event  .  WindowEvent  ; 
import   java  .  awt  .  geom  .  NoninvertibleTransformException  ; 
import   java  .  awt  .  geom  .  Rectangle2D  ; 
import   java  .  awt  .  print  .  Book  ; 
import   java  .  awt  .  print  .  PageFormat  ; 
import   java  .  awt  .  print  .  PrinterException  ; 
import   java  .  awt  .  print  .  PrinterJob  ; 
import   java  .  beans  .  PropertyChangeEvent  ; 
import   java  .  beans  .  PropertyChangeListener  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  RandomAccessFile  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 
import   java  .  net  .  URL  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   javax  .  swing  .  AbstractAction  ; 
import   javax  .  swing  .  Action  ; 
import   javax  .  swing  .  Box  ; 
import   javax  .  swing  .  ButtonGroup  ; 
import   javax  .  swing  .  Icon  ; 
import   javax  .  swing  .  ImageIcon  ; 
import   javax  .  swing  .  JButton  ; 
import   javax  .  swing  .  JDialog  ; 
import   javax  .  swing  .  JFileChooser  ; 
import   javax  .  swing  .  JFrame  ; 
import   javax  .  swing  .  JMenu  ; 
import   javax  .  swing  .  JMenuBar  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   javax  .  swing  .  JScrollPane  ; 
import   javax  .  swing  .  JSplitPane  ; 
import   javax  .  swing  .  JTextField  ; 
import   javax  .  swing  .  JToggleButton  ; 
import   javax  .  swing  .  JToolBar  ; 
import   javax  .  swing  .  JTree  ; 
import   javax  .  swing  .  SwingUtilities  ; 
import   javax  .  swing  .  event  .  TreeSelectionEvent  ; 
import   javax  .  swing  .  event  .  TreeSelectionListener  ; 
import   javax  .  swing  .  filechooser  .  FileFilter  ; 





public   class   PDFViewer   extends   JFrame   implements   KeyListener  ,  TreeSelectionListener  ,  PDFPageChangeListener  { 

public   static   final   String   TITLE  =  "SwingLabs PDF Viewer"  ; 


PDFFile   curFile  ; 


String   docName  ; 


JSplitPane   split  ; 


JScrollPane   thumbscroll  ; 


PDFThumbPanel   thumbs  ; 


PDFPagePanel   page  ; 


PDFPagePanel   fspp  ; 


int   curpage  =  -  1  ; 


JToggleButton   fullScreenButton  ; 


JTextField   pageField  ; 


PDFFullScreenWindow   fullScreen  ; 


OutlineNode   outline  =  null  ; 


PageFormat   pformat  =  PrinterJob  .  getPrinterJob  (  )  .  defaultPage  (  )  ; 


boolean   doThumb  =  true  ; 


Flag   docWaiter  ; 


PagePreparer   pagePrep  ; 


JDialog   olf  ; 


JMenu   docMenu  ; 






public   Icon   getIcon  (  String   name  )  { 
Icon   icon  =  null  ; 
URL   url  =  null  ; 
try  { 
url  =  getClass  (  )  .  getResource  (  name  )  ; 
icon  =  new   ImageIcon  (  url  )  ; 
if  (  icon  ==  null  )  { 
System  .  out  .  println  (  "Couldn't find "  +  url  )  ; 
} 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  "Couldn't find "  +  getClass  (  )  .  getName  (  )  +  "/"  +  name  )  ; 
e  .  printStackTrace  (  )  ; 
} 
return   icon  ; 
} 

Action   openAction  =  new   AbstractAction  (  "Open..."  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doOpen  (  )  ; 
} 
}  ; 

Action   pageSetupAction  =  new   AbstractAction  (  "Page setup..."  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doPageSetup  (  )  ; 
} 
}  ; 

Action   printAction  =  new   AbstractAction  (  "Print..."  ,  getIcon  (  "gfx/print.gif"  )  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doPrint  (  )  ; 
} 
}  ; 

Action   closeAction  =  new   AbstractAction  (  "Close"  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doClose  (  )  ; 
} 
}  ; 

Action   quitAction  =  new   AbstractAction  (  "Quit"  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doQuit  (  )  ; 
} 
}  ; 

class   ZoomAction   extends   AbstractAction  { 

double   zoomfactor  =  1.0  ; 

public   ZoomAction  (  String   name  ,  double   factor  )  { 
super  (  name  )  ; 
zoomfactor  =  factor  ; 
} 

public   ZoomAction  (  String   name  ,  Icon   icon  ,  double   factor  )  { 
super  (  name  ,  icon  )  ; 
zoomfactor  =  factor  ; 
} 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doZoom  (  zoomfactor  )  ; 
} 
} 

ZoomAction   zoomInAction  =  new   ZoomAction  (  "Zoom in"  ,  getIcon  (  "gfx/zoomin.gif"  )  ,  2.0  )  ; 

ZoomAction   zoomOutAction  =  new   ZoomAction  (  "Zoom out"  ,  getIcon  (  "gfx/zoomout.gif"  )  ,  0.5  )  ; 

Action   zoomToolAction  =  new   AbstractAction  (  ""  ,  getIcon  (  "gfx/zoom.gif"  )  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doZoomTool  (  )  ; 
} 
}  ; 

Action   fitInWindowAction  =  new   AbstractAction  (  "Fit in window"  ,  getIcon  (  "gfx/fit.gif"  )  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
try  { 
doFitInWindow  (  )  ; 
}  catch  (  NoninvertibleTransformException   nte  )  { 
nte  .  printStackTrace  (  )  ; 
} 
} 
}  ; 

class   ThumbAction   extends   AbstractAction   implements   PropertyChangeListener  { 

boolean   isOpen  =  true  ; 

public   ThumbAction  (  )  { 
super  (  "Hide thumbnails"  )  ; 
} 

public   void   propertyChange  (  PropertyChangeEvent   evt  )  { 
int   v  =  (  (  Integer  )  evt  .  getNewValue  (  )  )  .  intValue  (  )  ; 
if  (  v  <=  1  )  { 
isOpen  =  false  ; 
putValue  (  ACTION_COMMAND_KEY  ,  "Show thumbnails"  )  ; 
putValue  (  NAME  ,  "Show thumbnails"  )  ; 
}  else  { 
isOpen  =  true  ; 
putValue  (  ACTION_COMMAND_KEY  ,  "Hide thumbnails"  )  ; 
putValue  (  NAME  ,  "Hide thumbnails"  )  ; 
} 
} 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doThumbs  (  !  isOpen  )  ; 
} 
} 

ThumbAction   thumbAction  =  new   ThumbAction  (  )  ; 

Action   fullScreenAction  =  new   AbstractAction  (  "Full screen"  ,  getIcon  (  "gfx/fullscrn.gif"  )  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doFullScreen  (  (  evt  .  getModifiers  (  )  &  ActionEvent  .  SHIFT_MASK  )  !=  0  )  ; 
} 
}  ; 

Action   nextAction  =  new   AbstractAction  (  "Next"  ,  getIcon  (  "gfx/next.gif"  )  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doNext  (  )  ; 
} 
}  ; 

Action   firstAction  =  new   AbstractAction  (  "First"  ,  getIcon  (  "gfx/first.gif"  )  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doFirst  (  )  ; 
} 
}  ; 

Action   lastAction  =  new   AbstractAction  (  "Last"  ,  getIcon  (  "gfx/last.gif"  )  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doLast  (  )  ; 
} 
}  ; 

Action   prevAction  =  new   AbstractAction  (  "Prev"  ,  getIcon  (  "gfx/prev.gif"  )  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doPrev  (  )  ; 
} 
}  ; 






public   PDFViewer  (  boolean   useThumbs  )  { 
super  (  TITLE  )  ; 
addWindowListener  (  new   WindowAdapter  (  )  { 

@  Override 
public   void   windowClosing  (  WindowEvent   evt  )  { 
doQuit  (  )  ; 
} 
}  )  ; 
doThumb  =  useThumbs  ; 
init  (  )  ; 
} 




protected   void   init  (  )  { 
page  =  new   PDFPagePanel  (  )  ; 
page  .  addKeyListener  (  this  )  ; 
if  (  doThumb  )  { 
split  =  new   JSplitPane  (  JSplitPane  .  HORIZONTAL_SPLIT  )  ; 
split  .  addPropertyChangeListener  (  JSplitPane  .  DIVIDER_LOCATION_PROPERTY  ,  thumbAction  )  ; 
split  .  setOneTouchExpandable  (  true  )  ; 
thumbs  =  new   PDFThumbPanel  (  null  )  ; 
thumbscroll  =  new   JScrollPane  (  thumbs  ,  JScrollPane  .  VERTICAL_SCROLLBAR_ALWAYS  ,  JScrollPane  .  HORIZONTAL_SCROLLBAR_NEVER  )  ; 
split  .  setLeftComponent  (  thumbscroll  )  ; 
split  .  setRightComponent  (  page  )  ; 
getContentPane  (  )  .  add  (  split  ,  BorderLayout  .  CENTER  )  ; 
}  else  { 
getContentPane  (  )  .  add  (  page  ,  BorderLayout  .  CENTER  )  ; 
} 
JToolBar   toolbar  =  new   JToolBar  (  )  ; 
toolbar  .  setFloatable  (  false  )  ; 
JButton   jb  ; 
jb  =  new   JButton  (  firstAction  )  ; 
jb  .  setText  (  ""  )  ; 
toolbar  .  add  (  jb  )  ; 
jb  =  new   JButton  (  prevAction  )  ; 
jb  .  setText  (  ""  )  ; 
toolbar  .  add  (  jb  )  ; 
pageField  =  new   JTextField  (  "-"  ,  3  )  ; 
pageField  .  setMaximumSize  (  new   Dimension  (  45  ,  32  )  )  ; 
pageField  .  addActionListener  (  new   ActionListener  (  )  { 

public   void   actionPerformed  (  ActionEvent   evt  )  { 
doPageTyped  (  )  ; 
} 
}  )  ; 
toolbar  .  add  (  pageField  )  ; 
jb  =  new   JButton  (  nextAction  )  ; 
jb  .  setText  (  ""  )  ; 
toolbar  .  add  (  jb  )  ; 
jb  =  new   JButton  (  lastAction  )  ; 
jb  .  setText  (  ""  )  ; 
toolbar  .  add  (  jb  )  ; 
toolbar  .  add  (  Box  .  createHorizontalGlue  (  )  )  ; 
fullScreenButton  =  new   JToggleButton  (  fullScreenAction  )  ; 
fullScreenButton  .  setText  (  ""  )  ; 
toolbar  .  add  (  fullScreenButton  )  ; 
fullScreenButton  .  setEnabled  (  true  )  ; 
toolbar  .  add  (  Box  .  createHorizontalGlue  (  )  )  ; 
JToggleButton   jtb  ; 
ButtonGroup   bg  =  new   ButtonGroup  (  )  ; 
jtb  =  new   JToggleButton  (  zoomToolAction  )  ; 
jtb  .  setText  (  ""  )  ; 
bg  .  add  (  jtb  )  ; 
toolbar  .  add  (  jtb  )  ; 
jtb  =  new   JToggleButton  (  fitInWindowAction  )  ; 
jtb  .  setText  (  ""  )  ; 
bg  .  add  (  jtb  )  ; 
jtb  .  setSelected  (  true  )  ; 
toolbar  .  add  (  jtb  )  ; 
toolbar  .  add  (  Box  .  createHorizontalGlue  (  )  )  ; 
jb  =  new   JButton  (  printAction  )  ; 
jb  .  setText  (  ""  )  ; 
toolbar  .  add  (  jb  )  ; 
getContentPane  (  )  .  add  (  toolbar  ,  BorderLayout  .  NORTH  )  ; 
JMenuBar   mb  =  new   JMenuBar  (  )  ; 
JMenu   file  =  new   JMenu  (  "File"  )  ; 
file  .  add  (  openAction  )  ; 
file  .  add  (  closeAction  )  ; 
file  .  addSeparator  (  )  ; 
file  .  add  (  pageSetupAction  )  ; 
file  .  add  (  printAction  )  ; 
file  .  addSeparator  (  )  ; 
file  .  add  (  quitAction  )  ; 
mb  .  add  (  file  )  ; 
JMenu   view  =  new   JMenu  (  "View"  )  ; 
JMenu   zoom  =  new   JMenu  (  "Zoom"  )  ; 
zoom  .  add  (  zoomInAction  )  ; 
zoom  .  add  (  zoomOutAction  )  ; 
zoom  .  add  (  fitInWindowAction  )  ; 
zoom  .  setEnabled  (  false  )  ; 
view  .  add  (  zoom  )  ; 
view  .  add  (  fullScreenAction  )  ; 
if  (  doThumb  )  { 
view  .  addSeparator  (  )  ; 
view  .  add  (  thumbAction  )  ; 
} 
mb  .  add  (  view  )  ; 
setJMenuBar  (  mb  )  ; 
setEnabling  (  )  ; 
pack  (  )  ; 
Dimension   screen  =  Toolkit  .  getDefaultToolkit  (  )  .  getScreenSize  (  )  ; 
int   x  =  (  screen  .  width  -  getWidth  (  )  )  /  2  ; 
int   y  =  (  screen  .  height  -  getHeight  (  )  )  /  2  ; 
setLocation  (  x  ,  y  )  ; 
if  (  SwingUtilities  .  isEventDispatchThread  (  )  )  { 
setVisible  (  true  )  ; 
}  else  { 
try  { 
SwingUtilities  .  invokeAndWait  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
setVisible  (  true  )  ; 
} 
}  )  ; 
}  catch  (  InvocationTargetException   ie  )  { 
}  catch  (  InterruptedException   ie  )  { 
} 
} 
} 






public   void   gotoPage  (  int   pagenum  )  { 
if  (  pagenum  <  0  )  { 
pagenum  =  0  ; 
}  else   if  (  pagenum  >=  curFile  .  getNumPages  (  )  )  { 
pagenum  =  curFile  .  getNumPages  (  )  -  1  ; 
} 
forceGotoPage  (  pagenum  )  ; 
} 





public   void   forceGotoPage  (  int   pagenum  )  { 
if  (  pagenum  <=  0  )  { 
pagenum  =  0  ; 
}  else   if  (  pagenum  >=  curFile  .  getNumPages  (  )  )  { 
pagenum  =  curFile  .  getNumPages  (  )  -  1  ; 
} 
curpage  =  pagenum  ; 
pageField  .  setText  (  String  .  valueOf  (  curpage  +  1  )  )  ; 
PDFPage   pg  =  curFile  .  getPage  (  pagenum  +  1  )  ; 
if  (  fspp  !=  null  )  { 
fspp  .  showPage  (  pg  )  ; 
fspp  .  requestFocus  (  )  ; 
}  else  { 
page  .  showPage  (  pg  )  ; 
page  .  requestFocus  (  )  ; 
} 
if  (  doThumb  )  { 
thumbs  .  pageShown  (  pagenum  )  ; 
} 
if  (  pagePrep  !=  null  )  { 
pagePrep  .  quit  (  )  ; 
} 
pagePrep  =  new   PagePreparer  (  pagenum  )  ; 
pagePrep  .  start  (  )  ; 
setEnabling  (  )  ; 
} 




class   PagePreparer   extends   Thread  { 

int   waitforPage  ; 

int   prepPage  ; 






public   PagePreparer  (  int   waitforPage  )  { 
setDaemon  (  true  )  ; 
this  .  waitforPage  =  waitforPage  ; 
this  .  prepPage  =  waitforPage  +  1  ; 
} 

public   void   quit  (  )  { 
waitforPage  =  -  1  ; 
} 

@  Override 
public   void   run  (  )  { 
Dimension   size  =  null  ; 
Rectangle2D   clip  =  null  ; 
if  (  fspp  !=  null  )  { 
fspp  .  waitForCurrentPage  (  )  ; 
size  =  fspp  .  getCurSize  (  )  ; 
clip  =  fspp  .  getCurClip  (  )  ; 
}  else   if  (  page  !=  null  )  { 
page  .  waitForCurrentPage  (  )  ; 
size  =  page  .  getCurSize  (  )  ; 
clip  =  page  .  getCurClip  (  )  ; 
} 
if  (  waitforPage  ==  curpage  )  { 
PDFPage   pdfPage  =  curFile  .  getPage  (  prepPage  +  1  ,  true  )  ; 
if  (  pdfPage  !=  null  &&  waitforPage  ==  curpage  )  { 
pdfPage  .  getImage  (  size  .  width  ,  size  .  height  ,  clip  ,  null  ,  true  ,  true  )  ; 
} 
} 
} 
} 




public   void   setEnabling  (  )  { 
boolean   fileavailable  =  curFile  !=  null  ; 
boolean   pageshown  =  (  (  fspp  !=  null  )  ?  fspp  .  getPage  (  )  !=  null  :  page  .  getPage  (  )  !=  null  )  ; 
boolean   printable  =  fileavailable  &&  curFile  .  isPrintable  (  )  ; 
pageField  .  setEnabled  (  fileavailable  )  ; 
printAction  .  setEnabled  (  printable  )  ; 
closeAction  .  setEnabled  (  fileavailable  )  ; 
fullScreenAction  .  setEnabled  (  pageshown  )  ; 
prevAction  .  setEnabled  (  pageshown  )  ; 
nextAction  .  setEnabled  (  pageshown  )  ; 
firstAction  .  setEnabled  (  fileavailable  )  ; 
lastAction  .  setEnabled  (  fileavailable  )  ; 
zoomToolAction  .  setEnabled  (  pageshown  )  ; 
fitInWindowAction  .  setEnabled  (  pageshown  )  ; 
zoomInAction  .  setEnabled  (  pageshown  )  ; 
zoomOutAction  .  setEnabled  (  pageshown  )  ; 
} 






public   void   openFile  (  File   file  )  throws   IOException  { 
RandomAccessFile   raf  =  new   RandomAccessFile  (  file  ,  "r"  )  ; 
FileChannel   channel  =  raf  .  getChannel  (  )  ; 
ByteBuffer   buf  =  channel  .  map  (  FileChannel  .  MapMode  .  READ_ONLY  ,  0  ,  channel  .  size  (  )  )  ; 
PDFFile   newfile  =  null  ; 
try  { 
newfile  =  new   PDFFile  (  buf  )  ; 
}  catch  (  IOException   ioe  )  { 
openError  (  file  .  getPath  (  )  +  " doesn't appear to be a PDF file."  )  ; 
return  ; 
} 
doClose  (  )  ; 
this  .  curFile  =  newfile  ; 
docName  =  file  .  getName  (  )  ; 
setTitle  (  TITLE  +  ": "  +  docName  )  ; 
if  (  doThumb  )  { 
thumbs  =  new   PDFThumbPanel  (  curFile  )  ; 
thumbs  .  addPageChangeListener  (  this  )  ; 
thumbscroll  .  getViewport  (  )  .  setView  (  thumbs  )  ; 
thumbscroll  .  getViewport  (  )  .  setBackground  (  Color  .  gray  )  ; 
} 
setEnabling  (  )  ; 
forceGotoPage  (  0  )  ; 
try  { 
outline  =  curFile  .  getOutline  (  )  ; 
}  catch  (  IOException   ioe  )  { 
} 
if  (  outline  !=  null  )  { 
if  (  outline  .  getChildCount  (  )  >  0  )  { 
olf  =  new   JDialog  (  this  ,  "Outline"  )  ; 
olf  .  setDefaultCloseOperation  (  JDialog  .  DO_NOTHING_ON_CLOSE  )  ; 
olf  .  setLocation  (  this  .  getLocation  (  )  )  ; 
JTree   jt  =  new   JTree  (  outline  )  ; 
jt  .  setRootVisible  (  false  )  ; 
jt  .  addTreeSelectionListener  (  this  )  ; 
JScrollPane   jsp  =  new   JScrollPane  (  jt  )  ; 
olf  .  getContentPane  (  )  .  add  (  jsp  )  ; 
olf  .  pack  (  )  ; 
olf  .  setVisible  (  true  )  ; 
}  else  { 
if  (  olf  !=  null  )  { 
olf  .  setVisible  (  false  )  ; 
olf  =  null  ; 
} 
} 
} 
} 




public   void   openError  (  String   message  )  { 
JOptionPane  .  showMessageDialog  (  split  ,  message  ,  "Error opening file"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 




FileFilter   pdfFilter  =  new   FileFilter  (  )  { 

public   boolean   accept  (  File   f  )  { 
return   f  .  isDirectory  (  )  ||  f  .  getName  (  )  .  endsWith  (  ".pdf"  )  ; 
} 

public   String   getDescription  (  )  { 
return  "Choose a PDF file"  ; 
} 
}  ; 

private   File   prevDirChoice  ; 




public   void   doOpen  (  )  { 
try  { 
JFileChooser   fc  =  new   JFileChooser  (  )  ; 
fc  .  setCurrentDirectory  (  prevDirChoice  )  ; 
fc  .  setFileFilter  (  pdfFilter  )  ; 
fc  .  setMultiSelectionEnabled  (  false  )  ; 
int   returnVal  =  fc  .  showOpenDialog  (  this  )  ; 
if  (  returnVal  ==  JFileChooser  .  APPROVE_OPTION  )  { 
try  { 
prevDirChoice  =  fc  .  getSelectedFile  (  )  ; 
openFile  (  fc  .  getSelectedFile  (  )  )  ; 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
} 
}  catch  (  Exception   e  )  { 
JOptionPane  .  showMessageDialog  (  split  ,  "Opening files from your local "  +  "disk is not available\nfrom the "  +  "Java Web Start version of this "  +  "program.\n"  ,  "Error opening directory"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 





public   void   doOpen  (  String   name  )  { 
try  { 
openFile  (  new   File  (  name  )  )  ; 
}  catch  (  IOException   ioe  )  { 
} 
} 




public   void   doPageSetup  (  )  { 
PrinterJob   pjob  =  PrinterJob  .  getPrinterJob  (  )  ; 
pformat  =  pjob  .  pageDialog  (  pformat  )  ; 
} 




class   PrintThread   extends   Thread  { 

PDFPrintPage   ptPages  ; 

PrinterJob   ptPjob  ; 

public   PrintThread  (  PDFPrintPage   pages  ,  PrinterJob   pjob  )  { 
ptPages  =  pages  ; 
ptPjob  =  pjob  ; 
} 

@  Override 
public   void   run  (  )  { 
try  { 
ptPages  .  show  (  ptPjob  )  ; 
ptPjob  .  print  (  )  ; 
}  catch  (  PrinterException   pe  )  { 
JOptionPane  .  showMessageDialog  (  PDFViewer  .  this  ,  "Printing Error: "  +  pe  .  getMessage  (  )  ,  "Print Aborted"  ,  JOptionPane  .  ERROR_MESSAGE  )  ; 
} 
ptPages  .  hide  (  )  ; 
} 
} 




public   void   doPrint  (  )  { 
PrinterJob   pjob  =  PrinterJob  .  getPrinterJob  (  )  ; 
pjob  .  setJobName  (  docName  )  ; 
Book   book  =  new   Book  (  )  ; 
PDFPrintPage   pages  =  new   PDFPrintPage  (  curFile  )  ; 
book  .  append  (  pages  ,  pformat  ,  curFile  .  getNumPages  (  )  )  ; 
pjob  .  setPageable  (  book  )  ; 
if  (  pjob  .  printDialog  (  )  )  { 
new   PrintThread  (  pages  ,  pjob  )  .  start  (  )  ; 
} 
} 




public   void   doClose  (  )  { 
if  (  thumbs  !=  null  )  { 
thumbs  .  stop  (  )  ; 
} 
if  (  olf  !=  null  )  { 
olf  .  setVisible  (  false  )  ; 
olf  =  null  ; 
} 
if  (  doThumb  )  { 
thumbs  =  new   PDFThumbPanel  (  null  )  ; 
thumbscroll  .  getViewport  (  )  .  setView  (  thumbs  )  ; 
} 
setFullScreenMode  (  false  ,  false  )  ; 
page  .  showPage  (  null  )  ; 
curFile  =  null  ; 
setTitle  (  TITLE  )  ; 
setEnabling  (  )  ; 
} 





public   void   doQuit  (  )  { 
doClose  (  )  ; 
dispose  (  )  ; 
System  .  exit  (  0  )  ; 
} 




public   void   doZoomTool  (  )  { 
if  (  fspp  ==  null  )  { 
page  .  useZoomTool  (  true  )  ; 
} 
} 




public   void   doFitInWindow  (  )  throws   NoninvertibleTransformException  { 
if  (  fspp  ==  null  )  { 
page  .  useZoomTool  (  false  )  ; 
page  .  setClip  (  null  )  ; 
} 
} 




public   void   doThumbs  (  boolean   show  )  { 
if  (  show  )  { 
split  .  setDividerLocation  (  (  int  )  thumbs  .  getPreferredSize  (  )  .  width  +  (  int  )  thumbscroll  .  getVerticalScrollBar  (  )  .  getWidth  (  )  +  4  )  ; 
}  else  { 
split  .  setDividerLocation  (  0  )  ; 
} 
} 







public   void   doFullScreen  (  boolean   force  )  { 
setFullScreenMode  (  fullScreen  ==  null  ,  force  )  ; 
} 

public   void   doZoom  (  double   factor  )  { 
} 




public   void   doNext  (  )  { 
gotoPage  (  curpage  +  1  )  ; 
} 




public   void   doPrev  (  )  { 
gotoPage  (  curpage  -  1  )  ; 
} 




public   void   doFirst  (  )  { 
gotoPage  (  0  )  ; 
} 




public   void   doLast  (  )  { 
gotoPage  (  curFile  .  getNumPages  (  )  -  1  )  ; 
} 




public   void   doPageTyped  (  )  { 
int   pagenum  =  -  1  ; 
try  { 
pagenum  =  Integer  .  parseInt  (  pageField  .  getText  (  )  )  -  1  ; 
}  catch  (  NumberFormatException   nfe  )  { 
} 
if  (  pagenum  >=  curFile  .  getNumPages  (  )  )  { 
pagenum  =  curFile  .  getNumPages  (  )  -  1  ; 
} 
if  (  pagenum  >=  0  )  { 
if  (  pagenum  !=  curpage  )  { 
gotoPage  (  pagenum  )  ; 
} 
}  else  { 
pageField  .  setText  (  String  .  valueOf  (  curpage  )  )  ; 
} 
} 




class   PerformFullScreenMode   implements   Runnable  { 

boolean   force  ; 

public   PerformFullScreenMode  (  boolean   forcechoice  )  { 
force  =  forcechoice  ; 
} 

public   void   run  (  )  { 
fspp  =  new   PDFPagePanel  (  )  ; 
fspp  .  setBackground  (  Color  .  black  )  ; 
page  .  showPage  (  null  )  ; 
fullScreen  =  new   PDFFullScreenWindow  (  fspp  ,  force  )  ; 
fspp  .  addKeyListener  (  PDFViewer  .  this  )  ; 
gotoPage  (  curpage  )  ; 
fullScreenAction  .  setEnabled  (  true  )  ; 
} 
} 







public   void   setFullScreenMode  (  boolean   full  ,  boolean   force  )  { 
if  (  full  &&  fullScreen  ==  null  )  { 
fullScreenAction  .  setEnabled  (  false  )  ; 
new   Thread  (  new   PerformFullScreenMode  (  force  )  )  .  start  (  )  ; 
fullScreenButton  .  setSelected  (  true  )  ; 
}  else   if  (  !  full  &&  fullScreen  !=  null  )  { 
fullScreen  .  close  (  )  ; 
fspp  =  null  ; 
fullScreen  =  null  ; 
gotoPage  (  curpage  )  ; 
fullScreenButton  .  setSelected  (  false  )  ; 
} 
} 

public   static   void   main  (  String   args  [  ]  )  { 
String   fileName  =  null  ; 
boolean   useThumbs  =  true  ; 
for  (  int   i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
if  (  args  [  i  ]  .  equalsIgnoreCase  (  "-noThumb"  )  )  { 
useThumbs  =  false  ; 
}  else   if  (  args  [  i  ]  .  equalsIgnoreCase  (  "-help"  )  ||  args  [  i  ]  .  equalsIgnoreCase  (  "-h"  )  ||  args  [  i  ]  .  equalsIgnoreCase  (  "-?"  )  )  { 
System  .  out  .  println  (  "java com.sun.awc.PDFViewer [flags] [file]"  )  ; 
System  .  out  .  println  (  "flags: [-noThumb] [-help or -h or -?]"  )  ; 
System  .  exit  (  0  )  ; 
}  else  { 
fileName  =  args  [  i  ]  ; 
} 
} 
PDFViewer   viewer  ; 
viewer  =  new   PDFViewer  (  useThumbs  )  ; 
if  (  fileName  !=  null  )  { 
viewer  .  doOpen  (  fileName  )  ; 
} 
} 




public   void   keyPressed  (  KeyEvent   evt  )  { 
int   code  =  evt  .  getKeyCode  (  )  ; 
if  (  code  ==  KeyEvent  .  VK_LEFT  )  { 
doPrev  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_RIGHT  )  { 
doNext  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_UP  )  { 
doPrev  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_DOWN  )  { 
doNext  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_HOME  )  { 
doFirst  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_END  )  { 
doLast  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_PAGE_UP  )  { 
doPrev  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_PAGE_DOWN  )  { 
doNext  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_SPACE  )  { 
doNext  (  )  ; 
}  else   if  (  code  ==  KeyEvent  .  VK_ESCAPE  )  { 
setFullScreenMode  (  false  ,  false  )  ; 
} 
} 




class   PageBuilder   implements   Runnable  { 

int   value  =  0  ; 

long   timeout  ; 

Thread   anim  ; 

static   final   long   TIMEOUT  =  500  ; 


public   synchronized   void   keyTyped  (  int   keyval  )  { 
value  =  value  *  10  +  keyval  ; 
timeout  =  System  .  currentTimeMillis  (  )  +  TIMEOUT  ; 
if  (  anim  ==  null  )  { 
anim  =  new   Thread  (  this  )  ; 
anim  .  start  (  )  ; 
} 
} 





public   void   run  (  )  { 
long   now  ,  then  ; 
synchronized  (  this  )  { 
now  =  System  .  currentTimeMillis  (  )  ; 
then  =  timeout  ; 
} 
while  (  now  <  then  )  { 
try  { 
Thread  .  sleep  (  timeout  -  now  )  ; 
}  catch  (  InterruptedException   ie  )  { 
} 
synchronized  (  this  )  { 
now  =  System  .  currentTimeMillis  (  )  ; 
then  =  timeout  ; 
} 
} 
synchronized  (  this  )  { 
gotoPage  (  value  -  1  )  ; 
anim  =  null  ; 
value  =  0  ; 
} 
} 
} 

PageBuilder   pb  =  new   PageBuilder  (  )  ; 

public   void   keyReleased  (  KeyEvent   evt  )  { 
} 




public   void   keyTyped  (  KeyEvent   evt  )  { 
char   key  =  evt  .  getKeyChar  (  )  ; 
if  (  key  >=  '0'  &&  key  <=  '9'  )  { 
int   val  =  key  -  '0'  ; 
pb  .  keyTyped  (  val  )  ; 
} 
} 





public   void   valueChanged  (  TreeSelectionEvent   e  )  { 
if  (  e  .  isAddedPath  (  )  )  { 
OutlineNode   node  =  (  OutlineNode  )  e  .  getPath  (  )  .  getLastPathComponent  (  )  ; 
if  (  node  ==  null  )  { 
return  ; 
} 
try  { 
PDFAction   action  =  node  .  getAction  (  )  ; 
if  (  action  ==  null  )  { 
return  ; 
} 
if  (  action   instanceof   GoToAction  )  { 
PDFDestination   dest  =  (  (  GoToAction  )  action  )  .  getDestination  (  )  ; 
if  (  dest  ==  null  )  { 
return  ; 
} 
PDFObject   dest_page  =  dest  .  getPage  (  )  ; 
if  (  dest_page  ==  null  )  { 
return  ; 
} 
int   pageNum  =  curFile  .  getPageNumber  (  dest_page  )  ; 
if  (  pageNum  >=  0  )  { 
gotoPage  (  pageNum  )  ; 
} 
} 
}  catch  (  IOException   ioe  )  { 
ioe  .  printStackTrace  (  )  ; 
} 
} 
} 
} 

