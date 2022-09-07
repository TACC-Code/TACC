class BackupThread extends Thread {
    private void checkWriteOnMultiThread(final Stream stream) throws SkypeException {
        Thread[] threads = new Thread[20];
        final String[] results = new String[threads.length];
        for (int i = 0; i < threads.length; i++) {
            final int index = i;
            Thread thread = new Thread("write-" + index) {

                @Override
                public void run() {
                    try {
                        final Object lock = new Object();
                        stream.addStreamListener(new StreamAdapter() {

                            @Override
                            public void textReceived(String text) throws SkypeException {
                                results[index] = text;
                                synchronized (lock) {
                                    lock.notify();
                                }
                            }
                        });
                        synchronized (lock) {
                            stream.write("Hello, World!");
                            try {
                                lock.wait(10000);
                            } catch (InterruptedException e) {
                            }
                        }
                    } catch (SkypeException e) {
                    }
                }

                ;
            };
            thread.start();
            threads[i] = thread;
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
        for (String result : results) {
            assertEquals("Hello, World!", result);
        }
    }
}
