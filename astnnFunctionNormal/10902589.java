class BackupThread extends Thread {
    public int compareTo(Object obj, Object obj1) {
        ChannelListItem channellistitem = (ChannelListItem) obj;
        ChannelListItem channellistitem1 = (ChannelListItem) obj1;
        return channellistitem.getChannelNameLowercased().compareTo(channellistitem1.getChannelNameLowercased());
    }
}
