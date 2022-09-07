class BackupThread extends Thread {
    private void handleTimer(RetrieveAddressTimer timer) throws AppiaEventException {
        new RemoteViewEvent(timer.getChannel(), Direction.DOWN, this, new Group(groupID)).go();
        timer.go();
    }
}
