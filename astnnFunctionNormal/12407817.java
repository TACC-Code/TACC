class BackupThread extends Thread {
    private List<Address> getElectionView(Address deadMember) {
        final Cache jbossCache = mobicentsCache.getJBossCache();
        final Configuration config = jbossCache.getConfiguration();
        final boolean isBuddyReplicationEnabled = config.getBuddyReplicationConfig() != null && config.getBuddyReplicationConfig().isEnabled();
        if (isBuddyReplicationEnabled) {
            boolean createdTx = false;
            boolean doRollback = true;
            try {
                if (txMgr != null && txMgr.getTransaction() == null) {
                    txMgr.begin();
                    createdTx = true;
                }
                String fqnBackupRoot = getBuddyBackupFqn(deadMember);
                Node backupRoot = jbossCache.getNode(fqnBackupRoot);
                if (backupRoot != null) {
                    List<Address> buddies = (List<Address>) backupRoot.get(BUDDIES_STORE);
                    if (buddies == null) {
                        buddies = new ArrayList<Address>();
                        buddies.add(config.getRuntimeConfig().getChannel().getLocalAddress());
                    }
                    return buddies;
                } else {
                    return null;
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (createdTx) {
                    try {
                        if (!doRollback) {
                            txMgr.commit();
                        } else {
                            txMgr.rollback();
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            return null;
        } else {
            return currentView;
        }
    }
}
