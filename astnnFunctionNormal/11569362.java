class BackupThread extends Thread {
    public synchronized void informStartingEmail(Card card, int threadNum) {
        try {
            this.activityLogWriter.write(new Date(), card, "Starting", "", threadNum);
        } catch (Exception e) {
            handleLoggingError(e);
        }
    }
}
