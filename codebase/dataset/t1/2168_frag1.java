            if (java.util.zip.GZIPInputStream.GZIP_MAGIC == head) {

                java.io.ByteArrayInputStream bais = null;

                java.util.zip.GZIPInputStream gzis = null;

                java.io.ByteArrayOutputStream baos = null;

                byte[] buffer = new byte[2048];

                int length = 0;

                try {

                    baos = new java.io.ByteArrayOutputStream();

                    bais = new java.io.ByteArrayInputStream(bytes);

                    gzis = new java.util.zip.GZIPInputStream(bais);

                    while ((length = gzis.read(buffer)) >= 0) {
