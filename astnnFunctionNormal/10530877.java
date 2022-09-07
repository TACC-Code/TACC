class BackupThread extends Thread {
    private void readWriteTest(final int readcount, final int writecount, String testName) throws Throwable {
        final String[] rndStrings = new String[NTHREAD * NELEMENT];
        RandomArrayGenerator.fillRandArray(rndStrings);
        for (int threadID = 0; threadID < NTHREAD; threadID++) {
            if (threadID % 2 == 0) {
                for (int i = 0; i < NELEMENT; i++) {
                    String key = rndStrings[i + threadID * NELEMENT];
                    Assert.assertNull(mapStr.put(key, key));
                }
            }
        }
        Runnable[] tasks = new Runnable[NTHREAD];
        for (int threadID = 0; threadID < NTHREAD; threadID++) {
            final int index = threadID;
            tasks[threadID] = new Runnable() {

                public void run() {
                    final int opCount = getBlockSize();
                    for (int i = 0; i < NELEMENT; i++) {
                        String genKey = rndStrings[i + index * NELEMENT];
                        if (i % (readcount + writecount) < readcount) {
                            mapStr.get(genKey);
                        } else if (index % 2 == 0) {
                            mapStr.remove(genKey);
                        } else {
                            mapStr.put(genKey, genKey);
                        }
                    }
                }
            };
        }
        runner.runThreads(tasks, testName);
    }
}
