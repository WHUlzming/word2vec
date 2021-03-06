import java.io.File;

import java.io.IOException;

import java.io.InputStream;

import java.nio.ByteOrder;

import java.text.DecimalFormat;

import java.text.MessageFormat;

import java.util.Arrays;

import java.util.Enumeration;

import java.util.HashMap;

import java.util.HashSet;

import java.util.Iterator;

import java.util.LinkedList;

import java.util.Locale;

import java.util.Map;

import java.util.Properties;

import java.util.ResourceBundle;

import javax.xml.transform.Templates;

import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerConfigurationException;

import javax.xml.transform.TransformerFactory;

import javax.xml.transform.sax.SAXTransformerFactory;

import javax.xml.transform.sax.TransformerHandler;

import javax.xml.transform.stream.StreamResult;

import javax.xml.transform.stream.StreamSource;

import gnu.getopt.Getopt;

import gnu.getopt.LongOpt;

import org.dcm4che.data.Dataset;

import org.dcm4che.data.DcmEncodeParam;

import org.dcm4che.data.DcmObjectFactory;

import org.dcm4che.data.DcmParseException;

import org.dcm4che.dict.DictionaryFactory;

import org.dcm4che.dict.TagDictionary;

import org.dcm4che.dict.Tags;

import org.dcm4che.dict.VRMap;

import org.dcm4che.media.DirBuilder;

import org.dcm4che.media.DirBuilderFactory;

import org.dcm4che.media.DirBuilderPref;

import org.dcm4che.media.DirReader;

import org.dcm4che.media.DirRecord;

import org.dcm4che.media.DirWriter;

import org.dcm4che.util.UIDGenerator;



/**

 * @author    gunter.zeilinger@tiani.com

 * @version $Revision: 11418 $ $Date: 2009-05-15 07:54:22 -0400 (Fri, 15 May 2009) $

 * @since     May 10, 2003

 */

public class DcmDir {


