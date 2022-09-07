class BackupThread extends Thread {
            public void run() {
                Channel channel = connection.getChannel();
                try {
                    channel.close();
                } catch (IOException ignored) {
                }
            }
}
