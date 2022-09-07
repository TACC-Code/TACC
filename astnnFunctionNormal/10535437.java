class BackupThread extends Thread {
    public void testMonitor() throws InterruptedException {
        ResponseTimeMonitorThread rtmt = new ResponseTimeMonitorThread();
        Thread thread = new Thread(rtmt);
        thread.start();
        Channel channel = rtmt.getChannel();
        ResponseTimeMonitor monitor = new ResponseTimeMonitor(channel);
        assertEquals(0, monitor.getResponseTime());
        for (int i = 0; i < 100; i++) {
            monitor.logThroughputEvent(new ThroughputEventTestThing(1, i));
        }
        Thread.sleep(10);
        assertEquals(27, monitor.getResponseTime());
        for (int i = 0; i < 100; i++) {
            monitor.logThroughputEvent(new ThroughputEventTestThing(1, i * 10));
        }
        Thread.sleep(10);
        assertEquals(288, monitor.getResponseTime());
    }
}
