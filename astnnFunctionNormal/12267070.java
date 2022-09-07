class BackupThread extends Thread {
    public InstanceChecker() {
        try {
            f = new File("g15lastfmInstance.lock");
            if (f.exists()) {
                f.delete();
            }
            channel = new RandomAccessFile(f, "rw").getChannel();
            lock = channel.tryLock();
            if (lock == null) {
                channel.close();
                throw new RuntimeException("G15LastfmPlayer already running");
            }
            ShutdownHook shutdownHook = new ShutdownHook();
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            LOGGER.debug("Running G15Lastfm Player");
        } catch (IOException e) {
            throw new RuntimeException("Could not start process.", e);
        }
    }
}
