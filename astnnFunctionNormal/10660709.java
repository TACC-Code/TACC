class BackupThread extends Thread {
    private void registerClients() throws ClosedChannelException {
        SelectionKey selKey = null;
        for (RegisterableClient client : registerableClients) {
            logger.debug("registering client " + client.getClientId() + " for reading");
            registerableClients.remove(client);
            selKey = client.getChannel().register(selector, SelectionKey.OP_READ);
            selKey.attach(client.getClientId());
            logger.debug("registered client " + client.getClientId() + " for reading");
        }
    }
}
