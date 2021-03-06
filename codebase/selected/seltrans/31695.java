package   com  .  googlecode  .  protobuf  .  netty  .  example  ; 

public   final   class   Calculator  { 

private   Calculator  (  )  { 
} 

public   static   void   registerAllExtensions  (  com  .  google  .  protobuf  .  ExtensionRegistry   registry  )  { 
} 

public   static   final   class   CalcRequest   extends   com  .  google  .  protobuf  .  GeneratedMessage  { 

private   CalcRequest  (  )  { 
} 

private   static   final   CalcRequest   defaultInstance  =  new   CalcRequest  (  )  ; 

public   static   CalcRequest   getDefaultInstance  (  )  { 
return   defaultInstance  ; 
} 

public   CalcRequest   getDefaultInstanceForType  (  )  { 
return   defaultInstance  ; 
} 

public   static   final   com  .  google  .  protobuf  .  Descriptors  .  Descriptor   getDescriptor  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  internal_static_CalcRequest_descriptor  ; 
} 

protected   com  .  google  .  protobuf  .  GeneratedMessage  .  FieldAccessorTable   internalGetFieldAccessorTable  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  internal_static_CalcRequest_fieldAccessorTable  ; 
} 

public   static   final   int   OP1_FIELD_NUMBER  =  1  ; 

private   boolean   hasOp1  ; 

private   int   op1_  =  0  ; 

public   boolean   hasOp1  (  )  { 
return   hasOp1  ; 
} 

public   int   getOp1  (  )  { 
return   op1_  ; 
} 

public   static   final   int   OP2_FIELD_NUMBER  =  2  ; 

private   boolean   hasOp2  ; 

private   int   op2_  =  0  ; 

public   boolean   hasOp2  (  )  { 
return   hasOp2  ; 
} 

public   int   getOp2  (  )  { 
return   op2_  ; 
} 

public   final   boolean   isInitialized  (  )  { 
if  (  !  hasOp1  )  return   false  ; 
if  (  !  hasOp2  )  return   false  ; 
return   true  ; 
} 

public   void   writeTo  (  com  .  google  .  protobuf  .  CodedOutputStream   output  )  throws   java  .  io  .  IOException  { 
if  (  hasOp1  (  )  )  { 
output  .  writeInt32  (  1  ,  getOp1  (  )  )  ; 
} 
if  (  hasOp2  (  )  )  { 
output  .  writeInt32  (  2  ,  getOp2  (  )  )  ; 
} 
getUnknownFields  (  )  .  writeTo  (  output  )  ; 
} 

private   int   memoizedSerializedSize  =  -  1  ; 

public   int   getSerializedSize  (  )  { 
int   size  =  memoizedSerializedSize  ; 
if  (  size  !=  -  1  )  return   size  ; 
size  =  0  ; 
if  (  hasOp1  (  )  )  { 
size  +=  com  .  google  .  protobuf  .  CodedOutputStream  .  computeInt32Size  (  1  ,  getOp1  (  )  )  ; 
} 
if  (  hasOp2  (  )  )  { 
size  +=  com  .  google  .  protobuf  .  CodedOutputStream  .  computeInt32Size  (  2  ,  getOp2  (  )  )  ; 
} 
size  +=  getUnknownFields  (  )  .  getSerializedSize  (  )  ; 
memoizedSerializedSize  =  size  ; 
return   size  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseFrom  (  com  .  google  .  protobuf  .  ByteString   data  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
return   newBuilder  (  )  .  mergeFrom  (  data  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseFrom  (  com  .  google  .  protobuf  .  ByteString   data  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
return   newBuilder  (  )  .  mergeFrom  (  data  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseFrom  (  byte  [  ]  data  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
return   newBuilder  (  )  .  mergeFrom  (  data  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseFrom  (  byte  [  ]  data  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
return   newBuilder  (  )  .  mergeFrom  (  data  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseFrom  (  java  .  io  .  InputStream   input  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeFrom  (  input  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseFrom  (  java  .  io  .  InputStream   input  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeFrom  (  input  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseDelimitedFrom  (  java  .  io  .  InputStream   input  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeDelimitedFrom  (  input  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseDelimitedFrom  (  java  .  io  .  InputStream   input  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeDelimitedFrom  (  input  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseFrom  (  com  .  google  .  protobuf  .  CodedInputStream   input  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeFrom  (  input  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   parseFrom  (  com  .  google  .  protobuf  .  CodedInputStream   input  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeFrom  (  input  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   Builder   newBuilder  (  )  { 
return   Builder  .  create  (  )  ; 
} 

public   Builder   newBuilderForType  (  )  { 
return   newBuilder  (  )  ; 
} 

public   static   Builder   newBuilder  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   prototype  )  { 
return   newBuilder  (  )  .  mergeFrom  (  prototype  )  ; 
} 

public   Builder   toBuilder  (  )  { 
return   newBuilder  (  this  )  ; 
} 

public   static   final   class   Builder   extends   com  .  google  .  protobuf  .  GeneratedMessage  .  Builder  <  Builder  >  { 

private   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   result  ; 

private   Builder  (  )  { 
} 

private   static   Builder   create  (  )  { 
Builder   builder  =  new   Builder  (  )  ; 
builder  .  result  =  new   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  (  )  ; 
return   builder  ; 
} 

protected   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   internalGetResult  (  )  { 
return   result  ; 
} 

public   Builder   clear  (  )  { 
if  (  result  ==  null  )  { 
throw   new   IllegalStateException  (  "Cannot call clear() after build()."  )  ; 
} 
result  =  new   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  (  )  ; 
return   this  ; 
} 

public   Builder   clone  (  )  { 
return   create  (  )  .  mergeFrom  (  result  )  ; 
} 

public   com  .  google  .  protobuf  .  Descriptors  .  Descriptor   getDescriptorForType  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDescriptor  (  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   getDefaultInstanceForType  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
} 

public   boolean   isInitialized  (  )  { 
return   result  .  isInitialized  (  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   build  (  )  { 
if  (  result  !=  null  &&  !  isInitialized  (  )  )  { 
throw   newUninitializedMessageException  (  result  )  ; 
} 
return   buildPartial  (  )  ; 
} 

private   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   buildParsed  (  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
if  (  !  isInitialized  (  )  )  { 
throw   newUninitializedMessageException  (  result  )  .  asInvalidProtocolBufferException  (  )  ; 
} 
return   buildPartial  (  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   buildPartial  (  )  { 
if  (  result  ==  null  )  { 
throw   new   IllegalStateException  (  "build() has already been called on this Builder."  )  ; 
} 
com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   returnMe  =  result  ; 
result  =  null  ; 
return   returnMe  ; 
} 

public   Builder   mergeFrom  (  com  .  google  .  protobuf  .  Message   other  )  { 
if  (  other   instanceof   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  { 
return   mergeFrom  (  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  other  )  ; 
}  else  { 
super  .  mergeFrom  (  other  )  ; 
return   this  ; 
} 
} 

public   Builder   mergeFrom  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   other  )  { 
if  (  other  ==  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  )  return   this  ; 
if  (  other  .  hasOp1  (  )  )  { 
setOp1  (  other  .  getOp1  (  )  )  ; 
} 
if  (  other  .  hasOp2  (  )  )  { 
setOp2  (  other  .  getOp2  (  )  )  ; 
} 
this  .  mergeUnknownFields  (  other  .  getUnknownFields  (  )  )  ; 
return   this  ; 
} 

public   Builder   mergeFrom  (  com  .  google  .  protobuf  .  CodedInputStream   input  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   java  .  io  .  IOException  { 
com  .  google  .  protobuf  .  UnknownFieldSet  .  Builder   unknownFields  =  com  .  google  .  protobuf  .  UnknownFieldSet  .  newBuilder  (  this  .  getUnknownFields  (  )  )  ; 
while  (  true  )  { 
int   tag  =  input  .  readTag  (  )  ; 
switch  (  tag  )  { 
case   0  : 
this  .  setUnknownFields  (  unknownFields  .  build  (  )  )  ; 
return   this  ; 
default  : 
{ 
if  (  !  parseUnknownField  (  input  ,  unknownFields  ,  extensionRegistry  ,  tag  )  )  { 
this  .  setUnknownFields  (  unknownFields  .  build  (  )  )  ; 
return   this  ; 
} 
break  ; 
} 
case   8  : 
{ 
setOp1  (  input  .  readInt32  (  )  )  ; 
break  ; 
} 
case   16  : 
{ 
setOp2  (  input  .  readInt32  (  )  )  ; 
break  ; 
} 
} 
} 
} 

public   boolean   hasOp1  (  )  { 
return   result  .  hasOp1  (  )  ; 
} 

public   int   getOp1  (  )  { 
return   result  .  getOp1  (  )  ; 
} 

public   Builder   setOp1  (  int   value  )  { 
result  .  hasOp1  =  true  ; 
result  .  op1_  =  value  ; 
return   this  ; 
} 

public   Builder   clearOp1  (  )  { 
result  .  hasOp1  =  false  ; 
result  .  op1_  =  0  ; 
return   this  ; 
} 

public   boolean   hasOp2  (  )  { 
return   result  .  hasOp2  (  )  ; 
} 

public   int   getOp2  (  )  { 
return   result  .  getOp2  (  )  ; 
} 

public   Builder   setOp2  (  int   value  )  { 
result  .  hasOp2  =  true  ; 
result  .  op2_  =  value  ; 
return   this  ; 
} 

public   Builder   clearOp2  (  )  { 
result  .  hasOp2  =  false  ; 
result  .  op2_  =  0  ; 
return   this  ; 
} 
} 

static  { 
com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  getDescriptor  (  )  ; 
} 

static  { 
com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  internalForceInit  (  )  ; 
} 
} 

public   static   final   class   CalcResponse   extends   com  .  google  .  protobuf  .  GeneratedMessage  { 

private   CalcResponse  (  )  { 
} 

private   static   final   CalcResponse   defaultInstance  =  new   CalcResponse  (  )  ; 

public   static   CalcResponse   getDefaultInstance  (  )  { 
return   defaultInstance  ; 
} 

public   CalcResponse   getDefaultInstanceForType  (  )  { 
return   defaultInstance  ; 
} 

public   static   final   com  .  google  .  protobuf  .  Descriptors  .  Descriptor   getDescriptor  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  internal_static_CalcResponse_descriptor  ; 
} 

protected   com  .  google  .  protobuf  .  GeneratedMessage  .  FieldAccessorTable   internalGetFieldAccessorTable  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  internal_static_CalcResponse_fieldAccessorTable  ; 
} 

public   static   final   int   RESULT_FIELD_NUMBER  =  1  ; 

private   boolean   hasResult  ; 

private   int   result_  =  0  ; 

public   boolean   hasResult  (  )  { 
return   hasResult  ; 
} 

public   int   getResult  (  )  { 
return   result_  ; 
} 

public   final   boolean   isInitialized  (  )  { 
if  (  !  hasResult  )  return   false  ; 
return   true  ; 
} 

public   void   writeTo  (  com  .  google  .  protobuf  .  CodedOutputStream   output  )  throws   java  .  io  .  IOException  { 
if  (  hasResult  (  )  )  { 
output  .  writeInt32  (  1  ,  getResult  (  )  )  ; 
} 
getUnknownFields  (  )  .  writeTo  (  output  )  ; 
} 

private   int   memoizedSerializedSize  =  -  1  ; 

public   int   getSerializedSize  (  )  { 
int   size  =  memoizedSerializedSize  ; 
if  (  size  !=  -  1  )  return   size  ; 
size  =  0  ; 
if  (  hasResult  (  )  )  { 
size  +=  com  .  google  .  protobuf  .  CodedOutputStream  .  computeInt32Size  (  1  ,  getResult  (  )  )  ; 
} 
size  +=  getUnknownFields  (  )  .  getSerializedSize  (  )  ; 
memoizedSerializedSize  =  size  ; 
return   size  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseFrom  (  com  .  google  .  protobuf  .  ByteString   data  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
return   newBuilder  (  )  .  mergeFrom  (  data  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseFrom  (  com  .  google  .  protobuf  .  ByteString   data  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
return   newBuilder  (  )  .  mergeFrom  (  data  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseFrom  (  byte  [  ]  data  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
return   newBuilder  (  )  .  mergeFrom  (  data  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseFrom  (  byte  [  ]  data  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
return   newBuilder  (  )  .  mergeFrom  (  data  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseFrom  (  java  .  io  .  InputStream   input  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeFrom  (  input  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseFrom  (  java  .  io  .  InputStream   input  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeFrom  (  input  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseDelimitedFrom  (  java  .  io  .  InputStream   input  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeDelimitedFrom  (  input  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseDelimitedFrom  (  java  .  io  .  InputStream   input  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeDelimitedFrom  (  input  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseFrom  (  com  .  google  .  protobuf  .  CodedInputStream   input  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeFrom  (  input  )  .  buildParsed  (  )  ; 
} 

public   static   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   parseFrom  (  com  .  google  .  protobuf  .  CodedInputStream   input  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   java  .  io  .  IOException  { 
return   newBuilder  (  )  .  mergeFrom  (  input  ,  extensionRegistry  )  .  buildParsed  (  )  ; 
} 

public   static   Builder   newBuilder  (  )  { 
return   Builder  .  create  (  )  ; 
} 

public   Builder   newBuilderForType  (  )  { 
return   newBuilder  (  )  ; 
} 

public   static   Builder   newBuilder  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   prototype  )  { 
return   newBuilder  (  )  .  mergeFrom  (  prototype  )  ; 
} 

public   Builder   toBuilder  (  )  { 
return   newBuilder  (  this  )  ; 
} 

public   static   final   class   Builder   extends   com  .  google  .  protobuf  .  GeneratedMessage  .  Builder  <  Builder  >  { 

private   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   result  ; 

private   Builder  (  )  { 
} 

private   static   Builder   create  (  )  { 
Builder   builder  =  new   Builder  (  )  ; 
builder  .  result  =  new   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  (  )  ; 
return   builder  ; 
} 

protected   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   internalGetResult  (  )  { 
return   result  ; 
} 

public   Builder   clear  (  )  { 
if  (  result  ==  null  )  { 
throw   new   IllegalStateException  (  "Cannot call clear() after build()."  )  ; 
} 
result  =  new   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  (  )  ; 
return   this  ; 
} 

public   Builder   clone  (  )  { 
return   create  (  )  .  mergeFrom  (  result  )  ; 
} 

public   com  .  google  .  protobuf  .  Descriptors  .  Descriptor   getDescriptorForType  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDescriptor  (  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   getDefaultInstanceForType  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
} 

public   boolean   isInitialized  (  )  { 
return   result  .  isInitialized  (  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   build  (  )  { 
if  (  result  !=  null  &&  !  isInitialized  (  )  )  { 
throw   newUninitializedMessageException  (  result  )  ; 
} 
return   buildPartial  (  )  ; 
} 

private   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   buildParsed  (  )  throws   com  .  google  .  protobuf  .  InvalidProtocolBufferException  { 
if  (  !  isInitialized  (  )  )  { 
throw   newUninitializedMessageException  (  result  )  .  asInvalidProtocolBufferException  (  )  ; 
} 
return   buildPartial  (  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   buildPartial  (  )  { 
if  (  result  ==  null  )  { 
throw   new   IllegalStateException  (  "build() has already been called on this Builder."  )  ; 
} 
com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   returnMe  =  result  ; 
result  =  null  ; 
return   returnMe  ; 
} 

public   Builder   mergeFrom  (  com  .  google  .  protobuf  .  Message   other  )  { 
if  (  other   instanceof   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  )  { 
return   mergeFrom  (  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  )  other  )  ; 
}  else  { 
super  .  mergeFrom  (  other  )  ; 
return   this  ; 
} 
} 

public   Builder   mergeFrom  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   other  )  { 
if  (  other  ==  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  return   this  ; 
if  (  other  .  hasResult  (  )  )  { 
setResult  (  other  .  getResult  (  )  )  ; 
} 
this  .  mergeUnknownFields  (  other  .  getUnknownFields  (  )  )  ; 
return   this  ; 
} 

public   Builder   mergeFrom  (  com  .  google  .  protobuf  .  CodedInputStream   input  ,  com  .  google  .  protobuf  .  ExtensionRegistryLite   extensionRegistry  )  throws   java  .  io  .  IOException  { 
com  .  google  .  protobuf  .  UnknownFieldSet  .  Builder   unknownFields  =  com  .  google  .  protobuf  .  UnknownFieldSet  .  newBuilder  (  this  .  getUnknownFields  (  )  )  ; 
while  (  true  )  { 
int   tag  =  input  .  readTag  (  )  ; 
switch  (  tag  )  { 
case   0  : 
this  .  setUnknownFields  (  unknownFields  .  build  (  )  )  ; 
return   this  ; 
default  : 
{ 
if  (  !  parseUnknownField  (  input  ,  unknownFields  ,  extensionRegistry  ,  tag  )  )  { 
this  .  setUnknownFields  (  unknownFields  .  build  (  )  )  ; 
return   this  ; 
} 
break  ; 
} 
case   8  : 
{ 
setResult  (  input  .  readInt32  (  )  )  ; 
break  ; 
} 
} 
} 
} 

public   boolean   hasResult  (  )  { 
return   result  .  hasResult  (  )  ; 
} 

public   int   getResult  (  )  { 
return   result  .  getResult  (  )  ; 
} 

public   Builder   setResult  (  int   value  )  { 
result  .  hasResult  =  true  ; 
result  .  result_  =  value  ; 
return   this  ; 
} 

public   Builder   clearResult  (  )  { 
result  .  hasResult  =  false  ; 
result  .  result_  =  0  ; 
return   this  ; 
} 
} 

static  { 
com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  getDescriptor  (  )  ; 
} 

static  { 
com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  internalForceInit  (  )  ; 
} 
} 

public   abstract   static   class   CalcService   implements   com  .  google  .  protobuf  .  Service  { 

protected   CalcService  (  )  { 
} 

public   interface   Interface  { 

public   abstract   void   add  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  ; 

public   abstract   void   subtract  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  ; 

public   abstract   void   multiply  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  ; 

public   abstract   void   divide  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  ; 
} 

public   static   com  .  google  .  protobuf  .  Service   newReflectiveService  (  final   Interface   impl  )  { 
return   new   CalcService  (  )  { 

@  Override 
public   void   add  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  { 
impl  .  add  (  controller  ,  request  ,  done  )  ; 
} 

@  Override 
public   void   subtract  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  { 
impl  .  subtract  (  controller  ,  request  ,  done  )  ; 
} 

@  Override 
public   void   multiply  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  { 
impl  .  multiply  (  controller  ,  request  ,  done  )  ; 
} 

@  Override 
public   void   divide  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  { 
impl  .  divide  (  controller  ,  request  ,  done  )  ; 
} 
}  ; 
} 

public   static   com  .  google  .  protobuf  .  BlockingService   newReflectiveBlockingService  (  final   BlockingInterface   impl  )  { 
return   new   com  .  google  .  protobuf  .  BlockingService  (  )  { 

public   final   com  .  google  .  protobuf  .  Descriptors  .  ServiceDescriptor   getDescriptorForType  (  )  { 
return   getDescriptor  (  )  ; 
} 

public   final   com  .  google  .  protobuf  .  Message   callBlockingMethod  (  com  .  google  .  protobuf  .  Descriptors  .  MethodDescriptor   method  ,  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  google  .  protobuf  .  Message   request  )  throws   com  .  google  .  protobuf  .  ServiceException  { 
if  (  method  .  getService  (  )  !=  getDescriptor  (  )  )  { 
throw   new   java  .  lang  .  IllegalArgumentException  (  "Service.callBlockingMethod() given method descriptor for "  +  "wrong service type."  )  ; 
} 
switch  (  method  .  getIndex  (  )  )  { 
case   0  : 
return   impl  .  add  (  controller  ,  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  request  )  ; 
case   1  : 
return   impl  .  subtract  (  controller  ,  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  request  )  ; 
case   2  : 
return   impl  .  multiply  (  controller  ,  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  request  )  ; 
case   3  : 
return   impl  .  divide  (  controller  ,  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  request  )  ; 
default  : 
throw   new   java  .  lang  .  AssertionError  (  "Can't get here."  )  ; 
} 
} 

public   final   com  .  google  .  protobuf  .  Message   getRequestPrototype  (  com  .  google  .  protobuf  .  Descriptors  .  MethodDescriptor   method  )  { 
if  (  method  .  getService  (  )  !=  getDescriptor  (  )  )  { 
throw   new   java  .  lang  .  IllegalArgumentException  (  "Service.getRequestPrototype() given method "  +  "descriptor for wrong service type."  )  ; 
} 
switch  (  method  .  getIndex  (  )  )  { 
case   0  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
case   1  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
case   2  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
case   3  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
default  : 
throw   new   java  .  lang  .  AssertionError  (  "Can't get here."  )  ; 
} 
} 

public   final   com  .  google  .  protobuf  .  Message   getResponsePrototype  (  com  .  google  .  protobuf  .  Descriptors  .  MethodDescriptor   method  )  { 
if  (  method  .  getService  (  )  !=  getDescriptor  (  )  )  { 
throw   new   java  .  lang  .  IllegalArgumentException  (  "Service.getResponsePrototype() given method "  +  "descriptor for wrong service type."  )  ; 
} 
switch  (  method  .  getIndex  (  )  )  { 
case   0  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
case   1  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
case   2  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
case   3  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
default  : 
throw   new   java  .  lang  .  AssertionError  (  "Can't get here."  )  ; 
} 
} 
}  ; 
} 

public   abstract   void   add  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  ; 

public   abstract   void   subtract  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  ; 

public   abstract   void   multiply  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  ; 

public   abstract   void   divide  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  ; 

public   static   final   com  .  google  .  protobuf  .  Descriptors  .  ServiceDescriptor   getDescriptor  (  )  { 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  getDescriptor  (  )  .  getServices  (  )  .  get  (  0  )  ; 
} 

public   final   com  .  google  .  protobuf  .  Descriptors  .  ServiceDescriptor   getDescriptorForType  (  )  { 
return   getDescriptor  (  )  ; 
} 

public   final   void   callMethod  (  com  .  google  .  protobuf  .  Descriptors  .  MethodDescriptor   method  ,  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  google  .  protobuf  .  Message   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  google  .  protobuf  .  Message  >  done  )  { 
if  (  method  .  getService  (  )  !=  getDescriptor  (  )  )  { 
throw   new   java  .  lang  .  IllegalArgumentException  (  "Service.callMethod() given method descriptor for wrong "  +  "service type."  )  ; 
} 
switch  (  method  .  getIndex  (  )  )  { 
case   0  : 
this  .  add  (  controller  ,  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  request  ,  com  .  google  .  protobuf  .  RpcUtil  .  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  specializeCallback  (  done  )  )  ; 
return  ; 
case   1  : 
this  .  subtract  (  controller  ,  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  request  ,  com  .  google  .  protobuf  .  RpcUtil  .  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  specializeCallback  (  done  )  )  ; 
return  ; 
case   2  : 
this  .  multiply  (  controller  ,  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  request  ,  com  .  google  .  protobuf  .  RpcUtil  .  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  specializeCallback  (  done  )  )  ; 
return  ; 
case   3  : 
this  .  divide  (  controller  ,  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  )  request  ,  com  .  google  .  protobuf  .  RpcUtil  .  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  specializeCallback  (  done  )  )  ; 
return  ; 
default  : 
throw   new   java  .  lang  .  AssertionError  (  "Can't get here."  )  ; 
} 
} 

public   final   com  .  google  .  protobuf  .  Message   getRequestPrototype  (  com  .  google  .  protobuf  .  Descriptors  .  MethodDescriptor   method  )  { 
if  (  method  .  getService  (  )  !=  getDescriptor  (  )  )  { 
throw   new   java  .  lang  .  IllegalArgumentException  (  "Service.getRequestPrototype() given method "  +  "descriptor for wrong service type."  )  ; 
} 
switch  (  method  .  getIndex  (  )  )  { 
case   0  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
case   1  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
case   2  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
case   3  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  getDefaultInstance  (  )  ; 
default  : 
throw   new   java  .  lang  .  AssertionError  (  "Can't get here."  )  ; 
} 
} 

public   final   com  .  google  .  protobuf  .  Message   getResponsePrototype  (  com  .  google  .  protobuf  .  Descriptors  .  MethodDescriptor   method  )  { 
if  (  method  .  getService  (  )  !=  getDescriptor  (  )  )  { 
throw   new   java  .  lang  .  IllegalArgumentException  (  "Service.getResponsePrototype() given method "  +  "descriptor for wrong service type."  )  ; 
} 
switch  (  method  .  getIndex  (  )  )  { 
case   0  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
case   1  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
case   2  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
case   3  : 
return   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ; 
default  : 
throw   new   java  .  lang  .  AssertionError  (  "Can't get here."  )  ; 
} 
} 

public   static   Stub   newStub  (  com  .  google  .  protobuf  .  RpcChannel   channel  )  { 
return   new   Stub  (  channel  )  ; 
} 

public   static   final   class   Stub   extends   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcService   implements   Interface  { 

private   Stub  (  com  .  google  .  protobuf  .  RpcChannel   channel  )  { 
this  .  channel  =  channel  ; 
} 

private   final   com  .  google  .  protobuf  .  RpcChannel   channel  ; 

public   com  .  google  .  protobuf  .  RpcChannel   getChannel  (  )  { 
return   channel  ; 
} 

public   void   add  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  { 
channel  .  callMethod  (  getDescriptor  (  )  .  getMethods  (  )  .  get  (  0  )  ,  controller  ,  request  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ,  com  .  google  .  protobuf  .  RpcUtil  .  generalizeCallback  (  done  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  class  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  )  ; 
} 

public   void   subtract  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  { 
channel  .  callMethod  (  getDescriptor  (  )  .  getMethods  (  )  .  get  (  1  )  ,  controller  ,  request  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ,  com  .  google  .  protobuf  .  RpcUtil  .  generalizeCallback  (  done  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  class  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  )  ; 
} 

public   void   multiply  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  { 
channel  .  callMethod  (  getDescriptor  (  )  .  getMethods  (  )  .  get  (  2  )  ,  controller  ,  request  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ,  com  .  google  .  protobuf  .  RpcUtil  .  generalizeCallback  (  done  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  class  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  )  ; 
} 

public   void   divide  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  ,  com  .  google  .  protobuf  .  RpcCallback  <  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  >  done  )  { 
channel  .  callMethod  (  getDescriptor  (  )  .  getMethods  (  )  .  get  (  3  )  ,  controller  ,  request  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  ,  com  .  google  .  protobuf  .  RpcUtil  .  generalizeCallback  (  done  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  class  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  )  ; 
} 
} 

public   static   BlockingInterface   newBlockingStub  (  com  .  google  .  protobuf  .  BlockingRpcChannel   channel  )  { 
return   new   BlockingStub  (  channel  )  ; 
} 

public   interface   BlockingInterface  { 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   add  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  )  throws   com  .  google  .  protobuf  .  ServiceException  ; 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   subtract  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  )  throws   com  .  google  .  protobuf  .  ServiceException  ; 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   multiply  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  )  throws   com  .  google  .  protobuf  .  ServiceException  ; 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   divide  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  )  throws   com  .  google  .  protobuf  .  ServiceException  ; 
} 

private   static   final   class   BlockingStub   implements   BlockingInterface  { 

private   BlockingStub  (  com  .  google  .  protobuf  .  BlockingRpcChannel   channel  )  { 
this  .  channel  =  channel  ; 
} 

private   final   com  .  google  .  protobuf  .  BlockingRpcChannel   channel  ; 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   add  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  )  throws   com  .  google  .  protobuf  .  ServiceException  { 
return  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  )  channel  .  callBlockingMethod  (  getDescriptor  (  )  .  getMethods  (  )  .  get  (  0  )  ,  controller  ,  request  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   subtract  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  )  throws   com  .  google  .  protobuf  .  ServiceException  { 
return  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  )  channel  .  callBlockingMethod  (  getDescriptor  (  )  .  getMethods  (  )  .  get  (  1  )  ,  controller  ,  request  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   multiply  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  )  throws   com  .  google  .  protobuf  .  ServiceException  { 
return  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  )  channel  .  callBlockingMethod  (  getDescriptor  (  )  .  getMethods  (  )  .  get  (  2  )  ,  controller  ,  request  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  ; 
} 

public   com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse   divide  (  com  .  google  .  protobuf  .  RpcController   controller  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest   request  )  throws   com  .  google  .  protobuf  .  ServiceException  { 
return  (  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  )  channel  .  callBlockingMethod  (  getDescriptor  (  )  .  getMethods  (  )  .  get  (  3  )  ,  controller  ,  request  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  getDefaultInstance  (  )  )  ; 
} 
} 
} 

private   static   com  .  google  .  protobuf  .  Descriptors  .  Descriptor   internal_static_CalcRequest_descriptor  ; 

private   static   com  .  google  .  protobuf  .  GeneratedMessage  .  FieldAccessorTable   internal_static_CalcRequest_fieldAccessorTable  ; 

private   static   com  .  google  .  protobuf  .  Descriptors  .  Descriptor   internal_static_CalcResponse_descriptor  ; 

private   static   com  .  google  .  protobuf  .  GeneratedMessage  .  FieldAccessorTable   internal_static_CalcResponse_fieldAccessorTable  ; 

public   static   com  .  google  .  protobuf  .  Descriptors  .  FileDescriptor   getDescriptor  (  )  { 
return   descriptor  ; 
} 

private   static   com  .  google  .  protobuf  .  Descriptors  .  FileDescriptor   descriptor  ; 

static  { 
java  .  lang  .  String  [  ]  descriptorData  =  {  "\n\rexample.proto\"\'\n\013CalcRequest\022\013\n\003op1\030\001 "  +  "\002(\005\022\013\n\003op2\030\002 \002(\005\"\036\n\014CalcResponse\022\016\n\006resu"  +  "lt\030\001 \002(\0052\252\001\n\013CalcService\022\"\n\003Add\022\014.CalcRe"  +  "quest\032\r.CalcResponse\022\'\n\010Subtract\022\014.CalcR"  +  "equest\032\r.CalcResponse\022\'\n\010Multiply\022\014.Calc"  +  "Request\032\r.CalcResponse\022%\n\006Divide\022\014.CalcR"  +  "equest\032\r.CalcResponseB3\n%com.googlecode."  +  "protobuf.netty.exampleB\nCalculator"  }  ; 
com  .  google  .  protobuf  .  Descriptors  .  FileDescriptor  .  InternalDescriptorAssigner   assigner  =  new   com  .  google  .  protobuf  .  Descriptors  .  FileDescriptor  .  InternalDescriptorAssigner  (  )  { 

public   com  .  google  .  protobuf  .  ExtensionRegistry   assignDescriptors  (  com  .  google  .  protobuf  .  Descriptors  .  FileDescriptor   root  )  { 
descriptor  =  root  ; 
internal_static_CalcRequest_descriptor  =  getDescriptor  (  )  .  getMessageTypes  (  )  .  get  (  0  )  ; 
internal_static_CalcRequest_fieldAccessorTable  =  new   com  .  google  .  protobuf  .  GeneratedMessage  .  FieldAccessorTable  (  internal_static_CalcRequest_descriptor  ,  new   java  .  lang  .  String  [  ]  {  "Op1"  ,  "Op2"  }  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  class  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcRequest  .  Builder  .  class  )  ; 
internal_static_CalcResponse_descriptor  =  getDescriptor  (  )  .  getMessageTypes  (  )  .  get  (  1  )  ; 
internal_static_CalcResponse_fieldAccessorTable  =  new   com  .  google  .  protobuf  .  GeneratedMessage  .  FieldAccessorTable  (  internal_static_CalcResponse_descriptor  ,  new   java  .  lang  .  String  [  ]  {  "Result"  }  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  class  ,  com  .  googlecode  .  protobuf  .  netty  .  example  .  Calculator  .  CalcResponse  .  Builder  .  class  )  ; 
return   null  ; 
} 
}  ; 
com  .  google  .  protobuf  .  Descriptors  .  FileDescriptor  .  internalBuildGeneratedFileFrom  (  descriptorData  ,  new   com  .  google  .  protobuf  .  Descriptors  .  FileDescriptor  [  ]  {  }  ,  assigner  )  ; 
} 

public   static   void   internalForceInit  (  )  { 
} 
} 

