        Assert.assertEquals("Boswell, Jr.", reader.get(1));

        Assert.assertEquals(0L, reader.getCurrentRecord());

        Assert.assertEquals(2, reader.getColumnCount());

        Assert.assertEquals("\"Mac \"The Knife\" Peter\",\"Boswell, Jr.\"", reader.getRawRecord());

        Assert.assertFalse(reader.readRecord());

        reader.close();

    }
