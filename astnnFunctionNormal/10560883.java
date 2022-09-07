class BackupThread extends Thread {
    @Override
    public boolean authorise(Request request, Method method, Auth auth) {
        LOG.info(method.toString() + " " + resourceXmldbUri + " (write=" + method.isWrite + ")");
        if (auth == null) {
            if (LOG.isDebugEnabled()) LOG.debug("User hasn't been authenticated.");
            return false;
        }
        String userName = auth.getUser();
        Object tag = auth.getTag();
        String authURI = auth.getUri();
        if (tag == null) {
            if (LOG.isDebugEnabled()) LOG.debug("No tag, user " + userName + " not authenticated");
            return false;
        } else if (tag instanceof String) {
            String value = (String) tag;
            if (AUTHENTICATED.equals(value)) {
            } else {
                if (LOG.isDebugEnabled()) LOG.debug("Authentication tag contains wrong value, user " + userName + " is not authenticated");
                return false;
            }
        }
        if (method.isWrite) {
            if (!existResource.writeAllowed) {
                if (LOG.isDebugEnabled()) LOG.debug("User " + userName + " is NOT authorized to write resource, abort.");
                return false;
            }
        } else {
            if (!existResource.readAllowed) {
                if (LOG.isDebugEnabled()) LOG.debug("User " + userName + " is NOT authorized to read resource, abort.");
                return false;
            }
        }
        if (auth.getUri() == null) {
            if (LOG.isTraceEnabled()) LOG.trace("URI is null");
        }
        String action = method.isWrite ? "write" : "read";
        if (LOG.isDebugEnabled()) LOG.debug("User " + userName + " is authorized to " + action + " resource " + resourceXmldbUri.toString());
        return true;
    }
}
