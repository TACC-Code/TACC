class BackupThread extends Thread {
    public static String doGet(String url, String user, String passd) throws ClientProtocolException, IOException {
        String result = "";
        HttpGet httpRequest = new HttpGet(url);
        if (!user.equals("") && !passd.equals("")) {
            httpRequest.setHeader("Authorization", "Basic " + Base64.encodeBytes((user + ":" + passd).getBytes()));
        }
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            result = EntityUtils.toString(httpResponse.getEntity());
        }
        if (httpResponse.getStatusLine().getStatusCode() == 401) {
            result = ConfigInfo.HTTPRETURN.HTTP_ERROR_401;
        } else if (result.equals("")) {
            result = ConfigInfo.HTTPRETURN.HTTPERROR;
        }
        return result;
    }
}
