            raf.readDouble();

            fail("Test 3: EOFException expected.");

        } catch (EOFException e) {

        }

        raf.close();

        try {

            raf.writeDouble(Double.MIN_VALUE);

            fail("Test 4: IOException expected.");

        } catch (IOException e) {
