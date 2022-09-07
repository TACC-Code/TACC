class BackupThread extends Thread {
    public void testHTTPClientProxiesWithSocks5() throws Exception {
        proxySettings.setProxyType(ProxyType.SOCKS5);
        fps.setProxyOn(true);
        fps.setAuthentication(false);
        fps.setProxyVersion(ProxyType.SOCKS5);
        fps.setHttpRequest(true);
        String connectTo = "http://" + "localhost:" + DEST_PORT + "/myFile4.txt";
        HttpGet get = new HttpGet(connectTo);
        get.addHeader("connection", "close");
        HttpResponse response = httpClient.execute(get);
        String resp = new String(IOUtils.readFully(response.getEntity().getContent()));
        assertEquals("invalid response from server", "hello", resp);
        get.abort();
    }
}
