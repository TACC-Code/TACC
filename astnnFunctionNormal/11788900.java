class BackupThread extends Thread {
    public void testHTTPClientProxiesWithHttpProxy() throws Exception {
        proxySettings.setProxyType(ProxyType.HTTP);
        fps.setProxyOn(true);
        fps.setAuthentication(false);
        fps.setProxyVersion(ProxyType.HTTP);
        fps.setHttpRequest(true);
        String connectTo = "http://" + "localhost:" + DEST_PORT + "/myFile2.txt";
        HttpGet get = new HttpGet(connectTo);
        get.addHeader("connection", "close");
        HttpResponse response = httpClient.execute(get);
        String resp = new String(IOUtils.readFully(response.getEntity().getContent()));
        assertEquals("invalid response from server", "hello", resp);
        get.abort();
    }
}
