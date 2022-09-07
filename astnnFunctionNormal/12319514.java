class BackupThread extends Thread {
    public void testLocalCall() {
        final RemoteName descriptor = new RemoteName(IChannelBase.class, "testLocalCall");
        m_serverMessenger.registerChannelSubscriber(new ChannelSubscribor(), descriptor);
        final IChannelBase subscribor = (IChannelBase) m_serverMessenger.getChannelBroadcastor(descriptor);
        subscribor.testNoParams();
        subscribor.testPrimitives(1, (short) 0, 1, (byte) 1, true, (float) 1.0);
        subscribor.testString("a");
    }
}
