class BackupThread extends Thread {
    public Value handle(Node elem, Map conf) throws XmlConfigException {
        String source = getTextContent(elem, conf);
        URL url = null;
        try {
            url = new URL(source);
        } catch (MalformedURLException e) {
            url = XmlConfig.class.getResource(source);
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setCoalescing(true);
        Document doc;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(url.openStream());
        } catch (ParserConfigurationException ex) {
            throw new XmlConfigException("Could not create parser for file " + source, ex);
        } catch (SAXException ex) {
            throw new XmlConfigException("Could not parse file " + source, ex);
        } catch (IOException ex) {
            throw new XmlConfigException("Error while reading file " + source, ex);
        }
        try {
            handleNode(doc.getDocumentElement(), conf);
        } catch (XmlConfigException ex) {
            throw new XmlConfigException("Error while processing file " + source, ex);
        }
        return new Value(conf, Map.class);
    }
}
