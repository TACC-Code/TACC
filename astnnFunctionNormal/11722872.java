class BackupThread extends Thread {
    protected boolean writeToFile(File file) {
        throw new RuntimeException("Cannot write file - this is a read-only application");
    }
}
