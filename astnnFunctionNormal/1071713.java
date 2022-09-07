class BackupThread extends Thread {
    @Override
    public <T> Channel<T> createAnyToAnyChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException {
        return createChannel(name, PortArity.ANY, readPortLimit, PortArity.ANY, writePortLimit);
    }
}
