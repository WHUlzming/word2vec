    public static File getFileFromBytes(byte[] b, String outputFile) {

        BufferedOutputStream stream = null;

        File file = null;

        try {

            file = new File(outputFile);

            FileOutputStream fstream = new FileOutputStream(file);

            stream = new BufferedOutputStream(fstream);

            stream.write(b);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (stream != null) {

                try {

                    stream.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

        }

        return file;

    }
