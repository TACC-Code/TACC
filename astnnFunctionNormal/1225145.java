class BackupThread extends Thread {
    @Test
    public void testRemoveSessionWhileSessionDisconnects() throws Exception {
        final String user = "foo";
        DummyClient client = createDummyClient(user);
        assertTrue(client.connect(serverNode.getAppPort()).login());
        client.sendMessage(new byte[0], true);
        client.logout();
        client.checkDisconnectedCallback(true);
    }
}
