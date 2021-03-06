package   edu  .  usc  .  epigenome  .  uecgatk  .  bisulfitesnpmodel  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  io  .  PrintWriter  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  HashMap  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  LinkedList  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  TreeSet  ; 
import   net  .  sf  .  samtools  .  SAMFileWriter  ; 
import   net  .  sf  .  samtools  .  SAMFileWriterFactory  ; 
import   net  .  sf  .  samtools  .  SAMSequenceDictionary  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  variantcontext  .  Genotype  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  variantcontext  .  VariantContext  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  codecs  .  vcf  .  SortingVCFWriter  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  codecs  .  vcf  .  VCFConstants  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  codecs  .  vcf  .  VCFFilterHeaderLine  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  codecs  .  vcf  .  VCFHeader  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  codecs  .  vcf  .  VCFHeaderLine  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  codecs  .  vcf  .  VCFHeaderLineType  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  codecs  .  vcf  .  VCFInfoHeaderLine  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  codecs  .  vcf  .  VCFWriter  ; 
import   org  .  broadinstitute  .  sting  .  commandline  .  Argument  ; 
import   org  .  broadinstitute  .  sting  .  commandline  .  ArgumentCollection  ; 
import   org  .  broadinstitute  .  sting  .  commandline  .  Output  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  DownsampleType  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  contexts  .  AlignmentContext  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  contexts  .  ReferenceContext  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  datasources  .  rmd  .  ReferenceOrderedDataSource  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  datasources  .  reads  .  SAMDataSource  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  refdata  .  RefMetaDataTracker  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  BAQMode  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  By  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  DataSource  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  Downsample  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  LocusWalker  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  Reference  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  TreeReducible  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  Window  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  annotator  .  VariantAnnotatorEngine  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  genotyper  .  GenotypeLikelihoodsCalculationModel  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  genotyper  .  UnifiedArgumentCollection  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  genotyper  .  UnifiedGenotyper  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  genotyper  .  UnifiedGenotyperEngine  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  genotyper  .  VariantCallContext  ; 
import   org  .  broadinstitute  .  sting  .  gatk  .  walkers  .  genotyper  .  UnifiedGenotyper  .  UGStatistics  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  GenomeLoc  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  SampleUtils  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  baq  .  BAQ  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  pileup  .  PileupElement  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  pileup  .  ReadBackedPileup  ; 
import   org  .  broadinstitute  .  sting  .  utils  .  sam  .  GATKSAMRecord  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWalker  .  verboseWriter  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  bisulfitesnpmodel  .  BisulfiteSNPGenotypeLikelihoodsCalculationModel  .  MethylSNPModel  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  bisulfitesnpmodel  .  BaseUtilsMore  .  *  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWriter  .  FormatWriterBase  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWriter  .  NOMeSeqReads  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWriter  .  SortingCpgReadsWriter  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWriter  .  SortingFormatWriterBase  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWriter  .  cpgReads  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWriter  .  cpgReadsWriterImp  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWriter  .  SortingNOMeSeqReadsWriter  ; 
import   edu  .  usc  .  epigenome  .  uecgatk  .  YapingWriter  .  NOMeSeqReadsWriterImp  ; 




@  BAQMode  (  QualityMode  =  BAQ  .  QualityMode  .  OVERWRITE_QUALS  ,  ApplicationTime  =  BAQ  .  ApplicationTime  .  ON_INPUT  ) 
@  Reference  (  window  =  @  Window  (  start  =  -  500  ,  stop  =  500  )  ) 
@  By  (  DataSource  .  REFERENCE  ) 
@  Downsample  (  by  =  DownsampleType  .  NONE  ) 
public   class   BisulfiteGenotyper   extends   LocusWalker  <  BisulfiteVariantCallContext  ,  BisulfiteGenotyper  .  ContextCondition  >  implements   TreeReducible  <  BisulfiteGenotyper  .  ContextCondition  >  { 

@  ArgumentCollection 
private   static   BisulfiteArgumentCollection   BAC  =  new   BisulfiteArgumentCollection  (  )  ; 

private   static   boolean   autoEstimateC  =  false  ; 

private   static   boolean   secondIteration  =  false  ; 

private   static   int   MAXIMUM_CACHE_FOR_OUTPUT_VCF  =  3000000  ; 

private   static   long   COUNT_CACHE_FOR_OUTPUT_VCF  =  0  ; 

private   static   long   COUNT_CACHE_FOR_OUTPUT_READS  =  0  ; 

private   static   int   MAXIMUM_CACHE_FOR_OUTPUT_READS  =  300000  ; 

protected   TcgaVCFWriter   writer  =  null  ; 

protected   SortingTcgaVCFWriter   multiThreadWriter  =  null  ; 

protected   TcgaVCFWriter   additionalWriterForDefaultTcgaMode  =  null  ; 

protected   SortingTcgaVCFWriter   multiAdditionalWriterForDefaultTcgaMode  =  null  ; 

protected   FormatWriterBase   readsWriter  =  null  ; 

protected   SortingFormatWriterBase   multiThreadCpgReadsWriter  =  null  ; 

protected   TcgaVCFWriter   verboseWriter  =  null  ; 

protected   SAMFileWriter   samWriter  =  null  ; 

private   int   SAMPLE_READS_MEAN_COVERAGE  =  30  ; 

private   BisulfiteGenotyperEngine   BG_engine  =  null  ; 

private   String   argCommandline  =  ""  ; 

CytosineTypeStatus   summary  =  null  ; 




public   static   class   ContextCondition  { 


long   nBasesVisited  =  0  ; 


long   nBasesCallable  =  0  ; 


long   nBasesCalledConfidently  =  0  ; 


long   nCallsMade  =  0  ; 


long   nExtendedEvents  =  0  ; 


long   nCytosineBasesCalledConfidently  =  0  ; 


long   nCpgBasesCalledConfidently  =  0  ; 

long   nCphBasesCalledConfidently  =  0  ; 


long   nGchBasesCalledConfidently  =  0  ; 


long   nCchBasesCalledConfidently  =  0  ; 


long   nWchBasesCalledConfidently  =  0  ; 


long   nGcgBasesCalledConfidently  =  0  ; 


long   nCcgBasesCalledConfidently  =  0  ; 


long   nWcgBasesCalledConfidently  =  0  ; 


double   sumMethyCytosineBasesCalledConfidently  =  0  ; 


double   sumMethyCpgBasesCalledConfidently  =  0  ; 

double   sumMethyCphBasesCalledConfidently  =  0  ; 


double   sumMethyGchBasesCalledConfidently  =  0  ; 


double   sumMethyCchBasesCalledConfidently  =  0  ; 


double   sumMethyWchBasesCalledConfidently  =  0  ; 


double   sumMethyGcgBasesCalledConfidently  =  0  ; 


double   sumMethyCcgBasesCalledConfidently  =  0  ; 


double   sumMethyWcgBasesCalledConfidently  =  0  ; 

double   percentCallableOfAll  (  )  { 
return  (  100.0  *  nBasesCallable  )  /  (  nBasesVisited  -  nExtendedEvents  )  ; 
} 

double   percentCalledOfAll  (  )  { 
return  (  100.0  *  nBasesCalledConfidently  )  /  (  nBasesVisited  -  nExtendedEvents  )  ; 
} 

double   percentCalledOfCallable  (  )  { 
return  (  100.0  *  nBasesCalledConfidently  )  /  (  nBasesCallable  )  ; 
} 

double   percentMethyLevelOfC  (  )  { 
return  (  sumMethyCytosineBasesCalledConfidently  )  /  (  double  )  (  nCytosineBasesCalledConfidently  )  ; 
} 

double   percentMethyLevelOfCpg  (  )  { 
return  (  sumMethyCpgBasesCalledConfidently  )  /  (  double  )  (  nCpgBasesCalledConfidently  )  ; 
} 

double   percentMethyLevelOfCph  (  )  { 
return  (  sumMethyCphBasesCalledConfidently  )  /  (  double  )  (  nCphBasesCalledConfidently  )  ; 
} 

double   percentMethyLevelOfGch  (  )  { 
return  (  sumMethyGchBasesCalledConfidently  )  /  (  double  )  (  nGchBasesCalledConfidently  )  ; 
} 

double   percentMethyLevelOfCch  (  )  { 
return  (  sumMethyCchBasesCalledConfidently  )  /  (  double  )  (  nCchBasesCalledConfidently  )  ; 
} 

double   percentMethyLevelOfWch  (  )  { 
return  (  sumMethyWchBasesCalledConfidently  )  /  (  double  )  (  nWchBasesCalledConfidently  )  ; 
} 

double   percentMethyLevelOfGcg  (  )  { 
return  (  sumMethyGcgBasesCalledConfidently  )  /  (  double  )  (  nGcgBasesCalledConfidently  )  ; 
} 

double   percentMethyLevelOfCcg  (  )  { 
return  (  sumMethyCcgBasesCalledConfidently  )  /  (  double  )  (  nCcgBasesCalledConfidently  )  ; 
} 

double   percentMethyLevelOfWcg  (  )  { 
return  (  sumMethyWcgBasesCalledConfidently  )  /  (  double  )  (  nWcgBasesCalledConfidently  )  ; 
} 

HashMap  <  String  ,  Double  [  ]  >  otherCytosine  =  new   HashMap  <  String  ,  Double  [  ]  >  (  )  ; 

void   makeOtherCytosineMap  (  )  { 
if  (  !  BAC  .  forceOtherCytosine  .  isEmpty  (  )  )  { 
String  [  ]  tmpArray  =  BAC  .  forceOtherCytosine  .  split  (  ";"  )  ; 
for  (  String   tmp  :  tmpArray  )  { 
String  [  ]  tmp2  =  tmp  .  split  (  ":"  )  ; 
String   key  =  tmp2  [  0  ]  .  toUpperCase  (  )  ; 
Double  [  ]  value  =  new   Double  [  2  ]  ; 
value  [  0  ]  =  0.0  ; 
value  [  1  ]  =  0.0  ; 
otherCytosine  .  put  (  key  ,  value  )  ; 
} 
}  else   if  (  !  BAC  .  autoEstimateOtherCytosine  .  isEmpty  (  )  )  { 
String  [  ]  tmpArray  =  BAC  .  autoEstimateOtherCytosine  .  split  (  ";"  )  ; 
for  (  String   tmp  :  tmpArray  )  { 
String  [  ]  tmp2  =  tmp  .  split  (  ":"  )  ; 
String   key  =  tmp2  [  0  ]  .  toUpperCase  (  )  ; 
Double  [  ]  value  =  new   Double  [  2  ]  ; 
value  [  0  ]  =  0.0  ; 
value  [  1  ]  =  0.0  ; 
otherCytosine  .  put  (  key  ,  value  )  ; 
} 
}  else  { 
} 
} 
} 





public   void   initialize  (  )  { 
Set  <  String  >  samples  =  new   TreeSet  <  String  >  (  )  ; 
if  (  BAC  .  ASSUME_SINGLE_SAMPLE  !=  null  )  { 
samples  =  SampleUtils  .  getSAMFileSamples  (  getToolkit  (  )  .  getSAMFileHeader  (  )  )  ; 
if  (  !  samples  .  isEmpty  (  )  )  { 
System  .  out  .  println  (  "sample name provided was masked by bam file header"  )  ; 
}  else  { 
samples  .  add  (  BAC  .  ASSUME_SINGLE_SAMPLE  )  ; 
} 
}  else  { 
samples  =  SampleUtils  .  getSAMFileSamples  (  getToolkit  (  )  .  getSAMFileHeader  (  )  )  ; 
System  .  out  .  println  (  "samples provided: "  +  samples  .  toString  (  )  )  ; 
if  (  samples  .  isEmpty  (  )  )  { 
System  .  err  .  println  (  "No sample name provided, program will automately provide the bam file header: "  +  samples  .  toString  (  )  )  ; 
} 
} 
BG_engine  =  new   BisulfiteGenotyperEngine  (  getToolkit  (  )  ,  BAC  ,  logger  ,  samples  )  ; 
SAMSequenceDictionary   refDict  =  getToolkit  (  )  .  getMasterSequenceDictionary  (  )  ; 
if  (  autoEstimateC  )  { 
if  (  secondIteration  )  { 
File   outputVcfFile  =  new   File  (  BAC  .  vfn1  )  ; 
writer  =  new   TcgaVCFWriter  (  outputVcfFile  ,  refDict  ,  false  )  ; 
writer  .  setRefSource  (  getToolkit  (  )  .  getArguments  (  )  .  referenceFile  .  toString  (  )  )  ; 
writer  .  writeHeader  (  new   VCFHeader  (  getHeaderInfo  (  )  ,  samples  )  )  ; 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  =  new   SortingTcgaVCFWriter  (  writer  ,  MAXIMUM_CACHE_FOR_OUTPUT_VCF  )  ; 
multiThreadWriter  .  enableDiscreteLoci  (  BAC  .  lnc  )  ; 
if  (  BAC  .  ovd  )  { 
File   outputVerboseFile  =  new   File  (  BAC  .  fnovd  )  ; 
verboseWriter  =  new   TcgaVCFWriter  (  outputVerboseFile  ,  refDict  ,  false  )  ; 
verboseWriter  .  writeHeader  (  new   VCFHeader  (  getHeaderInfo  (  )  ,  samples  )  )  ; 
} 
} 
if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  DEFAULT_FOR_TCGA  )  { 
File   outputAdditionalVcfFile  =  new   File  (  BAC  .  vfn2  )  ; 
additionalWriterForDefaultTcgaMode  =  new   TcgaVCFWriter  (  outputAdditionalVcfFile  ,  refDict  ,  false  )  ; 
additionalWriterForDefaultTcgaMode  .  writeHeader  (  new   VCFHeader  (  getHeaderInfo  (  )  ,  samples  )  )  ; 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiAdditionalWriterForDefaultTcgaMode  =  new   SortingTcgaVCFWriter  (  additionalWriterForDefaultTcgaMode  ,  MAXIMUM_CACHE_FOR_OUTPUT_VCF  )  ; 
multiAdditionalWriterForDefaultTcgaMode  .  enableDiscreteLoci  (  BAC  .  lnc  )  ; 
} 
} 
if  (  BAC  .  orad  )  { 
File   outputBamFile  =  new   File  (  BAC  .  fnorad  )  ; 
SAMFileWriterFactory   samFileWriterFactory  =  new   SAMFileWriterFactory  (  )  ; 
samFileWriterFactory  .  setCreateIndex  (  true  )  ; 
samWriter  =  samFileWriterFactory  .  makeBAMWriter  (  getToolkit  (  )  .  getSAMFileHeader  (  )  ,  false  ,  outputBamFile  )  ; 
} 
if  (  BAC  .  fnocrd  !=  null  )  { 
File   outputReadsDetailFile  =  new   File  (  BAC  .  fnocrd  )  ; 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
readsWriter  =  new   NOMeSeqReadsWriterImp  (  outputReadsDetailFile  )  ; 
}  else  { 
readsWriter  =  new   cpgReadsWriterImp  (  outputReadsDetailFile  )  ; 
} 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
multiThreadCpgReadsWriter  =  new   SortingNOMeSeqReadsWriter  (  readsWriter  ,  MAXIMUM_CACHE_FOR_OUTPUT_READS  )  ; 
}  else  { 
multiThreadCpgReadsWriter  =  new   SortingCpgReadsWriter  (  readsWriter  ,  MAXIMUM_CACHE_FOR_OUTPUT_READS  )  ; 
} 
multiThreadCpgReadsWriter  .  writeHeader  (  true  )  ; 
}  else  { 
readsWriter  .  addHeader  (  true  )  ; 
} 
} 
}  else  { 
} 
}  else  { 
File   outputVcfFile  =  new   File  (  BAC  .  vfn1  )  ; 
writer  =  new   TcgaVCFWriter  (  outputVcfFile  ,  refDict  ,  false  )  ; 
writer  .  setRefSource  (  getToolkit  (  )  .  getArguments  (  )  .  referenceFile  .  toString  (  )  )  ; 
writer  .  writeHeader  (  new   VCFHeader  (  getHeaderInfo  (  )  ,  samples  )  )  ; 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  =  new   SortingTcgaVCFWriter  (  writer  ,  MAXIMUM_CACHE_FOR_OUTPUT_VCF  )  ; 
multiThreadWriter  .  enableDiscreteLoci  (  BAC  .  lnc  )  ; 
} 
if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  DEFAULT_FOR_TCGA  )  { 
File   outputAdditionalVcfFile  =  new   File  (  BAC  .  vfn2  )  ; 
additionalWriterForDefaultTcgaMode  =  new   TcgaVCFWriter  (  outputAdditionalVcfFile  ,  refDict  ,  false  )  ; 
additionalWriterForDefaultTcgaMode  .  writeHeader  (  new   VCFHeader  (  getHeaderInfo  (  )  ,  samples  )  )  ; 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiAdditionalWriterForDefaultTcgaMode  =  new   SortingTcgaVCFWriter  (  additionalWriterForDefaultTcgaMode  ,  MAXIMUM_CACHE_FOR_OUTPUT_VCF  )  ; 
multiAdditionalWriterForDefaultTcgaMode  .  enableDiscreteLoci  (  BAC  .  lnc  )  ; 
} 
} 
if  (  BAC  .  orad  )  { 
File   outputBamFile  =  new   File  (  BAC  .  fnorad  )  ; 
SAMFileWriterFactory   samFileWriterFactory  =  new   SAMFileWriterFactory  (  )  ; 
samFileWriterFactory  .  setCreateIndex  (  true  )  ; 
samWriter  =  samFileWriterFactory  .  makeBAMWriter  (  getToolkit  (  )  .  getSAMFileHeader  (  )  ,  false  ,  outputBamFile  )  ; 
} 
if  (  BAC  .  fnocrd  !=  null  )  { 
File   outputReadsDetailFile  =  new   File  (  BAC  .  fnocrd  )  ; 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
readsWriter  =  new   NOMeSeqReadsWriterImp  (  outputReadsDetailFile  )  ; 
}  else  { 
readsWriter  =  new   cpgReadsWriterImp  (  outputReadsDetailFile  )  ; 
} 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
multiThreadCpgReadsWriter  =  new   SortingNOMeSeqReadsWriter  (  readsWriter  ,  MAXIMUM_CACHE_FOR_OUTPUT_READS  )  ; 
}  else  { 
multiThreadCpgReadsWriter  =  new   SortingCpgReadsWriter  (  readsWriter  ,  MAXIMUM_CACHE_FOR_OUTPUT_READS  )  ; 
} 
multiThreadCpgReadsWriter  .  writeHeader  (  true  )  ; 
}  else  { 
readsWriter  .  addHeader  (  true  )  ; 
} 
} 
} 
if  (  !  secondIteration  )  summary  =  new   CytosineTypeStatus  (  BAC  )  ; 
} 





private   Set  <  VCFHeaderLine  >  getHeaderInfo  (  )  { 
Set  <  VCFHeaderLine  >  headerInfo  =  new   HashSet  <  VCFHeaderLine  >  (  )  ; 
if  (  !  BAC  .  NO_SLOD  )  headerInfo  .  add  (  new   VCFInfoHeaderLine  (  VCFConstants  .  STRAND_BIAS_KEY  ,  1  ,  VCFHeaderLineType  .  Float  ,  "Strand Bias"  )  )  ; 
headerInfo  .  add  (  new   VCFInfoHeaderLine  (  BisulfiteVCFConstants  .  CYTOSINE_TYPE  ,  -  1  ,  VCFHeaderLineType  .  String  ,  "Cytosine Type"  )  )  ; 
headerInfo  .  add  (  new   VCFInfoHeaderLine  (  BisulfiteVCFConstants  .  GENOTYPE_TYPE  ,  1  ,  VCFHeaderLineType  .  String  ,  "Genotype Type"  )  )  ; 
headerInfo  .  add  (  new   VCFInfoHeaderLine  (  BisulfiteVCFConstants  .  C_STRAND_KEY  ,  1  ,  VCFHeaderLineType  .  String  ,  "Cytosine in negative strand"  )  )  ; 
headerInfo  .  add  (  new   VCFInfoHeaderLine  (  BisulfiteVCFConstants  .  NUMBER_OF_C_KEY  ,  1  ,  VCFHeaderLineType  .  Integer  ,  "number of C in this Cytosine position"  )  )  ; 
headerInfo  .  add  (  new   VCFInfoHeaderLine  (  BisulfiteVCFConstants  .  NUMBER_OF_T_KEY  ,  1  ,  VCFHeaderLineType  .  Integer  ,  "number of T in this Cytosine position"  )  )  ; 
headerInfo  .  add  (  new   VCFInfoHeaderLine  (  BisulfiteVCFConstants  .  CYTOSINE_METHY_VALUE  ,  1  ,  VCFHeaderLineType  .  Float  ,  "Methylation value in this Cytosine position"  )  )  ; 
if  (  BAC  .  dbsnp  .  isBound  (  )  )  headerInfo  .  add  (  new   VCFInfoHeaderLine  (  VCFConstants  .  DBSNP_KEY  ,  0  ,  VCFHeaderLineType  .  Flag  ,  "dbSNP Membership"  )  )  ; 
if  (  BAC  .  STANDARD_CONFIDENCE_FOR_EMITTING  <  BAC  .  STANDARD_CONFIDENCE_FOR_CALLING  )  headerInfo  .  add  (  new   VCFFilterHeaderLine  (  UnifiedGenotyperEngine  .  LOW_QUAL_FILTER_NAME  ,  "Low quality"  )  )  ; 
headerInfo  .  add  (  new   VCFHeaderLine  (  BisulfiteVCFConstants  .  PROGRAM_ARGS  ,  argCommandline  )  )  ; 
return   headerInfo  ; 
} 









public   BisulfiteVariantCallContext   map  (  RefMetaDataTracker   tracker  ,  ReferenceContext   refContext  ,  AlignmentContext   rawContext  )  { 
CytosineTypeStatus   cts  =  null  ; 
if  (  secondIteration  )  { 
cts  =  summary  .  clone  (  )  ; 
}  else  { 
cts  =  new   CytosineTypeStatus  (  BAC  )  ; 
if  (  BAC  .  orad  )  downsamplingBamFile  (  rawContext  )  ; 
} 
BG_engine  .  setAutoParameters  (  autoEstimateC  ,  secondIteration  )  ; 
BG_engine  .  setCytosineTypeStatus  (  cts  )  ; 
return   BG_engine  .  calculateLikelihoodsAndGenotypes  (  tracker  ,  refContext  ,  rawContext  )  ; 
} 




public   ContextCondition   reduceInit  (  )  { 
ContextCondition   initiated  =  new   ContextCondition  (  )  ; 
initiated  .  makeOtherCytosineMap  (  )  ; 
return   initiated  ; 
} 

public   ContextCondition   treeReduce  (  ContextCondition   lhs  ,  ContextCondition   rhs  )  { 
lhs  .  nBasesCallable  +=  rhs  .  nBasesCallable  ; 
lhs  .  nBasesCalledConfidently  +=  rhs  .  nBasesCalledConfidently  ; 
lhs  .  nBasesVisited  +=  rhs  .  nBasesVisited  ; 
lhs  .  nCallsMade  +=  rhs  .  nCallsMade  ; 
lhs  .  nCytosineBasesCalledConfidently  +=  rhs  .  nCytosineBasesCalledConfidently  ; 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  BM  )  { 
lhs  .  nCpgBasesCalledConfidently  +=  rhs  .  nCpgBasesCalledConfidently  ; 
lhs  .  nCphBasesCalledConfidently  +=  rhs  .  nCphBasesCalledConfidently  ; 
lhs  .  sumMethyCpgBasesCalledConfidently  +=  rhs  .  sumMethyCpgBasesCalledConfidently  ; 
lhs  .  sumMethyCphBasesCalledConfidently  +=  rhs  .  sumMethyCphBasesCalledConfidently  ; 
}  else   if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
lhs  .  nGchBasesCalledConfidently  +=  rhs  .  nGchBasesCalledConfidently  ; 
lhs  .  nCchBasesCalledConfidently  +=  rhs  .  nCchBasesCalledConfidently  ; 
lhs  .  nWchBasesCalledConfidently  +=  rhs  .  nWchBasesCalledConfidently  ; 
lhs  .  nGcgBasesCalledConfidently  +=  rhs  .  nGcgBasesCalledConfidently  ; 
lhs  .  nCcgBasesCalledConfidently  +=  rhs  .  nCcgBasesCalledConfidently  ; 
lhs  .  nWcgBasesCalledConfidently  +=  rhs  .  nWcgBasesCalledConfidently  ; 
lhs  .  sumMethyGchBasesCalledConfidently  +=  rhs  .  sumMethyGchBasesCalledConfidently  ; 
lhs  .  sumMethyCchBasesCalledConfidently  +=  rhs  .  sumMethyCchBasesCalledConfidently  ; 
lhs  .  sumMethyWchBasesCalledConfidently  +=  rhs  .  sumMethyWchBasesCalledConfidently  ; 
lhs  .  sumMethyGcgBasesCalledConfidently  +=  rhs  .  sumMethyGcgBasesCalledConfidently  ; 
lhs  .  sumMethyCcgBasesCalledConfidently  +=  rhs  .  sumMethyCcgBasesCalledConfidently  ; 
lhs  .  sumMethyWcgBasesCalledConfidently  +=  rhs  .  sumMethyWcgBasesCalledConfidently  ; 
} 
lhs  .  sumMethyCytosineBasesCalledConfidently  +=  rhs  .  sumMethyCytosineBasesCalledConfidently  ; 
if  (  !  rhs  .  otherCytosine  .  isEmpty  (  )  )  { 
for  (  String   key  :  rhs  .  otherCytosine  .  keySet  (  )  )  { 
Double  [  ]  rhsValue  =  rhs  .  otherCytosine  .  get  (  key  )  ; 
Double  [  ]  lhsValue  =  new   Double  [  2  ]  ; 
if  (  lhs  .  otherCytosine  .  containsKey  (  key  )  )  { 
lhsValue  =  lhs  .  otherCytosine  .  get  (  key  )  ; 
lhsValue  [  0  ]  +=  rhsValue  [  0  ]  ; 
lhsValue  [  1  ]  +=  rhsValue  [  1  ]  ; 
lhs  .  otherCytosine  .  put  (  key  ,  lhsValue  )  ; 
} 
} 
} 
return   lhs  ; 
} 




public   ContextCondition   reduce  (  BisulfiteVariantCallContext   value  ,  ContextCondition   sum  )  { 
sum  .  nBasesVisited  ++  ; 
if  (  value  ==  null  )  return   sum  ; 
System  .  err  .  println  (  value  .  vc  .  getGenotypes  (  )  .  size  (  )  +  "\t"  +  value  .  vc  .  getGenotypes  (  )  .  getSampleNames  (  )  )  ; 
sum  .  nBasesCallable  ++  ; 
sum  .  nBasesCalledConfidently  +=  value  .  confidentlyCalled  ?  1  :  0  ; 
CytosineTypeStatus   ctsFirst  =  value  .  cts  .  get  (  value  .  cts  .  keySet  (  )  .  toArray  (  )  [  0  ]  )  ; 
sum  .  nCytosineBasesCalledConfidently  +=  ctsFirst  .  isC  ?  1  :  0  ; 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  BM  )  { 
sum  .  nCpgBasesCalledConfidently  +=  ctsFirst  .  isCpg  ?  1  :  0  ; 
sum  .  nCphBasesCalledConfidently  +=  ctsFirst  .  isCph  ?  1  :  0  ; 
sum  .  sumMethyCpgBasesCalledConfidently  +=  ctsFirst  .  isCpg  ?  ctsFirst  .  cpgMethyLevel  :  0  ; 
sum  .  sumMethyCphBasesCalledConfidently  +=  ctsFirst  .  isCph  ?  ctsFirst  .  cphMethyLevel  :  0  ; 
}  else   if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
sum  .  nGchBasesCalledConfidently  +=  ctsFirst  .  isGch  ?  1  :  0  ; 
sum  .  nCchBasesCalledConfidently  +=  ctsFirst  .  isCch  ?  1  :  0  ; 
sum  .  nWchBasesCalledConfidently  +=  ctsFirst  .  isWch  ?  1  :  0  ; 
sum  .  nGcgBasesCalledConfidently  +=  ctsFirst  .  isGcg  ?  1  :  0  ; 
sum  .  nCcgBasesCalledConfidently  +=  ctsFirst  .  isCcg  ?  1  :  0  ; 
sum  .  nWcgBasesCalledConfidently  +=  ctsFirst  .  isWcg  ?  1  :  0  ; 
sum  .  sumMethyGchBasesCalledConfidently  +=  ctsFirst  .  isGch  ?  ctsFirst  .  gchMethyLevel  :  0  ; 
sum  .  sumMethyCchBasesCalledConfidently  +=  ctsFirst  .  isCch  ?  ctsFirst  .  cchMethyLevel  :  0  ; 
sum  .  sumMethyWchBasesCalledConfidently  +=  ctsFirst  .  isWch  ?  ctsFirst  .  wchMethyLevel  :  0  ; 
sum  .  sumMethyGcgBasesCalledConfidently  +=  ctsFirst  .  isGcg  ?  ctsFirst  .  gcgMethyLevel  :  0  ; 
sum  .  sumMethyCcgBasesCalledConfidently  +=  ctsFirst  .  isCcg  ?  ctsFirst  .  ccgMethyLevel  :  0  ; 
sum  .  sumMethyWcgBasesCalledConfidently  +=  ctsFirst  .  isWcg  ?  ctsFirst  .  wcgMethyLevel  :  0  ; 
} 
sum  .  sumMethyCytosineBasesCalledConfidently  +=  ctsFirst  .  isC  ?  ctsFirst  .  cytosineMethyLevel  :  0  ; 
if  (  !  BAC  .  forceOtherCytosine  .  isEmpty  (  )  ||  !  BAC  .  autoEstimateOtherCytosine  .  isEmpty  (  )  )  { 
for  (  String   key  :  sum  .  otherCytosine  .  keySet  (  )  )  { 
if  (  ctsFirst  .  cytosineListMap  .  containsKey  (  key  )  )  { 
Double  [  ]  cytosineStatus  =  ctsFirst  .  cytosineListMap  .  get  (  key  )  ; 
Double  [  ]  values  =  sum  .  otherCytosine  .  get  (  key  )  ; 
values  [  0  ]  +=  Double  .  compare  (  cytosineStatus  [  3  ]  ,  1.0  )  ==  0  ?  1  :  0  ; 
values  [  1  ]  +=  Double  .  compare  (  cytosineStatus  [  3  ]  ,  1.0  )  ==  0  ?  cytosineStatus  [  2  ]  :  0  ; 
sum  .  otherCytosine  .  put  (  key  ,  values  )  ; 
} 
} 
} 
if  (  value  .  vc  ==  null  )  return   sum  ; 
try  { 
sum  .  nCallsMade  ++  ; 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
if  (  BAC  .  fnocrd  !=  null  &&  (  (  autoEstimateC  &&  secondIteration  )  ||  (  !  autoEstimateC  &&  !  secondIteration  )  )  )  { 
boolean   positiveStrand  =  true  ; 
String   sampleContext  =  ""  ; 
for  (  String   key  :  ctsFirst  .  cytosineListMap  .  keySet  (  )  )  { 
if  (  key  .  equalsIgnoreCase  (  "C-1"  )  )  { 
continue  ; 
} 
Double  [  ]  cytosineStatus  =  ctsFirst  .  cytosineListMap  .  get  (  key  )  ; 
if  (  Double  .  compare  (  cytosineStatus  [  3  ]  ,  1.0  )  ==  0  )  { 
if  (  cytosineStatus  [  0  ]  >  cytosineStatus  [  1  ]  )  { 
positiveStrand  =  true  ; 
}  else  { 
positiveStrand  =  false  ; 
} 
String  [  ]  tmpKey  =  key  .  split  (  "-"  )  ; 
if  (  sampleContext  .  equals  (  ""  )  )  { 
sampleContext  =  tmpKey  [  0  ]  ; 
}  else  { 
sampleContext  =  sampleContext  +  ","  +  tmpKey  [  0  ]  ; 
} 
} 
} 
if  (  !  sampleContext  .  equals  (  ""  )  )  { 
readsDetailReportForNOMeSeq  (  value  .  rawContext  ,  positiveStrand  ,  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  ,  sampleContext  ,  value  .  refBase  )  ; 
} 
} 
}  else  { 
if  (  ctsFirst  .  isCpg  )  { 
if  (  BAC  .  fnocrd  !=  null  &&  (  (  autoEstimateC  &&  secondIteration  )  ||  (  !  autoEstimateC  &&  !  secondIteration  )  )  )  { 
if  (  ctsFirst  .  cytosineListMap  .  get  (  "CG-1"  )  [  0  ]  >  ctsFirst  .  cytosineListMap  .  get  (  "CG-1"  )  [  1  ]  )  { 
readsDetailReport  (  value  .  rawContext  ,  true  ,  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  ,  value  .  refBase  )  ; 
}  else  { 
readsDetailReport  (  value  .  rawContext  ,  false  ,  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  ,  value  .  refBase  )  ; 
} 
} 
} 
} 
if  (  autoEstimateC  )  { 
if  (  secondIteration  )  { 
if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  EMIT_ALL_CYTOSINES  )  { 
if  (  ctsFirst  .  isC  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
}  else   if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  EMIT_ALL_CPG  )  { 
if  (  ctsFirst  .  isCpg  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
}  else   if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  EMIT_VARIANTS_ONLY  )  { 
if  (  value  .  isVariant  (  )  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
}  else   if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  EMIT_HET_SNPS_ONLY  )  { 
if  (  value  .  isHetSnp  (  )  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
}  else   if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  DEFAULT_FOR_TCGA  )  { 
if  (  ctsFirst  .  isCpg  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
if  (  value  .  isVariant  (  )  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiAdditionalWriterForDefaultTcgaMode  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiAdditionalWriterForDefaultTcgaMode  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
additionalWriterForDefaultTcgaMode  .  add  (  value  .  vc  )  ; 
} 
} 
}  else  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
if  (  BAC  .  ovd  )  { 
verboseWriter  .  add  (  value  .  vc  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
} 
}  else  { 
if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  EMIT_ALL_CYTOSINES  )  { 
if  (  ctsFirst  .  isC  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
}  else   if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  EMIT_ALL_CPG  )  { 
if  (  ctsFirst  .  isCpg  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
}  else   if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  EMIT_VARIANTS_ONLY  )  { 
if  (  value  .  isVariant  (  )  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
}  else   if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  EMIT_HET_SNPS_ONLY  )  { 
if  (  value  .  isHetSnp  (  )  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
}  else   if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  DEFAULT_FOR_TCGA  )  { 
if  (  ctsFirst  .  isCpg  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
if  (  value  .  isVariant  (  )  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiAdditionalWriterForDefaultTcgaMode  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiAdditionalWriterForDefaultTcgaMode  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
additionalWriterForDefaultTcgaMode  .  add  (  value  .  vc  )  ; 
} 
} 
}  else  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadWriter  .  add  (  value  .  vc  ,  value  .  refBase  .  getBase  (  )  )  ; 
COUNT_CACHE_FOR_OUTPUT_VCF  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_VCF  %  MAXIMUM_CACHE_FOR_OUTPUT_VCF  ==  0  )  { 
multiThreadWriter  .  writerFlush  (  )  ; 
System  .  gc  (  )  ; 
} 
}  else  { 
writer  .  add  (  value  .  vc  )  ; 
} 
} 
} 
}  catch  (  IllegalArgumentException   e  )  { 
throw   new   IllegalArgumentException  (  e  .  getMessage  (  )  +  "; this is often caused by using the --assume_single_sample_reads argument with the wrong sample name"  )  ; 
} 
return   sum  ; 
} 

public   void   onTraversalDone  (  ContextCondition   sum  )  { 
String   fn  =  BAC  .  vfn1  +  ".BisSNPMethySummarizeList.txt"  ; 
PrintWriter   outWriter  =  null  ; 
String   outLine  =  null  ; 
try  { 
outWriter  =  new   PrintWriter  (  new   File  (  fn  )  )  ; 
}  catch  (  FileNotFoundException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
logger  .  info  (  String  .  format  (  "Visited bases                                %d"  ,  sum  .  nBasesVisited  )  )  ; 
logger  .  info  (  String  .  format  (  "Callable bases                               %d"  ,  sum  .  nBasesCallable  )  )  ; 
logger  .  info  (  String  .  format  (  "Confidently called bases                     %d"  ,  sum  .  nBasesCalledConfidently  )  )  ; 
logger  .  info  (  String  .  format  (  "%% callable bases of all loci                 %3.3f"  ,  sum  .  percentCallableOfAll  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% confidently called bases of all loci       %3.3f"  ,  sum  .  percentCalledOfAll  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% confidently called bases of callable loci  %3.3f"  ,  sum  .  percentCalledOfCallable  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "Actual calls made                            %d"  ,  sum  .  nCallsMade  )  )  ; 
logger  .  info  (  String  .  format  (  "%% Methylation level of Cytosine loci       %3.3f"  ,  sum  .  percentMethyLevelOfC  (  )  )  )  ; 
outLine  =  "C-1:"  +  sum  .  percentMethyLevelOfC  (  )  +  "\t"  +  sum  .  nCytosineBasesCalledConfidently  +  "\n"  ; 
logger  .  info  (  String  .  format  (  "%% number of Cytosine loci       %d"  ,  sum  .  nCytosineBasesCalledConfidently  )  )  ; 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  BM  )  { 
logger  .  info  (  String  .  format  (  "%% Methylation level of CpG loci       %3.3f"  ,  sum  .  percentMethyLevelOfCpg  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of CpG loci       %d"  ,  sum  .  nCpgBasesCalledConfidently  )  )  ; 
logger  .  info  (  String  .  format  (  "%% Methylation level of CpH loci       %3.3f"  ,  sum  .  percentMethyLevelOfCph  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of CpH loci       %d"  ,  sum  .  nCphBasesCalledConfidently  )  )  ; 
outLine  =  outLine  +  "CG-1:"  +  sum  .  percentMethyLevelOfCpg  (  )  +  "\t"  +  sum  .  nCpgBasesCalledConfidently  +  "\n"  ; 
outLine  =  outLine  +  "CH-1:"  +  sum  .  percentMethyLevelOfCph  (  )  +  "\t"  +  sum  .  nCphBasesCalledConfidently  +  "\n"  ; 
}  else   if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
logger  .  info  (  String  .  format  (  "%% Methylation level of GCH loci       %3.3f"  ,  sum  .  percentMethyLevelOfGch  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of GCH loci       %d"  ,  sum  .  nGchBasesCalledConfidently  )  )  ; 
logger  .  info  (  String  .  format  (  "%% Methylation level of CCH loci       %3.3f"  ,  sum  .  percentMethyLevelOfCch  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of CCH loci       %d"  ,  sum  .  nCchBasesCalledConfidently  )  )  ; 
logger  .  info  (  String  .  format  (  "%% Methylation level of WCH loci       %3.3f"  ,  sum  .  percentMethyLevelOfWch  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of WCH loci       %d"  ,  sum  .  nWchBasesCalledConfidently  )  )  ; 
logger  .  info  (  String  .  format  (  "%% Methylation level of WCG loci       %3.3f"  ,  sum  .  percentMethyLevelOfWcg  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of WCG loci       %d"  ,  sum  .  nWcgBasesCalledConfidently  )  )  ; 
logger  .  info  (  String  .  format  (  "%% Methylation level of CCG loci       %3.3f"  ,  sum  .  percentMethyLevelOfCcg  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of CCG loci       %d"  ,  sum  .  nCcgBasesCalledConfidently  )  )  ; 
logger  .  info  (  String  .  format  (  "%% Methylation level of GCG loci       %3.3f"  ,  sum  .  percentMethyLevelOfGcg  (  )  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of GCG loci       %d"  ,  sum  .  nGcgBasesCalledConfidently  )  )  ; 
outLine  =  outLine  +  "GCH-2:"  +  sum  .  percentMethyLevelOfGch  (  )  +  "\t"  +  sum  .  nGchBasesCalledConfidently  +  "\n"  ; 
outLine  =  outLine  +  "CCH-2:"  +  sum  .  percentMethyLevelOfCch  (  )  +  "\t"  +  sum  .  nCchBasesCalledConfidently  +  "\n"  ; 
outLine  =  outLine  +  "WCH-2:"  +  sum  .  percentMethyLevelOfWch  (  )  +  "\t"  +  sum  .  nWchBasesCalledConfidently  +  "\n"  ; 
outLine  =  outLine  +  "WCG-2:"  +  sum  .  percentMethyLevelOfWcg  (  )  +  "\t"  +  sum  .  nWcgBasesCalledConfidently  +  "\n"  ; 
outLine  =  outLine  +  "CCG-2:"  +  sum  .  percentMethyLevelOfCcg  (  )  +  "\t"  +  sum  .  nCcgBasesCalledConfidently  +  "\n"  ; 
outLine  =  outLine  +  "GCG-2:"  +  sum  .  percentMethyLevelOfGcg  (  )  +  "\t"  +  sum  .  nGcgBasesCalledConfidently  +  "\n"  ; 
} 
if  (  !  BAC  .  forceOtherCytosine  .  isEmpty  (  )  ||  !  BAC  .  autoEstimateOtherCytosine  .  isEmpty  (  )  )  { 
for  (  String   key  :  sum  .  otherCytosine  .  keySet  (  )  )  { 
Double  [  ]  values  =  sum  .  otherCytosine  .  get  (  key  )  ; 
String  [  ]  tmp  =  key  .  split  (  "-"  )  ; 
String   name  =  tmp  [  0  ]  ; 
logger  .  info  (  String  .  format  (  "%% Methylation level of %s loci       %3.3f"  ,  name  ,  values  [  1  ]  /  values  [  0  ]  )  )  ; 
logger  .  info  (  String  .  format  (  "%% number of %s loci       %d"  ,  name  ,  values  [  0  ]  .  intValue  (  )  )  )  ; 
outLine  =  outLine  +  key  +  ":"  +  values  [  1  ]  /  values  [  0  ]  +  "\t"  +  values  [  0  ]  .  intValue  (  )  +  "\n"  ; 
} 
} 
summary  .  cytosineMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfC  (  )  )  ?  0  :  sum  .  percentMethyLevelOfC  (  )  ; 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  BM  )  { 
summary  .  cpgMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfCpg  (  )  )  ?  0  :  sum  .  percentMethyLevelOfCpg  (  )  ; 
summary  .  cphMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfCph  (  )  )  ?  0  :  sum  .  percentMethyLevelOfCph  (  )  ; 
}  else   if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
summary  .  gchMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfGch  (  )  )  ?  0  :  sum  .  percentMethyLevelOfGch  (  )  ; 
summary  .  cchMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfCch  (  )  )  ?  0  :  sum  .  percentMethyLevelOfCch  (  )  ; 
summary  .  wchMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfWch  (  )  )  ?  0  :  sum  .  percentMethyLevelOfWch  (  )  ; 
summary  .  gcgMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfGcg  (  )  )  ?  0  :  sum  .  percentMethyLevelOfGcg  (  )  ; 
summary  .  ccgMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfCcg  (  )  )  ?  0  :  sum  .  percentMethyLevelOfCcg  (  )  ; 
summary  .  wcgMethyLevel  =  Double  .  isNaN  (  sum  .  percentMethyLevelOfWcg  (  )  )  ?  0  :  sum  .  percentMethyLevelOfWcg  (  )  ; 
} 
for  (  String   cytosineType  :  summary  .  cytosineListMap  .  keySet  (  )  )  { 
String  [  ]  tmpKey  =  cytosineType  .  split  (  "-"  )  ; 
Double  [  ]  value  =  summary  .  cytosineListMap  .  get  (  cytosineType  )  ; 
if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "C"  )  )  { 
value  [  2  ]  =  summary  .  cytosineMethyLevel  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "CG"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateCpg  ?  summary  .  cpgMethyLevel  :  BAC  .  forceCpg  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "CH"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateCph  ?  summary  .  cphMethyLevel  :  BAC  .  forceCph  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "GCH"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateGch  ?  summary  .  gchMethyLevel  :  BAC  .  forceGch  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "CCH"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateCch  ?  summary  .  cchMethyLevel  :  BAC  .  forceCch  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "WCH"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateWch  ?  summary  .  wchMethyLevel  :  BAC  .  forceWch  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "GCG"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateGcg  ?  summary  .  gcgMethyLevel  :  BAC  .  forceGcg  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "CCG"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateCcg  ?  summary  .  ccgMethyLevel  :  BAC  .  forceCcg  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "WCG"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateWcg  ?  summary  .  wcgMethyLevel  :  BAC  .  forceWcg  ; 
}  else  { 
if  (  !  BAC  .  autoEstimateOtherCytosine  .  isEmpty  (  )  )  { 
String  [  ]  tmpArray  =  BAC  .  autoEstimateOtherCytosine  .  split  (  ";"  )  ; 
for  (  String   tmp  :  tmpArray  )  { 
String  [  ]  key  =  tmp  .  split  (  ":"  )  ; 
if  (  key  [  0  ]  .  equalsIgnoreCase  (  cytosineType  )  )  { 
Double  [  ]  summaryStat  =  sum  .  otherCytosine  .  get  (  cytosineType  )  ; 
value  [  2  ]  =  summaryStat  [  1  ]  /  summaryStat  [  0  ]  ; 
} 
} 
} 
if  (  !  BAC  .  forceOtherCytosine  .  isEmpty  (  )  )  { 
String  [  ]  tmpArray  =  BAC  .  forceOtherCytosine  .  split  (  ";"  )  ; 
for  (  String   tmp  :  tmpArray  )  { 
String  [  ]  key  =  tmp  .  split  (  ":"  )  ; 
if  (  key  [  0  ]  .  equalsIgnoreCase  (  cytosineType  )  )  { 
value  [  2  ]  =  Double  .  parseDouble  (  key  [  1  ]  )  ; 
} 
} 
} 
} 
if  (  value  [  2  ]  .  isNaN  (  )  )  value  [  2  ]  =  0.0  ; 
summary  .  cytosineListMap  .  put  (  cytosineType  ,  value  )  ; 
} 
if  (  BAC  .  orad  )  { 
samWriter  .  close  (  )  ; 
} 
if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  DEFAULT_FOR_TCGA  )  { 
} 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  &&  (  (  autoEstimateC  &&  secondIteration  )  ||  (  !  autoEstimateC  &&  !  secondIteration  )  )  )  { 
multiThreadWriter  .  close  (  )  ; 
if  (  BAC  .  OutputMode  ==  BisulfiteGenotyperEngine  .  OUTPUT_MODE  .  DEFAULT_FOR_TCGA  )  { 
multiAdditionalWriterForDefaultTcgaMode  .  close  (  )  ; 
} 
} 
if  (  BAC  .  fnocrd  !=  null  )  { 
if  (  (  autoEstimateC  &&  secondIteration  )  ||  (  !  autoEstimateC  &&  !  secondIteration  )  )  { 
if  (  getToolkit  (  )  .  getArguments  (  )  .  numberOfThreads  >  1  )  { 
multiThreadCpgReadsWriter  .  close  (  )  ; 
}  else  { 
readsWriter  .  close  (  )  ; 
} 
} 
} 
outWriter  .  println  (  outLine  )  ; 
outWriter  .  close  (  )  ; 
} 

public   void   setCytosineMethyStatus  (  CytosineTypeStatus   sumCytosine  )  { 
summary  =  sumCytosine  .  clone  (  )  ; 
if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  BM  )  { 
BAC  .  forceCpg  =  BAC  .  autoEstimateCpg  ?  sumCytosine  .  cpgMethyLevel  :  BAC  .  forceCpg  ; 
BAC  .  forceCph  =  BAC  .  autoEstimateCph  ?  sumCytosine  .  cphMethyLevel  :  BAC  .  forceCph  ; 
}  else   if  (  BAC  .  sequencingMode  ==  MethylSNPModel  .  GM  )  { 
BAC  .  forceGch  =  BAC  .  autoEstimateGch  ?  sumCytosine  .  gchMethyLevel  :  BAC  .  forceGch  ; 
BAC  .  forceCch  =  BAC  .  autoEstimateCch  ?  sumCytosine  .  cchMethyLevel  :  BAC  .  forceCch  ; 
BAC  .  forceWch  =  BAC  .  autoEstimateWch  ?  sumCytosine  .  wchMethyLevel  :  BAC  .  forceWch  ; 
BAC  .  forceGcg  =  BAC  .  autoEstimateGcg  ?  sumCytosine  .  gcgMethyLevel  :  BAC  .  forceGcg  ; 
BAC  .  forceCcg  =  BAC  .  autoEstimateCcg  ?  sumCytosine  .  ccgMethyLevel  :  BAC  .  forceCcg  ; 
BAC  .  forceWcg  =  BAC  .  autoEstimateWcg  ?  sumCytosine  .  wcgMethyLevel  :  BAC  .  forceWcg  ; 
} 
for  (  String   cytosineType  :  summary  .  cytosineListMap  .  keySet  (  )  )  { 
String  [  ]  tmpKey  =  cytosineType  .  split  (  "-"  )  ; 
Double  [  ]  value  =  summary  .  cytosineListMap  .  get  (  cytosineType  )  ; 
if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "C"  )  )  { 
value  [  2  ]  =  sumCytosine  .  cytosineMethyLevel  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "CG"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateCpg  ?  sumCytosine  .  cpgMethyLevel  :  BAC  .  forceCpg  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "CH"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateCph  ?  summary  .  cphMethyLevel  :  BAC  .  forceCph  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "GCH"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateGch  ?  sumCytosine  .  gchMethyLevel  :  BAC  .  forceGch  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "CCH"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateCch  ?  sumCytosine  .  cchMethyLevel  :  BAC  .  forceCch  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "WCH"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateWch  ?  sumCytosine  .  wchMethyLevel  :  BAC  .  forceWch  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "GCG"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateGcg  ?  sumCytosine  .  gcgMethyLevel  :  BAC  .  forceGcg  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "CCG"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateCcg  ?  sumCytosine  .  ccgMethyLevel  :  BAC  .  forceCcg  ; 
}  else   if  (  tmpKey  [  0  ]  .  equalsIgnoreCase  (  "WCG"  )  )  { 
value  [  2  ]  =  BAC  .  autoEstimateWcg  ?  sumCytosine  .  wcgMethyLevel  :  BAC  .  forceWcg  ; 
}  else  { 
if  (  !  BAC  .  forceOtherCytosine  .  isEmpty  (  )  )  { 
String  [  ]  tmpArray  =  BAC  .  forceOtherCytosine  .  split  (  ";"  )  ; 
for  (  String   tmp  :  tmpArray  )  { 
String  [  ]  key  =  tmp  .  split  (  ":"  )  ; 
if  (  key  [  0  ]  .  equalsIgnoreCase  (  cytosineType  )  )  { 
value  [  2  ]  =  Double  .  parseDouble  (  key  [  1  ]  )  ; 
} 
} 
} 
} 
if  (  value  [  2  ]  .  isNaN  (  )  )  value  [  2  ]  =  0.0  ; 
summary  .  cytosineListMap  .  put  (  cytosineType  ,  value  )  ; 
} 
} 

public   void   setAutoParameters  (  boolean   autoEstimateC  ,  boolean   secondIteration  ,  String   argCommandline  )  { 
this  .  autoEstimateC  =  autoEstimateC  ; 
this  .  secondIteration  =  secondIteration  ; 
this  .  argCommandline  =  argCommandline  ; 
} 

public   CytosineTypeStatus   getCytosineMethyStatus  (  )  { 
return   summary  .  clone  (  )  ; 
} 

public   TcgaVCFWriter   getWriter  (  )  { 
return   writer  ; 
} 

public   void   setWriter  (  TcgaVCFWriter   writer  )  { 
this  .  writer  =  writer  ; 
} 

public   void   downsamplingBamFile  (  AlignmentContext   rawContext  )  { 
if  (  rawContext  .  hasReads  (  )  )  { 
String   tag  =  "Xi"  ; 
Integer   coverageMarked  =  0  ; 
int   covergaeLimit  =  BAC  .  orcad  ; 
covergaeLimit  =  Math  .  max  (  (  covergaeLimit  *  rawContext  .  getBasePileup  (  )  .  depthOfCoverage  (  )  )  /  SAMPLE_READS_MEAN_COVERAGE  ,  1  )  ; 
ReadBackedPileup   downsampledPileup  =  BisSNPUtils  .  getDownsampledPileup  (  rawContext  .  getBasePileup  (  )  ,  covergaeLimit  )  ; 
for  (  PileupElement   p  :  rawContext  .  getBasePileup  (  )  )  { 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  !=  null  )  { 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  ==  2  )  if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  ==  1  )  coverageMarked  ++  ; 
} 
} 
for  (  PileupElement   p  :  downsampledPileup  )  { 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  !=  null  )  { 
} 
if  (  coverageMarked  >=  covergaeLimit  )  break  ; 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  ==  null  )  { 
samWriter  .  addAlignment  (  p  .  getRead  (  )  )  ; 
p  .  getRead  (  )  .  setAttribute  (  tag  ,  1  )  ; 
coverageMarked  ++  ; 
} 
} 
for  (  PileupElement   p  :  rawContext  .  getBasePileup  (  )  )  { 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  ==  null  )  p  .  getRead  (  )  .  setAttribute  (  tag  ,  2  )  ; 
} 
} 
} 

public   void   downsamplingBamFileLikeGATK  (  AlignmentContext   rawContext  )  { 
if  (  rawContext  .  hasReads  (  )  )  { 
String   tag  =  "Xi"  ; 
Integer   coverageMarked  =  0  ; 
int   covergaeLimit  =  getToolkit  (  )  .  getArguments  (  )  .  downsampleCoverage  ; 
ReadBackedPileup   downsampledPileup  =  rawContext  .  getBasePileup  (  )  .  getDownsampledPileup  (  covergaeLimit  )  ; 
for  (  PileupElement   p  :  rawContext  .  getBasePileup  (  )  )  { 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  !=  null  )  { 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  ==  2  )  System  .  out  .  println  (  "loc: "  +  rawContext  .  getLocation  (  )  .  getStart  (  )  +  " tag: "  +  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  )  ; 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  ==  1  )  coverageMarked  ++  ; 
} 
} 
for  (  PileupElement   p  :  downsampledPileup  )  { 
if  (  coverageMarked  >=  covergaeLimit  )  break  ; 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  ==  null  )  { 
samWriter  .  addAlignment  (  p  .  getRead  (  )  )  ; 
p  .  getRead  (  )  .  setAttribute  (  tag  ,  1  )  ; 
coverageMarked  ++  ; 
} 
} 
for  (  PileupElement   p  :  rawContext  .  getBasePileup  (  )  )  { 
if  (  p  .  getRead  (  )  .  getIntegerAttribute  (  tag  )  ==  null  )  p  .  getRead  (  )  .  setAttribute  (  tag  ,  2  )  ; 
} 
} 
} 

public   void   readsDetailReport  (  AlignmentContext   rawContext  ,  boolean   posStrand  ,  boolean   multiThread  ,  ReferenceContext   refContext  )  { 
if  (  rawContext  .  hasReads  (  )  )  { 
for  (  PileupElement   p  :  rawContext  .  getBasePileup  (  )  )  { 
GATKSAMRecordFilterStorage   GATKrecordFilterStor  =  new   GATKSAMRecordFilterStorage  (  p  .  getRead  (  )  ,  refContext  ,  BAC  )  ; 
if  (  !  GATKrecordFilterStor  .  isGoodBase  (  p  .  getOffset  (  )  )  )  { 
continue  ; 
} 
char   strand  ; 
if  (  p  .  getRead  (  )  .  getReadNegativeStrandFlag  (  )  )  { 
strand  =  '-'  ; 
if  (  !  posStrand  )  { 
char   methyStatus  ; 
if  (  p  .  getBase  (  )  ==  BaseUtilsMore  .  G  )  { 
methyStatus  =  'm'  ; 
cpgReads   cr  =  new   cpgReads  (  rawContext  .  getContig  (  )  ,  rawContext  .  getLocation  (  )  .  getStart  (  )  ,  methyStatus  ,  p  .  getQual  (  )  ,  strand  ,  p  .  getRead  (  )  .  getReadName  (  )  )  ; 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  add  (  cr  )  ; 
}  else  { 
readsWriter  .  add  (  cr  )  ; 
} 
}  else   if  (  p  .  getBase  (  )  ==  BaseUtilsMore  .  A  )  { 
methyStatus  =  'u'  ; 
cpgReads   cr  =  new   cpgReads  (  rawContext  .  getContig  (  )  ,  rawContext  .  getLocation  (  )  .  getStart  (  )  ,  methyStatus  ,  p  .  getQual  (  )  ,  strand  ,  p  .  getRead  (  )  .  getReadName  (  )  )  ; 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  add  (  cr  )  ; 
}  else  { 
readsWriter  .  add  (  cr  )  ; 
} 
} 
} 
}  else  { 
strand  =  '+'  ; 
if  (  posStrand  )  { 
char   methyStatus  ; 
if  (  p  .  getBase  (  )  ==  BaseUtilsMore  .  C  )  { 
methyStatus  =  'm'  ; 
cpgReads   cr  =  new   cpgReads  (  rawContext  .  getContig  (  )  ,  rawContext  .  getLocation  (  )  .  getStart  (  )  ,  methyStatus  ,  p  .  getQual  (  )  ,  strand  ,  p  .  getRead  (  )  .  getReadName  (  )  )  ; 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  add  (  cr  )  ; 
}  else  { 
readsWriter  .  add  (  cr  )  ; 
} 
}  else   if  (  p  .  getBase  (  )  ==  BaseUtilsMore  .  T  )  { 
methyStatus  =  'u'  ; 
cpgReads   cr  =  new   cpgReads  (  rawContext  .  getContig  (  )  ,  rawContext  .  getLocation  (  )  .  getStart  (  )  ,  methyStatus  ,  p  .  getQual  (  )  ,  strand  ,  p  .  getRead  (  )  .  getReadName  (  )  )  ; 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  add  (  cr  )  ; 
}  else  { 
readsWriter  .  add  (  cr  )  ; 
} 
} 
} 
} 
} 
} 
} 

public   void   readsDetailReportForNOMeSeq  (  AlignmentContext   rawContext  ,  boolean   posStrand  ,  boolean   multiThread  ,  String   sampleContext  ,  ReferenceContext   refContext  )  { 
if  (  rawContext  .  hasReads  (  )  )  { 
String   ref  =  ""  ; 
if  (  posStrand  )  { 
ref  =  ref  +  BaseUtilsMore  .  convertByteToString  (  refContext  .  getBase  (  )  )  ; 
}  else  { 
ref  =  ref  +  BaseUtilsMore  .  convertByteToString  (  BaseUtilsMore  .  iupacCodeComplement  (  refContext  .  getBase  (  )  )  )  ; 
} 
GenomeLoc   locPre  =  refContext  .  getGenomeLocParser  (  )  .  createGenomeLoc  (  refContext  .  getLocus  (  )  .  getContig  (  )  ,  refContext  .  getLocus  (  )  .  getStart  (  )  -  1  )  ; 
GenomeLoc   locPost  =  refContext  .  getGenomeLocParser  (  )  .  createGenomeLoc  (  refContext  .  getLocus  (  )  .  getContig  (  )  ,  refContext  .  getLocus  (  )  .  getStart  (  )  +  1  )  ; 
if  (  refContext  .  getWindow  (  )  .  containsP  (  locPre  )  )  { 
ReferenceContext   tmpRef  =  new   ReferenceContext  (  refContext  .  getGenomeLocParser  (  )  ,  locPre  ,  refContext  .  getWindow  (  )  ,  refContext  .  getBases  (  )  )  ; 
if  (  posStrand  )  { 
ref  =  BaseUtilsMore  .  convertByteToString  (  BaseUtilsMore  .  toIupacCodeNOMeSeqMode  (  tmpRef  .  getBase  (  )  ,  1  )  )  +  ref  ; 
}  else  { 
ref  =  ref  +  BaseUtilsMore  .  convertByteToString  (  BaseUtilsMore  .  toIupacCodeNOMeSeqMode  (  BaseUtilsMore  .  iupacCodeComplement  (  tmpRef  .  getBase  (  )  )  ,  3  )  )  ; 
} 
} 
if  (  refContext  .  getWindow  (  )  .  containsP  (  locPost  )  )  { 
ReferenceContext   tmpRef  =  new   ReferenceContext  (  refContext  .  getGenomeLocParser  (  )  ,  locPost  ,  refContext  .  getWindow  (  )  ,  refContext  .  getBases  (  )  )  ; 
if  (  posStrand  )  { 
ref  =  ref  +  BaseUtilsMore  .  convertByteToString  (  BaseUtilsMore  .  toIupacCodeNOMeSeqMode  (  tmpRef  .  getBase  (  )  ,  3  )  )  ; 
}  else  { 
ref  =  BaseUtilsMore  .  convertByteToString  (  BaseUtilsMore  .  toIupacCodeNOMeSeqMode  (  BaseUtilsMore  .  iupacCodeComplement  (  tmpRef  .  getBase  (  )  )  ,  1  )  )  +  ref  ; 
} 
} 
for  (  PileupElement   p  :  rawContext  .  getBasePileup  (  )  )  { 
GATKSAMRecordFilterStorage   GATKrecordFilterStor  =  new   GATKSAMRecordFilterStorage  (  p  .  getRead  (  )  ,  refContext  ,  BAC  )  ; 
if  (  !  GATKrecordFilterStor  .  isGoodBase  (  p  .  getOffset  (  )  )  )  { 
continue  ; 
} 
char   strand  ; 
COUNT_CACHE_FOR_OUTPUT_READS  ++  ; 
if  (  COUNT_CACHE_FOR_OUTPUT_READS  %  MAXIMUM_CACHE_FOR_OUTPUT_READS  ==  0  )  { 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  writerFlush  (  )  ; 
}  else  { 
readsWriter  .  writerFlush  (  )  ; 
} 
System  .  gc  (  )  ; 
} 
if  (  p  .  getRead  (  )  .  getReadNegativeStrandFlag  (  )  )  { 
strand  =  '-'  ; 
if  (  !  posStrand  )  { 
char   methyStatus  ; 
if  (  p  .  getBase  (  )  ==  BaseUtilsMore  .  G  )  { 
methyStatus  =  'm'  ; 
NOMeSeqReads   cr  =  new   NOMeSeqReads  (  rawContext  .  getContig  (  )  ,  rawContext  .  getLocation  (  )  .  getStart  (  )  ,  sampleContext  ,  ref  ,  methyStatus  ,  p  .  getQual  (  )  ,  strand  ,  p  .  getRead  (  )  .  getReadName  (  )  )  ; 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  add  (  cr  )  ; 
}  else  { 
(  (  NOMeSeqReadsWriterImp  )  readsWriter  )  .  add  (  cr  )  ; 
} 
}  else   if  (  p  .  getBase  (  )  ==  BaseUtilsMore  .  A  )  { 
methyStatus  =  'u'  ; 
NOMeSeqReads   cr  =  new   NOMeSeqReads  (  rawContext  .  getContig  (  )  ,  rawContext  .  getLocation  (  )  .  getStart  (  )  ,  sampleContext  ,  ref  ,  methyStatus  ,  p  .  getQual  (  )  ,  strand  ,  p  .  getRead  (  )  .  getReadName  (  )  )  ; 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  add  (  cr  )  ; 
}  else  { 
readsWriter  .  add  (  cr  )  ; 
} 
} 
} 
}  else  { 
strand  =  '+'  ; 
if  (  posStrand  )  { 
char   methyStatus  ; 
if  (  p  .  getBase  (  )  ==  BaseUtilsMore  .  C  )  { 
methyStatus  =  'm'  ; 
NOMeSeqReads   cr  =  new   NOMeSeqReads  (  rawContext  .  getContig  (  )  ,  rawContext  .  getLocation  (  )  .  getStart  (  )  ,  sampleContext  ,  ref  ,  methyStatus  ,  p  .  getQual  (  )  ,  strand  ,  p  .  getRead  (  )  .  getReadName  (  )  )  ; 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  add  (  cr  )  ; 
}  else  { 
readsWriter  .  add  (  cr  )  ; 
} 
}  else   if  (  p  .  getBase  (  )  ==  BaseUtilsMore  .  T  )  { 
methyStatus  =  'u'  ; 
NOMeSeqReads   cr  =  new   NOMeSeqReads  (  rawContext  .  getContig  (  )  ,  rawContext  .  getLocation  (  )  .  getStart  (  )  ,  sampleContext  ,  ref  ,  methyStatus  ,  p  .  getQual  (  )  ,  strand  ,  p  .  getRead  (  )  .  getReadName  (  )  )  ; 
if  (  multiThread  )  { 
multiThreadCpgReadsWriter  .  add  (  cr  )  ; 
}  else  { 
readsWriter  .  add  (  cr  )  ; 
} 
} 
} 
} 
} 
} 
} 

public   static   BisulfiteArgumentCollection   getBAC  (  )  { 
return   BAC  ; 
} 
} 

