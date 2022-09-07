class BackupThread extends Thread {
    @PostConstruct
    public void init() {
        logger.info("Initializing CacheUpdater ...");
        lChannel = this.getCache().getChannelList();
    }
}
