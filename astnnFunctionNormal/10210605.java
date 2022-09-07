class BackupThread extends Thread {
    DefaultWriter(ContextLog log, IndexWriter writer, boolean overrideCheckpoint, @Nullable String checkpoint, boolean created) throws IndexException {
        this.log = checkNotNull(log, "The log context must be provided");
        this.writer = checkNotNull(writer, "The index writer must be provided");
        this.properties = new MapMaker().makeMap();
        this.keys = Collections.unmodifiableSet(this.properties.keySet());
        try {
            final Map<String, String> commitData;
            final int documents;
            if (created) {
                commitData = ImmutableMap.of();
                documents = 0;
            } else {
                final IndexReader reader = IndexReader.open(writer, false);
                try {
                    Map<String, String> data = reader.getCommitUserData();
                    if (overrideCheckpoint) {
                        final Map<String, String> modified = Maps.newHashMap();
                        if (data != null) {
                            modified.putAll(data);
                        }
                        modified.put(IndexInfo.CHECKPOINT, checkpoint);
                        commitData = modified;
                    } else {
                        commitData = data;
                    }
                    documents = reader.numDocs();
                } finally {
                    Closeables.closeQuietly(reader);
                }
            }
            this.indexInfo = IndexInfo.fromMap(documents, commitData);
            this.checkpoint = this.indexInfo.getCheckpoint();
            this.targetCheckpoint = this.indexInfo.getTargetCheckpoint();
            this.properties.putAll(this.indexInfo.getProperties());
        } catch (LockObtainFailedException e) {
            indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.LOCKED);
            throw new IndexException(e);
        } catch (CorruptIndexException e) {
            indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.CORRUPT);
            throw new IndexException(e);
        } catch (IOException e) {
            indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.IOERROR);
            throw new IndexException(e);
        } catch (RuntimeException e) {
            indexStatus.compareAndSet(IndexStatus.OK, IndexStatus.ERROR);
            throw e;
        }
    }
}
