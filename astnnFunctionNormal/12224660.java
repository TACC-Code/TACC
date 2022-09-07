class BackupThread extends Thread {
    public XPSessionGalleryController(IXPSessionGalleryView view) throws Exception {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSIONGALLERY_CONTROLLER + "new instance for channel id: " + getChannelId());
        }
        this.sessionView = view;
        csl = new ConnectionStateListener();
        RemoteEventManager.getInstance().addConnectionStateListener(csl);
        rel = new RemoteEventListener(this.sessionView);
        RemoteEventManager.getInstance().addMessageListener(rel);
        sessionListener = new SessionListener();
        ClientApplication.getInstance().getXPSessionManager().addListener(sessionListener);
        ClientSideCommunicationFactory factory = ClientSideCommunicationFactory.getInstance();
        commandInterface = factory.getServerCommandInterface();
        localUserId = ClientApplication.getInstance().getUserManager().getUserId();
        localUserGroup = ClientApplication.getInstance().getUserManager().getUserGroup();
        sessionView.setLocalUserId(localUserId);
        sessionView.setLocalUserGroup(localUserGroup);
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(LogConstants.LOG_PREFIX_XPSESSIONGALLERY_CONTROLLER + "local user name=" + localUserId);
        }
    }
}
