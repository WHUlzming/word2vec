    public static boolean deleteAllSubFiles(String path) {

        File f = new File(path);

        if (!f.exists()) return false;

        if (f.isDirectory()) {

            File delFile[] = f.listFiles();

            int len = delFile.length;

            for (int j = 0; j < len; j++) {

                if (delFile[j].isDirectory()) {

                    deleteAllSubFiles(delFile[j].getAbsolutePath());

                }

                delFile[j].delete();

            }

        }

        return true;

    }
