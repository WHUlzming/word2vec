package   parser  ; 

import   java  .  io  .  BufferedReader  ; 
import   java  .  io  .  BufferedWriter  ; 
import   java  .  io  .  File  ; 
import   java  .  io  .  FileReader  ; 
import   java  .  io  .  FileWriter  ; 
import   java  .  io  .  IOException  ; 
import   java  .  io  .  InputStream  ; 
import   java  .  io  .  InputStreamReader  ; 
import   java  .  net  .  HttpURLConnection  ; 
import   java  .  net  .  MalformedURLException  ; 
import   java  .  net  .  URL  ; 
import   java  .  net  .  URLDecoder  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  HashSet  ; 
import   java  .  util  .  Set  ; 
import   java  .  util  .  StringTokenizer  ; 
import   org  .  hibernate  .  mapping  .  Array  ; 
import   util  .  StringUtils  ; 





public   class   HTMLPageExtractor  { 

public   static   void   main  (  String  [  ]  args  )  { 
String   root  =  "http://www.medworm.com/index.php?rid=2609173&cid=t__0_46_m&fid=38787&url=http%3A%2F%2Fmsf.ca%2Fblogs%2Fphotos%2F2009%2F07%2F14%2Fpakistan-4%2F"  ; 
String   dd  =  root  .  substring  (  root  .  indexOf  (  "http%"  )  ,  root  .  length  (  )  )  ; 
URLDecoder   urlDecoder  =  new   URLDecoder  (  )  ; 
System  .  out  .  println  (  urlDecoder  .  decode  (  dd  )  )  ; 
System  .  out  .  println  (  urlDecoder  .  decode  (  "http%3A%2F%2Fmsf.ca%2Fblogs%2Fphotos%2F2009%2F07%2F14%2Fpakistan-4%2F"  )  )  ; 
} 





public   static   String   decodeMecoURL  (  String   url  )  { 
String   decoded  =  url  .  substring  (  url  .  indexOf  (  "http%"  )  ,  url  .  length  (  )  )  ; 
URLDecoder   urlDecoder  =  new   URLDecoder  (  )  ; 
return   urlDecoder  .  decode  (  decoded  )  ; 
} 





public   static   void   extractContentAndSave  (  String   link  )  { 
try  { 
File   file  =  new   File  (  "C:\\fred\\test"  +  File  .  separator  +  HTMLPageExtractor  .  getURLName  (  link  )  +  ".txt"  )  ; 
if  (  !  file  .  exists  (  )  )  { 
BufferedWriter   out  =  new   BufferedWriter  (  new   FileWriter  (  file  )  )  ; 
out  .  write  (  HTMLPageExtractor  .  getTextFromHTMLPage  (  link  )  )  ; 
out  .  close  (  )  ; 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 

public   static   void   extractContentAndSave  (  String   link  ,  String   fileName  )  { 
try  { 
File   file  =  new   File  (  fileName  )  ; 
BufferedWriter   out  =  null  ; 
if  (  !  file  .  exists  (  )  )  { 
StringBuffer   stringBuffer  =  new   StringBuffer  (  )  ; 
stringBuffer  .  append  (  HTMLPageExtractor  .  getTextFromHTMLPage  (  link  )  )  ; 
if  (  stringBuffer  .  toString  (  )  .  length  (  )  >  1  &&  !  stringBuffer  .  toString  (  )  .  trim  (  )  .  equals  (  ""  )  )  { 
out  =  new   BufferedWriter  (  new   FileWriter  (  file  )  )  ; 
out  .  write  (  stringBuffer  .  toString  (  )  )  ; 
out  .  close  (  )  ; 
} 
} 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 





public   static   String   getURLName  (  String   url  )  { 
try  { 
if  (  !  url  .  contains  (  "http://"  )  )  { 
url  =  "http://"  +  url  ; 
} 
url  =  new   URL  (  url  )  .  getHost  (  )  ; 
}  catch  (  MalformedURLException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
int   index  =  0  ; 
if  (  url  .  contains  (  "www"  )  )  { 
index  =  1  ; 
} 
StringTokenizer   tempStringTokenizer  =  new   StringTokenizer  (  url  ,  "."  )  ; 
int   i  =  0  ; 
String   term  =  null  ; 
while  (  tempStringTokenizer  .  hasMoreTokens  (  )  )  { 
term  =  tempStringTokenizer  .  nextElement  (  )  .  toString  (  )  ; 
if  (  i  ==  index  )  { 
break  ; 
} 
i  ++  ; 
} 
return   term  ; 
} 









public   static   String   getText  (  File   document  )  throws   IOException  { 
String   content  =  ""  ; 
String   newLine  =  System  .  getProperty  (  "line.separator"  )  ; 
BufferedReader   reader  =  null  ; 
try  { 
reader  =  new   BufferedReader  (  new   FileReader  (  document  )  )  ; 
while  (  reader  .  ready  (  )  )  { 
content  +=  reader  .  readLine  (  )  +  newLine  ; 
} 
content  =  clearTags  (  content  )  ; 
}  finally  { 
try  { 
reader  .  close  (  )  ; 
}  catch  (  Exception   e  )  { 
} 
} 
return   content  ; 
} 

public   static   boolean   exists  (  String   URLName  )  { 
try  { 
HttpURLConnection  .  setFollowRedirects  (  false  )  ; 
HttpURLConnection   con  =  (  HttpURLConnection  )  new   URL  (  URLName  )  .  openConnection  (  )  ; 
con  .  setRequestMethod  (  "HEAD"  )  ; 
boolean   exist  =  (  !  (  ""  +  con  .  getResponseCode  (  )  )  .  trim  (  )  .  startsWith  (  "5"  )  ||  !  (  ""  +  con  .  getResponseCode  (  )  )  .  trim  (  )  .  startsWith  (  "4"  )  )  ; 
return   exist  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
return   false  ; 
} 
} 







public   static   String   getTextFromHTMLPage  (  String   url  )  { 
String   content  =  ""  ; 
URL   pageUrl  =  null  ; 
String   newLine  =  System  .  getProperty  (  "line.separator"  )  ; 
BufferedReader   reader  =  null  ; 
InputStream   in  =  null  ; 
try  { 
if  (  !  url  .  contains  (  "http://"  )  )  { 
url  =  "http://"  +  url  ; 
} 
pageUrl  =  new   URL  (  url  )  ; 
in  =  pageUrl  .  openStream  (  )  ; 
reader  =  new   BufferedReader  (  new   InputStreamReader  (  in  )  )  ; 
while  (  reader  .  ready  (  )  )  { 
content  +=  reader  .  readLine  (  )  +  newLine  ; 
} 
if  (  content  .  length  (  )  >  0  )  { 
content  =  clearTags  (  content  )  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
content  =  ""  ; 
}  finally  { 
try  { 
if  (  reader  !=  null  )  { 
reader  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
try  { 
if  (  in  !=  null  )  { 
in  .  close  (  )  ; 
} 
}  catch  (  IOException   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
} 
return   content  ; 
} 







public   static   String   clearTags  (  String   content  )  { 
int   scriptIndex  =  content  .  indexOf  (  "<script>"  )  ; 
int   endScriptIndex  =  content  .  indexOf  (  "</script>"  )  +  9  ; 
while  (  scriptIndex  !=  -  1  &&  endScriptIndex  !=  -  1  )  { 
if  (  scriptIndex  <  endScriptIndex  )  { 
String   subContent  =  content  .  substring  (  scriptIndex  ,  endScriptIndex  )  ; 
content  =  StringUtils  .  replace  (  content  ,  subContent  ,  ""  )  ; 
} 
scriptIndex  =  content  .  indexOf  (  "<script"  )  ; 
endScriptIndex  =  content  .  indexOf  (  "</script>"  ,  scriptIndex  )  +  9  ; 
} 
int   styleIndex  =  content  .  indexOf  (  "<style"  )  ; 
int   endStyleIndex  =  content  .  indexOf  (  "</style>"  )  +  9  ; 
while  (  styleIndex  !=  -  1  &&  endStyleIndex  !=  -  1  )  { 
if  (  styleIndex  <  endStyleIndex  )  { 
String   gambi  =  content  .  substring  (  styleIndex  ,  endStyleIndex  )  ; 
content  =  StringUtils  .  replace  (  content  ,  gambi  ,  ""  )  ; 
} 
styleIndex  =  content  .  indexOf  (  "<style"  )  ; 
endStyleIndex  =  content  .  indexOf  (  "</style>"  ,  styleIndex  )  +  9  ; 
} 
String   regExp  =  "<[^>]*>"  ; 
content  =  content  .  replaceAll  (  regExp  ,  ""  )  ; 
content  =  content  .  trim  (  )  ; 
return   content  ; 
} 







public   static   String   getTextFromHTMLPageUsingRegEx  (  String   url  )  { 
String   content  =  ""  ; 
if  (  !  url  .  contains  (  "http://"  )  )  { 
url  =  "http://"  +  url  ; 
} 
try  { 
content  =  getTextContentUsingRegEx  (  url  )  ; 
}  catch  (  Exception   e  )  { 
e  .  printStackTrace  (  )  ; 
} 
return   content  ; 
} 






public   static   String   getTextContentUsingRegEx  (  String   url  )  throws   Exception  { 
StringBuilder   sb  =  new   StringBuilder  (  )  ; 
BufferedReader   br  =  new   BufferedReader  (  new   FileReader  (  url  )  )  ; 
String   line  ; 
while  (  (  line  =  br  .  readLine  (  )  )  !=  null  )  { 
sb  .  append  (  line  )  ; 
} 
String   nohtml  =  sb  .  toString  (  )  .  replaceAll  (  "\\<.*?>"  ,  ""  )  ; 
return   nohtml  ; 
} 




public   static   Set  <  String  >  loadLinks  (  )  { 
Set  <  String  >  newlinks  =  new   HashSet  <  String  >  (  )  ; 
newlinks  .  add  (  "http://www.spikesource.com/suitetwo/"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/experiencewii"  )  ; 
newlinks  .  add  (  "http://freemind.sourceforge.net/wiki/index.php/Embedded_Mind_Maps#How_to_download_the_Freemind_Extension_for_MediaWiki"  )  ; 
newlinks  .  add  (  "http://ablvienna.wordpress.com/"  )  ; 
newlinks  .  add  (  "http://www.soa.com/index.php/section/company_press_detail/soa_software_acquires_logiclibrary"  )  ; 
newlinks  .  add  (  "http://www.snooth.com/"  )  ; 
newlinks  .  add  (  "http://www.spfc.com.br/"  )  ; 
newlinks  .  add  (  "https://jedi.dev.java.net/"  )  ; 
newlinks  .  add  (  "http://www.semanticfocus.com/blog/entry/title/some-truth-about-the-semantic-web/"  )  ; 
newlinks  .  add  (  "http://www.cs.ubc.ca/spider/poole/"  )  ; 
newlinks  .  add  (  "http://www.opusalegria.com.br/"  )  ; 
newlinks  .  add  (  "http://www.livemocha.com/"  )  ; 
newlinks  .  add  (  "http://famoso.disi.unitn.it/"  )  ; 
newlinks  .  add  (  "http://www.winsite.com/bin/Info?19000000037359"  )  ; 
newlinks  .  add  (  "http://2pt3.com/news/twotone-icons-for-free/"  )  ; 
newlinks  .  add  (  "http://2pt3.com/news/twotone-icons-for-free/"  )  ; 
newlinks  .  add  (  "http://www.feedjournal.com/"  )  ; 
newlinks  .  add  (  "http://www.sciencedirect.com/science?_ob=ArticleURL&_udi=B6VDC-4HNSB2G-1&_user=6239427&_rdoc=1&_fmt=&_orig=search&_sort=d&view=c&_version=1&_urlVersion=0&_userid=6239427&md5=4a66f30b3cd3566dcfa0af9553e022b6"  )  ; 
newlinks  .  add  (  "http://www.audioanarchy.org/"  )  ; 
newlinks  .  add  (  "http://xkcd.com/"  )  ; 
newlinks  .  add  (  "http://ontolog.cim3.net/cgi-bin/wiki.pl?ConferenceCall_2008_09_18"  )  ; 
newlinks  .  add  (  "http://sioc-project.org/firefox"  )  ; 
newlinks  .  add  (  "http://www.islifecorp.com.br/"  )  ; 
newlinks  .  add  (  "http://www.third-bit.com/"  )  ; 
newlinks  .  add  (  "http://www.elearnspace.org/Articles/connectivism.htm"  )  ; 
newlinks  .  add  (  "http://www.linuxdevcenter.com/pub/a/linux/2005/05/05/libcurl.html"  )  ; 
newlinks  .  add  (  "http://www.useit.com/alertbox/20010218.html"  )  ; 
newlinks  .  add  (  "http://www.talis.com/"  )  ; 
newlinks  .  add  (  "http://psy.ex.ac.uk/~tpostmes/refsbytype.php"  )  ; 
newlinks  .  add  (  "http://www.jeffheaton.com/ai"  )  ; 
newlinks  .  add  (  "http://goodoldai.org.yu/semantic_web_and_education/links.htm"  )  ; 
newlinks  .  add  (  "http://maemo.org/"  )  ; 
newlinks  .  add  (  "http://youxcity.punkt.at/?id=2"  )  ; 
newlinks  .  add  (  "http://forums.anarchy-online.com/showthread.php?t=206101&page=1&pp=20"  )  ; 
newlinks  .  add  (  "http://www.inf.ufrgs.br/~mirella/Rumo.html"  )  ; 
newlinks  .  add  (  "http://www.tietoenator.dk/default.asp?path=487,610"  )  ; 
newlinks  .  add  (  "http://www.infoq.com/articles/8-reasons-why-MDE-fails"  )  ; 
newlinks  .  add  (  "http://www.galaxyzoo.org/"  )  ; 
newlinks  .  add  (  "http://www.eskildsen.dk/"  )  ; 
newlinks  .  add  (  "http://www2.umassd.edu/SECenter/SAResources.html"  )  ; 
newlinks  .  add  (  "http://dionysos.mpch-mainz.mpg.de/~joeckel/pdflatex/"  )  ; 
newlinks  .  add  (  "http://www.openadvantage.org/articles/oadocument.2005-04-19.0329097790"  )  ; 
newlinks  .  add  (  "http://www.folhaonline.com/"  )  ; 
newlinks  .  add  (  "http://ejohn.org/apps/learn"  )  ; 
newlinks  .  add  (  "http://www.e-series.org/"  )  ; 
newlinks  .  add  (  "http://logic.stanford.edu/"  )  ; 
newlinks  .  add  (  "http://www.ariadne-eu.org/3_MD/ariadne_metadata_v31.htm"  )  ; 
newlinks  .  add  (  "http://www.ariadne-eu.org/3_MD/ariadne_metadata_v31.htm"  )  ; 
newlinks  .  add  (  "http://www.id-book.com/catherb/index.htm"  )  ; 
newlinks  .  add  (  "http://publib.boulder.ibm.com/wasce/V1.0.1/pt_BR/Tasks/Migrating/FromTomcat.html"  )  ; 
newlinks  .  add  (  "http://www.livejournal.com/"  )  ; 
newlinks  .  add  (  "http://www.hcirn.com/index.php"  )  ; 
newlinks  .  add  (  "http://bhandler.spaces.live.com/blog/cns!70F64BC910C9F7F3!1231.entry"  )  ; 
newlinks  .  add  (  "http://www.spikesource.com/suitetwo/"  )  ; 
newlinks  .  add  (  "http://www.zimbra.com/"  )  ; 
newlinks  .  add  (  "http://www.psp-hacks.com/"  )  ; 
newlinks  .  add  (  "http://www.cc.gatech.edu/~asb/papers/hci-for-kids.pdf"  )  ; 
newlinks  .  add  (  "http://www.hcii2009.org/"  )  ; 
newlinks  .  add  (  "http://www.opencalais.com/"  )  ; 
newlinks  .  add  (  "http://java.sun.com/j2se/javadoc/writingdoccomments/#exampleresult"  )  ; 
newlinks  .  add  (  "http://www.guardian.co.uk/weekend/story/0,,2129855,00.html"  )  ; 
newlinks  .  add  (  "http://blizzard.com/"  )  ; 
newlinks  .  add  (  "http://www.blockstatus.com/msn/stchecker"  )  ; 
newlinks  .  add  (  "http://www.greycobra.com/tutorial/Mac_Style_Buttons/"  )  ; 
newlinks  .  add  (  "http://www.stigmergicsystems.com/"  )  ; 
newlinks  .  add  (  "http://digg.com/"  )  ; 
newlinks  .  add  (  "http://code.google.com/p/ocropus/"  )  ; 
newlinks  .  add  (  "http://w5.cs.uni-sb.de/~butz/teaching/ie-ss03/papers/HCIinSF/"  )  ; 
newlinks  .  add  (  "http://www.joelamantia.com/blog/archives/ideas/tag_clouds_evolve_understanding_tag_clouds_1.html"  )  ; 
newlinks  .  add  (  "http://revistaepoca.globo.com/Revista/Epoca/0,,EDG79037-5855,00-ARQUIVO+MAX+GEHRINGER.html"  )  ; 
newlinks  .  add  (  "http://www.stoweboyd.com/message/2006/10/are_you_ready_f.html"  )  ; 
newlinks  .  add  (  "http://bpt.hpi.uni-potsdam.de/twiki/bin/view/Public/MathiasWeske"  )  ; 
newlinks  .  add  (  "http://casa.abril.uol.com.br/arquitetura/simulador/lb_coz/"  )  ; 
newlinks  .  add  (  "http://201.20.19.254/download/manuais/senado08_informatica_manual.pdf"  )  ; 
newlinks  .  add  (  "http://insitu.lri.fr/ecscw/workshop2.html"  )  ; 
newlinks  .  add  (  "http://cmap.ihmc.us/"  )  ; 
newlinks  .  add  (  "http://www.zdnet.com/swlib/prespick/prescrnt.html"  )  ; 
newlinks  .  add  (  "http://www.wvquine.org/"  )  ; 
newlinks  .  add  (  "http://www.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.vocabulix.com/online/Learn/Verbs"  )  ; 
newlinks  .  add  (  "http://acdc.linguateca.pt/aval_conjunta/aval_RI.html"  )  ; 
newlinks  .  add  (  "http://www.hotmail.com/"  )  ; 
newlinks  .  add  (  "http://buscatextual.cnpq.br/buscatextual/index.jsp"  )  ; 
newlinks  .  add  (  "http://dbh.nsd.uib.no/kanaler/"  )  ; 
newlinks  .  add  (  "http://www.gmail.com/"  )  ; 
newlinks  .  add  (  "http://ieeexplore.ieee.org/xpl/freeabs_all.jsp?arnumber=917969"  )  ; 
newlinks  .  add  (  "http://www.planeteclipse.org/planet/"  )  ; 
newlinks  .  add  (  "http://www.geocities.com/jmanderson_2000/trainers/trainers.htm"  )  ; 
newlinks  .  add  (  "http://www.cs.phs.uoa.gr/en/staff/vosniadou.html"  )  ; 
newlinks  .  add  (  "http://www.iath.virginia.edu/utc/uncletom/key/kyhp.html"  )  ; 
newlinks  .  add  (  "http://piwik.org/"  )  ; 
newlinks  .  add  (  "http://script.aculo.us/"  )  ; 
newlinks  .  add  (  "http://kom.aau.dk/~csp/PDP08/Sites/PDP5/index.php"  )  ; 
newlinks  .  add  (  "http://www.nachdenkseiten.de/"  )  ; 
newlinks  .  add  (  "http://equipe.nce.ufrj.br/adriano/c/apostila/algoritmos.htm"  )  ; 
newlinks  .  add  (  "http://meryl.net/2008/01/22/175-data-and-information-visualization-examples-and-resources/"  )  ; 
newlinks  .  add  (  "http://www.goats.com/"  )  ; 
newlinks  .  add  (  "http://www.nl.bol.com/is-bin/INTERSHOP.enfinity/eCS/Store/nl/-/EUR/BOL_DisplayProductInformation-Start?BOL_OWNER_ID=1001004000628049&Section=BOOK_EN"  )  ; 
newlinks  .  add  (  "http://plone.org/"  )  ; 
newlinks  .  add  (  "http://www.hpl.hp.com/research/idl/papers/ranking/ranking.html"  )  ; 
newlinks  .  add  (  "http://www.sciencedirect.com/science?_ob=ArticleURL&_udi=B6V17-4D1R5DP-1&_user=10&_rdoc=1&_fmt=&_orig=search&_sort=d&view=c&_version=1&_urlVersion=0&_userid=10&md5=1ef064a52adae25b15c4b7ad4635e9a6"  )  ; 
newlinks  .  add  (  "http://www.willarson.com/blog/?p=36"  )  ; 
newlinks  .  add  (  "http://www.ibm.com/developerworks/opensource/library/os-php-future/?ca=dgr-lnxw01PHP-Future"  )  ; 
newlinks  .  add  (  "http://www.celticfc.net/"  )  ; 
newlinks  .  add  (  "http://tnb.aau.dk/fg/it/index_p1.html"  )  ; 
newlinks  .  add  (  "http://www.programmableweb.com/reference"  )  ; 
newlinks  .  add  (  "http://www.stumbleupon.com/"  )  ; 
newlinks  .  add  (  "http://de.youtube.com/watch?v=Hjp0I_okX0w"  )  ; 
newlinks  .  add  (  "http://www.watch-movies.net/"  )  ; 
newlinks  .  add  (  "http://www.javablackbelt.com/"  )  ; 
newlinks  .  add  (  "http://is.tm.tue.nl/staff/wvdaalst/workflowcourse/"  )  ; 
newlinks  .  add  (  "http://www.cs.cmu.edu/afs/cs.cmu.edu/project/ai-repository/ai/lang/prolog/0.html"  )  ; 
newlinks  .  add  (  "http://www.nl.bol.com/is-bin/INTERSHOP.enfinity/eCS/Store/nl/-/EUR/BOL_DisplayProductInformation-Start?BOL_OWNER_ID=1001004001574158&Section=BOOK_EN"  )  ; 
newlinks  .  add  (  "http://www.sbu.ac.uk/~csse/publications/OOMetrics.html"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Transport_tycoon"  )  ; 
newlinks  .  add  (  "http://ubuntuusers.de/"  )  ; 
newlinks  .  add  (  "http://www.dr.dk/"  )  ; 
newlinks  .  add  (  "http://www.flickr.com/services/api/keys/"  )  ; 
newlinks  .  add  (  "http://simplejson.googlecode.com/svn/tags/simplejson-1.9.1/docs/index.html"  )  ; 
newlinks  .  add  (  "http://gentoo-wiki.com/Main_Page"  )  ; 
newlinks  .  add  (  "http://smiley963.tripod.com/humorous.html"  )  ; 
newlinks  .  add  (  "http://nelh.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://www.colocolo.cl/"  )  ; 
newlinks  .  add  (  "http://www.fundacionctic.org/web/contenidos/en"  )  ; 
newlinks  .  add  (  "http://jnewland.com/articles/2006/02/22/how-to-subscribe-to-tv-shows-using-the-democracy-player-bittorrent-rss"  )  ; 
newlinks  .  add  (  "http://www.g-masters.com/index.php?p=filelist;set=nds"  )  ; 
newlinks  .  add  (  "http://newmine.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://www.barsuk.com/shop/dcfcpst01"  )  ; 
newlinks  .  add  (  "http://www.teqlo.com/"  )  ; 
newlinks  .  add  (  "http://www.danielabarbosa.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://www.espirito.com.br/portal/artigos/eneas-canhadas/diferencas-entre-ser.html"  )  ; 
newlinks  .  add  (  "http://www.talkyoo.net/"  )  ; 
newlinks  .  add  (  "http://www.maltron.com/maltron-key-layout-custom.html"  )  ; 
newlinks  .  add  (  "http://www.faqs.org/rfcs/"  )  ; 
newlinks  .  add  (  "http://mikeg.typepad.com/perceptions/social_software/index.html"  )  ; 
newlinks  .  add  (  "http://www-128.ibm.com/developerworks/power/cell/"  )  ; 
newlinks  .  add  (  "http://site.magix.net/ap/registration_center/index.php3?lang=D&regType=home&targetUserLevel=3&targetRegStep=2"  )  ; 
newlinks  .  add  (  "http://blog.marcomendes.com/category/gestao-de-pessoas/"  )  ; 
newlinks  .  add  (  "http://www.cs.auc.dk/library/cgi-bin/search.cgi"  )  ; 
newlinks  .  add  (  "http://www.wikier.org/"  )  ; 
newlinks  .  add  (  "http://www.utne.com/2004-07-01/a-new-way-of-walking.aspx"  )  ; 
newlinks  .  add  (  "http://www.zengestrom.com/blog/2005/04/why_some_social.html"  )  ; 
newlinks  .  add  (  "http://www.improveit.com.br/xp"  )  ; 
newlinks  .  add  (  "http://www.sics.se/interaction/projects/ad/"  )  ; 
newlinks  .  add  (  "http://www.librarything.com/thingology/2007/02/when-tags-works-and-when-they-dont.php"  )  ; 
newlinks  .  add  (  "http://www.fingertime.com/harpoonlagoon.php"  )  ; 
newlinks  .  add  (  "http://inkido.indiana.edu/barab/index.html"  )  ; 
newlinks  .  add  (  "http://de.selfhtml.org/"  )  ; 
newlinks  .  add  (  "http://www.shadows.com/"  )  ; 
newlinks  .  add  (  "http://www.sandpile.org/"  )  ; 
newlinks  .  add  (  "http://www.notapositiva.com/trab_estudantes/trab_estudantes/filosofia/filosofia_trabalhos/perspfalsificacionpopper.htm"  )  ; 
newlinks  .  add  (  "http://www.triple-i.info/"  )  ; 
newlinks  .  add  (  "http://nform.ca/publications/social-software-building-block"  )  ; 
newlinks  .  add  (  "http://www.podcastfrancaisfacile.com/"  )  ; 
newlinks  .  add  (  "http://br.geocities.com/fabiano_lh/objectpascal.htm"  )  ; 
newlinks  .  add  (  "http://elan.org/career/usability/jobbanks.cfm"  )  ; 
newlinks  .  add  (  "http://www.cluetrain.com/"  )  ; 
newlinks  .  add  (  "http://www.net-it.com/"  )  ; 
newlinks  .  add  (  "http://www.cscw2008.org/papers.html"  )  ; 
newlinks  .  add  (  "http://www.miniclip.com/games/raft-wars/en/"  )  ; 
newlinks  .  add  (  "http://www.ifi.uzh.ch/ddis/msr/"  )  ; 
newlinks  .  add  (  "http://www.regular-expressions.info/index.html"  )  ; 
newlinks  .  add  (  "http://pt.wikipedia.org/wiki/Pierre_L%C3%A9vy"  )  ; 
newlinks  .  add  (  "http://www.cesar.edu.br/"  )  ; 
newlinks  .  add  (  "http://triplify.org/"  )  ; 
newlinks  .  add  (  "http://www.google.co.uk/search?q=usability+job&ie=UTF-8&hl=en&meta=cr%3DcountryUK%7CcountryGB"  )  ; 
newlinks  .  add  (  "http://www.kibeloco.com.br/"  )  ; 
newlinks  .  add  (  "http://www.taskcoach.org/"  )  ; 
newlinks  .  add  (  "http://protege.stanford.edu/download/download.html"  )  ; 
newlinks  .  add  (  "http://www.google.com/"  )  ; 
newlinks  .  add  (  "http://scholar.google.com/"  )  ; 
newlinks  .  add  (  "http://smeira.blog.terra.com.br/"  )  ; 
newlinks  .  add  (  "http://www.consumating.com/"  )  ; 
newlinks  .  add  (  "http://www.waves4you.com/"  )  ; 
newlinks  .  add  (  "http://www.korfiatis.info/"  )  ; 
newlinks  .  add  (  "http://css.maxdesign.com.au/floatutorial/index.htm"  )  ; 
newlinks  .  add  (  "http://www.thefreedictionary.com/"  )  ; 
newlinks  .  add  (  "http://www.blainekendall.com/deliciousmind/"  )  ; 
newlinks  .  add  (  "https://www.improve-innovation.eu/opencms/opencms/en/01_IMP/"  )  ; 
newlinks  .  add  (  "http://video.google.com/videoplay?docid=2211331871717618072&ei=CpTXSID9E6Gg2ALR0MXMBw&q=mccain+released&vt=lf"  )  ; 
newlinks  .  add  (  "http://www.pfungen.ch/de/"  )  ; 
newlinks  .  add  (  "http://greengeckodesign.com/projects/menumatic.aspx#examples"  )  ; 
newlinks  .  add  (  "http://www.conferencealerts.com/italy.htm"  )  ; 
newlinks  .  add  (  "http://www.ecs.soton.ac.uk/~nrj/pubs.html"  )  ; 
newlinks  .  add  (  "http://flickr.com/"  )  ; 
newlinks  .  add  (  "http://www.trillian.im/"  )  ; 
newlinks  .  add  (  "http://tranquility.auno.org/pictures/?id=12"  )  ; 
newlinks  .  add  (  "http://www.howstuffworks.com/"  )  ; 
newlinks  .  add  (  "http://jimjansen.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://i.adultswim.com/adultswim/games/surgeon/game.swf"  )  ; 
newlinks  .  add  (  "http://www.eyezmaze.com/eyezblog_en/blog/2005/09/grow_cube.html#monster"  )  ; 
newlinks  .  add  (  "http://www.vanderwal.net/random/entrysel.php?blog=1635"  )  ; 
newlinks  .  add  (  "http://blog.hbs.edu/faculty/amcafee/index.php/faculty_amcafee_v3/harbors_in_the_ocean_of_e_mail/"  )  ; 
newlinks  .  add  (  "http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/index.html"  )  ; 
newlinks  .  add  (  "http://sunsite.unc.edu/ibic/IBIC-homepage.html"  )  ; 
newlinks  .  add  (  "http://www.houserdesign.com/flickr/"  )  ; 
newlinks  .  add  (  "http://blogs.sun.com/peterreiser/feed/entries/atom?cat=%2FCommunity+Equity"  )  ; 
newlinks  .  add  (  "http://interfacelift.com/"  )  ; 
newlinks  .  add  (  "http://research.ihost.com/hopl/HOPL.html"  )  ; 
newlinks  .  add  (  "http://www.juliobattisti.com.br/tutoriais/acaciocosta/logicaalgoritmos001.asp"  )  ; 
newlinks  .  add  (  "http://www.onlinetvrecorder.com/"  )  ; 
newlinks  .  add  (  "http://smartsam.de/product.php?prod_id=21263"  )  ; 
newlinks  .  add  (  "http://www.katolsk-nord.dk/Engelsk/hoere_engelsk.htm"  )  ; 
newlinks  .  add  (  "http://www.skyscanner.net/"  )  ; 
newlinks  .  add  (  "http://moodle.org/"  )  ; 
newlinks  .  add  (  "http://blog.pietrosperoni.it/2005/05/25/tag-clouds-metric/"  )  ; 
newlinks  .  add  (  "http://www.ngcsu.edu/Academic/Arts_Let/Math_CS/GHAFARIA/csci1302/csci1302.htm"  )  ; 
newlinks  .  add  (  "http://tagcloud.oclc.org/tagcloud/TagCloudDemo?clear=true&usecase=on&stem=on&block=on&style=2&limit=100"  )  ; 
newlinks  .  add  (  "http://oskari.saarenmaa.fi/montblanc2007/"  )  ; 
newlinks  .  add  (  "http://pande.statics.org/"  )  ; 
newlinks  .  add  (  "http://infomesh.net/2001/swintro/"  )  ; 
newlinks  .  add  (  "http://www.diku.dk/undervisning/2003f/datV-system/papers/CHI2000frokjar.pdf"  )  ; 
newlinks  .  add  (  "http://mashable.com/2007/07/13/tagging-tools/"  )  ; 
newlinks  .  add  (  "http://www.longtail.com/the_long_tail/2008/09/revised-the-fou.html"  )  ; 
newlinks  .  add  (  "http://www.personal.psu.edu/iua1/ajax-django-sandbox.htm"  )  ; 
newlinks  .  add  (  "http://www.loa-cnr.it/"  )  ; 
newlinks  .  add  (  "http://www.zope.org/"  )  ; 
newlinks  .  add  (  "http://www.mozilla.org/status/"  )  ; 
newlinks  .  add  (  "http://wiki.osx86project.org/wiki/index.php/Hardware_.kext_Patching_List"  )  ; 
newlinks  .  add  (  "http://www.degulesider.dk/vbw/super/index.jsp"  )  ; 
newlinks  .  add  (  "http://www.thegladiators.org/"  )  ; 
newlinks  .  add  (  "http://www.w2.nl/"  )  ; 
newlinks  .  add  (  "http://www.les.inf.puc-rio.br/"  )  ; 
newlinks  .  add  (  "http://www-personal.umich.edu/~ladamic/"  )  ; 
newlinks  .  add  (  "http://bnj.sourceforge.net/"  )  ; 
newlinks  .  add  (  "http://www.planetsuse.org/"  )  ; 
newlinks  .  add  (  "http://www.aub.aau.dk/"  )  ; 
newlinks  .  add  (  "http://www.kongregate.com/games/ahnt/loops-of-zen?referrer=upupup&ref=nf"  )  ; 
newlinks  .  add  (  "http://catb.org/~esr/writings/cathedral-bazaar/"  )  ; 
newlinks  .  add  (  "http://www.infoq.com/articles/scalability-worst-practices;jsessionid=484F17508006679A2585664B7288DB82"  )  ; 
newlinks  .  add  (  "http://news.com.com/1606-2_3-6080229.html"  )  ; 
newlinks  .  add  (  "http://www.danskebank.dk/foruds"  )  ; 
newlinks  .  add  (  "http://www.joelonsoftware.com/articles/fog0000000069.html"  )  ; 
newlinks  .  add  (  "http://wallet.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.contentmanager.de/"  )  ; 
newlinks  .  add  (  "http://www.nlm.nih.gov/"  )  ; 
newlinks  .  add  (  "http://futurezone.orf.at/"  )  ; 
newlinks  .  add  (  "http://www.lyrics.com/"  )  ; 
newlinks  .  add  (  "http://www.summa.psychologie.uni-wuerzburg.de/summa/index.html"  )  ; 
newlinks  .  add  (  "http://www.boston.com/business/personaltech/articles/2007/06/17/game_designers_test_the_limits_of_artificial_intelligence/?page=1"  )  ; 
newlinks  .  add  (  "http://jena.sourceforge.net/"  )  ; 
newlinks  .  add  (  "http://www.avatarsreloaded.com/e107_plugins/forum/forum_viewtopic.php?363"  )  ; 
newlinks  .  add  (  "http://alltop.com/"  )  ; 
newlinks  .  add  (  "http://www.gamedesign.jp/index_en.html"  )  ; 
newlinks  .  add  (  "http://www.amazon.com/gp/product/B000098ZSO/104-2870557-4063119?v=glance&n=5174"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*newlinks.add"  )  ; 
newlinks  .  add  (  "http://www.io.com/~sj/PirateTalk.html"  )  ; 
newlinks  .  add  (  "http://www.googleidol.com/"  )  ; 
newlinks  .  add  (  "http://www.d.umn.edu/itss/support/Training/Online/webdesign/"  )  ; 
newlinks  .  add  (  "http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=21208"  )  ; 
newlinks  .  add  (  "http://www.ksl.stanford.edu/"  )  ; 
newlinks  .  add  (  "http://academhack.outsidethetext.com/home/2008/twitter-for-academia/"  )  ; 
newlinks  .  add  (  "http://www.studygs.net/portuges/"  )  ; 
newlinks  .  add  (  "http://www.roberthappe.net/"  )  ; 
newlinks  .  add  (  "http://obi.dnsalias.org/craig/"  )  ; 
newlinks  .  add  (  "http://www.forbrug.dk/test/testbasen/boern/barnevogne/"  )  ; 
newlinks  .  add  (  "http://osx86leo4all.wikidot.com/installation-support"  )  ; 
newlinks  .  add  (  "http://www.scrumalliance.org/"  )  ; 
newlinks  .  add  (  "http://www.transformingfreedom.com/de/hyperaudio/all"  )  ; 
newlinks  .  add  (  "http://www.allposters.com/"  )  ; 
newlinks  .  add  (  "http://www.tranquility.cx/forum/index.php?sid=0d2c4d5061b91bbbe0129d66155fdc17"  )  ; 
newlinks  .  add  (  "http://ai.eecs.umich.edu/cogarch0/max/index.html"  )  ; 
newlinks  .  add  (  "http://www.osnews.com/index.php"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Social_software"  )  ; 
newlinks  .  add  (  "http://www.dlib.org/dlib/january06/guy/01guy.html"  )  ; 
newlinks  .  add  (  "http://www2.dcc.ufmg.br/laboratorios/lsi/EAD/MapasConc/TutorialCMapTools.html"  )  ; 
newlinks  .  add  (  "http://www.013web.nl/"  )  ; 
newlinks  .  add  (  "http://www.sigs.com/objectcurrents/"  )  ; 
newlinks  .  add  (  "http://www.personaldna.com/report.php?k=cyoombvZuJIFZWc-GO-DDDCA-b23e&u=0e0ad6e2da34"  )  ; 
newlinks  .  add  (  "http://www.tuxdeluxe.org/node/267"  )  ; 
newlinks  .  add  (  "http://www.folha.com.br/"  )  ; 
newlinks  .  add  (  "http://struts.apache.org/2.x/"  )  ; 
newlinks  .  add  (  "http://www.momondo.com/"  )  ; 
newlinks  .  add  (  "http://www.sei.cmu.edu/splc2006/tech_program.html"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/"  )  ; 
newlinks  .  add  (  "http://blog.k-jahn.de/"  )  ; 
newlinks  .  add  (  "http://www.sei.cmu.edu/productlines/frame_report/frame_bib.htm"  )  ; 
newlinks  .  add  (  "http://diplo.uol.com.br/2007-10,a1976"  )  ; 
newlinks  .  add  (  "http://kicker.de/"  )  ; 
newlinks  .  add  (  "http://www.htdp.org/2003-09-26/Book/"  )  ; 
newlinks  .  add  (  "http://manugarg.googlepages.com/systemcallinlinux2_6.html"  )  ; 
newlinks  .  add  (  "https://www.ug.it.usyd.edu.au/~reflect/index.cgi?Assess"  )  ; 
newlinks  .  add  (  "http://ontolog.cim3.net/forum/ontolog-invitation/2008-09/msg00000.html"  )  ; 
newlinks  .  add  (  "http://www.periodicos.capes.gov.br/portugues/index.jsp"  )  ; 
newlinks  .  add  (  "http://www.cleverjoe.com/synths/trinity.html"  )  ; 
newlinks  .  add  (  "https://www.aub.aau.dk/formularer/bestil-peri.html"  )  ; 
newlinks  .  add  (  "http://www.welt-in-zahlen.de/laendervergleich.phtml"  )  ; 
newlinks  .  add  (  "http://www.heise.de/tp/default.html"  )  ; 
newlinks  .  add  (  "http://www.geekcomix.com/cgi-bin/classnotes/wiki.pl?UNIX01"  )  ; 
newlinks  .  add  (  "http://www.cse.yorku.ca/~jeff/notes/3101/slides.html"  )  ; 
newlinks  .  add  (  "http://www.cifras.com.br/"  )  ; 
newlinks  .  add  (  "http://www.acm.org/sigchi/chi96/proceedings/shortpap/Rodriguez/rn_txt.htm"  )  ; 
newlinks  .  add  (  "http://informatics.indiana.edu/subtletech/"  )  ; 
newlinks  .  add  (  "http://www.cbs.com/primetime/how_i_met_your_mother/"  )  ; 
newlinks  .  add  (  "http://www.netraker.com/nrinfo/research/FiveUsers.pdf"  )  ; 
newlinks  .  add  (  "http://www.cpsc.ucalgary.ca/grouplab/papers/1996/96-ShortPapers.CHI/8-TurboTurtle/turbo.html"  )  ; 
newlinks  .  add  (  "http://www.worth1000.com/tutorial.asp?sid=161043"  )  ; 
newlinks  .  add  (  "http://www.systematic.dk/uk"  )  ; 
newlinks  .  add  (  "http://usability.typepad.com/confusability/2005/04/usersaurus_vs_f.html"  )  ; 
newlinks  .  add  (  "http://www.confreaks.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/"  )  ; 
newlinks  .  add  (  "http://photos.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.sun.com/"  )  ; 
newlinks  .  add  (  "http://mainline.brynmawr.edu/EIAIR/CurriculumDescant/1.html"  )  ; 
newlinks  .  add  (  "http://www.tranquility.cx/misc/application.php"  )  ; 
newlinks  .  add  (  "http://epp.eurostat.ec.europa.eu/portal/page?_pageid=1996,45323734&_dad=portal&_schema=PORTAL&screen=welcomeref&open=/icts/isoc/isoc_ci/isoc_ci_cm&language=en&product=EU_MAIN_TREE&root=EU_MAIN_TREE&scrollto=129"  )  ; 
newlinks  .  add  (  "http://newmedia.purchase.edu/~Jeanine/jsexamples.html"  )  ; 
newlinks  .  add  (  "http://www.sei.cmu.edu/"  )  ; 
newlinks  .  add  (  "http://www.ninebells.dk/"  )  ; 
newlinks  .  add  (  "http://www.tencompetence.org/node/57"  )  ; 
newlinks  .  add  (  "http://aima.cs.berkeley.edu/ai.html"  )  ; 
newlinks  .  add  (  "http://socialsoftware.weblogsinc.com/"  )  ; 
newlinks  .  add  (  "http://sunlabs.sfbay.sun.com/start/IAS2007/cgi-bin/scmd.cgi?scmd=confirmSubmit&passcode=22X-I7H8I1H8E7"  )  ; 
newlinks  .  add  (  "http://www.twine.com/"  )  ; 
newlinks  .  add  (  "http://www.woundedmoon.org/win32_freeware.html"  )  ; 
newlinks  .  add  (  "http://www.w3c.org/"  )  ; 
newlinks  .  add  (  "http://www-sop.inria.fr/aid/broadway/index.html"  )  ; 
newlinks  .  add  (  "http://www.charges.com.br/"  )  ; 
newlinks  .  add  (  "http://www.wsu.edu/~brians/errors/errors.html"  )  ; 
newlinks  .  add  (  "http://labs.mozilla.com/2006/12/introducing-operator"  )  ; 
newlinks  .  add  (  "http://videotheek.surfnet.nl/play_proxy/mmc/24213/googleachterdeschermen.asf"  )  ; 
newlinks  .  add  (  "http://nettv.globo.com/NETServ/br/prog/canais.jsp?menu=6"  )  ; 
newlinks  .  add  (  "http://zenhabits.net/"  )  ; 
newlinks  .  add  (  "http://www.artima.com/weblogs/viewpost.jsp?thread=230001"  )  ; 
newlinks  .  add  (  "http://www.eclipse.org/articles/article.php?file=Article-MemoryView/index.html"  )  ; 
newlinks  .  add  (  "http://www.eee.bham.ac.uk/bull/lemore/"  )  ; 
newlinks  .  add  (  "http://readwriteweb.com/"  )  ; 
newlinks  .  add  (  "https://cepedia.sfbay/index.php?title=Web_n_plus_1"  )  ; 
newlinks  .  add  (  "http://www.cs.sfu.ca/~inkpen/Papers/Ed-Media_97/ed-media_97.html"  )  ; 
newlinks  .  add  (  "http://radar.oreilly.com/2008/07/an-esb-for-the-web.html"  )  ; 
newlinks  .  add  (  "http://www.itworld.com/offbeat/53396/economic-necessity-mother-software-reuse"  )  ; 
newlinks  .  add  (  "http://www.sk.com.br/sk-voca.html"  )  ; 
newlinks  .  add  (  "http://twostop.central.sun.com/portal/dt"  )  ; 
newlinks  .  add  (  "http://fsau.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://openchannelsoftware.org/projects/SAILE/"  )  ; 
newlinks  .  add  (  "http://www.cs.umd.edu/Library/TRs/CS-TR-4446/CS-TR-4446.pdf"  )  ; 
newlinks  .  add  (  "http://araucaria.computing.dundee.ac.uk/"  )  ; 
newlinks  .  add  (  "http://www.glyphweb.com/arda/"  )  ; 
newlinks  .  add  (  "http://newmedia.purchase.edu/~Jeanine/charts.html"  )  ; 
newlinks  .  add  (  "http://thefutureoflearning.wordpress.com/programm/"  )  ; 
newlinks  .  add  (  "http://pythonpaste.org/"  )  ; 
newlinks  .  add  (  "http://tw.rpi.edu/wiki/Main_Page"  )  ; 
newlinks  .  add  (  "http://allfutus.com/"  )  ; 
newlinks  .  add  (  "http://stuwww.uvt.nl/~s939238/"  )  ; 
newlinks  .  add  (  "http://www.amazon.de/ref=rd_www_amazon_at/?site-redirect=at"  )  ; 
newlinks  .  add  (  "http://www.osta.org/technology/cdqa4.htm"  )  ; 
newlinks  .  add  (  "http://maczealots.com/tutorials/wordpress/"  )  ; 
newlinks  .  add  (  "http://www.studivz.net/"  )  ; 
newlinks  .  add  (  "http://www.verb2verbe.com/"  )  ; 
newlinks  .  add  (  "http://www.slideshare.net/thecroaker/death-by-powerpoint"  )  ; 
newlinks  .  add  (  "http://uie.com/Articles/eight_is_not_enough.htm"  )  ; 
newlinks  .  add  (  "http://www.manager-magazin.de/it/cebit/0,2828,472166,00.html"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/"  )  ; 
newlinks  .  add  (  "http://code.google.com/p/gosu/"  )  ; 
newlinks  .  add  (  "http://plazes.com/plaze/44f806e67f3cb7c030536739fbb5f3c8/"  )  ; 
newlinks  .  add  (  "http://www.hamburg.de/"  )  ; 
newlinks  .  add  (  "http://www.lastfm.de/user/StexX"  )  ; 
newlinks  .  add  (  "http://www.hm.com/dk/start/start/index.jsp##"  )  ; 
newlinks  .  add  (  "http://www.legendasbrasil.com.br/html/"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Transmeta"  )  ; 
newlinks  .  add  (  "http://swaroopch.info/text/Byte_of_Python:Main_Page"  )  ; 
newlinks  .  add  (  "http://www.ulb.ac.be/psycho/psysoc/Papers/identityperformance.pdf"  )  ; 
newlinks  .  add  (  "http://www.hesteforum.dk/"  )  ; 
newlinks  .  add  (  "http://www.aintitcool.com/"  )  ; 
newlinks  .  add  (  "http://www.everythingismiscellaneous.com/"  )  ; 
newlinks  .  add  (  "http://www.cs.cmu.edu/afs/cs.cmu.edu/Web/People/bam/"  )  ; 
newlinks  .  add  (  "http://momondo.com/"  )  ; 
newlinks  .  add  (  "http://4suite.org/index.xhtml"  )  ; 
newlinks  .  add  (  "http://semanticsearch.org/"  )  ; 
newlinks  .  add  (  "http://www.springerlink.com/content/e5qxkkrvtbc054vg/"  )  ; 
newlinks  .  add  (  "http://blog.netvibes.com/"  )  ; 
newlinks  .  add  (  "http://www.cwi.nl/cwi/projects/abc.html"  )  ; 
newlinks  .  add  (  "http://www.cs.colorado.edu/conferences/idc2005/"  )  ; 
newlinks  .  add  (  "http://www21.cplan.com/pls/pg_sun/reg_sun_cec_post.contact_info"  )  ; 
newlinks  .  add  (  "http://www.csszengarden.com/"  )  ; 
newlinks  .  add  (  "http://www.mirabilis.com/index.html"  )  ; 
newlinks  .  add  (  "http://shinywhitebox.com/home/features/features.html"  )  ; 
newlinks  .  add  (  "http://www.munich.com/"  )  ; 
newlinks  .  add  (  "http://googlesystem.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://www.esl.eu/de/"  )  ; 
newlinks  .  add  (  "https://webnext.sfbay.sun.com/aliasgroup/syncManager.jsp"  )  ; 
newlinks  .  add  (  "http://ipwebdev.com/hermit/"  )  ; 
newlinks  .  add  (  "http://www.icmsc.sc.usp.br/eventos/sbes96/"  )  ; 
newlinks  .  add  (  "http://informationr.net/ir/8-1/paper144.html"  )  ; 
newlinks  .  add  (  "http://cruise.rise.com.br/"  )  ; 
newlinks  .  add  (  "http://www.econsultant.com/web2/"  )  ; 
newlinks  .  add  (  "http://www.download-de-videos.com/"  )  ; 
newlinks  .  add  (  "http://coe.sdsu.edu/eet/"  )  ; 
newlinks  .  add  (  "http://www.gatehouse.dk/default.php"  )  ; 
newlinks  .  add  (  "http://www.blogologue.com/blog_entry?id=1122979755X77"  )  ; 
newlinks  .  add  (  "http://www.doom9.org/index.html?/software2.htm"  )  ; 
newlinks  .  add  (  "http://www.marcosnahr.com.br/hierarquia-das-necessidades-no-design/"  )  ; 
newlinks  .  add  (  "http://ola-bini.blogspot.com/2007/12/code-size-and-dynamic-languages.html"  )  ; 
newlinks  .  add  (  "http://www.matrox.com/"  )  ; 
newlinks  .  add  (  "http://eric.ed.gov/ERICWebPortal/custom/portlets/recordDetails/detailmini.jsp?_nfpb=true&_&ERICExtSearch_SearchValue_0=EJ728897&ERICExtSearch_SearchType_0=no&accno=EJ728897"  )  ; 
newlinks  .  add  (  "https://www.benefitaccess.com/"  )  ; 
newlinks  .  add  (  "http://changethis.com/"  )  ; 
newlinks  .  add  (  "http://relationary.wordpress.com/2008/08/17/six-unities-analysis/"  )  ; 
newlinks  .  add  (  "http://sunwebcms.central.sun.com:8001/sunweb/cda/mainAssembly/0,2685,10220_43551_51054319,00.html#requestaccount"  )  ; 
newlinks  .  add  (  "http://fbim.fh-regensburg.de/~saj39122/jfroehl/diplom/e-index.html"  )  ; 
newlinks  .  add  (  "http://www.inf.ufrgs.br/~valdeni/webmedia2007/"  )  ; 
newlinks  .  add  (  "http://www.hcibib.org/"  )  ; 
newlinks  .  add  (  "http://www.lijit.com/signup/crawl"  )  ; 
newlinks  .  add  (  "http://labs.digg.com/"  )  ; 
newlinks  .  add  (  "http://www.smashingmagazine.com/2007/11/29/icons-for-your-desktop-and-icons-for-your-web-designs/"  )  ; 
newlinks  .  add  (  "http://the.taoofmac.com/"  )  ; 
newlinks  .  add  (  "http://www.wine.com/Default.asp?state=CA&next=%2FDefault.asp%3F"  )  ; 
newlinks  .  add  (  "http://www.aabsport.dk/"  )  ; 
newlinks  .  add  (  "http://webpages.charter.net/aofun/aorm.html"  )  ; 
newlinks  .  add  (  "http://www.shinystat.com/en/cat/Science-Technology/Computer-Science-0.html"  )  ; 
newlinks  .  add  (  "http://www.vendamais.com.br/Lideranca/?codFale=2"  )  ; 
newlinks  .  add  (  "http://www.orf.at/"  )  ; 
newlinks  .  add  (  "http://see.stanford.edu/"  )  ; 
newlinks  .  add  (  "http://shirky.com/writings/ontology_overrated.html"  )  ; 
newlinks  .  add  (  "http://www.semantic-web.at/"  )  ; 
newlinks  .  add  (  "http://www.babylon.com/definition/toll/Portuguese"  )  ; 
newlinks  .  add  (  "http://www.quakelive.com/"  )  ; 
newlinks  .  add  (  "http://www.mozilla.org/"  )  ; 
newlinks  .  add  (  "http://www.fundao.wiki.br/articles.asp?cod=45"  )  ; 
newlinks  .  add  (  "http://www.pucminas.br/conjuntura"  )  ; 
newlinks  .  add  (  "http://www.pyzine.com/Issue008/Section_Articles/article_Encodings.html"  )  ; 
newlinks  .  add  (  "http://www.google.com/search?sourceid=navclient&ie=UTF-8&rls=GGLJ,GGLJ:2006-35,GGLJ:en&q=Software+Engineering+Ontology+for+Project+Management"  )  ; 
newlinks  .  add  (  "http://www.ita.sel.sony.com/"  )  ; 
newlinks  .  add  (  "http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators"  )  ; 
newlinks  .  add  (  "http://www.opensourceforensics.org/"  )  ; 
newlinks  .  add  (  "http://revolucao.etc.br/archives/folksonomia-e-a-maneira-com-que-nos-colocamos-ordem-nas-coisas/"  )  ; 
newlinks  .  add  (  "http://www.mesem.ch/shopping_cart.php"  )  ; 
newlinks  .  add  (  "http://www.casaca.com.br/home/"  )  ; 
newlinks  .  add  (  "http://www.haibozhao.com/"  )  ; 
newlinks  .  add  (  "http://www.math.auc.dk/f-sn/civilsoftw.html"  )  ; 
newlinks  .  add  (  "http://www.microsoft.com/technet/sysinternals/default.mspx"  )  ; 
newlinks  .  add  (  "http://www.cm.is.ritsumei.ac.jp/c5-08/index.php?Call%20For%20Papers"  )  ; 
newlinks  .  add  (  "http://www.ux-sa.com/2007/09/structural-holes-and-online-social.html"  )  ; 
newlinks  .  add  (  "http://www.prenhall.com/"  )  ; 
newlinks  .  add  (  "http://isd.ktu.lt/MINE/"  )  ; 
newlinks  .  add  (  "http://www.cancaonova.com/portal/canais/liturgia/"  )  ; 
newlinks  .  add  (  "http://www.adapt.dk/"  )  ; 
newlinks  .  add  (  "http://www.istartedsomething.com/20060622/installing-and-uninstall-vista-beta-2-for-dual-boot-with-xp/"  )  ; 
newlinks  .  add  (  "http://pensarpython.incubadora.fapesp.br/portal"  )  ; 
newlinks  .  add  (  "http://ontology.buffalo.edu/"  )  ; 
newlinks  .  add  (  "http://www.it-personal.ch/links/index.html"  )  ; 
newlinks  .  add  (  "http://pt.shvoong.com/"  )  ; 
newlinks  .  add  (  "http://www.math.utah.edu/lab/ms/matlab/matlab.html"  )  ; 
newlinks  .  add  (  "http://www.merriam-webster.com/"  )  ; 
newlinks  .  add  (  "http://www.biozonen.dk/"  )  ; 
newlinks  .  add  (  "http://www.analytictech.com/borgatti/borg_social_capital_measures.htm"  )  ; 
newlinks  .  add  (  "http://www.getluky.net/freetag/"  )  ; 
newlinks  .  add  (  "http://web.mit.edu/6.035/www/index.html"  )  ; 
newlinks  .  add  (  "http://www.danga.com/memcached/"  )  ; 
newlinks  .  add  (  "http://www.aalborg.dk/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*newlinks.addhttp://pets.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.fsc.ufsc.br/~canzian/oficina/roteiro/Gauss.htm"  )  ; 
newlinks  .  add  (  "http://www.sk.com.br/sk.html"  )  ; 
newlinks  .  add  (  "http://www.wenxuecity.com/"  )  ; 
newlinks  .  add  (  "http://cancerweb.ncl.ac.uk/omd/"  )  ; 
newlinks  .  add  (  "http://www.hahn-express.de/hahn_bus_popup/default.htm"  )  ; 
newlinks  .  add  (  "http://economia.uol.com.br/ultnot/2008/05/14/guia_bovespa_bolsa_valores_aplicar_acoes.jhtm"  )  ; 
newlinks  .  add  (  "http://www.mezz.nl/"  )  ; 
newlinks  .  add  (  "http://educ.queensu.ca/~russellt/forum/1996a.htm"  )  ; 
newlinks  .  add  (  "http://www.lexmark.dk/support/"  )  ; 
newlinks  .  add  (  "http://www.questia.com/read/93928903"  )  ; 
newlinks  .  add  (  "http://www.fastcompany.com/magazine/122/is-the-tipping-point-toast.html"  )  ; 
newlinks  .  add  (  "http://www.nba.com/suns/"  )  ; 
newlinks  .  add  (  "http://stillinger.aau.dk/vis.php?nr=2238"  )  ; 
newlinks  .  add  (  "http://www.elisabethsalgadoencontrandovoce.com/ajudando_aluno_como_estudar.htm"  )  ; 
newlinks  .  add  (  "http://www.meiobit.com/google/opensocial_converg_ncia_das_redes_sociais"  )  ; 
newlinks  .  add  (  "http://www.geocities.com/projetoperiferia2/secB1.htm"  )  ; 
newlinks  .  add  (  "http://www.howtocreate.co.uk/tutorials/javascript/dombasics"  )  ; 
newlinks  .  add  (  "http://aied.inf.ed.ac.uk/"  )  ; 
newlinks  .  add  (  "http://www.liferay.com/web/guest/home"  )  ; 
newlinks  .  add  (  "http://cs03.ewedding.com/v30/main.php?a=gemaandbart"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/python-list.htm"  )  ; 
newlinks  .  add  (  "http://www.librarything.com/thingology/2007/02/when-tags-works-and-when-they-dont.php"  )  ; 
newlinks  .  add  (  "http://www.softronix.com/logo.html"  )  ; 
newlinks  .  add  (  "http://www.terra.com.br/capa/"  )  ; 
newlinks  .  add  (  "http://noolah.com/"  )  ; 
newlinks  .  add  (  "http://www.abassoftware.com/en/video/awb/pflitsch150.htm"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*newlinks.addhttp://classifieds.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.cidadedocerebro.com.br/"  )  ; 
newlinks  .  add  (  "http://www.nap.edu/readingroom/books/mentor/"  )  ; 
newlinks  .  add  (  "http://virtualsociety.sbs.ox.ac.uk/projects/lea.htm"  )  ; 
newlinks  .  add  (  "http://www.inf.puc-rio.br/"  )  ; 
newlinks  .  add  (  "http://sourceforge.net/projects/opentaps/"  )  ; 
newlinks  .  add  (  "http://www.freikarte.at/"  )  ; 
newlinks  .  add  (  "http://code.google.com/p/java2python/"  )  ; 
newlinks  .  add  (  "http://www.uxmatters.com/MT/archives/000209.php"  )  ; 
newlinks  .  add  (  "http://swc.scipy.org/"  )  ; 
newlinks  .  add  (  "http://tw.rpi.edu/portal/AAAI-SSS-09:_Social_Semantic_Web:_Where_Web_2.0_Meets_Web_3.0"  )  ; 
newlinks  .  add  (  "http://intranetblog.blogware.com/blog/_archives/2006/6/2/2001806.html"  )  ; 
newlinks  .  add  (  "http://www.systemiclogic.com/Welcome.html"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*newlinks.addhttp://health.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://java-source.net/open-source/web-testing-tools"  )  ; 
newlinks  .  add  (  "http://www.simplehelp.net/2006/10/08/10-os-x-finder-alternatives-compared-and-reviewed/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/toplevel/ymsgr8/*newlinks.addhttp://bookmarks.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.feijipiao.cn/"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/watch?v=M0ODskdEPnQ"  )  ; 
newlinks  .  add  (  "http://www.allposters.com/gallery.asp?CID=C8F794AAB63C450880B4613911C3D4DC&startat=http%3A//www.allposters.com/GetPoster.asp%3FAPNum%3D940856%26CID%3DC8F794AAB63C450880B4613911C3D4DC%26PPID%3D1%26search%3Dcasablanca%26f%3Dc%26FindID%3D188%26P%3D1%26PP%3D1%26sortby%3DPD%26cname%3DCasablanca%26SearchID%3D"  )  ; 
newlinks  .  add  (  "http://www.neon-project.org/"  )  ; 
newlinks  .  add  (  "http://ao.stratics.com/"  )  ; 
newlinks  .  add  (  "http://www.eclipse.org/articles/article.php?file=Article-AddingHelpToRCP/index.html"  )  ; 
newlinks  .  add  (  "http://www.sun.com/applink?SunDispatchService=JavaDesktopSystem&SunDispatchSubService=bookmark&SunDispatchValue=faq&SunDispatchVersion=3.0&SunDispatchLanguage=en&SunDispatchType=REDIR"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000179.php"  )  ; 
newlinks  .  add  (  "http://www.tv2.dk/"  )  ; 
newlinks  .  add  (  "http://www.bbc.co.uk/"  )  ; 
newlinks  .  add  (  "http://www.pantz.org/database/mysql/mysqlcommands.shtml"  )  ; 
newlinks  .  add  (  "http://www.nedesigns.com/"  )  ; 
newlinks  .  add  (  "http://www.svd.se/"  )  ; 
newlinks  .  add  (  "http://neologism.deri.ie/"  )  ; 
newlinks  .  add  (  "http://www.wired.com/wired/archive/9.12/aspergers.html?pg=1&topic=&topic_set="  )  ; 
newlinks  .  add  (  "http://flash-plaice.wikispaces.com/"  )  ; 
newlinks  .  add  (  "http://mcs.open.ac.uk/mj665/papers.html"  )  ; 
newlinks  .  add  (  "http://www.treehugger.com/files/2008/07/treehugger_tips_bike_to_work.php"  )  ; 
newlinks  .  add  (  "http://www.intel.com/"  )  ; 
newlinks  .  add  (  "http://semwiq.faw.uni-linz.ac.at/node/9"  )  ; 
newlinks  .  add  (  "http://www.fh-salzburg.ac.at/"  )  ; 
newlinks  .  add  (  "http://asc-parc.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://www.goodprice.ch/novabau/index.php?link=sanitaer.badezimmer_moebel.branchetti&see=1"  )  ; 
newlinks  .  add  (  "http://www.visitaalborg.com/international/en-gb/menu/tourist/turist-maalgruppe-forside.htm"  )  ; 
newlinks  .  add  (  "http://www2.glos.ac.uk/gdn/gibbs/index.htm"  )  ; 
newlinks  .  add  (  "http://beta.network.org/"  )  ; 
newlinks  .  add  (  "http://jasonderuna.com/blog/2008/01/13/when-second-wave-hci-meets-third-wave-challenges/"  )  ; 
newlinks  .  add  (  "http://jmvidal.cse.sc.edu/uscthesis/"  )  ; 
newlinks  .  add  (  "http://www.sigoa.org/"  )  ; 
newlinks  .  add  (  "http://www.wbuf.noaa.gov/tempfc.htm"  )  ; 
newlinks  .  add  (  "http://loadingreadyrun.com/showmovie.php?x=480&y=360&url=talklikepirate.mov"  )  ; 
newlinks  .  add  (  "http://red-inklings.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://moblin.org/index.html"  )  ; 
newlinks  .  add  (  "http://www.sao.dk/"  )  ; 
newlinks  .  add  (  "http://www.squeak.org/"  )  ; 
newlinks  .  add  (  "http://yahooresearchberkeley.com/blog/2007/05/16/the-emerging-semantics-web-the-semantic-web-is-dead/"  )  ; 
newlinks  .  add  (  "http://www.dr.dk/nyheder/baggrund/article.jhtml?articleID=119744"  )  ; 
newlinks  .  add  (  "http://www.inf.pucrs.br/ihc2008/pt-br/"  )  ; 
newlinks  .  add  (  "http://www.kuechenradio.org/wp/"  )  ; 
newlinks  .  add  (  "http://www.psych-sci.manchester.ac.uk/staff/MartinLea"  )  ; 
newlinks  .  add  (  "http://www.anarchy-online.com/content/news/articles/6100L/"  )  ; 
newlinks  .  add  (  "http://www.schwartzman.org.br/simon/regras.htm"  )  ; 
newlinks  .  add  (  "http://www.originalsignal.com/"  )  ; 
newlinks  .  add  (  "http://www.one.com/"  )  ; 
newlinks  .  add  (  "http://www.avatarexperience.eu/"  )  ; 
newlinks  .  add  (  "http://www.pycheesecake.org/wiki/PythonTestingToolsTaxonomy"  )  ; 
newlinks  .  add  (  "http://www.w3.org/2001/sw/"  )  ; 
newlinks  .  add  (  "http://www.linkedin.com/in/mazzolaluca"  )  ; 
newlinks  .  add  (  "http://www.guj.com.br/posts/list/29710.java"  )  ; 
newlinks  .  add  (  "http://www.via6.com/topico.php?tid=107469&uid=marcioeduardo&cid="  )  ; 
newlinks  .  add  (  "http://allsp.com/"  )  ; 
newlinks  .  add  (  "http://infocentre.frontend.com/infocentre/articles/backtobasics.html"  )  ; 
newlinks  .  add  (  "http://martinwaiss.com/2008/09/18/wie-man-die-ganze-blogosphaere-mit-einem-post-umstuerzen-kann/"  )  ; 
newlinks  .  add  (  "http://www.syntagm.co.uk/design/articles/howmany.htm"  )  ; 
newlinks  .  add  (  "http://www.haskell.org/~pairwise/intro/intro.html"  )  ; 
newlinks  .  add  (  "http://radar.oreilly.com/archives/2008/02/reuters_semantic_web_moneytech.html"  )  ; 
newlinks  .  add  (  "http://theocacao.com/document.page/414"  )  ; 
newlinks  .  add  (  "http://pt.wikipedia.org/wiki/Usabilidade"  )  ; 
newlinks  .  add  (  "http://www.irisa.fr/bunraku/OpenViBE/wiki/index.php?title=Main_Page"  )  ; 
newlinks  .  add  (  "http://www.ddhw.cn/"  )  ; 
newlinks  .  add  (  "http://ordbok.lagom.nl/woordenboek.html"  )  ; 
newlinks  .  add  (  "http://www.allposters.com/gallery.asp?CID=A7E5498C4BF34A108795027EC0BFAACC&startat=http%3A//www.allposters.com/GetPoster.asp%3FAPNum%3D415513%26CID%3DA7E5498C4BF34A108795027EC0BFAACC%26PPID%3D1%26search%3Dansel%2520adams%26f%3Dc%26FindID%3D22099%26P%3D1%26PP%3D8%26sortby%3DPD%26cname%3DAnsel+Adams%26SearchID%3D"  )  ; 
newlinks  .  add  (  "http://www.eclipse.org/articles/article.php?file=Article-Forms33/index.html"  )  ; 
newlinks  .  add  (  "http://adblockplus.org/en/"  )  ; 
newlinks  .  add  (  "http://www.hasenkopf.de/index_c.php?external=1&js=../inc/js_c.php&imgID=c2&bild=&div=c&pageHeader=FONTANA%20Waschbecken-Varianten&bgImg=bg_bottom&main=content_c/wa_loes.php&marginLeft=5&navHere=221&sessionID="  )  ; 
newlinks  .  add  (  "http://sourceforge.net/projects/ikewiki/"  )  ; 
newlinks  .  add  (  "http://cursa.ihmc.us/servlet/SBReadResourceServlet?rid=1110141466123_777415093_4926&partName=htmltext"  )  ; 
newlinks  .  add  (  "http://www.meebo.com/"  )  ; 
newlinks  .  add  (  "http://192.168.0.1/start.htm"  )  ; 
newlinks  .  add  (  "http://www.gnu.org/software/src-highlite/"  )  ; 
newlinks  .  add  (  "http://www.readwriteweb.com/archives/with_iceberg_everyone_can_program.php"  )  ; 
newlinks  .  add  (  "http://www.techeblog.com/index.php/tech-gadget/top-5-photoshop-tutorials"  )  ; 
newlinks  .  add  (  "http://ir.exp.sis.pitt.edu/gale/news/help/help.html"  )  ; 
newlinks  .  add  (  "http://dictionary.cambridge.org/"  )  ; 
newlinks  .  add  (  "https://onba.zkb.ch/"  )  ; 
newlinks  .  add  (  "http://aim.central/"  )  ; 
newlinks  .  add  (  "http://www.rideforbund.dk/"  )  ; 
newlinks  .  add  (  "http://www.degulesider.dk/"  )  ; 
newlinks  .  add  (  "http://www.gutenberg.org/wiki/Science_Fiction_"  )  ; 
newlinks  .  add  (  "http://psp-news.blogs.sapo.pt/"  )  ; 
newlinks  .  add  (  "http://www.intellectualwhores.com/masterladder.html"  )  ; 
newlinks  .  add  (  "http://blogs.zdnet.com/Hinchcliffe/?cat=16"  )  ; 
newlinks  .  add  (  "http://www.wetteronline.de/"  )  ; 
newlinks  .  add  (  "http://www.edge.org/3rd_culture/lanier06/lanier06_index.html"  )  ; 
newlinks  .  add  (  "http://englishrussia.com/?p=1060#more-1060"  )  ; 
newlinks  .  add  (  "http://ouseful.wordpress.com/"  )  ; 
newlinks  .  add  (  "http://www.pantheon.org/areas/all/articles.html"  )  ; 
newlinks  .  add  (  "http://tug.org/PSTricks/main.cgi/"  )  ; 
newlinks  .  add  (  "http://rur-ple.sourceforge.net/en/rur.htm"  )  ; 
newlinks  .  add  (  "http://www.shanghanlun.com.cn/"  )  ; 
newlinks  .  add  (  "http://www.wikiviz.org/wiki/Main_Page"  )  ; 
newlinks  .  add  (  "http://www.visual-literacy.org/periodic_table/periodic_table.html"  )  ; 
newlinks  .  add  (  "http://www.ismaskinen.dk/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*newlinks.addhttp://greetings.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Wikipedia:Category#How_to_create_categories"  )  ; 
newlinks  .  add  (  "http://www.boliga.dk/"  )  ; 
newlinks  .  add  (  "http://nitens.org/taraborelli/cvtex"  )  ; 
newlinks  .  add  (  "http://www.labiutil.inf.ufsc.br/"  )  ; 
newlinks  .  add  (  "http://www.wethinkthebook.net/home.aspx"  )  ; 
newlinks  .  add  (  "http://www.zephoria.org/thoughts/archives/2008/09/21/i_will_be_joini.html"  )  ; 
newlinks  .  add  (  "http://www.developer.com/java/other/article.php/1546201"  )  ; 
newlinks  .  add  (  "http://www.ah2008.org/index.php?section=1"  )  ; 
newlinks  .  add  (  "http://rachel.ns.purchase.edu/~Jeanine/jsexamples.html"  )  ; 
newlinks  .  add  (  "http://www.ctistartup.ch/"  )  ; 
newlinks  .  add  (  "http://aopocket.tngk.com/"  )  ; 
newlinks  .  add  (  "http://www.rsarchive.org/index.php"  )  ; 
newlinks  .  add  (  "http://www.lifewithalacrity.com/2004/10/tracing_the_evo.html"  )  ; 
newlinks  .  add  (  "http://www.sciencedirect.com/science?_ob=ArticleURL&_udi=B6WJB-4H74MFF-1&_user=6239427&_rdoc=1&_fmt=&_orig=search&_sort=d&view=c&_version=1&_urlVersion=0&_userid=6239427&md5=20de6446db64e44c60c94cd012ca0963"  )  ; 
newlinks  .  add  (  "http://blog.tv2.dk/migselvkitsa/"  )  ; 
newlinks  .  add  (  "http://www.prolearn-academy.org/"  )  ; 
newlinks  .  add  (  "http://www.facebook.com/"  )  ; 
newlinks  .  add  (  "http://www.smashingmagazine.com/2007/02/09/83-beautiful-wordpress-themes-you-probably-havent-seen/"  )  ; 
newlinks  .  add  (  "http://up.ucsd.edu/about/"  )  ; 
newlinks  .  add  (  "http://sparql-wrapper.sourceforge.net/"  )  ; 
newlinks  .  add  (  "http://www.guardian.co.uk/"  )  ; 
newlinks  .  add  (  "http://www.aace.org/conf/ELEARN/"  )  ; 
newlinks  .  add  (  "http://www.wikipedia.com/"  )  ; 
newlinks  .  add  (  "http://failblog.org/"  )  ; 
newlinks  .  add  (  "http://cid-ae935b3cde8015dd.skydrive.live.com/browse.aspx/Excel%20.NetMap%20-%20Social%20Network%20Add-in%20for%20Excel%202007"  )  ; 
newlinks  .  add  (  "http://www.reputation09.net/"  )  ; 
newlinks  .  add  (  "http://www.inf.pucrs.br/ihc2008/pt-br/categorias.php?conteudo=Categorias%20de%20Submiss%C3%A3o&tipo=workshops"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/watch?v=FqOfXumI18A"  )  ; 
newlinks  .  add  (  "http://kapustar.punkt.at/labs/knowledgelounge.org/"  )  ; 
newlinks  .  add  (  "http://www.utexas.edu/computer/vcl/"  )  ; 
newlinks  .  add  (  "http://www.kuren.org/ao/"  )  ; 
newlinks  .  add  (  "http://www.leo.org/"  )  ; 
newlinks  .  add  (  "http://www.cs.iastate.edu/~yasser/wlsvm/"  )  ; 
newlinks  .  add  (  "http://www.microsoft.com/isapi/redir.dll?prd=ie&pver=6&ar=IStart"  )  ; 
newlinks  .  add  (  "http://www.engineeringvillage2.org.cn/controller/servlet/Controller"  )  ; 
newlinks  .  add  (  "http://office.microsoft.com/en-us/help/HA012261671033.aspx"  )  ; 
newlinks  .  add  (  "http://latex.yauh.de/faq/index.html"  )  ; 
newlinks  .  add  (  "http://web-cat.cs.vt.edu/CsEdWiki/Virginia_Tech_CS1"  )  ; 
newlinks  .  add  (  "http://www.naturalspeech.com/"  )  ; 
newlinks  .  add  (  "http://www.redmine.org/"  )  ; 
newlinks  .  add  (  "http://videolectures.net/wsdm08_stanford/"  )  ; 
newlinks  .  add  (  "https://www.limofoundation.org/sf/sfmain/do/home"  )  ; 
newlinks  .  add  (  "http://video.google.com/videoplay?docid=4068767512452275304&q=fall+out+boy"  )  ; 
newlinks  .  add  (  "http://blogs.sun.com/richb/resource/freeware-list.html"  )  ; 
newlinks  .  add  (  "http://projmblog.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://ilps.science.uva.nl/twiki/bin/view/Main/WebHome"  )  ; 
newlinks  .  add  (  "http://www.ug.it.usyd.edu.au/~soft2130/index.cgi"  )  ; 
newlinks  .  add  (  "http://adm.aau.dk/fak-tekn/phd/jointreg.htm#APPENDIX%203"  )  ; 
newlinks  .  add  (  "http://nocash.emubase.de/gbatek.htm"  )  ; 
newlinks  .  add  (  "http://www.nist.gov/dads/"  )  ; 
newlinks  .  add  (  "http://forums.anarchyonline.com/showthread.php?p=4762640#post4762640"  )  ; 
newlinks  .  add  (  "http://cbaum.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://developer.apple.com/techpubs/mac/HIGuidelines/HIGuidelines-2.html"  )  ; 
newlinks  .  add  (  "http://planetrdf.com/"  )  ; 
newlinks  .  add  (  "http://pt-br.fxfeeds.mozilla.com/pt-BR/firefox/headlines.xml"  )  ; 
newlinks  .  add  (  "http://www.logo.com/imagine/"  )  ; 
newlinks  .  add  (  "http://www.emmssa.org/"  )  ; 
newlinks  .  add  (  "http://rdfohloh.wikier.org/"  )  ; 
newlinks  .  add  (  "http://aovault.ign.com/"  )  ; 
newlinks  .  add  (  "http://wsdiamond.di.unito.it/"  )  ; 
newlinks  .  add  (  "http://www.wthreex.com/rup/process/artifact/ovu_arts.htm"  )  ; 
newlinks  .  add  (  "http://www.jp.dk/"  )  ; 
newlinks  .  add  (  "http://www.ibm.com/developerworks/webservices/library/ws-reuse-soa.html"  )  ; 
newlinks  .  add  (  "http://www.the-acap.org/implement-acap.php"  )  ; 
newlinks  .  add  (  "http://ikewiki.salzburgresearch.at/"  )  ; 
newlinks  .  add  (  "http://www.punkt.at/"  )  ; 
newlinks  .  add  (  "http://portal.acm.org/citation.cfm?id=1255175.1255198"  )  ; 
newlinks  .  add  (  "http://www.stefansen.dk/"  )  ; 
newlinks  .  add  (  "http://hci.usask.ca/publications/1998/awareness-chi/effects-of-awareness-support.pdf"  )  ; 
newlinks  .  add  (  "http://antbeard.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://www.e-fense.com/helix/"  )  ; 
newlinks  .  add  (  "http://www.seipp.de/html/wohnthemen/ief_wohnthemen.html"  )  ; 
newlinks  .  add  (  "http://isedj.org/isecon/2000/405/"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Environmental_psychology"  )  ; 
newlinks  .  add  (  "http://www.nsf.gov/publications/pub_summ.jsp?ods_key=nsf08204"  )  ; 
newlinks  .  add  (  "http://www.uncrate.com/"  )  ; 
newlinks  .  add  (  "http://unabridged.merriam-webster.com/"  )  ; 
newlinks  .  add  (  "http://tagcrowd.com/"  )  ; 
newlinks  .  add  (  "http://meta.wikimedia.org/wiki/WYSIWYG_editor"  )  ; 
newlinks  .  add  (  "http://www.entropy.ch/software/macosx/welcome.html"  )  ; 
newlinks  .  add  (  "http://radar.oreilly.com/archives/2007/01/why_johnny_cant.html"  )  ; 
newlinks  .  add  (  "http://www.cio.com/archive/040106/et_main.html"  )  ; 
newlinks  .  add  (  "http://www.guild-awakened.net/forum/"  )  ; 
newlinks  .  add  (  "http://www.allposters.com/gallery.asp?CID=7A1E37544AA041A794A98E0A0C605606&txtSearch=Le+Seize+Septembre&image1.x=0&image1.y=0"  )  ; 
newlinks  .  add  (  "http://www.criarweb.com/artigos/682.php"  )  ; 
newlinks  .  add  (  "http://www.specialolympics.org/"  )  ; 
newlinks  .  add  (  "http://www.jnd.org/dn.mss/NorthwesternCommencement.html"  )  ; 
newlinks  .  add  (  "http://www.cs.utexas.edu/users/mfkb/index.html"  )  ; 
newlinks  .  add  (  "http://www.mapasmentais.com.br/"  )  ; 
newlinks  .  add  (  "http://www.microsoft.com/usability/UEPostings/p9-hanna.pdf"  )  ; 
newlinks  .  add  (  "http://www.ic.unicamp.br/~cmbm/desafios_SBC/CeciliaBaranauskas.pdf"  )  ; 
newlinks  .  add  (  "http://ii2.sourceforge.net/tex-index.html"  )  ; 
newlinks  .  add  (  "http://thoduv.drunkencoders.com/"  )  ; 
newlinks  .  add  (  "http://www.informatik.uni-trier.de/~ley/db/indices/a-tree/w/Weske:Mathias.html"  )  ; 
newlinks  .  add  (  "http://www.interaction-design.org/calendar/"  )  ; 
newlinks  .  add  (  "http://www.jooneworld.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/yroot/ymsgr8/*newlinks.addhttp://travel.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://msn.foxsports.com/fantasy/football/commissioner/league/home?leagueId=15302"  )  ; 
newlinks  .  add  (  "http://rachel.ns.purchase.edu/~Jeanine/flashlabs.html"  )  ; 
newlinks  .  add  (  "http://www.easyeclipse.org/site/distributions/python.html"  )  ; 
newlinks  .  add  (  "http://www.alpheccar.org/en/posts/show/67"  )  ; 
newlinks  .  add  (  "http://www.erobertparker.com/"  )  ; 
newlinks  .  add  (  "http://www.namastecafe.com/coffee_names.htm"  )  ; 
newlinks  .  add  (  "http://www.ubuntu.com/"  )  ; 
newlinks  .  add  (  "http://www.enalyzer.dk/news.htm#578"  )  ; 
newlinks  .  add  (  "http://dict.leo.org/?lang=de&lp=ende"  )  ; 
newlinks  .  add  (  "http://www.nepentheim.com/rkplayfields/ingamemaphuge.html"  )  ; 
newlinks  .  add  (  "http://home.netscape.com/bookmark/4_5/games.html"  )  ; 
newlinks  .  add  (  "http://www.rise.com.br/"  )  ; 
newlinks  .  add  (  "http://www.folha.uol.com.br/"  )  ; 
newlinks  .  add  (  "http://socialmedia.typepad.com/blog/2008/10/social-network-visualization-eyecandy.html"  )  ; 
newlinks  .  add  (  "http://www.semanticweb.gr/TheaOWLLib/"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/watch?v=YerJLZr6LEg&search=stephen%20lynch"  )  ; 
newlinks  .  add  (  "http://www.hvv.de/"  )  ; 
newlinks  .  add  (  "http://www.javascriptkit.com/script/script2/ajaxtooltip.shtml"  )  ; 
newlinks  .  add  (  "http://www.microsoft.com/isapi/redir.dll?prd=ie&ar=windowsmedia"  )  ; 
newlinks  .  add  (  "http://www.techcrunch.com/2007/09/06/exclusive-screen-shots-and-feature-overview-of-delicious-20-preview/"  )  ; 
newlinks  .  add  (  "http://www.computer.org/portal/site/csdl/index.jsp"  )  ; 
newlinks  .  add  (  "http://iswc2008.semanticweb.org/"  )  ; 
newlinks  .  add  (  "http://docs.plt-scheme.org/index.html"  )  ; 
newlinks  .  add  (  "http://www.befunky.com/"  )  ; 
newlinks  .  add  (  "http://www.dca.fee.unicamp.br/cursos/EA876/apostila/HTML/node24.html"  )  ; 
newlinks  .  add  (  "http://www.zomganime.com/"  )  ; 
newlinks  .  add  (  "http://www.w3.org/"  )  ; 
newlinks  .  add  (  "http://www.gentoo.org/"  )  ; 
newlinks  .  add  (  "http://www.twitkrit.de/2008/09/17/125/"  )  ; 
newlinks  .  add  (  "http://www.hafniafloorball.dk/newlinks.add"  )  ; 
newlinks  .  add  (  "http://learn.creativecommons.org/newlinks"  )  ; 
newlinks  .  add  (  "http://www.cs.usyd.edu.au/~judy/"  )  ; 
newlinks  .  add  (  "http://www.latex-project.org/intro.html"  )  ; 
newlinks  .  add  (  "http://www.mkbergman.com/?page_id=325"  )  ; 
newlinks  .  add  (  "http://www.winsupersite.com/"  )  ; 
newlinks  .  add  (  "http://zoom-mail1.swiss/en/mail.html?sid=78YHxN4O3xg&lang=en&cert=false"  )  ; 
newlinks  .  add  (  "http://www.news.com.au/heraldsun/story/0,21985,22556281-661,00.html"  )  ; 
newlinks  .  add  (  "http://www.ultimate-guitar.com/"  )  ; 
newlinks  .  add  (  "http://www.rise.com.br/research/"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Psychiatric_medication"  )  ; 
newlinks  .  add  (  "http://cordis.europa.eu/fp7/home_en.html"  )  ; 
newlinks  .  add  (  "http://www.microsoft.com/isapi/redir.dll?prd=ie&pver=5.0&ar=CLinks"  )  ; 
newlinks  .  add  (  "http://www.multiseries.com/multiseries/"  )  ; 
newlinks  .  add  (  "http://www.mister-wong.de/"  )  ; 
newlinks  .  add  (  "http://nepomuk.semanticdesktop.org/xwiki/bin/view/Main1/Publications"  )  ; 
newlinks  .  add  (  "http://www.dmi.dk/vejr/turist/turist1.UK.html"  )  ; 
newlinks  .  add  (  "http://gr.ayre.st/~grayrest/animator/animator.html"  )  ; 
newlinks  .  add  (  "http://www.microsoft.com/isapi/redir.dll?prd=ie&ar=hotmail"  )  ; 
newlinks  .  add  (  "http://www.brandspankingnew.net/archive/2005/05/flash_sliders_i.html"  )  ; 
newlinks  .  add  (  "http://www.bloomberg.com/markets/commodities/cfutures.html"  )  ; 
newlinks  .  add  (  "http://www.readwriteweb.com/archives/enterprise_20_nature_of_the_firm.php"  )  ; 
newlinks  .  add  (  "http://danzarrella.com/viral-marketing-glossary"  )  ; 
newlinks  .  add  (  "http://redcouch.typepad.com/"  )  ; 
newlinks  .  add  (  "http://www.juliobattisti.com.br/tutoriais/default.asp?cat=0009&ast=0021"  )  ; 
newlinks  .  add  (  "http://www.cs.le.ac.uk/people/elaw/"  )  ; 
newlinks  .  add  (  "http://www.w3.org/2007/01/wos-ec-program.html"  )  ; 
newlinks  .  add  (  "http://www.ddj.com/blog/portal/archives/2007/06/python_parallel.html"  )  ; 
newlinks  .  add  (  "http://www.amazon.com/gp/product/1570543208/qid=1147551593/sr=1-1/ref=sr_1_1/104-8276090-1783137?s=books&v=glance&n=283155"  )  ; 
newlinks  .  add  (  "http://alimanfoo.wordpress.com/"  )  ; 
newlinks  .  add  (  "http://www.martinkahr.com/source-code"  )  ; 
newlinks  .  add  (  "http://marcelotas.blog.uol.com.br/"  )  ; 
newlinks  .  add  (  "http://go.microsoft.com/fwlink/?linkid=68919"  )  ; 
newlinks  .  add  (  "http://www.iwm-kmrc.de/workshops/dlac/"  )  ; 
newlinks  .  add  (  "http://www.chris-hooley.com/2007/05/18/canonicalize-yo-shizzle-with-htaccess-every-time/"  )  ; 
newlinks  .  add  (  "http://www.triple-s.ch/"  )  ; 
newlinks  .  add  (  "http://www.linkcentral.dk/mobil.htm"  )  ; 
newlinks  .  add  (  "http://www.mycoted.com/Main_Page"  )  ; 
newlinks  .  add  (  "http://www.aprilzero.com/?blog/comments/web_workflow_part_i/"  )  ; 
newlinks  .  add  (  "http://www.all-guitar-chords.com/"  )  ; 
newlinks  .  add  (  "http://devcentral.iticentral.com/"  )  ; 
newlinks  .  add  (  "http://www.xe.com/"  )  ; 
newlinks  .  add  (  "http://gamehall.uol.com.br/dsmaniac/"  )  ; 
newlinks  .  add  (  "http://www.newsinside.org/"  )  ; 
newlinks  .  add  (  "http://ontologydesignpatterns.org/index.php/"  )  ; 
newlinks  .  add  (  "http://www.americansc.org.uk/Reviews/Organisational_Complex.htm"  )  ; 
newlinks  .  add  (  "http://home.netscape.com/bookmark/4_5/theglobe.html"  )  ; 
newlinks  .  add  (  "http://go.microsoft.com/fwlink/?LinkId=53540"  )  ; 
newlinks  .  add  (  "http://support.microsoft.com/kb/290953"  )  ; 
newlinks  .  add  (  "http://www.infoq.com/regvalidation.action?token=kk5HNp9BctIWsTsnoP0SaLqzBMR3Mayp"  )  ; 
newlinks  .  add  (  "http://www.guilhermo.com/ai_biblioteca/referencia.asp?referencia=127"  )  ; 
newlinks  .  add  (  "http://moinmo.in/MoinMoin"  )  ; 
newlinks  .  add  (  "http://www.coli.uni-sb.de/~bos/doris/"  )  ; 
newlinks  .  add  (  "http://www.gamedev.net/"  )  ; 
newlinks  .  add  (  "http://virgin-express.com/home.html"  )  ; 
newlinks  .  add  (  "http://www.adaptavist.com/display/Builder/Builder+3.x+Icons"  )  ; 
newlinks  .  add  (  "http://www.urbandictionary.com/define.php?term=karma"  )  ; 
newlinks  .  add  (  "http://www.mapletop.com/"  )  ; 
newlinks  .  add  (  "http://elearningtech.blogspot.com/2006/09/personal-and-group-learning-using-web.html"  )  ; 
newlinks  .  add  (  "http://www.prosa.dk/"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/watch?v=JMRF_ZXms9E"  )  ; 
newlinks  .  add  (  "http://www3.hi.is/~ebba/index-english.html"  )  ; 
newlinks  .  add  (  "http://129.156.93.167/"  )  ; 
newlinks  .  add  (  "http://pbli.org/pbl/generic_pbl.htm"  )  ; 
newlinks  .  add  (  "http://www.visualthesaurus.com/"  )  ; 
newlinks  .  add  (  "http://supervisord.org/"  )  ; 
newlinks  .  add  (  "http://estilo.uol.com.br/moda/album/gravatas2passos_album.jhtm?abrefoto=7"  )  ; 
newlinks  .  add  (  "http://books.google.com/books?hl=pt-BR&lr=&id=rPt7wRIcgLQC&oi=fnd&pg=RA1-PR11&sig=vMZ9_dmduc7U0k4p7U5v3yQ69t0&dq=de+george+richard+epistemic+authority#PPR7,M1"  )  ; 
newlinks  .  add  (  "http://swaml.berlios.de/"  )  ; 
newlinks  .  add  (  "http://www.hotelmadrid.com.br/hotel_fotos_quarto_conforto.html"  )  ; 
newlinks  .  add  (  "http://www.scholarpedia.org/"  )  ; 
newlinks  .  add  (  "http://www.fdm.dk/public/biler/trafiksikkerhed/bornibilen/barnestole/"  )  ; 
newlinks  .  add  (  "http://www.videocure.com/"  )  ; 
newlinks  .  add  (  "http://www.modernboard.de/board.php?boardid=391"  )  ; 
newlinks  .  add  (  "http://www.marxists.org/portugues/einstein/1949/05/por_que_socialismo.htm"  )  ; 
newlinks  .  add  (  "http://tv.globo.com/"  )  ; 
newlinks  .  add  (  "http://msdn.microsoft.com/ui/"  )  ; 
newlinks  .  add  (  "http://www.nordjyske.dk/"  )  ; 
newlinks  .  add  (  "http://scripts.sil.org/cms/scripts/page.php?site_id=nrsi&item_id=inputtoollinks"  )  ; 
newlinks  .  add  (  "http://www.springerlink.com/content/u2h4156j46272540/"  )  ; 
newlinks  .  add  (  "http://www.grapple-project.org/"  )  ; 
newlinks  .  add  (  "http://www.chem.uni-wuppertal.de/conference/latexinstruct.html"  )  ; 
newlinks  .  add  (  "http://coco.edfac.usyd.edu.au/"  )  ; 
newlinks  .  add  (  "https://onestop.sfbay/survey/viewResultsDetails.jsp?surveyId=1159483878953&section=0&question=16"  )  ; 
newlinks  .  add  (  "http://www.summitpost.org/view_object.php?object_id=110504"  )  ; 
newlinks  .  add  (  "http://foo.phl.univie.ac.at/mediawiki/index.php/Kolloquium"  )  ; 
newlinks  .  add  (  "http://pawpeds.com/"  )  ; 
newlinks  .  add  (  "http://blog.mathemagenic.com/feed/"  )  ; 
newlinks  .  add  (  "http://www.acm.org/conferences/sac/sac2009/"  )  ; 
newlinks  .  add  (  "http://wordle.net/"  )  ; 
newlinks  .  add  (  "http://psp.sagepub.com/cgi/content/refs/27/5/526"  )  ; 
newlinks  .  add  (  "http://www.anarchy-online.com/content/downloads/patches/"  )  ; 
newlinks  .  add  (  "http://www.iicm.tugraz.at/ViWo2009"  )  ; 
newlinks  .  add  (  "http://www.readwriteweb.com/archives/the_best_tools_for_visualization.php"  )  ; 
newlinks  .  add  (  "http://dewey.soe.berkeley.edu/boxer/"  )  ; 
newlinks  .  add  (  "http://www.anarchyarcanum.com/"  )  ; 
newlinks  .  add  (  "http://www.norvig.com/paip.html"  )  ; 
newlinks  .  add  (  "http://www.acidlabs.org/2008/08/26/speaking-at-govis-2009-user-centred-government-more-than-meets-the-eye/"  )  ; 
newlinks  .  add  (  "http://www.smallbytes.net/~bobkat/jesterlist.html"  )  ; 
newlinks  .  add  (  "http://exogen.case.edu/turbogears.html"  )  ; 
newlinks  .  add  (  "http://chip.cuccio.us/must-have-mac-software/"  )  ; 
newlinks  .  add  (  "http://excess.org/urwid/"  )  ; 
newlinks  .  add  (  "http://www.laedevolta.com.br/blog"  )  ; 
newlinks  .  add  (  "http://www.worldtimezone.com/"  )  ; 
newlinks  .  add  (  "http://informationr.net/ir/10-4/paper235.html"  )  ; 
newlinks  .  add  (  "http://www.cc.gatech.edu/~bader/COURSES/GATECH/CS8803-Spring2007/"  )  ; 
newlinks  .  add  (  "http://www.id-book.com/"  )  ; 
newlinks  .  add  (  "http://osiris.sunderland.ac.uk/~cs0gco/welcome.htm"  )  ; 
newlinks  .  add  (  "http://www.w3.org/2005/ajar/tab"  )  ; 
newlinks  .  add  (  "http://www.obel.com/"  )  ; 
newlinks  .  add  (  "http://www.strands.com/"  )  ; 
newlinks  .  add  (  "http://gcc.gnu.org/projects/tree-ssa/vectorization.html"  )  ; 
newlinks  .  add  (  "http://kiss.salzburgresearch.at/"  )  ; 
newlinks  .  add  (  "http://epistemicgames.org/eg/?cat=28"  )  ; 
newlinks  .  add  (  "http://jira.amqp.org/confluence/display/AMQP/Advanced+Message+Queuing+Protocol"  )  ; 
newlinks  .  add  (  "http://www.bbc.co.uk/worldservice/learningenglish/grammar/"  )  ; 
newlinks  .  add  (  "http://www.allacademic.com/meta/p_mla_apa_research_citation/1/7/3/0/0/p173006_index.html"  )  ; 
newlinks  .  add  (  "http://www.visi.com/~reuteler/leonardo.html"  )  ; 
newlinks  .  add  (  "http://www.chickipedia.com/"  )  ; 
newlinks  .  add  (  "http://www.wired.com/entertainment/music/news/2003/12/61673"  )  ; 
newlinks  .  add  (  "http://www.paradigma.com.br/gestao-do-conhecimento-na-pratica/view"  )  ; 
newlinks  .  add  (  "http://research.google.com/pubs/papers.html"  )  ; 
newlinks  .  add  (  "http://www.guuui.com/"  )  ; 
newlinks  .  add  (  "http://macspecialist.org/content/articles/essential_apps/"  )  ; 
newlinks  .  add  (  "http://www.marciokl.com/usabilidade/"  )  ; 
newlinks  .  add  (  "http://www.lukew.com/ff/entry.asp?156"  )  ; 
newlinks  .  add  (  "http://venturelab.ch/it/home.asp"  )  ; 
newlinks  .  add  (  "http://www.bash.org/"  )  ; 
newlinks  .  add  (  "https://oldwww.cs.aau.dk/index3.php?content=intern/semester/2008/Dat/4/uk_exam"  )  ; 
newlinks  .  add  (  "http://steve-yegge.blogspot.com/2007/06/rich-programmer-food.html?"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000057.php"  )  ; 
newlinks  .  add  (  "http://organicleadership.wordpress.com/category/john-maxwell/"  )  ; 
newlinks  .  add  (  "http://www.icg.org/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*"  )  ; 
newlinks  .  add  (  "http://messenger.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.testeusabilidade.com.br/"  )  ; 
newlinks  .  add  (  "http://www.uol.com.br/"  )  ; 
newlinks  .  add  (  "http://webmining.spd.louisville.edu/webkdd08/accepted.html"  )  ; 
newlinks  .  add  (  "http://sdow2008.semanticweb.org/"  )  ; 
newlinks  .  add  (  "https://www.dvdupgrades.ch/jb.html"  )  ; 
newlinks  .  add  (  "http://www.aaai.org/Resources/resources.php"  )  ; 
newlinks  .  add  (  "http://www.delphi.eti.br/"  )  ; 
newlinks  .  add  (  "http://xnet.media.mit.edu/"  )  ; 
newlinks  .  add  (  "http://www.useit.com/"  )  ; 
newlinks  .  add  (  "http://www.copypastecharacter.com/"  )  ; 
newlinks  .  add  (  "http://www.bud.ca/blog/the-great-python-component-swap-meet"  )  ; 
newlinks  .  add  (  "http://ipinfo.info/netrenderer/"  )  ; 
newlinks  .  add  (  "http://tv.truenuff.com/mac/"  )  ; 
newlinks  .  add  (  "http://www.kabalarians.com/cfm/Analyze.cfm?FirstName=aaliyah&Gender=Male"  )  ; 
newlinks  .  add  (  "http://www.afi.com/tvevents/100years/quotes.aspx#list"  )  ; 
newlinks  .  add  (  "http://www.casos.cs.cmu.edu/projects/automap/"  )  ; 
newlinks  .  add  (  "http://www.local.ch/de/Winterthur"  )  ; 
newlinks  .  add  (  "http://www.informaworld.com/smpp/1218702802-25319260/content~db=all~content=a743350973~tab=references"  )  ; 
newlinks  .  add  (  "http://www.connectbeam.com/"  )  ; 
newlinks  .  add  (  "http://facebook.com/"  )  ; 
newlinks  .  add  (  "http://www.toshiba.com/"  )  ; 
newlinks  .  add  (  "http://gvr.sourceforge.net/about.php"  )  ; 
newlinks  .  add  (  "http://en-us.fxfeeds.mozilla.com/en-US/firefox/headlines.xml"  )  ; 
newlinks  .  add  (  "http://www.pedagogiaemfoco.pro.br/met05.htm"  )  ; 
newlinks  .  add  (  "http://www.uni-koblenz.de/~peter/FDPLL/"  )  ; 
newlinks  .  add  (  "http://ocw.mit.edu/"  )  ; 
newlinks  .  add  (  "http://9rules.com/blog/2006/08/a-gradient-tutorial/"  )  ; 
newlinks  .  add  (  "http://hait-wiki.uvt.nl/"  )  ; 
newlinks  .  add  (  "http://www.gnowsis.org/About/index_html"  )  ; 
newlinks  .  add  (  "http://www.stanford.edu/group/brainsinsilicon/index.html"  )  ; 
newlinks  .  add  (  "http://www.dmi.dk/dmi/index/danmark/radar.htm"  )  ; 
newlinks  .  add  (  "http://www.vinxxgrip.ch/?view=2100&lan=en"  )  ; 
newlinks  .  add  (  "http://salzburgresearch.at/company/index.php"  )  ; 
newlinks  .  add  (  "http://corkd.com/"  )  ; 
newlinks  .  add  (  "http://www.readwriteweb.com/"  )  ; 
newlinks  .  add  (  "https://cepedia.sfbay/index.php?title=EMEA_Systems_Engineering"  )  ; 
newlinks  .  add  (  "http://acomment.net/16-top-javascriptajax-effects-for-modern-web-design/246"  )  ; 
newlinks  .  add  (  "http://www.phdcomics.com/"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Categorization"  )  ; 
newlinks  .  add  (  "http://psy.ex.ac.uk/~tpostmes/research.php"  )  ; 
newlinks  .  add  (  "http://www-dimat.unipv.it/biblio/isko/doc/folksonomies.htm"  )  ; 
newlinks  .  add  (  "http://umap09.fbk.eu/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/yroot/ymsgr8/*"  )  ; 
newlinks  .  add  (  "http://games.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://ec.europa.eu/education/programmes/mundus/projects/index_en.html"  )  ; 
newlinks  .  add  (  "http://www.faccat.com.br/dti/lp1.htm"  )  ; 
newlinks  .  add  (  "http://www.amazon.com/Software-Product-Management-Development-Execenablers/dp/1587622025/ref=pd_bbs_sr_3?ie=UTF8&s=books&qid=1195404009&sr=1-3"  )  ; 
newlinks  .  add  (  "http://www.smashingmagazine.com/2007/08/02/data-visualization-modern-approaches/"  )  ; 
newlinks  .  add  (  "http://www.antivirus.dk/"  )  ; 
newlinks  .  add  (  "http://www.asturlinux.org/"  )  ; 
newlinks  .  add  (  "http://www.telebroke.ch/flashok_d.html"  )  ; 
newlinks  .  add  (  "http://www.research.unizh.ch/p6544.htm"  )  ; 
newlinks  .  add  (  "http://code.google.com/edu/content/submissions/mapreduce/listing.html"  )  ; 
newlinks  .  add  (  "http://www.acidlabs.org/meet-stephen-collins/what-is-an-trib/"  )  ; 
newlinks  .  add  (  "http://www.lively.com/html/landing.html"  )  ; 
newlinks  .  add  (  "http://www.rejseplanen.dk/"  )  ; 
newlinks  .  add  (  "http://www.jstor.com/"  )  ; 
newlinks  .  add  (  "http://www.behaviour-driven.org/"  )  ; 
newlinks  .  add  (  "http://users.livejournal.com/_patrick_/3185471.html"  )  ; 
newlinks  .  add  (  "http://www.moscoe.org/"  )  ; 
newlinks  .  add  (  "http://www.foodnetwork.com/food/episode_archive/0,1904,FOOD_9975_213,00.html"  )  ; 
newlinks  .  add  (  "http://www.buzzvote.com/"  )  ; 
newlinks  .  add  (  "http://www.ontoclean.org/"  )  ; 
newlinks  .  add  (  "http://hp-yum.thecharacterbox.com/"  )  ; 
newlinks  .  add  (  "http://kenai.com/projects/community-equity/sources/svninternal/show"  )  ; 
newlinks  .  add  (  "http://community.daptiv.com/blogs/official_daptiv_blog/archive/2007/11/07/the-scoop-on-our-new-name.aspx"  )  ; 
newlinks  .  add  (  "http://workaround.org/pylons/beginning-pylons.html"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*"  )  ; 
newlinks  .  add  (  "http://autos.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.kde.cs.uni-kassel.de/ws/rsdc08/"  )  ; 
newlinks  .  add  (  "http://web.mit.edu/graphicidentity/glossary/index.html"  )  ; 
newlinks  .  add  (  "http://icexchange/"  )  ; 
newlinks  .  add  (  "http://www.hablarcondios.org/meditacaodiaria.asp"  )  ; 
newlinks  .  add  (  "http://www.ordnet.dk/"  )  ; 
newlinks  .  add  (  "http://www.spikesource.com/suitetwo/"  )  ; 
newlinks  .  add  (  "http://www.ted.com/index.php"  )  ; 
newlinks  .  add  (  "http://jis.sagepub.com/cgi/reprint/34/3/327"  )  ; 
newlinks  .  add  (  "http://www.djangoproject.com/"  )  ; 
newlinks  .  add  (  "http://www.newwebmag.com/2008/02/06/reuters-wants-the-world-to-be-tagged-3/"  )  ; 
newlinks  .  add  (  "http://portablevideogamer.com/"  )  ; 
newlinks  .  add  (  "http://orchestrationpatterns.com/"  )  ; 
newlinks  .  add  (  "http://wikis.sun.com/display/SocTagsMIR/Social+Tags+and+Music+Information+Retrieval;jsessionid=1D235FE70B29466987C04A2C23BC06D4"  )  ; 
newlinks  .  add  (  "http://www.gohtm.com/convert_pdf.asp"  )  ; 
newlinks  .  add  (  "http://sbooth.org/Max/"  )  ; 
newlinks  .  add  (  "http://protege.stanford.edu/"  )  ; 
newlinks  .  add  (  "http://www.nagios.org/"  )  ; 
newlinks  .  add  (  "http://www.atptennis.com/3/en/players/playerprofiles/?playernumber=G415"  )  ; 
newlinks  .  add  (  "http://del.icio.us/home';"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://my.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://alexprimo.com/category/usabilidade/"  )  ; 
newlinks  .  add  (  "http://www.game.unf.dk/"  )  ; 
newlinks  .  add  (  "http://www.c4lpt.co.uk/socialmedia/edutwitter.html"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/watch?v=sYIi-cTWOwg"  )  ; 
newlinks  .  add  (  "http://www.dlib.org/dlib/april05/hammond/04hammond.html"  )  ; 
newlinks  .  add  (  "http://www.fxpal.com/PapersAndAbstracts/papers/bor00.pdf"  )  ; 
newlinks  .  add  (  "http://www.cluetrain.com/"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/watch?v=AyzOUbkUf3M&feature=related"  )  ; 
newlinks  .  add  (  "http://ccat.sas.upenn.edu/~haroldfs/dravling/grice.html"  )  ; 
newlinks  .  add  (  "https://storm.canonical.com/"  )  ; 
newlinks  .  add  (  "http://www.supersaver.dk/"  )  ; 
newlinks  .  add  (  "https://www.google.com/accounts/ServiceLogin?service=mail&passive=true&rm=false&continue=http%3A%2F%2Fmail.google.com%2Fmail%2F%3Fui%3Dhtml%26zy%3Dl&ltmpl=ca_tlsosm_video&ltmplcache=2"  )  ; 
newlinks  .  add  (  "http://allforces.com/2006/04/14/css-signatures/"  )  ; 
newlinks  .  add  (  "http://www.cs.rochester.edu/~tetreaul/conferences.html"  )  ; 
newlinks  .  add  (  "http://www.tclscripting.com/"  )  ; 
newlinks  .  add  (  "http://www.javafree.org/content/view.jf?idContent=1"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Motivations_for_contributing_to_online_communities#Motivations_for_contributing_to_online_communities"  )  ; 
newlinks  .  add  (  "http://www.llnl.gov/computing/tutorials/pthreads/"  )  ; 
newlinks  .  add  (  "http://blogs.sfbay.sun.com/alecm/entry/what_we_need_to_do"  )  ; 
newlinks  .  add  (  "http://www.ehrensenf.de/"  )  ; 
newlinks  .  add  (  "http://www.koders.com/"  )  ; 
newlinks  .  add  (  "http://www.gvsu.edu/business/ijec/"  )  ; 
newlinks  .  add  (  "http://www.tourdepinse.dk/"  )  ; 
newlinks  .  add  (  "http://authorati.com/"  )  ; 
newlinks  .  add  (  "http://www.deathball.net/notpron/true/gototheothersite.htm"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://smallbusiness.yahoo.com/webhosting"  )  ; 
newlinks  .  add  (  "http://www.mozilla.org/source.html"  )  ; 
newlinks  .  add  (  "http://www-ccs.cs.umass.edu/"  )  ; 
newlinks  .  add  (  "http://webradioselugano.blogspot.com/"  )  ; 
newlinks  .  add  (  "http://www.digital-ecosystems.org/de/refs/ref_proj.html"  )  ; 
newlinks  .  add  (  "http://www.steria.dk/forretningsomraader/usability/"  )  ; 
newlinks  .  add  (  "http://dblp.l3s.de/?q=personalization&search_opt=all&moreAuthors=1&resTableName=query_resultJVvfGE&year_facet%5B%5D=2008&year_range=&venue_url=&resultsPerPage=100"  )  ; 
newlinks  .  add  (  "http://jude.change-vision.com/jude-web/index.html"  )  ; 
newlinks  .  add  (  "http://www.eclipse.org/epf/"  )  ; 
newlinks  .  add  (  "http://cat.inist.fr/?aModele=afficheN&cpsidt=1273704"  )  ; 
newlinks  .  add  (  "http://www.ug.it.usyd.edu.au/~soft3300/index.cgi?Home"  )  ; 
newlinks  .  add  (  "http://www.hacknot.info/hacknot/action/showEntry?eid=93"  )  ; 
newlinks  .  add  (  "http://a4esl.org/q/j/"  )  ; 
newlinks  .  add  (  "http://www.cpstartup.ch/default_eng.htm"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://bookmarks.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://esportes.terra.com.br/futebol/brasileiro/2008"  )  ; 
newlinks  .  add  (  "https://precisionconference.com/~idc/"  )  ; 
newlinks  .  add  (  "http://www.dssvanen.dk/"  )  ; 
newlinks  .  add  (  "http://www.mozilla.org/community.html"  )  ; 
newlinks  .  add  (  "http://wundt.uni-graz.at/kst.html"  )  ; 
newlinks  .  add  (  "http://www.allposters.com/gallery.asp?CID=A7E5498C4BF34A108795027EC0BFAACC&startat=http%3A//www.allposters.com/GetPoster.asp%3FAPNum%3D415532%26CID%3DA7E5498C4BF34A108795027EC0BFAACC%26PPID%3D1%26search%3Dansel%2520adams%26f%3Dc%26FindID%3D22099%26P%3D1%26PP%3D8%26sortby%3DPD%26cname%3DAnsel+Adams%26SearchID%3D"  )  ; 
newlinks  .  add  (  "http://www.tbray.org/ongoing/What/Technology/Concurrency/"  )  ; 
newlinks  .  add  (  "http://dbpedia.org/"  )  ; 
newlinks  .  add  (  "http://yihongs-research.blogspot.com/2008/01/macroscopic-regularity-over-microscopic.html"  )  ; 
newlinks  .  add  (  "http://www.wetteronline.de/deutsch.htm"  )  ; 
newlinks  .  add  (  "http://www.kaushalsheth.com/themes"  )  ; 
newlinks  .  add  (  "http://www.isls.org/cscl2009/"  )  ; 
newlinks  .  add  (  "http://go.microsoft.com/fwlink/?linkid=68920"  )  ; 
newlinks  .  add  (  "http://scoop.at/"  )  ; 
newlinks  .  add  (  "http://www.imdb.com/"  )  ; 
newlinks  .  add  (  "http://www.google.dk/"  )  ; 
newlinks  .  add  (  "http://www.inf.unisi.ch/mde4bpm08/"  )  ; 
newlinks  .  add  (  "http://politiken.dk/newsinenglish"  )  ; 
newlinks  .  add  (  "http://scratch.mit.edu/about"  )  ; 
newlinks  .  add  (  "http://wwwl.meebo.com/index-de.html"  )  ; 
newlinks  .  add  (  "http://krazydad.com/colrpickr/"  )  ; 
newlinks  .  add  (  "http://www.peerbox.com:8668/space/start/2006-09-14/1#Easy_Programming_for_Children_"  )  ; 
newlinks  .  add  (  "http://www.mayoclinic.com/health/vegetarian-diet/HQ01596/"  )  ; 
newlinks  .  add  (  "http://www.artcyclopedia.com/"  )  ; 
newlinks  .  add  (  "http://www.blogmarketingtactics.com/social-bookmarking/social-bookmarking-top-links.html"  )  ; 
newlinks  .  add  (  "http://validator.linkeddata.org/vapour"  )  ; 
newlinks  .  add  (  "http://icwe2008.webengineering.org/Program/AcceptedSubmissions/"  )  ; 
newlinks  .  add  (  "http://environment.newscientist.com/channel/earth/dn11462"  )  ; 
newlinks  .  add  (  "http://www.youtube.com/watch?v=YSsV2UfrpJ0&feature=related"  )  ; 
newlinks  .  add  (  "http://www.linhadecodigo.com/artigos.asp?id_ac=82&pag=2"  )  ; 
newlinks  .  add  (  "http://www.makelinux.net/kernel_map"  )  ; 
newlinks  .  add  (  "https://www.yellownet.ch/start_d.html"  )  ; 
newlinks  .  add  (  "http://www.msm.cam.ac.uk/phase-trans/2005/Stainless_steels/stainless.html"  )  ; 
newlinks  .  add  (  "https://olex.openlogic.com/libraries"  )  ; 
newlinks  .  add  (  "https://www.cs.aau.dk/mail/imp/login.php?Horde3=a015d343ccddde8f4ad88de853af50a0"  )  ; 
newlinks  .  add  (  "http://metaatem.net/words"  )  ; 
newlinks  .  add  (  "http://el.media.mit.edu/logo-foundation/pubs/papers/research_logo.html"  )  ; 
newlinks  .  add  (  "http://www.quickonlinetips.com/archives/2005/02/absolutely-delicious-complete-tools-collection/"  )  ; 
newlinks  .  add  (  "http://linux-net.osdl.org/index.php/Iproute2"  )  ; 
newlinks  .  add  (  "http://www.acmqueue.org/modules.php?searchterm=social+bookmarking&pa=showpage&pid=344&name=Content"  )  ; 
newlinks  .  add  (  "http://www.cs.iastate.edu/~jpathak/publications.html"  )  ; 
newlinks  .  add  (  "http://www.heise.de/"  )  ; 
newlinks  .  add  (  "http://tearain.tripod.com/hp/draw/draw.html"  )  ; 
newlinks  .  add  (  "http://www.ah2008.org/index.php?section=39"  )  ; 
newlinks  .  add  (  "http://citeseerx.ist.psu.edu/"  )  ; 
newlinks  .  add  (  "http://www.tagsistant.net/"  )  ; 
newlinks  .  add  (  "http://softwiki.de/HomePage"  )  ; 
newlinks  .  add  (  "http://lxr.mozilla.org/"  )  ; 
newlinks  .  add  (  "http://www.openrdf.org/"  )  ; 
newlinks  .  add  (  "http://etd.gsu.edu/theses/available/etd-08202007-163527/unrestricted/Hatter_Alicia_200712_ma.pdf"  )  ; 
newlinks  .  add  (  "http://blogs.sun.com/bblfish"  )  ; 
newlinks  .  add  (  "http://www.cs.aau.dk/"  )  ; 
newlinks  .  add  (  "http://en-us.www.mozilla.com/en-US/firefox/community/"  )  ; 
newlinks  .  add  (  "http://lattes.cnpq.br/index.htm"  )  ; 
newlinks  .  add  (  "http://auno.org/"  )  ; 
newlinks  .  add  (  "http://www.comp.lancs.ac.uk/~dixa/topics/formal/"  )  ; 
newlinks  .  add  (  "http://home.eng.iastate.edu/~tien/molhado/molhado_arch.html"  )  ; 
newlinks  .  add  (  "http://www.timeanddate.com/worldclock/personalapplet.html?cities=268,196,224,136,236,176"  )  ; 
newlinks  .  add  (  "http://www.scilab.org/"  )  ; 
newlinks  .  add  (  "http://www.brics.dk/jwig-mis/Censor/Censor*Search"  )  ; 
newlinks  .  add  (  "http://www.j-bradford-delong.net/movable_type/2004_archives/001272.html"  )  ; 
newlinks  .  add  (  "http://www.kongregate.com/games/"  )  ; 
newlinks  .  add  (  "http://www.theiiba.org//AM/Template.cfm?Section=Home"  )  ; 
newlinks  .  add  (  "http://aied2009.com/"  )  ; 
newlinks  .  add  (  "http://www.biglever.com/"  )  ; 
newlinks  .  add  (  "http://www.melkweg.nl/voorpagina.jsp?language=nederlands&disciplineid=muziek"  )  ; 
newlinks  .  add  (  "http://www.cs101.org/ipij/"  )  ; 
newlinks  .  add  (  "http://blog.semantic-web.at/"  )  ; 
newlinks  .  add  (  "http://www.turing.toronto.edu/"  )  ; 
newlinks  .  add  (  "http://home.stny.rr.com/jeffreypoulin/html/reucalc_basic.html"  )  ; 
newlinks  .  add  (  "http://imasters.uol.com.br/artigo/344/delphi/entendendo_e_usando_o_objeto_tframe/"  )  ; 
newlinks  .  add  (  "http://www.fsjb.edu.br/uniaracruz/cursos_graduacao/computacao/curric_cci/perio1_cci/"  )  ; 
newlinks  .  add  (  "http://aua50.aua.auc.dk/ct/CtAuc.nsf"  )  ; 
newlinks  .  add  (  "http://www.spl-book.net/"  )  ; 
newlinks  .  add  (  "http://www.orkut.com/"  )  ; 
newlinks  .  add  (  "http://flare.prefuse.org/"  )  ; 
newlinks  .  add  (  "http://www.eniro.dk/"  )  ; 
newlinks  .  add  (  "http://www.opcube.com/home.html"  )  ; 
newlinks  .  add  (  "http://lambda-the-ultimate.org/node/1733#comment-21414"  )  ; 
newlinks  .  add  (  "http://www.experthagmann.ch/100/con_liste.asp?langext=&prono=104&nCurKat=39&vSearch=&nCurPage="  )  ; 
newlinks  .  add  (  "http://startupmeme.com/tc50-edmodo-is-twitter-for-education/"  )  ; 
newlinks  .  add  (  "http://magazines.ivillage.com/redbook/diaries/v"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Conceptual_interoperability"  )  ; 
newlinks  .  add  (  "http://zesty.ca/python/scrape.py"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://entertainment.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.informatik.uni-augsburg.de/lehrstuehle/dbis/pmi/publications/all_pmi_tech-reports/tr-2006-4_hoe_khe_moe/"  )  ; 
newlinks  .  add  (  "http://www.techcrunch.com/"  )  ; 
newlinks  .  add  (  "http://www.cs.sfu.ca/people/Faculty/inkpen/publications.html"  )  ; 
newlinks  .  add  (  "http://www.hulaboys.de/"  )  ; 
newlinks  .  add  (  "http://www3.interscience.wiley.com/journal/117984068/home"  )  ; 
newlinks  .  add  (  "http://www.konsi.ch/Angebot.5.0.html"  )  ; 
newlinks  .  add  (  "http://www.somatematica.com.br/estat/basica/pagina6.php"  )  ; 
newlinks  .  add  (  "http://flickrcc.bluemountains.net/"  )  ; 
newlinks  .  add  (  "http://www.home.dk/sag/821-3151"  )  ; 
newlinks  .  add  (  "http://www.schalke04.de/"  )  ; 
newlinks  .  add  (  "http://www.quakeforge.net/"  )  ; 
newlinks  .  add  (  "http://see.stanford.edu/see/courses.aspx"  )  ; 
newlinks  .  add  (  "http://spears.socialpsychology.org/"  )  ; 
newlinks  .  add  (  "http://www.boligsiden.dk/"  )  ; 
newlinks  .  add  (  "http://web.ift.uib.no/Fysisk/Teori/KURS/WRK/TeX/symALL.html"  )  ; 
newlinks  .  add  (  "http://jsfromhell.com/pt/home"  )  ; 
newlinks  .  add  (  "http://www.sanolux.ch/d/produkteangebot/badheizkoerper_heizwand_heizkoerper.asp?navid=15"  )  ; 
newlinks  .  add  (  "http://www.jnd.org/"  )  ; 
newlinks  .  add  (  "http://www.downes.ca/news/OLDaily.htm"  )  ; 
newlinks  .  add  (  "http://si3.inf.ufrgs.br/HomePage/noticias/noti022d.cfm"  )  ; 
newlinks  .  add  (  "http://www.univie.ac.at/iwk/0809kal.html"  )  ; 
newlinks  .  add  (  "http://www.programmableweb.com/reference"  )  ; 
newlinks  .  add  (  "http://iwis.cs.aau.dk/"  )  ; 
newlinks  .  add  (  "http://oauth.net/"  )  ; 
newlinks  .  add  (  "http://www.zkb.ch/"  )  ; 
newlinks  .  add  (  "http://mikeg.typepad.com/perceptions/social_software/index.html"  )  ; 
newlinks  .  add  (  "http://www.zengestrom.com/blog/2005/04/why_some_social.html"  )  ; 
newlinks  .  add  (  "http://www.librarything.com/thingology/2007/02/when-tags-works-and-when-they-dont.php"  )  ; 
newlinks  .  add  (  "http://online-journals.org/"  )  ; 
newlinks  .  add  (  "http://go.microsoft.com/fwlink/?LinkId=50893"  )  ; 
newlinks  .  add  (  "http://www.blainekendall.com/deliciousmind/"  )  ; 
newlinks  .  add  (  "http://www.blitzbasic.com/"  )  ; 
newlinks  .  add  (  "http://openproj.org/wiki/index.php?title=POD_Documentation"  )  ; 
newlinks  .  add  (  "http://www.gslis.utexas.edu/~adillon/publications/useranalysis.html"  )  ; 
newlinks  .  add  (  "http://www.projektmanagement20.de/projektmanagement-20-ansatzte-fur-eine-definition/32/"  )  ; 
newlinks  .  add  (  "http://www-users.cs.york.ac.uk/~alistair/publications/pdf/assets96.pdf"  )  ; 
newlinks  .  add  (  "http://www.ht2009.org/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://pets.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://wallet.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://fullcoverage.yahoo.com/fc/"  )  ; 
newlinks  .  add  (  "http://www3.interscience.wiley.com/journal/119139335/abstract"  )  ; 
newlinks  .  add  (  "http://www.miniclip.com/games/en/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://messenger.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://domains.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/yroot/ymsgr8/*http://groups.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/links/ymsgr8/*http://downloads.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/yroot/ymsgr8/*http://music.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://domino.watson.ibm.com/comm/wwwr_thinkresearch.nsf/pages/20060627_dogear.html"  )  ; 
newlinks  .  add  (  "http://www.krak.dk/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://photos.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://yanyanbjd.blog.sohu.com/"  )  ; 
newlinks  .  add  (  "http://www.ai.mit.edu/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://careers.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://picasaweb.google.com/freddurao/Niver27"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://alerts.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://lucene.apache.org/solr/"  )  ; 
newlinks  .  add  (  "http://prefuse.org/"  )  ; 
newlinks  .  add  (  "http://subtitles.stargate-sg1.hu/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/yroot/ymsgr8/*http://shopping.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.pyzine.com/Issue008/Section_Articles/article_Encodings.html"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/links/ymsgr8/*http://www.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/toplevel/ymsgr8/*http://bookmarks.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/"  )  ; 
newlinks  .  add  (  "http://www.hafniafloorball.dk/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://classifieds.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.uxmatters.com/MT/archives/000209.php"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://health.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://t16web.lanl.gov/Kawano/gnuplot/index-e.html"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000179.php"  )  ; 
newlinks  .  add  (  "http://en.uclfantasy.uefa.com/"  )  ; 
newlinks  .  add  (  "http://www.gutenberg.org/wiki/Science_Fiction_"  )  ; 
newlinks  .  add  (  "http://cran.r-project.org/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://greetings.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.maersk-logistics.com/sw88.asp"  )  ; 
newlinks  .  add  (  "http://www.wethinkthebook.net/home.aspx"  )  ; 
newlinks  .  add  (  "http://pistachioconsulting.com/five-microblogging-options/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/yroot/ymsgr8/*http://travel.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://learn.creativecommons.org/"  )  ; 
newlinks  .  add  (  "http://chai.it.usyd.edu.au/"  )  ; 
newlinks  .  add  (  "http://www.exalead.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/yroot/ymsgr8/*http://smallbusiness.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.ludo.dk/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://snow.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://groups.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://messages.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.visualcomplexity.com/vc/"  )  ; 
newlinks  .  add  (  "http://www.oswd.org/"  )  ; 
newlinks  .  add  (  "http://www.seo13.com/list-of-250-social-bookmarking-websites/"  )  ; 
newlinks  .  add  (  "http://rs.resalliance.org/"  )  ; 
newlinks  .  add  (  "http://www.erickaakcire.net/delicious/pilot/pilotpaper.pdf"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/links/ymsgr8/*http://mail.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://www.hci-matters.com/blog/"  )  ; 
newlinks  .  add  (  "http://www.computer.org/tlt"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000057.php"  )  ; 
newlinks  .  add  (  "http://www.squidoo.com/introtoweb20"  )  ; 
newlinks  .  add  (  "http://en.wikipedia.org/wiki/Main_Page"  )  ; 
newlinks  .  add  (  "http://heather.cs.ucdavis.edu/~matloff/simcourse.html"  )  ; 
newlinks  .  add  (  "http://www.digital-web.com/articles/competitive_analysis_part_2/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/yroot/ymsgr8/*http://games.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://maurodelrio.com/2007/08/20/installing-osx-drivers-without-rebooting/"  )  ; 
newlinks  .  add  (  "http://jis.sagepub.com/cgi/reprint/34/3/327"  )  ; 
newlinks  .  add  (  "http://www.ebookee.com/72-05-GB-6993-eBooks-free-download-_40145.html"  )  ; 
newlinks  .  add  (  "http://del.icio.us/home';"  )  ; 
newlinks  .  add  (  "http://www.cluetrain.com/"  )  ; 
newlinks  .  add  (  "http://yihongs-research.blogspot.com/2008/01/macroscopic-regularity-over-microscopic.html"  )  ; 
newlinks  .  add  (  "http://www.peerbox.com:8668/space/start/2006-09-14/1#Easy_Programming_for_Children_(w._pics)"  )  ; 
newlinks  .  add  (  "http://www.blogmarketingtactics.com/social-bookmarking/social-bookmarking-top-links.html"  )  ; 
newlinks  .  add  (  "http://pychecker.sourceforge.net/"  )  ; 
newlinks  .  add  (  "http://del.icio.us/post?v=4;url='+encodeURIComponent(location.href)+';title='+encodeURIComponent(document.title)"  )  ; 
newlinks  .  add  (  "http://images.google.com/imgres?imgurl=http://www.dimap.ufrn.br/~jair/diuweb/DIUWeb1.gif&imgrefurl=http://www.dimap.ufrn.br/~jair/diuweb/index.html&h=220&w=359&sz=9&hl=pt-BR&start=91&sig2=_03DjmWugv8w8yyuCjnkIg&tbnid=GuaXKCgWbLfIbM:&tbnh=74&tbnw=121&ei=_6nGR5PQN432eYroySI&prev=/images%3Fq%3Dusabilidade%26start%3D90%26gbv%3D2%26ndsp%3D18%26hl%3Dpt-BR%26sa%3DN"  )  ; 
newlinks  .  add  (  "http://tinyurl.com/create.php?url='+location.href)"  )  ; 
newlinks  .  add  (  "http://www.programmableweb.com/reference"  )  ; 
newlinks  .  add  (  "http://mikeg.typepad.com/perceptions/social_software/index.html"  )  ; 
newlinks  .  add  (  "http://www.zengestrom.com/blog/2005/04/why_some_social.html"  )  ; 
newlinks  .  add  (  "http://www.librarything.com/thingology/2007/02/when-tags-works-and-when-they-dont.php"  )  ; 
newlinks  .  add  (  "http://www.blainekendall.com/deliciousmind/"  )  ; 
newlinks  .  add  (  "http://www.insanelymac.com/lofiversion/index.php/t77449.html"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://local.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://dataclustering.cse.msu.edu/"  )  ; 
newlinks  .  add  (  "http://mashable.com/2007/07/13/tagging-tools/"  )  ; 
newlinks  .  add  (  "http://wiki.osx86project.org/wiki/index.php/Hardware_.kext_Patching_List"  )  ; 
newlinks  .  add  (  "http://www.pyzine.com/Issue008/Section_Articles/article_Encodings.html"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/python-list.htm"  )  ; 
newlinks  .  add  (  "http://www.uxmatters.com/MT/archives/000209.php"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000179.php"  )  ; 
newlinks  .  add  (  "http://www.gutenberg.org/wiki/Science_Fiction_(Bookshelf)"  )  ; 
newlinks  .  add  (  "http://grafikdesign.wordpress.com/2008/02/23/how-do-i-install-a-kext-file/"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000057.php"  )  ; 
newlinks  .  add  (  "http://www.squidoo.com/introtoweb20"  )  ; 
newlinks  .  add  (  "http://www.digital-web.com/articles/competitive_analysis_part_2/"  )  ; 
newlinks  .  add  (  "http://maurodelrio.com/2007/08/20/installing-osx-drivers-without-rebooting/"  )  ; 
newlinks  .  add  (  "http://us.rd.yahoo.com/customize/favorites/ymsgr8/*http://autos.yahoo.com/"  )  ; 
newlinks  .  add  (  "http://jis.sagepub.com/cgi/reprint/34/3/327"  )  ; 
newlinks  .  add  (  "http://www.ebookee.com/72-05-GB-6993-eBooks-free-download-_40145.html"  )  ; 
newlinks  .  add  (  "http://del.icio.us/home"  )  ; 
newlinks  .  add  (  "http://www.cluetrain.com/"  )  ; 
newlinks  .  add  (  "http://yihongs-research.blogspot.com/2008/01/macroscopic-regularity-over-microscopic.html"  )  ; 
newlinks  .  add  (  "http://www.blogmarketingtactics.com/social-bookmarking/social-bookmarking-top-links.html"  )  ; 
newlinks  .  add  (  "http://del.icio.us/post?v=4;url='+encodeURIComponent(location.href)+';title='+encodeURIComponent(document.title)"  )  ; 
newlinks  .  add  (  "http://tinyurl.com/create.php?url='+location.href)"  )  ; 
newlinks  .  add  (  "http://www.programmableweb.com/reference"  )  ; 
newlinks  .  add  (  "http://mikeg.typepad.com/perceptions/social_software/index.html"  )  ; 
newlinks  .  add  (  "http://www.zengestrom.com/blog/2005/04/why_some_social.html"  )  ; 
newlinks  .  add  (  "http://www.librarything.com/thingology/2007/02/when-tags-works-and-when-they-dont.php"  )  ; 
newlinks  .  add  (  "http://www.blainekendall.com/deliciousmind/"  )  ; 
newlinks  .  add  (  "http://www.insanelymac.com/lofiversion/index.php/t77449.html"  )  ; 
newlinks  .  add  (  "http://dataclustering.cse.msu.edu/"  )  ; 
newlinks  .  add  (  "http://mashable.com/2007/07/13/tagging-tools/"  )  ; 
newlinks  .  add  (  "http://wiki.osx86project.org/wiki/index.php/Hardware_.kext_Patching_List"  )  ; 
newlinks  .  add  (  "http://www.pyzine.com/Issue008/Section_Articles/article_Encodings.html"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/python-list.htm"  )  ; 
newlinks  .  add  (  "http://www.uxmatters.com/MT/archives/000209.php"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000179.php"  )  ; 
newlinks  .  add  (  "http://www.wethinkthebook.net/home.aspx"  )  ; 
newlinks  .  add  (  "http://www.seo13.com/list-of-250-social-bookmarking-websites/"  )  ; 
newlinks  .  add  (  "http://grafikdesign.wordpress.com/2008/02/23/how-do-i-install-a-kext-file/"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000057.php"  )  ; 
newlinks  .  add  (  "http://www.squidoo.com/introtoweb20"  )  ; 
newlinks  .  add  (  "http://www.digital-web.com/articles/competitive_analysis_part_2/"  )  ; 
newlinks  .  add  (  "http://maurodelrio.com/2007/08/20/installing-osx-drivers-without-rebooting/"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.seo13.com/list-of-250-social-bookmarking-websites/"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.ebookee.com/72-05-GB-6993-eBooks-free-download-_40145.html"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://dataclustering.cse.msu.edu/"  )  ; 
newlinks  .  add  (  "javascript:location.href='http://del.icio.us/home';"  )  ; 
newlinks  .  add  (  "javascript:location.href='http://del.icio.us/post?v=4;url='+encodeURIComponent(location.href)+';title='+encodeURIComponent(document.title)"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url="  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.pyzine.com/Issue008/Section_Articles/article_Encodings.html"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.insanelymac.com/lofiversion/index.php/t77449.html"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.librarything.com/thingology/2007/02/when-tags-works-and-when-they-dont.php"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.wethinkthebook.net/home.aspx"  )  ; 
newlinks  .  add  (  "javascript:void(location.href='http://tinyurl.com/create.php?url='+location.href)"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.uxmatters.com/MT/archives/000209.php"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://semanticstudios.com/publications/semantics/000179.php"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://yihongs-research.blogspot.com/2008/01/macroscopic-regularity-over-microscopic.html"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.programmableweb.com/reference"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://maurodelrio.com/2007/08/20/installing-osx-drivers-without-rebooting/"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.blogmarketingtactics.com/social-bookmarking/social-bookmarking-top-links.html"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://jis.sagepub.com/cgi/reprint/34/3/327"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://wiki.osx86project.org/wiki/index.php/Hardware_.kext_Patching_List"  )  ; 
newlinks  .  add  (  "javascript:u=location.href;p=/https/;httpvar=(u.search(p)!=-1)?'https':'http';s=document.body.appendChild(document.createElement('script'));s.id='fs';s.language='javascript';void(s.src=httpvar+'://www.blinklist.com/Theme/Script/fav.js');"  )  ; 
newlinks  .  add  (  "place:sort=8&maxResults=10"  )  ; 
newlinks  .  add  (  "place:folder=BOOKMARKS_MENU&folder=UNFILED_BOOKMARKS&folder=TOOLBAR&sort=12&excludeQueries=1&excludeItemIfParentHasAnnotation=livemark%2FfeedURI&maxResults=10&queryType=1"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://effbot.org/zone/python-list.htm"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://mashable.com/2007/07/13/tagging-tools/"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://semanticstudios.com/publications/semantics/000057.php"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.digital-web.com/articles/competitive_analysis_part_2/"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.squidoo.com/introtoweb20"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.cluetrain.com/"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.blainekendall.com/deliciousmind/"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://www.zengestrom.com/blog/2005/04/why_some_social.html"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://grafikdesign.wordpress.com/2008/02/23/how-do-i-install-a-kext-file/"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://mikeg.typepad.com/perceptions/social_software/index.html"  )  ; 
newlinks  .  add  (  "chrome://ietab/content/reloaded.html?url=http://effbot.org/zone/"  )  ; 
newlinks  .  add  (  "place:sort=14&type=6&maxResults=10"  )  ; 
newlinks  .  add  (  "http://www.seo13.com/list-of-250-social-bookmarking-websites/"  )  ; 
newlinks  .  add  (  "http://www.ebookee.com/72-05-GB-6993-eBooks-free-download-_40145.html"  )  ; 
newlinks  .  add  (  "http://dataclustering.cse.msu.edu/"  )  ; 
newlinks  .  add  (  "http://del.icio.us/home';"  )  ; 
newlinks  .  add  (  "http://del.icio.us/post?v=4;url='+encodeURIComponent(location.href)+';title='+encodeURIComponent(document.title)"  )  ; 
newlinks  .  add  (  "http://www.pyzine.com/Issue008/Section_Articles/article_Encodings.html"  )  ; 
newlinks  .  add  (  "http://www.insanelymac.com/lofiversion/index.php/t77449.html"  )  ; 
newlinks  .  add  (  "http://www.librarything.com/thingology/2007/02/when-tags-works-and-when-they-dont.php"  )  ; 
newlinks  .  add  (  "http://www.wethinkthebook.net/home.aspx"  )  ; 
newlinks  .  add  (  "http://tinyurl.com/create.php?url='+location.href)"  )  ; 
newlinks  .  add  (  "http://www.uxmatters.com/MT/archives/000209.php"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000179.php"  )  ; 
newlinks  .  add  (  "http://yihongs-research.blogspot.com/2008/01/macroscopic-regularity-over-microscopic.html"  )  ; 
newlinks  .  add  (  "http://www.programmableweb.com/reference"  )  ; 
newlinks  .  add  (  "http://maurodelrio.com/2007/08/20/installing-osx-drivers-without-rebooting/"  )  ; 
newlinks  .  add  (  "http://www.blogmarketingtactics.com/social-bookmarking/social-bookmarking-top-links.html"  )  ; 
newlinks  .  add  (  "http://jis.sagepub.com/cgi/reprint/34/3/327"  )  ; 
newlinks  .  add  (  "http://wiki.osx86project.org/wiki/index.php/Hardware_.kext_Patching_List"  )  ; 
newlinks  .  add  (  "https/;httpvar=(u.search(p)!=-1)?'https':'http';s=document.body.appendChild(document.createElement('script'));s.id='fs';s.language='javascript';void(s.src=httpvar+'://www.blinklist.com/Theme/Script/fav.js');"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/python-list.htm"  )  ; 
newlinks  .  add  (  "http://mashable.com/2007/07/13/tagging-tools/"  )  ; 
newlinks  .  add  (  "http://semanticstudios.com/publications/semantics/000057.php"  )  ; 
newlinks  .  add  (  "http://www.digital-web.com/articles/competitive_analysis_part_2/"  )  ; 
newlinks  .  add  (  "http://www.squidoo.com/introtoweb20"  )  ; 
newlinks  .  add  (  "http://www.cluetrain.com/"  )  ; 
newlinks  .  add  (  "http://www.blainekendall.com/deliciousmind/"  )  ; 
newlinks  .  add  (  "http://www.zengestrom.com/blog/2005/04/why_some_social.html"  )  ; 
newlinks  .  add  (  "http://grafikdesign.wordpress.com/2008/02/23/how-do-i-install-a-kext-file/"  )  ; 
newlinks  .  add  (  "http://mikeg.typepad.com/perceptions/social_software/index.html"  )  ; 
newlinks  .  add  (  "http://effbot.org/zone/"  )  ; 
return   newlinks  ; 
} 
} 

