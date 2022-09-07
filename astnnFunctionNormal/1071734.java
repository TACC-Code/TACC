class BackupThread extends Thread {
    @Override
    public <T> BufferedChannel<T> createChannel(String name, PortArity readPortArity, int readPortLimit, PortArity writePortArity, int writePortLimit, int capacity) throws IllegalArgumentException {
        SimpleChannel<T> channel = new SimpleChannel<T>(name, readPortArity, readPortLimit, writePortArity, writePortLimit, capacity);
        if (monitorChannels) {
            channels.add(new WeakReference<SimpleChannel<?>>(channel));
        }
        return channel;
    }
}
