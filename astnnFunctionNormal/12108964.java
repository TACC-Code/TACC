class BackupThread extends Thread {
    private void handleChannelInit(ChannelInit event) {
        this.channel = event.getChannel();
        if (!initialNode && localHost != null) localHost = new InetSocketAddress(localHost.getAddress(), RegisterSocketEvent.FIRST_AVAILABLE);
        try {
            event.go();
            RegisterSocketEvent rse = new RegisterSocketEvent(this.channel, Direction.DOWN, this);
            rse.localHost = (localHost != null) ? localHost.getAddress() : null;
            rse.port = (localHost != null) ? localHost.getPort() : RegisterSocketEvent.FIRST_AVAILABLE;
            rse.init();
            rse.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }
}
