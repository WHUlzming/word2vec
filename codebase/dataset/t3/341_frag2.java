    public static void serializar(InputStream in, OutputStream out) {

        byte[] buffer = new byte[102400];

        int n;

        try {

            while ((n = in.read(buffer)) != -1) {

                out.write(buffer, 0, n);

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
