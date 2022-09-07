class BackupThread extends Thread {
    protected void doTestUnknownContentLength(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        long actualContentLength = conn.getContentLength();
        assertEquals("Invalid explicit content length", -1L, actualContentLength);
    }
}
