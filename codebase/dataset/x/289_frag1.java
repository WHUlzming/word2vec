    public void unmute() {

        try {

            user.unmute();

        } catch (ManagerCommunicationException e) {

            log.error("Failed to unmute participant: " + user.getUserNumber() + " due to '" + e.getMessage() + "'");

            e.printStackTrace();

        }

    }
