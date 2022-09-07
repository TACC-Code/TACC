class BackupThread extends Thread {
    public void testDefaultStates() {
        assertEquals("Default state should be inherit", ReadWriteState.INHERIT, contactsProxy.getReadWriteState());
        assertFalse("Default inherited value should be read/write", contactsProxy.isReadOnly());
    }
}
