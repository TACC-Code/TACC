class BackupThread extends Thread {
    public static final String read(Reader reader) throws IOException {
        StringWriter writer = new StringWriter(8192);
        copy(reader, writer);
        return writer.getBuffer().toString();
    }
}
