class BackupThread extends Thread {
    public void testReadWriteInheritance() {
        contactsProxy.setReadWriteState(ReadWriteState.READ_ONLY);
        assertTrue("Child should inherit read-only state from parent", wilmaProxy.isReadOnly());
        contactsProxy.setReadWriteState(ReadWriteState.READ_WRITE);
        assertFalse("Child should inherit read-write state from parent", wilmaProxy.isReadOnly());
    }
}
