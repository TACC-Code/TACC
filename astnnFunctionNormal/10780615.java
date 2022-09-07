class BackupThread extends Thread {
    protected InputStream getURLInputStream() throws Exception {
        URL url = new URL(fFilename);
        InputStream fin = url.openStream();
        BufferedInputStream bin = new BufferedInputStream(fin);
        return bin;
    }
}
