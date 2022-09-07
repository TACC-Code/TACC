class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLoginTwiceBlockUser() throws Exception {
        String name = "dummy";
        DummyClient client1 = createDummyClient(name);
        DummyClient client2 = createDummyClient(name);
        int port = serverNode.getAppPort();
        try {
            assertTrue(client1.connect(port).login());
            assertFalse(client2.connect(port).login());
        } finally {
            client1.disconnect();
            client2.disconnect();
        }
    }
}
