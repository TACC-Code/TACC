class BackupThread extends Thread {
    public final AuthenticationResult rawAuthenticate(final String inputUserId, final String password) {
        boolean isAuthenticated = false;
        boolean unknown = true;
        try {
            String username;
            XMPPConnection connection;
            String[] parsedUsername = inputUserId.split("@", 2);
            if (parsedUsername.length > 1) {
                String userdomain = parsedUsername[1];
                username = parsedUsername[0];
                ConnectionConfiguration configuration = new ConnectionConfiguration(servername, XMPP_PORT_NUMBER, userdomain);
                connection = new XMPPConnection(configuration);
            } else {
                connection = new XMPPConnection(servername);
                username = inputUserId;
            }
            connection.connect();
            connection.login(username, password);
            isAuthenticated = connection.isAuthenticated();
            unknown = false;
            connection.disconnect();
        } catch (XMPPException e) {
            LOG.error("Authentication caught an exception: " + e);
            String exceptionString = e.toString();
            if (exceptionString.contains("authentication failed")) {
                unknown = false;
                isAuthenticated = false;
            }
        }
        AuthenticationResult result;
        if (unknown) {
            result = AuthenticationResult.UNKNOWN;
        } else if (isAuthenticated) {
            result = AuthenticationResult.AUTHENTICATED;
        } else {
            result = AuthenticationResult.REJECTED;
        }
        return result;
    }
}
