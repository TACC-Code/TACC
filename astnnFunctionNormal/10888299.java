class BackupThread extends Thread {
    public void run() {
        try {
            in = connection.openInputStream();
            out = connection.openOutputStream();
            Writer writer = new Writer(this);
            Thread writeThread = new Thread(writer);
            writeThread.start();
            listener.handleStreamsOpen(this);
        } catch (IOException e) {
            close();
            listener.handleStreamsOpenError(this, e.getMessage());
            return;
        }
        while (!aborting) {
            int length = 0;
            try {
                byte[] lengthBuf = new byte[LENGTH_MAX_DIGITS];
                readFully(in, lengthBuf);
                length = readLength(lengthBuf);
                byte[] temp = new byte[length];
                readFully(in, temp);
                listener.handleReceivedMessage(this, temp);
            } catch (IOException e) {
                close();
                if (length == 0) {
                    listener.handleClose(this);
                } else {
                    listener.handleErrorClose(this, e.getMessage());
                }
            }
        }
    }
}
