class BackupThread extends Thread {
    public String postWithCookie(String url, List<NameValuePair> _params) {
        String strResult;
        HttpPost httpRequest = new HttpPost(url);
        BasicHeader mBasicHeader = new BasicHeader("Cookie", Constant.cookie);
        httpRequest.setHeader(mBasicHeader);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params = _params;
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, "gb2312"));
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
