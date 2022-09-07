class BackupThread extends Thread {
    public static void main(String[] args) {
        System.out.println("Waiting 20s to start...");
        sleep(20 * 1000);
        final ExecutorService executor = Executors.newCachedThreadPool();
        final List<Setup> setups = new ArrayList<Setup>();
        for (AccessMode accessMode : AccessMode.values()) {
            for (StoreMode storeMode : StoreMode.values()) {
                setups.add(new Setup(64, accessMode, storeMode, 10, 3, 10, 100, 5, 5));
            }
        }
        for (Setup setup : setups) {
            final PriceStore store = setup.storeMode.create(setup.initSize);
            final CountDownLatch latch = new CountDownLatch(setup.readerCount + setup.writerCount);
            new Writer(store, 0, ordered, null, 1).run();
            memCheckpoint("Pre test");
            System.out.println("\nStarted Test:");
            System.out.println("    count=" + COUNT);
            System.out.println("    burstCount=" + BURST_COUNT);
            System.out.println("    burstSize=" + BURST_SIZE);
            System.out.println("    initSize=" + setup.initSize);
            System.out.println("    storeMode=" + setup.storeMode);
            System.out.println("    accessMode=" + setup.accessMode);
            System.out.println("    writerCount=" + setup.writerCount);
            System.out.println("    writerLoop=" + setup.writerLoop);
            System.out.println("    writerBurstLagMillis=" + setup.writerBurstLag);
            System.out.println("    readerCount=" + setup.readerCount);
            System.out.println("    readerLoop=" + setup.readerLoop);
            System.out.println("    readerBurstLagMillis=" + setup.readerBurstLag);
            final long timestamp = System.currentTimeMillis();
            for (int i = 0; i < setup.writerCount; i++) {
                executor.execute(new Writer(store, setup.writerBurstLag, setup.accessMode.ids, latch, setup.writerLoop));
            }
            for (int i = 0; i < setup.readerCount; i++) {
                executor.execute(new Reader(store, setup.readerBurstLag, setup.accessMode.ids, latch, setup.readerLoop));
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
            }
            final long duration = (System.currentTimeMillis() - timestamp);
            System.out.println(String.format("Test Complete. Duration=%.2fs\n", (duration / 1000.0)));
            memCheckpoint("Post test");
            System.out.println("Waiting 10s...");
            sleep(10 * 1000);
        }
        System.exit(0);
    }
}
