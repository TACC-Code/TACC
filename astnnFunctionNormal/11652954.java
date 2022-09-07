class BackupThread extends Thread {
    public CacheKey putInIdentityMap(Object domainObject, Vector keys, Object writeLockValue, long readTime, ClassDescriptor descriptor) {
        ObjectBuilder builder = descriptor.getObjectBuilder();
        Object implementation = builder.unwrapObject(domainObject, getSession());
        IdentityMap map = getIdentityMap(descriptor);
        CacheKey cacheKey;
        if (isCacheAccessPreCheckRequired()) {
            getSession().startOperationProfile(SessionProfiler.CACHE);
            acquireReadLock();
            try {
                cacheKey = map.put(keys, implementation, writeLockValue, readTime);
            } finally {
                releaseReadLock();
            }
            getSession().endOperationProfile(SessionProfiler.CACHE);
        } else {
            cacheKey = map.put(keys, implementation, writeLockValue, readTime);
        }
        return cacheKey;
    }
}
