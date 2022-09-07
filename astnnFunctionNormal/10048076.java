class BackupThread extends Thread {
    public ResponseType spmlResumeRequest(ResumeRequestType request) {
        try {
            return (ResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
