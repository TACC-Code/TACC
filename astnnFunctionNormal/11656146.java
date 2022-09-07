class BackupThread extends Thread {
    void setSourceQueueLeft(ManagedBlockingQueue srcQueueLeft, ETLWorker worker) throws KETLThreadException {
        this.getUsedPortsFromWorker(worker, ETLWorker.getChannel(this.getXMLConfig(), ETLWorker.LEFT), ETLWorker.LEFT, "pLeftInputRecords");
        this.srcQueueLeft = srcQueueLeft;
        try {
            this.mLeftExpectedInputDataTypes = worker.getOutputRecordDatatypes(ETLWorker.getChannel(this.getXMLConfig(), ETLWorker.LEFT));
            this.mLeftInputRecordWidth = this.mLeftExpectedInputDataTypes.length;
        } catch (ClassNotFoundException e) {
            throw new KETLThreadException(e, this);
        }
    }
}
