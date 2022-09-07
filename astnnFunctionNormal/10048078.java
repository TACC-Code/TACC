class BackupThread extends Thread {
    public ModifyResponseType spmlModifyRequest(ModifyRequestType request) {
        try {
            return (ModifyResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
