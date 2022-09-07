class BackupThread extends Thread {
    @Override
    public ChannelReader getChannelReader() {
        return reader;
    }
}
