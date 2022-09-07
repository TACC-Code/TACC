class BackupThread extends Thread {
    private static void init(int threadCount) {
        try {
            GkcConfig.getInstance().setGroupkeySize(10000);
            GroupkeyCache.start();
            logger.info("GkcSinglethreadTest start load keys....");
            gkeys = FileUtils.readLines(new File("resources" + File.separatorChar + "gkeys.txt"), "UTF-8");
            logger.info("GkcSinglethreadTest keys load complete,gkeys.size:" + gkeys.size());
            GkcSinglethreadTest.threadCount = threadCount;
            writeService = Executors.newFixedThreadPool(threadCount);
            for (int i = 0; i < threadCount; i++) {
                writeService.submit(new GkcSinglethreadTest());
            }
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            logger.error("GkcSinglethreadTest init error!", e);
        }
    }
}
