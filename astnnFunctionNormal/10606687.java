class BackupThread extends Thread {
    @Test
    public void readStutteringStream() throws IOException {
        final String testData = "ab\ncd";
        final PipedReader reader = new PipedReader();
        final PipedWriter writer = new PipedWriter(reader);
        Thread t = new Thread() {

            public void run() {
                try {
                    for (int i = 0; i < testData.length(); i++) {
                        Thread.sleep(10);
                        log.debug("sending data: " + testData.charAt(i));
                        writer.append(testData.charAt(i));
                    }
                    writer.flush();
                } catch (Exception e) {
                    throw new RuntimeException("exception in test data driver thread", e);
                }
            }
        };
        t.start();
        sendAndCheckTestData(testData, reader);
    }
}
