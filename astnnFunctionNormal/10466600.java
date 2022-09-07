class BackupThread extends Thread {
    private MessageRecipient createMessageRecipient(Recipient recipient) throws RecipientException {
        MessageRecipient result = new MessageRecipient();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered createMessageRecipient");
        }
        setAddress(result, recipient.getAddress());
        result.setDeviceName(recipient.getDeviceName());
        result.setChannelName(recipient.getChannel());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Exited createMessageRecipient");
        }
        return result;
    }
}
