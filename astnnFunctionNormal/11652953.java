class BackupThread extends Thread {
    public void printLocks(Class theClass) {
        ClassDescriptor descriptor = getSession().getDescriptor(theClass);
        StringWriter writer = new StringWriter();
        HashMap threadCollection = new HashMap();
        writer.write(TraceLocalization.buildMessage("lock_writer_header", (Object[]) null) + Helper.cr());
        IdentityMap identityMap = getIdentityMap(descriptor);
        identityMap.collectLocks(threadCollection);
        Object[] parameters = new Object[1];
        for (Iterator threads = threadCollection.keySet().iterator(); threads.hasNext(); ) {
            Thread activeThread = (Thread) threads.next();
            parameters[0] = activeThread.getName();
            writer.write(TraceLocalization.buildMessage("active_thread", parameters) + Helper.cr());
            for (Iterator cacheKeys = ((HashSet) threadCollection.get(activeThread)).iterator(); cacheKeys.hasNext(); ) {
                CacheKey cacheKey = (CacheKey) cacheKeys.next();
                parameters[0] = cacheKey.getObject();
                writer.write(TraceLocalization.buildMessage("locked_object", parameters) + Helper.cr());
                parameters[0] = new Integer(cacheKey.getMutex().getDepth());
                writer.write(TraceLocalization.buildMessage("depth", parameters) + Helper.cr());
            }
            DeferredLockManager deferredLockManager = ConcurrencyManager.getDeferredLockManager(activeThread);
            if (deferredLockManager != null) {
                for (Iterator deferredLocks = deferredLockManager.getDeferredLocks().iterator(); deferredLocks.hasNext(); ) {
                    ConcurrencyManager lock = (ConcurrencyManager) deferredLocks.next();
                    parameters[0] = lock.getOwnerCacheKey().getObject();
                    writer.write(TraceLocalization.buildMessage("deferred_locks", parameters) + Helper.cr());
                }
            }
        }
        writer.write(Helper.cr() + TraceLocalization.buildMessage("lock_writer_footer", (Object[]) null) + Helper.cr());
        getSession().log(SessionLog.FINEST, SessionLog.CACHE, writer.toString(), null, null, false);
    }
}
