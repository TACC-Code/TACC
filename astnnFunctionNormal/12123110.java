class BackupThread extends Thread {
    public Channel[] getPublishedPosition() {
        Channel[] channels = null;
        String channelPath = this.getChannelPath();
        if (channelPath == null) {
            return null;
        }
        try {
            Channel channel = (Channel) Channel.getInstance(channelPath);
            channels = channel.getSelfAndAncestors();
        } catch (Exception e) {
            log.error(" �õ��ĵ�������λ�ó���", e);
        }
        return channels;
    }
}
