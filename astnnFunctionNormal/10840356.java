class BackupThread extends Thread {
    @Override
    public Channel getChannel(NucleotideGlyph nucleotide) {
        if (nucleotide == NucleotideGlyph.Adenine) {
            return getAChannel();
        }
        if (nucleotide == NucleotideGlyph.Cytosine) {
            return getCChannel();
        }
        if (nucleotide == NucleotideGlyph.Guanine) {
            return getGChannel();
        }
        return getTChannel();
    }
}
