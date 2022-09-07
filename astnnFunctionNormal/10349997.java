class BackupThread extends Thread {
        private Setup(int initSize, AccessMode mode, StoreMode store, int writerCount, int writerLoop, int writerBurstLag, int readerCount, int readerLoop, int readerBurstLag) {
            this.initSize = initSize;
            this.accessMode = mode;
            this.storeMode = store;
            this.writerCount = writerCount;
            this.writerLoop = writerLoop;
            this.writerBurstLag = writerBurstLag;
            this.readerCount = readerCount;
            this.readerLoop = readerLoop;
            this.readerBurstLag = readerBurstLag;
        }
}
