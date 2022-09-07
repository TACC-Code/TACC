class BackupThread extends Thread {
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
}
