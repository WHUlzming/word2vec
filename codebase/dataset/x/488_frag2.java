    public void print(String s) {

        try {

            reader.printString(s);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return;

    }
