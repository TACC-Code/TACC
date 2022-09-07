class BackupThread extends Thread {
    @Override
    public <T> BufferedChannel<T> createOneToAnyChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException {
        return createChannel(name, PortArity.ANY, readPortLimit, PortArity.ONE, writePortLimit, capacity);
    }
}
