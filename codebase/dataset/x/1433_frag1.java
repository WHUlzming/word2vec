    private void closeFileInErrorCase(RandomAccessFile file) {

        try {

            if (file != null) {

                file.close();

            }

        } catch (Exception e) {

        }

    }
