class BackupThread extends Thread {
    void shutdown(Exception shutdownException) {
        boolean changed = shutdown.compareAndSet(false, true);
        if (!changed) {
            return;
        }
        feederManager.removeFeeder(this);
        StatGroup pstats = (inputThread.protocol != null) ? inputThread.protocol.getStats(StatsConfig.DEFAULT) : new StatGroup(BinaryProtocolStatDefinition.GROUP_NAME, BinaryProtocolStatDefinition.GROUP_DESC);
        if (outputThread.protocol != null) {
            pstats.addAll(outputThread.protocol.getStats(StatsConfig.DEFAULT));
        }
        feederManager.incStats(pstats);
        LoggerUtils.info(logger, repImpl, "Shutting down feeder for replica " + replicaNameIdPair.getName() + ((shutdownException == null) ? "" : (" Reason: " + shutdownException.getMessage())) + RepUtils.writeTimesString(pstats));
        if (repNode.getReplicaCloseCatchupMs() >= 0) {
            try {
                inputThread.join();
            } catch (InterruptedException e) {
                LoggerUtils.warning(logger, repImpl, "Interrupted while waiting to join " + "thread:" + outputThread);
            }
        }
        outputThread.shutdownThread(logger);
        inputThread.shutdownThread(logger);
        LoggerUtils.finest(logger, repImpl, feederReplicaChannel + " isOpen=" + feederReplicaChannel.getChannel().isOpen());
    }
}
