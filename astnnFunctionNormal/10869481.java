class BackupThread extends Thread {
    public void testSetReadOnlyAttribute() throws Exception {
        if (!runTests) return;
        IJmxTestBean proxy = getProxy();
        try {
            proxy.setAge(900);
            fail("Should not be able to write to a read only attribute");
        } catch (InvalidInvocationException ex) {
        }
    }
}
