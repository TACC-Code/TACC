class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLoginTwicePreemptUser() throws Exception {
        tearDown(false);
        Properties props = SgsTestNode.getDefaultProperties(appName, null, DummyAppListener.class);
        props.setProperty("com.sun.sgs.impl.service.session.allow.new.login", "true");
        setUp(props, false, appName, protocolVersion);
        String name = "dummy";
        DummyClient client1 = createDummyClient(name);
        DummyClient client2 = createDummyClient(name);
        int port = serverNode.getAppPort();
        try {
            assertTrue(client1.connect(port).login());
            Thread.sleep(100);
            assertTrue(client2.connect(port).login());
            client1.checkDisconnectedCallback(false);
            assertTrue(client2.isConnected());
        } finally {
            client1.disconnect();
            client2.disconnect();
        }
    }
}
