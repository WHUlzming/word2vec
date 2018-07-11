    public static void copyFiles(String sourceDir, String destDir, FileMask fileMask) throws IOException {

        try {

            File sourceDirFile = new File(sourceDir);

            File destDirFile = new File(destDir);

            destDirFile.mkdirs();

            FileUtilities.copyFiles(sourceDirFile, destDirFile, fileMask);

        } catch (IOException ex) {

            logger.error("Impossible to copy files from '" + sourceDir + "' to '" + destDir + "'");

        }

    }
