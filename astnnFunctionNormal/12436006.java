class BackupThread extends Thread {
    public void transform(Reader reader, Writer writer, Map<String, Object> parameterMap) {
        copyAll(reader, writer);
    }
}
