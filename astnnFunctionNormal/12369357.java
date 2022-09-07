class BackupThread extends Thread {
    @Test
    public void testConnectionDiscoveredEventHandler_SecondDiscoveryAndConnectionParametersChange() {
        mocks.checking(new Expectations() {

            {
                one(context).isActive();
                will(returnValue(true));
                one(context).getSystemEventSource(ContextDiscoveredEvent.class);
                will(returnValue(sysEventSource));
            }
        });
        final IConnectionParameters params1 = mocks.mock(IConnectionParameters.class);
        ContextDiscoveredEvent event = new ContextDiscoveredEvent(context, connectionParams);
        ContextDiscoveredEventHandler candidate = new ContextDiscoveredEventHandler(state);
        mocks.checking(new Expectations() {

            {
                one(channel).getConnection();
                will(returnValue(connection));
                one(connection).isOutbound();
                will(returnValue(true));
                one(channel).getConnection();
                will(returnValue(connection));
                one(connectionParams).isEqual(connection);
                will(returnValue(false));
                allowing(connectionParams).getRemoteContextIdentity();
                will(returnValue(ID));
                one(channel).getConnection();
                will(returnValue(connection));
                one(connection).destroy();
            }
        });
        candidate.getState().getDiscoveredContexts().put(ID, params1);
        candidate.getState().getChannels().put(ID, channel);
        candidate.update(event);
    }
}
