class BackupThread extends Thread {
    public ListTargetsResponseType spmlListTargetsRequest(ListTargetsRequestType request) {
        try {
            return (ListTargetsResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
