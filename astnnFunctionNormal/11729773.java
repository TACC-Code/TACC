class BackupThread extends Thread {
    @Override
    public WritableByteChannel getWritableChannel() {
        try {
            if (file.exists() && file.length() > 0) {
                throw new IOException("File exists - overwriting not allowed");
            }
            WritableByteChannel channel = null;
            if (allowRandomAccess) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                channel = randomAccessFile.getChannel();
            } else {
                OutputStream os = new FileOutputStream(file);
                channel = Channels.newChannel(os);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Opened write channel to file: \n" + "   file: " + file + "\n" + "   random-access: " + allowRandomAccess);
            }
            return channel;
        } catch (Throwable e) {
            return null;
        }
    }
}
