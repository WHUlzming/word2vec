    public void test_18_112_writeObject() {

        Object objToSave = null;

        Object objLoaded;

        try {

            TimeZone test = TimeZone.getTimeZone("GMT");

            objToSave = test;

            if (DEBUG) System.out.println("Obj = " + objToSave);

            objLoaded = dumpAndReload(objToSave);

            assertTrue(MSG_TEST_FAILED + objToSave, test.equals(objLoaded));

        } catch (IOException e) {

            fail("IOException serializing " + objToSave + " : " + e.getMessage());

        } catch (ClassNotFoundException e) {

            fail("ClassNotFoundException reading Object type : " + e.getMessage());

        } catch (Error err) {

            System.out.println("Error when obj = " + objToSave);

            throw err;

        }

    }
