class BackupThread extends Thread {
    @Test
    public void encode() throws TraceEncoderException {
        ZTRChromatogram chromatogram = createMock(ZTRChromatogram.class);
        ChannelGroup channelGroup = new DefaultChannelGroup(new Channel(aconf, new short[0]), new Channel(cconf, new short[0]), new Channel(gconf, new short[0]), new Channel(tconf, new short[0]));
        expect(chromatogram.getBasecalls()).andReturn(new DefaultNucleotideEncodedGlyphs(bases));
        expect(chromatogram.getChannelGroup()).andReturn(channelGroup);
        replay(chromatogram);
        byte[] actual = sut.encodeChunk(chromatogram);
        assertArrayEquals(encodedBytes, actual);
        verify(chromatogram);
    }
}
