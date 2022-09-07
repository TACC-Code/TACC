class BackupThread extends Thread {
    private void increaseWriteIndex() {
        writeIndex++;
        if (writeIndex == readIndex) {
            readIndex++;
        }
        if (writeIndex >= bufferSize) {
            writeIndex -= bufferSize;
        }
        if (readIndex >= bufferSize) {
            readIndex -= bufferSize;
        }
    }
}
