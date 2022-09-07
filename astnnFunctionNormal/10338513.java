class BackupThread extends Thread {
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        final Tracer tracer = baseTracer.entering("createManagedConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo)");
        String channelId;
        Channel channel;
        boolean supportsLocalTransactions = false;
        try {
            SPIConnectionRequestInfo spiConnectionRequestInfo = (SPIConnectionRequestInfo) connectionRequestInfo;
            channelId = spiConnectionRequestInfo.getChannelId();
            tracer.info("Channel ID: " + channelId);
            channel = (Channel) CPAFactory.getInstance().getLookupManager().getCPAObject(CPAObjectType.CHANNEL, channelId);
            PasswordCredential passwordCredential = Util.getPasswordCredential(this, subject, connectionRequestInfo);
            ;
            String adapterStatus = channel.getValueAsString("adapterStatus");
            tracer.info("Adapter status: " + adapterStatus);
            if (adapterStatus != null && adapterStatus.equals("active")) {
                spiManagedConnection = new SPIManagedConnection(this, passwordCredential, supportsLocalTransactions, channelId, channel);
            } else {
                String errorMessage = "SPI Connection could not be created. Reason: Channel status is set to inactive.";
                ResourceException resourceException = new ResourceException(errorMessage);
                this.finalize();
                throw resourceException;
            }
        } catch (Throwable throwable) {
            tracer.catched(throwable);
            tracer.error(throwable.toString());
            throw new ResourceException(throwable.toString());
        }
        tracer.leaving();
        return spiManagedConnection;
    }
}
