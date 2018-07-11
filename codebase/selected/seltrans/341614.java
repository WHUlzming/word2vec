package   net  .  sf  .  mzmine  .  modules  .  rawdatamethods  .  peakpicking  .  manual  ; 

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




class   ManualPeak   implements   ChromatographicPeak  { 

private   RawDataFile   dataFile  ; 

private   double   mz  ,  rt  ,  height  ,  area  ; 

private   Range   rtRange  ,  mzRange  ,  intensityRange  ; 

private   TreeMap  <  Integer  ,  DataPoint  >  dataPointMap  ; 

private   int   fragmentScan  ,  representativeScan  ; 

private   IsotopePattern   isotopePattern  ; 

private   int   charge  =  0  ; 




ManualPeak  (  RawDataFile   dataFile  )  { 
this  .  dataFile  =  dataFile  ; 
dataPointMap  =  new   TreeMap  <  Integer  ,  DataPoint  >  (  )  ; 
} 




public   PeakStatus   getPeakStatus  (  )  { 
return   PeakStatus  .  MANUAL  ; 
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
return   CollectionUtils  .  toIntArray  (  dataPointMap  .  keySet  (  )  )  ; 
} 





public   DataPoint   getDataPoint  (  int   scanNumber  )  { 
return   dataPointMap  .  get  (  scanNumber  )  ; 
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

public   IsotopePattern   getIsotopePattern  (  )  { 
return   isotopePattern  ; 
} 

public   void   setIsotopePattern  (  IsotopePattern   isotopePattern  )  { 
this  .  isotopePattern  =  isotopePattern  ; 
} 








void   addDatapoint  (  int   scanNumber  ,  DataPoint   dataPoint  )  { 
double   rt  =  dataFile  .  getScan  (  scanNumber  )  .  getRetentionTime  (  )  ; 
if  (  dataPointMap  .  isEmpty  (  )  )  { 
rtRange  =  new   Range  (  rt  )  ; 
mzRange  =  new   Range  (  dataPoint  .  getMZ  (  )  )  ; 
intensityRange  =  new   Range  (  dataPoint  .  getIntensity  (  )  )  ; 
}  else  { 
rtRange  .  extendRange  (  rt  )  ; 
mzRange  .  extendRange  (  dataPoint  .  getMZ  (  )  )  ; 
intensityRange  .  extendRange  (  dataPoint  .  getIntensity  (  )  )  ; 
} 
dataPointMap  .  put  (  scanNumber  ,  dataPoint  )  ; 
} 

void   finalizePeak  (  )  { 
while  (  !  dataPointMap  .  isEmpty  (  )  )  { 
int   scanNumber  =  dataPointMap  .  firstKey  (  )  ; 
if  (  dataPointMap  .  get  (  scanNumber  )  .  getIntensity  (  )  >  0  )  break  ; 
dataPointMap  .  remove  (  scanNumber  )  ; 
} 
while  (  !  dataPointMap  .  isEmpty  (  )  )  { 
int   scanNumber  =  dataPointMap  .  lastKey  (  )  ; 
if  (  dataPointMap  .  get  (  scanNumber  )  .  getIntensity  (  )  >  0  )  break  ; 
dataPointMap  .  remove  (  scanNumber  )  ; 
} 
if  (  dataPointMap  .  isEmpty  (  )  )  { 
throw  (  new   IllegalStateException  (  "Peak can not be finalized without any data points"  )  )  ; 
} 
int   allScanNumbers  [  ]  =  CollectionUtils  .  toIntArray  (  dataPointMap  .  keySet  (  )  )  ; 
for  (  int   i  =  0  ;  i  <  allScanNumbers  .  length  ;  i  ++  )  { 
DataPoint   dataPoint  =  dataPointMap  .  get  (  allScanNumbers  [  i  ]  )  ; 
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
double   previousIntensity  =  dataPointMap  .  get  (  allScanNumbers  [  i  -  1  ]  )  .  getIntensity  (  )  ; 
double   thisIntensity  =  dataPointMap  .  get  (  allScanNumbers  [  i  ]  )  .  getIntensity  (  )  ; 
double   averageIntensity  =  (  previousIntensity  +  thisIntensity  )  /  2  ; 
area  +=  (  rtDifference  *  averageIntensity  )  ; 
} 
double   mzArray  [  ]  =  new   double  [  allScanNumbers  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  allScanNumbers  .  length  ;  i  ++  )  { 
mzArray  [  i  ]  =  dataPointMap  .  get  (  allScanNumbers  [  i  ]  )  .  getMZ  (  )  ; 
} 
this  .  mz  =  MathUtils  .  calcQuantile  (  mzArray  ,  0.5f  )  ; 
fragmentScan  =  ScanUtils  .  findBestFragmentScan  (  dataFile  ,  rtRange  ,  mzRange  )  ; 
if  (  fragmentScan  >  0  )  { 
Scan   fragmentScanObject  =  dataFile  .  getScan  (  fragmentScan  )  ; 
int   precursorCharge  =  fragmentScanObject  .  getPrecursorCharge  (  )  ; 
if  (  (  precursorCharge  >  0  )  &&  (  this  .  charge  ==  0  )  )  this  .  charge  =  precursorCharge  ; 
} 
} 

public   int   getRepresentativeScanNumber  (  )  { 
return   representativeScan  ; 
} 

public   int   getMostIntenseFragmentScanNumber  (  )  { 
return   fragmentScan  ; 
} 

public   int   getCharge  (  )  { 
return   charge  ; 
} 

public   void   setCharge  (  int   charge  )  { 
this  .  charge  =  charge  ; 
} 
} 

