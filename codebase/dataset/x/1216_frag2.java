    public boolean canSaveEdits() {

        if (shpFile.canWrite()) {

            File auxShx = new File(shxPath);

            if (auxShx.canWrite()) {

                File auxDbf = new File(dbfPath);

                if (auxDbf.canWrite()) return true;

            }

        }

        return false;

    }
