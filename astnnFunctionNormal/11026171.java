class BackupThread extends Thread {
    public static HttpURLConnection getHttpUrlConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection) {
            return (HttpURLConnection) connection;
        } else {
            throw new IllegalArgumentException("Url is not targeted at an HTTP resource" + url);
        }
    }
}
