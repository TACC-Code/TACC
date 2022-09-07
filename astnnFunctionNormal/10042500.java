class BackupThread extends Thread {
    @Test
    public void recover() throws Exception {
        final Exception recover = new RuntimeException();
        when(this.callableMock.call()).thenThrow(recover);
        when(this.retryHandlerMock.retry(this.callableMock, recover)).thenReturn("42");
        assertEquals("42", this.barrier.execute(this.callableMock, this.retryHandlerMock));
        final InOrder inOrder = inOrder(this.callableMock, this.retryHandlerMock, this.loggerMock, this.readLockMock, this.writeLockMock);
        inOrder.verify(this.readLockMock).lock();
        inOrder.verify(this.callableMock).call();
        inOrder.verify(this.loggerMock).recover(recover);
        inOrder.verify(this.readLockMock).unlock();
        inOrder.verify(this.writeLockMock).lock();
        inOrder.verify(this.retryHandlerMock).retry(this.callableMock, recover);
        inOrder.verify(this.writeLockMock).unlock();
        verify(this.loggerMock, never()).abort((Exception) anyObject());
    }
}
