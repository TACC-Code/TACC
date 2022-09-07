class BackupThread extends Thread {
    public String getAllChannelsAsJson(String blipItSvcHost, String category) {
        HttpEntity httpEntity = null;
        String channelsJson = null;
        try {
            String blipItSvcUrl = String.format("http://%s/blipit/%s/channel", blipItSvcHost, category);
            HttpGet httpGet = new HttpGet(blipItSvcUrl);
            httpGet.addHeader("Content-Type", JSON_CONTENT_TYPE);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            channelsJson = convertStreamToString(content);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return channelsJson;
    }
}
