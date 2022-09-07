class BackupThread extends Thread {
    public void testHTTPClientWithProxyOff() throws Exception {
        proxySettings.setProxyType(ProxyType.NONE);
        fps.setProxyOn(false);
        fps.setAuthentication(false);
        fps.setProxyVersion(ProxyType.NONE);
        fps.setHttpRequest(true);
        String connectTo = "http://" + "localhost:" + DEST_PORT + "/myFile.txt";
        HttpGet get = new HttpGet(connectTo);
        get.addHeader("connection", "close");
        HttpResponse response = httpClient.execute(get);
        byte[] resp = IOUtils.readFully(response.getEntity().getContent());
        assertEquals("invalid response from server", "hello", new String(resp));
        get.abort();
    }
}
