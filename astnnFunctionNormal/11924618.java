class BackupThread extends Thread {
    public static void main(String[] args) {
        final RingQueue<String> ring = new RingQueue(16);
        Thread write = new Thread() {

            public void run() {
                while (true) {
                    ring.put("" + System.currentTimeMillis());
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread read = new Thread() {

            public void run() {
                while (true) {
                    System.out.println(ring.poll());
                }
            }
        };
        read.start();
        write.start();
    }
}
