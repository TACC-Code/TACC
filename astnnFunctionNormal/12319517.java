class BackupThread extends Thread {
    public void testMultipleChannels() {
        final RemoteName testRemote2 = new RemoteName(IChannelBase.class, "testRemote2");
        final RemoteName testRemote3 = new RemoteName(IChannelBase.class, "testRemote3");
        final ChannelSubscribor subscribor2 = new ChannelSubscribor();
        m_clientMessenger.registerChannelSubscriber(subscribor2, testRemote2);
        final ChannelSubscribor subscribor3 = new ChannelSubscribor();
        m_clientMessenger.registerChannelSubscriber(subscribor3, testRemote3);
        assertHasChannel(testRemote2, m_hub);
        assertHasChannel(testRemote3, m_hub);
        final IChannelBase channelTest2 = (IChannelBase) m_serverMessenger.getChannelBroadcastor(testRemote2);
        channelTest2.testNoParams();
        assertCallCountIs(subscribor2, 1);
        final IChannelBase channelTest3 = (IChannelBase) m_serverMessenger.getChannelBroadcastor(testRemote3);
        channelTest3.testNoParams();
        assertCallCountIs(subscribor3, 1);
    }
}
