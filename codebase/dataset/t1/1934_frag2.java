    private static void putKey(Properties cfg, String s) {

        int pos = s.indexOf(':');

        if (pos == -1) {

            cfg.put("key." + s, "");

        } else {

            cfg.put("key." + s.substring(0, pos), s.substring(pos + 1));

        }

    }
