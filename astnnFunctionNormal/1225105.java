class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLogoutRequestAndDisconnectedCallback() throws Exception {
        final String name = "logout";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            checkBindings(1);
            client.logout();
            client.checkDisconnectedCallback(true);
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
