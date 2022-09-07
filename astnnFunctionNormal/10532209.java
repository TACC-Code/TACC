class BackupThread extends Thread {
    @Override
    protected boolean handleStartElement(String localName, Attributes attributes) throws IOException, XMLStreamException {
        if (localName.equals("table")) {
            if (depth == 0) {
                depth++;
            } else {
                TableProcessor tableProc = new TableProcessor(reader, writer, underlayingOutputStream);
                tableProc.setCollectionModel(currentCollection);
                tableProc.Start();
            }
        } else if (localName.equals("table-row")) {
            onMemoryWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(onMemoryStream, "UTF-8");
            this.writer = onMemoryWriter;
            this.underlayingOutputStream = onMemoryStream;
            this.writer.setNamespaceContext(reader.getNamespaceContext());
        } else {
            rowIsATemplate = localName.equals("user-field-get");
        }
        return false;
    }
}
