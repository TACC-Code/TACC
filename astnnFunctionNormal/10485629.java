class BackupThread extends Thread {
    protected Entry write(Entry entryToWrite, Entry expectedReturn, String failureMessage, boolean tsAdapterThreadNeeded) throws Exception {
        if (tsAdapterThreadNeeded) {
            startTSAdapterHelperThread();
        }
        getSpace().write(entryToWrite, null, Lease.FOREVER);
        Entry entry = getSpace().take(expectedReturn, null, DEFAULT_TIMEOUT);
        if (failureMessage == null) {
            assertNotNull("Couldn't take " + expectedReturn.getClass().getSimpleName(), entry);
        } else {
            assertNotNull(failureMessage, entry);
        }
        return entry;
    }
}
