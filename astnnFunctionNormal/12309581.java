class BackupThread extends Thread {
    public MicrosecondUnitsHandler(final TimeModel aModel, final TimeModelListener aModelProxy) {
        model = aModel;
        modelProxy = aModelProxy;
        try {
            final Channel ringFrequencyChannel = ChannelFactory.defaultFactory().getChannel(turnMHzPVName);
            if (ringFrequencyChannel.connectAndWait()) {
                period = 1 / ringFrequencyChannel.getValDbl();
                monitor = ringFrequencyChannel.addMonitorValue(this, Monitor.VALUE);
            } else {
                throw new RuntimeException("Microsecond units handler failed due to connection failure for PV:\n" + turnMHzPVName);
            }
        } catch (ConnectionException exception) {
            throw new RuntimeException("Microsecond units handler failed due to connection exception:\n" + exception.getMessage());
        } catch (GetException exception) {
            throw new RuntimeException("Microsecond units handler failed due to get exception:\n" + exception.getMessage());
        } catch (MonitorException exception) {
            throw new RuntimeException("Microsecond units handler failed due to monitor exception:\n" + exception.getMessage());
        }
    }
}
