class BackupThread extends Thread {
    protected String getChannelID(final RequestData requestData, final String xiPartyFrom, final String xiPartyTo) throws IOException, CPAException, ModuleExceptionEEDM {
        final Tracer tracer = this.baseTracer.entering("getChannelID(final RequestData requestData, final String xiPartyFrom, final String xiPartyTo)");
        String channelID = null;
        Channel channelName = null;
        InboundRuntimeLookup channelLookup = null;
        channelLookup = new InboundRuntimeLookup(EdifactUtil.ADAPTER_NAME, EdifactUtil.ADAPTER_NAMESPACE, xiPartyFrom, xiPartyTo, requestData.getRequestFromService(), requestData.getRequestToService(), requestData.getRequestInterface(), requestData.getRequestNamespace());
        if (channelLookup.getChannel() != null) {
            tracer.info("Channel lookup successful");
            channelName = channelLookup.getChannel();
            channelID = channelName.getObjectId();
            tracer.info("InboundRuntimeLookup channel retrieved {0}", channelID);
        } else {
            tracer.error("The channel cannot be determined. Reason: No agreement binding for the combination available: " + "AN {0}, ANS {1}, FP {2}, TP {3}, FS {4}, TS {5}, IF {6}, NS {7}", new Object[] { EdifactUtil.ADAPTER_NAME, EdifactUtil.ADAPTER_NAMESPACE, xiPartyFrom, xiPartyTo, requestData.getRequestFromService(), requestData.getRequestToService(), requestData.getRequestInterface(), requestData.getRequestNamespace() });
        }
        tracer.leaving();
        return channelID;
    }
}
