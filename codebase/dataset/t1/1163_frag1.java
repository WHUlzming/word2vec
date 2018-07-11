    public void PostFile(final String path) {

        final String urlString = this.urlString;

        final boolean saveResponse = this.saveResponse;

        final String responseFileName = this.responseFileName;

        AsynchUtil.runAsynchronously(new Runnable() {



            @Override

            public void run() {

                try {

                    performRequest(urlString, null, path, saveResponse, responseFileName);
