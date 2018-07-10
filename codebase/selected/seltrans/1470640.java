package   org  .  apache  .  lucene  .  search  .  function  ; 

import   java  .  io  .  IOException  ; 
import   java  .  util  .  Set  ; 
import   org  .  apache  .  lucene  .  index  .  IndexReader  ; 
import   org  .  apache  .  lucene  .  search  .  ComplexExplanation  ; 
import   org  .  apache  .  lucene  .  search  .  Explanation  ; 
import   org  .  apache  .  lucene  .  search  .  Query  ; 
import   org  .  apache  .  lucene  .  search  .  Scorer  ; 
import   org  .  apache  .  lucene  .  search  .  Searcher  ; 
import   org  .  apache  .  lucene  .  search  .  Similarity  ; 
import   org  .  apache  .  lucene  .  search  .  Weight  ; 
import   org  .  apache  .  lucene  .  util  .  ToStringUtils  ; 
















public   class   CustomScoreQuery   extends   Query  { 

private   Query   subQuery  ; 

private   ValueSourceQuery  [  ]  valSrcQueries  ; 

private   boolean   strict  =  false  ; 





public   CustomScoreQuery  (  Query   subQuery  )  { 
this  (  subQuery  ,  new   ValueSourceQuery  [  0  ]  )  ; 
} 









public   CustomScoreQuery  (  Query   subQuery  ,  ValueSourceQuery   valSrcQuery  )  { 
this  (  subQuery  ,  valSrcQuery  !=  null  ?  new   ValueSourceQuery  [  ]  {  valSrcQuery  }  :  new   ValueSourceQuery  [  0  ]  )  ; 
} 









public   CustomScoreQuery  (  Query   subQuery  ,  ValueSourceQuery   valSrcQueries  [  ]  )  { 
super  (  )  ; 
this  .  subQuery  =  subQuery  ; 
this  .  valSrcQueries  =  valSrcQueries  !=  null  ?  valSrcQueries  :  new   ValueSourceQuery  [  0  ]  ; 
if  (  subQuery  ==  null  )  throw   new   IllegalArgumentException  (  "<subquery> must not be null!"  )  ; 
} 

public   Query   rewrite  (  IndexReader   reader  )  throws   IOException  { 
subQuery  =  subQuery  .  rewrite  (  reader  )  ; 
for  (  int   i  =  0  ;  i  <  valSrcQueries  .  length  ;  i  ++  )  { 
valSrcQueries  [  i  ]  =  (  ValueSourceQuery  )  valSrcQueries  [  i  ]  .  rewrite  (  reader  )  ; 
} 
return   this  ; 
} 

public   void   extractTerms  (  Set   terms  )  { 
subQuery  .  extractTerms  (  terms  )  ; 
for  (  int   i  =  0  ;  i  <  valSrcQueries  .  length  ;  i  ++  )  { 
valSrcQueries  [  i  ]  .  extractTerms  (  terms  )  ; 
} 
} 

public   Object   clone  (  )  { 
CustomScoreQuery   clone  =  (  CustomScoreQuery  )  super  .  clone  (  )  ; 
clone  .  subQuery  =  (  Query  )  subQuery  .  clone  (  )  ; 
clone  .  valSrcQueries  =  new   ValueSourceQuery  [  valSrcQueries  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  valSrcQueries  .  length  ;  i  ++  )  { 
clone  .  valSrcQueries  [  i  ]  =  (  ValueSourceQuery  )  valSrcQueries  [  i  ]  .  clone  (  )  ; 
} 
return   clone  ; 
} 

public   String   toString  (  String   field  )  { 
StringBuffer   sb  =  new   StringBuffer  (  name  (  )  )  .  append  (  "("  )  ; 
sb  .  append  (  subQuery  .  toString  (  field  )  )  ; 
for  (  int   i  =  0  ;  i  <  valSrcQueries  .  length  ;  i  ++  )  { 
sb  .  append  (  ", "  )  .  append  (  valSrcQueries  [  i  ]  .  toString  (  field  )  )  ; 
} 
sb  .  append  (  ")"  )  ; 
sb  .  append  (  strict  ?  " STRICT"  :  ""  )  ; 
return   sb  .  toString  (  )  +  ToStringUtils  .  boost  (  getBoost  (  )  )  ; 
} 


public   boolean   equals  (  Object   o  )  { 
if  (  getClass  (  )  !=  o  .  getClass  (  )  )  { 
return   false  ; 
} 
CustomScoreQuery   other  =  (  CustomScoreQuery  )  o  ; 
if  (  this  .  getBoost  (  )  !=  other  .  getBoost  (  )  ||  !  this  .  subQuery  .  equals  (  other  .  subQuery  )  ||  this  .  valSrcQueries  .  length  !=  other  .  valSrcQueries  .  length  )  { 
return   false  ; 
} 
for  (  int   i  =  0  ;  i  <  valSrcQueries  .  length  ;  i  ++  )  { 
if  (  !  valSrcQueries  [  i  ]  .  equals  (  other  .  valSrcQueries  [  i  ]  )  )  { 
return   false  ; 
} 
} 
return   true  ; 
} 


public   int   hashCode  (  )  { 
int   valSrcHash  =  0  ; 
for  (  int   i  =  0  ;  i  <  valSrcQueries  .  length  ;  i  ++  )  { 
valSrcHash  +=  valSrcQueries  [  i  ]  .  hashCode  (  )  ; 
} 
return  (  getClass  (  )  .  hashCode  (  )  +  subQuery  .  hashCode  (  )  +  valSrcHash  )  ^  Float  .  floatToIntBits  (  getBoost  (  )  )  ; 
} 
























public   float   customScore  (  int   doc  ,  float   subQueryScore  ,  float   valSrcScores  [  ]  )  { 
if  (  valSrcScores  .  length  ==  1  )  { 
return   customScore  (  doc  ,  subQueryScore  ,  valSrcScores  [  0  ]  )  ; 
} 
if  (  valSrcScores  .  length  ==  0  )  { 
return   customScore  (  doc  ,  subQueryScore  ,  1  )  ; 
} 
float   score  =  subQueryScore  ; 
for  (  int   i  =  0  ;  i  <  valSrcScores  .  length  ;  i  ++  )  { 
score  *=  valSrcScores  [  i  ]  ; 
} 
return   score  ; 
} 





















public   float   customScore  (  int   doc  ,  float   subQueryScore  ,  float   valSrcScore  )  { 
return   subQueryScore  *  valSrcScore  ; 
} 












public   Explanation   customExplain  (  int   doc  ,  Explanation   subQueryExpl  ,  Explanation   valSrcExpls  [  ]  )  { 
if  (  valSrcExpls  .  length  ==  1  )  { 
return   customExplain  (  doc  ,  subQueryExpl  ,  valSrcExpls  [  0  ]  )  ; 
} 
if  (  valSrcExpls  .  length  ==  0  )  { 
return   subQueryExpl  ; 
} 
float   valSrcScore  =  1  ; 
for  (  int   i  =  0  ;  i  <  valSrcExpls  .  length  ;  i  ++  )  { 
valSrcScore  *=  valSrcExpls  [  i  ]  .  getValue  (  )  ; 
} 
Explanation   exp  =  new   Explanation  (  valSrcScore  *  subQueryExpl  .  getValue  (  )  ,  "custom score: product of:"  )  ; 
exp  .  addDetail  (  subQueryExpl  )  ; 
for  (  int   i  =  0  ;  i  <  valSrcExpls  .  length  ;  i  ++  )  { 
exp  .  addDetail  (  valSrcExpls  [  i  ]  )  ; 
} 
return   exp  ; 
} 












public   Explanation   customExplain  (  int   doc  ,  Explanation   subQueryExpl  ,  Explanation   valSrcExpl  )  { 
float   valSrcScore  =  1  ; 
if  (  valSrcExpl  !=  null  )  { 
valSrcScore  *=  valSrcExpl  .  getValue  (  )  ; 
} 
Explanation   exp  =  new   Explanation  (  valSrcScore  *  subQueryExpl  .  getValue  (  )  ,  "custom score: product of:"  )  ; 
exp  .  addDetail  (  subQueryExpl  )  ; 
exp  .  addDetail  (  valSrcExpl  )  ; 
return   exp  ; 
} 

private   class   CustomWeight   implements   Weight  { 

Similarity   similarity  ; 

Weight   subQueryWeight  ; 

Weight  [  ]  valSrcWeights  ; 

boolean   qStrict  ; 

public   CustomWeight  (  Searcher   searcher  )  throws   IOException  { 
this  .  similarity  =  getSimilarity  (  searcher  )  ; 
this  .  subQueryWeight  =  subQuery  .  weight  (  searcher  )  ; 
this  .  subQueryWeight  =  subQuery  .  weight  (  searcher  )  ; 
this  .  valSrcWeights  =  new   Weight  [  valSrcQueries  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  valSrcQueries  .  length  ;  i  ++  )  { 
this  .  valSrcWeights  [  i  ]  =  valSrcQueries  [  i  ]  .  createWeight  (  searcher  )  ; 
} 
this  .  qStrict  =  strict  ; 
} 

public   Query   getQuery  (  )  { 
return   CustomScoreQuery  .  this  ; 
} 

public   float   getValue  (  )  { 
return   getBoost  (  )  ; 
} 

public   float   sumOfSquaredWeights  (  )  throws   IOException  { 
float   sum  =  subQueryWeight  .  sumOfSquaredWeights  (  )  ; 
for  (  int   i  =  0  ;  i  <  valSrcWeights  .  length  ;  i  ++  )  { 
if  (  qStrict  )  { 
valSrcWeights  [  i  ]  .  sumOfSquaredWeights  (  )  ; 
}  else  { 
sum  +=  valSrcWeights  [  i  ]  .  sumOfSquaredWeights  (  )  ; 
} 
} 
sum  *=  getBoost  (  )  *  getBoost  (  )  ; 
return   sum  ; 
} 

public   void   normalize  (  float   norm  )  { 
norm  *=  getBoost  (  )  ; 
subQueryWeight  .  normalize  (  norm  )  ; 
for  (  int   i  =  0  ;  i  <  valSrcWeights  .  length  ;  i  ++  )  { 
if  (  qStrict  )  { 
valSrcWeights  [  i  ]  .  normalize  (  1  )  ; 
}  else  { 
valSrcWeights  [  i  ]  .  normalize  (  norm  )  ; 
} 
} 
} 

public   Scorer   scorer  (  IndexReader   reader  )  throws   IOException  { 
Scorer   subQueryScorer  =  subQueryWeight  .  scorer  (  reader  )  ; 
Scorer  [  ]  valSrcScorers  =  new   Scorer  [  valSrcWeights  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  valSrcScorers  .  length  ;  i  ++  )  { 
valSrcScorers  [  i  ]  =  valSrcWeights  [  i  ]  .  scorer  (  reader  )  ; 
} 
return   new   CustomScorer  (  similarity  ,  reader  ,  this  ,  subQueryScorer  ,  valSrcScorers  )  ; 
} 

public   Explanation   explain  (  IndexReader   reader  ,  int   doc  )  throws   IOException  { 
return   scorer  (  reader  )  .  explain  (  doc  )  ; 
} 
} 




private   class   CustomScorer   extends   Scorer  { 

private   final   CustomWeight   weight  ; 

private   final   float   qWeight  ; 

private   Scorer   subQueryScorer  ; 

private   Scorer  [  ]  valSrcScorers  ; 

private   IndexReader   reader  ; 

private   float   vScores  [  ]  ; 

private   CustomScorer  (  Similarity   similarity  ,  IndexReader   reader  ,  CustomWeight   w  ,  Scorer   subQueryScorer  ,  Scorer  [  ]  valSrcScorers  )  throws   IOException  { 
super  (  similarity  )  ; 
this  .  weight  =  w  ; 
this  .  qWeight  =  w  .  getValue  (  )  ; 
this  .  subQueryScorer  =  subQueryScorer  ; 
this  .  valSrcScorers  =  valSrcScorers  ; 
this  .  reader  =  reader  ; 
this  .  vScores  =  new   float  [  valSrcScorers  .  length  ]  ; 
} 

public   boolean   next  (  )  throws   IOException  { 
boolean   hasNext  =  subQueryScorer  .  next  (  )  ; 
if  (  hasNext  )  { 
for  (  int   i  =  0  ;  i  <  valSrcScorers  .  length  ;  i  ++  )  { 
valSrcScorers  [  i  ]  .  skipTo  (  subQueryScorer  .  doc  (  )  )  ; 
} 
} 
return   hasNext  ; 
} 

public   int   doc  (  )  { 
return   subQueryScorer  .  doc  (  )  ; 
} 

public   float   score  (  )  throws   IOException  { 
for  (  int   i  =  0  ;  i  <  valSrcScorers  .  length  ;  i  ++  )  { 
vScores  [  i  ]  =  valSrcScorers  [  i  ]  .  score  (  )  ; 
} 
return   qWeight  *  customScore  (  subQueryScorer  .  doc  (  )  ,  subQueryScorer  .  score  (  )  ,  vScores  )  ; 
} 

public   boolean   skipTo  (  int   target  )  throws   IOException  { 
boolean   hasNext  =  subQueryScorer  .  skipTo  (  target  )  ; 
if  (  hasNext  )  { 
for  (  int   i  =  0  ;  i  <  valSrcScorers  .  length  ;  i  ++  )  { 
valSrcScorers  [  i  ]  .  skipTo  (  subQueryScorer  .  doc  (  )  )  ; 
} 
} 
return   hasNext  ; 
} 

public   Explanation   explain  (  int   doc  )  throws   IOException  { 
Explanation   subQueryExpl  =  weight  .  subQueryWeight  .  explain  (  reader  ,  doc  )  ; 
if  (  !  subQueryExpl  .  isMatch  (  )  )  { 
return   subQueryExpl  ; 
} 
Explanation  [  ]  valSrcExpls  =  new   Explanation  [  valSrcScorers  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  valSrcScorers  .  length  ;  i  ++  )  { 
valSrcExpls  [  i  ]  =  valSrcScorers  [  i  ]  .  explain  (  doc  )  ; 
} 
Explanation   customExp  =  customExplain  (  doc  ,  subQueryExpl  ,  valSrcExpls  )  ; 
float   sc  =  qWeight  *  customExp  .  getValue  (  )  ; 
Explanation   res  =  new   ComplexExplanation  (  true  ,  sc  ,  CustomScoreQuery  .  this  .  toString  (  )  +  ", product of:"  )  ; 
res  .  addDetail  (  customExp  )  ; 
res  .  addDetail  (  new   Explanation  (  qWeight  ,  "queryBoost"  )  )  ; 
return   res  ; 
} 
} 

protected   Weight   createWeight  (  Searcher   searcher  )  throws   IOException  { 
return   new   CustomWeight  (  searcher  )  ; 
} 










public   boolean   isStrict  (  )  { 
return   strict  ; 
} 






public   void   setStrict  (  boolean   strict  )  { 
this  .  strict  =  strict  ; 
} 




public   String   name  (  )  { 
return  "custom"  ; 
} 
} 
