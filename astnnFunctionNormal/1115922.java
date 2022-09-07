class BackupThread extends Thread {
    public void testProxyAuthorization() throws Exception {
        Authenticator.setDefault(new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("user", "password".toCharArray());
            }
        });
        try {
            MockProxyServer proxy = new MockProxyServer("ProxyServer");
            URL url = new URL("http://remotehost:55555/requested.data");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", proxy.port())));
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            proxy.start();
            connection.connect();
            assertEquals("unexpected response code", 200, connection.getResponseCode());
            proxy.join();
            assertTrue("Connection did not send proxy authorization request", proxy.acceptedAuthorizedRequest);
        } finally {
            Authenticator.setDefault(null);
        }
    }
}
