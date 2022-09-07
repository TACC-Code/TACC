class BackupThread extends Thread {
    @Override
    public <T> BufferedChannel<T> createAnyToOneChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException {
        return createChannel(name, PortArity.ONE, readPortLimit, PortArity.ANY, writePortLimit, capacity);
    }
}
