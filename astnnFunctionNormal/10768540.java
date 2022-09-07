class BackupThread extends Thread {
    public Sequence getSequence(final String chromosome, final int oligoStartPos, final int oligoLength, final Sequence sequence) throws IOException, TeolennException {
        if (chromosome == null || oligoStartPos < 0) return null;
        if (!chromosome.equals(this.chr)) {
            if (this.inChannel != null) this.inChannel.close();
            this.chr = chromosome;
            File f = new File(oligosDir, chromosome + oligosExtension);
            FileInputStream fis = new FileInputStream(f);
            this.inChannel = fis.getChannel();
            this.positionConstant = calcPositionConstant();
            this.chrLength = ChromosomeNameResource.getRessource().getChromosomeLength(chromosome);
        }
        if (chrLength == -1) throw new TeolennException("Chromosome length not found.");
        final long pos = getFilePos(oligoStartPos, oligoLength, this.start1);
        final int length = this.positionConstant + getDigits(oligoStartPos) + oligoLength;
        if (this.bb == null || this.bb.capacity() != length) this.bb = ByteBuffer.allocate(length); else bb.clear();
        final int read = this.inChannel.read(bb, pos);
        if (read != length) throw new TeolennException("Error while reading sequence " + chromosome + "," + oligoStartPos + ". pos=" + pos + " oligolengtg=" + oligoLength + " len=" + length + " read=" + read + " bb.capacity=" + this.bb.capacity());
        return string2Sequence(new String(bb.array(), CHARSET), sequence);
    }
}
