class BackupThread extends Thread {
    public void initialize(Properties props) {
        logger.info("Test Server initialized");
        Channel clientLocChannel = AppContext.getChannelManager().createChannel(PLAYER_LOCATIONS_CHANNEL, this, Delivery.RELIABLE);
        commChannelRef = AppContext.getDataManager().createReference(clientLocChannel);
    }
}
