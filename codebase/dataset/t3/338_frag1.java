    public void test11() throws Exception {

        String data = "\"July 4th, 2005\"";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("July 4th, 2005", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("\"July 4th, 2005\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
