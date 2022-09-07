class BackupThread extends Thread {
    private void transferData(InputStream is, OutputStream os) throws FromException, ToException {
        PacketisedInputStream from = new PacketisedInputStream(is);
        PacketisedOutputStream to = new PacketisedOutputStream(os);
        int read;
        try {
            read = from.read(my_buffer);
        } catch (IOException ioex) {
            throw (FromException) (new FromException(ioex.getMessage())).fillInStackTrace();
        }
        while (read > 0) {
            try {
                to.write(my_buffer, 0, read);
            } catch (IOException ioex) {
                throw (ToException) (new ToException(ioex.getMessage())).fillInStackTrace();
            }
            try {
                read = from.read(my_buffer);
            } catch (IOException ioex) {
                throw (FromException) (new FromException(ioex.getMessage())).fillInStackTrace();
            }
        }
        try {
            to.finish();
        } catch (Exception ex) {
        }
    }
}
