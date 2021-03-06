                    public void run() {

                        try {

                            BufferedImage bufferedImage = ImageIO.read(in);

                            if (filmstripMode) {

                                filmstripPanel.addImage(bufferedImage, imageTime);

                            } else {

                                imagePanel.setImage(bufferedImage, imageTime);

                            }

                        } catch (IOException e) {

                            log.error("Failed to decode image for " + channelName + ".");

                            e.printStackTrace();

                        }

                    }
