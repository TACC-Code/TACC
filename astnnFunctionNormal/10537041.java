class BackupThread extends Thread {
    @Override
    public void write(Writer writer, int depth) throws IOException {
        FileInputStream in = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(in);
        StreamUtils.copyStream(reader, writer);
        reader.close();
        in.close();
    }
}
