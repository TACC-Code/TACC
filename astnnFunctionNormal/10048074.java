class BackupThread extends Thread {
    public StatusResponseType spmlStatusRequest(StatusRequestType request) {
        try {
            return (StatusResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
