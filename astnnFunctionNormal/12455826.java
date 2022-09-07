class BackupThread extends Thread {
        public boolean onData(INonBlockingConnection connection) throws IOException {
            synchronized (connection) {
                connection.write(connection.readStringByDelimiter("\r\n") + "\r\n");
            }
            return true;
        }
}
