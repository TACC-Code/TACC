class BackupThread extends Thread {
    public void connected(TCPChannel realChannel) throws IOException {
        ChannelsRunnable r = new ChannelsRunnable() {

            public void run() {
                try {
                    cb.connected(channel);
                } catch (Exception e) {
                    log.log(Level.WARNING, channel + "Exception", e);
                }
            }

            public RegisterableChannel getChannel() {
                return channel;
            }
        };
        svc.execute(realChannel, r);
    }
}
