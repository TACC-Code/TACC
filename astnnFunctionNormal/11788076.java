class BackupThread extends Thread {
    private void initializeFIData() throws IOException, SAXException {
        URL url = new URL(getUrl());
        XMLReader xmlReader = new Parser();
        xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
        xmlReader.setFeature("http://xml.org/sax/features/validation", false);
        xmlReader.setContentHandler(new DirectoryContentHandler());
        xmlReader.parse(new InputSource(url.openStream()));
    }
}
