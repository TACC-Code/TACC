class BackupThread extends Thread {
    private void testClientSessionRemoveWithManagedListener(final boolean removeListener) throws Exception {
        final String name = "testClient";
        int objectCount = getObjectCount();
        DummyClient client = createDummyClient(name);
        try {
            assertTrue(client.connect(serverNode.getAppPort()).login());
            System.err.println("Objects added: " + (getObjectCount() - objectCount));
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    System.err.println("forcing disconnection, session:" + name);
                    ClientSession session = (ClientSession) dataService.getBinding(name);
                    DummyClientSessionListener listener = (DummyClientSessionListener) dataService.getBinding(name + ".listener");
                    if (removeListener) {
                        dataService.removeObject(listener);
                    }
                    dataService.removeObject(session);
                }
            }, taskOwner);
            Thread.sleep(500);
            assertEquals(objectCount + (removeListener ? 0 : 1), getObjectCount());
        } finally {
            client.disconnect();
        }
    }
}
