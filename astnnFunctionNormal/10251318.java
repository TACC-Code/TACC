class BackupThread extends Thread {
    @Override
    public Channel getChannel() {
        return readable() ? readChannel : writeChannel;
    }
}
