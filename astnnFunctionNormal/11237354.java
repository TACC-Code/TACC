class BackupThread extends Thread {
    public void encode(final Writer writer, final SAMFileHeader header) {
        mFileHeader = header;
        this.writer = new BufferedWriter(writer);
        writeHDLine();
        for (final SAMSequenceRecord sequenceRecord : header.getSequenceDictionary().getSequences()) {
            writeSQLine(sequenceRecord);
        }
        for (final SAMReadGroupRecord readGroup : header.getReadGroups()) {
            writeRGLine(readGroup);
        }
        for (final SAMProgramRecord programRecord : header.getProgramRecords()) {
            writePGLine(programRecord);
        }
        for (final String comment : header.getComments()) {
            println(comment);
        }
        try {
            this.writer.flush();
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }
}
