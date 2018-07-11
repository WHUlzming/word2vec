    @Test

    public void test22() throws Exception {

        String data = "\"data \"\" here\"";

        CsvReader reader = CsvReader.parse(data);

        reader.setUseTextQualifier(false);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("\"data \"\" here\"", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("\"data \"\" here\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
