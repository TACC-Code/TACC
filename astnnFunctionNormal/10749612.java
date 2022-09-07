class BackupThread extends Thread {
    protected void writeLock() throws AccessPoemException {
        writeLock(PoemThread.sessionToken());
    }
}
