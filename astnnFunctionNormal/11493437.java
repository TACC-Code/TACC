class BackupThread extends Thread {
    public String getVersion(final URL url) {
        versionFound = null;
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser newSAXParser = null;
        UnicodeInputStream unicodeStream = null;
        try {
            InputStream resourceAsStream = url.openStream();
            unicodeStream = new UnicodeInputStream(resourceAsStream, XmlVersionFinder.ENCODING);
            unicodeStream.init();
            newSAXParser = factory.newSAXParser();
            newSAXParser.parse(unicodeStream, this);
        } catch (final Exception e) {
        } finally {
            CtuluLibFile.close(unicodeStream);
        }
        return versionFound;
    }
}
