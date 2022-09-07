class BackupThread extends Thread {
    public void delete(LogConnection client, Long low, Long high) throws ReplicatorException, InterruptedException {
        if (readOnly || !writeLock.isLocked()) {
            throw new THLException("Attempt to delete from read-only log");
        }
        long lowSeqno;
        long highSeqno;
        if (low == null) lowSeqno = index.getMinIndexedSeqno(); else lowSeqno = low;
        if (high == null) highSeqno = index.getMaxIndexedSeqno(); else highSeqno = high;
        if (highSeqno != index.getMaxIndexedSeqno() && lowSeqno != index.getMinIndexedSeqno()) {
            throw new THLException("Deletion range invalid; " + "must include one or both log end points: low seqno=" + lowSeqno + " high seqno=" + highSeqno);
        }
        for (LogIndexEntry lie : index.getIndexCopy()) {
            if (lie.startSeqno >= lowSeqno && lie.endSeqno <= highSeqno) {
                logger.info("Deleting log file: " + lie.toString());
                purgeFile(lie);
            } else if (lie.startSeqno < lowSeqno && lie.endSeqno >= lowSeqno) {
                logger.info("Truncating log file at seqno " + lowSeqno + ": " + lie.toString());
                truncateFile(client, lie, lowSeqno);
            }
        }
    }
}
