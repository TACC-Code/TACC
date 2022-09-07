class BackupThread extends Thread {
    public BatchResponseType spmlBatchRequest(BatchRequestType request) {
        try {
            return (BatchResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
