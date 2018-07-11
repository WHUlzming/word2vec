package   net  .  sourceforge  .  pebble  .  dao  .  file  ; 

import   net  .  sourceforge  .  pebble  .  dao  .  BlogEntryDAO  ; 
import   net  .  sourceforge  .  pebble  .  domain  .  *  ; 
import   net  .  sourceforge  .  pebble  .  util  .  FileUtils  ; 
import   java  .  io  .  File  ; 
import   java  .  text  .  SimpleDateFormat  ; 
import   java  .  util  .  List  ; 
import   java  .  util  .  Locale  ; 






public   class   FileBlogEntryDAOTest   extends   SingleBlogTestCase  { 

private   BlogEntryDAO   dao  =  new   FileBlogEntryDAO  (  )  ; 

private   Locale   defaultLocale  ; 

protected   void   setUp  (  )  throws   Exception  { 
super  .  setUp  (  )  ; 
defaultLocale  =  Locale  .  getDefault  (  )  ; 
Locale  .  setDefault  (  Locale  .  ENGLISH  )  ; 
} 

public   void   tearDown  (  )  throws   Exception  { 
super  .  tearDown  (  )  ; 
Locale  .  setDefault  (  defaultLocale  )  ; 
} 

public   void   testLoadBlogEntryFomFile  (  )  throws   Exception  { 
File   source  =  new   File  (  "test"  ,  "1081203335000.xml"  )  ; 
File   destination  =  new   File  (  blog  .  getRoot  (  )  ,  "2004/04/05/"  )  ; 
destination  .  mkdirs  (  )  ; 
FileUtils  .  copyFile  (  source  ,  new   File  (  destination  ,  "1081203335000.xml"  )  )  ; 
Day   day  =  blog  .  getBlogForDay  (  2004  ,  04  ,  05  )  ; 
Category   category1  =  new   Category  (  "/category1"  ,  "Category 1"  )  ; 
blog  .  addCategory  (  category1  )  ; 
Category   category2  =  new   Category  (  "/category2"  ,  "Category 2"  )  ; 
blog  .  addCategory  (  category2  )  ; 
BlogEntry   blogEntry  =  dao  .  loadBlogEntry  (  blog  ,  "1081203335000"  )  ; 
SimpleDateFormat   sdf  =  new   SimpleDateFormat  (  FileBlogEntryDAO  .  NEW_PERSISTENT_DATETIME_FORMAT  )  ; 
assertEquals  (  "Blog entry title"  ,  blogEntry  .  getTitle  (  )  )  ; 
assertEquals  (  "Blog entry subtitle"  ,  blogEntry  .  getSubtitle  (  )  )  ; 
assertEquals  (  "<p>Blog entry excerpt.</p>"  ,  blogEntry  .  getExcerpt  (  )  )  ; 
assertEquals  (  "<p>Blog entry body.</p>"  ,  blogEntry  .  getBody  (  )  )  ; 
assertEquals  (  1081203335000L  ,  blogEntry  .  getDate  (  )  .  getTime  (  )  )  ; 
assertEquals  (  "Europe/Paris"  ,  blogEntry  .  getTimeZoneId  (  )  )  ; 
assertTrue  (  blogEntry  .  isUnpublished  (  )  )  ; 
assertEquals  (  "simon"  ,  blogEntry  .  getAuthor  (  )  )  ; 
assertTrue  (  blogEntry  .  isCommentsEnabled  (  )  )  ; 
assertTrue  (  blogEntry  .  isTrackBacksEnabled  (  )  )  ; 
assertEquals  (  2  ,  blogEntry  .  getCategories  (  )  .  size  (  )  )  ; 
assertTrue  (  blogEntry  .  getCategories  (  )  .  contains  (  category1  )  )  ; 
assertTrue  (  blogEntry  .  getCategories  (  )  .  contains  (  category2  )  )  ; 
assertEquals  (  "sometag"  ,  blogEntry  .  getTags  (  )  )  ; 
Attachment   attachment  =  blogEntry  .  getAttachment  (  )  ; 
assertNotNull  (  attachment  )  ; 
assertEquals  (  "./files/java-development-on-mac-os-x.pdf"  ,  attachment  .  getUrl  (  )  )  ; 
assertEquals  (  3443670  ,  attachment  .  getSize  (  )  )  ; 
assertEquals  (  "application/pdf"  ,  attachment  .  getType  (  )  )  ; 
List   comments  =  blogEntry  .  getComments  (  )  ; 
assertEquals  (  2  ,  comments  .  size  (  )  )  ; 
Comment   comment1  =  (  Comment  )  comments  .  get  (  0  )  ; 
assertEquals  (  "Comment title 1"  ,  comment1  .  getTitle  (  )  )  ; 
assertEquals  (  "<p>Comment 1.</p>"  ,  comment1  .  getBody  (  )  )  ; 
assertEquals  (  "Comment author 1"  ,  comment1  .  getAuthor  (  )  )  ; 
assertEquals  (  "me@author1.com"  ,  comment1  .  getEmail  (  )  )  ; 
assertEquals  (  "http://www.author1.com"  ,  comment1  .  getWebsite  (  )  )  ; 
assertEquals  (  "127.0.0.1"  ,  comment1  .  getIpAddress  (  )  )  ; 
assertTrue  (  comment1  .  isApproved  (  )  )  ; 
assertEquals  (  sdf  .  parse  (  "05 Apr 2004 23:27:30:0 +0100"  )  ,  comment1  .  getDate  (  )  )  ; 
assertFalse  (  comment1  .  isAuthenticated  (  )  )  ; 
Comment   comment2  =  (  Comment  )  comments  .  get  (  1  )  ; 
assertEquals  (  "Re: "  +  blogEntry  .  getTitle  (  )  ,  comment2  .  getTitle  (  )  )  ; 
assertEquals  (  "<p>Comment 2.</p>"  ,  comment2  .  getBody  (  )  )  ; 
assertEquals  (  "Comment author 2"  ,  comment2  .  getAuthor  (  )  )  ; 
assertEquals  (  "me@author2.com"  ,  comment2  .  getEmail  (  )  )  ; 
assertEquals  (  "http://www.author2.com"  ,  comment2  .  getWebsite  (  )  )  ; 
assertEquals  (  "192.168.0.1"  ,  comment2  .  getIpAddress  (  )  )  ; 
assertTrue  (  comment2  .  isPending  (  )  )  ; 
assertEquals  (  sdf  .  parse  (  "05 Apr 2004 23:31:00:0 +0100"  )  ,  comment2  .  getDate  (  )  )  ; 
assertTrue  (  comment2  .  isAuthenticated  (  )  )  ; 
List   trackBacks  =  blogEntry  .  getTrackBacks  (  )  ; 
assertEquals  (  2  ,  trackBacks  .  size  (  )  )  ; 
TrackBack   trackBack1  =  (  TrackBack  )  trackBacks  .  get  (  0  )  ; 
assertEquals  (  "TrackBack title 1"  ,  trackBack1  .  getTitle  (  )  )  ; 
assertEquals  (  "TrackBack body 1."  ,  trackBack1  .  getExcerpt  (  )  )  ; 
assertEquals  (  "http://www.author1.com/entry"  ,  trackBack1  .  getUrl  (  )  )  ; 
assertEquals  (  "Blog name 1"  ,  trackBack1  .  getBlogName  (  )  )  ; 
assertEquals  (  "127.0.0.1"  ,  trackBack1  .  getIpAddress  (  )  )  ; 
assertTrue  (  trackBack1  .  isApproved  (  )  )  ; 
assertEquals  (  sdf  .  parse  (  "06 Apr 2004 07:09:24:0 +0100"  )  ,  trackBack1  .  getDate  (  )  )  ; 
TrackBack   trackBack2  =  (  TrackBack  )  trackBacks  .  get  (  1  )  ; 
assertEquals  (  "TrackBack title 2"  ,  trackBack2  .  getTitle  (  )  )  ; 
assertEquals  (  "TrackBack body 2."  ,  trackBack2  .  getExcerpt  (  )  )  ; 
assertEquals  (  "http://www.author2.com/entry"  ,  trackBack2  .  getUrl  (  )  )  ; 
assertEquals  (  "Blog name 2"  ,  trackBack2  .  getBlogName  (  )  )  ; 
assertEquals  (  "192.168.0.1"  ,  trackBack2  .  getIpAddress  (  )  )  ; 
assertTrue  (  trackBack2  .  isPending  (  )  )  ; 
assertEquals  (  sdf  .  parse  (  "06 Apr 2004 07:09:24:0 +0100"  )  ,  trackBack2  .  getDate  (  )  )  ; 
} 
} 

