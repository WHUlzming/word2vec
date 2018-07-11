    public void disconnect() {

        try {

            ftp.logout();

            if (ftp.isConnected()) {

                ftp.disconnect();

            }

        } catch (IOException e) {

            System.out.println("Could not disconnect from server.");

        }

    }
