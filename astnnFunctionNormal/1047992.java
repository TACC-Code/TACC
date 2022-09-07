class BackupThread extends Thread {
    public TrafficShapingFilter(ScheduledExecutorService scheduledExecutor, MessageSizeEstimator messageSizeEstimator, int maxReadThroughput, int maxWriteThroughput) {
        log.debug("ctor - executor: {} estimator: {} max read: {} max write: {}", new Object[] { scheduledExecutor, messageSizeEstimator, maxReadThroughput, maxWriteThroughput });
        if (scheduledExecutor == null) {
            scheduledExecutor = new ScheduledThreadPoolExecutor(poolSize);
        }
        if (messageSizeEstimator == null) {
            messageSizeEstimator = new DefaultMessageSizeEstimator() {

                @Override
                public int estimateSize(Object message) {
                    if (message instanceof IoBuffer) {
                        return ((IoBuffer) message).remaining();
                    }
                    return super.estimateSize(message);
                }
            };
        }
        this.scheduledExecutor = scheduledExecutor;
        this.messageSizeEstimator = messageSizeEstimator;
        setMaxReadThroughput(maxReadThroughput);
        setMaxWriteThroughput(maxWriteThroughput);
    }
}
