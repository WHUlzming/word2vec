    @Override

    public void run() {

        try {

            synchronize();

        } catch (Exception ex) {

            dispatchEvent(ex);

        }

    }
