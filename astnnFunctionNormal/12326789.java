class BackupThread extends Thread {
    public void doInitializeMetric(TimeSeriesContext rootContext, String path) {
        Counter c = rootContext.createCounterSeries(path, "Count of errors encountered during series file reads or writes", MEAN_COUNT_OVER(Time.seconds(1), captureTime), LATEST(captureTime));
        RoundRobinSerializer.setFileErrorCounter(c);
    }
}
