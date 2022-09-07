class BackupThread extends Thread {
        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readAvailable());
            connection.write(DELIMITER);
            return true;
        }
}
