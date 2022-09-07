class BackupThread extends Thread {
    @Override
    public <T> BufferedChannel<T> createOneToOneChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException {
        return createChannel(name, PortArity.ONE, readPortLimit, PortArity.ONE, writePortLimit, capacity);
    }
}
