class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testSendBeforeLoginComplete() throws Exception {
        DummyClient client = createDummyClient("dummy");
        try {
            client.connect(serverNode.getAppPort());
            client.login(false);
            client.sendMessagesFromClientInSequence(1, 0);
        } finally {
            client.disconnect();
        }
    }
}
