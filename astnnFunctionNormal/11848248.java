class BackupThread extends Thread {
    public CompositeParser(IParser reader, IParser writer) {
        this.reader = reader;
        this.writer = writer;
    }
}
