class BackupThread extends Thread {
    @Test
    public void getChannelId() {
        Dimmer dimmer = new Dimmer(0, "Dimmer 1");
        Channel channel = new Channel(4, "Channel 5");
        assertEquals(dimmer.getChannelId(), -1);
        dimmer.setChannel(channel);
        assertEquals(dimmer.getChannelId(), 4);
        dimmer.setChannel(null);
        assertEquals(dimmer.getChannelId(), -1);
    }
}
