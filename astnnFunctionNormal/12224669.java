class BackupThread extends Thread {
    private synchronized void attach() {
        if (xpSessionReceiver == null) {
            try {
                ClientSideCommunicationFactory factory = ClientSideCommunicationFactory.getInstance();
                localUserId = ClientApplication.getInstance().getUserManager().getUserId();
                localUserGroup = ClientApplication.getInstance().getUserManager().getUserGroup();
                sessionView.setLocalUserId(localUserId);
                sessionView.setLocalUserGroup(localUserGroup);
                xpSessionReceiver = factory.attachToList(getChannelId(), new ReplicatedEventListener(sessionView));
            } catch (NetworkException e) {
                ClientXPLog.logException(0, "Session Gallery Controller", "error when trying to attach", e, false);
            }
            try {
                sessionView.activate();
            } catch (Exception e) {
                ClientXPLog.logException(0, "Session Gallery Controller", "error when trying to activate the view", e, false);
            } finally {
                notifyAll();
            }
        }
    }
}
