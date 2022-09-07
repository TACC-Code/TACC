class BackupThread extends Thread {
    @Override
    public ChannelWriter getChannelWriter() {
        return writer;
    }
}
