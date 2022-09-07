class BackupThread extends Thread {
    public MMOConnection(final SelectorThread<T> selectorThread, final Socket socket, final SelectionKey key) {
        _selectorThread = selectorThread;
        _socket = socket;
        _address = socket.getInetAddress();
        _readableByteChannel = socket.getChannel();
        _writableByteChannel = socket.getChannel();
        _port = socket.getPort();
        _selectionKey = key;
        _sendQueue = new NioNetStackList<SendablePacket<T>>();
    }
}
