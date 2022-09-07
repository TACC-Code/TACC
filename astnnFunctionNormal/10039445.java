class BackupThread extends Thread {
    public String get(String url) {
        String strResult;
        HttpGet httpRequest = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity(), "gbk");
            } else {
                strResult = "bengle";
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            strResult = "bengle";
        } catch (IOException e) {
            e.printStackTrace();
            strResult = "bengle";
        } catch (Exception e) {
            e.printStackTrace();
            strResult = "bengle";
        }
        return strResult;
    }
}
