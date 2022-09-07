class BackupThread extends Thread {
    public List<EntryPoint> getEntryPoints(String ref) throws MalformedURLException, SAXException, IOException, ParserConfigurationException, URISyntaxException {
        if (entryPoints == null) {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            entryPoints = new EntryPointParser();
            parser.parse(URIFactory.url(ref + "/entry_points").openStream(), entryPoints);
        }
        return entryPoints.getAll();
    }
}
