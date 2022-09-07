class BackupThread extends Thread {
    @Override
    public <T> Channel<T> createOneToOneChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException {
        return createChannel(name, PortArity.ONE, readPortLimit, PortArity.ONE, writePortLimit);
    }
}
