class BackupThread extends Thread {
    public synchronized void write(Frame frame) {
        this.updateWrite();
        if (this.wo == this.ro && this.isReady) {
            this.updateRead();
        }
        buffer[wo] = frame;
        if (!this.isReady && (this.writeIndex() - this.readIndex()) >= delay) {
            this.isReady = true;
            if (listener != null) listener.onReady();
        }
    }
}
