class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLoggedInReturningNonSerializableClientSessionListener() throws Exception {
        DummyClient client = createDummyClient(NON_SERIALIZABLE);
        try {
            client.connect(serverNode.getAppPort());
            assertFalse(client.login());
            assertFalse(SimpleTestIdentityAuthenticator.allIdentities.getNotifyLoggedIn(NON_SERIALIZABLE));
        } finally {
            client.disconnect();
        }
    }
}
