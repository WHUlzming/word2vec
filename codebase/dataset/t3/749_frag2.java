    void fireInvitationDeliveryReceived(InvitationEvent event) {

        synchronized (connectionListeners) {

            for (final ConnectionListener connectionListener : connectionListeners) {

                try {

                    connectionListener.invitationDeliveryReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }



    void fireNumericReplyReceived(NumericEvent event) {
