    public static WebProxyCache getInstance() {

        if (instance == null) {

            instance = new WebProxyCache();

        }

        return instance;

    }
