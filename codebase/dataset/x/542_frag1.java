    public static final String loadFile(final String path) {

        StringBuffer html = new StringBuffer();

        FileInputStream stream;

        try {

            stream = new FileInputStream(path);

        } catch (FileNotFoundException e1) {

            e1.printStackTrace();

            throw new RuntimeException("<LoadFile> path=" + path + " err=" + e1.getMessage());

        }

        try {

            stream = new FileInputStream(path);

            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String data = "";

            do {

                data = reader.readLine();

                if (data != null) {

                    html.append(data);

                }

            } while (data != null);

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("<LoadFile> path=" + path + " err=" + e.getMessage());

        } finally {

            if (stream != null) {

                try {

                    stream.close();

                } catch (IOException e) {

                }

            }

        }

        return html.toString();

    }
