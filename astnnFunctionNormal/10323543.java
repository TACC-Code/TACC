class BackupThread extends Thread {
    @Override
    public void start() {
        try {
            SASLAuthentication.supportSASLMechanism("PLAIN", 0);
            final ConnectionConfiguration configuration = new ConnectionConfiguration(getHost(), getPort());
            this.connection = new XMPPConnection(configuration);
            fireTrace("Trying to connect.");
            this.connection.connect();
            fireTrace("Connected, trying to authorize.");
            if (getResource() != null) this.connection.login(getNode(), getPassword(), getResource()); else this.connection.login(getNode(), getPassword());
            fireTrace("Authorized successfully.");
            this.connection.addPacketListener(this, null);
            fireStarted();
        } catch (final XMPPException exception) {
            fireError(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
