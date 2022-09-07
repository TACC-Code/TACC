class BackupThread extends Thread {
    public void run() {
        try {
            _selector = Selector.open();
            _serverSocket = OhuaServerSocketFactory.getInstance().createServerSocket(_serverPort);
            _serverSocket.getChannel().accept();
            _serverSocket.getChannel().register(_selector, SelectionKey.OP_ACCEPT);
            _selectionHandler.waitForSelector(_selector, this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cleanUp();
            System.out.println("Slave Daemon is shutting down!");
        }
    }
}
