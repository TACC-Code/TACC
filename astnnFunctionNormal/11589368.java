class BackupThread extends Thread {
    public NSDataOutputStream(String file, int offset) throws IOException {
        this.file = file;
        write(readOff(file, offset));
    }
}
