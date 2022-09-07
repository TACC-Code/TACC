class BackupThread extends Thread {
    public String post(String url, List<NameValuePair> _params) {
        String strResult;
        HttpPost httpRequest = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params = _params;
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
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
