class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLoggedInReturningNullClientSessionListener() throws Exception {
        DummyClient client = createDummyClient(RETURN_NULL);
        try {
            client.connect(serverNode.getAppPort());
            assertFalse(client.login());
            assertFalse(SimpleTestIdentityAuthenticator.allIdentities.getNotifyLoggedIn(RETURN_NULL));
        } finally {
            client.disconnect();
        }
    }
}
