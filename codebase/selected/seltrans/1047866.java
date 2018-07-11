package   org  .  springframework  .  richclient  .  table  ; 

import   java  .  awt  .  EventQueue  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  Arrays  ; 
import   java  .  util  .  Collections  ; 
import   java  .  util  .  Comparator  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Map  ; 
import   javax  .  swing  .  SwingUtilities  ; 
import   javax  .  swing  .  event  .  TableModelEvent  ; 
import   javax  .  swing  .  table  .  TableModel  ; 
import   org  .  springframework  .  core  .  style  .  StylerUtils  ; 
import   org  .  springframework  .  util  .  Assert  ; 
import   org  .  springframework  .  util  .  comparator  .  NullSafeComparator  ; 














public   class   ShuttleSortableTableModel   extends   AbstractTableModelFilter   implements   SortableTableModel  { 

private   static   final   Comparator   OBJECT_COMPARATOR  =  new   NullSafeComparator  (  ToStringComparator  .  INSTANCE  ,  true  )  ; 

private   static   final   Comparator   COMPARABLE_COMPARATOR  =  NullSafeComparator  .  NULLS_LOW  ; 

private   Comparator  [  ]  columnComparators  ; 

private   List   columnsToSort  =  new   ArrayList  (  4  )  ; 

private   int  [  ]  indexes  ; 

private   int   compares  ; 

private   boolean   autoSortEnabled  =  true  ; 

private   Runnable   notifyTableRunnable  =  new   Runnable  (  )  { 

public   void   run  (  )  { 
fireTableDataChanged  (  )  ; 
} 
}  ; 

public   ShuttleSortableTableModel  (  TableModel   model  )  { 
super  (  model  )  ; 
allocateIndexes  (  )  ; 
resetComparators  (  )  ; 
} 

private   void   allocateIndexes  (  )  { 
int   rowCount  =  filteredModel  .  getRowCount  (  )  ; 
indexes  =  new   int  [  rowCount  ]  ; 
for  (  int   row  =  0  ;  row  <  rowCount  ;  row  ++  )  { 
indexes  [  row  ]  =  row  ; 
} 
} 

public   void   resetComparators  (  )  { 
resetComparators  (  Collections  .  EMPTY_MAP  )  ; 
} 








public   void   resetComparators  (  Map   comparators  )  { 
int   colCount  =  filteredModel  .  getColumnCount  (  )  ; 
columnComparators  =  new   Comparator  [  colCount  ]  ; 
Comparator   newComparator  ; 
Class   clazz  ; 
for  (  int   i  =  0  ;  i  <  columnComparators  .  length  ;  i  ++  )  { 
newComparator  =  (  Comparator  )  comparators  .  get  (  Integer  .  valueOf  (  i  )  )  ; 
if  (  newComparator  !=  null  )  setComparator  (  i  ,  newComparator  )  ;  else  { 
clazz  =  filteredModel  .  getColumnClass  (  i  )  ; 
if  (  clazz  ==  Object  .  class  ||  !  Comparable  .  class  .  isAssignableFrom  (  clazz  )  )  columnComparators  [  i  ]  =  OBJECT_COMPARATOR  ; 
} 
} 
} 

public   boolean   isCellEditable  (  int   row  ,  int   column  )  { 
return   filteredModel  .  isCellEditable  (  indexes  [  row  ]  ,  column  )  ; 
} 

public   boolean   isAutoSortEnabled  (  )  { 
return   autoSortEnabled  ; 
} 

public   void   setAutoSortEnabled  (  boolean   autoSortEnabled  )  { 
this  .  autoSortEnabled  =  autoSortEnabled  ; 
} 

public   Comparator   getComparator  (  int   columnIndex  )  { 
return   this  .  columnComparators  [  columnIndex  ]  ; 
} 

public   void   setComparator  (  int   columnIndex  ,  Comparator   comparator  )  { 
Assert  .  notNull  (  comparator  )  ; 
this  .  columnComparators  [  columnIndex  ]  =  comparator  ; 
} 

public   Object   getValueAt  (  int   row  ,  int   column  )  { 
return   filteredModel  .  getValueAt  (  indexes  [  row  ]  ,  column  )  ; 
} 

public   void   setValueAt  (  Object   value  ,  int   row  ,  int   column  )  { 
filteredModel  .  setValueAt  (  value  ,  indexes  [  row  ]  ,  column  )  ; 
} 

public   void   sortByColumn  (  ColumnToSort   columnToSort  )  { 
columnsToSort  .  clear  (  )  ; 
columnsToSort  .  add  (  columnToSort  )  ; 
sort  (  )  ; 
SwingUtilities  .  invokeLater  (  notifyTableRunnable  )  ; 
} 

public   void   sortByColumns  (  ColumnToSort  [  ]  columnsToSort  )  { 
this  .  columnsToSort  =  Arrays  .  asList  (  columnsToSort  )  ; 
sort  (  )  ; 
notifyTableChanged  (  )  ; 
} 

public   int  [  ]  sortByColumns  (  ColumnToSort  [  ]  columnsToSort  ,  int  [  ]  preSortSelectedRows  )  { 
int  [  ]  modelIndexes  =  new   int  [  preSortSelectedRows  .  length  ]  ; 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "Selected row indexes before sort"  +  StylerUtils  .  style  (  preSortSelectedRows  )  )  ; 
} 
for  (  int   i  =  0  ;  i  <  preSortSelectedRows  .  length  ;  i  ++  )  { 
modelIndexes  [  i  ]  =  convertSortedIndexToDataIndex  (  preSortSelectedRows  [  i  ]  )  ; 
} 
this  .  columnsToSort  =  Arrays  .  asList  (  columnsToSort  )  ; 
sort  (  )  ; 
int  [  ]  postSortSelectedRows  =  new   int  [  modelIndexes  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  modelIndexes  .  length  ;  i  ++  )  { 
postSortSelectedRows  [  i  ]  =  convertModelToRowIndex  (  modelIndexes  [  i  ]  )  ; 
} 
if  (  logger  .  isDebugEnabled  (  )  )  { 
logger  .  debug  (  "Selected row indexes after sort"  +  StylerUtils  .  style  (  postSortSelectedRows  )  )  ; 
} 
notifyTableChanged  (  )  ; 
return   postSortSelectedRows  ; 
} 

protected   void   notifyTableChanged  (  )  { 
if  (  !  EventQueue  .  isDispatchThread  (  )  )  { 
SwingUtilities  .  invokeLater  (  notifyTableRunnable  )  ; 
}  else  { 
notifyTableRunnable  .  run  (  )  ; 
} 
} 

public   int   convertSortedIndexToDataIndex  (  int   index  )  { 
return   indexes  [  index  ]  ; 
} 

public   int  [  ]  convertSortedIndexesToDataIndexes  (  int  [  ]  indexes  )  { 
int  [  ]  converted  =  new   int  [  indexes  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  indexes  .  length  ;  i  ++  )  { 
converted  [  i  ]  =  convertSortedIndexToDataIndex  (  indexes  [  i  ]  )  ; 
} 
return   converted  ; 
} 

public   int   convertModelToRowIndex  (  int   index  )  { 
for  (  int   i  =  0  ;  i  <  indexes  .  length  ;  i  ++  )  { 
if  (  index  ==  indexes  [  i  ]  )  { 
return   i  ; 
} 
} 
return   0  ; 
} 

public   int  [  ]  convertDataIndexesToSortedIndexes  (  int  [  ]  indexes  )  { 
int  [  ]  converted  =  new   int  [  indexes  .  length  ]  ; 
for  (  int   i  =  0  ;  i  <  indexes  .  length  ;  i  ++  )  { 
converted  [  i  ]  =  convertModelToRowIndex  (  indexes  [  i  ]  )  ; 
} 
return   converted  ; 
} 

private   void   sort  (  )  { 
if  (  columnsToSort  .  size  (  )  >  0  )  { 
checkModel  (  )  ; 
compares  =  0  ; 
doShuttleSort  (  (  int  [  ]  )  indexes  .  clone  (  )  ,  indexes  ,  0  ,  indexes  .  length  )  ; 
} 
} 

private   void   checkModel  (  )  { 
if  (  indexes  .  length  !=  filteredModel  .  getRowCount  (  )  )  { 
throw   new   IllegalStateException  (  "Sorter not informed of a change in model."  )  ; 
} 
} 

private   void   doShuttleSort  (  int   from  [  ]  ,  int   to  [  ]  ,  int   low  ,  int   high  )  { 
if  (  high  -  low  <  2  )  { 
return  ; 
} 
int   middle  =  (  low  +  high  )  /  2  ; 
doShuttleSort  (  to  ,  from  ,  low  ,  middle  )  ; 
doShuttleSort  (  to  ,  from  ,  middle  ,  high  )  ; 
int   p  =  low  ; 
int   q  =  middle  ; 
if  (  high  -  low  >=  4  &&  compare  (  from  [  middle  -  1  ]  ,  from  [  middle  ]  )  <=  0  )  { 
for  (  int   i  =  low  ;  i  <  high  ;  i  ++  )  { 
to  [  i  ]  =  from  [  i  ]  ; 
} 
return  ; 
} 
for  (  int   i  =  low  ;  i  <  high  ;  i  ++  )  { 
if  (  q  >=  high  ||  (  p  <  middle  &&  compare  (  from  [  p  ]  ,  from  [  q  ]  )  <=  0  )  )  { 
to  [  i  ]  =  from  [  p  ++  ]  ; 
}  else  { 
to  [  i  ]  =  from  [  q  ++  ]  ; 
} 
} 
} 

public   int   compare  (  int   row1  ,  int   row2  )  { 
compares  ++  ; 
for  (  int   level  =  0  ;  level  <  columnsToSort  .  size  (  )  ;  level  ++  )  { 
ColumnToSort   column  =  (  ColumnToSort  )  columnsToSort  .  get  (  level  )  ; 
int   result  =  compareRowsByColumn  (  row1  ,  row2  ,  column  .  getColumnIndex  (  )  )  ; 
if  (  result  !=  0  )  { 
return   column  .  getSortOrder  (  )  ==  SortOrder  .  ASCENDING  ?  result  :  -  result  ; 
} 
} 
return   0  ; 
} 

private   int   compareRowsByColumn  (  int   row1  ,  int   row2  ,  int   column  )  { 
Object   o1  =  filteredModel  .  getValueAt  (  row1  ,  column  )  ; 
Object   o2  =  filteredModel  .  getValueAt  (  row2  ,  column  )  ; 
Comparator   comparator  =  columnComparators  [  column  ]  ; 
if  (  comparator  !=  null  )  { 
return   comparator  .  compare  (  o1  ,  o2  )  ; 
} 
return   COMPARABLE_COMPARATOR  .  compare  (  o1  ,  o2  )  ; 
} 

public   void   tableChanged  (  final   TableModelEvent   e  )  { 
if  (  e  .  getType  (  )  ==  TableModelEvent  .  INSERT  )  { 
if  (  autoSortEnabled  )  { 
reallocateIndexesOnInsert  (  e  .  getFirstRow  (  )  ,  e  .  getLastRow  (  )  )  ; 
sort  (  )  ; 
final   int  [  ]  insertedRows  =  new   int  [  e  .  getLastRow  (  )  -  e  .  getFirstRow  (  )  +  1  ]  ; 
int   row  =  e  .  getFirstRow  (  )  ; 
for  (  int   i  =  0  ;  i  <  insertedRows  .  length  ;  i  ++  )  { 
insertedRows  [  i  ]  =  convertModelToRowIndex  (  row  ++  )  ; 
} 
for  (  int   i  =  0  ;  i  <  insertedRows  .  length  ;  i  ++  )  { 
fireTableRowsInserted  (  insertedRows  [  i  ]  ,  insertedRows  [  i  ]  )  ; 
} 
}  else  { 
reallocateIndexesOnInsert  (  e  .  getFirstRow  (  )  ,  e  .  getLastRow  (  )  )  ; 
super  .  tableChanged  (  e  )  ; 
} 
}  else   if  (  e  .  getType  (  )  ==  TableModelEvent  .  DELETE  )  { 
final   int  [  ]  deletedRows  =  new   int  [  e  .  getLastRow  (  )  -  e  .  getFirstRow  (  )  +  1  ]  ; 
int   row  =  e  .  getFirstRow  (  )  ; 
for  (  int   i  =  0  ;  i  <  deletedRows  .  length  ;  i  ++  )  { 
deletedRows  [  i  ]  =  convertModelToRowIndex  (  row  )  ; 
row  ++  ; 
} 
allocateIndexes  (  )  ; 
for  (  int   i  =  0  ;  i  <  deletedRows  .  length  ;  i  ++  )  { 
fireTableRowsDeleted  (  deletedRows  [  i  ]  ,  deletedRows  [  i  ]  )  ; 
} 
sort  (  )  ; 
}  else   if  (  e  .  getType  (  )  ==  TableModelEvent  .  UPDATE  )  { 
allocateIndexes  (  )  ; 
sort  (  )  ; 
fireTableDataChanged  (  )  ; 
}  else  { 
logger  .  warn  (  "Doing an unknown table change type: "  +  e  .  getType  (  )  )  ; 
allocateIndexes  (  )  ; 
sort  (  )  ; 
super  .  tableChanged  (  e  )  ; 
} 
} 

private   void   reallocateIndexesOnInsert  (  int   firstRow  ,  int   lastRow  )  { 
int   rowCount  =  filteredModel  .  getRowCount  (  )  ; 
int  [  ]  newIndexes  =  new   int  [  rowCount  ]  ; 
for  (  int   row  =  0  ;  row  <  indexes  .  length  ;  row  ++  )  { 
newIndexes  [  row  ]  =  indexes  [  row  ]  ; 
} 
for  (  int   row  =  firstRow  ;  row  <=  lastRow  ;  row  ++  )  { 
newIndexes  [  row  ]  =  row  ; 
} 
indexes  =  newIndexes  ; 
} 
} 

