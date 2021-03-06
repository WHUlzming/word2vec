        int[] dest = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        if (lastDispose > 0) {

            if (lastDispose == 3) {

                int n = frameCount - 2;

                if (n > 0) {

                    lastImage = getFrame(n - 1);

                } else {

                    lastImage = null;

                }

            }

            if (lastImage != null) {

                int[] prev = ((DataBufferInt) lastImage.getRaster().getDataBuffer()).getData();

                System.arraycopy(prev, 0, dest, 0, width * height);

                if (lastDispose == 2) {

                    Graphics2D g = image.createGraphics();

                    Color c = null;
