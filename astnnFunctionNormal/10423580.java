class BackupThread extends Thread {
    public LWOBFileReader(java.net.URL url) throws java.io.IOException {
        super(url.openStream());
        debugPrinter = new DebugOutput(127);
        marker = 0;
    }
}
