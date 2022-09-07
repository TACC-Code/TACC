class BackupThread extends Thread {
    private static void init(int threadCount) {
        try {
            GkcConfig.getInstance().setGroupkeySize(1000);
            GroupkeyCache.start();
            logger.info("GkcMultithreadTest start load keys....");
            gkeys = FileUtils.readLines(new File("resources" + File.separatorChar + "gkeys.txt"), "UTF-8");
            gidstr = FileUtils.readLines(new File("resources" + File.separatorChar + "gids.txt"), "UTF-8");
            for (String id : gidstr) gids.add(Long.parseLong(id));
            logger.info("GkcMultithreadTest keys load complete,gids.size:" + gids.size() + " gkeys.size:" + gkeys.size());
            GkcMultithreadTest.threadCount = threadCount;
            writeService = Executors.newFixedThreadPool(threadCount);
            for (int i = 0; i < threadCount; i++) {
                writeService.submit(new GkcMultithreadTest());
            }
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            logger.error("GkcMultithreadTest init error!", e);
        }
    }
}
