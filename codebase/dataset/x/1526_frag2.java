    public final void read() throws IOException {

        InputStream inputStream = mSocket.getInputStream();

        try {

            int count = inputStream.read(mBuffer, mBufferIndex, mBuffer.length - mBufferIndex);

            if (0 < count) {

                getLinesFromBuffer(count);

            }

        } catch (SocketTimeoutException e) {

            assert true;

        }

    }
