class BackupThread extends Thread {
    private void sendOutstandingMessages() throws IOException {
        if (boundChannel == null) {
            return;
        }
        synchronized (messages) {
            Iterator it = messages.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof SshMsgChannelData) {
                    boundChannel.sendChannelData(((SshMsgChannelData) obj).getChannelData());
                } else if (obj instanceof SshMsgChannelExtendedData) {
                    boundChannel.sendChannelExtData(((SshMsgChannelExtendedData) obj).getDataTypeCode(), ((SshMsgChannelExtendedData) obj).getChannelData());
                } else {
                    throw new IOException("[" + getName() + "] Invalid message type in pre bound message list!");
                }
            }
            messages.clear();
        }
    }
}
