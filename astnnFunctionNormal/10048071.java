class BackupThread extends Thread {
    public LookupResponseType spmlLookupRequest(LookupRequestType request) {
        try {
            return (LookupResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
