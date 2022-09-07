class BackupThread extends Thread {
    public void run() {
        while (running) {
            if (!loggedIn) {
                try {
                    supernodeConnector.connect();
                    supernode = (Consumer2Supernode) getDefaultServer().getProxyFactory().createProxy(new URL(supernodeConnector.getSupernodeUrl() + "/" + Consumer2Supernode.class.getSimpleName()), Consumer2Supernode.class);
                    loggedIn = true;
                    latestReportBack = 0L;
                } catch (Exception e) {
                    System.out.println("failed to connect to supernode -- retrying ...");
                    if (debug) e.printStackTrace();
                }
            }
            if (loggedIn && System.currentTimeMillis() - latestReportBack > reportBackInterval) {
                try {
                    supernode.login();
                    latestReportBack = System.currentTimeMillis();
                } catch (PpException e) {
                    loggedIn = false;
                    System.out.println("error reporting back -> supernode connection lost");
                    if (debug) e.printStackTrace();
                }
            }
            try {
                sleep(DAEMON_THREAD_SLEEP_INTERVAL);
            } catch (InterruptedException ie) {
            }
        }
        synchronized (this) {
            stopped = true;
            notify();
        }
    }
}
