class BackupThread extends Thread {
    @Test
    public void testLoginSuccess() throws Exception {
        DummyClient client = createDummyClient("success");
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
        } finally {
            client.disconnect();
        }
    }
}
