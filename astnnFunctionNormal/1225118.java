class BackupThread extends Thread {
    @Test
    public void testClientSessionToString() throws Exception {
        final String name = "testClient";
        DummyClient client = createDummyClient(name);
        try {
            assertTrue(client.connect(serverNode.getAppPort()).login());
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    ClientSession session = (ClientSession) dataService.getBinding(name);
                    if (!(session instanceof ClientSessionWrapper)) {
                        fail("session not instance of " + "ClientSessionWrapper");
                    }
                    System.err.println("session: " + session);
                }
            }, taskOwner);
        } finally {
            client.disconnect();
        }
    }
}
