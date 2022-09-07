class BackupThread extends Thread {
    public void testRemoteCall() {
        final RemoteName testRemote = new RemoteName(IChannelBase.class, "testRemote");
        final ChannelSubscribor subscribor1 = new ChannelSubscribor();
        m_serverMessenger.registerChannelSubscriber(subscribor1, testRemote);
        assertHasChannel(testRemote, m_hub);
        final IChannelBase channelTest = (IChannelBase) m_clientMessenger.getChannelBroadcastor(testRemote);
        channelTest.testNoParams();
        assertCallCountIs(subscribor1, 1);
        channelTest.testString("a");
        assertCallCountIs(subscribor1, 2);
        channelTest.testPrimitives(1, (short) 0, 1, (byte) 1, true, (float) 1.0);
        assertCallCountIs(subscribor1, 3);
        channelTest.testArray(null, null, null, null, null, null);
        assertCallCountIs(subscribor1, 4);
    }
}
