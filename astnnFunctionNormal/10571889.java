class BackupThread extends Thread {
    public void testGetChannelIdsNull() {
        List channelIds = broker.getChannelIds();
        Assert.assertNull(channelIds);
    }
}
