class BackupThread extends Thread {
    public static void removeChannel(String name) {
        channelhandler.removeChannel(channelhandler.getChannel(name));
    }
}
