    public void query() throws IOException, TransformerConfigurationException {

        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();

        Templates xslt = loadDcmDirXSL(tf);

        DirReader reader = fact.newDirReader(dirFile);

        try {

            query("", 1, 0, reader.getFirstRecordBy(null, keys, ignoreCase), tf, xslt);

        } finally {

            reader.close();

        }

    }
