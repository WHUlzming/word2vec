    public static String randomString(int length) {

        if (length < 1) {

            return null;

        }

        char[] randBuffer = new char[length];

        for (int i = 0; i < randBuffer.length; i++) {

            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];

        }

        return new String(randBuffer);

    }
