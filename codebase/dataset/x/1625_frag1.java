            tmp = tmp.replaceAll("\"", "@quot;");

            tmp = tmp.replaceAll("<", "@lt;");

            tmp = tmp.replaceAll(">", "@gt;");

            return tmp;

        }

        return "";

    }



    public static String undoReplaceCharacter(String str) {

        if (str != null) {

            String tmp = str.replaceAll("@amp;", "&");

            tmp = tmp.replaceAll("@quot;", "\"");
