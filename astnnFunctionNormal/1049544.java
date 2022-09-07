class BackupThread extends Thread {
    private void handleSendableEvent(SendableEvent event) throws AppiaEventException {
        if (event.getDir() == Direction.DOWN) {
            if (!hasAddressList()) {
                pendingEvents.add(event);
                new RemoteViewEvent(event.getChannel(), Direction.DOWN, this, new Group(groupID)).go();
            } else if (event.dest == null) {
                event.dest = getAddressRoundRobin();
            }
            if (pendingEvents.isEmpty()) {
                if (event.dest != null) {
                    if (logger.isDebugEnabled()) logger.debug("Sending to address: " + event.dest);
                    event.go();
                } else {
                    pendingEvents.add(event);
                }
            } else pendingEvents.add(event);
            trySendMessages();
        } else {
            event.go();
        }
    }
}
