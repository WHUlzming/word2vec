    public static String getKey(IRCMessage msg) {

        try {

            return (String) msg.getArgs().elementAt(1);

        } catch (ArrayIndexOutOfBoundsException e) {

            return "";

        }

    }
