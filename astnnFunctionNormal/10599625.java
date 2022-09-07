class BackupThread extends Thread {
    public PartOfSpeechImpl(URL url, String defaultPartOfSpeech) throws IOException {
        BufferedReader reader;
        String line;
        partOfSpeechMap = new HashMap();
        this.defaultPartOfSpeech = defaultPartOfSpeech;
        reader = new BufferedReader(new InputStreamReader(url.openStream()));
        line = reader.readLine();
        lineCount++;
        while (line != null) {
            if (!line.startsWith("***")) {
                parseAndAdd(line);
            }
            line = reader.readLine();
        }
        reader.close();
    }
}
