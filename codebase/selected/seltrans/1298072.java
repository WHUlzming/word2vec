package   com  .  be  .  table  ; 

import   java  .  util  .  *  ; 
import   com  .  be  .  vo  .  VATVO  ; 
import   com  .  util  .  comparator  .  *  ; 
import   com  .  util  .  table  .  *  ; 

public   class   VATModel   implements   ITableModel  { 

protected   VATVO   fTotal  ; 

protected   VATVO  [  ]  fTableData  ; 

protected   String   fSortedColumn  ; 

protected   int  [  ]  fSortOrder  ; 

protected   Comparator   fComparator  ; 

public   VATModel  (  )  { 
super  (  )  ; 
} 

public   void   setData  (  Object  [  ]  data  )  { 
fTableData  =  (  VATVO  [  ]  )  data  ; 
fSortOrder  =  new   int  [  (  fTableData  !=  null  )  ?  fTableData  .  length  :  0  ]  ; 
for  (  int   i  =  0  ;  i  <  fSortOrder  .  length  ;  i  ++  )  { 
fSortOrder  [  i  ]  =  i  ; 
} 
} 

public   Object   getValueAt  (  int   pRow  ,  String   pColumn  )  { 
VATVO   vo  =  fTableData  [  fSortOrder  [  pRow  ]  ]  ; 
if  (  "id"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getId  (  )  )  ; 
if  (  "code"  .  equals  (  pColumn  )  )  return   vo  .  getCode  (  )  ; 
if  (  "codeNum"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getCodeNum  (  )  )  ; 
if  (  "name"  .  equals  (  pColumn  )  )  return   vo  .  getName  (  )  ; 
if  (  "percent"  .  equals  (  pColumn  )  )  return   new   Double  (  vo  .  getPercent  (  )  )  ; 
if  (  "validFrom"  .  equals  (  pColumn  )  )  return   vo  .  getValidFrom  (  )  ; 
if  (  "validTo"  .  equals  (  pColumn  )  )  return   vo  .  getValidTo  (  )  ; 
return   null  ; 
} 

public   Object   getTotalValueAt  (  String   pColumn  )  { 
if  (  fTotal  !=  null  )  { 
if  (  "id"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getId  (  )  )  ; 
if  (  "code"  .  equals  (  pColumn  )  )  return   fTotal  .  getCode  (  )  ; 
if  (  "codeNum"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getCodeNum  (  )  )  ; 
if  (  "name"  .  equals  (  pColumn  )  )  return   fTotal  .  getName  (  )  ; 
if  (  "percent"  .  equals  (  pColumn  )  )  return   new   Double  (  fTotal  .  getPercent  (  )  )  ; 
if  (  "validFrom"  .  equals  (  pColumn  )  )  return   fTotal  .  getValidFrom  (  )  ; 
if  (  "validTo"  .  equals  (  pColumn  )  )  return   fTotal  .  getValidTo  (  )  ; 
} 
return   null  ; 
} 

public   void   sort  (  String   pColumn  ,  String   pSortDirection  )  { 
boolean   up  =  true  ; 
if  (  !  "u"  .  equals  (  pSortDirection  )  )  { 
up  =  false  ; 
} 
if  (  "id"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getId  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "code"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCode  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "codeNum"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCodeNum  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "name"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getName  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "percent"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
double  [  ]  temp  =  new   double  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getPercent  (  )  ; 
} 
fSortOrder  =  sorter  .  sortDouble  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "validFrom"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   DateComparator  (  )  ; 
java  .  sql  .  Date  [  ]  temp  =  new   java  .  sql  .  Date  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getValidFrom  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "validTo"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   DateComparator  (  )  ; 
java  .  sql  .  Date  [  ]  temp  =  new   java  .  sql  .  Date  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getValidTo  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
fSortedColumn  =  pColumn  ; 
} 

@  SuppressWarnings  (  "unchecked"  ) 
private   void   sort  (  Object  [  ]  a  ,  int   lo0  ,  int   hi0  ,  boolean   up  )  { 
int   lo  =  lo0  ; 
int   hi  =  hi0  ; 
if  (  lo  >=  hi  )  { 
return  ; 
} 
int   mid  =  (  lo  +  hi  )  /  2  ; 
sort  (  a  ,  lo  ,  mid  ,  up  )  ; 
sort  (  a  ,  mid  +  1  ,  hi  ,  up  )  ; 
int   end_lo  =  mid  ; 
int   start_hi  =  mid  +  1  ; 
while  (  (  lo  <=  end_lo  )  &&  (  start_hi  <=  hi  )  )  { 
boolean   isChange  ; 
if  (  up  )  { 
isChange  =  (  fComparator  .  compare  (  a  [  fSortOrder  [  lo  ]  ]  ,  a  [  fSortOrder  [  start_hi  ]  ]  )  <=  0  )  ; 
}  else  { 
isChange  =  (  fComparator  .  compare  (  a  [  fSortOrder  [  lo  ]  ]  ,  a  [  fSortOrder  [  start_hi  ]  ]  )  >=  0  )  ; 
} 
if  (  isChange  )  { 
lo  ++  ; 
}  else  { 
int   T  =  fSortOrder  [  start_hi  ]  ; 
for  (  int   k  =  start_hi  -  1  ;  k  >=  lo  ;  k  --  )  { 
fSortOrder  [  k  +  1  ]  =  fSortOrder  [  k  ]  ; 
} 
fSortOrder  [  lo  ]  =  T  ; 
lo  ++  ; 
end_lo  ++  ; 
start_hi  ++  ; 
} 
} 
} 

public   String  [  ]  getSortedColumns  (  )  { 
if  (  fSortedColumn  !=  null  )  { 
return   new   String  [  ]  {  fSortedColumn  }  ; 
}  else  { 
return   null  ; 
} 
} 

public   Object   getRowAt  (  int   index  )  { 
return  (  fTableData  !=  null  ?  fTableData  [  fSortOrder  [  index  ]  ]  :  null  )  ; 
} 

public   Object   getTotalRow  (  )  { 
return   fTotal  ; 
} 

public   int   getRowCount  (  )  { 
return  (  fTableData  !=  null  )  ?  fTableData  .  length  :  0  ; 
} 
} 

