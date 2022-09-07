class BackupThread extends Thread {
    @Override
    public boolean isReadWrite() {
        return readwrite;
    }
}
