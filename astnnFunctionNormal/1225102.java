class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testDisconnectFromServerAfterLogout() throws Exception {
        final String name = "logout";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            client.logout();
            assertTrue(client.isConnected());
            assertTrue(client.waitForDisconnect());
        } finally {
            client.disconnect();
        }
    }
}
