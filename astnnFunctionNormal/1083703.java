class BackupThread extends Thread {
    public void setTimeDelay(double delay) {
        System.out.println("set digi " + id + " timedelay to " + delay);
        if (!caputFlag) {
            return;
        }
        daqOff();
        timeDelaySetCh = ChannelFactory.defaultFactory().getChannel(timeDelaySetRec);
        CaMonitorScalar.setChannel(timeDelaySetCh, delay);
        daqOn();
    }
}
