        public InputStream(java.io.InputStream in, int options) {

            super(in);

            this.breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;

            this.encode = (options & ENCODE) == ENCODE;

            this.bufferLength = encode ? 4 : 3;

            this.buffer = new byte[bufferLength];

            this.position = -1;

            this.lineLength = 0;

            this.options = options;

            this.decodabet = getDecodabet(options);

        }
