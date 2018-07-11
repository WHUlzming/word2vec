package   hci  .  gnomex  .  useq  .  data  ; 

import   java  .  io  .  *  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  zip  .  ZipEntry  ; 
import   java  .  util  .  zip  .  ZipOutputStream  ; 
import   hci  .  gnomex  .  useq  .  *  ; 



public   class   RegionScoreData   extends   USeqData  { 

private   RegionScore  [  ]  sortedRegionScores  ; 

public   RegionScoreData  (  )  { 
} 


public   RegionScoreData  (  RegionScore  [  ]  sortedRegionScores  ,  SliceInfo   sliceInfo  )  { 
this  .  sortedRegionScores  =  sortedRegionScores  ; 
this  .  sliceInfo  =  sliceInfo  ; 
} 

public   RegionScoreData  (  File   binaryFile  )  throws   IOException  { 
sliceInfo  =  new   SliceInfo  (  binaryFile  .  getName  (  )  )  ; 
read  (  binaryFile  )  ; 
} 

public   RegionScoreData  (  DataInputStream   dis  ,  SliceInfo   sliceInfo  )  { 
this  .  sliceInfo  =  sliceInfo  ; 
read  (  dis  )  ; 
} 


public   static   void   updateSliceInfo  (  RegionScore  [  ]  sortedRegionScores  ,  SliceInfo   sliceInfo  )  { 
sliceInfo  .  setFirstStartPosition  (  sortedRegionScores  [  0  ]  .  getStart  (  )  )  ; 
sliceInfo  .  setLastStartPosition  (  sortedRegionScores  [  sortedRegionScores  .  length  -  1  ]  .  start  )  ; 
sliceInfo  .  setNumberRecords  (  sortedRegionScores  .  length  )  ; 
} 


public   int   fetchLastBase  (  )  { 
int   lastBase  =  -  1  ; 
for  (  RegionScore   r  :  sortedRegionScores  )  { 
int   end  =  r  .  getStop  (  )  ; 
if  (  end  >  lastBase  )  lastBase  =  end  ; 
} 
return   lastBase  ; 
} 


public   void   writeBed  (  PrintWriter   out  ,  boolean   fixScore  )  { 
String   chrom  =  sliceInfo  .  getChromosome  (  )  ; 
String   strand  =  sliceInfo  .  getStrand  (  )  ; 
for  (  int   i  =  0  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
if  (  fixScore  )  { 
int   score  =  USeqUtilities  .  fixBedScore  (  sortedRegionScores  [  i  ]  .  score  )  ; 
out  .  println  (  chrom  +  "\t"  +  sortedRegionScores  [  i  ]  .  start  +  "\t"  +  sortedRegionScores  [  i  ]  .  stop  +  "\t"  +  ".\t"  +  score  +  "\t"  +  strand  )  ; 
}  else   out  .  println  (  chrom  +  "\t"  +  sortedRegionScores  [  i  ]  .  start  +  "\t"  +  sortedRegionScores  [  i  ]  .  stop  +  "\t"  +  ".\t"  +  sortedRegionScores  [  i  ]  .  score  +  "\t"  +  strand  )  ; 
} 
} 


public   void   writeNative  (  PrintWriter   out  )  { 
String   chrom  =  sliceInfo  .  getChromosome  (  )  ; 
String   strand  =  sliceInfo  .  getStrand  (  )  ; 
if  (  strand  .  equals  (  "."  )  )  { 
out  .  println  (  "#Chr\tStart\tStop\tScore"  )  ; 
for  (  int   i  =  0  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  out  .  println  (  chrom  +  "\t"  +  sortedRegionScores  [  i  ]  .  start  +  "\t"  +  sortedRegionScores  [  i  ]  .  stop  +  "\t"  +  sortedRegionScores  [  i  ]  .  score  )  ; 
}  else  { 
out  .  println  (  "#Chr\tStart\tStop\tScore\tStrand"  )  ; 
for  (  int   i  =  0  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  out  .  println  (  chrom  +  "\t"  +  sortedRegionScores  [  i  ]  .  start  +  "\t"  +  sortedRegionScores  [  i  ]  .  stop  +  "\t"  +  sortedRegionScores  [  i  ]  .  score  +  "\t"  +  strand  )  ; 
} 
} 






public   File   write  (  File   saveDirectory  ,  boolean   attemptToSaveAsShort  )  { 
boolean   useShortBeginning  =  false  ; 
boolean   useShortLength  =  false  ; 
if  (  attemptToSaveAsShort  )  { 
int   bp  =  sortedRegionScores  [  0  ]  .  start  ; 
useShortBeginning  =  true  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  ; 
if  (  diff  >  65536  )  { 
useShortBeginning  =  false  ; 
break  ; 
} 
bp  =  currentStart  ; 
} 
useShortLength  =  true  ; 
for  (  int   i  =  0  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   diff  =  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  ; 
if  (  diff  >  65536  )  { 
useShortLength  =  false  ; 
break  ; 
} 
} 
} 
String   fileType  ; 
if  (  useShortBeginning  )  fileType  =  USeqUtilities  .  SHORT  ;  else   fileType  =  USeqUtilities  .  INT  ; 
if  (  useShortLength  )  fileType  =  fileType  +  USeqUtilities  .  SHORT  ;  else   fileType  =  fileType  +  USeqUtilities  .  INT  ; 
fileType  =  fileType  +  USeqUtilities  .  FLOAT  ; 
sliceInfo  .  setBinaryType  (  fileType  )  ; 
binaryFile  =  new   File  (  saveDirectory  ,  sliceInfo  .  getSliceName  (  )  )  ; 
FileOutputStream   workingFOS  =  null  ; 
DataOutputStream   workingDOS  =  null  ; 
try  { 
workingFOS  =  new   FileOutputStream  (  binaryFile  )  ; 
workingDOS  =  new   DataOutputStream  (  new   BufferedOutputStream  (  workingFOS  )  )  ; 
workingDOS  .  writeUTF  (  header  )  ; 
workingDOS  .  writeInt  (  sortedRegionScores  [  0  ]  .  start  )  ; 
int   bp  =  sortedRegionScores  [  0  ]  .  start  ; 
if  (  useShortBeginning  )  { 
if  (  useShortLength  ==  false  )  { 
workingDOS  .  writeInt  (  sortedRegionScores  [  0  ]  .  stop  -  sortedRegionScores  [  0  ]  .  start  )  ; 
workingDOS  .  writeFloat  (  sortedRegionScores  [  0  ]  .  score  )  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  -  32768  ; 
workingDOS  .  writeShort  (  (  short  )  (  diff  )  )  ; 
workingDOS  .  writeInt  (  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  )  ; 
workingDOS  .  writeFloat  (  sortedRegionScores  [  i  ]  .  score  )  ; 
bp  =  currentStart  ; 
} 
}  else  { 
workingDOS  .  writeShort  (  (  short  )  (  sortedRegionScores  [  0  ]  .  stop  -  sortedRegionScores  [  0  ]  .  start  -  32768  )  )  ; 
workingDOS  .  writeFloat  (  sortedRegionScores  [  0  ]  .  score  )  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  -  32768  ; 
workingDOS  .  writeShort  (  (  short  )  (  diff  )  )  ; 
workingDOS  .  writeShort  (  (  short  )  (  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  -  32768  )  )  ; 
workingDOS  .  writeFloat  (  sortedRegionScores  [  i  ]  .  score  )  ; 
bp  =  currentStart  ; 
} 
} 
}  else  { 
if  (  useShortLength  ==  false  )  { 
workingDOS  .  writeInt  (  sortedRegionScores  [  0  ]  .  stop  -  sortedRegionScores  [  0  ]  .  start  )  ; 
workingDOS  .  writeFloat  (  sortedRegionScores  [  0  ]  .  score  )  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  ; 
workingDOS  .  writeInt  (  diff  )  ; 
workingDOS  .  writeInt  (  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  )  ; 
workingDOS  .  writeFloat  (  sortedRegionScores  [  i  ]  .  score  )  ; 
bp  =  currentStart  ; 
} 
}  else  { 
workingDOS  .  writeShort  (  (  short  )  (  sortedRegionScores  [  0  ]  .  stop  -  sortedRegionScores  [  0  ]  .  start  -  32768  )  )  ; 
workingDOS  .  writeFloat  (  sortedRegionScores  [  0  ]  .  score  )  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  ; 
workingDOS  .  writeInt  (  diff  )  ; 
workingDOS  .  writeShort  (  (  short  )  (  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  -  32768  )  )  ; 
workingDOS  .  writeFloat  (  sortedRegionScores  [  i  ]  .  score  )  ; 
bp  =  currentStart  ; 
} 
} 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
binaryFile  =  null  ; 
}  finally  { 
USeqUtilities  .  safeClose  (  workingDOS  )  ; 
USeqUtilities  .  safeClose  (  workingFOS  )  ; 
} 
return   binaryFile  ; 
} 


public   static   RegionScoreData   merge  (  ArrayList  <  RegionScoreData  >  pdAL  )  { 
RegionScoreData  [  ]  pdArray  =  new   RegionScoreData  [  pdAL  .  size  (  )  ]  ; 
pdAL  .  toArray  (  pdArray  )  ; 
Arrays  .  sort  (  pdArray  )  ; 
int   num  =  0  ; 
for  (  int   i  =  0  ;  i  <  pdArray  .  length  ;  i  ++  )  num  +=  pdArray  [  i  ]  .  sortedRegionScores  .  length  ; 
RegionScore  [  ]  concatinate  =  new   RegionScore  [  num  ]  ; 
int   index  =  0  ; 
for  (  int   i  =  0  ;  i  <  pdArray  .  length  ;  i  ++  )  { 
RegionScore  [  ]  slice  =  pdArray  [  i  ]  .  sortedRegionScores  ; 
System  .  arraycopy  (  slice  ,  0  ,  concatinate  ,  index  ,  slice  .  length  )  ; 
index  +=  slice  .  length  ; 
} 
SliceInfo   sliceInfo  =  pdArray  [  0  ]  .  sliceInfo  ; 
RegionScoreData  .  updateSliceInfo  (  concatinate  ,  sliceInfo  )  ; 
return   new   RegionScoreData  (  concatinate  ,  sliceInfo  )  ; 
} 

public   static   RegionScoreData   mergeUSeqData  (  ArrayList  <  USeqData  >  useqDataAL  )  { 
int   num  =  useqDataAL  .  size  (  )  ; 
ArrayList  <  RegionScoreData  >  a  =  new   ArrayList  <  RegionScoreData  >  (  num  )  ; 
for  (  int   i  =  0  ;  i  <  num  ;  i  ++  )  a  .  add  (  (  RegionScoreData  )  useqDataAL  .  get  (  i  )  )  ; 
return   merge  (  a  )  ; 
} 




public   void   write  (  ZipOutputStream   out  ,  DataOutputStream   dos  ,  boolean   attemptToSaveAsShort  )  { 
boolean   useShortBeginning  =  false  ; 
boolean   useShortLength  =  false  ; 
if  (  attemptToSaveAsShort  )  { 
int   bp  =  sortedRegionScores  [  0  ]  .  start  ; 
useShortBeginning  =  true  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  ; 
if  (  diff  >  65536  )  { 
useShortBeginning  =  false  ; 
break  ; 
} 
bp  =  currentStart  ; 
} 
useShortLength  =  true  ; 
for  (  int   i  =  0  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   diff  =  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  ; 
if  (  diff  >  65536  )  { 
useShortLength  =  false  ; 
break  ; 
} 
} 
} 
String   fileType  ; 
if  (  useShortBeginning  )  fileType  =  USeqUtilities  .  SHORT  ;  else   fileType  =  USeqUtilities  .  INT  ; 
if  (  useShortLength  )  fileType  =  fileType  +  USeqUtilities  .  SHORT  ;  else   fileType  =  fileType  +  USeqUtilities  .  INT  ; 
fileType  =  fileType  +  USeqUtilities  .  FLOAT  ; 
sliceInfo  .  setBinaryType  (  fileType  )  ; 
binaryFile  =  null  ; 
try  { 
out  .  putNextEntry  (  new   ZipEntry  (  sliceInfo  .  getSliceName  (  )  )  )  ; 
dos  .  writeUTF  (  header  )  ; 
dos  .  writeInt  (  sortedRegionScores  [  0  ]  .  start  )  ; 
int   bp  =  sortedRegionScores  [  0  ]  .  start  ; 
if  (  useShortBeginning  )  { 
if  (  useShortLength  ==  false  )  { 
dos  .  writeInt  (  sortedRegionScores  [  0  ]  .  stop  -  sortedRegionScores  [  0  ]  .  start  )  ; 
dos  .  writeFloat  (  sortedRegionScores  [  0  ]  .  score  )  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  -  32768  ; 
dos  .  writeShort  (  (  short  )  (  diff  )  )  ; 
dos  .  writeInt  (  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  )  ; 
dos  .  writeFloat  (  sortedRegionScores  [  i  ]  .  score  )  ; 
bp  =  currentStart  ; 
} 
}  else  { 
dos  .  writeShort  (  (  short  )  (  sortedRegionScores  [  0  ]  .  stop  -  sortedRegionScores  [  0  ]  .  start  -  32768  )  )  ; 
dos  .  writeFloat  (  sortedRegionScores  [  0  ]  .  score  )  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  -  32768  ; 
dos  .  writeShort  (  (  short  )  (  diff  )  )  ; 
dos  .  writeShort  (  (  short  )  (  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  -  32768  )  )  ; 
dos  .  writeFloat  (  sortedRegionScores  [  i  ]  .  score  )  ; 
bp  =  currentStart  ; 
} 
} 
}  else  { 
if  (  useShortLength  ==  false  )  { 
dos  .  writeInt  (  sortedRegionScores  [  0  ]  .  stop  -  sortedRegionScores  [  0  ]  .  start  )  ; 
dos  .  writeFloat  (  sortedRegionScores  [  0  ]  .  score  )  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  ; 
dos  .  writeInt  (  diff  )  ; 
dos  .  writeInt  (  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  )  ; 
dos  .  writeFloat  (  sortedRegionScores  [  i  ]  .  score  )  ; 
bp  =  currentStart  ; 
} 
}  else  { 
dos  .  writeShort  (  (  short  )  (  sortedRegionScores  [  0  ]  .  stop  -  sortedRegionScores  [  0  ]  .  start  -  32768  )  )  ; 
dos  .  writeFloat  (  sortedRegionScores  [  0  ]  .  score  )  ; 
for  (  int   i  =  1  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
int   currentStart  =  sortedRegionScores  [  i  ]  .  start  ; 
int   diff  =  currentStart  -  bp  ; 
dos  .  writeInt  (  diff  )  ; 
dos  .  writeShort  (  (  short  )  (  sortedRegionScores  [  i  ]  .  stop  -  sortedRegionScores  [  i  ]  .  start  -  32768  )  )  ; 
dos  .  writeFloat  (  sortedRegionScores  [  i  ]  .  score  )  ; 
bp  =  currentStart  ; 
} 
} 
} 
out  .  closeEntry  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
USeqUtilities  .  safeClose  (  out  )  ; 
USeqUtilities  .  safeClose  (  dos  )  ; 
} 
} 


public   void   read  (  DataInputStream   dis  )  { 
try  { 
header  =  dis  .  readUTF  (  )  ; 
int   numberRegionScores  =  sliceInfo  .  getNumberRecords  (  )  ; 
sortedRegionScores  =  new   RegionScore  [  numberRegionScores  ]  ; 
String   fileType  =  sliceInfo  .  getBinaryType  (  )  ; 
if  (  USeqUtilities  .  REGION_SCORE_INT_INT_FLOAT  .  matcher  (  fileType  )  .  matches  (  )  )  { 
int   start  =  dis  .  readInt  (  )  ; 
sortedRegionScores  [  0  ]  =  new   RegionScore  (  start  ,  start  +  dis  .  readInt  (  )  ,  dis  .  readFloat  (  )  )  ; 
for  (  int   i  =  1  ;  i  <  numberRegionScores  ;  i  ++  )  { 
start  =  sortedRegionScores  [  i  -  1  ]  .  start  +  dis  .  readInt  (  )  ; 
sortedRegionScores  [  i  ]  =  new   RegionScore  (  start  ,  start  +  dis  .  readInt  (  )  ,  dis  .  readFloat  (  )  )  ; 
} 
}  else   if  (  USeqUtilities  .  REGION_SCORE_INT_SHORT_FLOAT  .  matcher  (  fileType  )  .  matches  (  )  )  { 
int   start  =  dis  .  readInt  (  )  ; 
sortedRegionScores  [  0  ]  =  new   RegionScore  (  start  ,  start  +  dis  .  readShort  (  )  +  32768  ,  dis  .  readFloat  (  )  )  ; 
for  (  int   i  =  1  ;  i  <  numberRegionScores  ;  i  ++  )  { 
start  =  sortedRegionScores  [  i  -  1  ]  .  start  +  dis  .  readInt  (  )  ; 
sortedRegionScores  [  i  ]  =  new   RegionScore  (  start  ,  start  +  dis  .  readShort  (  )  +  32768  ,  dis  .  readFloat  (  )  )  ; 
} 
}  else   if  (  USeqUtilities  .  REGION_SCORE_SHORT_SHORT_FLOAT  .  matcher  (  fileType  )  .  matches  (  )  )  { 
int   start  =  dis  .  readInt  (  )  ; 
sortedRegionScores  [  0  ]  =  new   RegionScore  (  start  ,  start  +  dis  .  readShort  (  )  +  32768  ,  dis  .  readFloat  (  )  )  ; 
for  (  int   i  =  1  ;  i  <  numberRegionScores  ;  i  ++  )  { 
start  =  sortedRegionScores  [  i  -  1  ]  .  start  +  dis  .  readShort  (  )  +  32768  ; 
sortedRegionScores  [  i  ]  =  new   RegionScore  (  start  ,  start  +  dis  .  readShort  (  )  +  32768  ,  dis  .  readFloat  (  )  )  ; 
} 
}  else   if  (  USeqUtilities  .  REGION_SCORE_SHORT_INT_FLOAT  .  matcher  (  fileType  )  .  matches  (  )  )  { 
int   start  =  dis  .  readInt  (  )  ; 
sortedRegionScores  [  0  ]  =  new   RegionScore  (  start  ,  start  +  dis  .  readInt  (  )  ,  dis  .  readFloat  (  )  )  ; 
for  (  int   i  =  1  ;  i  <  numberRegionScores  ;  i  ++  )  { 
start  =  sortedRegionScores  [  i  -  1  ]  .  start  +  dis  .  readShort  (  )  +  32768  ; 
sortedRegionScores  [  i  ]  =  new   RegionScore  (  start  ,  start  +  dis  .  readInt  (  )  ,  dis  .  readFloat  (  )  )  ; 
} 
}  else  { 
throw   new   IOException  (  "Incorrect file type for creating a RegionScore[] -> '"  +  fileType  +  "' in "  +  binaryFile  +  "\n"  )  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
USeqUtilities  .  safeClose  (  dis  )  ; 
} 
} 

public   RegionScore  [  ]  getRegionScores  (  )  { 
return   sortedRegionScores  ; 
} 

public   void   setRegionScores  (  RegionScore  [  ]  sortedRegionScores  )  { 
this  .  sortedRegionScores  =  sortedRegionScores  ; 
updateSliceInfo  (  sortedRegionScores  ,  sliceInfo  )  ; 
} 


public   boolean   trim  (  int   beginningBP  ,  int   endingBP  )  { 
ArrayList  <  RegionScore  >  al  =  new   ArrayList  <  RegionScore  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  sortedRegionScores  .  length  ;  i  ++  )  { 
if  (  sortedRegionScores  [  i  ]  .  isContainedBy  (  beginningBP  ,  endingBP  )  )  al  .  add  (  sortedRegionScores  [  i  ]  )  ; 
} 
if  (  al  .  size  (  )  ==  0  )  return   false  ; 
sortedRegionScores  =  new   RegionScore  [  al  .  size  (  )  ]  ; 
al  .  toArray  (  sortedRegionScores  )  ; 
updateSliceInfo  (  sortedRegionScores  ,  sliceInfo  )  ; 
return   true  ; 
} 
} 

