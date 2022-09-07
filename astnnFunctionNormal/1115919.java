class BackupThread extends Thread {
    public void testUsingProxy() throws Exception {
        MockServer server = new MockServer("server");
        MockServer proxy = new MockServer("proxy");
        URL url = new URL("http://localhost:" + server.port());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", proxy.port())));
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(2000);
        server.start();
        synchronized (bound) {
            if (!server.started) bound.wait(5000);
        }
        proxy.start();
        synchronized (bound) {
            if (!proxy.started) bound.wait(5000);
        }
        connection.connect();
        server.join();
        proxy.join();
        assertTrue("Connection does not use proxy", connection.usingProxy());
        assertTrue("Proxy server was not used", proxy.accepted);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
        assertFalse(huc.usingProxy());
    }
}
