class BackupThread extends Thread {
    @Test
    public void testReadLine2() throws Exception {
        ServerHandler srvHdl = new ServerHandler();
        IServer server = new Server(srvHdl);
        server.start();
        IBlockingConnection clientCon = new BlockingConnection("localhost", server.getLocalPort());
        QAUtil.sleep(1000);
        INonBlockingConnection serverCon = srvHdl.getConection();
        File file = QAUtil.createTestfile_400k();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel fc = raf.getChannel();
        serverCon.transferFrom(fc);
        fc.close();
        raf.close();
        serverCon.close();
        QAUtil.sleep(1000);
        InputStream is = Channels.newInputStream(clientCon);
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        do {
            line = lnr.readLine();
            if (line != null) {
                sb.append(line + "\r\n");
            }
        } while (line != null);
        InputStream is2 = new FileInputStream(file);
        LineNumberReader lnr2 = new LineNumberReader(new InputStreamReader(is2));
        StringBuilder sb2 = new StringBuilder();
        String line2 = null;
        do {
            line2 = lnr2.readLine();
            if (line2 != null) {
                sb2.append(line2 + "\r\n");
            }
        } while (line2 != null);
        Assert.assertEquals(sb2.toString(), sb.toString());
        file.delete();
        clientCon.close();
        server.close();
    }
}
