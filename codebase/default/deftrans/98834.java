import   junit  .  framework  .  *  ; 
import   java  .  util  .  concurrent  .  locks  .  *  ; 
import   java  .  util  .  concurrent  .  *  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 

public   class   ReentrantReadWriteLockTest   extends   JSR166TestCase  { 

public   static   void   main  (  String  [  ]  args  )  { 
junit  .  textui  .  TestRunner  .  run  (  suite  (  )  )  ; 
} 

public   static   Test   suite  (  )  { 
return   new   TestSuite  (  ReentrantReadWriteLockTest  .  class  )  ; 
} 




class   InterruptibleLockRunnable   implements   Runnable  { 

final   ReentrantReadWriteLock   lock  ; 

InterruptibleLockRunnable  (  ReentrantReadWriteLock   l  )  { 
lock  =  l  ; 
} 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lockInterruptibly  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
} 





class   InterruptedLockRunnable   implements   Runnable  { 

final   ReentrantReadWriteLock   lock  ; 

InterruptedLockRunnable  (  ReentrantReadWriteLock   l  )  { 
lock  =  l  ; 
} 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lockInterruptibly  (  )  ; 
threadShouldThrow  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
} 




static   class   PublicReentrantReadWriteLock   extends   ReentrantReadWriteLock  { 

PublicReentrantReadWriteLock  (  )  { 
super  (  )  ; 
} 

public   Collection  <  Thread  >  getQueuedThreads  (  )  { 
return   super  .  getQueuedThreads  (  )  ; 
} 

public   Collection  <  Thread  >  getWaitingThreads  (  Condition   c  )  { 
return   super  .  getWaitingThreads  (  c  )  ; 
} 
} 




public   void   testConstructor  (  )  { 
ReentrantReadWriteLock   rl  =  new   ReentrantReadWriteLock  (  )  ; 
assertFalse  (  rl  .  isFair  (  )  )  ; 
assertFalse  (  rl  .  isWriteLocked  (  )  )  ; 
assertEquals  (  0  ,  rl  .  getReadLockCount  (  )  )  ; 
ReentrantReadWriteLock   r2  =  new   ReentrantReadWriteLock  (  true  )  ; 
assertTrue  (  r2  .  isFair  (  )  )  ; 
assertFalse  (  r2  .  isWriteLocked  (  )  )  ; 
assertEquals  (  0  ,  r2  .  getReadLockCount  (  )  )  ; 
} 




public   void   testLock  (  )  { 
ReentrantReadWriteLock   rl  =  new   ReentrantReadWriteLock  (  )  ; 
rl  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  rl  .  isWriteLocked  (  )  )  ; 
assertTrue  (  rl  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertEquals  (  0  ,  rl  .  getReadLockCount  (  )  )  ; 
rl  .  writeLock  (  )  .  unlock  (  )  ; 
assertFalse  (  rl  .  isWriteLocked  (  )  )  ; 
assertFalse  (  rl  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertEquals  (  0  ,  rl  .  getReadLockCount  (  )  )  ; 
rl  .  readLock  (  )  .  lock  (  )  ; 
assertFalse  (  rl  .  isWriteLocked  (  )  )  ; 
assertFalse  (  rl  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertEquals  (  1  ,  rl  .  getReadLockCount  (  )  )  ; 
rl  .  readLock  (  )  .  unlock  (  )  ; 
assertFalse  (  rl  .  isWriteLocked  (  )  )  ; 
assertFalse  (  rl  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertEquals  (  0  ,  rl  .  getReadLockCount  (  )  )  ; 
} 




public   void   testFairLock  (  )  { 
ReentrantReadWriteLock   rl  =  new   ReentrantReadWriteLock  (  true  )  ; 
rl  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  rl  .  isWriteLocked  (  )  )  ; 
assertTrue  (  rl  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertEquals  (  0  ,  rl  .  getReadLockCount  (  )  )  ; 
rl  .  writeLock  (  )  .  unlock  (  )  ; 
assertFalse  (  rl  .  isWriteLocked  (  )  )  ; 
assertFalse  (  rl  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertEquals  (  0  ,  rl  .  getReadLockCount  (  )  )  ; 
rl  .  readLock  (  )  .  lock  (  )  ; 
assertFalse  (  rl  .  isWriteLocked  (  )  )  ; 
assertFalse  (  rl  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertEquals  (  1  ,  rl  .  getReadLockCount  (  )  )  ; 
rl  .  readLock  (  )  .  unlock  (  )  ; 
assertFalse  (  rl  .  isWriteLocked  (  )  )  ; 
assertFalse  (  rl  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertEquals  (  0  ,  rl  .  getReadLockCount  (  )  )  ; 
} 




public   void   testGetWriteHoldCount  (  )  { 
ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
for  (  int   i  =  1  ;  i  <=  SIZE  ;  i  ++  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertEquals  (  i  ,  lock  .  getWriteHoldCount  (  )  )  ; 
} 
for  (  int   i  =  SIZE  ;  i  >  0  ;  i  --  )  { 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
assertEquals  (  i  -  1  ,  lock  .  getWriteHoldCount  (  )  )  ; 
} 
} 




public   void   testUnlock_IllegalMonitorStateException  (  )  { 
ReentrantReadWriteLock   rl  =  new   ReentrantReadWriteLock  (  )  ; 
try  { 
rl  .  writeLock  (  )  .  unlock  (  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalMonitorStateException   success  )  { 
} 
} 




public   void   testWriteLockInterruptibly_Interrupted  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lockInterruptibly  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  lockInterruptibly  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t  .  interrupt  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t  .  join  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testWriteTryLock_Interrupted  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  tryLock  (  1000  ,  TimeUnit  .  MILLISECONDS  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  interrupt  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t  .  join  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testReadLockInterruptibly_Interrupted  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  readLock  (  )  .  lockInterruptibly  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t  .  interrupt  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t  .  join  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testReadTryLock_Interrupted  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  readLock  (  )  .  tryLock  (  1000  ,  TimeUnit  .  MILLISECONDS  )  ; 
threadShouldThrow  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  interrupt  (  )  ; 
t  .  join  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testWriteTryLockWhenLocked  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
threadAssertFalse  (  lock  .  writeLock  (  )  .  tryLock  (  )  )  ; 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testReadTryLockWhenLocked  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
threadAssertFalse  (  lock  .  readLock  (  )  .  tryLock  (  )  )  ; 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testMultipleReadLocks  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
threadAssertTrue  (  lock  .  readLock  (  )  .  tryLock  (  )  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testWriteAfterMultipleReadLocks  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  MEDIUM_DELAY_MS  )  ; 
t2  .  join  (  MEDIUM_DELAY_MS  )  ; 
assertTrue  (  !  t1  .  isAlive  (  )  )  ; 
assertTrue  (  !  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testReadAfterWriteLock  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  MEDIUM_DELAY_MS  )  ; 
t2  .  join  (  MEDIUM_DELAY_MS  )  ; 
assertTrue  (  !  t1  .  isAlive  (  )  )  ; 
assertTrue  (  !  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testReadHoldingWriteLock  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  lock  .  readLock  (  )  .  tryLock  (  )  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 





public   void   testReadHoldingWriteLock2  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  MEDIUM_DELAY_MS  )  ; 
t2  .  join  (  MEDIUM_DELAY_MS  )  ; 
assertTrue  (  !  t1  .  isAlive  (  )  )  ; 
assertTrue  (  !  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 





public   void   testReadHoldingWriteLock3  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  MEDIUM_DELAY_MS  )  ; 
t2  .  join  (  MEDIUM_DELAY_MS  )  ; 
assertTrue  (  !  t1  .  isAlive  (  )  )  ; 
assertTrue  (  !  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 





public   void   testWriteHoldingWriteLock4  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  MEDIUM_DELAY_MS  )  ; 
t2  .  join  (  MEDIUM_DELAY_MS  )  ; 
assertTrue  (  !  t1  .  isAlive  (  )  )  ; 
assertTrue  (  !  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testReadHoldingWriteLockFair  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  true  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  lock  .  readLock  (  )  .  tryLock  (  )  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 





public   void   testReadHoldingWriteLockFair2  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  true  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  MEDIUM_DELAY_MS  )  ; 
t2  .  join  (  MEDIUM_DELAY_MS  )  ; 
assertTrue  (  !  t1  .  isAlive  (  )  )  ; 
assertTrue  (  !  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 





public   void   testReadHoldingWriteLockFair3  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  true  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  MEDIUM_DELAY_MS  )  ; 
t2  .  join  (  MEDIUM_DELAY_MS  )  ; 
assertTrue  (  !  t1  .  isAlive  (  )  )  ; 
assertTrue  (  !  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 





public   void   testWriteHoldingWriteLockFair4  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  true  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  lock  .  isWriteLockedByCurrentThread  (  )  )  ; 
assertTrue  (  lock  .  getWriteHoldCount  (  )  ==  1  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  lock  .  getWriteHoldCount  (  )  ==  2  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  MEDIUM_DELAY_MS  )  ; 
t2  .  join  (  MEDIUM_DELAY_MS  )  ; 
assertTrue  (  !  t1  .  isAlive  (  )  )  ; 
assertTrue  (  !  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testTryLockWhenReadLocked  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
threadAssertTrue  (  lock  .  readLock  (  )  .  tryLock  (  )  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testWriteTryLockWhenReadLocked  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
threadAssertFalse  (  lock  .  writeLock  (  )  .  tryLock  (  )  )  ; 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testTryLockWhenReadLockedFair  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  true  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
threadAssertTrue  (  lock  .  readLock  (  )  .  tryLock  (  )  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testWriteTryLockWhenReadLockedFair  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  true  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
threadAssertFalse  (  lock  .  writeLock  (  )  .  tryLock  (  )  )  ; 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  readLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testWriteTryLock_Timeout  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
threadAssertFalse  (  lock  .  writeLock  (  )  .  tryLock  (  1  ,  TimeUnit  .  MILLISECONDS  )  )  ; 
}  catch  (  Exception   ex  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testReadTryLock_Timeout  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
threadAssertFalse  (  lock  .  readLock  (  )  .  tryLock  (  1  ,  TimeUnit  .  MILLISECONDS  )  )  ; 
}  catch  (  Exception   ex  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
t  .  join  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testWriteLockInterruptibly  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
try  { 
lock  .  writeLock  (  )  .  lockInterruptibly  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lockInterruptibly  (  )  ; 
threadShouldThrow  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t  .  interrupt  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t  .  join  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testReadLockInterruptibly  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
try  { 
lock  .  writeLock  (  )  .  lockInterruptibly  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  readLock  (  )  .  lockInterruptibly  (  )  ; 
threadShouldThrow  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t  .  interrupt  (  )  ; 
t  .  join  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testAwait_IllegalMonitor  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
try  { 
c  .  await  (  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalMonitorStateException   success  )  { 
}  catch  (  Exception   ex  )  { 
shouldThrow  (  )  ; 
} 
} 




public   void   testSignal_IllegalMonitor  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
try  { 
c  .  signal  (  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalMonitorStateException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testAwaitNanos_Timeout  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
long   t  =  c  .  awaitNanos  (  100  )  ; 
assertTrue  (  t  <=  0  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testAwait_Timeout  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testAwaitUntil_Timeout  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
java  .  util  .  Date   d  =  new   java  .  util  .  Date  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testAwait  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
c  .  await  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
c  .  signal  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t  .  join  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  t  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 


class   UninterruptableThread   extends   Thread  { 

private   Lock   lock  ; 

private   Condition   c  ; 

public   volatile   boolean   canAwake  =  false  ; 

public   volatile   boolean   interrupted  =  false  ; 

public   volatile   boolean   lockStarted  =  false  ; 

public   UninterruptableThread  (  Lock   lock  ,  Condition   c  )  { 
this  .  lock  =  lock  ; 
this  .  c  =  c  ; 
} 

public   synchronized   void   run  (  )  { 
lock  .  lock  (  )  ; 
lockStarted  =  true  ; 
while  (  !  canAwake  )  { 
c  .  awaitUninterruptibly  (  )  ; 
} 
interrupted  =  isInterrupted  (  )  ; 
lock  .  unlock  (  )  ; 
} 
} 




public   void   testAwaitUninterruptibly  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
UninterruptableThread   thread  =  new   UninterruptableThread  (  lock  .  writeLock  (  )  ,  c  )  ; 
try  { 
thread  .  start  (  )  ; 
while  (  !  thread  .  lockStarted  )  { 
Thread  .  sleep  (  100  )  ; 
} 
lock  .  writeLock  (  )  .  lock  (  )  ; 
try  { 
thread  .  interrupt  (  )  ; 
thread  .  canAwake  =  true  ; 
c  .  signal  (  )  ; 
}  finally  { 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
} 
thread  .  join  (  )  ; 
assertTrue  (  thread  .  interrupted  )  ; 
assertFalse  (  thread  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testAwait_Interrupt  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
c  .  await  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
threadShouldThrow  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t  .  interrupt  (  )  ; 
t  .  join  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  t  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testAwaitNanos_Interrupt  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
c  .  awaitNanos  (  SHORT_DELAY_MS  *  2  *  1000000  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
threadShouldThrow  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t  .  interrupt  (  )  ; 
t  .  join  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  t  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testAwaitUntil_Interrupt  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
java  .  util  .  Date   d  =  new   java  .  util  .  Date  (  )  ; 
c  .  awaitUntil  (  new   java  .  util  .  Date  (  d  .  getTime  (  )  +  10000  )  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
threadShouldThrow  (  )  ; 
}  catch  (  InterruptedException   success  )  { 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t  .  interrupt  (  )  ; 
t  .  join  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  t  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testSignalAll  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
c  .  await  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
c  .  await  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
try  { 
t1  .  start  (  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
c  .  signalAll  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  SHORT_DELAY_MS  )  ; 
t2  .  join  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  t1  .  isAlive  (  )  )  ; 
assertFalse  (  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testSerialization  (  )  { 
ReentrantReadWriteLock   l  =  new   ReentrantReadWriteLock  (  )  ; 
l  .  readLock  (  )  .  lock  (  )  ; 
l  .  readLock  (  )  .  unlock  (  )  ; 
try  { 
ByteArrayOutputStream   bout  =  new   ByteArrayOutputStream  (  10000  )  ; 
ObjectOutputStream   out  =  new   ObjectOutputStream  (  new   BufferedOutputStream  (  bout  )  )  ; 
out  .  writeObject  (  l  )  ; 
out  .  close  (  )  ; 
ByteArrayInputStream   bin  =  new   ByteArrayInputStream  (  bout  .  toByteArray  (  )  )  ; 
ObjectInputStream   in  =  new   ObjectInputStream  (  new   BufferedInputStream  (  bin  )  )  ; 
ReentrantReadWriteLock   r  =  (  ReentrantReadWriteLock  )  in  .  readObject  (  )  ; 
r  .  readLock  (  )  .  lock  (  )  ; 
r  .  readLock  (  )  .  unlock  (  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
unexpectedException  (  )  ; 
} 
} 




public   void   testhasQueuedThreads  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
Thread   t1  =  new   Thread  (  new   InterruptedLockRunnable  (  lock  )  )  ; 
Thread   t2  =  new   Thread  (  new   InterruptibleLockRunnable  (  lock  )  )  ; 
try  { 
assertFalse  (  lock  .  hasQueuedThreads  (  )  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
t1  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  lock  .  hasQueuedThreads  (  )  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  lock  .  hasQueuedThreads  (  )  )  ; 
t1  .  interrupt  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  lock  .  hasQueuedThreads  (  )  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  lock  .  hasQueuedThreads  (  )  )  ; 
t1  .  join  (  )  ; 
t2  .  join  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testHasQueuedThreadNPE  (  )  { 
final   ReentrantReadWriteLock   sync  =  new   ReentrantReadWriteLock  (  )  ; 
try  { 
sync  .  hasQueuedThread  (  null  )  ; 
shouldThrow  (  )  ; 
}  catch  (  NullPointerException   success  )  { 
} 
} 




public   void   testHasQueuedThread  (  )  { 
final   ReentrantReadWriteLock   sync  =  new   ReentrantReadWriteLock  (  )  ; 
Thread   t1  =  new   Thread  (  new   InterruptedLockRunnable  (  sync  )  )  ; 
Thread   t2  =  new   Thread  (  new   InterruptibleLockRunnable  (  sync  )  )  ; 
try  { 
assertFalse  (  sync  .  hasQueuedThread  (  t1  )  )  ; 
assertFalse  (  sync  .  hasQueuedThread  (  t2  )  )  ; 
sync  .  writeLock  (  )  .  lock  (  )  ; 
t1  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  sync  .  hasQueuedThread  (  t1  )  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  sync  .  hasQueuedThread  (  t1  )  )  ; 
assertTrue  (  sync  .  hasQueuedThread  (  t2  )  )  ; 
t1  .  interrupt  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  sync  .  hasQueuedThread  (  t1  )  )  ; 
assertTrue  (  sync  .  hasQueuedThread  (  t2  )  )  ; 
sync  .  writeLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  sync  .  hasQueuedThread  (  t1  )  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  sync  .  hasQueuedThread  (  t2  )  )  ; 
t1  .  join  (  )  ; 
t2  .  join  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetQueueLength  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
Thread   t1  =  new   Thread  (  new   InterruptedLockRunnable  (  lock  )  )  ; 
Thread   t2  =  new   Thread  (  new   InterruptibleLockRunnable  (  lock  )  )  ; 
try  { 
assertEquals  (  0  ,  lock  .  getQueueLength  (  )  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
t1  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertEquals  (  1  ,  lock  .  getQueueLength  (  )  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertEquals  (  2  ,  lock  .  getQueueLength  (  )  )  ; 
t1  .  interrupt  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertEquals  (  1  ,  lock  .  getQueueLength  (  )  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertEquals  (  0  ,  lock  .  getQueueLength  (  )  )  ; 
t1  .  join  (  )  ; 
t2  .  join  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetQueuedThreads  (  )  { 
final   PublicReentrantReadWriteLock   lock  =  new   PublicReentrantReadWriteLock  (  )  ; 
Thread   t1  =  new   Thread  (  new   InterruptedLockRunnable  (  lock  )  )  ; 
Thread   t2  =  new   Thread  (  new   InterruptibleLockRunnable  (  lock  )  )  ; 
try  { 
assertTrue  (  lock  .  getQueuedThreads  (  )  .  isEmpty  (  )  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  lock  .  getQueuedThreads  (  )  .  isEmpty  (  )  )  ; 
t1  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  lock  .  getQueuedThreads  (  )  .  contains  (  t1  )  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  lock  .  getQueuedThreads  (  )  .  contains  (  t1  )  )  ; 
assertTrue  (  lock  .  getQueuedThreads  (  )  .  contains  (  t2  )  )  ; 
t1  .  interrupt  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  lock  .  getQueuedThreads  (  )  .  contains  (  t1  )  )  ; 
assertTrue  (  lock  .  getQueuedThreads  (  )  .  contains  (  t2  )  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
assertTrue  (  lock  .  getQueuedThreads  (  )  .  isEmpty  (  )  )  ; 
t1  .  join  (  )  ; 
t2  .  join  (  )  ; 
}  catch  (  Exception   e  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testHasWaitersNPE  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
try  { 
lock  .  hasWaiters  (  null  )  ; 
shouldThrow  (  )  ; 
}  catch  (  NullPointerException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetWaitQueueLengthNPE  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
try  { 
lock  .  getWaitQueueLength  (  null  )  ; 
shouldThrow  (  )  ; 
}  catch  (  NullPointerException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetWaitingThreadsNPE  (  )  { 
final   PublicReentrantReadWriteLock   lock  =  new   PublicReentrantReadWriteLock  (  )  ; 
try  { 
lock  .  getWaitingThreads  (  null  )  ; 
shouldThrow  (  )  ; 
}  catch  (  NullPointerException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testHasWaitersIAE  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  (  lock  .  writeLock  (  )  .  newCondition  (  )  )  ; 
final   ReentrantReadWriteLock   lock2  =  new   ReentrantReadWriteLock  (  )  ; 
try  { 
lock2  .  hasWaiters  (  c  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalArgumentException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testHasWaitersIMSE  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  (  lock  .  writeLock  (  )  .  newCondition  (  )  )  ; 
try  { 
lock  .  hasWaiters  (  c  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalMonitorStateException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetWaitQueueLengthIAE  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  (  lock  .  writeLock  (  )  .  newCondition  (  )  )  ; 
final   ReentrantReadWriteLock   lock2  =  new   ReentrantReadWriteLock  (  )  ; 
try  { 
lock2  .  getWaitQueueLength  (  c  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalArgumentException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetWaitQueueLengthIMSE  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  (  lock  .  writeLock  (  )  .  newCondition  (  )  )  ; 
try  { 
lock  .  getWaitQueueLength  (  c  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalMonitorStateException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetWaitingThreadsIAE  (  )  { 
final   PublicReentrantReadWriteLock   lock  =  new   PublicReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  (  lock  .  writeLock  (  )  .  newCondition  (  )  )  ; 
final   PublicReentrantReadWriteLock   lock2  =  new   PublicReentrantReadWriteLock  (  )  ; 
try  { 
lock2  .  getWaitingThreads  (  c  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalArgumentException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetWaitingThreadsIMSE  (  )  { 
final   PublicReentrantReadWriteLock   lock  =  new   PublicReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  (  lock  .  writeLock  (  )  .  newCondition  (  )  )  ; 
try  { 
lock  .  getWaitingThreads  (  c  )  ; 
shouldThrow  (  )  ; 
}  catch  (  IllegalMonitorStateException   success  )  { 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testHasWaiters  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  (  lock  .  writeLock  (  )  .  newCondition  (  )  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
threadAssertFalse  (  lock  .  hasWaiters  (  c  )  )  ; 
threadAssertEquals  (  0  ,  lock  .  getWaitQueueLength  (  c  )  )  ; 
c  .  await  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  lock  .  hasWaiters  (  c  )  )  ; 
assertEquals  (  1  ,  lock  .  getWaitQueueLength  (  c  )  )  ; 
c  .  signal  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertFalse  (  lock  .  hasWaiters  (  c  )  )  ; 
assertEquals  (  0  ,  lock  .  getWaitQueueLength  (  c  )  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t  .  join  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  t  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetWaitQueueLength  (  )  { 
final   ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  (  lock  .  writeLock  (  )  .  newCondition  (  )  )  ; 
Thread   t  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
threadAssertFalse  (  lock  .  hasWaiters  (  c  )  )  ; 
threadAssertEquals  (  0  ,  lock  .  getWaitQueueLength  (  c  )  )  ; 
c  .  await  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
try  { 
t  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  lock  .  hasWaiters  (  c  )  )  ; 
assertEquals  (  1  ,  lock  .  getWaitQueueLength  (  c  )  )  ; 
c  .  signal  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertFalse  (  lock  .  hasWaiters  (  c  )  )  ; 
assertEquals  (  0  ,  lock  .  getWaitQueueLength  (  c  )  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t  .  join  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  t  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testGetWaitingThreads  (  )  { 
final   PublicReentrantReadWriteLock   lock  =  new   PublicReentrantReadWriteLock  (  )  ; 
final   Condition   c  =  lock  .  writeLock  (  )  .  newCondition  (  )  ; 
Thread   t1  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
threadAssertTrue  (  lock  .  getWaitingThreads  (  c  )  .  isEmpty  (  )  )  ; 
c  .  await  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
Thread   t2  =  new   Thread  (  new   Runnable  (  )  { 

public   void   run  (  )  { 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
threadAssertFalse  (  lock  .  getWaitingThreads  (  c  )  .  isEmpty  (  )  )  ; 
c  .  await  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
}  catch  (  InterruptedException   e  )  { 
threadUnexpectedException  (  )  ; 
} 
} 
}  )  ; 
try  { 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  lock  .  getWaitingThreads  (  c  )  .  isEmpty  (  )  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
t2  .  start  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertTrue  (  lock  .  hasWaiters  (  c  )  )  ; 
assertTrue  (  lock  .  getWaitingThreads  (  c  )  .  contains  (  t1  )  )  ; 
assertTrue  (  lock  .  getWaitingThreads  (  c  )  .  contains  (  t2  )  )  ; 
c  .  signalAll  (  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
Thread  .  sleep  (  SHORT_DELAY_MS  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
assertFalse  (  lock  .  hasWaiters  (  c  )  )  ; 
assertTrue  (  lock  .  getWaitingThreads  (  c  )  .  isEmpty  (  )  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
t1  .  join  (  SHORT_DELAY_MS  )  ; 
t2  .  join  (  SHORT_DELAY_MS  )  ; 
assertFalse  (  t1  .  isAlive  (  )  )  ; 
assertFalse  (  t2  .  isAlive  (  )  )  ; 
}  catch  (  Exception   ex  )  { 
unexpectedException  (  )  ; 
} 
} 




public   void   testToString  (  )  { 
ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
String   us  =  lock  .  toString  (  )  ; 
assertTrue  (  us  .  indexOf  (  "Write locks = 0"  )  >=  0  )  ; 
assertTrue  (  us  .  indexOf  (  "Read locks = 0"  )  >=  0  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
String   ws  =  lock  .  toString  (  )  ; 
assertTrue  (  ws  .  indexOf  (  "Write locks = 1"  )  >=  0  )  ; 
assertTrue  (  ws  .  indexOf  (  "Read locks = 0"  )  >=  0  )  ; 
lock  .  writeLock  (  )  .  unlock  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
String   rs  =  lock  .  toString  (  )  ; 
assertTrue  (  rs  .  indexOf  (  "Write locks = 0"  )  >=  0  )  ; 
assertTrue  (  rs  .  indexOf  (  "Read locks = 2"  )  >=  0  )  ; 
} 




public   void   testReadLockToString  (  )  { 
ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
String   us  =  lock  .  readLock  (  )  .  toString  (  )  ; 
assertTrue  (  us  .  indexOf  (  "Read locks = 0"  )  >=  0  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
lock  .  readLock  (  )  .  lock  (  )  ; 
String   rs  =  lock  .  readLock  (  )  .  toString  (  )  ; 
assertTrue  (  rs  .  indexOf  (  "Read locks = 2"  )  >=  0  )  ; 
} 




public   void   testWriteLockToString  (  )  { 
ReentrantReadWriteLock   lock  =  new   ReentrantReadWriteLock  (  )  ; 
String   us  =  lock  .  writeLock  (  )  .  toString  (  )  ; 
assertTrue  (  us  .  indexOf  (  "Unlocked"  )  >=  0  )  ; 
lock  .  writeLock  (  )  .  lock  (  )  ; 
String   ls  =  lock  .  writeLock  (  )  .  toString  (  )  ; 
assertTrue  (  ls  .  indexOf  (  "Locked"  )  >=  0  )  ; 
} 
} 

