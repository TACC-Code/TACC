class BackupThread extends Thread {
    public Vector getChannel(int n) {
        return bufferChannelFile[n - 1];
    }
}
