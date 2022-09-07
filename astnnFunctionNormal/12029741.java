class BackupThread extends Thread {
        @Override
        public void run() {
            try {
                URL url = new URL(TEST_URL);
                for (int r = 0; r < NUM_RUNS; r++) {
                    URLConnection conn = url.openConnection();
                    String status = conn.getHeaderField(null);
                    System.out.println(name + ":" + status);
                    if (!OK_STRING.equals(status)) {
                        errorCount++;
                    }
                    long sleepTime = (long) (Math.random() * (MAX_RANDOM_SLEEP - MIN_RANDOM_SLEEP));
                    Thread.sleep(sleepTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
