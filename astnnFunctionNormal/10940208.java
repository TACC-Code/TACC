class BackupThread extends Thread {
    protected void onChannelData(SshMsgChannelData msg) throws java.io.IOException {
        synchronized (messages) {
            if (boundChannel != null) {
                if (boundChannel.isOpen()) {
                    boundChannel.sendChannelData(msg.getChannelData());
                } else {
                    messages.add(msg);
                }
            }
        }
    }
}
