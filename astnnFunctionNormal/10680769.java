class BackupThread extends Thread {
    private boolean isSinglePacketPresent() {
        return this.readCursor == this.writeCursor;
    }
}
