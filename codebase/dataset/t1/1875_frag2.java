public class Acr2Dcm {



    static final byte[] PXDATA_GROUPLEN = { (byte) 0xe0, (byte) 0x7f, (byte) 0x00, (byte) 0x00, 4, 0, 0, 0 };



    static final byte[] PXDATA_TAG = { (byte) 0xe0, (byte) 0x7f, (byte) 0x10, (byte) 0x00 };



    static final DcmParserFactory pfact = DcmParserFactory.getInstance();



    static final DcmObjectFactory fact = DcmObjectFactory.getInstance();



    static final UIDGenerator gen = UIDGenerator.getInstance();
