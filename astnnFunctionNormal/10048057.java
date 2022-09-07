class BackupThread extends Thread {
    public ActiveResponseType spmlActiveRequest(ActiveRequestType request) {
        try {
            return (ActiveResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
