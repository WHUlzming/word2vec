            final URL url = new URL(params.getServer() + "/cgi/login.pl");

            http = NetUtils.doPostMultipart(url, getLogin());

            final String response = new String(StreamReader.read(http.getInputStream()), Charset.defaultCharset());
