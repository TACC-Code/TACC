class BackupThread extends Thread {
            public void run() {
                synchronized (LimeDaapServerNIO.this) {
                    for (DaapConnectionNIO connection : getDaapConnections()) {
                        for (int i = 0; i < libraryQueue.size(); i++) connection.enqueueLibrary(libraryQueue.get(i));
                        SelectableChannel channel = connection.getChannel();
                        try {
                            connection.update();
                            DaapController controller = allConnections.get(connection);
                            controller.setOps();
                        } catch (IOException ignored) {
                            try {
                                channel.close();
                            } catch (IOException ignoredToo) {
                            }
                        }
                    }
                    libraryQueue.clear();
                }
            }
}
