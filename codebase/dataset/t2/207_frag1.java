        FileOutputStream fos = new FileOutputStream(outFile);

        byte[] buf = new byte[100000];

        int len;

        while ((len = gin.read(buf)) > 0) fos.write(buf, 0, len);

        gin.close();

        fos.close();

        if (deleteGzipfileOnSuccess) infile.delete();
