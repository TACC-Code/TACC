class BackupThread extends Thread {
    public Iterator<DigestedPeptide> digestProteins(File fastaFile, PrintWriter tsvPw) throws ParseException, IOException, PeptideFragmentationException {
        FastaReader reader = FastaReader.newInstance();
        reader.setProgressBar(TerminalProgressBar.indeterminate());
        reader.parse(fastaFile);
        Iterator<FastaEntry> it = reader.iterator();
        List<DigestedPeptide> digests = new ArrayList<DigestedPeptide>();
        while (it.hasNext()) {
            FastaEntry nextEntry = it.next();
            Peptide protein = new Peptide.Builder(nextEntry.getSequence()).nterm(NTerminus.PROT_N).cterm(CTerminus.PROT_C).ambiguityEnabled().build();
            for (Digester digester : params.getDigesters()) {
                if (filter != null) {
                    digester.setCondition(filter);
                }
                digester.digest(protein);
                for (DigestedPeptide digest : digester.getDigests()) {
                    tsvPw.append(digest2String(digest));
                    tsvPw.append("\n");
                    tsvPw.flush();
                }
            }
        }
        return digests.iterator();
    }
}
