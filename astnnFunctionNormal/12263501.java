class BackupThread extends Thread {
    @Test
    public void testFrameworkSerialisation() throws InterruptedException {
        ContainerJUnitTest.JUnitListener listener = new ContainerJUnitTest.JUnitListener("FrameworkSerialisation1", EventListenerUtils.createFilter(Record.class, TxEvent.class));
        candidate.addListener(listener);
        candidate.markForRemoteSubscription();
        ContainerJUnitTest.JUnitListener remoteListener = new ContainerJUnitTest.JUnitListener("FrameworkSerialisation2", EventListenerUtils.createFilter(Record.class));
        final IContainer remoteContainer = context.getRemoteContainer(REMOTE_CONTEXT_ID, candidate.getIdentity(), candidate.getType(), DOMAIN);
        remoteContainer.addListener(remoteListener);
        final Value<IContainer> result = new Value<IContainer>();
        for (int i = 0; i < 20; i++) {
            listener.reset();
            listener.setExpectedUpdateCount(2);
            remoteListener.reset();
            triggerFieldValues(i, integerFields, doubleFields);
            candidate.flushFrame();
            listener.waitForUpdate();
            assertEquals("Event ", candidate, listener.container);
            final byte[] frame = listener.data.getBuffer();
            Runnable task = new Runnable() {

                public void run() {
                    result.set(new FrameReader().read(frame, REMOTE_CONTEXT_ID, context));
                }
            };
            Thread t = new Thread(task);
            t.start();
            t.join();
            remoteListener.waitForUpdate();
            assertNotSame("Failed to write/read container", candidate, result.get());
            assertEquals("Failed to write/read container", candidate, result.get());
        }
    }
}
