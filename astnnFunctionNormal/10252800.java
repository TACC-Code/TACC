class BackupThread extends Thread {
    public void accept(SelectionKey key) {
        Socket socket = new Socket();
        try {
            SocketChannel channel = getChannel().accept();
            if (channel != null) {
                logger.fine("accept a not null channel");
                socket.setChannel(channel);
                socket.setSelector(getSelector());
                registerKeys();
            }
        } catch (IOException e) {
            logger.severe(e.toString());
        }
    }
}
