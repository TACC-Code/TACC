class BackupThread extends Thread {
    public static void copyURLToFile(URL url, File file, final String proxyHost, final int proxyPort, final String proxyUsername, final String proxyPassword) throws Exception {
        HttpURLConnection connection = null;
        if (proxyHost == null) {
            connection = (HttpURLConnection) url.openConnection();
        } else {
            if (proxyUsername != null && proxyPassword != null) {
                Authenticator.setDefault(new Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
                    }
                });
            }
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = (HttpURLConnection) url.openConnection(proxy);
        }
        InputStream inputStream = connection.getInputStream();
        if (connection.getResponseCode() != 200 || inputStream == null) throw new Exception("HTTP error: " + connection.getResponseCode() + " - " + connection.getResponseMessage());
        org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
    }
}
