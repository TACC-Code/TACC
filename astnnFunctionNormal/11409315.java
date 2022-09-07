class BackupThread extends Thread {
        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            connection.setAutoflush(false);
            writer = new WriteProcessor(connection);
            Thread t = new Thread(writer);
            t.start();
            return true;
        }
}
