import   com  .  frinika  .  softsynth  .  string  .  Bowed  ; 
import   com  .  frinika  .  softsynth  .  string  .  Plucked1  ; 
import   com  .  frinika  .  softsynth  .  string  .  Plucked2  ; 
import   java  .  io  .  IOException  ; 
import   java  .  nio  .  ByteBuffer  ; 
import   java  .  nio  .  ByteOrder  ; 
import   java  .  nio  .  FloatBuffer  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  logging  .  Level  ; 
import   java  .  util  .  logging  .  Logger  ; 
import   javax  .  sound  .  midi  .  InvalidMidiDataException  ; 
import   javax  .  sound  .  midi  .  MidiDevice  ; 
import   javax  .  sound  .  midi  .  MidiMessage  ; 
import   javax  .  sound  .  midi  .  MidiSystem  ; 
import   javax  .  sound  .  midi  .  MidiUnavailableException  ; 
import   javax  .  sound  .  midi  .  Receiver  ; 
import   javax  .  sound  .  midi  .  ShortMessage  ; 
import   javax  .  sound  .  midi  .  Soundbank  ; 
import   javax  .  sound  .  midi  .  MidiDevice  .  Info  ; 
import   javax  .  sound  .  sampled  .  AudioFormat  ; 
import   javax  .  sound  .  sampled  .  AudioInputStream  ; 
import   javax  .  swing  .  JComponent  ; 
import   javax  .  swing  .  JFrame  ; 
import   javax  .  swing  .  JOptionPane  ; 
import   uk  .  org  .  toot  .  audio  .  core  .  AudioBuffer  ; 
import   uk  .  org  .  toot  .  audio  .  core  .  AudioProcess  ; 
import   uk  .  org  .  toot  .  audio  .  server  .  AudioClient  ; 
import   uk  .  org  .  toot  .  audio  .  server  .  AudioServer  ; 
import   uk  .  org  .  toot  .  audio  .  server  .  AudioServerConfiguration  ; 
import   uk  .  org  .  toot  .  audio  .  server  .  AudioServerServices  ; 
import   uk  .  org  .  toot  .  audio  .  server  .  IOAudioProcess  ; 
import   uk  .  org  .  toot  .  audio  .  server  .  MultiIOJavaSoundAudioServer  ; 
import   uk  .  org  .  toot  .  swingui  .  audioui  .  serverui  .  AudioServerUIServices  ; 
import   com  .  sun  .  media  .  sound  .  AudioSynthesizer  ; 





public   class   TestToot  { 

static   AudioServer   server  ; 

private   static   AudioServerConfiguration   serverConfig  ; 

static   MidiDevice   dev  ; 

private   static   boolean   isLinux  =  System  .  getProperty  (  "os.name"  )  .  equals  (  "Linux"  )  ; 

public   static   void   main  (  String  [  ]  args  )  throws   Exception  { 
server  =  new   MultiIOJavaSoundAudioServer  (  )  ; 
final   AudioBuffer   buff  =  server  .  createAudioBuffer  (  "BUFF"  )  ; 
List  <  String  >  list  =  server  .  getAvailableOutputNames  (  )  ; 
Object   a  [  ]  =  new   Object  [  list  .  size  (  )  ]  ; 
a  =  list  .  toArray  (  a  )  ; 
Object   selectedValue  =  JOptionPane  .  showInputDialog  (  null  ,  "Select audio output"  ,  "Please"  ,  JOptionPane  .  INFORMATION_MESSAGE  ,  null  ,  a  ,  a  [  0  ]  )  ; 
final   IOAudioProcess   out  =  server  .  openAudioOutput  (  (  String  )  selectedValue  ,  "output"  )  ; 
Info   infodev  [  ]  =  MidiSystem  .  getMidiDeviceInfo  (  )  ; 
Info   synthInfo  =  null  ; 
for  (  Info   info  :  infodev  )  { 
System  .  out  .  println  (  info  )  ; 
if  (  info  .  getName  (  )  .  equals  (  "Gervill"  )  )  { 
synthInfo  =  info  ; 
break  ; 
} 
} 
MidiDevice   outDev  =  MidiSystem  .  getMidiDevice  (  synthInfo  )  ; 
final   AudioSynthesizer   audosynth  =  (  AudioSynthesizer  )  outDev  ; 
MidiDevice  .  Info  [  ]  infos  =  MidiSystem  .  getMidiDeviceInfo  (  )  ; 
dev  =  null  ; 
for  (  int   i  =  0  ;  i  <  infos  .  length  ;  i  ++  )  { 
System  .  out  .  println  (  infos  [  i  ]  .  getName  (  )  )  ; 
} 
for  (  int   i  =  0  ;  i  <  infos  .  length  ;  i  ++  )  { 
if  (  infos  [  i  ]  .  getName  (  )  .  equals  (  "SL [hw:1,0]"  )  )  { 
MidiDevice  .  Info   rtinfo  =  infos  [  i  ]  ; 
dev  =  MidiSystem  .  getMidiDevice  (  rtinfo  )  ; 
if  (  dev  .  getMaxTransmitters  (  )  !=  0  )  { 
break  ; 
} 
dev  =  null  ; 
} 
} 
if  (  dev  ==  null  )  { 
return  ; 
} 
dev  .  open  (  )  ; 
final   AudioProcess   synthVoice  =  createSynthAudioProcess  (  audosynth  )  ; 
AudioClient   client  =  new   AudioClient  (  )  { 

public   void   setEnabled  (  boolean   arg0  )  { 
} 

public   void   work  (  int   arg0  )  { 
synthVoice  .  processAudio  (  buff  )  ; 
out  .  processAudio  (  buff  )  ; 
} 
}  ; 
Soundbank   sbk  =  new   Plucked2  (  )  ; 
audosynth  .  unloadAllInstruments  (  audosynth  .  getDefaultSoundbank  (  )  )  ; 
audosynth  .  loadAllInstruments  (  sbk  )  ; 
Receiver   du  =  audosynth  .  getReceiver  (  )  ; 
dev  .  getTransmitter  (  )  .  setReceiver  (  new   FilterReceiver  (  du  )  )  ; 
server  .  setClient  (  client  )  ; 
server  .  start  (  )  ; 
serverConfig  =  AudioServerServices  .  createServerConfiguration  (  server  )  ; 
configure  (  )  ; 
Runtime  .  getRuntime  (  )  .  addShutdownHook  (  new   Thread  (  )  { 

public   void   run  (  )  { 
System  .  out  .  println  (  "Stop..."  )  ; 
dev  .  close  (  )  ; 
audosynth  .  close  (  )  ; 
} 
}  )  ; 
} 

static   class   FilterReceiver   implements   Receiver  { 

protected   Receiver   chained  ; 

public   FilterReceiver  (  Receiver   chained  )  { 
this  .  chained  =  chained  ; 
} 

public   void   send  (  MidiMessage   mess  ,  long   timeStamp  )  { 
if  (  mess  .  getStatus  (  )  >=  ShortMessage  .  MIDI_TIME_CODE  )  { 
return  ; 
} 
if  (  mess   instanceof   ShortMessage  )  { 
ShortMessage   shm  =  (  ShortMessage  )  mess  ; 
if  (  isLinux  )  { 
if  (  shm  .  getStatus  (  )  ==  ShortMessage  .  PITCH_BEND  )  { 
short   low  =  (  byte  )  shm  .  getData1  (  )  ; 
short   high  =  (  byte  )  shm  .  getData2  (  )  ; 
int   channel  =  shm  .  getChannel  (  )  ; 
low  =  (  byte  )  shm  .  getData1  (  )  ; 
high  =  (  byte  )  shm  .  getData2  (  )  ; 
high  =  (  short  )  (  (  high  +  64  )  &  0x007f  )  ; 
try  { 
shm  .  setMessage  (  ShortMessage  .  PITCH_BEND  ,  channel  ,  low  ,  high  )  ; 
}  catch  (  InvalidMidiDataException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
} 
int   cmd  =  shm  .  getCommand  (  )  ; 
if  (  cmd  !=  240  )  { 
int   chn  =  shm  .  getChannel  (  )  ; 
int   dat1  =  shm  .  getData1  (  )  ; 
int   dat2  =  shm  .  getData2  (  )  ; 
System  .  out  .  println  (  " cmd:"  +  cmd  +  " chn:"  +  chn  +  " data:"  +  dat1  +  " "  +  dat2  )  ; 
} 
} 
chained  .  send  (  mess  ,  -  1  )  ; 
} 

public   void   close  (  )  { 
chained  .  close  (  )  ; 
} 
} 

static   AudioProcess   createSynthAudioProcess  (  AudioSynthesizer   audosynth  )  throws   MidiUnavailableException  { 
AudioFormat  .  Encoding   PCM_FLOAT  =  new   AudioFormat  .  Encoding  (  "PCM_FLOAT"  )  ; 
AudioFormat   format  =  new   AudioFormat  (  PCM_FLOAT  ,  44100  ,  32  ,  2  ,  4  *  2  ,  44100  ,  ByteOrder  .  nativeOrder  (  )  .  equals  (  ByteOrder  .  BIG_ENDIAN  )  )  ; 
final   AudioInputStream   ais  =  audosynth  .  openStream  (  format  ,  null  )  ; 
System  .  out  .  println  (  "PCM_FLOAT Encoding used!"  )  ; 
final   AudioProcess   synthVoice  =  new   AudioProcess  (  )  { 

byte  [  ]  streamBuffer  =  null  ; 

float  [  ]  floatArray  =  null  ; 

FloatBuffer   floatBuffer  =  null  ; 

public   void   close  (  )  { 
} 

public   void   open  (  )  { 
} 

public   int   processAudio  (  AudioBuffer   buffer  )  { 
if  (  buffer  ==  null  )  { 
return   0  ; 
} 
if  (  streamBuffer  ==  null  ||  streamBuffer  .  length  !=  buffer  .  getSampleCount  (  )  *  8  )  { 
ByteBuffer   bytebuffer  =  ByteBuffer  .  allocate  (  buffer  .  getSampleCount  (  )  *  8  )  .  order  (  ByteOrder  .  nativeOrder  (  )  )  ; 
streamBuffer  =  bytebuffer  .  array  (  )  ; 
floatArray  =  new   float  [  buffer  .  getSampleCount  (  )  *  2  ]  ; 
floatBuffer  =  bytebuffer  .  asFloatBuffer  (  )  ; 
} 
try  { 
ais  .  read  (  streamBuffer  ,  0  ,  buffer  .  getSampleCount  (  )  *  8  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
floatBuffer  .  position  (  0  )  ; 
floatBuffer  .  get  (  floatArray  )  ; 
float  [  ]  left  =  buffer  .  getChannel  (  0  )  ; 
float  [  ]  right  =  buffer  .  getChannel  (  1  )  ; 
for  (  int   n  =  0  ;  n  <  buffer  .  getSampleCount  (  )  *  2  ;  n  +=  2  )  { 
left  [  n  /  2  ]  =  floatArray  [  n  ]  ; 
right  [  n  /  2  ]  =  floatArray  [  n  +  1  ]  ; 
} 
return   AUDIO_OK  ; 
} 
}  ; 
return   synthVoice  ; 
} 

static   JFrame   configureFrame  ; 






public   static   void   configure  (  )  { 
if  (  configureFrame  !=  null  )  { 
configureFrame  .  setVisible  (  true  )  ; 
return  ; 
} 
final   JComponent   ui  =  AudioServerUIServices  .  createServerUI  (  server  ,  serverConfig  )  ; 
if  (  ui  ==  null  )  { 
return  ; 
} 
configureFrame  =  new   JFrame  (  )  ; 
configureFrame  .  setDefaultCloseOperation  (  JFrame  .  EXIT_ON_CLOSE  )  ; 
configureFrame  .  setAlwaysOnTop  (  true  )  ; 
configureFrame  .  setContentPane  (  ui  )  ; 
configureFrame  .  pack  (  )  ; 
configureFrame  .  setVisible  (  true  )  ; 
} 
} 

