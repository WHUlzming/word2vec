            nameValuePairs.add(new BasicNameValuePair("key", key));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);

            HttpResponse response = httpclient.execute(httppost);

            jsonstring = EntityUtils.toString(response.getEntity());

            Log.d("jsonStringReturned:", jsonstring);
