            raf.readChar();

            fail("Test 6: EOFException expected.");

        } catch (EOFException e) {

        }

        raf.close();

        try {

            raf.writeChar('E');

            fail("Test 7: IOException expected.");

        } catch (IOException e) {
