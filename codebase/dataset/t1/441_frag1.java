        public void write(byte[] theBytes, int off, int len) throws java.io.IOException {

            if (suspendEncoding) {

                super.out.write(theBytes, off, len);

                return;

            }

            for (int i = 0; i < len; i++) {

                write(theBytes[off + i]);

            }

        }
