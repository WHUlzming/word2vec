    public void getBytes(int index, byte[] dst, int dstIndex, int length) {

        int sliceId = sliceId(index);

        if (index > capacity() - length || dstIndex > dst.length - length) {

            throw new IndexOutOfBoundsException();

        }

        int i = sliceId;

        while (length > 0) {

            ChannelBuffer s = slices[i];

            int adjustment = indices[i];

            int localLength = Math.min(length, s.capacity() - (index - adjustment));

            s.getBytes(index - adjustment, dst, dstIndex, localLength);

            index += localLength;

            dstIndex += localLength;

            length -= localLength;

            i++;

        }

    }
