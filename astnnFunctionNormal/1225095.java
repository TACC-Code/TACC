class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLoginSuccessAndNotifyLoggedInCallback() throws Exception {
        String name = "success";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            if (SimpleTestIdentityAuthenticator.allIdentities.getNotifyLoggedIn(name)) {
                System.err.println("notifyLoggedIn invoked for identity: " + name);
            } else {
                fail("notifyLoggedIn not invoked for identity: " + name);
            }
        } finally {
            client.disconnect();
        }
    }
}
