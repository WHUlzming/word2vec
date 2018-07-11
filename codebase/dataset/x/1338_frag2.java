    protected void installTileRaster(Tile tile, DataRaster tileRaster, AVList params) throws java.io.IOException {

        java.io.File installLocation;

        Object result = this.installLocationForTile(params, tile);

        if (result instanceof java.io.File) {

            installLocation = (java.io.File) result;

        } else {

            String message = result.toString();

            Logging.logger().severe(message);

            throw new java.io.IOException(message);

        }

        synchronized (this.fileLock) {

            java.io.File dir = installLocation.getParentFile();

            if (!dir.exists()) {

                if (!dir.mkdirs()) {

                    String message = Logging.getMessage("generic.CannotCreateFile", dir);

                    Logging.logger().warning(message);

                }

            }

        }

        String formatSuffix = params.getStringValue(AVKey.FORMAT_SUFFIX);

        DataRasterWriter[] writers = this.getDataRasterWriters();

        Object writer = this.findWriterFor(tileRaster, formatSuffix, installLocation, writers);

        if (writer instanceof DataRasterWriter) {

            try {

                ((DataRasterWriter) writer).write(tileRaster, formatSuffix, installLocation);

            } catch (java.io.IOException e) {

                String message = Logging.getMessage("generic.ExceptionWhileWriting", installLocation);

                Logging.logger().log(java.util.logging.Level.SEVERE, message, e);

            }

        }

    }
