class BackupThread extends Thread {
    public ChannelManager getChannelManager() {
        if (channelManager == null) {
            channelManager = new EthernetTCPChannelManager(UBIQUITOS_ETH_TCP_PORT, UBIQUITOS_ETH_TCP_CONTROL_PORT, UBIQUITOS_ETH_TCP_PASSIVE_PORT_RANGE, cacheController);
        }
        return channelManager;
    }
}
