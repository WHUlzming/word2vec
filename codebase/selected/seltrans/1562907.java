package   ch  .  unizh  .  ini  .  jaer  .  chip  .  dvs320  ; 

import   ch  .  unizh  .  ini  .  jaer  .  chip  .  dvs320  .  cDVSTestHardwareInterface  ; 
import   ch  .  unizh  .  ini  .  jaer  .  chip  .  retina  .  *  ; 
import   net  .  sf  .  jaer  .  aemonitor  .  *  ; 
import   net  .  sf  .  jaer  .  biasgen  .  *  ; 
import   net  .  sf  .  jaer  .  biasgen  .  VDAC  .  VPot  ; 
import   net  .  sf  .  jaer  .  chip  .  *  ; 
import   net  .  sf  .  jaer  .  event  .  *  ; 
import   net  .  sf  .  jaer  .  hardwareinterface  .  *  ; 
import   java  .  awt  .  BorderLayout  ; 
import   java  .  math  .  BigInteger  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  text  .  ParseException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  Observable  ; 
import   java  .  util  .  Random  ; 
import   java  .  util  .  StringTokenizer  ; 
import   javax  .  swing  .  BoxLayout  ; 
import   javax  .  swing  .  JPanel  ; 
import   javax  .  swing  .  JTabbedPane  ; 
import   net  .  sf  .  jaer  .  Description  ; 
import   net  .  sf  .  jaer  .  biasgen  .  Pot  .  Sex  ; 
import   net  .  sf  .  jaer  .  biasgen  .  Pot  .  Type  ; 
import   net  .  sf  .  jaer  .  biasgen  .  VDAC  .  DAC  ; 
import   net  .  sf  .  jaer  .  biasgen  .  VDAC  .  VPotGUIControl  ; 
import   net  .  sf  .  jaer  .  util  .  ParameterControlPanel  ; 
import   net  .  sf  .  jaer  .  util  .  RemoteControlCommand  ; 
import   net  .  sf  .  jaer  .  util  .  RemoteControlled  ; 















@  Description  (  "cDVSTest color Dynamic Vision Test chip"  ) 
public   class   cDVSTest20   extends   AETemporalConstastRetina   implements   HasIntensity  { 

public   static   final   int   SIZEX_TOTAL  =  140  ; 

public   static   final   int   SIZE_Y  =  64  ; 

public   static   final   int   SIZE_Y_CDVS  =  32  ; 

public   static   final   int   SIZE_X_CDVS  =  32  ; 

public   static   final   int   SIZE_X_DVS  =  64  ; 

public   static   final   int   COLOR_CHANGE_BIT  =  1  ; 




public   static   final   int   POLMASK  =  1  ,  XSHIFT  =  Integer  .  bitCount  (  POLMASK  )  ,  XMASK  =  127  <<  XSHIFT  ,  YSHIFT  =  16  ,  YMASK  =  63  <<  YSHIFT  ,  INTENSITYMASK  =  0x40000000  ; 


public   static   final   int   DATA_TYPE_MASK  =  0xc000  ,  DATA_TYPE_ADDRESS  =  0x0000  ,  DATA_TYPE_TIMESTAMP  =  0x4000  ,  DATA_TYPE_WRAP  =  0x8000  ,  DATA_TYPE_TIMESTAMP_RESET  =  0xd000  ; 


public   static   final   int   ADDRESS_TYPE_MASK  =  0x2000  ,  EVENT_ADDRESS_MASK  =  POLMASK  |  XMASK  |  YMASK  ,  ADDRESS_TYPE_EVENT  =  0x0000  ,  ADDRESS_TYPE_ADC  =  0x2000  ; 


public   static   final   int   ADC_TYPE_MASK  =  0x1000  ,  ADC_DATA_MASK  =  0xfff  ,  ADC_START_BIT  =  0x1000  ,  ADC_CHANNEL_MASK  =  0x0000  ; 

public   static   final   int   MAX_ADC  =  (  int  )  (  (  1  <<  12  )  -  1  )  ; 


private   float   globalIntensity  =  0  ; 

private   CDVSLogIntensityFrameData   frameData  =  new   CDVSLogIntensityFrameData  (  )  ; 

private   cDVSTest20Renderer   cDVSRenderer  =  null  ; 

private   cDVSTest20DisplayMethod   cDVSDisplayMethod  =  null  ; 

private   boolean   displayLogIntensity  ; 

private   boolean   displayColorChangeEvents  ; 

private   boolean   displayLogIntensityChangeEvents  ; 


public   cDVSTest20  (  )  { 
setName  (  "cDVSTest20"  )  ; 
setEventClass  (  cDVSEvent  .  class  )  ; 
setSizeX  (  SIZEX_TOTAL  )  ; 
setSizeY  (  SIZE_Y  )  ; 
setNumCellTypes  (  3  )  ; 
setPixelHeightUm  (  14.5f  )  ; 
setPixelWidthUm  (  14.5f  )  ; 
setEventExtractor  (  new   cDVSTestExtractor  (  this  )  )  ; 
setBiasgen  (  new   cDVSTest20  .  cDVSTestBiasgen  (  this  )  )  ; 
displayLogIntensity  =  getPrefs  (  )  .  getBoolean  (  "displayLogIntensity"  ,  true  )  ; 
displayColorChangeEvents  =  getPrefs  (  )  .  getBoolean  (  "displayColorChangeEvents"  ,  true  )  ; 
displayLogIntensityChangeEvents  =  getPrefs  (  )  .  getBoolean  (  "displayLogIntensityChangeEvents"  ,  true  )  ; 
setRenderer  (  (  cDVSRenderer  =  new   cDVSTest20Renderer  (  this  )  )  )  ; 
cDVSDisplayMethod  =  new   cDVSTest20DisplayMethod  (  this  )  ; 
getCanvas  (  )  .  addDisplayMethod  (  cDVSDisplayMethod  )  ; 
getCanvas  (  )  .  setDisplayMethod  (  cDVSDisplayMethod  )  ; 
cDVSDisplayMethod  .  setIntensitySource  (  this  )  ; 
} 


@  Override 
public   void   cleanup  (  )  { 
super  .  cleanup  (  )  ; 
cDVSDisplayMethod  .  unregisterControlPanel  (  )  ; 
} 




public   cDVSTest20  (  HardwareInterface   hardwareInterface  )  { 
this  (  )  ; 
setHardwareInterface  (  hardwareInterface  )  ; 
} 

@  Override 
public   float   getIntensity  (  )  { 
return   globalIntensity  ; 
} 

@  Override 
public   void   setIntensity  (  float   f  )  { 
globalIntensity  =  f  ; 
} 

private   boolean   useOffChipCalibration  =  false  ; 




public   CDVSLogIntensityFrameData   getFrameData  (  )  { 
return   frameData  ; 
} 

Random   random  =  new   Random  (  )  ; 

int   debugSampleCounter  =  0  ; 




public   boolean   isUseOffChipCalibration  (  )  { 
return   useOffChipCalibration  ; 
} 




public   void   setUseOffChipCalibration  (  boolean   useOffChipCalibration  )  { 
this  .  useOffChipCalibration  =  useOffChipCalibration  ; 
this  .  getFrameData  (  )  .  setUseOffChipCalibration  (  useOffChipCalibration  )  ; 
if  (  useOffChipCalibration  )  { 
this  .  getFrameData  (  )  .  setCalibData1  (  )  ; 
} 
} 














public   class   cDVSTestExtractor   extends   RetinaExtractor  { 

private   int   lastIntenTs  =  0  ; 

public   cDVSTestExtractor  (  cDVSTest20   chip  )  { 
super  (  chip  )  ; 
} 

private   float   avdt  =  100  ; 





@  Override 
public   synchronized   EventPacket   extractPacket  (  AEPacketRaw   in  )  { 
if  (  out  ==  null  )  { 
out  =  new   EventPacket  (  chip  .  getEventClass  (  )  )  ; 
}  else  { 
out  .  clear  (  )  ; 
} 
if  (  in  ==  null  )  { 
return   out  ; 
} 
int   n  =  in  .  getNumEvents  (  )  ; 
int   skipBy  =  1  ; 
if  (  isSubSamplingEnabled  (  )  )  { 
while  (  n  /  skipBy  >  getSubsampleThresholdEventCount  (  )  )  { 
skipBy  ++  ; 
} 
} 
int  [  ]  datas  =  in  .  getAddresses  (  )  ; 
int  [  ]  timestamps  =  in  .  getTimestamps  (  )  ; 
OutputEventIterator   outItr  =  out  .  outputIterator  (  )  ; 
for  (  int   i  =  0  ;  i  <  n  ;  i  ++  )  { 
int   data  =  datas  [  i  ]  ; 
if  (  (  data  &  ADDRESS_TYPE_MASK  )  ==  ADDRESS_TYPE_EVENT  )  { 
if  (  (  data  &  INTENSITYMASK  )  ==  INTENSITYMASK  )  { 
int   dt  =  timestamps  [  i  ]  -  lastIntenTs  ; 
if  (  dt  >  50  )  { 
avdt  =  0.05f  *  dt  +  0.95f  *  avdt  ; 
setIntensity  (  1000f  /  avdt  )  ; 
} 
lastIntenTs  =  timestamps  [  i  ]  ; 
}  else  { 
cDVSEvent   e  =  (  cDVSEvent  )  outItr  .  nextOutput  (  )  ; 
e  .  address  =  data  &  EVENT_ADDRESS_MASK  ; 
e  .  timestamp  =  (  timestamps  [  i  ]  )  ; 
e  .  polarity  =  (  byte  )  (  data  &  POLMASK  )  ; 
e  .  x  =  (  short  )  (  (  (  data  &  XMASK  )  >  >  >  XSHIFT  )  )  ; 
if  (  e  .  x  <  0  )  { 
e  .  x  =  0  ; 
}  else   if  (  e  .  x  >  319  )  { 
} 
e  .  y  =  (  short  )  (  (  data  &  YMASK  )  >  >  >  YSHIFT  )  ; 
if  (  e  .  y  >  239  )  { 
e  .  y  =  239  ; 
}  else   if  (  e  .  y  <  0  )  { 
e  .  y  =  0  ; 
} 
if  (  e  .  x  <  SIZE_X_CDVS  *  2  )  { 
if  (  (  e  .  y  &  1  )  ==  1  )  { 
if  (  e  .  polarity  ==  1  )  { 
e  .  eventType  =  cDVSEvent  .  EventType  .  Brighter  ; 
}  else  { 
e  .  eventType  =  cDVSEvent  .  EventType  .  Darker  ; 
} 
}  else  { 
if  (  e  .  polarity  ==  0  )  { 
e  .  eventType  =  cDVSEvent  .  EventType  .  Redder  ; 
}  else  { 
e  .  eventType  =  cDVSEvent  .  EventType  .  Bluer  ; 
} 
} 
e  .  x  =  (  short  )  (  e  .  x  >  >  >  1  )  ; 
e  .  y  =  (  short  )  (  e  .  y  >  >  >  1  )  ; 
}  else  { 
if  (  e  .  polarity  ==  1  )  { 
e  .  eventType  =  cDVSEvent  .  EventType  .  Brighter  ; 
}  else  { 
e  .  eventType  =  cDVSEvent  .  EventType  .  Darker  ; 
} 
} 
} 
}  else   if  (  (  data  &  ADDRESS_TYPE_MASK  )  ==  ADDRESS_TYPE_ADC  )  { 
if  (  (  data  &  ADC_START_BIT  )  ==  ADC_START_BIT  )  { 
frameData  .  swapBuffers  (  )  ; 
frameData  .  setTimestamp  (  timestamps  [  i  ]  )  ; 
} 
frameData  .  put  (  data  &  ADC_DATA_MASK  )  ; 
} 
} 
return   out  ; 
} 
} 





@  Override 
public   void   setHardwareInterface  (  final   HardwareInterface   hardwareInterface  )  { 
this  .  hardwareInterface  =  hardwareInterface  ; 
cDVSTest20  .  cDVSTestBiasgen   bg  ; 
try  { 
if  (  getBiasgen  (  )  ==  null  )  { 
setBiasgen  (  bg  =  new   cDVSTest20  .  cDVSTestBiasgen  (  this  )  )  ; 
}  else  { 
getBiasgen  (  )  .  setHardwareInterface  (  (  BiasgenHardwareInterface  )  hardwareInterface  )  ; 
} 
}  catch  (  ClassCastException   e  )  { 
log  .  warning  (  e  .  getMessage  (  )  +  ": probably this chip object has a biasgen but the hardware interface doesn't, ignoring"  )  ; 
} 
} 


























public   class   cDVSTestBiasgen   extends   net  .  sf  .  jaer  .  biasgen  .  Biasgen  { 

ArrayList  <  HasPreference  >  hasPreferencesList  =  new   ArrayList  <  HasPreference  >  (  )  ; 

private   ConfigurableIPotRev0   pcas  ,  diffOn  ,  diffOff  ,  diff  ,  red  ,  blue  ,  amp  ; 

private   ConfigurableIPotRev0   refr  ,  pr  ,  foll  ; 

cDVSTest20OutputControlPanel   controlPanel  ; 

AllMuxes   allMuxes  =  new   AllMuxes  (  )  ; 

private   ShiftedSourceBias   ssn  ,  ssp  ,  ssnMid  ,  sspMid  ; 

private   ShiftedSourceBias  [  ]  ssBiases  =  new   ShiftedSourceBias  [  4  ]  ; 

private   VPot   thermometerDAC  ; 

ch  .  unizh  .  ini  .  jaer  .  chip  .  dvs320  .  cDVSTestHardwareInterfaceProxy   adcProxy  =  new   cDVSTestHardwareInterfaceProxy  (  cDVSTest20  .  this  )  ; 

int   pos  =  0  ; 

JPanel   bPanel  ; 

JTabbedPane   bgTabbedPane  ; 




public   cDVSTestBiasgen  (  Chip   chip  )  { 
super  (  chip  )  ; 
setName  (  "cDVSTest"  )  ; 
getMasterbias  (  )  .  setKPrimeNFet  (  55e-3f  )  ; 
getMasterbias  (  )  .  setMultiplier  (  9  *  (  24f  /  2.4f  )  /  (  4.8f  /  2.4f  )  )  ; 
getMasterbias  (  )  .  setWOverL  (  4.8f  /  2.4f  )  ; 
ssn  =  new   ShiftedSourceBias  (  this  )  ; 
ssn  .  setSex  (  Pot  .  Sex  .  N  )  ; 
ssn  .  setName  (  "SSN"  )  ; 
ssn  .  setTooltipString  (  "n-type shifted source that generates a regulated voltage near ground"  )  ; 
ssn  .  addObserver  (  this  )  ; 
ssp  =  new   ShiftedSourceBias  (  this  )  ; 
ssp  .  setSex  (  Pot  .  Sex  .  P  )  ; 
ssp  .  setName  (  "SSP"  )  ; 
ssp  .  setTooltipString  (  "p-type shifted source that generates a regulated voltage near Vdd"  )  ; 
ssp  .  addObserver  (  this  )  ; 
ssnMid  =  new   ShiftedSourceBias  (  this  )  ; 
ssnMid  .  setSex  (  Pot  .  Sex  .  N  )  ; 
ssnMid  .  setName  (  "SSNMid"  )  ; 
ssnMid  .  setTooltipString  (  "n-type shifted source that generates a regulated voltage inside rail, about 2 diode drops from ground"  )  ; 
ssnMid  .  addObserver  (  this  )  ; 
sspMid  =  new   ShiftedSourceBias  (  this  )  ; 
sspMid  .  setSex  (  Pot  .  Sex  .  P  )  ; 
sspMid  .  setName  (  "SSPMid"  )  ; 
sspMid  .  setTooltipString  (  "p-type shifted source that generates a regulated voltage about 2 diode drops from Vdd"  )  ; 
sspMid  .  addObserver  (  this  )  ; 
ssBiases  [  0  ]  =  ssnMid  ; 
ssBiases  [  1  ]  =  ssn  ; 
ssBiases  [  2  ]  =  sspMid  ; 
ssBiases  [  3  ]  =  ssp  ; 
final   float   Vdd  =  1.8f  ; 
DAC   dac  =  new   DAC  (  1  ,  8  ,  0  ,  Vdd  ,  Vdd  )  ; 
thermometerDAC  =  new   VPot  (  cDVSTest20  .  this  ,  "LogAmpRef"  ,  dac  ,  0  ,  Type  .  NORMAL  ,  Sex  .  N  ,  9  ,  0  ,  "Voltage DAC for log intensity switched cap amplifier"  )  ; 
thermometerDAC  .  addObserver  (  this  )  ; 
setPotArray  (  new   IPotArray  (  this  )  )  ; 
try  { 
pot  (  "diff,n,normal,differencing amp"  )  ; 
pot  (  "ON,n,normal,DVS brighter threshold"  )  ; 
pot  (  "OFF,n,normal,DVS darker threshold"  )  ; 
pot  (  "Red,n,normal,Redder threshold"  )  ; 
pot  (  "Blue,n,normal,Bluer threshold"  )  ; 
pot  (  "Amp,n,normal,DVS ON threshold"  )  ; 
pot  (  "pcas,p,cascode,DVS ON threshold"  )  ; 
pot  (  "pixInvB,n,normal,pixel inverter bias"  )  ; 
pot  (  "pr,p,normal,photoreceptor bias current"  )  ; 
pot  (  "fb,p,normal,photoreceptor follower bias current"  )  ; 
pot  (  "refr,p,normal,DVS refractory current"  )  ; 
pot  (  "AReqPd,n,normal,request pulldown threshold"  )  ; 
pot  (  "AReqEndPd,n,normal,handshake state machine pulldown bias current"  )  ; 
pot  (  "AEPuX,p,normal,AER column pullup"  )  ; 
pot  (  "AEPuY,p,normal,AER row pullup"  )  ; 
pot  (  "If_threshold,n,normal,integrate and fire intensity neuroon threshold"  )  ; 
pot  (  "If_refractory,n,normal,integrate and fire intensity neuron refractory period bias current"  )  ; 
pot  (  "FollPadBias,n,normal,follower pad buffer bias current"  )  ; 
pot  (  "ROgate,p,normal,bias voltage for log readout transistor "  )  ; 
pot  (  "ROcas,p,normal,bias voltage for log readout cascode "  )  ; 
pot  (  "refcurrent,p,normal,reference current for log readout "  )  ; 
pot  (  "RObuffer,n,normal,buffer bias for log readout"  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   Error  (  e  .  toString  (  )  )  ; 
} 
loadPreferences  (  )  ; 
} 

@  Override 
public   void   setHardwareInterface  (  BiasgenHardwareInterface   hardwareInterface  )  { 
super  .  setHardwareInterface  (  hardwareInterface  )  ; 
if  (  hardwareInterface  ==  null  )  { 
if  (  adcProxy  !=  null  )  { 
adcProxy  .  setHw  (  null  )  ; 
} 
}  else   if  (  hardwareInterface   instanceof   cDVSTestHardwareInterface  )  { 
adcProxy  .  setHw  (  (  ch  .  unizh  .  ini  .  jaer  .  chip  .  dvs320  .  cDVSTestHardwareInterface  )  hardwareInterface  )  ; 
}  else  { 
log  .  warning  (  "cannot set ADC hardware interface proxy hardware interface to "  +  hardwareInterface  +  " because it is not a cDVSTestHardwareInterface"  )  ; 
} 
} 

private   void   pot  (  String   s  )  throws   ParseException  { 
try  { 
String   d  =  ","  ; 
StringTokenizer   t  =  new   StringTokenizer  (  s  ,  d  )  ; 
if  (  t  .  countTokens  (  )  !=  4  )  { 
throw   new   Error  (  "only "  +  t  .  countTokens  (  )  +  " tokens in pot "  +  s  +  "; use , to separate tokens for name,sex,type,tooltip\nsex=n|p, type=normal|cascode"  )  ; 
} 
String   name  =  t  .  nextToken  (  )  ; 
String   a  ; 
a  =  t  .  nextToken  (  )  ; 
Sex   sex  =  null  ; 
if  (  a  .  equalsIgnoreCase  (  "n"  )  )  { 
sex  =  Sex  .  N  ; 
}  else   if  (  a  .  equalsIgnoreCase  (  "p"  )  )  { 
sex  =  Sex  .  P  ; 
}  else  { 
throw   new   ParseException  (  s  ,  s  .  lastIndexOf  (  a  )  )  ; 
} 
a  =  t  .  nextToken  (  )  ; 
Type   type  =  null  ; 
if  (  a  .  equalsIgnoreCase  (  "normal"  )  )  { 
type  =  Type  .  NORMAL  ; 
}  else   if  (  a  .  equalsIgnoreCase  (  "cascode"  )  )  { 
type  =  Type  .  CASCODE  ; 
}  else  { 
throw   new   ParseException  (  s  ,  s  .  lastIndexOf  (  a  )  )  ; 
} 
String   tip  =  t  .  nextToken  (  )  ; 
getPotArray  (  )  .  addPot  (  new   ConfigurableIPotCDVSTest  (  this  ,  name  ,  pos  ++  ,  type  ,  sex  ,  false  ,  true  ,  ConfigurableIPotCDVSTest  .  maxBitValue  /  2  ,  ConfigurableIPotCDVSTest  .  maxBuffeBitValue  ,  pos  ,  tip  )  )  ; 
}  catch  (  Exception   e  )  { 
throw   new   Error  (  e  .  toString  (  )  )  ; 
} 
} 

@  Override 
public   void   loadPreferences  (  )  { 
super  .  loadPreferences  (  )  ; 
if  (  hasPreferencesList  !=  null  )  { 
for  (  HasPreference   hp  :  hasPreferencesList  )  { 
hp  .  loadPreference  (  )  ; 
} 
} 
if  (  ssBiases  !=  null  )  { 
for  (  ShiftedSourceBias   ss  :  ssBiases  )  { 
ss  .  loadPreferences  (  )  ; 
} 
} 
if  (  thermometerDAC  !=  null  )  { 
thermometerDAC  .  loadPreferences  (  )  ; 
} 
} 

@  Override 
public   void   storePreferences  (  )  { 
super  .  storePreferences  (  )  ; 
for  (  HasPreference   hp  :  hasPreferencesList  )  { 
hp  .  storePreference  (  )  ; 
} 
if  (  ssBiases  !=  null  )  { 
for  (  ShiftedSourceBias   ss  :  ssBiases  )  { 
ss  .  storePreferences  (  )  ; 
} 
} 
if  (  thermometerDAC  !=  null  )  { 
thermometerDAC  .  storePreferences  (  )  ; 
} 
} 







@  Override 
public   JPanel   buildControlPanel  (  )  { 
bPanel  =  new   JPanel  (  )  ; 
bPanel  .  setLayout  (  new   BorderLayout  (  )  )  ; 
bgTabbedPane  =  new   JTabbedPane  (  )  ; 
JPanel   combinedBiasShiftedSourcePanel  =  new   JPanel  (  )  ; 
combinedBiasShiftedSourcePanel  .  setLayout  (  new   BoxLayout  (  combinedBiasShiftedSourcePanel  ,  BoxLayout  .  Y_AXIS  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  super  .  buildControlPanel  (  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   ShiftedSourceControls  (  ssn  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   ShiftedSourceControls  (  ssp  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   ShiftedSourceControls  (  ssnMid  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   ShiftedSourceControls  (  sspMid  )  )  ; 
combinedBiasShiftedSourcePanel  .  add  (  new   VPotGUIControl  (  thermometerDAC  )  )  ; 
bgTabbedPane  .  addTab  (  "Biases"  ,  combinedBiasShiftedSourcePanel  )  ; 
bgTabbedPane  .  addTab  (  "Output control"  ,  new   cDVSTest20OutputControlPanel  (  cDVSTest20  .  this  )  )  ; 
final   String   tabTitle  =  "ADC control"  ; 
bgTabbedPane  .  addTab  (  tabTitle  ,  new   ParameterControlPanel  (  adcProxy  )  )  ; 
bPanel  .  add  (  bgTabbedPane  ,  BorderLayout  .  CENTER  )  ; 
int   tabnum  =  getPrefs  (  )  .  getInt  (  "cDVSTest20.bgTabbedPaneSelectedIndex"  ,  0  )  ; 
if  (  tabnum  >  bgTabbedPane  .  getTabCount  (  )  -  1  )  tabnum  =  0  ; 
bgTabbedPane  .  setSelectedIndex  (  tabnum  )  ; 
bgTabbedPane  .  addMouseListener  (  new   java  .  awt  .  event  .  MouseAdapter  (  )  { 

@  Override 
public   void   mouseClicked  (  java  .  awt  .  event  .  MouseEvent   evt  )  { 
tabbedPaneMouseClicked  (  evt  )  ; 
} 
}  )  ; 
return   bPanel  ; 
} 

private   void   tabbedPaneMouseClicked  (  java  .  awt  .  event  .  MouseEvent   evt  )  { 
getPrefs  (  )  .  putInt  (  "cDVSTest20.bgTabbedPaneSelectedIndex"  ,  bgTabbedPane  .  getSelectedIndex  (  )  )  ; 
} 


@  Override 
public   byte  [  ]  formatConfigurationBytes  (  Biasgen   biasgen  )  { 
ByteBuffer   bb  =  ByteBuffer  .  allocate  (  1000  )  ; 
byte  [  ]  biasBytes  =  super  .  formatConfigurationBytes  (  biasgen  )  ; 
byte  [  ]  configBytes  =  allMuxes  .  formatConfigurationBytes  (  )  ; 
bb  .  put  (  configBytes  )  ; 
byte   vdac  =  (  byte  )  thermometerDAC  .  getBitValue  (  )  ; 
bb  .  put  (  vdac  )  ; 
bb  .  put  (  biasBytes  )  ; 
for  (  ShiftedSourceBias   ss  :  ssBiases  )  { 
bb  .  put  (  ss  .  getBinaryRepresentation  (  )  )  ; 
} 
byte  [  ]  allBytes  =  new   byte  [  bb  .  position  (  )  ]  ; 
bb  .  flip  (  )  ; 
bb  .  get  (  allBytes  )  ; 
return   allBytes  ; 
} 


public   final   float   RATIO  =  1.05f  ; 


public   final   float   MIN_THRESHOLD_RATIO  =  2f  ; 

public   final   float   MAX_DIFF_ON_CURRENT  =  6e-6f  ; 

public   final   float   MIN_DIFF_OFF_CURRENT  =  1e-9f  ; 

public   synchronized   void   increaseThreshold  (  )  { 
if  (  diffOn  .  getCurrent  (  )  *  RATIO  >  MAX_DIFF_ON_CURRENT  )  { 
return  ; 
} 
if  (  diffOff  .  getCurrent  (  )  /  RATIO  <  MIN_DIFF_OFF_CURRENT  )  { 
return  ; 
} 
diffOn  .  changeByRatio  (  RATIO  )  ; 
diffOff  .  changeByRatio  (  1  /  RATIO  )  ; 
} 

public   synchronized   void   decreaseThreshold  (  )  { 
float   diffI  =  diff  .  getCurrent  (  )  ; 
if  (  diffOn  .  getCurrent  (  )  /  MIN_THRESHOLD_RATIO  <  diffI  )  { 
return  ; 
} 
if  (  diffOff  .  getCurrent  (  )  >  diffI  /  MIN_THRESHOLD_RATIO  )  { 
return  ; 
} 
diffOff  .  changeByRatio  (  RATIO  )  ; 
diffOn  .  changeByRatio  (  1  /  RATIO  )  ; 
} 

public   synchronized   void   increaseRefractoryPeriod  (  )  { 
refr  .  changeByRatio  (  1  /  RATIO  )  ; 
} 

public   synchronized   void   decreaseRefractoryPeriod  (  )  { 
refr  .  changeByRatio  (  RATIO  )  ; 
} 

public   synchronized   void   increaseBandwidth  (  )  { 
pr  .  changeByRatio  (  RATIO  )  ; 
foll  .  changeByRatio  (  RATIO  )  ; 
} 

public   synchronized   void   decreaseBandwidth  (  )  { 
pr  .  changeByRatio  (  1  /  RATIO  )  ; 
foll  .  changeByRatio  (  1  /  RATIO  )  ; 
} 

public   synchronized   void   moreONType  (  )  { 
diffOn  .  changeByRatio  (  1  /  RATIO  )  ; 
diffOff  .  changeByRatio  (  RATIO  )  ; 
} 

public   synchronized   void   moreOFFType  (  )  { 
diffOn  .  changeByRatio  (  RATIO  )  ; 
diffOff  .  changeByRatio  (  1  /  RATIO  )  ; 
} 


class   OutputMux   extends   Observable   implements   HasPreference  ,  RemoteControlled  { 

int   nSrBits  ; 

int   nInputs  ; 

OutputMap   map  ; 

private   String   name  =  "OutputMux"  ; 

int   selectedChannel  =  -  1  ; 

String   bitString  =  null  ; 

final   String   CMD_SELECTMUX  =  "selectMux_"  ; 

OutputMux  (  int   nsr  ,  int   nin  ,  OutputMap   m  )  { 
nSrBits  =  nsr  ; 
nInputs  =  nin  ; 
map  =  m  ; 
hasPreferencesList  .  add  (  this  )  ; 
} 

@  Override 
public   String   toString  (  )  { 
return  "OutputMux name="  +  name  +  " nSrBits="  +  nSrBits  +  " nInputs="  +  nInputs  +  " selectedChannel="  +  selectedChannel  +  " channelName="  +  getChannelName  (  selectedChannel  )  +  " code="  +  getCode  (  selectedChannel  )  +  " getBitString="  +  bitString  ; 
} 

void   select  (  int   i  )  { 
selectWithoutNotify  (  i  )  ; 
setChanged  (  )  ; 
notifyObservers  (  )  ; 
} 

void   selectWithoutNotify  (  int   i  )  { 
selectedChannel  =  i  ; 
try  { 
sendConfiguration  (  cDVSTest20  .  cDVSTestBiasgen  .  this  )  ; 
}  catch  (  HardwareInterfaceException   ex  )  { 
log  .  warning  (  "selecting output: "  +  ex  )  ; 
} 
} 

void   put  (  int   k  ,  String   name  )  { 
map  .  put  (  k  ,  name  )  ; 
} 

OutputMap   getMap  (  )  { 
return   map  ; 
} 

int   getCode  (  int   i  )  { 
return   map  .  get  (  i  )  ; 
} 





String   getBitString  (  )  { 
StringBuilder   s  =  new   StringBuilder  (  )  ; 
int   code  =  selectedChannel  !=  -  1  ?  getCode  (  selectedChannel  )  :  0  ; 
int   k  =  nSrBits  -  1  ; 
while  (  k  >=  0  )  { 
int   x  =  code  &  (  1  <<  k  )  ; 
boolean   b  =  (  x  ==  0  )  ; 
s  .  append  (  b  ?  '0'  :  '1'  )  ; 
k  --  ; 
} 
bitString  =  s  .  toString  (  )  ; 
return   bitString  ; 
} 

String   getChannelName  (  int   i  )  { 
return   map  .  nameMap  .  get  (  i  )  ; 
} 

public   String   getName  (  )  { 
return   name  ; 
} 

public   void   setName  (  String   name  )  { 
this  .  name  =  name  ; 
} 

private   String   key  (  )  { 
return  "cDVSTest."  +  getClass  (  )  .  getSimpleName  (  )  +  "."  +  name  +  ".selectedChannel"  ; 
} 

@  Override 
public   void   loadPreference  (  )  { 
select  (  getPrefs  (  )  .  getInt  (  key  (  )  ,  -  1  )  )  ; 
} 

@  Override 
public   void   storePreference  (  )  { 
getPrefs  (  )  .  putInt  (  key  (  )  ,  selectedChannel  )  ; 
} 







@  Override 
public   String   processRemoteControlCommand  (  RemoteControlCommand   command  ,  String   input  )  { 
String  [  ]  t  =  input  .  split  (  "\\s"  )  ; 
if  (  t  .  length  <  2  )  { 
return  "? "  +  this  +  "\n"  ; 
}  else  { 
String   s  =  t  [  0  ]  ,  a  =  t  [  1  ]  ; 
try  { 
select  (  Integer  .  parseInt  (  a  )  )  ; 
return   this  +  "\n"  ; 
}  catch  (  NumberFormatException   e  )  { 
log  .  warning  (  "Bad number format: "  +  input  +  " caused "  +  e  )  ; 
return   e  .  toString  (  )  +  "\n"  ; 
}  catch  (  Exception   ex  )  { 
log  .  warning  (  ex  .  toString  (  )  )  ; 
return   ex  .  toString  (  )  ; 
} 
} 
} 
} 

class   OutputMap   extends   HashMap  <  Integer  ,  Integer  >  { 

HashMap  <  Integer  ,  String  >  nameMap  =  new   HashMap  <  Integer  ,  String  >  (  )  ; 

void   put  (  int   k  ,  int   v  ,  String   name  )  { 
put  (  k  ,  v  )  ; 
nameMap  .  put  (  k  ,  name  )  ; 
} 

void   put  (  int   k  ,  String   name  )  { 
nameMap  .  put  (  k  ,  name  )  ; 
} 
} 

class   VoltageOutputMap   extends   OutputMap  { 

final   void   put  (  int   k  ,  int   v  )  { 
put  (  k  ,  v  ,  "Voltage "  +  k  )  ; 
} 

VoltageOutputMap  (  )  { 
put  (  0  ,  1  )  ; 
put  (  1  ,  3  )  ; 
put  (  2  ,  5  )  ; 
put  (  3  ,  7  )  ; 
put  (  4  ,  9  )  ; 
put  (  5  ,  11  )  ; 
put  (  6  ,  13  )  ; 
put  (  7  ,  15  )  ; 
} 
} 

class   DigitalOutputMap   extends   OutputMap  { 

DigitalOutputMap  (  )  { 
for  (  int   i  =  0  ;  i  <  16  ;  i  ++  )  { 
put  (  i  ,  i  ,  "DigOut "  +  i  )  ; 
} 
} 
} 

class   VoltageOutputMux   extends   OutputMux  { 

VoltageOutputMux  (  int   n  )  { 
super  (  4  ,  8  ,  new   VoltageOutputMap  (  )  )  ; 
setName  (  "Voltages"  +  n  )  ; 
} 
} 

class   LogicMux   extends   OutputMux  { 

LogicMux  (  int   n  )  { 
super  (  4  ,  16  ,  new   DigitalOutputMap  (  )  )  ; 
setName  (  "LogicSignals"  +  n  )  ; 
} 
} 

class   AllMuxes   extends   ArrayList  <  OutputMux  >  { 

OutputMux  [  ]  vmuxes  =  {  new   VoltageOutputMux  (  1  )  ,  new   VoltageOutputMux  (  2  )  ,  new   VoltageOutputMux  (  3  )  ,  new   VoltageOutputMux  (  4  )  }  ; 

OutputMux  [  ]  dmuxes  =  {  new   LogicMux  (  1  )  ,  new   LogicMux  (  2  )  ,  new   LogicMux  (  3  )  ,  new   LogicMux  (  4  )  ,  new   LogicMux  (  5  )  }  ; 

byte  [  ]  formatConfigurationBytes  (  )  { 
int   nBits  =  0  ; 
StringBuilder   s  =  new   StringBuilder  (  )  ; 
s  .  append  (  "000000"  )  ; 
s  .  append  (  "00"  )  ; 
nBits  =  8  ; 
for  (  OutputMux   m  :  this  )  { 
s  .  append  (  m  .  getBitString  (  )  )  ; 
nBits  +=  m  .  nSrBits  ; 
} 
BigInteger   bi  =  new   BigInteger  (  s  .  toString  (  )  ,  2  )  ; 
byte  [  ]  byteArray  =  bi  .  toByteArray  (  )  ; 
int   nbytes  =  (  nBits  %  8  ==  0  )  ?  (  nBits  /  8  )  :  (  nBits  /  8  +  1  )  ; 
byte  [  ]  bytes  =  new   byte  [  nbytes  ]  ; 
System  .  arraycopy  (  byteArray  ,  0  ,  bytes  ,  nbytes  -  byteArray  .  length  ,  byteArray  .  length  )  ; 
return   bytes  ; 
} 

AllMuxes  (  )  { 
addAll  (  Arrays  .  asList  (  dmuxes  )  )  ; 
addAll  (  Arrays  .  asList  (  vmuxes  )  )  ; 
dmuxes  [  0  ]  .  setName  (  "DigMux4"  )  ; 
dmuxes  [  1  ]  .  setName  (  "DigMux3"  )  ; 
dmuxes  [  2  ]  .  setName  (  "DigMux2"  )  ; 
dmuxes  [  3  ]  .  setName  (  "DigMux1"  )  ; 
dmuxes  [  4  ]  .  setName  (  "DigMux0"  )  ; 
for  (  int   i  =  0  ;  i  <  5  ;  i  ++  )  { 
dmuxes  [  i  ]  .  put  (  0  ,  "nRxcolE"  )  ; 
dmuxes  [  i  ]  .  put  (  1  ,  "nAxcolE"  )  ; 
dmuxes  [  i  ]  .  put  (  2  ,  "nRY0"  )  ; 
dmuxes  [  i  ]  .  put  (  3  ,  "AY0"  )  ; 
dmuxes  [  i  ]  .  put  (  4  ,  "nAX0"  )  ; 
dmuxes  [  i  ]  .  put  (  5  ,  "nRXon"  )  ; 
dmuxes  [  i  ]  .  put  (  6  ,  "arbtopR"  )  ; 
dmuxes  [  i  ]  .  put  (  7  ,  "arbtopA"  )  ; 
dmuxes  [  i  ]  .  put  (  8  ,  "FF1"  )  ; 
dmuxes  [  i  ]  .  put  (  9  ,  "Acol"  )  ; 
dmuxes  [  i  ]  .  put  (  10  ,  "Rcol"  )  ; 
dmuxes  [  i  ]  .  put  (  11  ,  "Rrow"  )  ; 
dmuxes  [  i  ]  .  put  (  12  ,  "RxcolG"  )  ; 
dmuxes  [  i  ]  .  put  (  13  ,  "nArow"  )  ; 
} 
dmuxes  [  0  ]  .  put  (  14  ,  "nResetRxcol"  )  ; 
dmuxes  [  0  ]  .  put  (  15  ,  "nArowBottom"  )  ; 
for  (  int   i  =  1  ;  i  <  5  ;  i  ++  )  { 
dmuxes  [  i  ]  .  put  (  14  ,  "FF2"  )  ; 
dmuxes  [  i  ]  .  put  (  15  ,  "RCarb"  )  ; 
} 
vmuxes  [  0  ]  .  setName  (  "AnaMux3"  )  ; 
vmuxes  [  1  ]  .  setName  (  "AnaMux2"  )  ; 
vmuxes  [  2  ]  .  setName  (  "AnaMux1"  )  ; 
vmuxes  [  3  ]  .  setName  (  "AnaMux0"  )  ; 
for  (  int   i  =  0  ;  i  <  4  ;  i  ++  )  { 
vmuxes  [  i  ]  .  put  (  0  ,  "readout"  )  ; 
vmuxes  [  i  ]  .  put  (  1  ,  "DiffAmpOut"  )  ; 
vmuxes  [  i  ]  .  put  (  2  ,  "InPh"  )  ; 
} 
vmuxes  [  0  ]  .  put  (  2  ,  "InPh"  )  ; 
vmuxes  [  0  ]  .  put  (  3  ,  "refcurrent"  )  ; 
vmuxes  [  0  ]  .  put  (  4  ,  "DiffAmpRef"  )  ; 
vmuxes  [  0  ]  .  put  (  5  ,  "log"  )  ; 
vmuxes  [  0  ]  .  put  (  6  ,  "Vt"  )  ; 
vmuxes  [  0  ]  .  put  (  7  ,  "top"  )  ; 
vmuxes  [  1  ]  .  put  (  2  ,  "InPh"  )  ; 
vmuxes  [  1  ]  .  put  (  3  ,  "refcurrent"  )  ; 
vmuxes  [  1  ]  .  put  (  4  ,  "DiffAmpRef"  )  ; 
vmuxes  [  1  ]  .  put  (  5  ,  "log"  )  ; 
vmuxes  [  1  ]  .  put  (  6  ,  "Vt"  )  ; 
vmuxes  [  1  ]  .  put  (  7  ,  "top"  )  ; 
vmuxes  [  2  ]  .  put  (  2  ,  "Vdiff"  )  ; 
vmuxes  [  2  ]  .  put  (  3  ,  "phi1"  )  ; 
vmuxes  [  2  ]  .  put  (  4  ,  "phi2"  )  ; 
vmuxes  [  2  ]  .  put  (  5  ,  "Vcoldiff"  )  ; 
vmuxes  [  2  ]  .  put  (  6  ,  "Vs"  )  ; 
vmuxes  [  2  ]  .  put  (  7  ,  "sum"  )  ; 
vmuxes  [  3  ]  .  put  (  2  ,  "Vdiff"  )  ; 
vmuxes  [  3  ]  .  put  (  3  ,  "phi1"  )  ; 
vmuxes  [  3  ]  .  put  (  4  ,  "phi2"  )  ; 
vmuxes  [  3  ]  .  put  (  5  ,  "Vcoldiff"  )  ; 
vmuxes  [  3  ]  .  put  (  6  ,  "Vs"  )  ; 
vmuxes  [  3  ]  .  put  (  7  ,  "sum"  )  ; 
} 
} 
} 




public   boolean   isDisplayLogIntensity  (  )  { 
return   displayLogIntensity  ; 
} 




public   void   setDisplayLogIntensity  (  boolean   displayLogIntensity  )  { 
this  .  displayLogIntensity  =  displayLogIntensity  ; 
getPrefs  (  )  .  putBoolean  (  "displayLogIntensity"  ,  displayLogIntensity  )  ; 
getAeViewer  (  )  .  interruptViewloop  (  )  ; 
} 




public   boolean   isDisplayColorChangeEvents  (  )  { 
return   displayColorChangeEvents  ; 
} 




public   void   setDisplayColorChangeEvents  (  boolean   displayColorChangeEvents  )  { 
this  .  displayColorChangeEvents  =  displayColorChangeEvents  ; 
getPrefs  (  )  .  putBoolean  (  "displayColorChangeEvents"  ,  displayColorChangeEvents  )  ; 
getAeViewer  (  )  .  interruptViewloop  (  )  ; 
} 




public   boolean   isDisplayLogIntensityChangeEvents  (  )  { 
return   displayLogIntensityChangeEvents  ; 
} 




public   void   setDisplayLogIntensityChangeEvents  (  boolean   displayLogIntensityChangeEvents  )  { 
this  .  displayLogIntensityChangeEvents  =  displayLogIntensityChangeEvents  ; 
getPrefs  (  )  .  putBoolean  (  "displayLogIntensityChangeEvents"  ,  displayLogIntensityChangeEvents  )  ; 
getAeViewer  (  )  .  interruptViewloop  (  )  ; 
} 
} 

