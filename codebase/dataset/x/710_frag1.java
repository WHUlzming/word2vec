    public void close() throws CloseDriverException {

        try {

            dbf.close();

        } catch (IOException e) {

            throw new CloseDriverException(getName(), e);

        }

    }
