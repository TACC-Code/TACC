class BackupThread extends Thread {
    private synchronized void addActiveCall(NewStateEvent newStateEvent) {
        if (newStateEvent == null) {
            return;
        }
        if (NullStatus.isNull(newStateEvent.getChannel())) {
            return;
        }
        boolean add = true;
        for (NewStateEvent activeCall : activeCalls) {
            if (Compare.equal(activeCall.getChannel(), newStateEvent.getChannel())) {
                add = false;
                break;
            }
        }
        if (add) {
            activeCalls.add(newStateEvent);
        }
    }
}
