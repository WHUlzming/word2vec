    @Test

    public void test72() throws Exception {

        byte[] buffer;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        CsvWriter writer = new CsvWriter(stream, ',', Charset.forName("ISO-8859-1"));

        Assert.assertEquals('\0', writer.getRecordDelimiter());

        writer.setRecordDelimiter(';');

        Assert.assertEquals(';', writer.getRecordDelimiter());

        writer.write("a;b");

        writer.endRecord();

        writer.close();

        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("\"a;b\";", data);

    }
