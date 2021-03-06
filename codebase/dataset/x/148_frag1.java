    public void test6() throws Exception {

        String data = "1\r\n2";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("2", reader.get(0));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("2", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        Assert.assertEquals("", reader.getRawRecord());

        reader.close();

    }
