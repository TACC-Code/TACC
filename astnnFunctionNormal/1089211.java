class BackupThread extends Thread {
    public InputStream sendStream(String baseUrl, String method, Map<String, Object> params, InputStream in) throws HttpRequestException {
        String fullUrl = baseUrl + toRequestParamaters(params);
        try {
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            for (String key : params.keySet()) {
                connection.setRequestProperty(key, params.get(key).toString());
            }
            OutputStream outputStream = connection.getOutputStream();
            streamer.stream(in, outputStream);
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
