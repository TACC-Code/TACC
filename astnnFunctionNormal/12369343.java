class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    private void prepareForStart() {
        mocks.checking(new Expectations() {

            {
                allowing(sysEventSource).toIdentityString();
                will(returnValue("sysEventSource"));
                allowing(state).getContext();
                will(returnValue(context));
                allowing(state).getEventHandler();
                will(returnValue(listener));
                allowing(state).getFrameReader();
                will(returnValue(frameReader));
                allowing(state).getFrameWriter();
                will(returnValue(frameWriter));
                allowing(state).getSubscriptionManager();
                will(returnValue(subscriptionManager));
                allowing(state).getRemoteSubscriptions();
                will(returnValue(remoteSubscriptions));
                allowing(state).getDiscoveredContexts();
                will(returnValue(discoveredConnections));
                allowing(state).getConnectedContexts();
                will(returnValue(connectedContexts));
                allowing(state).getChannels();
                will(returnValue(channels));
                allowing(state).getChannelFactory();
                will(returnValue(factory));
                allowing(state).setChannels(with(a(Map.class)));
                allowing(state).getCONNECTINGContexts();
                will(returnValue(CONNECTINGContexts));
            }
        });
    }
}
