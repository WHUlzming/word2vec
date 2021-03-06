    private static final int SEND = 1;



    private final int mode;



    private DcmURL url = null;



    private int repeatSingle = 1;



    private int repeatWhole = 1;



    private int priority = Command.MEDIUM;



    private int acTimeout = 5000;



    private int dimseTimeout = 0;



    private int soCloseDelay = 500;



    private String uidSuffix = null;



    private AAssociateRQ assocRQ = aFact.newAAssociateRQ();



    private boolean packPDVs = false;



    private boolean truncPostPixelData = false;



    private int bufferSize = 2048;



    private byte[] buffer = null;



    private SSLContextAdapter tls = null;



    private String[] cipherSuites = null;
