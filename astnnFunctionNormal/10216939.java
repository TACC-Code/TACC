class BackupThread extends Thread {
    public void configure(URL url) throws IOException {
        LogManager.getLogManager().readConfiguration(url.openStream());
    }
}
