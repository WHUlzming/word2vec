package   com  .  be  .  table  ; 

import   java  .  util  .  *  ; 
import   com  .  be  .  vo  .  NavigationMenuVO  ; 
import   com  .  util  .  comparator  .  *  ; 
import   com  .  util  .  table  .  *  ; 

public   class   NavigationMenuModel   implements   ITableModel  { 

protected   NavigationMenuVO   fTotal  ; 

protected   NavigationMenuVO  [  ]  fTableData  ; 

protected   String   fSortedColumn  ; 

protected   int  [  ]  fSortOrder  ; 

protected   Comparator   fComparator  ; 

public   NavigationMenuModel  (  )  { 
super  (  )  ; 
} 

public   void   setData  (  Object  [  ]  data  )  { 
fTableData  =  (  NavigationMenuVO  [  ]  )  data  ; 
fSortOrder  =  new   int  [  (  fTableData  !=  null  )  ?  fTableData  .  length  :  0  ]  ; 
for  (  int   i  =  0  ;  i  <  fSortOrder  .  length  ;  i  ++  )  { 
fSortOrder  [  i  ]  =  i  ; 
} 
} 

public   Object   getValueAt  (  int   pRow  ,  String   pColumn  )  { 
NavigationMenuVO   vo  =  fTableData  [  fSortOrder  [  pRow  ]  ]  ; 
if  (  "id"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getId  (  )  )  ; 
if  (  "moduleName"  .  equals  (  pColumn  )  )  return   vo  .  getModuleName  (  )  ; 
if  (  "seqNumber"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getSeqNumber  (  )  )  ; 
if  (  "name"  .  equals  (  pColumn  )  )  return   vo  .  getName  (  )  ; 
if  (  "link"  .  equals  (  pColumn  )  )  return   vo  .  getLink  (  )  ; 
if  (  "level"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getLevel  (  )  )  ; 
if  (  "parent"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getParent  (  )  )  ; 
return   null  ; 
} 

public   Object   getTotalValueAt  (  String   pColumn  )  { 
if  (  fTotal  !=  null  )  { 
if  (  "id"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getId  (  )  )  ; 
if  (  "moduleName"  .  equals  (  pColumn  )  )  return   fTotal  .  getModuleName  (  )  ; 
if  (  "seqNumber"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getSeqNumber  (  )  )  ; 
if  (  "name"  .  equals  (  pColumn  )  )  return   fTotal  .  getName  (  )  ; 
if  (  "link"  .  equals  (  pColumn  )  )  return   fTotal  .  getLink  (  )  ; 
if  (  "level"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getLevel  (  )  )  ; 
if  (  "parent"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getParent  (  )  )  ; 
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
if  (  "moduleName"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getModuleName  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "seqNumber"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getSeqNumber  (  )  ; 
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
if  (  "link"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getLink  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "level"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getLevel  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "parent"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getParent  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
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

