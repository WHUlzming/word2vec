package   uk  .  ac  .  wlv  .  clg  .  nlp  .  temporalannotator  ; 

import   java  .  util  .  *  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  regex  .  *  ; 
import   java  .  util  .  Hashtable  ; 

public   class   TimePoint  { 

public   Hashtable  <  String  ,  String  >  htblVariables  ; 

public   List  <  String  >  toFind_ResultList  ; 

public   TimePoint  (  )  { 
htblVariables  =  new   Hashtable  <  String  ,  String  >  (  )  ; 
toFind_ResultList  =  null  ; 
} 

public   void   set  (  String   sHashKey  ,  String   sExpression  )  { 
if  (  sExpression  ==  null  )  { 
htblVariables  .  put  (  sHashKey  ,  ""  )  ; 
}  else  { 
htblVariables  .  put  (  sHashKey  ,  sExpression  )  ; 
} 
} 

public   String   get  (  String   sHashKey  )  { 
if  (  htblVariables  .  containsKey  (  sHashKey  )  )  { 
return   htblVariables  .  get  (  sHashKey  )  ; 
} 
return  ""  ; 
} 

public   String   displayLabel  (  boolean   bTimexTag  )  { 
String   sRez  ; 
if  (  bTimexTag  )  { 
sRez  =  "<TIMEX2"  ; 
}  else  { 
sRez  =  ""  ; 
} 
if  (  htblVariables  .  containsKey  (  "function"  )  )  { 
String   sFunctionPattern  =  "^(DOW|M|Y|D|H)=([^,]*)(,.*$|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sFunctionPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  htblVariables  .  get  (  "function"  )  .  toString  (  )  )  ; 
String  [  ]  arrsMatch  =  new   String  [  3  ]  ; 
arrsMatch  [  0  ]  =  ""  ; 
arrsMatch  [  1  ]  =  ""  ; 
arrsMatch  [  2  ]  =  ""  ; 
while  (  m  .  find  (  )  )  { 
if  (  m  .  start  (  1  )  <  m  .  end  (  1  )  )  { 
arrsMatch  [  0  ]  =  m  .  group  (  1  )  ; 
}  else  { 
break  ; 
} 
if  (  m  .  start  (  2  )  <  m  .  end  (  2  )  )  { 
arrsMatch  [  1  ]  =  m  .  group  (  2  )  ; 
}  else  { 
break  ; 
} 
if  (  m  .  start  (  3  )  <  m  .  end  (  3  )  )  { 
arrsMatch  [  2  ]  =  m  .  group  (  3  )  ; 
}  else  { 
} 
if  (  arrsMatch  [  0  ]  .  compareTo  (  "DOW"  )  ==  0  )  { 
this  .  set  (  "dayOfWeek"  ,  arrsMatch  [  1  ]  )  ; 
}  else   if  (  arrsMatch  [  0  ]  .  compareTo  (  "M"  )  ==  0  )  { 
this  .  set  (  "month"  ,  arrsMatch  [  1  ]  )  ; 
}  else   if  (  arrsMatch  [  0  ]  .  compareTo  (  "Y"  )  ==  0  )  { 
this  .  set  (  "year"  ,  arrsMatch  [  1  ]  )  ; 
}  else   if  (  arrsMatch  [  0  ]  .  compareTo  (  "D"  )  ==  0  )  { 
this  .  set  (  "dayOfMonth"  ,  arrsMatch  [  1  ]  )  ; 
}  else  { 
this  .  set  (  "hour"  ,  arrsMatch  [  1  ]  )  ; 
} 
String   sPattern3  =  "^,([^,]*$)"  ; 
Pattern   pattern3  =  Pattern  .  compile  (  sPattern3  )  ; 
Matcher   m3  =  pattern3  .  matcher  (  arrsMatch  [  2  ]  )  ; 
if  (  m3  .  matches  (  )  )  { 
this  .  set  (  "function"  ,  arrsMatch  [  0  ]  )  ; 
}  else  { 
this  .  set  (  "function"  ,  ""  )  ; 
} 
} 
} 
String   sPatternKey  =  "dayOfMonth|week|month|hour|minute|second|miliSec"  ; 
Pattern   patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
Vector  <  String  >  v  =  new   Vector  <  String  >  (  htblVariables  .  keySet  (  )  )  ; 
Collections  .  sort  (  v  )  ; 
for  (  Enumeration  <  String  >  e  =  v  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   sKey  =  e  .  nextElement  (  )  ; 
Matcher   mKey  =  patternKey  .  matcher  (  sKey  )  ; 
if  (  mKey  .  matches  (  )  )  { 
Pattern   pattern2  =  Pattern  .  compile  (  "^(\\d)$"  )  ; 
String   sTemp  =  htblVariables  .  get  (  sKey  )  ; 
Matcher   m2  =  pattern2  .  matcher  (  sTemp  )  ; 
if  (  m2  .  matches  (  )  )  { 
sTemp  =  "0"  +  sTemp  ; 
htblVariables  .  put  (  sKey  ,  sTemp  )  ; 
} 
} 
if  (  (  htblVariables  .  get  (  sKey  )  .  length  (  )  >  0  )  &&  (  sKey  .  compareTo  (  "text"  )  !=  0  )  &&  (  sKey  .  compareTo  (  "toFind"  )  !=  0  )  )  { 
if  (  (  sKey  .  compareTo  (  "val"  )  !=  0  )  )  { 
sRez  +=  " "  +  sKey  +  "=\""  +  htblVariables  .  get  (  sKey  )  +  "\""  ; 
}  else  { 
String   sNewVal  =  htblVariables  .  get  (  sKey  )  .  replaceAll  (  "-9"  ,  "X"  )  ; 
sRez  +=  " "  +  sKey  +  "=\""  +  sNewVal  +  "\""  ; 
} 
} 
} 
if  (  htblVariables  .  containsKey  (  "timeZone"  )  )  { 
Pattern   patternTimeZone  =  Pattern  .  compile  (  "^((\\+|-)(\\d\\d))(:(\\d\\d))?$"  )  ; 
String   sTemp  =  htblVariables  .  get  (  "timeZone"  )  ; 
Matcher   m2  =  patternTimeZone  .  matcher  (  sTemp  )  ; 
if  (  m2  .  matches  (  )  )  { 
String   sHourWithSign  =  m2  .  group  (  1  )  ; 
String   sMin  =  m2  .  group  (  5  )  ; 
if  (  toFind_ResultList  ==  null  )  { 
toFind_ResultList  =  new   ArrayList  <  String  >  (  )  ; 
} 
toFind_ResultList  .  add  (  "H"  )  ; 
toFind_ResultList  .  add  (  sHourWithSign  )  ; 
if  (  sMin  !=  null  )  { 
toFind_ResultList  .  add  (  "MIN"  )  ; 
toFind_ResultList  .  add  (  sMin  )  ; 
} 
} 
} 
if  (  toFind_ResultList  !=  null  )  { 
sRez  +=  " toFind=\""  ; 
for  (  int   i  =  0  ;  i  <  toFind_ResultList  .  size  (  )  ;  i  ++  )  { 
sRez  +=  toFind_ResultList  .  get  (  i  )  +  " "  ; 
} 
sRez  +=  "\""  ; 
} 
if  (  bTimexTag  )  { 
sRez  +=  ">"  ; 
} 
return   sRez  ; 
} 

public   String   displayText  (  )  { 
return   htblVariables  .  get  (  "text"  )  ; 
} 

public   String   displayWithoutTimexTag  (  )  { 
String   sRez  =  displayLabel  (  false  )  ; 
return   sRez  .  trim  (  )  ; 
} 

public   String   display  (  )  { 
String   sRez  =  displayLabel  (  true  )  ; 
sRez  +=  htblVariables  .  get  (  "text"  )  +  "</TIMEX2>"  ; 
return   sRez  ; 
} 

public   static   List  <  TimePoint  >  copyArray  (  List  <  TimePoint  >  tmpRanges  )  { 
List  <  TimePoint  >  rangeTimePoint  =  new   ArrayList  <  TimePoint  >  (  )  ; 
for  (  int   i  =  0  ;  i  <  tmpRanges  .  size  (  )  ;  i  ++  )  { 
TimePoint   tp  =  new   TimePoint  (  )  ; 
tp  .  copy  (  tmpRanges  .  get  (  i  )  )  ; 
rangeTimePoint  .  add  (  tp  )  ; 
} 
return   rangeTimePoint  ; 
} 

public   void   copy  (  TimePoint   tp  )  { 
htblVariables  =  new   Hashtable  <  String  ,  String  >  (  tp  .  htblVariables  )  ; 
} 

public   void   clear  (  )  { 
htblVariables  .  clear  (  )  ; 
} 

public   TimePoint   interpretTimex  (  )  { 
TimePoint   newTP  =  new   TimePoint  (  )  ; 
newTP  .  set  (  "text"  ,  htblVariables  .  get  (  "text"  )  )  ; 
String   sX  =  ""  ; 
Hashtable  <  String  ,  String  >  htblMapVal  =  new   Hashtable  <  String  ,  String  >  (  )  ; 
htblMapVal  .  put  (  "HALFH"  ,  "30MIN"  )  ; 
htblMapVal  .  put  (  "THIRDH"  ,  "20MIN"  )  ; 
htblMapVal  .  put  (  "QUARTH"  ,  "15MIN"  )  ; 
htblMapVal  .  put  (  "HALFY"  ,  "6M"  )  ; 
htblMapVal  .  put  (  "THIRDY"  ,  "4M"  )  ; 
htblMapVal  .  put  (  "QUARTY"  ,  "3M"  )  ; 
String   sPatternKey  ; 
Pattern   patternKey  ; 
Matcher   mKey  ; 
if  (  htblVariables  .  containsKey  (  "val"  )  )  { 
String   sHashVal  =  htblVariables  .  get  (  "val"  )  ; 
if  (  sHashVal  .  compareTo  (  "predecessor"  )  ==  0  )  { 
newTP  .  set  (  "val"  ,  "PAST_REF"  )  ; 
newTP  .  set  (  "resolved"  ,  "1"  )  ; 
} 
sX  =  sHashVal  ; 
sPatternKey  =  "PAST_REF|PRESENT_REF|FUTURE_REF"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sHashVal  )  ; 
if  (  mKey  .  find  (  )  )  { 
newTP  .  set  (  "resolved"  ,  "1"  )  ; 
newTP  .  set  (  "val"  ,  sX  )  ; 
} 
sPatternKey  =  "(\\d)?\\+?1(HALF|THIRD|QUART)(H|Y)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sX  )  ; 
if  (  mKey  .  find  (  )  )  { 
if  (  (  mKey  .  group  (  1  )  !=  null  )  &&  mKey  .  group  (  1  )  .  length  (  )  >  0  )  { 
sX  =  mKey  .  group  (  1  )  +  mKey  .  group  (  3  )  +  ":"  ; 
}  else  { 
sX  =  ""  ; 
} 
sX  +=  htblMapVal  .  get  (  mKey  .  group  (  2  )  +  mKey  .  group  (  3  )  )  ; 
if  (  mKey  .  group  (  3  )  .  compareTo  (  "H"  )  ==  0  )  { 
newTP  .  set  (  "granularity"  ,  "MIN"  )  ; 
}  else  { 
newTP  .  set  (  "granularity"  ,  "M"  )  ; 
} 
} 
sPatternKey  =  "(SE|D|W|M|Y|Q|E|C|L)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sX  )  ; 
if  (  mKey  .  find  (  )  )  { 
newTP  .  set  (  "val"  ,  "P"  +  sX  )  ; 
newTP  .  set  (  "type"  ,  "DURATION"  )  ; 
newTP  .  set  (  "resolved"  ,  "1"  )  ; 
newTP  .  set  (  "granularity"  ,  getGran  (  mKey  .  group  (  1  )  )  )  ; 
}  else  { 
sPatternKey  =  "(MIN|S|H)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sX  )  ; 
if  (  mKey  .  find  (  )  )  { 
newTP  .  set  (  "val"  ,  "PT"  +  sX  )  ; 
newTP  .  set  (  "type"  ,  "DURATION"  )  ; 
newTP  .  set  (  "resolved"  ,  "1"  )  ; 
newTP  .  set  (  "granularity"  ,  getGran  (  mKey  .  group  (  1  )  )  )  ; 
} 
} 
newTP  .  set  (  "function"  ,  ""  )  ; 
newTP  .  set  (  "mod"  ,  get  (  "mod"  )  )  ; 
return   newTP  ; 
} 
String   sVal  =  ""  ; 
@  SuppressWarnings  (  "unused"  )  String   sResolved  =  "0"  ; 
@  SuppressWarnings  (  "unused"  )  String   sFully_Det  =  "0"  ; 
List  <  String  >  lstRezultat  =  null  ; 
String   sGrann  =  "0"  ; 
if  (  htblVariables  .  containsKey  (  "year"  )  ||  htblVariables  .  containsKey  (  "month"  )  ||  htblVariables  .  containsKey  (  "dayOfMonth"  )  ||  htblVariables  .  containsKey  (  "dayOfWeek"  )  )  { 
if  (  containsNotEmpty  (  "year"  )  )  { 
String   sYear  =  htblVariables  .  get  (  "year"  )  ; 
sVal  +=  sYear  +  "-"  ; 
sGrann  =  "Y"  ; 
}  else  { 
sVal  +=  "XXXX-"  ; 
lstRezultat  =  Arrays  .  asList  (  "Y"  ,  "+0"  )  ; 
} 
if  (  containsNotEmpty  (  "month"  )  )  { 
sVal  +=  htblVariables  .  get  (  "month"  )  +  "-"  ; 
sGrann  =  "M"  ; 
}  else  { 
if  (  containsNotEmpty  (  "dayOfMonth"  )  )  { 
sVal  +=  "XX-"  ; 
lstRezultat  =  Arrays  .  asList  (  "M"  ,  "+0"  )  ; 
}  else  { 
if  (  containsNotEmpty  (  "dayOfWeek"  )  )  { 
sVal  +=  "WXX-"  ; 
lstRezultat  =  new   ArrayList  <  String  >  (  )  ; 
lstRezultat  .  add  (  "W"  )  ; 
lstRezultat  .  add  (  "+0"  )  ; 
String   sDow  =  htblVariables  .  get  (  "dayOfWeek"  )  ; 
{ 
try  { 
lstRezultat  .  add  (  "DOW"  )  ; 
lstRezultat  .  add  (  sDow  )  ; 
}  catch  (  Exception   e  )  { 
System  .  out  .  println  (  e  .  toString  (  )  )  ; 
} 
} 
} 
} 
} 
if  (  containsNotEmpty  (  "dayOfMonth"  )  )  { 
sVal  +=  htblVariables  .  get  (  "dayOfMonth"  )  ; 
sGrann  =  "D"  ; 
}  else  { 
if  (  containsNotEmpty  (  "dayOfWeek"  )  )  { 
if  (  lstRezultat  .  size  (  )  >  2  )  { 
sGrann  =  "DOW"  ; 
sVal  +=  htblVariables  .  get  (  "dayOfWeek"  )  ; 
}  else  { 
sVal  +=  htblVariables  .  get  (  "dayOfWeek"  )  ; 
sGrann  =  "D"  ; 
} 
} 
} 
sVal  =  sVal  .  replaceAll  (  "\\-+$"  ,  ""  )  ; 
}  else  { 
if  (  htblVariables  .  containsKey  (  "special"  )  &&  (  htblVariables  .  get  (  "special"  )  .  compareTo  (  "1"  )  ==  0  )  )  { 
lstRezultat  =  Arrays  .  asList  (  "Y"  ,  "+0"  )  ; 
sGrann  =  "D"  ; 
newTP  .  set  (  "special"  ,  "1"  )  ; 
} 
} 
if  (  htblVariables  .  containsKey  (  "hour"  )  ||  htblVariables  .  containsKey  (  "minute"  )  ||  htblVariables  .  containsKey  (  "second"  )  )  { 
sVal  +=  "T"  ; 
boolean   bHourIsNumber  =  false  ; 
if  (  containsNotEmpty  (  "hour"  )  )  { 
sVal  +=  htblVariables  .  get  (  "hour"  )  +  ":"  ; 
sGrann  =  "H"  ; 
if  (  lstRezultat  ==  null  )  { 
lstRezultat  =  Arrays  .  asList  (  "D"  ,  "+0"  )  ; 
} 
try  { 
@  SuppressWarnings  (  "unused"  )  int   iHour  =  Integer  .  parseInt  (  htblVariables  .  get  (  "hour"  )  )  ; 
bHourIsNumber  =  true  ; 
}  catch  (  Exception   e  )  { 
} 
} 
if  (  containsNotEmpty  (  "minute"  )  )  { 
sVal  +=  htblVariables  .  get  (  "minute"  )  +  ":"  ; 
sGrann  =  "MIN"  ; 
if  (  lstRezultat  ==  null  )  { 
lstRezultat  =  Arrays  .  asList  (  "H"  ,  "+0"  )  ; 
} 
}  else  { 
if  (  bHourIsNumber  )  { 
sVal  +=  "00"  +  ":"  ; 
} 
} 
if  (  containsNotEmpty  (  "second"  )  )  { 
sVal  +=  htblVariables  .  get  (  "second"  )  ; 
sGrann  =  "S"  ; 
if  (  lstRezultat  ==  null  )  { 
lstRezultat  =  Arrays  .  asList  (  "MIN"  ,  "+0"  )  ; 
} 
}  else  { 
if  (  bHourIsNumber  )  { 
sVal  +=  "00"  +  ":"  ; 
} 
} 
sVal  =  sVal  .  replaceAll  (  ":+$"  ,  ""  )  ; 
} 
newTP  .  set  (  "val"  ,  sVal  )  ; 
newTP  .  set  (  "mod"  ,  htblVariables  .  get  (  "mod"  )  !=  null  ?  htblVariables  .  get  (  "mod"  )  :  ""  )  ; 
newTP  .  set  (  "granularity"  ,  sGrann  )  ; 
if  (  (  lstRezultat  !=  null  )  ||  (  (  lstRezultat  ==  null  )  &&  htblVariables  .  containsKey  (  "function"  )  &&  htblVariables  .  get  (  "function"  )  .  length  (  )  >  0  )  )  { 
if  (  lstRezultat  !=  null  )  { 
newTP  .  toFind  (  lstRezultat  )  ; 
} 
newTP  .  set  (  "resolved"  ,  "0"  )  ; 
}  else  { 
newTP  .  set  (  "resolved"  ,  "1"  )  ; 
newTP  .  set  (  "fullySpec"  ,  "1"  )  ; 
} 
if  (  containsNotEmpty  (  "function"  )  )  { 
String   sFct  =  htblVariables  .  get  (  "function"  )  ; 
sPatternKey  =  "plural|repeat"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  find  (  )  )  { 
newTP  .  set  (  "set"  ,  "YES"  )  ; 
newTP  .  set  (  "resolved"  ,  "1"  )  ; 
sPatternKey  =  "^plural\\((SE|D|W|M|Y|Q|E|C|L)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "PX"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "granularity"  ,  getGran  (  mKey  .  group  (  1  )  )  )  ; 
}  else  { 
sPatternKey  =  "^plural\\((H|MIN|S)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "PTX"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "granularity"  ,  getGran  (  mKey  .  group  (  1  )  )  )  ; 
}  else  { 
sPatternKey  =  "^plural\\((MO|AF|EV|NI)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "PTX"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "granularity"  ,  "H"  )  ; 
}  else  { 
sPatternKey  =  "^plural\\(DOW=(\\d)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "XXXX-WXX-"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "granularity"  ,  "D"  )  ; 
}  else  { 
sPatternKey  =  "^plural\\(H=(.*)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "XXXX-XX-XXT"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "granularity"  ,  "H"  )  ; 
}  else  { 
sPatternKey  =  "^repeat\\((SE|D|W|M|Y|Q|E|C|L|H|MIN|S)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "periodicity"  ,  "F1"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "granularity"  ,  getGran  (  mKey  .  group  (  1  )  )  )  ; 
}  else  { 
sPatternKey  =  "^repeat\\(DOW=(\\d)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "XXXX-WXX-"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "periodicity"  ,  "F1W"  )  ; 
newTP  .  set  (  "granularity"  ,  "D"  )  ; 
}  else  { 
sPatternKey  =  "^repeat\\(M=(\\d+),D=(\\d+)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "XXXX-"  +  mKey  .  group  (  1  )  +  "-"  +  mKey  .  group  (  2  )  )  ; 
newTP  .  set  (  "periodicity"  ,  "F1Y"  )  ; 
newTP  .  set  (  "granularity"  ,  "D"  )  ; 
}  else  { 
sPatternKey  =  "^repeat\\(M=(\\d+)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "XXXX-"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "periodicity"  ,  "F1Y"  )  ; 
newTP  .  set  (  "granularity"  ,  "M"  )  ; 
}  else  { 
sPatternKey  =  "^repeat\\(H=(.+)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
newTP  .  set  (  "val"  ,  "T"  +  mKey  .  group  (  1  )  )  ; 
newTP  .  set  (  "periodicity"  ,  "F1D"  )  ; 
newTP  .  set  (  "granularity"  ,  "H"  )  ; 
} 
} 
} 
} 
} 
} 
} 
} 
} 
} 
}  else  { 
sPatternKey  =  "^((predecessor|successor)\\([^\\[\\]]*\\)|[^\\(\\)\\[\\]]+)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
List  <  String  >  sTempEval  =  evalFunction  (  sFct  ,  0  )  ; 
newTP  .  set  (  "granularity"  ,  getGran  (  sTempEval  .  get  (  sTempEval  .  size  (  )  -  2  )  )  )  ; 
newTP  .  toFind  (  sTempEval  )  ; 
newTP  .  set  (  "resolved"  ,  "0"  )  ; 
}  else  { 
sPatternKey  =  "^\\(?(SE|D|W|M|Y|Q|E|C|L|H|MIN|S)\\)?$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
lstRezultat  =  Arrays  .  asList  (  mKey  .  group  (  1  )  ,  "+0"  )  ; 
newTP  .  set  (  "granularity"  ,  getGran  (  mKey  .  group  (  1  )  )  )  ; 
newTP  .  toFind  (  lstRezultat  )  ; 
newTP  .  set  (  "resolved"  ,  "0"  )  ; 
} 
} 
} 
} 
return   newTP  ; 
} 

public   void   toFind  (  List  <  String  >  lstRes  )  { 
toFind_ResultList  =  lstRes  ; 
} 

public   String   getGran  (  String   sGran  )  { 
String   sPatternKey  ; 
Pattern   patternKey  ; 
Matcher   mKey  ; 
sPatternKey  =  "^(Y|E|C|L)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sGran  )  ; 
if  (  mKey  .  matches  (  )  )  { 
return  "Y"  ; 
} 
sPatternKey  =  "^(SE|M|Q)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sGran  )  ; 
if  (  mKey  .  matches  (  )  )  { 
return  "M"  ; 
} 
return   sGran  ; 
} 

public   List  <  String  >  evalFunction  (  String   sFct  ,  int   iSign  )  { 
int   iX  =  0  ; 
String   sY  =  ""  ; 
List  <  String  >  lstResult  =  new   ArrayList  <  String  >  (  )  ; 
String   sPatternKey  ; 
Pattern   patternKey  ; 
Matcher   mKey  ; 
sPatternKey  =  "^predecessor\\(([^\\[\\]]*)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
return   evalFunction  (  mKey  .  group  (  1  )  ,  iSign  -  1  )  ; 
} 
sPatternKey  =  "^successor\\(([^\\[\\]]*)\\)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
return   evalFunction  (  mKey  .  group  (  1  )  ,  iSign  +  1  )  ; 
} 
sPatternKey  =  "^SPECIAL$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
if  (  iSign  >=  0  )  { 
lstResult  .  add  (  "Y"  )  ; 
lstResult  .  add  (  "+"  +  Integer  .  toString  (  iSign  )  )  ; 
}  else  { 
lstResult  .  add  (  "Y"  )  ; 
lstResult  .  add  (  Integer  .  toString  (  iSign  )  )  ; 
} 
}  else  { 
sPatternKey  =  "^(SE|D|W|M|Y|Q|E|C|L|H|MIN|S)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
if  (  iSign  >=  0  )  { 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
lstResult  .  add  (  "+"  +  Integer  .  toString  (  iSign  )  )  ; 
}  else  { 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
lstResult  .  add  (  Integer  .  toString  (  iSign  )  )  ; 
} 
}  else  { 
sPatternKey  =  "^(\\d+)(SE|D|W|M|Y|Q|E|C|L|H|MIN|S)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
int   iGr1  =  Integer  .  parseInt  (  mKey  .  group  (  1  )  )  ; 
if  (  iSign  >=  0  )  { 
lstResult  .  add  (  mKey  .  group  (  2  )  )  ; 
lstResult  .  add  (  "+"  +  Integer  .  toString  (  iSign  *  iGr1  )  )  ; 
}  else  { 
lstResult  .  add  (  mKey  .  group  (  2  )  )  ; 
lstResult  .  add  (  Integer  .  toString  (  iSign  *  iGr1  )  )  ; 
} 
}  else  { 
sPatternKey  =  "^((\\d+)\\+)?1(HALF|THIRD|Q)(SE|D|W|M|Y|Q|E|C|L|H|MIN)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
iX  =  Integer  .  parseInt  (  mKey  .  group  (  2  )  )  ; 
sY  =  mKey  .  group  (  4  )  ; 
sPatternKey  =  "Y$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  find  (  )  )  { 
if  (  sFct  .  indexOf  (  "HALF"  )  >=  0  )  { 
iX  =  iX  *  12  +  6  ; 
} 
if  (  sFct  .  indexOf  (  "THIRD"  )  >=  0  )  { 
iX  =  iX  *  12  +  4  ; 
} 
if  (  sFct  .  indexOf  (  "Q"  )  >=  0  )  { 
iX  =  iX  *  12  +  3  ; 
} 
sY  =  "M"  ; 
} 
sPatternKey  =  "H$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  find  (  )  )  { 
if  (  sFct  .  indexOf  (  "HALF"  )  >=  0  )  { 
iX  =  iX  *  60  +  30  ; 
} 
if  (  sFct  .  indexOf  (  "THIRD"  )  >=  0  )  { 
iX  =  iX  *  60  +  20  ; 
} 
if  (  sFct  .  indexOf  (  "Q"  )  >=  0  )  { 
iX  =  iX  *  60  +  15  ; 
} 
sY  =  "MIN"  ; 
} 
if  (  iSign  >=  0  )  { 
lstResult  .  add  (  sY  )  ; 
lstResult  .  add  (  "+"  +  Integer  .  toString  (  iX  )  )  ; 
}  else  { 
lstResult  .  add  (  sY  )  ; 
lstResult  .  add  (  Integer  .  toString  (  iX  )  )  ; 
} 
}  else  { 
sPatternKey  =  "^M=(\\d+),D=(\\d+)$$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
if  (  iSign  >=  0  )  { 
lstResult  .  add  (  "Y"  )  ; 
lstResult  .  add  (  "+"  +  Integer  .  toString  (  iSign  )  )  ; 
lstResult  .  add  (  "M"  )  ; 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
lstResult  .  add  (  "D"  )  ; 
lstResult  .  add  (  mKey  .  group  (  2  )  )  ; 
}  else  { 
lstResult  .  add  (  "Y"  )  ; 
lstResult  .  add  (  Integer  .  toString  (  iSign  )  )  ; 
lstResult  .  add  (  "M"  )  ; 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
lstResult  .  add  (  "D"  )  ; 
lstResult  .  add  (  mKey  .  group  (  2  )  )  ; 
} 
}  else  { 
sPatternKey  =  "^DOW=(\\d|WE)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
if  (  iSign  >=  0  )  { 
lstResult  .  add  (  "W"  )  ; 
lstResult  .  add  (  "+"  +  Integer  .  toString  (  iSign  )  )  ; 
lstResult  .  add  (  "DOW"  )  ; 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
}  else  { 
lstResult  .  add  (  "W"  )  ; 
lstResult  .  add  (  Integer  .  toString  (  iSign  )  )  ; 
lstResult  .  add  (  "DOW"  )  ; 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
} 
}  else  { 
sPatternKey  =  "^M=(.+)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
if  (  iSign  >=  0  )  { 
lstResult  .  add  (  "Y"  )  ; 
lstResult  .  add  (  "+"  +  Integer  .  toString  (  iSign  )  )  ; 
lstResult  .  add  (  "M"  )  ; 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
}  else  { 
lstResult  .  add  (  "Y"  )  ; 
lstResult  .  add  (  Integer  .  toString  (  iSign  )  )  ; 
lstResult  .  add  (  "M"  )  ; 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
} 
}  else  { 
sPatternKey  =  "^H=(.+)$"  ; 
patternKey  =  Pattern  .  compile  (  sPatternKey  )  ; 
mKey  =  patternKey  .  matcher  (  sFct  )  ; 
if  (  mKey  .  matches  (  )  )  { 
if  (  iSign  >=  0  )  { 
lstResult  .  add  (  "D"  )  ; 
lstResult  .  add  (  "+"  +  Integer  .  toString  (  iSign  )  )  ; 
lstResult  .  add  (  "H"  )  ; 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
}  else  { 
lstResult  .  add  (  "D"  )  ; 
lstResult  .  add  (  Integer  .  toString  (  iSign  )  )  ; 
lstResult  .  add  (  "H"  )  ; 
lstResult  .  add  (  mKey  .  group  (  1  )  )  ; 
} 
}  else  { 
} 
} 
} 
} 
} 
} 
} 
} 
return   lstResult  ; 
} 

public   boolean   containsNotEmpty  (  String   sKey  )  { 
return   htblVariables  .  containsKey  (  sKey  )  &&  (  htblVariables  .  get  (  sKey  )  .  length  (  )  >  0  )  ; 
} 

public   void   clearUnusedAttributes  (  )  { 
Vector  <  String  >  v  =  new   Vector  <  String  >  (  htblVariables  .  keySet  (  )  )  ; 
for  (  Enumeration  <  String  >  e  =  v  .  elements  (  )  ;  e  .  hasMoreElements  (  )  ;  )  { 
String   sKey  =  e  .  nextElement  (  )  ; 
if  (  (  sKey  .  compareTo  (  "val"  )  ==  0  )  ||  (  sKey  .  compareTo  (  "text"  )  ==  0  )  ||  (  sKey  .  compareTo  (  "mod"  )  ==  0  )  ||  (  sKey  .  compareTo  (  "anchor_dir"  )  ==  0  )  ||  (  sKey  .  compareTo  (  "anchor_val"  )  ==  0  )  ||  (  sKey  .  compareTo  (  "set"  )  ==  0  )  )  { 
}  else  { 
htblVariables  .  remove  (  sKey  )  ; 
} 
} 
} 

public   String   getGranularity  (  int   iIndex  )  { 
if  (  toFind_ResultList  !=  null  )  { 
if  (  iIndex  <  toFind_ResultList  .  size  (  )  )  { 
return   toFind_ResultList  .  get  (  iIndex  )  ; 
} 
} 
return  ""  ; 
} 

public   int   getYear  (  )  { 
String   sVal  =  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  ""  )  ==  0  )  return  -  1  ; 
String   sPattern  =  "^(\\d+)(-|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  sVal  )  ; 
if  (  m  .  find  (  )  )  { 
try  { 
int   iRes  =  Integer  .  parseInt  (  m  .  group  (  1  )  )  ; 
return   iRes  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return  -  1  ; 
} 

public   String   getMonth  (  )  { 
String   sVal  =  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  ""  )  ==  0  )  { 
return  "-1"  ; 
} 
String   sPattern  =  "^(\\d+|X+)-(\\d+|H\\d|Q\\d|SP|SU|WI|FA)(-|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  sVal  )  ; 
if  (  m  .  find  (  )  )  { 
try  { 
String   sRes  =  m  .  group  (  2  )  ; 
return   sRes  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return  "-1"  ; 
} 

public   int   getWeek  (  )  { 
String   sVal  =  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  ""  )  ==  0  )  return  -  1  ; 
String   sPattern  =  "W(\\d+)(-|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  sVal  )  ; 
if  (  m  .  find  (  )  )  { 
try  { 
int   iRes  =  Integer  .  parseInt  (  m  .  group  (  1  )  )  ; 
return   iRes  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return  -  1  ; 
} 

public   int   getDayOfMonth  (  )  { 
String   sVal  =  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  ""  )  ==  0  )  return  -  1  ; 
String   sPattern  =  "^(\\d+|X+)-(\\d+|H\\d|Q\\d|SP|SU|WI|FA|XX)-(\\d+)(T|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  sVal  )  ; 
if  (  m  .  find  (  )  )  { 
try  { 
int   iRes  =  Integer  .  parseInt  (  m  .  group  (  3  )  )  ; 
return   iRes  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return  -  1  ; 
} 

public   String   getDayOfWeek  (  )  { 
String   sVal  =  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  ""  )  ==  0  )  return  "-1"  ; 
String   sPattern  =  "W(\\d+|X+)-(\\d|WE)(T|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  sVal  )  ; 
if  (  m  .  find  (  )  )  { 
try  { 
String   sRes  =  m  .  group  (  2  )  ; 
return   sRes  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return  "-1"  ; 
} 

public   String   getHour  (  )  { 
String   sVal  =  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  ""  )  ==  0  )  return  "-1"  ; 
String   sPattern  =  "T(\\d+|MO|MI|DT|MI|EV|AF)(:|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  sVal  )  ; 
if  (  m  .  find  (  )  )  { 
try  { 
String   sRes  =  m  .  group  (  1  )  ; 
return   sRes  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return  "-1"  ; 
} 

public   String   getMinute  (  )  { 
String   sVal  =  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  ""  )  ==  0  )  return  "-1"  ; 
String   sPattern  =  "T(\\d+|MO|MI|DT|MI|EV|AF|XX):(\\d+)(:|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  sVal  )  ; 
if  (  m  .  find  (  )  )  { 
try  { 
String   sRes  =  m  .  group  (  2  )  ; 
return   sRes  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return  "-1"  ; 
} 

public   int   getSecond  (  )  { 
String   sVal  =  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  ""  )  ==  0  )  return  -  1  ; 
String   sPattern  =  "T(\\d+|MO|MI|DT|MI|EV|AF|XX):(\\d+|XX):(\\d+)$"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  )  ; 
Matcher   m  =  pattern  .  matcher  (  sVal  )  ; 
if  (  m  .  find  (  )  )  { 
try  { 
int   iRes  =  Integer  .  parseInt  (  m  .  group  (  3  )  )  ; 
return   iRes  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return  -  1  ; 
} 

public   static   String   extend_To_TwoNumbers  (  String   sAttribVal  )  { 
if  (  sAttribVal  .  length  (  )  ==  1  )  { 
sAttribVal  =  "0"  +  sAttribVal  ; 
} 
return   sAttribVal  ; 
} 

public   void   postCorrections  (  TimePoint   refTime  )  { 
change_W_To_TwoNumberFormat  (  )  ; 
change_DOW_To_TwoNumberFormat  (  )  ; 
change_M_To_TwoNumberFormat  (  )  ; 
change_DOM_To_TwoNumberFormat  (  )  ; 
changeYWDow_toYMDom  (  )  ; 
setANCHORvalues  (  refTime  )  ; 
replaceMINtoM  (  )  ; 
clearUnusedAttributes  (  )  ; 
toFind_ResultList  =  null  ; 
} 

public   void   replaceMINtoM  (  )  { 
String   sVal  =  htblVariables  .  get  (  "val"  )  ; 
String   thePattern  =  "MIN"  ; 
Pattern   pattern  =  Pattern  .  compile  (  thePattern  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
Matcher   matcher  =  pattern  .  matcher  (  sVal  )  ; 
sVal  =  matcher  .  replaceAll  (  "M"  )  ; 
set  (  "val"  ,  sVal  )  ; 
} 

public   void   change_M_To_TwoNumberFormat  (  )  { 
String   sVal  =  htblVariables  .  get  (  "val"  )  ; 
String   sPattern  =  "^(\\d+|X+)-(\\d+|H\\d|Q\\d|SP|SU|WI|FA)(-|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
Matcher   matcher  =  pattern  .  matcher  (  sVal  )  ; 
if  (  matcher  .  find  (  )  )  { 
try  { 
String   sYear  =  matcher  .  group  (  1  )  ; 
String   sMonth  =  matcher  .  group  (  2  )  ; 
if  (  sMonth  .  length  (  )  ==  1  )  { 
sMonth  =  "0"  +  sMonth  ; 
} 
sVal  =  matcher  .  replaceAll  (  sYear  +  "-"  +  sMonth  +  matcher  .  group  (  3  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
set  (  "val"  ,  sVal  )  ; 
} 

public   void   change_DOM_To_TwoNumberFormat  (  )  { 
String   sVal  =  htblVariables  .  get  (  "val"  )  ; 
String   sPattern  =  "^(\\d+|X+)-(\\d+|H\\d|Q\\d|SP|SU|WI|FA|XX)-(\\d+)(T|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
Matcher   matcher  =  pattern  .  matcher  (  sVal  )  ; 
if  (  matcher  .  find  (  )  )  { 
try  { 
String   sYear  =  matcher  .  group  (  1  )  ; 
String   sMonth  =  matcher  .  group  (  2  )  ; 
if  (  sMonth  .  length  (  )  ==  1  )  { 
sMonth  =  "0"  +  sMonth  ; 
} 
String   sDom  =  matcher  .  group  (  3  )  .  toString  (  )  ; 
if  (  sDom  .  length  (  )  ==  1  )  { 
sDom  =  "0"  +  sDom  ; 
} 
sVal  =  matcher  .  replaceAll  (  sYear  +  "-"  +  sMonth  +  "-"  +  sDom  +  matcher  .  group  (  4  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
set  (  "val"  ,  sVal  )  ; 
} 

public   void   change_W_To_TwoNumberFormat  (  )  { 
String   sVal  =  htblVariables  .  get  (  "val"  )  ; 
String   sPattern  =  "W(\\d+)(-|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
Matcher   matcher  =  pattern  .  matcher  (  sVal  )  ; 
if  (  matcher  .  find  (  )  )  { 
try  { 
String   sWeekOfYear  =  matcher  .  group  (  1  )  ; 
if  (  sWeekOfYear  .  length  (  )  ==  1  )  { 
sWeekOfYear  =  "0"  +  sWeekOfYear  ; 
} 
sVal  =  matcher  .  replaceAll  (  "W"  +  sWeekOfYear  +  matcher  .  group  (  2  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
set  (  "val"  ,  sVal  )  ; 
} 

public   void   change_DOW_To_TwoNumberFormat  (  )  { 
String   sVal  =  htblVariables  .  get  (  "val"  )  ; 
String   sPattern  =  "W(\\d+|X+)-(\\d|WE)(T|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  sPattern  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
Matcher   matcher  =  pattern  .  matcher  (  sVal  )  ; 
if  (  matcher  .  find  (  )  )  { 
try  { 
String   sDow  =  matcher  .  group  (  2  )  .  toString  (  )  ; 
if  (  sDow  .  length  (  )  ==  1  )  { 
sDow  =  "0"  +  sDow  ; 
} 
sVal  =  matcher  .  replaceAll  (  "W"  +  matcher  .  group  (  1  )  +  "-"  +  sDow  +  matcher  .  group  (  3  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
set  (  "val"  ,  sVal  )  ; 
} 

public   void   changeYWDow_toYMDom  (  )  { 
String   sVal  =  htblVariables  .  get  (  "val"  )  ; 
String   thePattern  =  "^(\\d+)-W(\\d+)-(\\d+|WE)(T|$)"  ; 
Pattern   pattern  =  Pattern  .  compile  (  thePattern  ,  Pattern  .  CASE_INSENSITIVE  )  ; 
Matcher   matcher  =  pattern  .  matcher  (  sVal  )  ; 
if  (  matcher  .  find  (  )  )  { 
try  { 
int   iYear  =  Integer  .  parseInt  (  matcher  .  group  (  1  )  )  ; 
int   iWeekOfYear  =  Integer  .  parseInt  (  matcher  .  group  (  2  )  )  ; 
String   sDow  =  matcher  .  group  (  3  )  .  toString  (  )  ; 
int   iDow  ; 
if  (  (  sDow  .  compareTo  (  "WE"  )  ==  0  )  )  { 
iDow  =  6  ; 
return  ; 
}  else  { 
iDow  =  Integer  .  parseInt  (  sDow  )  ; 
} 
String   sNewVal  =  GregorianCalendarHelper  .  getYYYYMMDD_From_YYYY_W_DOW  (  iYear  ,  iWeekOfYear  ,  iDow  )  ; 
sVal  =  matcher  .  replaceAll  (  sNewVal  +  matcher  .  group  (  4  )  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
set  (  "val"  ,  sVal  )  ; 
} 

public   String   getNormalizedTime  (  )  { 
String   sNormTime  =  "T"  ; 
String   sHour  =  getHour  (  )  ; 
if  (  sHour  .  compareTo  (  "-1"  )  !=  0  )  { 
sNormTime  +=  extend_To_TwoNumbers  (  sHour  )  ; 
} 
String   sMin  =  getMinute  (  )  ; 
if  (  sMin  .  compareTo  (  "-1"  )  !=  0  )  { 
sNormTime  +=  ":"  +  extend_To_TwoNumbers  (  sMin  )  ; 
} 
int   iSec  =  getSecond  (  )  ; 
if  (  iSec  !=  -  1  )  { 
sNormTime  +=  ":"  +  extend_To_TwoNumbers  (  Integer  .  toString  (  iSec  )  )  ; 
} 
return   sNormTime  ; 
} 

public   String   getNormalized_YYYY_M_DOW  (  )  { 
String   sNewVal  =  Integer  .  toString  (  this  .  getYear  (  )  )  +  "-"  +  extend_To_TwoNumbers  (  this  .  getMonth  (  )  )  +  "-"  +  extend_To_TwoNumbers  (  Integer  .  toString  (  this  .  getDayOfMonth  (  )  )  )  ; 
return   sNewVal  ; 
} 

public   String   getNormalized_YYYY_M  (  )  { 
String   sNewVal  =  Integer  .  toString  (  this  .  getYear  (  )  )  +  "-"  +  extend_To_TwoNumbers  (  this  .  getMonth  (  )  )  ; 
return   sNewVal  ; 
} 

public   String   getNormalized_YYYY_W  (  )  { 
String   sNewVal  =  ""  ; 
int   iWeek  =  this  .  getWeek  (  )  ; 
int   iYY  ,  iDom  ; 
String   sM  ; 
try  { 
if  (  iWeek  ==  -  1  )  { 
iYY  =  this  .  getYear  (  )  ; 
sM  =  this  .  getMonth  (  )  ; 
iDom  =  this  .  getDayOfMonth  (  )  ; 
sNewVal  =  GregorianCalendarHelper  .  getYYYY_W_From_YYYYMDOM  (  iYY  ,  Integer  .  parseInt  (  sM  )  ,  iDom  )  ; 
}  else  { 
sNewVal  =  Integer  .  toString  (  this  .  getYear  (  )  )  +  "-W"  +  extend_To_TwoNumbers  (  Integer  .  toString  (  this  .  getWeek  (  )  )  )  ; 
} 
}  catch  (  Exception   e  )  { 
} 
return   sNewVal  ; 
} 

public   void   setANCHORvalues  (  TimePoint   refTime  )  { 
String   sVal  =  htblVariables  .  get  (  "val"  )  ; 
if  (  sVal  .  compareTo  (  "PRESENT_REF"  )  ==  0  )  { 
set  (  "val"  ,  refTime  .  get  (  "val"  )  )  ; 
}  else   if  (  sVal  .  compareTo  (  "PAST_REF"  )  ==  0  )  { 
set  (  "anchor_dir"  ,  "BEFORE"  )  ; 
set  (  "anchor_val"  ,  refTime  .  get  (  "val"  )  )  ; 
}  else   if  (  sVal  .  compareTo  (  "FUTURE_REF"  )  ==  0  )  { 
set  (  "anchor_dir"  ,  "AFTER"  )  ; 
set  (  "anchor_val"  ,  refTime  .  get  (  "val"  )  )  ; 
}  else   if  (  sVal  .  matches  (  "^P.*Y$"  )  )  { 
set  (  "anchor_dir"  ,  "BEFORE"  )  ; 
set  (  "anchor_val"  ,  Integer  .  toString  (  refTime  .  getYear  (  )  )  )  ; 
}  else   if  (  sVal  .  matches  (  "^P.*M$"  )  )  { 
set  (  "anchor_dir"  ,  "BEFORE"  )  ; 
String   sNewVal  =  refTime  .  getNormalized_YYYY_M  (  )  ; 
set  (  "anchor_val"  ,  sNewVal  )  ; 
}  else   if  (  sVal  .  matches  (  "^P.*W$"  )  )  { 
set  (  "anchor_dir"  ,  "BEFORE"  )  ; 
String   sNewVal  =  refTime  .  getNormalized_YYYY_W  (  )  ; 
set  (  "anchor_val"  ,  sNewVal  )  ; 
}  else   if  (  sVal  .  matches  (  "^P.*D$"  )  )  { 
set  (  "anchor_dir"  ,  "BEFORE"  )  ; 
String   sNewVal  =  refTime  .  getNormalized_YYYY_M_DOW  (  )  ; 
set  (  "anchor_val"  ,  sNewVal  )  ; 
}  else   if  (  sVal  .  matches  (  "^P.*T.*"  )  )  { 
set  (  "anchor_dir"  ,  "WITHIN"  )  ; 
String   sNewVal  =  refTime  .  getNormalized_YYYY_M_DOW  (  )  ; 
set  (  "anchor_val"  ,  sNewVal  )  ; 
} 
} 

public   List  <  TimePoint  >  splitTP_Into_Range  (  List  <  TimePoint  >  rangeTp  )  { 
List  <  TimePoint  >  lstNewTPs  =  new   ArrayList  <  TimePoint  >  (  )  ; 
lstNewTPs  .  add  (  this  )  ; 
return   lstNewTPs  ; 
} 
} 

