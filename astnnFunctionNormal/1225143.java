class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testLocalSendPerformance() throws Exception {
        final String user = "dummy";
        DummyClient client = createDummyClient(user);
        assertTrue(client.connect(serverNode.getAppPort()).login());
        int numIterations = 10;
        final ByteBuffer msg = ByteBuffer.allocate(0);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numIterations; i++) {
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    DataManager dataManager = AppContext.getDataManager();
                    ClientSession session = (ClientSession) dataManager.getBinding(user);
                    session.send(msg);
                }
            }, taskOwner);
        }
        long endTime = System.currentTimeMillis();
        System.err.println("send, iterations: " + numIterations + ", elapsed time: " + (endTime - startTime) + " ms.");
    }
}
