class BackupThread extends Thread {
    public void testMarshalBadObject() {
        try {
            marshaller.marshal(Thread.currentThread(), writer);
            fail("should not have marshalled");
        } catch (Exception e) {
        }
    }
}
