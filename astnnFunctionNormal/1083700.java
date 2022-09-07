class BackupThread extends Thread {
    @Override
    public void daqOn() {
        daqSetCh = ChannelFactory.defaultFactory().getChannel(daqSetRec);
        CaMonitorScalar.setChannel(daqSetCh, 1);
    }
}
