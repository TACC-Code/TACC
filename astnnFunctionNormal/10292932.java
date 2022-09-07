class BackupThread extends Thread {
    public void testCtorWithCreateDirectory() {
        File tmp = new File("toto" + new Date().getTime());
        if (tmp.exists()) throw new IllegalStateException("Wannabe temp file already exists");
        TemporaryFS fs = new TemporaryFS(tmp);
        assertTrue("File not created", fs.root().exists());
        assertTrue("Not a directory", fs.root().isDirectory());
        assertTrue("Cannot write or read", fs.root().canWrite() && fs.root().canRead());
        tmp.delete();
    }
}
