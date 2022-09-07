class BackupThread extends Thread {
    public static void write(File file, Reader reader) throws IOException {
        write(file, reader, false);
    }
}
