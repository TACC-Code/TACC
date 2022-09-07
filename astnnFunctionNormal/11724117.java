class BackupThread extends Thread {
    public InputStream openWithProxy(URL url) throws IOException {
        try {
            IProxyService proxyService = getProxyService();
            IProxyData[] proxyDataForHost = proxyService.select(url.toURI());
            for (IProxyData data : proxyDataForHost) {
                if (data.getHost() != null) {
                    System.setProperty("http.proxySet", "true");
                    System.setProperty("http.proxyHost", data.getHost());
                }
                if (data.getHost() != null) {
                    System.setProperty("http.proxyPort", String.valueOf(data.getPort()));
                }
            }
            return new BufferedInputStream(url.openStream());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
}
