class BackupThread extends Thread {
    private XMLEventReader newReader(URL url) throws IOException {
        debug(url);
        XMLInputFactory f = XMLInputFactory.newInstance();
        f.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
        f.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
        f.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
        f.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        XMLEventReader reader = null;
        try {
            reader = f.createXMLEventReader(new IgnoreLine2(new InputStreamReader(url.openStream())));
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
        return reader;
    }
}
