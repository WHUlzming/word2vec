    public void test12() throws Exception {

        String data = " 1";

        CsvReader reader = CsvReader.parse(data);

        reader.setTrimWhitespace(false);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals(" 1", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals(" 1", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
