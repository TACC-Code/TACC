class BackupThread extends Thread {
    protected void cancelConnection(final DaapConnectionNIO connection) {
        NIODispatcher.instance().getScheduledExecutorService().execute(new Runnable() {

            public void run() {
                Channel channel = connection.getChannel();
                try {
                    channel.close();
                } catch (IOException ignored) {
                }
            }
        });
    }
}
