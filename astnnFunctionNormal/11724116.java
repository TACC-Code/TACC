class BackupThread extends Thread {
    public InputStream openWithProxy(URI uri) throws IOException {
        IProxyService proxyService = getProxyService();
        IProxyData[] proxyDataForHost = proxyService.select(uri);
        for (IProxyData data : proxyDataForHost) {
            if (data.getHost() != null) {
                System.setProperty("http.proxySet", "true");
                System.setProperty("http.proxyHost", data.getHost());
            }
            if (data.getHost() != null) {
                System.setProperty("http.proxyPort", String.valueOf(data.getPort()));
            }
        }
        URL url;
        url = uri.toURL();
        return new BufferedInputStream(url.openStream());
    }
}
