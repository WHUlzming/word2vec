    public void test112() throws Exception {

        try {

            CsvWriter writer = new CsvWriter((String) null, ',', Charset.forName("ISO-8859-1"));

        } catch (Exception ex) {

            assertException(new IllegalArgumentException("Parameter fileName can not be null."), ex);

        }

    }
