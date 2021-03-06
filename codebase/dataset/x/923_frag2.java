    @Override

    public void mark(int readLimit) {

        final InputStream in = this.in;

        if (in != null) {

            in.mark(readLimit);

            this.remainingAtMark = this.remaining;

        } else {

            this.remainingAtMark = 0;

        }

    }
