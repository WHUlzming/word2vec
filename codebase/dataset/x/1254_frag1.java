    public void waitForMore() {

        if (isClosed()) {

            return;

        }

        synchronized (notifier) {

            try {

                notifier.wait();

            } catch (InterruptedException e) {

            }

        }

    }
