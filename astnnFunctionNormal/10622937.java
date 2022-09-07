class BackupThread extends Thread {
    public java.nio.channels.FileChannel getChannel() {
        return raf.getChannel();
    }
}
