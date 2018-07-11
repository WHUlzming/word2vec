    private static int charToNibble(char c) {

        if ('0' <= c && c <= '9') {

            return c - '0';

        } else if ('a' <= c && c <= 'f') {

            return c - 'a' + 0xa;

        } else if ('A' <= c && c <= 'F') {

            return c - 'A' + 0xa;

        } else {

            throw new IllegalArgumentException("Invalid hex character: " + c);

        }

    }
