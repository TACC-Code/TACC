class BackupThread extends Thread {
    public synchronized void informFailedEmail(Card card, int threadNum, Exception e) {
        this.sendingDialog.incrementProgress();
        this.bulkMailResults.numFailed++;
        try {
            Date now = new Date();
            this.activityLogWriter.write(now, card, "Failed", e.getMessage(), threadNum);
            this.failedLogWriter.write(now, card);
            this.processedLogWriter.write(now, card);
        } catch (Exception ex) {
            handleLoggingError(ex);
        }
    }
}
