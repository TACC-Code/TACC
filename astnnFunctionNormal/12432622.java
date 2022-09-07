class BackupThread extends Thread {
    private CodeValueFactoryImpl() {
        java.net.URL url = null;
        try {
            _map = new CodeSystemMap();
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
            reader.setContentHandler(_map);
            url = Thread.currentThread().getContextClassLoader().getResource("code-value-factory.xml");
            if (url == null) throw new CodeValueFactory.ConfigurationException("code-value-factory.xml file not found in classpath");
            reader.parse(new InputSource(url.openStream()));
        } catch (Throwable ex) {
            throw new CodeValueFactory.ConfigurationException(url.toString() + ": " + ex.getMessage(), ex);
        }
    }
}
