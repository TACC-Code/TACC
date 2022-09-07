class BackupThread extends Thread {
    public static String getXML(String url) {
        String line = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            line = EntityUtils.toString(httpEntity);
        } catch (Exception e) {
            line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
            line = null;
        }
        return line;
    }
}
