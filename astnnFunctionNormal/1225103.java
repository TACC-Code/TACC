class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testDisconnectedCallbackAfterClientDropsConnection() throws Exception {
        final String name = "dummy";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            checkBindings(1);
            client.disconnect();
            client.checkDisconnectedCallback(false);
            checkBindings(0);
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    try {
                        dataService.getBinding(name);
                        fail("expected ObjectNotFoundException: " + "object not removed");
                    } catch (ObjectNotFoundException e) {
                    }
                }
            }, taskOwner);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("testLogout interrupted");
        } finally {
            client.disconnect();
        }
    }
}
