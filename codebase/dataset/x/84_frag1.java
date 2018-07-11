    private String encodeRequestData(BT747Hashtable data, String encodingOrNull) throws UnsupportedEncodingException {

        String encoding = DEFAULT_ENCODING;

        if (encodingOrNull != null) {

            encoding = encodingOrNull;

        }

        StringBuffer encodedData = new StringBuffer();

        BT747Hashtable it = data.iterator();

        while (it.hasNext()) {

            String key = (String) it.nextKey();

            String value = (String) data.get(key);

            if (encodedData.length() > 0) {

                encodedData.append('&');

            }

            encodedData.append(URLEncoder.encode(key, encoding));

            encodedData.append('=');

            encodedData.append(URLEncoder.encode(value, encoding));

        }

        return encodedData.toString();

    }
