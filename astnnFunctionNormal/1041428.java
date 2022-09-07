class BackupThread extends Thread {
    public ServerSessionHandler(ConnectorSrvIFX server, int idleTime, int bufferSize, MessageIDInfo messageIDInfo) {
        this.server = server;
        this.channelName = server.getChannelName();
        this.messageIDInfo = messageIDInfo;
        this.numClient = 0;
        this.idleTime = idleTime;
        this.bufferSize = bufferSize;
        this.table = new TimeoutHashMap<MessageID, IoSession>(server.getTimeout());
    }
}
