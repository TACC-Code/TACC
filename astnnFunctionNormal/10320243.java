class BackupThread extends Thread {
    public void testThreadedUsage() throws InterruptedException {
        MockRewriter m1 = new MockRewriter("s1", "u1");
        MockRewriter m2 = new MockRewriter("s2", "u2");
        MockRewriter m3 = new MockRewriter("s3", "u3");
        Thread t1 = new Thread(new UrlRewriteTestRunnable(m1));
        Thread t2 = new Thread(new UrlRewriteTestRunnable(m2));
        Thread t3 = new Thread(new UrlRewriteTestRunnable(m3));
        t1.start();
        t2.start();
        t3.start();
        t1.join(1000);
        t2.join(1000);
        t3.join(1000);
        assertEquals(1, m1.count);
        assertEquals(1, m2.count);
        assertEquals(1, m3.count);
    }
}
