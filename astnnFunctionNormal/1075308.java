class BackupThread extends Thread {
    public static void closeChannelPanel(String channelName) {
        closeChannelPanel(getChannelPanel(channelName));
    }
}
