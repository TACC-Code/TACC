class BackupThread extends Thread {
    public ChannelInfo[] getChannels() {
        ChannelInfo[] ans = new ChannelInfo[_channels.size()];
        for (int i = 0; i < _channels.size(); i++) ans[i] = (ChannelInfo) _channels.elementAt(i);
        return ans;
    }
}
