    @Test

    public void test77() {

        try {

            CsvReader.parse(null);

        } catch (Exception ex) {

            assertException(new IllegalArgumentException("Parameter data can not be null."), ex);

        }

    }
