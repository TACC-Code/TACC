class BackupThread extends Thread {
    public ServiceConnection(ServiceDescriptor descriptor) {
        _descriptor = descriptor;
        _pendingRequests = new HashMap<IMessage, FutureResponse>();
        _pendingStates = new HashMap<IMessage, IterativeRunState>();
        _channelListener = new IChannelListener() {

            public void handleChannelEvent(IChannelEvent event) {
                synchronized (ServiceConnection.this) {
                    if (event instanceof IChannelMessageEvent) try {
                        IChannelMessageEvent msg = (IChannelMessageEvent) event;
                        IMessage message = MessageManager.toMessage(msg);
                        ID serviceID = _descriptor.getServiceID();
                        ID from = MessageManager.toCanonical(msg.getFromContainerID());
                        if (serviceID == null || from.equals(serviceID)) handleMessage(message); else {
                            Activator.warn("Message was received from unexpected source :" + msg.getFromContainerID(), null);
                            if (LOGGER.isWarnEnabled()) LOGGER.warn("Message was received from someone else? expected: " + serviceID + " received " + from + "/" + msg.getFromContainerID());
                        }
                    } catch (Exception e) {
                        Activator.error("Failed to process message ", e);
                        LOGGER.error("Exception while retrieving message ", e);
                    } else if (event instanceof IChannelConnectEvent) {
                        IChannelConnectEvent connect = (IChannelConnectEvent) event;
                        if (LOGGER.isDebugEnabled()) LOGGER.debug("Connection on " + connect.getChannelID() + " from " + connect.getTargetID());
                    } else if (event instanceof IChannelDisconnectEvent) disconnected((IChannelDisconnectEvent) event);
                }
            }
        };
    }
}
