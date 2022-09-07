class BackupThread extends Thread {
    public IncomingBatch(Batch batch) {
        this.batchId = batch.getBatchId();
        this.nodeId = batch.getNodeId();
        this.channelId = batch.getChannelId();
        this.status = Status.LD;
    }
}
