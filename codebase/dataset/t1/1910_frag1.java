        if (error) {

            System.err.println(messages.getString("try"));

        }

        System.exit(1);

    }



    private static String replace(String val, String from, String to) {

        return from.equals(val) ? to : val;
