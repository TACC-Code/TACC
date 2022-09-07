class BackupThread extends Thread {
    private BaseFinancialInstitutionData loadInstitutionData(String href) throws IOException, SAXException {
        if (LOG.isInfoEnabled()) {
            LOG.info("Loading institution data from: " + href);
        }
        URL url = new URL(href);
        XMLReader xmlReader = new Parser();
        xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
        xmlReader.setFeature("http://xml.org/sax/features/validation", false);
        InstitutionContentHandler institutionHandler = new InstitutionContentHandler();
        xmlReader.setContentHandler(institutionHandler);
        xmlReader.parse(new InputSource(url.openStream()));
        return institutionHandler.data;
    }
}
