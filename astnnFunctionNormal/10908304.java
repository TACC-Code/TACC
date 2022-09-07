class BackupThread extends Thread {
    public void connectWriterToReader(Class<? extends OTFDataWriter> writer, Class<? extends OTFDataReader> reader) {
        Collection<Class<?>> readerClasses = this.getToEntries(writer);
        if (!readerClasses.isEmpty()) {
            throw new RuntimeException("We already have a reader for this writer.");
        }
        Entry entry = new Entry(writer, reader);
        connections.add(entry);
    }
}
