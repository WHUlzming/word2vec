package   tudresden  .  ocl20  .  core  .  parser  ; 

import   tudresden  .  ocl20  .  core  .  parser  .  util  .  *  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  sablecc  .  analysis  .  *  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  sablecc  .  lexer  .  Lexer  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  sablecc  .  lexer  .  LexerException  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  sablecc  .  parser  .  Parser  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  sablecc  .  parser  .  ParserException  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  sablecc  .  node  .  *  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  util  .  TextualCSTBuilder  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  util  .  GraphicalCSTBuilder  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  astgen  .  LAttrAstGenerator  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  astgen  .  Heritage  ; 
import   tudresden  .  ocl20  .  core  .  parser  .  astgen  .  NodeFactory  ; 
import   tudresden  .  ocl20  .  core  .  *  ; 
import   tudresden  .  ocl20  .  core  .  MetaModelConst  ; 
import   tudresden  .  ocl20  .  core  .  ModelManager  ; 
import   tudresden  .  ocl20  .  core  .  OclModel  ; 
import   tudresden  .  ocl20  .  core  .  OclModelException  ; 
import   tudresden  .  ocl20  .  core  .  jmi  .  ocl  .  *  ; 
import   tudresden  .  ocl20  .  core  .  jmi  .  ocl  .  types  .  *  ; 
import   tudresden  .  ocl20  .  core  .  jmi  .  ocl  .  expressions  .  *  ; 
import   tudresden  .  ocl20  .  core  .  jmi  .  ocl  .  commonmodel  .  *  ; 
import   javax  .  jmi  .  reflect  .  *  ; 
import   org  .  jgraph  .  layout  .  JGraphLayoutAlgorithm  ; 
import   org  .  jgraph  .  layout  .  TreeLayoutAlgorithm  ; 
import   org  .  jgraph  .  graph  .  GraphModel  ; 
import   org  .  jgraph  .  graph  .  DefaultGraphModel  ; 
import   org  .  jgraph  .  graph  .  DefaultGraphCell  ; 
import   org  .  jgraph  .  graph  .  AttributeMap  ; 
import   org  .  jgraph  .  graph  .  ConnectionSet  ; 
import   org  .  jgraph  .  graph  .  CellView  ; 
import   org  .  jgraph  .  graph  .  AbstractCellView  ; 
import   org  .  jgraph  .  JGraph  ; 
import   java  .  awt  .  Color  ; 
import   javax  .  swing  .  tree  .  DefaultMutableTreeNode  ; 
import   javax  .  swing  .  tree  .  DefaultTreeModel  ; 
import   javax  .  swing  .  SwingConstants  ; 
import   java  .  awt  .  geom  .  Rectangle2D  ; 
import   java  .  awt  .  Dimension  ; 
import   java  .  io  .  *  ; 
import   java  .  util  .  *  ; 
import   java  .  util  .  zip  .  *  ; 







public   class   OCL20GUI   extends   javax  .  swing  .  JFrame   implements   SimpleMessageSink  { 

private   String   mOclFilename  =  null  ; 

private   void   setOclFilename  (  String   filename  )  { 
mOclFilename  =  filename  ; 
setTitle  (  "Experimental OCL20 Parser GUI ["  +  mOclFilename  +  "]"  )  ; 
} 


private   Start   cst  =  null  ; 

private   OclModel   oclModel  =  null  ; 

private   File   mOclConstraintDirectory  =  null  ; 

private   File   mModelDirectory  =  null  ; 

private   void   setOclModel  (  OclModel   model  ,  String   modelXmiPath  )  { 
this  .  oclModel  =  model  ; 
String   modelName  =  model  .  getName  (  )  ; 
this  .  mTxfModelFile  .  setText  (  modelXmiPath  )  ; 
this  .  mTxfModelName  .  setText  (  model  .  getName  (  )  )  ; 
} 

private   OclModel   getOclModel  (  )  { 
return   this  .  oclModel  ; 
} 

private   void   setCSTViewRoot  (  DefaultMutableTreeNode   node  )  { 
mCSTView  .  setModel  (  new   DefaultTreeModel  (  node  )  )  ; 
} 


public   OCL20GUI  (  )  { 
initMembers  (  )  ; 
initComponents  (  )  ; 
initCustomComponents  (  )  ; 
setOclFilename  (  "<no file>"  )  ; 
} 

public   void   processMessage  (  String   msg  )  { 
this  .  mASTLogTextArea  .  append  (  msg  +  "\n"  )  ; 
} 

private   void   initMembers  (  )  { 
try  { 
java  .  net  .  URL   url  =  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "constraints"  )  ; 
assert  (  url  !=  null  )  :  "cannot obtain url for constraints directory, check class path"  ; 
java  .  net  .  URI   uri  =  new   java  .  net  .  URI  (  url  .  toExternalForm  (  )  +  "/"  )  ; 
this  .  mOclConstraintDirectory  =  new   java  .  io  .  File  (  uri  )  ; 
System  .  out  .  println  (  "Constraints directory (File): "  +  this  .  mOclConstraintDirectory  .  toString  (  )  )  ; 
}  catch  (  java  .  net  .  URISyntaxException   ue  )  { 
ue  .  printStackTrace  (  )  ; 
throw   new   RuntimeException  (  "Cannot locate ocl constraint file directory (\"constraints\")."  ,  ue  )  ; 
} 
try  { 
java  .  net  .  URL   url  =  ClassLoader  .  getSystemClassLoader  (  )  .  getResource  (  "PoseidonProjects"  )  ; 
java  .  net  .  URI   uri  =  new   java  .  net  .  URI  (  url  .  toExternalForm  (  )  +  "/"  )  ; 
this  .  mModelDirectory  =  new   File  (  uri  )  ; 
}  catch  (  java  .  net  .  URISyntaxException   ue  )  { 
ue  .  printStackTrace  (  )  ; 
throw   new   RuntimeException  (  "Cannot locate poseidon project directory."  ,  ue  )  ; 
} 
} 

private   void   initCustomComponents  (  )  { 
} 






private   void   initComponents  (  )  { 
java  .  awt  .  GridBagConstraints   gridBagConstraints  ; 
mConstraintFileChooser  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
mMessageDialog  =  new   javax  .  swing  .  JDialog  (  )  ; 
mButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mCloseButton  =  new   javax  .  swing  .  JButton  (  )  ; 
mMessageScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
mMessage  =  new   javax  .  swing  .  JTextArea  (  )  ; 
mModelFileChooser  =  new   javax  .  swing  .  JFileChooser  (  )  ; 
mTabPane  =  new   javax  .  swing  .  JTabbedPane  (  )  ; 
mConstraintPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mConstraintButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mButtonLoadConstraint  =  new   javax  .  swing  .  JButton  (  )  ; 
mButtonSaveConstraint  =  new   javax  .  swing  .  JButton  (  )  ; 
mConstraintEditorPositionLabel  =  new   javax  .  swing  .  JLabel  (  )  ; 
mConstraintScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
mConstraintTextArea  =  new   javax  .  swing  .  JTextArea  (  )  ; 
mCSTViewPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mCSTButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mButtonParse  =  new   javax  .  swing  .  JButton  (  )  ; 
mButtonExpandAll  =  new   javax  .  swing  .  JButton  (  )  ; 
mButtonVisualize  =  new   javax  .  swing  .  JButton  (  )  ; 
mCSTViewScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
mCSTView  =  new   javax  .  swing  .  JTree  (  )  ; 
mCSTView  .  setModel  (  null  )  ; 
mCSTVisualizerPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mCSTVizScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
mGraphCSTViz  =  new   org  .  jgraph  .  JGraph  (  )  ; 
mGraphCSTViz  .  setModel  (  new   DefaultGraphModel  (  )  )  ; 
mGraphCSTViz  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  2147483647  ,  2147483647  )  )  ; 
mGraphCSTViz  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  240  ,  160  )  )  ; 
mGraphCSTViz  .  setAntiAliased  (  true  )  ; 
mGraphCSTViz  .  setAlignmentX  (  javax  .  swing  .  JComponent  .  CENTER_ALIGNMENT  )  ; 
mGraphCSTViz  .  setAlignmentY  (  javax  .  swing  .  JComponent  .  CENTER_ALIGNMENT  )  ; 
mGraphCSTViz  .  setBorder  (  javax  .  swing  .  BorderFactory  .  createLineBorder  (  Color  .  LIGHT_GRAY  ,  5  )  )  ; 
mGraphCSTViz  .  setEditable  (  false  )  ; 
mGraphCSTViz  .  setBendable  (  false  )  ; 
mGraphCSTViz  .  setConnectable  (  false  )  ; 
mGraphCSTViz  .  setDisconnectable  (  false  )  ; 
mRepoPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mRepoButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mButtonListModels  =  new   javax  .  swing  .  JButton  (  )  ; 
mButtonDeleteModel  =  new   javax  .  swing  .  JButton  (  )  ; 
mRepoInfoPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mLblMetaModel1  =  new   javax  .  swing  .  JLabel  (  )  ; 
mCbxMetamodel1  =  new   javax  .  swing  .  JComboBox  (  )  ; 
mRepoContentScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
mRepoContentView  =  new   javax  .  swing  .  JTree  (  )  ; 
mRepoContentView  .  setModel  (  null  )  ; 
mModelPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mModelButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mButtonLoadModel  =  new   javax  .  swing  .  JButton  (  )  ; 
mButtonReserve1  =  new   javax  .  swing  .  JButton  (  )  ; 
mButtonReserve2  =  new   javax  .  swing  .  JButton  (  )  ; 
mModelInfoPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mLblMetaModel  =  new   javax  .  swing  .  JLabel  (  )  ; 
mCbxMetamodel  =  new   javax  .  swing  .  JComboBox  (  )  ; 
mLblModelFile  =  new   javax  .  swing  .  JLabel  (  )  ; 
mTxfModelFile  =  new   javax  .  swing  .  JTextField  (  )  ; 
mLblModelName  =  new   javax  .  swing  .  JLabel  (  )  ; 
mTxfModelName  =  new   javax  .  swing  .  JTextField  (  )  ; 
mPanelRemainder  =  new   javax  .  swing  .  JPanel  (  )  ; 
mMdlLogScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
mMdlLogTextArea  =  new   javax  .  swing  .  JTextArea  (  )  ; 
mASTViewPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mASTLogScrollPane  =  new   javax  .  swing  .  JScrollPane  (  )  ; 
mASTLogTextArea  =  new   javax  .  swing  .  JTextArea  (  )  ; 
mASTViewButtonPanel  =  new   javax  .  swing  .  JPanel  (  )  ; 
mBtnASTGenerate  =  new   javax  .  swing  .  JButton  (  )  ; 
mBtnASTLogClear  =  new   javax  .  swing  .  JButton  (  )  ; 
mBtnExportModel  =  new   javax  .  swing  .  JButton  (  )  ; 
mConstraintFileChooser  .  setApproveButtonText  (  "Load"  )  ; 
mConstraintFileChooser  .  setCurrentDirectory  (  this  .  mOclConstraintDirectory  )  ; 
mConstraintFileChooser  .  setFileHidingEnabled  (  false  )  ; 
mConstraintFileChooser  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mConstraintFileChooserActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mMessageDialog  .  setTitle  (  "Attention"  )  ; 
mCloseButton  .  setText  (  "Close"  )  ; 
mCloseButton  .  setActionCommand  (  "CLOSE"  )  ; 
mCloseButton  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mCloseButtonActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mButtonPanel  .  add  (  mCloseButton  )  ; 
mMessageDialog  .  getContentPane  (  )  .  add  (  mButtonPanel  ,  java  .  awt  .  BorderLayout  .  SOUTH  )  ; 
mMessageScrollPane  .  setViewportView  (  mMessage  )  ; 
mMessageDialog  .  getContentPane  (  )  .  add  (  mMessageScrollPane  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
mModelFileChooser  .  setApproveButtonText  (  "Load"  )  ; 
mModelFileChooser  .  setCurrentDirectory  (  this  .  mModelDirectory  )  ; 
mModelFileChooser  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mModelFileChooserActionPerformed  (  evt  )  ; 
} 
}  )  ; 
setTitle  (  "Experimental OCL20 Parser GUI"  )  ; 
setName  (  "OCLGuiFrame"  )  ; 
addWindowListener  (  new   java  .  awt  .  event  .  WindowAdapter  (  )  { 

public   void   windowClosing  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
exitForm  (  evt  )  ; 
} 
}  )  ; 
mTabPane  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  320  ,  240  )  )  ; 
mTabPane  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  620  ,  440  )  )  ; 
mConstraintPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
mConstraintButtonPanel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  140  ,  200  )  )  ; 
mConstraintButtonPanel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  140  ,  200  )  )  ; 
mButtonLoadConstraint  .  setText  (  "Load from file"  )  ; 
mButtonLoadConstraint  .  setActionCommand  (  "CONSTRAINT_LOAD"  )  ; 
mButtonLoadConstraint  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  125  ,  25  )  )  ; 
mButtonLoadConstraint  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  125  ,  25  )  )  ; 
mButtonLoadConstraint  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  125  ,  25  )  )  ; 
mButtonLoadConstraint  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
onActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mConstraintButtonPanel  .  add  (  mButtonLoadConstraint  )  ; 
mButtonSaveConstraint  .  setText  (  "Save to file"  )  ; 
mButtonSaveConstraint  .  setActionCommand  (  "CONSTRAINT_SAVE"  )  ; 
mButtonSaveConstraint  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  125  ,  25  )  )  ; 
mButtonSaveConstraint  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
onActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mConstraintButtonPanel  .  add  (  mButtonSaveConstraint  )  ; 
mConstraintEditorPositionLabel  .  setText  (  "Line x, Column y"  )  ; 
mConstraintEditorPositionLabel  .  setToolTipText  (  "Lines and columns are counted from 1."  )  ; 
mConstraintButtonPanel  .  add  (  mConstraintEditorPositionLabel  )  ; 
mConstraintPanel  .  add  (  mConstraintButtonPanel  ,  java  .  awt  .  BorderLayout  .  EAST  )  ; 
mConstraintTextArea  .  setFont  (  new   java  .  awt  .  Font  (  "Monospaced"  ,  0  ,  12  )  )  ; 
mConstraintTextArea  .  setMinimumSize  (  null  )  ; 
mConstraintTextArea  .  setPreferredSize  (  null  )  ; 
mConstraintTextArea  .  addCaretListener  (  new   javax  .  swing  .  event  .  CaretListener  (  )  { 

public   void   caretUpdate  (  javax  .  swing  .  event  .  CaretEvent   evt  )  { 
mConstraintTextAreaCaretUpdate  (  evt  )  ; 
} 
}  )  ; 
mConstraintScrollPane  .  setViewportView  (  mConstraintTextArea  )  ; 
mConstraintPanel  .  add  (  mConstraintScrollPane  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
mTabPane  .  addTab  (  "Constraint"  ,  mConstraintPanel  )  ; 
mCSTViewPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
mCSTViewPanel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  320  ,  220  )  )  ; 
mCSTButtonPanel  .  setBorder  (  new   javax  .  swing  .  border  .  EtchedBorder  (  javax  .  swing  .  border  .  EtchedBorder  .  RAISED  )  )  ; 
mCSTButtonPanel  .  setAlignmentX  (  1.0F  )  ; 
mCSTButtonPanel  .  setAlignmentY  (  0.0F  )  ; 
mCSTButtonPanel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  120  ,  200  )  )  ; 
mCSTButtonPanel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  120  ,  200  )  )  ; 
mCSTButtonPanel  .  setRequestFocusEnabled  (  false  )  ; 
mButtonParse  .  setText  (  "Parse (CS)"  )  ; 
mButtonParse  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonParse  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mButtonParseActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mCSTButtonPanel  .  add  (  mButtonParse  )  ; 
mButtonExpandAll  .  setText  (  "Expand All"  )  ; 
mButtonExpandAll  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mButtonExpandAllActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mCSTButtonPanel  .  add  (  mButtonExpandAll  )  ; 
mButtonVisualize  .  setText  (  "Visualize"  )  ; 
mButtonVisualize  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonVisualize  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mButtonVisualizeActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mCSTButtonPanel  .  add  (  mButtonVisualize  )  ; 
mCSTViewPanel  .  add  (  mCSTButtonPanel  ,  java  .  awt  .  BorderLayout  .  EAST  )  ; 
mCSTViewScrollPane  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  2147483647  ,  2147483647  )  )  ; 
mCSTViewScrollPane  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mCSTView  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  2147483647  ,  2147483647  )  )  ; 
mCSTView  .  setMinimumSize  (  null  )  ; 
mCSTView  .  setPreferredSize  (  null  )  ; 
mCSTViewScrollPane  .  setViewportView  (  mCSTView  )  ; 
mCSTViewPanel  .  add  (  mCSTViewScrollPane  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
mTabPane  .  addTab  (  "CST"  ,  mCSTViewPanel  )  ; 
mCSTVisualizerPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
mCSTVisualizerPanel  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  2147483647  ,  2147483647  )  )  ; 
mCSTVizScrollPane  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  2147483647  ,  2147483647  )  )  ; 
mCSTVizScrollPane  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mCSTVizScrollPane  .  setViewportView  (  mGraphCSTViz  )  ; 
mCSTVisualizerPanel  .  add  (  mCSTVizScrollPane  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
mTabPane  .  addTab  (  "CST Viz"  ,  mCSTVisualizerPanel  )  ; 
mRepoPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
mRepoButtonPanel  .  setBorder  (  new   javax  .  swing  .  border  .  EtchedBorder  (  javax  .  swing  .  border  .  EtchedBorder  .  RAISED  )  )  ; 
mRepoButtonPanel  .  setAlignmentX  (  1.0F  )  ; 
mRepoButtonPanel  .  setAlignmentY  (  0.0F  )  ; 
mRepoButtonPanel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  120  ,  200  )  )  ; 
mRepoButtonPanel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  120  ,  200  )  )  ; 
mRepoButtonPanel  .  setRequestFocusEnabled  (  false  )  ; 
mButtonListModels  .  setText  (  "List Mdls"  )  ; 
mButtonListModels  .  setActionCommand  (  "REPO_LIST_MODELS"  )  ; 
mButtonListModels  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonListModels  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonListModels  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonListModels  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mButtonListModelsActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mRepoButtonPanel  .  add  (  mButtonListModels  )  ; 
mButtonDeleteModel  .  setText  (  "Erase Mdl"  )  ; 
mButtonDeleteModel  .  setActionCommand  (  "REPO_DELETE_MODEL"  )  ; 
mButtonDeleteModel  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonDeleteModel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonDeleteModel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mRepoButtonPanel  .  add  (  mButtonDeleteModel  )  ; 
mRepoPanel  .  add  (  mRepoButtonPanel  ,  java  .  awt  .  BorderLayout  .  EAST  )  ; 
mRepoInfoPanel  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
mRepoInfoPanel  .  setBorder  (  new   javax  .  swing  .  border  .  EtchedBorder  (  javax  .  swing  .  border  .  EtchedBorder  .  RAISED  )  )  ; 
mLblMetaModel1  .  setText  (  "Metamodel"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  3  ,  3  ,  0  ,  3  )  ; 
mRepoInfoPanel  .  add  (  mLblMetaModel1  ,  gridBagConstraints  )  ; 
mCbxMetamodel1  .  setModel  (  new   javax  .  swing  .  DefaultComboBoxModel  (  new   String  [  ]  {  "UML 1.5"  ,  "MOF 1.4"  }  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  0  ,  3  ,  0  ,  3  )  ; 
mRepoInfoPanel  .  add  (  mCbxMetamodel1  ,  gridBagConstraints  )  ; 
mRepoContentScrollPane  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  2147483647  ,  2147483647  )  )  ; 
mRepoContentScrollPane  .  setMinimumSize  (  null  )  ; 
mRepoContentView  .  setBorder  (  new   javax  .  swing  .  border  .  TitledBorder  (  "Models with selected Metamodel"  )  )  ; 
mRepoContentScrollPane  .  setViewportView  (  mRepoContentView  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  weighty  =  1.0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  3  ,  3  ,  0  ,  3  )  ; 
mRepoInfoPanel  .  add  (  mRepoContentScrollPane  ,  gridBagConstraints  )  ; 
mRepoPanel  .  add  (  mRepoInfoPanel  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
mTabPane  .  addTab  (  "Repo"  ,  mRepoPanel  )  ; 
mModelPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
mModelButtonPanel  .  setBorder  (  new   javax  .  swing  .  border  .  EtchedBorder  (  javax  .  swing  .  border  .  EtchedBorder  .  RAISED  )  )  ; 
mModelButtonPanel  .  setAlignmentX  (  1.0F  )  ; 
mModelButtonPanel  .  setAlignmentY  (  0.0F  )  ; 
mModelButtonPanel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  120  ,  200  )  )  ; 
mModelButtonPanel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  120  ,  200  )  )  ; 
mModelButtonPanel  .  setRequestFocusEnabled  (  false  )  ; 
mButtonLoadModel  .  setText  (  "Load XMI"  )  ; 
mButtonLoadModel  .  setActionCommand  (  "LOAD_MODEL_XMI"  )  ; 
mButtonLoadModel  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonLoadModel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonLoadModel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonLoadModel  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mButtonLoadModelActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mModelButtonPanel  .  add  (  mButtonLoadModel  )  ; 
mButtonReserve1  .  setText  (  "Reserve1"  )  ; 
mButtonReserve1  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonReserve1  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonReserve1  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonReserve1  .  setEnabled  (  false  )  ; 
mButtonReserve1  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mButtonReserve1ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mModelButtonPanel  .  add  (  mButtonReserve1  )  ; 
mButtonReserve2  .  setText  (  "Reserve2"  )  ; 
mButtonReserve2  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonReserve2  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonReserve2  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  100  ,  25  )  )  ; 
mButtonReserve2  .  setEnabled  (  false  )  ; 
mButtonReserve2  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mButtonReserve2ActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mModelButtonPanel  .  add  (  mButtonReserve2  )  ; 
mModelPanel  .  add  (  mModelButtonPanel  ,  java  .  awt  .  BorderLayout  .  EAST  )  ; 
mModelInfoPanel  .  setLayout  (  new   java  .  awt  .  GridBagLayout  (  )  )  ; 
mModelInfoPanel  .  setBorder  (  new   javax  .  swing  .  border  .  EtchedBorder  (  javax  .  swing  .  border  .  EtchedBorder  .  RAISED  )  )  ; 
mLblMetaModel  .  setText  (  "Metamodel"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  3  ,  3  ,  0  ,  3  )  ; 
mModelInfoPanel  .  add  (  mLblMetaModel  ,  gridBagConstraints  )  ; 
mCbxMetamodel  .  setModel  (  new   javax  .  swing  .  DefaultComboBoxModel  (  new   String  [  ]  {  "UML 1.5"  ,  "MOF 1.4"  }  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  anchor  =  java  .  awt  .  GridBagConstraints  .  WEST  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  0  ,  3  ,  0  ,  3  )  ; 
mModelInfoPanel  .  add  (  mCbxMetamodel  ,  gridBagConstraints  )  ; 
mLblModelFile  .  setText  (  "OCL model origin"  )  ; 
mLblModelFile  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  32767  ,  32767  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  9  ,  3  ,  0  ,  3  )  ; 
mModelInfoPanel  .  add  (  mLblModelFile  ,  gridBagConstraints  )  ; 
mTxfModelFile  .  setEditable  (  false  )  ; 
mTxfModelFile  .  setText  (  "<undefined>"  )  ; 
mTxfModelFile  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  120  ,  19  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  0  ,  3  ,  0  ,  3  )  ; 
mModelInfoPanel  .  add  (  mTxfModelFile  ,  gridBagConstraints  )  ; 
mLblModelName  .  setText  (  "Model name"  )  ; 
mLblModelName  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  32767  ,  32767  )  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  9  ,  3  ,  0  ,  3  )  ; 
mModelInfoPanel  .  add  (  mLblModelName  ,  gridBagConstraints  )  ; 
mTxfModelName  .  setEditable  (  false  )  ; 
mTxfModelName  .  setText  (  "<undefined>"  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  HORIZONTAL  ; 
gridBagConstraints  .  weightx  =  1.0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  0  ,  3  ,  0  ,  3  )  ; 
mModelInfoPanel  .  add  (  mTxfModelName  ,  gridBagConstraints  )  ; 
mPanelRemainder  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
mMdlLogScrollPane  .  setViewportView  (  mMdlLogTextArea  )  ; 
mPanelRemainder  .  add  (  mMdlLogScrollPane  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
gridBagConstraints  =  new   java  .  awt  .  GridBagConstraints  (  )  ; 
gridBagConstraints  .  gridwidth  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  gridheight  =  java  .  awt  .  GridBagConstraints  .  REMAINDER  ; 
gridBagConstraints  .  fill  =  java  .  awt  .  GridBagConstraints  .  BOTH  ; 
gridBagConstraints  .  weighty  =  1.0  ; 
gridBagConstraints  .  insets  =  new   java  .  awt  .  Insets  (  3  ,  3  ,  3  ,  3  )  ; 
mModelInfoPanel  .  add  (  mPanelRemainder  ,  gridBagConstraints  )  ; 
mModelPanel  .  add  (  mModelInfoPanel  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
mTabPane  .  addTab  (  "Model"  ,  mModelPanel  )  ; 
mASTViewPanel  .  setLayout  (  new   java  .  awt  .  BorderLayout  (  )  )  ; 
mASTLogScrollPane  .  setViewportView  (  mASTLogTextArea  )  ; 
mASTViewPanel  .  add  (  mASTLogScrollPane  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
mASTViewButtonPanel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  120  ,  10  )  )  ; 
mASTViewButtonPanel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  120  ,  10  )  )  ; 
mBtnASTGenerate  .  setText  (  "Generate"  )  ; 
mBtnASTGenerate  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnASTGenerate  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnASTGenerate  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnASTGenerate  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mBtnASTGenerateActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mASTViewButtonPanel  .  add  (  mBtnASTGenerate  )  ; 
mBtnASTLogClear  .  setText  (  "Clear"  )  ; 
mBtnASTLogClear  .  setActionCommand  (  "ASTLOG_CLEAR"  )  ; 
mBtnASTLogClear  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnASTLogClear  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnASTLogClear  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnASTLogClear  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mBtnASTLogClearActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mASTViewButtonPanel  .  add  (  mBtnASTLogClear  )  ; 
mBtnExportModel  .  setText  (  "Export XMI"  )  ; 
mBtnExportModel  .  setActionCommand  (  "EXPORT_MODEL_XMI"  )  ; 
mBtnExportModel  .  setMaximumSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnExportModel  .  setMinimumSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnExportModel  .  setPreferredSize  (  new   java  .  awt  .  Dimension  (  110  ,  25  )  )  ; 
mBtnExportModel  .  addActionListener  (  new   java  .  awt  .  event  .  ActionListener  (  )  { 

public   void   actionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
onActionPerformed  (  evt  )  ; 
} 
}  )  ; 
mASTViewButtonPanel  .  add  (  mBtnExportModel  )  ; 
mASTViewPanel  .  add  (  mASTViewButtonPanel  ,  java  .  awt  .  BorderLayout  .  EAST  )  ; 
mTabPane  .  addTab  (  "ASTGen log"  ,  mASTViewPanel  )  ; 
getContentPane  (  )  .  add  (  mTabPane  ,  java  .  awt  .  BorderLayout  .  CENTER  )  ; 
pack  (  )  ; 
} 

private   void   mButtonListModelsActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
String   metaModel  =  MetaModelConst  .  UML15  ; 
TextualModelTreeBuilder   mtb  =  new   TextualModelTreeBuilder  (  ModelManager  .  getInstance  (  )  )  ; 
DefaultMutableTreeNode   root  =  new   DefaultMutableTreeNode  (  "[Repository]"  )  ; 
try  { 
List   models  =  OclModel  .  findOclModels  (  metaModel  )  ; 
Iterator   it  =  models  .  iterator  (  )  ; 
if  (  it  .  hasNext  (  )  )  { 
OclModel   currentModel  =  (  OclModel  )  it  .  next  (  )  ; 
tudresden  .  ocl20  .  core  .  jmi  .  ocl  .  commonmodel  .  Package   topPackage  =  currentModel  .  getTopPackage  (  )  ; 
DefaultMutableTreeNode   dmtn  =  mtb  .  caseTopPackage  (  topPackage  )  ; 
root  .  add  (  dmtn  )  ; 
} 
}  catch  (  OclModelException   mex  )  { 
mMdlLogTextArea  .  append  (  "\n\n====[ Exception ]====\n"  +  mex  .  getMessage  (  )  +  "\n"  )  ; 
StringWriter   sw  =  new   StringWriter  (  1024  )  ; 
PrintWriter   pw  =  new   PrintWriter  (  sw  )  ; 
mex  .  printStackTrace  (  pw  )  ; 
pw  .  flush  (  )  ; 
sw  .  flush  (  )  ; 
mMdlLogTextArea  .  append  (  sw  .  toString  (  )  )  ; 
} 
this  .  mRepoContentView  .  setModel  (  new   DefaultTreeModel  (  root  )  )  ; 
} 

private   void   mButtonReserve2ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mMdlLogTextArea  .  setText  (  ""  )  ; 
} 

private   void   mButtonReserve1ActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
} 

private   void   mBtnASTGenerateActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
if  (  cst  ==  null  )  { 
this  .  displayMessage  (  "Please parse a constraint first."  )  ; 
return  ; 
} 
if  (  oclModel  ==  null  )  { 
this  .  displayMessage  (  "Please load a model first."  )  ; 
return  ; 
} 
LAttrAstGenerator   astgen  =  new   LAttrAstGenerator  (  oclModel  )  ; 
astgen  .  setMessageSink  (  this  )  ; 
Heritage   hrtg  =  new   Heritage  (  )  ; 
try  { 
oclModel  .  beginTrans  (  true  )  ; 
cst  .  apply  (  astgen  ,  hrtg  )  ; 
oclModel  .  endTrans  (  false  )  ; 
processMessage  (  "Attribute evaluation completed successfully."  )  ; 
}  catch  (  AttrEvalException   evex  )  { 
oclModel  .  endTrans  (  true  )  ; 
String   message  =  evex  .  getMessage  (  )  ; 
String   position  =  null  ; 
Token   tk  =  astgen  .  getCurrentToken  (  )  ; 
if  (  tk  !=  null  )  { 
position  =  ""  +  tk  .  getLine  (  )  +  ":"  +  tk  .  getPos  (  )  ; 
}  else  { 
position  =  "<unknown>"  ; 
} 
processMessage  (  "===[ Contextual Analysis Error at "  +  position  +  "]==="  )  ; 
processMessage  (  message  )  ; 
StringWriter   swriter  =  new   StringWriter  (  1024  )  ; 
PrintWriter   pwriter  =  new   PrintWriter  (  swriter  )  ; 
evex  .  printStackTrace  (  pwriter  )  ; 
pwriter  .  flush  (  )  ; 
swriter  .  flush  (  )  ; 
processMessage  (  swriter  .  toString  (  )  )  ; 
Throwable   cause  =  evex  .  getCause  (  )  ; 
if  (  cause  !=  null  )  { 
processMessage  (  "Cause: "  +  cause  .  getMessage  (  )  )  ; 
swriter  =  new   StringWriter  (  1024  )  ; 
pwriter  =  new   PrintWriter  (  swriter  )  ; 
cause  .  printStackTrace  (  pwriter  )  ; 
pwriter  .  flush  (  )  ; 
swriter  .  flush  (  )  ; 
processMessage  (  swriter  .  toString  (  )  )  ; 
} 
processMessage  (  "======="  )  ; 
}  catch  (  JmiException   jmix  )  { 
oclModel  .  endTrans  (  true  )  ; 
String   message  =  jmix  .  getMessage  (  )  ; 
String   position  =  null  ; 
Token   tk  =  astgen  .  getCurrentToken  (  )  ; 
if  (  tk  !=  null  )  { 
position  =  ""  +  tk  .  getLine  (  )  +  ":"  +  tk  .  getPos  (  )  ; 
}  else  { 
position  =  "<unknown>"  ; 
} 
processMessage  (  "===[ JMI Exception during contextual analysis at "  +  position  +  "]==="  )  ; 
RefObject   elInErr  =  jmix  .  getElementInError  (  )  ; 
Object   objInErr  =  jmix  .  getObjectInError  (  )  ; 
if  (  objInErr  !=  null  )  { 
processMessage  (  "Object in error (class name): "  +  objInErr  .  getClass  (  )  .  getName  (  )  )  ; 
processMessage  (  "Object in error (toString): "  +  objInErr  )  ; 
} 
if  (  elInErr  !=  null  )  { 
processMessage  (  "Element in error (clas name): "  +  elInErr  .  getClass  (  )  .  getName  (  )  )  ; 
processMessage  (  "Element in error (MOF Id): "  +  elInErr  .  refMofId  (  )  )  ; 
processMessage  (  "Element in error (toString) "  +  elInErr  )  ; 
} 
processMessage  (  "=== Exception message and stack trace follows ==="  )  ; 
processMessage  (  message  )  ; 
StringWriter   swriter  =  new   StringWriter  (  1024  )  ; 
PrintWriter   pwriter  =  new   PrintWriter  (  swriter  )  ; 
jmix  .  printStackTrace  (  pwriter  )  ; 
pwriter  .  flush  (  )  ; 
swriter  .  flush  (  )  ; 
processMessage  (  swriter  .  toString  (  )  )  ; 
}  catch  (  Throwable   t  )  { 
oclModel  .  endTrans  (  true  )  ; 
String   message  =  t  .  getMessage  (  )  ; 
if  (  message  ==  null  )  { 
message  =  "No exception detail message available"  ; 
} 
String   position  =  null  ; 
Token   tk  =  astgen  .  getCurrentToken  (  )  ; 
if  (  tk  !=  null  )  { 
position  =  ""  +  tk  .  getLine  (  )  +  ":"  +  tk  .  getPos  (  )  ; 
}  else  { 
position  =  "<unknown>"  ; 
} 
processMessage  (  "===[ Exception during contextual analysis at "  +  position  +  "]==="  )  ; 
processMessage  (  message  )  ; 
StringWriter   swriter  =  new   StringWriter  (  1024  )  ; 
PrintWriter   pwriter  =  new   PrintWriter  (  swriter  )  ; 
t  .  printStackTrace  (  pwriter  )  ; 
pwriter  .  flush  (  )  ; 
swriter  .  flush  (  )  ; 
processMessage  (  swriter  .  toString  (  )  )  ; 
processMessage  (  "======="  )  ; 
} 
} 

private   void   mBtnASTLogClearActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
this  .  mASTLogTextArea  .  setText  (  null  )  ; 
} 

private   void   mModelFileChooserActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
} 

private   void   mButtonLoadModelActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
String   cmd  =  evt  .  getActionCommand  (  )  ; 
int   returnValue  =  -  1  ; 
if  (  cmd  .  equals  (  "LOAD_MODEL_XMI"  )  )  { 
int   selectedMetamodelIndex  =  this  .  mCbxMetamodel  .  getSelectedIndex  (  )  ; 
if  (  selectedMetamodelIndex  <  0  )  { 
displayMessage  (  "No meta model selected (selection index -1)"  )  ; 
return  ; 
} 
assert  (  selectedMetamodelIndex  <  2  )  :  "meta model index "  +  selectedMetamodelIndex  +  " not supported in mButtonLoadModelActionPerformed"  ; 
String   metaModelName  =  null  ; 
switch  (  selectedMetamodelIndex  )  { 
case   0  : 
metaModelName  =  MetaModelConst  .  UML15  ; 
break  ; 
case   1  : 
metaModelName  =  MetaModelConst  .  MOF14  ; 
break  ; 
default  : 
displayMessage  (  "Unsupported meta model selected (selection index >= 2)"  )  ; 
break  ; 
} 
returnValue  =  mModelFileChooser  .  showOpenDialog  (  this  )  ; 
if  (  returnValue  ==  javax  .  swing  .  JFileChooser  .  APPROVE_OPTION  )  { 
File   XmiFile  =  mModelFileChooser  .  getSelectedFile  (  )  ; 
if  (  (  XmiFile  .  getName  (  )  )  .  endsWith  (  ".zargo"  )  )  { 
loadModelArgo  (  metaModelName  ,  XmiFile  )  ; 
}  else  { 
loadModelXmi  (  metaModelName  ,  XmiFile  )  ; 
} 
} 
} 
} 

private   void   mConstraintFileChooserActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
} 

private   void   mButtonVisualizeActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
System  .  out  .  println  (  "mButtonVisualizeActionPerformed: entry"  )  ; 
displayConcreteSyntaxTree  (  )  ; 
System  .  out  .  println  (  "mButtonVisualizeActionPerformed: exit"  )  ; 
} 

private   void   mButtonExpandAllActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
javax  .  swing  .  JTree   theTree  =  mCSTView  ; 
for  (  int   i  =  0  ;  i  <  theTree  .  getRowCount  (  )  ;  i  ++  )  { 
theTree  .  expandRow  (  i  )  ; 
} 
} 

private   void   mConstraintTextAreaCaretUpdate  (  javax  .  swing  .  event  .  CaretEvent   evt  )  { 
int   dotPos  =  evt  .  getDot  (  )  ; 
String   posStr  =  null  ; 
try  { 
int   line  =  mConstraintTextArea  .  getLineOfOffset  (  dotPos  )  ; 
int   lineStartOffset  =  mConstraintTextArea  .  getLineStartOffset  (  line  )  ; 
int   column  =  dotPos  -  lineStartOffset  ; 
posStr  =  "Line "  +  (  line  +  1  )  +  ", Column "  +  (  column  +  1  )  ; 
}  catch  (  javax  .  swing  .  text  .  BadLocationException   ex  )  { 
posStr  =  "Invalid position"  ; 
} 
mConstraintEditorPositionLabel  .  setText  (  posStr  )  ; 
} 

private   void   mButtonParseActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
runParser  (  )  ; 
} 

private   void   mCloseButtonActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
mMessageDialog  .  dispose  (  )  ; 
} 

private   void   onActionPerformed  (  java  .  awt  .  event  .  ActionEvent   evt  )  { 
String   cmd  =  evt  .  getActionCommand  (  )  ; 
int   returnValue  =  -  1  ; 
if  (  cmd  .  equals  (  "CONSTRAINT_LOAD"  )  )  { 
returnValue  =  mConstraintFileChooser  .  showOpenDialog  (  this  )  ; 
if  (  returnValue  ==  javax  .  swing  .  JFileChooser  .  APPROVE_OPTION  )  { 
loadOclFile  (  mConstraintFileChooser  .  getSelectedFile  (  )  )  ; 
} 
}  else   if  (  cmd  .  equals  (  "CONSTRAINT_SAVE"  )  )  { 
returnValue  =  mConstraintFileChooser  .  showSaveDialog  (  this  )  ; 
if  (  returnValue  ==  javax  .  swing  .  JFileChooser  .  APPROVE_OPTION  )  { 
saveOclFile  (  mConstraintFileChooser  .  getSelectedFile  (  )  )  ; 
} 
}  else   if  (  cmd  .  equals  (  "EXPORT_MODEL_XMI"  )  )  { 
returnValue  =  mModelFileChooser  .  showSaveDialog  (  this  )  ; 
if  (  returnValue  ==  javax  .  swing  .  JFileChooser  .  APPROVE_OPTION  )  { 
saveModelXmi  (  mModelFileChooser  .  getSelectedFile  (  )  )  ; 
} 
} 
} 

private   void   loadOclFile  (  java  .  io  .  File   file  )  { 
String   path  =  file  .  getAbsolutePath  (  )  ; 
boolean   canRead  =  file  .  canRead  (  )  ; 
if  (  !  canRead  )  { 
displayMessage  (  "Cannot read file "  +  path  +  "! Aborting load."  )  ; 
}  else  { 
try  { 
java  .  io  .  BufferedReader   br  =  new   java  .  io  .  BufferedReader  (  new   java  .  io  .  InputStreamReader  (  new   java  .  io  .  FileInputStream  (  path  )  )  )  ; 
mConstraintTextArea  .  setText  (  ""  )  ; 
String   line  =  br  .  readLine  (  )  ; 
while  (  line  !=  null  )  { 
mConstraintTextArea  .  append  (  line  )  ; 
mConstraintTextArea  .  append  (  "\n"  )  ; 
line  =  br  .  readLine  (  )  ; 
} 
br  .  close  (  )  ; 
setOclFilename  (  file  .  getName  (  )  )  ; 
}  catch  (  java  .  io  .  IOException   ex  )  { 
displayMessage  (  "Error reading file "  +  path  +  "! Check system error stream for details."  )  ; 
ex  .  printStackTrace  (  System  .  err  )  ; 
} 
} 
mTabPane  .  setSelectedComponent  (  mCSTViewPanel  )  ; 
} 

private   void   saveOclFile  (  java  .  io  .  File   file  )  { 
String   path  =  file  .  getAbsolutePath  (  )  ; 
boolean   exists  =  file  .  exists  (  )  ; 
boolean   canWrite  =  file  .  canWrite  (  )  ; 
if  (  !  exists  ||  canWrite  )  { 
try  { 
java  .  io  .  BufferedWriter   bw  =  new   java  .  io  .  BufferedWriter  (  new   java  .  io  .  OutputStreamWriter  (  new   java  .  io  .  FileOutputStream  (  path  )  )  )  ; 
String   text  =  mConstraintTextArea  .  getText  (  )  ; 
bw  .  write  (  text  )  ; 
bw  .  close  (  )  ; 
setOclFilename  (  file  .  getName  (  )  )  ; 
}  catch  (  java  .  io  .  IOException   ex  )  { 
displayMessage  (  "Error saving file "  +  path  +  "! Check system error stream for details."  )  ; 
ex  .  printStackTrace  (  System  .  err  )  ; 
} 
}  else  { 
displayMessage  (  "Cannot write to file "  +  path  +  "! Aborting save."  )  ; 
} 
} 

private   void   saveModelXmi  (  java  .  io  .  File   file  )  { 
OclModel   model  =  this  .  getOclModel  (  )  ; 
if  (  model  ==  null  )  { 
displayMessage  (  "No model loaded, cannot save."  )  ; 
return  ; 
} 
String   path  =  file  .  getAbsolutePath  (  )  ; 
boolean   exists  =  file  .  exists  (  )  ; 
boolean   canWrite  =  file  .  canWrite  (  )  ; 
if  (  !  exists  ||  canWrite  )  { 
try  { 
model  .  save  (  path  )  ; 
}  catch  (  OclModelException   ex  )  { 
displayMessage  (  "Error saving OCL model. "  +  ex  .  getMessage  (  )  +  ", check system error stream for details."  )  ; 
ex  .  printStackTrace  (  System  .  err  )  ; 
} 
}  else  { 
displayMessage  (  "Cannot write to file "  +  path  +  "! Aborting save."  )  ; 
} 
} 

public   static   final   void   copyInputStream  (  InputStream   in  ,  OutputStream   out  )  throws   IOException  { 
byte  [  ]  buffer  =  new   byte  [  1024  ]  ; 
int   len  ; 
while  (  (  len  =  in  .  read  (  buffer  )  )  >=  0  )  out  .  write  (  buffer  ,  0  ,  len  )  ; 
in  .  close  (  )  ; 
out  .  close  (  )  ; 
} 

private   void   loadModelArgo  (  String   metaModelname  ,  java  .  io  .  File   file  )  { 
String   argoName  =  file  .  getName  (  )  ; 
String   path  =  file  .  getAbsolutePath  (  )  ; 
if  (  !  argoName  .  endsWith  (  ".zargo"  )  )  { 
System  .  err  .  println  (  "Not an ArgoUML file: "  +  path  +  " (no .zargo suffix)! Aborting load."  )  ; 
return  ; 
} 
if  (  !  file  .  canRead  (  )  )  { 
System  .  err  .  println  (  "Cannot read model file "  +  path  +  "! Aborting load."  )  ; 
return  ; 
} 
try  { 
String   baseName  =  argoName  .  substring  (  0  ,  argoName  .  length  (  )  -  6  )  ; 
ZipFile   argoFile  =  new   ZipFile  (  file  )  ; 
File   temp  =  File  .  createTempFile  (  baseName  ,  ".xmi"  )  ; 
temp  .  deleteOnExit  (  )  ; 
copyInputStream  (  argoFile  .  getInputStream  (  argoFile  .  getEntry  (  baseName  +  ".xmi"  )  )  ,  new   BufferedOutputStream  (  new   FileOutputStream  (  temp  )  )  )  ; 
argoFile  .  close  (  )  ; 
loadModelXmi  (  metaModelname  ,  temp  )  ; 
}  catch  (  IOException   ioe  )  { 
System  .  err  .  println  (  "Unhandled exception:"  )  ; 
ioe  .  printStackTrace  (  )  ; 
} 
return  ; 
} 

private   void   loadModelXmi  (  String   metaModelName  ,  java  .  io  .  File   file  )  { 
String   path  =  file  .  getAbsolutePath  (  )  ; 
String   modelUrl  =  "file:"  +  path  ; 
boolean   canRead  =  file  .  canRead  (  )  ; 
if  (  !  canRead  )  { 
displayMessage  (  "Cannot read model file "  +  path  +  "! Aborting load."  )  ; 
}  else  { 
System  .  out  .  println  (  "Reading model from URL "  +  modelUrl  )  ; 
try  { 
OclModel   oldModel  =  this  .  getOclModel  (  )  ; 
if  (  oldModel  !=  null  )  { 
oldModel  .  delete  (  )  ; 
} 
OclModel   newModel  =  new   OclModel  (  metaModelName  ,  modelUrl  )  ; 
this  .  setOclModel  (  newModel  ,  modelUrl  )  ; 
tudresden  .  ocl20  .  core  .  jmi  .  ocl  .  commonmodel  .  Package   topPkg  =  newModel  .  getTopPackage  (  )  ; 
String   topName  =  topPkg  .  getNameA  (  )  ; 
System  .  out  .  println  (  "Top package name: '"  +  topName  +  "'"  )  ; 
}  catch  (  OclModelException   ex  )  { 
displayMessage  (  "Error loading OCL model. "  +  ex  .  getMessage  (  )  +  ", check system error stream for details."  )  ; 
ex  .  printStackTrace  (  System  .  err  )  ; 
} 
} 
} 

private   void   displayMessage  (  String   message  )  { 
mMessage  .  setText  (  message  )  ; 
mMessageDialog  .  setModal  (  true  )  ; 
mMessageDialog  .  pack  (  )  ; 
mMessageDialog  .  setVisible  (  true  )  ; 
} 

private   void   displayException  (  String   reason  ,  Exception   e  )  { 
StringWriter   sw  =  new   StringWriter  (  1024  )  ; 
PrintWriter   pw  =  new   PrintWriter  (  sw  )  ; 
pw  .  println  (  reason  )  ; 
pw  .  println  (  e  .  getLocalizedMessage  (  )  )  ; 
e  .  printStackTrace  (  pw  )  ; 
pw  .  close  (  )  ; 
displayMessage  (  sw  .  toString  (  )  )  ; 
} 


private   void   exitForm  (  java  .  awt  .  event  .  WindowEvent   evt  )  { 
if  (  oclModel  !=  null  )  { 
oclModel  .  closeAndShutDown  (  )  ; 
} 
System  .  exit  (  0  )  ; 
} 




public   static   void   main  (  String   args  [  ]  )  { 
new   OCL20GUI  (  )  .  show  (  )  ; 
} 

private   void   runParser  (  )  { 
long   start_time  ,  stop_time  ; 
String   text  =  mConstraintTextArea  .  getText  (  )  ; 
try  { 
start_time  =  System  .  currentTimeMillis  (  )  ; 
Lexer   lexer  =  new   Lexer  (  new   PushbackReader  (  new   StringReader  (  text  )  ,  1024  )  )  ; 
Parser   parser  =  new   Parser  (  lexer  )  ; 
cst  =  parser  .  parse  (  )  ; 
if  (  cst  !=  null  )  { 
TextualCSTBuilder   builder  =  new   TextualCSTBuilder  (  )  ; 
cst  .  apply  (  builder  )  ; 
DefaultMutableTreeNode   root  =  builder  .  getRootNode  (  )  ; 
setCSTViewRoot  (  root  )  ; 
} 
stop_time  =  System  .  currentTimeMillis  (  )  ; 
}  catch  (  ParserException   pe  )  { 
displayException  (  "Parser exception (syntax error) at token '"  +  pe  .  getToken  (  )  .  getText  (  )  +  "'"  ,  pe  )  ; 
}  catch  (  Exception   e  )  { 
displayException  (  "Cannot parse OCL constraint"  ,  e  )  ; 
} 
} 

private   void   displayConcreteSyntaxTree  (  )  { 
System  .  out  .  println  (  "displayConcreteSyntaxTree: entry"  )  ; 
Start   root  =  cst  ; 
GraphicalCSTBuilder   gcstb  =  new   GraphicalCSTBuilder  (  mGraphCSTViz  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: before tree traversal"  )  ; 
root  .  apply  (  gcstb  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: after tree traversal"  )  ; 
JGraphTreeBuilder   jgtb  =  gcstb  .  getBuilder  (  )  ; 
GraphModel   graphModel  =  jgtb  .  getModel  (  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: before root cell consolidation"  )  ; 
List   rootList  =  jgtb  .  getRootCells  (  )  ; 
Iterator   it  =  rootList  .  iterator  (  )  ; 
int   rootCellCount  =  jgtb  .  countRootCells  (  )  ; 
Object  [  ]  rootCells  =  new   Object  [  rootCellCount  ]  ; 
int   rootCellIndex  =  0  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: before root cell consolidation loop"  )  ; 
while  (  it  .  hasNext  (  )  )  { 
rootCells  [  rootCellIndex  ]  =  it  .  next  (  )  ; 
rootCellIndex  ++  ; 
} 
System  .  out  .  println  (  "displayConcreteSyntaxTree: after root cell consolidation"  )  ; 
ConnectionSet   connections  =  jgtb  .  getConnections  (  )  ; 
Map   attributes  =  jgtb  .  getAttributes  (  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: before graph model integration"  )  ; 
graphModel  .  insert  (  rootCells  ,  attributes  ,  connections  ,  null  ,  null  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: after graph model integration"  )  ; 
TreeLayoutAlgorithm   alg  =  new   TreeLayoutAlgorithm  (  )  ; 
alg  .  setAlignment  (  SwingConstants  .  TOP  )  ; 
alg  .  setOrientation  (  SwingConstants  .  NORTH  )  ; 
alg  .  setCenterRoot  (  false  )  ; 
alg  .  setCombineLevelNodes  (  false  )  ; 
alg  .  setNodeDistance  (  160  )  ; 
alg  .  setLevelDistance  (  80  )  ; 
mGraphCSTViz  .  setAlignmentX  (  javax  .  swing  .  JComponent  .  CENTER_ALIGNMENT  )  ; 
mGraphCSTViz  .  setAlignmentY  (  javax  .  swing  .  JComponent  .  CENTER_ALIGNMENT  )  ; 
mGraphCSTViz  .  setModel  (  graphModel  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: before layout processing"  )  ; 
alg  .  run  (  mGraphCSTViz  ,  rootCells  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: after layout processing"  )  ; 
mGraphCSTViz  .  scrollCellToVisible  (  rootCells  [  0  ]  )  ; 
mGraphCSTViz  .  setBackground  (  Color  .  LIGHT_GRAY  )  ; 
Object  [  ]  roots  =  mGraphCSTViz  .  getRoots  (  )  ; 
Rectangle2D   bounds  =  mGraphCSTViz  .  getCellBounds  (  roots  )  ; 
int   width  =  (  int  )  Math  .  round  (  bounds  .  getWidth  (  )  )  ; 
int   height  =  (  int  )  Math  .  round  (  bounds  .  getHeight  (  )  )  ; 
mCSTVizScrollPane  .  setPreferredSize  (  mGraphCSTViz  .  getPreferredScrollableViewportSize  (  )  )  ; 
mGraphCSTViz  .  setPreferredSize  (  mGraphCSTViz  .  getPreferredScrollableViewportSize  (  )  )  ; 
mGraphCSTViz  .  scrollCellToVisible  (  roots  [  0  ]  )  ; 
mGraphCSTViz  .  graphDidChange  (  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: after \"graphDidChange()\""  )  ; 
mTabPane  .  setSelectedComponent  (  mCSTVisualizerPanel  )  ; 
mGraphCSTViz  .  invalidate  (  )  ; 
mCSTVizScrollPane  .  invalidate  (  )  ; 
System  .  out  .  println  (  "displayConcreteSyntaxTree: after invalidate()"  )  ; 
mTabPane  .  validate  (  )  ; 
} 

private   javax  .  swing  .  JScrollPane   mASTLogScrollPane  ; 

private   javax  .  swing  .  JTextArea   mASTLogTextArea  ; 

private   javax  .  swing  .  JPanel   mASTViewButtonPanel  ; 

private   javax  .  swing  .  JPanel   mASTViewPanel  ; 

private   javax  .  swing  .  JButton   mBtnASTGenerate  ; 

private   javax  .  swing  .  JButton   mBtnASTLogClear  ; 

private   javax  .  swing  .  JButton   mBtnExportModel  ; 

private   javax  .  swing  .  JButton   mButtonDeleteModel  ; 

private   javax  .  swing  .  JButton   mButtonExpandAll  ; 

private   javax  .  swing  .  JButton   mButtonListModels  ; 

private   javax  .  swing  .  JButton   mButtonLoadConstraint  ; 

private   javax  .  swing  .  JButton   mButtonLoadModel  ; 

private   javax  .  swing  .  JPanel   mButtonPanel  ; 

private   javax  .  swing  .  JButton   mButtonParse  ; 

private   javax  .  swing  .  JButton   mButtonReserve1  ; 

private   javax  .  swing  .  JButton   mButtonReserve2  ; 

private   javax  .  swing  .  JButton   mButtonSaveConstraint  ; 

private   javax  .  swing  .  JButton   mButtonVisualize  ; 

private   javax  .  swing  .  JPanel   mCSTButtonPanel  ; 

private   javax  .  swing  .  JTree   mCSTView  ; 

private   javax  .  swing  .  JPanel   mCSTViewPanel  ; 

private   javax  .  swing  .  JScrollPane   mCSTViewScrollPane  ; 

private   javax  .  swing  .  JPanel   mCSTVisualizerPanel  ; 

private   javax  .  swing  .  JScrollPane   mCSTVizScrollPane  ; 

private   javax  .  swing  .  JComboBox   mCbxMetamodel  ; 

private   javax  .  swing  .  JComboBox   mCbxMetamodel1  ; 

private   javax  .  swing  .  JButton   mCloseButton  ; 

private   javax  .  swing  .  JPanel   mConstraintButtonPanel  ; 

private   javax  .  swing  .  JLabel   mConstraintEditorPositionLabel  ; 

private   javax  .  swing  .  JFileChooser   mConstraintFileChooser  ; 

private   javax  .  swing  .  JPanel   mConstraintPanel  ; 

private   javax  .  swing  .  JScrollPane   mConstraintScrollPane  ; 

private   javax  .  swing  .  JTextArea   mConstraintTextArea  ; 

private   org  .  jgraph  .  JGraph   mGraphCSTViz  ; 

private   javax  .  swing  .  JLabel   mLblMetaModel  ; 

private   javax  .  swing  .  JLabel   mLblMetaModel1  ; 

private   javax  .  swing  .  JLabel   mLblModelFile  ; 

private   javax  .  swing  .  JLabel   mLblModelName  ; 

private   javax  .  swing  .  JScrollPane   mMdlLogScrollPane  ; 

private   javax  .  swing  .  JTextArea   mMdlLogTextArea  ; 

private   javax  .  swing  .  JTextArea   mMessage  ; 

private   javax  .  swing  .  JDialog   mMessageDialog  ; 

private   javax  .  swing  .  JScrollPane   mMessageScrollPane  ; 

private   javax  .  swing  .  JPanel   mModelButtonPanel  ; 

private   javax  .  swing  .  JFileChooser   mModelFileChooser  ; 

private   javax  .  swing  .  JPanel   mModelInfoPanel  ; 

private   javax  .  swing  .  JPanel   mModelPanel  ; 

private   javax  .  swing  .  JPanel   mPanelRemainder  ; 

private   javax  .  swing  .  JPanel   mRepoButtonPanel  ; 

private   javax  .  swing  .  JScrollPane   mRepoContentScrollPane  ; 

private   javax  .  swing  .  JTree   mRepoContentView  ; 

private   javax  .  swing  .  JPanel   mRepoInfoPanel  ; 

private   javax  .  swing  .  JPanel   mRepoPanel  ; 

private   javax  .  swing  .  JTabbedPane   mTabPane  ; 

private   javax  .  swing  .  JTextField   mTxfModelFile  ; 

private   javax  .  swing  .  JTextField   mTxfModelName  ; 
} 

