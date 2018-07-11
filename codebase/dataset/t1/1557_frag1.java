public class InstallCert {



    public static void main(String[] args) throws Exception {

        String host;

        int port;

        char[] passphrase;

        if ((args.length == 1) || (args.length == 2)) {

            String[] c = args[0].split(":");

            host = c[0];

            port = (c.length == 1) ? 443 : Integer.parseInt(c[1]);

            String p = (args.length == 1) ? "changeit" : args[1];

            passphrase = p.toCharArray();

        } else {

            System.out.println("Usage: java InstallCert <host>[:port] [passphrase]");

            return;
