    protected int readInt(DataInputStream dis) throws IOException {

        if (swap) {

            return Utilities.readLittleEndianInt(dis);

        } else {

            return dis.readInt();

        }

    }
