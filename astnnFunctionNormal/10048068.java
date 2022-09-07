class BackupThread extends Thread {
    public SearchResponseType spmlSearchIterateRequest(oasis.names.tc.spml._2._0.search.IterateRequestType request) {
        try {
            return (SearchResponseType) mediator.sendMessage(request, doMakeDestination(request), psp.getChannel());
        } catch (IdentityMediationException e) {
            throw new RuntimeException(e);
        }
    }
}
