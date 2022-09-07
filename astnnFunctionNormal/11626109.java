class BackupThread extends Thread {
    AsteriskQueueEntryImpl getEntry(String channelName) {
        synchronized (entries) {
            for (AsteriskQueueEntryImpl entry : entries) {
                if (entry.getChannel().getName().equals(channelName)) {
                    return entry;
                }
            }
        }
        return null;
    }
}
