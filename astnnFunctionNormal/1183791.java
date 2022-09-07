class BackupThread extends Thread {
    private final void writeToAgent(MessageEvent e, ChannelBuffer buffer, AgentSession agentSession) {
        ChannelFuture future = e.getChannel().write(buffer);
        agentSession.setSentResponseRequest();
        agentSession.setReleasedCoordinationLock();
        if ((agentSession.getFileWriteEndTime() - agentSession.getFileWriteStartTime()) > 500) {
            LOG.error("File writing is slowing down please check the log directory");
        }
        if (future != null) {
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    try {
                        ChannelFutureListener.CLOSE.operationComplete(f);
                    } catch (Throwable t) {
                        LOG.error("ERROR While closing channel :" + f.getChannel() + " " + f.getCause());
                    }
                }
            });
        }
    }
}
