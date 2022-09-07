class BackupThread extends Thread {
    public Object getDocument(String uri) throws FunctionCallException {
        try {
            XMLStreamReader parser;
            XMLInputFactory xmlInputFactory = StAXUtils.getXMLInputFactory();
            Boolean oldValue = (Boolean) xmlInputFactory.getProperty(XMLInputFactory.IS_COALESCING);
            try {
                xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);
                if (uri.indexOf(':') == -1) {
                    parser = xmlInputFactory.createXMLStreamReader(new FileInputStream(uri));
                } else {
                    URL url = new URL(uri);
                    parser = xmlInputFactory.createXMLStreamReader(url.openStream());
                }
            } finally {
                if (oldValue != null) {
                    xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, oldValue);
                }
                StAXUtils.releaseXMLInputFactory(xmlInputFactory);
            }
            StAXOMBuilder builder = new StAXOMBuilder(parser);
            return builder.getDocumentElement().getParent();
        } catch (Exception e) {
            throw new FunctionCallException(e);
        }
    }
}
