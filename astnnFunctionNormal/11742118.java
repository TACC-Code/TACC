class BackupThread extends Thread {
        public TSegment() throws IOException {
            final Segment readOnlySnapShotSegment = collector.getReadOnlySnapShot();
            this.txnBaseId = readOnlySnapShotSegment.getNextId();
            final long baseId = readOnlySnapShotSegment.getBaseId();
            this.txnUnit = UnitDir.writeNewInstance(pendingDir, collector.nextUnitId());
            {
                FileChannel entryChannel = new RandomAccessFile(txnUnit.getEntryFile(), "rw").getChannel();
                FileChannel indexChannel = new RandomAccessFile(txnUnit.getIndexFile(), "rw").getChannel();
                this.index = MutableBaseIdIndex.writeNewIndex(indexChannel, Word.INT, getTxnBaseId(), 0);
                newSegment = new BaseSegment(index, entryChannel);
            }
            {
                FileChannel deleteSetChannel = new RandomAccessFile(txnUnit.getDeleteSetFile(), "rw").getChannel();
                this.deleteSet = FileBackedDeleteSet.writeNewInstance(deleteSetChannel, baseId, getTxnBaseId());
            }
            {
                Segment[] segments = new Segment[] { new DeleteSetFilterSegment(readOnlySnapShotSegment, deleteSet), newSegment };
                this.implDelegate = new UnionSegment(segments);
            }
            if (logger.isLoggable(Level.FINER)) logger.finer("created new txn " + txnUnit.getDir().getName() + " (txn base id is " + txnBaseId + ")");
        }
}
