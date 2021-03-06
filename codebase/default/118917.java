import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.*;
import javax.activation.*;

public class search {

    static String protocol = "imap";

    static String host = null;

    static String user = null;

    static String password = null;

    static String mbox = "INBOX";

    static String url = null;

    static boolean debug = false;

    public static void main(String argv[]) {
        int optind;
        String subject = null;
        String from = null;
        boolean or = false;
        boolean today = false;
        for (optind = 0; optind < argv.length; optind++) {
            if (argv[optind].equals("-T")) {
                protocol = argv[++optind];
            } else if (argv[optind].equals("-H")) {
                host = argv[++optind];
            } else if (argv[optind].equals("-U")) {
                user = argv[++optind];
            } else if (argv[optind].equals("-P")) {
                password = argv[++optind];
            } else if (argv[optind].equals("-or")) {
                or = true;
            } else if (argv[optind].equals("-D")) {
                debug = true;
            } else if (argv[optind].equals("-f")) {
                mbox = argv[++optind];
            } else if (argv[optind].equals("-L")) {
                url = argv[++optind];
            } else if (argv[optind].equals("-subject")) {
                subject = argv[++optind];
            } else if (argv[optind].equals("-from")) {
                from = argv[++optind];
            } else if (argv[optind].equals("-today")) {
                today = true;
            } else if (argv[optind].equals("--")) {
                optind++;
                break;
            } else if (argv[optind].startsWith("-")) {
                System.out.println("Usage: search [-D] [-L url] [-T protocol] [-H host] " + "[-U user] [-P password] [-f mailbox] " + "[-subject subject] [-from from] [-or] [-today]");
                System.exit(1);
            } else {
                break;
            }
        }
        try {
            if ((subject == null) && (from == null) && !today) {
                System.out.println("Specify either -subject, -from or -today");
                System.exit(1);
            }
            Properties props = System.getProperties();
            Session session = Session.getInstance(props, null);
            session.setDebug(debug);
            Store store = null;
            if (url != null) {
                URLName urln = new URLName(url);
                store = session.getStore(urln);
                store.connect();
            } else {
                if (protocol != null) store = session.getStore(protocol); else store = session.getStore();
                if (host != null || user != null || password != null) store.connect(host, user, password); else store.connect();
            }
            Folder folder = store.getDefaultFolder();
            if (folder == null) {
                System.out.println("Cant find default namespace");
                System.exit(1);
            }
            folder = folder.getFolder(mbox);
            if (folder == null) {
                System.out.println("Invalid folder");
                System.exit(1);
            }
            folder.open(Folder.READ_ONLY);
            SearchTerm term = null;
            if (subject != null) term = new SubjectTerm(subject);
            if (from != null) {
                FromStringTerm fromTerm = new FromStringTerm(from);
                if (term != null) {
                    if (or) term = new OrTerm(term, fromTerm); else term = new AndTerm(term, fromTerm);
                } else term = fromTerm;
            }
            if (today) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                c.set(Calendar.AM_PM, Calendar.AM);
                ReceivedDateTerm startDateTerm = new ReceivedDateTerm(ComparisonTerm.GE, c.getTime());
                c.add(Calendar.DATE, 1);
                ReceivedDateTerm endDateTerm = new ReceivedDateTerm(ComparisonTerm.LT, c.getTime());
                SearchTerm dateTerm = new AndTerm(startDateTerm, endDateTerm);
                if (term != null) {
                    if (or) term = new OrTerm(term, dateTerm); else term = new AndTerm(term, dateTerm);
                } else term = dateTerm;
            }
            Message[] msgs = folder.search(term);
            System.out.println("FOUND " + msgs.length + " MESSAGES");
            if (msgs.length == 0) System.exit(1);
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(msgs, fp);
            for (int i = 0; i < msgs.length; i++) {
                System.out.println("--------------------------");
                System.out.println("MESSAGE #" + (i + 1) + ":");
                dumpPart(msgs[i]);
            }
            folder.close(false);
            store.close();
        } catch (Exception ex) {
            System.out.println("Oops, got exception! " + ex.getMessage());
            ex.printStackTrace();
        }
        System.exit(1);
    }

    public static void dumpPart(Part p) throws Exception {
        if (p instanceof Message) {
            Message m = (Message) p;
            Address[] a;
            if ((a = m.getFrom()) != null) {
                for (int j = 0; j < a.length; j++) System.out.println("FROM: " + a[j].toString());
            }
            if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
                for (int j = 0; j < a.length; j++) System.out.println("TO: " + a[j].toString());
            }
            System.out.println("SUBJECT: " + m.getSubject());
            Date d = m.getSentDate();
            System.out.println("SendDate: " + (d != null ? d.toLocaleString() : "UNKNOWN"));
            Flags flags = m.getFlags();
            StringBuffer sb = new StringBuffer();
            Flags.Flag[] sf = flags.getSystemFlags();
            boolean first = true;
            for (int i = 0; i < sf.length; i++) {
                String s;
                Flags.Flag f = sf[i];
                if (f == Flags.Flag.ANSWERED) s = "\\Answered"; else if (f == Flags.Flag.DELETED) s = "\\Deleted"; else if (f == Flags.Flag.DRAFT) s = "\\Draft"; else if (f == Flags.Flag.FLAGGED) s = "\\Flagged"; else if (f == Flags.Flag.RECENT) s = "\\Recent"; else if (f == Flags.Flag.SEEN) s = "\\Seen"; else continue;
                if (first) first = false; else sb.append(' ');
                sb.append(s);
            }
            String[] uf = flags.getUserFlags();
            for (int i = 0; i < uf.length; i++) {
                if (first) first = false; else sb.append(' ');
                sb.append(uf[i]);
            }
            System.out.println("FLAGS = " + sb.toString());
        }
        System.out.println("CONTENT-TYPE: " + p.getContentType());
        Object o = p.getContent();
        if (o instanceof String) {
            System.out.println("This is a String");
            System.out.println((String) o);
        } else if (o instanceof Multipart) {
            System.out.println("This is a Multipart");
            Multipart mp = (Multipart) o;
            int count = mp.getCount();
            for (int i = 0; i < count; i++) dumpPart(mp.getBodyPart(i));
        } else if (o instanceof InputStream) {
            System.out.println("This is just an input stream");
            InputStream is = (InputStream) o;
            int c;
            while ((c = is.read()) != -1) System.out.write(c);
        }
    }
}
