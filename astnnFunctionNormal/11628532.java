class BackupThread extends Thread {
    @Test
    public void roundtripNegative() {
        EbmlEncoder encoder = new EbmlEncoder();
        EbmlDecoder decoder = new EbmlDecoder();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        long write = 0x8877665544332211L;
        for (int i = 8; i > 0; i--) {
            buffer.clear();
            encoder.encodeSignedInteger(buffer, write, i);
            buffer.flip();
            long read = decoder.decodeSignedInteger(buffer, i);
            assertThat(read, isEqualTo(write));
            write >>= 8;
        }
    }
}
