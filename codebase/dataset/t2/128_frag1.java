        public void encrypt(int[] buffer) {

            int nLen = buffer.length;

            long lTemp;

            for (int nI = 0; nI < nLen; nI += 2) {

                lTemp = intArrayToLong(buffer, nI);

                lTemp = encryptBlock(lTemp);

                longToIntArray(lTemp, buffer, nI);

            }

        }
