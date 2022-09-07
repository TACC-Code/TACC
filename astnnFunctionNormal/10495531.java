class BackupThread extends Thread {
    public void connectFailed(RegisterableChannel realChannel, final Throwable e) {
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    cb.connectFailed(channel, e);
                } catch (Exception e) {
                    log.log(Level.WARNING, channel + "Exception", e);
                }
            }

            public RegisterableChannel getChannel() {
                return channel;
            }
        };
        svc.execute(null, r);
    }
}
