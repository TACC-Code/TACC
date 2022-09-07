class BackupThread extends Thread {
        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, ClosedChannelException, MaxReadSizeExceededException {
            String attachment = (String) connection.getAttachment();
            if (attachment == null) {
                isErrorOccured.set(true);
                System.out.println("error");
            }
            int available = connection.available();
            if (available > 0) {
                connection.write(connection.readByteBufferByLength(available));
            }
            return true;
        }
}
