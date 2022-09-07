class BackupThread extends Thread {
    public InputStream read(String baseUrl, String method, Map<String, Object> params) throws HttpRequestException {
        String fullUrl = baseUrl + toRequestParamaters(params);
        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("User-Agent", useragent);
            for (String key : params.keySet()) {
                connection.setRequestProperty(key, params.get(key).toString());
            }
            int rc = connection.getResponseCode();
            Log.info(this, method + " " + fullUrl + " --> " + rc);
            if (HttpURLConnection.HTTP_OK != rc) {
                String error = cr.toString(connection.getErrorStream());
                throw new HttpRequestException(rc, error);
            }
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            throw new HttpRequestException(e);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
}
