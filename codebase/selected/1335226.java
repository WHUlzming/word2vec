package org.lwjgl.util.applet;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 * The AppletLoader enables deployment of LWJGL to applets in an easy
 * and polished way. The loader will display a configurable logo and progressbar
 * while the relevant jars (generic and native) are downloaded from a specified source.
 * </p>
 * <p>
 * The downloaded jars are extracted to the users temporary directory - and if enabled, cached for
 * faster loading in future uses.
 * </p>
 * <p>
 * The following applet parameters are required:
 * <ul>
 * <li>al_main - [String] Full package and class the applet to instantiate and display when loaded.</li>
 * <li>al_jars - [String] Comma separated list of jars to download.</li>
 * <p>
 * <li>al_windows - [String] Jar containing native files for windows.</li>
 * <li>al_linux - [String] Jar containing native files for linux.</li>
 * <li>al_mac - [String] Jar containing native files for mac.</li>
 * <li>al_solaris - [String] Jar containing native files for solaris.</li>
 * <li>al_freebsd - [String] Jar containing native files for freebsd.</li>
 * </ul>
 * </p>
 * <p>
 * Additionally the following parameters can be supplied to tweak the behaviour of the AppletLoader.
 * <ul>
 * <li>al_cache - [boolean] Whether to use cache system. <i>Default: true</i>.</li>
 * <li>al_version - [int or float] Version of deployment. If this is specified, the jars will be cached and
 * reused if the version matches. If version doesn't match all of the files are reloaded.</li>
 * 
 * <li>al_debug - [boolean] Whether to enable debug mode. <i>Default: false</i>.</li>
 * <li>al_min_jre - [String] Specify the minimum jre version that the applet requires, should be in format like 1.6.0_24 or a subset like 1.6 <i>Default: 1.5</i>.</li>
 * <li>al_prepend_host - [boolean] Whether to limit caching to this domain, disable if your applet is hosted on multiple domains and needs to share the cache. <i>Default: true</i>.</li>
 * <li>al_lookup_threads - [int] Specify the number of concurrent threads to use to get file information before downloading. <i>Default: 1</i>.</li>
 * <p>
 * <li>al_windows64 - [String] If specified it will be used instead of al_windows on 64bit windows systems.</li>
 * <li>al_windows32 - [String] If specified it will be used instead of al_windows on 32bit windows systems.</li>
 * <li>al_linux64 - [String] If specified it will be used instead of al_linux on 64bit linux systems.</li>
 * <li>al_linux32 - [String] If specified it will be used instead of al_linux on 32bit linux systems.</li>
 * <li>al_mac32 - [String] If specified it will be used instead of al_mac on 64bit mac systems.</li>
 * <li>al_mac64 - [String] If specified it will be used instead of al_mac on 32bit mac systems.</li>
 * <li>al_macppc - [String] If specified it will be used instead of al_mac on PPC mac systems.</li>
 * <p>
 * <li>boxbgcolor - [String] any String AWT color ("red", "blue", etc), RGB (0-255) or hex formated color (#RRGGBB) to use as background. <i>Default: #ffffff</i>.</li>
 * <li>boxfgcolor - [String] any String AWT color ("red", "blue", etc), RGB (0-255) or hex formated color (#RRGGBB) to use as foreground. <i>Default: #000000</i>.</li>
 * <p>
 * <li>al_logo - [String Path of of the logo resource to paint while loading.<i>Default: "appletlogo.gif"</i>.</li>
 * <li>al_progressbar - [String] Path of the progressbar resource to paint on top of the logo, width clipped by percentage.<i>Default: "appletprogress.gif"</i>.</li>
 * <p>
 * <li>lwjgl_arguments - </li> [String] used to pass LWJGL parameters to LWJGL e.g. ("-Dorg.lwjgl.input.Mouse.allowNegativeMouseCoords=true -Dorg.lwjgl.util.Debug=true").</li>
 * </ul>
 * </p>
 * @author kappaOne <one.kappa@gmail.com>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$
 * $Id$
 * 
 * Contributors:
 * <ul>
 * <li>Arielsan</li>
 * <li>Bobjob</li>
 * <li>Dashiva</li>
 * <li>Dr_evil</li>
 * <li>Elias Naur</li>
 * <li>Kevin Glass</li>
 * <li>Matthias Mann</li>
 * <li>Mickelukas</li>
 * <li>NateS</li>
 * <li>Pelle Johnsen</li>
 * <li>Riven</li>
 * <li>Ruben01</li>
 * <li>Shannon Smith</li>
 * </ul>
 * 
 */
public class AppletLoader extends Applet implements Runnable, AppletStub {

    /** initializing */
    public static final int STATE_INIT = 1;

    /** checking version of jre */
    public static final int STATE_CHECK_JRE_VERSION = 2;

    /** determining which packages that are required */
    public static final int STATE_DETERMINING_PACKAGES = 3;

    /** checking for already downloaded files */
    public static final int STATE_CHECKING_CACHE = 4;

    /** checking if any updates are available for cache files */
    public static final int STATE_CHECKING_FOR_UPDATES = 5;

    /** downloading packages */
    public static final int STATE_DOWNLOADING = 6;

    /** extracting packages */
    public static final int STATE_EXTRACTING_PACKAGES = 7;

    /** validating packages */
    public static final int STATE_VALIDATING_PACKAGES = 8;

    /** updating the classpath */
    public static final int STATE_UPDATING_CLASSPATH = 9;

    /** switching to real applet */
    public static final int STATE_SWITCHING_APPLET = 10;

    /** initializing real applet */
    public static final int STATE_INITIALIZE_REAL_APPLET = 11;

    /** stating real applet */
    public static final int STATE_START_REAL_APPLET = 12;

    /** done */
    public static final int STATE_DONE = 13;

    /** used to calculate length of progress bar */
    protected volatile int percentage;

    /** current size of download in bytes */
    protected int currentSizeDownload;

    /** total size of download in bytes */
    protected int totalSizeDownload;

    /** current size of extracted in bytes */
    protected int currentSizeExtract;

    /** total size of extracted in bytes */
    protected int totalSizeExtract;

    /** logo to be shown while loading */
    protected Image logo, logoBuffer;

    /** progressbar to render while loading */
    protected Image progressbar, progressbarBuffer;

    /** offscreen image used */
    protected Image offscreen;

    /** set to true while painting is done */
    protected boolean painting;

    /** background color of applet */
    protected Color bgColor = Color.white;

    /** color to write foreground in */
    protected Color fgColor = Color.black;

    /** urls of the jars to download */
    protected URL[] urlList;

    /** classLoader used to add downloaded jars to the classpath */
    protected ClassLoader classLoader;

    /** actual thread that does the loading */
    protected Thread loaderThread;

    /** animation thread that renders our load screen while loading */
    protected Thread animationThread;

    /** applet to load after all downloads are complete */
    protected Applet lwjglApplet;

    /** whether we're running in debug mode */
    protected boolean debugMode;

    /** whether to prepend host to cache path */
    protected boolean prependHost;

    /** Used to store file names with lastModified time */
    protected HashMap<String, Long> filesLastModified;

    /** Sizes of files to download */
    protected int[] fileSizes;

    /** Number of native jars */
    protected int nativeJarCount;

    /** whether to use caching system, only download files that have changed */
    protected boolean cacheEnabled;

    /** String to display as a subtask */
    protected String subtaskMessage = "";

    /** state of applet loader */
    protected volatile int state = STATE_INIT;

    /** whether lzma is supported */
    protected boolean lzmaSupported;

    /** whether pack200 is supported */
    protected boolean pack200Supported;

    /** whether to run in headless mode */
    protected boolean headless = false;

    /** whether to switch applets in headless mode or wait longer */
    protected boolean headlessWaiting = true;

    /** messages to be passed via liveconnect in headless mode */
    protected String[] headlessMessage;

    /** threads to use when fetching information of files to be downloaded */
    protected int concurrentLookupThreads;

    /** whether a fatal error occurred */
    protected boolean fatalError;

    /** whether a certificate refused error occurred */
    protected boolean certificateRefused;

    /** whether the minimum required JRE version is not found */
    protected boolean minimumJreNotFound;

    /** generic error message to display on error */
    protected String[] genericErrorMessage = { "An error occured while loading the applet.", "Please contact support to resolve this issue.", "<placeholder for error message>" };

    /** error message to display if user refuses to accept certificate*/
    protected String[] certificateRefusedMessage = { "Permissions for Applet Refused.", "Please accept the permissions dialog to allow", "the applet to continue the loading process." };

    /** error message to display if minimum JRE version is not met */
    protected String[] minimumJREMessage = { "Your version of Java is out of date.", "Visit java.com to get the latest version.", "Java <al_min_jre> or greater is required." };

    /** fatal error message to display */
    protected String[] errorMessage;

    /** have natives been loaded by another instance of this applet */
    protected static boolean natives_loaded;

    public void init() {
        setState(STATE_INIT);
        String[] requiredArgs = { "al_main", "al_jars" };
        for (String requiredArg : requiredArgs) {
            if (getParameter(requiredArg) == null) {
                fatalErrorOccured("missing required applet parameter: " + requiredArg, null);
                return;
            }
        }
        cacheEnabled = getBooleanParameter("al_cache", true);
        debugMode = getBooleanParameter("al_debug", false);
        prependHost = getBooleanParameter("al_prepend_host", true);
        headless = getBooleanParameter("al_headless", false);
        concurrentLookupThreads = getIntParameter("al_lookup_threads", 1);
        bgColor = getColor("boxbgcolor", Color.white);
        setBackground(bgColor);
        fgColor = getColor("boxfgcolor", Color.black);
        if (!headless) {
            logo = getImage(getStringParameter("al_logo", "appletlogo.gif"));
            progressbar = getImage(getStringParameter("al_progressbar", "appletprogress.gif"));
        }
        try {
            Class.forName("LZMA.LzmaInputStream");
            lzmaSupported = true;
        } catch (Throwable e) {
        }
        try {
            java.util.jar.Pack200.class.getSimpleName();
            pack200Supported = true;
        } catch (Throwable e) {
        }
    }

    /**
	 * Generates a stacktrace in the form of a string
	 * @param exception Exception to make stacktrace of
	 * @return Stacktrace of exception in the form of a string
	 */
    private static String generateStacktrace(Exception exception) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        return result.toString();
    }

    public void start() {
        if (lwjglApplet != null) {
            lwjglApplet.start();
        } else {
            if (loaderThread == null && !fatalError) {
                loaderThread = new Thread(this);
                loaderThread.setName("AppletLoader.loaderThread");
                loaderThread.start();
                if (!headless) {
                    animationThread = new Thread() {

                        public void run() {
                            while (loaderThread != null) {
                                repaint();
                                AppletLoader.this.sleep(100);
                            }
                            animationThread = null;
                        }
                    };
                    animationThread.setName("AppletLoader.animationthread");
                    animationThread.start();
                }
            }
        }
    }

    public void stop() {
        if (lwjglApplet != null) {
            lwjglApplet.stop();
        }
    }

    public void destroy() {
        if (lwjglApplet != null) {
            lwjglApplet.destroy();
        }
    }

    /**
	 * Clean up resources
	 */
    protected void cleanUp() {
        progressbar = null;
        logo = null;
        logoBuffer = null;
        progressbarBuffer = null;
        offscreen = null;
    }

    /**
	 * Retrieves the applet that has been loaded. Useful for liveconnect.
	 */
    public Applet getApplet() {
        return lwjglApplet;
    }

    /**
	 * Retrieves the current status of the AppletLoader and is 
	 * used by liveconnect when running in headless mode.
	 * 
	 * This method will return the current progress of the AppletLoader
	 * as a value from 0-100. In the case of a fatal error it will
	 * return -1. If the certificate is refused it will return -2.
	 * If the minimum jre requirement is not met will return -3.
	 * 
	 * When method returns 100 the AppletLoader will sleep until the 
	 * method is called again. When called again it will switch to the
	 * LWJGL Applet. This is a useful trigger to start the LWJGL applet 
	 * when needed.
	 */
    public int getStatus() {
        if (fatalError) {
            headlessMessage = errorMessage;
            if (certificateRefused) return -2;
            if (minimumJreNotFound) return -3;
            return -1;
        }
        if (percentage == 100 && headlessWaiting) {
            headlessWaiting = false;
        }
        if (percentage == 95) {
            percentage = 100;
        }
        String[] message = { getDescriptionForState(), subtaskMessage };
        headlessMessage = message;
        return percentage;
    }

    /**
	 * Retrieves the current message for the current status. 
	 * Used by liveconnect when running in headless mode.
	 */
    public String[] getMessages() {
        return headlessMessage;
    }

    /**
	 * Transfers the call of AppletResize from the stub to the lwjglApplet.
	 */
    public void appletResize(int width, int height) {
        resize(width, height);
    }

    public final void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (state == STATE_DONE) {
            cleanUp();
            return;
        }
        if (headless) return;
        if (offscreen == null) {
            offscreen = createImage(getWidth(), getHeight());
            if (logo != null) {
                logoBuffer = createImage(logo.getWidth(null), logo.getHeight(null));
                offscreen.getGraphics().drawImage(logo, 0, 0, this);
                imageUpdate(logo, ImageObserver.FRAMEBITS, 0, 0, 0, 0);
            }
            if (progressbar != null) {
                progressbarBuffer = createImage(progressbar.getWidth(null), progressbar.getHeight(null));
                offscreen.getGraphics().drawImage(progressbar, 0, 0, this);
                imageUpdate(progressbar, ImageObserver.FRAMEBITS, 0, 0, 0, 0);
            }
        }
        Graphics og = offscreen.getGraphics();
        FontMetrics fm = og.getFontMetrics();
        og.setColor(bgColor);
        og.fillRect(0, 0, offscreen.getWidth(null), offscreen.getHeight(null));
        og.setColor(fgColor);
        if (fatalError) {
            for (int i = 0; i < errorMessage.length; i++) {
                if (errorMessage[i] != null) {
                    int messageX = (offscreen.getWidth(null) - fm.stringWidth(errorMessage[i])) / 2;
                    int messageY = (offscreen.getHeight(null) - (fm.getHeight() * errorMessage.length)) / 2;
                    og.drawString(errorMessage[i], messageX, messageY + i * fm.getHeight());
                }
            }
        } else {
            og.setColor(fgColor);
            painting = true;
            int x = offscreen.getWidth(null) / 2;
            int y = offscreen.getHeight(null) / 2;
            if (logo != null) {
                og.drawImage(logoBuffer, x - logo.getWidth(null) / 2, y - logo.getHeight(null) / 2, this);
            }
            String message = getDescriptionForState();
            int messageX = (offscreen.getWidth(null) - fm.stringWidth(message)) / 2;
            int messageY = y + 20;
            if (logo != null) messageY += logo.getHeight(null) / 2; else if (progressbar != null) messageY += progressbar.getHeight(null) / 2;
            og.drawString(message, messageX, messageY);
            if (subtaskMessage.length() > 0) {
                messageX = (offscreen.getWidth(null) - fm.stringWidth(subtaskMessage)) / 2;
                og.drawString(subtaskMessage, messageX, messageY + 20);
            }
            if (progressbar != null) {
                int barSize = (progressbar.getWidth(null) * percentage) / 100;
                og.clipRect(x - progressbar.getWidth(null) / 2, 0, barSize, offscreen.getHeight(null));
                og.drawImage(progressbarBuffer, x - progressbar.getWidth(null) / 2, y - progressbar.getHeight(null) / 2, this);
            }
            painting = false;
        }
        og.dispose();
        g.drawImage(offscreen, (getWidth() - offscreen.getWidth(null)) / 2, (getHeight() - offscreen.getHeight(null)) / 2, null);
    }

    /**
	 * When an animated gif frame is ready to be drawn the ImageObserver
	 * will call this method.
	 *
	 * The Image frame is copied into a buffer, which is then drawn.
	 * This is done to prevent image tearing on gif animations.
	 */
    public boolean imageUpdate(Image img, int flag, int x, int y, int width, int height) {
        if (state == STATE_DONE) return false;
        if (flag == ImageObserver.FRAMEBITS && !painting) {
            Image buffer;
            if (img == logo) buffer = logoBuffer; else buffer = progressbarBuffer;
            Graphics g = buffer.getGraphics();
            g.setColor(bgColor);
            g.fillRect(0, 0, buffer.getWidth(null), buffer.getHeight(null));
            if (img == progressbar && logo != null) {
                g.drawImage(logoBuffer, progressbar.getWidth(null) / 2 - logo.getWidth(null) / 2, progressbar.getHeight(null) / 2 - logo.getHeight(null) / 2, null);
            }
            g.drawImage(img, 0, 0, this);
            g.dispose();
            repaint();
        }
        return true;
    }

    /**
	 * @return string describing the state of the loader
	 */
    protected String getDescriptionForState() {
        switch(state) {
            case STATE_INIT:
                return "Initializing loader";
            case STATE_CHECK_JRE_VERSION:
                return "Checking version";
            case STATE_DETERMINING_PACKAGES:
                return "Determining packages to load";
            case STATE_CHECKING_CACHE:
                return "Calculating download size";
            case STATE_CHECKING_FOR_UPDATES:
                return "Checking for updates";
            case STATE_DOWNLOADING:
                return "Downloading packages";
            case STATE_EXTRACTING_PACKAGES:
                return "Extracting downloaded packages";
            case STATE_VALIDATING_PACKAGES:
                return "Validating packages";
            case STATE_UPDATING_CLASSPATH:
                return "Updating classpath";
            case STATE_SWITCHING_APPLET:
                return "Switching applet";
            case STATE_INITIALIZE_REAL_APPLET:
                return "Initializing real applet";
            case STATE_START_REAL_APPLET:
                return "Starting real applet";
            case STATE_DONE:
                return "Done loading";
            default:
                return "unknown state";
        }
    }

    /**
	 * Trims the passed file string based on the available capabilities
	 * @param file string of files to be trimmed
	 * @return trimmed string based on capabilities of client
	 */
    protected String trimExtensionByCapabilities(String file) {
        if (!pack200Supported) {
            file = file.replace(".pack", "");
        }
        if (!lzmaSupported && file.endsWith(".lzma")) {
            file = file.replace(".lzma", "");
            System.out.println("LZMA decoder (lzma.jar) not found, trying " + file + " without lzma extension.");
        }
        return file;
    }

    /**
	 * Reads list of jars to download and adds the urls to urlList
	 * also finds out which OS you are on and adds appropriate native
	 * jar to the urlList
	 */
    protected void loadJarURLs() throws Exception {
        setState(STATE_DETERMINING_PACKAGES);
        String jarList = getParameter("al_jars");
        String nativeJarList = null;
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Win")) {
            if (System.getProperty("os.arch").endsWith("64")) {
                nativeJarList = getParameter("al_windows64");
            } else {
                nativeJarList = getParameter("al_windows32");
            }
            if (nativeJarList == null) {
                nativeJarList = getParameter("al_windows");
            }
        } else if (osName.startsWith("Linux") || osName.startsWith("Unix")) {
            if (System.getProperty("os.arch").endsWith("64")) {
                nativeJarList = getParameter("al_linux64");
            } else {
                nativeJarList = getParameter("al_linux32");
            }
            if (nativeJarList == null) {
                nativeJarList = getParameter("al_linux");
            }
        } else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            if (System.getProperty("os.arch").endsWith("64")) {
                nativeJarList = getParameter("al_mac64");
            } else if (System.getProperty("os.arch").contains("ppc")) {
                nativeJarList = getParameter("al_macppc");
            } else {
                nativeJarList = getParameter("al_mac32");
            }
            if (nativeJarList == null) {
                nativeJarList = getParameter("al_mac");
            }
        } else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            nativeJarList = getParameter("al_solaris");
        } else if (osName.startsWith("FreeBSD")) {
            nativeJarList = getParameter("al_freebsd");
        } else {
            fatalErrorOccured("OS (" + osName + ") not supported", null);
            return;
        }
        if (nativeJarList == null) {
            fatalErrorOccured("no lwjgl natives files found", null);
            return;
        }
        jarList = trimExtensionByCapabilities(jarList);
        StringTokenizer jars = new StringTokenizer(jarList, ", ");
        nativeJarList = trimExtensionByCapabilities(nativeJarList);
        StringTokenizer nativeJars = new StringTokenizer(nativeJarList, ", ");
        int jarCount = jars.countTokens();
        nativeJarCount = nativeJars.countTokens();
        urlList = new URL[jarCount + nativeJarCount];
        URL path = getCodeBase();
        for (int i = 0; i < jarCount; i++) {
            urlList[i] = new URL(path, jars.nextToken());
        }
        for (int i = jarCount; i < jarCount + nativeJarCount; i++) {
            urlList[i] = new URL(path, nativeJars.nextToken());
        }
    }

    /**
	 * 9 steps
	 *
	 * 1) check jre version meets minimum requirements
	 * 2) check applet cache and decide which jars to download
	 * 3) download the jars
	 * 4) extract native files
	 * 5) validate jars for any corruption
	 * 6) save applet cache information
	 * 7) add jars to class path
	 * 8) set any lwjgl properties
	 * 9) switch to loaded applet
	 */
    public void run() {
        percentage = 5;
        try {
            debug_sleep(2000);
            if (!isMinJREVersionAvailable()) {
                minimumJreNotFound = true;
                fatalErrorOccured("Java " + getStringParameter("al_min_jre", "1.5") + " or greater is required.", null);
                return;
            }
            loadJarURLs();
            String path = getCacheDirectory();
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File versionFile = new File(dir, "version");
            boolean versionAvailable = false;
            String version = getParameter("al_version");
            if (version != null) {
                versionAvailable = compareVersion(versionFile, version.toLowerCase());
            }
            if (!versionAvailable) {
                getJarInfo(dir);
                downloadJars(path);
                extractJars(path);
                extractNatives(path);
                validateJars(path);
                if (version != null) {
                    percentage = 90;
                    writeObjectFile(versionFile, version.toLowerCase());
                }
                writeObjectFile(new File(dir, "timestamps"), filesLastModified);
            }
            updateClassPath(path);
            setLWJGLProperties();
            if (headless) {
                while (headlessWaiting) {
                    Thread.sleep(100);
                }
            }
            EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    try {
                        switchApplet();
                    } catch (Exception e) {
                        fatalErrorOccured("This occurred while '" + getDescriptionForState() + "'", e);
                    }
                    setState(STATE_DONE);
                    repaint();
                }
            });
        } catch (Exception e) {
            certificateRefused = e instanceof AccessControlException;
            fatalErrorOccured("This occurred while '" + getDescriptionForState() + "'", e);
        } finally {
            loaderThread = null;
        }
    }

    /**
	 * When this method is supplied with a JRE version it will compare it to the
	 * current JRE version.
	 * 
	 * minimum requried JRE version is set using al_min_jre parameter, if not 
	 * this is not set then the value will default to version 1.5
	 * 
	 * The minimumVersion should follow a structure such as x.x.x_x
	 * Example values would include 1.6.0_10 or a subset like 1.6.0 or 1.6
	 * 
	 * @return returns true if the available version is greater or equal to the 
	 * minimum version required
	 * 
	 * @throws Exception a NumberFormatException is thrown if the string is not valid
	 */
    public boolean isMinJREVersionAvailable() throws Exception {
        setState(STATE_CHECK_JRE_VERSION);
        String minimumVersion = getStringParameter("al_min_jre", "1.5");
        String javaVersion = System.getProperty("java.version");
        String[] jvmVersionData = javaVersion.split("[_\\.]");
        String[] minVersionData = minimumVersion.split("[_\\.]");
        int maxLength = Math.max(jvmVersionData.length, minVersionData.length);
        int[] jvmVersion = new int[maxLength];
        int[] minVersion = new int[maxLength];
        for (int i = 0; i < jvmVersionData.length; i++) {
            jvmVersion[i] = Integer.parseInt(jvmVersionData[i]);
        }
        for (int i = 0; i < minVersionData.length; i++) {
            minVersion[i] = Integer.parseInt(minVersionData[i]);
        }
        for (int i = 0; i < maxLength; i++) {
            if (jvmVersion[i] < minVersion[i]) return false;
        }
        return true;
    }

    /**
	 * This method will return true if the version stored in the file
	 * matches the supplied String version.
	 * 
	 * @param versionFile - location to file containing version information
	 * @param version - String version that needs to be compared
	 * @return returns true if the version in file matches specified version
	 */
    protected boolean compareVersion(File versionFile, String version) {
        if (versionFile.exists()) {
            String s = readStringFile(versionFile);
            if (s != null && s.equals(version)) {
                percentage = 90;
                if (debugMode) {
                    System.out.println("Loading Cached Applet Version: " + version);
                }
                debug_sleep(2000);
                return true;
            }
        }
        return false;
    }

    /**
	 * Parses the java_arguments list and sets lwjgl specific 
	 * properties accordingly, before the launch.
	 */
    protected void setLWJGLProperties() {
        String lwjglArguments = getParameter("lwjgl_arguments");
        if (lwjglArguments != null && lwjglArguments.length() > 0) {
            int start = lwjglArguments.indexOf("-Dorg.lwjgl");
            while (start != -1) {
                int end = lwjglArguments.indexOf(" ", start);
                if (end == -1) {
                    end = lwjglArguments.length();
                }
                String[] keyValue = lwjglArguments.substring(start + 2, end).split("=");
                System.setProperty(keyValue[0], keyValue[1]);
                if (debugMode) {
                    System.out.println("Setting property " + keyValue[0] + " to " + keyValue[1]);
                }
                start = lwjglArguments.indexOf("-Dorg.lwjgl", end);
            }
        }
    }

    /**
	 * This method will return the location of the cache directory. All the
	 * applet files will be downloaded and stored here. A folder will be
	 * created inside the LWJGL cache directory from the al_title parameter.
	 * This folder will also be prepended by the host name of the codebase 
	 * to avoid conflict with same named applets on other hosts.
	 * 
	 * @return path to applets cache directory
	 * @throws Exception if access is denied
	 */
    protected String getCacheDirectory() throws Exception {
        String path = AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {

            public String run() throws Exception {
                String codebase = "";
                if (prependHost) {
                    codebase = getCodeBase().getHost();
                    if (codebase == null || codebase.length() == 0) {
                        codebase = "localhost";
                    }
                    codebase += File.separator;
                }
                return getLWJGLCacheDir() + File.separator + codebase + getParameter("al_title") + File.separator;
            }
        });
        return path;
    }

    /**
	 * Get path to the lwjgl cache directory. This location will be where
	 * the OS keeps temporary files.
	 * 
	 * @return path to the lwjgl cache directory
	 */
    protected String getLWJGLCacheDir() {
        String cacheDir = System.getProperty("deployment.user.cachedir");
        if (cacheDir == null || System.getProperty("os.name").startsWith("Win")) {
            cacheDir = System.getProperty("java.io.tmpdir");
        }
        return cacheDir + File.separator + "lwjglcache";
    }

    /**
	 * read String object from File
	 *
	 * @param file to be read
	 * @return the String stored in the file or null if it fails
	 */
    protected String readStringFile(File file) {
        try {
            return (String) readObjectFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * read the HashMap from File
	 *
	 * @param file the file to read
	 * @return the hashmap stored in the file or an empty hashmap if it fails
	 */
    @SuppressWarnings("unchecked")
    protected HashMap<String, Long> readHashMapFile(File file) {
        try {
            return (HashMap<String, Long>) readObjectFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Long>();
    }

    /**
	 * read the object from the File
	 * 
	 * @param file the file to read
	 * @return the object contained in the file or null if it fails
	 * @throws Exception if it fails to read object from file
	 */
    protected Object readObjectFile(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        try {
            ObjectInputStream dis = new ObjectInputStream(fis);
            Object object = dis.readObject();
            dis.close();
            return object;
        } catch (Exception e) {
            throw e;
        } finally {
            fis.close();
        }
    }

    /**
	 * write object to specified File
	 *
	 * @param file the file to write out to
	 * @param object the contents of the file
	 * @throws Exception if it fails to write file
	 */
    protected void writeObjectFile(File file, Object object) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream dos = new ObjectOutputStream(fos);
        dos.writeObject(object);
        dos.close();
        fos.close();
    }

    /**
	 * Edits the ClassPath at runtime to include the jars
	 * that have just been downloaded and then adds the
	 * lwjgl natives folder property.
	 *
	 * @param path location where applet is stored
	 * @throws Exception if it fails to add classpath
	 */
    protected void updateClassPath(final String path) throws Exception {
        setState(STATE_UPDATING_CLASSPATH);
        percentage = 95;
        URL[] urls = new URL[urlList.length];
        for (int i = 0; i < urlList.length; i++) {
            String file = new File(path, getJarName(urlList[i])).toURI().toString();
            file = file.replace("!", "%21");
            urls[i] = new URL(file);
        }
        final Certificate[] certs = getCurrentCertificates();
        String osName = System.getProperty("os.name");
        final boolean isMacOS = (osName.startsWith("Mac") || osName.startsWith("Darwin"));
        classLoader = new URLClassLoader(urls) {

            protected PermissionCollection getPermissions(CodeSource codesource) {
                PermissionCollection perms = null;
                try {
                    if (isMacOS) {
                        if (certificatesMatch(certs, codesource.getCertificates())) {
                            perms = new Permissions();
                            perms.add(new AllPermission());
                            return perms;
                        }
                    }
                    Method method = SecureClassLoader.class.getDeclaredMethod("getPermissions", new Class[] { CodeSource.class });
                    method.setAccessible(true);
                    perms = (PermissionCollection) method.invoke(getClass().getClassLoader(), new Object[] { codesource });
                    String host = getCodeBase().getHost();
                    if (host != null && (host.length() > 0)) {
                        perms.add(new SocketPermission(host, "connect,accept"));
                    } else if ("file".equals(codesource.getLocation().getProtocol())) {
                        String path = codesource.getLocation().getFile().replace('/', File.separatorChar);
                        perms.add(new FilePermission(path, "read"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return perms;
            }

            protected String findLibrary(String libname) {
                return path + "natives" + File.separator + System.mapLibraryName(libname);
            }
        };
        debug_sleep(2000);
        unloadNatives(path);
        System.setProperty("org.lwjgl.librarypath", path + "natives");
        System.setProperty("net.java.games.input.librarypath", path + "natives");
        System.setProperty("java.library.path", path + "natives");
        natives_loaded = true;
    }

    /**
	 * Unload natives loaded by a different classloader.
	 *
	 * Due to limitations of the jvm, native files can only
	 * be loaded once and only be used by the classloader
	 * they were loaded from.
	 *
	 * Due to the way applets on plugin1 work, one jvm must
	 * be used for all applets. We need to use multiple
	 * classloaders in the same jvm due to LWJGL's static
	 * nature. In order to solve this we simply remove the
	 * natives from a previous classloader allowing a new
	 * classloader to use those natives in the same jvm.
	 *
	 * This method will only attempt to unload natives from a
	 * previous classloader if it detects that the natives have
	 * been loaded in the same jvm.
	 *
	 * @param nativePath directory where natives are stored
	 */
    private void unloadNatives(String nativePath) {
        if (!natives_loaded) {
            return;
        }
        try {
            Field field = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            field.setAccessible(true);
            Vector libs = (Vector) field.get(getClass().getClassLoader());
            String path = new File(nativePath).getCanonicalPath();
            for (int i = 0; i < libs.size(); i++) {
                String s = (String) libs.get(i);
                if (s.startsWith(path)) {
                    libs.remove(i);
                    i--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * replace the current applet with the lwjgl applet
	 * using AppletStub and initialise and start it
	 */
    protected void switchApplet() throws Exception {
        setState(STATE_SWITCHING_APPLET);
        percentage = 100;
        debug_sleep(2000);
        Thread.currentThread().setContextClassLoader(classLoader);
        Class appletClass = classLoader.loadClass(getParameter("al_main"));
        lwjglApplet = (Applet) appletClass.newInstance();
        lwjglApplet.setStub(this);
        lwjglApplet.setSize(getWidth(), getHeight());
        setLayout(new BorderLayout());
        add(lwjglApplet);
        validate();
        setState(STATE_INITIALIZE_REAL_APPLET);
        lwjglApplet.init();
        setState(STATE_START_REAL_APPLET);
        lwjglApplet.start();
    }

    /**
	 * This method will get the files sizes of the files to download.
	 * It wil further get the lastModified time of files
	 * and save it in a hashmap, if cache is enabled it will mark
	 * those files that have not changed since last download to not
	 * redownloaded.
	 *
	 * @param dir - location to read cache file from
	 * @throws Exception - if fails to get infomation
	 */
    protected void getJarInfo(File dir) throws Exception {
        setState(STATE_CHECKING_CACHE);
        filesLastModified = new HashMap<String, Long>();
        fileSizes = new int[urlList.length];
        File timestampsFile = new File(dir, "timestamps");
        if (timestampsFile.exists()) {
            setState(STATE_CHECKING_FOR_UPDATES);
            filesLastModified = readHashMapFile(timestampsFile);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentLookupThreads);
        Queue<Future> requests = new LinkedList<Future>();
        final Object sync = new Integer(1);
        for (int j = 0; j < urlList.length; j++) {
            final int i = j;
            Future request = executorService.submit(new Runnable() {

                public void run() {
                    try {
                        URLConnection urlconnection = urlList[i].openConnection();
                        urlconnection.setDefaultUseCaches(false);
                        if (urlconnection instanceof HttpURLConnection) {
                            ((HttpURLConnection) urlconnection).setRequestMethod("HEAD");
                        }
                        fileSizes[i] = urlconnection.getContentLength();
                        long lastModified = urlconnection.getLastModified();
                        String fileName = getFileName(urlList[i]);
                        if (cacheEnabled && lastModified != 0 && filesLastModified.containsKey(fileName)) {
                            long savedLastModified = filesLastModified.get(fileName);
                            if (savedLastModified == lastModified) {
                                fileSizes[i] = -2;
                            }
                        }
                        if (fileSizes[i] >= 0) {
                            synchronized (sync) {
                                totalSizeDownload += fileSizes[i];
                            }
                        }
                        filesLastModified.put(fileName, lastModified);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to fetch information for " + urlList[i], e);
                    }
                }
            });
            requests.add(request);
        }
        while (!requests.isEmpty()) {
            Iterator<Future> iterator = requests.iterator();
            while (iterator.hasNext()) {
                Future request = iterator.next();
                if (request.isDone()) {
                    request.get();
                    iterator.remove();
                    percentage = 5 + (int) (10 * (urlList.length - requests.size()) / (float) urlList.length);
                }
            }
            Thread.sleep(10);
        }
        executorService.shutdown();
    }

    /**
	 * Will download the jars from the server using the list of urls
	 * in urlList, while at the same time updating progress bar
	 *
	 * @param path location of the directory to save to
	 * @throws Exception if download fails
	 */
    protected void downloadJars(String path) throws Exception {
        setState(STATE_DOWNLOADING);
        URLConnection urlconnection;
        int initialPercentage = percentage = 15;
        byte buffer[] = new byte[65536];
        for (int i = 0; i < urlList.length; i++) {
            if (fileSizes[i] == -2) continue;
            int unsuccessfulAttempts = 0;
            int maxUnsuccessfulAttempts = 3;
            boolean downloadFile = true;
            while (downloadFile) {
                downloadFile = false;
                debug_sleep(2000);
                urlconnection = urlList[i].openConnection();
                if (urlconnection instanceof HttpURLConnection) {
                    urlconnection.setRequestProperty("Cache-Control", "no-cache");
                    urlconnection.connect();
                }
                String currentFile = getFileName(urlList[i]);
                InputStream inputstream = getJarInputStream(currentFile, urlconnection);
                FileOutputStream fos = new FileOutputStream(path + currentFile);
                int bufferSize;
                long downloadStartTime = System.currentTimeMillis();
                int downloadedAmount = 0;
                int fileSize = 0;
                String downloadSpeedMessage = "";
                while ((bufferSize = inputstream.read(buffer, 0, buffer.length)) != -1) {
                    debug_sleep(10);
                    fos.write(buffer, 0, bufferSize);
                    currentSizeDownload += bufferSize;
                    fileSize += bufferSize;
                    percentage = initialPercentage + ((currentSizeDownload * 45) / totalSizeDownload);
                    subtaskMessage = "Retrieving: " + currentFile + " " + ((currentSizeDownload * 100) / totalSizeDownload) + "%";
                    downloadedAmount += bufferSize;
                    long timeLapse = System.currentTimeMillis() - downloadStartTime;
                    if (timeLapse >= 1000) {
                        float downloadSpeed = (float) downloadedAmount / timeLapse;
                        downloadSpeed = ((int) (downloadSpeed * 100)) / 100f;
                        downloadSpeedMessage = " - " + downloadSpeed + " KB/sec";
                        downloadedAmount = 0;
                        downloadStartTime = System.currentTimeMillis();
                    }
                    subtaskMessage += downloadSpeedMessage;
                }
                inputstream.close();
                fos.close();
                if (urlconnection instanceof HttpURLConnection) {
                    if (fileSize == fileSizes[i]) {
                    } else if (fileSizes[i] <= 0) {
                    } else {
                        unsuccessfulAttempts++;
                        if (unsuccessfulAttempts < maxUnsuccessfulAttempts) {
                            downloadFile = true;
                            currentSizeDownload -= fileSize;
                        } else {
                            throw new Exception("failed to download " + currentFile);
                        }
                    }
                }
            }
        }
        subtaskMessage = "";
    }

    /**
	 * Retrieves a jar files input stream. This method exists primarily to fix an Opera hang in getInputStream
	 * @param urlconnection connection to get input stream from
	 * @return InputStream or null if not possible
	 */
    protected InputStream getJarInputStream(final String currentFile, final URLConnection urlconnection) throws Exception {
        final InputStream[] is = new InputStream[1];
        for (int j = 0; j < 3 && is[0] == null; j++) {
            Thread t = new Thread() {

                public void run() {
                    try {
                        is[0] = urlconnection.getInputStream();
                    } catch (IOException e) {
                    }
                }
            };
            t.setName("JarInputStreamThread");
            t.start();
            int iterationCount = 0;
            while (is[0] == null && iterationCount++ < 5) {
                try {
                    t.join(1000);
                } catch (InterruptedException inte) {
                }
            }
            if (is[0] == null) {
                try {
                    t.interrupt();
                    t.join();
                } catch (InterruptedException inte) {
                }
            }
        }
        if (is[0] == null) {
            throw new Exception("Unable to get input stream for " + currentFile);
        }
        return is[0];
    }

    /**
	 *  Extract LZMA File
	 *  @param in Input path to pack file
	 *  @param out output path to resulting file
	 *  @throws Exception if any errors occur
	 */
    protected void extractLZMA(String in, String out) throws Exception {
        File f = new File(in);
        FileInputStream fileInputHandle = new FileInputStream(f);
        Class<?> clazz = Class.forName("LZMA.LzmaInputStream");
        Constructor constructor = clazz.getDeclaredConstructor(InputStream.class);
        InputStream inputHandle = (InputStream) constructor.newInstance(fileInputHandle);
        OutputStream outputHandle = new FileOutputStream(out);
        byte[] buffer = new byte[1 << 14];
        int ret = inputHandle.read(buffer);
        while (ret >= 1) {
            outputHandle.write(buffer, 0, ret);
            ret = inputHandle.read(buffer);
        }
        inputHandle.close();
        outputHandle.close();
        f.delete();
    }

    /**
	 *  Extract GZip File
	 *  @param in Input path to pack file
	 *  @param out output path to resulting file
	 *  @throws Exception if any errors occur
	 */
    protected void extractGZip(String in, String out) throws Exception {
        File f = new File(in);
        FileInputStream fileInputHandle = new FileInputStream(f);
        InputStream inputHandle = new GZIPInputStream(fileInputHandle);
        OutputStream outputHandle = new FileOutputStream(out);
        byte[] buffer = new byte[1 << 14];
        int ret = inputHandle.read(buffer);
        while (ret >= 1) {
            outputHandle.write(buffer, 0, ret);
            ret = inputHandle.read(buffer);
        }
        inputHandle.close();
        outputHandle.close();
        f.delete();
    }

    /**
	 *  Extract Pack File
	 *  @param in Input path to pack file
	 *  @param out output path to resulting file
	 *  @throws Exception if any errors occur
	 */
    protected void extractPack(String in, String out) throws Exception {
        File f = new File(in);
        FileOutputStream fostream = new FileOutputStream(out);
        JarOutputStream jostream = new JarOutputStream(fostream);
        Pack200.Unpacker unpacker = Pack200.newUnpacker();
        unpacker.unpack(f, jostream);
        jostream.close();
        fostream.close();
        f.delete();
    }

    /**
	 *  Extract all jars from any lzma/gz/pack files
	 *
	 *  @param path output path
	 *  @throws Exception if any errors occur
	 */
    protected void extractJars(String path) throws Exception {
        setState(STATE_EXTRACTING_PACKAGES);
        float increment = (float) 10.0 / urlList.length;
        for (int i = 0; i < urlList.length; i++) {
            if (fileSizes[i] == -2) continue;
            percentage = 55 + (int) (increment * (i + 1));
            String filename = getFileName(urlList[i]);
            if (filename.endsWith(".pack.lzma")) {
                subtaskMessage = "Extracting: " + filename + " to " + replaceLast(filename, ".lzma", "");
                debug_sleep(1000);
                extractLZMA(path + filename, path + replaceLast(filename, ".lzma", ""));
                subtaskMessage = "Extracting: " + replaceLast(filename, ".lzma", "") + " to " + replaceLast(filename, ".pack.lzma", "");
                debug_sleep(1000);
                extractPack(path + replaceLast(filename, ".lzma", ""), path + replaceLast(filename, ".pack.lzma", ""));
            } else if (filename.endsWith(".pack.gz")) {
                subtaskMessage = "Extracting: " + filename + " to " + replaceLast(filename, ".gz", "");
                debug_sleep(1000);
                extractGZip(path + filename, path + replaceLast(filename, ".gz", ""));
                subtaskMessage = "Extracting: " + replaceLast(filename, ".gz", "") + " to " + replaceLast(filename, ".pack.gz", "");
                debug_sleep(1000);
                extractPack(path + replaceLast(filename, ".gz", ""), path + replaceLast(filename, ".pack.gz", ""));
            } else if (filename.endsWith(".pack")) {
                subtaskMessage = "Extracting: " + filename + " to " + replaceLast(filename, ".pack", "");
                debug_sleep(1000);
                extractPack(path + filename, path + replaceLast(filename, ".pack", ""));
            } else if (filename.endsWith(".lzma")) {
                subtaskMessage = "Extracting: " + filename + " to " + replaceLast(filename, ".lzma", "");
                debug_sleep(1000);
                extractLZMA(path + filename, path + replaceLast(filename, ".lzma", ""));
            } else if (filename.endsWith(".gz")) {
                subtaskMessage = "Extracting: " + filename + " to " + replaceLast(filename, ".gz", "");
                debug_sleep(1000);
                extractGZip(path + filename, path + replaceLast(filename, ".gz", ""));
            }
        }
    }

    /**
	 * This method will extract all file from the native jar and extract them
	 * to the subdirectory called "natives" in the local path, will also check
	 * to see if the native jar files is signed properly
	 *
	 * @param path base folder containing all downloaded jars
	 * @throws Exception if it fails to extract files
	 */
    protected void extractNatives(String path) throws Exception {
        setState(STATE_EXTRACTING_PACKAGES);
        float percentageParts = 15f / nativeJarCount;
        File nativeFolder = new File(path + "natives");
        if (!nativeFolder.exists()) {
            nativeFolder.mkdir();
        }
        Certificate[] certificate = getCurrentCertificates();
        for (int i = urlList.length - nativeJarCount; i < urlList.length; i++) {
            if (fileSizes[i] == -2) {
                continue;
            }
            String nativeJar = getJarName(urlList[i]);
            JarFile jarFile = new JarFile(path + nativeJar, true);
            Enumeration entities = jarFile.entries();
            totalSizeExtract = 0;
            int jarNum = i - (urlList.length - nativeJarCount);
            while (entities.hasMoreElements()) {
                JarEntry entry = (JarEntry) entities.nextElement();
                if (entry.isDirectory() || entry.getName().indexOf('/') != -1) {
                    continue;
                }
                totalSizeExtract += entry.getSize();
            }
            currentSizeExtract = 0;
            entities = jarFile.entries();
            while (entities.hasMoreElements()) {
                JarEntry entry = (JarEntry) entities.nextElement();
                if (entry.isDirectory() || entry.getName().indexOf('/') != -1) {
                    continue;
                }
                File f = new File(path + "natives" + File.separator + entry.getName());
                if (f.exists()) {
                    if (!f.delete()) {
                        continue;
                    }
                }
                debug_sleep(1000);
                InputStream in = jarFile.getInputStream(jarFile.getEntry(entry.getName()));
                OutputStream out = new FileOutputStream(path + "natives" + File.separator + entry.getName());
                int bufferSize;
                byte buffer[] = new byte[65536];
                while ((bufferSize = in.read(buffer, 0, buffer.length)) != -1) {
                    debug_sleep(10);
                    out.write(buffer, 0, bufferSize);
                    currentSizeExtract += bufferSize;
                    percentage = 65 + (int) (percentageParts * (jarNum + currentSizeExtract / (float) totalSizeExtract));
                    subtaskMessage = "Extracting: " + entry.getName() + " " + ((currentSizeExtract * 100) / totalSizeExtract) + "%";
                }
                in.close();
                out.close();
                if (!certificatesMatch(certificate, entry.getCertificates())) {
                    f.delete();
                    throw new Exception("The certificate(s) in " + nativeJar + " do not match the AppletLoader!");
                }
            }
            subtaskMessage = "";
            jarFile.close();
            File f = new File(path + nativeJar);
            f.delete();
        }
    }

    /**
	 * Compare two certificate chains to see if they match
	 *
	 * @param cert1 first chain of certificates
	 * @param cert2 second chain of certificates
	 * 
	 * @return true if the certificate chains are the same
	 */
    protected static boolean certificatesMatch(Certificate[] certs1, Certificate[] certs2) throws Exception {
        if (certs1 == null || certs2 == null) {
            return false;
        }
        if (certs1.length != certs2.length) {
            System.out.println("Certificate chain differs in length [" + certs1.length + " vs " + certs2.length + "]!");
            return false;
        }
        for (int i = 0; i < certs1.length; i++) {
            if (!certs1[i].equals(certs2[i])) {
                System.out.println("Certificate mismatch found!");
                return false;
            }
        }
        return true;
    }

    /**
	 * Returns the current certificate chain of the AppletLoader
	 * 
	 * @return - certificate chain of AppletLoader
	 */
    protected static Certificate[] getCurrentCertificates() throws Exception {
        Certificate[] certificate = AppletLoader.class.getProtectionDomain().getCodeSource().getCertificates();
        if (certificate == null) {
            URL location = AppletLoader.class.getProtectionDomain().getCodeSource().getLocation();
            JarURLConnection jurl = (JarURLConnection) (new URL("jar:" + location.toString() + "!/org/lwjgl/util/applet/AppletLoader.class").openConnection());
            jurl.setDefaultUseCaches(true);
            certificate = jurl.getCertificates();
        }
        return certificate;
    }

    /**
	 *  Check and validate jars which will be loaded into the classloader to make 
	 *  sure that they are not corrupt. This ensures corrupt files are never marked
	 *  as successful downloadeds by the cache system.
	 *  
	 *  @param path - where the jars are stored
	 *  @throws Exception if a corrupt jar is found
	 */
    protected void validateJars(String path) throws Exception {
        setState(STATE_VALIDATING_PACKAGES);
        percentage = 80;
        float percentageParts = 10f / urlList.length;
        for (int i = 0; i < urlList.length - nativeJarCount; i++) {
            debug_sleep(1000);
            if (fileSizes[i] == -2) continue;
            subtaskMessage = "Validating: " + getJarName(urlList[i]);
            File file = new File(path, getJarName(urlList[i]));
            if (!isZipValid(file)) {
                throw new Exception("The file " + getJarName(urlList[i]) + " is corrupt!");
            }
            percentage = 80 + (int) (percentageParts * i);
        }
        subtaskMessage = "";
    }

    /**
	 * This method will check if a zip file is valid by running through it
	 * and checking for any corruption and CRC failures
	 * 
	 * @param file - zip file to test
	 * @return boolean - runs false if the file is corrupt
	 */
    protected boolean isZipValid(File file) {
        try {
            ZipFile zipFile = new ZipFile(file);
            try {
                Enumeration e = zipFile.entries();
                byte[] buffer = new byte[4096];
                while (e.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) e.nextElement();
                    CRC32 crc = new CRC32();
                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                    CheckedInputStream cis = new CheckedInputStream(bis, crc);
                    while (cis.read(buffer, 0, buffer.length) != -1) {
                    }
                    if (crc.getValue() != zipEntry.getCrc()) {
                        return false;
                    }
                }
                return true;
            } finally {
                zipFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * Get Image from path provided
	 *
	 * @param s location of the image
	 * @return the Image file
	 */
    protected Image getImage(String s) {
        if (s.length() == 0) return null;
        Image image = null;
        try {
            image = getImage(new URL(getCodeBase(), s));
        } catch (Exception e) {
        }
        if (image == null) {
            image = getImage(Thread.currentThread().getContextClassLoader().getResource(s));
        }
        if (image != null) {
            return image;
        }
        fatalErrorOccured("Unable to load the logo/progressbar image: " + s, null);
        return null;
    }

    /**
	 * Get Image from path provided
	 *
	 * @param url location of the image
	 * @return the Image file
	 */
    public Image getImage(URL url) {
        try {
            MediaTracker tracker = new MediaTracker(this);
            Image image = super.getImage(url);
            tracker.addImage(image, 0);
            tracker.waitForAll();
            if (!tracker.isErrorAny()) {
                return image;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * Get jar name from URL.
	 *
	 * @param url Get jar file name from this url
	 * @return file name as string
	 */
    protected String getJarName(URL url) {
        String fileName = url.getFile();
        if (fileName.endsWith(".pack.lzma")) {
            fileName = replaceLast(fileName, ".pack.lzma", "");
        } else if (fileName.endsWith(".pack.gz")) {
            fileName = replaceLast(fileName, ".pack.gz", "");
        } else if (fileName.endsWith(".pack")) {
            fileName = replaceLast(fileName, ".pack", "");
        } else if (fileName.endsWith(".lzma")) {
            fileName = replaceLast(fileName, ".lzma", "");
        } else if (fileName.endsWith(".gz")) {
            fileName = replaceLast(fileName, ".gz", "");
        }
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    /**
	 * Get file name portion of URL.
	 *
	 * @param url Get file name from this url
	 * @return file name as string
	 */
    protected String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    /**
	 * Retrieves the color
	 *
	 * @param param Color to load
	 * @param defaultColor Default color to use if no color to load
	 * @return Color to use
	 */
    protected Color getColor(String param, Color defaultColor) {
        String color = getParameter(param);
        if (color == null) return defaultColor;
        if (color.indexOf(",") != -1) {
            StringTokenizer st = new StringTokenizer(color, ",");
            try {
                return new Color(Integer.parseInt(st.nextToken().trim()), Integer.parseInt(st.nextToken().trim()), Integer.parseInt(st.nextToken().trim()));
            } catch (Exception e) {
                return defaultColor;
            }
        }
        try {
            return Color.decode(color);
        } catch (NumberFormatException e) {
        }
        try {
            return (Color) Color.class.getField(color).get(null);
        } catch (Exception e) {
            return defaultColor;
        }
    }

    /**
	 * Replaces the last occurrence of the specified target substring with
	 * the specified replacement string in a string.
	 * 
	 * @param original - String to search
	 * @param target - substring to find
	 * @param replacement - what to replace target substring with
	 * @return - return the modified string, if target substring not found return original string
	 */
    public String replaceLast(String original, String target, String replacement) {
        int index = original.lastIndexOf(target);
        if (index == -1) {
            return original;
        }
        return original.substring(0, index) + replacement + original.substring(index + target.length());
    }

    /**
	 * Retrieves the String value for the parameter
	 * @param name Name of parameter
	 * @param defaultValue default value to return if no such parameter
	 * @return value of parameter or defaultValue
	 */
    protected String getStringParameter(String name, String defaultValue) {
        String parameter = getParameter(name);
        if (parameter != null) {
            return parameter;
        }
        return defaultValue;
    }

    /**
	 * Retrieves the boolean value for the parameter
	 * @param name Name of parameter
	 * @param defaultValue default value to return if no such parameter
	 * @return value of parameter or defaultValue
	 */
    protected boolean getBooleanParameter(String name, boolean defaultValue) {
        String parameter = getParameter(name);
        if (parameter != null) {
            return Boolean.parseBoolean(parameter);
        }
        return defaultValue;
    }

    /**
	 * Retrieves the int value for the applet
	 * @param name Name of parameter
	 * @param defaultValue default value to return if no such parameter
	 * @return value of parameter or defaultValue
	 */
    protected int getIntParameter(String name, int defaultValue) {
        String parameter = getParameter(name);
        if (parameter != null) {
            return Integer.parseInt(parameter);
        }
        return defaultValue;
    }

    /**
	 * Sets the error message and print debug information
	 *
	 * @param error Error message to print
	 */
    protected void fatalErrorOccured(String error, Exception e) {
        fatalError = true;
        if (minimumJreNotFound) {
            errorMessage = minimumJREMessage;
            errorMessage[errorMessage.length - 1] = error;
        } else if (certificateRefused) {
            errorMessage = certificateRefusedMessage;
        } else {
            errorMessage = genericErrorMessage;
            errorMessage[errorMessage.length - 1] = error;
        }
        System.out.println(error);
        if (e != null) {
            System.out.println(e.getMessage());
            System.out.println(generateStacktrace(e));
        }
        repaint();
    }

    /** 
	 * set the state of applet loader 
	 * @param new state of applet loader
	 * */
    protected void setState(int state) {
        this.state = state;
        if (debugMode) {
            System.out.println(getDescriptionForState());
        }
    }

    /**
	 * Utility method for sleeping
	 * Will only really sleep if debug has been enabled
	 * @param ms milliseconds to sleep
	 */
    protected void debug_sleep(long ms) {
        if (debugMode) {
            sleep(ms);
        }
    }

    /**
	 * Utility method for sleeping
	 * @param ms milliseconds to sleep
	 */
    protected void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
        }
    }
}
