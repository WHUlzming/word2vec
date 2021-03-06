    private void loadLaserDiscs(String fileName, HashMap<Integer, LaserDisc> map) {

        try {

            CSVReader reader = new CSVReader(new FileReader(basePath + fileName), ';', '\"');

            String[] nextLine;

            LaserDisc ld;

            while ((nextLine = reader.readNext()) != null) {

                ld = new LaserDisc();

                ld.load(nextLine);

                map.put(ld.MOVIEID, ld);

            }

            reader.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
