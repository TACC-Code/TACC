class BackupThread extends Thread {
    public void endTraceFileMonitoring_VOLT(Object jobID, Object host) {
        System.out.println(this.getTimeString() + " TF - TraceFileMonitor.endTraceFileMonitoring(" + jobID + "," + host + ") ~ START");
        host = this.getCleanHostname("" + host);
        TraceFileId tfId = new TraceFileId(host, jobID);
        this.tfStore.deleteTraceFile(tfId);
        JobToMetricMappingStore mappingStore = JobToMetricMappingStore.getInstance();
        MetricNChannelPair mNc = mappingStore.get(host, jobID);
        if (mNc == null) {
            System.out.println("TF - TraceFileMonitor.endTraceFileMonitoring(" + jobID + "," + host + ") ~ mNc == null.");
        } else {
            System.out.println("TF - TraceFileMonitor.endTraceFileMonitoring(" + jobID + "," + host + ") ~ here should timeout be impl.");
            this.monitorConsumerStop(host, mNc.getMid(), mNc.getChannel());
        }
        mappingStore.removeMapping(host, jobID);
        System.out.println(this.getTimeString() + " TF - TraceFileMonitor.endTraceFileMonitoring(" + jobID + "," + host + ") ~ END");
        return;
    }
}
