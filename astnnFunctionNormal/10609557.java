class BackupThread extends Thread {
    private static void writeWeirdTranscriptProperties(String indent, Transcript trans, PrintWriter pw) {
        if (trans.readThroughStopResidue() != null) {
            if (trans.readThroughStopResidue().equals("U")) writeProperty(indent, "stop_codon_redefinition_as_selenocysteine", "true", 0, pw); else if (trans.readThroughStopResidue().equals("X")) writeProperty(indent, "stop_codon_readthrough", "true", 0, pw); else writeProperty(indent, "stop_codon_readthrough", trans.readThroughStopResidue(), 0, pw);
        }
        if (trans.plus1FrameShiftPosition() > 0) writeProperty(indent, "plus_1_translational_frameshift", trans.plus1FrameShiftPosition() + "", 0, pw);
        if (trans.minus1FrameShiftPosition() > 0) writeProperty(indent, "minus_1_translational_frameshift", trans.minus1FrameShiftPosition() + "", 0, pw);
        if (trans.unConventionalStart()) writeProperty(indent, "non_canonical_start_codon", trans.getStartCodon(), 0, pw);
        if (trans.isMissing5prime()) writeProperty(indent, "missing_start_codon", "true", 0, pw);
        if (trans.isMissing3prime()) writeProperty(indent, "missing_stop_codon", "true", 0, pw);
        if ((trans.getNonConsensusAcceptorNum() >= 0 || trans.getNonConsensusDonorNum() >= 0)) {
            if (trans.getProperty("non_canonical_splice_site").equals("")) writeProperty(indent, "non_canonical_splice_site", (trans.nonConsensusSplicingOkay() ? "approved" : "unapproved"), 0, pw);
        }
        if (trans.isProblematic()) writeProperty(indent, "problem", "true", 0, pw);
    }
}
