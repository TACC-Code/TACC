class BackupThread extends Thread {
    private synchronized void removeActiveCall(String channel) {
        if (channel == null) {
            return;
        }
        NewStateEvent removeCall = null;
        for (NewStateEvent activeCall : activeCalls) {
            if (Compare.equal(activeCall.getChannel(), channel)) {
                removeCall = activeCall;
                break;
            }
        }
        if (removeCall != null) {
            activeCalls.remove(removeCall);
        }
    }
}
