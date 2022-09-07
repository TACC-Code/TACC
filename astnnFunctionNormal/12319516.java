class BackupThread extends Thread {
    public void testMultipleClients() throws Exception {
        final RemoteName test = new RemoteName(IChannelBase.class, "test");
        final ChannelSubscribor client1Subscribor = new ChannelSubscribor();
        m_clientMessenger.registerChannelSubscriber(client1Subscribor, test);
        assertHasChannel(test, m_hub);
        assertEquals(1, m_clientMessenger.getUnifiedMessenger().getLocalEndPointCount(test));
        final String mac = MacFinder.GetHashedMacAddress();
        final ClientMessenger clientMessenger2 = new ClientMessenger("localhost", SERVER_PORT, "client2", mac);
        final ChannelMessenger client2 = new ChannelMessenger(new UnifiedMessenger(clientMessenger2));
        ((IChannelBase) client2.getChannelBroadcastor(test)).testString("a");
        assertCallCountIs(client1Subscribor, 1);
    }
}
