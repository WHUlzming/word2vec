    private String[] parseContentTypeHeader(String header) {

        String[] result = new String[] { "text/plain", null };

        StringTokenizer st = new StringTokenizer(header, ";=");

        result[0] = st.nextToken();

        while (st.hasMoreTokens()) {

            String parameter = st.nextToken();

            if (st.hasMoreTokens()) {

                String value = stripQuotes(st.nextToken());

                if (parameter.trim().equalsIgnoreCase("charset")) {

                    result[1] = value;

                }

            }

        }

        return result;

    }
