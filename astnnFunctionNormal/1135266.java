class BackupThread extends Thread {
    public URLConnection createURLConnection(URL url) throws IOException {
        Proxy proxy = selectProxy(url);
        URLConnection co = (proxy == null) ? url.openConnection() : url.openConnection(proxy);
        configureURLConnection(co);
        return co;
    }
}
