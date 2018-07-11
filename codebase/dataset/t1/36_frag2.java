    public void test38() throws Exception {

        String data = "1\r\n\r\n\"\"\r\n \r\n2";

        CsvReader reader = CsvReader.parse(data);

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("", reader.get(0));

        Assert.assertEquals(1L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("\"\"", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("", reader.get(0));

        Assert.assertEquals(2L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals(" ", reader.getRawRecord());

        Assert.assertTrue(reader.readRecord());

        Assert.assertEquals("2", reader.get(0));

        Assert.assertEquals(3L, reader.getCurrentRecord());

        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("2", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
