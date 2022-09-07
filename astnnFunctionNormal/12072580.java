class BackupThread extends Thread {
        public void handleAccept(Socket socket) throws IOException {
            synchronized (this) {
                if (!running) {
                    IOUtils.close(socket);
                    return;
                }
            }
            DaapConnectionNIO connection = new DaapConnectionNIO(LimeDaapServerNIO.this, socket.getChannel());
            DaapController cont = new DaapController(connection);
            socket.setSoTimeout(0);
            allConnections.put(connection, cont);
            addPendingConnection(connection);
            ((NIOMultiplexor) socket).setReadObserver(cont);
            ((NIOMultiplexor) socket).setWriteObserver(cont);
        }
}
