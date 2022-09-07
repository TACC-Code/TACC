class BackupThread extends Thread {
    public static boolean acquireLock() {
        getTempDir().mkdirs();
        Log.print(0, "Work dir set to " + getTempDir());
        try {
            Log.print(0, "Acquiring lock on work directory...");
            lockFile = new File(getTempDir(), ".lock");
            channel = new RandomAccessFile(lockFile, "rw").getChannel();
            lock = channel.tryLock();
        } catch (IOException e) {
            throw new Profiler4JError("I/O error while acquiring lock", e);
        }
        if (lock == null) {
            Log.print(0, "ERROR: Another Profiler4j Agent is using the current work dir");
            return false;
        }
        if (getTempDir().exists()) {
            _removeTempDir();
        }
        getUninstrumentedDir().mkdirs();
        Log.print(0, "Lock acquired successfully");
        return true;
    }
}
