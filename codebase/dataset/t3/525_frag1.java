    public static String gre(String ss) throws Exception {

        String host = ss.substring(ss.indexOf("//") + 2);

        host = host.substring(0, host.indexOf("/"));

        Socket js = new Socket(host, 80);

        PrintStream outStream = new PrintStream(js.getOutputStream());

        BufferedInputStream bis = new BufferedInputStream(js.getInputStream());

        outStream.println(ss);

        outStream.flush();

        int b;

        StringBuffer sb = new StringBuffer();

        try {

            while ((b = bis.read()) != -1) {

                sb.append((char) b);

            }

        } catch (Exception e) {

            System.err.println(e);

        }

        bis.close();

        outStream.close();

        js.close();

        return sb.toString();

    }
