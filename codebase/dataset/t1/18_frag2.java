        buffer = stream.toByteArray();

        stream.close();

        String data = Charset.forName("ISO-8859-1").decode(ByteBuffer.wrap(buffer)).toString();

        Assert.assertEquals("1\\,\\\\\\\r\\\n2\r\n1\\,\\\\\\;2;", data);

    }


