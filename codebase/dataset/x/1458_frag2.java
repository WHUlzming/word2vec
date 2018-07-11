    public jnamed(String conffile) throws IOException, ZoneTransferException {

        FileInputStream fs;

        List ports = new ArrayList();

        List addresses = new ArrayList();

        try {

            fs = new FileInputStream(conffile);

        } catch (Exception e) {

            System.out.println("Cannot open " + conffile);

            return;

        }

        try {

            caches = new HashMap();

            znames = new HashMap();

            TSIGs = new HashMap();

            InputStreamReader isr = new InputStreamReader(fs);

            BufferedReader br = new BufferedReader(isr);

            String line = null;

            while ((line = br.readLine()) != null) {

                StringTokenizer st = new StringTokenizer(line);

                if (!st.hasMoreTokens()) continue;

                String keyword = st.nextToken();

                if (!st.hasMoreTokens()) {

                    System.out.println("Invalid line: " + line);

                    continue;

                }

                if (keyword.charAt(0) == '#') continue;

                if (keyword.equals("primary")) addPrimaryZone(st.nextToken(), st.nextToken()); else if (keyword.equals("secondary")) addSecondaryZone(st.nextToken(), st.nextToken()); else if (keyword.equals("cache")) {
