class BackupThread extends Thread {
    private void switchEndian(byte[] b, int off, int readCount) {
        for (int i = off; i < (off + readCount); i += 2) {
            byte temp;
            temp = b[i];
            b[i] = b[i + 1];
            b[i + 1] = temp;
        }
    }
}
