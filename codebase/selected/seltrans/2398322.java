package   org  .  hmaciel  .  descop  .  otros  ; 

import   java  .  security  .  MessageDigest  ; 

public   class   XMLUtils  { 

public   static   String   parse  (  String   s  )  { 
return   s  .  replaceAll  (  "&"  ,  "&amp;"  )  .  replaceAll  (  "<"  ,  "&lt;"  )  .  replaceAll  (  ">"  ,  "&gt;"  )  .  replaceAll  (  "\""  ,  "&quot;"  )  .  replaceAll  (  "'"  ,  "&#39;"  )  ; 
} 

private   static   String   convertToHex  (  byte  [  ]  data  )  { 
StringBuffer   buf  =  new   StringBuffer  (  )  ; 
for  (  int   i  =  0  ;  i  <  data  .  length  ;  i  ++  )  { 
int   halfbyte  =  (  data  [  i  ]  >  >  >  4  )  &  0x0F  ; 
int   two_halfs  =  0  ; 
do  { 
if  (  (  0  <=  halfbyte  )  &&  (  halfbyte  <=  9  )  )  buf  .  append  (  (  char  )  (  '0'  +  halfbyte  )  )  ;  else   buf  .  append  (  (  char  )  (  'a'  +  (  halfbyte  -  10  )  )  )  ; 
halfbyte  =  data  [  i  ]  &  0x0F  ; 
}  while  (  two_halfs  ++  <  1  )  ; 
} 
return   buf  .  toString  (  )  ; 
} 

public   static   String   MD5  (  String   text  )  { 
try  { 
MessageDigest   md  ; 
md  =  MessageDigest  .  getInstance  (  "MD5"  )  ; 
byte  [  ]  md5hash  =  new   byte  [  32  ]  ; 
md  .  update  (  text  .  getBytes  (  "iso-8859-1"  )  ,  0  ,  text  .  length  (  )  )  ; 
md5hash  =  md  .  digest  (  )  ; 
return   convertToHex  (  md5hash  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  .  toString  (  )  )  ; 
} 
return   null  ; 
} 
} 

