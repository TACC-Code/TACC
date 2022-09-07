class BackupThread extends Thread {
    ReadableByteChannel getChannel() {
        return channel;
    }
}
