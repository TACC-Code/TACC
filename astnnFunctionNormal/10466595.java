class BackupThread extends Thread {
    private Failures aggregateResults(MessageRecipients failures) {
        final Failures result = MessageFactory.getDefaultInstance().createFailures();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered aggregateResults");
        }
        if (failures != null) {
            for (Iterator iterator = failures.getIterator(); iterator.hasNext(); ) {
                MessageRecipient failure = (MessageRecipient) iterator.next();
                try {
                    Recipient recipient = getFailedRecipient(failure);
                    recipient.setFailureReason(failure.getFailureReason());
                    recipient.setChannel(failure.getChannelName());
                    result.add(recipient);
                } catch (RecipientException e) {
                    LOGGER.warn("recipient-conversion-failure", failure, e);
                }
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Exited aggregateResults");
        }
        return result;
    }
}
