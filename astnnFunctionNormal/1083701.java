class BackupThread extends Thread {
    @Override
    public void daqOff() {
        daqSetCh = ChannelFactory.defaultFactory().getChannel(daqSetRec);
        CaMonitorScalar.setChannel(daqSetCh, 0);
    }
}
