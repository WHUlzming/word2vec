    public static String getFilenameWithoutExt(String filename) {

        int pos = filename.lastIndexOf(".");

        if (pos == -1) {

            return filename;

        }

        return filename.substring(0, pos);

    }
