class BackupThread extends Thread {
    public void performWarmupOperations(CacheWrapper w) throws Exception {
        this.wrapper = w;
        log.info("Cache launched, performing " + (Integer) operationCount + " put and get operations ");
        Thread[] warmupThreads = new Thread[WARMUP_THREADS];
        final AtomicInteger writes = new AtomicInteger(0);
        final AtomicInteger reads = new AtomicInteger(0);
        final Random r = new Random();
        for (int i = 0; i < WARMUP_THREADS; i++) {
            final int threadId = i;
            warmupThreads[i] = new Thread() {

                public void run() {
                    while (writes.get() < operationCount && reads.get() < operationCount) {
                        boolean isGet = r.nextInt(2) == 1;
                        int operationId;
                        if (isGet) {
                            if ((operationId = reads.getAndIncrement()) < operationCount) doGet(operationId, threadId);
                        } else {
                            if ((operationId = writes.getAndIncrement()) < operationCount) doPut(operationId, threadId);
                        }
                    }
                }
            };
            warmupThreads[i].start();
        }
        log.info("Joining warmupThreads");
        for (Thread t : warmupThreads) t.join();
        log.info("Cache warmup ended!");
    }
}
