class BackupThread extends Thread {
    private synchronized Message[] readMessages() throws FetcherException {
        if (pop3 == null) {
            logger.error("Error you are not connected to pop3 server " + configuration.getMailServer().toString());
            throw new FetcherException(FetcherExceptionCode.NOT_CONNECTED);
        }
        Message[] messagesResult = null;
        try {
            POP3MessageInfo[] messages = pop3.listMessages();
            if (messages != null) {
                messagesResult = new Message[messages.length];
                logger.info("They are " + messages.length + " message(s) on configuration.getMailbox() ");
                for (int i = 0; i < Math.min(configuration.getMaxFetchedMessage(), messages.length); i++) {
                    POP3MessageInfo message = messages[i];
                    logger.debug("Reading message " + message.number + " on configuration.getMailbox() ");
                    Reader reader = pop3.retrieveMessage(message.number);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    char[] buffer = new char[4096];
                    int read = 0;
                    while ((read = reader.read(buffer)) >= 0) {
                        String readString = new String(buffer, 0, read);
                        output.write(readString.getBytes());
                        logger.trace("read " + read + " chars : " + readString);
                    }
                    reader.close();
                    output.close();
                    Message messageResult = new MessageImpl();
                    messageResult.setBinaryContent(output.toByteArray());
                    Calendar calendar = Calendar.getInstance();
                    messageResult.setReceivedTime(calendar.getTimeInMillis());
                    messageResult.setId(message.number);
                    messagesResult[message.number - 1] = messageResult;
                }
                if (configuration.isDeleteAfterFetch()) {
                    for (POP3MessageInfo message : messages) {
                        logger.debug("Deleting message " + message.number + " on configuration.getMailbox() ");
                        pop3.deleteMessage(message.number);
                    }
                }
            } else {
                logger.info("They are no message on configuration.getMailbox() ");
            }
        } catch (IOException ex) {
            logger.error("Error when try to read mailbox messages " + configuration.getMailbox().toString());
            throw new FetcherException(ex, FetcherExceptionCode.MAILBOX_LOGIN_ERROR);
        }
        return messagesResult;
    }
}
