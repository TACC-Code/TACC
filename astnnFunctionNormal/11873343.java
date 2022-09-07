class BackupThread extends Thread {
    @Override
    SelectableChannel getChannel() {
        assert isTransportLayerThread();
        throw new UnsupportedOperationException("asynchronous connection not supported");
    }
}
