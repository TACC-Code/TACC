class BackupThread extends Thread {
    @Test
    public void testSimple() throws Exception {
        IServer server = new Server(new EchoHandler());
        server.start();
        File file = QAUtil.createTestfile_400k();
        IBlockingConnection con = new BlockingConnection("localhost", server.getLocalPort());
        con.setFlushmode(FlushMode.SYNC);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer transferBuffer = ByteBuffer.allocate(4096);
        int read = 0;
        do {
            transferBuffer.clear();
            read = channel.read(transferBuffer);
            transferBuffer.flip();
            if (read > 0) {
                con.write(transferBuffer);
            }
        } while (read > 0);
        channel.close();
        raf.close();
        File tempFile = QAUtil.createTempfile();
        RandomAccessFile raf2 = new RandomAccessFile(tempFile, "rw");
        FileChannel fc2 = raf2.getChannel();
        con.transferTo(fc2, (int) file.length());
        fc2.close();
        raf2.close();
        QAUtil.isEquals(file, tempFile);
        file.delete();
        tempFile.delete();
        con.close();
        server.close();
    }
}
