package   hu  .  openig  .  tools  ; 

import   hu  .  openig  .  core  .  Configuration  ; 
import   hu  .  openig  .  utils  .  IOUtils  ; 
import   java  .  awt  .  event  .  ActionEvent  ; 
import   java  .  awt  .  event  .  ActionListener  ; 
import   java  .  io  .  BufferedOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileFilter  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  FilenameFilter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  concurrent  .  ExecutorService  ; 
import   java  .  util  .  concurrent  .  Executors  ; 
import   java  .  util  .  concurrent  .  TimeUnit  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   javax  .  swing  .  BoxLayout  ; 
import   javax  .  swing  .  JButton  ; 
import   javax  .  swing  .  JCheckBox  ; 
import   javax  .  swing  .  JFrame  ; 
import   javax  .  swing  .  JPanel  ; 
import   javax  .  swing  .  SwingWorker  ; 





public   final   class   PackageStuff  { 


private   PackageStuff  (  )  { 
} 





static   void   buildPatch  (  String   version  )  { 
try  { 
ZipOutputStream   zout  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  "open-ig-images-"  +  version  +  ".zip"  )  ,  1024  *  1024  )  )  ; 
try  { 
zout  .  setLevel  (  9  )  ; 
processDirectory  (  ".\\images\\"  ,  ".\\images"  ,  zout  ,  null  )  ; 
}  finally  { 
zout  .  close  (  )  ; 
} 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
try  { 
ZipOutputStream   zout  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  "open-ig-upgrade-"  +  version  +  "2.zip"  )  ,  1024  *  1024  )  )  ; 
try  { 
zout  .  setLevel  (  9  )  ; 
processDirectory  (  ".\\data\\"  ,  ".\\data"  ,  zout  ,  null  )  ; 
}  finally  { 
zout  .  close  (  )  ; 
} 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 





static   void   buildMapEditor  (  String   version  )  { 
try  { 
ZipOutputStream   zout  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  "open-ig-mapeditor-"  +  version  +  ".jar"  )  ,  1024  *  1024  )  )  ; 
try  { 
zout  .  setLevel  (  9  )  ; 
processDirectory  (  ".\\bin\\"  ,  ".\\bin"  ,  zout  ,  null  )  ; 
addFile  (  "META-INF/MANIFEST.MF"  ,  "META-INF/MANIFEST.MF.mapeditor"  ,  zout  )  ; 
}  finally  { 
zout  .  close  (  )  ; 
} 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 





static   void   buildGame  (  String   version  )  { 
try  { 
ZipOutputStream   zout  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  "open-ig-"  +  version  +  ".jar"  )  ,  1024  *  1024  )  )  ; 
try  { 
zout  .  setLevel  (  9  )  ; 
processDirectory  (  ".\\bin\\"  ,  ".\\bin"  ,  zout  ,  new   FilenameFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   dir  ,  String   name  )  { 
name  =  name  .  toLowerCase  (  )  ; 
String   d  =  dir  .  toString  (  )  .  replace  (  '\\'  ,  '/'  )  ; 
if  (  !  d  .  endsWith  (  "/"  )  )  { 
d  +=  "/"  ; 
} 
d  +=  name  ; 
return  !  name  .  contains  (  "splash_medium"  )  &&  !  name  .  contains  (  "launcher_background"  )  &&  !  d  .  contains  (  "/launcher"  )  ; 
} 
}  )  ; 
addFile  (  "META-INF/MANIFEST.MF"  ,  "META-INF/MANIFEST.MF"  ,  zout  )  ; 
}  finally  { 
zout  .  close  (  )  ; 
} 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 




static   void   buildLauncher  (  )  { 
try  { 
ZipOutputStream   zout  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  "open-ig-launcher.jar"  )  ,  1024  *  1024  )  )  ; 
try  { 
zout  .  setLevel  (  9  )  ; 
processDirectory  (  ".\\bin\\"  ,  ".\\bin"  ,  zout  ,  new   FilenameFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   dir  ,  String   name  )  { 
String   d  =  dir  .  toString  (  )  .  replace  (  '\\'  ,  '/'  )  ; 
if  (  !  d  .  endsWith  (  "/"  )  )  { 
d  +=  "/"  ; 
} 
d  +=  name  ; 
return   d  .  contains  (  "hu/openig/launcher"  )  ||  d  .  contains  (  "hu/openig/utils/XElement"  )  ||  d  .  contains  (  "hu/openig/utils/IOUtils"  )  ||  d  .  contains  (  "hu/openig/utils/Parallels"  )  ||  d  .  contains  (  "hu/openig/utils/ImageUtils"  )  ||  d  .  contains  (  "hu/openig/utils/ConsoleWatcher"  )  ||  d  .  contains  (  "hu/openig/gfx/button_medium.png"  )  ||  d  .  contains  (  "hu/openig/gfx/button_medium_pressed.png"  )  ||  d  .  contains  (  "hu/openig/gfx/launcher_background.png"  )  ||  d  .  contains  (  "hu/openig/gfx/hungarian.png"  )  ||  d  .  contains  (  "hu/openig/gfx/english.png"  )  ||  d  .  contains  (  "hu/openig/gfx/german.png"  )  ||  d  .  contains  (  "hu/openig/gfx/loading.gif"  )  ||  d  .  contains  (  "hu/openig/ui/IGButton"  )  ||  d  .  contains  (  "hu/openig/render/RenderTools"  )  ||  d  .  contains  (  "hu/openig/render/GenericMediumButton"  )  ||  d  .  contains  (  "hu/openig/render/GenericButtonRenderer"  )  ; 
} 
}  )  ; 
addFile  (  "META-INF/MANIFEST.MF"  ,  "META-INF/MANIFEST.MF.launcher"  ,  zout  )  ; 
}  finally  { 
zout  .  close  (  )  ; 
} 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 





static   void   buildTestbed  (  String   version  )  { 
try  { 
ZipOutputStream   zout  =  new   ZipOutputStream  (  new   BufferedOutputStream  (  new   FileOutputStream  (  "open-ig-testbed-"  +  version  +  ".jar"  )  ,  1024  *  1024  )  )  ; 
try  { 
zout  .  setLevel  (  9  )  ; 
processDirectory  (  ".\\bin\\"  ,  ".\\bin"  ,  zout  ,  null  )  ; 
addFile  (  "META-INF/MANIFEST.MF"  ,  "META-INF/MANIFEST.MF.testbed"  ,  zout  )  ; 
}  finally  { 
zout  .  close  (  )  ; 
} 
}  catch  (  IOException   ex  )  { 
ex  .  printStackTrace  (  )  ; 
} 
} 








static   void   addFile  (  String   entryName  ,  String   fileName  ,  ZipOutputStream   zout  )  throws   IOException  { 
ZipEntry   mf  =  new   ZipEntry  (  entryName  )  ; 
File   mfm  =  new   File  (  fileName  )  ; 
if  (  mfm  .  canRead  (  )  )  { 
mf  .  setSize  (  mfm  .  length  (  )  )  ; 
mf  .  setTime  (  mfm  .  lastModified  (  )  )  ; 
zout  .  putNextEntry  (  mf  )  ; 
zout  .  write  (  IOUtils  .  load  (  mfm  )  )  ; 
}  else  { 
throw   new   RuntimeException  (  "File not found: "  +  mfm  .  toString  (  )  )  ; 
} 
} 









static   void   processDirectory  (  String   baseDir  ,  String   currentDir  ,  ZipOutputStream   zout  ,  FilenameFilter   filter  )  throws   IOException  { 
File  [  ]  files  =  new   File  (  currentDir  )  .  listFiles  (  new   FileFilter  (  )  { 

@  Override 
public   boolean   accept  (  File   pathname  )  { 
return  !  pathname  .  isHidden  (  )  ; 
} 
}  )  ; 
if  (  files  !=  null  )  { 
for  (  File   f  :  files  )  { 
if  (  f  .  isDirectory  (  )  )  { 
processDirectory  (  baseDir  ,  f  .  getPath  (  )  ,  zout  ,  filter  )  ; 
}  else  { 
String   fpath  =  f  .  getPath  (  )  ; 
String   fpath2  =  fpath  .  substring  (  baseDir  .  length  (  )  )  ; 
if  (  filter  ==  null  ||  filter  .  accept  (  f  .  getParentFile  (  )  ,  f  .  getName  (  )  )  )  { 
System  .  out  .  printf  (  "Adding %s as %s%n"  ,  fpath  ,  fpath2  )  ; 
ZipEntry   ze  =  new   ZipEntry  (  fpath2  .  replace  (  '\\'  ,  '/'  )  )  ; 
ze  .  setSize  (  f  .  length  (  )  )  ; 
ze  .  setTime  (  f  .  lastModified  (  )  )  ; 
zout  .  putNextEntry  (  ze  )  ; 
zout  .  write  (  IOUtils  .  load  (  f  )  )  ; 
} 
} 
} 
} 
} 





public   static   void   main  (  String  [  ]  args  )  throws   Exception  { 
JFrame   f  =  new   JFrame  (  "Build"  )  ; 
f  .  setDefaultCloseOperation  (  JFrame  .  DISPOSE_ON_CLOSE  )  ; 
JPanel   p  =  new   JPanel  (  )  ; 
p  .  setLayout  (  new   BoxLayout  (  p  ,  BoxLayout  .  PAGE_AXIS  )  )  ; 
f  .  getContentPane  (  )  .  add  (  p  )  ; 
final   JCheckBox   cb1  =  new   JCheckBox  (  "Build game"  ,  true  )  ; 
final   JCheckBox   cb2  =  new   JCheckBox  (  "Build launcher"  ,  false  )  ; 
final   JCheckBox   cb3  =  new   JCheckBox  (  "Build patch"  ,  false  )  ; 
final   JButton   run  =  new   JButton  (  "Run"  )  ; 
run  .  addActionListener  (  new   ActionListener  (  )  { 

@  Override 
public   void   actionPerformed  (  ActionEvent   e  )  { 
run  .  setEnabled  (  false  )  ; 
final   boolean   v1  =  cb1  .  isSelected  (  )  ; 
final   boolean   v2  =  cb2  .  isSelected  (  )  ; 
final   boolean   v3  =  cb3  .  isSelected  (  )  ; 
SwingWorker  <  Void  ,  Void  >  sw  =  new   SwingWorker  <  Void  ,  Void  >  (  )  { 

@  Override 
protected   Void   doInBackground  (  )  throws   Exception  { 
final   ExecutorService   exec  =  Executors  .  newFixedThreadPool  (  Runtime  .  getRuntime  (  )  .  availableProcessors  (  )  )  ; 
if  (  v1  )  { 
exec  .  execute  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
buildGame  (  Configuration  .  VERSION  )  ; 
} 
}  )  ; 
} 
if  (  v2  )  { 
exec  .  execute  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
buildLauncher  (  )  ; 
} 
}  )  ; 
} 
if  (  v3  )  { 
exec  .  execute  (  new   Runnable  (  )  { 

@  Override 
public   void   run  (  )  { 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  "yyyyMMdd"  )  ; 
buildPatch  (  sdf  .  format  (  new   Date  (  )  )  +  "a"  )  ; 
} 
}  )  ; 
} 
exec  .  shutdown  (  )  ; 
exec  .  awaitTermination  (  1  ,  TimeUnit  .  DAYS  )  ; 
return   null  ; 
} 

@  Override 
protected   void   done  (  )  { 
run  .  setEnabled  (  true  )  ; 
} 
}  ; 
sw  .  execute  (  )  ; 
} 
}  )  ; 
p  .  add  (  cb1  )  ; 
p  .  add  (  cb2  )  ; 
p  .  add  (  cb3  )  ; 
p  .  add  (  run  )  ; 
f  .  pack  (  )  ; 
f  .  setResizable  (  false  )  ; 
f  .  setLocationRelativeTo  (  null  )  ; 
f  .  setVisible  (  true  )  ; 
} 
} 

