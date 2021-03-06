            logger.warning(Logger.SECURITY_FAILURE, "Problem decoding string using " + PREFERRED_ENCODING + "; substituting native platform encoding instead", uee);

        }

        bytes = decode(bytes, 0, bytes.length, options);

        if (bytes != null && bytes.length >= 4) {

            int head = ((int) bytes[0] & 0xff) | ((bytes[1] << 8) & 0xff00);

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

                        baos.write(buffer, 0, length);

                    }

                    bytes = baos.toByteArray();

                } catch (java.io.IOException e) {

                } finally {

                    try {

                        baos.close();

                    } catch (Exception e) {

                    }

                    try {

                        gzis.close();

                    } catch (Exception e) {

                    }

                    try {

                        bais.close();

                    } catch (Exception e) {

                    }

                }

            }

        }

        return bytes;

    }
