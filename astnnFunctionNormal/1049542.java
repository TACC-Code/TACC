class BackupThread extends Thread {
    private void handleChannelInit(ChannelInit init) throws AppiaException {
        new RetrieveAddressTimer(timerPeriod, init.getChannel(), Direction.DOWN, this, EventQualifier.ON).go();
        init.go();
    }
}
