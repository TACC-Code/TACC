class BackupThread extends Thread {
    public void disconnectAll() {
        NIODispatcher.instance().getScheduledExecutorService().execute(new Runnable() {

            public void run() {
                for (DaapConnectionNIO next : allConnections.keySet()) {
                    Channel channel = next.getChannel();
                    try {
                        channel.close();
                    } catch (IOException ignored) {
                    }
                    synchronized (LimeDaapServerNIO.this) {
                        libraryQueue.clear();
                    }
                }
            }
        });
    }
}
