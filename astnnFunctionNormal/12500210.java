class BackupThread extends Thread {
    @Override
    public Channel<V> getChannel() {
        return wrapped.getChannel();
    }
}
