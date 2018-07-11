    public static List<String> readStrings(DataInput in) throws IOException {

        int len = in.readInt();

        if (len <= 0) {

            return null;

        }

        List<String> ret = new Vector<String>();

        for (int i = 0; i < len; i++) {

            ret.add(readString(in));

        }

        return ret;

    }
