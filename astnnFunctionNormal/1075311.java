class BackupThread extends Thread {
    public static ChannelPanel getChannelPanel(String channelName) {
        int index = tabHolder.indexOfTab(channelName);
        if (index != -1) {
            return (ChannelPanel) tabHolder.getComponentAt(index);
        } else {
            return null;
        }
    }
}
