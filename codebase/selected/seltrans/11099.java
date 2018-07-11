package   io  ; 

import   ij  .  plugin  .  *  ; 
import   ij  .  *  ; 
import   ij  .  process  .  *  ; 
import   ij  .  io  .  *  ; 
import   java  .  net  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  awt  .  *  ; 
import   java  .  awt  .  image  .  *  ; 

public   class   Animated_Gif_Reader   implements   PlugIn  { 

public   void   run  (  String   arg  )  { 
String   name  ; 
if  (  arg  ==  null  ||  arg  .  equals  (  ""  )  )  { 
OpenDialog   od  =  new   OpenDialog  (  "Animated Gif Reader"  ,  null  )  ; 
name  =  od  .  getFileName  (  )  ; 
if  (  name  ==  null  )  return  ; 
arg  =  od  .  getDirectory  (  )  +  name  ; 
}  else   name  =  arg  .  substring  (  arg  .  lastIndexOf  (  '/'  )  +  1  )  ; 
GifDecoder   d  =  new   GifDecoder  (  )  ; 
int   status  =  d  .  read  (  arg  )  ; 
int   n  =  d  .  getFrameCount  (  )  ; 
if  (  n  ==  0  )  { 
IJ  .  error  (  "It appears that "  +  arg  +  " does not contain any frames"  )  ; 
return  ; 
} 
ImageStack   stack  =  null  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
ImageProcessor   frame  =  d  .  getFrame  (  i  )  ; 
if  (  i  ==  0  )  stack  =  new   ImageStack  (  frame  .  getWidth  (  )  ,  frame  .  getHeight  (  )  )  ; 
int   t  =  d  .  getDelay  (  i  )  ; 
stack  .  addSlice  (  null  ,  frame  )  ; 
} 
if  (  stack  !=  null  )  new   ImagePlus  (  name  ,  stack  )  .  show  (  )  ; 
} 
} 























class   GifDecoder  { 




public   static   final   int   STATUS_OK  =  0  ; 




public   static   final   int   STATUS_FORMAT_ERROR  =  1  ; 




public   static   final   int   STATUS_OPEN_ERROR  =  2  ; 

protected   BufferedInputStream   in  ; 

protected   int   status  ; 

protected   int   width  ; 

protected   int   height  ; 

protected   boolean   gctFlag  ; 

protected   int   gctSize  ; 

protected   int   loopCount  ; 

protected   int  [  ]  gct  ; 

protected   int  [  ]  lct  ; 

protected   int  [  ]  act  ; 

protected   int   bgIndex  ; 

protected   int   bgColor  ; 

protected   int   lastBgColor  ; 

protected   int   pixelAspect  ; 

protected   boolean   lctFlag  ; 

protected   boolean   interlace  ; 

protected   int   lctSize  ; 

protected   int   ix  ,  iy  ,  iw  ,  ih  ; 

protected   Rectangle   lastRect  ; 

protected   ImageProcessor   image  ; 

protected   ImageProcessor   lastImage  ; 

protected   byte  [  ]  block  =  new   byte  [  256  ]  ; 

protected   int   blockSize  =  0  ; 

protected   int   dispose  =  0  ; 

protected   int   lastDispose  =  0  ; 

protected   boolean   transparency  =  false  ; 

protected   int   delay  =  0  ; 

protected   int   transIndex  ; 

protected   static   final   int   MaxStackSize  =  4096  ; 

protected   short  [  ]  prefix  ; 

protected   byte  [  ]  suffix  ; 

protected   byte  [  ]  pixelStack  ; 

protected   byte  [  ]  pixels  ; 

protected   Vector   frames  ; 

protected   int   frameCount  ; 







public   int   getDelay  (  int   n  )  { 
delay  =  -  1  ; 
if  (  (  n  >=  0  )  &&  (  n  <  frameCount  )  )  delay  =  (  (  GifFrame  )  frames  .  elementAt  (  n  )  )  .  delay  ; 
return   delay  ; 
} 






public   ImageProcessor   getFrame  (  int   n  )  { 
ImageProcessor   im  =  null  ; 
if  (  (  n  >=  0  )  &&  (  n  <  frameCount  )  )  im  =  (  (  GifFrame  )  frames  .  elementAt  (  n  )  )  .  image  ; 
return   im  ; 
} 





public   int   getFrameCount  (  )  { 
return   frameCount  ; 
} 






public   ImageProcessor   getImage  (  )  { 
return   getFrame  (  0  )  ; 
} 







public   int   getLoopCount  (  )  { 
return   loopCount  ; 
} 







public   int   read  (  BufferedInputStream   is  )  { 
init  (  )  ; 
if  (  is  !=  null  )  { 
in  =  is  ; 
readHeader  (  )  ; 
if  (  !  err  (  )  )  { 
readContents  (  )  ; 
if  (  frameCount  <  0  )  status  =  STATUS_FORMAT_ERROR  ; 
} 
}  else  { 
status  =  STATUS_OPEN_ERROR  ; 
} 
try  { 
is  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
} 
return   status  ; 
} 







public   int   read  (  String   name  )  { 
status  =  STATUS_OK  ; 
try  { 
name  =  name  .  trim  (  )  ; 
if  (  name  .  indexOf  (  "://"  )  >  0  )  { 
URL   url  =  new   URL  (  name  )  ; 
in  =  new   BufferedInputStream  (  url  .  openStream  (  )  )  ; 
}  else  { 
in  =  new   BufferedInputStream  (  new   FileInputStream  (  name  )  )  ; 
} 
status  =  read  (  in  )  ; 
}  catch  (  IOException   e  )  { 
status  =  STATUS_OPEN_ERROR  ; 
} 
return   status  ; 
} 





protected   void   decodeImageData  (  )  { 
int   NullCode  =  -  1  ; 
int   npix  =  iw  *  ih  ; 
int   available  ,  clear  ,  code_mask  ,  code_size  ,  end_of_information  ,  in_code  ,  old_code  ,  bits  ,  code  ,  count  ,  i  ,  datum  ,  data_size  ,  first  ,  top  ,  bi  ,  pi  ; 
if  (  (  pixels  ==  null  )  ||  (  pixels  .  length  <  npix  )  )  pixels  =  new   byte  [  npix  ]  ; 
if  (  prefix  ==  null  )  prefix  =  new   short  [  MaxStackSize  ]  ; 
if  (  suffix  ==  null  )  suffix  =  new   byte  [  MaxStackSize  ]  ; 
if  (  pixelStack  ==  null  )  pixelStack  =  new   byte  [  MaxStackSize  +  1  ]  ; 
data_size  =  read  (  )  ; 
clear  =  1  <<  data_size  ; 
end_of_information  =  clear  +  1  ; 
available  =  clear  +  2  ; 
old_code  =  NullCode  ; 
code_size  =  data_size  +  1  ; 
code_mask  =  (  1  <<  code_size  )  -  1  ; 
for  (  code  =  0  ;  code  <  clear  ;  code  ++  )  { 
prefix  [  code  ]  =  0  ; 
suffix  [  code  ]  =  (  byte  )  code  ; 
} 
datum  =  bits  =  count  =  first  =  top  =  pi  =  bi  =  0  ; 
for  (  i  =  0  ;  i  <  npix  ;  )  { 
if  (  top  ==  0  )  { 
if  (  bits  <  code_size  )  { 
if  (  count  ==  0  )  { 
count  =  readBlock  (  )  ; 
if  (  count  <=  0  )  break  ; 
bi  =  0  ; 
} 
datum  +=  (  (  (  int  )  block  [  bi  ]  )  &  0xff  )  <<  bits  ; 
bits  +=  8  ; 
bi  ++  ; 
count  --  ; 
continue  ; 
} 
code  =  datum  &  code_mask  ; 
datum  >  >=  code_size  ; 
bits  -=  code_size  ; 
if  (  (  code  >  available  )  ||  (  code  ==  end_of_information  )  )  break  ; 
if  (  code  ==  clear  )  { 
code_size  =  data_size  +  1  ; 
code_mask  =  (  1  <<  code_size  )  -  1  ; 
available  =  clear  +  2  ; 
old_code  =  NullCode  ; 
continue  ; 
} 
if  (  old_code  ==  NullCode  )  { 
pixelStack  [  top  ++  ]  =  suffix  [  code  ]  ; 
old_code  =  code  ; 
first  =  code  ; 
continue  ; 
} 
in_code  =  code  ; 
if  (  code  ==  available  )  { 
pixelStack  [  top  ++  ]  =  (  byte  )  first  ; 
code  =  old_code  ; 
} 
while  (  code  >  clear  )  { 
pixelStack  [  top  ++  ]  =  suffix  [  code  ]  ; 
code  =  prefix  [  code  ]  ; 
} 
first  =  (  (  int  )  suffix  [  code  ]  )  &  0xff  ; 
if  (  available  >=  MaxStackSize  )  break  ; 
pixelStack  [  top  ++  ]  =  (  byte  )  first  ; 
prefix  [  available  ]  =  (  short  )  old_code  ; 
suffix  [  available  ]  =  (  byte  )  first  ; 
available  ++  ; 
if  (  (  (  available  &  code_mask  )  ==  0  )  &&  (  available  <  MaxStackSize  )  )  { 
code_size  ++  ; 
code_mask  +=  available  ; 
} 
old_code  =  in_code  ; 
} 
top  --  ; 
pixels  [  pi  ++  ]  =  pixelStack  [  top  ]  ; 
i  ++  ; 
} 
for  (  i  =  pi  ;  i  <  npix  ;  i  ++  )  pixels  [  i  ]  =  0  ; 
} 




protected   boolean   err  (  )  { 
return   status  !=  STATUS_OK  ; 
} 




protected   void   init  (  )  { 
status  =  STATUS_OK  ; 
frameCount  =  0  ; 
frames  =  new   Vector  (  )  ; 
gct  =  null  ; 
lct  =  null  ; 
} 




protected   int   read  (  )  { 
int   curByte  =  0  ; 
try  { 
curByte  =  in  .  read  (  )  ; 
}  catch  (  IOException   e  )  { 
status  =  STATUS_FORMAT_ERROR  ; 
} 
return   curByte  ; 
} 






protected   int   readBlock  (  )  { 
blockSize  =  read  (  )  ; 
int   n  =  0  ; 
int   count  ; 
if  (  blockSize  >  0  )  { 
try  { 
while  (  n  <  blockSize  )  { 
count  =  in  .  read  (  block  ,  n  ,  blockSize  -  n  )  ; 
if  (  count  ==  -  1  )  break  ; 
n  +=  count  ; 
} 
}  catch  (  IOException   e  )  { 
} 
if  (  n  <  blockSize  )  status  =  STATUS_FORMAT_ERROR  ; 
} 
return   n  ; 
} 







protected   int  [  ]  readColorTable  (  int   ncolors  )  { 
int   nbytes  =  3  *  ncolors  ; 
int  [  ]  tab  =  null  ; 
byte  [  ]  c  =  new   byte  [  nbytes  ]  ; 
int   n  =  0  ; 
try  { 
n  =  in  .  read  (  c  )  ; 
}  catch  (  IOException   e  )  { 
} 
if  (  n  <  nbytes  )  status  =  STATUS_FORMAT_ERROR  ;  else  { 
tab  =  new   int  [  256  ]  ; 
int   i  =  0  ; 
int   j  =  0  ; 
while  (  i  <  ncolors  )  { 
int   r  =  (  (  int  )  c  [  j  ++  ]  )  &  0xff  ; 
int   g  =  (  (  int  )  c  [  j  ++  ]  )  &  0xff  ; 
int   b  =  (  (  int  )  c  [  j  ++  ]  )  &  0xff  ; 
tab  [  i  ++  ]  =  0xff000000  |  (  r  <<  16  )  |  (  g  <<  8  )  |  b  ; 
} 
} 
return   tab  ; 
} 




protected   void   readContents  (  )  { 
boolean   done  =  false  ; 
while  (  !  (  done  ||  err  (  )  )  )  { 
int   code  =  read  (  )  ; 
switch  (  code  )  { 
case   0x2C  : 
readImage  (  )  ; 
break  ; 
case   0x21  : 
code  =  read  (  )  ; 
switch  (  code  )  { 
case   0xf9  : 
readGraphicControlExt  (  )  ; 
break  ; 
case   0xff  : 
readBlock  (  )  ; 
String   app  =  ""  ; 
for  (  int   i  =  0  ;  i  <  11  ;  i  ++  )  app  +=  (  char  )  block  [  i  ]  ; 
if  (  app  .  equals  (  "NETSCAPE2.0"  )  )  readNetscapeExt  (  )  ;  else   skip  (  )  ; 
break  ; 
default  : 
skip  (  )  ; 
} 
break  ; 
case   0x3b  : 
done  =  true  ; 
break  ; 
default  : 
status  =  STATUS_FORMAT_ERROR  ; 
} 
} 
} 




protected   void   readGraphicControlExt  (  )  { 
read  (  )  ; 
int   packed  =  read  (  )  ; 
dispose  =  (  packed  &  0x1c  )  >  >  1  ; 
transparency  =  (  packed  &  1  )  !=  0  ; 
delay  =  readShort  (  )  *  10  ; 
transIndex  =  read  (  )  ; 
read  (  )  ; 
} 




protected   void   readHeader  (  )  { 
String   id  =  ""  ; 
for  (  int   i  =  0  ;  i  <  6  ;  i  ++  )  id  +=  (  char  )  read  (  )  ; 
if  (  !  id  .  startsWith  (  "GIF"  )  )  { 
status  =  STATUS_FORMAT_ERROR  ; 
return  ; 
} 
readLSD  (  )  ; 
if  (  gctFlag  &&  !  err  (  )  )  { 
gct  =  readColorTable  (  gctSize  )  ; 
bgColor  =  gct  [  bgIndex  ]  ; 
} 
} 




protected   void   readImage  (  )  { 
ix  =  readShort  (  )  ; 
iy  =  readShort  (  )  ; 
iw  =  readShort  (  )  ; 
ih  =  readShort  (  )  ; 
int   packed  =  read  (  )  ; 
lctFlag  =  (  packed  &  0x80  )  !=  0  ; 
interlace  =  (  packed  &  0x40  )  !=  0  ; 
lctSize  =  2  <<  (  packed  &  7  )  ; 
if  (  lctFlag  )  { 
lct  =  readColorTable  (  lctSize  )  ; 
act  =  lct  ; 
}  else  { 
act  =  gct  ; 
if  (  bgIndex  ==  transIndex  )  bgColor  =  0  ; 
} 
int   save  =  0  ; 
if  (  transparency  )  { 
save  =  act  [  transIndex  ]  ; 
act  [  transIndex  ]  =  0  ; 
} 
if  (  act  ==  null  )  { 
status  =  STATUS_FORMAT_ERROR  ; 
} 
if  (  err  (  )  )  return  ; 
decodeImageData  (  )  ; 
skip  (  )  ; 
if  (  err  (  )  )  return  ; 
frameCount  ++  ; 
image  =  new   ColorProcessor  (  width  ,  height  )  ; 
setPixels  (  )  ; 
frames  .  addElement  (  new   GifFrame  (  image  ,  delay  )  )  ; 
if  (  transparency  )  act  [  transIndex  ]  =  save  ; 
resetFrame  (  )  ; 
} 




protected   void   readLSD  (  )  { 
width  =  readShort  (  )  ; 
height  =  readShort  (  )  ; 
int   packed  =  read  (  )  ; 
gctFlag  =  (  packed  &  0x80  )  !=  0  ; 
gctSize  =  2  <<  (  packed  &  7  )  ; 
bgIndex  =  read  (  )  ; 
pixelAspect  =  read  (  )  ; 
} 




protected   void   readNetscapeExt  (  )  { 
do  { 
readBlock  (  )  ; 
if  (  block  [  0  ]  ==  0x03  )  { 
int   b1  =  (  (  int  )  block  [  1  ]  )  &  0xff  ; 
int   b2  =  (  (  int  )  block  [  2  ]  )  &  0xff  ; 
loopCount  =  (  b2  <<  8  )  |  b1  ; 
} 
}  while  (  (  blockSize  >  0  )  &&  !  err  (  )  )  ; 
} 




protected   int   readShort  (  )  { 
return   read  (  )  |  (  read  (  )  <<  8  )  ; 
} 




protected   void   resetFrame  (  )  { 
lastDispose  =  dispose  ; 
lastRect  =  new   Rectangle  (  ix  ,  iy  ,  iw  ,  ih  )  ; 
lastImage  =  image  ; 
lastBgColor  =  bgColor  ; 
int   dispose  =  0  ; 
boolean   transparency  =  false  ; 
int   delay  =  0  ; 
lct  =  null  ; 
} 





protected   void   setPixels  (  )  { 
int  [  ]  dest  =  (  int  [  ]  )  image  .  getPixels  (  )  ; 
if  (  lastDispose  >  0  )  { 
if  (  lastDispose  ==  3  )  { 
int   n  =  frameCount  -  2  ; 
if  (  n  >  0  )  lastImage  =  getFrame  (  n  -  1  )  ;  else   lastImage  =  null  ; 
} 
if  (  lastImage  !=  null  )  { 
int  [  ]  prev  =  (  int  [  ]  )  lastImage  .  getPixels  (  )  ; 
System  .  arraycopy  (  prev  ,  0  ,  dest  ,  0  ,  width  *  height  )  ; 
if  (  (  lastDispose  ==  2  )  &&  (  lastBgColor  !=  0  )  )  { 
image  .  setColor  (  new   Color  (  lastBgColor  )  )  ; 
image  .  setRoi  (  lastRect  )  ; 
image  .  fill  (  )  ; 
} 
} 
} 
int   pass  =  1  ; 
int   inc  =  8  ; 
int   iline  =  0  ; 
for  (  int   i  =  0  ;  i  <  ih  ;  i  ++  )  { 
int   line  =  i  ; 
if  (  interlace  )  { 
if  (  iline  >=  ih  )  { 
pass  ++  ; 
switch  (  pass  )  { 
case   2  : 
iline  =  4  ; 
break  ; 
case   3  : 
iline  =  2  ; 
inc  =  4  ; 
break  ; 
case   4  : 
iline  =  1  ; 
inc  =  2  ; 
} 
} 
line  =  iline  ; 
iline  +=  inc  ; 
} 
line  +=  iy  ; 
if  (  line  <  height  )  { 
int   k  =  line  *  width  ; 
int   dx  =  k  +  ix  ; 
int   dlim  =  dx  +  iw  ; 
if  (  (  k  +  width  )  <  dlim  )  dlim  =  k  +  width  ; 
int   sx  =  i  *  iw  ; 
while  (  dx  <  dlim  )  { 
int   index  =  (  (  int  )  pixels  [  sx  ++  ]  )  &  0xff  ; 
dest  [  dx  ++  ]  =  act  [  index  ]  ; 
} 
} 
} 
} 





protected   void   skip  (  )  { 
do  { 
readBlock  (  )  ; 
}  while  (  (  blockSize  >  0  )  &&  !  err  (  )  )  ; 
} 
} 

class   GifFrame  { 

public   GifFrame  (  ImageProcessor   im  ,  int   del  )  { 
image  =  im  ; 
delay  =  del  ; 
} 

public   ImageProcessor   image  ; 

public   int   delay  ; 
} 

