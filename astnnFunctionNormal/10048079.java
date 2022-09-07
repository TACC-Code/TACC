class BackupThread extends Thread {
    protected EndpointDescriptor doMakeDestination(RequestType request) throws IdentityMediationException {
        Channel c = psp.getChannel();
        for (IdentityMediationEndpoint ie : c.getEndpoints()) {
            if (ie.getType().equals(serviceType)) {
                if (ie.getBinding().equals(binding)) {
                    return c.getIdentityMediator().resolveEndpoint(c, ie);
                }
            }
        }
        logger.error("Cannot resolve endpoint for " + serviceType + "/" + binding);
        return null;
    }
}
