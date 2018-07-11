    public static String[] moveToDir(String[] srcPaths, String destDirPath, boolean overwrite) {

        String[] destPaths = new String[srcPaths.length];

        for (int i = 0; i < srcPaths.length; i++) {

            destPaths[i] = moveToDir(srcPaths[i], destDirPath, overwrite);

        }

        return destPaths;

    }
