class BackupThread extends Thread {
    private void expire() {
        try {
            readWriteLock.writeLock().lock();
            if (cacheExpiryPolicy != null) {
                if (!cacheExpiryPolicy.getPolicy().shouldRun(this)) {
                    return;
                }
                List<RevisionKeyList<K>> keysToKill = new ArrayList<RevisionKeyList<K>>();
                for (RevisionKeyList<K> rkl : keysPerRevisionMap.values()) {
                    if (rkl.getRevision() != CURRENT_REVISION.get()) {
                        if (cacheExpiryPolicy.getPolicy().shouldExpire(this, rkl)) {
                            keysToKill.add(rkl);
                        }
                    }
                }
                for (RevisionKeyList<K> rkl : keysToKill) {
                    for (K ck : rkl.getKeys()) {
                        CacheElementRevisions<K, V> revs = revisions.get(ck);
                        revs.removeRevision(rkl.getRevision());
                    }
                    keysPerRevisionMap.remove(rkl.getRevision());
                    cacheExpiryPolicy.getHandler().expired(rkl);
                }
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
