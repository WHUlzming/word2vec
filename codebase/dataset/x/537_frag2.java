        if (firstColon <= 0) {

            throw new MalformedURLException("no protocol specified: " + url);

        } else {

            final Map handlerMap = getHandlerMap();

            final String protocol = url.substring(0, firstColon);

            final URLStreamHandler handler;

            if ((handlerMap == null) || (handler = (URLStreamHandler) handlerMap.get(protocol)) == null) throw new MalformedURLException("unknown protocol: " + protocol);
