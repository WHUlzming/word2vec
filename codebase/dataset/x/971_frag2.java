    public void close() {

        if (null != in) {

            try {

                in.close();

            } catch (IOException e) {

            }

            in = null;

        }

        if (connection != null) {

            ((HttpURLConnection) connection).disconnect();

            connection = null;

        }

    }
