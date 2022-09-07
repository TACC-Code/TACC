class BackupThread extends Thread {
    private int getBufferContentSize() {
        if (readIndex <= writeIndex) {
            return writeIndex - readIndex;
        }
        return bufferSize - readIndex + writeIndex;
    }
}
