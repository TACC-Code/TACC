class BackupThread extends Thread {
    static Element getJDOMresult(final StringWriter writer) {
        Element result = null;
        final StringReader reader = new StringReader(writer.toString());
        try {
            final SAXBuilder builder = new SAXBuilder();
            final Document doc = builder.build(reader);
            result = (Element) doc.getRootElement().detach();
        } catch (final Exception e) {
        }
        return result;
    }
}
