class BackupThread extends Thread {
    ChannelStream createSource() throws IOException {
        return info.srcProtocol.getChannelStream(this);
    }
}
