class BackupThread extends Thread {
    public CancelResponseType spmlCancelRequest(CancelRequestType request) {
        try {
            return (CancelResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
