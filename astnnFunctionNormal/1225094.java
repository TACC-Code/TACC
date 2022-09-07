class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testSendAfterLoginComplete() throws Exception {
        DummyClient client = createDummyClient("dummy");
        try {
            client.connect(serverNode.getAppPort());
            client.login(true);
            client.sendMessagesFromClientInSequence(1, 1);
        } finally {
            client.disconnect();
        }
    }
}
