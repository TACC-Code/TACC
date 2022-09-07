class BackupThread extends Thread {
    public Filter saveFilter(String blipItSvcHost, Filter filter) {
        Filter savedFilter = null;
        HttpEntity httpEntity = null;
        try {
            String filterJson = BlipItUtils.toFilterJson(filter);
            String blipItSvcUrl = String.format("http://%s/blipit/ad/filter", blipItSvcHost);
            StringEntity stringEntity = new StringEntity(filterJson, "UTF-8");
            stringEntity.setContentType(JSON_CONTENT_TYPE);
            HttpPost httpPost = new HttpPost(blipItSvcUrl);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            savedFilter = BlipItUtils.toFilter(convertStreamToString(content));
        } catch (UnsupportedEncodingException e) {
            logError(e);
        } catch (ClientProtocolException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return savedFilter;
    }
}
