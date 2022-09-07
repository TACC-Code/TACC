class BackupThread extends Thread {
    public void setTimeDiv(int index) {
        System.out.println("set digi " + id + " timediv to " + index);
        if (!caputFlag) {
            return;
        }
        daqOff();
        timeDivSetCh = ChannelFactory.defaultFactory().getChannel(timeDivSetRec);
        CaMonitorScalar.setChannel(timeDivSetCh, index);
        daqOn();
    }
}
