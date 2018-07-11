package   net  .  sf  .  openforge  .  forge  .  library  .  pipe  ; 

import   java  .  util  .  *  ; 
import   java  .  io  .  *  ; 
import   net  .  sf  .  openforge  .  forge  .  api  .  entry  .  *  ; 
import   net  .  sf  .  openforge  .  forge  .  api  .  ipcore  .  *  ; 
import   net  .  sf  .  openforge  .  forge  .  api  .  pin  .  *  ; 
import   net  .  sf  .  openforge  .  forge  .  api  .  runtime  .  *  ; 
import   net  .  sf  .  openforge  .  forge  .  api  .  timing  .  *  ; 
































public   class   MultiClockPipe   implements   HDLWriter  { 

private   static   int   instanceCount  =  1  ; 

private   static   final   String   IPCORENAME  =  "MultiClockPipe_ForgeCore"  ; 

private   String   moduleName  ; 






private   boolean   gbuf  =  false  ; 

private   int   bufferDepth  =  0  ; 

private   IPCore   asyncFifo  ; 

private   PinOut   writeEnable  ; 

private   PinOut   writeData  ; 

private   PinIn   fifoFull  ; 

private   PinIn   writeStatus  ; 

private   PinOut   readEnable  ; 

private   PinIn   readData  ; 

private   PinIn   fifoEmpty  ; 

private   PinIn   readStatus  ; 










public   MultiClockPipe  (  )  { 
this  (  32  ,  ClockDomain  .  GLOBAL  .  getClockPin  (  )  ,  ClockDomain  .  GLOBAL  .  getClockPin  (  )  )  ; 
} 














public   MultiClockPipe  (  int   dataPathSize  ,  EntryMethod   writeSide  ,  EntryMethod   readSide  )  { 
this  (  dataPathSize  ,  writeSide  .  getClockPin  (  )  ,  readSide  .  getClockPin  (  )  )  ; 
} 












public   MultiClockPipe  (  int   dataPathSize  ,  ClockPin   writeClock  ,  ClockPin   readClock  )  { 
switch  (  dataPathSize  )  { 
case   1  : 
case   8  : 
case   16  : 
case   32  : 
case   64  : 
break  ; 
default  : 
throw   new   net  .  sf  .  openforge  .  forge  .  api  .  ForgeApiException  (  "Illegal data path size to MultiClockPipe: "  +  dataPathSize  +  ", expected 1, 8, 16, 32, or 64"  )  ; 
} 
if  (  writeClock  ==  null  )  { 
writeClock  =  ClockDomain  .  GLOBAL  .  getClockPin  (  )  ; 
} 
if  (  readClock  ==  null  )  { 
readClock  =  ClockDomain  .  GLOBAL  .  getClockPin  (  )  ; 
} 
moduleName  =  IPCORENAME  +  instanceCount  ; 
instanceCount  ++  ; 
asyncFifo  =  new   IPCore  (  moduleName  )  ; 
asyncFifo  .  connectClock  (  readClock  ,  "read_clock_in"  )  ; 
asyncFifo  .  connectClock  (  writeClock  ,  "write_clock_in"  )  ; 
asyncFifo  .  connectReset  (  readClock  .  getResetPin  (  )  ,  "read_reset_in"  )  ; 
asyncFifo  .  connectReset  (  writeClock  .  getResetPin  (  )  ,  "write_reset_in"  )  ; 
asyncFifo  .  setWriter  (  this  )  ; 
writeEnable  =  new   PinOut  (  asyncFifo  ,  "write_enable_in"  ,  1  ,  0  )  ; 
writeData  =  new   PinOut  (  asyncFifo  ,  "write_data_in"  ,  dataPathSize  )  ; 
fifoFull  =  new   PinIn  (  asyncFifo  ,  "full_out"  ,  1  )  ; 
writeStatus  =  new   PinIn  (  asyncFifo  ,  "write_status_out"  ,  4  )  ; 
readEnable  =  new   PinOut  (  asyncFifo  ,  "read_enable_in"  ,  1  ,  0  )  ; 
readData  =  new   PinIn  (  asyncFifo  ,  "read_data_out"  ,  dataPathSize  )  ; 
fifoEmpty  =  new   PinIn  (  asyncFifo  ,  "empty_out"  ,  1  )  ; 
readStatus  =  new   PinIn  (  asyncFifo  ,  "read_status_out"  ,  4  )  ; 
this  .  bufferDepth  =  511  ; 
} 











public   void   setGlobalClockBuffer  (  boolean   value  )  { 
this  .  gbuf  =  value  ; 
} 








public   int   getBufferDepth  (  )  { 
return  (  this  .  bufferDepth  )  ; 
} 











public   void   write  (  boolean   b  )  { 
write  (  b  ?  1  :  0  )  ; 
} 












public   void   write  (  long   l  )  { 
while  (  isFull  (  )  )  { 
} 
Timing  .  waitSyncLocal  (  )  ; 
writeEnable  .  setNow  (  1  )  ; 
writeData  .  setNow  (  l  )  ; 
Timing  .  waitClockLocal  (  )  ; 
} 











public   void   writeNonBlock  (  boolean   b  )  { 
writeNonBlock  (  b  ?  1  :  0  )  ; 
} 













public   void   writeNonBlock  (  long   l  )  { 
Timing  .  throughputLocal  (  0  )  ; 
writeEnable  .  setNow  (  1  )  ; 
writeData  .  setNow  (  l  )  ; 
Timing  .  waitClockLocal  (  )  ; 
} 








public   boolean   isFull  (  )  { 
return  (  fifoFull  .  getBoolean  (  )  )  ; 
} 













































public   int   getWriteStatus  (  )  { 
return  (  writeStatus  .  getUnsignedInt  (  )  )  ; 
} 














































public   int   getReadStatus  (  )  { 
return  (  readStatus  .  getUnsignedInt  (  )  )  ; 
} 








public   boolean   isEmpty  (  )  { 
return  (  fifoEmpty  .  getBoolean  (  )  )  ; 
} 












public   boolean   readBoolean  (  )  { 
return  (  (  (  readLong  (  )  &  1  )  ==  1  )  ?  true  :  false  )  ; 
} 












public   byte   readByte  (  )  { 
return  (  (  byte  )  readLong  (  )  )  ; 
} 












public   char   readChar  (  )  { 
return  (  (  char  )  readLong  (  )  )  ; 
} 












public   short   readShort  (  )  { 
return  (  (  short  )  readLong  (  )  )  ; 
} 












public   int   readInt  (  )  { 
return  (  (  int  )  readLong  (  )  )  ; 
} 












public   long   readLong  (  )  { 
while  (  isEmpty  (  )  )  { 
} 
Timing  .  waitSyncLocal  (  )  ; 
readEnable  .  setNow  (  1  )  ; 
Timing  .  waitClockLocal  (  )  ; 
return  (  readData  .  getLong  (  )  )  ; 
} 












public   int   read  (  )  { 
return  (  readInt  (  )  )  ; 
} 













public   boolean   readBooleanNonBlock  (  )  { 
return  (  (  (  readLongNonBlock  (  )  &  1  )  ==  1  )  ?  true  :  false  )  ; 
} 













public   byte   readByteNonBlock  (  )  { 
return  (  (  byte  )  readLongNonBlock  (  )  )  ; 
} 













public   char   readCharNonBlock  (  )  { 
return  (  (  char  )  readLongNonBlock  (  )  )  ; 
} 













public   short   readShortNonBlock  (  )  { 
return  (  (  short  )  readLongNonBlock  (  )  )  ; 
} 













public   int   readIntNonBlock  (  )  { 
return  (  (  int  )  readLongNonBlock  (  )  )  ; 
} 













public   long   readLongNonBlock  (  )  { 
Timing  .  throughputLocal  (  0  )  ; 
readEnable  .  setNow  (  1  )  ; 
Timing  .  waitClockLocal  (  )  ; 
return  (  readData  .  getLong  (  )  )  ; 
} 













public   int   readNonBlock  (  )  { 
return  (  readIntNonBlock  (  )  )  ; 
} 

public   java  .  util  .  List   writeVerilog  (  IPCore   target  ,  java  .  io  .  PrintWriter   pw  )  { 
ArrayList   result  =  new   ArrayList  (  )  ; 
if  (  this  .  gbuf  )  { 
result  .  add  (  "BUFGP"  )  ; 
} 
result  .  add  (  "MUXF5"  )  ; 
result  .  add  (  "MUXCY_L"  )  ; 
switch  (  RunTime  .  getFamily  (  )  )  { 
case   RunTime  .  VIRTEX2  : 
case   RunTime  .  VIRTEX2P  : 
case   RunTime  .  SPARTAN3  : 
result  .  add  (  "RAMB16_S36_S36"  )  ; 
break  ; 
default  : 
result  .  add  (  "RAMB4_S8_S8"  )  ; 
break  ; 
} 
result  .  add  (  "FDSE"  )  ; 
result  .  add  (  "FDRE"  )  ; 
result  .  add  (  "FDR"  )  ; 
int   dataSize  =  writeData  .  getSize  (  )  ; 
pw  .  println  (  "/***********************************************************************\\"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " *  Module      : adapted from: fifoctlr_ic.v in xapp: 131               *"  )  ; 
pw  .  println  (  " *  Last Update : 03/27/03                                               *"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " *  Description : FIFO controller top level.                             *"  )  ; 
pw  .  println  (  " *                Implements a 511 x N [N=1,8,16,32,64] FIFO with        *"  )  ; 
pw  .  println  (  " *                independent read/write clocks                          *"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " *  The following Verilog code implements a 511xN FIFO in a Virtex#      *"  )  ; 
pw  .  println  (  " *  device.  The inputs are a Read Clock and Read Enable, a Write Clock  *"  )  ; 
pw  .  println  (  " *  and Write Enable, Write Data, and a FIFO_reset signal as an initial  *"  )  ; 
pw  .  println  (  " *  reset.  The outputs are Read Data, Full, Empty, and the FIFOstatus   *"  )  ; 
pw  .  println  (  " *  outputs, which indicate roughly how full the FIFO is.                *"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " *  3/27/03 Modifications for Forge:                                     *"  )  ; 
pw  .  println  (  " *          Removed fifo_gsr, it was not needed.  Replaced all           *"  )  ; 
pw  .  println  (  " *          synchronous always blocks with hard instantiations of the    *"  )  ; 
pw  .  println  (  " *          desired flop primitives and attached defparams for INIT      *"  )  ; 
pw  .  println  (  " *          values that were non-zero. Reworked the fifo status output   *"  )  ; 
pw  .  println  (  " *          to support two, one for the read side and one for the write  *"  )  ; 
pw  .  println  (  " *          allowing users of the fifo to detect if enough room or data  *"  )  ; 
pw  .  println  (  " *          is available to perform a burst.                             *"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " *  Designer    : Nick Camilleri                                         *"  )  ; 
pw  .  println  (  " *  Mods        : Jonathan Harris                                        *"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " *  Company     : Xilinx, Inc.                                           *"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " *  Disclaimer  : THESE DESIGNS ARE PROVIDED \"AS IS\" WITH NO WARRANTY    *"  )  ; 
pw  .  println  (  " *                WHATSOEVER AND XILINX SPECIFICALLY DISCLAIMS ANY       *"  )  ; 
pw  .  println  (  " *                IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR     *"  )  ; 
pw  .  println  (  " *                A PARTICULAR PURPOSE, OR AGAINST INFRINGEMENT.         *"  )  ; 
pw  .  println  (  " *                THEY ARE ONLY INTENDED TO BE USED WITHIN XILINX        *"  )  ; 
pw  .  println  (  " *                DEVICES.                                               *"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " *                Copyright (c) 2003-2004 Xilinx, Inc.                        *"  )  ; 
pw  .  println  (  " *                All rights reserved                                    *"  )  ; 
pw  .  println  (  " *                                                                       *"  )  ; 
pw  .  println  (  " \\***********************************************************************/"  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "module "  +  moduleName  +  " (read_clock_in,  write_clock_in, read_reset_in, write_reset_in, read_enable_in, "  )  ; 
pw  .  println  (  "                    write_enable_in, write_data_in, "  )  ; 
pw  .  println  (  "                    read_data_out, full_out, "  )  ; 
pw  .  println  (  "                    empty_out, write_status_out, read_status_out);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   input read_clock_in, write_clock_in;"  )  ; 
pw  .  println  (  "   input read_reset_in, write_reset_in;"  )  ; 
pw  .  println  (  "   input read_enable_in, write_enable_in;"  )  ; 
if  (  dataSize  ==  1  )  { 
pw  .  println  (  "   input write_data_in;"  )  ; 
pw  .  println  (  "   output read_data_out;"  )  ; 
}  else  { 
pw  .  println  (  "   input ["  +  (  dataSize  -  1  )  +  ":0] write_data_in;"  )  ; 
pw  .  println  (  "   output ["  +  (  dataSize  -  1  )  +  ":0] read_data_out;"  )  ; 
} 
pw  .  println  (  "   output       full_out, empty_out;"  )  ; 
pw  .  println  (  "   output [3:0] write_status_out, read_status_out;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire         read_clock, write_clock;"  )  ; 
pw  .  println  (  "   wire         read_enable = read_enable_in;"  )  ; 
pw  .  println  (  "   wire         write_enable = write_enable_in;"  )  ; 
if  (  dataSize  ==  1  )  { 
pw  .  println  (  "   wire write_data = write_data_in;"  )  ; 
pw  .  println  (  "   wire read_data;"  )  ; 
}  else  { 
pw  .  println  (  "   wire ["  +  (  dataSize  -  1  )  +  ":0]   write_data = write_data_in;"  )  ; 
pw  .  println  (  "   wire ["  +  (  dataSize  -  1  )  +  ":0]   read_data;"  )  ; 
} 
pw  .  println  (  "   assign       read_data_out = read_data;"  )  ; 
pw  .  println  (  "   wire         full, empty;"  )  ; 
pw  .  println  (  "   assign       full_out = full;"  )  ; 
pw  .  println  (  "   assign       empty_out = empty;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire [8:0]   read_addr, write_addr, read_addr_plus_one, write_addr_plus_one;"  )  ; 
pw  .  println  (  "   wire [8:0]   write_addrgray, write_nextgray;"  )  ; 
pw  .  println  (  "   wire [8:0]   read_addrgray, read_nextgray, read_lastgray;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire         read_allow, write_allow, empty_allow, full_allow;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire [8:0]   read_fifo_status, write_fifo_status;"  )  ; 
pw  .  println  (  "   assign       read_status_out = read_fifo_status[8:5];"  )  ; 
pw  .  println  (  "   assign       write_status_out = write_fifo_status[8:5];"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire [8:0]   ecomp, fcomp;"  )  ; 
pw  .  println  (  "   wire [8:0]   emuxcyo, fmuxcyo;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire         emptyg, fullg;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire         gnd = 0;"  )  ; 
pw  .  println  (  "   wire         pwr = 1;"  )  ; 
pw  .  println  (  "   "  )  ; 
if  (  this  .  gbuf  )  { 
pw  .  println  (  "   /**********************************************************************\\"  )  ; 
pw  .  println  (  "    *                                                                      *"  )  ; 
pw  .  println  (  "    * Global input clock buffers are instantianted for both the read_clock *"  )  ; 
pw  .  println  (  "    * and the write_clock, to avoid skew problems.                         *"  )  ; 
pw  .  println  (  "    *                                                                      *"  )  ; 
pw  .  println  (  "    \\**********************************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   BUFGP gclkread (.I(read_clock_in), .O(read_clock));"  )  ; 
pw  .  println  (  "   BUFGP gclkwrite (.I(write_clock_in), .O(write_clock));"  )  ; 
}  else  { 
pw  .  println  (  "   assign read_clock = read_clock_in;"  )  ; 
pw  .  println  (  "   assign write_clock = write_clock_in;"  )  ; 
} 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   /**********************************************************************\\"  )  ; 
pw  .  println  (  "    *                                                                      *"  )  ; 
pw  .  println  (  "    * Block RAM instantiation for FIFO.  Module is 512x8, of which one     *"  )  ; 
pw  .  println  (  "    * address location is sacrificed for the overall speed of the design.  *"  )  ; 
pw  .  println  (  "    *                                                                      *"  )  ; 
pw  .  println  (  "    \\**********************************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
writeBlockRams  (  pw  ,  dataSize  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   /************************************************************\\"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  Allow flags determine whether FIFO control logic can      *"  )  ; 
pw  .  println  (  "    *  operate.  If read_enable is driven high, and the FIFO is  *"  )  ; 
pw  .  println  (  "    *  not Empty, then Reads are allowed.  Similarly, if the     *"  )  ; 
pw  .  println  (  "    *  write_enable signal is high, and the FIFO is not Full,    *"  )  ; 
pw  .  println  (  "    *  then Writes are allowed.                                  *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    \\************************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign       read_allow = (read_enable && ! empty);"  )  ; 
pw  .  println  (  "   assign       write_allow = (write_enable && ! full);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign       full_allow = (full || write_enable);"  )  ; 
pw  .  println  (  "   assign       empty_allow = (empty || read_enable);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   /***********************************************************\\"  )  ; 
pw  .  println  (  "    *                                                           *"  )  ; 
pw  .  println  (  "    *  Empty flag is set on fifo_gsr (initial), or when gray    *"  )  ; 
pw  .  println  (  "    *  code counters are equal, or when there is one word in    *"  )  ; 
pw  .  println  (  "    *  the FIFO, and a Read operation is about to be performed. *"  )  ; 
pw  .  println  (  "    *                                                           *"  )  ; 
pw  .  println  (  "    \\***********************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     empty_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of empty_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE empty_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                    .CE(empty_allow),"  )  ; 
pw  .  println  (  "                    .S(read_reset_in),"  )  ; 
pw  .  println  (  "                    .D(emptyg),"  )  ; 
pw  .  println  (  "                    .Q(empty));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   /***********************************************************\\"  )  ; 
pw  .  println  (  "    *                                                           *"  )  ; 
pw  .  println  (  "    *  Full flag is set on fifo_gsr (initial, but it is cleared *"  )  ; 
pw  .  println  (  "    *  on the first valid write_clock edge after fifo_gsr is    *"  )  ; 
pw  .  println  (  "    *  de-asserted), or when Gray-code counters are one away    *"  )  ; 
pw  .  println  (  "    *  from being equal (the Write Gray-code address is equal   *"  )  ; 
pw  .  println  (  "    *  to the Last Read Gray-code address), or when the Next    *"  )  ; 
pw  .  println  (  "    *  Write Gray-code address is equal to the Last Read Gray-  *"  )  ; 
pw  .  println  (  "    *  code address, and a Write operation is about to be       *"  )  ; 
pw  .  println  (  "    *  performed.                                               *"  )  ; 
pw  .  println  (  "    *                                                           *"  )  ; 
pw  .  println  (  "    \\***********************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     full_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of empty_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE full_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                   .CE(full_allow),"  )  ; 
pw  .  println  (  "                   .S(write_reset_in),"  )  ; 
pw  .  println  (  "                   .D(fullg),"  )  ; 
pw  .  println  (  "                   .Q(full));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   /************************************************************\\"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  Generation of Read address pointers.  The primary one is  *"  )  ; 
pw  .  println  (  "    *  binary (read_addr), and the Gray-code derivatives are     *"  )  ; 
pw  .  println  (  "    *  generated via pipelining the binary-to-Gray-code result.  *"  )  ; 
pw  .  println  (  "    *  The initial values are important, so they're in sequence. *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  Grey-code addresses are used so that the registered       *"  )  ; 
pw  .  println  (  "    *  Full and Empty flags are always clean, and never in an    *"  )  ; 
pw  .  println  (  "    *  unknown state due to the asynchonous relationship of the  *"  )  ; 
pw  .  println  (  "    *  Read and Write clocks.  In the worst case scenario, Full  *"  )  ; 
pw  .  println  (  "    *  and Empty would simply stay active one cycle longer, but  *"  )  ; 
pw  .  println  (  "    *  it would not generate an error or give false values.      *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    \\************************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign       read_addr_plus_one = read_addr + 1;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_0_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[0]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_1_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[1]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_2_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[2]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_3_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[3]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_4_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[4]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_5_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[5]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_6_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[6]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_7_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[7]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addr_8_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .CE(read_allow),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr_plus_one[8]),"  )  ; 
pw  .  println  (  "                          .Q(read_addr[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // read_nextgray initializes to 9'b100000000"  )  ; 
pw  .  println  (  "   FDRE read_nextgray_0_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[1] ^ read_addr[0]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_nextgray_1_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[2] ^ read_addr[1]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_nextgray_2_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[3] ^ read_addr[2]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_nextgray_3_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[4] ^ read_addr[3]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_nextgray_4_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[5] ^ read_addr[4]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_nextgray_5_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[6] ^ read_addr[5]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_nextgray_6_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[7] ^ read_addr[6]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_nextgray_7_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[8] ^ read_addr[7]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     read_nextgray_8_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of read_nextgray_8_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE read_nextgray_8_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .S(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addr[8]),"  )  ; 
pw  .  println  (  "                              .Q(read_nextgray[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // read_addrgray initializes to 9'b100000001"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     read_addrgray_0_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of read_addrgray_0_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE read_addrgray_0_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .S(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[0]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addrgray_1_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[1]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addrgray_2_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[2]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addrgray_3_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[3]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addrgray_4_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[4]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addrgray_5_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[5]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addrgray_6_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[6]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_addrgray_7_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[7]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     read_addrgray_8_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of read_addrgray_8_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE read_addrgray_8_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .S(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_nextgray[8]),"  )  ; 
pw  .  println  (  "                              .Q(read_addrgray[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // read_lastgray initializes to 9'b100000011"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     read_lastgray_0_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of read_lastgray_0_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE read_lastgray_0_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .S(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[0]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     read_lastgray_1_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of read_lastgray_1_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE read_lastgray_1_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .S(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[1]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_lastgray_2_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[2]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_lastgray_3_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[3]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_lastgray_4_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[4]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_lastgray_5_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[5]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_lastgray_6_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[6]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_lastgray_7_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .R(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[7]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     read_lastgray_8_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of read_lastgray_8_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE read_lastgray_8_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                              .CE(read_allow),"  )  ; 
pw  .  println  (  "                              .S(read_reset_in),"  )  ; 
pw  .  println  (  "                              .D(read_addrgray[8]),"  )  ; 
pw  .  println  (  "                              .Q(read_lastgray[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   /************************************************************\\"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  Generation of Write address pointers.  Identical copy of  *"  )  ; 
pw  .  println  (  "    *  read pointer generation above, except for names.          *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    \\************************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign       write_addr_plus_one = write_addr + 1;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_0_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[0]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_1_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[1]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_2_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[2]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_3_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[3]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_4_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[4]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_5_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[5]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_6_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[6]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_7_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[7]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addr_8_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .CE(write_allow),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr_plus_one[8]),"  )  ; 
pw  .  println  (  "                           .Q(write_addr[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // write_nextgray initializes to 9'b100000000"  )  ; 
pw  .  println  (  "   FDRE write_nextgray_0_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[1] ^ write_addr[0]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_nextgray_1_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[2] ^ write_addr[1]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_nextgray_2_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[3] ^ write_addr[2]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_nextgray_3_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[4] ^ write_addr[3]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_nextgray_4_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[5] ^ write_addr[4]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_nextgray_5_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[6] ^ write_addr[5]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_nextgray_6_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[7] ^ write_addr[6]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_nextgray_7_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[8] ^ write_addr[7]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     write_nextgray_8_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of write_nextgray_8_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE write_nextgray_8_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .S(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_addr[8]),"  )  ; 
pw  .  println  (  "                               .Q(write_nextgray[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // write_addrgray initializes to 9'b100000001"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     write_addrgray_0_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of write_addrgray_0_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE write_addrgray_0_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .S(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[0]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addrgray_1_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[1]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addrgray_2_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[2]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addrgray_3_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[3]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addrgray_4_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[4]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addrgray_5_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[5]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addrgray_6_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[6]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_addrgray_7_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .R(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[7]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // synopsys translate_off"  )  ; 
pw  .  println  (  "   defparam     write_addrgray_8_inst.INIT = 1'b1;"  )  ; 
pw  .  println  (  "   // synopsys translate_on"  )  ; 
pw  .  println  (  "   // synthesis attribute INIT of write_addrgray_8_inst is \"1\""  )  ; 
pw  .  println  (  "   FDSE write_addrgray_8_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                               .CE(write_allow),"  )  ; 
pw  .  println  (  "                               .S(write_reset_in),"  )  ; 
pw  .  println  (  "                               .D(write_nextgray[8]),"  )  ; 
pw  .  println  (  "                               .Q(write_addrgray[8]));      "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   /************************************************************\\"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  Alternative generation of FIFOstatus outputs.  Used to    *"  )  ; 
pw  .  println  (  "    *  determine how full FIFO is, based on how far the Write    *"  )  ; 
pw  .  println  (  "    *  pointer is ahead of the Read pointer.  read_truegray      *   "  )  ; 
pw  .  println  (  "    *  is synchronized to write_clock (rag_writesync), converted *"  )  ; 
pw  .  println  (  "    *  to binary (ra_writesync), and then subtracted from the    *"  )  ; 
pw  .  println  (  "    *  pipelined write_addr (write_addrr) to find out how many   *"  )  ; 
pw  .  println  (  "    *  words are in the FIFO (fifostatus).  The top bits are     *   "  )  ; 
pw  .  println  (  "    *  then 1/2 full, 1/4 full, etc. (not mutually exclusive).   *"  )  ; 
pw  .  println  (  "    *  fifostatus has a one-cycle latency on write_clock; or,    *"  )  ; 
pw  .  println  (  "    *  one cycle after the write address is incremented on a     *   "  )  ; 
pw  .  println  (  "    *  write operation, fifostatus is updated with the new       *   "  )  ; 
pw  .  println  (  "    *  capacity information.  There is a two-cycle latency on    *"  )  ; 
pw  .  println  (  "    *  read operations.                                          *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  If read_clock is much faster than write_clock, it is      *   "  )  ; 
pw  .  println  (  "    *  possible that the fifostatus counter could drop several   *"  )  ; 
pw  .  println  (  "    *  positions in one write_clock cycle, so the low-order bits *"  )  ; 
pw  .  println  (  "    *  of fifostatus are not as reliable.                        *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  The above description if mirrored for a read clock        *"  )  ; 
pw  .  println  (  "    *  synchronized verfion of the same value.                   *"  )  ; 
pw  .  println  (  "    \\************************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire [8:0]   read_truegray, rag_writesync, write_addrr;"  )  ; 
pw  .  println  (  "   wire [8:0]   ra_writesync;"  )  ; 
pw  .  println  (  "   wire [2:0]   write_xorout;"  )  ; 
pw  .  println  (  "   wire [8:0]   fifostatus_write_int;"  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "   FDR read_truegray_0_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[1] ^ read_addr[0]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_truegray_1_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[2] ^ read_addr[1]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_truegray_2_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[3] ^ read_addr[2]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_truegray_3_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[4] ^ read_addr[3]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_truegray_4_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[5] ^ read_addr[4]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_truegray_5_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[6] ^ read_addr[5]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_truegray_6_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[7] ^ read_addr[6]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_truegray_7_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[8] ^ read_addr[7]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_truegray_8_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                             .R(read_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_addr[8]),"  )  ; 
pw  .  println  (  "                             .Q(read_truegray[8]));   "  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_0_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[0]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_1_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[1]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_2_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[2]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_3_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[3]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_4_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[4]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_5_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[5]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_6_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[6]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_7_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[7]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR rag_writesync_8_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                             .R(write_reset_in),"  )  ; 
pw  .  println  (  "                             .D(read_truegray[8]),"  )  ; 
pw  .  println  (  "                             .Q(rag_writesync[8]));   "  )  ; 
pw  .  println  (  "      "  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifoxor4 write_xor8_5 (.data(rag_writesync[8:5]), .out(write_xorout[0]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifoxor5 write_xor8_4 (.data(rag_writesync[8:4]), .out(write_xorout[1]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifoxor4 write_xor4_1 (.data(rag_writesync[4:1]), .out(write_xorout[2]));"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign       ra_writesync = { rag_writesync[8],"  )  ; 
pw  .  println  (  "                                 (rag_writesync[8] ^ rag_writesync[7]),"  )  ; 
pw  .  println  (  "                                 (rag_writesync[8] ^ rag_writesync[7] ^ rag_writesync[6]),"  )  ; 
pw  .  println  (  "                                 write_xorout[0],"  )  ; 
pw  .  println  (  "                                 write_xorout[1], "  )  ; 
pw  .  println  (  "                                 (write_xorout[1] ^ rag_writesync[3]),"  )  ; 
pw  .  println  (  "                                 (write_xorout[1] ^ rag_writesync[3] ^ rag_writesync[2]),"  )  ; 
pw  .  println  (  "                                 (write_xorout[0] ^ write_xorout[2]),"  )  ; 
pw  .  println  (  "                                 (write_xorout[0] ^ write_xorout[2] ^ rag_writesync[0])"  )  ; 
pw  .  println  (  "                                 };"  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "   FDR write_addrr_0_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[0]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_addrr_1_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[1]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_addrr_2_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[2]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_addrr_3_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[3]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_addrr_4_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[4]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_addrr_5_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[5]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_addrr_6_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[6]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_addrr_7_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[7]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_addrr_8_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                           .R(write_reset_in),"  )  ; 
pw  .  println  (  "                           .D(write_addr[8]),"  )  ; 
pw  .  println  (  "                           .Q(write_addrr[8]));      "  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "   assign       fifostatus_write_int = write_addrr - ra_writesync;"  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_0_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[0]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_1_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[1]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_2_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[2]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_3_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[3]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_4_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[4]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_5_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[5]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_6_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[6]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_7_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[7]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE write_fifo_status_8_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                                  .CE(!full),"  )  ; 
pw  .  println  (  "                                  .R(write_reset_in),"  )  ; 
pw  .  println  (  "                                  .D(fifostatus_write_int[8]),"  )  ; 
pw  .  println  (  "                                  .Q(write_fifo_status[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   // read fifo status logic"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire [8:0]   write_truegray, wag_readsync, read_addrr;"  )  ; 
pw  .  println  (  "   wire [8:0]   wa_readsync;"  )  ; 
pw  .  println  (  "   wire [2:0]   read_xorout;"  )  ; 
pw  .  println  (  "   wire [8:0]   fifostatus_read_int;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_0_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[1] ^ write_addr[0]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_1_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[2] ^ write_addr[1]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_2_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[3] ^ write_addr[2]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_3_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[4] ^ write_addr[3]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_4_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[5] ^ write_addr[4]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_5_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[6] ^ write_addr[5]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_6_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[7] ^ write_addr[6]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_7_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[8] ^ write_addr[7]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR write_truegray_8_inst (.C(write_clock),"  )  ; 
pw  .  println  (  "                              .R(write_reset_in),"  )  ; 
pw  .  println  (  "                              .D(write_addr[8]),"  )  ; 
pw  .  println  (  "                              .Q(write_truegray[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_0_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[0]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_1_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[1]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_2_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[2]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_3_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[3]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_4_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[4]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_5_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[5]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_6_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[6]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_7_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[7]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR wag_readsync_8_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                            .R(read_reset_in),"  )  ; 
pw  .  println  (  "                            .D(write_truegray[8]),"  )  ; 
pw  .  println  (  "                            .Q(wag_readsync[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifoxor4 read_xor8_5 (.data(wag_readsync[8:5]), .out(read_xorout[0]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifoxor5 read_xor8_4 (.data(wag_readsync[8:4]), .out(read_xorout[1]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifoxor4 read_xor4_1 (.data(wag_readsync[4:1]), .out(read_xorout[2]));"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign       wa_readsync = { wag_readsync[8],"  )  ; 
pw  .  println  (  "                                (wag_readsync[8] ^ wag_readsync[7]),"  )  ; 
pw  .  println  (  "                                (wag_readsync[8] ^ wag_readsync[7] ^ wag_readsync[6]),"  )  ; 
pw  .  println  (  "                                read_xorout[0],"  )  ; 
pw  .  println  (  "                                read_xorout[1], "  )  ; 
pw  .  println  (  "                                (read_xorout[1] ^ wag_readsync[3]),"  )  ; 
pw  .  println  (  "                                (read_xorout[1] ^ wag_readsync[3] ^ wag_readsync[2]),"  )  ; 
pw  .  println  (  "                                (read_xorout[0] ^ read_xorout[2]),"  )  ; 
pw  .  println  (  "                                (read_xorout[0] ^ read_xorout[2] ^ wag_readsync[0])"  )  ; 
pw  .  println  (  "                                };"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_0_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[0]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_1_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[1]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_2_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[2]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_3_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[3]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_4_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[4]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_5_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[5]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_6_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[6]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_7_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[7]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDR read_addrr_8_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                          .R(read_reset_in),"  )  ; 
pw  .  println  (  "                          .D(read_addr[8]),"  )  ; 
pw  .  println  (  "                          .Q(read_addrr[8]));      "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign       fifostatus_read_int = (wa_readsync - read_addrr);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_0_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[0]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[0]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_1_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[1]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[1]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_2_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[2]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[2]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_3_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[3]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[3]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_4_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[4]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[4]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_5_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[5]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[5]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_6_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[6]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[6]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_7_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[7]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[7]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   FDRE read_fifo_status_8_inst (.C(read_clock),"  )  ; 
pw  .  println  (  "                                 .CE(!empty),"  )  ; 
pw  .  println  (  "                                 .R(read_reset_in),"  )  ; 
pw  .  println  (  "                                 .D(fifostatus_read_int[8]),"  )  ; 
pw  .  println  (  "                                 .Q(read_fifo_status[8]));   "  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "   /************************************************************\\"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  The two conditions decoded with special carry logic are   *"  )  ; 
pw  .  println  (  "    *  Empty and Full (gated versions).  These are used to       *"  )  ; 
pw  .  println  (  "    *  determine the next state of the Full/Empty flags.  Carry  *"  )  ; 
pw  .  println  (  "    *  logic is used for optimal speed.  (The previous           *"  )  ; 
pw  .  println  (  "    *  implementation of AlmostEmpty and AlmostFull have been    *"  )  ; 
pw  .  println  (  "    *  wrapped into the corresponding carry chains for faster    *"  )  ; 
pw  .  println  (  "    *  performance).                                             *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  When write_addrgray is equal to read_addrgray, the FIFO   *"  )  ; 
pw  .  println  (  "    *  is Empty, and emptyg (combinatorial) is asserted.  Or,    *"  )  ; 
pw  .  println  (  "    *  when write_addrgray is equal to read_nextgray (1 word in  *"  )  ; 
pw  .  println  (  "    *  the FIFO) then the FIFO potentially could be going Empty, *"  )  ; 
pw  .  println  (  "    *  so emptyg is asserted, and the Empty flip-flop enable is  *"  )  ; 
pw  .  println  (  "    *  gated with empty_allow, which is conditioned with a valid *"  )  ; 
pw  .  println  (  "    *  read.                                                     *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  Similarly, when read_lastgray is equal to write_addrgray, *"  )  ; 
pw  .  println  (  "    *  the FIFO is full (511 addresses).  Or, when read_lastgray *"  )  ; 
pw  .  println  (  "    *  is equal to write_nextgray, then the FIFO potentially     *   "  )  ; 
pw  .  println  (  "    *  could be going Full, so fullg is asserted, and the Full   *"  )  ; 
pw  .  println  (  "    *  flip-flop enable is gated with full_allow, which is       *   "  )  ; 
pw  .  println  (  "    *  conditioned with a valid write.                           *"  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    *  Note: To have utilized the full address space (512)       *   "  )  ; 
pw  .  println  (  "    *  would have required extra logic to determine Full/Empty   *"  )  ; 
pw  .  println  (  "    *  on equal addresses, and this would have slowed down the   *"  )  ; 
pw  .  println  (  "    *  overall performance, which was the top priority.          *   "  )  ; 
pw  .  println  (  "    *                                                            *"  )  ; 
pw  .  println  (  "    \\************************************************************/"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor0 (.a(write_addrgray[0]), .b(read_addrgray[0]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[0]), .sel(empty), .out(ecomp[0]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor1 (.a(write_addrgray[1]), .b(read_addrgray[1]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[1]), .sel(empty), .out(ecomp[1]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor2 (.a(write_addrgray[2]), .b(read_addrgray[2]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[2]), .sel(empty), .out(ecomp[2]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor3 (.a(write_addrgray[3]), .b(read_addrgray[3]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[3]), .sel(empty), .out(ecomp[3]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor4 (.a(write_addrgray[4]), .b(read_addrgray[4]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[4]), .sel(empty), .out(ecomp[4]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor5 (.a(write_addrgray[5]), .b(read_addrgray[5]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[5]), .sel(empty), .out(ecomp[5]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor6 (.a(write_addrgray[6]), .b(read_addrgray[6]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[6]), .sel(empty), .out(ecomp[6]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor7 (.a(write_addrgray[7]), .b(read_addrgray[7]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[7]), .sel(empty), .out(ecomp[7]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor emuxor8 (.a(write_addrgray[8]), .b(read_addrgray[8]),"  )  ; 
pw  .  println  (  "                      .c(read_nextgray[8]), .sel(empty), .out(ecomp[8]));"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy0 (.DI(gnd), .CI(pwr),        .S(ecomp[0]), .LO(emuxcyo[0]));"  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy1 (.DI(gnd), .CI(emuxcyo[0]), .S(ecomp[1]), .LO(emuxcyo[1]));"  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy2 (.DI(gnd), .CI(emuxcyo[1]), .S(ecomp[2]), .LO(emuxcyo[2]));"  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy3 (.DI(gnd), .CI(emuxcyo[2]), .S(ecomp[3]), .LO(emuxcyo[3]));"  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy4 (.DI(gnd), .CI(emuxcyo[3]), .S(ecomp[4]), .LO(emuxcyo[4]));"  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy5 (.DI(gnd), .CI(emuxcyo[4]), .S(ecomp[5]), .LO(emuxcyo[5]));"  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy6 (.DI(gnd), .CI(emuxcyo[5]), .S(ecomp[6]), .LO(emuxcyo[6]));"  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy7 (.DI(gnd), .CI(emuxcyo[6]), .S(ecomp[7]), .LO(emuxcyo[7]));"  )  ; 
pw  .  println  (  "   MUXCY_L emuxcy8 (.DI(gnd), .CI(emuxcyo[7]), .S(ecomp[8]), .LO(emptyg));"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor0 (.a(read_lastgray[0]), .b(write_addrgray[0]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[0]), .sel(full), .out(fcomp[0]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor1 (.a(read_lastgray[1]), .b(write_addrgray[1]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[1]), .sel(full), .out(fcomp[1]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor2 (.a(read_lastgray[2]), .b(write_addrgray[2]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[2]), .sel(full), .out(fcomp[2]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor3 (.a(read_lastgray[3]), .b(write_addrgray[3]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[3]), .sel(full), .out(fcomp[3]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor4 (.a(read_lastgray[4]), .b(write_addrgray[4]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[4]), .sel(full), .out(fcomp[4]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor5 (.a(read_lastgray[5]), .b(write_addrgray[5]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[5]), .sel(full), .out(fcomp[5]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor6 (.a(read_lastgray[6]), .b(write_addrgray[6]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[6]), .sel(full), .out(fcomp[6]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor7 (.a(read_lastgray[7]), .b(write_addrgray[7]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[7]), .sel(full), .out(fcomp[7]));"  )  ; 
pw  .  println  (  "   "  +  moduleName  +  "_fifomuxor fmuxor8 (.a(read_lastgray[8]), .b(write_addrgray[8]),"  )  ; 
pw  .  println  (  "                      .c(write_nextgray[8]), .sel(full), .out(fcomp[8]));"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy0 (.DI(gnd), .CI(pwr),        .S(fcomp[0]), .LO(fmuxcyo[0]));"  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy1 (.DI(gnd), .CI(fmuxcyo[0]), .S(fcomp[1]), .LO(fmuxcyo[1]));"  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy2 (.DI(gnd), .CI(fmuxcyo[1]), .S(fcomp[2]), .LO(fmuxcyo[2]));"  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy3 (.DI(gnd), .CI(fmuxcyo[2]), .S(fcomp[3]), .LO(fmuxcyo[3]));"  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy4 (.DI(gnd), .CI(fmuxcyo[3]), .S(fcomp[4]), .LO(fmuxcyo[4]));"  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy5 (.DI(gnd), .CI(fmuxcyo[4]), .S(fcomp[5]), .LO(fmuxcyo[5]));"  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy6 (.DI(gnd), .CI(fmuxcyo[5]), .S(fcomp[6]), .LO(fmuxcyo[6]));"  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy7 (.DI(gnd), .CI(fmuxcyo[6]), .S(fcomp[7]), .LO(fmuxcyo[7]));"  )  ; 
pw  .  println  (  "   MUXCY_L fmuxcy8 (.DI(gnd), .CI(fmuxcyo[7]), .S(fcomp[8]), .LO(fullg));"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "endmodule"  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "/************************************************************\\"  )  ; 
pw  .  println  (  " *                                                            *"  )  ; 
pw  .  println  (  " *  The logic modules below are coded explicitly, to ensure   *"  )  ; 
pw  .  println  (  " *  that the logic is implemented in a minimum of levels.     *"  )  ; 
pw  .  println  (  " *                                                            *"  )  ; 
pw  .  println  (  " \\************************************************************/"  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "module "  +  moduleName  +  "_fifoxor5 (data, out);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   input [4:0] data;"  )  ; 
pw  .  println  (  "   output      out;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   wire        muxout, mdata1, mdata0;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign      mdata1 = ! (data[4] ^ data[3] ^ data[2] ^ data[1]);"  )  ; 
pw  .  println  (  "   assign      mdata0 = (data[4] ^ data[3] ^ data[2] ^ data[1]);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   MUXF5 muxf5_1 (.I0(mdata0), .I1(mdata1), .S(data[0]), .O(muxout));"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign      out = muxout;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "endmodule"  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "module "  +  moduleName  +  "_fifoxor4 (data, out);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   input [3:0] data;"  )  ; 
pw  .  println  (  "   output      out;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign      out = (data[3] ^ data[2] ^ data[1] ^ data[0]);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "endmodule"  )  ; 
pw  .  println  (  ""  )  ; 
pw  .  println  (  "module "  +  moduleName  +  "_fifomuxor (a, b, c, sel, out);"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   input a, b, c, sel;"  )  ; 
pw  .  println  (  "   output out;"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "   assign #1 out = (((a == b) && sel) || ((a == c) && ! sel));"  )  ; 
pw  .  println  (  "   "  )  ; 
pw  .  println  (  "endmodule"  )  ; 
pw  .  println  (  ""  )  ; 
return  (  result  )  ; 
} 

private   void   writeBlockRams  (  PrintWriter   pw  ,  int   dataSize  )  { 
switch  (  RunTime  .  getFamily  (  )  )  { 
case   RunTime  .  VIRTEX2  : 
case   RunTime  .  VIRTEX2P  : 
case   RunTime  .  SPARTAN3  : 
writeVirtex2BlockRam  (  pw  ,  dataSize  )  ; 
break  ; 
default  : 
writeVirtexBlockRam  (  pw  ,  dataSize  )  ; 
break  ; 
} 
} 

private   void   writeVirtexBlockRam  (  PrintWriter   pw  ,  int   dataSize  )  { 
int   rams  =  1  ; 
switch  (  dataSize  )  { 
case   1  : 
case   8  : 
rams  =  1  ; 
pw  .  println  (  " wire [7:0] int_read_data;"  )  ; 
break  ; 
case   16  : 
rams  =  2  ; 
pw  .  println  (  " wire [15:0] int_read_data;"  )  ; 
break  ; 
case   32  : 
rams  =  4  ; 
pw  .  println  (  " wire [31:0] int_read_data;"  )  ; 
break  ; 
case   64  : 
rams  =  8  ; 
pw  .  println  (  " wire [64:0] int_read_data;"  )  ; 
break  ; 
} 
for  (  int   ram  =  0  ;  ram  <  rams  ;  ram  ++  )  { 
pw  .  println  (  "   RAMB4_S8_S8 bram"  +  (  ram  +  1  )  +  " ( .ADDRA(read_addr),"  )  ; 
pw  .  println  (  "                       .ADDRB(write_addr),"  )  ; 
pw  .  println  (  "                       .DIA(gnd_bus),"  )  ; 
if  (  dataSize  ==  1  )  { 
pw  .  println  (  "                       .DIB({{7'h0},{write_data}}),"  )  ; 
}  else  { 
pw  .  println  (  "                       .DIB(write_data["  +  (  (  (  ram  +  1  )  *  8  )  -  1  )  +  ":"  +  (  ram  *  8  )  +  "]),"  )  ; 
} 
pw  .  println  (  "                       .WEA(gnd),"  )  ; 
pw  .  println  (  "                       .WEB(pwr), "  )  ; 
pw  .  println  (  "                       .CLKA(read_clock),"  )  ; 
pw  .  println  (  "                       .CLKB(write_clock),"  )  ; 
pw  .  println  (  "                       .RSTA(gnd),"  )  ; 
pw  .  println  (  "                       .RSTB(gnd),"  )  ; 
pw  .  println  (  "                       .ENA(read_allow),"  )  ; 
pw  .  println  (  "                       .ENB(write_allow),"  )  ; 
pw  .  println  (  "                       .DOA(int_read_data ["  +  (  (  (  ram  +  1  )  *  8  )  -  1  )  +  ":"  +  (  ram  *  8  )  +  "]),"  )  ; 
pw  .  println  (  "                       .DOB());"  )  ; 
} 
if  (  dataSize  ==  1  )  pw  .  println  (  "  assign read_data = int_read_data[0];"  )  ;  else   if  (  dataSize  <=  32  )  pw  .  println  (  "  assign read_data = int_read_data["  +  (  dataSize  -  1  )  +  ":0];"  )  ;  else   pw  .  println  (  "  assign read_data = int_read_data;"  )  ; 
} 

private   void   writeVirtex2BlockRam  (  PrintWriter   pw  ,  int   dataSize  )  { 
pw  .  println  (  " wire ["  +  (  (  dataSize  <=  32  )  ?  32  :  64  )  +  ":0] int_read_data;"  )  ; 
pw  .  println  (  "  RAMB16_S36_S36 bram1 ( .WEA(gnd),"  )  ; 
pw  .  println  (  "                         .ENA(read_allow),"  )  ; 
pw  .  println  (  "                         .SSRA(gnd),"  )  ; 
pw  .  println  (  "                         .CLKA(read_clock),"  )  ; 
pw  .  println  (  "                         .ADDRA(read_addr),"  )  ; 
pw  .  println  (  "                         .DIA(32'h0),"  )  ; 
pw  .  println  (  "                         .DIPA(4'h0),"  )  ; 
pw  .  println  (  "                         .DOPA(),"  )  ; 
pw  .  println  (  "                         .DOA(int_read_data[31:0]),"  )  ; 
pw  .  println  (  "                         .WEB(pwr),"  )  ; 
pw  .  println  (  "                         .ENB(write_allow),"  )  ; 
pw  .  println  (  "                         .SSRB(gnd),"  )  ; 
pw  .  println  (  "                         .CLKB(write_clock),"  )  ; 
pw  .  println  (  "                         .ADDRB(write_addr),"  )  ; 
if  (  dataSize  <  32  )  pw  .  println  (  "                         .DIB({{"  +  (  32  -  dataSize  )  +  "'h0},{write_data}}),"  )  ;  else   pw  .  println  (  "                         .DIB(write_data[31:0]),"  )  ; 
pw  .  println  (  "                         .DIPB(4'h0),"  )  ; 
pw  .  println  (  "                         .DOPB(),"  )  ; 
pw  .  println  (  "                         .DOB());"  )  ; 
if  (  dataSize  >  32  )  { 
pw  .  println  (  "  RAMB16_S36_S36 bram2 ( .WEA(gnd),"  )  ; 
pw  .  println  (  "                         .ENA(read_allow),"  )  ; 
pw  .  println  (  "                         .SSRA(gnd),"  )  ; 
pw  .  println  (  "                         .CLKA(read_clock),"  )  ; 
pw  .  println  (  "                         .ADDRA(read_addr),"  )  ; 
pw  .  println  (  "                         .DIA(32'h0),"  )  ; 
pw  .  println  (  "                         .DIPA(4'h0),"  )  ; 
pw  .  println  (  "                         .DOPA(),"  )  ; 
pw  .  println  (  "                         .DOA(int_read_data[63:32]),"  )  ; 
pw  .  println  (  "                         .WEB(pwr),"  )  ; 
pw  .  println  (  "                         .ENB(write_allow),"  )  ; 
pw  .  println  (  "                         .SSRB(gnd),"  )  ; 
pw  .  println  (  "                         .CLKB(write_clock),"  )  ; 
pw  .  println  (  "                         .ADDRB(write_addr),"  )  ; 
pw  .  println  (  "                         .DIB(write_data[63:32]),"  )  ; 
pw  .  println  (  "                         .DIPB(4'h0),"  )  ; 
pw  .  println  (  "                         .DOPB(),"  )  ; 
pw  .  println  (  "                         .DOB());"  )  ; 
} 
if  (  dataSize  ==  1  )  pw  .  println  (  "  assign read_data = int_read_data[0];"  )  ;  else   if  (  dataSize  <=  32  )  pw  .  println  (  "  assign read_data = int_read_data["  +  (  dataSize  -  1  )  +  ":0];"  )  ;  else   pw  .  println  (  "  assign read_data = int_read_data;"  )  ; 
} 
} 

