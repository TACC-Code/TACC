class BackupThread extends Thread {
    private synchronized NewStateEvent getActiveCall(String channel) {
        if (channel == null) {
            return null;
        }
        for (NewStateEvent activeCall : activeCalls) {
            if (Compare.equal(activeCall.getChannel(), channel)) {
                return activeCall;
            }
        }
        return null;
    }
}
