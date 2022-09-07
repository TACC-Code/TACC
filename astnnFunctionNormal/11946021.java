class BackupThread extends Thread {
    public boolean create() throws DBException {
        try {
            raf = new RandomAccessFile(file, "rw");
            fc = raf.getChannel();
            lock = fc.tryLock();
            if (lock == null) {
                System.err.println("FATAL ERROR: Cannot open '" + file.getName() + "' for exclusive access");
                System.exit(1);
            }
            fileHeader.write();
            createTransactionLog();
            openTransactionLog();
            Transaction tx = new Transaction();
            checkTransaction(tx);
            try {
                tx.commit();
            } catch (DBException e) {
                tx.cancel();
                throw e;
            }
            closeTransactionLog();
            lock.release();
            raf.close();
            fc.close();
            reset();
            return true;
        } catch (Exception e) {
            throw new FilerException(FaultCodes.GEN_CRITICAL_ERROR, "Error creating " + file.getName(), e);
        }
    }
}
