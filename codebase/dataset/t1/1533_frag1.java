    private void send(ActiveAssociation active, File file) throws InterruptedException, IOException {

        if (!file.isDirectory()) {

            for (int i = 0; i < repeatSingle; ++i) {

                sendFile(active, file);

            }

            return;

        }

        File[] list = file.listFiles();

        for (int i = 0; i < list.length; ++i) {

            send(active, list[i]);

        }

    }
