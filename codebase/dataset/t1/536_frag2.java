public class JavaMailServlet extends HttpServlet implements SingleThreadModel {



    String protocol = "imap";



    String mbox = "INBOX";



    /**

     * This method handles the "POST" submission from two forms: the

     * login form and the message compose form. The login form has the

     * following parameters: <code>hostname</code>, <code>username</code>,

     * and <code>password</code>. The <code>send</code> parameter denotes

     * that the method is processing the compose form submission.

     */

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession ssn = req.getSession(true);

        String send = req.getParameter("send");

        String host = req.getParameter("hostname");

        String user = req.getParameter("username");

        String passwd = req.getParameter("password");

        URLName url = new URLName(protocol, host, -1, mbox, user, passwd);

        ServletOutputStream out = res.getOutputStream();

        res.setContentType("text/html");

        out.println("<html><body bgcolor=\"#CCCCFF\">");

        if (send != null) {

            send(req, res, out, ssn);

        } else {

            MailUserData mud = new MailUserData(url);

            ssn.putValue("javamailservlet", mud);

            try {

                Properties props = System.getProperties();

                props.put("mail.smtp.host", host);

                Session session = Session.getDefaultInstance(props, null);

                session.setDebug(false);

                Store store = session.getStore(url);

                store.connect();

                Folder folder = store.getDefaultFolder();

                if (folder == null) throw new MessagingException("No default folder");

                folder = folder.getFolder(mbox);

                if (folder == null) throw new MessagingException("Invalid folder");

                folder.open(Folder.READ_WRITE);

                int totalMessages = folder.getMessageCount();

                Message[] msgs = folder.getMessages();

                FetchProfile fp = new FetchProfile();

                fp.add(FetchProfile.Item.ENVELOPE);

                folder.fetch(msgs, fp);

                System.out.println("Login from: " + store.getURLName());

                mud.setSession(session);

                mud.setStore(store);

                mud.setFolder(folder);

                out.print("<center>");

                out.print("<font face=\"Arial,Helvetica\" font size=+3>");

                out.println("<b>Welcome to JavaMail!</b></font></center><p>");

                out.println("<table width=\"50%\" border=0 align=center>");

                out.print("<tr><td width=\"75%\" bgcolor=\"#ffffcc\">");

                out.print("<font face=\"Arial,Helvetica\" font size=-1>");

                out.println("<b>FolderName</b></font></td><br>");

                out.print("<td width=\"25%\" bgcolor=\"#ffffcc\">");

                out.print("<font face=\"Arial,Helvetica\" font size=-1>");

                out.println("<b>Messages</b></font></td><br>");

                out.println("</tr>");

                out.print("<tr><td width=\"75%\" bgcolor=\"#ffffff\">");

                out.print("<a href=\"" + HttpUtils.getRequestURL(req) + "\">" + "Inbox" + "</a></td><br>");

                out.println("<td width=\"25%\" bgcolor=\"#ffffff\">" + totalMessages + "</td>");

                out.println("</tr>");

                out.println("</table");

            } catch (Exception ex) {

                out.println(ex.toString());

            } finally {

                out.println("</body></html>");

                out.close();

            }

        }

    }



    /**

     * This method handles the GET requests for the client.

     */

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        HttpSession ses = req.getSession(false);

        ServletOutputStream out = res.getOutputStream();

        MailUserData mud = getMUD(ses);

        if (mud == null) {

            res.setContentType("text/html");

            out.println("<html><body>Please Login (no session)</body></html>");

            out.close();

            return;

        }

        if (!mud.getStore().isConnected()) {

            res.setContentType("text/html");

            out.println("<html><body>Not Connected To Store</body></html>");

            out.close();

            return;

        }

        String msgStr = req.getParameter("message");

        String logout = req.getParameter("logout");

        String compose = req.getParameter("compose");

        String part = req.getParameter("part");

        int msgNum = -1;

        int partNum = -1;

        if (msgStr != null) {

            msgNum = Integer.parseInt(msgStr);

            if (part == null) {

                res.setContentType("text/html");

                displayMessage(mud, req, out, msgNum);

            } else if (part != null) {

                partNum = Integer.parseInt(part);

                displayPart(mud, msgNum, partNum, out, res);

            }

        } else if (compose != null) {

            compose(mud, res, out);

        } else if (logout != null) {

            try {

                mud.getFolder().close(false);

                mud.getStore().close();

                ses.invalidate();

                out.println("<html><body>Logged out OK</body></html>");

            } catch (MessagingException mex) {

                out.println(mex.toString());

            }

        } else {

            displayHeaders(mud, req, out);

        }

    }



    private void displayMessage(MailUserData mud, HttpServletRequest req, ServletOutputStream out, int msgNum) throws IOException {

        out.println("<html>");

        out.println("<HEAD><TITLE>JavaMail Servlet</TITLE></HEAD>");

        out.println("<BODY bgcolor=\"#ccccff\">");

        out.print("<center><font face=\"Arial,Helvetica\" ");

        out.println("font size=\"+3\"><b>");

        out.println("Message " + (msgNum + 1) + " in folder " + mud.getStore().getURLName() + "/INBOX</b></font></center><p>");

        try {

            Message msg = mud.getFolder().getMessage(msgNum);

            displayMessageHeaders(mud, msg, out);

            Object o = msg.getContent();

            if (msg.isMimeType("text/plain")) {

                out.println("<pre>");

                out.println((String) o);

                out.println("</pre>");

            } else if (msg.isMimeType("multipart/*")) {

                Multipart mp = (Multipart) o;

                int cnt = mp.getCount();

                for (int i = 0; i < cnt; i++) {

                    displayPart(mud, msgNum, mp.getBodyPart(i), i, req, out);

                }

            } else {

                out.println(msg.getContentType());

            }

        } catch (MessagingException mex) {

            out.println(mex.toString());

        }

        out.println("</BODY></html>");

        out.close();

    }



    /** 

     * This method displays a message part. <code>text/plain</code>

     * content parts are displayed inline. For all other parts,

     * a URL is generated and displayed; clicking on the URL

     * brings up the part in a separate page.

     */

    private void displayPart(MailUserData mud, int msgNum, Part part, int partNum, HttpServletRequest req, ServletOutputStream out) throws IOException {

        if (partNum != 0) out.println("<p><hr>");

        try {

            String sct = part.getContentType();

            if (sct == null) {

                out.println("invalid part");

                return;

            }

            ContentType ct = new ContentType(sct);

            if (partNum != 0) out.println("<b>Attachment Type:</b> " + ct.getBaseType() + "<br>");

            if (ct.match("text/plain")) {

                out.println("<pre>");

                out.println((String) part.getContent());

                out.println("</pre>");

            } else {

                String s;

                if ((s = part.getFileName()) != null) out.println("<b>Filename:</b> " + s + "<br>");

                s = null;

                if ((s = part.getDescription()) != null) out.println("<b>Description:</b> " + s + "<br>");

                out.println("<a href=\"" + HttpUtils.getRequestURL(req) + "?message=" + msgNum + "&part=" + partNum + "\">Display Attachment</a>");

            }

        } catch (MessagingException mex) {

            out.println(mex.toString());

        }

    }



    /**

     * This method gets the stream from for a given msg part and 

     * pushes it out to the browser with the correct content type.

     * Used to display attachments and relies on the browser's

     * content handling capabilities.

     */

    private void displayPart(MailUserData mud, int msgNum, int partNum, ServletOutputStream out, HttpServletResponse res) throws IOException {

        Part part = null;

        try {

            Message msg = mud.getFolder().getMessage(msgNum);

            Multipart mp = (Multipart) msg.getContent();

            part = mp.getBodyPart(partNum);

            String sct = part.getContentType();

            if (sct == null) {

                out.println("invalid part");

                return;

            }

            ContentType ct = new ContentType(sct);

            res.setContentType(ct.getBaseType());

            InputStream is = part.getInputStream();

            int i;

            while ((i = is.read()) != -1) out.write(i);

            out.flush();

            out.close();

        } catch (MessagingException mex) {

            out.println(mex.toString());

        }

    }



    /**

     * This is a utility message that pretty-prints the message 

     * headers for message that is being displayed.

     */

    private void displayMessageHeaders(MailUserData mud, Message msg, ServletOutputStream out) throws IOException {

        try {

            out.println("<b>Date:</b> " + msg.getSentDate() + "<br>");

            Address[] fr = msg.getFrom();

            if (fr != null) {

                boolean tf = true;

                out.print("<b>From:</b> ");

                for (int i = 0; i < fr.length; i++) {

                    out.print(((tf) ? " " : ", ") + getDisplayAddress(fr[i]));

                    tf = false;

                }

                out.println("<br>");

            }

            Address[] to = msg.getRecipients(Message.RecipientType.TO);

            if (to != null) {

                boolean tf = true;

                out.print("<b>To:</b> ");

                for (int i = 0; i < to.length; i++) {

                    out.print(((tf) ? " " : ", ") + getDisplayAddress(to[i]));

                    tf = false;

                }

                out.println("<br>");

            }

            Address[] cc = msg.getRecipients(Message.RecipientType.CC);

            if (cc != null) {

                boolean cf = true;

                out.print("<b>CC:</b> ");

                for (int i = 0; i < cc.length; i++) {

                    out.print(((cf) ? " " : ", ") + getDisplayAddress(cc[i]));

                    cf = false;

                }

                out.println("<br>");

            }

            out.print("<b>Subject:</b> " + ((msg.getSubject() != null) ? msg.getSubject() : "") + "<br>");

        } catch (MessagingException mex) {

            out.println(msg.toString());

        }

    }



    /**

     * This method displays the URL's for the available commands and the

     * INBOX headerlist 

     */

    private void displayHeaders(MailUserData mud, HttpServletRequest req, ServletOutputStream out) throws IOException {

        SimpleDateFormat df = new SimpleDateFormat("EE M/d/yy");

        out.println("<html>");

        out.println("<HEAD><TITLE>JavaMail Servlet</TITLE></HEAD>");

        out.println("<BODY bgcolor=\"#ccccff\"><hr>");

        out.print("<center><font face=\"Arial,Helvetica\" font size=\"+3\">");

        out.println("<b>Folder " + mud.getStore().getURLName() + "/INBOX</b></font></center><p>");

        out.println("<font face=\"Arial,Helvetica\" font size=\"+3\"><b>");

        out.println("<a href=\"" + HttpUtils.getRequestURL(req) + "?logout=true\">Logout</a>");

        out.println("<a href=\"" + HttpUtils.getRequestURL(req) + "?compose=true\" target=\"compose\">Compose</a>");

        out.println("</b></font>");

        out.println("<hr>");

        out.print("<table cellpadding=1 cellspacing=1 ");

        out.println("width=\"100%\" border=1>");

        out.println("<tr><td width=\"25%\" bgcolor=\"ffffcc\">");

        out.println("<font face=\"Arial,Helvetica\" font size=\"+1\">");

        out.println("<b>Sender</b></font></td>");

        out.println("<td width=\"15%\" bgcolor=\"ffffcc\">");

        out.println("<font face=\"Arial,Helvetica\" font size=\"+1\">");

        out.println("<b>Date</b></font></td>");

        out.println("<td bgcolor=\"ffffcc\">");

        out.println("<font face=\"Arial,Helvetica\" font size=\"+1\">");

        out.println("<b>Subject</b></font></td></tr>");

        try {

            Folder f = mud.getFolder();

            int msgCount = f.getMessageCount();

            Message m = null;

            for (int i = 1; i <= msgCount; i++) {

                m = f.getMessage(i);

                if (m.isSet(Flags.Flag.DELETED)) continue;

                out.println("<tr valigh=middle>");

                out.print("<td width=\"25%\" bgcolor=\"ffffff\">");

                out.println("<font face=\"Arial,Helvetica\">" + ((m.getFrom() != null) ? m.getFrom()[0].toString() : "") + "</font></td>");

                out.print("<td nowrap width=\"15%\" bgcolor=\"ffffff\">");

                out.println("<font face=\"Arial,Helvetica\">" + df.format((m.getSentDate() != null) ? m.getSentDate() : m.getReceivedDate()) + "</font></td>");

                out.print("<td bgcolor=\"ffffff\">");

                out.println("<font face=\"Arial,Helvetica\">" + "<a href=\"" + HttpUtils.getRequestURL(req) + "?message=" + i + "\">" + ((m.getSubject() != null) ? m.getSubject() : "<i>No Subject</i>") + "</a>" + "</font></td>");

                out.println("</tr>");

            }

        } catch (MessagingException mex) {

            out.println("<tr><td>" + mex.toString() + "</td></tr>");

            mex.printStackTrace();

        }

        out.println("</table>");

        out.println("</BODY></html>");

        out.flush();

        out.close();

    }



    /** 

     * This method handles the request when the user hits the

     * <i>Compose</i> link. It send the compose form to the browser.

     */

    private void compose(MailUserData mud, HttpServletResponse res, ServletOutputStream out) throws IOException {

        res.setContentType("text/html");

        out.println(composeForm);

        out.close();

    }



    /**

     * This method processes the send request from the compose form

     */

    private void send(HttpServletRequest req, HttpServletResponse res, ServletOutputStream out, HttpSession ssn) throws IOException {

        String to = req.getParameter("to");

        String cc = req.getParameter("cc");

        String subj = req.getParameter("subject");

        String text = req.getParameter("text");

        try {

            MailUserData mud = getMUD(ssn);

            if (mud == null) throw new Exception("trying to send, but not logged in");

            Message msg = new MimeMessage(mud.getSession());

            InternetAddress[] toAddrs = null, ccAddrs = null;

            if (to != null) {

                toAddrs = InternetAddress.parse(to, false);

                msg.setRecipients(Message.RecipientType.TO, toAddrs);

            } else throw new MessagingException("No \"To\" address specified");

            if (cc != null) {

                ccAddrs = InternetAddress.parse(cc, false);

                msg.setRecipients(Message.RecipientType.CC, ccAddrs);

            }

            if (subj != null) msg.setSubject(subj);

            URLName u = mud.getURLName();

            msg.setFrom(new InternetAddress(u.getUsername() + "@" + u.getHost()));

            if (text != null) msg.setText(text);

            Transport.send(msg);

            out.println("<h1>Message sent successfully</h1></body></html>");

            out.close();

        } catch (Exception mex) {

            out.println("<h1>Error sending message.</h1>");

            out.println(mex.toString());

            out.println("<br></body></html>");

        }

    }



    private String getDisplayAddress(Address a) {

        String pers = null;

        String addr = null;

        if (a instanceof InternetAddress && ((pers = ((InternetAddress) a).getPersonal()) != null)) {

            addr = pers + "  " + "&lt;" + ((InternetAddress) a).getAddress() + "&gt;";

        } else addr = a.toString();

        return addr;

    }



    private MailUserData getMUD(HttpSession ses) throws IOException {

        MailUserData mud = null;

        if (ses == null) {

            return null;

        } else {

            if ((mud = (MailUserData) ses.getValue("javamailservlet")) == null) {

                return null;

            }

        }

        return mud;

    }



    public String getServletInfo() {

        return "A mail reader servlet";

    }



    /**

     * This is the HTML code for the compose form. Another option would

     * have been to use a separate html page.

     */

    private static String composeForm = "<HTML><HEAD><TITLE>JavaMail Compose</TITLE></HEAD><BODY BGCOLOR=\"#CCCCFF\"><FORM ACTION=\"/servlet/JavaMailServlet\" METHOD=\"POST\"><input type=\"hidden\" name=\"send\" value=\"send\"><P ALIGN=\"CENTER\"><B><FONT SIZE=\"4\" FACE=\"Verdana, Arial, Helvetica\">JavaMail Compose Message</FONT></B><P><TABLE BORDER=\"0\" WIDTH=\"100%\"><TR><TD WIDTH=\"16%\" HEIGHT=\"22\">	<P ALIGN=\"RIGHT\"><B><FONT FACE=\"Verdana, Arial, Helvetica\">To:</FONT></B></TD><TD WIDTH=\"84%\" HEIGHT=\"22\"><INPUT TYPE=\"TEXT\" NAME=\"to\" SIZE=\"30\"> <FONT SIZE=\"1\" FACE=\"Verdana, Arial, Helvetica\"> (separate addresses with commas)</FONT></TD></TR><TR><TD WIDTH=\"16%\"><P ALIGN=\"RIGHT\"><B><FONT FACE=\"Verdana, Arial, Helvetica\">CC:</FONT></B></TD><TD WIDTH=\"84%\"><INPUT TYPE=\"TEXT\" NAME=\"cc\" SIZE=\"30\"> <FONT SIZE=\"1\" FACE=\"Verdana, Arial, Helvetica\"> (separate addresses with commas)</FONT></TD></TR><TR><TD WIDTH=\"16%\"><P ALIGN=\"RIGHT\"><B><FONT FACE=\"Verdana, Arial, Helvetica\">Subject:</FONT></B></TD><TD WIDTH=\"84%\"><INPUT TYPE=\"TEXT\" NAME=\"subject\" SIZE=\"55\"></TD></TR><TR><TD WIDTH=\"16%\">&nbsp;</TD><TD WIDTH=\"84%\"><TEXTAREA NAME=\"text\" ROWS=\"15\" COLS=\"53\"></TEXTAREA></TD></TR><TR><TD WIDTH=\"16%\" HEIGHT=\"32\">&nbsp;</TD><TD WIDTH=\"84%\" HEIGHT=\"32\"><INPUT TYPE=\"SUBMIT\" NAME=\"Send\" VALUE=\"Send\"><INPUT TYPE=\"RESET\" NAME=\"Reset\" VALUE=\"Reset\"></TD></TR></TABLE></FORM></BODY></HTML>";

}



/**

 * This class is used to store session data for each user's session. It

 * is stored in the HttpSession.

 */

class MailUserData {



    URLName url;



    Session session;



    Store store;



    Folder folder;



    public MailUserData(URLName urlname) {

        url = urlname;

    }



    public URLName getURLName() {

        return url;

    }



    public Session getSession() {

        return session;

    }



    public void setSession(Session s) {

        session = s;

    }



    public Store getStore() {

        return store;

    }



    public void setStore(Store s) {

        store = s;

    }



    public Folder getFolder() {

        return folder;

    }



    public void setFolder(Folder f) {

        folder = f;

    }

}
