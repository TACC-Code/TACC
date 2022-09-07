class BackupThread extends Thread {
    protected void setupCheckpointRecover() throws IOException {
        long started = System.currentTimeMillis();
        ;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Starting recovery setup -- copying into place " + "bdbje log files -- for checkpoint named " + this.checkpointRecover.getDisplayName());
        }
        this.checkpointer.recover(this);
        this.progressStats.info("CHECKPOINT RECOVER " + this.checkpointRecover.getDisplayName());
        File bdbSubDir = CheckpointUtils.getBdbSubDirectory(this.checkpointRecover.getDirectory());
        FileUtils.copyFiles(bdbSubDir, CheckpointUtils.getJeLogsFilter(), getStateDisk(), true, false);
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Finished recovery setup for checkpoint named " + this.checkpointRecover.getDisplayName() + " in " + (System.currentTimeMillis() - started) + "ms.");
        }
    }
}
