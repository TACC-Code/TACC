class BackupThread extends Thread {
    public HttpURLConnection connect(String baseUrl, String method, Map<String, Object> params) throws HttpRequestException {
        String fullUrl = baseUrl + toRequestParamaters(params);
        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            for (String key : params.keySet()) {
                connection.setRequestProperty(key, params.get(key).toString());
            }
            Log.info(this, method + " " + fullUrl + " --> opened for streaming");
            return connection;
        } catch (MalformedURLException e) {
            throw new HttpRequestException(e);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
}
