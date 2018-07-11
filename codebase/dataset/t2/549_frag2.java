        if (bDeleteSourceOnSuccess) try {

            if (!(new File(sSource)).delete()) Log.log(Log.WARNING, "lazyj.Utils", "compress: could not delete original file (" + sSource + ")");

        } catch (SecurityException se) {

            Log.log(Log.ERROR, "lazyj.Utils", "compress: security constraints prevents file deletion");
