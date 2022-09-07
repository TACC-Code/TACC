class BackupThread extends Thread {
    public AbstractTestSamplesSection() {
        chromatogram = createMock(SCFChromatogram.class);
        mockChannelGroup = createMock(ChannelGroup.class);
        expect(mockChannelGroup.getCChannel()).andStubReturn(new Channel(EMPTY_CONFIDENCE, cSamples));
        expect(mockChannelGroup.getGChannel()).andStubReturn(new Channel(EMPTY_CONFIDENCE, gSamples));
        expect(mockChannelGroup.getTChannel()).andStubReturn(new Channel(EMPTY_CONFIDENCE, tSamples));
        expect(chromatogram.getChannelGroup()).andStubReturn(mockChannelGroup);
        sut = createSectionHandler();
    }
}
