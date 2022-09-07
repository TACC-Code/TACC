class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLogoutAndNotifyLoggedOutCallback() throws Exception {
        String name = "logout";
        DummyClient client = createDummyClient(name);
        try {
            assertTrue(client.connect(serverNode.getAppPort()).login());
            client.logout();
            if (SimpleTestIdentityAuthenticator.allIdentities.getNotifyLoggedIn(name)) {
                System.err.println("notifyLoggedIn invoked for identity: " + name);
            } else {
                fail("notifyLoggedIn not invoked for identity: " + name);
            }
            if (SimpleTestIdentityAuthenticator.allIdentities.getNotifyLoggedOut(name)) {
                System.err.println("notifyLoggedOut invoked for identity: " + name);
            } else {
                fail("notifyLoggedOut not invoked for identity: " + name);
            }
        } finally {
            client.disconnect();
        }
    }
}
