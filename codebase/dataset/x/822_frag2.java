    private String getData(String url) throws Exception {

        u = new URL(url);

        uc = (HttpURLConnection) u.openConnection();

        uc.setRequestProperty("Cookie", UploadedDotToAccount.getPhpsessioncookie() + ";" + UploadedDotToAccount.getLogincookie() + ";" + UploadedDotToAccount.getAuthcookie());

        br = new BufferedReader(new InputStreamReader(uc.getInputStream()));

        String k = "";

        while ((tmp = br.readLine()) != null) {

            k += tmp;

        }

        return k;

    }
