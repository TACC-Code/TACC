class BackupThread extends Thread {
    private void doTestWithCompleteState(boolean completeState, boolean hasLock) throws InterruptedException {
        final byte[] frame = completeState ? new FrameWriter().writeComplete(candidate) : ContainerJUnitTest.getFrame(context, candidate, hasLock, true);
        if (completeState) {
            context.removeContainer((IContainer) result);
            assertFalse("remote reference was removed again", context.removeContainer((IContainer) result));
        }
        result = ContainerJUnitTest.readFrame(context, candidate, frame);
        assertFalse("Not remote!", result.isLocal());
        assertNotSame("Failed to write/read container", candidate, result);
        assertEquals("Failed to write/read container", candidate, result);
    }
}
