class BackupThread extends Thread {
    public EventServer(HLAModel m, EventWriter modelWriter, int port, EventWriterFactory writerFactory, EventReaderFactory readerFactory) {
        model = m;
        this.modelWriter = modelWriter;
        this.port = port;
        this.writerFactory = writerFactory;
        this.readerFactory = readerFactory;
    }
}
