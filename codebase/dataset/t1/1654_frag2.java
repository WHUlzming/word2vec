                logger.error(Logger.SECURITY_FAILURE, "Problem writing gzip stream", e);

                return null;

            } finally {

                try {

                    gzos.close();

                } catch (Exception e) {

                }

                try {

                    b64os.close();

                } catch (Exception e) {

                }

                try {

                    baos.close();

                } catch (Exception e) {

                }

            }

            try {

                return new String(baos.toByteArray(), PREFERRED_ENCODING);

            } catch (java.io.UnsupportedEncodingException uue) {

                return new String(baos.toByteArray());

            }

        } else {
