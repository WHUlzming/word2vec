    public void login() {

        try {

            initialize();

            loginUGotFile();

        } catch (Exception ex) {

            Logger.getLogger(UGotFileAccount.class.getName()).log(Level.SEVERE, null, ex);

        }

    }
