    public static String move_files(String sessionid, String keys, String absolutePathForTheDestinationTag) {

        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";

        try {

            Log.d("current running function name:", "move_files");

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));

            nameValuePairs.add(new BasicNameValuePair("m", "move_file"));

            nameValuePairs.add(new BasicNameValuePair("absolute_new_parent_tag", absolutePathForTheDestinationTag));

            nameValuePairs.add(new BasicNameValuePair("keys", keys));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);

            HttpResponse response = httpclient.execute(httppost);

            resultJsonString = EntityUtils.toString(response.getEntity());

            Log.d("jsonStringReturned:", resultJsonString);

            return resultJsonString;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return resultJsonString;

    }



    /**

	 * rename a specific file

	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session

	 * @param key specify the file key for you to rename

	 * @param newFileName the new file name you want to set

	 */

    public static String rename_file(String sessionid, String key, String newFileName) {
