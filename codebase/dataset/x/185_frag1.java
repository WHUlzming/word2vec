    public static String join(String[] array, String sep) {

        if (array == null || array.length == 0) {

            return "";

        }

        int len = array.length;

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < (len - 1); i++) {

            sb.append(array[i]).append(sep);

        }

        sb.append(array[len - 1]);

        return sb.toString();

    }
