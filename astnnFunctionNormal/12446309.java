class BackupThread extends Thread {
    protected List<ChannelInfo> getChannelData() {
        if (channels == null) {
            channels = new BasicEventList<ChannelInfo>();
            Transaction ta = taFactory.createTransaction();
            ta.add(new TORead<ChannelInfo>(channels, ChannelInfo.class));
            ta.execute();
        }
        return channels;
    }
}
