    public void fromDB_NotExistingCollection() {

        System.out.println("FromDB_NotExistingCollection");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        String uri = "xmldb:exist://guest:guest@localhost:8080/exist/xmlrpc/db/" + TESTCASENAME + "/foo/bar.xml";

        try {

            XmldbURL xmldbUri = new XmldbURL(uri);

            getDocument(xmldbUri, baos);

            fail("Not existing collection: exception should be thrown");

        } catch (Exception ex) {

            if (!ex.getCause().getMessage().matches(".*Collection /db/.* not found.*")) {

                ex.printStackTrace();

                LOG.error(ex);

                fail(ex.getMessage());

            }

        }

    }
