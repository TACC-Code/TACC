class BackupThread extends Thread {
    protected String getPostRequestContent(String urlText, String method, String headerKey, String headerValue) throws Exception {
        URL url = new URL(urlText);
        HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
        urlcon.setRequestMethod(method);
        urlcon.setUseCaches(false);
        urlcon.setDoOutput(true);
        if (headerKey != null && headerValue != null) {
            urlcon.addRequestProperty(headerKey, headerValue);
        }
        urlcon.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
        String line = reader.readLine();
        reader.close();
        urlcon.disconnect();
        return line;
    }
}
