class BackupThread extends Thread {
    @Test
    public void testBlocking() throws Exception {
        Handler serverHandler = new Handler("s");
        IServer server = new Server(serverHandler);
        StreamUtils.start(server);
        int received = 0;
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        connection.setAutoflush(false);
        WriteProcessor writeProcessor = new WriteProcessor(connection);
        new Thread(writeProcessor).start();
        do {
            byte[] response = connection.readBytesByLength(RECORD.length);
            Assert.assertTrue(QAUtil.isEquals(response, RECORD));
            System.out.print("c");
            received++;
        } while (received < LOOPS);
        do {
            QAUtil.sleep(100);
        } while ((serverHandler.received < LOOPS));
        Assert.assertFalse(serverHandler.errorOccured);
        connection.close();
        server.close();
    }
}
