class BackupThread extends Thread {
    private synchronized void setPaused(boolean paused) {
        if (this.paused == paused) return;
        this.paused = paused;
        if (paused) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
            if (writeThread != null) synchronized (writeThread) {
                writeThread.notify();
            }
        }
    }
}
