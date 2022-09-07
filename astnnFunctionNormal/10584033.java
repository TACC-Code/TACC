class BackupThread extends Thread {
    public static void pipe(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[10240];
        int len;
        while ((len = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, len);
        }
    }
}
