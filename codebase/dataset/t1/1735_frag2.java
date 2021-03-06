public class DcmSnd implements PollDirSrv.Handler {



    private static final String[] DEF_TS = { UIDs.ImplicitVRLittleEndian };



    private static final int PCID_ECHO = 1;



    static final Logger log = Logger.getLogger("DcmSnd");



    private static ResourceBundle messages = ResourceBundle.getBundle("DcmSnd", Locale.getDefault());



    private static final UIDDictionary uidDict = DictionaryFactory.getInstance().getDefaultUIDDictionary();



    private static final AssociationFactory aFact = AssociationFactory.getInstance();



    private static final DcmObjectFactory oFact = DcmObjectFactory.getInstance();



    private static final DcmParserFactory pFact = DcmParserFactory.getInstance();



    private static final int ECHO = 0;



    private static final int SEND = 1;



    private static final int POLL = 2;



    private final int mode;



    private DcmURL url = null;



    private int repeatSingle = 1;



    private int repeatWhole = 1;



    private int priority = Command.MEDIUM;



    private int acTimeout = 5000;



    private int dimseTimeout = 0;



    private int soCloseDelay = 500;
