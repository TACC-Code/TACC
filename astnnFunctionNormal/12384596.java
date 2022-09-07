class BackupThread extends Thread {
    public static void setConservation(final PolyPeptide protein, final String pdbId) throws IOException {
        char chainId = protein.get(0).getAtom(0).getChainId();
        if (chainId == ' ') {
            chainId = '_';
        }
        String urlPath = "http://consurfdb.tau.ac.il/consurf_db/" + pdbId.toUpperCase() + "/" + chainId + "/consurf.grades";
        Consurf consurf = null;
        URL url = new URL(urlPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        consurf = new Consurf(br);
        consurf.assignConservation(protein);
    }
}
