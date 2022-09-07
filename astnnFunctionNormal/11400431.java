class BackupThread extends Thread {
    public void cutPeptideWithTrypsin() throws JPLParseException, JPLEmptySequenceException {
        JPLTrypsinKRnotPCutter trypsin = new JPLTrypsinKRnotPCutter();
        JPLIAASequence sequence = new JPLAASequence.Builder("SALYTNIKALAS").build();
        JPLCleaver protease = new JPLCleaver(trypsin);
        protease.digest(sequence);
    }
}
