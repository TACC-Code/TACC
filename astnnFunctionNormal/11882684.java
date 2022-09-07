class BackupThread extends Thread {
    public Ico(URL url) throws BadIcoResException, IOException {
        this(url.openStream());
    }
}
