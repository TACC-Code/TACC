class BackupThread extends Thread {
    public UpdatesResponseType spmlUpdatesIterateRequest(IterateRequestType request) {
        try {
            return (UpdatesResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
