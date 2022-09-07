class BackupThread extends Thread {
    public BaseXmlProcessor(XMLStreamReader reader, XMLStreamWriter writer, OutputStream underlayingOutputStream) {
        this.reader = reader;
        this.writer = writer;
        this.underlayingOutputStream = underlayingOutputStream;
    }
}
