class BackupThread extends Thread {
    public long getGetChannelCalls() {
        return ((AggregateProfileOperation) getChannelOp).getCount();
    }
}
