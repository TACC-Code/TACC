class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLoggedInThrowingRuntimeException() throws Exception {
        DummyClient client = createDummyClient(THROW_RUNTIME_EXCEPTION);
        try {
            client.connect(serverNode.getAppPort());
            assertFalse(client.login());
            assertFalse(SimpleTestIdentityAuthenticator.allIdentities.getNotifyLoggedIn(THROW_RUNTIME_EXCEPTION));
        } finally {
            client.disconnect();
        }
    }
}
