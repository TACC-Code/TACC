class BackupThread extends Thread {
    public String getAdsAsJson(String blipItSvcHost, String filterId) {
        HttpEntity httpEntity = null;
        String adsJson = null;
        try {
            String blipItSvcUrl = String.format("http://%s/blipit/ad/filter/%s", blipItSvcHost, filterId);
            HttpGet httpGet = new HttpGet(blipItSvcUrl);
            httpGet.addHeader("Content-Type", JSON_CONTENT_TYPE);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            adsJson = convertStreamToString(content);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return adsJson;
    }
}
