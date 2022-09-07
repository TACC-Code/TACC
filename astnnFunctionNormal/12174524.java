class BackupThread extends Thread {
    public CsvImpl(Reader reader, Writer writer) {
        this.reader = new CSVReader(reader);
        this.writer = new CSVWriter(writer);
    }
}
