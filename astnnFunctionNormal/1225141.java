class BackupThread extends Thread {
    @Test
    public void testClientSendWithListenerThrowingRetryableException() throws Exception {
        String name = "clientname";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            receivedMessageException = new MaybeRetryException("retryable", true);
            client.sendMessagesFromClientInSequence(5, 5);
        } finally {
            client.disconnect();
        }
    }
}
