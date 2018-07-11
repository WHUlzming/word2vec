    public static String getFileSuffix(String filename) {

        int lastDot = filename.lastIndexOf('.');

        if (lastDot < 0) {

            return null;

        }

        return toLowerCase(filename.substring(lastDot + 1));

    }
