class BackupThread extends Thread {
    public void setChannelName(String chanName) {
        ch_ = null;
        if (chanName == null) {
            return;
        }
        if (badChannelNames.containsKey(chanName) == false) {
            callBack = new IEventSinkValue() {

                public void eventValue(ChannelRecord record, Channel chan) {
                    currValueMonitor = record.doubleValue();
                }
            };
            ch_ = ChannelFactory.defaultFactory().getChannel(chanName);
            try {
                ch_.addMonitorValue(callBack, Monitor.VALUE);
            } catch (ConnectionException e) {
                badChannelNames.put(chanName, null);
                ch_ = null;
            } catch (MonitorException e) {
                badChannelNames.put(chanName, null);
                ch_ = null;
            }
        }
    }
}
