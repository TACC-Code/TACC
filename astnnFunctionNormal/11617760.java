class BackupThread extends Thread {
    public static List<BibtexEntry> fetchMedline(String id) {
        String baseUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&rettype=citation&id=" + id;
        MedlineImporter mlImporter = new MedlineImporter();
        List<BibtexEntry> entries;
        InputStream is;
        try {
            URL url = new URL(baseUrl);
            URLConnection data = url.openConnection();
            is = data.getInputStream();
            System.out.println("fetching id=" + id);
            entries = mlImporter.importEntries(is);
            return entries;
        } catch (IOException e) {
            return new ArrayList<BibtexEntry>();
        }
    }
}
