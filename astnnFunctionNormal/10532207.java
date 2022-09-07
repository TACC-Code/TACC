class BackupThread extends Thread {
    public TableProcessor(XMLStreamReader reader, XMLStreamWriter writer, OutputStream underlayingOutputStream) throws XMLStreamException, FactoryConfigurationError {
        super(reader, writer, underlayingOutputStream);
        fileWriter = writer;
        fileUnderlayingStream = underlayingOutputStream;
        onMemoryStream = new ByteArrayOutputStream();
        onMemoryWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(onMemoryStream, "UTF-8");
    }
}
