            public void run() {

                try {

                    player.play();

                    playerCount.decrementAndGet();

                    synchronized (playerCount) {

                        playerCount.notify();

                    }

                } catch (JavaLayerException jle) {

                    jle.printStackTrace();

                }

            }
