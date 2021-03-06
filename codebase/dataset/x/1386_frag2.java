    public static Vector readLines(String path) {

        BufferedReader f;

        String line;

        line = "";

        Vector data = new Vector();

        try {

            f = new BufferedReader(new FileReader(path));

            while ((line = f.readLine()) != null) {

                data.add(line.trim());

            }

            f.close();

        } catch (IOException e) {

            logger.log(Level.SEVERE, "Exception thrown in readLines(String path)", e);

        }

        return data;

    }
