class BackupThread extends Thread {
    @Test
    public void testClientSessionIsConnected() throws Exception {
        DummyClient client = createDummyClient("clientname");
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
                        if (session.isConnected() == true) {
                            System.err.println("session is connected");
                            return;
                        } else {
                            fail("Expected connected session: " + session);
                        }
                    }
                    fail("expected a connected session");
                }
            }, taskOwner);
        } finally {
            client.disconnect();
        }
    }
}
