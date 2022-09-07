class BackupThread extends Thread {
    private DatagramChannel openDatagramChannel() {
        DatagramChannel dc = null;
        try {
            dc = DatagramChannel.open();
            dc.socket().bind(null);
            dc.configureBlocking(false);
            connBroker.getCommFacade().getChannelManager().registerChannel(dc);
            return dc;
        } catch (IOException e) {
            Logger.logError(e, "Error creating datagram channel!");
            throw new IllegalStateException("Error creating datagram channel!");
        }
    }
}
