class BackupThread extends Thread {
    public static final Csv getInstance(Reader reader, Writer writer) {
        return new CsvImpl(reader, writer);
    }
}
