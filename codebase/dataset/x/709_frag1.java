            String newurl = (new URL(mServerURL.getProtocol(), mServerURL.getHost(), port, uri)).toExternalForm();

            HttpURL httpURL = createURL(newurl);

            if (mCredentials.getGrpUserName() != null && mCredentials.getGrpUserName().length() > 0) {
