class BackupThread extends Thread {
    public void testHTTPClientProxiesWithAuthSocks4() throws Exception {
        proxySettings.setProxyType(ProxyType.SOCKS4);
        proxySettings.setProxyAuthRequired(true);
        proxySettings.setProxyUser(FakeProxyServer.USER);
        proxySettings.setProxyPass(FakeProxyServer.PASS);
        fps.setProxyOn(true);
        fps.setAuthentication(true);
        fps.setProxyVersion(ProxyType.SOCKS4);
        fps.setHttpRequest(true);
        String connectTo = "http://" + "localhost:" + DEST_PORT + "/myFile3.txt";
        HttpGet get = new HttpGet(connectTo);
        get.addHeader("connection", "close");
        HttpResponse response = httpClient.execute(get);
        String resp = new String(IOUtils.readFully(response.getEntity().getContent()));
        assertEquals("invalid response from server", "hello", resp);
        get.abort();
    }
}
