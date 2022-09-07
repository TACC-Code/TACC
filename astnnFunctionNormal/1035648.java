class BackupThread extends Thread {
    private synchronized void setup() {
        Channel channel = ChannelFactory.defaultFactory().getChannel(ch);
        try {
            if (!channel.isConnected()) {
                channel.requestConnection();
            }
            channel.addMonitorValTime(this, Monitor.VALUE);
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (MonitorException e) {
            e.printStackTrace();
        }
    }
}
