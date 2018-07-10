package   org  .  lateralgm  .  file  ; 

import   static   org  .  lateralgm  .  main  .  Util  .  deRef  ; 
import   java  .  awt  .  image  .  BufferedImage  ; 
import   java  .  awt  .  image  .  DataBufferInt  ; 
import   java  .  awt  .  image  .  DirectColorModel  ; 
import   java  .  awt  .  image  .  WritableRaster  ; 
import   java  .  io  .  ByteArrayOutputStream  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  util  .  zip  .  Deflater  ; 
import   javax  .  imageio  .  ImageIO  ; 
import   org  .  lateralgm  .  resources  .  InstantiableResource  ; 
import   org  .  lateralgm  .  resources  .  Resource  ; 
import   org  .  lateralgm  .  resources  .  ResourceReference  ; 
import   org  .  lateralgm  .  util  .  PropertyMap  ; 

public   class   GmStreamEncoder   extends   StreamEncoder  { 

protected   int   originalPos  =  -  1  ; 

protected   OutputStream   originalStream  ; 

private   int  [  ]  table  =  null  ; 

public   GmStreamEncoder  (  OutputStream   o  )  { 
super  (  o  )  ; 
} 

public   GmStreamEncoder  (  File   f  )  throws   FileNotFoundException  { 
super  (  f  )  ; 
} 

public   GmStreamEncoder  (  String   filePath  )  throws   FileNotFoundException  { 
super  (  filePath  )  ; 
} 

public   void   write  (  byte   b  [  ]  )  throws   IOException  { 
write  (  b  ,  0  ,  b  .  length  )  ; 
} 

public   void   write  (  byte   b  [  ]  ,  int   off  ,  int   len  )  throws   IOException  { 
if  (  table  !=  null  )  { 
for  (  int   i  =  0  ;  i  <  len  ;  i  ++  )  { 
int   t  =  b  [  off  +  i  ]  &  0xFF  ; 
int   x  =  table  [  (  t  +  pos  +  i  )  &  0xFF  ]  ; 
b  [  off  +  i  ]  =  (  byte  )  x  ; 
} 
} 
super  .  write  (  b  ,  off  ,  len  )  ; 
} 

public   void   write  (  int   b  )  throws   IOException  { 
if  (  table  !=  null  )  b  =  table  [  (  b  +  pos  )  &  0xFF  ]  ; 
super  .  write  (  b  )  ; 
} 

public   void   writeStr  (  String   str  )  throws   IOException  { 
write4  (  str  .  length  (  )  )  ; 
write  (  str  .  getBytes  (  GmStreamDecoder  .  CHARSET  )  )  ; 
} 

public   void   writeStr1  (  String   str  )  throws   IOException  { 
write  (  Math  .  min  (  str  .  length  (  )  ,  255  )  )  ; 
write  (  str  .  getBytes  (  GmStreamDecoder  .  CHARSET  )  ,  0  ,  Math  .  min  (  str  .  length  (  )  ,  255  )  )  ; 
} 

public   void   writeBool  (  boolean   val  )  throws   IOException  { 
write4  (  val  ?  1  :  0  )  ; 
} 

public  <  P   extends   Enum  <  P  >  >  void   write4  (  PropertyMap  <  P  >  map  ,  P  ...  keys  )  throws   IOException  { 
for  (  P   key  :  keys  )  write4  (  (  Integer  )  map  .  get  (  key  )  )  ; 
} 

public  <  P   extends   Enum  <  P  >  >  void   writeStr  (  PropertyMap  <  P  >  map  ,  P  ...  keys  )  throws   IOException  { 
for  (  P   key  :  keys  )  writeStr  (  (  String  )  map  .  get  (  key  )  )  ; 
} 

public  <  P   extends   Enum  <  P  >  >  void   writeBool  (  PropertyMap  <  P  >  map  ,  P  ...  keys  )  throws   IOException  { 
for  (  P   key  :  keys  )  writeBool  (  (  Boolean  )  map  .  get  (  key  )  )  ; 
} 

public  <  P   extends   Enum  <  P  >  >  void   writeD  (  PropertyMap  <  P  >  map  ,  P  ...  keys  )  throws   IOException  { 
for  (  P   key  :  keys  )  writeD  (  (  Double  )  map  .  get  (  key  )  )  ; 
} 

public  <  R   extends   Resource  <  R  ,  ?  >  >  void   writeId  (  ResourceReference  <  R  >  id  )  throws   IOException  { 
writeId  (  id  ,  -  1  )  ; 
} 

public  <  R   extends   Resource  <  R  ,  ?  >  >  void   writeId  (  ResourceReference  <  R  >  id  ,  int   noneval  )  throws   IOException  { 
R   r  =  deRef  (  id  )  ; 
if  (  r  !=  null  &&  r   instanceof   InstantiableResource  <  ?  ,  ?  >  )  write4  (  (  (  InstantiableResource  <  ?  ,  ?  >  )  r  )  .  getId  (  )  )  ;  else   write4  (  noneval  )  ; 
} 

public   void   compress  (  byte  [  ]  data  )  throws   IOException  { 
Deflater   compresser  =  new   Deflater  (  )  ; 
compresser  .  setInput  (  data  )  ; 
compresser  .  finish  (  )  ; 
byte  [  ]  buffer  =  new   byte  [  2048  ]  ; 
ByteArrayOutputStream   baos  =  new   ByteArrayOutputStream  (  )  ; 
while  (  !  compresser  .  finished  (  )  )  { 
int   len  =  compresser  .  deflate  (  buffer  )  ; 
baos  .  write  (  buffer  ,  0  ,  len  )  ; 
} 
write4  (  baos  .  size  (  )  )  ; 
write  (  baos  .  toByteArray  (  )  )  ; 
} 

public   void   beginDeflate  (  )  { 
originalStream  =  out  ; 
out  =  new   ByteArrayOutputStream  (  )  ; 
originalPos  =  pos  ; 
pos  =  0  ; 
} 





public   void   endDeflate  (  )  throws   IOException  { 
if  (  originalStream  !=  null  )  { 
flush  (  )  ; 
ByteArrayOutputStream   baos  =  (  ByteArrayOutputStream  )  out  ; 
pos  =  originalPos  ; 
originalPos  =  -  1  ; 
out  =  originalStream  ; 
originalStream  =  null  ; 
compress  (  baos  .  toByteArray  (  )  )  ; 
} 
} 

public   void   writeZlibImage  (  BufferedImage   image  )  throws   IOException  { 
int  [  ]  pixels  =  image  .  getRGB  (  0  ,  0  ,  image  .  getWidth  (  )  ,  image  .  getHeight  (  )  ,  null  ,  0  ,  image  .  getWidth  (  )  )  ; 
DirectColorModel   cm  =  new   DirectColorModel  (  24  ,  0x00FF0000  ,  0x0000FF00  ,  0x000000FF  )  ; 
WritableRaster   raster  =  cm  .  createCompatibleWritableRaster  (  image  .  getWidth  (  )  ,  image  .  getHeight  (  )  )  ; 
int  [  ]  data  =  (  (  DataBufferInt  )  raster  .  getDataBuffer  (  )  )  .  getData  (  )  ; 
final   int   transparentReplacement  =  0xF496A1  ; 
final   int   threshold  =  0x80000000  ; 
for  (  int   i  =  0  ;  i  <  pixels  .  length  ;  i  ++  )  if  (  pixels  [  i  ]  <  threshold  )  data  [  i  ]  =  transparentReplacement  ;  else   data  [  i  ]  =  pixels  [  i  ]  &  0x00FFFFFF  ; 
ByteArrayOutputStream   out  =  new   ByteArrayOutputStream  (  )  ; 
ImageIO  .  write  (  new   BufferedImage  (  cm  ,  raster  ,  false  ,  null  )  ,  "bmp"  ,  out  )  ; 
compress  (  out  .  toByteArray  (  )  )  ; 
} 

public   void   writeBGRAImage  (  BufferedImage   image  ,  boolean   useTransp  )  throws   IOException  { 
int   width  =  image  .  getWidth  (  )  ; 
int   height  =  image  .  getHeight  (  )  ; 
int   pixels  [  ]  =  image  .  getRGB  (  0  ,  0  ,  width  ,  height  ,  null  ,  0  ,  width  )  ; 
write4  (  pixels  .  length  *  4  )  ; 
int   trans  =  image  .  getRGB  (  0  ,  height  -  1  )  &  0x00FFFFFF  ; 
for  (  int   p  =  0  ;  p  <  pixels  .  length  ;  p  ++  )  { 
write  (  pixels  [  p  ]  &  0xFF  )  ; 
write  (  pixels  [  p  ]  >  >  >  8  &  0xFF  )  ; 
write  (  pixels  [  p  ]  >  >  >  16  &  0xFF  )  ; 
if  (  useTransp  &&  (  (  pixels  [  p  ]  &  0x00FFFFFF  )  ==  trans  )  )  write  (  0  )  ;  else   write  (  pixels  [  p  ]  >  >  >  24  )  ; 
} 
} 






public   void   setSeed  (  int   s  )  { 
if  (  s  >=  0  )  table  =  makeEncodeTable  (  s  )  ;  else   table  =  null  ; 
} 

protected   static   int  [  ]  makeEncodeTable  (  int   seed  )  { 
int  [  ]  table  =  new   int  [  256  ]  ; 
int   a  =  6  +  (  seed  %  250  )  ; 
int   b  =  seed  /  250  ; 
for  (  int   i  =  0  ;  i  <  256  ;  i  ++  )  table  [  i  ]  =  i  ; 
for  (  int   i  =  1  ;  i  <  10001  ;  i  ++  )  { 
int   j  =  1  +  (  (  i  *  a  +  b  )  %  254  )  ; 
int   t  =  table  [  j  ]  ; 
table  [  j  ]  =  table  [  j  +  1  ]  ; 
table  [  j  +  1  ]  =  t  ; 
} 
return   table  ; 
} 
} 
