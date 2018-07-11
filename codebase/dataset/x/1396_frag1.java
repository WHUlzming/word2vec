                public void run() {

                    while (!isClosed()) {

                        try {

                            Socket s = delegate.accept();

                            observer.handleAccept(s);

                        } catch (IOException ignored) {

                        }

                    }

                    observer.shutdown();

                }
