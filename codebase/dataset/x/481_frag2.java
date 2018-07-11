    public void disconnect() {

        try {

            in.close();

            out.close();

        } catch (IOException ex) {

            logger.error(ex + " at " + ex.getStackTrace()[0].toString());

        }

    }
