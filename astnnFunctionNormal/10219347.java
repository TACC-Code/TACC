class BackupThread extends Thread {
        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
            connection.write(connection.readByteBufferByDelimiter("\r\n"));
            return true;
        }
}
