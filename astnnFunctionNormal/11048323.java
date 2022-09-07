class BackupThread extends Thread {
    public static void removeChannel(int id) {
        channelhandler.removeChannel(channelhandler.getChannel(id));
    }
}
