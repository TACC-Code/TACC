class BackupThread extends Thread {
    @Override
    public <T> BufferedChannel<T> createAnyToAnyChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException {
        return createChannel(name, PortArity.ANY, readPortLimit, PortArity.ANY, writePortLimit, capacity);
    }
}
