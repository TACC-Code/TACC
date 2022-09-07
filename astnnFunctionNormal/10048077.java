class BackupThread extends Thread {
    public ValidatePasswordResponseType spmlValidatePasswordRequest(ValidatePasswordRequestType request) {
        try {
            return (ValidatePasswordResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
