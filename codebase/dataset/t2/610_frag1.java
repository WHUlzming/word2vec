        byte[] buf = new byte[8192];

        int n;

        while ((n = fr.read(buf)) >= 0) fw.write(buf, 0, n);

        fr.close();

        fw.close();

    }
