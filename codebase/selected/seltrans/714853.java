package   org  .  lm2a  .  client  ; 

import   java  .  io  .  FileNotFoundException  ; 
import   java  .  io  .  FileOutputStream  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  OutputStream  ; 
import   java  .  io  .  PrintStream  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  ServerSocket  ; 
import   java  .  net  .  Socket  ; 
import   java  .  net  .  URL  ; 
import   java  .  util  .  Date  ; 
import   java  .  util  .  Timer  ; 
import   java  .  util  .  TimerTask  ; 
import   org  .  lm2a  .  exceptions  .  CoverlessException  ; 
import   player  .  PlayerListener  ; 

public   class   SynchronizedResumeControlHTTP   implements   PlayerListener  { 

private   static   final   int   PUERTO  =  5678  ; 

private   static   final   int   BUFFER  =  512  ; 

private   URL   url  ; 

private   FileOutputStream   fout  ; 

private   String   theFile  =  null  ; 

private   HttpURLConnection   httpURLConnection  ; 

private   boolean   conectado  ; 

private   ServerSocket   conex  ; 

private   int   longitudArchivo  =  0  ; 

int   offset  =  0  ; 

int   bytesread  =  0  ; 

byte  [  ]  b  =  null  ; 

private   InputStream   archivoInputStream  ; 

int   puntoInterrupcion  =  0  ; 

boolean   reconnected  =  false  ; 

int   estadoPlayer  =  0  ; 

private   static   final   int   STOP  =  1  ; 

private   static   final   int   PLAY  =  2  ; 

private   static   final   int   PAUSE  =  3  ; 

boolean   lastChunk  =  false  ; 

boolean   request  =  false  ; 

private   boolean   espero  =  true  ; 

private   OutputStream   outCliente  ; 

private   Socket   cliente  ; 

public   void   irURL  (  String   argumento  )  { 
try  { 
url  =  new   URL  (  argumento  )  ; 
String   protocol  =  url  .  getProtocol  (  )  ; 
if  (  !  protocol  .  equals  (  "http"  )  )  { 
throw   new   IllegalArgumentException  (  "URL debe usar el protocolo 'http:'"  )  ; 
} 
}  catch  (  MalformedURLException   e  )  { 
error  (  "ResumeControlHTTP(String[] argumentos): "  +  url  +  " no es una buena URL."  )  ; 
e  .  printStackTrace  (  )  ; 
} 
urlParse  (  url  )  ; 
} 

public   void   urlParse  (  URL   u  )  { 
theFile  =  u  .  getFile  (  )  ; 
ver  (  "urlParse(URL u): Archivo = "  +  theFile  )  ; 
theFile  =  theFile  .  substring  (  theFile  .  lastIndexOf  (  '/'  )  +  1  )  ; 
try  { 
fout  =  new   FileOutputStream  (  theFile  )  ; 
}  catch  (  FileNotFoundException   fnfe  )  { 
error  (  "urlParse(URL u): ERROR #RC3--> Problemas al crear el archivo de salida"  )  ; 
fnfe  .  printStackTrace  (  )  ; 
} 
} 

public   void   proceso  (  )  { 
try  { 
abreConeccion  (  )  ; 
conex  =  new   ServerSocket  (  PUERTO  )  ; 
ver  (  "--> TRACE: CreaServer:run(): Atendiendo pedidos en el puerto "  +  PUERTO  )  ; 
while  (  espero  )  { 
cliente  =  conex  .  accept  (  )  ; 
espero  =  false  ; 
error  (  "CreaServer:run(): Llego el request"  )  ; 
} 
b  =  new   byte  [  longitudArchivo  ]  ; 
String   contentType  =  "liculape/rebebaira"  ; 
outCliente  =  cliente  .  getOutputStream  (  )  ; 
PrintStream   outStream  =  new   PrintStream  (  outCliente  )  ; 
outStream  .  print  (  "HTTP/1.0 200 OK\r\n"  )  ; 
Date   now  =  new   Date  (  )  ; 
outStream  .  print  (  "Date: "  +  now  +  "\r\n"  )  ; 
outStream  .  print  (  "Server: [lm2a] Server 1.0\r\n"  )  ; 
outStream  .  print  (  "Content-length: "  +  longitudArchivo  +  "\r\n"  )  ; 
outStream  .  print  (  "Content-type: "  +  contentType  +  "\r\n\r\n"  )  ; 
}  catch  (  IOException   e  )  { 
error  (  "abrirServerSocket(): ERROR #RC6--> Problemas al abrir conexiones"  )  ; 
e  .  printStackTrace  (  )  ; 
} 
try  { 
leeDesde  (  0  )  ; 
}  catch  (  CoverlessException   cle  )  { 
setConexion  (  false  )  ; 
httpURLConnection  =  null  ; 
longitudArchivo  =  longitudArchivo  -  offset  ; 
error  (  "proceso(): Error--> Palmo cuando intente leer la posicion "  +  offset  +  " bytes"  )  ; 
try  { 
archivoInputStream  .  close  (  )  ; 
}  catch  (  IOException   e  )  { 
ver  (  "proceso(): Excepcion al hacer el close"  )  ; 
e  .  printStackTrace  (  )  ; 
} 
tryConnection  (  offset  )  ; 
cle  .  printStackTrace  (  )  ; 
} 
} 

public   HttpURLConnection   abreConeccion  (  )  throws   IOException  { 
httpURLConnection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
ver  (  "--> TRACE: abreConeccion(): Conexion URL creada"  )  ; 
if  (  httpURLConnection  !=  null  )  { 
setConexion  (  true  )  ; 
archivoInputStream  =  requestHTTP  (  url  ,  httpURLConnection  )  ; 
longitudArchivo  =  leerLongitudArchivo  (  )  ; 
for  (  int   i  =  0  ;  ;  i  ++  )  { 
String   name  =  httpURLConnection  .  getHeaderFieldKey  (  i  )  ; 
String   value  =  httpURLConnection  .  getHeaderField  (  i  )  ; 
if  (  name  ==  null  &&  value  ==  null  )  { 
break  ; 
} 
if  (  name  ==  null  )  { 
System  .  out  .  println  (  "\nServer HTTP version, Response code:"  )  ; 
System  .  out  .  println  (  value  )  ; 
System  .  out  .  print  (  "\n"  )  ; 
}  else  { 
System  .  out  .  println  (  name  +  "="  +  value  +  "\n"  )  ; 
} 
} 
} 
return   httpURLConnection  ; 
} 

public   HttpURLConnection   reAbreConeccion  (  )  throws   IOException  { 
ver  (  "reAbreConeccion(): Intentando crear el socket"  )  ; 
httpURLConnection  =  (  HttpURLConnection  )  url  .  openConnection  (  )  ; 
if  (  httpURLConnection  !=  null  )  { 
setConexion  (  true  )  ; 
reconnected  =  true  ; 
ver  (  "reAbreConeccion(): "  +  " Offset = "  +  offset  +  " LongitudArchivo = "  +  longitudArchivo  )  ; 
archivoInputStream  =  requestRangedHTTP  (  url  ,  httpURLConnection  ,  offset  +  ""  ,  longitudArchivo  +  ""  )  ; 
for  (  int   i  =  0  ;  ;  i  ++  )  { 
String   name  =  httpURLConnection  .  getHeaderFieldKey  (  i  )  ; 
String   value  =  httpURLConnection  .  getHeaderField  (  i  )  ; 
if  (  name  ==  null  &&  value  ==  null  )  { 
break  ; 
} 
if  (  name  ==  null  )  { 
System  .  out  .  println  (  "\nServer HTTP version, Response code:"  )  ; 
System  .  out  .  println  (  value  )  ; 
System  .  out  .  print  (  "\n"  )  ; 
}  else  { 
System  .  out  .  println  (  name  +  "="  +  value  +  "\n"  )  ; 
} 
} 
} 
return   httpURLConnection  ; 
} 

public   int   leerLongitudArchivo  (  )  { 
return   httpURLConnection  .  getContentLength  (  )  ; 
} 

public   void   escribirArchivo  (  byte  [  ]  b  ,  int   off  ,  int   len  )  { 
try  { 
fout  .  write  (  b  ,  off  ,  len  )  ; 
if  (  reconnected  )  { 
ver  (  "escribirArchivo(byte[] b, int off, int len): "  )  ; 
verArray  (  b  ,  off  ,  len  )  ; 
reconnected  =  false  ; 
} 
}  catch  (  IOException   ioe  )  { 
error  (  "escribirArchivo(byte[] b, int off, int len): ERROR #RC4: Problemas al escribir el archivo de salida"  )  ; 
ioe  .  printStackTrace  (  )  ; 
}  catch  (  IndexOutOfBoundsException   iobe  )  { 
error  (  "escribirArchivo(byte[] b, int off, int len): Error de overflow para longitud b = "  +  b  .  length  +  " con off = "  +  off  +  " y len = "  +  len  )  ; 
} 
} 

public   void   abrirServerSocket  (  )  throws   IOException  { 
} 

public   void   tryConnection  (  int   d  )  { 
int   delay  =  5000  ; 
int   period  =  15000  ; 
final   Timer   timer  =  new   Timer  (  )  ; 
ver  (  "tryConnection(int d): d= "  +  d  )  ; 
timer  .  scheduleAtFixedRate  (  new   TimerTask  (  )  { 

public   void   run  (  )  { 
try  { 
httpURLConnection  =  reAbreConeccion  (  )  ; 
ver  (  "tryConnection(int d): Re-Conectado-->"  +  " Offset: "  +  offset  +  " Hasta: "  +  longitudArchivo  )  ; 
if  (  conectado  )  { 
procesoReConexion  (  )  ; 
timer  .  cancel  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
error  (  "tryConnection(int d): Intento de coneccion fallido"  )  ; 
} 
} 
}  ,  delay  ,  period  )  ; 
} 

public   void   procesoReConexion  (  )  { 
puntoInterrupcion  =  offset  ; 
ver  (  "procesoReConexion():"  )  ; 
ver  (  "procesoReConexion(): d = "  +  offset  )  ; 
try  { 
bytesread  =  0  ; 
leeDesde  (  0  )  ; 
}  catch  (  CoverlessException   e  )  { 
error  (  "procesoReConexion() problema: CoverlessException"  )  ; 
e  .  printStackTrace  (  )  ; 
} 
} 

public   void   leeDesde  (  int   posicion  )  throws   CoverlessException  { 
offset  =  posicion  ; 
int   chunk  =  BUFFER  ; 
while  (  bytesread  >=  0  )  { 
if  (  (  longitudArchivo  -  offset  )  <  BUFFER  )  { 
chunk  =  longitudArchivo  -  offset  ; 
} 
try  { 
bytesread  =  archivoInputStream  .  read  (  b  ,  offset  ,  chunk  )  ; 
ver  (  "leeDesde(int posicion): Leyendo y guardando en B en posici�n = "  +  offset  )  ; 
}  catch  (  IOException   e  )  { 
error  (  "leeDesde(int posicion): IOException mientras leia "  +  offset  +  " con chunk = "  +  chunk  )  ; 
throw   new   CoverlessException  (  "leeDesde(int posicion): ERROR #RC7--> Problemas de cobertura"  +  "\n longitud de archivo le�da = "  +  longitudArchivo  +  "\n Offset = "  +  offset  )  ; 
} 
if  (  bytesread  ==  -  1  )  { 
ver  (  "leeDesde(int posicion): Archivo leido"  )  ; 
break  ; 
} 
if  (  bytesread  ==  0  )  { 
ver  (  "leeDesde(int posicion): Archivo leido = 0"  )  ; 
break  ; 
} 
try  { 
outCliente  .  write  (  b  ,  offset  ,  bytesread  )  ; 
outCliente  .  flush  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
escribirArchivo  (  b  ,  offset  ,  bytesread  )  ; 
offset  +=  bytesread  ; 
} 
if  (  offset  !=  longitudArchivo  )  { 
throw   new   CoverlessException  (  "leeDesde(int posicion): ERROR #RC7--> Problemas de cobertura"  +  "\n longitud de archivo le�da = "  +  longitudArchivo  +  "\n Offset = "  +  offset  )  ; 
} 
ver  (  "--> TRACE: leeDesde(int posicion): lectura terminada. Se leyeron "  +  offset  )  ; 
try  { 
fout  .  flush  (  )  ; 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   void   verArray  (  byte  [  ]  b  ,  int   desde  ,  int   cuanto  )  { 
ver  (  "verArray(byte[] b, int desde, int cuanto): Desde = "  +  desde  +  "  Hasta = "  +  cuanto  )  ; 
String   x  =  new   String  (  b  )  ; 
String   y  =  x  .  substring  (  desde  ,  cuanto  )  ; 
ver  (  "verArray(byte[] b, int desde, int cuanto): "  +  y  )  ; 
} 

public   void   setConexion  (  boolean   valor  )  { 
conectado  =  valor  ; 
} 

public   boolean   getConexion  (  )  { 
return   conectado  ; 
} 

public   void   error  (  String   mje  )  { 
System  .  err  .  println  (  mje  )  ; 
} 

public   void   ver  (  String   mje  )  { 
System  .  out  .  println  (  mje  )  ; 
} 

public   InputStream   requestHTTP  (  URL   _url  ,  HttpURLConnection   _httpURLConnection  )  throws   IOException  { 
_httpURLConnection  .  setRequestMethod  (  "GET"  )  ; 
_httpURLConnection  .  connect  (  )  ; 
InputStream   is  =  _httpURLConnection  .  getInputStream  (  )  ; 
int   code  =  _httpURLConnection  .  getResponseCode  (  )  ; 
if  (  code  ==  HttpURLConnection  .  HTTP_OK  )  { 
ver  (  "requestHTTP(URL _url, HttpURLConnection _httpURLConnection): Codigo devuelto = "  +  code  )  ; 
} 
return   is  ; 
} 

public   InputStream   requestRangedHTTP  (  URL   _url  ,  HttpURLConnection   _httpURLConnection  ,  String   desde  ,  String   hasta  )  throws   IOException  { 
_httpURLConnection  .  setRequestMethod  (  "GET"  )  ; 
_httpURLConnection  .  setRequestProperty  (  "Accept-Ranges"  ,  "bytes"  )  ; 
_httpURLConnection  .  setRequestProperty  (  "Range"  ,  "bytes="  +  desde  +  "-"  )  ; 
_httpURLConnection  .  connect  (  )  ; 
InputStream   is  =  _httpURLConnection  .  getInputStream  (  )  ; 
int   code  =  _httpURLConnection  .  getResponseCode  (  )  ; 
if  (  code  ==  HttpURLConnection  .  HTTP_OK  )  { 
ver  (  "requestHTTP(URL _url, HttpURLConnection _httpURLConnection): Codigo devuelto = "  +  code  )  ; 
} 
return   is  ; 
} 

public   void   actualFPS  (  float   arg0  )  { 
} 

public   void   changedState  (  int   arg0  )  { 
this  .  estadoPlayer  =  arg0  ; 
if  (  estadoPlayer  ==  STOP  )  { 
ver  (  "changedState(int arg0: Player STOPPED"  )  ; 
} 
if  (  estadoPlayer  ==  PLAY  )  { 
ver  (  "changedState(int arg0: Player PLAYING"  )  ; 
} 
if  (  estadoPlayer  ==  PAUSE  )  { 
ver  (  "changedState(int arg0: Player PAUSED"  )  ; 
} 
} 

public   void   fpsChanged  (  float   arg0  )  { 
} 

public   void   handleAnchor  (  String   arg0  )  { 
} 

public   void   muteChanged  (  boolean   arg0  )  { 
} 

public   void   playRequestWhenClosed  (  )  { 
} 

public   void   playerSize  (  int   arg0  ,  int   arg1  )  { 
} 

public   void   playerTime  (  long   arg0  )  { 
} 

public   void   preferSize  (  int   arg0  ,  int   arg1  )  { 
} 

public   void   scalingChanged  (  boolean   arg0  )  { 
} 

public   void   serverNTPTimeOrigin  (  long   arg0  )  { 
} 

public   void   speedChanged  (  double   arg0  )  { 
} 

public   void   speedScalingChanged  (  boolean   arg0  )  { 
} 

public   void   urlChanged  (  String   arg0  )  { 
ver  (  "urlChanged(String arg0): El player intenta conectar con "  +  arg0  )  ; 
} 

public   void   usingMetrics  (  boolean   arg0  )  { 
} 

public   void   volumeChanged  (  double   arg0  )  { 
} 
} 

