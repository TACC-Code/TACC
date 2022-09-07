class BackupThread extends Thread {
        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.resetToReadMark();
            connection.markReadPosition();
            int i = connection.readInt();
            int length = connection.readInt();
            if (connection.available() >= length) {
                connection.removeReadMark();
                connection.write(connection.readByteBufferByLength(length));
            }
            connection.flush();
            return true;
        }
}
