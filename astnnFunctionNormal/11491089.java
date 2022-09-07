class BackupThread extends Thread {
    private void doWriteReadTest(Random rnd, IContainer expected, boolean completeState, boolean remove) throws InterruptedException {
        if (!completeState) {
            ((StringField) expected.get((FIELD1))).set("lasers-" + rnd.nextLong());
            ((DoubleField) expected.get((FIELD2))).set(rnd.nextDouble());
            ((IntegerField) expected.get((FIELD3))).set(rnd.nextInt());
        }
        if (remove) {
            assertTrue("remote reference not removed", context.removeContainer((IContainer) result));
            assertFalse("remote reference was removed again", context.removeContainer((IContainer) result));
        }
        final byte[] frame = completeState ? new FrameWriter().writeComplete(expected) : ContainerJUnitTest.getFrame(context, expected, false, true);
        result = null;
        Runnable task = new Runnable() {

            public void run() {
                result = new FrameReader().read(frame, "remoteContextIdentity", context);
            }
        };
        Thread reader = new Thread(task);
        reader.start();
        reader.join();
        assertNotSame("Did not write/read correctly", expected, result);
        assertEquals("Did not write/read correctly", expected, result);
    }
}
