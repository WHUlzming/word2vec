    public static boolean isDisplayGraphics() {

        try {

            return displayGraphics && !GraphicsEnvironment.isHeadless();

        } catch (HeadlessException e) {

            return false;

        }

    }
