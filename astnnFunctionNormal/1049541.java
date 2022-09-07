class BackupThread extends Thread {
    private void handleChannelClose(ChannelClose close) throws AppiaException {
        new RetrieveAddressTimer(timerPeriod, close.getChannel(), Direction.DOWN, this, EventQualifier.OFF).go();
        close.go();
    }
}
