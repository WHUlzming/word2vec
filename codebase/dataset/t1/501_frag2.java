        MessageDigest md = null;

        try {

            md = MessageDigest.getInstance("SHA");

        } catch (NoSuchAlgorithmException e) {

            throw new Exception(e.getMessage());

        }

        try {

            md.update(plaintext.getBytes("UTF-8"));

        } catch (UnsupportedEncodingException e) {
