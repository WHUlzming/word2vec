    public static boolean touch(String sfile) throws IOException {

        File file = new File(sfile);

        if (file.exists()) {

            return false;

        } else {

            file.getParentFile().mkdirs();

            file.createNewFile();

            return true;

        }

    }
