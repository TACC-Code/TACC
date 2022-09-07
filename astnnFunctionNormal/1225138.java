class BackupThread extends Thread {
    @Test
    public void testClientSessionSendAbortRetryable() throws Exception {
        DummyClient client = createDummyClient("clientname");
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                int tryCount = 0;

                public void run() {
                    Set<ClientSession> sessions = getAppListener().getSessions();
                    ClientSession session = sessions.iterator().next();
                    try {
                        session.send(ByteBuffer.wrap(new byte[4096]));
                    } catch (MessageRejectedException e) {
                        fail("Should not run out of buffer space: " + e);
                    }
                    if (++tryCount < 100) {
                        throw new MaybeRetryException("Retryable", true);
                    }
                }
            }, taskOwner);
        } finally {
            client.disconnect();
        }
    }
}
