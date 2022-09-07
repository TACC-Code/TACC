class BackupThread extends Thread {
    public static String doPost2(String url, String user, String passd, List<BasicNameValuePair> list) {
        HttpPost httpRequest = new HttpPost(url);
        String result = "";
        if (!user.equals("") && !passd.equals("")) {
            httpRequest.setHeader("Authorization", "Basic " + Base64.encodeBytes((user + ":" + passd).getBytes()));
        }
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println(httpResponse.getStatusLine().getStatusCode());
                return result = EntityUtils.toString(httpResponse.getEntity());
            } else if (httpResponse.getStatusLine().getStatusCode() == 401) {
                return result = ConfigInfo.HTTPRETURN.HTTP_ERROR_401;
            } else if (result.equals("")) {
                return result = ConfigInfo.HTTPRETURN.HTTPERROR;
            } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                return result = "";
            } else {
                return result = ConfigInfo.HTTPRETURN.COMMHTTPERRORS;
            }
        } catch (Exception e) {
            return result = ConfigInfo.HTTPRETURN.COMMHTTPERRORS;
        }
    }
}
