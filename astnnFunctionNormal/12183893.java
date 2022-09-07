class BackupThread extends Thread {
    public String getChannelMode() {
        String str = null;
        if ((channelMode >= 0) && (channelMode < channelLabels.length)) {
            str = channelLabels[channelMode];
        }
        return str;
    }
}
