    static class StreamReader extends Thread {



        private InputStream is;



        private StringWriter sw;



        StreamReader(InputStream is) {

            this.is = is;

            sw = new StringWriter();

        }



        public void run() {

            try {

                int c;

                while ((c = is.read()) != -1) sw.write(c);

            } catch (IOException e) {

                ;

            }

        }



        String getResult() {

            return sw.toString();

        }

    }

}



class FionaUI extends JDialog implements ActionListener {
