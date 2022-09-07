class BackupThread extends Thread {
    public void verifyIsStartedInChannelMonitoring() throws ModuleExceptionEEDM {
        final Tracer tracer = baseTracer.entering("verifyIsActivatedInChannelMonitoring()");
        ChannelActivationStatus channelActivationStatus = null;
        try {
            channelActivationStatus = AdminManagerImpl.getInstance().getChannelActivationStatus(this.getId());
        } catch (Exception e) {
            tracer.error("Exception occured during channel activation status determination");
            throw new ModuleExceptionEEDM(HTTPUtil.INACTIVE_ADAPTER_ERROR, ModuleExceptionEEDM.UNSPECIFIED_ERROR);
        }
        if (channelActivationStatus.getActivationState() == ActivationState.STOPPED) {
            tracer.error("Channel has been stopped. Message may not be processed.");
            throw new ModuleExceptionEEDM(HTTPUtil.INACTIVE_ADAPTER_ERROR, ModuleExceptionEEDM.UNSPECIFIED_ERROR);
        }
        tracer.info("Channel activation status: {0}", channelActivationStatus);
        tracer.leaving();
    }
}
