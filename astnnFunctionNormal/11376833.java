class BackupThread extends Thread {
    public void deleteChannel(Channel channel) {
        sqlTemplate.update(getSql("deleteNodeChannelSql"), new Object[] { channel.getChannelId() });
        sqlTemplate.update(getSql("deleteChannelSql"), new Object[] { channel.getChannelId() });
        reloadChannels();
    }
}
