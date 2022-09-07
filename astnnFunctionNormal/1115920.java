class BackupThread extends Thread {
    public void testUsingProxy2() throws Exception {
        try {
            System.setProperty("http.proxyHost", "localhost");
            System.setProperty("http.proxyPort", jettyPort + "");
            URL url = new URL(jettyURL);
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.getInputStream();
            assertTrue(urlConnect.usingProxy());
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            serverSocket.close();
            System.setProperty("http.proxyPort", port + "");
            url = new URL(jettyURL);
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.getInputStream();
            assertFalse(urlConnect.usingProxy());
            url = new URL("http://localhost:" + port);
            urlConnect = (HttpURLConnection) url.openConnection();
            try {
                urlConnect.getInputStream();
                fail("should throw ConnectException");
            } catch (ConnectException e) {
            }
            assertFalse(urlConnect.usingProxy());
        } finally {
            System.setProperties(null);
        }
    }
}
