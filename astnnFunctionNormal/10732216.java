class BackupThread extends Thread {
    public void testFailWriteToReadOnlyBuffer() throws Exception {
        writeDefaultContent(8192);
        MappedFileBuffer buf = new MappedFileBuffer(_testFile, 1024, false);
        buf.getInt(8000L);
        try {
            buf.putInt(8000L, 0x12345678);
            fail("able to write to read-only buffer");
        } catch (ReadOnlyBufferException ee) {
        }
    }
}
