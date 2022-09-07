class BackupThread extends Thread {
    public HttpURLConnection proxiedURLConnection(URL url) throws IOException, PussycatException {
        HttpURLConnection uc = null;
        if (this.httpProxy == null || this.httpProxy.equals("") || PussycatUtils.isLocalURL(url.toString())) {
            System.getProperties().put("proxySet", "false");
        } else {
            System.getProperties().put("proxySet", "true");
        }
        if (System.getProperties().getProperty("proxySet").equals("true")) {
            uc = (java.net.HttpURLConnection) url.openConnection(new java.net.Proxy(java.net.Proxy.Type.HTTP, new java.net.InetSocketAddress(this.httpProxy, this.httpProxyPort)));
        } else {
            uc = (java.net.HttpURLConnection) url.openConnection();
        }
        return uc;
    }
}
