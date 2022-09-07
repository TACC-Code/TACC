class BackupThread extends Thread {
    public ResponseType spmlUpdatesCloseIteratorRequest(oasis.names.tc.spml._2._0.updates.CloseIteratorRequestType request) {
        try {
            return (ResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
