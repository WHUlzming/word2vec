    void fireCtcpPingRequestReceived(CtcpPingRequestEvent event) {

        synchronized (ctcpListeners) {

            for (final CtcpListener ctcpListener : ctcpListeners) {

                try {

                    ctcpListener.pingRequestReceived(event);

                } catch (Exception exc) {

                    handleException(exc);

                }

            }

        }

    }
