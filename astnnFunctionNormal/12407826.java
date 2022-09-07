class BackupThread extends Thread {
    @Override
    public void startCluster() {
        synchronized (this) {
            if (started) {
                throw new IllegalStateException("cluster already started");
            }
            mobicentsCache.startCache();
            final Cache<?, ?> cache = mobicentsCache.getJBossCache();
            if (!cache.getConfiguration().getCacheMode().equals(CacheMode.LOCAL)) {
                currentView = new ArrayList<Address>(cache.getConfiguration().getRuntimeConfig().getChannel().getView().getMembers());
                cache.addCacheListener(this);
                Configuration conf = cache.getConfiguration();
                if (conf.getBuddyReplicationConfig() != null && conf.getBuddyReplicationConfig().isEnabled()) {
                    if (conf.getRuntimeConfig().getBuddyGroup() != null) {
                        Node root = cache.getRoot();
                        root.put(BUDDIES_STORE, conf.getRuntimeConfig().getBuddyGroup().getBuddies());
                    }
                }
            }
            started = true;
        }
    }
}
