class BackupThread extends Thread {
    @Test
    public void testClientSendWithListenerThrowingNonRetryableException() throws Exception {
        String name = "clientname";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            receivedMessageException = new MaybeRetryException("non-retryable", false);
            int numMessages = 5;
            for (int i = 0; i < numMessages; i++) {
                ByteBuffer buf = ByteBuffer.allocate(4);
                buf.putInt(i).flip();
                client.sendMessage(buf.array(), true);
            }
            client.waitForSessionListenerToReceiveExpectedMessages(numMessages - 1);
            client.validateMessageSequence(client.sessionListenerReceivedMessages, numMessages - 1, 1);
        } finally {
            client.disconnect();
        }
    }
}
