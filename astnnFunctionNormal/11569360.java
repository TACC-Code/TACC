class BackupThread extends Thread {
    public synchronized void informSentEmail(Card card, int threadNum) {
        this.sendingDialog.incrementProgress();
        this.bulkMailResults.numSent++;
        try {
            Date now = new Date();
            this.activityLogWriter.write(now, card, "Sent", "", threadNum);
            this.processedLogWriter.write(now, card);
        } catch (Exception e) {
            handleLoggingError(e);
        }
    }
}
