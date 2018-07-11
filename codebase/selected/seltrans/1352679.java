package   com  .  creditors  .  table  ; 

import   java  .  util  .  *  ; 
import   com  .  creditors  .  vo  .  PF28VO  ; 
import   com  .  util  .  comparator  .  *  ; 
import   com  .  util  .  table  .  *  ; 

public   class   PF28Model   implements   ITableModel  { 

protected   PF28VO   fTotal  ; 

protected   PF28VO  [  ]  fTableData  ; 

protected   String   fSortedColumn  ; 

protected   int  [  ]  fSortOrder  ; 

protected   Comparator   fComparator  ; 

public   PF28Model  (  )  { 
super  (  )  ; 
} 

public   void   setData  (  Object  [  ]  data  )  { 
fTableData  =  (  PF28VO  [  ]  )  data  ; 
fSortOrder  =  new   int  [  (  fTableData  !=  null  )  ?  fTableData  .  length  :  0  ]  ; 
for  (  int   i  =  0  ;  i  <  fSortOrder  .  length  ;  i  ++  )  { 
fSortOrder  [  i  ]  =  i  ; 
} 
} 

public   Object   getValueAt  (  int   pRow  ,  String   pColumn  )  { 
PF28VO   vo  =  fTableData  [  fSortOrder  [  pRow  ]  ]  ; 
if  (  "id"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getId  (  )  )  ; 
if  (  "type"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getType  (  )  )  ; 
if  (  "principalID"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getPrincipalID  (  )  )  ; 
if  (  "currencyBookID"  .  equals  (  pColumn  )  )  return   new   Short  (  vo  .  getCurrencyBookID  (  )  )  ; 
if  (  "amount"  .  equals  (  pColumn  )  )  return   vo  .  getAmount  (  )  ; 
if  (  "currencyPayID"  .  equals  (  pColumn  )  )  return   new   Short  (  vo  .  getCurrencyPayID  (  )  )  ; 
if  (  "countryID"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getCountryID  (  )  )  ; 
if  (  "creationDate"  .  equals  (  pColumn  )  )  return   vo  .  getCreationDate  (  )  ; 
if  (  "dueDate"  .  equals  (  pColumn  )  )  return   vo  .  getDueDate  (  )  ; 
if  (  "esrAccountID"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getEsrAccountID  (  )  )  ; 
if  (  "referenceNumber"  .  equals  (  pColumn  )  )  return   vo  .  getReferenceNumber  (  )  ; 
if  (  "senderReference"  .  equals  (  pColumn  )  )  return   vo  .  getSenderReference  (  )  ; 
if  (  "codierzeile"  .  equals  (  pColumn  )  )  return   vo  .  getCodierzeile  (  )  ; 
if  (  "recipientID"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getRecipientID  (  )  )  ; 
if  (  "beneficiaryID"  .  equals  (  pColumn  )  )  return   new   Long  (  vo  .  getBeneficiaryID  (  )  )  ; 
return   null  ; 
} 

public   Object   getTotalValueAt  (  String   pColumn  )  { 
if  (  fTotal  !=  null  )  { 
if  (  "id"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getId  (  )  )  ; 
if  (  "type"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getType  (  )  )  ; 
if  (  "principalID"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getPrincipalID  (  )  )  ; 
if  (  "currencyBookID"  .  equals  (  pColumn  )  )  return   new   Short  (  fTotal  .  getCurrencyBookID  (  )  )  ; 
if  (  "amount"  .  equals  (  pColumn  )  )  return   fTotal  .  getAmount  (  )  ; 
if  (  "currencyPayID"  .  equals  (  pColumn  )  )  return   new   Short  (  fTotal  .  getCurrencyPayID  (  )  )  ; 
if  (  "countryID"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getCountryID  (  )  )  ; 
if  (  "creationDate"  .  equals  (  pColumn  )  )  return   fTotal  .  getCreationDate  (  )  ; 
if  (  "dueDate"  .  equals  (  pColumn  )  )  return   fTotal  .  getDueDate  (  )  ; 
if  (  "esrAccountID"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getEsrAccountID  (  )  )  ; 
if  (  "referenceNumber"  .  equals  (  pColumn  )  )  return   fTotal  .  getReferenceNumber  (  )  ; 
if  (  "senderReference"  .  equals  (  pColumn  )  )  return   fTotal  .  getSenderReference  (  )  ; 
if  (  "codierzeile"  .  equals  (  pColumn  )  )  return   fTotal  .  getCodierzeile  (  )  ; 
if  (  "recipientID"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getRecipientID  (  )  )  ; 
if  (  "beneficiaryID"  .  equals  (  pColumn  )  )  return   new   Long  (  fTotal  .  getBeneficiaryID  (  )  )  ; 
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
if  (  "type"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getType  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "principalID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getPrincipalID  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "currencyBookID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
short  [  ]  temp  =  new   short  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCurrencyBookID  (  )  ; 
} 
fSortOrder  =  sorter  .  sortShort  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "amount"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   BigDecimalComparator  (  )  ; 
java  .  math  .  BigDecimal  [  ]  temp  =  new   java  .  math  .  BigDecimal  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getAmount  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "currencyPayID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
short  [  ]  temp  =  new   short  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCurrencyPayID  (  )  ; 
} 
fSortOrder  =  sorter  .  sortShort  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "countryID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCountryID  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "creationDate"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   DateComparator  (  )  ; 
java  .  sql  .  Date  [  ]  temp  =  new   java  .  sql  .  Date  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCreationDate  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "dueDate"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   DateComparator  (  )  ; 
java  .  sql  .  Date  [  ]  temp  =  new   java  .  sql  .  Date  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getDueDate  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "esrAccountID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getEsrAccountID  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "referenceNumber"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getReferenceNumber  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "senderReference"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getSenderReference  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "codierzeile"  .  equals  (  pColumn  )  )  { 
fComparator  =  new   StringComparator  (  )  ; 
String  [  ]  temp  =  new   String  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getCodierzeile  (  )  ; 
} 
sort  (  temp  ,  0  ,  temp  .  length  -  1  ,  up  )  ; 
} 
if  (  "recipientID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getRecipientID  (  )  ; 
} 
fSortOrder  =  sorter  .  sortLong  (  temp  ,  fSortOrder  ,  up  )  ; 
} 
if  (  "beneficiaryID"  .  equals  (  pColumn  )  )  { 
Sorter   sorter  =  new   Sorter  (  )  ; 
long  [  ]  temp  =  new   long  [  fTableData  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  temp  .  length  ;  i  ++  )  { 
temp  [  i  ]  =  fTableData  [  i  ]  .  getBeneficiaryID  (  )  ; 
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

