package sun.applet;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import sun.misc.Ref;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A frame to show the applet tag in.
 */
class TextFrame extends Frame {

    /**
     * Create the tag frame.
     */
    TextFrame(int x, int y, String title, String text) {
        setTitle(title);
        TextArea txt = new TextArea(20, 60);
        txt.setText(text);
        txt.setEditable(false);
        add("Center", txt);
        Panel p = new Panel();
        add("South", p);
        Button b = new Button(amh.getMessage("button.dismiss", "Dismiss"));
        p.add(b);
        class ActionEventListener implements ActionListener {

            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        }
        b.addActionListener(new ActionEventListener());
        pack();
        move(x, y);
        setVisible(true);
        WindowListener windowEventListener = new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                dispose();
            }
        };
        addWindowListener(windowEventListener);
    }

    private static AppletMessageHandler amh = new AppletMessageHandler("textframe");
}

/**
 * Lets us construct one using unix-style one shot behaviors
 */
class StdAppletViewerFactory implements AppletViewerFactory {

    public AppletViewer createAppletViewer(int x, int y, URL doc, Hashtable atts) {
        return new AppletViewer(x, y, doc, atts, System.out, this);
    }

    public MenuBar getBaseMenuBar() {
        return new MenuBar();
    }

    public boolean isStandalone() {
        return true;
    }
}

/**
 * The applet viewer makes it possible to run a Java applet without using a browser.
 * For details on the syntax that <B>appletviewer</B> supports, see
 * <a href="../../../docs/tooldocs/appletviewertags.html">AppletViewer Tags</a>.
 * (The document named appletviewertags.html in the JDK's docs/tooldocs directory,
 *  once the JDK docs have been installed.)
 */
public class AppletViewer extends Frame implements AppletContext {

    /**
     * Some constants...
     */
    private static String defaultSaveFile = "Applet.ser";

    /**
     * The panel in which the applet is being displayed.
     */
    AppletViewerPanel panel;

    /**
     * The status line.
     */
    Label label;

    /**
     * output status messages to this stream
     */
    PrintStream statusMsgStream;

    /**
     * For cloning
     */
    AppletViewerFactory factory;

    private final class UserActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            processUserAction(evt);
        }
    }

    /**
     * Create the applet viewer
     */
    public AppletViewer(int x, int y, URL doc, Hashtable atts, PrintStream statusMsgStream, AppletViewerFactory factory) {
        this.factory = factory;
        this.statusMsgStream = statusMsgStream;
        setTitle(amh.getMessage("tool.title", atts.get("code")));
        MenuBar mb = factory.getBaseMenuBar();
        Menu m = new Menu(amh.getMessage("menu.applet"));
        addMenuItem(m, "menuitem.restart");
        addMenuItem(m, "menuitem.reload");
        addMenuItem(m, "menuitem.stop");
        addMenuItem(m, "menuitem.save");
        addMenuItem(m, "menuitem.start");
        addMenuItem(m, "menuitem.clone");
        m.add(new MenuItem("-"));
        addMenuItem(m, "menuitem.tag");
        addMenuItem(m, "menuitem.info");
        addMenuItem(m, "menuitem.edit").disable();
        addMenuItem(m, "menuitem.encoding");
        m.add(new MenuItem("-"));
        addMenuItem(m, "menuitem.props");
        m.add(new MenuItem("-"));
        addMenuItem(m, "menuitem.close");
        if (factory.isStandalone()) {
            addMenuItem(m, "menuitem.quit");
        }
        mb.add(m);
        setMenuBar(mb);
        add("Center", panel = new AppletViewerPanel(doc, atts));
        add("South", label = new Label(amh.getMessage("label.hello")));
        panel.init();
        appletPanels.addElement(panel);
        pack();
        move(x, y);
        setVisible(true);
        WindowListener windowEventListener = new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                appletClose();
            }

            public void windowIconified(WindowEvent evt) {
                appletStop();
            }

            public void windowDeiconified(WindowEvent evt) {
                appletStart();
            }
        };
        class AppletEventListener implements AppletListener {

            public void appletStateChanged(AppletEvent evt) {
                switch(evt.getID()) {
                    case AppletPanel.APPLET_RESIZE:
                        {
                            AppletPanel src = (AppletPanel) evt.getSource();
                            if (src != null) {
                                setSize(getPreferredSize());
                                validate();
                            }
                        }
                }
            }
        }
        ;
        addWindowListener(windowEventListener);
        panel.addAppletListener(new AppletEventListener());
        showStatus(amh.getMessage("status.start"));
        initEventQueue();
    }

    public MenuItem addMenuItem(Menu m, String s) {
        MenuItem mItem = new MenuItem(amh.getMessage(s));
        mItem.addActionListener(new UserActionListener());
        return m.add(mItem);
    }

    /**
     * Send the initial set of events to the appletviewer event queue.
     * On start-up the current behaviour is to load the applet and call
     * Applet.init() and Applet.start().
     */
    private void initEventQueue() {
        String eventList = System.getProperty("appletviewer.send.event");
        if (eventList == null) {
            panel.sendEvent(AppletPanel.APPLET_LOAD);
            panel.sendEvent(AppletPanel.APPLET_INIT);
            panel.sendEvent(AppletPanel.APPLET_START);
        } else {
            String[] events = splitSeparator(",", eventList);
            for (int i = 0; i < events.length; i++) {
                System.out.println("Adding event to queue: " + events[i]);
                if (events[i].equals("dispose")) panel.sendEvent(AppletPanel.APPLET_DISPOSE); else if (events[i].equals("load")) panel.sendEvent(AppletPanel.APPLET_LOAD); else if (events[i].equals("init")) panel.sendEvent(AppletPanel.APPLET_INIT); else if (events[i].equals("start")) panel.sendEvent(AppletPanel.APPLET_START); else if (events[i].equals("stop")) panel.sendEvent(AppletPanel.APPLET_STOP); else if (events[i].equals("destroy")) panel.sendEvent(AppletPanel.APPLET_DESTROY); else if (events[i].equals("quit")) panel.sendEvent(AppletPanel.APPLET_QUIT); else if (events[i].equals("error")) panel.sendEvent(AppletPanel.APPLET_ERROR); else System.out.println("Unrecognized event name: " + events[i]);
            }
            while (!panel.emptyEventQueue()) ;
            appletSystemExit();
        }
    }

    /**
     * Split a string based on the presence of a specified separator.  Returns
     * an array of arbitrary length.  The end of each element in the array is
     * indicated by the separator of the end of the string.  If there is a
     * separator immediately before the end of the string, the final element
     * will be empty.  None of the strings will contain the separator.  Useful
     * when separating strings such as "foo/bar/bas" using separator "/".
     *
     * @param sep  The separator.
     * @param s    The string to split.
     * @return     An array of strings.  Each string in the array is determined
     *             by the location of the provided sep in the original string,
     *             s.  Whitespace not stripped.
     */
    private String[] splitSeparator(String sep, String s) {
        Vector v = new Vector();
        int tokenStart = 0;
        int tokenEnd = 0;
        while ((tokenEnd = s.indexOf(sep, tokenStart)) != -1) {
            v.addElement(s.substring(tokenStart, tokenEnd));
            tokenStart = tokenEnd + 1;
        }
        v.addElement(s.substring(tokenStart));
        String[] retVal = new String[v.size()];
        v.copyInto(retVal);
        return retVal;
    }

    private static Map audioClips = new HashMap();

    /**
     * Get an audio clip.
     */
    public AudioClip getAudioClip(URL url) {
        checkConnect(url);
        synchronized (audioClips) {
            AudioClip clip = (AudioClip) audioClips.get(url);
            if (clip == null) {
                audioClips.put(url, clip = new AppletAudioClip(url));
            }
            return clip;
        }
    }

    private static Map imageRefs = new HashMap();

    /**
     * Get an image.
     */
    public Image getImage(URL url) {
        return getCachedImage(url);
    }

    static Image getCachedImage(URL url) {
        return (Image) getCachedImageRef(url).get();
    }

    /**
     * Get an image ref.
     */
    static Ref getCachedImageRef(URL url) {
        synchronized (imageRefs) {
            AppletImageRef ref = (AppletImageRef) imageRefs.get(url);
            if (ref == null) {
                ref = new AppletImageRef(url);
                imageRefs.put(url, ref);
            }
            return ref;
        }
    }

    /**
     * Flush the image cache.
     */
    static void flushImageCache() {
        imageRefs.clear();
    }

    static Vector appletPanels = new Vector();

    /**
     * Get an applet by name.
     */
    public Applet getApplet(String name) {
        AppletSecurity security = (AppletSecurity) System.getSecurityManager();
        name = name.toLowerCase();
        SocketPermission panelSp = new SocketPermission(panel.getCodeBase().getHost(), "connect");
        for (Enumeration e = appletPanels.elements(); e.hasMoreElements(); ) {
            AppletPanel p = (AppletPanel) e.nextElement();
            String param = p.getParameter("name");
            if (param != null) {
                param = param.toLowerCase();
            }
            if (name.equals(param) && p.getDocumentBase().equals(panel.getDocumentBase())) {
                SocketPermission sp = new SocketPermission(p.getCodeBase().getHost(), "connect");
                if (panelSp.implies(sp)) {
                    return p.applet;
                }
            }
        }
        return null;
    }

    /**
     * Return an enumeration of all the accessible
     * applets on this page.
     */
    public Enumeration getApplets() {
        AppletSecurity security = (AppletSecurity) System.getSecurityManager();
        Vector v = new Vector();
        SocketPermission panelSp = new SocketPermission(panel.getCodeBase().getHost(), "connect");
        for (Enumeration e = appletPanels.elements(); e.hasMoreElements(); ) {
            AppletPanel p = (AppletPanel) e.nextElement();
            if (p.getDocumentBase().equals(panel.getDocumentBase())) {
                SocketPermission sp = new SocketPermission(p.getCodeBase().getHost(), "connect");
                if (panelSp.implies(sp)) {
                    v.addElement(p.applet);
                }
            }
        }
        return v.elements();
    }

    /**
     * Ignore.
     */
    public void showDocument(URL url) {
    }

    /**
     * Ignore.
     */
    public void showDocument(URL url, String target) {
    }

    /**
     * Show status.
     */
    public void showStatus(String status) {
        label.setText(status);
    }

    /**
     * System parameters.
     */
    static Hashtable systemParam = new Hashtable();

    static {
        systemParam.put("codebase", "codebase");
        systemParam.put("code", "code");
        systemParam.put("alt", "alt");
        systemParam.put("width", "width");
        systemParam.put("height", "height");
        systemParam.put("align", "align");
        systemParam.put("vspace", "vspace");
        systemParam.put("hspace", "hspace");
    }

    /**
     * Print the HTML tag.
     */
    public static void printTag(PrintStream out, Hashtable atts) {
        out.print("<applet");
        String v = (String) atts.get("codebase");
        if (v != null) {
            out.print(" codebase=\"" + v + "\"");
        }
        v = (String) atts.get("code");
        if (v == null) {
            v = "applet.class";
        }
        out.print(" code=\"" + v + "\"");
        v = (String) atts.get("width");
        if (v == null) {
            v = "150";
        }
        out.print(" width=" + v);
        v = (String) atts.get("height");
        if (v == null) {
            v = "100";
        }
        out.print(" height=" + v);
        v = (String) atts.get("name");
        if (v != null) {
            out.print(" name=\"" + v + "\"");
        }
        out.println(">");
        int len = atts.size();
        String params[] = new String[len];
        len = 0;
        for (Enumeration e = atts.keys(); e.hasMoreElements(); ) {
            String param = (String) e.nextElement();
            int i = 0;
            for (; i < len; i++) {
                if (params[i].compareTo(param) >= 0) {
                    break;
                }
            }
            System.arraycopy(params, i, params, i + 1, len - i);
            params[i] = param;
            len++;
        }
        for (int i = 0; i < len; i++) {
            String param = params[i];
            if (systemParam.get(param) == null) {
                out.println("<param name=" + param + " value=\"" + atts.get(param) + "\">");
            }
        }
        out.println("</applet>");
    }

    /**
     * Make sure the atrributes are uptodate.
     */
    public void updateAtts() {
        Dimension d = panel.getSize();
        Insets in = panel.getInsets();
        panel.atts.put("width", new Integer(d.width - (in.left + in.right)).toString());
        panel.atts.put("height", new Integer(d.height - (in.top + in.bottom)).toString());
    }

    /**
     * Restart the applet.
     */
    void appletRestart() {
        panel.sendEvent(AppletPanel.APPLET_STOP);
        panel.sendEvent(AppletPanel.APPLET_DESTROY);
        panel.sendEvent(AppletPanel.APPLET_INIT);
        panel.sendEvent(AppletPanel.APPLET_START);
    }

    /**
     * Reload the applet.
     */
    void appletReload() {
        panel.sendEvent(AppletPanel.APPLET_STOP);
        panel.sendEvent(AppletPanel.APPLET_DESTROY);
        panel.sendEvent(AppletPanel.APPLET_DISPOSE);
        AppletPanel.flushClassLoader(panel.baseURL);
        try {
            panel.joinAppletThread();
            panel.release();
        } catch (InterruptedException e) {
            return;
        }
        panel.createAppletThread();
        panel.sendEvent(AppletPanel.APPLET_LOAD);
        panel.sendEvent(AppletPanel.APPLET_INIT);
        panel.sendEvent(AppletPanel.APPLET_START);
    }

    /**
     * Save the applet to a well known file (for now) as a serialized object
     */
    void appletSave() {
        AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                panel.sendEvent(AppletPanel.APPLET_STOP);
                FileDialog fd = new FileDialog(AppletViewer.this, amh.getMessage("appletsave.filedialogtitle"), FileDialog.SAVE);
                fd.setDirectory(System.getProperty("user.dir"));
                fd.setFile(defaultSaveFile);
                fd.show();
                String fname = fd.getFile();
                if (fname == null) {
                    panel.sendEvent(AppletPanel.APPLET_START);
                    return null;
                }
                String dname = fd.getDirectory();
                File file = new File(dname, fname);
                try {
                    OutputStream s = new FileOutputStream(file);
                    ObjectOutputStream os = new ObjectOutputStream(s);
                    showStatus(amh.getMessage("appletsave.err1", panel.applet.toString(), file.toString()));
                    os.writeObject(panel.applet);
                } catch (IOException ex) {
                    System.err.println(amh.getMessage("appletsave.err2", ex));
                } finally {
                    panel.sendEvent(AppletPanel.APPLET_START);
                }
                return null;
            }
        });
    }

    /**
     * Clone the viewer and the applet.
     */
    void appletClone() {
        Point p = location();
        updateAtts();
        factory.createAppletViewer(p.x + XDELTA, p.y + YDELTA, panel.documentURL, (Hashtable) panel.atts.clone());
    }

    /**
     * Show the applet tag.
     */
    void appletTag() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        updateAtts();
        printTag(new PrintStream(out), panel.atts);
        showStatus(amh.getMessage("applettag"));
        Point p = location();
        new TextFrame(p.x + XDELTA, p.y + YDELTA, amh.getMessage("applettag.textframe"), out.toString());
    }

    /**
     * Show the applet info.
     */
    void appletInfo() {
        String str = panel.applet.getAppletInfo();
        if (str == null) {
            str = amh.getMessage("appletinfo.applet");
        }
        str += "\n\n";
        String atts[][] = panel.applet.getParameterInfo();
        if (atts != null) {
            for (int i = 0; i < atts.length; i++) {
                str += atts[i][0] + " -- " + atts[i][1] + " -- " + atts[i][2] + "\n";
            }
        } else {
            str += amh.getMessage("appletinfo.param");
        }
        Point p = location();
        new TextFrame(p.x + XDELTA, p.y + YDELTA, amh.getMessage("appletinfo.textframe"), str);
    }

    /**
     * Show character encoding type
     */
    void appletCharacterEncoding() {
        showStatus(amh.getMessage("appletencoding", encoding));
    }

    /**
     * Edit the applet.
     */
    void appletEdit() {
    }

    /**
     * Properties.
     */
    static AppletProps props;

    public static synchronized void networkProperties() {
        if (props == null) {
            props = new AppletProps();
        }
        props.addNotify();
        props.setVisible(true);
    }

    /**
     * Start the applet.
     */
    void appletStart() {
        panel.sendEvent(AppletPanel.APPLET_START);
    }

    /**
     * Stop the applet.
     */
    void appletStop() {
        panel.sendEvent(AppletPanel.APPLET_STOP);
    }

    /**
     * Shutdown a viewer.
     * Stop, Destroy, Dispose and Quit a viewer
     */
    private void appletShutdown(AppletPanel p) {
        p.sendEvent(AppletPanel.APPLET_STOP);
        p.sendEvent(AppletPanel.APPLET_DESTROY);
        p.sendEvent(AppletPanel.APPLET_DISPOSE);
        p.sendEvent(AppletPanel.APPLET_QUIT);
    }

    /**
     * Close this viewer.
     * Stop, Destroy, Dispose and Quit an AppletView, then
     * reclaim resources and exit the program if this is
     * the last applet.
     */
    void appletClose() {
        appletShutdown(panel);
        appletPanels.removeElement(panel);
        dispose();
        if (countApplets() == 0) {
            appletSystemExit();
        }
    }

    /**
     * Exit the program.
     * Exit from the program (if not stand alone) - do no clean-up
     */
    private void appletSystemExit() {
        if (factory.isStandalone()) System.exit(0);
    }

    /**
     * Quit all viewers.
     * Shutdown all viewers properly then
     * exit from the program (if not stand alone)
     */
    protected void appletQuit() {
        for (Enumeration e = appletPanels.elements(); e.hasMoreElements(); ) {
            AppletPanel p = (AppletPanel) e.nextElement();
            appletShutdown(p);
        }
        appletSystemExit();
    }

    /**
     * Handle events.
     */
    public void processUserAction(ActionEvent evt) {
        String label = ((MenuItem) evt.getSource()).getLabel();
        if (amh.getMessage("menuitem.restart").equals(label)) {
            appletRestart();
            return;
        }
        if (amh.getMessage("menuitem.reload").equals(label)) {
            appletReload();
            return;
        }
        if (amh.getMessage("menuitem.clone").equals(label)) {
            appletClone();
            return;
        }
        if (amh.getMessage("menuitem.stop").equals(label)) {
            appletStop();
            return;
        }
        if (amh.getMessage("menuitem.save").equals(label)) {
            appletSave();
            return;
        }
        if (amh.getMessage("menuitem.start").equals(label)) {
            appletStart();
            return;
        }
        if (amh.getMessage("menuitem.tag").equals(label)) {
            appletTag();
            return;
        }
        if (amh.getMessage("menuitem.info").equals(label)) {
            appletInfo();
            return;
        }
        if (amh.getMessage("menuitem.encoding").equals(label)) {
            appletCharacterEncoding();
            return;
        }
        if (amh.getMessage("menuitem.edit").equals(label)) {
            appletEdit();
            return;
        }
        if (amh.getMessage("menuitem.props").equals(label)) {
            networkProperties();
            return;
        }
        if (amh.getMessage("menuitem.close").equals(label)) {
            appletClose();
            return;
        }
        if (factory.isStandalone() && amh.getMessage("menuitem.quit").equals(label)) {
            appletQuit();
            return;
        }
    }

    /**
     * How many applets are running?
     */
    public static int countApplets() {
        return appletPanels.size();
    }

    /**
     * The current character.
     */
    static int c;

    /**
     * Scan spaces.
     */
    public static void skipSpace(Reader in) throws IOException {
        while ((c >= 0) && ((c == ' ') || (c == '\t') || (c == '\n') || (c == '\r'))) {
            c = in.read();
        }
    }

    /**
     * Scan identifier
     */
    public static String scanIdentifier(Reader in) throws IOException {
        StringBuffer buf = new StringBuffer();
        while (true) {
            if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9')) || (c == '_')) {
                buf.append((char) c);
                c = in.read();
            } else {
                return buf.toString();
            }
        }
    }

    /**
     * Scan tag
     */
    public static Hashtable scanTag(Reader in) throws IOException {
        Hashtable atts = new Hashtable();
        skipSpace(in);
        while (c >= 0 && c != '>') {
            String att = scanIdentifier(in);
            String val = "";
            skipSpace(in);
            if (c == '=') {
                int quote = -1;
                c = in.read();
                skipSpace(in);
                if ((c == '\'') || (c == '\"')) {
                    quote = c;
                    c = in.read();
                }
                StringBuffer buf = new StringBuffer();
                while ((c > 0) && (((quote < 0) && (c != ' ') && (c != '\t') && (c != '\n') && (c != '\r') && (c != '>')) || ((quote >= 0) && (c != quote)))) {
                    buf.append((char) c);
                    c = in.read();
                }
                if (c == quote) {
                    c = in.read();
                }
                skipSpace(in);
                val = buf.toString();
            }
            if (!val.equals("")) {
                atts.put(att.toLowerCase(), val);
            }
            while (true) {
                if ((c == '>') || (c < 0) || ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9')) || (c == '_')) break;
                c = in.read();
            }
        }
        return atts;
    }

    private static int x = 0;

    private static int y = 0;

    private static final int XDELTA = 30;

    private static final int YDELTA = XDELTA;

    static String encoding = null;

    private static Reader makeReader(InputStream is) {
        if (encoding != null) {
            try {
                return new BufferedReader(new InputStreamReader(is, encoding));
            } catch (IOException x) {
            }
        }
        InputStreamReader r = new InputStreamReader(is);
        encoding = r.getEncoding();
        return new BufferedReader(r);
    }

    /**
     * Scan an html file for <applet> tags
     */
    public static void parse(URL url, String enc) throws IOException {
        encoding = enc;
        parse(url, System.out, new StdAppletViewerFactory());
    }

    public static void parse(URL url) throws IOException {
        parse(url, System.out, new StdAppletViewerFactory());
    }

    public static void parse(URL url, PrintStream statusMsgStream, AppletViewerFactory factory) throws IOException {
        boolean isAppletTag = false;
        boolean isObjectTag = false;
        boolean isEmbedTag = false;
        String requiresNameWarning = amh.getMessage("parse.warning.requiresname");
        String paramOutsideWarning = amh.getMessage("parse.warning.paramoutside");
        String appletRequiresCodeWarning = amh.getMessage("parse.warning.applet.requirescode");
        String appletRequiresHeightWarning = amh.getMessage("parse.warning.applet.requiresheight");
        String appletRequiresWidthWarning = amh.getMessage("parse.warning.applet.requireswidth");
        String objectRequiresCodeWarning = amh.getMessage("parse.warning.object.requirescode");
        String objectRequiresHeightWarning = amh.getMessage("parse.warning.object.requiresheight");
        String objectRequiresWidthWarning = amh.getMessage("parse.warning.object.requireswidth");
        String embedRequiresCodeWarning = amh.getMessage("parse.warning.embed.requirescode");
        String embedRequiresHeightWarning = amh.getMessage("parse.warning.embed.requiresheight");
        String embedRequiresWidthWarning = amh.getMessage("parse.warning.embed.requireswidth");
        String appNotLongerSupportedWarning = amh.getMessage("parse.warning.appnotLongersupported");
        java.net.URLConnection conn = url.openConnection();
        Reader in = makeReader(conn.getInputStream());
        url = conn.getURL();
        int ydisp = 1;
        Hashtable atts = null;
        while (true) {
            c = in.read();
            if (c == -1) break;
            if (c == '<') {
                c = in.read();
                if (c == '/') {
                    c = in.read();
                    String nm = scanIdentifier(in);
                    if (nm.equalsIgnoreCase("applet") || nm.equalsIgnoreCase("object") || nm.equalsIgnoreCase("embed")) {
                        if (isObjectTag) {
                            if (atts.get("code") == null && atts.get("object") == null) {
                                statusMsgStream.println(objectRequiresCodeWarning);
                                atts = null;
                            }
                        }
                        if (atts != null) {
                            factory.createAppletViewer(x, y, url, atts);
                            x += XDELTA;
                            y += YDELTA;
                            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                            if ((x > d.width - 300) || (y > d.height - 300)) {
                                x = 0;
                                y = 2 * ydisp * YDELTA;
                                ydisp++;
                            }
                        }
                        atts = null;
                        isAppletTag = false;
                        isObjectTag = false;
                        isEmbedTag = false;
                    }
                } else {
                    String nm = scanIdentifier(in);
                    if (nm.equalsIgnoreCase("param")) {
                        Hashtable t = scanTag(in);
                        String att = (String) t.get("name");
                        if (att == null) {
                            statusMsgStream.println(requiresNameWarning);
                        } else {
                            String val = (String) t.get("value");
                            if (val == null) {
                                statusMsgStream.println(requiresNameWarning);
                            } else if (atts != null) {
                                atts.put(att.toLowerCase(), val);
                            } else {
                                statusMsgStream.println(paramOutsideWarning);
                            }
                        }
                    } else if (nm.equalsIgnoreCase("applet")) {
                        isAppletTag = true;
                        atts = scanTag(in);
                        if (atts.get("code") == null && atts.get("object") == null) {
                            statusMsgStream.println(appletRequiresCodeWarning);
                            atts = null;
                        } else if (atts.get("width") == null) {
                            statusMsgStream.println(appletRequiresWidthWarning);
                            atts = null;
                        } else if (atts.get("height") == null) {
                            statusMsgStream.println(appletRequiresHeightWarning);
                            atts = null;
                        }
                    } else if (nm.equalsIgnoreCase("object")) {
                        isObjectTag = true;
                        atts = scanTag(in);
                        if (atts.get("codebase") != null) {
                            atts.remove("codebase");
                        }
                        if (atts.get("width") == null) {
                            statusMsgStream.println(objectRequiresWidthWarning);
                            atts = null;
                        } else if (atts.get("height") == null) {
                            statusMsgStream.println(objectRequiresHeightWarning);
                            atts = null;
                        }
                    } else if (nm.equalsIgnoreCase("embed")) {
                        isEmbedTag = true;
                        atts = scanTag(in);
                        if (atts.get("code") == null && atts.get("object") == null) {
                            statusMsgStream.println(embedRequiresCodeWarning);
                            atts = null;
                        } else if (atts.get("width") == null) {
                            statusMsgStream.println(embedRequiresWidthWarning);
                            atts = null;
                        } else if (atts.get("height") == null) {
                            statusMsgStream.println(embedRequiresHeightWarning);
                            atts = null;
                        }
                    } else if (nm.equalsIgnoreCase("app")) {
                        statusMsgStream.println(appNotLongerSupportedWarning);
                        Hashtable atts2 = scanTag(in);
                        nm = (String) atts2.get("class");
                        if (nm != null) {
                            atts2.remove("class");
                            atts2.put("code", nm + ".class");
                        }
                        nm = (String) atts2.get("src");
                        if (nm != null) {
                            atts2.remove("src");
                            atts2.put("codebase", nm);
                        }
                        if (atts2.get("width") == null) {
                            atts2.put("width", "100");
                        }
                        if (atts2.get("height") == null) {
                            atts2.put("height", "100");
                        }
                        printTag(statusMsgStream, atts2);
                        statusMsgStream.println();
                    }
                }
            }
        }
        in.close();
    }

    /**
     * Old main entry point.
     *
     * @deprecated
     */
    public static void main(String argv[]) {
        Main.main(argv);
    }

    private static AppletMessageHandler amh = new AppletMessageHandler("appletviewer");

    private static void checkConnect(URL url) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            try {
                java.security.Permission perm = url.openConnection().getPermission();
                if (perm != null) security.checkPermission(perm); else security.checkConnect(url.getHost(), url.getPort());
            } catch (java.io.IOException ioe) {
                security.checkConnect(url.getHost(), url.getPort());
            }
        }
    }
}
