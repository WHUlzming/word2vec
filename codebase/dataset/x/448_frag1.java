    public void close() {

        try {

            if (chan != null) {

                chan.close();

                chan = null;

            }

            if (fis != null) {

                fis.close();

                fis = null;

            }

            buf = null;

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
