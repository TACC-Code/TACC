class BackupThread extends Thread {
    public static void removeChannelListener(int id) {
        channelhandler.getChannel(id).addListener(null);
    }
}
