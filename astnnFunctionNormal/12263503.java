class BackupThread extends Thread {
    private void doFrameworkSerialisationTest(final Stats stats, boolean deltas, Record record, IntegerField[] intFields, DoubleField[] dblFields) throws Exception {
        ContainerJUnitTest.JUnitListener listener = new ContainerJUnitTest.JUnitListener("FrameworkSerialisation3", EventListenerUtils.createFilter(Record.class, TxEvent.class));
        record.addListener(listener);
        record.markForRemoteSubscription();
        ContainerJUnitTest.JUnitListener remoteListener = new ContainerJUnitTest.JUnitListener("FrameworkSerialisation4", EventListenerUtils.createFilter(Record.class));
        final IContainer remoteContainer = context.getRemoteContainer(REMOTE_CONTEXT_ID, record.getIdentity(), record.getType(), DOMAIN);
        remoteContainer.addListener(remoteListener);
        final Value<IContainer> result = new Value<IContainer>();
        for (int i = 0; i < ITERATIONS; i++) {
            listener.reset();
            listener.setExpectedUpdateCount(2);
            remoteListener.reset();
            if (deltas) {
                randomTriggerFieldValues(intFields, dblFields);
            } else {
                triggerFieldValues(i, intFields, dblFields);
            }
            record.flushFrame();
            listener.waitForUpdate();
            assertEquals("Event ", record, listener.container);
            final byte[] frame = listener.data.getBuffer();
            stats.start("write-framework", 0);
            stats.stop("write-framework", listener.data.getBufferCreateTime());
            stats.dataSize(frame.length);
            Runnable task = new Runnable() {

                public void run() {
                    stats.start("read-framework", System.nanoTime());
                    result.set(new FrameReader().read(frame, REMOTE_CONTEXT_ID, context));
                    stats.stop("read-framework", System.nanoTime());
                }
            };
            Thread t = new Thread(task);
            t.start();
            t.join();
            remoteListener.waitForUpdate();
            assertNotSame("Failed to write/read container", record, result.get());
            assertEquals("Failed to write/read container", record, result.get());
        }
        stats.setCount(ITERATIONS);
    }
}
