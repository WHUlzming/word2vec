    public boolean isConnected() {

        if (this.source == null) {

            return false;

        } else {

            return this.source.VerifyConnection();

        }

    }
