package   net  .  sf  .  mzmine  .  modules  .  peaklistmethods  .  gapfilling  .  samerange  ; 

import   java  .  util  .  TreeMap  ; 
import   net  .  sf  .  mzmine  .  data  .  ChromatographicPeak  ; 
import   net  .  sf  .  mzmine  .  data  .  DataPoint  ; 
import   net  .  sf  .  mzmine  .  data  .  IsotopePattern  ; 
import   net  .  sf  .  mzmine  .  data  .  PeakStatus  ; 
import   net  .  sf  .  mzmine  .  data  .  RawDataFile  ; 
import   net  .  sf  .  mzmine  .  data  .  Scan  ; 
import   net  .  sf  .  mzmine  .  util  .  CollectionUtils  ; 
import   net  .  sf  .  mzmine  .  util  .  MathUtils  ; 
import   net  .  sf  .  mzmine  .  util  .  PeakUtils  ; 
import   net  .  sf  .  mzmine  .  util  .  Range  ; 
import   net  .  sf  .  mzmine  .  util  .  ScanUtils  ; 




class   SameRangePeak   implements   ChromatographicPeak  { 

private   RawDataFile   dataFile  ; 

private   double   mz  ,  rt  ,  height  ,  area  ; 

private   Range   rtRange  ,  mzRange  ,  intensityRange  ; 

private   TreeMap  <  Integer  ,  DataPoint  >  mzPeakMap  ; 

private   int   fragmentScan  ,  representativeScan  ; 

private   IsotopePattern   isotopePattern  ; 

private   int   charge  =  0  ; 




SameRangePeak  (  RawDataFile   dataFile  )  { 
this  .  dataFile  =  dataFile  ; 
mzPeakMap  =  new   TreeMap  <  Integer  ,  DataPoint  >  (  )  ; 
} 




public   PeakStatus   getPeakStatus  (  )  { 
return   PeakStatus  .  ESTIMATED  ; 
} 




public   double   getMZ  (  )  { 
return   mz  ; 
} 




public   double   getRT  (  )  { 
return   rt  ; 
} 




public   double   getHeight  (  )  { 
return   height  ; 
} 




public   double   getArea  (  )  { 
return   area  ; 
} 




public   int  [  ]  getScanNumbers  (  )  { 
return   CollectionUtils  .  toIntArray  (  mzPeakMap  .  keySet  (  )  )  ; 
} 





public   DataPoint   getDataPoint  (  int   scanNumber  )  { 
return   mzPeakMap  .  get  (  scanNumber  )  ; 
} 

public   Range   getRawDataPointsIntensityRange  (  )  { 
return   intensityRange  ; 
} 

public   Range   getRawDataPointsMZRange  (  )  { 
return   mzRange  ; 
} 

public   Range   getRawDataPointsRTRange  (  )  { 
return   rtRange  ; 
} 




public   RawDataFile   getDataFile  (  )  { 
return   dataFile  ; 
} 

public   String   getName  (  )  { 
return   PeakUtils  .  peakToString  (  this  )  ; 
} 








void   addDatapoint  (  int   scanNumber  ,  DataPoint   dataPoint  )  { 
double   rt  =  dataFile  .  getScan  (  scanNumber  )  .  getRetentionTime  (  )  ; 
if  (  mzPeakMap  .  isEmpty  (  )  )  { 
rtRange  =  new   Range  (  rt  )  ; 
mzRange  =  new   Range  (  dataPoint  .  getMZ  (  )  )  ; 
intensityRange  =  new   Range  (  dataPoint  .  getIntensity  (  )  )  ; 
}  else  { 
rtRange  .  extendRange  (  rt  )  ; 
mzRange  .  extendRange  (  dataPoint  .  getMZ  (  )  )  ; 
intensityRange  .  extendRange  (  dataPoint  .  getIntensity  (  )  )  ; 
} 
mzPeakMap  .  put  (  scanNumber  ,  dataPoint  )  ; 
} 

void   finalizePeak  (  )  { 
while  (  !  mzPeakMap  .  isEmpty  (  )  )  { 
int   scanNumber  =  mzPeakMap  .  firstKey  (  )  ; 
if  (  mzPeakMap  .  get  (  scanNumber  )  .  getIntensity  (  )  >  0  )  break  ; 
mzPeakMap  .  remove  (  scanNumber  )  ; 
} 
while  (  !  mzPeakMap  .  isEmpty  (  )  )  { 
int   scanNumber  =  mzPeakMap  .  lastKey  (  )  ; 
if  (  mzPeakMap  .  get  (  scanNumber  )  .  getIntensity  (  )  >  0  )  break  ; 
mzPeakMap  .  remove  (  scanNumber  )  ; 
} 
if  (  mzPeakMap  .  isEmpty  (  )  )  { 
throw  (  new   IllegalStateException  (  "Peak can not be finalized without any data points"  )  )  ; 
} 
int   allScanNumbers  [  ]  =  CollectionUtils  .  toIntArray  (  mzPeakMap  .  keySet  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  allScanNumbers  .  length  ;  i  ++  )  { 
DataPoint   dataPoint  =  mzPeakMap  .  get  (  allScanNumbers  [  i  ]  )  ; 
double   rt  =  dataFile  .  getScan  (  allScanNumbers  [  i  ]  )  .  getRetentionTime  (  )  ; 
if  (  dataPoint  .  getIntensity  (  )  >  height  )  { 
height  =  dataPoint  .  getIntensity  (  )  ; 
representativeScan  =  allScanNumbers  [  i  ]  ; 
this  .  rt  =  rt  ; 
} 
} 
area  =  0  ; 
for  (  int   i  =  1  ;  i  <  allScanNumbers  .  length  ;  i  ++  )  { 
double   previousRT  =  dataFile  .  getScan  (  allScanNumbers  [  i  -  1  ]  )  .  getRetentionTime  (  )  *  60d  ; 
double   currentRT  =  dataFile  .  getScan  (  allScanNumbers  [  i  ]  )  .  getRetentionTime  (  )  *  60d  ; 
double   rtDifference  =  currentRT  -  previousRT  ; 
double   previousIntensity  =  mzPeakMap  .  get  (  allScanNumbers  [  i  -  1  ]  )  .  getIntensity  (  )  ; 
double   thisIntensity  =  mzPeakMap  .  get  (  allScanNumbers  [  i  ]  )  .  getIntensity  (  )  ; 
double   averageIntensity  =  (  previousIntensity  +  thisIntensity  )  /  2  ; 
area  +=  (  rtDifference  *  averageIntensity  )  ; 
} 
double   mzArray  [  ]  =  new   double  [  allScanNumbers  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  allScanNumbers  .  length  ;  i  ++  )  { 
mzArray  [  i  ]  =  mzPeakMap  .  get  (  allScanNumbers  [  i  ]  )  .  getMZ  (  )  ; 
} 
this  .  mz  =  MathUtils  .  calcQuantile  (  mzArray  ,  0.5f  )  ; 
fragmentScan  =  ScanUtils  .  findBestFragmentScan  (  dataFile  ,  rtRange  ,  mzRange  )  ; 
if  (  fragmentScan  >  0  )  { 
Scan   fragmentScanObject  =  dataFile  .  getScan  (  fragmentScan  )  ; 
int   precursorCharge  =  fragmentScanObject  .  getPrecursorCharge  (  )  ; 
if  (  (  precursorCharge  >  0  )  &&  (  this  .  charge  ==  0  )  )  this  .  charge  =  precursorCharge  ; 
} 
} 

public   void   setMZ  (  double   mz  )  { 
this  .  mz  =  mz  ; 
} 

public   int   getRepresentativeScanNumber  (  )  { 
return   representativeScan  ; 
} 

public   int   getMostIntenseFragmentScanNumber  (  )  { 
return   fragmentScan  ; 
} 

public   IsotopePattern   getIsotopePattern  (  )  { 
return   isotopePattern  ; 
} 

public   void   setIsotopePattern  (  IsotopePattern   isotopePattern  )  { 
this  .  isotopePattern  =  isotopePattern  ; 
} 

public   int   getCharge  (  )  { 
return   charge  ; 
} 

public   void   setCharge  (  int   charge  )  { 
this  .  charge  =  charge  ; 
} 
} 

