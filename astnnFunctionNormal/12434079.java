class BackupThread extends Thread {
    public void readJournalList(String resourceFileName) {
        URL url = JournalAbbreviations.class.getResource(resourceFileName);
        try {
            readJournalList(new InputStreamReader(url.openStream()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
