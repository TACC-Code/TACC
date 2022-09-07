class BackupThread extends Thread {
    void removeEntry(AsteriskQueueEntryImpl entry, Date dateReceived) {
        synchronized (serviceLevelTimerTasks) {
            if (serviceLevelTimerTasks.containsKey(entry)) {
                ServiceLevelTimerTask timerTask = serviceLevelTimerTasks.get(entry);
                timerTask.cancel();
                serviceLevelTimerTasks.remove(entry);
            }
        }
        boolean changed;
        synchronized (entries) {
            changed = entries.remove(entry);
            if (changed) {
                shift();
            }
        }
        if (changed) {
            entry.getChannel().setQueueEntry(null);
            entry.left(dateReceived);
            fireEntryLeave(entry);
        }
    }
}
