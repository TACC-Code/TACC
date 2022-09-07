class BackupThread extends Thread {
    public void nudge(int on) {
        readIndex = writeIndex;
        writeIndex += on;
        int ns = getSampleCount();
        if (writeIndex >= ns) {
            writeIndex -= ns;
        }
    }
}
