package org.yccheok.jstock.gui;

import com.thoughtworks.xstream.XStream;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.yccheok.jstock.engine.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.TimeTCPClient;
import org.yccheok.jstock.analysis.Connection;
import org.yccheok.jstock.analysis.DoubleConstantOperator;
import org.yccheok.jstock.analysis.EqualityOperator;
import org.yccheok.jstock.analysis.Indicator;
import org.yccheok.jstock.analysis.OperatorIndicator;
import org.yccheok.jstock.analysis.SinkOperator;
import org.yccheok.jstock.analysis.StockOperator;
import org.yccheok.jstock.internationalization.MessagesBundle;
import org.yccheok.jstock.network.Utils.Type;

/**
 *
 * @author yccheok
 */
public class Utils {

    /** Creates a new instance of Utils */
    private Utils() {
    }

    /**
     * Restart the application.
     *
     * There are some important aspects to have in mind for this code:
     * + The application's main class must be in a jar file. mainFrame
     *   must be an instance of any class inside the same jar file (could be the
     *   main class too).
     * + The called java VM will be the same that the application is currently
     *   running on.
     * + There is no special error checking: the java VM may return an error like
     *   class not found or jar not found, and it will not be caught by the code
     *   posted above.
     *
     * The function will never return if it doesn't catch an error. It would be
     * a good practice to close all the handlers that could conflict with the
     * 'duplicate' new application before calling restartApplication(). There
     * will be a small time (which depends on many factors) where both
     * applications will be running at the same time.
     *
     * @param mainFrame One and only one mainFrame
     * @return true if restart success
     */
    public static boolean restartApplication(MainFrame mainFrame) {
        String javaBin = System.getProperty("java.home") + "/bin/javaw";
        File jarFile;
        try {
            jarFile = new File(mainFrame.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (Exception e) {
            log.error(null, e);
            return false;
        }
        if (!jarFile.getName().endsWith(".jar") && !jarFile.getName().endsWith(".exe")) {
            return false;
        }
        String toExec[] = null;
        if (jarFile.getName().endsWith(".exe")) {
            toExec = new String[] { jarFile.getPath() };
        } else {
            toExec = new String[] { javaBin, "-jar", jarFile.getPath() };
        }
        mainFrame.save();
        try {
            Process p = Runtime.getRuntime().exec(toExec);
        } catch (Exception e) {
            log.error(null, e);
            return false;
        }
        mainFrame.setVisible(false);
        mainFrame.dispose();
        System.exit(0);
        return true;
    }

    public static java.util.Date getNTPDate() {
        List<String> hosts = getNTPServers();
        for (String host : hosts) {
            TimeTCPClient client = new TimeTCPClient();
            client.setDefaultTimeout(5000);
            try {
                client.connect(host);
                java.util.Date ntpDate = client.getDate();
                client.disconnect();
                if (ntpDate != null) {
                    return ntpDate;
                }
            } catch (java.net.SocketException exp) {
                log.error(host, exp);
            } catch (java.io.IOException exp) {
                log.error(host, exp);
            }
        }
        return null;
    }

    public static boolean extractZipFile(String zipFilePath, boolean overwrite) {
        return extractZipFile(new File(zipFilePath), overwrite);
    }

    public static boolean extractZipFile(File zipFilePath, boolean overwrite) {
        InputStream inputStream = null;
        ZipInputStream zipInputStream = null;
        boolean status = true;
        try {
            inputStream = new FileInputStream(zipFilePath);
            zipInputStream = new ZipInputStream(inputStream);
            final byte[] data = new byte[1024];
            while (true) {
                ZipEntry zipEntry = null;
                FileOutputStream outputStream = null;
                try {
                    zipEntry = zipInputStream.getNextEntry();
                    if (zipEntry == null) break;
                    final String destination = Utils.getUserDataDirectory() + zipEntry.getName();
                    if (overwrite == false) {
                        if (Utils.isFileOrDirectoryExist(destination)) continue;
                    }
                    if (zipEntry.isDirectory()) {
                        Utils.createCompleteDirectoryHierarchyIfDoesNotExist(destination);
                    } else {
                        final File file = new File(destination);
                        Utils.createCompleteDirectoryHierarchyIfDoesNotExist(file.getParentFile());
                        int size = zipInputStream.read(data);
                        if (size > 0) {
                            outputStream = new FileOutputStream(destination);
                            do {
                                outputStream.write(data, 0, size);
                                size = zipInputStream.read(data);
                            } while (size >= 0);
                        }
                    }
                } catch (IOException exp) {
                    log.error(null, exp);
                    status = false;
                    break;
                } finally {
                    close(outputStream);
                    closeEntry(zipInputStream);
                }
            }
        } catch (IOException exp) {
            log.error(null, exp);
            status = false;
        } finally {
            close(zipInputStream);
            close(inputStream);
        }
        return status;
    }

    public static String getUUIDValue(String url, String key) {
        final org.yccheok.jstock.gui.Utils.InputStreamAndMethod inputStreamAndMethod = org.yccheok.jstock.gui.Utils.getResponseBodyAsStreamBasedOnProxyAuthOption(url);
        if (inputStreamAndMethod.inputStream == null) {
            inputStreamAndMethod.method.releaseConnection();
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStreamAndMethod.inputStream);
        } catch (IOException exp) {
            log.error(null, exp);
            return null;
        } catch (IllegalArgumentException exp) {
            log.error(null, exp);
            return null;
        } finally {
            close(inputStreamAndMethod.inputStream);
            inputStreamAndMethod.method.releaseConnection();
        }
        final String _id = properties.getProperty("id");
        if (_id == null) {
            log.info("UUID not found");
            return null;
        }
        final String id = org.yccheok.jstock.gui.Utils.decrypt(_id);
        if (id.equals(org.yccheok.jstock.gui.Utils.getJStockUUID()) == false) {
            log.info("UUID doesn't match");
            return null;
        }
        final String value = properties.getProperty(key);
        if (value == null) {
            log.info("Value not found");
            return null;
        }
        return value;
    }

    public static Map<String, String> getUUIDValue(String url) {
        Map<String, String> map = new HashMap<String, String>();
        final org.yccheok.jstock.gui.Utils.InputStreamAndMethod inputStreamAndMethod = org.yccheok.jstock.gui.Utils.getResponseBodyAsStreamBasedOnProxyAuthOption(url);
        if (inputStreamAndMethod.inputStream == null) {
            inputStreamAndMethod.method.releaseConnection();
            return java.util.Collections.emptyMap();
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStreamAndMethod.inputStream);
        } catch (IOException exp) {
            log.error(null, exp);
            return java.util.Collections.emptyMap();
        } catch (IllegalArgumentException exp) {
            log.error(null, exp);
            return java.util.Collections.emptyMap();
        } finally {
            close(inputStreamAndMethod.inputStream);
            inputStreamAndMethod.method.releaseConnection();
        }
        final String _id = properties.getProperty("id");
        if (_id == null) {
            log.info("UUID not found");
            return java.util.Collections.emptyMap();
        }
        final String id = org.yccheok.jstock.gui.Utils.decrypt(_id);
        if (id.equals(org.yccheok.jstock.gui.Utils.getJStockUUID()) == false) {
            log.info("UUID doesn't match");
            return java.util.Collections.emptyMap();
        }
        for (Object key : properties.keySet()) {
            if (key != null) {
                map.put(key.toString(), properties.getProperty(key.toString()));
            }
        }
        return map;
    }

    /**
     * Returns <code>ZipEntry</code> which is usable in both Linux and Windows.
     *
     * @param zipEntryName zip entry name
     * @return <code>ZipEntry</code> which is usable in both Linux and Windows
     */
    public static ZipEntry getZipEntry(String zipEntryName) {
        return new ZipEntry(zipEntryName.replace(File.separator, "/"));
    }

    private static List<String> getNTPServers() {
        final List<String> defaultServer = java.util.Collections.unmodifiableList(java.util.Arrays.asList("time-a.nist.gov", "time-b.nist.gov", "time-nw.nist.gov"));
        List<String> servers = Utils.NTPServers;
        if (servers != null) {
            return servers;
        }
        final String server = getUUIDValue(org.yccheok.jstock.network.Utils.getURL(Type.NTP_SERVER_TXT), "server");
        if (server != null) {
            String[] s = server.split(",");
            if (s.length > 0) {
                List<String> me = java.util.Collections.unmodifiableList(java.util.Arrays.asList(s));
                Utils.NTPServers = me;
                return me;
            }
        }
        Utils.NTPServers = defaultServer;
        return defaultServer;
    }

    public static void launchWebBrowser(String address) {
        if (Desktop.isDesktopSupported()) {
            final Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                URL url = null;
                String string = address;
                try {
                    url = new URL(string);
                } catch (MalformedURLException ex) {
                    return;
                }
                try {
                    desktop.browse(url.toURI());
                } catch (URISyntaxException ex) {
                } catch (IOException ex) {
                }
            }
        }
    }

    public static void launchWebBrowser(javax.swing.event.HyperlinkEvent evt) {
        if (HyperlinkEvent.EventType.ACTIVATED.equals(evt.getEventType())) {
            URL url = evt.getURL();
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    if (url == null) {
                        String string = "http://" + evt.getDescription();
                        try {
                            url = new URL(string);
                        } catch (MalformedURLException ex) {
                            return;
                        }
                    }
                    try {
                        desktop.browse(url.toURI());
                    } catch (URISyntaxException ex) {
                    } catch (IOException ex) {
                    }
                }
            }
        }
    }

    public static java.awt.Image getScaledImage(Image image, int maxWidth, int maxHeight) {
        image = new ImageIcon(image).getImage();
        final int imgWidth = image.getWidth(null);
        final int imgHeight = image.getHeight(null);
        final int preferredWidth = Math.min(imgWidth, maxWidth);
        final int preferredHeight = Math.min(imgHeight, maxHeight);
        final double scaleX = (double) preferredWidth / (double) imgWidth;
        final double scaleY = (double) preferredHeight / (double) imgHeight;
        final double bestScale = Math.min(scaleX, scaleY);
        return image.getScaledInstance((int) ((double) imgWidth * bestScale), (int) ((double) imgHeight * bestScale), Image.SCALE_SMOOTH);
    }

    private static boolean hasAlpha(Image image) {
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        boolean hasAlpha = hasAlpha(image);
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    public static boolean deleteDir(File dir, boolean deleteRoot) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]), true);
                if (!success) {
                    return false;
                }
            }
        }
        if (deleteRoot) {
            return dir.delete();
        }
        return true;
    }

    public static boolean createCompleteDirectoryHierarchyIfDoesNotExist(String directory) {
        return createCompleteDirectoryHierarchyIfDoesNotExist(new File(directory));
    }

    private static boolean createCompleteDirectoryHierarchyIfDoesNotExist(File f) {
        if (f == null) return true;
        if (false == createCompleteDirectoryHierarchyIfDoesNotExist(f.getParentFile())) {
            return false;
        }
        String path = null;
        try {
            path = f.getCanonicalPath();
        } catch (IOException ex) {
            log.error(null, ex);
            return false;
        }
        return createDirectoryIfDoesNotExist(path);
    }

    public static boolean isFileOrDirectoryExist(String fileOrDirectory) {
        java.io.File f = new java.io.File(fileOrDirectory);
        return f.exists();
    }

    public static boolean createDirectoryIfDoesNotExist(String directory) {
        java.io.File f = new java.io.File(directory);
        if (f.exists() == false) {
            if (f.mkdir()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static String getUserDataDirectory() {
        return System.getProperty("user.home") + File.separator + ".jstock" + File.separator + getApplicationVersionString() + File.separator;
    }

    public static AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));
    }

    public static boolean migrateFrom104jTo105() {
        final File oldDirectory = new File(System.getProperty("user.home") + File.separator + ".jstock" + File.separator + "1.0.4" + File.separator);
        final File newDirectory = new File(getUserDataDirectory());
        if (newDirectory.isDirectory() && newDirectory.exists()) {
            return true;
        }
        if (oldDirectory.isDirectory() == false || oldDirectory.exists() == false) {
            return true;
        }
        boolean success = oldDirectory.renameTo(newDirectory);
        if (!success) {
            return false;
        }
        final File indicatorDirectory = new File(getUserDataDirectory() + "indicator" + File.separator);
        final boolean status = deleteDir(indicatorDirectory, true);
        return status;
    }

    public static Color getColor(double price, double referencePrice) {
        final boolean reverse = org.yccheok.jstock.engine.Utils.isFallBelowAndRiseAboveColorReverse();
        if (price < referencePrice) {
            if (reverse) {
                return JStockOptions.DEFAULT_HIGHER_NUMERICAL_VALUE_FOREGROUND_COLOR;
            } else {
                return JStockOptions.DEFAULT_LOWER_NUMERICAL_VALUE_FOREGROUND_COLOR;
            }
        }
        if (price > referencePrice) {
            if (reverse) {
                return JStockOptions.DEFAULT_LOWER_NUMERICAL_VALUE_FOREGROUND_COLOR;
            } else {
                return JStockOptions.DEFAULT_HIGHER_NUMERICAL_VALUE_FOREGROUND_COLOR;
            }
        }
        return JStockOptions.DEFAULT_NORMAL_TEXT_FOREGROUND_COLOR;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static Stock getEmptyStock(Code code, Symbol symbol) {
        return new Stock(code, symbol, "", Stock.Board.Unknown, Stock.Industry.Unknown, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, 0.0, 0, Calendar.getInstance());
    }

    public static void deleteAllOldFiles(File dir, int days) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteAllOldFiles(new File(dir, children[i]), days);
            }
            if (dir.list().length == 0) {
                dir.delete();
            }
        } else {
            final long today = System.currentTimeMillis();
            final long timeStamp = dir.lastModified();
            final long difMil = today - timeStamp;
            final long milPerDay = 1000 * 60 * 60 * 24;
            final long d = difMil / milPerDay;
            if (d >= days) {
                dir.delete();
            }
        }
    }

    public static String getApplicationVersionString() {
        return APPLICATION_VERSION_STRING;
    }

    public static String encrypt(String source) {
        if (source.length() <= 0) return "";
        org.jasypt.encryption.pbe.PBEStringEncryptor pbeStringEncryptor = new org.jasypt.encryption.pbe.StandardPBEStringEncryptor();
        pbeStringEncryptor.setPassword(getJStockUUID());
        return pbeStringEncryptor.encrypt(source);
    }

    public static String decrypt(String source) {
        if (source.length() <= 0) {
            return "";
        }
        org.jasypt.encryption.pbe.PBEStringEncryptor pbeStringEncryptor = new org.jasypt.encryption.pbe.StandardPBEStringEncryptor();
        pbeStringEncryptor.setPassword(getJStockUUID());
        try {
            return pbeStringEncryptor.decrypt(source);
        } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException exp) {
            log.error(null, exp);
        }
        return "";
    }

    public static String getJStockUUID() {
        return "fe78440e-e0fe-4efb-881d-264a01be483c";
    }

    public static boolean isWindows() {
        String windowsString = "Windows";
        String osName = System.getProperty("os.name");
        if (osName == null) return false;
        return osName.regionMatches(true, 0, windowsString, 0, windowsString.length());
    }

    public static Executor getZoombiePool() {
        return zombiePool;
    }

    public static Indicator getLastPriceRiseAboveIndicator(double lastPrice) {
        final StockOperator stockOperator = new StockOperator();
        stockOperator.setType(StockOperator.Type.LastPrice);
        final DoubleConstantOperator doubleConstantOperator = new DoubleConstantOperator();
        doubleConstantOperator.setConstant(lastPrice);
        final EqualityOperator equalityOperator = new EqualityOperator();
        equalityOperator.setEquality(EqualityOperator.Equality.GreaterOrEqual);
        final SinkOperator sinkOperator = new SinkOperator();
        final Connection stockToEqualityConnection = new Connection();
        final Connection doubleConstantToEqualityConnection = new Connection();
        final Connection equalityToSinkConnection = new Connection();
        stockOperator.addOutputConnection(stockToEqualityConnection, 0);
        equalityOperator.addInputConnection(stockToEqualityConnection, 0);
        doubleConstantOperator.addOutputConnection(doubleConstantToEqualityConnection, 0);
        equalityOperator.addInputConnection(doubleConstantToEqualityConnection, 1);
        equalityOperator.addOutputConnection(equalityToSinkConnection, 0);
        sinkOperator.addInputConnection(equalityToSinkConnection, 0);
        final OperatorIndicator operatorIndicator = new OperatorIndicator();
        operatorIndicator.setName("RiseAbove");
        operatorIndicator.add(stockOperator);
        operatorIndicator.add(doubleConstantOperator);
        operatorIndicator.add(equalityOperator);
        operatorIndicator.add(sinkOperator);
        assert (operatorIndicator.getType() == OperatorIndicator.Type.AlertIndicator);
        operatorIndicator.preCalculate();
        return operatorIndicator;
    }

    public static Indicator getLastPriceFallBelowIndicator(double lastPrice) {
        final StockOperator stockOperator = new StockOperator();
        stockOperator.setType(StockOperator.Type.LastPrice);
        final DoubleConstantOperator doubleConstantOperator = new DoubleConstantOperator();
        doubleConstantOperator.setConstant(lastPrice);
        final EqualityOperator equalityOperator = new EqualityOperator();
        equalityOperator.setEquality(EqualityOperator.Equality.LesserOrEqual);
        final SinkOperator sinkOperator = new SinkOperator();
        final Connection stockToEqualityConnection = new Connection();
        final Connection doubleConstantToEqualityConnection = new Connection();
        final Connection equalityToSinkConnection = new Connection();
        stockOperator.addOutputConnection(stockToEqualityConnection, 0);
        equalityOperator.addInputConnection(stockToEqualityConnection, 0);
        doubleConstantOperator.addOutputConnection(doubleConstantToEqualityConnection, 0);
        equalityOperator.addInputConnection(doubleConstantToEqualityConnection, 1);
        equalityOperator.addOutputConnection(equalityToSinkConnection, 0);
        sinkOperator.addInputConnection(equalityToSinkConnection, 0);
        final OperatorIndicator operatorIndicator = new OperatorIndicator();
        operatorIndicator.setName("FallBelow");
        operatorIndicator.add(stockOperator);
        operatorIndicator.add(doubleConstantOperator);
        operatorIndicator.add(equalityOperator);
        operatorIndicator.add(sinkOperator);
        assert (operatorIndicator.getType() == OperatorIndicator.Type.AlertIndicator);
        operatorIndicator.preCalculate();
        return operatorIndicator;
    }

    public static void setDefaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (java.lang.ClassNotFoundException exp) {
            log.error(null, exp);
        } catch (java.lang.InstantiationException exp) {
            log.error(null, exp);
        } catch (java.lang.IllegalAccessException exp) {
            log.error(null, exp);
        } catch (javax.swing.UnsupportedLookAndFeelException exp) {
            log.error(null, exp);
        }
    }

    public static class CloudFile {

        public final File file;

        public final long checksum;

        public final long date;

        public final int version;

        private CloudFile(File file, long checksum, long date, int version) {
            this.file = file;
            this.checksum = checksum;
            this.date = date;
            this.version = version;
        }

        public static CloudFile newInstance(File file, long checksum, long date, int version) {
            return new CloudFile(file, checksum, date, version);
        }
    }

    public static CloudFile loadFromCloud(String username, String password) {
        CaptchaRespond captchaRespond = null;
        do {
            final String url = "https://jstock-cloud.appspot.com/DownloadServlet";
            final PostMethod post = new PostMethod(url);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                org.yccheok.jstock.engine.Utils.setHttpClientProxyFromSystemProperties(httpClient);
                org.yccheok.jstock.gui.Utils.setHttpClientProxyCredentialsFromJStockOptions(httpClient);
                NameValuePair[] data = null;
                if (captchaRespond == null) {
                    data = new NameValuePair[] { new NameValuePair("Email", username), new NameValuePair("Passwd", password) };
                } else {
                    data = new NameValuePair[] { new NameValuePair("Email", username), new NameValuePair("Passwd", password), new NameValuePair("logintoken", captchaRespond.logintoken), new NameValuePair("logincaptcha", captchaRespond.logincaptcha) };
                }
                post.setRequestBody(data);
                httpClient.executeMethod(post);
                final Header header = post.getResponseHeader("Content-Type");
                if (header == null || header.getValue() == null) {
                    return null;
                }
                if (true == header.getValue().contains("text/plain")) {
                    final String respond = post.getResponseBodyAsString();
                    if (respond == null) {
                        return null;
                    }
                    captchaRespond = Utils.getCapchaRespond(respond);
                    if (captchaRespond == null) {
                        return null;
                    }
                    continue;
                }
                if (false == header.getValue().equalsIgnoreCase("application/octet-stream")) {
                    return null;
                }
                String _checksum = post.getResponseHeader("jstock-custom-checksum").getValue();
                String _date = post.getResponseHeader("jstock-custom-date").getValue();
                String _version = post.getResponseHeader("jstock-custom-version").getValue();
                if (_checksum == null || _date == null || _version == null) {
                    return null;
                }
                long checksum = Long.parseLong(_checksum);
                long date = Long.parseLong(_date);
                int version = Integer.parseInt(_version);
                inputStream = post.getResponseBodyAsStream();
                final File temp = File.createTempFile(Utils.getJStockUUID(), ".zip");
                temp.deleteOnExit();
                outputStream = new FileOutputStream(temp);
                byte buf[] = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                return CloudFile.newInstance(temp, checksum, date, version);
            } catch (FileNotFoundException ex) {
                log.error(null, ex);
                return null;
            } catch (IOException ex) {
                log.error(null, ex);
                return null;
            } catch (NumberFormatException ex) {
                log.error(null, ex);
                return null;
            } finally {
                close(outputStream);
                close(inputStream);
                post.releaseConnection();
            }
        } while (true);
    }

    private static class CaptchaRespond {

        public final String logintoken;

        public final String logincaptcha;

        public CaptchaRespond(String logintoken, String logincaptcha) {
            this.logintoken = logintoken;
            this.logincaptcha = logincaptcha;
        }
    }

    private static CaptchaRespond getCapchaRespond(String respond) {
        assert (respond != null);
        final String[] res = respond.split("\\r?\\n");
        final Map<String, String> map = new HashMap<String, String>();
        for (String r : res) {
            final String[] v = r.split("=", 2);
            if (v.length == 2) {
                v[0] = v[0].trim();
                v[1] = v[1].trim();
                if (v[0].length() == 0 || v[1].length() == 0) {
                    continue;
                }
                map.put(v[0], v[1]);
            }
        }
        if (map.containsKey("CaptchaToken") && map.containsKey("CaptchaUrl")) {
            final String CaptchaToken = map.get("CaptchaToken");
            final String CaptchaUrl = map.get("CaptchaUrl");
            try {
                URL url = new URL("https://www.google.com/accounts/" + CaptchaUrl);
                BufferedImage image = ImageIO.read(url);
                final CaptchaInputJDialog dialog = new CaptchaInputJDialog(MainFrame.getInstance(), image, true);
                dialog.setLocationRelativeTo(MainFrame.getInstance());
                dialog.setVisible(true);
                if (dialog.getCaptcha() == null || dialog.getCaptcha().length() <= 0) {
                    return null;
                }
                return new CaptchaRespond(CaptchaToken, dialog.getCaptcha());
            } catch (Exception exp) {
                log.error(null, exp);
                return null;
            }
        }
        return null;
    }

    public static boolean saveToCloud(String username, String password, File file) {
        CaptchaRespond captchaRespond = null;
        do {
            final String url = "https://jstock-cloud.appspot.com/UploadServlet";
            final PostMethod post = new PostMethod(url);
            try {
                org.yccheok.jstock.engine.Utils.setHttpClientProxyFromSystemProperties(httpClient);
                org.yccheok.jstock.gui.Utils.setHttpClientProxyCredentialsFromJStockOptions(httpClient);
                Part[] parts = null;
                if (captchaRespond == null) {
                    parts = new Part[] { new StringPart("Email", username), new StringPart("Passwd", password), new StringPart("Date", new Date().getTime() + ""), new StringPart("Checksum", org.yccheok.jstock.analysis.Utils.getChecksum(file) + ""), new StringPart("Version", org.yccheok.jstock.gui.Utils.getApplicationVersionID() + ""), new FilePart("file", file) };
                } else {
                    parts = new Part[] { new StringPart("Email", username), new StringPart("Passwd", password), new StringPart("Date", new Date().getTime() + ""), new StringPart("Checksum", org.yccheok.jstock.analysis.Utils.getChecksum(file) + ""), new StringPart("Version", org.yccheok.jstock.gui.Utils.getApplicationVersionID() + ""), new StringPart("logintoken", captchaRespond.logintoken), new StringPart("logincaptcha", captchaRespond.logincaptcha), new FilePart("file", file) };
                }
                post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
                httpClient.executeMethod(post);
                final String respond = post.getResponseBodyAsString();
                if (respond == null) {
                    return false;
                }
                if (respond.equals("OK")) {
                    return true;
                }
                captchaRespond = Utils.getCapchaRespond(respond);
                if (captchaRespond == null) {
                    return false;
                }
            } catch (FileNotFoundException ex) {
                log.error(null, ex);
                return false;
            } catch (IOException ex) {
                log.error(null, ex);
                return false;
            } finally {
                post.releaseConnection();
            }
        } while (true);
    }

    public static boolean isCompatible(int applicationVersionID) {
        if (applicationVersionID == APPLICATION_VERSION_ID) {
            return true;
        } else if (applicationVersionID >= 1051 && applicationVersionID <= 1071) {
            return true;
        }
        return false;
    }

    /**
     * Get response body through non-standard POST method.
     * Please refer to <url>http://stackoverflow.com/questions/1473255/is-jakarta-httpclient-sutitable-for-the-following-task/1473305#1473305</url>
     *
     * @param uri For example, http://X/%5bvUpJYKw4QvGRMBmhATUxRwv4JrU9aDnwNEuangVyy6OuHxi2YiY=%5dImage?
     * @param formData For example, [SORT]=0,1,0,10,5,0,KL,0&[FIELD]=33,38,51
     * @return the response body. null if fail.
     */
    public static String getPOSTResponseBodyAsStringBasedOnProxyAuthOption(String uri, String formData) {
        org.yccheok.jstock.engine.Utils.setHttpClientProxyFromSystemProperties(httpClient);
        org.yccheok.jstock.gui.Utils.setHttpClientProxyCredentialsFromJStockOptions(httpClient);
        final PostMethod method = new PostMethod(uri);
        final RequestEntity entity;
        try {
            entity = new StringRequestEntity(formData, "application/x-www-form-urlencoded", "UTF-8");
        } catch (UnsupportedEncodingException exp) {
            log.error(null, exp);
            return null;
        }
        method.setRequestEntity(entity);
        method.setContentChunked(false);
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        String respond = null;
        try {
            if (jStockOptions.isProxyAuthEnabled()) {
                method.setFollowRedirects(false);
                httpClient.executeMethod(method);
                int statuscode = method.getStatusCode();
                if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER) || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                    Header header = method.getResponseHeader("location");
                    HttpMethod RedirectMethod = new GetMethod(header.getValue());
                    method.releaseConnection();
                    try {
                        httpClient.executeMethod(RedirectMethod);
                        respond = RedirectMethod.getResponseBodyAsString();
                    } catch (HttpException exp) {
                        log.error(null, exp);
                        return null;
                    } catch (IOException exp) {
                        log.error(null, exp);
                        return null;
                    } finally {
                        RedirectMethod.releaseConnection();
                    }
                } else {
                    respond = method.getResponseBodyAsString();
                }
            } else {
                httpClient.executeMethod(method);
                respond = method.getResponseBodyAsString();
            }
        } catch (HttpException exp) {
            log.error(null, exp);
            return null;
        } catch (IOException exp) {
            log.error(null, exp);
            return null;
        } finally {
            method.releaseConnection();
        }
        return respond;
    }

    public static String getResponseBodyAsStringBasedOnProxyAuthOption(String request) {
        org.yccheok.jstock.engine.Utils.setHttpClientProxyFromSystemProperties(httpClient);
        org.yccheok.jstock.gui.Utils.setHttpClientProxyCredentialsFromJStockOptions(httpClient);
        final HttpMethod method = new GetMethod(request);
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        String respond = null;
        try {
            if (jStockOptions.isProxyAuthEnabled()) {
                method.setFollowRedirects(false);
                httpClient.executeMethod(method);
                int statuscode = method.getStatusCode();
                if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER) || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                    Header header = method.getResponseHeader("location");
                    HttpMethod RedirectMethod = new GetMethod(header.getValue());
                    method.releaseConnection();
                    try {
                        httpClient.executeMethod(RedirectMethod);
                        respond = RedirectMethod.getResponseBodyAsString();
                    } catch (HttpException exp) {
                        log.error(null, exp);
                        return null;
                    } catch (IOException exp) {
                        log.error(null, exp);
                        return null;
                    } finally {
                        RedirectMethod.releaseConnection();
                    }
                } else {
                    respond = method.getResponseBodyAsString();
                }
            } else {
                httpClient.executeMethod(method);
                respond = method.getResponseBodyAsString();
            }
        } catch (HttpException exp) {
            log.error(null, exp);
            return null;
        } catch (IOException exp) {
            log.error(null, exp);
            return null;
        } finally {
            method.releaseConnection();
        }
        return respond;
    }

    public static class InputStreamAndMethod {

        public final InputStream inputStream;

        public final HttpMethod method;

        public InputStreamAndMethod(InputStream inputStream, HttpMethod method) {
            this.inputStream = inputStream;
            this.method = method;
        }
    }

    public static InputStreamAndMethod getResponseBodyAsStreamBasedOnProxyAuthOption(String request) {
        org.yccheok.jstock.engine.Utils.setHttpClientProxyFromSystemProperties(httpClient);
        org.yccheok.jstock.gui.Utils.setHttpClientProxyCredentialsFromJStockOptions(httpClient);
        final GetMethod method = new GetMethod(request);
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        InputStreamAndMethod inputStreamAndMethod = null;
        InputStream respond = null;
        HttpMethod methodToClosed = method;
        try {
            if (jStockOptions.isProxyAuthEnabled()) {
                method.setFollowRedirects(false);
                httpClient.executeMethod(method);
                int statuscode = method.getStatusCode();
                if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER) || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                    Header header = method.getResponseHeader("location");
                    GetMethod RedirectMethod = new GetMethod(header.getValue());
                    methodToClosed = RedirectMethod;
                    method.releaseConnection();
                    try {
                        httpClient.executeMethod(RedirectMethod);
                        respond = RedirectMethod.getResponseBodyAsStream();
                    } catch (HttpException exp) {
                        log.error(null, exp);
                    } catch (IOException exp) {
                        log.error(null, exp);
                    }
                } else {
                    methodToClosed = method;
                    respond = method.getResponseBodyAsStream();
                }
            } else {
                methodToClosed = method;
                httpClient.executeMethod(method);
                respond = method.getResponseBodyAsStream();
            }
        } catch (HttpException exp) {
            log.error(null, exp);
        } catch (IOException exp) {
            log.error(null, exp);
        } finally {
            inputStreamAndMethod = new InputStreamAndMethod(respond, methodToClosed);
        }
        return inputStreamAndMethod;
    }

    private static void setHttpClientProxyCredentialsFromJStockOptions(HttpClient httpClient) {
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        if (jStockOptions.isProxyAuthEnabled() == false) {
            httpClient.getState().clearCredentials();
        } else {
            httpClient.getState().setProxyCredentials(AuthScope.ANY, jStockOptions.getCredentials());
        }
    }

    public static String getFileExtension(String s) {
        String ext = "";
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String getFileExtension(File f) {
        return getFileExtension(f.getName());
    }

    public static ApplicationInfo getLatestApplicationInfo() {
        final String request = org.yccheok.jstock.network.Utils.getURL(Type.VERSION_INFORMATION_TXT);
        final org.yccheok.jstock.gui.Utils.InputStreamAndMethod inputStreamAndMethod = org.yccheok.jstock.gui.Utils.getResponseBodyAsStreamBasedOnProxyAuthOption(request);
        if (inputStreamAndMethod.inputStream == null) {
            inputStreamAndMethod.method.releaseConnection();
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStreamAndMethod.inputStream);
        } catch (IOException exp) {
            log.error(null, exp);
            return null;
        } catch (IllegalArgumentException exp) {
            log.error(null, exp);
            return null;
        } finally {
            close(inputStreamAndMethod.inputStream);
            inputStreamAndMethod.method.releaseConnection();
        }
        final String applicationVersionID = properties.getProperty("applicationVersionID");
        final String windowsDownloadLink = properties.getProperty("windowsDownloadLink");
        final String linuxDownloadLink = properties.getProperty("linuxDownloadLink");
        final String macDownloadLink = properties.getProperty("macDownloadLink");
        final String solarisDownloadLink = properties.getProperty("solarisDownloadLink");
        if (applicationVersionID == null || windowsDownloadLink == null || linuxDownloadLink == null || macDownloadLink == null || solarisDownloadLink == null) {
            return null;
        }
        final int version;
        try {
            version = Integer.parseInt(applicationVersionID);
        } catch (NumberFormatException exp) {
            log.error(null, exp);
            return null;
        }
        return new ApplicationInfo(version, windowsDownloadLink, linuxDownloadLink, macDownloadLink, solarisDownloadLink);
    }

    public static int getApplicationVersionID() {
        return Utils.APPLICATION_VERSION_ID;
    }

    public static String toHTML(String plainText) {
        plainText = plainText.replace(System.getProperty("line.separator"), "<br>");
        return "<html><head></head><body>" + plainText + "</body></html>";
    }

    @SuppressWarnings("unchecked")
    public static <A> A fromXML(Class c, Reader reader) {
        XStream xStream = new XStream();
        try {
            Object object = xStream.fromXML(reader);
            if (c.isInstance(object)) {
                return (A) object;
            }
        } catch (Exception exp) {
            log.error(null, exp);
        } finally {
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <A> A fromXML(Class c, File file) {
        XStream xStream = new XStream();
        InputStream inputStream = null;
        Reader reader = null;
        try {
            inputStream = new java.io.FileInputStream(file);
            reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            Object object = xStream.fromXML(reader);
            if (c.isInstance(object)) {
                return (A) object;
            }
        } catch (Exception exp) {
            log.error(null, exp);
        } finally {
            close(reader);
            close(inputStream);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <A> A fromXML(Class c, String filePath) {
        return (A) fromXML(c, new File(filePath));
    }

    public static boolean toXML(Object object, File file) {
        XStream xStream = new XStream();
        OutputStream outputStream = null;
        Writer writer = null;
        try {
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
            xStream.toXML(object, writer);
        } catch (Exception exp) {
            log.error(null, exp);
            return false;
        } finally {
            close(writer);
            close(outputStream);
        }
        return true;
    }

    public static boolean toXML(Object object, String filePath) {
        return toXML(object, new File(filePath));
    }

    public static String getExtraDataDirectory() {
        return org.yccheok.jstock.gui.Utils.getUserDataDirectory() + "extra" + File.separator;
    }

    public static String toHTMLFileSrcFormat(String fileName) {
        try {
            return new File(fileName).toURI().toURL().toString();
        } catch (MalformedURLException ex) {
            log.error(null, ex);
        }
        return "file:" + fileName;
    }

    public static class FileEx {

        public final File file;

        public final org.yccheok.jstock.file.Statement.Type type;

        public FileEx(File file, org.yccheok.jstock.file.Statement.Type type) {
            this.file = file;
            this.type = type;
        }
    }

    public static FileEx promptSavePortfolioCSVAndExcelJFileChooser(final String suggestedFileName) {
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        final JFileChooser chooser = new JFileChooser(jStockOptions.getLastFileIODirectory());
        final FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Documents (*.csv)", "csv");
        final FileNameExtensionFilter xlsFilter = new FileNameExtensionFilter("Microsoft Excel (*.xls)", "xls");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(csvFilter);
        chooser.addChoosableFileFilter(xlsFilter);
        final org.yccheok.jstock.gui.file.PortfolioSelectionJPanel portfolioSelectionJPanel = new org.yccheok.jstock.gui.file.PortfolioSelectionJPanel();
        chooser.setAccessory(portfolioSelectionJPanel);
        chooser.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                final boolean flag = ((FileNameExtensionFilter) evt.getNewValue()).equals(csvFilter);
                portfolioSelectionJPanel.setEnabled(flag);
                chooser.setSelectedFile(chooser.getFileFilter().getDescription().equals(csvFilter.getDescription()) ? new File(portfolioSelectionJPanel.getSuggestedFileName()) : new File(suggestedFileName));
            }
        });
        portfolioSelectionJPanel.addJRadioButtonsActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                chooser.setSelectedFile(new File(portfolioSelectionJPanel.getSuggestedFileName()));
            }
        });
        final java.util.Map<String, FileNameExtensionFilter> map = new HashMap<String, FileNameExtensionFilter>();
        map.put(csvFilter.getDescription(), csvFilter);
        map.put(xlsFilter.getDescription(), xlsFilter);
        final FileNameExtensionFilter filter = map.get(jStockOptions.getLastSavedFileNameExtensionDescription());
        if (filter != null) {
            chooser.setFileFilter(filter);
        }
        portfolioSelectionJPanel.setEnabled(chooser.getFileFilter().getDescription().equals(csvFilter.getDescription()));
        chooser.setSelectedFile(chooser.getFileFilter().getDescription().equals(csvFilter.getDescription()) ? new File(portfolioSelectionJPanel.getSuggestedFileName()) : new File(suggestedFileName));
        while (true) {
            final int returnVal = chooser.showSaveDialog(MainFrame.getInstance());
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return null;
            }
            File file = chooser.getSelectedFile();
            if (file == null) {
                return null;
            }
            final String extension = Utils.getFileExtension(file);
            if (extension.equals("csv") == false && extension.equals("xls") == false) {
                if (chooser.getFileFilter().getDescription().equals(csvFilter.getDescription())) {
                    try {
                        file = new File(file.getCanonicalPath() + ".csv");
                    } catch (IOException ex) {
                        log.error(null, ex);
                    }
                } else if (chooser.getFileFilter().getDescription().equals(xlsFilter.getDescription())) {
                    try {
                        file = new File(file.getCanonicalPath() + ".xls");
                    } catch (IOException ex) {
                        log.error(null, ex);
                    }
                } else {
                    return null;
                }
            }
            if (file.exists()) {
                final String output = MessageFormat.format(MessagesBundle.getString("question_message_replace_old_template"), file.getName());
                final int result = javax.swing.JOptionPane.showConfirmDialog(MainFrame.getInstance(), output, MessagesBundle.getString("question_title_replace_old"), javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
                if (result != javax.swing.JOptionPane.YES_OPTION) {
                    continue;
                }
            }
            final String parent = chooser.getSelectedFile().getParent();
            if (parent != null) {
                jStockOptions.setLastFileIODirectory(parent);
            }
            if (Utils.getFileExtension(file).equals("csv")) {
                jStockOptions.setLastFileNameExtensionDescription(csvFilter.getDescription());
            } else if (Utils.getFileExtension(file).equals("xls")) {
                jStockOptions.setLastFileNameExtensionDescription(xlsFilter.getDescription());
            } else {
                return null;
            }
            return new FileEx(file, portfolioSelectionJPanel.getType());
        }
    }

    public static JRadioButton getSelection(ButtonGroup group) {
        for (Enumeration e = group.getElements(); e.hasMoreElements(); ) {
            JRadioButton b = (JRadioButton) e.nextElement();
            if (b.getModel() == group.getSelection()) {
                return b;
            }
        }
        return null;
    }

    private static File promptOpenJFileChooser(FileNameExtensionFilter... fileNameExtensionFilters) {
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        final JFileChooser chooser = new JFileChooser(jStockOptions.getLastFileIODirectory());
        chooser.setAcceptAllFileFilterUsed(false);
        for (FileNameExtensionFilter fileNameExtensionFilter : fileNameExtensionFilters) {
            chooser.addChoosableFileFilter(fileNameExtensionFilter);
        }
        final java.util.Map<String, FileNameExtensionFilter> map = new HashMap<String, FileNameExtensionFilter>();
        for (FileNameExtensionFilter fileNameExtensionFilter : fileNameExtensionFilters) {
            map.put(fileNameExtensionFilter.getDescription(), fileNameExtensionFilter);
        }
        final FileNameExtensionFilter filter = map.get(jStockOptions.getLastSavedFileNameExtensionDescription());
        if (filter != null) {
            chooser.setFileFilter(filter);
        }
        int returnVal = chooser.showOpenDialog(MainFrame.getInstance());
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = chooser.getSelectedFile();
        if (file == null || !file.exists()) {
            return null;
        }
        final String parent = chooser.getSelectedFile().getParent();
        if (parent != null) {
            jStockOptions.setLastFileIODirectory(parent);
        }
        final String extension = Utils.getFileExtension(file);
        for (FileNameExtensionFilter fileNameExtensionFilter : fileNameExtensionFilters) {
            final String[] extensions = fileNameExtensionFilter.getExtensions();
            if (extensions.length <= 0) {
                continue;
            }
            if (extension.equals(extensions[0])) {
                jStockOptions.setLastFileNameExtensionDescription(fileNameExtensionFilter.getDescription());
                return file;
            }
        }
        return null;
    }

    public static void playAlertSound() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    clip.addLineListener(new LineListener() {

                        @Override
                        public void update(LineEvent event) {
                            if (event.getType() == LineEvent.Type.STOP) {
                                event.getLine().close();
                            }
                        }
                    });
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(Utils.class.getResourceAsStream("/sounds/doorbell.wav"));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    log.error(null, e);
                }
            }
        }).start();
    }

    public static File promptOpenCSVAndExcelJFileChooser() {
        final FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Documents (*.csv)", "csv");
        final FileNameExtensionFilter xlsFilter = new FileNameExtensionFilter("Microsoft Excel (*.xls)", "xls");
        return promptOpenJFileChooser(csvFilter, xlsFilter);
    }

    public static File promptOpenZippedJFileChooser() {
        final FileNameExtensionFilter zippedFilter = new FileNameExtensionFilter("Zipped Files (*.zip)", "zip");
        return promptOpenJFileChooser(zippedFilter);
    }

    public static String stockPriceDecimalFormat(Object value) {
        final DecimalFormat decimalFormat = new DecimalFormat("0.00#");
        return decimalFormat.format(value);
    }

    public static String stockPriceDecimalFormat(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00#");
        return decimalFormat.format(value);
    }

    private static File promptSaveJFileChooser(String suggestedFileName, FileNameExtensionFilter... fileNameExtensionFilters) {
        final JStockOptions jStockOptions = MainFrame.getInstance().getJStockOptions();
        final JFileChooser chooser = new JFileChooser(jStockOptions.getLastFileIODirectory());
        chooser.setAcceptAllFileFilterUsed(false);
        for (FileNameExtensionFilter fileNameExtensionFilter : fileNameExtensionFilters) {
            chooser.addChoosableFileFilter(fileNameExtensionFilter);
        }
        chooser.setSelectedFile(new File(suggestedFileName));
        final java.util.Map<String, FileNameExtensionFilter> map = new HashMap<String, FileNameExtensionFilter>();
        for (FileNameExtensionFilter fileNameExtensionFilter : fileNameExtensionFilters) {
            map.put(fileNameExtensionFilter.getDescription(), fileNameExtensionFilter);
        }
        final FileNameExtensionFilter filter = map.get(jStockOptions.getLastSavedFileNameExtensionDescription());
        if (filter != null) {
            chooser.setFileFilter(filter);
        }
        while (true) {
            final int returnVal = chooser.showSaveDialog(MainFrame.getInstance());
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return null;
            }
            File file = chooser.getSelectedFile();
            if (file == null) {
                return null;
            }
            final String extension = Utils.getFileExtension(file);
            boolean found = false;
            root: for (FileNameExtensionFilter fileNameExtensionFilter : fileNameExtensionFilters) {
                String[] extensions = fileNameExtensionFilter.getExtensions();
                for (String e : extensions) {
                    if (e.equals(extension)) {
                        found = true;
                        break root;
                    }
                }
            }
            if (!found) {
                for (FileNameExtensionFilter fileNameExtensionFilter : fileNameExtensionFilters) {
                    String[] extensions = fileNameExtensionFilter.getExtensions();
                    if (extensions.length <= 0) {
                        continue;
                    }
                    final String e = extensions[0];
                    if (chooser.getFileFilter().getDescription().equals(fileNameExtensionFilter.getDescription())) {
                        try {
                            if (e.startsWith(".")) {
                                file = new File(file.getCanonicalPath() + e);
                            } else {
                                file = new File(file.getCanonicalPath() + "." + e);
                            }
                        } catch (IOException ex) {
                            log.error(null, ex);
                        }
                        break;
                    }
                }
            }
            if (file.exists()) {
                final String output = MessageFormat.format(MessagesBundle.getString("question_message_replace_old_template"), file.getName());
                final int result = javax.swing.JOptionPane.showConfirmDialog(MainFrame.getInstance(), output, MessagesBundle.getString("question_title_replace_old"), javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
                if (result != javax.swing.JOptionPane.YES_OPTION) {
                    continue;
                }
            }
            final String parent = chooser.getSelectedFile().getParent();
            if (parent != null) {
                jStockOptions.setLastFileIODirectory(parent);
            }
            final String e = Utils.getFileExtension(file);
            for (FileNameExtensionFilter fileNameExtensionFilter : fileNameExtensionFilters) {
                String[] extensions = fileNameExtensionFilter.getExtensions();
                if (extensions.length <= 0) {
                    continue;
                }
                if (e.equals(extensions[0])) {
                    jStockOptions.setLastFileNameExtensionDescription(fileNameExtensionFilter.getDescription());
                    break;
                }
            }
            return file;
        }
    }

    public static File promptSaveZippedJFileChooser(String suggestedFileName) {
        final FileNameExtensionFilter zippedFilter = new FileNameExtensionFilter("Zipped Files (*.zip)", "zip");
        return promptSaveJFileChooser(suggestedFileName, zippedFilter);
    }

    /**
     * Get a new bold version of specified font, with rest of specified font
     * attributes remained the same.
     * 
     * @param font specified font
     * @return a new bold version of specified font
     */
    public static Font getBoldFont(Font font) {
        return font.deriveFont(font.getStyle() | Font.BOLD);
    }

    public static File promptSaveCSVAndExcelJFileChooser(String suggestedFileName) {
        final FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Documents (*.csv)", "csv");
        final FileNameExtensionFilter xlsFilter = new FileNameExtensionFilter("Microsoft Excel (*.xls)", "xls");
        return promptSaveJFileChooser(suggestedFileName, csvFilter, xlsFilter);
    }

    /**
     * Performs close operation on ZIP output stream, without the need of
     * writing cumbersome try...catch block.
     *
     * @param zipOutputStream The ZIP input stream.
     * @return Returns false if there is an exception during close operation.
     * Otherwise returns true.
     */
    public static boolean closeEntry(ZipOutputStream zipOutputStream) {
        if (null != zipOutputStream) {
            try {
                zipOutputStream.closeEntry();
            } catch (IOException ex) {
                log.error(null, ex);
                return false;
            }
        }
        return true;
    }

    /**
     * Performs close operation on ZIP input stream, without the need of
     * writing cumbersome try...catch block.
     *
     * @param zipInputStream The ZIP input stream.
     */
    public static void closeEntry(ZipInputStream zipInputStream) {
        if (null != zipInputStream) {
            try {
                zipInputStream.closeEntry();
            } catch (IOException ex) {
                log.error(null, ex);
            }
        }
    }

    /**
     * Performs close operation on Closeable stream, without the need of
     * writing cumbersome try...catch block.
     *
     * @param closeable The closeable stream.
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException ex) {
                log.error(null, ex);
            }
        }
    }

    /**
     * Performs download and save the download as temporary file.
     * 
     * @param location Download URL location
     * @return The saved temporary file if download success. <code>null</code>
     * if failed.
     */
    public static File downloadAsTempFile(String location) {
        final Utils.InputStreamAndMethod inputStreamAndMethod = Utils.getResponseBodyAsStreamBasedOnProxyAuthOption(location);
        if (inputStreamAndMethod.inputStream == null) {
            inputStreamAndMethod.method.releaseConnection();
            return null;
        }
        OutputStream out = null;
        File temp = null;
        try {
            temp = File.createTempFile(Utils.getJStockUUID(), null);
            temp.deleteOnExit();
            out = new FileOutputStream(temp);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStreamAndMethod.inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return temp;
        } catch (IOException ex) {
            log.error(null, ex);
        } finally {
            close(out);
            close(inputStreamAndMethod.inputStream);
            inputStreamAndMethod.method.releaseConnection();
        }
        return null;
    }

    /**
     * Returns list of Han Yu Pin Yin's prefix of every characters. If the
     * character is an alphabet or numerical, the original character will be
     * used. If there is any error occur during conversion, that particular
     * character will be ignored.
     *
     * @param chinese String to be converted
     * @return List of Han Yu Pin Yin's prefix of every characters.
     */
    public static List<String> toHanyuPinyin(String chinese) {
        if (chinese.isEmpty()) {
            return new ArrayList<String>();
        }
        List<StringBuilder> stringBuilders = null;
        for (int i = 0, length = chinese.length(); i < length; i++) {
            final char c = chinese.charAt(i);
            String[] pinyins = null;
            final java.util.Set<Character> set = new java.util.HashSet<Character>();
            if (CharUtils.isAscii(c)) {
                if (CharUtils.isAsciiAlphanumeric(c)) {
                    set.add(c);
                }
            } else {
                try {
                    pinyins = PinyinHelper.toHanyuPinyinStringArray(c, DEFAULT_HANYU_PINYIN_OUTPUT_FORMAT);
                    if (pinyins != null) {
                        for (String pinyin : pinyins) {
                            set.add(pinyin.charAt(0));
                        }
                    }
                } catch (BadHanyuPinyinOutputFormatCombination ex) {
                    log.error(null, ex);
                }
            }
            final List<StringBuilder> tmps = stringBuilders;
            stringBuilders = new ArrayList<StringBuilder>();
            if (tmps == null) {
                for (Character character : set) {
                    final StringBuilder me = new StringBuilder();
                    me.append(character);
                    stringBuilders.add(me);
                }
            } else {
                for (Character character : set) {
                    for (StringBuilder tmp : tmps) {
                        final StringBuilder me = new StringBuilder();
                        me.append(tmp);
                        me.append(character);
                        stringBuilders.add(me);
                    }
                }
            }
        }
        List<String> result = new ArrayList<String>();
        if (stringBuilders != null) {
            for (StringBuilder stringBuilder : stringBuilders) {
                result.add(stringBuilder.toString());
            }
        }
        return result;
    }

    /**
     * Returns default currency symbol, regardless what country we are in right
     * now.
     *
     * @return Default currency symbol, regardless what country we are in right
     * now.
     */
    public static String getDefaultCurrencySymbol() {
        return "$";
    }

    /**
     * Represents latest application information. This is being used for
     * application upgrading.
     */
    public static class ApplicationInfo {

        /**
         * ID to represent application version.
         */
        public final int applicationVersionID;

        /**
         * URL link to download Windows application version <code>applicationVersionID</code>
         */
        public final String windowsDownloadLink;

        /**
         * URL link to download Linux application version <code>applicationVersionID</code>
         */
        public final String linuxDownloadLink;

        /**
         * URL link to download Mac application version <code>applicationVersionID</code>
         */
        public final String macDownloadLink;

        /**
         * URL link to download Solaris application version <code>applicationVersionID</code>
         */
        public final String solarisDownloadLink;

        /**
         * Constructs application information object.
         *
         * @param applicationVersionID ID to represent application version
         * @param windowsDownloadLink URL link to download Windows application
         * @param linuxDownloadLink URL link to download Linux application
         * @param macDownloadLink URL link to download Mac application
         * @param solarisDownloadLink URL link to download Solaris application
         */
        public ApplicationInfo(int applicationVersionID, String windowsDownloadLink, String linuxDownloadLink, String macDownloadLink, String solarisDownloadLink) {
            this.applicationVersionID = applicationVersionID;
            this.windowsDownloadLink = windowsDownloadLink;
            this.linuxDownloadLink = linuxDownloadLink;
            this.macDownloadLink = macDownloadLink;
            this.solarisDownloadLink = solarisDownloadLink;
        }
    }

    private static final HanyuPinyinOutputFormat DEFAULT_HANYU_PINYIN_OUTPUT_FORMAT = new HanyuPinyinOutputFormat();

    static {
        DEFAULT_HANYU_PINYIN_OUTPUT_FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        DEFAULT_HANYU_PINYIN_OUTPUT_FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        DEFAULT_HANYU_PINYIN_OUTPUT_FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    private static volatile List<String> NTPServers = null;

    private static final String APPLICATION_VERSION_STRING = "1.0.5";

    private static final int APPLICATION_VERSION_ID = 1072;

    private static Executor zombiePool = Executors.newFixedThreadPool(Utils.NUM_OF_THREADS_ZOMBIE_POOL);

    private static final int NUM_OF_THREADS_ZOMBIE_POOL = 4;

    private static final HttpClient httpClient;

    static {
        MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
        multiThreadedHttpConnectionManager.getParams().setMaxTotalConnections(128);
        multiThreadedHttpConnectionManager.getParams().setDefaultMaxConnectionsPerHost(128);
        httpClient = new HttpClient(multiThreadedHttpConnectionManager);
        multiThreadedHttpConnectionManager.getParams().setMaxConnectionsPerHost(httpClient.getHostConfiguration(), 128);
    }

    private static final Log log = LogFactory.getLog(Utils.class);
}
