class BackupThread extends Thread {
    void logTimeStat(boolean read, long begin, long end) {
        EventLog.writeEvent(EVENT_DB_OPERATION, mPath, read ? 0 : 1, end - begin);
    }
}
