    private String buildQueryString(Map<String, String> params) {

        StringBuffer query = new StringBuffer();

        if (params.size() > 0) {

            query.append("?");

            for (String key : params.keySet()) {

                query.append(key);

                query.append("=");

                query.append(encodeParameter(params.get(key)));

                query.append("&");

            }

            if (query.charAt(query.length() - 1) == '&') {

                query.deleteCharAt(query.length() - 1);

            }

        }

        return query.toString();

    }



    protected String encodeParameter(String s) {
