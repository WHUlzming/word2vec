        System.exit(0);

    }



    public static Date processDate(String date) {

        StringTokenizer tokens = new StringTokenizer(date, "-");

        try {

            int num = tokens.countTokens();

            if (num != 3) {

                return null;

            }

            int month = Calendar.JANUARY + (Integer.parseInt(tokens.nextToken()) - 1);
