class BackupThread extends Thread {
    @Test
    public void testClientSend() throws Exception {
        String name = "clientname";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            client.sendMessagesFromClientInSequence(5, 5);
        } finally {
            client.disconnect();
        }
    }
}
