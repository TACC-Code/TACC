class BackupThread extends Thread {
    @Test(expected = ArithmeticException.class)
    public void fatalWithRetryHandler() throws Exception {
        final Exception cause = new UnsupportedOperationException();
        final Exception fatal = new ArithmeticException();
        when(this.callableMock.call()).thenThrow(cause).thenThrow(fatal);
        try {
            this.barrier.execute(this.callableMock);
        } finally {
            final InOrder inOrder = inOrder(this.callableMock, this.loggerMock, this.readLockMock, this.writeLockMock);
            inOrder.verify(this.readLockMock).lock();
            inOrder.verify(this.callableMock).call();
            inOrder.verify(this.loggerMock).recover(cause);
            inOrder.verify(this.readLockMock).unlock();
            inOrder.verify(this.writeLockMock).lock();
            inOrder.verify(this.callableMock).call();
            inOrder.verify(this.loggerMock).abort(fatal);
            inOrder.verify(this.writeLockMock).unlock();
        }
    }
}
