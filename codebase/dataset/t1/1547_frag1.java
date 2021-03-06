    }



    private byte[] getBytesFromFile(File file) throws IOException {

        InputStream is = new FileInputStream(file);

        long length = file.length();

        if (length > Integer.MAX_VALUE) {

            return null;

        }

        byte[] bytes = new byte[(int) length];

        int offset = 0;

        int numRead = 0;

        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {

            offset += numRead;

        }

        if (offset < bytes.length) {

            throw new IOException("Could not completely read file " + file.getName());

        }

        is.close();

        return bytes;

    }
