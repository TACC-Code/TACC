class BackupThread extends Thread {
    public XiChannel lookupChannel(final RequestData requestData, final String fromAS2Party, final String toAS2Party) throws ModuleExceptionEEDM, IOException, CPAException {
        final Tracer tracer = this.baseTracer.entering("lookupChannel(final RequestData requestData, final String fromAS2Party, final String toAS2Party)");
        final String channelId = getChannelID(requestData, fromAS2Party, toAS2Party);
        final LookupManager lookupManager = LookupManager.getInstance();
        tracer.info("LookupManager lookupManager created", lookupManager);
        final Channel channel = (Channel) lookupManager.getCPAObject(CPAObjectType.CHANNEL, channelId);
        tracer.info("Channel channel created", channel);
        tracer.leaving();
        return new XiChannelSapImpl(channelId, channel, this.moduleProcessor);
    }
}
