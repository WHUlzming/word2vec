    public static Dimension getimageSize(File f) {

        Dimension imageSize = null;

        try {

            imageSize = Sanselan.getImageSize(f);

        } catch (ImageReadException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return imageSize;

    }
