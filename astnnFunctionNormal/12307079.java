class BackupThread extends Thread {
    public void channelConnected(Channel channel) {
        final Channel c = channel;
        new Thread() {

            public void run() {
                if (c.getChannelName().equals("SuperPeer")) bucle1(); else if (c.getChannelName().equals("MANET")) bucle();
            }
        }.start();
    }
}
