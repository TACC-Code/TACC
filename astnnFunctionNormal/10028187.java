class BackupThread extends Thread {
    public static void write(List<Archive> archives, List<Artist> artists, String filename, boolean backup) {
        XMLWriter xmlWriter;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(ENCODING);
            FileWriter fileWriter = new FileWriter(filename);
            xmlWriter = new XMLWriter(fileWriter, format);
            Document doc = createRoot();
            Element root = doc.getRootElement();
            Element archivesEl = root.addElement(ELEMENT_ARCHIVES);
            Element tradelist = root.addElement(ELEMENT_TRADELIST);
            for (Archive archive : archives) {
                createArchiveElement(archivesEl, archive);
            }
            for (Artist artist : artists) {
                Element artistElement = createArtistElement(tradelist, artist);
                for (Show show : artist.getShows()) {
                    createShowElement(artistElement, show);
                }
            }
            xmlWriter.write(doc);
            xmlWriter.close();
            if (backup) {
                DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
                String timestamp = df.format(new Date());
                String backupFN = filename + "." + timestamp;
                FileUtils.copyFile(new File(filename), new File(backupFN));
            }
        } catch (IOException e) {
            ErrorMessage.handle(e);
        }
    }
}
