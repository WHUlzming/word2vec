            fail("IllegalArgumentException should have been thrown");

        } catch (IllegalArgumentException ex) {

        }

        try {

            new IdentityDecoder(channel, null, null);

            fail("IllegalArgumentException should have been thrown");

        } catch (IllegalArgumentException ex) {

        }

        try {

            new IdentityDecoder(channel, inbuf, null);

            fail("IllegalArgumentException should have been thrown");
