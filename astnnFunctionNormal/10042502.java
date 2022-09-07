class BackupThread extends Thread {
    @Test(expected = ArithmeticException.class)
    public void fatal() throws Exception {
        final Exception cause = new UnsupportedOperationException();
        final Exception fatal = new ArithmeticException();
        when(this.callableMock.call()).thenThrow(cause);
        when(this.retryHandlerMock.retry(this.callableMock, cause)).thenThrow(fatal);
        try {
            this.barrier.execute(this.callableMock, this.retryHandlerMock);
        } finally {
            final InOrder inOrder = inOrder(this.callableMock, this.retryHandlerMock, this.loggerMock, this.readLockMock, this.writeLockMock);
            inOrder.verify(this.readLockMock).lock();
            inOrder.verify(this.callableMock).call();
            inOrder.verify(this.loggerMock).recover(cause);
            inOrder.verify(this.readLockMock).unlock();
            inOrder.verify(this.writeLockMock).lock();
            inOrder.verify(this.retryHandlerMock).retry(this.callableMock, cause);
            inOrder.verify(this.loggerMock).abort(fatal);
            inOrder.verify(this.writeLockMock).unlock();
        }
    }
}
