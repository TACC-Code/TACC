class BackupThread extends Thread {
    public void testUsingProxySelector() throws Exception {
        MockServer server = new MockServer("server");
        MockServer proxy = new MockServer("proxy");
        URL url = new URL("http://localhost:" + server.port());
        ProxySelector defPS = ProxySelector.getDefault();
        ProxySelector.setDefault(new TestProxySelector(server.port(), proxy.port()));
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
        } finally {
            ProxySelector.setDefault(defPS);
        }
    }
}
