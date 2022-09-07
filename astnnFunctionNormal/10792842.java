class BackupThread extends Thread {
    public String toString() {
        return write + "/" + read;
    }
}
