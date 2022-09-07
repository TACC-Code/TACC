class BackupThread extends Thread {
    public static synchronized void print(LogComponent component, Throwable throwable) {
        int componentOrdinal = component.ordinal;
        LogLevel level = LogLevel.severe;
        if (INSTANCE.HasSyncLog[componentOrdinal]) {
            for (LogWriter writer : component.SyncWriters) {
                Calendar time = Calendar.getInstance();
                INSTANCE.WriterThread.writeLog(writer, level, time, null, throwable);
            }
        }
        if (INSTANCE.HasAsyncLog[componentOrdinal]) {
            LogRow row = new LogRow(level, null, throwable);
            for (LogWriter writer : component.AsyncWriters) {
                writer.LogRows.add(row);
            }
        }
    }
}
