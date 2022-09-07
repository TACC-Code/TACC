class BackupThread extends Thread {
    public boolean open() throws DBException {
        try {
            raf = new RandomAccessFile(file, "rw");
            fc = raf.getChannel();
            lock = fc.tryLock();
            if (lock == null) {
                System.err.println("FATAL ERROR: Cannot open '" + file.getName() + "' for exclusive access");
                System.exit(1);
            }
            if (exists()) {
                fileHeader.read();
                opened = true;
            } else opened = false;
            if (opened) openTransactionLog();
            return opened;
        } catch (Exception e) {
            throw new FilerException(FaultCodes.GEN_CRITICAL_ERROR, "Error opening " + file.getName(), e);
        }
    }
}
