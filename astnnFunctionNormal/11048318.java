class BackupThread extends Thread {
    public static Channel addChannel(String name) {
        return channelhandler.getChannel(name);
    }
}
