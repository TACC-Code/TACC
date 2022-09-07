class BackupThread extends Thread {
    public ChannelManager getChannelManager() {
        if (channelManager == null) {
            channelManager = new EthernetRTPChannelManager(UBIQUITOS_ETH_RTP_PASSIVE_PORT_RANGE);
        }
        return channelManager;
    }
}
