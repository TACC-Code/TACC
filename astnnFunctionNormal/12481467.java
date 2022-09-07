class BackupThread extends Thread {
    public static synchronized void print(LogComponent component, LogLevel level, String message) {
        int componentOrdinal = component.ordinal;
        if (INSTANCE.HasSyncLog[componentOrdinal]) {
            for (LogWriter writer : component.SyncWriters) {
                Calendar time = Calendar.getInstance();
                INSTANCE.WriterThread.writeLog(writer, level, time, message, null);
            }
        }
        if (INSTANCE.HasAsyncLog[componentOrdinal]) {
            LogRow row = new LogRow(level, message, null);
            for (LogWriter writer : component.AsyncWriters) {
                writer.LogRows.add(row);
            }
        }
    }
}
