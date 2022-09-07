class BackupThread extends Thread {
    protected void onChannelExtData(SshMsgChannelExtendedData msg) throws java.io.IOException {
        synchronized (messages) {
            if (boundChannel != null) {
                if (boundChannel.isOpen()) {
                    boundChannel.sendChannelExtData(msg.getDataTypeCode(), msg.getChannelData());
                } else {
                    messages.add(msg);
                }
            }
        }
    }
}
