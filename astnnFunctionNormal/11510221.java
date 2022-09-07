class BackupThread extends Thread {
    public static QueryKeyHandler parse(URL url) throws IOException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException err) {
            throw new SAXException(err);
        }
        QueryKeyHandler qkh = new QueryKeyHandler();
        InputStream in = url.openStream();
        parser.parse(in, qkh);
        in.close();
        return qkh;
    }
}
