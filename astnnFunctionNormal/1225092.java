class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLoginRedirect() throws Exception {
        int serverAppPort = serverNode.getAppPort();
        String[] users = new String[] { "sleepy", "bashful", "dopey", "doc" };
        Set<DummyClient> clients = new HashSet<DummyClient>();
        addNodes(users);
        boolean failed = false;
        int redirectCount = 0;
        ConfigurableNodePolicy.setRoundRobinPolicy();
        try {
            for (String user : users) {
                DummyClient client = createDummyClient(user);
                client.connect(serverAppPort);
                if (client.login()) {
                    if (client.getConnectPort() != serverAppPort) {
                        redirectCount++;
                    }
                } else {
                    failed = true;
                    System.err.println("login for user: " + user + " failed");
                }
                clients.add(client);
            }
            int expectedRedirects = users.length - 1;
            if (redirectCount != expectedRedirects) {
                failed = true;
                System.err.println("Expected " + expectedRedirects + " redirects, got " + redirectCount);
            } else {
                System.err.println("Number of redirected users: " + redirectCount);
            }
            if (failed) {
                fail("test failed (see output)");
            }
        } finally {
            for (DummyClient client : clients) {
                try {
                    client.disconnect();
                } catch (Exception e) {
                    System.err.println("Exception disconnecting client: " + client);
                }
            }
        }
    }
}
