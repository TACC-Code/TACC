class BackupThread extends Thread {
    public EnvironmentImpl getEnvironment(File envHome, EnvironmentConfig config, boolean checkImmutableParams, boolean openIfNeeded, RepConfigProxy repConfigProxy) throws EnvironmentNotFoundException, EnvironmentLockedException {
        String environmentKey = null;
        EnvironmentImpl envImpl = null;
        synchronized (this) {
            environmentKey = getEnvironmentMapKey(envHome);
            envImpl = envs.get(environmentKey);
            if (envImpl != null) {
                if (envImpl.isReplicated() && (repConfigProxy == null) && !config.getReadOnly()) {
                    throw new UnsupportedOperationException("This environment was previously opened for " + "replication. It cannot be re-opened in read/write " + "mode for standalone operation.");
                }
                envImpl.checkIfInvalid();
                if (checkImmutableParams) {
                    envImpl.checkImmutablePropsForEquality(DbInternal.getProps(config));
                }
                envImpl.incReferenceCount();
            } else {
                if (openIfNeeded) {
                    EnvironmentImpl sharedCacheEnv = config.getSharedCache() ? getAnySharedCacheEnv() : null;
                    envImpl = (repConfigProxy == null) ? new EnvironmentImpl(envHome, config, sharedCacheEnv) : loadRepImpl(envHome, config, sharedCacheEnv, repConfigProxy);
                    assert config.getSharedCache() == envImpl.getSharedCache();
                    envImpl.incReferenceCount();
                    envs.put(environmentKey, envImpl);
                }
            }
        }
        if (envImpl != null) {
            boolean success = false;
            try {
                envImpl.finishInit(config);
                synchronized (this) {
                    addToSharedCacheEnvs(envImpl);
                }
                success = true;
            } finally {
                if (!success) {
                    envs.remove(environmentKey);
                }
            }
        }
        return envImpl;
    }
}
