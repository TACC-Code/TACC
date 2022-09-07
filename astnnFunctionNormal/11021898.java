class BackupThread extends Thread {
    public void initialize(PluginInterface _pi) {
        plugin_interface = _pi;
        logger = plugin_interface.getLogger().getChannel("RunEverythingSeedingRules");
        plugin_interface.addListener(new PluginListener() {

            public void initializationComplete() {
            }

            public void closedownInitiated() {
                closing = true;
            }

            public void closedownComplete() {
            }
        });
        downloads = new HashMap();
        downloads_mon = plugin_interface.getUtilities().getMonitor();
        work_sem = plugin_interface.getUtilities().getSemaphore();
        plugin_interface.getDownloadManager().addListener(this);
        plugin_interface.getUtilities().createTimer("DownloadRules", true).addPeriodicEvent(10000, new UTTimerEventPerformer() {

            public void perform(UTTimerEvent event) {
                checkRules();
            }
        });
        plugin_interface.getUtilities().createThread("DownloadRules", new Runnable() {

            public void run() {
                processLoop();
            }
        });
    }
}
