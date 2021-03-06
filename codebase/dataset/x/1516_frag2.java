    public void reset(String sysID) throws IOException {

        close();

        isStandalone = false;

        this.source = null;

        try {

            this.url = new URL(defaultContext, sysID);

        } catch (MalformedURLException e) {

            this.url = new File(sysID).toURL();

        }

        this.sysID = url.toString();

    }
