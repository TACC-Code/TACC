class BackupThread extends Thread {
    public AddResponseType spmlAddRequest(AddRequestType request) {
        try {
            return (AddResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
