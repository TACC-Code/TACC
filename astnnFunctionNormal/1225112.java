class BackupThread extends Thread {
    @Test
    public void testClientSessionGetName() throws Exception {
        final String name = "clientname";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    DummyAppListener appListener = getAppListener();
                    Set<ClientSession> sessions = appListener.getSessions();
                    if (sessions.isEmpty()) {
                        fail("appListener contains no client sessions!");
                    }
                    for (ClientSession session : appListener.getSessions()) {
                        if (session.getName().equals(name)) {
                            System.err.println("names match");
                            return;
                        } else {
                            fail("Expected session name: " + name + ", got: " + session.getName());
                        }
                    }
                    fail("expected disconnected session");
                }
            }, taskOwner);
        } finally {
            client.disconnect();
        }
    }
}
