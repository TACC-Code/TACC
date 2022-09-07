class BackupThread extends Thread {
    @Test
    public void testClientSessionToStringNoTransaction() throws Exception {
        final String name = "testClient";
        DummyClient client = createDummyClient(name);
        try {
            assertTrue(client.connect(serverNode.getAppPort()).login());
            GetClientSessionTask task = new GetClientSessionTask(name);
            txnScheduler.runTask(task, taskOwner);
            try {
                System.err.println("session: " + task.session.toString());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                fail("unexpected exception in ClientSessionWrapper.toString");
            }
        } finally {
            client.disconnect();
        }
    }
}
