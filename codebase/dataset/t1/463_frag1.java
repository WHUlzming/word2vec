        Assert.assertEquals(1, reader.getColumnCount());

        Assert.assertEquals("1", reader.get(0));

        Assert.assertEquals(8L, reader.getCurrentRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();
