    DcmDir(File dirfile, Properties cfg) throws IOException {

        this.dirFile = dirfile.getCanonicalFile();

        this.cfg = cfg;

        String rm = replace(cfg.getProperty("readme"), "<none>", null);

        if (rm != null) {

            this.readMeFile = new File(rm);

            this.readMeCharset = replace(cfg.getProperty("readme-charset"), "<none>", null);

        }

        this.id = replace(cfg.getProperty("fs-id", ""), "<none>", "");

        this.uid = replace(cfg.getProperty("fs-uid", ""), "<auto>", "");

        this.maxlen = new Integer(cfg.getProperty("maxlen", "79"));

        this.vallen = new Integer(cfg.getProperty("maxlen", "64"));

        this.skipGroupLen = !"<yes>".equals(cfg.getProperty("grouplen"));

        this.undefSeqLen = !"<yes>".equals(cfg.getProperty("seqlen"));

        this.undefItemLen = !"<yes>".equals(cfg.getProperty("itemlen"));

        this.onlyInUse = "<yes>".equals(cfg.getProperty("onlyInUse"));

        this.ignoreCase = "<yes>".equals(cfg.getProperty("ignoreCase"));

        for (Enumeration it = cfg.keys(); it.hasMoreElements(); ) {

            String key = (String) it.nextElement();

            if (key.startsWith("key.")) {

                try {

                    keys.putXX(Tags.forName(key.substring(4)), cfg.getProperty(key));

                } catch (Exception e) {

                    throw new IllegalArgumentException("Illegal key - " + key + "=" + cfg.getProperty(key));

                }

            }

        }

        String qrl = keys.getString(Tags.QueryRetrieveLevel, QRLEVEL[1]);

        this.qrLevel = Arrays.asList(QRLEVEL).indexOf(qrl);

        if (qrLevel == -1) {

            throw new IllegalArgumentException("Illegal Query Retrieve Level - " + qrl);

        }

        keys.remove(Tags.QueryRetrieveLevel);

    }
