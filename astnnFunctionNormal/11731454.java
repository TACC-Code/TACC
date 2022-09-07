class BackupThread extends Thread {
    @Override
    public void run() {
        SocketChannel channel = null;
        Executor s = null;
        mythread = Thread.currentThread();
        while (goon) {
            if (debug) {
                logger.info("execute server " + this + " go on");
            }
            try {
                channel = cf.getChannel();
                logger.debug("execute channel = " + channel);
                if (mythread.isInterrupted()) {
                    goon = false;
                    logger.debug("execute server " + this + " interrupted");
                    channel.close();
                } else {
                    s = new Executor(channel);
                    if (goon) {
                        servers.add(s);
                        s.start();
                        logger.debug("server " + s + " started");
                    } else {
                        s = null;
                        channel.close();
                    }
                }
            } catch (InterruptedException e) {
                goon = false;
                Thread.currentThread().interrupt();
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }
            }
        }
        if (debug) {
            logger.info("execute server " + this + " terminated");
        }
    }
}
