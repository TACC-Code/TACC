class BackupThread extends Thread {
    protected void Process() throws IOException, XMLStreamException {
        try {
            int eventType = reader.getEventType();
            while (reader.hasNext()) {
                switch(eventType) {
                    case XMLStreamConstants.START_DOCUMENT:
                        if (!isPartial) {
                            writer.writeStartDocument(reader.getEncoding(), reader.getVersion());
                            writer.setNamespaceContext(reader.getNamespaceContext());
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        if (!isPartial) writer.writeEndDocument();
                        break;
                    case XMLStreamConstants.CDATA:
                        writer.writeCData(reader.getText());
                        break;
                    case XMLStreamConstants.COMMENT:
                        writer.writeComment(reader.getText());
                        break;
                    case XMLStreamConstants.DTD:
                        writer.writeDTD(reader.getText());
                        break;
                    case XMLStreamConstants.ENTITY_REFERENCE:
                        writer.writeEntityRef(reader.getLocalName());
                        break;
                    case XMLStreamConstants.NAMESPACE:
                        _processNamespaces();
                        break;
                    case XMLStreamConstants.PROCESSING_INSTRUCTION:
                        String pIData = reader.getPIData();
                        if (pIData == null) writer.writeProcessingInstruction(reader.getPITarget()); else writer.writeProcessingInstruction(reader.getPITarget(), pIData);
                        break;
                    case XMLStreamConstants.SPACE:
                        writer.writeCharacters(reader.getText());
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        this.modifiedAttributes.clear();
                        if (_processStartElement()) {
                            _processNamespaces();
                            _processAttributes();
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (!handleEndElement(reader.getLocalName())) writer.writeEndElement();
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (!handleContent(reader.getText())) {
                            writer.writeCharacters(reader.getText());
                        }
                        break;
                    case XMLStreamConstants.ATTRIBUTE:
                        _processAttributes();
                        break;
                }
                if (stop) return;
                eventType = reader.next();
            }
            if (!isPartial) writer.close();
            reader.close();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        }
    }
}
