class BackupThread extends Thread {
    public static Channel getMetaChannel() {
        return channelhandler.getChannel(0);
    }
}
