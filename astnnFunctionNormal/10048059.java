class BackupThread extends Thread {
    public ResponseType spmlSetPasswordRequest(SetPasswordRequestType request) {
        try {
            return (ResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
