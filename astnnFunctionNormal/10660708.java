class BackupThread extends Thread {
    private void unregisterClients() throws IOException {
        for (RegisterableClient client : unregisterableClients) {
            logger.debug("unregistering client " + client.getClientId() + " for reading");
            unregisterableClients.remove(client);
            client.getChannel().keyFor(selector).cancel();
            client.getChannel().close();
            logger.debug("unregistered client " + client.getClientId() + " for reading");
            selector.selectNow();
        }
    }
}
