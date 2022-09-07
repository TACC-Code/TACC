class BackupThread extends Thread {
    public static void serializeSource(Source src, XMLStreamWriter writer) throws XMLStreamException {
        XMLStreamReader reader = SourceReaderFactory.createSourceReader(src, true);
        int state;
        do {
            state = reader.next();
            switch(state) {
                case XMLStreamConstants.START_ELEMENT:
                    String uri = reader.getNamespaceURI();
                    String prefix = reader.getPrefix();
                    String localName = reader.getLocalName();
                    if (prefix == null) {
                        if (uri == null) {
                            writer.writeStartElement(localName);
                        } else {
                            writer.writeStartElement(uri, localName);
                        }
                    } else {
                        assert uri != null;
                        if (prefix.length() > 0) {
                            String writerURI = null;
                            if (writer.getNamespaceContext() != null) writerURI = writer.getNamespaceContext().getNamespaceURI(prefix);
                            String writerPrefix = writer.getPrefix(uri);
                            if (declarePrefix(prefix, uri, writerPrefix, writerURI)) {
                                writer.writeStartElement(prefix, localName, uri);
                                writer.setPrefix(prefix, uri != null ? uri : "");
                                writer.writeNamespace(prefix, uri);
                            } else {
                                writer.writeStartElement(prefix, localName, uri);
                            }
                        } else {
                            writer.writeStartElement(prefix, localName, uri);
                        }
                    }
                    int n = reader.getNamespaceCount();
                    for (int i = 0; i < n; i++) {
                        String nsPrefix = reader.getNamespacePrefix(i);
                        if (nsPrefix == null) nsPrefix = "";
                        String writerURI = null;
                        if (writer.getNamespaceContext() != null) writerURI = writer.getNamespaceContext().getNamespaceURI(nsPrefix);
                        String readerURI = reader.getNamespaceURI(i);
                        if (writerURI == null || ((nsPrefix.length() == 0) || (prefix.length() == 0)) || (!nsPrefix.equals(prefix) && !writerURI.equals(readerURI))) {
                            writer.setPrefix(nsPrefix, readerURI != null ? readerURI : "");
                            writer.writeNamespace(nsPrefix, readerURI != null ? readerURI : "");
                        }
                    }
                    n = reader.getAttributeCount();
                    for (int i = 0; i < n; i++) {
                        String attrPrefix = reader.getAttributePrefix(i);
                        String attrURI = reader.getAttributeNamespace(i);
                        writer.writeAttribute(attrPrefix != null ? attrPrefix : "", attrURI != null ? attrURI : "", reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                        setUndeclaredPrefix(attrPrefix, attrURI, writer);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    writer.writeEndElement();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    writer.writeCharacters(reader.getText());
            }
        } while (state != XMLStreamConstants.END_DOCUMENT);
        reader.close();
    }
}
