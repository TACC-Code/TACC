class BackupThread extends Thread {
    private void writeTraceData(OutputStream out, Contig<T> contig) throws IOException {
        final NucleotideEncodedGlyphs consensus = contig.getConsensus();
        SortedSet<T> sortedReads = new TreeSet<T>(new TraceOrderComparator<T>());
        sortedReads.addAll(contig.getPlacedReads());
        for (T read : sortedReads) {
            writeString(out, "<trace>\n");
            writeString(out, createTag("trace_name", read.getId()));
            writeString(out, createTag("nbasecalls", read.getEncodedGlyphs().getUngappedLength()));
            Range validRange = read.getValidRange().convertRange(CoordinateSystem.RESIDUE_BASED);
            int numberOfGaps = read.getEncodedGlyphs().getNumberOfGaps();
            writeString(out, "<valid>\n");
            writeString(out, createTag("start", validRange.getLocalStart()));
            writeString(out, createTag("stop", validRange.getLocalEnd()));
            writeString(out, "</valid>\n");
            writeString(out, String.format("<tiling direction=\"%s\">%n", read.getSequenceDirection()));
            writeString(out, createTag("start", read.getStart() + 1));
            writeString(out, createTag("stop", read.getEnd() + 1));
            writeString(out, "</tiling>\n");
            writeString(out, String.format("<traceconsensus>%n"));
            int ungappedConsnesusStart = consensus.convertGappedValidRangeIndexToUngappedValidRangeIndex((int) read.getStart());
            int ungappedConsnesusEnd = consensus.convertGappedValidRangeIndexToUngappedValidRangeIndex((int) read.getEnd());
            writeString(out, createTag("start", ungappedConsnesusStart + 1));
            writeString(out, createTag("stop", ungappedConsnesusEnd + 1));
            writeString(out, "</traceconsensus>\n");
            if (numberOfGaps > 0) {
                writeString(out, createTag("ntracegaps", numberOfGaps));
                writeString(out, String.format("<tracegaps source=\"%s\">%s</tracegaps>%n", ContigDataSubmissionType.INLINE, createDeltaGapString(read.getEncodedGlyphs().getGapIndexes())));
            }
            writeString(out, "</trace>\n");
        }
    }
}
