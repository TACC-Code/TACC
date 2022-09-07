class BackupThread extends Thread {
    public XXXMessageRouterRunner(MessageRouter<T> router, MessageIOReader<? extends T> reader, MessageIOWriter<T> writer) {
        setRouter(router);
        _reader = reader;
        _writer = writer;
    }
}
