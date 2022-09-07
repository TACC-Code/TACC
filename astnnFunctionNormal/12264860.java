class BackupThread extends Thread {
    public String rewriteXmlForXfire(final XMLStreamReader reader, final String rootTagName, final String xmlns) throws XMLStreamException {
        final StringBuilder builder = new StringBuilder();
        builder.append("<");
        builder.append(rootTagName);
        builder.append(" xmlns=\"");
        builder.append(xmlns);
        builder.append("\">");
        for (int event = reader.getEventType(); event != XMLStreamConstants.END_DOCUMENT; event = reader.next()) {
            if (event == XMLStreamConstants.START_ELEMENT) {
                if (reader.getLocalName().equals("Envelope") && reader.getNamespaceURI().equals("http://schemas.xmlsoap.org/soap/envelope/")) {
                    continue;
                }
                if (reader.getLocalName().equals("Body") && reader.getNamespaceURI().equals("http://schemas.xmlsoap.org/soap/envelope/")) {
                    continue;
                }
                if (reader.getLocalName().equals(rootTagName)) {
                    continue;
                }
                builder.append("<");
                builder.append(reader.getLocalName());
                builder.append(">");
            }
            if (event == XMLStreamConstants.CHARACTERS) {
                builder.append(this.escape(reader.getText().trim()));
            }
            if (event == XMLStreamConstants.END_ELEMENT) {
                builder.append("</");
                builder.append(reader.getLocalName());
                builder.append(">");
                if (reader.getLocalName().equals(rootTagName)) {
                    break;
                }
            }
        }
        reader.close();
        return builder.toString();
    }
}
