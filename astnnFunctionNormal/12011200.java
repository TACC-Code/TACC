class BackupThread extends Thread {
    public void crawlCheckpoint(File checkpointDir) throws Exception {
        super.crawlCheckpoint(checkpointDir);
        logger.fine("Started serializing already seen as part " + "of checkpoint. Can take some time.");
        if (this.pendingUris != null) {
            this.pendingUris.sync();
        }
        CheckpointUtils.writeObjectToFile(this.alreadyIncluded, checkpointDir);
        logger.fine("Finished serializing already seen as part " + "of checkpoint.");
        CheckpointUtils.writeObjectToFile(this, checkpointDir);
    }
}
