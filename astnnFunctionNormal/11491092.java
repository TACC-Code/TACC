class BackupThread extends Thread {
    @Test
    public void testWriteThenReadNested() {
        IWireIdentityRegistry registry = new SWFWireIdentityRegistry();
        byte[] frame = FrameWriter.writeNested(components, registry, new BasicOperation(new DefaultPermissionProfile()), false);
        byte[] chaff = new byte[] { 21, 34, 45, 12 };
        frame = ArrayUtils.addAll(chaff, frame);
        int start = chaff.length;
        JUnitReader reader = new JUnitReader();
        FrameReader.readNestedSWF(new BasicOperation(new DefaultPermissionProfile()), frame, start, frame.length, reader);
        assertNotSame("Did not write/read correctly", this.components, reader.components);
        assertEquals("Did not write/read correctly", this.components, reader.components);
    }
}
