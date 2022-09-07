class BackupThread extends Thread {
    public void commit(RevisionObjectCache<K, V> cache) {
        try {
            readWriteLock.writeLock().lock();
            long revision = CURRENT_REVISION.incrementAndGet();
            List<K> keysForRevision = keysPerRevisionMap.get(revision - 1).getKeys();
            List<K> cache_keys = new ArrayList<K>(keysForRevision.size() + cache.getAddedElements().size());
            cache_keys.addAll(keysForRevision);
            for (K key : cache.getAddedElements()) {
                assert !revisions.containsKey(key);
                CacheElementRevisions<K, V> revs = new CacheElementRevisions<K, V>(this, key);
                V added = cache.getElement(key);
                if (added == null) {
                    throw new ObjectCacheException("Added CacheElement is null.");
                }
                V addedClone = factory.createClone(added);
                revs.addElement(revision, key, addedClone);
                cache_keys.add(key);
                revisions.put(key, revs);
            }
            for (K key : cache.getModifiedElements()) {
                V element = cache.getElement(key);
                CacheElementRevisions<K, V> revs = revisions.get(key);
                assert revs != null;
                V merged = factory.createClone(factory.merge(revs.getLeading(), element));
                revs.addModification(revision, key, merged);
            }
            for (K key : cache.getRemovedElements()) {
                CacheElementRevisions<K, V> revs = revisions.get(key);
                assert revs != null;
                revs.removeElement(revision, key);
                cache_keys.remove(key);
                revisions.put(key, revs);
            }
            keysPerRevisionMap.put(revision, new RevisionKeyList<K>(revision, cache_keys));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
