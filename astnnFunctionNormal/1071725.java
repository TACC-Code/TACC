class BackupThread extends Thread {
    @Override
    public <T> Channel<T> createOneToAnyChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException {
        return createChannel(name, PortArity.ANY, readPortLimit, PortArity.ONE, writePortLimit);
    }
}
