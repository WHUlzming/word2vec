            formparams.add(new BasicNameValuePair("servicename", "ZohoPC"));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");

            httppost.setEntity(entity);

            HttpResponse httpresponse = httpclient.execute(httppost);

            NULogger.getLogger().info("Getting cookies........");

            Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();

            Cookie escookie = null;

            while (it.hasNext()) {

                escookie = it.next();
