class BackupThread extends Thread {
    private String getChannelID(String groupName, String channelName) {
        return groupName + "#" + channelName;
    }
}
