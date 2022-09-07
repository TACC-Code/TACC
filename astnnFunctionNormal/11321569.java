class BackupThread extends Thread {
    public static void copyStream(Reader reader, Writer writer) throws IOException {
        int count;
        char[] buffer = new char[1024];
        while ((count = reader.read(buffer)) > 0) writer.write(buffer, 0, count);
    }
}
