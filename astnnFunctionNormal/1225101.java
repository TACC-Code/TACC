class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testHighWater() throws Exception {
        tearDown(false);
        Properties props = SgsTestNode.getDefaultProperties(appName, null, DummyAppListener.class);
        props.setProperty("com.sun.sgs.impl.service.session.login.high.water", "2");
        setUp(props, false, appName, protocolVersion);
        DummyClient client1 = createDummyClient("client1");
        DummyClient client2 = createDummyClient("client2");
        int port = serverNode.getAppPort();
        WatchdogService watchdog = serverNode.getWatchdogService();
        try {
            client1.connect(port).login();
            Thread.sleep(100);
            assertTrue(client1.isConnected());
            assertEquals(Health.GREEN, watchdog.getLocalNodeHealthNonTransactional());
            client2.connect(port).login();
            Thread.sleep(100);
            assertTrue(client2.isConnected());
            assertEquals(Health.YELLOW, watchdog.getLocalNodeHealthNonTransactional());
            client1.disconnect();
            Thread.sleep(100);
            assertFalse(client1.isConnected());
            assertEquals(Health.GREEN, watchdog.getLocalNodeHealthNonTransactional());
        } finally {
            client1.disconnect();
            client2.disconnect();
        }
    }
}
