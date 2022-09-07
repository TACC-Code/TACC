class BackupThread extends Thread {
    void setSourceQueueRight(ManagedBlockingQueue srcQueueRight, ETLWorker worker) throws KETLThreadException {
        this.getUsedPortsFromWorker(worker, ETLWorker.getChannel(this.getXMLConfig(), ETLWorker.RIGHT), ETLWorker.RIGHT, "pRightInputRecords");
        this.srcQueueRight = srcQueueRight;
        try {
            this.mRightExpectedInputDataTypes = worker.getOutputRecordDatatypes(ETLWorker.getChannel(this.getXMLConfig(), ETLWorker.RIGHT));
            this.mRightInputRecordWidth = this.mRightExpectedInputDataTypes.length;
        } catch (ClassNotFoundException e) {
            throw new KETLThreadException(e, this);
        }
    }
}
