package   prefuse  .  util  .  force  ; 

import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Iterator  ; 
import   java  .  util  .  Random  ; 




















public   class   NBodyForce   extends   AbstractForce  { 

private   static   String  [  ]  pnames  =  new   String  [  ]  {  "GravitationalConstant"  ,  "Distance"  ,  "BarnesHutTheta"  }  ; 

public   static   final   float   DEFAULT_GRAV_CONSTANT  =  -  1.0f  ; 

public   static   final   float   DEFAULT_MIN_GRAV_CONSTANT  =  -  10f  ; 

public   static   final   float   DEFAULT_MAX_GRAV_CONSTANT  =  10f  ; 

public   static   final   float   DEFAULT_DISTANCE  =  -  1f  ; 

public   static   final   float   DEFAULT_MIN_DISTANCE  =  -  1f  ; 

public   static   final   float   DEFAULT_MAX_DISTANCE  =  500f  ; 

public   static   final   float   DEFAULT_THETA  =  0.9f  ; 

public   static   final   float   DEFAULT_MIN_THETA  =  0.0f  ; 

public   static   final   float   DEFAULT_MAX_THETA  =  1.0f  ; 

public   static   final   int   GRAVITATIONAL_CONST  =  0  ; 

public   static   final   int   MIN_DISTANCE  =  1  ; 

public   static   final   int   BARNES_HUT_THETA  =  2  ; 

private   float   xMin  ,  xMax  ,  yMin  ,  yMax  ; 

private   QuadTreeNodeFactory   factory  =  new   QuadTreeNodeFactory  (  )  ; 

private   QuadTreeNode   root  ; 

private   Random   rand  =  new   Random  (  12345678L  )  ; 




public   NBodyForce  (  )  { 
this  (  DEFAULT_GRAV_CONSTANT  ,  DEFAULT_DISTANCE  ,  DEFAULT_THETA  )  ; 
} 












public   NBodyForce  (  float   gravConstant  ,  float   minDistance  ,  float   theta  )  { 
params  =  new   float  [  ]  {  gravConstant  ,  minDistance  ,  theta  }  ; 
minValues  =  new   float  [  ]  {  DEFAULT_MIN_GRAV_CONSTANT  ,  DEFAULT_MIN_DISTANCE  ,  DEFAULT_MIN_THETA  }  ; 
maxValues  =  new   float  [  ]  {  DEFAULT_MAX_GRAV_CONSTANT  ,  DEFAULT_MAX_DISTANCE  ,  DEFAULT_MAX_THETA  }  ; 
root  =  factory  .  getQuadTreeNode  (  )  ; 
} 





public   boolean   isItemForce  (  )  { 
return   true  ; 
} 




protected   String  [  ]  getParameterNames  (  )  { 
return   pnames  ; 
} 








private   void   setBounds  (  float   xMin  ,  float   yMin  ,  float   xMax  ,  float   yMax  )  { 
this  .  xMin  =  xMin  ; 
this  .  yMin  =  yMin  ; 
this  .  xMax  =  xMax  ; 
this  .  yMax  =  yMax  ; 
} 




public   void   clear  (  )  { 
clearHelper  (  root  )  ; 
root  =  factory  .  getQuadTreeNode  (  )  ; 
} 

private   void   clearHelper  (  QuadTreeNode   n  )  { 
for  (  int   i  =  0  ;  i  <  n  .  children  .  length  ;  i  ++  )  { 
if  (  n  .  children  [  i  ]  !=  null  )  clearHelper  (  n  .  children  [  i  ]  )  ; 
} 
factory  .  reclaim  (  n  )  ; 
} 







public   void   init  (  ForceSimulator   fsim  )  { 
clear  (  )  ; 
float   x1  =  Float  .  MAX_VALUE  ,  y1  =  Float  .  MAX_VALUE  ; 
float   x2  =  Float  .  MIN_VALUE  ,  y2  =  Float  .  MIN_VALUE  ; 
Iterator   itemIter  =  fsim  .  getItems  (  )  ; 
while  (  itemIter  .  hasNext  (  )  )  { 
ForceItem   item  =  (  ForceItem  )  itemIter  .  next  (  )  ; 
float   x  =  item  .  location  [  0  ]  ; 
float   y  =  item  .  location  [  1  ]  ; 
if  (  x  <  x1  )  x1  =  x  ; 
if  (  y  <  y1  )  y1  =  y  ; 
if  (  x  >  x2  )  x2  =  x  ; 
if  (  y  >  y2  )  y2  =  y  ; 
} 
float   dx  =  x2  -  x1  ,  dy  =  y2  -  y1  ; 
if  (  dx  >  dy  )  { 
y2  =  y1  +  dx  ; 
}  else  { 
x2  =  x1  +  dy  ; 
} 
setBounds  (  x1  ,  y1  ,  x2  ,  y2  )  ; 
synchronized  (  this  )  { 
itemIter  =  fsim  .  getItems  (  )  ; 
while  (  itemIter  .  hasNext  (  )  )  { 
ForceItem   item  =  (  ForceItem  )  itemIter  .  next  (  )  ; 
insert  (  item  )  ; 
} 
} 
calcMass  (  root  )  ; 
} 







public   void   insert  (  ForceItem   item  )  { 
try  { 
insert  (  item  ,  root  ,  xMin  ,  yMin  ,  xMax  ,  yMax  )  ; 
}  catch  (  StackOverflowError   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

private   void   insert  (  ForceItem   p  ,  QuadTreeNode   n  ,  float   x1  ,  float   y1  ,  float   x2  ,  float   y2  )  { 
if  (  n  .  hasChildren  )  { 
insertHelper  (  p  ,  n  ,  x1  ,  y1  ,  x2  ,  y2  )  ; 
}  else   if  (  n  .  value  !=  null  )  { 
if  (  isSameLocation  (  n  .  value  ,  p  )  )  { 
insertHelper  (  p  ,  n  ,  x1  ,  y1  ,  x2  ,  y2  )  ; 
}  else  { 
ForceItem   v  =  n  .  value  ; 
n  .  value  =  null  ; 
insertHelper  (  v  ,  n  ,  x1  ,  y1  ,  x2  ,  y2  )  ; 
insertHelper  (  p  ,  n  ,  x1  ,  y1  ,  x2  ,  y2  )  ; 
} 
}  else  { 
n  .  value  =  p  ; 
} 
} 

private   static   boolean   isSameLocation  (  ForceItem   f1  ,  ForceItem   f2  )  { 
float   dx  =  Math  .  abs  (  f1  .  location  [  0  ]  -  f2  .  location  [  0  ]  )  ; 
float   dy  =  Math  .  abs  (  f1  .  location  [  1  ]  -  f2  .  location  [  1  ]  )  ; 
return  (  dx  <  0.01  &&  dy  <  0.01  )  ; 
} 

private   void   insertHelper  (  ForceItem   p  ,  QuadTreeNode   n  ,  float   x1  ,  float   y1  ,  float   x2  ,  float   y2  )  { 
float   x  =  p  .  location  [  0  ]  ,  y  =  p  .  location  [  1  ]  ; 
float   splitx  =  (  x1  +  x2  )  /  2  ; 
float   splity  =  (  y1  +  y2  )  /  2  ; 
int   i  =  (  x  >=  splitx  ?  1  :  0  )  +  (  y  >=  splity  ?  2  :  0  )  ; 
if  (  n  .  children  [  i  ]  ==  null  )  { 
n  .  children  [  i  ]  =  factory  .  getQuadTreeNode  (  )  ; 
n  .  hasChildren  =  true  ; 
} 
if  (  i  ==  1  ||  i  ==  3  )  x1  =  splitx  ;  else   x2  =  splitx  ; 
if  (  i  >  1  )  y1  =  splity  ;  else   y2  =  splity  ; 
insert  (  p  ,  n  .  children  [  i  ]  ,  x1  ,  y1  ,  x2  ,  y2  )  ; 
} 

private   void   calcMass  (  QuadTreeNode   n  )  { 
float   xcom  =  0  ,  ycom  =  0  ; 
n  .  mass  =  0  ; 
if  (  n  .  hasChildren  )  { 
for  (  int   i  =  0  ;  i  <  n  .  children  .  length  ;  i  ++  )  { 
if  (  n  .  children  [  i  ]  !=  null  )  { 
calcMass  (  n  .  children  [  i  ]  )  ; 
n  .  mass  +=  n  .  children  [  i  ]  .  mass  ; 
xcom  +=  n  .  children  [  i  ]  .  mass  *  n  .  children  [  i  ]  .  com  [  0  ]  ; 
ycom  +=  n  .  children  [  i  ]  .  mass  *  n  .  children  [  i  ]  .  com  [  1  ]  ; 
} 
} 
} 
if  (  n  .  value  !=  null  )  { 
n  .  mass  +=  n  .  value  .  mass  ; 
xcom  +=  n  .  value  .  mass  *  n  .  value  .  location  [  0  ]  ; 
ycom  +=  n  .  value  .  mass  *  n  .  value  .  location  [  1  ]  ; 
} 
n  .  com  [  0  ]  =  xcom  /  n  .  mass  ; 
n  .  com  [  1  ]  =  ycom  /  n  .  mass  ; 
} 





public   void   getForce  (  ForceItem   item  )  { 
try  { 
forceHelper  (  item  ,  root  ,  xMin  ,  yMin  ,  xMax  ,  yMax  )  ; 
}  catch  (  StackOverflowError   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

private   void   forceHelper  (  ForceItem   item  ,  QuadTreeNode   n  ,  float   x1  ,  float   y1  ,  float   x2  ,  float   y2  )  { 
float   dx  =  n  .  com  [  0  ]  -  item  .  location  [  0  ]  ; 
float   dy  =  n  .  com  [  1  ]  -  item  .  location  [  1  ]  ; 
float   r  =  (  float  )  Math  .  sqrt  (  dx  *  dx  +  dy  *  dy  )  ; 
boolean   same  =  false  ; 
if  (  r  ==  0.0f  )  { 
dx  =  (  rand  .  nextFloat  (  )  -  0.5f  )  /  50.0f  ; 
dy  =  (  rand  .  nextFloat  (  )  -  0.5f  )  /  50.0f  ; 
r  =  (  float  )  Math  .  sqrt  (  dx  *  dx  +  dy  *  dy  )  ; 
same  =  true  ; 
} 
boolean   minDist  =  params  [  MIN_DISTANCE  ]  >  0f  &&  r  >  params  [  MIN_DISTANCE  ]  ; 
if  (  (  !  n  .  hasChildren  &&  n  .  value  !=  item  )  ||  (  !  same  &&  (  x2  -  x1  )  /  r  <  params  [  BARNES_HUT_THETA  ]  )  )  { 
if  (  minDist  )  return  ; 
float   v  =  params  [  GRAVITATIONAL_CONST  ]  *  item  .  mass  *  n  .  mass  /  (  r  *  r  *  r  )  ; 
item  .  force  [  0  ]  +=  v  *  dx  ; 
item  .  force  [  1  ]  +=  v  *  dy  ; 
}  else   if  (  n  .  hasChildren  )  { 
float   splitx  =  (  x1  +  x2  )  /  2  ; 
float   splity  =  (  y1  +  y2  )  /  2  ; 
for  (  int   i  =  0  ;  i  <  n  .  children  .  length  ;  i  ++  )  { 
if  (  n  .  children  [  i  ]  !=  null  )  { 
forceHelper  (  item  ,  n  .  children  [  i  ]  ,  (  i  ==  1  ||  i  ==  3  ?  splitx  :  x1  )  ,  (  i  >  1  ?  splity  :  y1  )  ,  (  i  ==  1  ||  i  ==  3  ?  x2  :  splitx  )  ,  (  i  >  1  ?  y2  :  splity  )  )  ; 
} 
} 
if  (  minDist  )  return  ; 
if  (  n  .  value  !=  null  &&  n  .  value  !=  item  )  { 
float   v  =  params  [  GRAVITATIONAL_CONST  ]  *  item  .  mass  *  n  .  value  .  mass  /  (  r  *  r  *  r  )  ; 
item  .  force  [  0  ]  +=  v  *  dx  ; 
item  .  force  [  1  ]  +=  v  *  dy  ; 
} 
} 
} 




public   static   final   class   QuadTreeNode  { 

public   QuadTreeNode  (  )  { 
com  =  new   float  [  ]  {  0.0f  ,  0.0f  }  ; 
children  =  new   QuadTreeNode  [  4  ]  ; 
} 

boolean   hasChildren  =  false  ; 

float   mass  ; 

float  [  ]  com  ; 

ForceItem   value  ; 

QuadTreeNode  [  ]  children  ; 
} 





public   static   final   class   QuadTreeNodeFactory  { 

private   int   maxNodes  =  50000  ; 

private   ArrayList  <  QuadTreeNode  >  nodes  =  new   ArrayList  (  )  ; 

public   QuadTreeNode   getQuadTreeNode  (  )  { 
if  (  nodes  .  size  (  )  >  0  )  { 
return  (  QuadTreeNode  )  nodes  .  remove  (  nodes  .  size  (  )  -  1  )  ; 
}  else  { 
return   new   QuadTreeNode  (  )  ; 
} 
} 

public   void   reclaim  (  QuadTreeNode   n  )  { 
n  .  mass  =  0  ; 
n  .  com  [  0  ]  =  0.0f  ; 
n  .  com  [  1  ]  =  0.0f  ; 
n  .  value  =  null  ; 
n  .  hasChildren  =  false  ; 
Arrays  .  fill  (  n  .  children  ,  null  )  ; 
if  (  nodes  .  size  (  )  <  maxNodes  )  nodes  .  add  (  n  )  ; 
} 
} 
} 

