    synchronized void fireCtcpUserinfoRequestReceived(CtcpUserinfoRequestEvent event) {

        for (Iterator it = ctcpListeners.iterator(); it.hasNext(); ) {

            try {

                ((CtcpListener) it.next()).userinfoRequestReceived(event);

            } catch (Exception exc) {

                handleException(exc);

            }

        }

    }
