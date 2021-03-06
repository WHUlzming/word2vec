    @Override

    public final int write(CharBuffer source) throws IOException {

        final CharBuffer buffer = ensureOpen(-1);

        final int count = source.remaining();

        final int r = buffer.remaining();

        if (r < count) {

            int bufferSize = buffer.capacity();

            if (bufferSize > count) {

                flushBuffer(buffer);

                buffer.put(source);

                return count;

            } else if (this.xout != null) {

                flushBuffer(buffer);

                return this.xout.write(source);

            } else if (source.hasArray()) {

                flushBuffer(buffer);

                this.out.write(source.array(), source.arrayOffset() + source.position(), count);

                source.position(source.limit());

                return count;

            } else {

                flushBuffer(buffer);

                this.out.append(source);

                source.position(source.limit());

                return count;

            }

        } else {

            buffer.put(source);

            return count;

        }

    }
