class BackupThread extends Thread {
    public TRNonBlockingServer(String _name, int _port, InetAddress _bind_ip, boolean _apply_ip_filter, boolean _start_up_ready, TRNonBlockingServerProcessorFactory _processor_factory) throws TRTrackerServerException {
        super(_name, _port, false, _apply_ip_filter, _start_up_ready);
        processor_factory = _processor_factory;
        read_selector = new VirtualChannelSelector(_name + ":" + _port, VirtualChannelSelector.OP_READ, false);
        write_selector = new VirtualChannelSelector(_name + ":" + _port, VirtualChannelSelector.OP_WRITE, true);
        boolean ok = false;
        if (_port == 0) {
            throw (new TRTrackerServerException("port of 0 not currently supported"));
        }
        try {
            InetSocketAddress address;
            if (_bind_ip == null) {
                _bind_ip = NetworkAdmin.getSingleton().getSingleHomedServiceBindAddress();
                if (_bind_ip == null) {
                    address = new InetSocketAddress(_port);
                } else {
                    current_bind_ip = _bind_ip;
                    address = new InetSocketAddress(_bind_ip, _port);
                }
            } else {
                current_bind_ip = _bind_ip;
                address = new InetSocketAddress(_bind_ip, _port);
            }
            accept_server = VirtualServerChannelSelectorFactory.createBlocking(address, 0, this);
            accept_server.start();
            AEThread read_thread = new AEThread("TRTrackerServer:readSelector") {

                public void runSupport() {
                    selectLoop(read_selector);
                }
            };
            read_thread.setDaemon(true);
            read_thread.start();
            AEThread write_thread = new AEThread("TRTrackerServer:writeSelector") {

                public void runSupport() {
                    selectLoop(write_selector);
                }
            };
            write_thread.setDaemon(true);
            write_thread.start();
            AEThread close_thread = new AEThread("TRTrackerServer:closeScheduler") {

                public void runSupport() {
                    closeLoop();
                }
            };
            close_thread.setDaemon(true);
            close_thread.start();
            Logger.log(new LogEvent(LOGID, "TRTrackerServer: Non-blocking listener established on port " + getPort()));
            ok = true;
        } catch (Throwable e) {
            Logger.logTextResource(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_ERROR, "Tracker.alert.listenfail"), new String[] { "" + getPort() });
            throw (new TRTrackerServerException("TRTrackerServer: accept fails", e));
        } finally {
            if (!ok) {
                destroySupport();
            }
        }
    }
}
