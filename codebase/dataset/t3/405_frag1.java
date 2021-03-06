    @Test

    public void test2() throws Exception {

        String data = "\"bob said, \"\"Hey!\"\"\",2, 3 ";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("bob said, \"Hey!\"", reader.get(0));

        Assert.assertEquals("2", reader.get(1));

        Assert.assertEquals("3", reader.get(2));

        Assert.assertEquals(',', reader.getDelimiter());

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(3, reader.getColumnCount());

        Assert.assertEquals("\"bob said, \"\"Hey!\"\"\",2, 3 ", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.close();

    }
