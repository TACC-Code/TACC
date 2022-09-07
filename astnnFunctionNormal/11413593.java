class BackupThread extends Thread {
    public void start() throws LifecycleException {
        if (this.started) return;
        try {
            cluster.registerManager(this);
            CatalinaCluster catclust = (CatalinaCluster) cluster;
            LazyReplicatedMap map = new LazyReplicatedMap(this, catclust.getChannel(), DEFAULT_REPL_TIMEOUT, getMapName(), getClassLoaders());
            map.setChannelSendOptions(mapSendOptions);
            this.sessions = map;
            super.start();
            this.started = true;
        } catch (Exception x) {
            log.error("Unable to start BackupManager", x);
            throw new LifecycleException("Failed to start BackupManager", x);
        }
    }
}
