    private static String[] toSuffixes(String[] extensions) {

        String[] suffixes = new String[extensions.length];

        for (int i = 0; i < extensions.length; i++) {

            suffixes[i] = "." + extensions[i];

        }

        return suffixes;

    }
