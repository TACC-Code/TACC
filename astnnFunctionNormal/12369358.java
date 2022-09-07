class BackupThread extends Thread {
    @Test
    public void testChannelSubscriptionEventHandler() {
        mocks.checking(new Expectations() {

            {
                one(context).getSystemEventSource(RemoteContainerSubscriptionEvent.class);
                will(returnValue(sysEventSource));
            }
        });
        final String idRegex = "id-regex";
        DualValue<ISubscriptionParameters, IEventListener> values = new DualValue<ISubscriptionParameters, IEventListener>(new SubscriptionParameters(idRegex, TYPE, DOMAIN), listener);
        RemoteContainerSubscriptionEvent event = new RemoteContainerSubscriptionEvent(context, ID, values);
        RemoteContainerSubscriptionEventHandler candidate = new RemoteContainerSubscriptionEventHandler(state);
        candidate.getState().getChannels().put(ID, channel);
        mocks.checking(new Expectations() {

            {
                one(channel).subscribe(new SubscriptionParameters(idRegex, TYPE, DOMAIN));
                one(channel).addListener(new SubscriptionParameters(idRegex, TYPE, DOMAIN), listener);
            }
        });
        candidate.update(event);
    }
}
