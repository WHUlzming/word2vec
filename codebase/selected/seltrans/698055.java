package   org  .  apache  .  commons  .  jrcs  .  diff  .  myers  ; 

import   org  .  apache  .  commons  .  jrcs  .  diff  .  *  ; 
import   java  .  util  .  *  ; 
















public   class   MyersDiff   implements   DiffAlgorithm  { 

private   static   int   MAXTIME  =  3000  ; 

public   boolean   checkMaxTime  =  false  ; 




public   MyersDiff  (  )  { 
} 

public   void   checkMaxTime  (  boolean   checkMaxTime  )  { 
this  .  checkMaxTime  =  checkMaxTime  ; 
} 




public   Revision   diff  (  Object  [  ]  orig  ,  Object  [  ]  rev  )  throws   DifferentiationFailedException  { 
PathNode   path  =  buildPath  (  orig  ,  rev  )  ; 
return   buildRevision  (  path  ,  orig  ,  rev  )  ; 
} 











public   PathNode   buildPath  (  Object  [  ]  orig  ,  Object  [  ]  rev  )  throws   DifferentiationFailedException  { 
int   N  ; 
int   M  ; 
int   MAX  ; 
int   size  ; 
int   middle  ; 
Map  <  Integer  ,  PathNode  >  diagonal  ; 
PathNode   d_kminus  ; 
PathNode   d_kplus  ; 
PathNode   path  ; 
PathNode   node  ; 
int   kmiddle  ; 
int   kplus  ; 
int   kminus  ; 
PathNode   prev  ; 
int   i  ; 
int   j  ; 
long   startTime  ; 
if  (  orig  ==  null  )  { 
throw   new   IllegalArgumentException  (  "original sequence is null"  )  ; 
} 
if  (  rev  ==  null  )  { 
throw   new   IllegalArgumentException  (  "revised sequence is null"  )  ; 
} 
N  =  orig  .  length  ; 
M  =  rev  .  length  ; 
MAX  =  N  +  M  +  1  ; 
size  =  1  +  2  *  MAX  ; 
middle  =  (  size  +  1  )  /  2  ; 
diagonal  =  new   HashMap  <  Integer  ,  PathNode  >  (  )  ; 
path  =  null  ; 
startTime  =  System  .  currentTimeMillis  (  )  ; 
diagonal  .  put  (  middle  +  1  ,  new   Snake  (  0  ,  -  1  ,  null  )  )  ; 
for  (  int   d  =  0  ;  d  <  MAX  ;  d  ++  )  { 
if  (  checkMaxTime  &&  System  .  currentTimeMillis  (  )  -  startTime  >  MAXTIME  )  { 
throw   new   org  .  jmeld  .  diff  .  MaxTimeExceededException  (  "Algoritm is taking up to much time"  )  ; 
} 
for  (  int   k  =  -  d  ;  k  <=  d  ;  k  +=  2  )  { 
kmiddle  =  middle  +  k  ; 
kplus  =  kmiddle  +  1  ; 
kminus  =  kmiddle  -  1  ; 
prev  =  null  ; 
d_kminus  =  diagonal  .  get  (  kminus  )  ; 
d_kplus  =  diagonal  .  get  (  kplus  )  ; 
if  (  (  k  ==  -  d  )  ||  (  k  !=  d  &&  d_kminus  .  i  <  d_kplus  .  i  )  )  { 
i  =  d_kplus  .  i  ; 
prev  =  d_kplus  ; 
}  else  { 
i  =  d_kminus  .  i  +  1  ; 
prev  =  d_kminus  ; 
} 
diagonal  .  remove  (  kminus  )  ; 
j  =  i  -  k  ; 
node  =  new   DiffNode  (  i  ,  j  ,  prev  )  ; 
while  (  i  <  N  &&  j  <  M  &&  orig  [  i  ]  .  equals  (  rev  [  j  ]  )  )  { 
i  ++  ; 
j  ++  ; 
} 
if  (  i  >  node  .  i  )  { 
node  =  new   Snake  (  i  ,  j  ,  node  )  ; 
} 
diagonal  .  put  (  kmiddle  ,  node  )  ; 
if  (  i  >=  N  &&  j  >=  M  )  { 
return   diagonal  .  get  (  kmiddle  )  ; 
} 
} 
diagonal  .  put  (  middle  +  d  -  1  ,  null  )  ; 
} 
throw   new   DifferentiationFailedException  (  "could not find a diff path"  )  ; 
} 

private   boolean   isEmpty  (  Object   o  )  { 
String   s  ; 
if  (  !  (  o   instanceof   String  )  )  { 
return   false  ; 
} 
s  =  (  String  )  o  ; 
return  (  s  ==  null  ||  s  .  trim  (  )  .  compareTo  (  ""  )  ==  0  )  ; 
} 











public   Revision   buildRevision  (  PathNode   path  ,  Object  [  ]  orig  ,  Object  [  ]  rev  )  { 
if  (  path  ==  null  )  { 
throw   new   IllegalArgumentException  (  "path is null"  )  ; 
} 
if  (  orig  ==  null  )  { 
throw   new   IllegalArgumentException  (  "original sequence is null"  )  ; 
} 
if  (  rev  ==  null  )  { 
throw   new   IllegalArgumentException  (  "revised sequence is null"  )  ; 
} 
Revision   revision  =  new   Revision  (  )  ; 
if  (  path  .  isSnake  (  )  )  { 
path  =  path  .  prev  ; 
} 
while  (  path  !=  null  &&  path  .  prev  !=  null  &&  path  .  prev  .  j  >=  0  )  { 
if  (  path  .  isSnake  (  )  )  { 
throw   new   IllegalStateException  (  "bad diffpath: found snake when looking for diff"  )  ; 
} 
int   i  =  path  .  i  ; 
int   j  =  path  .  j  ; 
path  =  path  .  prev  ; 
int   ianchor  =  path  .  i  ; 
int   janchor  =  path  .  j  ; 
Delta   delta  =  Delta  .  newDelta  (  new   Chunk  (  orig  ,  ianchor  ,  i  -  ianchor  )  ,  new   Chunk  (  rev  ,  janchor  ,  j  -  janchor  )  )  ; 
revision  .  insertDelta  (  delta  )  ; 
if  (  path  .  isSnake  (  )  )  { 
path  =  path  .  prev  ; 
} 
} 
return   revision  ; 
} 
} 

