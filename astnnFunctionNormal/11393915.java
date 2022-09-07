class BackupThread extends Thread {
    @Override
    public Channel getChannel() {
        return fileChannel;
    }
}
