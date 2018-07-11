    public void close() {

        try {

            printCommand("QUIT");

            controlWriter.flush();

            waitForResult();

            controlSocket.close();

        } catch (IOException e) {

            System.out.println(e);

        }

    }
