        if (bytes == null) throw new NullPointerException(); else if ((offset < 0) || (offset > bytes.length) || (len < 0) || ((offset + len) > bytes.length) || ((offset + len) < 0)) {

            throw new IndexOutOfBoundsException();

        } else if (len == 0) return 0; else if (readPtr >= length) return -1; else {
