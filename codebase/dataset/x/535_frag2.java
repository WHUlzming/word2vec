    public void mute() {

        try {

            user.mute();

        } catch (ManagerCommunicationException e) {

            log.error("Failed to mute participant: " + user.getUserNumber() + " due to '" + e.getMessage() + "'");

            e.printStackTrace();

        }

    }
