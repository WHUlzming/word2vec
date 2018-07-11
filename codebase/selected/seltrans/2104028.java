package   org  .  pustefixframework  .  maven  .  plugins  .  eclipse  ; 

import   java  .  io  .  File  ; 
import   java  .  io  .  IOException  ; 
import   java  .  util  .  ArrayList  ; 
import   java  .  util  .  List  ; 
import   org  .  apache  .  maven  .  model  .  Resource  ; 
import   org  .  apache  .  maven  .  plugin  .  AbstractMojo  ; 
import   org  .  apache  .  maven  .  plugin  .  MojoExecutionException  ; 
import   org  .  apache  .  maven  .  plugin  .  MojoFailureException  ; 
import   org  .  apache  .  maven  .  project  .  MavenProject  ; 
import   org  .  pustefixframework  .  util  .  io  .  FileUtils  ; 













public   class   CopyPostProcessResourceMojo   extends   AbstractMojo  { 


protected   MavenProject   mavenProject  ; 




private   File   target  ; 




private   File   source  ; 

public   void   execute  (  )  throws   MojoExecutionException  ,  MojoFailureException  { 
if  (  source  .  exists  (  )  )  { 
File   parent  =  target  .  getParentFile  (  )  ; 
if  (  parent  !=  null  &&  !  parent  .  exists  (  )  )  parent  .  mkdirs  (  )  ; 
try  { 
FileUtils  .  copyFile  (  source  ,  target  )  ; 
}  catch  (  IOException   e  )  { 
throw   new   MojoExecutionException  (  "Error copying resource file"  ,  e  )  ; 
} 
} 
Resource   resource  =  new   Resource  (  )  ; 
resource  .  setDirectory  (  target  .  getParentFile  (  )  .  getAbsolutePath  (  )  )  ; 
List  <  String  >  includes  =  new   ArrayList  <  String  >  (  )  ; 
includes  .  add  (  target  .  getName  (  )  )  ; 
resource  .  setIncludes  (  includes  )  ; 
mavenProject  .  addResource  (  resource  )  ; 
} 
} 

