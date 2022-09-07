class BackupThread extends Thread {
    public final void setChannelValue(String channelName, Object value) {
        getChannel(channelName).setValue(value);
    }
}
