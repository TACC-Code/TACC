class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testDisconnectedCallbackThrowingNonRetryableException() throws Exception {
        DummyClient client = createDummyClient(DISCONNECT_THROWS_NONRETRYABLE_EXCEPTION);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            checkBindings(1);
            client.logout();
            client.checkDisconnectedCallback(true);
            Thread.sleep(250);
            checkBindings(0);
        } finally {
            client.disconnect();
        }
    }
}
