class BackupThread extends Thread {
    @Override
    public <T> Channel<T> createChannel(String name, PortArity readPortArity, int readPortLimit, PortArity writePortArity, int writePortLimit) throws IllegalArgumentException {
        SimpleChannel<T> channel = new SimpleChannel<T>(name, readPortArity, readPortLimit, writePortArity, writePortLimit, 0);
        if (monitorChannels) {
            channels.add(new WeakReference<SimpleChannel<?>>(channel));
        }
        return channel;
    }
}
