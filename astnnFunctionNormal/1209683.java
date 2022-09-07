class BackupThread extends Thread {
    public synchronized Frame read() {
        if (!this.isReady) {
            return null;
        }
        frame = buffer[ro];
        buffer[ro] = null;
        this.updateRead();
        if (this.readIndex() - 1 == this.writeIndex()) {
            this.isReady = false;
        }
        return frame;
    }
}
