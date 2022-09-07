class BackupThread extends Thread {
    public String getWithCookie(String url) {
        String strResult;
        HttpGet httpRequest = new HttpGet(url);
        BasicHeader mBasicHeader = new BasicHeader("Cookie", Constant.cookie);
        httpRequest.setHeader(mBasicHeader);
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity());
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
