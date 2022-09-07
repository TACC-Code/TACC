class BackupThread extends Thread {
    void set_dictionary(byte[] d, int start, int n) {
        System.arraycopy(d, start, window, 0, n);
        read = write = n;
    }
}
