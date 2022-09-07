class BackupThread extends Thread {
    public void testWriteBits() throws IOException {
        final BasicImageOutputStreamImpl out = new BasicImageOutputStreamImpl(8);
        final ImageInputStream in = new BasicImageInputStreamImpl(out.buff);
        out.writeBits(0xff, 7);
        out.writeBit(0);
        out.writeBits(0xabcdef, 24);
        assertEquals("writeBits failed", 0xfe, in.read());
        assertEquals("writeBits failed", 0xab, in.read());
        assertEquals("writeBits failed", 0xcd, in.read());
        assertEquals("writeBits failed", 0xef, in.read());
    }
}
