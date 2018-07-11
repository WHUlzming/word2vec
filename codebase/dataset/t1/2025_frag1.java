        File file = new File(System.getProperty("user.home") + File.separator + ".keystore");

        if (file.isFile() == false) {

            char SEP = File.separatorChar;

            File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");

            file = new File(dir, "jssecacerts");

            if (file.isFile() == false) {

                file = new File(dir, "cacerts");

            }

        }

        System.out.println("Loading KeyStore " + file + "...");
