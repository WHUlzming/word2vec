        print("Initiating startup sequence...");

        printProperties();

        sw = new StopWatch();

        setServerError(null);

        try {

            openServerSocket();

        } catch (Exception e) {
