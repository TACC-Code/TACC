class BackupThread extends Thread {
    private static void lockLocation() throws IOException {
        theLockFile = new RandomAccessFile(new File(theLocation, "blitz.lock"), "rw");
        theLockChannel = theLockFile.getChannel();
        theLock = theLockChannel.tryLock();
        if (theLock == null) throw new IOException("Couldn't lock, are you running another Blitz instance in this directory?");
    }
}
