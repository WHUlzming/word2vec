        ConnectionThread(String address, int port, ServerInterface si, LogonDialog dialog, boolean reconnecting) {

            this.address = address;

            this.dialog = dialog;

            this.port = port;

            this.si = si;

            this.reconnecting = reconnecting;

            localConnection = false;

            Thread thread = new Thread(this);

            thread.start();

        }
