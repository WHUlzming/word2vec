    Token new12(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) {

        return new TCbegin(line, pos);

    }



    Token new13(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) {

        return new TCend(line, pos);

    }



    Token new14(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) {

        return new TCinput(line, pos);

    }



    Token new15(@SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) {

        return new TCinclude(line, pos);

    }



    Token new16(@SuppressWarnings("hiding") String text, @SuppressWarnings("hiding") int line, @SuppressWarnings("hiding") int pos) {
