class BackupThread extends Thread {
        @Override
        protected Expression instantiate(Object oldInstance, Encoder out) {
            BasicChromatogram chromatogram = (BasicChromatogram) oldInstance;
            return new Expression(chromatogram, ChromatogramXMLSerializer.class, "buildBasicChromatogram", new Object[] { Nucleotides.asString(chromatogram.getNucleotideSequence().asList()), new EncodedByteData(PhredQuality.toArray(chromatogram.getQualities().asList())).encodeData(), chromatogram.getPeaks(), chromatogram.getChannelGroup(), chromatogram.getComments() });
        }
}
