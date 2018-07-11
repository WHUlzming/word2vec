    public static synchronized void playSound(final Clip clip) {

        Thread soundThread = new Thread(new Runnable() {



            public void run() {

                clip.stop();

                clip.setFramePosition(0);

                clip.start();

            }

        });

        soundThread.setName("Sound");

        soundThread.start();

    }
