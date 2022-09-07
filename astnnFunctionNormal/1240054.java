class BackupThread extends Thread {
    public void testParse() throws FileNotFoundException, XMLStreamException {
        TrimExpressionEvaluator ee = new TrimExpressionEvaluator();
        ee.addVariable("spaces", "Variable with embedded spaces");
        ee.addVariable("ampersand", "Variable with embedded & ampersand");
        ee.addVariable("backslash", "Variable with embedded \\ backslash");
        ee.addVariable("lessthan", "Variable with embedded < less than");
        ee.addVariable("greaterthan", "Variable with embedded > greater than");
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(getClass().getResourceAsStream("testel.trim.xml"), "UTF-8");
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(System.out, "UTF-8");
        while (reader.hasNext()) {
            switch(reader.getEventType()) {
                case XMLStreamReader.START_DOCUMENT:
                    writer.writeStartDocument(reader.getEncoding(), reader.getVersion());
                    writer.writeCharacters("\n");
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    writer.writeEndDocument();
                    break;
                case XMLStreamReader.CHARACTERS:
                    writer.writeCharacters((String) ee.evaluate(reader.getText()));
                    break;
                case XMLStreamReader.START_ELEMENT:
                    writer.writeStartElement(reader.getLocalName());
                    for (int n = 0; n < reader.getNamespaceCount(); n++) {
                        writer.writeNamespace(reader.getNamespacePrefix(n), reader.getNamespaceURI(n));
                    }
                    for (int a = 0; a < reader.getAttributeCount(); a++) {
                        writer.writeAttribute(reader.getAttributeLocalName(a), (String) ee.evaluate(reader.getAttributeValue(a)));
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    writer.writeEndElement();
                    break;
            }
            reader.next();
        }
        reader.close();
        writer.close();
    }
}
