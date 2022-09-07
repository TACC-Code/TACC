class BackupThread extends Thread {
    @Test
    public void abiVisitorMatchesZTR() throws FileNotFoundException, TraceDecoderException, IOException {
        BasicChromatogramFile actualAbi = new BasicChromatogramFile();
        Ab1FileParser.parseAb1File(RESOURCES.getFile("files/SDBHD01T00PB1A1672F.ab1"), actualAbi);
        assertEquals(expectedZTR.getBasecalls(), actualAbi.getBasecalls());
        assertEquals(expectedZTR.getPeaks(), actualAbi.getPeaks());
        assertEquals(expectedZTR.getQualities(), actualAbi.getQualities());
        assertEquals(expectedZTR.getChannelGroup(), actualAbi.getChannelGroup());
        assertEquals(expectedZTR.getNumberOfTracePositions(), actualAbi.getNumberOfTracePositions());
        assertEquals(expectedZTR.getProperties(), actualAbi.getProperties());
    }
}
