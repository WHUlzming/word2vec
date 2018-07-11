    public boolean contains(Object o) {

        boolean wasInterrupted = beforeRead();

        try {

            return c_.contains(o);

        } finally {

            afterRead(wasInterrupted);

        }

    }
