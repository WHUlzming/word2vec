    public static byte[] encodeBytesToBytes(byte[] source, int off, int len, int options) throws java.io.IOException {

        if (source == null) {

            throw new NullPointerException("Cannot serialize a null array.");

        }

        if (off < 0) {

            throw new IllegalArgumentException("Cannot have negative offset: " + off);

        }

        if (len < 0) {

            throw new IllegalArgumentException("Cannot have length offset: " + len);

        }

        if (off + len > source.length) {

            throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off, len, source.length));

        }

        if ((options & GZIP) > 0) {

            java.io.ByteArrayOutputStream baos = null;

            java.util.zip.GZIPOutputStream gzos = null;

            Base641.OutputStream b64os = null;

            try {

                baos = new java.io.ByteArrayOutputStream();

                b64os = new Base641.OutputStream(baos, ENCODE | options);

                gzos = new java.util.zip.GZIPOutputStream(b64os);

                gzos.write(source, off, len);

                gzos.close();

            } catch (java.io.IOException e) {

                throw e;

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

            return baos.toByteArray();

        } else {

            boolean breakLines = (options & DO_BREAK_LINES) > 0;

            int len43 = len * 4 / 3;

            byte[] outBuff = new byte[(len43) + ((len % 3) > 0 ? 4 : 0) + (breakLines ? (len43 / MAX_LINE_LENGTH) : 0)];

            int d = 0;

            int e = 0;

            int len2 = len - 2;

            int lineLength = 0;

            for (; d < len2; d += 3, e += 4) {

                encode3to4(source, d + off, 3, outBuff, e, options);

                lineLength += 4;

                if (breakLines && lineLength == MAX_LINE_LENGTH) {

                    outBuff[e + 4] = NEW_LINE;

                    e++;

                    lineLength = 0;

                }

            }

            if (d < len) {

                encode3to4(source, d + off, len - d, outBuff, e, options);

                e += 4;

            }

            byte[] finalOut = new byte[e];

            System.arraycopy(outBuff, 0, finalOut, 0, e);

            return finalOut;

        }

    }
