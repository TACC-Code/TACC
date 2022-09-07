class BackupThread extends Thread {
    public void test_connectLjava_io_PipedWriter() throws Exception {
        char[] c = null;
        preader = new PipedReader();
        t = new Thread(pwriter = new PWriter(), "");
        preader.connect(pwriter.pw);
        t.start();
        Thread.sleep(500);
        c = new char[11];
        preader.read(c, 0, 11);
        assertEquals("Read incorrect chars", "Hello World", new String(c));
        try {
            preader.connect(pwriter.pw);
            fail("Failed to throw exception connecting to pre-connected reader");
        } catch (IOException e) {
        }
    }
}
