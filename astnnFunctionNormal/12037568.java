class BackupThread extends Thread {
    private void login(final String server, final String id, final String password) throws XMPPException {
        connection = new XMPPConnection(server);
        try {
            connection.connect();
            connection.login(id, password);
            final Roster roster = connection.getRoster();
            roster.addRosterListener(this);
            String userJabberId = agent.getUser().getJabberId();
            checkUserSubscription(roster, userJabberId);
            presenceChanged(roster.getPresence(userJabberId));
            chat = connection.getChatManager().createChat(userJabberId, new ChatMessageListener());
        } catch (XMPPException e) {
            if (connection.isConnected()) {
                connection.disconnect();
            }
            connection = null;
            throw e;
        }
    }
}
