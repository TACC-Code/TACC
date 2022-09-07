class BackupThread extends Thread {
    public void waitingForWrite(SimpleNetworkWriterOperator writerOp) {
        SocketChannel channel = writerOp.getChannel();
        _selectionHandler.issueChangeRequest(channel, SelectionKey.OP_WRITE);
    }
}
