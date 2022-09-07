class BackupThread extends Thread {
    @Test
    public void encode() throws TraceEncoderException {
        ZTRChromatogram mockChromatogram = createMock(ZTRChromatogram.class);
        ChannelGroup channelGroup = new DefaultChannelGroup(new Channel(new byte[0], aTraces), new Channel(new byte[0], cTraces), new Channel(new byte[0], gTraces), new Channel(new byte[0], tTraces));
        expect(mockChromatogram.getNumberOfTracePositions()).andReturn(aTraces.length);
        expect(mockChromatogram.getChannelGroup()).andReturn(channelGroup);
        replay(mockChromatogram);
        byte[] actual = sut.encodeChunk(mockChromatogram);
        assertArrayEquals(encodedBytes, actual);
        verify(mockChromatogram);
    }
}
