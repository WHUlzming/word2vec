        public void stop() {

            if (pbThread != null) {

                pbThread.interrupt();

            }

            pbThread = null;

        }
