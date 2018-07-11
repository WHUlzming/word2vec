    synchronized void fireMessageReceived(MessageEvent event) {

        for (Iterator it = channelListeners.iterator(); it.hasNext(); ) {

            try {

                ((ChannelListener) it.next()).messageReceived(event);

            } catch (Exception exc) {

                handleException(exc);

            }

        }

    }
