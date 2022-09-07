class BackupThread extends Thread {
    private void roll() {
        if (!isCurrent()) {
            try {
                readWriteLock.writeLock().acquire();
                if (!isCurrent()) {
                    currentStatistics.setStopDate(nextRollDate.getTime());
                    completeStatistics = currentStatistics;
                    currentStatistics = new Statistics(nextRollDate.getTime());
                    nextRollDate.add(units, period);
                    compositeStatisticsListener.statistics(alias, completeStatistics);
                }
            } catch (Throwable e) {
                LOG.error("Unable to roll statistics log", e);
            } finally {
                readWriteLock.writeLock().release();
            }
        }
    }
}
