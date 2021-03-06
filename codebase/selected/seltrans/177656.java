package   org  .  cishell  .  templates  .  dataset  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  net  .  URL  ; 
import   java  .  nio  .  channels  .  Channels  ; 
import   java  .  nio  .  channels  .  FileChannel  ; 
import   java  .  nio  .  channels  .  ReadableByteChannel  ; 
import   java  .  util  .  Dictionary  ; 
import   org  .  cishell  .  framework  .  CIShellContext  ; 
import   org  .  cishell  .  framework  .  algorithm  .  Algorithm  ; 
import   org  .  cishell  .  framework  .  algorithm  .  AlgorithmExecutionException  ; 
import   org  .  cishell  .  framework  .  algorithm  .  AlgorithmFactory  ; 
import   org  .  cishell  .  framework  .  data  .  BasicData  ; 
import   org  .  cishell  .  framework  .  data  .  Data  ; 
import   org  .  cishell  .  framework  .  data  .  DataProperty  ; 
import   org  .  cishell  .  templates  .  Activator  ; 
import   org  .  osgi  .  framework  .  BundleContext  ; 
import   org  .  osgi  .  service  .  component  .  ComponentContext  ; 
import   org  .  osgi  .  service  .  metatype  .  MetaTypeProvider  ; 





public   class   DatasetFactory   implements   AlgorithmFactory  { 

protected   File   datasetFile  ; 

protected   BundleContext   bContext  ; 

protected   String   datasetPath  ; 

protected   String   format  ; 

protected   String   label  ; 

protected   void   activate  (  ComponentContext   ctxt  )  { 
try  { 
bContext  =  ctxt  .  getBundleContext  (  )  ; 
datasetPath  =  (  String  )  ctxt  .  getProperties  (  )  .  get  (  "dataset"  )  ; 
format  =  (  String  )  ctxt  .  getProperties  (  )  .  get  (  "out_data"  )  ; 
label  =  (  String  )  ctxt  .  getProperties  (  )  .  get  (  "label"  )  ; 
}  catch  (  Throwable   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

protected   void   deactivate  (  ComponentContext   ctxt  )  { 
bContext  =  null  ; 
datasetPath  =  null  ; 
datasetFile  =  null  ; 
format  =  null  ; 
label  =  null  ; 
} 




public   Algorithm   createAlgorithm  (  Data  [  ]  data  ,  Dictionary   parameters  ,  CIShellContext   context  )  { 
return   new   DatasetAlgorithm  (  context  )  ; 
} 




public   MetaTypeProvider   createParameters  (  Data  [  ]  data  )  { 
return   null  ; 
} 

private   class   DatasetAlgorithm   implements   Algorithm  { 

CIShellContext   ciContext  ; 

public   DatasetAlgorithm  (  CIShellContext   ciContext  )  { 
this  .  ciContext  =  ciContext  ; 
} 

public   Data  [  ]  execute  (  )  throws   AlgorithmExecutionException  { 
try  { 
Data   data  =  new   BasicData  (  getDataset  (  )  ,  format  )  ; 
data  .  getMetadata  (  )  .  put  (  DataProperty  .  LABEL  ,  label  )  ; 
return   new   Data  [  ]  {  data  }  ; 
}  catch  (  IOException   e  )  { 
throw   new   AlgorithmExecutionException  (  e  )  ; 
} 
} 
} 

protected   File   getDataset  (  )  throws   IOException  { 
if  (  datasetFile  ==  null  ||  !  datasetFile  .  exists  (  )  )  { 
datasetFile  =  copyFile  (  Activator  .  getTempDirectory  (  )  ,  datasetPath  )  ; 
} 
return   datasetFile  ; 
} 

private   File   copyFile  (  File   dir  ,  String   path  )  throws   IOException  { 
URL   entry  =  bContext  .  getBundle  (  )  .  getEntry  (  path  )  ; 
path  =  path  .  replace  (  '/'  ,  File  .  separatorChar  )  ; 
File   outFile  =  File  .  createTempFile  (  getName  (  path  )  +  "-"  ,  ".tmp"  ,  dir  )  ; 
FileOutputStream   outStream  =  new   FileOutputStream  (  outFile  )  ; 
ReadableByteChannel   in  =  Channels  .  newChannel  (  entry  .  openStream  (  )  )  ; 
FileChannel   out  =  outStream  .  getChannel  (  )  ; 
out  .  transferFrom  (  in  ,  0  ,  Integer  .  MAX_VALUE  )  ; 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
return   outFile  ; 
} 

private   String   getName  (  String   path  )  { 
if  (  path  .  lastIndexOf  (  File  .  separator  )  ==  path  .  length  (  )  -  1  )  { 
path  =  path  .  substring  (  0  ,  path  .  length  (  )  -  1  )  ; 
} 
path  =  path  .  substring  (  path  .  lastIndexOf  (  File  .  separatorChar  )  +  1  ,  path  .  length  (  )  )  ; 
return   path  ; 
} 
} 

