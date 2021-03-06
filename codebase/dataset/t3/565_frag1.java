    public static Image[] loadSequenceImages(String fileName, String range) {

        try {

            int start_range = -1;

            int end_range = -1;

            int images_count = 1;

            int minusIndex = range.indexOf('-');

            if ((minusIndex > 0) && (minusIndex < (range.length() - 1))) {

                try {

                    start_range = Integer.parseInt(range.substring(0, minusIndex));

                    end_range = Integer.parseInt(range.substring(minusIndex + 1));

                    if (start_range < end_range) {

                        images_count = end_range - start_range + 1;

                    }

                } catch (Exception ex) {

                    ex.printStackTrace();

                }

            }

            Image[] images = new Image[images_count];

            for (int i = 0; i < images_count; i++) {

                String imageName = fileName;

                if (images_count > 1) {

                    int dotIndex = fileName.lastIndexOf('.');

                    if (dotIndex >= 0) {

                        imageName = fileName.substring(0, dotIndex) + (start_range + i) + fileName.substring(dotIndex);

                    }

                }

                images[i] = GraphicsUtils.loadImage(imageName);

            }

            return images;

        } catch (Exception ex) {

            ex.printStackTrace();

        }

        return null;

    }
