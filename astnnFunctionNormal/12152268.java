class BackupThread extends Thread {
    public boolean handle(final MessageEvent me) {
        if (me.getMessage() instanceof Event) {
            final Event pe = (Event) me.getMessage();
            if (pe.conversationId != currentConversationId) {
                logger.debug("stopping obsolete conversation id: {}, current: {}", pe.getConversationId(), currentConversationId);
                return true;
            }
            write(me.getChannel());
            return true;
        }
        return false;
    }
}
