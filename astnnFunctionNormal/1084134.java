class BackupThread extends Thread {
    public void process(Request request) {
        try {
            if (request.getType() == ProtocolConstants.NULL) {
                LOG.info("--> process()");
                MessageMSG message = request.getMessage();
                if (message.getChannel() != null && message.getChannel().getState() == Channel.STATE_ACTIVE) {
                    LOG.debug("send empty reply");
                    OutputDataStream os = new OutputDataStream();
                    os.setComplete();
                    message.sendRPY(os);
                } else {
                    LOG.debug("cannot send empty reply [" + ((message.getChannel() != null) ? message.getChannel() + "] [" + message.getChannel().getState() + "]" : "null") + "]");
                }
                LOG.info("<-- process()");
            } else {
                super.process(request);
            }
        } catch (Exception e) {
            LOG.error("exception processing request [" + e + ", " + e.getMessage() + "]");
        }
    }
}
