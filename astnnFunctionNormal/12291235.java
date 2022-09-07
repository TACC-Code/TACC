class BackupThread extends Thread {
        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
            connection.write(connection.readByteBufferByLength(connection.available()));
            return true;
        }
}
