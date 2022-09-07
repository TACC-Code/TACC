class BackupThread extends Thread {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                List<Character> resources = getResources();
                boolean shared = RANDOM.nextInt(FRACTION_EX) != 0;
                MultiReadWriteLock<Character> rwLock = PROVIDER.newLock(resources);
                MultiLock<Character> lock = shared ? rwLock.readLock() : rwLock.writeLock();
                log(shared, resources, "ready");
                try {
                    long time = System.currentTimeMillis();
                    if (doLock(lock)) {
                        time = System.currentTimeMillis() - time;
                        log(shared, resources, "lock (" + time + ")");
                        try {
                            Thread.sleep(WORK_TIME);
                        } catch (InterruptedException e) {
                            log(shared, resources, "aborted work");
                            lock.unlock();
                            log(shared, resources, "unlock");
                            break;
                        }
                        lock.unlock();
                        log(shared, resources, "unlock");
                    } else {
                        log(shared, resources, "failed");
                    }
                } catch (InterruptedException e) {
                    log(shared, resources, "interrupted");
                    break;
                }
            }
        }
}
