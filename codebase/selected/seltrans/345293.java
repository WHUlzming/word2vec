package   net  .  sf  .  mzmine  .  alignmentresultvisualizers  ; 

import   net  .  sf  .  mzmine  .  alignmentresultmethods  .  *  ; 
import   net  .  sf  .  mzmine  .  alignmentresultvisualizers  .  *  ; 
import   net  .  sf  .  mzmine  .  datastructures  .  *  ; 
import   net  .  sf  .  mzmine  .  distributionframework  .  *  ; 
import   net  .  sf  .  mzmine  .  miscellaneous  .  *  ; 
import   net  .  sf  .  mzmine  .  peaklistmethods  .  *  ; 
import   net  .  sf  .  mzmine  .  rawdatamethods  .  *  ; 
import   net  .  sf  .  mzmine  .  rawdatavisualizers  .  *  ; 
import   net  .  sf  .  mzmine  .  userinterface  .  *  ; 
import   java  .  text  .  *  ; 
import   java  .  awt  .  *  ; 
import   java  .  awt  .  print  .  *  ; 
import   javax  .  print  .  attribute  .  HashPrintRequestAttributeSet  ; 
import   java  .  awt  .  image  .  *  ; 
import   java  .  awt  .  geom  .  *  ; 
import   java  .  awt  .  event  .  MouseListener  ; 
import   java  .  awt  .  event  .  MouseEvent  ; 
import   java  .  awt  .  event  .  ActionListener  ; 
import   java  .  awt  .  event  .  ItemListener  ; 
import   java  .  awt  .  event  .  ItemEvent  ; 
import   java  .  awt  .  datatransfer  .  Clipboard  ; 
import   javax  .  swing  .  *  ; 
import   javax  .  swing  .  event  .  InternalFrameListener  ; 
import   javax  .  swing  .  event  .  InternalFrameEvent  ; 
import   javax  .  swing  .  event  .  ChangeListener  ; 
import   javax  .  swing  .  event  .  ChangeEvent  ; 
import   java  .  util  .  Vector  ; 
import   java  .  util  .  Hashtable  ; 
import   java  .  text  .  DecimalFormat  ; 
import   jmprojection  .  Preprocessor  ; 
import   jmprojection  .  Sammons  ; 





public   class   AlignmentResultVisualizerSammonsPlotView   extends   JInternalFrame   implements   Printable  ,  AlignmentResultVisualizer  ,  InternalFrameListener  { 

private   static   final   double   marginSize  =  (  double  )  0.02  ; 

private   MainWindow   mainWin  ; 

private   Statusbar   statBar  ; 

private   AlignmentResult   alignmentResult  ; 

private   int  [  ]  sampleClasses  ; 

private   PlotYAxis   leftPnl  ; 

private   PlotXAxis   bottomPnl  ; 

private   PlotArea   plotArea  ; 

private   int   retval  ; 

private   AlignmentResultVisualizerSammonsPlotViewParameters   myParameters  ; 






public   AlignmentResultVisualizerSammonsPlotView  (  MainWindow   _mainWin  )  { 
mainWin  =  _mainWin  ; 
statBar  =  mainWin  .  getStatusBar  (  )  ; 
getContentPane  (  )  .  setLayout  (  new   BorderLayout  (  )  )  ; 
bottomPnl  =  new   PlotXAxis  (  )  ; 
bottomPnl  .  setMinimumSize  (  new   Dimension  (  getWidth  (  )  ,  25  )  )  ; 
bottomPnl  .  setPreferredSize  (  new   Dimension  (  getWidth  (  )  ,  25  )  )  ; 
bottomPnl  .  setBackground  (  Color  .  white  )  ; 
getContentPane  (  )  .  add  (  bottomPnl  ,  java  .  awt  .  BorderLayout  .  SOUTH  )  ; 
leftPnl  =  new   PlotYAxis  (  )  ; 
leftPnl  .  setMinimumSize  (  new   Dimension  (  100  ,  getHeight  (  )  )  )  ; 
leftPnl  .  setPreferredSize  (  new   Dimension  (  100  ,  getHeight  (  )  )  )  ; 
leftPnl  .  setBackground  (  Color  .  white  )  ; 
getContentPane  (  )  .  add  (  leftPnl  ,  java  .  awt  .  BorderLayout  .  WEST  )  ; 
plotArea  =  new   PlotArea  (  this  )  ; 
plotArea  .  setBackground  (  Color  .  white  )  ; 
getContentPane  (  )  .  add  (  plotArea  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
setResizable  (  true  )  ; 
setIconifiable  (  true  )  ; 
addInternalFrameListener  (  this  )  ; 
} 








public   AlignmentResultVisualizerSammonsPlotViewParameters   askParameters  (  AlignmentResult   _alignmentResult  ,  AlignmentResultVisualizerSammonsPlotViewParameters   _myParameters  )  { 
alignmentResult  =  _alignmentResult  ; 
myParameters  =  _myParameters  ; 
setTitle  (  alignmentResult  .  getNiceName  (  )  +  ": logratio plot"  )  ; 
int  [  ]  rawDataIDs  =  alignmentResult  .  getRawDataIDs  (  )  ; 
RawDataPlaceHolder  [  ]  rawDatas  =  new   RawDataPlaceHolder  [  rawDataIDs  .  length  ]  ; 
if  (  alignmentResult  .  isImported  (  )  )  { 
for  (  int   i  =  0  ;  i  <  rawDataIDs  .  length  ;  i  ++  )  { 
rawDatas  [  i  ]  =  new   RawDataPlaceHolder  (  alignmentResult  .  getImportedRawDataName  (  rawDataIDs  [  i  ]  )  ,  rawDataIDs  [  i  ]  )  ; 
} 
}  else  { 
for  (  int   i  =  0  ;  i  <  rawDataIDs  .  length  ;  i  ++  )  { 
rawDatas  [  i  ]  =  new   RawDataPlaceHolder  (  mainWin  .  getItemSelector  (  )  .  getRawDataByID  (  rawDataIDs  [  i  ]  )  .  getNiceName  (  )  ,  rawDataIDs  [  i  ]  )  ; 
} 
} 
String  [  ]  paramNames  =  {  "Number of iterations"  }  ; 
Double  [  ]  paramValues  =  {  new   Double  (  myParameters  .  paramTrainingLength  )  }  ; 
NumberFormat  [  ]  paramFormats  =  new   NumberFormat  [  1  ]  ; 
paramFormats  [  0  ]  =  NumberFormat  .  getIntegerInstance  (  )  ; 
SelectClassLabelsDialog   stgd  =  new   SelectClassLabelsDialog  (  mainWin  ,  "Sammons Plot"  ,  "Please define class labels for samples"  ,  rawDatas  ,  paramNames  ,  paramValues  ,  paramFormats  )  ; 
stgd  .  showModal  (  mainWin  .  getDesktop  (  )  )  ; 
retval  =  stgd  .  getExitCode  (  )  ; 
if  (  retval  ==  -  1  )  { 
return   null  ; 
} 
sampleClasses  =  stgd  .  getClasses  (  )  ; 
preparePlot  (  )  ; 
return   myParameters  ; 
} 

public   void   setAlignmentResult  (  AlignmentResult   alignmentResult  )  { 
} 





public   void   refreshVisualizer  (  int   changeType  )  { 
if  (  changeType  ==  AlignmentResultVisualizer  .  CHANGETYPE_PEAK_MEASURING_SETTING  )  { 
preparePlot  (  )  ; 
} 
} 




private   void   preparePlot  (  )  { 
ClientDialog   waitDialog  =  new   ClientDialog  (  mainWin  )  ; 
waitDialog  .  setTitle  (  "Calculating plot, please wait..."  )  ; 
waitDialog  .  addJob  (  new   Integer  (  1  )  ,  alignmentResult  .  getNiceName  (  )  ,  "client-side"  ,  Task  .  JOBSTATUS_UNDERPROCESSING_STR  ,  new   Double  (  0  )  )  ; 
waitDialog  .  showMe  (  )  ; 
waitDialog  .  paintNow  (  )  ; 
if  (  mainWin  .  getParameterStorage  (  )  .  getGeneralParameters  (  )  .  getPeakMeasuringType  (  )  ==  GeneralParameters  .  PARAMETERVALUE_PEAKMEASURING_HEIGHT  )  { 
setTitle  (  alignmentResult  .  getNiceName  (  )  +  ": Sammons plot of average peak heights."  )  ; 
} 
if  (  mainWin  .  getParameterStorage  (  )  .  getGeneralParameters  (  )  .  getPeakMeasuringType  (  )  ==  GeneralParameters  .  PARAMETERVALUE_PEAKMEASURING_AREA  )  { 
setTitle  (  alignmentResult  .  getNiceName  (  )  +  ": Sammons plot of average peak areas."  )  ; 
} 
int   numOfSamples  =  alignmentResult  .  getNumOfRawDatas  (  )  ; 
int   numOfPeaks  =  alignmentResult  .  getNumOfRows  (  )  ; 
int   numOfDim  =  alignmentResult  .  getNumOfFullRows  (  )  ; 
int  [  ]  rawDataIDs  =  alignmentResult  .  getRawDataIDs  (  )  ; 
String  [  ]  rawDataNames  =  new   String  [  rawDataIDs  .  length  ]  ; 
double  [  ]  [  ]  data  =  new   double  [  numOfSamples  ]  [  numOfDim  ]  ; 
for  (  int   sample  =  0  ;  sample  <  numOfSamples  ;  sample  ++  )  { 
int   rawDataID  =  rawDataIDs  [  sample  ]  ; 
if  (  alignmentResult  .  isImported  (  )  )  { 
rawDataNames  [  sample  ]  =  alignmentResult  .  getImportedRawDataName  (  rawDataID  )  ; 
}  else  { 
rawDataNames  [  sample  ]  =  mainWin  .  getItemSelector  (  )  .  getRawDataByID  (  rawDataID  )  .  getNiceName  (  )  ; 
} 
int   colInd  =  0  ; 
for  (  int   peak  =  0  ;  peak  <  numOfPeaks  ;  peak  ++  )  { 
if  (  !  (  alignmentResult  .  isFullRow  (  peak  )  )  )  { 
continue  ; 
} 
if  (  mainWin  .  getParameterStorage  (  )  .  getGeneralParameters  (  )  .  getPeakMeasuringType  (  )  ==  GeneralParameters  .  PARAMETERVALUE_PEAKMEASURING_HEIGHT  )  { 
data  [  sample  ]  [  colInd  ]  =  alignmentResult  .  getPeakHeight  (  rawDataID  ,  peak  )  ; 
} 
if  (  mainWin  .  getParameterStorage  (  )  .  getGeneralParameters  (  )  .  getPeakMeasuringType  (  )  ==  GeneralParameters  .  PARAMETERVALUE_PEAKMEASURING_AREA  )  { 
data  [  sample  ]  [  colInd  ]  =  alignmentResult  .  getPeakArea  (  rawDataID  ,  peak  )  ; 
} 
colInd  ++  ; 
} 
} 
double  [  ]  [  ]  dataT  =  new   double  [  data  [  0  ]  .  length  ]  [  data  .  length  ]  ; 
for  (  int   sample  =  0  ;  sample  <  data  .  length  ;  sample  ++  )  { 
for  (  int   dim  =  0  ;  dim  <  data  [  0  ]  .  length  ;  dim  ++  )  { 
dataT  [  dim  ]  [  sample  ]  =  (  data  [  sample  ]  [  dim  ]  )  ; 
} 
} 
waitDialog  .  updateJobStatus  (  new   Integer  (  1  )  ,  Task  .  JOBSTATUS_UNDERPROCESSING_STR  ,  new   Double  (  0.25  )  )  ; 
waitDialog  .  paintNow  (  )  ; 
double  [  ]  [  ]  dataT2  =  Preprocessor  .  autoScaleToUnityVariance  (  dataT  )  ; 
waitDialog  .  updateJobStatus  (  new   Integer  (  1  )  ,  Task  .  JOBSTATUS_UNDERPROCESSING_STR  ,  new   Double  (  0.50  )  )  ; 
waitDialog  .  paintNow  (  )  ; 
for  (  int   sample  =  0  ;  sample  <  data  .  length  ;  sample  ++  )  { 
for  (  int   dim  =  0  ;  dim  <  data  [  0  ]  .  length  ;  dim  ++  )  { 
data  [  sample  ]  [  dim  ]  =  dataT2  [  dim  ]  [  sample  ]  ; 
} 
} 
dataT2  =  null  ; 
waitDialog  .  updateJobStatus  (  new   Integer  (  1  )  ,  Task  .  JOBSTATUS_UNDERPROCESSING_STR  ,  new   Double  (  0.75  )  )  ; 
waitDialog  .  paintNow  (  )  ; 
Sammons   projector  =  new   Sammons  (  data  ,  2  )  ; 
for  (  int   i  =  0  ;  i  <  myParameters  .  paramTrainingLength  ;  i  ++  )  { 
projector  .  iterate  (  )  ; 
} 
double  [  ]  [  ]  results  =  projector  .  getState  (  )  ; 
waitDialog  .  updateJobStatus  (  new   Integer  (  1  )  ,  Task  .  JOBSTATUS_UNDERPROCESSING_STR  ,  new   Double  (  0.99  )  )  ; 
waitDialog  .  paintNow  (  )  ; 
plotArea  .  setData  (  results  [  0  ]  ,  results  [  1  ]  ,  sampleClasses  ,  rawDataNames  )  ; 
waitDialog  .  hideMe  (  )  ; 
} 




public   void   internalFrameActivated  (  InternalFrameEvent   e  )  { 
mainWin  .  getItemSelector  (  )  .  setActiveAlignmentResult  (  alignmentResult  )  ; 
} 

public   void   internalFrameClosed  (  InternalFrameEvent   e  )  { 
} 

public   void   internalFrameClosing  (  InternalFrameEvent   e  )  { 
} 

public   void   internalFrameDeactivated  (  InternalFrameEvent   e  )  { 
} 

public   void   internalFrameDeiconified  (  InternalFrameEvent   e  )  { 
} 

public   void   internalFrameIconified  (  InternalFrameEvent   e  )  { 
} 

public   void   internalFrameOpened  (  InternalFrameEvent   e  )  { 
} 




public   void   updateSelectedRow  (  )  { 
int   rowNum  =  alignmentResult  .  getSelectedRow  (  )  ; 
plotArea  .  selectAlignmentRow  (  rowNum  )  ; 
} 




public   void   printMe  (  )  { 
PrinterJob   printJob  =  PrinterJob  .  getPrinterJob  (  )  ; 
HashPrintRequestAttributeSet   pSet  =  new   HashPrintRequestAttributeSet  (  )  ; 
if  (  printJob  .  printDialog  (  pSet  )  )  { 
printJob  .  setPrintable  (  this  )  ; 
try  { 
printJob  .  print  (  pSet  )  ; 
}  catch  (  Exception   PrintException  )  { 
} 
} 
} 




public   int   print  (  Graphics   g  ,  PageFormat   pf  ,  int   pi  )  { 
double   sx  ,  sy  ; 
final   int   titleHeight  =  30  ; 
if  (  pi  >  0  )  { 
return   NO_SUCH_PAGE  ; 
}  else  { 
Graphics2D   g2  =  (  Graphics2D  )  g  ; 
g2  .  translate  (  pf  .  getImageableX  (  )  ,  pf  .  getImageableY  (  )  )  ; 
g2  .  drawString  (  this  .  getTitle  (  )  ,  0  ,  titleHeight  -  5  )  ; 
g2  .  translate  (  0  ,  titleHeight  )  ; 
sx  =  (  double  )  pf  .  getImageableWidth  (  )  /  (  double  )  getContentPane  (  )  .  getWidth  (  )  ; 
sy  =  (  double  )  (  pf  .  getImageableHeight  (  )  -  titleHeight  )  /  (  double  )  getContentPane  (  )  .  getHeight  (  )  ; 
g2  .  transform  (  AffineTransform  .  getScaleInstance  (  sx  ,  sy  )  )  ; 
RepaintManager   currentManager  =  RepaintManager  .  currentManager  (  getContentPane  (  )  )  ; 
currentManager  .  setDoubleBufferingEnabled  (  false  )  ; 
getContentPane  (  )  .  paint  (  g2  )  ; 
currentManager  .  setDoubleBufferingEnabled  (  true  )  ; 
return   Printable  .  PAGE_EXISTS  ; 
} 
} 




public   void   copyMe  (  )  { 
Toolkit   toolkit  =  Toolkit  .  getDefaultToolkit  (  )  ; 
Clipboard   clipboard  =  toolkit  .  getSystemClipboard  (  )  ; 
int   w  =  getContentPane  (  )  .  getWidth  (  )  ; 
int   h  =  getContentPane  (  )  .  getHeight  (  )  ; 
BufferedImage   bi  =  new   BufferedImage  (  w  ,  h  ,  BufferedImage  .  TYPE_INT_ARGB  )  ; 
Graphics2D   g  =  (  Graphics2D  )  bi  .  getGraphics  (  )  ; 
getContentPane  (  )  .  paint  (  g  )  ; 
clipboard  .  setContents  (  new   TransferableImage  (  bi  )  ,  null  )  ; 
} 




private   class   PlotArea   extends   JPanel   implements   ActionListener  ,  java  .  awt  .  event  .  MouseListener  ,  java  .  awt  .  event  .  MouseMotionListener  { 

private   final   int   rightMargin  =  70  ; 

private   AlignmentResultVisualizerSammonsPlotView   masterFrame  ; 

private   double   minComp2  ; 

private   double   maxComp2  ; 

private   double   minComp1  ; 

private   double   maxComp1  ; 

private   double   zoomMinComp2  ; 

private   double   zoomMaxComp2  ; 

private   double   zoomMinComp1  ; 

private   double   zoomMaxComp1  ; 

private   double   cursorPositionComp2  ; 

private   double   cursorPositionComp1  ; 

private   double   mouseAreaStartComp2  ; 

private   double   mouseAreaEndComp2  ; 

private   double   mouseAreaStartComp1  ; 

private   double   mouseAreaEndComp1  ; 

private   double   selectionFirstClickComp2  ; 

private   double   selectionFirstClickComp1  ; 

private   double   selectionLastClickComp2  ; 

private   double   selectionLastClickComp1  ; 

private   int  [  ]  alignmentRowValues  =  null  ; 

private   double  [  ]  comp1Values  =  null  ; 

private   double  [  ]  comp2Values  =  null  ; 

private   int  [  ]  sampleClasses  =  null  ; 

private   String  [  ]  sampleLabels  =  null  ; 

private   JPopupMenu   popupMenu  ; 

private   JMenuItem   zoomToSelectionMenuItem  ; 

private   JMenuItem   zoomOutMenuItem  ; 

private   JMenuItem   zoomOutLittleMenuItem  ; 

private   JMenuItem   selectNearestPeakMenuItem  ; 






public   PlotArea  (  AlignmentResultVisualizerSammonsPlotView   _masterFrame  )  { 
masterFrame  =  _masterFrame  ; 
popupMenu  =  new   JPopupMenu  (  )  ; 
zoomToSelectionMenuItem  =  new   JMenuItem  (  "Zoom to selection"  )  ; 
zoomToSelectionMenuItem  .  addActionListener  (  this  )  ; 
zoomToSelectionMenuItem  .  setEnabled  (  false  )  ; 
popupMenu  .  add  (  zoomToSelectionMenuItem  )  ; 
zoomOutMenuItem  =  new   JMenuItem  (  "Zoom out full"  )  ; 
zoomOutMenuItem  .  addActionListener  (  this  )  ; 
popupMenu  .  add  (  zoomOutMenuItem  )  ; 
zoomOutLittleMenuItem  =  new   JMenuItem  (  "Zoom out little"  )  ; 
zoomOutLittleMenuItem  .  addActionListener  (  this  )  ; 
popupMenu  .  add  (  zoomOutLittleMenuItem  )  ; 
addMouseListener  (  this  )  ; 
addMouseMotionListener  (  this  )  ; 
} 




public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   e  )  { 
Object   src  =  e  .  getSource  (  )  ; 
if  (  src  ==  zoomToSelectionMenuItem  )  { 
zoomMinComp2  =  mouseAreaStartComp2  ; 
zoomMaxComp2  =  mouseAreaEndComp2  ; 
zoomMinComp1  =  mouseAreaStartComp1  ; 
zoomMaxComp1  =  mouseAreaEndComp1  ; 
bottomPnl  .  setScale  (  zoomMinComp1  ,  zoomMaxComp1  )  ; 
leftPnl  .  setScale  (  zoomMinComp2  ,  zoomMaxComp2  )  ; 
repaint  (  )  ; 
} 
if  (  src  ==  zoomOutMenuItem  )  { 
zoomMinComp2  =  minComp2  ; 
zoomMaxComp2  =  maxComp2  ; 
zoomMinComp1  =  minComp1  ; 
zoomMaxComp1  =  maxComp1  ; 
bottomPnl  .  setScale  (  zoomMinComp1  ,  zoomMaxComp1  )  ; 
leftPnl  .  setScale  (  zoomMinComp2  ,  zoomMaxComp2  )  ; 
repaint  (  )  ; 
} 
if  (  src  ==  zoomOutLittleMenuItem  )  { 
zoomOutLittle  (  )  ; 
bottomPnl  .  setScale  (  zoomMinComp1  ,  zoomMaxComp1  )  ; 
leftPnl  .  setScale  (  zoomMinComp2  ,  zoomMaxComp2  )  ; 
repaint  (  )  ; 
} 
} 




public   void   selectAlignmentRow  (  int   rowNum  )  { 
} 




private   void   zoomOutLittle  (  )  { 
double   midX  =  (  zoomMinComp1  +  zoomMaxComp1  )  /  (  double  )  2.0  ; 
double   midY  =  (  zoomMinComp2  +  zoomMaxComp2  )  /  (  double  )  2.0  ; 
double   tmpMinX  ,  tmpMaxX  ; 
double   tmpMinY  ,  tmpMaxY  ; 
if  (  (  (  midX  -  zoomMinComp1  )  >  0  )  &&  (  (  zoomMaxComp1  -  midX  )  >  0  )  )  { 
tmpMinX  =  (  int  )  (  java  .  lang  .  Math  .  round  (  midX  -  (  midX  -  zoomMinComp1  )  *  1.5  )  )  ; 
tmpMaxX  =  (  int  )  (  java  .  lang  .  Math  .  round  (  midX  +  (  zoomMaxComp1  -  midX  )  *  1.5  )  )  ; 
}  else  { 
tmpMinX  =  zoomMinComp1  -  1  ; 
tmpMaxX  =  zoomMaxComp1  +  1  ; 
} 
if  (  (  (  midY  -  zoomMinComp2  )  >  0  )  &&  (  (  zoomMaxComp2  -  midY  )  >  0  )  )  { 
tmpMinY  =  midY  -  (  midY  -  zoomMinComp2  )  *  (  double  )  1.5  ; 
tmpMaxY  =  midY  +  (  zoomMaxComp2  -  midY  )  *  (  double  )  1.5  ; 
}  else  { 
tmpMinY  =  zoomMinComp2  -  1  ; 
tmpMaxY  =  zoomMaxComp2  +  1  ; 
} 
if  (  tmpMinX  <  minComp1  )  { 
tmpMinX  =  minComp1  ; 
} 
if  (  tmpMaxX  >  maxComp1  )  { 
tmpMaxX  =  maxComp1  ; 
} 
if  (  tmpMinY  <  minComp2  )  { 
tmpMinY  =  minComp2  ; 
} 
if  (  tmpMaxY  >  maxComp2  )  { 
tmpMaxY  =  maxComp2  ; 
} 
zoomMinComp1  =  tmpMinX  ; 
zoomMaxComp1  =  tmpMaxX  ; 
zoomMinComp2  =  tmpMinY  ; 
zoomMaxComp2  =  tmpMaxY  ; 
} 




public   void   paint  (  Graphics   g  )  { 
super  .  paint  (  g  )  ; 
double   x  ,  y  ; 
int   x1  ,  y1  ,  x2  ,  y2  ; 
int   radius  ; 
double   comp2  ,  comp1  ; 
int   sampleClass  ; 
String   sampleLabel  ; 
double   red  ,  green  ; 
Color   c  ; 
if  (  comp2Values  !=  null  )  { 
double   diff_x_dat  =  zoomMaxComp1  -  zoomMinComp1  ; 
double   diff_y_dat  =  zoomMaxComp2  -  zoomMinComp2  ; 
double   diff_x_scr  =  getWidth  (  )  -  rightMargin  ; 
double   diff_y_scr  =  getHeight  (  )  ; 
if  (  diff_x_scr  <=  0  )  { 
return  ; 
} 
if  (  diff_y_scr  <=  0  )  { 
return  ; 
} 
if  (  diff_x_scr  <  diff_y_scr  )  { 
radius  =  (  int  )  java  .  lang  .  Math  .  round  (  diff_x_scr  *  0.01  )  ; 
}  else  { 
radius  =  (  int  )  java  .  lang  .  Math  .  round  (  diff_y_scr  *  0.01  )  ; 
} 
if  (  radius  <  1  )  { 
radius  =  1  ; 
} 
for  (  int   ind  =  0  ;  ind  <  comp2Values  .  length  ;  ind  ++  )  { 
comp1  =  comp1Values  [  ind  ]  ; 
comp2  =  comp2Values  [  ind  ]  ; 
sampleClass  =  sampleClasses  [  ind  ]  ; 
sampleLabel  =  sampleLabels  [  ind  ]  ; 
x1  =  (  int  )  java  .  lang  .  Math  .  round  (  (  double  )  diff_x_scr  *  (  (  double  )  (  comp1  -  zoomMinComp1  )  /  (  double  )  diff_x_dat  )  )  ; 
y1  =  (  int  )  (  diff_y_scr  *  (  1  -  (  (  comp2  -  zoomMinComp2  )  /  diff_y_dat  )  )  )  ; 
switch  (  sampleClass  )  { 
case   0  : 
g  .  setColor  (  Color  .  lightGray  )  ; 
break  ; 
case   1  : 
g  .  setColor  (  Color  .  red  )  ; 
break  ; 
case   2  : 
g  .  setColor  (  Color  .  green  )  ; 
break  ; 
case   3  : 
g  .  setColor  (  Color  .  blue  )  ; 
break  ; 
case   4  : 
g  .  setColor  (  Color  .  orange  )  ; 
break  ; 
case   5  : 
g  .  setColor  (  Color  .  yellow  )  ; 
break  ; 
case   6  : 
g  .  setColor  (  Color  .  cyan  )  ; 
break  ; 
case   7  : 
g  .  setColor  (  Color  .  magenta  )  ; 
break  ; 
case   8  : 
g  .  setColor  (  Color  .  pink  )  ; 
break  ; 
} 
g  .  fillOval  (  x1  -  radius  ,  y1  -  radius  ,  2  *  radius  ,  2  *  radius  )  ; 
g  .  setColor  (  Color  .  black  )  ; 
g  .  drawBytes  (  sampleLabel  .  getBytes  (  )  ,  0  ,  sampleLabel  .  length  (  )  ,  x1  ,  y1  )  ; 
} 
x  =  cursorPositionComp1  ; 
x2  =  (  int  )  java  .  lang  .  Math  .  round  (  (  double  )  diff_x_scr  *  (  (  double  )  (  x  -  zoomMinComp1  )  /  (  double  )  (  zoomMaxComp1  -  zoomMinComp1  )  )  )  ; 
g  .  setColor  (  Color  .  red  )  ; 
g  .  drawLine  (  x2  ,  0  ,  x2  ,  (  int  )  diff_y_scr  )  ; 
y  =  cursorPositionComp2  ; 
y2  =  (  int  )  java  .  lang  .  Math  .  round  (  (  double  )  diff_y_scr  *  (  (  double  )  (  y  -  zoomMinComp2  )  /  (  double  )  (  zoomMaxComp2  -  zoomMinComp2  )  )  )  ; 
g  .  setColor  (  Color  .  red  )  ; 
g  .  drawLine  (  0  ,  (  int  )  (  diff_y_scr  -  y2  )  ,  (  int  )  diff_x_scr  ,  (  int  )  (  diff_y_scr  -  y2  )  )  ; 
x  =  mouseAreaStartComp1  ; 
x1  =  (  int  )  java  .  lang  .  Math  .  round  (  (  double  )  diff_x_scr  *  (  (  double  )  (  x  -  zoomMinComp1  )  /  (  double  )  (  zoomMaxComp1  -  zoomMinComp1  )  )  )  ; 
x  =  mouseAreaEndComp1  ; 
x2  =  (  int  )  java  .  lang  .  Math  .  round  (  (  double  )  diff_x_scr  *  (  (  double  )  (  x  -  zoomMinComp1  )  /  (  double  )  (  zoomMaxComp1  -  zoomMinComp1  )  )  )  ; 
y  =  mouseAreaStartComp2  ; 
y1  =  (  int  )  java  .  lang  .  Math  .  round  (  (  double  )  diff_y_scr  *  (  (  double  )  (  y  -  zoomMinComp2  )  /  (  double  )  (  zoomMaxComp2  -  zoomMinComp2  )  )  )  ; 
y  =  mouseAreaEndComp2  ; 
y2  =  (  int  )  java  .  lang  .  Math  .  round  (  (  double  )  diff_y_scr  *  (  (  double  )  (  y  -  zoomMinComp2  )  /  (  double  )  (  zoomMaxComp2  -  zoomMinComp2  )  )  )  ; 
g  .  setColor  (  Color  .  blue  )  ; 
g  .  drawRect  (  x1  ,  (  int  )  (  diff_y_scr  -  y2  )  ,  x2  -  x1  ,  (  int  )  (  y2  -  y1  )  )  ; 
} 
} 




public   void   setData  (  double  [  ]  _comp1Values  ,  double  [  ]  _comp2Values  ,  int  [  ]  _sampleClasses  ,  String  [  ]  _sampleLabels  )  { 
comp1Values  =  _comp1Values  ; 
comp2Values  =  _comp2Values  ; 
sampleClasses  =  _sampleClasses  ; 
sampleLabels  =  _sampleLabels  ; 
minComp1  =  comp1Values  [  0  ]  ; 
maxComp1  =  comp1Values  [  0  ]  ; 
for  (  double   d  :  comp1Values  )  { 
if  (  minComp1  >=  d  )  { 
minComp1  =  d  ; 
} 
if  (  maxComp1  <=  d  )  { 
maxComp1  =  d  ; 
} 
} 
minComp2  =  comp2Values  [  0  ]  ; 
maxComp2  =  comp2Values  [  0  ]  ; 
for  (  double   d  :  comp2Values  )  { 
if  (  minComp2  >=  d  )  { 
minComp2  =  d  ; 
} 
if  (  maxComp2  <=  d  )  { 
maxComp2  =  d  ; 
} 
} 
double   midComp1  =  (  minComp1  +  maxComp1  )  /  2.0  ; 
double   midComp2  =  (  minComp2  +  maxComp2  )  /  2.0  ; 
minComp1  =  midComp1  -  (  midComp1  -  minComp1  )  *  1.20  ; 
maxComp1  =  midComp1  +  (  maxComp1  -  midComp1  )  *  1.20  ; 
minComp2  =  midComp2  -  (  midComp2  -  minComp2  )  *  1.20  ; 
maxComp2  =  midComp2  +  (  maxComp2  -  midComp2  )  *  1.20  ; 
zoomMinComp1  =  minComp1  ; 
zoomMaxComp1  =  maxComp1  ; 
zoomMinComp2  =  minComp2  ; 
zoomMaxComp2  =  maxComp2  ; 
bottomPnl  .  setScale  (  zoomMinComp1  ,  zoomMaxComp1  )  ; 
leftPnl  .  setScale  (  zoomMinComp2  ,  zoomMaxComp2  )  ; 
} 




public   void   mouseClicked  (  MouseEvent   e  )  { 
} 

public   void   mouseEntered  (  MouseEvent   e  )  { 
} 

public   void   mouseExited  (  MouseEvent   e  )  { 
} 

public   void   mouseReleased  (  MouseEvent   e  )  { 
if  (  e  .  getButton  (  )  !=  MouseEvent  .  BUTTON1  )  { 
popupMenu  .  show  (  e  .  getComponent  (  )  ,  e  .  getX  (  )  ,  e  .  getY  (  )  )  ; 
}  else  { 
selectionFirstClickComp1  =  -  1  ; 
selectionFirstClickComp2  =  -  1  ; 
selectionLastClickComp1  =  -  1  ; 
selectionLastClickComp2  =  -  1  ; 
} 
statBar  .  setStatusText  (  ""  )  ; 
} 

private   int   lastPressedButtonWas  ; 




public   void   mousePressed  (  MouseEvent   e  )  { 
lastPressedButtonWas  =  e  .  getButton  (  )  ; 
if  (  e  .  getButton  (  )  ==  MouseEvent  .  BUTTON1  )  { 
int   w  =  getWidth  (  )  -  rightMargin  ; 
double   diff_x_dat  =  zoomMaxComp1  -  zoomMinComp1  ; 
double   diff_x_scr  =  w  ; 
double   xpos  =  (  zoomMinComp1  +  diff_x_dat  *  e  .  getX  (  )  /  diff_x_scr  )  ; 
int   h  =  getHeight  (  )  ; 
double   diff_y_dat  =  zoomMaxComp2  -  zoomMinComp2  ; 
double   diff_y_scr  =  h  ; 
double   ypos  =  (  zoomMinComp2  +  diff_y_dat  *  (  double  )  (  h  -  e  .  getY  (  )  )  /  diff_y_scr  )  ; 
selectionFirstClickComp1  =  xpos  ; 
selectionFirstClickComp2  =  ypos  ; 
mouseAreaStartComp1  =  xpos  ; 
mouseAreaEndComp1  =  xpos  ; 
mouseAreaStartComp2  =  ypos  ; 
mouseAreaEndComp2  =  ypos  ; 
cursorPositionComp1  =  xpos  ; 
cursorPositionComp2  =  ypos  ; 
zoomToSelectionMenuItem  .  setEnabled  (  false  )  ; 
repaint  (  )  ; 
} 
statBar  .  setStatusText  (  ""  )  ; 
} 

public   void   mouseDragged  (  MouseEvent   e  )  { 
if  (  lastPressedButtonWas  !=  MouseEvent  .  BUTTON1  )  { 
return  ; 
} 
int   w  =  getWidth  (  )  -  rightMargin  ; 
double   diff_x_dat  =  zoomMaxComp1  -  zoomMinComp1  ; 
double   diff_x_scr  =  w  ; 
double   xpos  =  zoomMinComp1  +  diff_x_dat  *  e  .  getX  (  )  /  diff_x_scr  ; 
int   h  =  getHeight  (  )  ; 
double   diff_y_dat  =  zoomMaxComp2  -  zoomMinComp2  ; 
double   diff_y_scr  =  h  ; 
double   ypos  =  (  zoomMinComp2  +  diff_y_dat  *  (  double  )  (  h  -  e  .  getY  (  )  )  /  diff_y_scr  )  ; 
if  (  selectionFirstClickComp1  ==  -  1  )  { 
selectionFirstClickComp1  =  xpos  ; 
selectionFirstClickComp2  =  ypos  ; 
}  else  { 
selectionLastClickComp1  =  xpos  ; 
selectionLastClickComp2  =  ypos  ; 
if  (  selectionLastClickComp1  <  zoomMinComp1  )  { 
selectionLastClickComp1  =  zoomMinComp1  ; 
} 
if  (  selectionLastClickComp1  >  (  zoomMaxComp1  )  )  { 
selectionLastClickComp1  =  zoomMaxComp1  ; 
} 
if  (  selectionLastClickComp2  <  zoomMinComp2  )  { 
selectionLastClickComp2  =  zoomMinComp2  ; 
} 
if  (  selectionLastClickComp2  >  zoomMaxComp2  )  { 
selectionLastClickComp2  =  zoomMaxComp2  ; 
} 
if  (  selectionLastClickComp1  >  selectionFirstClickComp1  )  { 
mouseAreaStartComp1  =  selectionFirstClickComp1  ; 
mouseAreaEndComp1  =  selectionLastClickComp1  ; 
}  else  { 
mouseAreaStartComp1  =  selectionLastClickComp1  ; 
mouseAreaEndComp1  =  selectionFirstClickComp1  ; 
} 
if  (  selectionLastClickComp2  >  selectionFirstClickComp2  )  { 
mouseAreaStartComp2  =  selectionFirstClickComp2  ; 
mouseAreaEndComp2  =  selectionLastClickComp2  ; 
}  else  { 
mouseAreaStartComp2  =  selectionLastClickComp2  ; 
mouseAreaEndComp2  =  selectionFirstClickComp2  ; 
} 
zoomToSelectionMenuItem  .  setEnabled  (  true  )  ; 
repaint  (  )  ; 
} 
statBar  .  setStatusText  (  ""  )  ; 
} 




public   void   mouseMoved  (  MouseEvent   e  )  { 
} 
} 

private   class   PlotXAxis   extends   JPanel  { 

private   final   int   leftMargin  =  100  ; 

private   final   int   rightMargin  =  70  ; 

private   DecimalFormat   numberFormatter  ; 

private   double   minX  ; 

private   double   maxX  ; 

public   PlotXAxis  (  )  { 
super  (  )  ; 
numberFormatter  =  new   DecimalFormat  (  "0.0"  )  ; 
} 

public   void   paint  (  Graphics   g  )  { 
super  .  paint  (  g  )  ; 
int   w  =  getWidth  (  )  ; 
double   h  =  getHeight  (  )  ; 
if  (  w  <=  0  )  { 
return  ; 
} 
if  (  h  <=  0  )  { 
return  ; 
} 
double   dataRange  =  maxX  -  minX  ; 
if  (  dataRange  ==  0  )  { 
return  ; 
} 
double   pixelsPerUnit  =  (  double  )  (  w  -  leftMargin  -  rightMargin  )  /  dataRange  ; 
if  (  pixelsPerUnit  <=  0  )  { 
return  ; 
} 
double   unitsPerTic  =  0  ; 
while  (  (  unitsPerTic  *  pixelsPerUnit  )  <  60  )  { 
unitsPerTic  ++  ; 
} 
double   pixelsPerTic  =  (  double  )  unitsPerTic  *  pixelsPerUnit  ; 
int   numoftics  =  (  int  )  java  .  lang  .  Math  .  floor  (  (  double  )  dataRange  /  (  double  )  unitsPerTic  )  ; 
this  .  setForeground  (  Color  .  black  )  ; 
g  .  drawLine  (  (  int  )  leftMargin  ,  0  ,  (  int  )  (  w  -  rightMargin  )  ,  0  )  ; 
String   tmps  ; 
double   xpos  =  leftMargin  ; 
double   xval  =  minX  ; 
for  (  int   t  =  0  ;  t  <  numoftics  ;  t  ++  )  { 
tmps  =  numberFormatter  .  format  (  xval  )  ; 
g  .  drawLine  (  (  int  )  java  .  lang  .  Math  .  round  (  xpos  )  ,  0  ,  (  int  )  java  .  lang  .  Math  .  round  (  xpos  )  ,  (  int  )  (  h  /  4  )  )  ; 
g  .  drawBytes  (  tmps  .  getBytes  (  )  ,  0  ,  tmps  .  length  (  )  ,  (  int  )  java  .  lang  .  Math  .  round  (  xpos  )  ,  (  int  )  (  3  *  h  /  4  )  )  ; 
xval  +=  unitsPerTic  ; 
xpos  +=  pixelsPerTic  ; 
} 
} 

public   void   setScale  (  double   _minX  ,  double   _maxX  )  { 
minX  =  _minX  ; 
maxX  =  _maxX  ; 
repaint  (  )  ; 
} 
} 




private   class   PlotYAxis   extends   JPanel  { 

private   final   double   bottomMargin  =  (  double  )  0.0  ; 

private   final   double   topMargin  =  (  double  )  0.0  ; 

private   double   minY  ; 

private   double   maxY  ; 

private   int   numTics  ; 

private   DecimalFormat   numberFormatter  ; 

public   PlotYAxis  (  )  { 
super  (  )  ; 
numberFormatter  =  new   DecimalFormat  (  "0.0"  )  ; 
} 

public   void   paint  (  Graphics   g  )  { 
super  .  paint  (  g  )  ; 
double   w  =  getWidth  (  )  ; 
double   h  =  getHeight  (  )  ; 
if  (  w  <=  0  )  { 
return  ; 
} 
if  (  h  <=  0  )  { 
return  ; 
} 
double   dataRange  =  maxY  -  minY  ; 
if  (  dataRange  ==  0  )  { 
return  ; 
} 
double   pixelsPerUnit  =  (  double  )  (  h  -  topMargin  -  bottomMargin  )  /  dataRange  ; 
if  (  pixelsPerUnit  <=  0  )  { 
return  ; 
} 
double   unitsPerTic  =  0  ; 
while  (  (  unitsPerTic  *  pixelsPerUnit  )  <  60  )  { 
unitsPerTic  ++  ; 
} 
double   pixelsPerTic  =  (  double  )  unitsPerTic  *  pixelsPerUnit  ; 
int   numoftics  =  (  int  )  java  .  lang  .  Math  .  floor  (  (  double  )  dataRange  /  (  double  )  unitsPerTic  )  ; 
this  .  setForeground  (  Color  .  black  )  ; 
g  .  drawLine  (  (  int  )  w  -  1  ,  0  ,  (  int  )  w  -  1  ,  (  int  )  h  )  ; 
String   tmps  ; 
double   ypos  =  bottomMargin  ; 
double   yval  =  minY  ; 
for  (  int   t  =  1  ;  t  <=  numoftics  ;  t  ++  )  { 
tmps  =  numberFormatter  .  format  (  yval  )  ; 
g  .  drawLine  (  (  int  )  (  3  *  w  /  4  )  ,  (  int  )  (  h  -  ypos  )  ,  (  int  )  (  w  )  ,  (  int  )  (  h  -  ypos  )  )  ; 
g  .  drawBytes  (  tmps  .  getBytes  (  )  ,  0  ,  tmps  .  length  (  )  ,  (  int  )  (  w  /  4  )  -  4  ,  (  int  )  (  h  -  ypos  )  )  ; 
yval  +=  unitsPerTic  ; 
ypos  +=  pixelsPerTic  ; 
} 
} 

public   void   setScale  (  double   _minY  ,  double   _maxY  )  { 
minY  =  _minY  ; 
maxY  =  _maxY  ; 
repaint  (  )  ; 
} 
} 

private   class   RawDataPlaceHolder  { 

private   String   niceName  ; 

private   int   rawDataID  ; 

public   RawDataPlaceHolder  (  String   _niceName  ,  int   _rawDataID  )  { 
niceName  =  _niceName  ; 
rawDataID  =  _rawDataID  ; 
} 

public   String   toString  (  )  { 
return   niceName  ; 
} 

public   int   getRawDataID  (  )  { 
return   rawDataID  ; 
} 
} 
} 

