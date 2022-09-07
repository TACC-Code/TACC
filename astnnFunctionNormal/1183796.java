class BackupThread extends Thread {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        AgentSession agentSession = (AgentSession) ctx.getAttachment();
        if (agentSession == null) {
            agentSession = new AgentSession("unkown");
        }
        Throwable exception = e.getCause();
        NetworkCodes.CODE code = null;
        CollectorStatus.STATUS stat = null;
        if (exception instanceof java.net.ConnectException) {
            code = NetworkCodes.CODE.COORDINATION_CONNECTION_ERROR;
            stat = CollectorStatus.STATUS.COORDINATION_ERROR;
        } else if (exception instanceof CoordinationException) {
            CoordinationException coordExcp = (CoordinationException) exception;
            if (coordExcp.isConnectException()) {
                code = NetworkCodes.CODE.COORDINATION_CONNECTION_ERROR;
                stat = CollectorStatus.STATUS.COORDINATION_ERROR;
            } else {
                code = NetworkCodes.CODE.COORDINATION_LOCK_ERROR;
                stat = CollectorStatus.STATUS.COORDINATION_LOCK_ERROR;
            }
        } else {
            code = NetworkCodes.CODE.UNKOWN;
            stat = CollectorStatus.STATUS.UNKOWN_ERROR;
        }
        try {
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
            buffer.writeInt(code.num());
            buffer.writeBytes(code.msg().getBytes("UTF-8"));
            if (e.getChannel().isOpen()) {
                ChannelFuture future = e.getChannel().write(buffer);
                future.addListener(ChannelFutureListener.CLOSE);
            } else {
                LOG.error("Channel was closed by agent " + agentSession.getAgentName() + ": exception: " + code.num() + " " + code.msg() + " cause: " + exception + " cannot be written to agent");
            }
            collectorStatus.setStatus(stat, e.getCause().toString());
            collectorStatus.incCounter("Errors_Caught", 1);
            LOG.error(agentSession.toString());
            LOG.error(exception.toString(), exception);
        } catch (Throwable t) {
            LOG.error("Throwed exception in catchException " + t);
        }
    }
}
