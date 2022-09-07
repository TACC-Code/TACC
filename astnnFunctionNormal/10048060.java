class BackupThread extends Thread {
    public ResetPasswordResponseType spmlResetPasswordRequest(ResetPasswordRequestType request) {
        try {
            return (ResetPasswordResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
