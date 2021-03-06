import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  lang  .  reflect  .  InvocationTargetException  ; 

class   Debugger   implements   jdpConstants  { 




private   int   initial_bp  =  0  ; 




private   String   initial_macro  =  null  ; 




private   String   bi_runner  =  null  ; 




private   String   bi_args  [  ]  ; 




private   boolean   quit  =  false  ; 




private   JDPCommandInterface   jdp_console  ; 




private   OsProcess   user  ; 




private   String   saved_args  [  ]  ; 




private   String   saved_progname  ; 




private   breakpointList   saved_bpset  ; 







private   int   printMode  ; 




public   static   boolean   interpretMode  =  false  ; 




private   boolean   viewBoot  ; 




private   boolean   dejavu  ; 




private   jdpMacro   macro  ; 




private   int   debuggerEnvironment  ; 

private   final   int   EXTERNALCREATE  =  1  ; 

private   final   int   EXTERNALATTACH  =  2  ; 

private   final   int   INTERNAL  =  3  ; 




private   boolean   runstat  ; 





String   classesNeededFilename  =  null  ; 






String   classpath  =  null  ; 




static   char   integerPreference  =  'd'  ; 

static   char   stackPreference  =  'x'  ; 

static   char   fprPreference  =  'f'  ; 












public   Debugger  (  int   bp  ,  String   runner  ,  boolean   rawMode  ,  boolean   interpreted  ,  String   init_macro  ,  JDPCommandInterface   console  ,  boolean   _viewBoot  ,  boolean   _dejavu  )  { 
Platform  .  init  (  )  ; 
initial_bp  =  bp  +  16  ; 
if  (  init_macro  !=  null  )  initial_macro  =  init_macro  +  ".jdp"  ;  else   initial_macro  =  null  ; 
bi_runner  =  runner  ; 
jdp_console  =  console  ; 
printMode  =  PRINTASSEMBLY  ; 
interpretMode  =  interpreted  ; 
viewBoot  =  _viewBoot  ; 
dejavu  =  _dejavu  ; 
if  (  runner  ==  null  )  debuggerEnvironment  =  EXTERNALATTACH  ;  else   debuggerEnvironment  =  EXTERNALCREATE  ; 
macro  =  new   jdpMacro  (  )  ; 
} 











public   void   init  (  String   args  [  ]  )  { 
int   i  ,  status  ; 
saved_args  =  args  ; 
VM_Method   mymethod  [  ]  ; 
VM_LineNumberMap   mylinemap  ; 
parseRunArgs  (  args  )  ; 
user  =  new   OsProcessExternal  (  bi_runner  ,  bi_args  ,  saved_progname  ,  classesNeededFilename  ,  classpath  )  ; 
setInitialBreakPoint  (  )  ; 
if  (  initial_macro  !=  null  )  { 
macro  .  load  (  initial_macro  )  ; 
}  else   if  (  (  new   File  (  "startup.jdp"  )  )  .  exists  (  )  )  { 
macro  .  load  (  "startup.jdp"  )  ; 
} 
} 




public   void   exit  (  )  { 
if  (  user  !=  null  )  user  .  pkill  (  )  ; 
} 

public   static   void   debugMarker  (  )  { 
} 










public   void   attach  (  int   processID  ,  String   args  [  ]  )  { 
parseRunArgs  (  args  )  ; 
try  { 
user  =  new   OsProcessExternal  (  processID  ,  saved_progname  ,  classesNeededFilename  ,  classpath  )  ; 
user  .  reg  .  cacheJTOC  (  )  ; 
if  (  interpretMode  )  { 
mapVM  .  cachePointers  (  )  ; 
} 
user  .  bmap  .  fillBootMethodTable  (  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printCurrentInstr  (  )  )  ; 
}  catch  (  OsProcessException   e  )  { 
} 
} 




public   void   exitAttached  (  )  { 
if  (  user  !=  null  )  { 
user  .  bpset  .  clearAllBreakpoint  (  )  ; 
user  .  mdetach  (  )  ; 
} 
} 





private   void   parseRunArgs  (  String   args  [  ]  )  { 
String   bootimage  =  null  ; 
int   i  ; 
for  (  i  =  0  ;  i  <  args  .  length  ;  i  ++  )  { 
if  (  args  [  i  ]  .  equals  (  "-n"  )  )  { 
classesNeededFilename  =  args  [  ++  i  ]  ; 
}  else   if  (  args  [  i  ]  .  equals  (  "-classpath"  )  )  { 
classpath  =  args  [  ++  i  ]  ; 
}  else   if  (  args  [  i  ]  .  equals  (  "-i"  )  )  { 
bootimage  =  args  [  ++  i  ]  ; 
}  else  { 
break  ; 
} 
} 
if  (  classpath  ==  null  )  { 
jdp_console  .  writeOutput  (  "ERROR:  no classpath, static fields will be incorrect"  )  ; 
} 
if  (  classesNeededFilename  ==  null  )  { 
jdp_console  .  writeOutput  (  "ERROR: no classesNeeded file, line number will not be correct"  )  ; 
return  ; 
} 
if  (  bootimage  !=  null  )  { 
saved_progname  =  bootimage  ; 
bi_args  =  new   String  [  args  .  length  -  i  +  2  ]  ; 
int   j  =  0  ; 
bi_args  [  j  ++  ]  =  bi_runner  ; 
bi_args  [  j  ++  ]  =  "-X:i="  +  bootimage  ; 
for  (  int   k  =  i  ;  k  <  args  .  length  ;  k  ++  )  { 
bi_args  [  j  ++  ]  =  args  [  k  ]  ; 
} 
}  else  { 
saved_progname  =  args  [  args  .  length  -  1  ]  ; 
bi_args  =  new   String  [  2  ]  ; 
bi_args  [  0  ]  =  bi_runner  ; 
bi_args  [  1  ]  =  saved_progname  ; 
} 
} 

public   boolean   runCommand  (  )  { 
String   cmd  ; 
String   cmd_args  [  ]  ; 
if  (  macro  .  next  (  )  )  { 
String   cmd_arg_string  =  " "  ; 
cmd  =  macro  .  cmd  (  )  ; 
cmd_args  =  macro  .  args  (  )  ; 
for  (  int   i  =  0  ;  i  <  cmd_args  .  length  ;  i  ++  )  { 
cmd_arg_string  +=  cmd_args  [  i  ]  +  " "  ; 
} 
jdp_console  .  writeOutput  (  "\n"  )  ; 
jdp_console  .  writeOutput  (  "Macro line "  +  macro  .  currentLine  (  )  +  ": "  +  cmd  +  cmd_arg_string  )  ; 
}  else  { 
jdp_console  .  readCommand  (  user  )  ; 
cmd  =  jdp_console  .  cmd  (  )  ; 
cmd_args  =  jdp_console  .  args  (  )  ; 
} 
if  (  !  cmd  .  equals  (  ""  )  )  { 
if  (  cmd  .  equals  (  "quit"  )  ||  cmd  .  equals  (  "q"  )  )  { 
return   true  ; 
}  else  { 
try  { 
return   jdpCommand  (  cmd  ,  cmd_args  )  ; 
}  catch  (  Exception   e  )  { 
jdp_console  .  writeOutput  (  "ERROR executing jdp command: "  +  e  .  getMessage  (  )  )  ; 
jdp_console  .  writeOutput  (  "email to jvm-coders or try again . . . "  )  ; 
} 
} 
} 
return   false  ; 
} 















private   boolean   jdpCommand  (  String   command  ,  String  [  ]  args  )  { 
runstat  =  true  ; 
int   addr  ,  count  ; 
if  (  user  ==  null  )  { 
if  (  command  .  equals  (  "run"  )  )  { 
switch  (  args  .  length  )  { 
case   0  : 
restart  (  saved_args  )  ; 
break  ; 
default  : 
restart  (  args  )  ; 
} 
}  else   if  (  command  .  equals  (  "help"  )  ||  command  .  equals  (  "h"  )  ||  command  .  equals  (  "?"  )  )  { 
if  (  args  .  length  ==  0  )  printHelp  (  ""  )  ;  else   printHelp  (  args  [  0  ]  )  ; 
}  else  { 
jdp_console  .  writeOutput  (  "No program running, enter:  run ... "  )  ; 
} 
return   false  ; 
} 
if  (  command  .  equals  (  "step"  )  ||  command  .  equals  (  "s"  )  )  { 
boolean   skip_prolog  =  false  ; 
printMode  =  PRINTASSEMBLY  ; 
runstat  =  user  .  pstep  (  0  ,  printMode  ,  skip_prolog  )  ; 
if  (  runstat  ==  true  )  refreshEnvironment  (  )  ; 
}  else   if  (  command  .  equals  (  "stepbr"  )  ||  command  .  equals  (  "sbr"  )  )  { 
printMode  =  PRINTASSEMBLY  ; 
runstat  =  user  .  pstepOverBranch  (  0  )  ; 
if  (  runstat  ==  true  )  refreshEnvironment  (  )  ; 
}  else   if  (  command  .  equals  (  "stepline"  )  ||  command  .  equals  (  "sl"  )  )  { 
printMode  =  PRINTASSEMBLY  ; 
runstat  =  user  .  pstepLine  (  0  ,  printMode  )  ; 
if  (  runstat  ==  true  )  refreshEnvironment  (  )  ; 
}  else   if  (  command  .  equals  (  "steplineover"  )  ||  command  .  equals  (  "slo"  )  )  { 
printMode  =  PRINTSOURCE  ; 
runstat  =  user  .  pstepLineOverMethod  (  0  )  ; 
if  (  runstat  ==  true  )  refreshEnvironment  (  )  ; 
}  else   if  (  command  .  equals  (  "run"  )  )  { 
jdp_console  .  writeOutput  (  "Debuggee is running, kill before restarting"  )  ; 
}  else   if  (  command  .  equals  (  "kill"  )  ||  command  .  equals  (  "k"  )  )  { 
switch  (  debuggerEnvironment  )  { 
case   EXTERNALCREATE  : 
runstat  =  false  ; 
break  ; 
case   EXTERNALATTACH  : 
jdp_console  .  writeOutput  (  "Cannot kill attached process, type quit to detach debugger"  )  ; 
break  ; 
case   INTERNAL  : 
jdp_console  .  writeOutput  (  "Debugger running inside JVM, type quit to exit debugger"  )  ; 
} 
}  else   if  (  command  .  equals  (  "cont"  )  ||  command  .  equals  (  "c"  )  )  { 
if  (  debuggerEnvironment  ==  EXTERNALATTACH  &&  !  user  .  bpset  .  anyBreakpointExist  (  )  )  { 
jdp_console  .  writeOutput  (  "no breakpoint currently set, detaching process"  )  ; 
return   true  ; 
}  else  { 
runstat  =  user  .  pcontinue  (  0  ,  printMode  ,  true  )  ; 
if  (  runstat  ==  true  )  refreshEnvironment  (  )  ; 
} 
}  else   if  (  command  .  equals  (  "cthread"  )  ||  command  .  equals  (  "ct"  )  )  { 
runstat  =  user  .  pcontinue  (  0  ,  printMode  ,  false  )  ; 
if  (  runstat  ==  true  )  refreshEnvironment  (  )  ; 
}  else   if  (  command  .  equals  (  "creturn"  )  ||  command  .  equals  (  "cr"  )  )  { 
runstat  =  user  .  pcontinueToReturn  (  0  ,  printMode  )  ; 
if  (  runstat  ==  true  )  refreshEnvironment  (  )  ; 
}  else   if  (  command  .  equals  (  "thread"  )  ||  command  .  equals  (  "th"  )  )  { 
doThread  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "reg"  )  ||  command  .  equals  (  "r"  )  )  { 
doRegisterRead  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "wreg"  )  ||  command  .  equals  (  "wr"  )  )  { 
doRegisterWrite  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "memraw"  )  ||  command  .  equals  (  "mraw"  )  )  { 
doMemoryReadRaw  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "mem"  )  ||  command  .  equals  (  "m"  )  )  { 
doMemoryRead  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "wmem"  )  ||  command  .  equals  (  "wm"  )  )  { 
doMemoryWrite  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "print"  )  ||  command  .  equals  (  "p"  )  )  { 
doPrintCommand  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "printclass"  )  ||  command  .  equals  (  "pc"  )  )  { 
doPrintClassCommand  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "getclass"  )  )  { 
doGetClassCommand  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "getinstance"  )  )  { 
doGetInstanceCommand  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "getarray"  )  )  { 
doGetArrayCommand  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "getcl"  )  )  { 
doGetClassAndLine  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "getcia"  )  )  { 
doGetCurrentInstrAddr  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "getframes"  )  )  { 
doGetFrames  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "getlocals"  )  )  { 
doGetLocals  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "listb"  )  ||  command  .  equals  (  "lb"  )  )  { 
jdp_console  .  writeOutput  (  "(this command has been removed because the Opt compiler does not generate the bytecode map)"  )  ; 
}  else   if  (  command  .  equals  (  "listi"  )  ||  command  .  equals  (  "li"  )  )  { 
doListInstruction  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "listt"  )  ||  command  .  equals  (  "lt"  )  )  { 
doListThread  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "break"  )  ||  command  .  equals  (  "b"  )  )  { 
doSetBreakpoint  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "clearbreak"  )  ||  command  .  equals  (  "cb"  )  )  { 
doClearBreakpoint  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "stack"  )  ||  command  .  equals  (  "f"  )  )  { 
doCurrentFrame  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "where"  )  ||  command  .  equals  (  "w"  )  )  { 
doShortFrame  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "whereframe"  )  ||  command  .  equals  (  "wf"  )  )  { 
doFullFrame  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "preference"  )  ||  command  .  equals  (  "pref"  )  )  { 
doSetPreference  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "preference"  )  ||  command  .  equals  (  "x2d"  )  )  { 
doConvertHexToInt  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "preference"  )  ||  command  .  equals  (  "d2x"  )  )  { 
doConvertIntToHex  (  command  ,  args  )  ; 
}  else   if  (  command  .  equals  (  "test"  )  )  { 
doTest  (  args  )  ; 
}  else   if  (  command  .  equals  (  "test1"  )  )  { 
doTest1  (  args  )  ; 
}  else   if  (  command  .  equals  (  "count"  )  )  { 
doThreadCount  (  0  )  ; 
}  else   if  (  command  .  equals  (  "zerocount"  )  )  { 
doThreadCount  (  1  )  ; 
}  else   if  (  command  .  equals  (  "readmem"  )  )  { 
if  (  args  .  length  !=  0  )  { 
try  { 
addr  =  parseHex32  (  args  [  0  ]  )  ; 
int   mydata  =  user  .  mem  .  read  (  addr  )  ; 
jdp_console  .  writeOutput  (  "true memory = x"  +  Integer  .  toHexString  (  mydata  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad address: "  +  args  [  0  ]  )  ; 
} 
} 
}  else   if  (  command  .  equals  (  "verbose"  )  ||  command  .  equals  (  "v"  )  )  { 
if  (  user  .  verbose  )  { 
jdp_console  .  writeOutput  (  "Verbose now OFF"  )  ; 
user  .  verbose  =  false  ; 
}  else  { 
jdp_console  .  writeOutput  (  "Verbose now ON"  )  ; 
user  .  verbose  =  true  ; 
} 
}  else   if  (  command  .  equals  (  "help"  )  ||  command  .  equals  (  "h"  )  ||  command  .  equals  (  "?"  )  )  { 
if  (  args  .  length  ==  0  )  printHelp  (  ""  )  ;  else   printHelp  (  args  [  0  ]  )  ; 
}  else   if  (  macro  .  exists  (  command  +  ".jdp"  )  )  { 
macro  .  load  (  command  +  ".jdp"  )  ; 
}  else  { 
jdp_console  .  writeOutput  (  "Command not implemented"  )  ; 
} 
return   false  ; 
} 

public   boolean   checkCleanup  (  )  { 
if  (  !  runstat  &&  (  user  !=  null  )  )  { 
saved_bpset  =  user  .  bpset  ; 
user  .  pkill  (  )  ; 
user  =  null  ; 
} 
return  !  runstat  ; 
} 

public   int   getThreadNumber  (  )  { 
return   user  .  reg  .  getContextThreadID  (  )  ; 
} 





private   void   refreshEnvironment  (  )  { 
user  .  reg  .  setContextThreadIDFromRun  (  )  ; 
user  .  reg  .  cacheJTOC  (  )  ; 
if  (  interpretMode  )  mapVM  .  cachePointers  (  )  ; 
} 



private   void   doTest1  (  String   args  [  ]  )  { 
} 



private   void   doTest  (  String   args  [  ]  )  { 
} 





private   void   doThreadCount  (  int   option  )  { 
switch  (  option  )  { 
case   0  : 
; 
System  .  out  .  println  (  user  .  listThreadsCounts  (  )  )  ; 
break  ; 
case   1  : 
; 
user  .  zeroThreadsCounts  (  )  ; 
break  ; 
default  : 
break  ; 
} 
} 





private   void   setInitialBreakPoint  (  )  { 
int   status  ; 
status  =  user  .  mwait  (  )  ; 
while  (  user  .  isIgnoredTrap  (  status  )  )  { 
user  .  mcontinue  (  0  )  ; 
status  =  user  .  mwait  (  )  ; 
} 
if  (  initial_bp  !=  0  )  { 
breakpoint   bp  =  new   breakpoint  (  0  ,  0  ,  initial_bp  )  ; 
user  .  bpset  .  setBreakpoint  (  bp  )  ; 
user  .  mcontinue  (  0  )  ; 
status  =  user  .  mwait  (  )  ; 
while  (  user  .  isIgnoredTrap  (  status  )  )  { 
user  .  mcontinue  (  0  )  ; 
status  =  user  .  mwait  (  )  ; 
} 
user  .  bpset  .  clearBreakpoint  (  bp  )  ; 
user  .  reg  .  cacheJTOC  (  )  ; 
if  (  interpretMode  )  { 
mapVM  .  cachePointers  (  )  ; 
} 
user  .  bmap  .  fillBootMethodTable  (  )  ; 
if  (  !  viewBoot  )  { 
goToMainMethod  (  )  ; 
} 
} 
} 





private   void   goToMainMethod  (  )  { 
breakpoint   bp  =  null  ; 
try  { 
bp  =  user  .  bmap  .  findBreakpoint  (  "VM.debugBreakpoint"  ,  null  ,  user  .  reg  .  hardwareIP  (  )  )  ; 
}  catch  (  BmapMultipleException   e1  )  { 
jdp_console  .  writeOutput  (  e1  .  getMessage  (  )  )  ; 
}  catch  (  BmapNotFoundException   e2  )  { 
jdp_console  .  writeOutput  (  e2  .  getMessage  (  )  )  ; 
} 
user  .  bpset  .  setBreakpoint  (  bp  )  ; 
user  .  enableIgnoreOtherBreakpointTrap  (  )  ; 
user  .  pcontinue  (  0  ,  PRINTASSEMBLY  ,  true  )  ; 
refreshEnvironment  (  )  ; 
if  (  dejavu  )  { 
System  .  out  .  println  (  "An extra continue for Dejavu ..."  )  ; 
user  .  pcontinue  (  0  ,  PRINTASSEMBLY  ,  true  )  ; 
refreshEnvironment  (  )  ; 
} 
refreshEnvironment  (  )  ; 
breakpoint   main_bp  ; 
if  (  dejavu  )  { 
main_bp  =  setDejaVuMainBreakpoint  (  )  ; 
}  else  { 
main_bp  =  setMainBreakpoint  (  )  ; 
} 
user  .  bpset  .  clearBreakpoint  (  bp  )  ; 
user  .  disableIgnoreOtherBreakpointTrap  (  )  ; 
user  .  pcontinue  (  0  ,  PRINTASSEMBLY  ,  true  )  ; 
refreshEnvironment  (  )  ; 
user  .  bpset  .  clearBreakpoint  (  main_bp  )  ; 
} 






private   breakpoint   setDejaVuMainBreakpoint  (  )  { 
JDP_Class   dejavuClass  =  null  ; 
try  { 
dejavuClass  =  user  .  bmap  .  objectToJDPClass  (  "DejaVu"  ,  0  ,  true  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
JDP_Field   classNameField  =  null  ; 
for  (  int   i  =  0  ;  i  <  dejavuClass  .  fields  .  size  (  )  ;  i  ++  )  { 
classNameField  =  (  JDP_Field  )  dejavuClass  .  fields  .  elementAt  (  i  )  ; 
if  (  classNameField  .  name  .  equals  (  "mainClassName"  )  )  break  ; 
} 
return   setBreakpointAtStringClass  (  classNameField  )  ; 
} 





private   breakpoint   setMainBreakpoint  (  )  { 
JDP_Class   mainThread  =  null  ; 
mainThread  =  user  .  bmap  .  currentThreadToJDPClass  (  )  ; 
JDP_Field   field  =  null  ; 
for  (  int   i  =  0  ;  i  <  mainThread  .  fields  .  size  (  )  ;  i  ++  )  { 
field  =  (  JDP_Field  )  mainThread  .  fields  .  elementAt  (  i  )  ; 
if  (  field  .  name  .  equals  (  "args"  )  )  break  ; 
} 
JDP_Class   argsArray  =  null  ; 
try  { 
argsArray  =  user  .  bmap  .  arrayTypeToJDPClass  (  field  .  name  ,  field  .  type  ,  field  .  address  ,  false  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
JDP_Field   classNameField  =  (  JDP_Field  )  argsArray  .  fields  .  elementAt  (  0  )  ; 
return   setBreakpointAtStringClass  (  classNameField  )  ; 
} 






private   breakpoint   setBreakpointAtStringClass  (  JDP_Field   stringField  )  { 
JDP_Class   stringClass  =  new   JDP_Class  (  )  ; 
stringClass  .  name  =  stringField  .  type  ; 
stringClass  .  address  =  stringField  .  address  ; 
try  { 
user  .  bmap  .  classToJDPClass  (  stringClass  .  name  ,  stringClass  .  address  ,  false  ,  stringClass  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
JDP_Field   valueField  =  (  JDP_Field  )  stringClass  .  fields  .  elementAt  (  0  )  ; 
String   charArrayString  =  valueField  .  value  ; 
charArrayString  =  charArrayString  .  substring  (  1  ,  charArrayString  .  indexOf  (  '}'  )  )  ; 
StringTokenizer   st  =  new   StringTokenizer  (  charArrayString  ,  ", "  ,  false  )  ; 
StringBuffer   ret  =  new   StringBuffer  (  )  ; 
while  (  st  .  hasMoreTokens  (  )  )  { 
ret  .  append  (  st  .  nextToken  (  )  )  ; 
} 
String   className  =  ret  .  toString  (  )  ; 
breakpoint   bp  =  null  ; 
try  { 
bp  =  user  .  bmap  .  findBreakpoint  (  className  +  ".main"  ,  null  ,  user  .  reg  .  hardwareIP  (  )  )  ; 
}  catch  (  BmapMultipleException   e1  )  { 
jdp_console  .  writeOutput  (  e1  .  getMessage  (  )  )  ; 
}  catch  (  BmapNotFoundException   e2  )  { 
jdp_console  .  writeOutput  (  e2  .  getMessage  (  )  )  ; 
} 
user  .  bpset  .  setBreakpoint  (  bp  )  ; 
return   bp  ; 
} 







private   void   restart  (  String   args  [  ]  )  { 
user  =  new   OsProcessExternal  (  bi_runner  ,  bi_args  ,  saved_progname  ,  classesNeededFilename  ,  classpath  )  ; 
setInitialBreakPoint  (  )  ; 
if  (  saved_bpset  .  size  (  )  >  0  )  { 
for  (  int   i  =  0  ;  i  <  saved_bpset  .  size  (  )  ;  i  ++  )  { 
breakpoint   bp  =  (  breakpoint  )  saved_bpset  .  elementAt  (  i  )  ; 
if  (  bp  .  next_addr  !=  -  1  )  { 
user  .  bpset  .  setBreakpoint  (  bp  )  ; 
} 
} 
} 
} 






public   boolean   calledFromDebugger  (  )  { 
return   false  ; 
} 


















private   void   compileBootImage  (  String   args  [  ]  )  { 
String   bi_args  [  ]  =  new   String  [  args  .  length  -  1  ]  ; 
String   bi_name  =  args  [  args  .  length  -  1  ]  ; 
Class   pub_cl  ; 
Object   pub_obj  ; 
java  .  lang  .  reflect  .  Method   pub_methods  [  ]  ; 
jdp_console  .  writeOutput  (  "Compiling Boot Image for "  +  bi_name  +  " . . . "  )  ; 
for  (  int   i  =  0  ;  i  <  bi_args  .  length  ;  i  ++  )  { 
bi_args  [  i  ]  =  args  [  i  +  1  ]  ; 
} 
try  { 
pub_cl  =  Class  .  forName  (  args  [  0  ]  )  ; 
pub_obj  =  pub_cl  .  newInstance  (  )  ; 
pub_methods  =  pub_cl  .  getMethods  (  )  ; 
for  (  int   n  =  0  ;  n  <  pub_methods  .  length  ;  n  ++  )  { 
if  (  pub_methods  [  n  ]  .  getName  (  )  .  equals  (  "main"  )  )  { 
Object   invoke_args  [  ]  =  {  bi_args  }  ; 
pub_methods  [  n  ]  .  invoke  (  pub_obj  ,  invoke_args  )  ; 
return  ; 
} 
} 
}  catch  (  ClassNotFoundException   e  )  { 
jdp_console  .  writeOutput  (  "cannot compile, publicizing class loader not found: "  +  args  [  0  ]  )  ; 
System  .  exit  (  1  )  ; 
}  catch  (  InstantiationException   e1  )  { 
jdp_console  .  writeOutput  (  "cannot compile, problem instantiating class"  )  ; 
System  .  exit  (  1  )  ; 
}  catch  (  IllegalAccessException   e2  )  { 
jdp_console  .  writeOutput  (  "cannot compile, illegal access to class"  )  ; 
System  .  exit  (  1  )  ; 
}  catch  (  InvocationTargetException   e3  )  { 
jdp_console  .  writeOutput  (  "cannot compile, Invocation Target Exception:"  )  ; 
jdp_console  .  writeOutput  (  e3  .  getMessage  (  )  )  ; 
System  .  exit  (  1  )  ; 
} 
} 









public   void   doFullFrame  (  String   command  ,  String  [  ]  args  )  { 
int   from  ,  to  ; 
try  { 
switch  (  args  .  length  )  { 
case   0  : 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstackTraceFull  (  0  ,  20  )  )  ; 
break  ; 
case   1  : 
if  (  args  [  0  ]  .  length  (  )  ==  8  )  { 
int   fp  =  parseHex32  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstackTraceFull  (  fp  )  )  ; 
}  else  { 
from  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstackTraceFull  (  from  ,  from  )  )  ; 
} 
break  ; 
case   2  : 
from  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
to  =  Integer  .  parseInt  (  args  [  1  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstackTraceFull  (  from  ,  to  )  )  ; 
break  ; 
default  : 
printHelp  (  command  )  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad stack frame numbers (decimal) or frame pointer value (hex)"  )  ; 
} 
} 








public   void   doShortFrame  (  String   command  ,  String  [  ]  args  )  { 
int   from  ,  to  ; 
try  { 
switch  (  args  .  length  )  { 
case   0  : 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstackTrace  (  0  ,  20  )  )  ; 
break  ; 
case   1  : 
if  (  args  [  0  ]  .  length  (  )  ==  8  )  { 
int   fp  =  parseHex32  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstackTrace  (  fp  )  )  ; 
}  else  { 
from  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstackTrace  (  from  ,  from  )  )  ; 
} 
break  ; 
case   2  : 
from  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
to  =  Integer  .  parseInt  (  args  [  1  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstackTrace  (  from  ,  to  )  )  ; 
break  ; 
default  : 
printHelp  (  command  )  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad stack frame numbers (decimal) or frame pointer value (hex)"  )  ; 
} 
} 









public   void   doCurrentFrame  (  String   command  ,  String  [  ]  args  )  { 
try  { 
int   width  ,  fp  ; 
switch  (  args  .  length  )  { 
case   0  : 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstack  (  0  ,  4  )  )  ; 
break  ; 
case   1  : 
if  (  args  [  0  ]  .  length  (  )  ==  8  )  { 
fp  =  parseHex32  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstack  (  fp  ,  4  )  )  ; 
}  else  { 
width  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstack  (  0  ,  width  )  )  ; 
} 
break  ; 
case   2  : 
fp  =  parseHex32  (  args  [  0  ]  )  ; 
width  =  Integer  .  parseInt  (  args  [  1  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  printJVMstack  (  fp  ,  width  )  )  ; 
break  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
printHelp  (  command  )  ; 
} 
} 








public   void   doClearBreakpoint  (  String   command  ,  String  [  ]  args  )  { 
if  (  args  .  length  ==  0  )  { 
user  .  bpset  .  clearBreakpoint  (  )  ; 
jdp_console  .  writeOutput  (  "breakpoint cleared"  )  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "all"  )  )  { 
user  .  bpset  .  clearAllBreakpoint  (  )  ; 
jdp_console  .  writeOutput  (  "all breakpoints cleared"  )  ; 
}  else  { 
try  { 
int   addr  =  parseHex32  (  args  [  0  ]  )  ; 
breakpoint   bp  =  user  .  bpset  .  lookup  (  addr  )  ; 
if  (  bp  !=  null  )  { 
user  .  bpset  .  clearBreakpoint  (  bp  )  ; 
jdp_console  .  writeOutput  (  "breakpoint cleared"  )  ; 
}  else   jdp_console  .  writeOutput  (  "no breakpoint at "  +  args  [  0  ]  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "Clear breakpoint: please specify hex address"  )  ; 
} 
} 
} 









public   void   doSetBreakpoint  (  String   command  ,  String  [  ]  args  )  { 
breakpoint   bp  =  null  ; 
if  (  args  .  length  !=  0  )  { 
try  { 
int   addr  =  parseHex32  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  "Caution: setting breakpoint at raw address. \n  If the code is moved by GC, the breakpoint will be lost \n  and the trap instruction will be left in the code."  )  ; 
bp  =  new   breakpoint  (  addr  )  ; 
}  catch  (  NumberFormatException   e  )  { 
String   sig  =  null  ; 
if  (  args  .  length  >=  2  )  { 
sig  =  args  [  1  ]  ; 
} 
try  { 
bp  =  user  .  bmap  .  findBreakpoint  (  args  [  0  ]  ,  sig  ,  user  .  reg  .  hardwareIP  (  )  )  ; 
}  catch  (  BmapMultipleException   e1  )  { 
jdp_console  .  writeOutput  (  e1  .  getMessage  (  )  )  ; 
}  catch  (  BmapNotFoundException   e2  )  { 
jdp_console  .  writeOutput  (  e2  .  getMessage  (  )  )  ; 
} 
} 
if  (  bp  !=  null  )  { 
user  .  bpset  .  setBreakpoint  (  bp  )  ; 
jdp_console  .  writeOutput  (  "breakpoint at: "  +  bp  .  toString  (  user  .  bmap  )  )  ; 
} 
}  else  { 
jdp_console  .  writeOutput  (  user  .  bpset  .  list  (  )  )  ; 
} 
} 









public   void   doListInstruction  (  String   command  ,  String  [  ]  args  )  { 
int   count  ; 
int   addr  =  -  1  ; 
try  { 
switch  (  args  .  length  )  { 
case   0  : 
addr  =  user  .  reg  .  currentIP  (  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  listInstruction  (  addr  ,  10  )  )  ; 
break  ; 
case   1  : 
addr  =  parseHex32  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  listInstruction  (  addr  ,  10  )  )  ; 
break  ; 
default  : 
addr  =  parseHex32  (  args  [  0  ]  )  ; 
count  =  Integer  .  parseInt  (  args  [  1  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  listInstruction  (  addr  ,  count  )  )  ; 
break  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad address: "  +  args  [  0  ]  )  ; 
} 
} 








public   void   doMemoryWrite  (  String   command  ,  String  [  ]  args  )  { 
if  (  args  .  length  ==  2  )  { 
try  { 
int   data  =  parseHex32  (  args  [  1  ]  )  ; 
int   addr  =  parseHex32  (  args  [  0  ]  )  ; 
user  .  mem  .  write  (  addr  ,  data  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad value for write: "  +  args  [  0  ]  +  ", "  +  args  [  1  ]  )  ; 
} 
}  else  { 
printHelp  (  command  )  ; 
} 
} 








public   void   doMemoryRead  (  String   command  ,  String  [  ]  args  )  { 
int   addr  ,  count  ; 
switch  (  args  .  length  )  { 
case   0  : 
printHelp  (  command  )  ; 
break  ; 
case   1  : 
try  { 
addr  =  parseHex32  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  print  (  addr  ,  5  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad address: "  +  args  [  0  ]  )  ; 
} 
break  ; 
default  : 
try  { 
addr  =  parseHex32  (  args  [  0  ]  )  ; 
count  =  Integer  .  parseInt  (  args  [  1  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  mem  .  print  (  addr  ,  count  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad address or count: "  +  args  [  0  ]  +  ", "  +  args  [  1  ]  )  ; 
} 
break  ; 
} 
} 








public   void   doMemoryReadRaw  (  String   command  ,  String  [  ]  args  )  { 
StringBuffer   ret  =  new   StringBuffer  (  )  ; 
ret  .  append  (  "Actual memory (breakpoints shown as is):\n"  )  ; 
int   addr  ,  count  ; 
switch  (  args  .  length  )  { 
case   0  : 
jdp_console  .  writeOutput  (  ret  .  toString  (  )  )  ; 
printHelp  (  command  )  ; 
break  ; 
case   1  : 
try  { 
addr  =  parseHex32  (  args  [  0  ]  )  ; 
ret  .  append  (  user  .  mem  .  printRaw  (  addr  ,  5  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
ret  .  append  (  "bad address: "  +  args  [  0  ]  +  "\n"  )  ; 
} 
jdp_console  .  writeOutput  (  ret  .  toString  (  )  )  ; 
break  ; 
default  : 
try  { 
addr  =  parseHex32  (  args  [  0  ]  )  ; 
count  =  Integer  .  parseInt  (  args  [  1  ]  )  ; 
ret  .  append  (  user  .  mem  .  printRaw  (  addr  ,  count  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
ret  .  append  (  "bad address or count: "  +  args  [  0  ]  +  ", "  +  args  [  1  ]  +  "\n"  )  ; 
} 
jdp_console  .  writeOutput  (  ret  .  toString  (  )  )  ; 
break  ; 
} 
} 








public   void   doRegisterWrite  (  String   command  ,  String  [  ]  args  )  { 
if  (  args  .  length  ==  2  )  { 
try  { 
int   regnum  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
int   data  =  parseHex32  (  args  [  1  ]  )  ; 
user  .  reg  .  write  (  regnum  ,  data  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad value for write: "  +  args  [  0  ]  +  ", "  +  args  [  1  ]  )  ; 
} 
}  else  { 
printHelp  (  command  )  ; 
} 
} 









public   void   doRegisterRead  (  String   command  ,  String  [  ]  args  )  { 
try  { 
switch  (  args  .  length  )  { 
case   0  : 
jdp_console  .  writeOutput  (  user  .  reg  .  getValue  (  "0"  ,  0  )  )  ; 
break  ; 
case   1  : 
jdp_console  .  writeOutput  (  user  .  reg  .  getValue  (  args  [  0  ]  ,  1  )  )  ; 
break  ; 
case   2  : 
try  { 
int   count  =  Integer  .  parseInt  (  args  [  1  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  reg  .  getValue  (  args  [  0  ]  ,  count  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad count: "  +  args  [  1  ]  )  ; 
} 
break  ; 
default  : 
printHelp  (  command  )  ; 
} 
}  catch  (  Exception   e  )  { 
jdp_console  .  writeOutput  (  e  .  getMessage  (  )  )  ; 
} 
} 








public   void   doPrintClassCommand  (  String   command  ,  String  [  ]  args  )  { 
if  (  args  .  length  ==  0  )  return  ; 
try  { 
int   addr  =  parseHex32  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  user  .  bmap  .  addressToClassString  (  addr  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
try  { 
jdp_console  .  writeOutput  (  args  [  0  ]  +  " = "  +  user  .  bmap  .  staticToString  (  args  [  0  ]  )  )  ; 
}  catch  (  BmapNotFoundException   e1  )  { 
jdp_console  .  writeOutput  (  e1  .  getMessage  (  )  )  ; 
} 
}  catch  (  memoryException   e1  )  { 
jdp_console  .  writeOutput  (  args  [  0  ]  +  " is not a valid object address"  )  ; 
} 
} 







public   void   doGetClassCommand  (  String   command  ,  String  [  ]  args  )  { 
try  { 
JDP_Class   jdpClass  =  user  .  bmap  .  objectToJDPClass  (  args  [  0  ]  ,  0  ,  true  )  ; 
jdpClass  .  fields  .  trimToSize  (  )  ; 
jdp_console  .  writeOutput  (  jdpClass  )  ; 
}  catch  (  BmapNotFoundException   e1  )  { 
jdp_console  .  writeOutput  (  e1  .  getMessage  (  )  )  ; 
}  catch  (  memoryException   e2  )  { 
jdp_console  .  writeOutput  (  args  [  0  ]  +  " is not a valid object address"  )  ; 
}  catch  (  NoSuchClassException   e3  )  { 
jdp_console  .  writeOutput  (  args  [  0  ]  +  " is an invalid class name"  )  ; 
} 
} 







public   void   doGetInstanceCommand  (  String   command  ,  String  [  ]  args  )  { 
if  (  args  [  0  ]  .  startsWith  (  "("  )  )  { 
int   rparen  =  args  [  0  ]  .  indexOf  (  ')'  )  ; 
if  (  rparen  ==  -  1  )  { 
jdp_console  .  writeOutput  (  "missing parenthesis for class name: "  +  args  [  0  ]  )  ; 
return  ; 
} 
try  { 
JDP_Class   jdpClass  =  new   JDP_Class  (  )  ; 
jdpClass  .  address  =  parseHex32  (  args  [  1  ]  )  ; 
jdpClass  .  name  =  args  [  0  ]  .  substring  (  1  ,  rparen  )  ; 
jdpClass  .  instance  =  true  ; 
try  { 
user  .  bmap  .  classToJDPClass  (  jdpClass  .  name  ,  jdpClass  .  address  ,  false  ,  jdpClass  )  ; 
jdpClass  .  fields  .  trimToSize  (  )  ; 
jdp_console  .  writeOutput  (  jdpClass  )  ; 
}  catch  (  memoryException   e  )  { 
jdp_console  .  writeOutput  (  "("  +  e  .  getMessage  (  )  +  ")"  )  ; 
}  catch  (  NoSuchClassException   e2  )  { 
jdp_console  .  writeOutput  (  jdpClass  .  name  +  " is an invalid class name"  )  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad address for casting: "  +  args  [  1  ]  )  ; 
} 
}  else  { 
jdp_console  .  writeOutput  (  "invalid args for getinstance"  )  ; 
} 
return  ; 
} 







public   void   doGetArrayCommand  (  String   command  ,  String  [  ]  args  )  { 
try  { 
JDP_Class   jdpClass  =  user  .  bmap  .  arrayTypeToJDPClass  (  args  [  0  ]  ,  args  [  1  ]  ,  parseHex32  (  args  [  2  ]  )  ,  false  )  ; 
jdpClass  .  fields  .  trimToSize  (  )  ; 
jdp_console  .  writeOutput  (  jdpClass  )  ; 
}  catch  (  memoryException   e  )  { 
jdp_console  .  writeOutput  (  "("  +  e  .  getMessage  (  )  +  ")"  )  ; 
}  catch  (  NoSuchClassException   e2  )  { 
jdp_console  .  writeOutput  (  args  [  0  ]  +  " is an invalid class name"  )  ; 
}  catch  (  NumberFormatException   e3  )  { 
jdp_console  .  writeOutput  (  "bad address for casting: "  +  args  [  1  ]  )  ; 
e3  .  printStackTrace  (  )  ; 
}  catch  (  BmapNotFoundException   e4  )  { 
jdp_console  .  writeOutput  (  args  [  0  ]  +  " is an invalid class name"  )  ; 
} 
return  ; 
} 






public   void   doGetClassAndLine  (  String   command  ,  String  [  ]  args  )  { 
jdp_console  .  writeOutput  (  user  .  bmap  .  getCurrentClassAndLineNumber  (  )  )  ; 
} 






public   void   doGetCurrentInstrAddr  (  String   command  ,  String  [  ]  args  )  { 
jdp_console  .  writeOutput  (  String  .  valueOf  (  user  .  reg  .  currentIP  (  )  )  )  ; 
} 







public   void   doGetFrames  (  String   command  ,  String  [  ]  args  )  { 
jdp_console  .  writeOutput  (  user  .  bmap  .  getFrames  (  )  )  ; 
} 






public   void   doGetLocals  (  String   command  ,  String  [  ]  args  )  { 
try  { 
jdp_console  .  writeOutput  (  user  .  bmap  .  localsToJDPLocals  (  Integer  .  parseInt  (  args  [  0  ]  )  ,  Integer  .  parseInt  (  args  [  1  ]  )  ,  Integer  .  parseInt  (  args  [  2  ]  )  ,  Integer  .  parseInt  (  args  [  3  ]  )  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bag argument format"  )  ; 
} 
} 








public   void   doPrintCommand  (  String   command  ,  String  [  ]  args  )  { 
int   addr  ,  frame  ; 
String   varname  ; 
if  (  args  .  length  ==  0  )  { 
jdp_console  .  writeOutput  (  user  .  bmap  .  localVariableToString  (  0  ,  null  )  )  ; 
return  ; 
} 
if  (  args  [  0  ]  .  startsWith  (  "("  )  )  { 
int   rparen  =  args  [  0  ]  .  indexOf  (  ')'  )  ; 
if  (  rparen  ==  -  1  )  { 
jdp_console  .  writeOutput  (  "missing parenthesis for class name: "  +  args  [  0  ]  )  ; 
return  ; 
} 
try  { 
addr  =  parseHex32  (  args  [  1  ]  )  ; 
String   classname  =  args  [  0  ]  .  substring  (  1  ,  rparen  )  ; 
try  { 
jdp_console  .  writeOutput  (  classname  +  " = "  +  user  .  bmap  .  classToString  (  classname  ,  addr  ,  false  )  )  ; 
}  catch  (  memoryException   e  )  { 
jdp_console  .  writeOutput  (  "("  +  e  .  getMessage  (  )  +  ")"  )  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "bad address for casting: "  +  args  [  1  ]  )  ; 
} 
return  ; 
} 
frame  =  CommandLine  .  localParseFrame  (  args  [  0  ]  )  ; 
varname  =  CommandLine  .  localParseName  (  args  [  0  ]  )  ; 
if  (  frame  !=  -  1  )  { 
if  (  varname  ==  null  )  { 
jdp_console  .  writeOutput  (  user  .  bmap  .  localVariableToString  (  frame  ,  null  )  )  ; 
}  else  { 
if  (  varname  .  equals  (  "this"  )  ||  varname  .  startsWith  (  "this."  )  )  jdp_console  .  writeOutput  (  args  [  0  ]  +  " = "  +  user  .  bmap  .  currentClassToString  (  frame  ,  varname  )  )  ;  else   jdp_console  .  writeOutput  (  user  .  bmap  .  localVariableToString  (  frame  ,  varname  )  )  ; 
} 
return  ; 
} 
if  (  varname  .  equals  (  "this"  )  ||  varname  .  startsWith  (  "this."  )  )  jdp_console  .  writeOutput  (  args  [  0  ]  +  " = "  +  user  .  bmap  .  currentClassToString  (  0  ,  varname  )  )  ;  else   jdp_console  .  writeOutput  (  user  .  bmap  .  localVariableToString  (  0  ,  varname  )  )  ; 
return  ; 
} 






public   void   doThread  (  String   command  ,  String  [  ]  args  )  { 
int   threadID  ,  threadPointer  ; 
try  { 
switch  (  args  .  length  )  { 
case   0  : 
threadID  =  user  .  reg  .  registerToTPIndex  (  user  .  reg  .  hardwareTP  (  )  )  ; 
jdp_console  .  writeOutput  (  "context of executing thread: "  +  threadID  )  ; 
user  .  reg  .  setContextThreadID  (  threadID  )  ; 
break  ; 
case   1  : 
if  (  args  [  0  ]  .  equalsIgnoreCase  (  "off"  )  )  { 
user  .  reg  .  setContextThreadID  (  0  )  ; 
}  else  { 
threadID  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
user  .  reg  .  setContextThreadID  (  threadID  )  ; 
} 
break  ; 
default  : 
printHelp  (  command  )  ; 
} 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "invalid thread ID"  )  ; 
}  catch  (  Exception   e1  )  { 
jdp_console  .  writeOutput  (  e1  .  getMessage  (  )  )  ; 
} 
} 






public   void   doListThread  (  String   command  ,  String  [  ]  args  )  { 
if  (  args  .  length  ==  0  )  { 
jdp_console  .  writeOutput  (  user  .  listAllThreads  (  true  )  )  ; 
}  else   if  (  args  .  length  ==  1  )  { 
if  (  args  [  0  ]  .  equals  (  "all"  )  )  { 
jdp_console  .  writeOutput  (  user  .  listAllThreads  (  false  )  )  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "byname"  )  )  { 
jdp_console  .  writeOutput  (  user  .  listAllThreads  (  true  )  )  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "ready"  )  )  { 
jdp_console  .  writeOutput  (  user  .  listReadyThreads  (  )  )  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "wakeup"  )  )  { 
jdp_console  .  writeOutput  (  user  .  listWakeupThreads  (  )  )  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "run"  )  )  { 
jdp_console  .  writeOutput  (  user  .  listRunThreads  (  )  )  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "system"  )  )  { 
jdp_console  .  writeOutput  (  user  .  listSystemThreads  (  )  )  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "gc"  )  )  { 
jdp_console  .  writeOutput  (  user  .  listGCThreads  (  )  )  ; 
}  else  { 
printHelp  (  command  )  ; 
} 
}  else  { 
printHelp  (  command  )  ; 
} 
} 






public   void   doSetPreference  (  String   command  ,  String  [  ]  args  )  { 
StringBuffer   ret  =  new   StringBuffer  (  )  ; 
if  (  args  .  length  ==  0  )  { 
ret  .  append  (  "Current preferences: \n"  )  ; 
ret  .  append  (  "  integer = "  +  integerPreference  +  "\n"  )  ; 
ret  .  append  (  "  stack = "  +  stackPreference  +  "\n"  )  ; 
ret  .  append  (  "  fpr = "  +  fprPreference  +  "\n"  )  ; 
jdp_console  .  writeOutput  (  ret  .  toString  (  )  )  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "int"  )  )  { 
if  (  args  [  1  ]  .  equals  (  "hexadecimal"  )  ||  args  [  1  ]  .  equals  (  "hex"  )  ||  args  [  1  ]  .  equals  (  "x"  )  )  integerPreference  =  'x'  ;  else   if  (  args  [  1  ]  .  equals  (  "decimal"  )  ||  args  [  1  ]  .  equals  (  "dec"  )  ||  args  [  1  ]  .  equals  (  "d"  )  )  integerPreference  =  'd'  ;  else   printHelp  (  command  )  ; 
return  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "stack"  )  )  { 
if  (  args  [  1  ]  .  equals  (  "hexadecimal"  )  ||  args  [  1  ]  .  equals  (  "hex"  )  ||  args  [  1  ]  .  equals  (  "x"  )  )  stackPreference  =  'x'  ;  else   if  (  args  [  1  ]  .  equals  (  "decimal"  )  ||  args  [  1  ]  .  equals  (  "dec"  )  ||  args  [  1  ]  .  equals  (  "d"  )  )  stackPreference  =  'd'  ;  else   printHelp  (  command  )  ; 
return  ; 
}  else   if  (  args  [  0  ]  .  equals  (  "fpr"  )  )  { 
if  (  args  [  1  ]  .  equals  (  "hexadecimal"  )  ||  args  [  1  ]  .  equals  (  "hex"  )  ||  args  [  1  ]  .  equals  (  "x"  )  )  fprPreference  =  'x'  ;  else   if  (  args  [  1  ]  .  equals  (  "float"  )  ||  args  [  1  ]  .  equals  (  "f"  )  )  fprPreference  =  'f'  ;  else   printHelp  (  command  )  ; 
return  ; 
}  else  { 
printHelp  (  command  )  ; 
} 
} 

private   void   doConvertHexToInt  (  String   command  ,  String  [  ]  args  )  { 
if  (  args  .  length  !=  0  )  { 
try  { 
int   num  =  parseHex32  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  args  [  0  ]  +  " = "  +  num  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "cannot convert, bad number: "  +  args  [  0  ]  )  ; 
} 
}  else  { 
System  .  out  .  println  (  "Convert hex to decimal, usage:  x2d hexnum"  )  ; 
} 
} 

private   void   doConvertIntToHex  (  String   command  ,  String  [  ]  args  )  { 
if  (  args  .  length  !=  0  )  { 
try  { 
int   num  =  Integer  .  parseInt  (  args  [  0  ]  )  ; 
jdp_console  .  writeOutput  (  args  [  0  ]  +  " = "  +  Integer  .  toHexString  (  num  )  )  ; 
}  catch  (  NumberFormatException   e  )  { 
jdp_console  .  writeOutput  (  "cannot convert, bad number: "  +  args  [  0  ]  )  ; 
} 
}  else  { 
System  .  out  .  println  (  "Convert number to hex, usage:  d2x decimalnumber"  )  ; 
} 
} 








private   int   parseHex32  (  String   hexString  )  throws   NumberFormatException  { 
int   firstInt  =  Integer  .  parseInt  (  hexString  .  substring  (  0  ,  1  )  ,  16  )  ; 
if  (  hexString  .  length  (  )  <  8  ||  firstInt  <=  7  )  return   Integer  .  parseInt  (  hexString  ,  16  )  ;  else   if  (  hexString  .  length  (  )  ==  8  )  { 
int   lower  =  Integer  .  parseInt  (  hexString  .  substring  (  1  ,  hexString  .  length  (  )  )  ,  16  )  ; 
return   lower  +  (  firstInt  <<  28  )  ; 
}  else   throw   new   NumberFormatException  (  )  ; 
} 









private   void   printHelp  (  String   command  )  { 
StringBuffer   ret  =  new   StringBuffer  (  )  ; 
if  (  command  .  equals  (  "step"  )  ||  command  .  equals  (  "s"  )  )  { 
ret  .  append  (  "Format:  < s | step > \n"  )  ; 
ret  .  append  (  "Single step only the current thread by one machine instruction, \nstepping into all method invocations\n"  )  ; 
}  else   if  (  command  .  equals  (  "stepbr"  )  ||  command  .  equals  (  "sbr"  )  )  { 
ret  .  append  (  "Format:  < sbr | stepbr >\n"  )  ; 
ret  .  append  (  "Single step only the current thread by one machine instruction, \nstepping over method invocations\n"  )  ; 
}  else   if  (  command  .  equals  (  "stepline"  )  ||  command  .  equals  (  "sl"  )  )  { 
ret  .  append  (  "Format:  < sl | stepline > \n"  )  ; 
ret  .  append  (  "Single step only the current thread by one java source line, stepping into method invocations\n"  )  ; 
ret  .  append  (  "(may need to hit enter twice to step one line because currently jdp may not be able to set precise breakpoints)\n"  )  ; 
}  else   if  (  command  .  equals  (  "steplineover"  )  ||  command  .  equals  (  "slo"  )  )  { 
ret  .  append  (  "Format:  < slo | steplineover >\n"  )  ; 
ret  .  append  (  "Single step only the current thread by one java source line, stepping over method invocations\n"  )  ; 
}  else   if  (  command  .  equals  (  "run"  )  )  { 
ret  .  append  (  "Format:  < run > <name ... >\n"  )  ; 
ret  .  append  (  "Start a new program\n"  )  ; 
ret  .  append  (  "If no program name is specified, rerun the last program\n"  )  ; 
ret  .  append  (  "All current breakpoints will be set\n"  )  ; 
ret  .  append  (  "The current program must be killed before restarting\n"  )  ; 
}  else   if  (  command  .  equals  (  "kill"  )  ||  command  .  equals  (  "k"  )  )  { 
ret  .  append  (  "Format:  < k | kill >\n"  )  ; 
ret  .  append  (  "Terminate the current program without exiting the debugger\n"  )  ; 
}  else   if  (  command  .  equals  (  "cthread"  )  ||  command  .  equals  (  "ct"  )  )  { 
ret  .  append  (  "Format:  < ct | cthread > \n"  )  ; 
ret  .  append  (  "Continue only current thread, passing to the program any pending signal\n"  )  ; 
}  else   if  (  command  .  equals  (  "cont"  )  ||  command  .  equals  (  "c"  )  )  { 
ret  .  append  (  "Format:  < c | cont > \n"  )  ; 
ret  .  append  (  "Continue all threads, passing to the program any pending signal\n"  )  ; 
}  else   if  (  command  .  equals  (  "creturn"  )  ||  command  .  equals  (  "cr"  )  )  { 
ret  .  append  (  "Format:  < cr | creturn >\n"  )  ; 
ret  .  append  (  "continue only current thread to the end of this method \n"  )  ; 
ret  .  append  (  "(i.e. go up one stack frame)\n"  )  ; 
}  else   if  (  command  .  equals  (  "where"  )  ||  command  .  equals  (  "w"  )  )  { 
ret  .  append  (  "Format:  < w | where > < from | to > <hexval>\n"  )  ; 
ret  .  append  (  "Display stack trace\n"  )  ; 
ret  .  append  (  "Up to 20 frames are displayed and the number of remaining frames are indicated\n"  )  ; 
ret  .  append  (  "Any frame, range of frames, or a specific frame pointer can be specified\n"  )  ; 
ret  .  append  (  "If we are in the prolog code, the stack frame is being constructed\n"  )  ; 
ret  .  append  (  "so a place holder will be shown for the frame\n"  )  ; 
}  else   if  (  command  .  equals  (  "whereframe"  )  ||  command  .  equals  (  "wf"  )  )  { 
ret  .  append  (  "Format:  < wf | whereframe > < from | to > <hexval>\n"  )  ; 
ret  .  append  (  "Display stack trace with arguments, local variables, temporary variables for each stack frame\n"  )  ; 
ret  .  append  (  "Up to 20 frames are displayed and the number of remaining frames are indicated\n"  )  ; 
ret  .  append  (  "Any frame, range of frames, or a specific frame pointer can be specified\n"  )  ; 
}  else   if  (  command  .  equals  (  "reg"  )  ||  command  .  equals  (  "r"  )  ||  command  .  equals  (  "wreg"  )  ||  command  .  equals  (  "wr"  )  )  { 
ret  .  append  (  "Format 1:  < r | reg > <num|name> <count>\n"  )  ; 
ret  .  append  (  "Format 2:  < wr | wreg > <num|name> <hexval>\n"  )  ; 
ret  .  append  (  "Display/update hardware registers (not thread context registers)\n"  )  ; 
ret  .  append  (  "Specify register by number:  0-31, 128-136, 138, 148, 256-287\n"  )  ; 
ret  .  append  (  "or by names: \n"  )  ; 
String   regname  =  ""  ; 
for  (  int   i  =  0  ;  i  <  VM_BaselineConstants  .  GPR_NAMES  .  length  ;  i  ++  )  regname  +=  VM_BaselineConstants  .  GPR_NAMES  [  i  ]  +  " "  ; 
ret  .  append  (  regname  )  ; 
regname  =  ""  ; 
for  (  int   i  =  0  ;  i  <  VM_BaselineConstants  .  FPR_NAMES  .  length  ;  i  ++  )  regname  +=  VM_BaselineConstants  .  FPR_NAMES  [  i  ]  +  " "  ; 
ret  .  append  (  regname  )  ; 
ret  .  append  (  "IP LR\n"  )  ; 
}  else   if  (  command  .  equals  (  "mem"  )  ||  command  .  equals  (  "m"  )  ||  command  .  equals  (  "wmem"  )  ||  command  .  equals  (  "wm"  )  ||  command  .  equals  (  "memraw"  )  ||  command  .  equals  (  "mraw"  )  )  { 
ret  .  append  (  "Format 1:  < m | mem > <hexaddr> <count>\n"  )  ; 
ret  .  append  (  "Format 2:  < wm | wmem > <hexaddr> <hexvalue>\n"  )  ; 
ret  .  append  (  "Format 3:  < mraw | memraw > <hexaddr> <hexvalue>\n"  )  ; 
ret  .  append  (  "Display/update memory at this address\n"  )  ; 
ret  .  append  (  "If count is not specified, 5 words will be displayed\n"  )  ; 
ret  .  append  (  "For mem and wmem, the breakpoints are transparent\n"  )  ; 
ret  .  append  (  "For memraw, the actual memory contents are shown with the breakpoints as is (intended for debugging jdp)\n"  )  ; 
}  else   if  (  command  .  equals  (  "printclass"  )  ||  command  .  equals  (  "pc"  )  )  { 
ret  .  append  (  "Format 1:   < pc | printclass> <class><.field><[n]>\n"  )  ; 
ret  .  append  (  "Print the static fields for this class\n"  )  ; 
ret  .  append  (  "(including super classes up to but not including java.lang.Object)\n"  )  ; 
ret  .  append  (  "For array, specify an individual element or omit the rightmost dimension \nto display the full dimension\n"  )  ; 
ret  .  append  (  "The variable name can be nested arbitrarily\n"  )  ; 
ret  .  append  (  "Example:\n"  )  ; 
ret  .  append  (  "   pc class                         print the static variables\n"  )  ; 
ret  .  append  (  "   pc class.field                   print this field\n"  )  ; 
ret  .  append  (  "   pc class.array[2]                print this array element\n"  )  ; 
ret  .  append  (  "   pc class.field1.array[4].field2  nested expression\n\n"  )  ; 
ret  .  append  (  "Format 2:   < pc | printclass> <hexaddr>\n"  )  ; 
ret  .  append  (  "   pc 01234567                      print the type for this address\n\n"  )  ; 
}  else   if  (  command  .  equals  (  "print"  )  ||  command  .  equals  (  "p"  )  )  { 
ret  .  append  (  "Format 1:   < p | print> frame<:localvar><.field><[n]>\n"  )  ; 
ret  .  append  (  "Print the content of a local variable in this stack frame;\n"  )  ; 
ret  .  append  (  "If no name is specified, all locals in the current scope are printed\n"  )  ; 
ret  .  append  (  "The name can be the string this to print the current object\n"  )  ; 
ret  .  append  (  "Example:\n"  )  ; 
ret  .  append  (  "   p 0                             print all locals in frame 0\n"  )  ; 
ret  .  append  (  "   p this                          print the current object in frame 0\n"  )  ; 
ret  .  append  (  "   p 1:mylocal.field1              print this local variable in frame 1\n\n"  )  ; 
ret  .  append  (  "Format 2:   < p | print> (classname) hexaddress\n"  )  ; 
ret  .  append  (  "Cast the address as an instance of this class\n"  )  ; 
ret  .  append  (  "and print the contents \n\n"  )  ; 
ret  .  append  (  "Format 3:   < p | print><@class.staticvar>\n"  )  ; 
ret  .  append  (  "Print the address of a static variable for this class\n"  )  ; 
}  else   if  (  command  .  equals  (  "listb"  )  ||  command  .  equals  (  "lb"  )  )  { 
ret  .  append  (  "Format:  < lb | listb > <hexaddr>\n"  )  ; 
ret  .  append  (  "Dissassemble the bytecodes of the method containing this address\n"  )  ; 
ret  .  append  (  "If address is not specified, use the current PC\n"  )  ; 
ret  .  append  (  "(this command has been removed because the Optimizing compiler does not generate the bytecode information)\n"  )  ; 
}  else   if  (  command  .  equals  (  "listi"  )  ||  command  .  equals  (  "li"  )  )  { 
ret  .  append  (  "Format:  < li | listi > <hexaddr><count>\n"  )  ; 
ret  .  append  (  "Dissassemble the machine instruction in this range of addresses\n"  )  ; 
ret  .  append  (  "If address is not specified, use the current PC\n"  )  ; 
ret  .  append  (  "Default count is 10\n"  )  ; 
}  else   if  (  command  .  equals  (  "listt"  )  ||  command  .  equals  (  "lt"  )  )  { 
ret  .  append  (  "Format:  < lt | listt > <all|byname|run|ready|wakeup|system|gc>\n"  )  ; 
ret  .  append  (  "List the threads, select the type of thread by:\n"  )  ; 
ret  .  append  (  "  all      all threads listed by top stack frame\n"  )  ; 
ret  .  append  (  "  byname   all threads listed by thread class name\n"  )  ; 
ret  .  append  (  "  run      threads currently loaded in the system threads\n"  )  ; 
ret  .  append  (  "  ready    threads in the VM_Scheduler ready queue\n"  )  ; 
ret  .  append  (  "  wakeup   threads in the VM_Scheduler wakeup queue\n"  )  ; 
ret  .  append  (  "  system   dump the state of the system threads\n"  )  ; 
ret  .  append  (  "  gc       garbage collector threads\n"  )  ; 
ret  .  append  (  "Annotation: \n"  )  ; 
ret  .  append  (  "  threads loaded in system thread are indicated by >\n"  )  ; 
ret  .  append  (  "  the current thread in which the debugger stops is indicated by ->\n"  )  ; 
}  else   if  (  command  .  equals  (  "thread"  )  ||  command  .  equals  (  "th"  )  )  { 
ret  .  append  (  "Format:  < th | thread > <threadID|off>\n"  )  ; 
ret  .  append  (  "Select a thread context by its ID \n"  )  ; 
ret  .  append  (  "(this is a small integer, get all current thread ID by the listt command)\n"  )  ; 
ret  .  append  (  "The new thread context will be shown in the jdp prompt\n"  )  ; 
ret  .  append  (  "and all future stack and local display will be for this thread\n"  )  ; 
ret  .  append  (  "If no ID is specified, the context is returned to the current thread in which the debugger has stopped\n"  )  ; 
ret  .  append  (  "To force jdp to use the context in the hardware register, specify an ID of 0 or OFF; jdp will stay there until the context is set manually to a valid thread ID\n"  )  ; 
ret  .  append  (  "jdp will start in the OFF thread (i.e. no thread context)\n"  )  ; 
}  else   if  (  command  .  equals  (  "break"  )  ||  command  .  equals  (  "b"  )  )  { 
ret  .  append  (  "Format:  < b ><hexaddr><class.method:line sig>\n"  )  ; 
ret  .  append  (  "Set breakpoint by hex address or symbolic name \n"  )  ; 
ret  .  append  (  "With no argument, the list of current breakpoints is shown\n"  )  ; 
ret  .  append  (  "For symbolic name, jdp will attempt to match partial names\n"  )  ; 
ret  .  append  (  "The method prolog is skipped;  to break at the start of the prolog, \nspecify 0 for the line number\n"  )  ; 
ret  .  append  (  "example:\n"  )  ; 
ret  .  append  (  "   b                        list current breakpoints\n"  )  ; 
ret  .  append  (  "   b 0123abcd               at this hex address\n"  )  ; 
ret  .  append  (  "   b class:line             at java source line in class\n"  )  ; 
ret  .  append  (  "   b method                 at start of method, skipping prolog\n"  )  ; 
ret  .  append  (  "   b class.method           at start of method, skipping prolog\n"  )  ; 
ret  .  append  (  "   b class.method sig       for overloaded method\n"  )  ; 
ret  .  append  (  "   b class.method:0         at start of method prolog\n"  )  ; 
ret  .  append  (  "the class file must be generated with -g to get the line number\n"  )  ; 
}  else   if  (  command  .  equals  (  "clearbreak"  )  ||  command  .  equals  (  "cb"  )  )  { 
ret  .  append  (  "Format:  < cb ><hexaddr|all> \n"  )  ; 
ret  .  append  (  "Clear breakpoint at the hex address or all breakpoint\n"  )  ; 
ret  .  append  (  "(type b to get the list of current breakpoints)\n"  )  ; 
ret  .  append  (  "If no address is specified, clear breakpoint at the current instruction\n"  )  ; 
}  else   if  (  command  .  equals  (  "stack"  )  ||  command  .  equals  (  "f"  )  )  { 
ret  .  append  (  "Format:  < f | stack > <hexval> <n>\n"  )  ; 
ret  .  append  (  "Display current JVM stack \n"  )  ; 
ret  .  append  (  "showing n words at the top and bottom, the default is 4 words\n"  )  ; 
ret  .  append  (  "The value for Frame Pointer may be specified in <hexval>\n"  )  ; 
}  else   if  (  command  .  equals  (  "preference"  )  ||  command  .  equals  (  "pref"  )  )  { 
ret  .  append  (  "Format:  < preference | pref> <string>\n"  )  ; 
ret  .  append  (  "Set user preferences\n"  )  ; 
ret  .  append  (  "To display integer in hex or decimal, specify:  int  < hex | x | dec | d > \n"  )  ; 
ret  .  append  (  "To display stack with/without a decimal column, specify: stack < hex | x | dec | d > \n"  )  ; 
ret  .  append  (  "To display floating point register in hex or float, specify:  fpr  < hex | x | float | f > "  )  ; 
}  else   if  (  command  .  equals  (  "verbose"  )  ||  command  .  equals  (  "v"  )  )  { 
ret  .  append  (  "Format:  < v | verbose >\n"  )  ; 
ret  .  append  (  "Toggle verbose mode on and off\n"  )  ; 
ret  .  append  (  "In verbose, the current stack frame is automatically displayed\n"  )  ; 
}  else   if  (  command  .  equals  (  "macro"  )  )  { 
ret  .  append  (  "Format:  <your macro name>\n"  )  ; 
ret  .  append  (  "jdp will search for the named file with the .jdp suffix in the current class path\n"  )  ; 
ret  .  append  (  "Each line is read and executed it as if it is entered from the command line\n"  )  ; 
ret  .  append  (  "The file should contain normal jdp commands\n"  )  ; 
ret  .  append  (  "On start up, jdp will look for the file startup.jdp in the current directory\n"  )  ; 
ret  .  append  (  "If it exists, it will be loaded and executed automatically\n"  )  ; 
}  else   if  (  command  .  equals  (  "q"  )  ||  command  .  equals  (  "quit"  )  )  { 
ret  .  append  (  "Format:  <q | quit>\n"  )  ; 
ret  .  append  (  "Exit debugger\n"  )  ; 
}  else   if  (  command  .  equals  (  "enter"  )  )  { 
ret  .  append  (  "Format:  (enter)\n"  )  ; 
ret  .  append  (  "Repeat last command\n"  )  ; 
}  else  { 
ret  .  append  (  "step          step current thread by instruction, into method\n"  )  ; 
ret  .  append  (  "stepbr        step current thread by instruction, over method\n"  )  ; 
ret  .  append  (  "stepline      step current thread by java source line, into method \n"  )  ; 
ret  .  append  (  "steplineover  step current thread by java source line, over method \n"  )  ; 
ret  .  append  (  "creturn       continue to last caller \n"  )  ; 
ret  .  append  (  "cthread       continue current thread only\n"  )  ; 
ret  .  append  (  "cont          continue all threads\n"  )  ; 
ret  .  append  (  "kill          terminate program \n"  )  ; 
ret  .  append  (  "run           start new program \n"  )  ; 
ret  .  append  (  "break         list/set breakpoint \n"  )  ; 
ret  .  append  (  "clearbreak    clear breakpoints \n\n"  )  ; 
ret  .  append  (  "thread        set or turn off thread context\n"  )  ; 
ret  .  append  (  "where         print short stack trace \n"  )  ; 
ret  .  append  (  "whereframe    print full stack trace \n"  )  ; 
ret  .  append  (  "stack         display formatted stack \n"  )  ; 
ret  .  append  (  "mem           display memory\n"  )  ; 
ret  .  append  (  "memraw        display actual memory, jdp breakpoints visible\n"  )  ; 
ret  .  append  (  "wmem          write memory \n"  )  ; 
ret  .  append  (  "reg           display registers \n"  )  ; 
ret  .  append  (  "wreg          write register \n"  )  ; 
ret  .  append  (  "printclass    print the class statics or the type of an object address\n"  )  ; 
ret  .  append  (  "print         print local variables or cast an address as an object\n"  )  ; 
ret  .  append  (  "listi         list machine instruction\n"  )  ; 
ret  .  append  (  "listt         list threads\n\n"  )  ; 
ret  .  append  (  "quit          exit debugger\n"  )  ; 
ret  .  append  (  "preference    set user preference\n"  )  ; 
ret  .  append  (  "verbose       toggle verbose mode\n"  )  ; 
ret  .  append  (  "(macro name)  load and execute this macro (a text file with suffix .jdp)\n"  )  ; 
ret  .  append  (  "x2d, d2x      convert number between hex and decimal\n"  )  ; 
ret  .  append  (  "(enter)       repeat last command\n\n"  )  ; 
ret  .  append  (  "To get more information on a specific command, type: \n \thelp thiscommand\n"  )  ; 
} 
jdp_console  .  writeOutput  (  ret  .  toString  (  )  )  ; 
} 
} 

