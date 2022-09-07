class BackupThread extends Thread {
    public void send(String baseUrl, String method, Map<String, Object> params) throws HttpRequestException {
        String fullUrl = baseUrl + toRequestParamaters(params);
        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            for (String key : params.keySet()) {
                connection.setRequestProperty(key, params.get(key).toString());
            }
            int rc = connection.getResponseCode();
            Log.info(this, method + " " + fullUrl + " --> " + rc);
        } catch (MalformedURLException e) {
            throw new HttpRequestException(e);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
}
