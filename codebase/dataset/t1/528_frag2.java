    private static String toHexString(byte[] bytes) {

        StringBuilder sb = new StringBuilder(bytes.length * 3);

        for (int b : bytes) {

            b &= 0xff;

            sb.append(HEXDIGITS[b >> 4]);

            sb.append(HEXDIGITS[b & 15]);

            sb.append(' ');

        }

        return sb.toString();

    }



    private static class SavingTrustManager implements X509TrustManager {



        private final X509TrustManager tm;



        private X509Certificate[] chain;



        SavingTrustManager(X509TrustManager tm) {

            this.tm = tm;

        }



        public X509Certificate[] getAcceptedIssuers() {

            throw new UnsupportedOperationException();

        }



        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            throw new UnsupportedOperationException();

        }



        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            this.chain = chain;

            tm.checkServerTrusted(chain, authType);
