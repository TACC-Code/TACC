class BackupThread extends Thread {
    @Override
    public void close() {
        if (closed()) {
            return;
        }
        if (closefd) {
            try {
                if (readChannel != null) {
                    readChannel.close();
                    if (writeChannel != null && readChannel != writeChannel) {
                        writeChannel.close();
                    }
                } else {
                    writeChannel.close();
                }
            } catch (IOException ioe) {
                throw Py.IOError(ioe);
            }
        }
        super.close();
    }
}
